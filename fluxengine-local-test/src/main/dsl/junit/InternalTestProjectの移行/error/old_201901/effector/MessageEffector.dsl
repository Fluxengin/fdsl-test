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
    alertMessage: string          # アラートメッセージ
    categoryGroupId: number       # カテゴリグループID
    categorySort: string          # カテゴリソート順