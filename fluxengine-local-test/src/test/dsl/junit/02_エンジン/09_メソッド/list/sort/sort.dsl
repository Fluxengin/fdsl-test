test 1 数値のソート:
    2019/03/20 00:00:01:
        inspect:
            l2.join(",") == "-2147483649,-2147483648,-0.1,2,2,5,100000000000":

test 2 文字列のソート:
    2019/03/20 00:00:01:
        inspect:
            l4.join(",") == "a,a ,a a,aa,aaa":

test 3 日時のソート:
    2019/03/20 00:11:22:
        inspect:
            l6.join(",") == "2018-12-31T23:59:59,2019-01-01T09:50:01,2019-01-01T10:00,2019-03-20T00:11:22":