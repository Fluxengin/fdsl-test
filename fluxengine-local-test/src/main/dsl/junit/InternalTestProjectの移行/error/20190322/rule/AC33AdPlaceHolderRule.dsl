import placeHolderEvent: event/AC33AdPlaceHolderEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector

string alertMsg1: 広告表示オプションの設定が不足しています。対象キャンペーン：{0}({1})。設定されている広告表示オプション：{2}
string alertMsg2: alertMsg1.format(placeHolderEvent.campaignName, placeHolderEvent.campaignId, placeHolderEvent.placeholderType)

# テーブルフィールド：チェック日時、チェック項目ID、アカウントSEQ、キャンペーンID
effect msgEffector:
    checkedDateTime: now()                       # チェック日時
    checkItemId: 3300                            # チェック項目ID
    media: placeHolderEvent.media                # 媒体
    adProject: ""
    accountId: placeHolderEvent.accountId        # アカウントID
    campaignId: placeHolderEvent.campaignId      # キャンペーンID
    alertMessage: alertMsg2                      # ALERT MESSAGE
    watch(placeHolderEvent):                     # placeHolderEventに対する評価が成立する場合、該当永続化をすぐに実施すること。