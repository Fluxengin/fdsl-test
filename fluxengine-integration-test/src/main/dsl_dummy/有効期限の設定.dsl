event 有効期限の検証イベント:
  s: string

persister 有効期限の検証イベントの永続化_dummy:
  s: string
  persist("有効期限の検証イベントの永続化_dummy"):

persist 有効期限の検証イベントの永続化_dummy:
  s: 有効期限の検証イベント.s
  watch(有効期限の検証イベント):