persister 項目変更の検証_増加_期限切れ:
  s1: string
  n1: number
  persist("persister項目変更の検証"):
    lifetime: now()

persister 項目変更の検証_減少_期限切れ:
  s2: string
  persist("persister項目変更の検証"):
    lifetime: now()

persister 項目変更の検証_増加_期限内:
  s3: string
  n3: number
  persist("persister項目変更の検証"):
    lifetime: today()

persister 項目変更の検証_減少_期限内:
  s4: string
  persist("persister項目変更の検証"):
    lifetime: today()

event 項目変更の検証イベント:
  dummy: string

string s1next: ユーティリティ.concat(項目変更の検証_増加_期限切れ.s1, "_after")
number n1next: 項目変更の検証_増加_期限切れ.n1 + 1

persist 項目変更の検証_増加_期限切れ:
  s1: s1next
  n1: n1next
  watch(項目変更の検証イベント):

string s2next: ユーティリティ.concat(項目変更の検証_減少_期限切れ.s2, "_after")

persist 項目変更の検証_減少_期限切れ:
  s2: s2next
  watch(項目変更の検証イベント):

string s3next: ユーティリティ.concat(項目変更の検証_増加_期限内.s3, "_after")
number n3next: 項目変更の検証_増加_期限内.n3 + 3

persist 項目変更の検証_増加_期限内:
  s3: s3next
  n3: n3next
  watch(項目変更の検証イベント):

string s4next: ユーティリティ.concat(項目変更の検証_減少_期限内.s4, "_after")

persist 項目変更の検証_減少_期限内:
  s4: s4next
  watch(項目変更の検証イベント):
