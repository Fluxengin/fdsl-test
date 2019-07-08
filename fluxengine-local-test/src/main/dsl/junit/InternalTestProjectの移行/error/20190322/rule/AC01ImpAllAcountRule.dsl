#キャンペーン
import impAllEvent: event/AC01ImpAllAccountEvent
#出力
import msgEffector: effector/MessageEffector
#取得項目通知メッセージ.通知メッセージ内容
#import alertMsg<1000>: variant/AlertMsgVariant
rule r1:
    impAllEvent.netWork == "Search":
    watch(impAllEvent):
    
rule r2:
    impAllEvent.netWork == "Display":
    watch(impAllEvent):
    
#Search
string alertMsg1: impシェア損失率（予算):{0}
string alertMsg2: alertMsg1.format(impAllEvent.Searchs)
#Display
string alertMsg3: impシェア損失率（予算):{0}
string alertMsg4: alertMsg1.format(impAllEvent.Displays)

#出力
effect msgEffector:
    adProject: ""
    media: impAllEvent.media
    accountId: impAllEvent.accountId
    checkedDateTime: now()
    alertMessage: alertMsg2
    checkItemId: 1000
    adProjectSeq: ""
    watch(r1):

#出力
effect msgEffector as effector1:
    adProject: ""
    media: impAllEvent.media
    accountId: impAllEvent.accountId
    checkedDateTime: now()
    alertMessage: alertMsg4
    checkItemId: 1000
    adProjectSeq: ""
    watch(r2):








