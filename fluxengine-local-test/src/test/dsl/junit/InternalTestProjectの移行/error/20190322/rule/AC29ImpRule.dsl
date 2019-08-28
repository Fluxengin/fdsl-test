test 1 広告グループのレコード投入:
    2019/01/23 21:00:00:
        AC29ImpEvent:
            media: "YSS"             # 対象媒体
            accountId: 2900001       # アカウントID
            adgroupId: 29001         # 広告グループID
            reportDate: "2019/01/25"   # 実行日
        inspect:
            AddAdGroupInfo.media == "YSS":
            AddAdGroupInfo.adgroupId == 29001:
            AddAdGroupInfo.reportDate == "2019/01/25":
    2019/01/23 21:01:00:
        AC29ImpEvent:
            media: "YDN"             # 対象媒体
            accountId: 2900001       # アカウントID
            adgroupId: 29002         # 広告グループID
            reportDate: "2019/01/25"   # 実行日
        inspect:
            AddAdGroupInfo.media == "YSS":
            AddAdGroupInfo.adgroupId == 29002:
            AddAdGroupInfo.reportDate == "2019/01/25":