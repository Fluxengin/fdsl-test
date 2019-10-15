effector 値変更の検証:
  storage_prefix: string
  filename: string
  contents: string

event effect値の変更イベント:
  storage_prefix: string
  attr1: string
  attr2: string

effect 値変更の検証:
  storage_prefix: effect値の変更イベント.storage_prefix
  filename: "値変更の検証_after.txt"
  contents: effect値の変更イベント.attr2
  watch(effect値の変更イベント):