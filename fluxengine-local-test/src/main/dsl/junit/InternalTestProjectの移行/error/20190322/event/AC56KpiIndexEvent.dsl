# イベント : カテゴリー累計Cost
export event KpiIndexEvent:
    categoryGroupId: number          # カテゴリグループID
    sort: number                     # カテゴリソート順
    ctgrLyrNo: number                # カテゴリ階層番号
    adProjectSeq: number             # 広告案件SEQ
    dailyThresholdSeq: number        # 日閾値SEQ
    gTtlGoalPerformance: number      # 累計目標実績
    gTtlGoalPlan: number             # 累計目標計画
    upperLimitThresholdRate: number  # 上限閾値率
    lowerLimitThresholdRate: number  # 下限閾値率
