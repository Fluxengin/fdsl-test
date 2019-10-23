event ev1:
  dummy: string

effector ef2:
  message: string

effect ef2:
  message: "エフェクタの無効化#ef2 が発火しました2"
  watch(ev1):
