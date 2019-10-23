event ev1:
  dummy: string

effector ef1:
  message: string

effect ef1:
  message: "エフェクタの無効化#ef1 が発火しました"
  watch(ev1):
