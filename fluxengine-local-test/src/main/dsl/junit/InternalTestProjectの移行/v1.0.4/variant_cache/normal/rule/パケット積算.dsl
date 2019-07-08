import パケットイベント: event/パケットイベント
import 数値<パケットイベント.端末ID>: variant/ユーザー情報
import 文字列<パケットイベント.端末ID>: variant/ユーザー情報
import List情報<condition>: variant/ユーザー情報
import パケット積算データ<文字列>: persister/パケット積算データ
import メール送信: effector/ユーザー通知

list condition: [{ checkNo: "4001",  execDay: today(), accountId: パケットイベント.端末ID}]

rule r1:
  List情報.ユーザーID=="uid12345" && 数値 == 1 && 文字列 == "あ":
  watch(パケットイベント):
