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
   :db/valueType :db.type/long
   :db/cardinality :db.cardinality/many
   :db/doc "myMeishi"
   :db.install/_attribute :db.part/db}

  {:db/id #db/id[:db.part/db]
   :db/ident :user/hasMeishi
   :db/valueType :db.type/long
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
   :db/ident :meishi/company
   :db/valueType :db.type/string
   :db/cardinality :db.cardinality/one
   :db/fulltext false
   :db/doc "meishi company"
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

  ]
  )

  (def meishi-data
  [
  [:db/add 1 :user/loginId "logtaro1"]
  [:db/add 1 :user/name "本名太郎"]
  [:db/add 1 :user/myMeishi 1 ]
  [:db/add 1 :user/myMeishi 2 ]
  [:db/add 1 :user/myMeishi 3 ]
  [:db/add 1 :user/hasMeishi 4 ]
  [:db/add 1 :user/hasMeishi 6 ]
  [:db/add 1 :meishi/title "めいし１"]
  [:db/add 1 :meishi/company "株式会社 名刺１"]
  [:db/add 1 :meishi/name "名刺太郎"]
  [:db/add 1 :meishi/addr "錦糸町1-1-1"]
  [:db/add 1 :meishi/tel "03-9999-9991"]
  [:db/add 1 :meishi/email "taro@meishi-email1.tst"]
  [:db/add 2 :meishi/title "めいし２"]
  [:db/add 2 :meishi/company "株式会社 名刺２"]
  [:db/add 2 :meishi/name "名刺太郎２"]
  [:db/add 2 :meishi/addr "錦糸町2-2-2"]
  [:db/add 2 :meishi/tel "03-9999-9992"]
  [:db/add 2 :meishi/email "taro@meishi-email2.tst"]
  [:db/add 3 :meishi/title "めいし３"]
  [:db/add 3 :meishi/company "株式会社 名刺３"]
  [:db/add 3 :meishi/name "名刺太郎３"]
  [:db/add 3 :meishi/addr "錦糸町3-3-3"]
  [:db/add 3 :meishi/tel "03-9999-9993"]
  [:db/add 3 :meishi/email "taro@meishi-email3.tst"]

  [:db/add 2 :user/loginId "logtaro2"]
  [:db/add 2 :user/name "本名次郎"]
  [:db/add 2 :user/myMeishi 4 ]
  [:db/add 2 :user/myMeishi 5 ]
  [:db/add 2 :user/hasMeishi 1 ]
  [:db/add 3 :user/name "三郎"]
  [:db/add 3 :user/loginId "logtaro3"]
  [:db/add 2 :user/myMeishi 6 ]
  [:db/add 4 :user/name "四郎"]
  [:db/add 4 :user/loginId "logtaro4"]
  [:db/add 5 :user/name "五郎"]
  [:db/add 5 :user/loginId "logtaro5"]

  [:db/add 4 :meishi/title "めいし４"]
  [:db/add 4 :meishi/company "株式会社 名刺４"]
  [:db/add 4 :meishi/name "名刺次郎・４"]
  [:db/add 4 :meishi/addr "錦糸町4-4-4"]
  [:db/add 4 :meishi/tel "03-9999-9994"]
  [:db/add 4 :meishi/email "jiro@meishi-email4.tst"]
  [:db/add 5 :meishi/title "めいし５"]
  [:db/add 5 :meishi/company "株式会社 名刺５"]
  [:db/add 5 :meishi/name "名刺次郎・５"]
  [:db/add 5 :meishi/addr "錦糸町5-5-5"]
  [:db/add 5 :meishi/tel "03-9999-9995"]
  [:db/add 5 :meishi/email "jiro@meishi-email5.tst"]
  [:db/add 6 :meishi/title "めいし６"]
  [:db/add 6 :meishi/company "株式会社 名刺６"]
  [:db/add 6 :meishi/name "名刺三郎・５"]
  [:db/add 6 :meishi/addr "錦糸町5-5-5"]
  [:db/add 6 :meishi/tel "03-9999-9995"]
  [:db/add 6 :meishi/email "saburo@meishi-email5.tst"]
  ]
)
