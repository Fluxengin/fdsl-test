event Memorystore取得イベント:
  requestid: string
  keys: list

read Memorystore取得Read:
  requestid: string
  watch(Memorystore取得イベント):
  get(Memorystore取得イベント.requestid):

effector Memorystore取得:
  requestid: string
  keys: list

effect Memorystore取得:
  requestid: Memorystore取得Read.requestid
  keys: Memorystore取得イベント.keys
  watch(Memorystore取得Read):