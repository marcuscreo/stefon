(ns stefon.shell-test

  (:require [speclj.core :refer :all]
            [stefon.shell :as shell]
            [clojure.pprint :as pprint]))



(describe "one"

          (before (shell/start-system))
          (after (shell/stop-system))


          ;; ====
          (it "Create the System"

              (let [system (shell/create-system)]

                ;; checking i) not nil, ii) keys and iii) content
                (should-not-be-nil system)

                (should-not-be-nil (some #{:posts :assets :tags} (keys system)))
                (should= {:posts [], :assets [], :tags [], :channel-spout nil, :channel-sink nil} system)))


          (it "Start the System"

              (let [result (shell/start-system)]

                (should= 'stefon.shell (ns-name *ns*))))

          (it "Stop the System"

              (let [started (shell/start-system)
                    stopped (shell/stop-system)]

                (should= 'user (ns-name *ns*)))))