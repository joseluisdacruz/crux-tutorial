(ns crux-tutorial.core
  (:require [crux.api :as crux])
  (:gen-class))

(def crux
  (crux/start-standalone-node
   {:kv-backend "crux.kv.memdb.MemKv"
    :db-dir "data/db-dir"
    :event-log-dir "data/eventlog-1"}))

(def manifest
  {:crux.db/id :manifest
   :pilot-name "José Luís"
   :id/rocket "SB002-sol"
   :id/employee "22910x2"
   :badges "SETUP"
   :cargo ["stereo" "gold fish" "slippers" "secret note"]})

#_(crux/submit-tx crux [[:crux.tx/put manifest]])
#_(crux/entity (crux/db crux) :manifest)
#_(crux/submit-tx crux
                [[:crux.tx/put
                  {:crux.db/id :commodity/Pu
                   :common-name "Plutonium"
                   :type :element/metal
                   :density 19.816
                   :redioactive true}]
                 [:crux.tx/put
                  {:crux.db/id :commodity/N
                   :common-name "Nitrogen"
                   :type :element/gas
                   :density 1.2506
                   :radioactive false}]

                 [:crux.tx/put
                  {:crux.db/id :commodity/CH4
                   :common-name "Methane"
                   :type :molecule/gas
                   :density 0.717
                   :radioactive false}]])

#_(crux/submit-tx crux
                [[:crux.tx/put
                  {:crux.db/id :stock/Pu
                   :commod :commodity/Pu
                   :weight-ton 21}
                  #inst "2115-02-13T18"] ;; valid-time

                 [:crux.tx/put
                  {:crux.db/id :stock/Pu
                   :commod :commodity/Pu
                   :weight-ton 23}
                  #inst "2115-02-14T18"]

                 [:crux.tx/put
                  {:crux.db/id :stock/Pu
                   :commod :commodity/Pu
                   :weight-ton 22.2}
                  #inst "2115-02-15T18"]

                 [:crux.tx/put
                  {:crux.db/id :stock/Pu
                   :commod :commodity/Pu
                   :weight-ton 24}
                  #inst "2115-02-18T18"]

                 [:crux.tx/put
                  {:crux.db/id :stock/Pu
                   :commod :commodity/Pu
                   :weight-ton 24.9}
                  #inst "2115-02-19T18"]])

#_(crux/submit-tx crux
                [[:crux.tx/put
                  {:crux.db/id :stock/N
                   :commod :commodity/N
                   :weight-ton 3}
                  #inst "2115-02-13T18" ;; start valid-time
                  #inst "2115-02-19T18"] ;; end valid-time

                 [:crux.tx/put
                  {:crux.db/id :stock/CH4
                   :commod :commodity/CH4
                   :weight-ton 92}
                  #inst "2115-02-15T18"
                  #inst "2115-02-19T18"]])

(defn easy-ingest
  "Uses Crux put transaction to add a vector of documents to a specified node"
  [node docs]
  (crux/submit-tx node
                  (vec (for [doc docs]
                         [:crux.tx/put doc]))))
                         

(crux/entity (crux/db crux #inst "2115-02-14") :stock/N)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
