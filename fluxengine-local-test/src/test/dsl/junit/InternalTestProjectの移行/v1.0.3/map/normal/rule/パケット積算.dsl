test 1 通常積算:
    2018/11/10 00:12:34:
        パケットイベント:
            端末ID: "tid112233"
            使用量: 5900
            日時: 2018/11/10 00:00:01
        inspect:
            パケット積算データ.使用量 == 5900:

test 2 上限超過:
    2018/11/10 21:00:00:
        パケットイベント:
            端末ID: "tid112233"
            使用量: 5368709120
            日時: 2018/11/10 00:00:01
        inspect:
            パケット積算データ.使用量 == 5368709120:

test 1 1:
    2018/11/10 00:12:34:
        パケットイベント:
            端末ID: "tid112233"
            使用量: 5900
            日時: 2018/11/10 00:00:01
        inspect:
            case_1_map_result_1 == 3.2:

test 1 2:
    2018/11/10 00:12:34:
        パケットイベント:
            端末ID: "tid112233"
            使用量: 5900
            日時: 2018/11/10 00:00:01
        inspect:
            case_1_map_result_2 == "a":
test 1 3:
    2018/11/10 00:12:34:
        パケットイベント:
            端末ID: "tid112233"
            使用量: 5900
            日時: 2018/11/10 00:00:01
        inspect:
            case_1_map_result_3 == "a=test":
test 1 4:
    2018/11/10 00:12:34:
        パケットイベント:
            端末ID: "tid112233"
            使用量: 5900
            日時: 2018/11/10 00:00:01
        inspect:
            case_1_map_result_4 == 1:

test 1 5:
    2018/11/10 00:12:34:
        test_map: {a1: "variantmap_a1", a2: "variantmap_a2", a3: "variantmap_a3"}
        パケットイベント:
            端末ID: "tid112233"
            使用量: 5900
            日時: 2018/11/10 00:00:01
        inspect:
            case_2_2_map == test_map:

test 1 6:
    2018/11/10 00:12:34:
        test: 1
        test_map: {a1: "m2_a1", a2: "m2_a2", a3: "m2_a3"}
        パケットイベント:
            端末ID: "tid112233"
            使用量: 5900
            日時: 2018/11/10 00:00:01
        inspect:
            case_2_2_map == test_map:

test 1 7:
    2018/11/10 00:12:34:
        test: 1
        test_map:
           a1: "a"
           a2: 1
           a3: 2
           a4: 3
           a5: 4
           a6: 5
        パケットイベント:
            端末ID: "tid112233"
            使用量: 5900
            日時: 2018/11/10 00:00:01
        inspect:
            case_3_map == test_map: