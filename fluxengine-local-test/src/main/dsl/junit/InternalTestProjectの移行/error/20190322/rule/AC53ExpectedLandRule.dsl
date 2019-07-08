# アラートチェックのNo.53に対する処理
import ExpectedLandEvent: event/AC53ExpectedLandEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector
# 着地率 ＝【[DM]カテゴリ一覧_サマリ.着地予測コスト-実績】／ 【カテゴリ一覧_サマリ.カテゴリ予算(fee込)目標値】
number onTheRate: ExpectedLandEvent.landPredictionCost/ExpectedLandEvent.ctgrBudgetGoalValue
number upperThresholdRate: ExpectedLandEvent.upperLimitThresholdRate*0.01
number lowerthresholdRate: ExpectedLandEvent.lowerLimitThresholdRate*0.01

# if (着地率  > カテゴリチェック項目詳細設定.上限閾値率)
# アラートメッセージは「over」
state upperLimitState:
    afford:
        ExpectedLandEvent:
            onTheRate > upperThresholdRate: over
    over:
    watch(ExpectedLandEvent):
    persist(ExpectedLandEvent.categoryGroupId):
        lifetime: today()

rule upperLimitRule:
    upperLimitState.currentState == "over":
    watch(upperLimitState):

# if (着地率 < カテゴリチェック項目詳細設定.下限閾値率)
# アラートメッセージは「short」
state lowerLimitState:
    afford:
        ExpectedLandEvent:
            onTheRate < lowerthresholdRate: short
    short:
    watch(ExpectedLandEvent):
    persist(ExpectedLandEvent.categoryGroupId):
        lifetime: today()

rule lowerLimitRule:
    lowerLimitState.currentState == "short":
    watch(lowerLimitState):

#テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時
# アラートメッセージは「over」
effect msgEffector as effector1:
    checkItemId: 53                                       # チェック項目ID
    media: "カテゴリ"                                     # カテゴリ
    categoryGroupId: ExpectedLandEvent.categoryGroupId    # カテゴリグループID
    kategoryNo: ExpectedLandEvent.ctgrLyrNo               # カテゴリソート順
    categorySort: ExpectedLandEvent.sort                  # カテゴリ階層番号
    adProjectSeq: ExpectedLandEvent.adProjectSeq          # 広告案件SEQ
    adProject: "1"                                        # 広告案件
    checkedDateTime: now()                                # チェック日時
    alertMessage: "over"                                  # アラートメッセージ:"over" 
    watch(upperLimitRule):                                # upperLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。

#テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時
# アラートメッセージは「short」
effect msgEffector as effector2:
    checkItemId: 53                                       # チェック項目ID
    media: "カテゴリ"                                     # カテゴリ
    categoryGroupId: ExpectedLandEvent.categoryGroupId    # カテゴリグループID
    kategoryNo: ExpectedLandEvent.ctgrLyrNo               # カテゴリソート順
    adProjectSeq: ExpectedLandEvent.adProjectSeq          # カテゴリ階層番号
    categorySort: ExpectedLandEvent.sort                  # 広告案件SEQ
    adProject: "1"                                        # 広告案件
    checkedDateTime: now()                                # チェック日時
    alertMessage: "short"                                 # アラートメッセージ:"short" 
    watch(lowerLimitRule):                                # lowerLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。
