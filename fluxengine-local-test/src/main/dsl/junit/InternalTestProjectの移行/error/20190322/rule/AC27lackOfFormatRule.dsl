#広告グループ
import AC27lackOfFormatEvent: event/AC27lackOfFormatEvent
#出力
import msgEffector: effector/MessageEffector
#取得項目通知メッセージ.通知メッセージ内容
#import alertMsg<2700>: variant/AlertMsgVariant
# 対象アカウント取得処理（Javaプラグイン）
import account<condition>: variant/AccountVariant
#同一広告グループ内のad_typeは以下の種類が揃っていなければ 、アラート対象となる。
string adTypes: AC27lackOfFormatEvent.adType
string medias: AC27lackOfFormatEvent.media
#同一広告グループ内のimage_creative_sizeは以下のサイズが揃っていなければ、アラート対象となる。
string imageSize: AC27lackOfFormatEvent.imageCreativeSize

map condition:
    {
      checkNo: "2700",
      execDay: today(),
      accountId: AC27lackOfFormatEvent.accountId,
      
   }
   
string alertMsg1: 対象キャンペーン:{0}({1})対象広告グループ:{2}({3})media=YDNの場合{4}が設定されていせん,media=YDNの場合フォーマット:{5}が設定されていません。
string alertMsg2: alertMsg1.format(AC27lackOfFormatEvent.campaignName,AC27lackOfFormatEvent.campaignId,AC27lackOfFormatEvent.adgroupName,AC27lackOfFormatEvent.adgroupId,imageSize,AC27lackOfFormatEvent.adType)

string alertMsg3: 対象キャンペーン:{0}({1})対象広告グループ:{2}({3})media=Adwordsの場合、{4}が設定されていせん,Adwords=YDNの場合フォーマット:{5}が設定されていません。
string alertMsg4: alertMsg1.format(AC27lackOfFormatEvent.campaignName,AC27lackOfFormatEvent.campaignId,AC27lackOfFormatEvent.adgroupName,AC27lackOfFormatEvent.adgroupId,imageSize,AC27lackOfFormatEvent.adType)

rule conditionOne:
    medias=="YDN" && adTypes!="Texd ad" && adTypes!="Responsive display ad" && adTypes!="Image ad" && adgroupIds==AC27lackOfFormatEvent.adgroupId:
    watch(AC27lackOfFormatEvent):

rule conditionTow:
    medias=="YDN" && imageSize!="1200*628" && imageSize!="300*250" && imageSize!="300*300" && imageSize!="320*50" && adgroupIds==AC27lackOfFormatEvent.adgroupId:
    watch(AC27lackOfFormatEvent):

rule conditionThree:
    medias=="adwords"  && adTypes!="Responsive display ad" && adTypes!="Image ad" && adgroupIds==AC27lackOfFormatEvent.adgroupId:
    watch(AC27lackOfFormatEvent):

rule conditionFour:
    medias=="adwords" && imageSize!="300*600" && imageSize!="160*600"  && imageSize!="300*250" && imageSize!="728*90" && imageSize!="320*50" && adgroupIds==AC27lackOfFormatEvent.adgroupId:
    watch(AC27lackOfFormatEvent):

rule conditionFive:
    medias=="adwords" && AC27lackOfFormatEvent.multiAssetResponsiveDisplayAdMarketingImages=="" && AC27lackOfFormatEvent.multiAssetResponsiveDisplayAdSquareMarketingImages=="" && adgroupIds==AC27lackOfFormatEvent.adgroupId:
    watch(AC27lackOfFormatEvent):


# 複合キーを作成
string sumKey: AC27lackOfFormatEvent.adgroupId+AC27lackOfFormatEvent.accountId+AC27lackOfFormatEvent.media+AC27lackOfFormatEvent.campaignId+27
#persister : データをsumKeyに永続化する
persist AddAdGroupInfo:
    adgId: AC27lackOfFormatEvent.adgroupId
    watch(AC27lackOfFormatEvent):

#永続化values
number adgroupIds: AddAdGroupInfo.adgId

#persister : データをsumKeyに永続化する
persister AddAdGroupInfo:
    adgId: number
    persist(sumKey):
       lifetime: today()

#出力
effect msgEffector:
    adProject: account.adComponentName
    media: AC27lackOfFormatEvent.media
    accountId: AC27lackOfFormatEvent.accountId
    checkedDateTime: now()
    alertMessage: alertMsg2
    checkItemId: 2700
    adProjectSeq: account.adProjectSeq
    campaignId: AC27lackOfFormatEvent.campaignId
    adGroupId: AC27lackOfFormatEvent.adgroupId
    watch(conditionOne, conditionTow):
    
    
#出力
effect msgEffector as effector1:
    adProject: account.adComponentName
    media: AC27lackOfFormatEvent.media
    accountId: AC27lackOfFormatEvent.accountId
    checkedDateTime: now()
    alertMessage: alertMsg4
    checkItemId: 2700
    adProjectSeq: account.adProjectSeq
    campaignId: AC27lackOfFormatEvent.campaignId
    adGroupId: AC27lackOfFormatEvent.adgroupId
    watch(conditionThree, conditionFour, conditionFive):

