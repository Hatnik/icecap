(ns icecap.core
  (:require [caesium.core :refer [sodium-init]]
            [icecap.crypto :refer [bogus-kdf bogus-scheme]]
            [icecap.rest :as rest]
            [icecap.store.mem :refer [mem-store]]
            [org.httpkit.server :refer [run-server]]
            [taoensso.timbre :refer [info]])
  (:gen-class))

(defn -main
  "Run the (development) server."
  [& args]
  (let [dev-mode (= args ["dev"])
        components {:store (mem-store) :kdf (bogus-kdf) :scheme (bogus-scheme)}
        handler (rest/build-site components :dev-mode dev-mode)]
    (info (str "Starting, dev-mode " (if dev-mode "on" "off")))
    (info "Initializing libsodium")
    (sodium-init)
    (info "Running API server")
    (run-server handler {:port 8080})))
