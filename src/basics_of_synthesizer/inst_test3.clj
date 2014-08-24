(ns basics-of-synthesizer.inst-test3
  (:use [overtone.live]))

(comment
  ;watch your mouse y! keep it low
  (demo 8
    (let [mix-val 1
          orig-sound   (* (decay2:kr (* (impulse:kr 8 0)
                                      (+ 0.3 (* -0.3 (lf-saw:kr 0.3))))
                                   0.001
                                   0.3)
                        (mix (pulse [80 81] 0.3)))
          thresh-val (mouse-x 0.1 0.5)
          sb-val     (mouse-y 1 10)
          noise-gate (compander :in orig-sound
                                :control orig-sound
                                :thresh thresh-val
                                :slope-below (mouse-y 1 10)
                                :slope-above 1
                                :clamp-time 0.01
                                :relax-time 0.1)
          trig       (impulse:kr 5)
          poll-x (poll trig thresh-val "thresh")
          poll-y (poll trig sb-val "slope-below")

          mixed (x-fade2 orig-sound noise-gate mix-val)]
      mixed))
)

(comment
  (demo 20
    (let [excitation (* (env-gen:kr
                           (perc)
                           (mouse-button:kr 0 1 0)
                           1.0 0.0 0.1 0)
                        (pink-noise))
          tension (mouse-x 0.01 0.1)
          loss (mouse-y 0.999999 0.999 EXP)]
      (membrane-circle excitation tension loss)))
)
(comment
  (demo 20
    (let [excitation (* (env-gen:kr
                           (perc)
                           (mouse-button:kr 0 1 0)
                           1.0 0.0 0.1 0)
                        (pink-noise))
          tension (mouse-x 0.01 0.1)
          loss (mouse-y 0.999999 0.999 EXP)]
      (membrane-hexagon excitation tension loss)))
)

(comment
  (demo 10
    (vosim (impulse 100) (mouse-x 440 880 1) 3 0.99))
)

(defsynth rand-vosim []
  (let [p (t-rand:ar 0.0 1.0 (impulse:ar 6))
        t (impulse:ar (* 9 (+ 1 (> p 0.95))))
        f (t-rand:ar [40.0 120.0 220.0] [440.0 990.0 880.0] t)
        n (t-rand:ar 4.0 [8.0 16.0 32.0] t)
        d (t-rand:ar [0.2 0.4 0.6] [0.6 0.8 1.0] t)
        a (t-rand:ar 0.0 [0.2 0.6 1.0] t)
        l (t-rand:ar -1.0 1.0 t)
        x (mouse-x:kr 0.25 2.0)
        y (mouse-y:kr 0.25 1.5)
        z 9.0
        x_ (* x (lin-lin (lf-noise2:kr z) -1.0 1.0 0.25 2.0))
        y_ (* y (lin-lin (lf-noise2:kr z) -1.0 1.0 0.25 2.0))]
    (out:ar 0
            (pan2:ar (* 0.5 (mix:ar (* (vosim:ar t (* f x_) n (* d y_)) a)))
                     l 1))))
(comment
  (demo 20 (rand-vosim))
)
