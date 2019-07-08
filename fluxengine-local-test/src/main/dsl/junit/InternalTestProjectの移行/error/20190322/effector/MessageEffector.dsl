#エフェクト：チェック結果（MySqlに保存する）
export effector msgEffector:
    checkItemId: number           # チェック項目ID
    media: string                 # 媒体
    adProjectSeq: number          # 広告案件SEQ
    adProject: string             # 広告案件
    accountId: number             # アカウントID
    checkedDateTime: datetime     # チェック日時
    campaignId: number            # キャンペーンID
    adGroupId: number             # 広告グループID
    adId: number                  # 広告ID
    keywordId: number             # キーワードID
    alertMessage: string          # アラートメッセージ
    categoryGroupId: number       # カテゴリグループID
    categorySort: string          # カテゴリソート順
    kategoryNo: number            # カテゴリ階層番号
    timeThresholdId: number       # 時間閾値SEQ
    dayThresholdId: number        # 日閾値SEQ
    dayKpiThresholdId: number     # カテゴリKPI日閾値SEQ
