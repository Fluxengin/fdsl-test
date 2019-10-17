event variant変更の検証イベント:
  attr1: number
  attr2: number
  attr3: string
  attr4: string
  attr5: string
  attr6: string
  attr7: string
  attr8: string

number 値変更の検証: variant変更の検証イベント.attr2 + 10

string プラグインへのパラメタ変更_キャッシュ無効:
  get(variant変更の検証イベント.attr4):
    cache: off

string プラグインへのパラメタ変更_キャッシュ有効_値変更:
  get(variant変更の検証イベント.attr6):
    cache: "a"  # 不変の値にして、いつまでもキャッシュさせる

string プラグインへのパラメタ変更_キャッシュ有効_値不変:
  get(variant変更の検証イベント.attr8):
    cache: "a"  # 不変の値にして、いつまでもキャッシュさせる

persister variant変更の検証:
  値変更の検証: number
  プラグインへのパラメタ変更_キャッシュ無効: string
  プラグインへのパラメタ変更_キャッシュ有効_値変更: string
  プラグインへのパラメタ変更_キャッシュ有効_値不変: string
  persist("variant変更の検証"):

persist variant変更の検証:
  値変更の検証: 値変更の検証
  プラグインへのパラメタ変更_キャッシュ無効: プラグインへのパラメタ変更_キャッシュ無効
  プラグインへのパラメタ変更_キャッシュ有効_値変更: プラグインへのパラメタ変更_キャッシュ有効_値変更
  プラグインへのパラメタ変更_キャッシュ有効_値不変: プラグインへのパラメタ変更_キャッシュ有効_値不変
  watch(variant変更の検証イベント):