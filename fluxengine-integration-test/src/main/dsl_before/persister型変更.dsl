persister 型変更の検証_期限切れ:
  contents1: string
  persist("persister型変更の検証"):
    lifetime: now()

persister 型変更の検証_期限内:
  contents2: date
  persist("persister型変更の検証"):
    lifetime: today()

persister 型変更の検証_期限内_error:
  contents2_error: date
  persist("persister型変更の検証"):
    lifetime: today()

persister 型変更の検証_計算可能1:
  contents3: string
  persist("persister型変更の検証"):
    lifetime: today()

event 型変更の検証イベント:
  dummy: string

event 型変更の検証イベント_error1:
  dummy: string

persist 型変更の検証_期限切れ:
  contents1: "型変更の検証_期限切れ_before"
  watch(型変更の検証イベント):

persist 型変更の検証_期限内:
  contents2: today()
  watch(型変更の検証イベント):

persist 型変更の検証_期限内_error:
  contents2_error: today()
  watch(型変更の検証イベント_error1):

persist 型変更の検証_計算可能1:
  contents3: "123"
  watch(型変更の検証イベント):

# enumと内部値の変換

event 型変更の検証イベント_string_to_enum:
  dummy: string

persister 型変更の検証_string_to_enum:
  contents4: string
  persist("persister型変更の検証"):

persist 型変更の検証_string_to_enum:
  contents4: "A"
  watch(型変更の検証イベント_string_to_enum):

enum enum型変更の検証_enum_to_number:
  5: ON
  0: OFF

event 型変更の検証イベント_enum_to_number:
  dummy: string

persister 型変更の検証_enum_to_number:
  contents5: enum型変更の検証_enum_to_number
  persist("persister型変更の検証"):

persist 型変更の検証_enum_to_number:
  contents5: enum型変更の検証_enum_to_number.ON
  watch(型変更の検証イベント_enum_to_number):