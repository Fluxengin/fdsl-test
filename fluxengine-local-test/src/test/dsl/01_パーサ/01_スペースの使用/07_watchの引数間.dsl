test 01 1引数のwatchの前、後、前後にスペースを入れる:
    2019/02/06 00:00:01:
        e1:
            code: "code01"
            value: 0.1
        inspect:
            log == "Effector1 c = code01, v = 0.1":
test 02 2引数(OR)のwatchの前、後、前後にスペースを入れる:
    2019/02/06 00:00:01:
        e2:
            code: "code02"
            value: 0.2
        inspect:
            log == "Effector2 c = code02, v = 0.2":
test 03 2引数(AND)のwatchの前、後、前後にスペースを入れる:
    2019/02/06 00:00:01:
        e3:
            code: "code03"
            value: 0.3
        inspect:
            log == "Effector3 c = code03, v = 0.3":
test 04 3引数(OR)のwatchの前、後、前後にスペースを入れる:
    2019/02/06 00:00:01:
        e4:
            code: "code04"
            value: 0.4
        inspect:
            log == "Effector4 c = code04, v = 0.4":
test 05 3引数(AND)のwatchの前、後、前後にスペースを入れる:
    2019/02/06 00:00:01:
        e5:
            code: "code 05"
            value: 0.5
        inspect:
            log == "Effector5 c = code 05, v = 0.5":

