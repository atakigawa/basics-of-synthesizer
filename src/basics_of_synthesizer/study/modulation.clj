(ns basics-of-synthesizer.study.modulation
  (:use overtone.live))


;lfo -> oscillator
(definst lfo-osc-1 [freq 440 amp 0.8 lfo-rate 6 lfo-depth 3.5]
  (let [lfo (* lfo-depth (lf-tri lfo-rate))
        freq (+ freq lfo)
        src (sin-osc freq)]
    (* amp src)))
(comment
  (lfo-osc-1)
  (stop)
)

(definst lfo-osc-2 [freq 440 amp 0.8]
  (let [lfo (* (/ freq 7) (lf-noise0:kr 12))
        freq (+ freq lfo)
        src (sin-osc freq)]
    (* amp src)))
(comment
  (lfo-osc-2)
  (stop)
)


;lfo -> filter
(definst lfo-filter-test [freq 440 amp 0.8
                   lfo-rate 1.8 lfo-depth 200.5]
  (let [lfo (* lfo-depth (lf-tri lfo-rate))
        f-freq (+ freq )
        src (distort (pulse [freq (* 1.01 freq)] 0.66))
        src (rlpf src f-freq 0.5)]
    (* amp src)))
(comment
  (lfo-filter-test)
  (stop)
)

;lfo -> volume
(definst lfo-vol-test [freq 440 amp 0.8
                   lfo-rate 4 lfo-depth 2]
  (let [lfo (* (/ 1 lfo-depth)
               (lin-lin (lf-pulse:kr lfo-rate) 0 1 0.2 1))
        src (sin-osc freq)]
    (* amp src lfo)))
(comment
  (lfo-vol-test)
  (stop)
)


;----------------------------------------------------------
(defn filter-by-index [coll idx]
  (map (partial nth coll) idx))

(def ff-intro-notes
  (let [
        scale1 (scale :C4  :major)
        scale2 (scale :A4  :minor)
        scale3 (scale :F4  :major)
        scale4 (scale :G4  :major)
        scale5 (scale :Ab4 :major)
        scale6 (scale :Bb4 :major)
        phrase1 (concat
           (map #(vector % -24) (filter-by-index scale1 [0 1 2 4]))
           (map #(vector % -12) (filter-by-index scale1 [0 1 2 4]))
           (map #(vector %   0) (filter-by-index scale1 [0 1 2 4]))
           (map #(vector % +12) (filter-by-index scale1 [0 1 2 4]))
           (map #(vector % +12) (filter-by-index scale1 [7 4 2 1]))
           (map #(vector %   0) (filter-by-index scale1 [7 4 2 1]))
           (map #(vector % -12) (filter-by-index scale1 [7 4 2 1]))
           (map #(vector % -24) (filter-by-index scale1 [7 4 2 1]))
          )
        phrase2 (concat
          (map #(vector % -24) (filter-by-index scale2 [0 1 2 4]))
          (map #(vector % -12) (filter-by-index scale2 [0 1 2 4]))
          (map #(vector %   0) (filter-by-index scale2 [0 1 2 4]))
          (map #(vector % +12) (filter-by-index scale2 [0 1 2 4]))
          (map #(vector % +12) (filter-by-index scale2 [7 4 2 1]))
          (map #(vector %   0) (filter-by-index scale2 [7 4 2 1]))
          (map #(vector % -12) (filter-by-index scale2 [7 4 2 1]))
          (map #(vector % -24) (filter-by-index scale2 [7 4 2 1]))
          )
        phrase3 (concat
          (map #(vector % -24) (filter-by-index scale3 [0 1 2 4]))
          (map #(vector % -12) (filter-by-index scale3 [0 1 2 4]))
          (map #(vector %   0) (filter-by-index scale3 [0 1 2 4]))
          (map #(vector % +12) (filter-by-index scale3 [0 1 2 4]))
          (map #(vector % +12) (filter-by-index scale3 [7 4 2 1]))
          (map #(vector %   0) (filter-by-index scale3 [7 4 2 1]))
          (map #(vector % -12) (filter-by-index scale3 [7 4 2 1]))
          (map #(vector % -24) (filter-by-index scale3 [7 4 2 1]))
          )
        phrase4 (concat
          (map #(vector % -24) (filter-by-index scale4 [2 4]))
          (map #(vector % -12) (filter-by-index scale4 [0 1 2 4]))
          (map #(vector %   0) (filter-by-index scale4 [0 1 2 4]))
          (map #(vector % +12) (filter-by-index scale4 [0 1 2 4]))
          (map #(vector % +24) (filter-by-index scale4 [0 1 2 1 0]))
          (map #(vector % +12) (filter-by-index scale4 [4 2 1 0]))
          (map #(vector %   0) (filter-by-index scale4 [4 2 1 0]))
          (map #(vector % -12) (filter-by-index scale4 [4 2 1 0]))
          (map #(vector % -24) (filter-by-index scale4 [4]))
          )
        phrase5 (concat
          (map #(vector % -24) (filter-by-index scale5 [0 2 4 6]))
          (map #(vector % -12) (filter-by-index scale5 [0 2 4 6]))
          (map #(vector %   0) (filter-by-index scale5 [0 2 4 6]))
          (map #(vector % +12) (filter-by-index scale5 [0 2 4 6]))
          (map #(vector % +12) (filter-by-index scale5 [7 6 4 2]))
          (map #(vector %   0) (filter-by-index scale5 [7 6 4 2]))
          (map #(vector % -12) (filter-by-index scale5 [7 6 4 2]))
          (map #(vector % -24) (filter-by-index scale5 [7 6 4 2]))
          )
        phrase6 (concat
          (map #(vector % -24) (filter-by-index scale6 [0 2 4 6]))
          (map #(vector % -12) (filter-by-index scale6 [0 2 4 6]))
          (map #(vector %   0) (filter-by-index scale6 [0 2 4 6]))
          (map #(vector % +12) (filter-by-index scale6 [0 2 4 6]))
          (map #(vector % +12) (filter-by-index scale6 [7 6 4 2]))
          (map #(vector %   0) (filter-by-index scale6 [7 6 4 2]))
          (map #(vector % -12) (filter-by-index scale6 [7 6 4 2]))
          (map #(vector % -24) (filter-by-index scale6 [7 6 4 2]))
          )
        notes (concat
          phrase1 phrase2
          phrase1 phrase2
          phrase3 phrase4
          phrase5 phrase6
        )
        notes (map #(midicps (+ (get % 0) (get % 1))) notes)
        ]
    notes)
)


;lfo -> pan , osc
(defsynth ff-intro [amp 0.6 speed 6 pan-speed 0.2]
  (let [notes ff-intro-notes
        pan-lfo (sin-osc:kr [pan-speed (* 1.1 pan-speed)])
        freq (demand:kr (impulse:kr speed) 0 (dseq notes INF))
        src (lpf (lf-tri [freq (* 1.01 freq)]) (+ 60 freq))
        sig (comb-n src 0.2 0.2 2)]
    (out 0 (pan2 (* amp sig) pan-lfo))))
(comment
  (def ff (ff-intro))
  (stop)
)
