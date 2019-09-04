persister 有効期限の検証:
  s: string
  persist("有効期限の検証"):

event 有効期限の検証イベント:
  s: string

persist 有効期限の検証:
  s: 有効期限の検証イベント.s
  watch(有効期限の検証イベント):