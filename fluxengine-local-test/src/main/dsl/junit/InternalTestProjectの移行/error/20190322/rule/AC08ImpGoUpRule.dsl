# イベント : キャンペーン
import ImpGoUpEvent: event/AC08ImpGoUpEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector
# 取得項目通知メッセージ.通知メッセージ内容
#import alertMsg<80000>: variant/AlertMsgVariant

string alertMsg1: 対象キャンペーン：{0}({1})一昨日：インプレッションシェア:{2}昨日：インプレッションシェア:{3}
string alertMsg2: alertMsg1.format(ImpGoUpEvent.campaignName,ImpGoUpEvent.campaignId,ImpGoUpEvent.cost2,ImpGoUpEvent.cost1)

# 出力
# テーブルフィールド：チェック項目ID、チェック日時 、アカウントID、キャンペーンID
effect msgEffector:
    adProject: "No"                           # 広告案件
    media: "No"                               # 対象媒体
    accountId: ImpGoUpEvent.accountId         # アカウントID
    checkedDateTime: now()                    # チェック日時
    alertMessage: alertMsg2                    # アラートメッセージ
    checkItemId: 80000                        # チェック項目ID
    adProjectSeq: 0                           # 広告案件SEQ
    campaignId: ImpGoUpEvent.campaignId       # キャンペーンID
    watch(ImpGoUpEvent):                      # ImpGoUpEventに対する評価が成立する場合、該当永続化をすぐに実施すること。
