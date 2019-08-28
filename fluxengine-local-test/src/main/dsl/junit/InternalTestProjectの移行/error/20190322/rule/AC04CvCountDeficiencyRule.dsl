# イベント : 広告グループ
import adEvent: event/AC04adEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector
# 取得項目通知メッセージ.通知メッセージ内容
import alertMsg<4000>: variant/AlertMsgVariant

# 出力
# テーブルフィールド：チェック項目ID、チェック日時 、媒体、広告案件SEQ、広告案件、アカウントID、キャンペーンID、広告グループID
effect msgEffector:
    adProject: ""              # 広告案件
    media: adEvent.media                      # 対象媒体
    accountId: adEvent.accountId              # アカウントID
    checkedDateTime: now()                    # チェック日時
    alertMessage: alertMsg                    # アラートメッセージ
    checkItemId: 4000                         # チェック項目ID
    adProjectSeq: 0        				             # 広告案件SEQ
    campaignId: adEvent.campaignId            # キャンペーンID
    adGroupId: adEvent.adGroupId              # 広告グループID
    watch(adEvent):                           # adEventに対する評価が成立する場合、該当永続化をすぐに実施すること。
