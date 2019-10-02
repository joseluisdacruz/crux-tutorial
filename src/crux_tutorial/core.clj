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

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
