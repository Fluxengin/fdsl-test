import パケットイベント: event/パケットイベント
import ユーザー情報<パケットイベント.端末ID>: variant/ユーザー情報
import メール送信: effector/ユーザー通知

read 日別データ:
    端末ID: string
    日時: string
    使用量: number
    watch(パケットイベント):
    get(パケットイベント.端末ID):

rule 日別データ検証:
    日別データ.使用量 > 500:
    watch(日別データ):

effect メール送信 as アラート:
    ユーザーID: ユーザー情報.ユーザーID
    日時: 日別データ.日時
    メッセージ: "閾値超過のデータがありました。"
    watch(日別データ検証):