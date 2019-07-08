# アラートチェックのNo.24に対する処理
# Pubサーバからの複数レコードが一つずつチェックし
# キャンペーン名に「競合」or「他社」が入っている場合、アラート対象となります。
# イベント : 掲載データ-キャンペーン
import AC24PrintedCampaignEvent: event/AC24PrintedCampaignEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector
# 通知メッセージから出力するメッセージ情報を取得する。
# import alertMsg<2400>: variant/AlertMsgVariant
# アカウント情報を取得する。
import account<condition>: variant/AccountVariant

map condition:
    {
      checkNo: "2400",
      execDay: today(),
      accountId: accountId,
      media: media
    }
string templateMsg: "「競合」、「他社」はキャンペーン名に入っています。社内承認済か確認ください。対象キャンペーン{0}({1}) 対象広告グループ{2}({3})"
# 掲載データ-キャンペーン・対象媒体
string media: AC24PrintedCampaignEvent.media
# 掲載データ-キャンペーン・アカウントID
number accountId: AC24PrintedCampaignEvent.accountId
# 掲載データ-キャンペーン・キャンペーンID
number campaignId: AC24PrintedCampaignEvent.campaignId
# 掲載データ-キャンペーン・キャンペーンNAME
string campaignName: AC24PrintedCampaignEvent.campaignName
# 掲載データ-広告グループ・広告グループID
number adgroupId: AC24PrintedCampaignEvent.adgroupId
# 掲載データ-広告グループ・広告グループNAME
string adgroupName: AC24PrintedCampaignEvent.adgroupName
string alertMsg: templateMsg.format(campaignName, campaignId, adgroupName, adgroupId)

# BigQueryからの掲載データ-キャンペーンに対して判断条件によって
# 下記の条件と合うと、アラート対象となります。
rule AdKpJudge:
    # 掲載データ-キャンペーンがアラート対象とする場合
    account.accountSeq != "":
    watch(AC24PrintedCampaignEvent):       # 掲載データ-キャンペーンに対するイベントが投入する場合、該当評価を実施する。

# エフェクト：掲載データ-キャンペーンに対するチェック結果（MySqlに保存する）
# テーブルフィールド：チェック日時、チェック項目ID、媒体、広告案件SEQ、広告案件、アカウントID、キャンペーンID
effect msgEffector as KpEffect:
    checkedDateTime: now()                 # チェック日時
    checkItemId: 2401                      # チェック項目ID
    media: media                           # 媒体
    adProjectSeq: account.adProjectSeq     # 広告案件SEQ
    adProject: account.adComponentName     # 広告案件
    accountId: accountId                   # アカウントID
    campaignId: campaignId                 # キャンペーンID
    alertMessage: alertMsg                 # アラートメッセージ
    watch(AdKpJudge):                      # AdKpJudgeに対する評価が成立する場合、MySqlに該当アラートチェックのチェック結果を登録する。