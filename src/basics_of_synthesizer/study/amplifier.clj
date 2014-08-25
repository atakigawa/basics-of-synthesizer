(ns basics-of-synthesizer.study.amplifier
  (:use overtone.live))

; example$B$K$"$C$?E,Ev$J(Bpad$B2;$r<Z$j$F$-$?(B.
; $B0l1~%N%j$H$7$F$O!"(B
; $B;XDj$7$?2;Id$"$?$j$N2;$r=P$9(Bsaw$B$H!"(B
; $B$=$N%*%/%?!<%V2<$N2;$r=P$9(Bsine$B$rAH$_9g$o$;$F$$$k(B.

;$B2;NL0lDj(B
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

; ADSR$B$r$D$1$F2;NL$KJQ2=(B(sustain$B$"$j(B)
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
;;sustain$B$"$j$N(Benvelope$B$O(Bgate$B$r(B0$B$K$9$k$3$H$G%-!<$rN%$7$?$N$HF1$8$3$H$K$J$k(B
;(ctl my-pad2 :gate 0)

; ADSR$B$r$D$1$F2;NL$KJQ2=(B(sustain$B$J$7(B)
; (perc atk decay)$B$O(B(adsr atk decay 0 decay)$B$HF1$8(B
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

; $B$*$^$1$N$d$D(B
; pad2$B$G;H$C$?(Badsr$B$HF1$8$b$N$r(Blow pass filter $B$N(Bfrequency
; $B$KBP$7$F$+$1$F$_$?(B.
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
