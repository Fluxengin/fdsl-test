#イベント：キャンペーン
export event ImpGoUpEvent:
    accountId: number             # アカウントID
    campaignId: number            # キャンペーンID
    cost1: number                 # 前日）のキャンペーン.search_impression_share
    cost2: number                 # 前々日）のキャンペーン.search_impression_share
    campaignName: string          # イベント名
