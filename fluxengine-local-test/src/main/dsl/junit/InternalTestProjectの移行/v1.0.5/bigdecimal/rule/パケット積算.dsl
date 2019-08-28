import パケットイベント: event/パケットイベント
import ユーザー情報<パケットイベント.端末ID>: variant/ユーザー情報
import パケット積算データ<"test">: persister/パケット積算データ
import valueEffector: effector/BigDecimalTestEffector
import メール送信: effector/ユーザー通知

number n0: 1
number n1_1: -9223372036854775808 - 1
number n1_2: 9223372036854775807 + 1
number n1_3: -9223372036854775808 - 1
number n1_4: 9223372036854775807 + 1


bool b1:
    n1_1 == パケットイベント.使用量: true
    else: false


effect valueEffector as valueEffector1:
    intValue: n1_1
    longValue: n1_1
    floatValue: n1_1
    doubleValue: n1_1
    intObject: n1_1
    longObject: n1_1
    floatValue: n1_1
    doubleObject: n1_1
    bigIntegerObject: n1_1
    bigDecimalObject: n1_1
    watch(パケットイベント):

effect valueEffector as valueEffector2:
    intValue: n1_2
    longValue: n1_2
    floatValue: n1_2
    doubleValue: n1_2
    intObject: n1_2
    longObject: n1_2
    floatValue: n1_2
    doubleObject: n1_2
    bigIntegerObject: n1_2
    bigDecimalObject: n1_2
    watch(パケットイベント):

effect valueEffector as valueEffector3:
    intValue: n1_3
    longValue: n1_3
    floatValue: n1_3
    doubleValue: n1_3
    intObject: n1_3
    longObject: n1_3
    floatValue: n1_3
    doubleObject: n1_3
    bigIntegerObject: n1_3
    bigDecimalObject: n1_3
    watch(パケットイベント):

effect valueEffector as valueEffector4:
    intValue: n1_4
    longValue: n1_4
    floatValue: n1_4
    doubleValue: n1_4
    intObject: n1_4
    longObject: n1_4
    floatValue: n1_4
    doubleObject: n1_4
    bigIntegerObject: n1_4
    bigDecimalObject: n1_4
    watch(パケットイベント):

#--------------------------------------------------------------------------------------------

list l: [9223372036854775808, -9223372036854775809]
list mapList: [ {a1: "x", a2: 9223372036854775808}, {a1: "y", a2: -9223372036854775809} ]
string joinedList: l.sort().join(",")
number sumList: l.sum()
number avgList: l.avg()
number sumMapList: mapList.sum(a2)
number avgMapList: mapList.avg(a2)
list expectedSortedList: [-9223372036854775809, 9223372036854775808]
list sortedList: l.sort()
number roundResult: round(9223372036854775808.35, 1)

# NG
string filterMapList: mapList.filter(a2 == -9223372036854775809).toString()

bool b2_1:
    joinedList == "-9223372036854775809,9223372036854775808": true
    else: false

bool b2_2:
    sumList == -1: true
    else: false

bool b2_3:
    avgList == -0.5: true
    else: false

bool b2_4:
    sumMapList == -1: true
    else: false

bool b2_5:
    avgMapList == -0.5: true
    else: false

bool b2_6:
    filterMapList == "[{a1=y, a2=-9223372036854775809}]": true
    else: false

bool b2_7:
    sortedList == expectedSortedList: true
    else: false

bool b2_8:
    roundResult == 9223372036854775808.4: true
    else: false


#--------------------------------------------------------------------------------------------

number n3:  -9223372036854775809 * -1
number n3_1:  -9223372036854775809 + 1
number n3_2:  -9223372036854775809 - 1
number n3_3:  -9223372036854775809 * 1
number n3_4:  -9223372036854775809 / 1
number n3_5: -9223372036854775809 + 9223372036854775808
number n3_6:  9223372036854775808 + 1
number n3_7:  9223372036854775808 - 1
number n3_8:  9223372036854775808 * 1
number n3_9:  9223372036854775808 / 1
number n3_10:  1 / -9223372036854775809
number n3_11:  1 / 9223372036854775808

bool b3_1:
    n3_1 == -9223372036854775808: true
    else: false

bool b3_2:
    n3_2 == -9223372036854775810: true
    else: false

bool b3_3:
    n3_3 == -9223372036854775809: true
    else: false

bool b3_4:
    n3_4 == -9223372036854775809: true
    else: false

bool b3_5:
    n3_5 == -1: true
    else: false

bool b3_6:
    n3_6 == 9223372036854775809: true
    else: false

bool b3_7:
    n3_7 == 9223372036854775807: true
    else: false

bool b3_8:
    n3_8 == 9223372036854775808: true
    else: false

bool b3_9:
    n3_9 == 9223372036854775808: true
    else: false

bool b3_10:
    n3_10 < 0: true
    else: false

bool b3_11:
    n3_11 > 0: true
    else: false
#--------------------------------------------------------------------------------------------

bool b4_1:
    (9223372036854775808 + 1) > 9223372036854775808 && (9223372036854775808 + 1) > 9223372036854775808 && (-9223372036854775809 - 1) < -9223372036854775809 && (-9223372036854775809 - 1) < -9223372036854775809 : true
    else: false

bool b4_2:
    (-9223372036854775809 - 1) < -9223372036854775809 && (-9223372036854775809 - 1) < -9223372036854775809 && (9223372036854775808 + 1) > 9223372036854775808 && (9223372036854775808 + 1) > 9223372036854775808: true
    else: false

bool b4_3:
    (9223372036854775808/ 9223372036854775808/ 9223372036854775808) > (-9223372036854775809/ -9223372036854775809/ -9223372036854775809): true
    else: false

#--------------------------------------------------------------------------------------------
number n5: パケット積算データ.使用量1 + パケット積算データ.使用量2 + パケット積算データ.使用量3
persist パケット積算データ:
    使用量1: 9223372036854775808
    使用量2: n5
    使用量3: パケットイベント.使用量
    watch(パケットイベント):

#超えていたらメールを出す
rule パケット積算:
    n5 >= ユーザー情報.パケット上限:
    watch(パケットイベント):

effect メール送信:
    値: n5
    ユーザーID: "test"
    日時: now()
    メッセージ: "パケット使用量を超過しました。"
    watch(パケット積算):

state 状態遷移:
    s1:
        パケットイベント: s2
    s2:
        パケットイベント:
                  パケットイベント.使用量 == -9223372036854775809: s3
    s3:
        パケットイベント:
                  パケットイベント.使用量 > -9223372036854775809: s2
    s4:
    watch(パケットイベント):
    persist(ユーザー情報.ユーザーID):
        lifetime: today()

rule 状態遷移ルール:
    状態遷移.currentState == s3:
    watch(状態遷移):

effect メール送信 as 状態遷移アラート:
    値:
    ユーザーID: "test"
    日時: now()
    メッセージ: "状態がs3になりました。"
    watch(状態遷移ルール):