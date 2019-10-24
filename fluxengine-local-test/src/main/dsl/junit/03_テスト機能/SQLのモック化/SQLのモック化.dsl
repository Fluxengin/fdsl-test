event e:
  code: string
  value: number

string 条件式なしで文字列取得:
  sql: mysql/m1.sql
  params:
    player_id<varchar>: e.code
  cache: off

string 条件式分岐で文字列取得:
  e.code == "i-1":
    sql: mysql/m2.sql
    params:
      bo_user_id<varchar>: e.code
  else:
    sql: mysql/m3.sql
    params:
      div_id<bigint>: e.value
      role<varchar>: "manager"
  cache: today()

struct 条件式分岐で複数属性取得:
  name: string
  age: number
  birthday: date
  createtime: datetime
  e.code == "i-2":
    sql: mysql/m4.sql
    params:
      player_id<varchar>: e.code
  else:
    sql: mysql/m5.sql
    params:
      group_id<bigint>: e.value
      group_code<varchar>: e.code
  cache: today()

struct 複数属性取得:
  name: string
  age: number
  birthday: date
  createtime: datetime
  sql: mysql/m6.sql
  params:
  cache: today()
