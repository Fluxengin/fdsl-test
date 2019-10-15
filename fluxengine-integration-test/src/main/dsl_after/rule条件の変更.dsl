persister rule条件変更の検証:
  contents: string
  persist("rule条件変更の検証"):

rule 条件変更の検証:
  rule条件変更の検証イベント.number_value <= 10:
  watch(rule条件変更の検証イベント):

event rule条件変更の検証イベント:
  number_value: number

persist rule条件変更の検証:
  contents: "exists"
  watch(条件変更の検証):