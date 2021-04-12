(ns learning-compojure.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :as p]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [not-found]]
            [hiccup.core :refer :all]
            [hiccup.form :as form])
  (:gen-class))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))


(defn does-user-exist?
  []
  ;[req
  ; (let [{:keys [params]} req
  ;       email (get params "email")
  ;       password (get params "password")]
 (html [:div [:p "email" "email"]
             [:br]
             [:p "password" "password"]]))


(defn not-found1 []
    "<h1>404 Error!</h1>
     <b>Page not found!</b>
     <p><a href='..'>Return to main page</p>")


(defn get-login-info [req]
    (let [{:keys [params]} req
          param-name (get params "name")
          email (get params "email")
          password (get params "password")]
        (println "my get-login-info " "params:" params)
        (html [:div
               [:p "email youve submitted " email]
               [:br]
               [:p "password " password]])))


(defn login
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


(defn main [req]
   "<div>
      <h1>Hello Web Page with Routing!</h1>
       <p>What would you like to do?</p>
       <p><a href='./get-form.html'>Submit a GET request</a></p>
       <p><a href='./post-form.html'>Submit a POST request</a></p>
   </div>")


(defroutes routes
  (GET "/" [req] (main req))
  (GET "/login" [] (login))
  (POST "/get-login-info" [req] (get-login-info req))
  ;(GET "/does-user-exist?" req (does-user-exist?))
  (not-found (not-found1)))


(def app
  (-> routes
      p/wrap-params))


(defonce server
  (run-jetty #'app {:port 8080 :join? false}))

;(server)
