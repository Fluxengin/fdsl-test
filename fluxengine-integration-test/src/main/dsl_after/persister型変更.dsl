persister 型変更の検証_期限切れ:
  contents1: number
  persist("persister型変更の検証"):
    lifetime: now()

persister 型変更の検証_期限内:
  contents2: string
  persist("persister型変更の検証"):
    lifetime: today()

persister 型変更の検証_期限内_error:
  contents2_error: string
  persist("persister型変更の検証"):
    lifetime: today()

persister 型変更の検証_計算可能1:
  contents3: number
  persist("persister型変更の検証"):
    lifetime: today()

event 型変更の検証イベント:
  dummy: string

# イベントを分けてみる
event 型変更の検証イベント2:
  dummy: string

event 型変更の検証イベント2_error1:
  dummy: string

number n1: 型変更の検証_期限切れ.contents1 + 1

persist 型変更の検証_期限切れ:
  contents1: n1
  watch(型変更の検証イベント):

string s2: ユーティリティ.concat(型変更の検証_期限内.contents2, "_after")

persist 型変更の検証_期限内:
  contents2: s2
  watch(型変更の検証イベント2):

string s2_error: ユーティリティ.concatstring(型変更の検証_期限内_error.contents2_error, "_after")

persist 型変更の検証_期限内_error:
  contents2_error: s2_error
  watch(型変更の検証イベント2_error1):

number n3: 型変更の検証_計算可能1.contents3 * 3

persist 型変更の検証_計算可能1:
  contents3: n3
  watch(型変更の検証イベント):
