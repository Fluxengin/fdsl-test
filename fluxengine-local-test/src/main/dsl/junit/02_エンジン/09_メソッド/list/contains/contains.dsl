list l1: []
bool b1: l1.contains(true)

list l2:
    - 2019/03/20
    - 2019/03/21
bool b2: l2.contains(2019/03/21)

list l3:
    - 文字: "a"
      数値: 0.1
      日時: 2019/03/20
    - 文字: "a"
      数値: 0.1
      日時: 2019/03/21

map toFind:
    文字: "a"
    数値: 0.1
    日時: 2019/03/20

bool b3: l3.contains(toFind)