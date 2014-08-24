(ns basics-of-synthesizer.inst-test1
  (:use [overtone.live]
        [overtone.inst.drum]
        [overtone.inst.piano]
        [overtone.inst.synth]
        ))

(comment
  (kick)
  (kick2)
  (kick3 :amp 1)
  (kick4 :amp 1)
  (dub-kick :freq 250)
  (dance-kick)
  (dance-kick :fattack 0.01 :fdecay 0.1)
  (dry-kick :freq 80 :amp 0.8)
  (quick-kick :freq 50)
  (open-hat)
  (closed-hat)
  (hat-demo)
  (closed-hat2)
  (hat3)
  (soft-hat :amp 1)
  (snare :amp 1)
  (snare2 :amp 1)
  (noise-snare :amp 1)
  (tone-snare :freq 440 :amp 1)
  (tom :freq 150 :amp 1)
  (clap)
  (haziti-clap)
  (bing)
)

(comment
  (piano)
)


(defn play-for-ms [sound option ms]
  (apply sound (mapcat (fn [[k v]] [k v]) option))
   (at (+ (now) ms)
     (ctl sound :gate 0)))

(defn play-for-ms2 [sound option ms]
  (apply sound (mapcat (fn [[k v]] [k v]) option))
   (at (+ (now) ms)
     (kill sound)))

(play-for-ms simple-flute {:freq 440 :amp 1} 3000)
(play-for-ms cs80lead {:freq 440 :amp 1 :dtune 0.01} 3000)
(play-for-ms2 supersaw {:freq 440 :amp 0.6} 3000)

(demo 3 (ticker))
(demo 3 (ping))

(demo 3 (tb303)) ;??

(play-for-ms mooger {:note 64 :amp 1
                     :osc1 0 :osc1-level 0.6
                     :osc2 1 :osc2-level 0.2
                     :cutoff 1200
                     :fattack 0.01 :fdecay 0.6
                     :fsustain 0.2} 3000)

(demo 3 (rise-fall-pad))
(demo 3 (pad))
(demo 3 (overpad))

(demo 3 (buzz))

(demo 3 (bass :freq 100 :amp 1))
(play-for-ms daf-bass {:freq 80 :amp 0.6} 3000)
(demo 3 (grunge-bass :note 40 :amp 1 :dur 2 :a 0.1 :d 0.4))
(play-for-ms vintage-bass {:note 60 :velocity 120 :amp 0.6} 3000)

(demo 3 (ks1 :amp 1))
(demo 3 (ks1-demo :amp 1))
(play-for-ms2 ks-stringer {} 3000)

(play-for-ms2 fm-demo {} 3000) ;move mouse

(whoahaha :dur 3 :freq 800 :mul 1000)
(whoahaha :dur 3 :freq 800 :osc 30 :mul 80)

(play-for-ms2 bubbles {:bass-freq 70} 5000)
