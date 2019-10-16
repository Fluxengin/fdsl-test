number v1: e.attr1 + p1.contents1

event e:
  attr1: number
  attr2: number

persister p1:
  contents1: number
  persist("transaction_key1"):

persister p2:
  contents2: number
  persist("transaction_key2"):

persist p1:
  contents1: v1
  watch(e):

persist p2:
  contents2: e.attr2
  watch(e):
