(ns basics-of-synthesizer.inst.sampled-flute
  (:use [overtone.core])
  (:require
   [tutorial.flute-vibrato :as vibrato]))

(definst sampled-flute-vibrato
  [note 60 level 1 rate 1 loop? 0
   attack 0 decay 1 sustain 1 release 0.1 curve -4 gate 1]
  (let [buf (index:kr (:id vibrato/index-buffer) note)
        env (env-gen (adsr attack decay sustain release level curve)
                     :gate gate
                     :action FREE)]
    (* env (scaled-play-buf 2 buf :level level :loop loop? :action FREE))))

(sampled-flute-vibrato 60)
(sampled-flute-vibrato 61)
