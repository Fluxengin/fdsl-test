persister 値変更の検証:
  contents1: number
  persist("persist値変更の検証"):

event 値変更の検証イベント:
  input: number

persist 値変更の検証:
  contents1: 値変更の検証.contents1 + 値変更の検証イベント.input
  watch(値変更の検証イベント):