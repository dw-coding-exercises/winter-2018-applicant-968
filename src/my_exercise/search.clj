(ns my-exercise.search
  (:require [hiccup.page :refer [html5]]
            [clojure.string :as str]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [my-exercise.us-state :as us-state]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [my-exercise.turbovote :refer [elections]]))

(def states (set us-state/postal-abbreviations))

(defn parse-int
  "If the given value represents and integer, return the int, otherwise return the value"
  [x]
  (try
    (Integer/parseInt x)
    (catch java.lang.NumberFormatException e x)))

(defn header [_]
  [:head
   [:meta {:charset "UTF-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1.0, maximum-scale=1.0"}]
   [:title "Election search results"]
   [:link {:rel "stylesheet" :href "default.css"}]])

(defn empty-response [_ params]
  [:div {:class "empty-response elections-response"}
   [:h1 "No Elections found"]
   [:p (:city params) ", " (:state params) " has no upcoming elections"]])

(defn list-elections [elections params]
  [:div {:class "elections-response"}
   [:h1 "Elections"]
   (for [e elections]
     [:div {:class "election-detail"}
      [:h2 [:a {:href (:website e)} (:description e)]]
      [:p {:class "election-date" } (:date e)]
      [:ul {:class "election-districts"}
       (for [dist (:district-divisions e)]
         [:li [:p "Voting division: " (:ocd-id dist)]
          [:ul {:class "voting-methods"}
           (for [method (:voting-methods dist)]
             [:li (str/replace (name (:type method)) #"-" " ")])]])]])])

(defn election-info
  "Display an election results page for the given US address"
  [params]
  (let [upcoming-elections (elections params)]
    (if (> (count upcoming-elections) 0)
      (list-elections upcoming-elections params)
      (empty-response upcoming-elections params))))

(defn invalid-search
  "Display an error page when invalid search params are submitted"
  [errors]
  [:div {:class "error-response elections-response"}
   [:h1 "Invalid search"]
   [:ul {:class "errors-list"}
    (for [e errors]
      [:li (str (-> e first name) ": " (-> e second first))])]])

(defn valid-address?
  "Determine whether the given map can be construed as a valid US address"
  [params]
  (b/validate params
              :street v/required
              :city v/required
              :state [v/required
                      [#(contains? states (clojure.string/upper-case %))
                       :message "An invalid state abbreviation was submitted"]]
              :zip [v/required v/number [#(= (count (str %)) 6) :message "Zip code must be 6 digits"]]))

(defn results [request]
  (html5
   (header request)
   (let [[errors result] (valid-address? (update (:params request) :zip parse-int))]
     (if errors
       (invalid-search errors)
       (election-info result)))))
