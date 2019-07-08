# アラートチェックのNo.24に対する処理
# Pubサーバからの複数レコードが一つずつチェックし
# キャンペーン名又は広告グループ名に「競合」or「他社」が入っている場合、アラート対象となります。
# イベント : 掲載データ-広告グループ
import AC24PrintedAdgroupNameEvent: event/AC24PrintedAdgroupNameEvent
# イベント : 掲載データ-キャンペーン
import AC24PrintedCampaignEvent: event/AC24PrintedCampaignEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector

# 共通部品・対象媒体※共通部品実装待ちTODO
string inMedia: "YSS"
# 共通部品・対象アカウント※共通部品実装待ちTODO
string inAccountId: "ai001"
# 共通部品・実行日※共通部品実装待ちTODO
string inReportDate: "2019/01/29"

# 掲載データ-広告グループ・対象媒体
string media: AC24PrintedAdgroupNameEvent.media
# 掲載データ-広告グループ・アカウントID
string accountId: AC24PrintedAdgroupNameEvent.accountId
# 掲載データ-広告グループ・広告グループID
number adgroupId: AC24PrintedAdgroupNameEvent.adgroupId
# 掲載データ-広告グループ・広告グループ名
number adgroupName: AC24PrintedAdgroupNameEvent.adgroupName
# 掲載データ-広告グループ・レポート取得日
string reportDate: AC24PrintedAdgroupNameEvent.reportDate
# 掲載データ-広告グループの投入フラグ(true: アラート対象 false: アラート対象外)
bool adPrintedAdFlag:
    # 掲載データ-広告グループ・対象媒体 IN 対象媒体 &
    # 掲載データ-広告グループ・アカウントID IN 対象アカウント &
    # 掲載データ-広告グループ・レポート取得日＝実行日
    inMedia == media && inAccountId == accountId && inReportDate == reportDate: true
    else: false

# 掲載データ-キャンペーン・対象媒体
string mediaKp: AC24PrintedCampaignEvent.media
# 掲載データ-キャンペーン・アカウントID
string accountIdKp: AC24PrintedCampaignEvent.accountId
# 掲載データ-キャンペーン・キャンペーンID
number campaignId: AC24PrintedCampaignEvent.campaignId
# 掲載データ-キャンペーン・キャンペーン名
number campaignName: AC24PrintedCampaignEvent.campaignName
# 掲載データ-キャンペーン・レポート取得日
string reportDateKp: AC24PrintedCampaignEvent.reportDate
# 掲載データ-キャンペーンの投入フラグ(true: アラート対象 false: アラート対象外)
bool adPrintedKpFlag:
    # 掲載データ-キャンペーン・対象媒体 IN 対象媒体 &
    # 掲載データ-キャンペーン・アカウントID IN 対象アカウント
    inMedia == mediaKp && inAccountId == accountIdKp: true
    else: false

# BigQueryからの掲載データ-広告グループに対して判断条件によって
# 下記の条件と合うと、アラート対象となります。
rule AdgroupJudge:
    # 掲載データ-広告グループがアラート対象とする場合
    adPrintedAdFlag == true:
    watch(AC24PrintedAdgroupNameEvent):      # 掲載データ-広告グループに対するイベントが投入する場合、該当評価を実施する。

# BigQueryからの掲載データ-キャンペーンに対して判断条件によって
# 下記の条件と合うと、アラート対象となります。
rule AdKpJudge:
    # 掲載データ-キャンペーンがアラート対象とする場合
    adPrintedKpFlag == true:
    watch(AC24PrintedCampaignEvent):         # 掲載データ-キャンペーンに対するイベントが投入する場合、該当評価を実施する。

# エフェクト：掲載データ-キャンペーンに対するチェック結果（MySqlに保存する）
# テーブルフィールド：チェック日時、チェック項目ID、媒体、広告案件SEQ、広告案件、アカウントID、キャンペーンID
effect msgEffector as KpEffect:
    checkItemId: 24              # チェック項目ID
    media: mediaKp               # 媒体
    adProjectSeq: 101            # 広告案件SEQ
    adProject: "広告案件01"      # 広告案件
    accountId: 10                # アカウントID
    checkedDateTime: now()       # チェック日時
    campaignId: campaignId       # キャンペーンID
    adGroupId: 0                 # 広告グループID
    alertMessage: "アラート24"   # アラートメッセージ
    categoryGroupId: 1           # カテゴリグループID
    categorySort: ""             # カテゴリソート順
    watch(AdKpJudge):            # AdKpJudgeに対する評価が成立する場合、MySqlに該当アラートチェックのチェック結果を登録する。

# エフェクト：掲載データ-広告グループに対するチェック結果（MySqlに保存する）
# テーブルフィールド：チェック日時、チェック項目ID、媒体、広告案件SEQ、広告案件、アカウントID、広告グループID
effect msgEffector as AdEffect:
    checkItemId: 24              # チェック項目ID
    media: media                 # 媒体
    adProjectSeq: 101            # 広告案件SEQ
    adProject: "広告案件02"      # 広告案件
    accountId: 10                # アカウントID
    checkedDateTime: now()       # チェック日時
    campaignId: 0                # キャンペーンID
    adGroupId: adgroupId         # 広告グループID
    alertMessage: "アラート24"   # アラートメッセージ
    categoryGroupId: 1           # カテゴリグループID
    categorySort: ""             # カテゴリソート順
    watch(AdgroupJudge):         # AdgroupJudgeに対する評価が成立する場合、MySqlに該当アラートチェックのチェック結果を登録する。

