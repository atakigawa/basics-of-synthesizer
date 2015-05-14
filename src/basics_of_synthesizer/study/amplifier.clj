(ns basics-of-synthesizer.study.amplifier
  (:use overtone.live))

; exampleにあった適当なpad音を借りてきた.
; 一応ノリとしては、
; 指定した音符あたりの音を出すsawと、
; そのオクターブ下の音を出すsineを組み合わせている.

;音量一定
(definst pad1 [note 60 amp 0.7 attack 0.001 release 2]
  (let [freq  (midicps note)
        f-eg (+ freq
                (* 3 freq (env-gen (perc 0.012 (- release 0.1)))))
        bfreq (/ freq 2)
        sub-src (* 0.7 (sin-osc [bfreq (* 0.99 bfreq)]))
        src (lpf (saw [freq (* freq 1.01)]) f-eg)
        sig (+ src sub-src)]
    (* amp sig)))
;(pad1)
;(stop)

; ADSRをつけて音量に変化(sustainあり)
(definst pad2 [note 60 amp 0.7
               attack 1 decay 1 sustain 0.3 release 2
               gate 1]
  (let [freq  (midicps note)
        my-adsr (adsr attack decay sustain release)
        amp-eg (env-gen my-adsr gate :action FREE)
        f-eg (+ freq
                (* 3 freq (env-gen (perc 0.012 (- release 0.1)))))
        bfreq (/ freq 2)
        sub-src (* 0.7 (sin-osc [bfreq (* 0.99 bfreq)]))
        src (lpf (saw [freq (* freq 1.01)]) f-eg)
        sig (+ src sub-src)]
    (* amp amp-eg sig)))
;(def my-pad2 (pad2))
;;sustainありのenvelopeはgateを0にすることでキーを離したのと同じことになる
;(ctl my-pad2 :gate 0)

; ADSRをつけて音量に変化(sustainなし)
; (perc atk decay)は(adsr atk decay 0 decay)と同じ
(definst pad3 [note 60 amp 0.7 attack 0.001 release 2]
  (let [freq  (midicps note)
        amp-eg (env-gen (perc attack release) :action FREE)
        f-eg (+ freq
                (* 3 freq (env-gen (perc 0.012 (- release 0.1)))))
        bfreq (/ freq 2)
        sub-src (* 0.7 (sin-osc [bfreq (* 0.99 bfreq)]))
        src (lpf (saw [freq (* freq 1.01)]) f-eg)
        sig (+ src sub-src)]
    (* amp amp-eg sig)))
;(pad3)

; おまけのやつ
; pad2で使ったadsrと同じものをlow pass filter のfrequency
; に対してかけてみた.
(definst pad4 [note 60 amp 0.7
               attack 3 decay 0.3 sustain 0.3 release 2
               f-eg-gate 1]
  (let [freq  (midicps note)
        my-adsr (adsr attack decay sustain release)
        f-eg (+ freq
                (* 10 freq (env-gen my-adsr f-eg-gate)))
        bfreq (/ freq 2)
        sig   (+ (* 0.7 (sin-osc [bfreq (* 0.99 bfreq)]))
                 (rlpf (saw [freq (* freq 1.01)]) f-eg 0.4))]
    (* amp sig)))
(def my-pad4 (pad4))
(ctl my-pad4 :f-eg-gate 0)
(stop)
