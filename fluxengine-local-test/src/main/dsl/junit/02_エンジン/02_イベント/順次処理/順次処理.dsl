# ポイントを100加算し、その結果1000ポイントを超えていればゴールドクラス、そうでなければノーマルクラスと判定するロジック

event ポイント加算:
  加算されるポイント: number

event クラス判定:

# 普通こんな分け方しないが、テストのため
persister ユーザのポイント:
  現在のポイント: number
  persist("user1"):

persister ユーザのクラス:
  現在のクラス: string
  persist("user1"):

number 新しいポイント合計: ユーザのポイント.現在のポイント + ポイント加算.加算されるポイント

persist ユーザのポイント:
  現在のポイント: 新しいポイント合計
  watch(ポイント加算):

string 新しいクラス:
  ユーザのポイント.現在のポイント > 1000: "ゴールド"
  else: "ノーマル"

persist ユーザのクラス:
  現在のクラス: 新しいクラス
  watch(クラス判定):