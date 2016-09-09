(ns full.rollbar.middleware
  "Rollbar middleware for full.http.server applications."
  (:require [clojure.core.async :refer [<!]]
            [full.async :refer [go-try]]
            [full.rollbar.core :as rollbar]
            [full.core.sugar :refer [?hash-map]]
            [camelsnake.core :refer [->snake_case]]))


(defn report-exception>
  [handler> & {:keys [person-fn custom-fn]}]
  (fn [req]
    (go-try
      (let [res (<! (handler> req))]
        (when (and @rollbar/enabled?
                   (instance? Throwable res)
                   (or (nil? (:status (ex-data res)))
                       (>= (:status (ex-data res)) 500)))
          (rollbar/report> res (?hash-map :request req
                                          :person-fn person-fn
                                          :custom-fn custom-fn)))
        res))))
