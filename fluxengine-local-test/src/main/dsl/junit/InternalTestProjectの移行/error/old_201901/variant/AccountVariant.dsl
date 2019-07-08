export struct account<condition>:
    accountSeq: number
    adComponentSeq: number
    adComponentName: string
    adProjectSeq: number
    upperLimitThreshold: number
    lowerLimitThreshold: number
    upperLimitThresholdRate: number
    lowerLimitThresholdRate: number
    dataReferencePeriod: date
    get(condition):