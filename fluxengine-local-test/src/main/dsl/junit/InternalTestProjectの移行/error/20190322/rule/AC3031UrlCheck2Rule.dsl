import urlCheck2Event: event/AC3031UrlCheck2Event
# アラートチェック番号によって、MySqlの通知メッセージからメッセージ詳細を取得する。
#import alertMsg<3031>: variant/AlertMsgVariant
# 対象アカウント取得処理（Javaプラグイン）
import account<condition>: variant/AccountVariant
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector

map condition:
    {
      checkNo: "3031",
      execDay: today(),
      accountId: urlCheck2Event.accountId,
      media: urlCheck2Event.media
    }

# 最終ページ URL
bool check1:
    urlCheck2Event.finalUrls.contains(" ")

rule checkRule:
    check1 == true:
    watch(urlCheck2Event):

string alertMsg1: 対象キャンペーン:{0}({1}) 対象広告グループ:{2}({3}) 対象広告:{4}({5})
string alertMsg2: alertMsg1.format(urlCheck2Event.campaignName, urlCheck2Event.campaignId, urlCheck2Event.adGroupName, urlCheck2Event.adGroupId, urlCheck2Event.adId, urlCheck2Event.adName)

# テーブルフィールド：チェック日時、チェック項目ID、アカウントSEQ、キャンペーンID、広告グループID、広告ID、キーワードID
# エフェクト：チェック結果（MySqlに保存する）
effect msgEffector:
    checkedDateTime: now()                      # チェック日時
    checkItemId: 3031                           # チェック項目ID
    media: urlCheck2Event.media                 # 媒体
    adProjectSeq: account.adProjectSeq          # 広告案件SEQ
    adProject: account.adComponentName          # 広告案件
    accountId: urlCheck2Event.accountId         # アカウントID
    campaignId: urlCheck2Event.campaignId       # キャンペーンID
    adGroupId: urlCheck2Event.adGroupId         # 広告グループID
    adId: urlCheck2Event.adId                   # 広告ID
    alertMessage: alertMsg2                     # ALERT MESSAGE
    watch(checkRule):