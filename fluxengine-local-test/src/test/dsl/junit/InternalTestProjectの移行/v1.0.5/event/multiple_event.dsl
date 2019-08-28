test 1 イベント同時投入:
    2018/11/10 00:12:34:
        e1:
            code: "code_1"
            value: 1
        e2:
            code: "code_2"
            value: 2
        inspect:
            p.exists()  == true:
            p.v == 3:
            log == "■■アラート:2018-11-10T00:12:34 e1とe2を入信しました":

test 1 片方のみ投入:
    2018/11/10 00:12:34:
        e1:
            code: "code_1"
            value: 1
        inspect:
            p.exists()  == false:
    2018/11/10 00:12:33:
        e2:
            code: "code_2"
            value: 2
        inspect:
            p.exists()  == false: