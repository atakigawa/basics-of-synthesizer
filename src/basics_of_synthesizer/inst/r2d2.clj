(ns basics-of-synthesizer.inst.r2d2
  (:use [overtone.live]))

;http://en.wikibooks.org/wiki/Designing_Sound_in_SuperCollider/R2D2

;(
;SynthDef(\fmSynth, { |carrierFreq=100, carrierLvl=0.5, modulatorRatio=5, modulatorIndex=1.5, outputAmp=0.2, sig, out=0|
; 
;    // the simple FM core
;    sig = LFSaw.ar(carrierFreq, 1, 0.5, 0.5) * carrierLvl;
;    sig = sig + SinOsc.ar(carrierFreq * modulatorRatio) * modulatorIndex;
;    sig = cos( sig * 2pi) * outputAmp * 0.06;
;    Out.ar(out, sig);
;},1!5).add;
;)
; 
;// At first start the synth:
;g = Synth(\fmSynth);
; 
;// Play with the parameters:
;g.set(\carrierFreq, 800);
;g.set(\carrierFreq, 50);
;g.set(\carrierFreq, 100, \modulatorRatio, 5, \modulatorIndex, 0.5);
;g.set(\carrierFreq, 40, \modulatorRatio, 7, \modulatorIndex, 1.5);
;g.set(\carrierFreq, 955, \carrierLvl, 0.4, \modulatorRatio, 3, \modulatorIndex, 4);
;// ... etc.
; 
(defsynth fm-synth [carrier-freq 100
                    carrier-lvl 0.5
                    mod-ratio 5
                    mod-index 1.5
                    amp 0.5
                    bus 0]
  (let [carrier (* (mul-add (sin-osc carrier-freq) 0.5 0.5)
                   carrier-lvl)
        mod-fm  (* (sin-osc (* carrier-freq mod-ratio))
                   mod-index)
        sig     (+ carrier mod-fm)
        sig     (cos (* sig Math/PI 2))]
    (out bus (* amp sig))))

(defsynth fm-synth2 [carrier-freq 100
                    mod-amt 3
                    mod-depth 1.5
                    amp 0.5
                    atk-amp 0.4
                    bus 0]
  (let [mod-eg    (env-gen (envelope
                             [0 0.1 8 19 37] [0.01 0.8 0.6 0.6]))
        amp-eg    (env-gen (envelope
                             [0.01 0.7 1 1 0] [0.01 0.8 0.6 0.6])
                              :action FREE)
        mod-freq  (* mod-eg
                     (* carrier-freq mod-depth)
                     (sin-osc (/ carrier-freq (reciprocal mod-amt))))
        sig     (sin-osc (+ carrier-freq mod-freq))
        sig     (* sig amp-eg)
        atk-env (env-gen (perc))
        atk-osc (sin-osc [(* 1.2 carrier-freq) (* 2 carrier-freq)])
        atk-sig (* atk-osc atk-env atk-amp)
        ]
    (out bus (* amp (+ sig atk-sig)))))

(comment
  (def g (fm-synth))
  (ctl g :carrier-freq 800)
  (ctl g :carrier-freq 50)
  (ctl g :carrier-freq 100 :mod-ratio 5 :mod-index 0.5)
  (ctl g :carrier-freq 100 :mod-ratio 5 :mod-index 0.0)
  (ctl g :carrier-freq 40 :mod-ratio 7 :mod-index 1.5)
  (ctl g :carrier-freq 955 :carrier-lvl 0.4 :mod-ratio 3 :mod-index 4)
  (stop)

  (fm-synth2)
  (fm-synth2 :carrier-freq 200 :mod-amt 5)
  (fm-synth2 :carrier-freq 80 :mod-amt 5)
  (fm-synth2 :carrier-freq 80 :mod-amt 5 :mod-depth 8.5)
  (fm-synth2 :carrier-freq 80 :mod-amt 3 :mod-depth 8.5)
  (fm-synth2 :carrier-freq 80 :mod-amt 1 :mod-depth 32 :amp 0.4)
  (stop)
)

(definst r2d2 [period 0]
  (let [
      period (-> period (* 600) (+ 100))
      change (impulse:kr (local-in:kr 1))
      impulse-trigs [5 8 8.5 7.5 9 8.2 5.8 10]
      rate (demand:kr (coin-gate:kr (/ 1 3) change) 0 (drand impulse-trigs INF))
      _l-out (local-out:kr rate)

      change-half (coin-gate:kr (/ 1 2) change)
      change-third (coin-gate:kr (/ 1 3) change)
      change-fourth (coin-gate:kr (/ 1 4) change)

      carrier-freq (t-rand:kr 0 1000 change)
      cf-ramp (t-rand:kr 0 1 change)

      carrier-lvl (t-rand:kr 0 9000 change-third)
      cl-ramp (t-rand:kr 0 1 change-third)

      mod-ratio (t-rand:kr 0 800 change-fourth)
      mr-ramp (t-rand:kr 0 1 change-fourth)

      mod-index (t-rand:kr 0 100 change-fourth)
      mi-ramp (t-rand:kr 0 1 change-fourth)

      amp (t-rand:kr 0 1 change-half)
      ap-ramp (t-rand:kr 0 1 change-half)

      carrier-freq (* 0.6 (ramp (/ carrier-freq 1000)
                                (-> cf-ramp (* period) (/ 1000))))
      carrier-lvl  (+ 100 (ramp carrier-lvl
                                (-> cl-ramp (* period) (/ 1000))))
      mod-ratio    (+ 20 (ramp mod-ratio
                                (-> mr-ramp (* period) (/ 1000))))
      mod-index    (+ 0.2 (ramp (/ mod-index 200)
                                (-> mi-ramp (* period) (/ 1000))))
      amp          (+ 0.2 (ramp amp
                                (-> ap-ramp (* period) (+ 3) (/ 1000))))

      carrier (* (mul-add (lf-saw:ar carrier-freq 1) 0.5 0.5)
                 carrier-lvl)
      mod-fm  (* (sin-osc (* carrier-freq mod-ratio))
                 mod-index)
      sig     (+ carrier mod-fm)
      sig     (* (cos (* sig Math/PI 2)) amp)

      tmp1 (exp (* (* -2 Math/PI) (* 10000 (sample-dur))))
      tmp2 (exp (* (* -2 Math/PI) (* 100 (sample-dur))))
      sig (-> sig (one-pole tmp1) (one-pole tmp1))
      sig (- sig (one-pole sig tmp2))
      sig (- sig (one-pole sig tmp2))
    ]
    [sig sig])
  )
(comment
  (def my-r2 (r2d2))
  (ctl my-r2 :period 0.3)
  (ctl my-r2 :period 0.5)
  (ctl my-r2 :period 0.7)
  (stop)
)
