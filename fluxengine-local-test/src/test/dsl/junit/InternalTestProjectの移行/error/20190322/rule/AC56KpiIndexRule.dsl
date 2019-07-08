test 1 AC56KpiIndexEvent:
    2019/01/26 00:12:34:
        KpiIndexEvent:
            categoryGroupId: 560001          # カテゴリグループID
            sort: 1                          # カテゴリソート順
            ctgrLyrNo: 560002                # カテゴリ階層番号
            adProjectSeq: 560003             # 広告案件SEQ
            dailyThresholdSeq: 560004        # 日閾値SEQ
            gTtlGoalPerformance: 800         # 累計目標実績
            gTtlGoalPlan: 1000               # 累計目標計画
            upperLimitThresholdRate: 79      # 上限閾値率
            lowerLimitThresholdRate: 81      # 下限閾値率
        inspect:
            upperLimitState.currentState == "over":
            lowerLimitState.currentState == "short":