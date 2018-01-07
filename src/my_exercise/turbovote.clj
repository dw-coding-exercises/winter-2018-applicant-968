(ns my-exercise.turbovote
  (:require [clj-http.client :as client]
            [clojure.string :as str]
            [clojure.edn :as edn]))


(def turbovote-api-base
  "https://api.turbovote.org/elections/upcoming?district-divisions=")
(defn turbovote-query
  "
  Given a US address map containing :city and :state, return a URL
  which can be used to query the turbovote API for upcoming elections

  It would be better to use a general function for converting data to
  URLs rather than this ad-hoc concatenation"
  [address]
  (let [state-ocd (str "ocd-division/country:us/state:" (str/lower-case (:state address)))
        place-ocd (str state-ocd "/place:" (str/lower-case (str/replace (:city address) #" " "_")))]
    (str turbovote-api-base (str/join "," [state-ocd place-ocd]))))

(defn elections
  "
  Given a US address map containing :city and :state, return a list
  of upcoming elections for the specified location"
  [address]
  (let [query (turbovote-query address)
        elections (-> query client/get :body edn/read-string)]
    elections))
