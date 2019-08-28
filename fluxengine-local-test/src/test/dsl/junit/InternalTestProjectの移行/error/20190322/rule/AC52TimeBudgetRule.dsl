test 1 timeBudgetEvent1:
    2019/01/24 00:12:34:
        timeBudgetEvent:
            categoryGroupId: 520000
            sort: 1
            ctgrLyrNo: 520001
            adProjectSeq: 520002
            timeThresholdSeq: 520003
            cost: 500
            upperThreshold: 2000
            lowerthreshold: 1000
            upperThresholdRate: 10
            lowerthresholdRate: 60
        inspect:
            upperLimitState.currentState == "over":
            lowerLimitState.currentState == "short":