test 1 createTimeがeventDateとeventTimeで取得できる:
  2019/09/20 00:00:01:
    e1:
      key: "dummy"
      createTime: 2019/09/21 01:02:03
    inspect:
      d1 == 2019/09/21:
      d2 == 2019/09/21 01:02:03:

test 2 属性なしのイベントでもcreateTimeが正しく取得できる:
  2019/09/20 00:00:02:
    e2:
      createTime: 2019/09/22 02:03:04
    inspect:
      d1 == 2019/09/22:
      d2 == 2019/09/22 02:03:04:

test 3 複数イベントを投入してもそれぞれのcreateTimeが正しく取得できる:
  2019/09/20 00:00:03:
    e1:
      key: "dummy"
      createTime: 2019/09/23 01:02:03
    e2:
      createTime: 2019/09/24 02:03:04
    inspect:
      p1.作成日 == 2019/09/23:
      p2.作成日時 == 2019/09/24 02:03:04:
