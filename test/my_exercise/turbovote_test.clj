(ns my-exercise.turbovote-test
  (:require [clojure.test :refer :all]
            [my-exercise.turbovote :refer :all]))

(deftest turbovote-query-test
  (testing "turbovote-query returns the correct URL"
    (is (= "https://api.turbovote.org/elections/upcoming?district-divisions=ocd-division/country:us/state:co,ocd-division/country:us/state:co/place:denver"
           (turbovote-query {:city "Denver" :state "CO"})))))
