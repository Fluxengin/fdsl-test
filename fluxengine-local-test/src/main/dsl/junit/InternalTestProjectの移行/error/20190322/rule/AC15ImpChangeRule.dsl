# イベント : キャンペーン
import ImpChangeEvent: event/AC15ImpChangeEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector
# 取得項目通知メッセージ.通知メッセージ内容
import alertMsg<1500>: variant/AlertMsgVariant

# 出力
# テーブルフィールド：チェック日時、チェック項目ID、アカウントSEQ、キャンペーンID、広告グループID
effect msgEffector:
    adProject: "No"                           # 広告案件
    media: "No"                               # 対象媒体
    accountId: ImpChangeEvent.accountId       # アカウントID
    checkedDateTime: now()                    # チェック日時
    alertMessage: "15alertCheck"              # アラートメッセージ
    checkItemId: 15000                        # チェック項目ID
    adProjectSeq: 0                           # 広告案件SEQ
    campaignId: ImpChangeEvent.campaignId     # キャンペーンID
    adGroupId: ImpChangeEvent.adgroupId       #広告ID
    watch(ImpChangeEvent):                    # ImpChangeEventに対する評価が成立する場合、該当永続化をすぐに実施すること。
