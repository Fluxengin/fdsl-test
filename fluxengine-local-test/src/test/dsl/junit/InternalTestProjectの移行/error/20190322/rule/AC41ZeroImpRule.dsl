test 1 s1:
    2019/01/23 21:00:00:
        AC41ZeroImpEvent:
            media: "YSS"                # 対象媒体
            campaignId: 10001           # キャンペーンID
            accountId: 001              # アカウントID
            adGroupId: 100001           # 広告グループID
            adId: 41                    # 広告ID
            campaignName: "campname"    # イベント名
            adgroupName: "adgroupname"  # 広告チーム名
            adName: "adname"            # 広告の名称 
            dataReferencePeriod: 5
        inspect:
            holderState.currentState == "over":