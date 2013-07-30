(ns stefon.shell

  (:require [lamina.core :as lamina]
            [stefon.domain :as domain]
            [stefon.shell.functions :as functions]
            [stefon.shell.plugin :as plugin]))


;; SYSTEM structure & functions
(def  ^{:doc "In memory representation of the running system structures"}
      ^:dynamic *SYSTEM* (atom nil))

(defn create-system []

  {:posts []
   :assets []
   :tags []

   :channel-spout nil
   :channel-sink nil})

(defn start-system

  ([] (start-system (create-system)))
  ([system]

     ;; Setup the system atom & attach plugin channels
     (swap! *SYSTEM* (fn [inp] (plugin/create-plugin-system system)))

     ;; Generate Post, Asset and Tag record types

     ;; switch namespaces
     (in-ns 'stefon.shell)))

(defn stop-system []

  (swap! *SYSTEM* (fn [inp]  nil))
  (in-ns 'user))


;; SUBSCRIPTION code
(defn close-plugin-channel []
  (lamina/force-close (:channel-spout @*SYSTEM*)))

(defn attach-plugin
  "This function returns takes 1 function,
   receieve-handler: a function called when the plugin receives a message

   And returns another function,
   @returns: a function to invoke when the plugin needs to send the system a message"
  [receive-handler]

  (lamina/receive-all (:channel-spout @*SYSTEM*) receive-handler)
  (fn [^clojure.lang.PersistentHashMap event]
    (lamina/enqueue (:channel-sink @*SYSTEM*))))

(defn- publish-event
  "This function, internally, lets the core system pass messages to attached plugins"
  [^clojure.lang.PersistentHashMap event]

  (lamina/enqueue (:channel-spout @*SYSTEM*) event))


;; Posts
(defn create-post [title content created-date] (functions/create *SYSTEM* :posts 'stefon.domain.Post title content created-date))
(defn retrieve-post [ID] (functions/retrieve *SYSTEM* :posts ID))
(defn update-post [ID update-map] (functions/update *SYSTEM* :posts ID update-map))
(defn delete-post [ID] (functions/delete *SYSTEM* :posts ID))
(defn find-posts [param-map] (functions/find *SYSTEM* :posts param-map))
(defn list-posts [] (functions/list *SYSTEM* :posts))


;; Assets
(defn create-asset [asset type] (functions/create *SYSTEM* :assets 'stefon.domain.Asset asset type))
(defn retrieve-asset [ID] (functions/retrieve *SYSTEM* :assets ID))
(defn update-asset [ID update-map] (functions/update *SYSTEM* :assets ID update-map))
(defn delete-asset [ID] (functions/delete *SYSTEM* :assets ID))
(defn find-assets [param-map] (functions/find *SYSTEM* :assets param-map))
(defn list-assets [] (functions/list *SYSTEM* :assets))


;; Tags
(defn create-tag [name] (functions/create *SYSTEM* :tags 'stefon.domain.Tag name))
(defn retrieve-tag [ID] (functions/retrieve *SYSTEM* :tags ID))
(defn update-tag [ID update-map] (functions/update *SYSTEM* :tags ID update-map))
(defn delete-tag [ID] (functions/delete *SYSTEM* :tags ID))
(defn find-tags [param-map] (functions/find *SYSTEM* :tags param-map))
(defn list-tags [] (functions/list *SYSTEM* :tags))
