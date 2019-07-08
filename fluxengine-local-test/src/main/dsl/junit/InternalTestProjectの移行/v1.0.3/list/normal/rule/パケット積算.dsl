import パケットイベント: event/パケットイベント
import ユーザー情報<パケットイベント.端末ID>: variant/ユーザー情報
import externalList: variant/ユーザー情報
import パケット積算データ<ユーザー情報.ユーザーID>: persister/パケット積算データ
import メール送信 as 再送信: effector/ユーザー通知


### NG ###
## Expected: should be parser exception.   Actual: behavior discrepancy.
# list l: [1, 2, 3]
# number n: l.sum                  runtime exception
# number n: l.sum(                 parser error
# number n: l.sum()(               working
# number n: l.sum()()              runtime exception

list test_list: [1, 2, 3]
### OK ###
# regular pattern (Supported)
number a: 1
number b: 3
number c: 1
list case_1_list: [1, 2, 3]
list case_2_list: [a, b, c]
list case_3_list: [ {a1: "x", a2: 1}, {a1: "y", a2: 2} ]
list case_4_list: [ {a1: "x", a2: a}, {a1: "y", a2: b} ]
list case_5_listl: [1,    2,    3]
number case_1_result_1: case_1_list.sum()
number case_1_result_2: case_1_list.avg()
number case_2_result_1: case_2_list.sum()
number case_2_result_2: case_2_list.avg()
number case_3_result_1: case_3_list.sum(a2)
number case_3_result_2: case_3_list.avg(a2)
number case_4_result_1: case_4_list.sum(a2)
number case_4_result_2: case_4_list.avg(a2)

bool case_1_result_3:
   case_1_list.sum() == 6: true
   else: false

bool case_1_result_4:
   case_1_list.avg() == 2: true
   else: false

bool case_3_result_3:
   case_3_list.sum(a2) == 3: true
   else: false

bool case_3_result_4:
   case_3_list.avg(a2) == 1.5: true
   else: false


## From FDSL Specific
list standard_case_1_list: [3, 1, 2]
list standard_case_1_result: standard_case_1_list.sort()        # m = [1, 2, 3]

list standard_case_2_list: [1, 2, 3]
string standard_case_2_result: standard_case_2_list.join(",")   # s = "1,2,3"

list standard_case_3_list: [3, 1, 2]
list standard_case_3_result: standard_case_3_list.append(4)             # m = [3, 1, 2, 4]

list standard_case_4_list: [7, 8]
list standard_case_4_result: standard_case_3_list.append(standard_case_4_list)           # mm = [3, 1, 2, 7, 8]

list standard_case_5_result: standard_case_3_result.append(standard_case_4_list).sort()     # x = [1, 2, 3, 4, 7, 8]

list standard_case_6_list:
    - {code: 1, name: "aaa", kind: "a"}
    - {code: 2, name: "bbb", kind: "b"}
    - {code: 3, name: "ccc", kind: "a"}

list standard_case_6_result_1: standard_case_6_list.filter(kind == "a")               # code 1, 3 の要素を返す
list standard_case_6_result_2: standard_case_6_list.filter(code > 1)                  # code 2, 3 の要素を返す
list standard_case_6_result_3: standard_case_6_list.filter(kind == "a" && code < 3)   # code 1 の要素を返す
list standard_case_6_result_4: standard_case_6_list

bool standard_case_1_result_bool:
    standard_case_1_result[0] == 1 : true
    else: false

number standard_case_1_result_in_round: round(standard_case_1_result[0])
number standard_case_1_result_of_count: standard_case_1_result.count()

rule サイズ確認:
    standard_case_1_result.count() == 3:
    watch(パケットイベント):

number standerd_case_7_result: externalList.count()

list standard_case_8_list: [ {a1: "x", a2: 1}, {a1: "y", a2: 2} ]
# 比較演算子
string standard_case_8_result_1: standard_case_8_list.filter(a1 == "==").toString()
string standard_case_8_result_2: standard_case_8_list.filter(a1 == "!=").toString()
string standard_case_8_result_3: standard_case_8_list.filter(a1 == "<").toString()
string standard_case_8_result_4: standard_case_8_list.filter(a1 == "<=").toString()
string standard_case_8_result_5: standard_case_8_list.filter(a1 == ">").toString()
string standard_case_8_result_6: standard_case_8_list.filter(a1 == ">=").toString()

# 算術演算子
string standard_case_8_result_7: standard_case_8_list.filter(a1 == "+").toString()
string standard_case_8_result_8: standard_case_8_list.filter(a1 == "-").toString()
string standard_case_8_result_9: standard_case_8_list.filter(a1 == "*").toString()
string standard_case_8_result_10: standard_case_8_list.filter(a1 == "/").toString()
string standard_case_8_result_11: standard_case_8_list.filter(a1 == "%").toString()

# インクリメント・デクリメント演算子
string standard_case_8_result_12: standard_case_8_list.filter(a1 == "++").toString()
string standard_case_8_result_13: standard_case_8_list.filter(a1 == "--").toString()

# ビット演算子
string standard_case_8_result_14: standard_case_8_list.filter(a1 == "&").toString().toString()
string standard_case_8_result_15: standard_case_8_list.filter(a1 == "|").toString().toString()
string standard_case_8_result_16: standard_case_8_list.filter(a1 == "^").toString().toString()
string standard_case_8_result_17: standard_case_8_list.filter(a1 == "~").toString().toString()

# シフト演算子
string standard_case_8_result_18: standard_case_8_list.filter(a1 == "<<").toString()
string standard_case_8_result_19: standard_case_8_list.filter(a1 == ">>").toString()
string standard_case_8_result_20: standard_case_8_list.filter(a1 == ">>>").toString()

# 代入演算子
string standard_case_8_result_21: standard_case_8_list.filter(a1 == "=").toString()
string standard_case_8_result_22: standard_case_8_list.filter(a1 == "+=").toString()
string standard_case_8_result_23: standard_case_8_list.filter(a1 == "-=").toString()
string standard_case_8_result_24: standard_case_8_list.filter(a1 == "*=").toString()
string standard_case_8_result_25: standard_case_8_list.filter(a1 == "/=").toString()
string standard_case_8_result_26: standard_case_8_list.filter(a1 == "%=").toString()
string standard_case_8_result_27: standard_case_8_list.filter(a1 == "&=").toString()
string standard_case_8_result_28: standard_case_8_list.filter(a1 == "|=").toString()
string standard_case_8_result_29: standard_case_8_list.filter(a1 == "^=").toString()
string standard_case_8_result_30: standard_case_8_list.filter(a1 == "<<=").toString()
string standard_case_8_result_31: standard_case_8_list.filter(a1 == ">>=").toString()

# 論理演算子
string standard_case_8_result_32: standard_case_8_list.filter(a1 == "&&").toString()
string standard_case_8_result_33: standard_case_8_list.filter(a1 == "||").toString()
string standard_case_8_result_34: standard_case_8_list.filter(a1 == "!").toString()

# 優先順位
string standard_case_8_result_35: standard_case_8_list.filter(a1 == "(").toString()
string standard_case_8_result_36: standard_case_8_list.filter(a1 == ")").toString()
string standard_case_8_result_37: standard_case_8_list.filter(a1 == "()").toString()

# index指定
string standard_case_8_result_38: standard_case_8_list.filter(a1 == "[]").toString()

# メンバー指定
string standard_case_8_result_39: standard_case_8_list.filter(a1 == "a.a").toString()

list standard_case_9_list: [ {a1: "x", a2: 1}, {a1: "y", a2: 2} ]
string standard_case_9_result_1: standard_case_9_list.filter(a1 == "<a>").toString()
string standard_case_9_result_2: standard_case_9_list.filter(a1 == "<").toString()
string standard_case_9_result_3: standard_case_9_list.filter(a1 == "<=").toString()

list resultList:
       - {case_1_result_1: case_1_result_1}
       - {case_1_result_2: case_1_result_2}
       - {case_1_result_3: case_1_result_3}
       - {case_1_result_4: case_1_result_4}
       - {case_2_result_1: case_2_result_1}
       - {case_2_result_2: case_2_result_2}
       - {case_3_result_1: case_3_result_1}
       - {case_3_result_2: case_3_result_2}
       - {case_3_result_3: case_3_result_3}
       - {case_3_result_4: case_3_result_4}
       - {case_4_result_1: case_4_result_1}
       - {case_4_result_2: case_4_result_2}
string results: resultList.join(",")

list standardResultList:
       - {standard_case_1_result: standard_case_1_result}
       - {standard_case_2_result: standard_case_2_result}
       - {standard_case_3_result: standard_case_3_result}
       - {standard_case_4_result: standard_case_4_result}
       - {standard_case_5_result: standard_case_5_result}
       - {standard_case_6_result_1: standard_case_6_result_1}
       - {standard_case_6_result_2: standard_case_6_result_2}
       - {standard_case_6_result_3: standard_case_6_result_3}
       - {standard_case_6_result_4: standard_case_6_result_4}
       - {standard_case_1_result_bool: standard_case_1_result_bool}
       - {standard_case_1_result_in_round: standard_case_1_result_in_round}
       - {standard_case_1_result_of_count: standard_case_1_result_of_count}
       - {standerd_case_7_result: standerd_case_7_result}
       - {standard_case_8_result_1: standard_case_8_result_1}
       - {standard_case_8_result_2: standard_case_8_result_2}
       - {standard_case_8_result_3: standard_case_8_result_3}
       - {standard_case_8_result_4: standard_case_8_result_4}
       - {standard_case_8_result_5: standard_case_8_result_5}
       - {standard_case_8_result_6: standard_case_8_result_6}
       - {standard_case_8_result_7: standard_case_8_result_7}
       - {standard_case_8_result_8: standard_case_8_result_8}
       - {standard_case_8_result_9: standard_case_8_result_9}
       - {standard_case_8_result_10: standard_case_8_result_10}
       - {standard_case_8_result_11: standard_case_8_result_11}
       - {standard_case_8_result_12: standard_case_8_result_12}
       - {standard_case_8_result_13: standard_case_8_result_13}
       - {standard_case_8_result_14: standard_case_8_result_14}
       - {standard_case_8_result_15: standard_case_8_result_15}
       - {standard_case_8_result_16: standard_case_8_result_16}
       - {standard_case_8_result_17: standard_case_8_result_17}
       - {standard_case_8_result_18: standard_case_8_result_18}
       - {standard_case_8_result_19: standard_case_8_result_19}
       - {standard_case_8_result_20: standard_case_8_result_20}
       - {standard_case_8_result_21: standard_case_8_result_21}
       - {standard_case_8_result_22: standard_case_8_result_22}
       - {standard_case_8_result_23: standard_case_8_result_23}
       - {standard_case_8_result_24: standard_case_8_result_24}
       - {standard_case_8_result_25: standard_case_8_result_25}
       - {standard_case_8_result_26: standard_case_8_result_26}
       - {standard_case_8_result_27: standard_case_8_result_27}
       - {standard_case_8_result_28: standard_case_8_result_28}
       - {standard_case_8_result_29: standard_case_8_result_29}
       - {standard_case_8_result_30: standard_case_8_result_30}
       - {standard_case_8_result_31: standard_case_8_result_31}
       - {standard_case_8_result_32: standard_case_8_result_32}
       - {standard_case_8_result_33: standard_case_8_result_33}
       - {standard_case_8_result_34: standard_case_8_result_34}
       - {standard_case_8_result_35: standard_case_8_result_35}
       - {standard_case_8_result_36: standard_case_8_result_36}
       - {standard_case_8_result_37: standard_case_8_result_37}
       - {standard_case_8_result_38: standard_case_8_result_38}
       - {standard_case_8_result_39: standard_case_8_result_39}
       - {standard_case_9_result_1: standard_case_9_result_1}
       - {standard_case_9_result_2: standard_case_9_result_2}
       - {standard_case_9_result_3: standard_case_9_result_3}

string standardResults: standardResultList.join(",")

## irregular pattern (Unsupported)
# list l: [1, 2, 3]
# number n: l.avg() + l.sum()
# number n: round(l.avg())
# number n: 1 + l.sum()

# list l: [ {a1: 1, a2: 1}, {a1: 1, a2: 2} ]
# number n: l.sum()

# list list1: [1, 2, 3]
# list l: [ list1 ]
# number n: l.sum()

# list l: [1, 2, 3]
# number n: l.sum(a1)

# list l: [ {a1: "x", a2: 1}, {a1: "y", a2: 1} ]
# number n: l.sum(a3)

number 累積パケットデータ: パケット積算データ.使用量 + パケットイベント.使用量

#永続化する
persist パケット積算データ:
    使用量: 累積パケットデータ
    watch(パケットイベント):

#超えていたらメールを出す
rule パケット積算:
    累積パケットデータ >= ユーザー情報.パケット上限:
    watch(パケットイベント):

effect 再送信 as mail_1:
    値:
    ユーザーID: standardResults
    日時: now()
    メッセージ: "パケット使用量を超過しました。"
    watch(パケット積算):

effect 再送信 as mail_2:
    値:
    ユーザーID: standardResults
    日時: now()
    メッセージ: "サイズ確認できました"
    watch(サイズ確認):
