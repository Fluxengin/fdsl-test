#掲載データ-キーワード
import sevEvent: event/AC07sevEvent
#取得項目通知メッセージ.通知メッセージ内容
import alertMsg<1901>: variant/AlertMsgVariant
# 対象アカウント取得処理（Javaプラグイン）
import account<condition>: variant/AccountVariant
#出力
import msgEffector: effector/MessageEffector

map test: {a1: 1000, a2: "1-1"}

bool b1:
    test.a2 == "1-1": true
    else: false

date d: 2018/12/01
bool b2:
    d == 2018/12/01: true
    else: false


datetime dt: 2018/12/01 00:00:00
bool b3:
    dt == 2018/12/01 00:00:00: true
    else: false


list testList: [ {a1: "x", a2: 1}, {a1: "y", a2: 2} ]
string result: testList.filter(a1 == "<=")



list condition:
    [{
      checkNo: "7",
      execDay: today(),
      componentId: sevEvent.accountId,
      mediaName: sevEvent.media,
      componentTypeId: 71001,
   }]


string media: sevEvent.media
#(同一アカウントの掲載データ-キーワード.keywordに重複がある  &  重複のkeywordの対応するkeyword_match_typeも完全一致
#&media IN 対象媒体 & account_id IN 対象アカウント
rule r1:
    71002 == sevEvent.accountId && keyword == sevEvent.keyword && keywordMatchType == sevEvent.keywordMatchType && accountId == sevEvent.accountId:
    watch(sevEvent):

# 複合キーを作成
string sumKey: sevEvent.keywordMatchType+sevEvent.keyword+sevEvent.accountId+sevEvent.media
#persister : データをsumKeyに永続化する
persist p:
    skmt: sevEvent.keywordMatchType
    sk: sevEvent.keyword
    sa: sevEvent.accountId
    watch(sevEvent):

#永続化values
string keywordMatchType: p.skmt
string keyword: p.sk
number accountId: p.sa

# persister : データをsumKeyに永続化する
persister p:
    skmt: string
    sk: string
    sa: number
    persist(sumKey):
       lifetime: today()
#出力
effect msgEffector:
    adProjectSeq:
    campaignId:
    adGroupId:
    categoryGroupId:
    categorySort:
    adProject: "0777"
    media: sevEvent.media
    accountId: sevEvent.accountId
    checkedDateTime: now()
    alertMessage: alertMsg
    checkItemId: 7
    watch(r1):