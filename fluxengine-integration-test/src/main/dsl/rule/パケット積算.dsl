import パケットイベント: event/パケットイベント
import ユーザー情報<パケットイベント.端末ID>: variant/ユーザー情報
import パケット積算データ<ユーザー情報.ユーザーID>: persister/パケット積算データ
import メール送信: effector/ユーザー通知

number 累積パケットデータ: パケット積算データ.使用量 + パケットイベント.使用量

#永続化する
persist パケット積算データ:
    使用量: 累積パケットデータ
    watch(パケットイベント):

#超えていたらメールを出す
rule パケット積算:
    累積パケットデータ >= ユーザー情報.パケット上限:
    watch(パケットイベント):

effect メール送信:
    ユーザーID: ユーザー情報.ユーザーID
    日時: now()
    メッセージ: "パケット使用量を超過しました。"
    watch(パケット積算):

state 状態遷移:
    s1:
        パケットイベント: s2
    s2:
    watch(パケットイベント):
    persist(ユーザー情報.ユーザーID):
        lifetime: today()