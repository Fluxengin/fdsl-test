# アラートチェックのNo.54に対する処理
import LandingForecastEvent: event/AC54LandingForecastEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector
# 着地率 ＝【カテゴリ_時間累計実績サマリ.着地コスト】／ 【カテゴリ_当日実績.コストCost(Fee込)計画】
number onTheRate: LandingForecastEvent.landCost/LandingForecastEvent.cost
# カテゴリチェック項目詳細設定.上限閾値率
number upperThresholdRate: LandingForecastEvent.upperLimitThresholdRate*0.01
# カテゴリチェック項目詳細設定.下限閾値率
number lowerthresholdRate: LandingForecastEvent.lowerLimitThresholdRate*0.01

# if (着地率  > カテゴリチェック項目詳細設定.上限閾値率)
# アラートメッセージは「over」
state upperLimitState:
    afford:
        LandingForecastEvent:
            onTheRate > upperThresholdRate: over
    over:
    watch(LandingForecastEvent):
    persist(LandingForecastEvent.categoryGroupId):
        lifetime: today()

rule upperLimitRule:
    upperLimitState.currentState == "over":
    watch(upperLimitState):

# if (着地率 < カテゴリチェック項目詳細設定.下限閾値率)
# アラートメッセージは「short」
state lowerLimitState:
    afford:
        LandingForecastEvent:
            onTheRate < lowerthresholdRate: short
    short:
    watch(LandingForecastEvent):
    persist(LandingForecastEvent.categoryGroupId):
        lifetime: today()

rule lowerLimitRule:
    lowerLimitState.currentState == "short":
    watch(lowerLimitState):

#テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時
# アラートメッセージは「over」
effect msgEffector as effector1:
    checkItemId: 54                                        # チェック項目ID
    media: "カテゴリ"                                      # カテゴリ
    categoryGroupId: LandingForecastEvent.categoryGroupId  # カテゴリグループID
    kategoryNo: LandingForecastEvent.ctgrLyrNo             # カテゴリソート順
    categorySort: LandingForecastEvent.sort                # カテゴリ階層番号
    adProjectSeq: LandingForecastEvent.adProjectSeq        # 広告案件SEQ
    adProject: "1"                                         # 広告案件
    checkedDateTime: now()                                 # チェック日時
    alertMessage: "over"                                   # アラートメッセージ:"over" 
    watch(upperLimitRule):                                 # upperLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。

#テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時
# アラートメッセージは「short」
effect msgEffector as effector2:
    checkItemId: 54                                        # チェック項目ID
    media: "カテゴリ"                                      # カテゴリ
    categoryGroupId: LandingForecastEvent.categoryGroupId  # カテゴリグループID
    kategoryNo: LandingForecastEvent.ctgrLyrNo             # カテゴリソート順
    adProjectSeq: LandingForecastEvent.adProjectSeq        # カテゴリ階層番号
    categorySort: LandingForecastEvent.sort                # 広告案件SEQ
    adProject: "1"                                         # 広告案件
    checkedDateTime: now()                                 # チェック日時
    alertMessage: "short"                                  # アラートメッセージ:"short" 
    watch(lowerLimitRule):                                 # lowerLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。