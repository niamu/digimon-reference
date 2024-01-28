(ns digimon-reference.core
  (:gen-class)
  (:require
   [clj-http.client :as client]
   [clojure.data.json :as json]
   [clojure.string :as string]))

(def origin "https://digimon.net")

(def reference-suffix-by-language
  {:ja ""
   :en "_en"
   :zh "_zh-CHS"
   :ko "_ko"})

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

(defn- digimon-name-fixes
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

(defn book
  []
  (reduce-kv (fn [accl language reference-suffix]
               (->> (directory language)
                    (pmap (fn [{:keys [directory-name name]}]
                            {:reference/id (str origin "/reference"
                                                reference-suffix
                                                "/detail.php?directory_name="
                                                directory-name)
                             :reference/language language
                             :reference/name (digimon-name-fixes name)
                             :reference/image (str origin "/cimages/digimon/"
                                                   directory-name
                                                   ".jpg")}))
                    (concat accl)))
             []
             reference-suffix-by-language))

(defn -main
  [& args]
  (clojure.pprint/pprint (book)))
