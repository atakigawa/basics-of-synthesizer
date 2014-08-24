(ns basics-of-synthesizer.nature.fire-sound
  (:use [overtone.live]))

;http://en.wikibooks.org/wiki/Designing_Sound_in_SuperCollider/Fire

(defn test-play
  ([inst] (test-play inst 0.8 2))
  ([inst vol dur]
    (let
      [eg (env-gen (perc 0.001 dur) :action FREE)]
      (demo (* vol inst eg)))))

(comment
  (test-play (white-noise))
  (test-play (brown-noise))
  (test-play (pink-noise))
  (test-play (clip-noise))
  (test-play (gray-noise))
  (test-play (crackle))
  (test-play (logistic 3.8 1000))
  (test-play (lf-noise0))
  (test-play (lf-noise1))
  (test-play (lf-noise2))
  (test-play (lfd-noise0))
  (test-play (lfd-noise0))
  (test-play (hasher))
  (test-play (mantissa-mask))
)

;{WhiteNoise.ar(LFNoise2.kr(1))}.play
(comment
  (demo 8
    (let [vol-ctl (lf-noise2:kr 1)
          src (white-noise)]
      (* 0.8 src vol-ctl)))
)

;{WhiteNoise.ar(LFNoise2.kr(1).squared)}.play
(comment
  (demo 8
    (let [vol-ctl (lf-noise2:kr 1)
          vol-ctl (squared vol-ctl)
          src (white-noise)]
      (* 0.8 src vol-ctl)))
)

;{HPF.ar(WhiteNoise.ar, 1000) * LFNoise2.kr(1).squared.squared}.play
(comment
  (demo 8
    (let [vol-ctl (lf-noise2:kr 1)
          vol-ctl (-> vol-ctl squared squared)
          src (hpf (white-noise) 1000)]
      (* 0.8 src vol-ctl)))
)

;{WhiteNoise.ar * Line.ar(1, 0, 0.02, doneAction: 2)}.play
(comment
  (demo 8
    (let [eg (line:kr 1 0 0.02 FREE)
          src (white-noise)]
      (* 1.0 src eg)))
)

;{WhiteNoise.ar * EnvGen.ar(Env.perc(0, 0.02, curve: 0), Dust.kr(1))}.play
(comment
  (demo 8
    (let [eg (env-gen (perc 0.001 0.02) (dust:kr 1))
          src (white-noise)]
      (* 1.0 src eg)))
)

;(
;{
;    var trigs, durscale, son, resfreq;
;    trigs = Dust.kr(1);
;    durscale = TRand.kr(1, 1.5, trigs); // vary duration between default 20ms and 30ms
;    resfreq = TExpRand.kr(100, 1000, trigs); // different resonant frequency for each one
;    son = WhiteNoise.ar * EnvGen.ar(Env.perc(0, 0.02, curve: 0), trigs, timeScale: durscale);
;    son = son + BPF.ar(son, resfreq, 20);
;}.play
;)

;{LPF.ar(WhiteNoise.ar, 30) * 100}.play
(comment
  (demo 8
    (let [src (lpf (white-noise) 30)]
      (* 100.0 src))) ;it clips
)

;{BPF.ar(WhiteNoise.ar, 30, 0.2) * 20}.play
(comment
  (demo 8
    (let [src (bpf (white-noise) 30 0.2)]
      (* 20.0 src)))
)

;{LeakDC.ar(LeakDC.ar(BPF.ar(WhiteNoise.ar, 30, 0.2) * 50).clip2(0.9)) * 0.5}.play
(comment
  (demo 8
    (let [src (bpf (white-noise) 30 0.2)]
      (-> (* 50 src)
          (leak-dc)
          (clip2 0.9)
          (leak-dc)
          (* 0.5)
          )))
)

;(
;~firegen = {
;    var trigs, durscale, resfreq;
;    var noise, hissing, crackles, lapping;
;    // A common noise source
;    noise = WhiteNoise.ar;
;    // Hissing
;    hissing = HPF.ar(noise, 1000) * LFNoise2.kr(1).squared.squared;
;    // Crackle
;    trigs = Dust.kr(1);
;    durscale = TRand.kr(1, 1.5, trigs); // vary duration between default 20ms and 30ms
;    resfreq = TExpRand.kr(100, 1000, trigs); // different resonant frequency for each one
;    crackles = noise * EnvGen.ar(Env.perc(0, 0.02, curve: 0), trigs, timeScale: durscale);
;    crackles = crackles + BPF.ar(crackles, resfreq, 20);
;    // Flame
;    lapping = LeakDC.ar(LeakDC.ar(BPF.ar(noise, 30, 0.2) * 50).clip2(0.9)) * 0.5;
;    // Combine them:
;    ([crackles, hissing, lapping] * [0.1, 0.3, 0.6]).sum * 3
;};
;~firegen.play
;)
(defcgen firegen []
  (:ar
    (let [noise (white-noise)
          ;hissing
          vol-ctl (lf-noise2:kr 1)
          vol-ctl (-> vol-ctl (squared) (squared))
          hissing (* (hpf noise 1000) vol-ctl)
          ;crackle
          trigs (dust:kr 1)
          dur-scale (t-rand:kr 1 1.5 trigs)
          reso-freq (t-exp-rand:kr 100 1000 trigs)
          eg (env-gen (perc 0 0.02 :curve 0) trigs :time-scale dur-scale)
          crackles (* noise eg)
          crackles (+ crackles (bpf crackles reso-freq 20))
          ;flame
          lapping (->
                    (bpf noise 30, 0.2)
                    (* 50)
                    (leak-dc)
                    (clip2 0.9)
                    (leak-dc)
                    (* 0.5))
          ]
      (apply + (map * [crackles hissing lapping] [0.1 0.3 0.6]))
      ))
  )
;(demo 8 (firegen))

;(
;{
;    BPF.ar(~firegen,  600, 1/0.2) +
;    BPF.ar(~firegen, 1200, 1/0.6) +
;    BPF.ar(~firegen, 2600, 1/0.4) +
;    HPF.ar(~firegen, 1000)
;}.play
;)
(definst poly-firegen []
  (+ (bpf (firegen) 600 (/ 1 0.2))
     (bpf (firegen) 1200 (/ 1 0.6))
     (bpf (firegen) 2600 (/ 1 0.4))
     (hpf (firegen) 1000))
  )
;(poly-firegen)
;(stop)
