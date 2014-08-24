(ns basics-of-synthesizer.sample1
  (:use [overtone.core]
        [overtone.live]))

;https://github.com/overtone/overtone/wiki/Getting-Started
(odoc saw)
(odoc line)

(definst foo [] (saw [99 100 102]))
(definst quux [freq 440] (* 0.2 (saw freq)))
(definst trem [freq 400 depth 10 rate 6 length 3]
  (* 0.3
     (line:kr 0 1 length FREE)
     (saw (+ freq (* depth (sin-osc:kr rate))))))

(foo)
(kill foo) ;stop all foos

(quux)
(ctl quux :freq 200)

(trem)
(trem 200 60 0.8)
(trem 60 30 0.2)

(stop) ;stop all

;--------------------------------------
;https://github.com/overtone/overtone/wiki/Oscillators

(odoc env-gen)
(odoc lin)
(odoc adsr)
(odoc lf-pulse)

(definst sin-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (sin-osc freq)
     vol))
(sin-wave)

(definst sin-wave2 [freq 440 attack 0.01 decay 0.6 sustain 0.0 release 0.4 vol 0.4]
  (* (env-gen (adsr attack decay sustain release) 1 1 0 1 FREE)
     (sin-osc freq)
     vol))
(sin-wave2)

(definst saw-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (saw freq)
     vol))
(saw-wave)

(definst square-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (lf-pulse:ar freq)
     vol))
(square-wave)

(definst noisey [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (pink-noise) ; also have (white-noise) and others...
     vol))
(noisey)

(definst triangle-wave [freq 440 attack 0.01 sustain 0.1 release 0.4 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (lf-tri freq)
     vol))
(triangle-wave)

(definst spooky-house [freq 440 width 0.2 attack 0.3 sustain 4 release 0.3 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (sin-osc (+ freq (* 20 (lf-pulse:kr 0.5 0 0.2))))
     vol))
(spooky-house)

;--------------------------------------
;https://github.com/overtone/overtone/wiki/Filters

(odoc lpf)
(odoc hpf)
(odoc bpf)
(odoc mouse-x)
(odoc pluck)
(odoc perc)

;; low-pass; move the mouse left and right to change the threshold frequency
(demo 10 (lpf (saw 100) (mouse-x 40 5000 EXP)))

;; high-pass; move the mouse left and right to change the threshold frequency
(demo 10 (hpf (saw 100) (mouse-x 40 5000 EXP)))

;; band-pass; move mouse left/right to change threshold frequency; up/down to change bandwidth (top is narrowest)
(demo 10 (bpf (saw 100) (mouse-x 40 5000 EXP) (mouse-y 0.01 1 LIN)))



(demo (* (sin-osc) (env-gen (perc 0.001 2) :action FREE) 0.4))
(demo (* (white-noise) (env-gen (perc 0.001 2) :action FREE) 0.4))

;; here we generate a pulse of white noise, and pass it through a pluck filter
;; with a delay based on the given frequency
(let [freq 220]
   (demo (pluck (* (white-noise) (env-gen (perc 0.001 2) :action FREE)) 1 3 (/ 1 freq))))

;----------------------------------------
;https://github.com/overtone/overtone/wiki/Buffers-and-audio-files

(def SOME-SAMPLES
  {406   :click      436   :ride           777   :kick         802 :close-hat
   2086  :kick2      8323  :powerwords     9088  :jetbike      13254 :cymbal
   16309 :open-snare 16568 :two-cows       25649 :subby        26657 :open-hat
   26903 :snare      30628 :steam-whistles 33637 :boom         44293 :sleigh-bells
   48310 :clap       50623 :water-drops    80187 :witch-cackle 80401 :explosion
   87731 :snap  })

(def prepare-sample-sounds
  (doseq [[k v] SOME-SAMPLES]
    (intern *ns* (symbol (name v)) (freesound k)))
  )

(click)
(ride)
(kick)
(kick2)
(subby)
(close-hat)
(open-hat)
(cymbal)
(snare)
(open-snare)
(clap)
(snap)
(powerwords)
(jetbike)
(two-cows)
(steam-whistles)
(boom)
(sleigh-bells)
(water-drops)
(witch-cackle)
(explosion)

;----------------------------------------
;http://cjelupton.wordpress.com/2014/07/15/adventures-in-clojure/
(odoc mix)
(odoc lpf)
(odoc lin-lin)
(odoc lin-exp)

(demo 3 (saw [50 (line:kr 100 1600 5) 101 100.5]))
(demo 3 (mix (saw [50 (line:kr 100 1600 5) 101 100.5])))
(demo 8 (lf-tri (line 2 20 5))) ; so low barely audible

(demo 7 (lpf (mix (saw [50 (line 100 1600 5) 101 100.5]))
             (lin-lin (lf-tri (line 2 20 5)) -1 1 400 4000)))

;----------------------------------------
;https://github.com/overtone/overtone/wiki/Live-coding
(odoc compander)
(odoc pulse)

(definst kick [freq 120 dur 0.3 width 0.5]
  (let [freq-env (* freq (env-gen (perc 0 (* 0.99 dur))))
        env (env-gen (perc 0.01 dur) 1 1 0 1 FREE)
        sqr (* (env-gen (perc 0 0.01)) (pulse (* 2 freq) width))
        src (sin-osc freq-env)
        drum (+ sqr (* env src))]
    (compander drum drum 0.2 1 0.1 0.01 0.01)))
(kick)

(definst c-hat [amp 0.8 t 0.04]
  (let [env (env-gen (perc 0.001 t) 1 1 0 1 FREE)
        noise (white-noise)
        sqr (* (env-gen (perc 0.01 0.04)) (pulse 880 0.2))
        filt (bpf (+ sqr noise) 9000 0.5)]
    (* amp env filt)))
(c-hat)

(definst buga [freq 30 dur 0.6 width 0.5]
  (let [
        src (white-noise)
        src2 (saw [freq (* 1.1 freq) (* 0.9 freq)])
        env (env-gen (perc 0.01 dur) 1 1 0 1 FREE)
        snd (* src env)
        snd2 (* src2 env)
        snd (+ snd snd2)

        trig  (impulse:kr (/ 1 (metro-bpm metro)))
        swr   (demand trig 0 (dseq [13] INF))
        sweep (lin-exp (lf-saw swr) -1 1 20 1000)

        snd (lpf snd sweep)
        snd (* 0.4 (normalizer snd))
       ]
    snd))
(buga)

(def metro (metronome 128))
(metro) ; => current beat number
(metro 100) ; => timestamp of 100th beat


(defn player [beat]
  (at (metro (+ 0 beat)) (kick))
  (at (metro (+ 0.5 beat)) (c-hat))
  (at (metro (+ 1 beat)) (kick))
  (at (metro (+ 1.5 beat)) (c-hat))
  (at (metro (+ 2 beat)) (kick))
  (at (metro (+ 2.5 beat)) (c-hat))
  (at (metro (+ 3 beat)) (kick))
  (at (metro (+ 3.5 beat)) (kick))
  (at (metro (+ 3.5 beat)) (buga))
  (at (metro (+ 3.5 beat)) (c-hat))
  (apply-by (metro (+ 4 beat)) #'player (+ 4 beat) []))

(player (metro))
(stop)

(metro-bpm metro 160) ; Change the playback speed by sending a message to metro like this
