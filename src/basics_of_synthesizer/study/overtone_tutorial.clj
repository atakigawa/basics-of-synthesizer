(ns basics-of-synthesizer.study.overtone-tutorial
  (:use overtone.live))

; (comment &args) で囲っているのは、このnamespaceを最初評価した
; 際に一斉に音が鳴るのを避けるため. fireplaceは1行ずつ評価できるので
; commentブロックに入ったままでもコマンド打てば音が鳴る. セミコロン
; コメントはさすがに無理だけど.

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

(my-sin)     ; 440Hzで鳴らす
(my-sin 220) ; 220Hzで鳴らす
(my-sin 447) ; 447Hzで鳴らす
(stop)

(def hoge (my-sin))  ; 440Hzで鳴らしたのを持っといて
(ctl hoge :freq 220) ; 220Hzに変える
(ctl hoge :freq 447) ; 447Hzに変える

; おまけ
; 複数チャンネルで鳴らすのを書くのが非常に楽.
; 以下のコードで、440Hzのsinと442のsinが作られる.
(definst bar [] (sin-osc [440 442]))
; 揺れてる. 2つ鳴ってるのが分かる.
(bar)
(stop)
