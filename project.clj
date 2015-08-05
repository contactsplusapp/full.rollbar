(defproject fullcontact/full.rollbar "0.8.3-SNAPSHOT"
  :description "Library to ship exceptions and request information to the rollbar logging service."

  :url "https://github.com/fullcontact/full.monty"

  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}

  :deploy-repositories [["releases" {:url "https://clojars.org/repo/" :creds :gpg}]]

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [fullcontact/camelsnake "0.8.3-SNAPSHOT"]
                 [fullcontact/full.async "0.8.3-SNAPSHOT"]
                 [fullcontact/full.core "0.8.3-SNAPSHOT"]
                 [fullcontact/full.http "0.8.3-SNAPSHOT"]]
  :aot :all

  :plugins [[lein-midje "3.1.3"]]

  :profiles {:dev {:dependencies [[midje "1.6.3"]]}})

