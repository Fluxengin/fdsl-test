event 入力データ:
  文字列: string
  数値: number

list l: [1, 2, 3, 4, 0.5]

map m:
  list1: [10, -10]

number n1: l.count() * l.sum() * l.avg() + 2 * 3
number n2: m.list1.count() / 33 * 10 * m.list1.sum() + round(2 / 3, 3, down)
number n3: 入力データ.文字列.split(",").count() / 2 - 入力データ.数値 * 1000 + 2 * round(1 / 7, 4)