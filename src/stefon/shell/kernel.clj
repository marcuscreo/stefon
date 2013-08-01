(ns stefon.shell.kernel

  (:require [lamina.core :as lamina]
            [clojure.string :as string]

            [stefon.shell.plugin :as plugin]
            [stefon.shell.functions :as functions]))


;; KERNEL message handling
(defn send-message [event]
  (lamina/enqueue (:channel-spout @*SYSTEM*) event))

(defn handle-incoming-messages
  "Goes through all the keys and passes associated values to system mapped action. Event structures should look like below. Full mappings can be found in resources/config.clj.

   {:stefon.post.create {:parameters {:title \"Latest In Biotechnology\" :content \"Lorem ipsum.\" :created-date \"0000\" }}}"
  [event]

  (println (str ">> handle-incoming-messages CALLED > " event))
  (let [action-config (:action-mappings (load-file "resources/config.edn"))
        action-keys (keys action-config)

        filtered-event-keys (keys (select-keys event action-keys))]

    ;; ====
    ;; perform actions, based on keys
    (println (str ">> filtered-event-keys[" filtered-event-keys "] / action-config[" action-config "]"))
    (reduce (fn [rslt ekey]

              (let [afn (ekey action-config)
                    params (-> event ekey :parameters vals)]

                ;; execute the mapped action
                (println (str ">> execute on key[" ekey "] / payload[" `(~afn ~@params) "]"))
                (eval `(~afn ~@params) )

                ;; notify other plugins what has taken place; replacing :stefon... with :plugin...
                (send-message {(keyword (string/replace (name :stefon.post.create) #"stefon" "plugin"))
                               {:parameters (-> event ekey :parameters)}})))
            []
            filtered-event-keys)

    ;; ====
    ;; pass along any event(s) for which we do not have mappings
    (let [event-less-known-mappings (eval `(~dissoc ~event ~@action-keys))]

      (println (str ">> forwarding unknown events > " event-less-known-mappings))
      (send-message event-less-known-mappings))))



(defn attach-kernel
  "Attaches a listener / handler to an in coming lamina channel"

  ([system]
     (attach-kernel system handle-incoming-messages))

  ([system message-handler]
     (lamina/receive-all (:channel-spout system) message-handler)))



;; SYSTEM structure & functions
(def  ^{:doc "In memory representation of the running system structures"}
      ^:dynamic *SYSTEM* (atom nil))

(defn get-system [] *SYSTEM*)

(defn start-system [system kernel-handler]

  ;; Setup the system atom & attach plugin channels
  (swap! *SYSTEM* (fn [inp]

                    (let [with-plugin-system (plugin/create-plugin-system system)]
                      (attach-kernel with-plugin-system kernel-handler)
                      with-plugin-system))))


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