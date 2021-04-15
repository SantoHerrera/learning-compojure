(ns learning-compojure.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :as p]
            [ring.util.response :as r]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [not-found]]
            [hiccup.core :refer :all]
            [hiccup.form :as form])
  (:gen-class))

;https://gist.github.com/zehnpaard/665edf183818b4df707b5f0535ecdc0c
;zip where template came from. couldnt get it to work, so made new project with a lot of same code.
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def users {:email "user1"
            :password "fuck"})


(defn does-user-exist?
  [data email password]
  (and (= email (:email data))
       (= password (:password data))))



(defn not-found1 []
    "<h1>404 Error!</h1>
     <b>Page not found!</b>
     <p><a href='..'>Return to main page</p>")

(defn does-exist []
  (html [:h1 "does fucking exits"]))

(defn doesnt-exist []
  (html [:h1 "doesnt exist"]))

(defn test1 [boolean]
  (html [:h1 boolean "nice"]))


(defn get-login-info [req]
    (let [{:keys [params]} req
          param-name (get params "name")
          email (get params "email")
          password (get params "password")
          user-exists (does-user-exist? users email password)]
        (html [:h1 (str user-exists email password) " lost"])))


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
       <p><a href='./login'>login</a></p>
   </div>")


(defroutes routes
  (GET "/" [req] (main req))
  (GET "/login" [] (login))
  (GET "/doesnt-exist" [] (doesnt-exist))
  (GET "/does-exist" [] (does-exist))
  ;(GET "/test" (test1))
  (POST "/get-login-info" req (get-login-info req))
  ;(GET "/does-user-exist?" req (does-user-exist?))
  (not-found (not-found1)))


(def app
  (-> routes
      p/wrap-params))


(defonce server
  (run-jetty #'app {:port 8080 :join? false}))

;(server)
