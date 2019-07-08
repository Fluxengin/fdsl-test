import パケットイベント: event/パケットイベント
import BigInteger<パケットイベント.端末ID>: variant/ユーザー情報
import パケット積算データ<"test">: persister/パケット積算データ
import メール送信: effector/ユーザー通知

#NG ログがおかしい ■■アラート:null 1fluxengineabcde_has been addedfluxengineabcde_
string message: BigInteger + "has been added"

#NG parser error
#string message: "the key " + "has been added"

effect メール送信:
    BigInteger: BigInteger
    メッセージ: message
    watch(パケットイベント):