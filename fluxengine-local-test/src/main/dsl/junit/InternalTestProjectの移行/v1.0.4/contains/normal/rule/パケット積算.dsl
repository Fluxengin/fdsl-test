import パケットイベント: event/パケットイベント
import 数値<パケットイベント.端末ID>: variant/ユーザー情報
import 文字列<パケットイベント.端末ID>: variant/ユーザー情報
import パケット積算データ<文字列>: persister/パケット積算データ
import メール送信: effector/ユーザー通知

list l1: [3, 1, 2]
bool b1_1: l1.contains(1)          # b = true
bool b1_2: l1.contains(0)          # b = false

list l2: ["a", "b", "c"]
bool b2_1: l2.contains("a")          # b = true
bool b2_2: l2.contains("d")          # b = false

list l3: [ {a1: "x", a2: 2}, {a1: "y", a2: 2} ]
map m1_1: {a1: "x", a2: 2}
map m1_2: {a1: "xxx", a2: 2}
bool b3_1: l3.contains(m1_1)        # b =true
bool b3_2: l3.contains(m1_2)        # b =false

string s1: "abc"
bool b4_1: s1.contains("a")    # b = true
bool b4_2: s1.contains("d")    # b = false

string s2: "abc"
bool b5_1: s2.startsWith("a")    # b = true
bool b5_2: s2.startsWith("b")    # b = false

string s3: "abc"
bool b6_1: s3.endsWith("c")    # b = true
bool b6_2: s3.endsWith("b")    # b = false

list l4_1: [2019/01/01]
bool b7_1: l4_1.contains(2019/01/01)          # b = true
list l4_2: [true, false]
bool b7_2: l4_2.contains(true)          # b = false

bool b7_1:
    l1.contains(1) == true: true
    else: false

bool b7_2:
    l1.contains(0) == false: true
    else: false

bool b8_1:
    l2.contains("a") == true: true
    else: false

bool b8_2:
    l2.contains("d") == false: true
    else: false

bool b9_1:
    l3.contains(m1_1) == true: true
    else: false

bool b9_2:
    l3.contains(m1_2) == false: true
    else: false

bool b10_1:
    s1.contains("a") == true: true
    else: false

bool b10_2:
    s1.contains("d") == false: true
    else: false

bool b11_1:
    s2.startsWith("a") == true: true
    else: false

bool b11_2:
    s2.startsWith("b") == false: true
    else: false

bool b12_1:
    s3.endsWith("c") == true: true
    else: false

bool b12_2:
    s3.endsWith("b") == false: true
    else: false

string s4: "a"
bool b13_1: s4.contains("a")    # b = true
bool b13_2: s4.startsWith("a")    # b = true
bool b13_3: s4.endsWith("a")    # b = true

string s5: ""
bool b14_1: s5.contains("")    # b = true
bool b14_2: s5.startsWith("")    # b = true
bool b14_3: s5.endsWith("")    # b = true

map m2: {a1: "a"}
bool b15_1: m2.a1.contains("a")    # b = true
bool b15_2: m2.a1.startsWith("a")    # b = true
bool b15_3: m2.a1.endsWith("a")    # b = true


bool b16_1: 文字列.contains("あ")    # b = true
bool b16_2: 文字列.startsWith("あ")    # b = true
bool b16_3: 文字列.endsWith("あ")    # b = true


string c: "a"
string s6: "a"
bool b17_1: s6.contains(c)    # b = true
bool b17_2: s6.startsWith(c)    # b = true
bool b17_3: s6.endsWith(c)    # b = true

bool b18_1: パケットイベント.端末ID.contains("tid")
bool b18_2: パケットイベント.端末ID.contains(" ")

rule r_1:
    パケットイベント.端末ID.contains("tid"):
    watch(パケットイベント):

effect メール送信:
    メッセージ: "ルール成立"
    watch(r_1):

persist パケット積算データ:
    使用量: 100
    watch(r_1):

string s7: "a"
bool b19_1:
    s7.contains("&&") && s7.contains("||"): true
    else: false
bool b19_2:
    s7.contains("&&") || s7.contains("||"): true
    else: false

# unsupported
number n: 1234
bool b99: n.contains(1234)    # b = false
