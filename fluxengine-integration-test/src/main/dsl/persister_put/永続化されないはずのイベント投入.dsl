event Persister参照イベント:
  端末ID: string

#永続化しないように
event Persister参照DummyEvent:
  端末ID: string

persister Persister参照:
  使用量: number
  persist("PersisterRead01"):
    lifetime: today()

persist Persister参照:
  使用量: 100
  watch(Persister参照DummyEvent):

rule Persister参照ルール:
  0 < Persister参照.使用量:
  watch(Persister参照イベント):
