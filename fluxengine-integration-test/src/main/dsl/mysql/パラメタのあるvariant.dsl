export string パラメタのあるvariant<test_id, id>:
  sql: mysql/getMySQL_string.sql
  params:
    test_id<varchar>: test_id
    id<bigint>: id
  cache: off