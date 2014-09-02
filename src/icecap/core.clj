(ns icecap.core
  (:require [icecap.rest :as rest]
            [icecap.crypto :refer [bogus-kdf bogus-scheme]]
            [icecap.store.mem :refer [mem-store]]
            [org.httpkit.server :refer [run-server]])
  (:gen-class))

(defn -main
  "Run the (development) server."
  [& args]
  (let [dev-mode (= args ["dev"])
        components {:store (mem-store) :kdf (bogus-kdf) :scheme (bogus-scheme)}
        handler (rest/build-site components :dev-mode dev-mode)]
    (run-server handler {:port 8080})))
