# アラートチェックのNo.29に対する処理のステップ2
# Pubサーバからの複数レコードが一つずつチェックし
# 該当アラーチェック処理のステップ1で永続化したものと比べると、
# 存在しないれコートがアラート対象(IMPが０の広告グループ)とすること。
# アラート対象：IMPが０の広告グループ＝「掲載した広告グループ」－「IMPが発生する広告グループ」

# イベント : 掲載データ-広告グループ
import AC29PubEvent: event/AC29PubEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector
# 通知メッセージから出力するメッセージ情報を取得する。
#import alertMsg<2900>: variant/AlertMsgVariant
# アカウント情報を取得する。
import account<condition>: variant/AccountVariant

map condition:
    {
      checkNo: "2900",
      execDay: today(),
      accountId: accountId,
      media: media
   }
string templateMsg: "IMPが発生していない広告グループがあります。対象キャンペーン:{0}（{1}）対象広告グループ:{2}（{3}）"
# 掲載データ-広告グループ・対象媒体
string media: AC29PubEvent.media
# 掲載データ-広告グループ・対象アカウント
number accountId: AC29PubEvent.accountId
# 掲載データ-広告グループ・実行日
string reportDate: AC29PubEvent.reportDate
# 掲載データ-広告グループ・広告グループID
number adgroupId: AC29PubEvent.adgroupId
# 掲載データ-広告グループ・広告グループNAME
string adgroupName: AC29PubEvent.adgroupName
# 広告・キャンペーンID
number campaignId: AC29PubEvent.campaignId
# 広告・キャンペーンNAME
string campaignName: AC29PubEvent.campaignName
string alertMsg: templateMsg.format(campaignName, campaignId, adgroupName, adgroupId)

# BigQueryからのレコードに対して判断条件によって、条件に合うと永続化を実施します。
# 条件に合わないと、処理対象外となります。
rule CollectJudge:
    #掲載データ-広告グループ.account_id IN 対象アカウント
    #IMP０の広告グループがない場合
    account.accountSeq != "" && retAdGrouId != "":
    watch(AC29PubEvent):

# 重複チェック用永続化キー：チェックID＋対象媒体＋アカウントID＋広告グループID
string ac29: "ac29"
string checkKey: media + accountId + adgroupId + ac29
# 存在性チェック
# 重複チェック用永続化キーに対するレコードが存在するかチェックする。
bool isFlag: AddAdGroupInfo.exists()

# アラート対象広告グループID
# アラート対象：IMPが０の広告グループ＝「掲載した広告グループ」－「IMPが発生する広告グループ」
string retAdGrouId: 
    #投入する広告グループIDが既にKVSに格納した場合、アラート対象としません。
    isFlag == true: ""
    #投入する広告グループIDがKVSに格納しない場合、アラート対象とします。
    else: adgroupId

# 永続化処理：投入するレコードが一つずつ永続化する。
# 該当アラートチェックに対して今度の処理対象レコードがすべてKVSに格納しておきます。
# 該当永続化の終了するのは他の処理の前提となります。
persister AddAdGroupInfo:
    media: string             # 対象媒体
    accountId: number         # アカウントID
    adgroupId: number         # 広告グループID
    reportDate: string        # 実行日
    persist(checkKey):        # 永続化キーを設定する。
        lifetime: today()     # 寿命を１日に設定

persist AddAdGroupInfo:
    media: media              # 対象媒体
    accountId: accountId      # アカウントID
    adgroupId: adgroupId      # 広告グループID
    reportDate: reportDate    # 実行日
    watch(CollectJudge):      # CollectJudgeに対する評価が成立する場合、該当永続化をすぐに実施すること。

# テーブルフィールド：チェック日時、チェック項目ID、媒体、広告案件SEQ、広告案件、アカウントID、広告グループID
effect msgEffector:
    checkedDateTime: now()                 # チェック日時
    checkItemId: 2902                      # チェック項目ID
    media: media                           # 媒体
    adProjectSeq: account.adProjectSeq     # 広告案件SEQ
    adProject: account.adComponentName     # 広告案件
    accountId: accountId                   # アカウントID
    adGroupId: adgroupId                   # 広告グループID
    alertMessage: alertMsg                 # アラートメッセージ
    watch(CollectJudge):                   # CollectJudgeに対する評価が成立する場合、MySqlに該当アラートチェックのチェック結果を登録する。
