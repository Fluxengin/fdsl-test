# イベント：カテゴリ予算
import timeBudgetEvent: event/AC52TimeBudgetEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector

# 上限予実率 ＝ 【カテゴリ_時間実績.コストCost(fee込)実績】／ 【時間閾値.上限閾値】
number upperCostRate: timeBudgetEvent.cost/timeBudgetEvent.upperThreshold
number upperThresholdRate: timeBudgetEvent.upperThresholdRate*0.01
# 下限予実率 ＝ 【カテゴリ_時間実績.コストCost(fee込)実績】／ 【時間閾値.下限閾値】
number lowerCostRate: timeBudgetEvent.cost/timeBudgetEvent.lowerthreshold
number lowerthresholdRate: timeBudgetEvent.lowerthresholdRate*0.01

# if 上限予実率  > カテゴリチェック項目詳細設定.上限閾値率
# アラートメッセージは「over」
state upperLimitState:
    afford:
        timeBudgetEvent:
            upperCostRate > upperThresholdRate: over
    over:
    watch(timeBudgetEvent):
    persist(timeBudgetEvent.categoryGroupId):
        lifetime: today()

rule upperLimitRule:
    upperLimitState.currentState == "over":
    watch(upperLimitState):

# if 下限予実率 < カテゴリチェック項目詳細設定.下限閾値率)
# アラートメッセージは「short」
state lowerLimitState:
    afford:
        timeBudgetEvent:
            lowerCostRate < lowerthresholdRate: short
    short:
    watch(timeBudgetEvent):
    persist(timeBudgetEvent.categoryGroupId):
        lifetime: today()

rule lowerLimitRule:
    lowerLimitState.currentState == "short":
    watch(lowerLimitState):

#テーブルフィールド：チェック項目ID、カテゴリグループID、ソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、時間閾値SEQ
effect msgEffector as effector1:
    checkItemId: 52                                     # チェック項目ID
    media: "categroy"                                   # カテゴリ
    categoryGroupId: timeBudgetEvent.categoryGroupId    # カテゴリグループID
    categorySort: timeBudgetEvent.sort                  # カテゴリソート順
    kategoryNo: timeBudgetEvent.ctgrLyrNo               # カテゴリ階層番号
    adProjectSeq: timeBudgetEvent.adProjectSeq          # 広告案件SEQ
    adProject: "1"                                      # 広告案件
    checkedDateTime: now()                              # チェック日時
    dayThresholdId: timeBudgetEvent.timeThresholdSeq    # 時間閾値SEQ
    alertMessage: "over"                                # アラートメッセージ:"over" 
    watch(upperLimitRule):                              # upperLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。

#テーブルフィールド：チェック項目ID、カテゴリグループID、ソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、時間閾値SEQ
effect msgEffector as effector2:
    checkItemId: 52                                     # チェック項目ID
    media: "categroy"                                   # カテゴリ
    categoryGroupId: timeBudgetEvent.categoryGroupId    # カテゴリグループID
    categorySort: timeBudgetEvent.sort                  # カテゴリソート順
    kategoryNo: timeBudgetEvent.ctgrLyrNo               # カテゴリ階層番号
    adProjectSeq: timeBudgetEvent.adProjectSeq          # 広告案件SEQ
    adProject: "1"                                      # 広告案件
    checkedDateTime: now()                              # チェック日時
    dayThresholdId: timeBudgetEvent.timeThresholdSeq    # 時間閾値SEQ
    alertMessage: "short"                               # アラートメッセージ:"short" 
    watch(lowerLimitRule):                              # upperLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。