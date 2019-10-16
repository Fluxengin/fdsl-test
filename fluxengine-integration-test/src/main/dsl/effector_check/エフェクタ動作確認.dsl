# エフェクタ動作確認のためのeventとeffector
event エフェクタ送信イベント:
  ユーザーID: string
  メッセージ: string

effector DB書き込み送信:
  ユーザーID: string
  日時: datetime
  メッセージ: string

effect DB書き込み送信:
  ユーザーID: エフェクタ送信イベント.ユーザーID
  日時: now()
  メッセージ: エフェクタ送信イベント.メッセージ
  watch(エフェクタ送信イベント):

event エフェクタ送信イベント2:
  ユーザーID: string
  数値: number
  メッセージ: string

effector DB書き込み送信2:
  ユーザーID: string
  数値: number
  メッセージ: string

effect DB書き込み送信2:
  ユーザーID: エフェクタ送信イベント2.ユーザーID
  数値: エフェクタ送信イベント2.数値
  メッセージ: エフェクタ送信イベント2.メッセージ
  watch(エフェクタ送信イベント2):
