persister 型変更の検証_期限切れ:
  contents1: number
  persist("persister型変更の検証"):
    lifetime: now()

persister 型変更の検証_期限内:
  contents2: string
  persist("persister型変更の検証"):
    lifetime: today()

persister 型変更の検証_計算可能1:
  contents3: number
  persist("persister型変更の検証"):
    lifetime: today()

event 型変更の検証イベント:
  dummy: string

# 型変更の検証_期限内は実行時エラーになるはずなので、
# 他の処理を巻き添えにしないために、イベントを分ける
event 型変更の検証イベント2:
  dummy: string

number n1: 型変更の検証_期限切れ.contents1 + 1

persist 型変更の検証_期限切れ:
  contents1: n1
  watch(型変更の検証イベント):

string s2: ユーティリティ.concat(型変更の検証_期限内.contents2, "_after")

persist 型変更の検証_期限内:
  contents2: s2
  watch(型変更の検証イベント2):

number n3: 型変更の検証_計算可能1.contents3 * 3

persist 型変更の検証_計算可能1:
  contents3: n3
  watch(型変更の検証イベント):
