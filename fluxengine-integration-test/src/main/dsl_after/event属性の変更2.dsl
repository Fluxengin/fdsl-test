event 属性の増加の検証:
  attr1: string
  attr2: number
  attr3: bool

event 属性の減少の検証:
  attr1: string

event 属性の型変更の検証:
  attr1: string
  attr2: list

map m1:
  contents1: 属性の増加の検証.attr1
  contents2: 属性の増加の検証.attr2
  contents3: 属性の増加の検証.attr3

map m2:
  contents1: 属性の減少の検証.attr1

map m3:
  contents1: 属性の型変更の検証.attr1
  contents2: 属性の型変更の検証.attr2

persister event属性の増加の検証:
  contents: map
  persist("event属性の変更の検証2"):

persister event属性の減少の検証:
  contents: map
  persist("event属性の変更の検証2"):

persister event属性の型変更の検証:
  contents: map
  persist("event属性の変更の検証2"):

persist event属性の増加の検証:
  contents: m1
  watch(属性の増加の検証):

persist event属性の減少の検証:
  contents: m2
  watch(属性の減少の検証):

persist event属性の型変更の検証:
  contents: m3
  watch(属性の型変更の検証):
