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

(crux/submit-tx crux [[:crux.tx/put manifest]])

(crux/entity (crux/db crux) :manifest)

(crux/submit-tx crux
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

(crux/submit-tx crux
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

(crux/entity (crux/db crux #inst "2115-02-19T18") :stock/Pu)

(crux/submit-tx crux
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

(crux/submit-tx crux
                [[:crux.tx/put (assoc manifest :badges ["SETUP" "PUT"])]])

(crux/entity (crux/db crux) :manifest)

(crux/entity (crux/db crux #inst "2115-02-20") :stock/N)

(def data
  [{:crux.db/id :commodity/Pu
    :common-name "Plutonium"
    :type :element/metal
    :density 19.816
    :radioactive true}

   {:crux.db/id :commodity/N
    :common-name "Nitrogen"
    :type :element/gas
    :density 1.2506
    :radioactive false}

   {:crux.db/id :commodity/CH4
    :common-name "Methane"
    :type :molecule/gas
    :density 0.717
    :radioactive false}

   {:crux.db/id :commodity/Au
    :common-name "Gold"
    :type :element/metal
    :density 19.300
    :radioactive false}

   {:crux.db/id :commodity/C
    :common-name "Carbon"
    :type :element/non-metal
    :density 2.267
    :radioactive false}

   {:crux.db/id :commodity/borax
    :common-name "Borax"
    :IUPAC-name "Sodium tetraborate decahydrate"
    :other-names ["Borax decahydrate" "sodium borate" "sodium tetraborate" "disodium tetraborate"]
    :type :mineral/solid
    :appearance "white solid"
    :density 1.73
    :radioactive false}])

(easy-ingest crux data)

;; Datalog

(crux/q (crux/db crux)
        '{:find [element]
          :where [[element :type :element/metal]]})

(crux/q (crux/db crux)
        {:find '[name]
         :where '[[e :type :element/metal]
                  [e :common-name name]]})

(crux/q (crux/db crux)
        '{:find [name rho]
          :where [[e :density rho]
                  [e :common-name name]]})

(crux/q (crux/db crux)
        {:find '[name]
         :where '[[e :type t]
                  [e :common-name name]]
         :args [{'t :element/metal}]})

(defn filter-type
  [type]
  (crux/q (crux/db crux)
          {:find  '[name]
           :where '[[e :type t]
                    [e :common-name name]]
           :args [{'t type}]}))

(defn filter-appearance
  [description]
  (crux/q (crux/db crux)
          {:find '[name IUPAC]
           :where '[[e :common-name name]
                    [e :IUPAC-name IUPAC]
                    [e :appearance appearance]]
           :args [{'appearance description}]}))
                     
(filter-type :element/metal)

(filter-appearance "white solid")

(crux/submit-tx
 crux
 [[:crux.tx/put
   (assoc manifest :badges ["SETUP" "PUT" "DATALOG-QUERIES"])]])

(crux/submit-tx
 crux
 [[:crux.tx/put
   {:crux.db/id :consumer/RJ29sUU
    :consumer-id :RJ29sUU
    :first-name "Jay"
    :last-name "Rose"
    :cover? true
    :cover-type :Full}
   #inst "2114-12-03"]])

(crux/submit-tx
 crux
 [[:crux.tx/put
   {:crux.db/id :consumer/RJ29sUU
    :consumer-id :RJ29sUU
    :first-name "Jay"
    :last-name "Rose"
    :cover? true
    :cover-type :Full}
   #inst "2113-12-03" ;; Valid time start
   #inst "2114-12-03"] ;; Valid time end

  [:crux.tx/put
   {:crux.db/id :consumer/RJ29sUU
    :consumer-id :RJ29sUU
    :first-name "Jay"
    :last-name "Rose"
    :cover? true
    :cover-type :Full}
   #inst "2112-12-03"
   #inst "2113-12-03"]

  [:crux.tx/put
   {:crux.db/id :consumer/RJ29sUU
    :consumer-id :RJ29sUU
    :first-name "Jay"
    :last-name "Rose"
    :cover? false}
   #inst "2112-06-03"
   #inst "2112-12-02"]

  [:crux.tx/put
   {:crux.db/id :consumer/RJ29sUU
    :consumer-id :RJ29sUU
    :first-name "Jay"
    :last-name "Rose"
    :cover? true
    :cover-type :Promotional}
   #inst "2111-06-03"
   #inst "2112-06-03"]])

(crux/q (crux/db crux #inst "2115-07-03")
        {:find '[cover type]
         :where '[[e :consumer-id :RJ29sUU]
                  [e :cover? cover]
                  [e :cover-type type]]})

(crux/q (crux/db crux #inst "2111-07-03")
        {:find '[cover type]
         :where '[[e :consumer-id :RJ29sUU]
                  [e :cover? cover]
                  [e :cover-type type]]})

(crux/q (crux/db crux #inst "2112-07-03")
        {:find '[cover type]
         :where '[[e :consumer-id :RJ29sUU]
                  [e :cover? cover]
                  [e :cover-type type]]})

(crux/submit-tx
 crux
 [[:crux.tx/put
   (assoc manifest :badges ["SETUP" "PUT" "DATALOG-QUERIES" "BITEMP"])]])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
