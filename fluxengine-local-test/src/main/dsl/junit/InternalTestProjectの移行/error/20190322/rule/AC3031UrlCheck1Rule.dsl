import urlCheck1Event: event/AC3031UrlCheck1Event
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
      accountId: urlCheck1Event.accountId,
      media: urlCheck1Event.media
    }

# トラッキング テンプレート
bool check1:
    urlCheck1Event.trackingUrlTemplate.contains(" ")

# 最終ページ URL
bool check2:
    urlCheck1Event.finalUrls.contains(" ")

# モバイルの最終ページ URL
bool check3:
    urlCheck1Event.finalMobileUrls.contains(" ")

# アプリの最終リンク先 URL
bool check4:
    urlCheck1Event.finalAppUrls.contains(" ")

# 各URLをチェックし、いずれかスペースが入っている場合、アラート対象となる。
rule checkRule:
    check1 == true || check2 == true || check3 == true || check4 == true:
    watch(urlCheck1Event):

string alertMsg1: 対象キャンペーン:{0}({1}) 対象広告グループ:{2}({3}) 対象KW:{4}({5})
string alertMsg2: alertMsg1.format(urlCheck1Event.campaignName, urlCheck1Event.campaignId, urlCheck1Event.adGroupName, urlCheck1Event.adGroupId, urlCheck1Event.keywordId, urlCheck1Event.keyword)

# テーブルフィールド：チェック日時、チェック項目ID、アカウントSEQ、キャンペーンID、広告グループID、広告ID、キーワードID
# エフェクト：チェック結果（MySqlに保存する）
effect msgEffector:
    checkedDateTime: now()                      # チェック日時
    checkItemId: 3031                           # チェック項目ID
    media: urlCheck1Event.media                 # 媒体
    adProjectSeq: account.adProjectSeq          # 広告案件SEQ
    adProject: account.adComponentName          # 広告案件
    accountId: urlCheck1Event.accountId         # アカウントID
    campaignId: urlCheck1Event.campaignId       # キャンペーンID
    adGroupId: urlCheck1Event.adGroupId         # 広告グループID
    keywordId: urlCheck1Event.keywordId         # キーワードID
    alertMessage: alertMsg2                     # ALERT MESSAGE
    watch(checkRule):
