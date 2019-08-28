import パケットイベント: event/パケットイベント
import ユーザー情報<パケットイベント.端末ID>: variant/ユーザー情報
import List情報<condition>: variant/ユーザー情報
import パケット積算データ<ユーザー情報.ユーザーID>: persister/パケット積算データ
import メール送信: effector/ユーザー通知

list condition: [{ checkNo: "4001",  execDay: today(), accountId: パケットイベント.端末ID}]

string s1: "a,b,c"
list l1: s1.split(",")
string t: l1.join(",") # t = "a,b,c"



rule xxx:
  List情報.ユーザーID=="tid112233":
  watch(パケットイベント):
