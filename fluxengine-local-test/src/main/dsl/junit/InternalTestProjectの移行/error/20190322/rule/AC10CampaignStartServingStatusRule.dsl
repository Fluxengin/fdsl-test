# アラートチェックのNo.10に対する処理
# システムは、開始日設定したキャンペーンが、ステータスがオフになった場合アラート。
# イベント : 掲載データ-キャンペーン
import campaignStartServingStatusEvent: event/AC10CampaignStartServingStatusEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector
# 通知メッセージから出力するメッセージ情報を取得する。
# import alertMsg<1100>: variant/AlertMsgVariant

# 掲載データ-キャンペーン・媒体
string media: campaignStartServingStatusEvent.media
# 掲載データ-キャンペーン・広告案件SEQ
number adProjectSeq: campaignStartServingStatusEvent.adProjectSeq
# 掲載データ-キャンペーン・広告案件
string adProject: campaignStartServingStatusEvent.adProject
# 掲載データ-キャンペーン・アカウントID
number accountId: campaignStartServingStatusEvent.accountId
# 掲載データ-キャンペーン・キャンペーンID
number campaignId: campaignStartServingStatusEvent.campaignId
# 掲載データ-キャンペーン.campaign_name
string campaignName: campaignStartServingStatusEvent.campaignName
string campaignStartDate: campaignStartServingStatusEvent.campaignStartDate
string templateAlertMsg: "キャンペーン{0}（{1}）が{2}に開始に向かいました。"
string alertMsg: templateAlertMsg.format(campaignName, campaignId, campaignStartDate)

# テーブルフィールド：チェック日時、チェック項目ID、媒体、広告案件SEQ、広告案件、アカウントID、キャンペーンID
effect msgEffector:
    checkedDateTime: now()                      # チェック日時
    checkItemId: 1000                           # チェック項目ID
    media: media                                # 媒体
    adProjectSeq: adProjectSeq                  # 広告案件SEQ
    adProject: adProject                        # 広告案件
    accountId: accountId                        # アカウントID
    campaignId: campaignId                      # キャンペーンID
    alertMessage: alertMsg                      # アラートメッセージ
    watch(campaignStartServingStatusEvent):     # campaignStartServingStatusEventに対する評価が成立する場合、MySqlに該当アラートチェックのチェック結果を登録する。