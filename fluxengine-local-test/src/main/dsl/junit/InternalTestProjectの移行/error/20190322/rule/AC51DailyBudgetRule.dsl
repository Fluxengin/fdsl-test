# イベント：カテゴリ予算
import dailyBudgetEvent: event/AC51DailyBudgetEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector

# 上限予実率 ＝ 【カテゴリ_当日実績.コストCost(fee込)実績】／ 【日閾値.上限閾値】
number upperCostRate: dailyBudgetEvent.cost/dailyBudgetEvent.upperThreshold
number upperThresholdRate: dailyBudgetEvent.upperThresholdRate*0.01
# 下限予実率 ＝ 【カテゴリ_当日実績.コストCost(fee込)実績】／ 【日閾値.下限閾値】
number lowerCostRate: dailyBudgetEvent.cost/dailyBudgetEvent.lowerthreshold
number lowerthresholdRate: dailyBudgetEvent.lowerthresholdRate*0.01

# if 上限予実率  > カテゴリチェック項目詳細設定.上限閾値率
# アラートメッセージは「over」
state upperLimitState:
    afford:
        dailyBudgetEvent:
            upperCostRate > upperThresholdRate: over
    over:
    watch(dailyBudgetEvent):
    persist(dailyBudgetEvent.categoryGroupId):
        lifetime: today()

rule upperLimitRule:
    upperLimitState.currentState == "over":
    watch(upperLimitState):

# if 下限予実率 < カテゴリチェック項目詳細設定.下限閾値率)
# アラートメッセージは「short」
state lowerLimitState:
    afford:
        dailyBudgetEvent:
            lowerCostRate < lowerthresholdRate: short
    short:
    watch(dailyBudgetEvent):
    persist(dailyBudgetEvent.categoryGroupId):
        lifetime: today()

rule lowerLimitRule:
    lowerLimitState.currentState == "short":
    watch(lowerLimitState):

# テーブルフィールド：チェック項目ID、カテゴリグループID、ソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
effect msgEffector as effector1:
    checkItemId: 51                                      # チェック項目ID
    media: "カテゴリ"                                    # カテゴリ
    categoryGroupId: dailyBudgetEvent.categoryGroupId    # カテゴリグループID
    categorySort: dailyBudgetEvent.sort                  # カテゴリソート順
    kategoryNo: dailyBudgetEvent.ctgrLyrNo               # カテゴリ階層番号
    adProjectSeq: dailyBudgetEvent.adProjectSeq          # 広告案件SEQ
    adProject: "1"                                       # 広告案件
    checkedDateTime: now()                               # チェック日時
    dayThresholdId: dailyBudgetEvent.dailyThresholdSeq   # 日閾値SEQ
    alertMessage: "over"                                 # アラートメッセージ:"over" 
    watch(upperLimitRule):                               # upperLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。

# テーブルフィールド：チェック項目ID、カテゴリグループID、ソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
effect msgEffector as effector2:
    checkItemId: 51                                      # チェック項目ID
    media: "カテゴリ"                                    # カテゴリ
    categoryGroupId: dailyBudgetEvent.categoryGroupId    # カテゴリグループID
    categorySort: dailyBudgetEvent.sort                  # カテゴリソート順
    kategoryNo: dailyBudgetEvent.ctgrLyrNo               # カテゴリ階層番号
    adProjectSeq: dailyBudgetEvent.adProjectSeq          # 広告案件SEQ
    adProject: "1"                                       # 広告案件
    checkedDateTime: now()                               # チェック日時
    dayThresholdId: dailyBudgetEvent.dailyThresholdSeq   # 日閾値SEQ
    alertMessage: "short"                                # アラートメッセージ:"short" 
    watch(lowerLimitRule):                               # lowerLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。