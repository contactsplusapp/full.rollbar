(ns full.rollbar.core
  (:require [full.http.client :refer [req>]]
            [full.async :refer [go-try <?]]
            [camelsnake.core :refer [->snake_case]]
            [full.core.log :as log]
            [full.core.config :refer [opt]])
  (:import (java.net InetAddress)))


(def rollbar-access-token (opt [:rollbar :access-token] :default nil))
(def rollbar-environment (opt [:rollbar :environment] :default nil))

(def enabled?
  (delay (and @rollbar-access-token @rollbar-environment)))

(def host (delay (.getHostAddress (InetAddress/getLocalHost))))

(defn- rollbar-req>
  [body]
  (req> {:base-url "https://api.rollbar.com"
         :resource "api/1/item/"
         :method :post
         :body body
         :body-json-key-fn ->snake_case}))

(defn root-exception
  [ex]
  (loop [ex ex]
    (if-let [cause (.getCause ex)]
      (recur cause)
      ex)))

(defn error-msg
  [exc]
  (let [message (.getMessage exc)]
    (if (empty? message)
      (.toString (.getClass exc))
      message)))

(defn- frame
  [row]
  {:filename (-> row .getFileName)
   :lineno (-> row .getLineNumber)
   :method (str (-> row .getClassName) "/" (-> row .getMethodName))})

(defn- frames
  [ex]
  (->> ex (.getStackTrace) (map frame)))

(defn- exception
  [ex]
  {:class (-> ex .getClass .toString)
   :message (error-msg ex)})

(defn trace-payload
  [ex]
  (let [root-ex (root-exception ex)]
    {:frames (frames root-ex)
     :exception (exception root-ex)}))

(defn request-payload
  [req]
  {:method (clojure.string/upper-case (name (:request-method req)))
   :url (:uri req)
   :headers (dissoc (:headers req) "authorization")
   :user_ip (:remote-addr req)})

(defn rollbar-payload
  [ex host access-token environment]
  {:access_token access-token
   :data {:environment environment
          :host host
          :language "clojure"
          :body {:trace (trace-payload ex)}}})

(defn report>
  "Reports an exception to Rollbar."
  [ex {:keys [request person-fn custom-fn]}]
  (go-try
    (when @enabled?
      (-> (rollbar-payload ex @host @rollbar-access-token @rollbar-environment)
          (cond->
            request (assoc-in [:data :body :request] (request-payload request))
            person-fn (assoc-in [:data :person] (person-fn request))
            custom-fn (assoc-in [:data :custom] (custom-fn request)))
          (rollbar-req>) <?))))
