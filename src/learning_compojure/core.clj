(ns learning-compojure.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :as p]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [not-found]]
            [hiccup.core :refer :all]
            [hiccup.form :as form])
  (:gen-class))

;

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

; (defn req-res-displayer [handler]
;   (fn [req]
;     (let [res (handler req)]
;       (println "\nRequest:")
;       (clojure.pprint/pprint req)
;       (println "\nResponse:")
;       (clojure.pprint/pprint res)
;       res)))
;
; (defn display-req
;   [req]
;   (let [{:keys [params]} req
;         email (get params "email")
;         password (get params "password")]
;       (println "here" email password)))


(defn login-page
  []
  (html [:div
          (form/form-to [:post "does-user-exist?"]
              [:label {:for "email"} "Email:"]
              [:br]
              [:input {:type "text" :id "email" :name "email" :placeholder "email"}]
              [:br]
              [:label {:for "password"} "Password:"]
              [:br]
              [:input {:type "text" :id "password" :name "password" :placeholder "password"}]
              [:br]
              [:br]
              [:input {:type "submit" :value "Submit"}])]))

(defn does-user-exist?
  []
  ;[req
  ; (let [{:keys [params]} req
  ;       email (get params "email")
  ;       password (get params "password")]
 (html [:div [:p "email" "email"]
             [:br]
             [:p "password" "password"]]))

(def my-atom (atom {}))


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

(defn display-resultV2 [req]
    (let [{:keys [params uri]} req
          param-name (get params "name")
          email (get params "email")
          password (get params "password")]
          ;req-type (if (= uri "/get-submit") "GET" "POST")]
        (println "my display-resultV2" "params" params "req" req)
        (html [:div
               [:p "email youve submitted" email]
               [:br]
               [:p "password" password]])))



(defn not-found1 []
    "<h1>404 Error!</h1>
     <b>Page not found!</b>
     <p><a href='..'>Return to main page</p>")

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

(defn main [req]
   "<div>
      <h1>Hello Web Page with Routing!</h1>
       <p>What would you like to do?</p>
       <p><a href='./get-form.html'>Submit a GET request</a></p>
       <p><a href='./post-form.html'>Submit a POST request</a></p>
   </div>")


(defn loginV2
  []
  (html [:div
          (form/form-to [:post "get-login-info"]
              [:label {:for "email"} "Email:"]
              [:br]
              [:input {:type "text" :id "email" :name "email" :placeholder "email"}]
              [:br]
              [:label {:for "password"} "Password:"]
              [:br]
              [:input {:type "text" :id "password" :name "password" :placeholder "password"}]
              [:br]
              [:br]
              [:input {:type "submit" :value "Submit"}])]))

(defroutes routes
  (GET "/" req (main req))
  (GET "/get-form.html" req (get-form req))
  (GET "/post-form.html" req (post-form req))
  (GET "/get-submit" req (display-result req))
  (GET "/login" [] (loginV2))
  (POST "/get-login-info" req (display-resultV2 req))
  (POST "/post-submit" req (display-resultV2 req))
  ;(GET "/does-user-exist?" req (does-user-exist?))
  (not-found (not-found1)))

(def app
  (-> routes
      ;logger
      ;req-res-displayer
      p/wrap-params))

(defonce server
  (run-jetty #'app {:port 8080 :join? false}))

;(server)
