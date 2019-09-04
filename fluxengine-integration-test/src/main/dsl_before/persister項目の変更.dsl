persister 項目変更の検証_増加_期限切れ:
  s1: string
  persist("persister項目変更の検証"):
    lifetime: now()

persister 項目変更の検証_減少_期限切れ:
  s2: string
  n2: number
  persist("persister項目変更の検証"):
    lifetime: now()

persister 項目変更の検証_増加_期限内:
  s3: string
  persist("persister項目変更の検証"):
    lifetime: today()

persister 項目変更の検証_減少_期限内:
  s4: string
  n4: number
  persist("persister項目変更の検証"):
    lifetime: today()

event 項目変更の検証イベント:
  dummy: string

persist 項目変更の検証_増加_期限切れ:
  s1: "項目変更の検証_増加_期限切れ_before"
  watch(項目変更の検証イベント):

persist 項目変更の検証_減少_期限切れ:
  s2: "項目変更の検証_減少_期限切れ_before"
  n2: 2
  watch(項目変更の検証イベント):

persist 項目変更の検証_増加_期限内:
  s3: "項目変更の検証_増加_期限内_before"
  watch(項目変更の検証イベント):

persist 項目変更の検証_減少_期限内:
  s4: "項目変更の検証_減少_期限内_before"
  n4: 4
  watch(項目変更の検証イベント):
