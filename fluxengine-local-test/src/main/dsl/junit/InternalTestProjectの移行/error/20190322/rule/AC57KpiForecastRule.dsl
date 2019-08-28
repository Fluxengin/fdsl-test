# アラートチェックのNo.57に対する処理
# 累積消化アラート-日別
# システムは日次累積予算と実績を比較し、下限と上限の範囲外の場合、アラートを出す。
# イベント : KPI指標により、上限か下限の設定を選択する
import KpiForecastEvent: event/AC57KpiForecastEvent
# エフェクト : MySqlのチェック結果にメッセージ詳細を出力する
import msgEffector: effector/MessageEffector

# 予実比 ＝【カテゴリ一覧_サマリ.累計実績値_n】／ 【カテゴリ一覧_サマリ.累計目標値_n】
# n:1,2,3,4,5
number differenceValue1: KpiForecastEvent.gTtlJskVal1/KpiForecastEvent.gTtlTrgtVal1
number differenceValue2: KpiForecastEvent.gTtlJskVal2/KpiForecastEvent.gTtlTrgtVal2
number differenceValue3: KpiForecastEvent.gTtlJskVal3/KpiForecastEvent.gTtlTrgtVal3
number differenceValue4: KpiForecastEvent.gTtlJskVal4/KpiForecastEvent.gTtlTrgtVal4
number differenceValue5: KpiForecastEvent.gTtlJskVal5/KpiForecastEvent.gTtlTrgtVal5
# 上限閾値率
number upperLimitThresholdRate: KpiForecastEvent.upperLimitThresholdRate*0.01
# 下限閾値率
number lowerLimitThresholdRate: KpiForecastEvent.lowerLimitThresholdRate*0.01
# カテゴリグループID
number categoryGroupId: KpiForecastEvent.categoryGroupId
# カテゴリソート順
number sort: KpiForecastEvent.sort
# カテゴリ階層番号
number ctgrLyrNo: KpiForecastEvent.ctgrLyrNo
# 広告案件SEQ
number adProjectSeq: KpiForecastEvent.adProjectSeq
# 日閾値SEQ
number dailyThresholdSeq: KpiForecastEvent.dailyThresholdSeq

# if (予実比  > 日閾値.上限閾値率) 
# アラートメッセージは「over」
state upperLimitState:
    afford:
        KpiForecastEvent:
            differenceValue1 > upperLimitThresholdRate: over
    over:
    watch(KpiForecastEvent):
    persist(categoryGroupId):
        lifetime: today()

rule upperLimitRule:
    upperLimitState.currentState == "over":
    watch(upperLimitState):

# if (予実比 < 日閾値.下限閾値率) 
# アラートメッセージは「short」
state lowerLimitState:
    afford:
        KpiForecastEvent:
            differenceValue1 < lowerLimitThresholdRate: short
    short:
    watch(KpiForecastEvent):
    persist(categoryGroupId):
        lifetime: today()

rule lowerLimitRule:
    lowerLimitState.currentState == "short":
    watch(lowerLimitState):


# n=2
# if (予実比  > 日閾値.上限閾値率)
# アラートメッセージは「over」
state upperLimitStateTwo:
    afford:
        KpiForecastEvent:
            differenceValue2 > upperLimitThresholdRate: over
    over:
    watch(KpiForecastEvent):
    persist(categoryGroupId):
        lifetime: today()

rule upperLimitRuleTwo:
    upperLimitStateTwo.currentState == "over":
    watch(upperLimitStateTwo):

# if (予実比 < 日閾値.下限閾値率)
# アラートメッセージは「short」
state lowerLimitStateTwo:
    afford:
        KpiForecastEvent:
            differenceValue2 < lowerLimitThresholdRate: short
    short:
    watch(KpiForecastEvent):
    persist(categoryGroupId):
        lifetime: today()

rule lowerLimitRuleTwo:
    lowerLimitStateTwo.currentState == "short":
    watch(lowerLimitStateTwo):


# n=3
# if (予実比  > 日閾値.上限閾値率)
# アラートメッセージは「over」
state upperLimitStateThree:
    afford:
        KpiForecastEvent:
            differenceValue3 > upperLimitThresholdRate: over
    over:
    watch(KpiForecastEvent):
    persist(categoryGroupId):
        lifetime: today()

rule upperLimitRuleThree:
    upperLimitStateThree.currentState == "over":
    watch(upperLimitStateThree):

# if (予実比 < 日閾値.下限閾値率)
# アラートメッセージは「short」
state lowerLimitStateThree:
    afford:
        KpiForecastEvent:
            differenceValue3 < lowerLimitThresholdRate: short
    short:
    watch(KpiForecastEvent):
    persist(categoryGroupId):
        lifetime: today()

rule lowerLimitRuleThree:
    lowerLimitStateThree.currentState == "short":
    watch(lowerLimitStateThree):

# n=4
# if (予実比  > 日閾値.上限閾値率)
# アラートメッセージは「over」
state upperLimitStateFour:
    afford:
        KpiForecastEvent:
            differenceValue4 > upperLimitThresholdRate: over
    over:
    watch(KpiForecastEvent):
    persist(categoryGroupId):
        lifetime: today()

rule upperLimitRuleFour:
    upperLimitStateFour.currentState == "over":
    watch(upperLimitStateFour):

# if (予実比 < 日閾値.下限閾値率)
# アラートメッセージは「short」
state lowerLimitStateFour:
    afford:
        KpiForecastEvent:
            differenceValue4 < lowerLimitThresholdRate: short
    short:
    watch(KpiForecastEvent):
    persist(categoryGroupId):
        lifetime: today()

rule lowerLimitRuleFour:
    lowerLimitStateFour.currentState == "short":
    watch(lowerLimitStateFour):


# n=5
# if (予実比  > 日閾値.上限閾値率)
# アラートメッセージは「over」
state upperLimitStateFive:
    afford:
        KpiForecastEvent:
            differenceValue5 > upperLimitThresholdRate: over
    over:
    watch(KpiForecastEvent):
    persist(categoryGroupId):
        lifetime: today()

rule upperLimitRuleFive:
    upperLimitStateFive.currentState == "over":
    watch(upperLimitStateFive):

# if (予実比 < 日閾値.下限閾値率)
# アラートメッセージは「short」
state lowerLimitStateFive:
    afford:
        KpiForecastEvent:
            differenceValue5 < lowerLimitThresholdRate: short
    short:
    watch(KpiForecastEvent):
    persist(categoryGroupId):
        lifetime: today()

rule lowerLimitRuleFive:
    lowerLimitStateFive.currentState == "short":
    watch(lowerLimitStateFive):




# テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
# アラートメッセージは「over」
effect msgEffector as effector1:
    checkItemId: 57                       # チェック項目ID
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
    checkItemId: 57                       # チェック項目ID
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

# テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
# アラートメッセージは「over」
effect msgEffector as effector3:
    checkItemId: 57                       # チェック項目ID
    media: "カテゴリ"                     # カテゴリ
    categoryGroupId: categoryGroupId      # カテゴリグループID
    categorySort: sort                    # カテゴリソート順
    kategoryNo: ctgrLyrNo                 # カテゴリ階層番号
    adProjectSeq: adProjectSeq            # 広告案件SEQ
    adProject: "1"                        # 広告案件
    checkedDateTime: now()                # チェック日時
    dayThresholdId: dailyThresholdSeq     # 日閾値SEQ
    alertMessage: "over"                  # アラートメッセージ:"over"
    watch(upperLimitRuleTwo):                # upperLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。

# テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
# アラートメッセージは「short」
effect msgEffector as effector4:
    checkItemId: 57                       # チェック項目ID
    media: "カテゴリ"                     # カテゴリ
    categoryGroupId: categoryGroupId      # カテゴリグループID
    categorySort: sort                    # カテゴリソート順
    kategoryNo: ctgrLyrNo                 # カテゴリ階層番号
    adProjectSeq: adProjectSeq            # 広告案件SEQ
    adProject: "1"                        # 広告案件
    checkedDateTime: now()                # チェック日時
    dayThresholdId: dailyThresholdSeq     # 日閾値SEQ
    alertMessage: "short"                 # アラートメッセージ:"short"
    watch(lowerLimitRuleTwo):                # lowerLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。

# テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
# アラートメッセージは「over」
effect msgEffector as effector5:
    checkItemId: 57                       # チェック項目ID
    media: "カテゴリ"                     # カテゴリ
    categoryGroupId: categoryGroupId      # カテゴリグループID
    categorySort: sort                    # カテゴリソート順
    kategoryNo: ctgrLyrNo                 # カテゴリ階層番号
    adProjectSeq: adProjectSeq            # 広告案件SEQ
    adProject: "1"                        # 広告案件
    checkedDateTime: now()                # チェック日時
    dayThresholdId: dailyThresholdSeq     # 日閾値SEQ
    alertMessage: "over"                  # アラートメッセージ:"over"
    watch(upperLimitRuleThree):                # upperLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。

# テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
# アラートメッセージは「short」
effect msgEffector as effector6:
    checkItemId: 57                       # チェック項目ID
    media: "カテゴリ"                     # カテゴリ
    categoryGroupId: categoryGroupId      # カテゴリグループID
    categorySort: sort                    # カテゴリソート順
    kategoryNo: ctgrLyrNo                 # カテゴリ階層番号
    adProjectSeq: adProjectSeq            # 広告案件SEQ
    adProject: "1"                        # 広告案件
    checkedDateTime: now()                # チェック日時
    dayThresholdId: dailyThresholdSeq     # 日閾値SEQ
    alertMessage: "short"                 # アラートメッセージ:"short"
    watch(lowerLimitRuleThree):                # lowerLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。

# テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
# アラートメッセージは「over」
effect msgEffector as effector7:
    checkItemId: 57                       # チェック項目ID
    media: "カテゴリ"                     # カテゴリ
    categoryGroupId: categoryGroupId      # カテゴリグループID
    categorySort: sort                    # カテゴリソート順
    kategoryNo: ctgrLyrNo                 # カテゴリ階層番号
    adProjectSeq: adProjectSeq            # 広告案件SEQ
    adProject: "1"                        # 広告案件
    checkedDateTime: now()                # チェック日時
    dayThresholdId: dailyThresholdSeq     # 日閾値SEQ
    alertMessage: "over"                  # アラートメッセージ:"over"
    watch(upperLimitRuleFour):                # upperLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。

# テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
# アラートメッセージは「short」
effect msgEffector as effector8:
    checkItemId: 57                       # チェック項目ID
    media: "カテゴリ"                     # カテゴリ
    categoryGroupId: categoryGroupId      # カテゴリグループID
    categorySort: sort                    # カテゴリソート順
    kategoryNo: ctgrLyrNo                 # カテゴリ階層番号
    adProjectSeq: adProjectSeq            # 広告案件SEQ
    adProject: "1"                        # 広告案件
    checkedDateTime: now()                # チェック日時
    dayThresholdId: dailyThresholdSeq     # 日閾値SEQ
    alertMessage: "short"                 # アラートメッセージ:"short"
    watch(lowerLimitRuleFour):                # lowerLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。

# テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
# アラートメッセージは「over」
effect msgEffector as effector9:
    checkItemId: 57                       # チェック項目ID
    media: "カテゴリ"                     # カテゴリ
    categoryGroupId: categoryGroupId      # カテゴリグループID
    categorySort: sort                    # カテゴリソート順
    kategoryNo: ctgrLyrNo                 # カテゴリ階層番号
    adProjectSeq: adProjectSeq            # 広告案件SEQ
    adProject: "1"                        # 広告案件
    checkedDateTime: now()                # チェック日時
    dayThresholdId: dailyThresholdSeq     # 日閾値SEQ
    alertMessage: "over"                  # アラートメッセージ:"over"
    watch(upperLimitRuleFive):                # upperLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。

# テーブルフィールド：チェック項目ID、カテゴリグループID、カテゴリソート順、カテゴリ階層番号、広告案件SEQ、チェック日時、日閾値SEQ
# アラートメッセージは「short」
effect msgEffector as effector10:
    checkItemId: 57                       # チェック項目ID
    media: "カテゴリ"                     # カテゴリ
    categoryGroupId: categoryGroupId      # カテゴリグループID
    categorySort: sort                    # カテゴリソート順
    kategoryNo: ctgrLyrNo                 # カテゴリ階層番号
    adProjectSeq: adProjectSeq            # 広告案件SEQ
    adProject: "1"                        # 広告案件
    checkedDateTime: now()                # チェック日時
    dayThresholdId: dailyThresholdSeq     # 日閾値SEQ
    alertMessage: "short"                 # アラートメッセージ:"short"
    watch(lowerLimitRuleFive):                # lowerLimitRuleに対する評価が成立する場合、該当永続化をすぐに実施すること。




