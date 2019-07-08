# アラートチェックのNo.56に対する処理
# 累積消化アラート-日別
# システムは日次累積予算と実績を比較し、下限と上限の範囲外の場合、アラートを出す。
# イベント : KPI指標により、上限か下限の設定を選択する
import KpiIndexEvent: event/AC56KpiIndexEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector

# 予実比 ＝【カテゴリ一覧_サマリ.累計目標実績】／ 【カテゴリ一覧_サマリ.累計目標計画】
number pPGoalRate: KpiIndexEvent.gTtlGoalPerformance/KpiIndexEvent.gTtlGoalPlan
# 上限閾値率
number upperLimitThresholdRate: KpiIndexEvent.upperLimitThresholdRate*0.01
# 下限閾値率
number lowerLimitThresholdRate: KpiIndexEvent.lowerLimitThresholdRate*0.01
# カテゴリグループID
number categoryGroupId: KpiIndexEvent.categoryGroupId
# カテゴリソート順
number sort: KpiIndexEvent.sort
# カテゴリ階層番号
number ctgrLyrNo: KpiIndexEvent.ctgrLyrNo
# 広告案件SEQ
number adProjectSeq: KpiIndexEvent.adProjectSeq
# 日閾値SEQ
number dailyThresholdSeq: KpiIndexEvent.dailyThresholdSeq

# if (予実比  > 日閾値.上限閾値率) 
# アラートメッセージは「over」
state upperLimitState:
    afford:
        KpiIndexEvent:
            pPGoalRate > upperLimitThresholdRate: over
    over:
    watch(KpiIndexEvent):
    persist(categoryGroupId):
        lifetime: today()

rule upperLimitRule:
    upperLimitState.currentState == "over":
    watch(upperLimitState):

# if (予実比 < 日閾値.下限閾値率) 
# アラートメッセージは「short」
state lowerLimitState:
    afford:
        KpiIndexEvent:
            pPGoalRate < lowerLimitThresholdRate: short
    short:
    watch(KpiIndexEvent):
    persist(categoryGroupId):
        lifetime: today()

rule lowerLimitRule:
    lowerLimitState.currentState == "short":
    watch(lowerLimitState):

# テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
# アラートメッセージは「over」
effect msgEffector as effector1:
    checkItemId: 56                       # チェック項目ID
    media: "カテゴリ"                     # カテゴリ
    categoryGroupId: categoryGroupId      # カテゴリグループID
    categorySort: sort                    # カテゴリソート順
    kategoryNo: ctgrLyrNo                 # カテゴリ階層番号
    adProjectSeq: adProjectSeq            # 広告案件SEQ
    adProject: "1"                        # 広告案件
    checkedDateTime: now()                # チェック日時
    dayThresholdId: dailyThresholdSeq     # 日閾値SEQ
    alertMessage: "over"                  # アラートメッセージ:"over"
    watch(upperLimitRule):                # lowerLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。

# テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
# アラートメッセージは「short」
effect msgEffector as effector2:
    checkItemId: 56                       # チェック項目ID
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
