(ns digimon-reference.core
  (:gen-class)
  (:require
   [clj-http.client :as client]
   [clojure.data.json :as json]
   [clojure.string :as string]
   [hickory.core :as hickory]
   [hickory.select :as select]))

(defn- http-post
  ([url]
   (http-post url {}))
  ([url options]
   (->> (client/post url options)
        :body)))

(defn- http-get
  ([url]
   (http-get url {}))
  ([url options]
   (->> (client/get url (assoc options
                               :cookie-policy :standard
                               :retry-handler (constantly false)))
        :body)))

(defn- entry
  [{:keys [directory-name]}]
  (let [response (http-get "https://digimon.net/reference/detail.php"
                           {:headers {"Accept-Language" "ja"}
                            :query-params {:directory_name directory-name}})
        page (->> response
                  hickory/parse
                  hickory/as-hickory
                  (select/select (select/descendant
                                  (select/and (select/tag "section")
                                              (select/class "l-contents"))
                                  (select/class "p-ref")))
                  first)
        romaji (->> page
                    (select/select (select/descendant
                                    (select/class "c-titleSet__sub")))
                    first
                    :content
                    first)
        main-title (->> page
                        (select/select (select/descendant
                                        (select/class "c-titleSet__main")))
                        first
                        :content
                        first)
        image (->> page
                   (select/select (select/descendant
                                   (select/class "p-ref__picitem")
                                   (select/tag "img")))
                   first
                   :attrs
                   :src)]
    (reduce-kv (fn [m k v]
                 (cond-> m
                   v (assoc k v)))
               {}
               {:reference/id directory-name
                :reference/romaji romaji
                :reference/name main-title
                :reference/href (str "https://digimon.net/reference/detail.php?"
                                     "directory_name=" directory-name)
                :reference/image (str "https://digimon.net"
                                      (string/replace image #"^\.\." ""))})))

(defn- directory
  []
  (loop [next 0
         rows []]
    (let [j (-> (http-get "https://digimon.net/reference/request.php"
                          {:headers {"X-Requested-With"
                                     "XMLHttpRequest"}
                           :query-params {:digimon_name nil
                                          :name nil
                                          :digimon_level nil
                                          :attribute nil
                                          :type nil
                                          :next next}})
                (json/read-str :key-fn #(keyword (string/replace % "_" "-"))))]
      (if (pos? (:next j))
        (recur (:next j)
               (concat rows (:rows j)))
        rows))))

(defn- wovnio-key
  []
  (->> (http-get "https://digimon.net/reference/"
                 {:headers {"Accept-Language" "ja"}})
       hickory/parse
       hickory/as-hickory
       (select/select (select/descendant (select/tag "script")))
       (filter (fn [script]
                 (get-in script [:attrs :data-wovnio])))
       ((comp #(string/replace % "key=" "")
              #(get-in % [:attrs :data-wovnio])
              first))))

(defn- translate
  [reference]
  (let [s (:reference/name reference)
        romaji (:reference/romaji reference)
        reference (dissoc reference :reference/romaji)]
    (->> (-> (http-post "https://ee.wovn.io/values/translate"
                        {:headers {"Content-Type" "application/json"}
                         :body (json/write-str {:token (wovnio-key)
                                                :srcs [s]
                                                :defaultLang "ja"
                                                :host "digimon.net"})})
             (json/read-str)
             (get-in ["text_vals" s]))
         (sort-by key)
         (reduce (fn [accl [k v]]
                   (let [lang (keyword (string/replace k #"zh.*" "zh"))
                         digimon-name (string/trim (get-in v [0 "data"]))
                         id (str (name lang) "/" digimon-name)]
                     (if (contains? (set (map :reference/id accl)) id)
                       accl
                       (conj accl
                             (merge reference
                                    {:reference/id id
                                     :reference/language lang
                                     :reference/name digimon-name})))))
                 [(merge reference
                         {:reference/id (str "ja/" s)
                          :reference/language :ja})
                  (merge reference
                         {:reference/id (str "romaji/" romaji)
                          :reference/language :en
                          :reference/name romaji})]))))

(defn book
  []
  (->> (directory)
       (pmap entry)
       (mapcat translate)))

(defn -main
  [& args]
  (book))
