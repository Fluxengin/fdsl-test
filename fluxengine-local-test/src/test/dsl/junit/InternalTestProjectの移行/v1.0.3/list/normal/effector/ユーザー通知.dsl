test 1 メール送信正常:
    2018/11/10 00:12:34:
        effect メール送信:
            ユーザーID: "uid12345"
            日時: now()
            メッセージ: "メールメッセージ"
        inspect:
            log == "■■アラート:2018-11-10T00:12:34 メールメッセージ":

test 2 エラーテスト:
    2018/11/10 00:12:34:
        effect メール送信:
            ユーザーID: "uid12345"
            日時: "exception"
            メッセージ: "エラー：メールメッセージ"
        inspect:
            error == "IllegalArgumentException":