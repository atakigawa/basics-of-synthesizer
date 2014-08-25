(ns basics-of-synthesizer.study.filter
  (:use overtone.live))


;lpf
(definst lpf-test [freq 440 amp 0.8
                   f-freq-s 200 f-freq-e 1600 f-freq-dur 3]
  (let [f-freq (lag (line:kr f-freq-s f-freq-e f-freq-dur))
        src (saw [freq (* 1.01 freq)])
        src2 (* 0.6 (sin-osc (/ freq 2)))
        src (+ src src2)
        src (lpf src f-freq)]
    (* amp src)))
(comment
  (lpf-test)
  (stop)
)


;hpf
(definst hpf-test [freq 440 amp 0.8
                   f-freq-s 1600 f-freq-e 200 f-freq-dur 3]
  (let [f-freq (lag (line:kr f-freq-s f-freq-e f-freq-dur))
        src (saw [freq (* 1.01 freq)])
        src2 (* 0.6 (sin-osc (/ freq 2)))
        src (+ src src2)
        src (hpf src f-freq)]
    (* amp src)))
(comment
  (hpf-test)
  (stop)
)

;bpf
(definst bpf-test [freq 440 amp 0.8
                   f-freq-s 200 f-freq-e 1600 f-freq-dur 3]
  (let [f-freq (lag (line:kr f-freq-s f-freq-e f-freq-dur))
        src (saw [freq (* 1.01 freq)])
        src2 (* 0.6 (sin-osc (/ freq 2)))
        src (+ src src2)
        src (bpf src f-freq)]
    (* amp src)))
(comment
  (bpf-test)
  (stop)
)

;notch ()
(definst notch-test [freq 440 amp 0.8
                   f-freq-s 100 f-freq-e 1200 f-freq-dur 3]
  (let [f-freq (lag (line:kr f-freq-s f-freq-e f-freq-dur))
        src (saw [freq (* 1.01 freq)])
        src2 (* 0.6 (sin-osc (/ freq 2)))
        src (+ src src2)
        src (brf src f-freq 2)]
    (* amp src)))
(comment
  (notch-test)
  (stop)
)

;resonance
(definst rlpf-test [freq 440 amp 0.8 f-freq 440
                   reso-s 2 reso-e 0.2 reso-dur 3]
  (let [reso (lag (line:kr reso-s reso-e reso-dur))
        src (saw [freq (* 1.01 freq)])
        src2 (* 0.6 (sin-osc (/ freq 2)))
        src (+ src src2)
        src (rlpf src f-freq reso)]
    (* amp src)))
(comment
  (rlpf-test)
  (stop)
)
