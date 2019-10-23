runner:
  effect: off

test 1 effectorは発火しない:
  2019/10/21 00:00:01:
    ev1:
      dummy: "dummy"
    inspect:
      log != "メッセージ = エフェクタの無効化#ef1 が発火しました":