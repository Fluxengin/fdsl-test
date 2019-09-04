persister 型変更の検証_期限切れ:
  contents1: number
  persist("persister型変更の検証"):
    lifetime: now()

persister 型変更の検証_期限内:
  contents2:
    string_value: string
    number_value: number
  persist("persister型変更の検証"):
    lifetime: today()

persister 型変更の検証_計算可能1:
  contents3: number
  persist("persister型変更の検証"):
    lifetime: today()

event 型変更の検証イベント:
  dummy: string

number n1: 型変更の検証_期限切れ.contents1 + 1

persist 型変更の検証_期限切れ:
  contents1: n1
  watch(型変更の検証イベント):

string s2: ユーティリティ.concat(型変更の検証_期限内.contents2.string_value, "after")
number n2: 型変更の検証_期限内.contents2.number_value + 2

persist 型変更の検証_期限内:
  contents2:
    string_value: s2
    number_value: n2
  watch(型変更の検証イベント):

number n3: 型変更の検証_計算可能1.contents3 * 3

persist 型変更の検証_計算可能1:
  contents3: n3
  watch(型変更の検証イベント):
