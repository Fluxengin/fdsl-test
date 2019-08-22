SELECT bit_field,
       int_field,
       double_field,
       decimal_field,
       datetime_field,
       timestamp_field,
       varchar_field
  FROM getmysql_test
 WHERE test_id = ?
 ORDER BY id
