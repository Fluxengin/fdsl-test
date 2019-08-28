# アラートチェックのNo.11に対する処理
# システムは、終了日設定したキャンペーンが、ステータスがオンになった場合アラート。
# イベント : 掲載データ-キャンペーン
import campaignServingStatusEvent: event/AC11CampaignServingStatusEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector
# 通知メッセージから出力するメッセージ情報を取得する。
# import alertMsg<1100>: variant/AlertMsgVariant

# 掲載データ-キャンペーン・媒体
string media: campaignServingStatusEvent.media
# 掲載データ-キャンペーン・広告案件SEQ
number adProjectSeq: campaignServingStatusEvent.adProjectSeq
# 掲載データ-キャンペーン・広告案件
string adProject: campaignServingStatusEvent.adProject
# 掲載データ-キャンペーン・アカウントID
number accountId: campaignServingStatusEvent.accountId
# 掲載データ-キャンペーン・キャンペーンID
number campaignId: campaignServingStatusEvent.campaignId
string campaignName: campaignServingStatusEvent.campaignName
string campaignEndDate: campaignServingStatusEvent.campaignEndDate
string templateMsg: "キャンペーン{0}（{1}）が{2}に終了に向かいました。"
string alertMsg: templateMsg.format(campaignId, campaignName, campaignEndDate)

# テーブルフィールド：チェック日時、チェック項目ID、媒体、広告案件SEQ、広告案件、アカウントID、キャンペーンID
effect msgEffector:
    checkedDateTime: now()                 # チェック日時
    checkItemId: 1100                      # チェック項目ID
    media: media                           # 媒体
    adProjectSeq: adProjectSeq             # 広告案件SEQ
    adProject: adProject                   # 広告案件
    accountId: accountId                   # アカウントID
    campaignId: campaignId                 # キャンペーンID
    alertMessage: alertMsg                 # アラートメッセージ
    watch(campaignServingStatusEvent):     # campaignServingStatusEventに対する評価が成立する場合、MySqlに該当アラートチェックのチェック結果を登録する。