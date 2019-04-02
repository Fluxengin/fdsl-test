# runnerはどうやらサポートしていないようなので、
# 本テストは@Functionで名前を付けていない関数の
# 呼び出しがうまくいかない不具合を確認するためのテストとする

#runner:
#    function:
#        twice(3): 9

test 1 n1はtwiceに1足した値:
    2019/03/22 00:00:01:
        inspect:
            n1 == 10:
