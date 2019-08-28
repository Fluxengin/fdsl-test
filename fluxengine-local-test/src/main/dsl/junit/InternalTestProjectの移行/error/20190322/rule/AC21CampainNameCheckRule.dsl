# アラートチェックのNo.21に対する処理
# キャンペーンタイプが「ディスプレイ ネットワーク」の場合、キャンペーン名にある言葉が含まれる場合、アラート対象として抽出する。
# イベント : キャンペーン
import AC21CampainNameCheckEvent: event/AC21CampainNameCheckEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector
# 通知メッセージから出力するメッセージ情報を取得する。
# import alertMsg<2100>: variant/AlertMsgVariant
string templateMsg: "キャンペーンタイプの設定を確認してください対象キャンペーン：{0}（{1}）対象キャンペーンタイプ：{2}"
# キャンペーン・媒体
string media: AC21CampainNameCheckEvent.media
# キャンペーン・広告案件SEQ
number adProjectSeq: AC21CampainNameCheckEvent.adProjectSeq
# キャンペーン・広告案件
string adProject: AC21CampainNameCheckEvent.adProject
# キャンペーン・アカウントID
number accountId: AC21CampainNameCheckEvent.accountId
# キャンペーン・キャンペーンID
number campaignId: AC21CampainNameCheckEvent.campaignId
string campaignName: AC21CampainNameCheckEvent.campaignName
string campaignType: AC21CampainNameCheckEvent.campaignType
string alertMsg: templateMsg.format(campaignName, campaignId, campaignType)

# テーブルフィールド：チェック日時、チェック項目ID、媒体、広告案件SEQ、広告案件、アカウントID、キャンペーンID
effect msgEffector:
    checkedDateTime: now()                 # チェック日時
    checkItemId: 2100                      # チェック項目ID
    media: media                           # 媒体
    adProjectSeq: adProjectSeq             # 広告案件SEQ
    adProject: adProject                   # 広告案件
    accountId: accountId                   # アカウントID
    campaignId: campaignId                 # キャンペーンID
    alertMessage: alertMsg                 # アラートメッセージ
    watch(AC21CampainNameCheckEvent):  # AC21CampainNameCheckEventに対する評価が成立する場合、MySqlに該当アラートチェックのチェック結果を登録する。
