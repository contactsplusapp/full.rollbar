# full.rollbar

[![Clojars Project](https://img.shields.io/clojars/v/fullcontact/full.rollbar.svg)](https://clojars.org/fullcontact/full.rollbar)
[![Build Status](https://travis-ci.org/fullcontact/full.rollbar.svg?branch=master)](https://travis-ci.org/fullcontact/full.rollbar)

Async client for [Rollbar](https://rollbar.com) error tracking service.


## Config

Configuration is loaded implicitly via [full.core](https://github.com/fullcontact/full.core)
and you'll need to include this in your config files:

```yaml
rollbar:
  access-token: "1234"
  environment: "production"
```


### Reporting in HTTP middleware

full.rollbar has request middleware that can be used in [full.http](https://github.com/fullcontact/full.http)'s request handling
pipelines. Use it like this:

Exception collection is done via a middleware method `rollbar.middleware/report-exception>`. You'll need to included it
before any other exception handlers.

```clojure

(full.http.server/defroutes some-routes
  ; Route definitions
  )

(defn api [routes]
  (-> (rollbar.middleware/report-exception> rotues)
      ; more middleware methods
      ))

; when running the server:
(full.http.server/run-server (api #'some-routes))
```

`report-exception>` accepts optional arguments for populating `custom` and
`person` fields of the Rollbar [API request](https://rollbar.com/docs/api/items_post/):

```
(report-exception>
  handler
  :person-fn (fn [req] {:email (:account-email req)})
  :custom-fn (fn [req] {:foo "bar"}))
```

See this [example server](https://github.com/fullcontact/full.bootstrap/blob/master/examples/http-service/src/example/api.clj) for more info
on writing middleware and full.http applications.


### Manual reporting

To send an exception to Rollbar manually, you can use `rollbar.core/report>`
method, which accepts an exception.
