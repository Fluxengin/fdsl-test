event Memorystore取得イベント:
  requestid: string
  keys: list

effector Memorystore取得:
  requestid: string
  keys: list

effect Memorystore取得:
  requestid: Memorystore取得イベント.requestid
  keys: Memorystore取得イベント.keys
  watch(Memorystore取得イベント):