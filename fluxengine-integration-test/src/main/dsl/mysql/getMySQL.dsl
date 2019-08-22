event getMySQLイベント:
  test_id: string

event getMySQLイベント属性あり:
  test_id: string
  value: number
  persist_id: string

string 条件分岐なしの単一属性:
  sql: mysql/getMySQL_string.sql
  params:
    test_id<varchar>: getMySQLイベント.test_id
    id<bigint>: 0
  cache: off

persister 条件分岐なしの単一属性persister:
  value_string: string
  persist("getMySQL_条件分岐なしの単一属性"):
    lifetime: today()

persist 条件分岐なしの単一属性persister:
  value_string: 条件分岐なしの単一属性
  watch(getMySQLイベント):

struct 条件分岐なしの複数属性:
  bit_field: bool
  int_field: number
  double_field: number
  decimal_field: number
  datetime_field: datetime
  timestamp_field: datetime
  varchar_field: string
  sql: mysql/getMySQL_struct.sql
  params:
    test_id<varchar>: getMySQLイベント.test_id
    id<bigint>: 1
  cache: off

persister 条件分岐なしの複数属性persister:
  bit_field: bool
  int_field: number
  double_field: number
  decimal_field: number
  datetime_field: datetime
  timestamp_field: datetime
  varchar_field: string
  persist("getMySQL_条件分岐なしの複数属性"):
    lifetime: today()

persist 条件分岐なしの複数属性persister:
  bit_field: 条件分岐なしの複数属性.bit_field
  int_field: 条件分岐なしの複数属性.int_field
  double_field: 条件分岐なしの複数属性.double_field
  decimal_field: 条件分岐なしの複数属性.decimal_field
  datetime_field: 条件分岐なしの複数属性.datetime_field
  timestamp_field: 条件分岐なしの複数属性.timestamp_field
  varchar_field: 条件分岐なしの複数属性.varchar_field
  watch(getMySQLイベント):

number 条件分岐ありの単一属性:
  getMySQLイベント属性あり.value >= 50:
    sql: mysql/getMySQL_number.sql
    params:
      test_id<varchar>: getMySQLイベント属性あり.test_id
      id<bigint>: 2
  else:
    sql: mysql/getMySQL_number.sql
    params:
      test_id<varchar>: getMySQLイベント属性あり.test_id
      id<bigint>: 3
  cache: off

persister 条件分岐ありの単一属性persister:
  value_number: number
  persist(getMySQLイベント属性あり.persist_id):
    lifetime: today()

persist 条件分岐ありの単一属性persister:
  value_number: 条件分岐ありの単一属性
  watch(getMySQLイベント属性あり):

struct 条件分岐ありの複数属性:
  bit_field: bool
  int_field: number
  double_field: number
  decimal_field: number
  datetime_field: datetime
  timestamp_field: datetime
  varchar_field: string
  getMySQLイベント属性あり.value < 50:
    sql: mysql/getMySQL_struct.sql
    params:
      test_id<varchar>: getMySQLイベント属性あり.test_id
      id<bigint>: 4
  else:
    sql: mysql/getMySQL_struct.sql
    params:
      test_id<varchar>: getMySQLイベント属性あり.test_id
      id<bigint>: 5
  cache: off

persister 条件分岐ありの複数属性persister:
  bit_field: bool
  int_field: number
  double_field: number
  decimal_field: number
  datetime_field: datetime
  timestamp_field: datetime
  varchar_field: string
  persist(getMySQLイベント属性あり.persist_id):
    lifetime: today()

persist 条件分岐ありの複数属性persister:
  bit_field: 条件分岐ありの複数属性.bit_field
  int_field: 条件分岐ありの複数属性.int_field
  double_field: 条件分岐ありの複数属性.double_field
  decimal_field: 条件分岐ありの複数属性.decimal_field
  datetime_field: 条件分岐ありの複数属性.datetime_field
  timestamp_field: 条件分岐ありの複数属性.timestamp_field
  varchar_field: 条件分岐ありの複数属性.varchar_field
  watch(getMySQLイベント属性あり):

struct param1つのSQL:
  bit_field: bool
  int_field: number
  double_field: number
  decimal_field: number
  datetime_field: datetime
  timestamp_field: datetime
  varchar_field: string
  sql: mysql/getMySQL_1param.sql
  params:
    test_id<varchar>: getMySQLイベント.test_id
  cache: off

persister param1つのSQLpersister:
  bit_field: bool
  int_field: number
  double_field: number
  decimal_field: number
  datetime_field: datetime
  timestamp_field: datetime
  varchar_field: string
  persist("getMySQL_param1つのSQL"):
    lifetime: today()

persist param1つのSQLpersister:
  bit_field: param1つのSQL.bit_field
  int_field: param1つのSQL.int_field
  double_field: param1つのSQL.double_field
  decimal_field: param1つのSQL.decimal_field
  datetime_field: param1つのSQL.datetime_field
  timestamp_field: param1つのSQL.timestamp_field
  varchar_field: param1つのSQL.varchar_field
  watch(getMySQLイベント):

string paramなしのSQL:
  sql: mysql/getMySQL_0param.sql
  params:
  cache: off

persister paramなしのSQLpersister:
  value_string: string
  persist("getMySQL_paramなしのSQL"):
    lifetime: today()

persist paramなしのSQLpersister:
  value_string: paramなしのSQL
  watch(getMySQLイベント):

import パラメタのあるvariant<getMySQLイベント.test_id, 6>: mysql/パラメタのあるvariant

persister パラメタのあるvariantpersister:
  value_string: string
  persist("getMySQL_パラメタのあるvariant"):
    lifetime: today()

persist パラメタのあるvariantpersister:
  value_string: パラメタのあるvariant
  watch(getMySQLイベント):