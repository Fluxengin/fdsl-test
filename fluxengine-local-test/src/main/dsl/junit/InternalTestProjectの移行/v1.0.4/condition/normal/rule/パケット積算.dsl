import パケットイベント: event/パケットイベント
import 数値<パケットイベント.端末ID>: variant/ユーザー情報
import 文字列<パケットイベント.端末ID>: variant/ユーザー情報
import List情報<condition>: variant/ユーザー情報
import パケット積算データ<文字列>: persister/パケット積算データ
import メール送信: effector/ユーザー通知

list condition: [{ checkNo: "4001",  execDay: today(), accountId: パケットイベント.端末ID}]

bool b1:
   文字列=="あ" : true
   else : false

bool b2:
   数値==1 : true
   else : false

bool b3:
   文字列=="あ" && 数値==1: true
   else : false

bool b4:
   数値==1 && 文字列=="あ" : true
   else : false

bool b5:
   List情報.ユーザーID=="uid12345" && 数値==1 && 文字列=="あ" : true
   else : false

string s:
    1 == 1 || 2 == 2 && 3 == 3: "true"
    else: "false"

number n: round(1.9)
bool b6:
   n == 2: true
   else: false