import case_4_map: variant/ユーザー情報
import パケットイベント: event/パケットイベント
import ユーザー情報<case_4_map>: variant/ユーザー情報
import パケット積算データ<ユーザー情報.ユーザーID>: persister/パケット積算データ
import メール送信: effector/ユーザー通知

number 累積パケットデータ: パケット積算データ.使用量 + パケットイベント.使用量


#永続化する
persist パケット積算データ:
    使用量: 累積パケットデータ
    watch(パケットイベント):

#超えていたらメールを出す
rule パケット積算:
    累積パケットデータ >= ユーザー情報.パケット上限:
    watch(パケットイベント):

effect メール送信:
    ユーザーID: resultlist.join(",")
    日時: now()
    メッセージ: "パケット使用量を超過しました。"
    watch(パケット積算):


### NG ###

map test_map: {a1: 1}

### OK ###
## regular pattern (Supported)
# 基本
string str:  "a={0}, b={1}"
number test: 2
map case_1_map:
   a1: "a"
   a2: 1.2
   a3: 2.0
   a4: "a={0}"
   a5: today()
   a6: now()
   a7: str.format("test1", "test2")
   a8: test * test
   a9: [1, 2, 3]
   a10: {inner1: "test"}
number case_1_map_result_1: case_1_map.a2 + case_1_map.a3
string case_1_map_result_2: case_1_map.a1
string case_1_map_result_3: case_1_map.a4.format("test")
number case_1_map_result_4: round(case_1_map.a2)

# 条件値
map case_2_1_map: {a1: "m2_a1", a2: "m2_a2", a3: "m2_a3"}
map case_2_2_map:
  test == 2: {a1: "variantmap_a1", a2: "variantmap_a2", a3: "variantmap_a3"}
  else: case_2_1_map

# 代入
map case_3_map: case_4_map


# 要素として使用
list resultlist: [case_1_map, case_2_1_map, case_2_2_map, case_3_map]


## irregular pattern (Unsupported)
#  map m:
#     a1: "a"
#     a2: 1.2
#     a3: 2.0
#  number n: m.a4       # null
#-------------------------------------
#  map m:
#     a1: "a"
#     a2: 1.2
#     a3: 2.0
#  number n: m          # parser error
#-------------------------------------
#  map m:
#     a1: "a"
#  m.a1: "b"            # parser error
#  m: another_map       # parser error
