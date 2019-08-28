# イベント : KPIアラート-日別目標
export event KpiForecastEvent:
    categoryGroupId: number          # カテゴリグループID
    sort: number                     # カテゴリソート順
    ctgrLyrNo: number                # カテゴリ階層番号
    adProjectSeq: number             # 広告案件SEQ
    dailyThresholdSeq: number        # 日閾値SEQ
    gTtlTrgtVal1: number             # 累計目標値_1
    gTtlTrgtVal2: number             # 累計目標値_2
    gTtlTrgtVal3: number             # 累計目標値_3
    gTtlTrgtVal4: number             # 累計目標値_4
    gTtlTrgtVal5: number             # 累計目標値_5
    gTtlJskVal1: number              # 実績累計値_1
    gTtlJskVal2: number              # 実績累計値_2
    gTtlJskVal3: number              # 実績累計値_3
    gTtlJskVal4: number              # 実績累計値_4
    gTtlJskVal5: number              # 実績累計値_5
    upperLimitThresholdRate: number  # 上限閾値率
    lowerLimitThresholdRate: number  # 下限閾値率
    #upperLowerFlag: string			 #上下限フラグ
