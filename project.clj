(defproject fullcontact/full.rollbar "0.10.1-SNAPSHOT"
  :description "Library to ship exceptions and request information to the rollbar logging service."
  :url "https://github.com/fullcontact/full.rollbar"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :deploy-repositories [["releases" {:url "https://clojars.org/repo/" :creds :gpg}]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [fullcontact/camelsnake "0.9.0"]
                 [fullcontact/full.async "0.9.0"]
                 [fullcontact/full.core "0.10.0"]
                 [fullcontact/full.http "0.10.0"]]
  :aot :all
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "--no-sign"]
                  ["deploy"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]]
  :plugins [[lein-midje "3.1.3"]]
  :profiles {:dev {:dependencies [[midje "1.7.0"]]}})

