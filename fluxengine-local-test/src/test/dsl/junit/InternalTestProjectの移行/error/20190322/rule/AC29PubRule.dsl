test 1 投入する広告グループIDがKVSに格納しない場合:
    2019/01/23 21:01:00:
        AC29PubEvent:
            media: "YSS"             # 対象媒体
            accountId: 2900001       # アカウントID
            adgroupId: 29001         # 広告グループID
            reportDate: "2019/01/25"   # 実行日
        inspect:
            AddAdGroupInfo.media == "YSS":
            AddAdGroupInfo.adgroupId == 29001:
            AddAdGroupInfo.reportDate == "2019/01/25":
test 2 投入する広告グループIDがKVSに格納済み場合:
    2019/01/23 21:01:00:
        AC29PubEvent:
            media: "YSS"             # 対象媒体
            accountId: 2900001       # アカウントID
            adgroupId: 29001         # 広告グループID
            reportDate: "2019/01/25"   # 実行日
        inspect:
            AddAdGroupInfo.media == "YSS":
            AddAdGroupInfo.adgroupId == 29001:
            AddAdGroupInfo.reportDate == "2019/01/25":
    2019/01/23 21:02:00:
        AC29PubEvent:
            media: "YND"             # 対象媒体
            accountId: 2900001       # アカウントID
            adgroupId: 29002         # 広告グループID
            reportDate: "2019/01/26"   # 実行日
        inspect:
            AddAdGroupInfo.media == "YND":
            AddAdGroupInfo.adgroupId == 29002:
            AddAdGroupInfo.reportDate == "2019/01/26":