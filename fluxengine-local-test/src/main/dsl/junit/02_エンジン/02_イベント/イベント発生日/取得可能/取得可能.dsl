event e1:
  key: string

date d1: eventDate()

datetime d2: eventTime()

# 属性なしのイベント
event e2:

persister p1:
  作成日: date
  persist("p1"):

persist p1:
  作成日: eventDate()
  watch(e1):

persister p2:
  作成日時: datetime
  persist("p2"):

persist p2:
  作成日時: eventTime()
  watch(e2):
