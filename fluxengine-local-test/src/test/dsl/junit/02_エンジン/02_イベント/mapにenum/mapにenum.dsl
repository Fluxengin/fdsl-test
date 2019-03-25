test 1 mapの中でもenumが使える:
    2019/03/22 00:00:01:
        ユーザイベント:
            userId: 1001
            userName: "ユーザ1"
            availableFunctions:
                機能1: FLAG_ON
                機能2: FLAG_OFF
                機能3: FLAG_ON
            状態: FLAG_ON
        inspect:
            s1 == "機能1ON":