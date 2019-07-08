test 1 掲載データ-広告１レコード目投入:
    2019/01/23 21:00:00:
        AC23PrintedAdEvent:
            media: "YSS"                          # 対象媒体
            accountId: 2300001                    # アカウントID
            adgroupId: 1000                       # 広告グループID
            adId: 365                             # 広告ID
            campaignId: 200                       # キャンペーンID
            reportDate: "2019/01/23"              # レポート取得日
            title: "標題００１"                   # タイトル
            headlinePart1: "タイトル０１"         # タイトル1
            headlinePart2: "タイトル０２"         # タイトル2
            description: "アラートチェック２３番" # 説明文
            description1: "備考１"                # 説明文1
            description2: "備考２"                # 説明文2
            finalUrls: "http://wwww.dac.co.jp"    # 最終リンク先URL
        inspect:
            AddAdGroupInfo.media == "YSS":
            AddAdGroupInfo.accountId == 2301001:
            AddAdGroupInfo.adgroupId == 1000:
            AddContentInfo.title == "標題００１":
    2019/01/23 21:01:00:
        AC23PrintedAdEvent:
            media: "YSS"                          # 対象媒体
            accountId: 2300001                    # アカウントID
            adgroupId: 1001                       # 広告グループID
            adId: 365                             # 広告ID
            campaignId: 200                       # キャンペーンID
            reportDate: "2019/01/23"              # レポート取得日
            title: "標題００１"                   # タイトル
            headlinePart1: "タイトル０１"         # タイトル1
            headlinePart2: "タイトル０２"         # タイトル2
            description: "アラートチェック２３番" # 説明文
            description1: "備考１"                # 説明文1
            description2: "備考２"                # 説明文2
            finalUrls: "http://wwww.dac.co.jp"    # 最終リンク先URL
        inspect:
            AddAdGroupInfo.media == "YSS":
            AddAdGroupInfo.accountId == 2301001:
            AddAdGroupInfo.adgroupId == 1001:
            AddContentInfo.title == "標題００１":