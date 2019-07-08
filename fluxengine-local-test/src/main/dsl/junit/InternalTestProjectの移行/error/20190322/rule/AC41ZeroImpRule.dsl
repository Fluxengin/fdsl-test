# イベント：掲載データ-広告
import AC41ZeroImpEvent: event/AC41ZeroImpEvent
# 取得項目通知メッセージ.通知メッセージ内容
#import alertMsg<4100>: variant/AlertMsgVariant
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector

string alertMsg1: 過去{0}日間でIMP0の広告があります対象キャンペーン:{1}（{2}）対象広告グループ:{3}（{4}）対象広告:{5}（{6}）
string alertMsg2: alertMsg1.format(AC41ZeroImpEvent.dataReferencePeriod,AC41ZeroImpEvent.campaignName,AC41ZeroImpEvent.campaignId,AC41ZeroImpEvent.adgroupName,AC41ZeroImpEvent.adGroupId,AC41ZeroImpEvent.adName,AC41ZeroImpEvent.adId)

# テーブルフィールド:チェック項目ID、媒体、広告案件SEQ、広告案件、アカウントID、キャンペーンID、広告グループID、広告ID、チェック日時
effect msgEffector:
    adProject: ""        # 広告案件
    media: AC41ZeroImpEvent.media                # 媒体
    accountId: AC41ZeroImpEvent.accountId        # アカウントID
    campaignId: AC41ZeroImpEvent.campaignId      # キャンペーンID
    adGroupId: AC41ZeroImpEvent.adGroupId        # 広告グループID
    checkedDateTime: now()                       # チェック日時
    alertMessage: alertMsg2                      # アラートメッセージ
    adId: AC41ZeroImpEvent.adId                  # 広告ID
    adProjectSeq: 0   # 広告案件SEQ
    checkItemId: 4100                            # チェック項目ID
    watch(AC41ZeroImpEvent):                     # AC41ZeroImpEventに対する評価が成立する場合、該当永続化をすぐに実施すること。