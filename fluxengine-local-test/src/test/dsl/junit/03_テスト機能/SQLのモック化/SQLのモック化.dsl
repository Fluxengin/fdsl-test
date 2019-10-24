runner:
  database:
    mysql/m1.sql:
      - params:
          player_id: "i-1"
        result: "player-name-1"
      - params:
          player_id: "i-2"
        result: "player-name-1-1"
    mysql/m2.sql:
      - params:
          bo_user_id: "i-1"
        result: "bo-user-name-1"
      - params:
          bo_user_id: "i-2"
        result: "bo-user-name-1"
    mysql/m3.sql:
      - params:
          div_id: 100
          role: "manager"
        result: "bo-manager-name-1"
      - params:
          div_id: 200
          role: "manager"
        result: "bo-manager-name-2"
    mysql/m4.sql:
      - params:
          player_id: "i-2"
        result:
          name: "player-name-2"
          age: 23
          birthday: 1996/10/24
          createtime: now()
    mysql/m5.sql:
      - params:
          group_id: 100
          group_code: "i-1"
        result:
          name: "player-name-3"
          age: 20
          birthday: 1999/09/24
          createtime: 2019/08/23 00:00:02
      - params:
          group_id: 200
          group_code: "i-1"
        result:
          name: "player-name-3"
          age: 21
          birthday: 1998/09/24
          createtime: 2019/08/23 00:00:02
    mysql/m6.sql:
      - params:
        result:
          name: "player-name-4"
          age: 35
          birthday: 1984/02/24
          createtime: 2017/05/01 00:00:03

test 1 モックに指定した値が取得できる:
  2019/10/24 00:00:01:
    e:
      code: "i-1"
      value: 100
    inspect:
      条件式なしで文字列取得 == "player-name-1":
      条件式分岐で文字列取得 == "bo-user-name-1":
      条件式分岐で複数属性取得.name == "player-name-3":
      条件式分岐で複数属性取得.age == 20:
      条件式分岐で複数属性取得.birthday == 1999/09/24:
      条件式分岐で複数属性取得.createtime == 2019/08/23 00:00:02:
      複数属性取得.name == "player-name-4":
      複数属性取得.age == 35:
      複数属性取得.birthday == 1984/02/24:
      複数属性取得.createtime == 2017/05/01 00:00:03:

test 2 paramsの値によって異なる値を返せる:
  2019/10/24 00:00:01:
    e:
      code: "i-1"
      value: 200
    inspect:
      条件式なしで文字列取得 == "player-name-1":
      条件式分岐で文字列取得 == "bo-user-name-1":
      条件式分岐で複数属性取得.name == "player-name-3":
      条件式分岐で複数属性取得.age == 21:
      条件式分岐で複数属性取得.birthday == 1998/09/24:
      条件式分岐で複数属性取得.createtime == 2019/08/23 00:00:02:
      複数属性取得.name == "player-name-4":
      複数属性取得.age == 35:
      複数属性取得.birthday == 1984/02/24:
      複数属性取得.createtime == 2017/05/01 00:00:03:

test 3 条件分岐もテストする:
  2019/10/24 00:00:01:
    e:
      code: "i-2"
      value: 100
    inspect:
      条件式なしで文字列取得 == "player-name-1-1":
      条件式分岐で文字列取得 == "bo-manager-name-1":
      条件式分岐で複数属性取得.name == "player-name-2":
      条件式分岐で複数属性取得.age == 23:
      条件式分岐で複数属性取得.birthday == 1996/10/24:
      条件式分岐で複数属性取得.createtime == 2019/10/24 00:00:01:
      複数属性取得.name == "player-name-4":
      複数属性取得.age == 35:
      複数属性取得.birthday == 1984/02/24:
      複数属性取得.createtime == 2017/05/01 00:00:03:

test 4 連続でイベントが来ても正しく値を返せる:
  2019/10/24 00:00:01:
    e:
      code: "i-2"
      value: 100
    inspect:
      条件式なしで文字列取得 == "player-name-1-1":
      条件式分岐で文字列取得 == "bo-manager-name-1":
      条件式分岐で複数属性取得.name == "player-name-2":
      条件式分岐で複数属性取得.age == 23:
      条件式分岐で複数属性取得.birthday == 1996/10/24:
      条件式分岐で複数属性取得.createtime == 2019/10/24 00:00:01:
      複数属性取得.name == "player-name-4":
      複数属性取得.age == 35:
      複数属性取得.birthday == 1984/02/24:
      複数属性取得.createtime == 2017/05/01 00:00:03:
  2019/10/24 01:00:01:
    e:
      code: "i-1"
      value: 200
    inspect:
      条件式なしで文字列取得 == "player-name-1":
      条件式分岐で文字列取得 == "bo-user-name-1":
      条件式分岐で複数属性取得.name == "player-name-3":
      条件式分岐で複数属性取得.age == 21:
      条件式分岐で複数属性取得.birthday == 1998/09/24:
      条件式分岐で複数属性取得.createtime == 2019/08/23 00:00:02:
      複数属性取得.name == "player-name-4":
      複数属性取得.age == 35:
      複数属性取得.birthday == 1984/02/24:
      複数属性取得.createtime == 2017/05/01 00:00:03:
  2019/10/24 02:00:01:
    e:
      code: "i-2"
      value: 200
    inspect:
      条件式なしで文字列取得 == "player-name-1-1":
      条件式分岐で文字列取得 == "bo-manager-name-2":
      条件式分岐で複数属性取得.name == "player-name-2":
      条件式分岐で複数属性取得.age == 23:
      条件式分岐で複数属性取得.birthday == 1996/10/24:
      条件式分岐で複数属性取得.createtime == 2019/10/24 00:00:01: # キャッシュが働いているため
      複数属性取得.name == "player-name-4":
      複数属性取得.age == 35:
      複数属性取得.birthday == 1984/02/24:
      複数属性取得.createtime == 2017/05/01 00:00:03:
  2019/10/24 03:00:01:
    e:
      code: "i-1"
      value: 200
    inspect:
      条件式なしで文字列取得 == "player-name-1":
      条件式分岐で文字列取得 == "bo-user-name-1":
      条件式分岐で複数属性取得.name == "player-name-3":
      条件式分岐で複数属性取得.age == 21:
      条件式分岐で複数属性取得.birthday == 1998/09/24:
      条件式分岐で複数属性取得.createtime == 2019/08/23 00:00:02:
      複数属性取得.name == "player-name-4":
      複数属性取得.age == 35:
      複数属性取得.birthday == 1984/02/24:
      複数属性取得.createtime == 2017/05/01 00:00:03:
