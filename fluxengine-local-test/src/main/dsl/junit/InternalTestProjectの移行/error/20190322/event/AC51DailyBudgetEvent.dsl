# イベント：カテゴリ予算
export event dailyBudgetEvent:
    categoryGroupId: number       # カテゴリグループID
    sort: number                  # カテゴリソート順
    ctgrLyrNo: number             # カテゴリ階層番号
    adProjectSeq: number          # 広告案件SEQ
    dailyThresholdSeq: number     # 日閾値SEQ
    cost: number                  # コストCost(fee込)実績
    upperThreshold: number        # 上限閾値
    lowerthreshold: number        # 下限閾値
    upperThresholdRate: number    # 上限閾値率
    lowerthresholdRate: number    # 下限閾値率
