test 1 AC55CostPerformancePlanEvent:
    2019/01/26 00:12:34:
        costPerformancePlanEvent:
            categoryGroupId: 550001          # カテゴリグループID
            sort: 1                          # カテゴリソート順
            ctgrLyrNo: 550002                # カテゴリ階層番号
            adProjectSeq: 550003             # 広告案件SEQ
            dailyThresholdSeq: 550004        # 日閾値SEQ
            gTtlCostPerformance: 800         # 累計Cost(+Fee)-実績
            gTtlCostPlan: 1000               # 累計Cost(+Fee)-計画
            upperLimitThresholdRate: 79      # 上限閾値率
            lowerLimitThresholdRate: 81      # 下限閾値率
        inspect:
            upperLimitState.currentState == "over":
            lowerLimitState.currentState == "short":