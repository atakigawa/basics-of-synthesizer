(ns basics-of-synthesizer.study.overtone-tutorial
  (:use overtone.live))

; lein repl が動いてて(vimだったら)fireplaceが起動してれば
; このファイルの適当なコード書いてある行でcppと打つと行ごとに
; 評価できるはず.
; なお、cppは現在カーソルがある括弧のみ(最も内側)を評価するので、
; 入れ子になってるときは先頭に移動してやらないと行ごと評価できない.
;
; Connectionがないよーと言われたら、:Connectと打ってreplにつなぐ.

; (comment &args) で囲っているのは、このnamespaceを最初評価した
; 際に一斉に音が鳴るのを避けるため. fireplaceは1行ずつ評価できるので
; commentブロックに入ったままでもコマンド打てば音が鳴る. セミコロン
; コメントはさすがに無理だけど.

; (odoc hoge)という便利な関数があって、引数に取ったものの
; clojure docをみることができる
(odoc hpf)

; IMPORTANT
; 最初に覚えるべきコマンドは(stop).
; とりあえずこれ叩けば全部止まる.
(stop)

; IMPORTANT
; PCの音量は、最初かなり下げ目にした方がいい.
; 謎のファイル叩く場合も、とりあえず下げて鳴らしてみてから上げた方がいい.


; --------------------------------------------------------------

; sin波の基音440Hzのオシレータ作ってそれをsynthとして定義してfooに保存.
; とりあえずここでは、(out 0 (pan2 osc)) はdefsynthする時のお決まり
; フレーズてことで.
(def foo (synth (out 0 (pan2 (sin-osc 440)))))

; synthを評価することで、音が鳴る
; 評価するとidを返すので、それを使ってパラメータを変更したり
; 音止めたりできる
; to kill the synth or modify its parameters.
(comment
  (def id (foo))
  (kill id)
)

; 大抵、音を作るときはdefsynthかdefinstする.
; definstはdefsynthのラッパーで、outが0番でpanが真ん中として
; defsynthしてるだけ.
;
; defsynth, definstの引数には必ずデフォルト値が必要.
; 引数で指定できる値は全て跡からctl関数で動的に変更可能.

; これと
(defsynth my-sin [freq 440]
  (out 0 (pan2 (sin-osc freq))))
; これは同じ
(definst foo [freq 440] (sin-osc freq))

(comment
  (my-sin)     ; 440Hzで鳴らす
  (my-sin 220) ; 220Hzで鳴らす
  (my-sin 447) ; 447Hzで鳴らす
  (stop)
)

(comment
  (def hoge (my-sin))  ; 440Hzで鳴らしたのを持っといて
  (ctl hoge :freq 220) ; 220Hzに変える
  (ctl hoge :freq 447) ; 447Hzに変える
)

; 複数チャンネルで鳴らすのを書くのが非常に楽.
; 以下のコードで、440Hzのsinと442のsinが作られる.
(definst bar [] (sin-osc [440 442]))
; 揺れてる. 2つ鳴ってるのが分かる.
(comment
  (bar)
  (stop)
)

; overtoneの演算とかメモ
; (osc freq1)) 基音freq1のoscを作る
; (osc [freq1 freq2])) 配列に入れたfreqの数だけoscを作る
; (* ugen int) ugenの音量を変更する(1.0 = 変化なし)
; (+ ugen ugen) ugenを足しあわせて一つの音にする

; ar と kr について
; audio rateとcontrol rateの略.
; 音そのものの信号と、音をコントロール
; するための信号とで、1秒間あたりに必要な数は全然違う.
; 音そのものの信号は定義的にsample rateと同じだけ必要だが、
; 音をコントロールするための信号の分割量はもっと全然少なくて
; 問題ない. 例えば秒間128回もあれば、1/128分音符と同じだけ
; 細かくトリガを指定できることになり、もはや十分.
; というわけでovertoneの関数はname:arとname:krの両方を
; 取れるものが多い(ほかにもirとかなんかあるけど)
