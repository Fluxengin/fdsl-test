#掲載データ-キーワード
import sevEvent: event/AC07sevEvent
#取得項目通知メッセージ.通知メッセージ内容
#import alertMsg<7000>: variant/AlertMsgVariant
# 対象アカウント取得処理（Javaプラグイン）
import account<condition>: variant/AccountVariant
#出力
import msgEffector: effector/MessageEffector

map condition:
    {
      checkNo: "7000",
      execDay: today(),
      acccountId: sevEvent.accountId
   }

string alertMsg1: 対象キーワード：{0}対象広告グループリスト：{1}({2})
string alertMsg2: alertMsg1.format(sevEvent.keyword,sevEvent.adgroupName,sevEvent.adgroupId)

#(同一アカウントの掲載データ-キーワード.keywordに重複がある  &  重複のkeywordの対応するkeyword_match_typeも完全一致
#&media IN 対象媒体 & account_id IN 対象アカウント
rule r1:
    keyword==sevEvent.keyword && keywordMatchType==sevEvent.keywordMatchType && accountId==sevEvent.accountId:
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
    adProject: account.adComponentName
    media: sevEvent.media
    accountId: sevEvent.accountId
    checkedDateTime: now()
    alertMessage: alertMsg2
    checkItemId: 7000
    adProjectSeq: account.adProjectSeq
    watch(r1):
