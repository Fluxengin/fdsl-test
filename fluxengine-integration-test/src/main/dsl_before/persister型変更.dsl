persister 型変更の検証_期限切れ:
  contents1: string
  persist("persister型変更の検証"):
    lifetime: now()

persister 型変更の検証_期限内:
  contents2: date
  persist("persister型変更の検証"):
    lifetime: today()

persister 型変更の検証_計算可能1:
  contents3: string
  persist("persister型変更の検証"):
    lifetime: today()

event 型変更の検証イベント:
  dummy: string

persist 型変更の検証_期限切れ:
  contents1: "型変更の検証_期限切れ_before"
  watch(型変更の検証イベント):

persist 型変更の検証_期限内:
  contents2: today()
  watch(型変更の検証イベント):

persist 型変更の検証_計算可能1:
  contents3: "123"
  watch(型変更の検証イベント):
