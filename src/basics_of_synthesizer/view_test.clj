(ns basics-of-synthesizer.view-test
  (:use [incanter core stats charts]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(comment
  (view (histogram (sample-normal 1000)))
  (view (function-plot sin -10 10))
  )
