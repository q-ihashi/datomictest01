(ns sample3.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [sample3.meishi :as meishi]
            ;次の２つのS式を追加。カッコのネストに注意。
            ;ring関連で使用する関数のrequire宣言を追加している。
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]
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
  (GET "/" [] "Hello World")
  (GET "/datomic-name" [] (str (q '[:find ?e ?v :where [?e :user/name ?v ]] (get-db conn))))
  (GET "/datomic-json-name" [] (str "{ \"data\": " (q '[:find ?e ?v :where [?e :user/name ?v ]] (get-db conn)) " }"))
  (GET "/datomic-addr" [] (str (q '[:find ?e ?v :where [?e :user/address ?v ]] (get-db conn))))
  (GET "/datomic-join" [] (str (q '[:find ?e ?v1 ?v1tx ?v2 ?v2tx :where [?e :user/name ?v1 ?v1tx ][?e :user/address ?v2 ?v2tx ]] (get-db conn))))
  (GET "/datomic-entity"  [] (str (q '[:find ?k ?v ?tx ?added :where [17592186045570 ?k ?v ?tx ?added]] (get-db conn))))
  (GET "/datomic-all"  [] (str (q '[:find ?e  ?tx ?added :where [?e :user/name "taro" ?tx ?added]] (get-db conn))))
  (GET "/datomic-txInstant"  [] (str (q '[:find ?e ?v :where [?e :db/txInstant ?v]] (get-db conn))))
  (GET "/datomicschemaname" [] (str (d/transact conn s-tx-user-name)))
  (GET "/datomicschemaaddr" [] (str (d/transact conn s-tx-user-address)))
  (GET "/datomicschemainit" [] (str (d/transact conn meishi/meishi-schema)))
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

