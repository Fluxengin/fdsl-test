test 1 パイプラインが時間が経過しても機能する:
    2018/11/10 00:00:00:
        e: {cd: "01", value: 1}
        inspect:
            n == 1:
    2018/11/10 01:23:45:
        e: {cd: "01", value: 2}
        inspect:
            n == 3:
    2018/11/10 05:43:21:
        e: {cd: "01", value: 97}
        inspect:
            n == 100:
            log != "2018-11-10T05:43:21 message":   # effect なし
    2018/11/10 07:00:00:
        e: {cd: "01", value: 1}
        inspect:
            n == 101:
            log == "2018-11-10T07:00:00 message":   # effect あり
    2018/11/11 00:00:00:
        e: {cd: "01", value: 1}
        inspect:
            n == 1:                                 # 日が変わって初期化
    2018/11/11 00:00:10:
        e: {cd: "02", value: 1}
        inspect:
            n == 1:                                 # cdが変わると永続化のキーが別になる