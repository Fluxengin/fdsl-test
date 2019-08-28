# アラートチェックのNo.29に対する処理のステップ1
# Pubサーバからの複数レコードが一つずつチェックし
# ロジック判断条件に会うレコードが対象として永続化を実施しておきます。
# イベント : 広告グループ
import AC29ImpEvent: event/AC29ImpEvent
# アカウント情報を取得する。
import account<condition>: variant/AccountVariant

map condition:
    {
      checkNo: "2900",
      execDay: today(),
      accountId: accountId,
      media: media
   }

# 掲載データ-広告グループ・対象媒体
string media: AC29ImpEvent.media
# 掲載データ-広告グループ・対象アカウント
number accountId: AC29ImpEvent.accountId
# 掲載データ-広告グループ・実行日
string reportDate: AC29ImpEvent.reportDate
# 掲載データ-広告グループ・広告グループID
number adgroupId: AC29ImpEvent.adgroupId

# BigQueryからのレコードに対して判断条件によって、条件に合うと永続化を実施します。
# 条件に合わないと、処理対象外となります。
rule CollectJudge:
    #掲載データ-広告グループ.account_id IN 対象アカウント
    account.accountSeq != "":
    watch(AC29ImpEvent):

# 永続化キー：チェックID＋対象媒体＋アカウントID＋広告グループID
string ac29: "ac29"
string key: media + accountId + adgroupId + ac29

# 永続化処理：投入するレコードが一つずつ永続化する。
# 該当アラートチェックに対して今度の処理対象レコードがすべてKVSに格納しておきます。
# 該当永続化の終了するのは他の処理の前提となります。
persister AddAdGroupInfo:
    media: string                # 対象媒体
    accountId: number            # アカウントID
    adgroupId: number            # 広告グループID
    reportDate: string           # 実行日
    persist(key):                # 永続化キーを設定する。
        lifetime: today()        # 寿命を１日に設定

persist AddAdGroupInfo:
    media: media                 # 対象媒体
    accountId: accountId         # アカウントID
    adgroupId: adgroupId         # 広告グループID
    reportDate: reportDate       # 実行日
    watch(CollectJudge):         # CollectJudgeに対する評価が成立する場合、該当永続化をすぐに実施すること。