event 型変更の検証イベント:
  dummy: string

event 型変更の検証イベント2:
  dummy: string

persister 型変更の検証イベントの永続化_dummy:
  dummy: string
  persist("型変更の検証イベントの永続化_dummy"):

persister 型変更の検証イベントの永続化2_dummy:
  dummy: string
  persist("型変更の検証イベントの永続化2_dummy"):

persist 型変更の検証イベントの永続化_dummy:
  dummy: 型変更の検証イベント.dummy
  watch(型変更の検証イベント):

persist 型変更の検証イベントの永続化2_dummy:
  dummy: 型変更の検証イベント2.dummy
  watch(型変更の検証イベント2):
