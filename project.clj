(defproject sample3 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.8"]
                 ;↓の２つのS式を追加。
                 [ring/ring-core "1.2.2"]
                 [ring/ring-json "0.2.0"]
                 [clj-time "0.8.0"]
                 [org.clojure/data.json "0.2.5"]
                 [com.datomic/datomic-free "0.9.4815.12"
                  :exclusions [... org.clojure/clojure]]
                ]
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler sample3.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}}
  ;↓この一行を追加。
  :web-content "public"
  :jvm-opts ^:replace ["-Xmx1g" "-server"]
)

