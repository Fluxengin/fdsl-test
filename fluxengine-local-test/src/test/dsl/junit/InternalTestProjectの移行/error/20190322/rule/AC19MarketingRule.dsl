test 1 s1:
    2019/01/23 21:00:00:
        AC19MarketingEvent:
            media: "YSS"                      # 対象媒体
            campaignId: 10000                 # キャンペーンID
            accountId: 001                    # アカウントID
            campaignName: "campName19"        # 広告ID
            adgroupName: "adName19"           # イベント名
            adgroupId: 19000
        inspect:
            holderState.currentState == "over":