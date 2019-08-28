#イベント：カテゴリ
export event LandingForecastEvent:
    categoryGroupId: number           # カテゴリグループID
    ctgrLyrNo: number                 # カテゴリ階層番号
    sort: number                      # カテゴリソート順
    adProjectSeq: number              # 広告案件SEQ
    upperLimitThresholdRate: number   # 上限閾値率
    lowerLimitThresholdRate: number   # 下限閾値率
    landCost: number                  # カテゴリ_時間累計実績サマリ.着地コスト
    cost: number                      # カテゴリ_当日実績.コストCost(Fee込)計画