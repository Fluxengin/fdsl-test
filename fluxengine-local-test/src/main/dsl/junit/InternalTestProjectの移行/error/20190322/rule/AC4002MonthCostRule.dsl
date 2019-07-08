# イベント：月別予算
import monthCostEvent: event/AC4002MonthCostEvent
# アラートチェック番号によって、MySqlの通知メッセージからメッセージ詳細を取得する。
#import alertMsg<4002>: variant/AlertMsgVariant
# 対象アカウント取得処理（Javaプラグイン）
import account<condition>: variant/AccountVariant
# 月別予算取得処理（Javaプラグイン）
import monthBudget<condition2>: variant/MonthBudgetVariant
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector

map condition:
    {
      checkNo: "4002",
      execDay: today(),
      accountId: monthCostEvent.accountId,
    }

map condition2:
    {
      checkNo: "4002",
      accountId: monthCostEvent.accountId,
      media: monthCostEvent.media,
      componentTypeId: 2
    }

# 月別予算.月額の予算
number monthlyBudgetTotal: monthBudget
# 月別予算.コスト
number costTotal: monthCostEvent.cost
# （月別予算.月額の予算－cost（合計後））／月別予算.月の額予算
number monthAmount: monthlyBudgetTotal-costTotal
number costRate: (monthlyBudgetTotal-costTotal)/monthlyBudgetTotal
# 下限閾値率
number lowlimitRate: account.lowerLimitThresholdRate*0.01

# 予算状態
state budgetState:
    afford:
        monthCostEvent:
            # 下記の条件に合うと、状態を変わる。
            # （monthly_budget（合計後）－cost（合計後））／monthly_budget（合計後）＜＝下限閾値率
            costRate <= lowlimitRate: over
    over:
    watch(monthCostEvent):                       # 月別予算イベントが投入する場合、該当評価を行います。
    persist(monthCostEvent.accountId):           # アカウントIDがキーとして永続化する。
        lifetime: today()

# 現在状態が超過の場合、アラートチェック結果を格納すること。
rule budgetRule:
    budgetState.currentState == "over":
    watch(budgetState):

string alertMsg1: アカウントの残高が{0}を切っております。予算増額をお願いいたします。
string alertMsg2: alertMsg1.format(monthAmount)

#テーブルフィールド：チェック日時、チェック項目ID、アカウントSEQ
effect msgEffector:
    checkedDateTime: now()                     # チェック日時
    checkItemId: 4002                          # チェック項目ID
    media: monthCostEvent.media                # 媒体
    accountId: monthCostEvent.accountId        # アカウントID
    alertMessage: alertMsg2                    # ALERT MESSAGE
    watch(budgetRule):                         # budgetRuleに対する評価が成立する場合、MySqlに該当アラートチェックのチェック結果を登録する。