test 1 dailyBudgetEvent1:
    2019/01/24 00:12:34:
        dailyBudgetEvent:
            categoryGroupId: 510000
            sort: 1
            ctgrLyrNo: 510001
            adProjectSeq: 510002
            dailyThresholdSeq: 510003
            cost: 500
            upperThreshold: 2000
            lowerthreshold: 1000
            upperThresholdRate: 10
            lowerthresholdRate: 60
        inspect:
            upperLimitState.currentState == "over":
            lowerLimitState.currentState == "short":