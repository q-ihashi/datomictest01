(ns sample3.meishi
  (:require [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]
            [datomic.api :as d :refer [db q]]
            ))

(defn meishi-schema [c]
  (d/transact c
    [
 ;; user
     {:db/id #db/id[:db.part/db]
  :db/ident :user/loginId
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/fulltext false
  :db/doc "loginId"
  :db.install/_attribute :db.part/db}

 {:db/id #db/id[:db.part/db]
  :db/ident :user/name
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/fulltext true
  :db/doc "user name"
  :db.install/_attribute :db.part/db}

 {:db/id #db/id[:db.part/db]
  :db/ident :user/myMeishi
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/many
  :db/doc "myMeishi"
  :db.install/_attribute :db.part/db}

 {:db/id #db/id[:db.part/db]
  :db/ident :user/hasMeishi
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/many
  :db/doc "hasMeishi"
  :db.install/_attribute :db.part/db}

     ;;meishi
     {:db/id #db/id[:db.part/db]
  :db/ident :meishi/title
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/fulltext true
  :db/doc "meishi title"
  :db.install/_attribute :db.part/db}

 {:db/id #db/id[:db.part/db]
  :db/ident :meishi/name
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/fulltext true
  :db/doc "meishi name"
  :db.install/_attribute :db.part/db}

 {:db/id #db/id[:db.part/db]
  :db/ident :meishi/addr
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/fulltext true
  :db/doc "meishi addr"
  :db.install/_attribute :db.part/db}

 {:db/id #db/id[:db.part/db]
  :db/ident :meishi/tel
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/fulltext true
  :db/doc "meishi tel"
  :db.install/_attribute :db.part/db}

 {:db/id #db/id[:db.part/db]
  :db/ident :meishi/email
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/fulltext true
  :db/doc "meishi email"
  :db.install/_attribute :db.part/db}

 ]
  ))
