(ns riemann.campfire
  "Forwards events to Campfire"
  (:use [clojure.string :only [join]])
  (:require [clj-campfire.core :as cf]))

(defn cf-settings
  "Setup settings for campfire. Required information is your api-token, ssl connection
  true or false, and your campfire sub-domain."
  [token ssl sub-domain]
  {:api-token token, :ssl ssl, :sub-domain sub-domain})

(defn room
  "Sets up the room to send events too. Pass in the settings created with cf-settings
  and the room name"
  [settings room-name]
  (cf/room-by-name settings room-name))

(defn format_string
  [e]
  (str (join " " ["HOST:" (str (:host e)) "SERVICE:" (str (:service e)) "STATE:" (str (:state e)) "DESC:" (str (:description e))]))
  )

(defn campfire
  "Creates an adaptor to forward events to campfire.
  TODO: write more here once the event formatting is better.
  TODO: tests
  Tested with:
  (streams
    (by [:host, :service]
      (let [camp (campfire \"token\", true, \"sub-domain\", \"room\")]
        camp)))"
  [token ssl sub-domain room-name]
  (fn [e]
   (let [string (str (join " " ["HOST:" (str (:host e)) "SERVICE:" (str (:service e)) "STATE:" (str (:state e)) "DESC:" (str (:description e))]))]
   (cf/message (room (cf-settings token ssl sub-domain) room-name) string))))
