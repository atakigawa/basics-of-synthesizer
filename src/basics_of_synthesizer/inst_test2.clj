(ns basics-of-synthesizer.inst-test2
  (:use [overtone.live]
        [overtone.synth.sts])
  (:require [overtone.synth.ixi])
)

(comment
  (overtone.synth.ixi/kick :amp 0.5)
  (overtone.synth.ixi/kick2 :amp 0.5)
  (overtone.synth.ixi/kick3 :amp 1)
  (overtone.synth.ixi/snare :amp 0.4 :snare-level 98 :snare-tightness 2200 :sustain 0.5)

  (do
    (prophet :freq (midi->hz (note :C3)) :decay 5)
    (prophet :freq (midi->hz (note :E3)) :decay 5)
    (prophet :freq (midi->hz (note :F#3)) :decay 5)
    (prophet :freq (midi->hz (note :B4)) :decay 5)
  )
)


(defn play-for-ms [sound option ms]
  (apply sound (mapcat (fn [[k v]] [k v]) option))
   (at (+ (now) ms)
     (ctl sound :gate 0)))

(defn play-for-ms2 [sound option ms]
  (apply sound (mapcat (fn [[k v]] [k v]) option))
   (at (+ (now) ms)
     (kill sound)))

