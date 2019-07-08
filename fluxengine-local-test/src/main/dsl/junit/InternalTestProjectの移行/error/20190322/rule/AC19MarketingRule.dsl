#取得項目通知メッセージ.通知メッセージ内容
#import alertMsg<1900>: variant/AlertMsgVariant
#出力
import msgEffector: effector/MessageEffector
#「広告グループ.
import AC19MarketingEvent: event/AC19MarketingEvent

string alertMsg1: 対象キャンペーン{0}({1})対象広告グループ：{2}({3})
string alertMsg2: alertMsg1.format(AC19MarketingEvent.campaignName,AC19MarketingEvent.campaignId,AC19MarketingEvent.adgroupName,AC19MarketingEvent.adgroupId)

#出力
effect msgEffector:
        adProject: ""
        media: AC19MarketingEvent.media
        accountId: AC19MarketingEvent.accountId
        campaignId: AC19MarketingEvent.campaignId
        adGroupId: AC19MarketingEvent.adgroupId
        checkedDateTime: now()
        alertMessage: alertMsg2
        checkItemId: 1900
        adProjectSeq: 0
        watch(AC19MarketingEvent):