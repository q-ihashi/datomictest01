(ns sample3.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [sample3.meishi :as meishi]
            ;次の２つのS式を追加。カッコのネストに注意。
            ;ring関連で使用する関数のrequire宣言を追加している。
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]
            [clojure.data.json :as json]
            [datomic.api :as d :refer [db q]]
            ))

(def uri "datomic:free://127.0.0.1:4334/test")
;(def uri "datomic:free://192.168.0.71:4334/test")
;(def uri "datomic:free://192.168.0.71:4334/test?h2-port=4335&h2-web-port=4336")
;(def uri "datomic:dev://192.168.0.71:4334/test")
;(d/create-database "datomic:free://127.0.0.1:4334/test")
;(def uri "datomic:mem://simple-db")
(d/create-database uri)
;(def conn (d/connect "datomic:free://192.168.0.71:4334/test"))
(def conn (d/connect uri))
;(def dbw (d/db conn))

; スキーマ定義
(def s-tx-user-name
  [{:db/id #db/id[:db.part/db]
   :db/ident :user/name
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/doc "A person's name"
   :db.install/_attribute :db.part/db}]
  )

(def s-tx-user-address
  [{:db/id #db/id[:db.part/db]
   :db/ident :user/address
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db.install/_attribute :db.part/db}]
  )

;(def s-tx-book-name
;  {:db/id #db/id[:db.part/db]
;   :db/ident :book/name
;   :db/valueType :db.type/string
;   :db/cardinality :db.cardinality/one
;   :db.install/_attribute :db.part/db})
;
; 定義登録
;(d/transact conn s-tx-user-name)
;@(d/transact conn s-tx-user-address)
;@(d/transact conn s-tx-book-name)

;(d/transact conn [[:db/add 1 :user/address "kanagawa"]
;                  [:db/add 1 :user/name "taro"]])
;

;(def results (q '[:find ?e :where [?e :user/name]] (d/db conn)))

(defn get-db [cn] (d/db cn))
(defn tra [sch] (d/transact conn sch))

(defroutes app-routes
  (GET "/datomicschemainit" [] (str (d/transact conn meishi/meishi-schema)))
  (GET "/datomicschemainitdata" [] (str (d/transact conn meishi/meishi-data)))
  (GET "/user-get-p" [pp1]
       (let [pp1long (Long/parseLong pp1)]
           (json/write-str
               {:uname (map first (q '[:find ?v :in $ ?p :where [?p :user/name ?v ]]   (get-db conn) pp1long)),
                :myMeishi  (map first (q '[:find ?v :in $ ?p :where [?p :user/myMeishi ?v]] (get-db conn) pp1long)),
                :hasMeishi (map first (q '[:find ?v :in $ ?p :where [?p :user/hasMeishi ?v]] (get-db conn) pp1long)),
               }))
  )
  (GET "/meishi-get-p" [pp1]
       (let [pp1long (Long/parseLong pp1)]
           (json/write-str
               {:title   (map first (q '[:find ?v :in $ ?p :where [?p :meishi/title ?v]] (get-db conn) pp1long)),
                :company (map first (q '[:find ?v :in $ ?p :where [?p :meishi/company ?v]]  (get-db conn) pp1long)),
                :name    (map first (q '[:find ?v :in $ ?p :where [?p :meishi/name ?v]]  (get-db conn) pp1long)),
                :addr    (map first (q '[:find ?v :in $ ?p :where [?p :meishi/addr ?v]]  (get-db conn) pp1long)),
                :tel     (map first (q '[:find ?v :in $ ?p :where [?p :meishi/tel ?v]]   (get-db conn) pp1long)),
                :email   (map first (q '[:find ?v :in $ ?p :where [?p :meishi/email ?v]] (get-db conn) pp1long)),
               }))
  )
  (GET "/meishi-tx-p" [pp1]
       (let [pp1long (Long/parseLong pp1)]
           (json/write-str
             {:txInstant (.format (java.text.SimpleDateFormat. "MM/dd/yyyy") (q '[:find ?when :in $ ?p :where [?p :meishi/name ?n][?tx :db/txInstant ?when]] (get-db conn) pp1long))
             }))
  )

  (GET "/meishi-add-uname"     [upid updata] (str (d/transact conn [[:db/add (Long/parseLong upid) :user/name updata]])))
  (GET "/meishi-add-myMeishi"  [upid updata] (str (d/transact conn [[:db/add (Long/parseLong upid) :user/myMeishi (Long/parseLong updata)]])))
  (GET "/meishi-add-hasMeishi" [upid updata] (str (d/transact conn [[:db/add (Long/parseLong upid) :user/hasMeishi (Long/parseLong updata)]])))
  (GET "/meishi-add-title"     [upid updata] (str (d/transact conn [[:db/add (Long/parseLong upid) :meishi/title updata]])))
  (GET "/meishi-add-company"   [upid updata] (str (d/transact conn [[:db/add (Long/parseLong upid) :meishi/company updata]])))
  (GET "/meishi-add-name"      [upid updata] (str (d/transact conn [[:db/add (Long/parseLong upid) :meishi/name updata]])))
  (GET "/meishi-add-addr"      [upid updata] (str (d/transact conn [[:db/add (Long/parseLong upid) :meishi/address updata]])))
  (GET "/meishi-add-tel"       [upid updata] (str (d/transact conn [[:db/add (Long/parseLong upid) :meishi/tel updata]])))
  (GET "/meishi-add-email"     [upid updata] (str (d/transact conn [[:db/add (Long/parseLong upid) :meishi/email updata]])))


;
; ↓残骸
  (GET "/datomic-json-meishi2-p" [pp1]
       (json/write-str {:uname (q '[:find ?v :in $ ?p :where [?p :user/name ?v ]]   (get-db conn) (Long/parseLong pp1)),
                        :myMeishi  (q '[:find ?v :in $ ?p :where [?p :user/myMeishi ?v]] (get-db conn) (Long/parseLong pp1)),
                        :hasMeishi (q '[:find ?v :in $ ?p :where [?p :user/hasMeishi ?v]] (get-db conn) (Long/parseLong pp1)),
                        :name  (q '[:find ?v :in $ ?p :where [?p :meishi/name ?v]]  (get-db conn) (Long/parseLong pp1)),
                        :addr  (q '[:find ?v :in $ ?p :where [?p :meishi/addr ?v]]  (get-db conn) (Long/parseLong pp1)),
                        :tel   (q '[:find ?v :in $ ?p :where [?p :meishi/tel ?v]]   (get-db conn) (Long/parseLong pp1)),
                        :email (q '[:find ?v :in $ ?p :where [?p :meishi/email ?v]] (get-db conn) (Long/parseLong pp1)),
                        }))
  (GET "/datomic-json-meishi2-p" [pp1]
       (json/write-str {:uname (q '[:find ?v :in $ ?p :where [?p :user/name ?v ]]   (get-db conn) (Long/parseLong pp1))})
      )
  (GET "/" [] "Hello World")
  (GET "/datomic-name" [] (str (q '[:find ?e ?v :where [?e :user/name ?v ]] (get-db conn))))
  (GET "/datomic-json-namebk" [] (str "{ \"data\": " (q '[:find ?e ?v :where [?e :user/name ?v ]] (get-db conn)) " }"))
  (GET "/datomic-json-name" [] (json/write-str {:data (q '[:find ?e ?v :where [?e :user/name ?v ]] (get-db conn)) }))
  (GET "/datomic-json-name-p" [pp1] (json/write-str {:data (q '[:find ?v :in $ ?p :where [1 :user/name ?v ]] (get-db conn) (Long/parseLong pp1)) }))
  (GET "/datomic-json-meishi-p" [p] (json/write-str {:data (q '[:find ?vn ?vt :where [1 :user/name ?vn ][1 :meishi/title ?vt]] (get-db conn)) }))
  (GET "/datomic-json-test" [pp] (json/write-str {:data pp}))
  (GET "/datomic-addr" [] (str (q '[:find ?e ?v :where [?e :user/address ?v ]] (get-db conn))))
  (GET "/datomic-join" [] (str (q '[:find ?e ?v1 ?v1tx ?v2 ?v2tx :where [?e :user/name ?v1 ?v1tx ][?e :user/address ?v2 ?v2tx ]] (get-db conn))))
  (GET "/datomic-entity"  [] (str (q '[:find ?k ?v ?tx ?added :where [17592186045570 ?k ?v ?tx ?added]] (get-db conn))))
  (GET "/datomic-all"  [] (str (q '[:find ?e  ?tx ?added :where [?e :user/name "taro" ?tx ?added]] (get-db conn))))
  (GET "/datomic-txInstant"  [] (str (q '[:find ?e ?v :where [?e :db/txInstant ?v]] (get-db conn))))
  (GET "/datomicschemaname" [] (str (d/transact conn s-tx-user-name)))
  (GET "/datomicschemaaddr" [] (str (d/transact conn s-tx-user-address)))
;  (GET "/datomicschemainit" [] (str (map tra meishi/meishi-schema)))
  (GET "/datomicadd" [] (str (d/transact conn [[:db/add #db/id[:db.part/user] :user/address "kanagawa"]
                  [:db/add #db/id[:db.part/user] :user/name "taro"]])))
  (GET "/datomicadd2" [] (str (d/transact conn [[:db/add #db/id[:db.part/user] :user/address "kanagawa"]
                  [:db/add #db/id[:db.part/user] :user/name "taro"]])))
  (GET "/datomicadd3" [] (str (d/transact conn [[:db/add #db/id[:db.part/user] :user/name "TARO"]])))
  (GET "/datomicadd4" [] (str (d/transact conn [[:db/add 1 :user/name "TARO"]])))

  (GET "/datomicaddparam" [addid] (str (d/transact conn [[:db/add (Long/parseLong addid) :user/address (str "kanagawa" addid)]
                  [:db/add (Long/parseLong addid) :user/name (str "taro" addid)]])))
  (GET "/datomic-upname" [upid updata] (str (d/transact conn [[:db/add (Long/parseLong upid) :user/name updata]])))
  (GET "/datomic-upaddr" [upid updata] (str (d/transact conn [[:db/add (Long/parseLong upid) :user/address updata]])))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

