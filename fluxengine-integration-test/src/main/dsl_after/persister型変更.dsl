persister 型変更の検証_期限切れ:
  contents1: number
  persist("persister型変更の検証"):
    lifetime: now()

persister 型変更の検証_期限内:
  contents2: string
  persist("persister型変更の検証"):
    lifetime: today()

persister 型変更の検証_期限内_error:
  contents2_error: string
  persist("persister型変更の検証"):
    lifetime: today()

persister 型変更の検証_計算可能1:
  contents3: number
  persist("persister型変更の検証"):
    lifetime: today()

event 型変更の検証イベント:
  dummy: string

# イベントを分けてみる
event 型変更の検証イベント2:
  dummy: string

event 型変更の検証イベント2_error1:
  dummy: string

number n1: 型変更の検証_期限切れ.contents1 + 1

persist 型変更の検証_期限切れ:
  contents1: n1
  watch(型変更の検証イベント):

string s2: ユーティリティ.concat(型変更の検証_期限内.contents2, "_after")

persist 型変更の検証_期限内:
  contents2: s2
  watch(型変更の検証イベント2):

string s2_error: ユーティリティ.concatstring(型変更の検証_期限内_error.contents2_error, "_after")

persist 型変更の検証_期限内_error:
  contents2_error: s2_error
  watch(型変更の検証イベント2_error1):

number n3: 型変更の検証_計算可能1.contents3 * 3

persist 型変更の検証_計算可能1:
  contents3: n3
  watch(型変更の検証イベント):

# enumと内部値の変換

enum enum型変更の検証イベント_string_to_enum:
  "A": CODE_A
  "B": CODE_B
  "C": CODE_C

event 型変更の検証イベント_string_to_enum:
  dummy: string

persister 型変更の検証_string_to_enum:
  contents4: enum型変更の検証イベント_string_to_enum
  persist("persister型変更の検証"):

persister 型変更の検証_string_to_enum_判定:
  contents4_判定: bool
  persist("persister型変更の検証"):

bool contents4_判定:
  型変更の検証_string_to_enum.contents4 == CODE_A: true
  else: false

persist 型変更の検証_string_to_enum_判定:
  contents4_判定: contents4_判定
  watch(型変更の検証イベント_string_to_enum):

event 型変更の検証イベント_enum_to_number:
  dummy: string

persister 型変更の検証_enum_to_number:
  contents5: number
  persist("persister型変更の検証"):

persister 型変更の検証_enum_to_number_判定:
  contents5_判定: number
  persist("persister型変更の検証"):

number contents5_演算: 型変更の検証_enum_to_number.contents5 * 2
bool contents5_判定:
  contents5_演算 == 10: true
  else: false

persist 型変更の検証_enum_to_number_判定:
  contents5_判定: contents5_判定
  watch(型変更の検証イベント_enum_to_number):