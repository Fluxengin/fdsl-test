SELECT name
  FROM bo_officer
 WHERE div_id = ?
   AND role = ?
 ORDER BY id
 LIMIT 1
