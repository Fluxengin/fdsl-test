test 1 分岐1つめ:
    2019/03/13 00:00:00:
        イベント1:
            success: true
            player_info:
                name: "test name"
                age: 20
                birthday: 1999/01/01
                registration_datetime: 2019/01/01 00:00:00
                extra_info: []
            memo:
                - "memo1"
                - "memo2"
        inspect:
            n1 == 1:
            s1 == "メモあり":

test 2 分岐2つめ:
    2019/03/13 00:00:00:
        イベント1:
            success: false
            player_info:
                name:
                age:
                birthday:
                registration_datetime:
                extra_info: []
            memo: []
        inspect:
            n1 == 2:
            s1 == "メモなし":