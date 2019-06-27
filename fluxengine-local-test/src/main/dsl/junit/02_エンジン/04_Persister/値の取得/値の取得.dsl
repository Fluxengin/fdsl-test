event パケット使用:
  ユーザーID: string
  使用量: number
  期限1: string
  期限2: string

persister パケット積算:
  使用量: number
  persist(パケット使用.ユーザーID):
    lifetime: パケット使用.期限1

number 累積パケット: パケット積算.使用量 + パケット使用.使用量

persist パケット積算:
  使用量: 累積パケット
  watch(パケット使用):

state 状態:
  s1:
    パケット使用: s2
  s2:
  watch(パケット使用):
  persist(パケット使用.ユーザーID):
    lifetime: パケット使用.期限2