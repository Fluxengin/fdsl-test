number x: 1
number y: 1

# 算術演算子
number n1: x + y
bool b1_1:
   n1 == 2: true
   else: false

number n2: x - y
bool b1_2:
   n2 == 0: true
   else: false

number n3: x * y
bool b1_3:
   n3 == 1: true
   else: false

number n4: x / y
bool b1_4:
   n4 == 1: true
   else: false

# 余りは非対応
# number n5: x % y
# bool b1_5:
#    n5 == 0: true
#    else: false

# インクリメント・デクリメント
# number n6: ++x
# bool b2_1:
#    n6 == 2: true
#    else: false

# number n7: x++
# bool b2_2:
#    n7 == 2: true
#    else: false

# number n8: --x
# bool b2_3:
#    n8 == 0: true
#    else: false

# number n9: x--
# bool b2_4:
#    n9 == 0: true
#    else: false


# ビット演算子
#bool b3_1_1:
#   0 & 0 == 0: true
#   else: false

#bool b3_1_2:
#   1 & 0 == 0: true
#   else: false

#bool b3_1_3:
#   0 & 1 == 0: true
#   else: false

#bool b3_1_4:
#   1 & 1 == 0: true
#   else: false

#bool b3_2_1:
#   0 | 0 == 0: true
#   else: false

#bool b3_2_2:
#   1 | 0 == 1: true
#   else: false

#bool b3_2_3:
#   0 | 1 == 1: true
#   else: false

#bool b3_2_4:
#   1 | 1 == 1: true
#   else: false

#bool b3_3_1:
#   0 ^ 0 == 0: true
#   else: false

#bool b3_3_2:
#   1 ^ 0 == 1: true
#   else: false

#bool b3_3_3:
#   0 ^ 1 == 1: true
#   else: false

#bool b3_3_4:
#   1 ^ 1 == 0: true
#   else: false

#bool b3_4_1:
#   ~0 == 1: true
#   else: false

#bool b3_4_2:
#   ~0 == 0: true
#   else: false

####################################
#       代入演算子は使えない
# = += -= *= /= %= &= |= ^= <<= >>=
####################################


# 論理演算子
# true == trueはかけない

bool b4_1_1:
   true == false && true == false: false
   else: true

bool b4_1_2:
   true == true && true == false : false
   else: true

bool b4_1_3:
   true == false && true == true : false
   else: true

bool b4_1_4:
   true == true && true == true : true
   else: false

bool b4_2_1:
   true == false || true == false : false
   else: true

bool b4_2_2:
   true == true || true == false : true
   else: false

bool b4_2_3:
   true == false || true == true : true
   else: false

bool b4_2_4:
   true == true || true == true : true
   else: false

# NOT演算子
#bool b4_3_1:
#   !true == false: true
#   else: false

#bool b4_3_2:
#   !false == true: true
#   else: false

# 比較演算子
bool b5_1:
   x == y : true
   else: false

bool b5_2:
   x != y : false
   else: true

bool b5_3:
   x < y : false
   else: true

bool b5_4:
   x <= y : true
   else: false

bool b5_5:
   x > y : false
   else: true

bool b5_6:
   x >= y : true
   else: false

# 単項演算子
number n6_1: -1 + 1
# number n6_2: +1 - 1
bool b6_1:
    n6_1 == 0: true
    else: false

# bool b6_2:
#     n6_2 == 0: true
#     else: false

##########################################
# 定数宣言における順次演算子(,)は使えない
##########################################
# number n1, n2

##########################################
#
# ver.1.0.5時点で使用可能な演算子
#
##########################################
# グループ化: ()
# 単項目演算子: -(マイナス)
# 算術演算子: + - * /
# 比較演算子: < <= > >= == !=
# 論理演算子: || &&
##########################################
#
# 優先順位
#
##########################################
# 0. グループ化 ()
# 1. 右から結合 -(マイナス)
# ----------------------------プラスはパーサエラー
# 2. 左から結合 * /
# 3. 左から結合 + -
# ----------------------------算術演算子と比較演算子の結合式は非対応
# 4. 左から結合 < <= > >= == !=
# ----------------------------比較演算子同士の結合式は非対応。< <= > >=は== !=より優先されるのが一般的
# 5. 左から結合 || &&
# ----------------------------&&は||より優先されるのが一般的
##########################################

number n7_1: -1 * 2
bool b7_1:
    n7_1 == -2: true
    else: false

number n7_2_1: 1 + 2 * 2
bool b7_2_1:
    n7_2_1 == 5: true
    else: false

number n7_2_2: (1 + 2) * 2
bool b7_2_2:
    n7_2_2 == 6: true
    else: false

number n7_3_1: 1 - 2 / 2
bool b7_3_1:
    n7_3_1 == 0: true
    else: false

number n7_3_2: (1 - 2) / 2
bool b7_3_2:
    n7_3_2 == -0.5: true
    else: false

# bool b7_4:
#     true == 0 < 1 : true
#     else: false

# bool b7_5_1:
#     true == 0 <= 1 : true
#     else: false

# bool b7_5_2:
#     true == 1 <= 1 : true
#     else: false

# bool b7_6:
#     true != 0 > 1 : true
#     else: false

# bool b7_7_1:
#     true != 0 >= 1 : true
#     else: false

# bool b7_7_2:
#     false != 1 >= 1 : true
#     else: false

bool b7_8:
    1 == 1 && 2 == 2 : true
    else: false

bool b7_9:
    1 != 0 && 2 != 0 : true
    else: false

bool b7_10:
    1 == 1 || 0 == 2 : true
    else: false

bool b7_11:
    1 != 0 || 0 != 0 : true
    else: false

bool b7_12_1:
    1 == 1 || 2 == 2 && 3 == 0 : true
    else: false

bool b7_12_2:
    1 == 1 || (2 == 2 && 3 == 0) : true
    else: false

bool b7_13:
    3 == 0 && 1 == 1 || 2 == 2 : true
    else: false

date t: 2018/11/12
bool b7_14:
    today(+1d) == 2018/11/11 < t: true
    else: false
