(ns icecap.store.riak-integration-test
  (:require [icecap.store.riak :refer :all]
            [icecap.store.test-props :refer :all]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test :refer :all]
            [clojurewerkz.welle.buckets :as wb]
            [clojurewerkz.welle.core :as wc]))

(def ^:private riak-test-url "http://localhost:8098/riak")
(def ^:private riak-test-store)

(defn connect
  [f]
  (let [conn (wc/connect riak-test-url)
        store (riak-store conn "test-bucket")]
    (try
      (wb/update conn "test-bucket" (bucket-props))
      (with-redefs-fn {#'riak-test-store store} f)
      (finally
        (wc/shutdown conn)))))

(use-fixtures :once connect)

(defspec ^:riak riak-store-roundtrip
  (roundtrip-prop riak-test-store))

(defspec ^:riak riak-store-delete
  (delete-prop riak-test-store))
