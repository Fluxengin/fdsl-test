#キャンペーン
import impEvent: event/AC02ImpEvent
#出力
import msgEffector: effector/MessageEffector
#取得項目通知メッセージ.通知メッセージ内容
#import alertMsg<2000>: variant/AlertMsgVariant

rule r1:
    impEvent.network == "Search":
    watch(impEvent):
    
rule r2:
    impEvent.network == "Display":
    watch(impEvent):

#Search
string alertMsg1: 対象キャンペーン：{0}({1})impシェア損失率（予算）:{2}
string alertMsg2: alertMsg1.format(impEvent.campaignName,impEvent.campaignId,impEvent.searchs)
#Display
string alertMsg3: 対象キャンペーン：{0}({1})impシェア損失率（予算）:{2}
string alertMsg4: alertMsg3.format(impEvent.campaignName,impEvent.campaignId,impEvent.displays)

#出力
effect msgEffector:
    adProject: ""
    adProjectSeq: 0
    media: impEvent.media
    accountId: impEvent.accountId
    checkedDateTime: now()
    alertMessage: alertMsg2
    checkItemId: 2000
    campaignId: impEvent.campaignId
    watch(r1):
    
 
#出力
effect msgEffector as effector1:
    adProject: ""
    adProjectSeq: 0
    media: impEvent.media
    accountId: impEvent.accountId
    checkedDateTime: now()
    alertMessage: alertMsg4
    checkItemId: 2000
    campaignId: impEvent.campaignId
    watch(r2):













