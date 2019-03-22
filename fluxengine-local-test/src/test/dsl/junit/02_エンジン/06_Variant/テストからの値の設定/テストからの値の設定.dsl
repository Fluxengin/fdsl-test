test 1 結合テスト:
    2019/03/22 00:00:01:
        e1:
            value: 1000
        inspect:
            n1 == 1:
            s2 == "ON":
    2019/03/22 00:00:02:
        e1:
            value: -100
        inspect:
            n1 == 0:
            s2 == "OFF":

test 2 s2の単体テスト:
    2019/03/22 00:00:01:
        n1: 1
        inspect:
            s2 == "ON":
    2019/03/22 00:00:02:
        n1: 0
        inspect:
            s2 == "OFF":
    2019/03/22 00:00:03:
        n1: 2
        inspect:
            s2 == "OFF":
