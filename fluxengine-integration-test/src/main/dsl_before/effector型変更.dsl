effector 型変更の検証:
  storage_prefix: string
  filename: string
  contents: number

event effector型変更の検証イベント1:
  storage_prefix: string

event effector型変更の検証イベント2:
  storage_prefix: string

effect 型変更の検証 as 型変更の検証_変換可能:
  storage_prefix: effector型変更の検証イベント1.storage_prefix
  filename: "型変更の検証_変換可能_before.txt"
  contents: 123
  watch(effector型変更の検証イベント1):

effect 型変更の検証 as 型変更の検証_変換不可能:
  storage_prefix: effector型変更の検証イベント2.storage_prefix
  filename: "型変更の検証_変換不可能_before.txt"
  contents: 456
  watch(effector型変更の検証イベント2):
