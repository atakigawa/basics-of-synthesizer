(ns basics-of-synthesizer.study.oscillator
  (:use overtone.live))

; $B4pK\E*$JGH7A(B
; (saw freq)$B$@$1$G$b2;$OLD$k$,!"$3$3$G$O(Bamp-eg$B$r$+$1$F(B
; $B$]!<$s$C$F$$$&46$8$N2;$K$7$F$$$k(B.



; saw
(definst my-saw [freq 440]
  (* (env-gen (perc 0.1 1.8) :action FREE)
     (saw freq)))

; square
(definst my-square [freq 440]
   (* (env-gen (perc 0.1 1.8) :action FREE)
     (square freq)))

; pulse (pulse$B$N(Bwidth$B$,(B0.5 = square)
(definst my-pulse [freq 440 width 0.33]
   (* (env-gen (perc 0.1 1.8) :action FREE)
     (pulse freq width)))

; tri
(definst my-tri [freq 440]
   (* (env-gen (perc 0.1 1.8) :action FREE)
     (lf-tri freq)))

;sine
(definst my-sin [freq 440]
  (* (env-gen (perc 0.1 1.8) :action FREE)
     (sin-osc freq)))

; noise
; overtone$B$K$O?'$s$J<oN`$N%N%$%:$,Dj5A$5$l$F$k(B
(definst noisey []
     (* (env-gen (perc 0.1 1.8) :action FREE)
     (white-noise)))
(definst pink-noisey []
     (* (env-gen (perc 0.1 1.8) :action FREE)
     (pink-noise)))

(comment
  (my-saw)
  (my-square)
  (my-pulse 440 0.5) ;square
  (my-pulse)
  (my-tri)
  (my-sin)
  (noisey)
  (pink-noisey)
)
