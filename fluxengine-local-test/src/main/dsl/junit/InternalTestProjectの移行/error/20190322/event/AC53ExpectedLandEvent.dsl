# イベント：日別予算
export event ExpectedLandEvent:
    categoryGroupId: number             # カテゴリグループID
    ctgrLyrNo: number                   # カテゴリ階層番号
    sort: number                        # カテゴリソート順
    adProjectSeq: number                # 広告案件SEQ
    upperLimitThresholdRate: number     # 上限閾値率
    lowerLimitThresholdRate: number     # 下限閾値率
    landPredictionCost: number          # カテゴリ一覧_サマリ.着地予測コスト-実績
    ctgrBudgetGoalValue: number         # カテゴリ一覧_サマリ.カテゴリ予算(fee込)目標値