# イベント : 媒体
import mediaEvent: event/AC4001MediaEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
#import alertMsg<4001>: variant/AlertMsgVariant
#import account<condition>: variant/AccountVariant
# 取得項目通知メッセージ.通知メッセージ内容
import msgEffector: effector/MessageEffector

map condition:
    {
      checkNo: "4001",
      execDay: today(),
      accountId: mediaEvent.accountId
    }

string alertMsg1: 対象広告：{0}({1}) 広告の審査状況は{2}になっています
string alertMsg2: alertMsg1.format(mediaEvent.adName, mediaEvent.adId, mediaEvent.approvalStatus)

# テーブルフィールド：チェック日時、チェック項目ID、アカウントSEQ、広告ID
effect msgEffector:
    checkedDateTime: now()                 # チェック日時
    checkItemId: 4001                      # チェック項目ID
    media: mediaEvent.media                # 媒体
    accountId: mediaEvent.accountId        # アカウントID
    adId: mediaEvent.adId                  # 広告ID
    alertMessage: alertMsg2                # ALERT MESSAGE
    watch(mediaEvent):                     # mediaEventに対する評価が成立する場合、該当永続化をすぐに実施すること。
