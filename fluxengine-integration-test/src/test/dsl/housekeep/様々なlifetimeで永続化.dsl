test 1 lifetimeなしのDSLが正しく機能する:
  2019/06/20 00:00:01:
    Housekeepパケット使用A:
      ユーザーID: "userA"
      使用量: 500
    inspect:
      Housekeepパケット積算A.使用量 == 500:
      Housekeep状態A.currentState == s2:

test 2 persisterのみlifetimeありのDSLが正しく機能する:
  2019/06/20 00:00:01:
    Housekeepパケット使用B:
      ユーザーID: "userB"
      使用量: 500
      期限1: "2019-06-20"
    inspect:
      Housekeepパケット積算B.使用量 == 500:
      Housekeep状態B.currentState == s2:

test 3 stateのみlifetimeありのDSLが正しく機能する:
  2019/06/20 00:00:01:
    Housekeepパケット使用C:
      ユーザーID: "userC"
      使用量: 500
      期限2: "2019-06-21"
    inspect:
      Housekeepパケット積算C.使用量 == 500:
      Housekeep状態C.currentState == s2:

test 4 両方lifetimeありのDSLが正しく機能する:
  2019/06/20 00:00:01:
    Housekeepパケット使用D:
      ユーザーID: "userD"
      使用量: 500
      期限1: "2019-06-20"
      期限2: "2019-06-21"
    inspect:
      Housekeepパケット積算D.使用量 == 500:
      Housekeep状態D.currentState == s2:

test 5 ユーザーごとに使用量が異なる:
  2019/06/20 00:00:01:
    Housekeepパケット使用D:
      ユーザーID: "user1"
      使用量: 500
      期限1: "2019-06-20"
      期限2: "2019-06-21"
    inspect:
      Housekeepパケット積算D.使用量 == 500:
      Housekeep状態D.currentState == s2:
  2019/06/20 01:00:01:
    Housekeepパケット使用D:
      ユーザーID: "user1"
      使用量: 1000
      期限1: "2019-06-20"
      期限2: "2019-06-21"
    inspect:
      Housekeepパケット積算D.使用量 == 1500:
      Housekeep状態D.currentState == s2:
  2019/06/20 02:00:01:
    Housekeepパケット使用D:
      ユーザーID: "user2"
      使用量: 1000
      期限1: "2019-06-21"
      期限2: "2019-06-21"
    inspect:
      Housekeepパケット積算D.使用量 == 1000:
      Housekeep状態D.currentState == s2:
