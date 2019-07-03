event Subscriptionイベント:
  ユーザーID: string
  文字列: string

persister Subscriptionイベント永続化:
  文字列: string
  persist(Subscriptionイベント.ユーザーID):
    lifetime: today()

persist Subscriptionイベント永続化:
  文字列: Subscriptionイベント.文字列
  watch(Subscriptionイベント):