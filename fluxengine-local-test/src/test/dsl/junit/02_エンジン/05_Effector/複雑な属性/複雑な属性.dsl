test 1 複雑な属性も正しく取得できる:
    2019/03/25 00:00:01:
        e1:
            code: "001"
            value: 100
        inspect:
            log == "a11 = 1-1":
            log == "a12 = 001code":
            log == "a21 = 2019/03/25":
            log == "a221 = constant":
            log == "a222 = value100":
            log == "a3 = list,of,string":
            log == "a4 = a41=a41-1,a42=1,a43=2019/03/26,a41=a41-2,a42=2,a43=2019/03/27":
