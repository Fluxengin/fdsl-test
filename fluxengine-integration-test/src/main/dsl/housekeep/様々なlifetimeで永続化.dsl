# lifetimeなし
event Housekeepパケット使用A:
  ユーザーID: string
  使用量: number

persister Housekeepパケット積算A:
  使用量: number
  persist(Housekeepパケット使用A.ユーザーID):

number Housekeep累積パケットA: Housekeepパケット積算A.使用量 + Housekeepパケット使用A.使用量

persist Housekeepパケット積算A:
  使用量: Housekeep累積パケットA
  watch(Housekeepパケット使用A):

state Housekeep状態A:
  s1:
    Housekeepパケット使用A: s2
  s2:
  watch(Housekeepパケット使用A):
  persist(Housekeepパケット使用A.ユーザーID):

# persisterのみlifetimeあり
event Housekeepパケット使用B:
  ユーザーID: string
  使用量: number
  期限1: string

persister Housekeepパケット積算B:
  使用量: number
  persist(Housekeepパケット使用B.ユーザーID):
    lifetime: Housekeepパケット使用B.期限1

number Housekeep累積パケットB: Housekeepパケット積算B.使用量 + Housekeepパケット使用B.使用量

persist Housekeepパケット積算B:
  使用量: Housekeep累積パケットB
  watch(Housekeepパケット使用B):

state Housekeep状態B:
  s1:
    Housekeepパケット使用B: s2
  s2:
  watch(Housekeepパケット使用B):
  persist(Housekeepパケット使用B.ユーザーID):

# stateのみlifetimeあり
event Housekeepパケット使用C:
  ユーザーID: string
  使用量: number
  期限2: string

persister Housekeepパケット積算C:
  使用量: number
  persist(Housekeepパケット使用C.ユーザーID):

number Housekeep累積パケットC: Housekeepパケット積算C.使用量 + Housekeepパケット使用C.使用量

persist Housekeepパケット積算C:
  使用量: Housekeep累積パケットC
  watch(Housekeepパケット使用C):

state Housekeep状態C:
  s1:
    Housekeepパケット使用C: s2
  s2:
  watch(Housekeepパケット使用C):
  persist(Housekeepパケット使用C.ユーザーID):
    lifetime: Housekeepパケット使用C.期限2

# 両方lifetimeあり
event Housekeepパケット使用D:
  ユーザーID: string
  使用量: number
  期限1: string
  期限2: string

persister Housekeepパケット積算D:
  使用量: number
  persist(Housekeepパケット使用D.ユーザーID):
    lifetime: Housekeepパケット使用D.期限1

number Housekeep累積パケットD: Housekeepパケット積算D.使用量 + Housekeepパケット使用D.使用量

persist Housekeepパケット積算D:
  使用量: Housekeep累積パケットD
  watch(Housekeepパケット使用D):

state Housekeep状態D:
  s1:
    Housekeepパケット使用D: s2
  s2:
  watch(Housekeepパケット使用D):
  persist(Housekeepパケット使用D.ユーザーID):
    lifetime: Housekeepパケット使用D.期限2
