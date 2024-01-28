(ns digimon-reference.core
  (:gen-class)
  (:require
   [clj-http.client :as client]
   [clojure.data.json :as json]
   [clojure.string :as string]
   [hickory.core :as h]
   [hickory.select :as select]))

(def origin "https://digimon.net")

(def reference-suffix-by-language
  {"ja" ""
   "en" "_en"
   "zh-Hans" "_zh-CHS"
   "ko" "_ko"})

(defn- http-post
  ([url]
   (http-post url {}))
  ([url options]
   (->> (client/post url options)
        :body)))

(defonce ^:private http-get*
  (memoize (fn [url options]
             (client/get url (assoc options
                                    :retry-handler
                                    (fn [ex try-count http-context]
                                      (if (> try-count 3)
                                        false
                                        (do (Thread/sleep 30000)
                                            true))))))))

(defn- http-get
  ([url]
   (http-get url {}))
  ([url options]
   (->> (http-get* url options)
        :body)))

(defn- name-fixes
  [s]
  (-> (string/trim s)
      (string/replace "ShineGreymon:Ruin Mode" "ShineGreymon: Ruin Mode")
      (string/replace "Kuzuhamon Maid Mode" "Kuzuhamon: Maid Mode")
      (string/replace "（" " (")
      (string/replace "）" ")")
      (string/replace #"\s+" " ")))

(defn- directory
  [language]
  (loop [next 0
         rows []]
    (let [j (-> (http-get (str origin "/reference"
                               (get reference-suffix-by-language language "")
                               "/request.php")
                          {:headers {"X-Requested-With" "XMLHttpRequest"}
                           :cookie-policy :standard
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
        (concat rows (:rows j))))))

(defn- parse-reference-html
  [url]
  (let [parsed-doc (some->> (http-get url)
                            h/parse
                            h/as-hickory)
        new? (boolean (some->> parsed-doc
                               (select/select (select/descendant
                                               (select/class "p-ref__ico")
                                               (select/class "p-ico")))
                               first))
        [[level] [digimon-type] [attribute] special-moves]
        (some->> parsed-doc
                 (select/select (select/descendant
                                 (select/class "p-ref__info")
                                 (select/tag "dd")))
                 (map :content))
        profile (some->> parsed-doc
                         (select/select (select/descendant
                                         (select/class "p-ref__profile")))
                         first
                         :content
                         (filter string?)
                         (map string/trim)
                         (remove empty?)
                         (string/join "\n"))]
    (when parsed-doc
      {:reference/new? new?
       :reference/level level
       :reference/type digimon-type
       :reference/attribute attribute
       :reference/special-moves (->> special-moves
                                     (filter string?)
                                     (mapv (fn [s] (string/replace s "・" ""))))
       :reference/profile profile})))

(defn book
  []
  (->> reference-suffix-by-language
       (reduce-kv (fn [accl language reference-suffix]
                    (->> (directory language)
                         (pmap (fn [{:keys [directory-name name]}]
                                 (let [url (str origin "/reference"
                                                reference-suffix
                                                "/detail.php?directory_name="
                                                directory-name)
                                       image (str origin "/cimages/digimon/"
                                                  directory-name
                                                  ".jpg")]
                                   (merge {:reference/id url
                                           :reference/language language
                                           :reference/name (name-fixes name)
                                           :reference/image image}
                                          (parse-reference-html url)))))
                         (concat accl)))
                  [])
       (sort-by (juxt :reference/image
                      :reference/language))))

(defn -main
  [& args]
  (book))
