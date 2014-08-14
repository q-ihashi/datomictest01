(ns sample3.meishi
  (:require [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]
            [datomic.api :as d :refer [db q]]
            ))

(def meishi-schema
[
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
   :db/fulltext false
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

  {:db/id #db/id[:db.part/db]
   :db/ident :meishi/title
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/fulltext false
   :db/doc "meishi title"
   :db.install/_attribute :db.part/db}

  {:db/id #db/id[:db.part/db]
   :db/ident :meishi/name
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/fulltext false
   :db/doc "meishi name"
   :db.install/_attribute :db.part/db}

  {:db/id #db/id[:db.part/db]
   :db/ident :meishi/addr
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/fulltext false
   :db/doc "meishi addr"
   :db.install/_attribute :db.part/db}

  {:db/id #db/id[:db.part/db]
   :db/ident :meishi/tel
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/fulltext false
   :db/doc "meishi tel"
   :db.install/_attribute :db.part/db}

  {:db/id #db/id[:db.part/db]
   :db/ident :meishi/email
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/fulltext false
   :db/doc "meishi email"
   :db.install/_attribute :db.part/db}

  [:db/add 1 :user/name "taro1"]
  [:db/add 1 :user/loginId "logtaro1"]
  [:db/add 2 :user/name "taro2"]
  [:db/add 2 :user/loginId "logtaro2"]
  [:db/add 3 :user/name "taro3"]
  [:db/add 3 :user/loginId "logtaro3"]
  [:db/add 4 :user/name "taro4"]
  [:db/add 4 :user/loginId "logtaro4"]
  [:db/add 5 :user/name "taro5"]
  [:db/add 5 :user/loginId "logtaro5"]
  ]
  )
