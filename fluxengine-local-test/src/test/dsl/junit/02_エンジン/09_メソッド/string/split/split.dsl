test 1 後方の空文字列も無視されない:
    2019/03/25 00:00:01:
        inspect:
            s2 == "a,b,,c,,,":

test 2 正規表現は無視される:
    2019/03/25 00:00:01:
        inspect:
            l3.count() == 1: