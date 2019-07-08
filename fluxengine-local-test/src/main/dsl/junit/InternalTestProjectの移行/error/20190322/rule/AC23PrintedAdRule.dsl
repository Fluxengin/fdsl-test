# アラートチェックのNo.23に対する処理
# Pubサーバからの複数レコードが一つずつチェックし
# 違う広告グループの場合、広告内容が一致するとアラートとすること。
# イベント : 掲載データ-広告
import AC23PrintedAdEvent: event/AC23PrintedAdEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector
# import alertMsg<2300>: variant/AlertMsgVariant
# アカウント情報を取得する。
import account<condition>: variant/AccountVariant

map condition:
    {
      checkNo: "2300",
      execDay: today(),
      accountId: accountId,
      media: media
    }
string templateMsg: "同一の広告が複数の広告グループにはいってます。対象広告グループリスト：{0}（{1}）"
# 掲載データ-広告・対象媒体
string media: AC23PrintedAdEvent.media
# 掲載データ-広告・アカウントID
number accountId: AC23PrintedAdEvent.accountId
# 掲載データ-広告・広告グループID
number adgroupId: AC23PrintedAdEvent.adgroupId
# 掲載データ-広告・広告グループNAME
string adgroupName: AC23PrintedAdEvent.adgroupName
# 掲載データ-広告・広告ID
number adId: AC23PrintedAdEvent.adId
# 掲載データ-広告・キャンペーンID
number campaignId: AC23PrintedAdEvent.campaignId
# 掲載データ-広告・レポート取得日
string reportDate: AC23PrintedAdEvent.reportDate
# 掲載データ-広告・タイトル
string title: AC23PrintedAdEvent.title
# 掲載データ-広告・タイトル1
string headlinePart1: AC23PrintedAdEvent.headlinePart1
# 掲載データ-広告・タイトル2
string headlinePart2: AC23PrintedAdEvent.headlinePart2
# 掲載データ-広告・説明文
string description: AC23PrintedAdEvent.description
# 掲載データ-広告・説明文1
string description1: AC23PrintedAdEvent.description1
# 掲載データ-広告・説明文2
string description2: AC23PrintedAdEvent.description2
# 掲載データ-広告・最終リンク先URL
string finalUrls: AC23PrintedAdEvent.finalUrls
string alertMsg: templateMsg.format(adgroupName, adgroupId)

# BigQueryからのレコードに対して判断条件によって、条件に合うと永続化を実施します。
# 条件に合わないと、処理対象外となります。
rule CollectJudge:
    # 掲載データ-広告.media IN 対象媒体 &
    # 掲載データ-広告.account_id IN 対象アカウント
    account.accountSeq != "":
    watch(AC23PrintedAdEvent):

# 永続化実施するか判断処理
# 存在性チェックの結果に問わず別々に永続化する。
rule CheckJudge:
    # アラートする場合
    alertResult == "OK":
    # アラートしない場合
    alertResult == "NG":
    watch(CollectJudge):

# アラートする場合
# 存在性チェックの結果に問わず別々に永続化する。
rule AlertJudge:
    # アラートする場合
    alertResult == "OK":
    watch(CollectJudge):

# 永続化キー1：チェックID+対象媒体+アカウントID+広告グループID
string ac23: "23"
string key1: media + accountId + adgroupId + ac23
# 永続化キー2：チェックID+タイトル+タイトル1+タイトル2+説明文+説明文1+説明文2+最終リンク先URL
string key2: title + headlinePart1 + headlinePart2 + description + description1 + description2 + finalUrls + ac23

#存在性チェック
# 永続化キー1に対するレコードが存在するかチェックする。
bool isKy1Flag: AddAdGroupInfo.exists()
# 永続化キー2に対するレコードが存在するかチェックする。
bool isKy2Flag: AddContentInfo.exists()

# アラート対象とするか下記のように判断します。
string alertResult:
    #違う広告グループの場合、広告内容が一致するとアラートすること。
    isKy2Flag == true && isKy1Flag == false: "OK"
    #上記以外の場合、アラート対象外とすること。
    else: "NG"

# 永続化処理：投入するレコードが一つずつ永続化する。
# 該当アラートチェックに対して今度の処理対象レコードがすべてKVSに格納しておきます。
# 該当永続化の終了するのは他の処理の前提となります。
persister AddAdGroupInfo:
    media: string             # 対象媒体
    accountId: number         # アカウントID
    adgroupId: number         # 広告グループID
    campaignId: number        # キャンペーンID
    reportDate: string        # レポート取得日
    persist(key1):            # 永続化キーを設定する。
        lifetime: today()     # 寿命を１日に設定

# 永続化処理：投入するレコードが一つずつ永続化する。
# 該当アラートチェックに対して今度の処理対象レコードがすべてKVSに格納しておきます。
# 該当永続化の終了するのは他の処理の前提となります。
persister AddContentInfo:
    title: string             # タイトル
    headlinePart1: string     # タイトル1
    headlinePart2: string     # タイトル2
    description: string       # 説明文
    description1: string      # 説明文1
    description2: string      # 説明文2
    finalUrls: string         # 最終リンク先URL
    persist(key2):            # 永続化キーを設定する。
        lifetime: today()     # 寿命を１日に設定

persist AddAdGroupInfo:
    media: media              # 対象媒体
    accountId: accountId      # アカウントID
    adgroupId: adgroupId      # 広告グループID
    campaignId: campaignId    # キャンペーンID
    reportDate: reportDate    # レポート取得日
    watch(CheckJudge):        # CheckJudgeに対する評価が成立する場合、該当永続化をすぐに実施すること。

persist AddContentInfo:
    title: title                    # タイトル
    headlinePart1: headlinePart1    # タイトル1
    headlinePart2: headlinePart2    # タイトル2
    description: description        # 説明文
    description1: description1      # 説明文1
    description2: description2      # 説明文2
    finalUrls: finalUrls            # 最終リンク先URL
    watch(CheckJudge):              # CheckJudgeに対する評価が成立する場合、該当永続化をすぐに実施すること。

# エフェクト：チェック結果（MySqlに保存する）
# テーブルフィールド：チェック日時、チェック項目ID、媒体、広告案件SEQ、広告案件、アカウントID、キャンペーンID、広告グループID、広告ID
effect msgEffector:
    checkedDateTime: now()                 # チェック日時
    checkItemId: 2300                      # チェック項目ID
    media: media                           # 媒体
    adProjectSeq: account.adProjectSeq     # 広告案件SEQ
    adProject: account.adComponentName     # 広告案件
    accountId: accountId                   # アカウントID
    campaignId: campaignId                 # キャンペーンID
    adGroupId: adgroupId                   # 広告グループID
    adId: adId                             # 広告ID
    alertMessage: alertMsg                 # アラートメッセージ
    watch(AlertJudge):                     # AlertJudgeに対する評価が成立する場合、MySqlに該当アラートチェックのチェック結果を登録する。