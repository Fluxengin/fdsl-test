# DSLのnumberがプラグインのObjectで受け取れないことが分かった

#effector 型変更の検証:
#  storage_prefix: string
#  filename: string
#  contents: number
#
#event effector型変更の検証イベント:
#  storage_prefix: string
#
#effect 型変更の検証:
#  storage_prefix: effector型変更の検証イベント.storage_prefix
#  filename: "型変更の検証_変換可能_before.txt"
#  contents: 123
#  watch(effector型変更の検証イベント):
