(ns basics-of-synthesizer.nature.bubbles
  (:use [overtone.live])
  (:require [clojure.contrib.math]))

;http://en.wikibooks.org/wiki/Designing_Sound_in_SuperCollider/Bubbles

(defn test-play
  ([inst] (test-play inst 0.8 2))
  ([inst vol dur]
    (let
      [eg (env-gen (perc 0.001 dur) :action FREE)]
      (demo (* vol inst eg)))))

(comment
  (test-play (white-noise))
)

;(
;SynthDef(\bubbletrigs, {|out=0, probability=0.5|
;    var trigs, buf, a;
;    // These two lines create a loop of zeroes 
;    // with some ones (i.e. triggers) placed at prime-number locations
;    a = {0}.dup(200);
;    [29, 37, 47, 67, 89, 113, 157, 197].do{|val| a[val] = 1};
;    buf = a.as(LocalBuf);
;    // playbuf by default will use the server's rate, but we want one item every 15ms
;    trigs = PlayBuf.kr(1, buf, 0.015.reciprocal / (s.sampleRate / s.options.blockSize), loop: 1);
;    // Randomly discard half of them, to remove too much obvious looping
;    trigs = CoinGate.kr(probability, trigs);
;    // Let's poll to watch the events appearing
;    trigs.poll(trigs);
;    Out.kr(out, trigs);
;}).store
;)
(defsynth bubble-trigs [bus 0 probability 0.5]
  (let [a (into [] (repeat 200 0))
        a (apply assoc a (mapcat #(vector % 1)
                                 [29 37 47 67 89 113 157 197]))
        trigs (duty:kr 0.020 0 (dseq a INF))
        trigs (coin-gate probability trigs)
        _poll (poll trigs trigs "bubble poll:")
        ]
    (out:kr bus trigs)))
;(bubble-trigs)
;(stop)

;(
;SynthDef(\bubblebub, {  |out=0, t_trig=0, attack=0.01, decay=0.08, pitchcurvelen=0.1, freq=1000, doneAction=0, amp=0.1|
;    var pitch, son;
;    amp   = amp * EnvGen.ar(Env.perc(attack, decay).delay(0.003), t_trig, doneAction: doneAction);
;    pitch = freq * EnvGen.ar(Env.new([0,0,1],[0,1]).exprange(1, 2.718), t_trig, timeScale: pitchcurvelen);
;    son = SinOsc.ar(pitch);
;    // high-pass to remove any lowpitched artifacts, scale amplitude
;    son = HPF.ar(son, 500) * amp * 10;
;    Out.ar(out, son);
;}).store
;)
(definst bubble-bub [t-trig 0 atk 0.01 decay 0.08 pitch-curve-len 0.1 freq 1000 done-action NO-ACTION amp 0.1]
  (let [
        amp-eg (lag (env-gen (perc atk decay) t-trig)
                    0.03)
        amp (* amp amp-eg)
        freq-eg (lin-exp (env-gen (envelope [0 0 1] [0 1])
                         t-trig :time-scale pitch-curve-len)
                         0 1 1 2.718)
        freq (* freq freq-eg)
        src (hpf (sin-osc freq) 500)]
    (* amp 10 src)))
;(bubble-bub) ; no sound
;(ctl bubble-bub :t-trig 1) ; a sound
;(bubble-bub 1 :freq 2000 :decay 0.005 :amp 0.01) ; a sound
;(stop)


;This next one's a bit more complex - we do as the book does and make smaller bubbles have (a) higher pitch (b) lower volume (c) shorter duration. To connect these values together we define a "sizefactor" and use Pkey to reuse it in each of the args.
;(
;p = Pbind(
;    \instrument, \bubblebub,
;    \sizefactor, Pwhite(0,1,inf),
;    \dur, Pgauss(0.3, 0.2),
;    \freq,  Pkey(\sizefactor).linexp(0, 1, 1000, 3000),
;    \amp ,  Pkey(\sizefactor).linlin(0, 1, 0.15, 0.04), 
;    \decay, Pkey(\sizefactor).linlin(0, 1, 0.05, 0.08), 
;    \doneAction, 2
;).play
;)

(defn my-lin-lin [in srclo srchi dstlo dsthi]
  (let [relative-pos (/ (- in srclo) (- srchi srclo))
        new-scale (- dsthi dstlo)]
    (+ (* relative-pos new-scale) dstlo)))

(defn my-lin-exp [in srclo srchi dstlo dsthi]
  (let [base  (/ dsthi dstlo)
        power (/ (- in srclo) (- srchi srclo))]
    (* (clojure.contrib.math/expt base power) dstlo)))

(defsynth my-trig2 [t-id 0]
  (let [trigger (dust:kr 1)
        value (t-rand:kr 0 1 trigger)]
    (send-trig trigger t-id value)))

(defonce uid (trig-id))
(defn play-bubble [value]
  (let [freq  (my-lin-exp value 0 1 700 2000)
        amp   (my-lin-lin value 0 1 0.08 0.01)
        decay (my-lin-lin value 0 1 0.03 0.005)]
    (bubble-bub 1 :freq freq :decay decay :amp amp :done-action FREE)
    ))
;;(on-latest-trigger uid #(println "trigger: " %) ::my-trig2)
;(on-latest-trigger uid play-bubble ::my-trig2)
;(my-trig2 uid)
;(stop)
;(remove-event-handler ::my-trig2)
