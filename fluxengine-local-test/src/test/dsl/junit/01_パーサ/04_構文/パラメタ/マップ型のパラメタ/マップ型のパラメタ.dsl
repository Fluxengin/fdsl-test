test 1 マップもパラメタにとれる:
    2019/03/28 00:00:01:
        map data:
            key1: "abc"
            key2: 123
            key3: true
        get stringified<data, " = ", ", ">:
        inspect:
            stringified == "key1 = abc, key2 = 123, key3 = true":