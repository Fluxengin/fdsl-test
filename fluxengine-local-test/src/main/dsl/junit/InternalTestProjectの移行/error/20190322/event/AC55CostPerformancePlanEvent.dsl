# イベント : カテゴリー累計Cost
export event costPerformancePlanEvent:
    categoryGroupId: number          # カテゴリグループID
    sort: number                     # カテゴリソート順
    ctgrLyrNo: number                # カテゴリ階層番号
    adProjectSeq: number             # 広告案件SEQ
    dailyThresholdSeq: number        # 日閾値SEQ
    gTtlCostPerformance: number      # 累計Cost(+Fee)-実績
    gTtlCostPlan: number             # 累計Cost(+Fee)-計画
    upperLimitThresholdRate: number  # 上限閾値率
    lowerLimitThresholdRate: number  # 下限閾値率
