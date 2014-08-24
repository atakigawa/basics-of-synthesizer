(ns basics-of-synthesizer.nature.rain
  (:use [overtone.live]))

;http://en.wikibooks.org/wiki/Designing_Sound_in_SuperCollider/Rain

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
;x = {
;    var gaus, osc;
;    gaus = {WhiteNoise.ar}.dup(12).sum;
;    gaus = LPF.ar(BPF.ar(gaus, 50, 1/0.4), 500);
; 
;    // 
;    osc = SinOsc.ar(gaus.linlin(-1, 1, 40, 80)) * gaus.squared * 10;
;    osc = (osc - 0.35).max(0);
; 
;    2.do{
;        osc = HPF.ar(osc, 500);
;    };
; 
;    osc.dup
;}.play
;)
(definst raindrop-bg []
  (let [src (gray-noise:ar)
        src (-> src (hpf 1000) (hpf 1000) (lpf 6800))]
    (* 0.1 src)))
(definst raindrop []
  (let [
        ;gaus (lf-gauss:kr)
        gaus (mix (* (apply vector (repeat 12 1.0)) (white-noise:ar)))
        gaus (* gaus 3)
        gaus (-> gaus (bpf 60 (/ 1 0.4)) (lpf 500))
        src (sin-osc (lin-lin gaus -1 1 40 80))
        src (* src (squared gaus) 10)
        src (max (- src 0.35) 0)
        src (-> src (hpf 500) (hpf 500) (hpf 500))
        ]
    (* [1.0 1.0] src)))
;(raindrop)
;(raindrop-bg)
;(stop)
