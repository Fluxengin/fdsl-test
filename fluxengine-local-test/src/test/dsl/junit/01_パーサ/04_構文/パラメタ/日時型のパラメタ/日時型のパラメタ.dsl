test 1 日時型のパラメタにnowが使える:
    2019/03/28 00:00:01:
        get 日時<now()>:
        inspect:
            日時 == "2019-03-28 00_00_01":

test 2 日時型のパラメタにリテラルが使える:
    2019/03/28 00:00:01:
        get 日時<2019/03/29 12:34:56>:
        inspect:
            日時 == "2019-03-29 12_34_56":
