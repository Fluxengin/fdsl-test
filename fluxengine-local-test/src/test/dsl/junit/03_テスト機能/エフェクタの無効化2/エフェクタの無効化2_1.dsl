runner:
  effect: off

test 1 effectorは発火しない:
  2019/10/23 00:00:01:
    ev1:
      dummy: "dummy"
    inspect:
      log != "メッセージ = エフェクタの無効化#ef2 が発火しました1":