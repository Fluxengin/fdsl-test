test 1 イベントが順番通りに複数件投入される:
  2019/09/20 00:00:01:
    ポイント加算:
      加算されるポイント: 900
  2019/09/20 00:00:02:
    ポイント加算:
      加算されるポイント: 200
    クラス判定:
    inspect:
      ユーザのポイント.現在のポイント == 1100:
      ユーザのクラス.現在のクラス == "ゴールド":

test 2 順番通りが異なると結果が異なることを確認する:
  2019/09/20 00:00:01:
    ポイント加算:
      加算されるポイント: 900
  2019/09/20 00:00:02:
    クラス判定:
    ポイント加算:
      加算されるポイント: 200
    inspect:
      ユーザのポイント.現在のポイント == 1100:
      ユーザのクラス.現在のクラス == "ノーマル":
