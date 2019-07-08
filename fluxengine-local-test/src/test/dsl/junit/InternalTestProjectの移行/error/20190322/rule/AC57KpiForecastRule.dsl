test 1 AC57KpiForecastEvent:
    2019/01/26 00:12:34:
        KpiForecastEvent:
            categoryGroupId: 560001          # カテゴリグループID
            sort: 1                          # カテゴリソート順
            ctgrLyrNo: 570002                # カテゴリ階層番号
            adProjectSeq: 570003             # 広告案件SEQ
            dailyThresholdSeq: 570004        # 日閾値SEQ
            gTtlTrgtVal1: 570005             # 累計目標値_1
            gTtlTrgtVal2: 570006             # 累計目標値_2
            gTtlTrgtVal3: 570007             # 累計目標値_3
            gTtlTrgtVal4: 570008             # 累計目標値_4
            gTtlTrgtVal5: 570009             # 累計目標値_5
            gTtlJskVal1: 570010              # 実績累計値_1
            gTtlJskVal2: 570011              # 実績累計値_2
            gTtlJskVal3: 570012              # 実績累計値_3
            gTtlJskVal4: 570013              # 実績累計値_4
            gTtlJskVal5: 570014              # 実績累計値_5
            upperLimitThresholdRate: 79      # 上限閾値率
            lowerLimitThresholdRate: 81      # 下限閾値率
        inspect:
            upperLimitState.currentState == "over":
            lowerLimitState.currentState == "short":