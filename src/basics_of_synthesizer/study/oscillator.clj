(ns basics-of-synthesizer.study.oscillator
  (:use overtone.live))

; 基本的な波形
; (saw freq)だけでも音は鳴るが、ここではamp-egをかけて
; ぽーんっていう感じの音にしている.



; saw
(definst my-saw [freq 440]
  (* (env-gen (perc 0.1 1.8) :action FREE)
     (saw freq)))

; square
(definst my-square [freq 440]
   (* (env-gen (perc 0.1 1.8) :action FREE)
     (square freq)))

; pulse (pulseのwidthが0.5 = square)
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
; overtoneには色んな種類のノイズが定義されてる
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
