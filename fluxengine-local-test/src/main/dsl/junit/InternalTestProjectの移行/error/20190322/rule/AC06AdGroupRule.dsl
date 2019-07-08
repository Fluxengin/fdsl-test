# イベント : 広告グループ
import adGroupEvent: event/AC06AdGroupEvent
import msgEffector: effector/MessageEffector

string alertMsg1: 別々の広告グループで同一クエリーが発生しています。対象クエリ:{0}  対象広告グループリスト:{1}
string alertMsg2: alertMsg1.format(adGroupEvent.query, adGroupEvent.adGroupId)

# テーブルフィールド：チェック日時、チェック項目ID、アカウントSEQ、キャンペーンID、広告グループID
effect msgEffector:
    checkedDateTime: now()                      # チェック日時
    checkItemId: 6000                           # チェック項目ID
    media: adGroupEvent.media                   # 媒体
    adProject: ""
    accountId: adGroupEvent.accountId           # アカウントID
    campaignId: adGroupEvent.campaignId         # キャンペーンID
    adGroupId: adGroupEvent.adGroupId           # 広告グループID
    alertMessage: alertMsg2                     # ALERT MESSAGE
    watch(adGroupEvent):                        # adGroupEventに対する評価が成立する場合、該当永続化をすぐに実施すること。