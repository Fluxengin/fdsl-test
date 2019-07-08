# アラートチェックのNo.55に対する処理
# 累積消化アラート-日別
# システムは日次累積予算と実績を比較し、下限と上限の範囲外の場合、アラートを出す。
# イベント : カテゴリー累計Cost
import costPerformancePlanEvent: event/AC55CostPerformancePlanEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector

# 予実比 ＝【[DM]カテゴリ一覧_サマリ.累計Cost(+Fee)-実績】／ 【カテゴリ一覧_サマリ.累計Cost(+Fee)-計画】
number pPCostRate: costPerformancePlanEvent.gTtlCostPerformance/costPerformancePlanEvent.gTtlCostPlan
# 上限閾値率
number upperLimitThresholdRate: costPerformancePlanEvent.upperLimitThresholdRate*0.01
# 下限閾値率
number lowerLimitThresholdRate: costPerformancePlanEvent.lowerLimitThresholdRate*0.01
# カテゴリグループID
number categoryGroupId: costPerformancePlanEvent.categoryGroupId
# カテゴリソート順
number sort: costPerformancePlanEvent.sort
# カテゴリ階層番号
number ctgrLyrNo: costPerformancePlanEvent.ctgrLyrNo
# 広告案件SEQ
number adProjectSeq: costPerformancePlanEvent.adProjectSeq
# 日閾値SEQ
number dailyThresholdSeq: costPerformancePlanEvent.dailyThresholdSeq

# if (予実比  > 日閾値.上限閾値率) 
# アラートメッセージは「over」
state upperLimitState:
    afford:
        costPerformancePlanEvent:
            pPCostRate > upperLimitThresholdRate: over
    over:
    watch(costPerformancePlanEvent):
    persist(categoryGroupId):
        lifetime: today()

rule upperLimitRule:
    upperLimitState.currentState == "over":
    watch(upperLimitState):

# if (予実比 < 日閾値.下限閾値率) 
# アラートメッセージは「short」
state lowerLimitState:
    afford:
        costPerformancePlanEvent:
            pPCostRate < lowerLimitThresholdRate: short
    short:
    watch(costPerformancePlanEvent):
    persist(categoryGroupId):
        lifetime: today()

rule lowerLimitRule:
    lowerLimitState.currentState == "short":
    watch(lowerLimitState):

# テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
# アラートメッセージは「over」
effect msgEffector as effector1:
    checkItemId: 55                       # チェック項目ID
    media: "カテゴリ"                     # カテゴリ
    categoryGroupId: categoryGroupId      # カテゴリグループID
    categorySort: sort                    # カテゴリソート順
    kategoryNo: ctgrLyrNo                 # カテゴリ階層番号
    adProjectSeq: adProjectSeq            # 広告案件SEQ
    adProject: "1"                        # 広告案件
    checkedDateTime: now()                # チェック日時
    dayThresholdId: dailyThresholdSeq     # 日閾値SEQ
    alertMessage: "over"                  # アラートメッセージ:"over"
    watch(upperLimitRule):                # upperLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。

# テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
# アラートメッセージは「short」
effect msgEffector as effector2:
    checkItemId: 55                       # チェック項目ID
    media: "カテゴリ"                     # カテゴリ
    categoryGroupId: categoryGroupId      # カテゴリグループID
    categorySort: sort                    # カテゴリソート順
    kategoryNo: ctgrLyrNo                 # カテゴリ階層番号
    adProjectSeq: adProjectSeq            # 広告案件SEQ
    adProject: "1"                        # 広告案件
    checkedDateTime: now()                # チェック日時
    dayThresholdId: dailyThresholdSeq     # 日閾値SEQ
    alertMessage: "short"                 # アラートメッセージ:"short"
    watch(lowerLimitRule):                # lowerLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。
