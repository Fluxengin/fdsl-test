# アラートチェックのNo.26に対する処理
# 広告グループにおいて1IMP以上発生している広告が3つ未満の場合、アラート対象として抽出する。
# イベント : 広告
import AC26AdGroupImpThreeCheckEvent: event/AC26AdGroupImpThreeCheckEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector
# 通知メッセージから出力するメッセージ情報を取得する。
# import alertMsg<2600>: variant/AlertMsgVariant

string templateMsg: "広告文の設定が不足しています 対象キャンペーン:{0}（{1}）対象広告グループ:{2}（{3}）不足数:{4}"
# 広告・媒体
string media: AC26AdGroupImpThreeCheckEvent.media
# 広告・広告案件SEQ
number adProjectSeq: AC26AdGroupImpThreeCheckEvent.adProjectSeq
# 広告・広告案件
string adProject: AC26AdGroupImpThreeCheckEvent.adProject
# 広告・アカウントID
number accountId: AC26AdGroupImpThreeCheckEvent.accountId
# 広告・キャンペーンID
number campaignId: AC26AdGroupImpThreeCheckEvent.campaignId
# 広告・キャンペーンNAME
string campaignName: AC26AdGroupImpThreeCheckEvent.campaignName
# 広告・広告グループID
number adGroupId: AC26AdGroupImpThreeCheckEvent.adGroupId
# 広告・広告グループNAME
string adGroupName: AC26AdGroupImpThreeCheckEvent.adGroupName
number impcount: AC26AdGroupImpThreeCheckEvent.impcount
string alertMsg: templateMsg.format(campaignName, campaignId, adGroupName, adGroupId, impcount)

# テーブルフィールド：チェック日時、チェック項目ID、媒体、広告案件SEQ、広告案件、アカウントID、キャンペーンID、広告グループID
effect msgEffector:
    checkedDateTime: now()                 # チェック日時
    checkItemId: 2600                      # チェック項目ID
    media: media                           # 媒体
    adProjectSeq: adProjectSeq             # 広告案件SEQ
    adProject: adProject                   # 広告案件
    accountId: accountId                   # アカウントID
    campaignId: campaignId                 # キャンペーンID
    adGroupId: adGroupId                   # 広告グループID
    alertMessage: alertMsg                 # アラートメッセージ
    watch(AC26AdGroupImpThreeCheckEvent):  # AC26AdGroupImpThreeCheckEventに対する評価が成立する場合、MySqlに該当アラートチェックのチェック結果を登録する。