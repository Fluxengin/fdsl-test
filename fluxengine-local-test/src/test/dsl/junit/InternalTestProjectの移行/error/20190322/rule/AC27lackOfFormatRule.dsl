test 1 s1:
    2019/01/23 21:00:00:
        AC27lackOfFormatEvent:
            media: "YDN"                                               # 広告グループID
            campaignId: 1                                              # アカウントID
            accountId: 2743021                                           # 対象媒体
            adgroupId: 1                                               # キャンペーンID
            adType: "1"                                                # 広告種類
            imageCreativeSize: "1"                                     # サイズ
            multiAssetResponsiveDisplayAdSquareMarketingImages: "2"
            multiAssetResponsiveDisplayAdMarketingImages: "3"
            campaignName: "camp27"                                     # 広告ID
            adgroupName: "adname27"                                    # イベント名
        inspect:
            holderState.currentState == "over":
    2019/01/23 22:00:00:
        AC27lackOfFormatEvent:
            media: "YDN"                                               # 広告グループID
            campaignId: 1                                              # アカウントID
            accountId: 2743021                                         # 対象媒体
            adgroupId: 1                                               # キャンペーンID
            adType: "1"                                                # 広告種類
            imageCreativeSize: "1"                                     # サイズ
            multiAssetResponsiveDisplayAdSquareMarketingImages: "2"
            multiAssetResponsiveDisplayAdMarketingImages: "3"
            campaignName: "camp27"                                     # 広告ID
            adgroupName: "adname27"                                    # イベント名
        inspect:
            holderState.currentState == "over":