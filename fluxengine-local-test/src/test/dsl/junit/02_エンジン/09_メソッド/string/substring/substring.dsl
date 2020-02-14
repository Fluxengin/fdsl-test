test 1 引数2個のsubstring:
  2020/02/14 00:00:01:
    inspect:
      s2 == s1:
      s3 == "bcdefghijklmnopqrstuvwxy":
      s4 == "":
      s5 == "":
      s7 == "":

test 2 引数1個のsubstring:
  2020/02/14 00:00:02:
    inspect:
      s8 == "klmnopqrstuvwxyz":
      s9 == "z":
      s10 == "":
      s11 == "":

test 3 イベントの属性でも取得できる:
  2020/02/14 00:00:03:
    e1:
      begin: 2
      end: 14
    inspect:
      s12 == "cdefghijklmn":
  2020/02/14 00:00:04:
    e1:
      begin: 26
      end: 26
    inspect:
      s12 == "":

test 4 persisterの属性でも取得できる:
  2020/02/14 00:00:04:
    e1:
      begin: 10
      end: 11
    inspect:
      s13 == "k":
  2020/02/14 00:00:05:
    e1:
      begin: -2
      end: 5
    inspect:
      s13 == "ijklmnop":