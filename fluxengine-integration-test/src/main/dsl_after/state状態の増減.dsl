state 状態の増加の検証:
  s1:
    状態の増減検証イベント: s2
  s2:
    状態の増減検証イベント: s3
  s3:
  watch(状態の増減検証イベント):
  persist("状態の増減の検証"):

state 状態の減少の検証_現在の状態あり:
  s1:
    状態の増減検証イベント: s2
  s2:
    状態の増減検証イベント: s3
  s3:
  watch(状態の増減検証イベント):
  persist("状態の増減の検証"):

state 状態の減少の検証_現在の状態なし:
  s1:
    状態の増減検証イベント: s3
  s3:
    状態の増減検証イベント: s4
  s4:
  watch(状態の増減検証イベント):
  persist("状態の増減の検証"):

event 状態の増減検証イベント:
