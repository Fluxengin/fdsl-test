test 1 日付型のパラメタにtodayが使える:
    2019/03/28 00:00:01:
        get 日付<today()>:
        inspect:
            日付 == "2019-03-28":

test 2 日付型のパラメタにリテラルが使える:
    2019/03/28 00:00:01:
        get 日付<2019/03/29>:
        inspect:
            日付 == "2019-03-29":
