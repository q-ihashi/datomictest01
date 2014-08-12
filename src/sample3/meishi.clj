(ns sample3.meishi
  (:require [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]
            [datomic.api :as d :refer [db q]]
            ))

(def meishi-schema
[
 ;; user
 {:db/id #db/id[:db.part/db]
  :db/ident :user/loginId
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/fulltext false
  :db/doc "loginId"}

 {:db/id #db/id[:db.part/db]
  :db/ident :user/name
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/fulltext true
  :db/doc "user name"}

 {:db/id #db/id[:db.part/db]
  :db/ident :user/myMeishi
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/many
  :db/doc "myMeishi"}

 {:db/id #db/id[:db.part/db]
  :db/ident :user/hasMeishi
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/many
  :db/doc "hasMeishi"}

 ; meishi
 {:db/id #db/id[:db.part/db]
  :db/ident :meishi/title
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/fulltext true
  :db/doc "meishi title"}

 {:db/id #db/id[:db.part/db]
  :db/ident :meishi/name
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/fulltext true
  :db/doc "meishi name"}

 {:db/id #db/id[:db.part/db]
  :db/ident :meishi/addr
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/fulltext true
  :db/doc "meishi addr"}

 {:db/id #db/id[:db.part/db]
  :db/ident :meishi/tel
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/fulltext true
  :db/doc "meishi tel"}

 {:db/id #db/id[:db.part/db]
  :db/ident :meishi/email
  :db/valueType :db.type/string
  :db/cardinality :db.cardinality/one
  :db/fulltext true
  :db/doc "meishi email"}

 ]
  )
