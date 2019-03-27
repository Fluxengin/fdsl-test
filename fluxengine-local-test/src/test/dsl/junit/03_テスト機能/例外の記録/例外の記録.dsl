test 1 チェック例外を取得できる:
    2019/03/27 00:00:01:
        effect f1:
            code: "IO"
        inspect:
            error == "IOException":

test 2 非チェック例外を取得できる:
    2019/03/27 00:00:01:
        effect f1:
            code: "Null"
        inspect:
            error == "NullPointerException":

test 3 親クラスの場合はfalseになる:
    2019/03/27 00:00:01:
        effect f1:
            code: "Null"
        inspect:
            error != "RuntimeException":
