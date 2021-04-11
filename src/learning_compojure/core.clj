(ns learning-compojure.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :as p]
            ;[simple-compojure.middleware :as m]
            ;[simple-compojure.routes :as r]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [not-found]])
            ;[simple-compojure.views :as v])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defonce log-atom (atom []))
;(println @log-atom)
(defn logger [handler]
  (fn [req]
    (let [res (handler req)]
      (swap! log-atom #(conj % {:request req :response res}))
      res)))

(defn req-res-displayer [handler]
  (fn [req]
    (let [res (handler req)]
      (println "\nRequest:")
      (clojure.pprint/pprint req)
      (println "\nResponse:")
      (clojure.pprint/pprint res)
      res)))

(defn main [req]
    "<div>
       <h1>Hello Web Page with Routing!</h1>
       <p>What would you like to do?</p>
       <p><a href='./get-form.html'>Submit a GET request</a></p>
       <p><a href='./post-form.html'>Submit a POST request</a></p>
     </div>")

(defn get-form [req]
    "<div>
      <h1>Hello GET Form!</h1>
      <p>Submit a message with GET</p>
      <form method=\"get\" action=\"get-submit\">
         <input type=\"text\" name=\"name\" />
         <input type=\"submit\" value\"submit\" />
      </form>
      <p><a href='..'>Return to main page</p>
    </div>")

(defn post-form [req]
   "<div>
      <h1>Hello POST Form!</h1>
      <p>Submit a message with POST</p>
      <form method=\"post\" action=\"post-submit\">
        <input type=\"text\" name=\"name\" />
        <input type=\"submit\" value\"submit\" />
      </form>
     <p><a href='..'>Return to main page</p>
    </div>")

(defn display-result [req]
    (let [{:keys [params uri]} req
          param-name (get params "name")
          req-type (if (= uri "/get-submit") "GET" "POST")]
      (str
        "<div>
          <h1>Hello " param-name "!</h1>
          <p>Submitted via a " req-type " request.</p>
          <p><a href='..'>Return to main page</p>
        </div>")))

(defn not-found1 []
    "<h1>404 Error!</h1>
     <b>Page not found!</b>
     <p><a href='..'>Return to main page</p>")

(defroutes routes
  (GET "/" req (main req))
  (GET "/get-form.html" req (get-form req))
  (GET "/post-form.html" req (post-form req))
  (GET "/get-submit" req (display-result req))
  (POST "/post-submit" req (display-result req))
  (not-found (not-found1)))

(def app
  (-> routes
      logger
      req-res-displayer
      p/wrap-params))

(defonce server
  (run-jetty #'app {:port 8080 :join? false}))

(server)
