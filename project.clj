(defn artifactory [path]
  {:url (str "https://contactsplus.jfrog.io/artifactory/" path)
   :sign-releases false})

(defproject fullcontact/full.rollbar "0.10.7"
  :description "Library to ship exceptions and request information to the rollbar logging service."
  :url "https://github.com/contactsplusapp/full.rollbar"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :repositories [["fullcontact" ~(artifactory "repo")]
                 ["releases" ~(artifactory "libs-release-local")]
                 ["snapshots" ~(artifactory "libs-snapshot-local")]]
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "0.7.559"]
                 [fullcontact/camelsnake "0.9.0"]
                 [fullcontact/full.async "1.1.1"]
                 [fullcontact/full.core "1.1.3" :exclusions [org.clojure/clojurescript]]
                 [fullcontact/full.http "1.0.11"]]
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "--no-sign"]
                  ["deploy"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]]
  :plugins [[lein-midje "3.1.3"]]
  :profiles {:dev {:dependencies [[midje "1.10.9"]]}})

