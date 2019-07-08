import メール送信: effector/ユーザー通知

event e1:
    code: string
    value: number

event e2:
    code: string
    value: number

persister p:
    c: string
    v: number
    persist("key"):
        lifetime: today()

number n: e1.value + e2.value

persist p:
    c: "abc"
    v: n
    watch(e1 && e2):

effect メール送信:
    値:
    ユーザーID:
    日時: now()
    メッセージ: "e1とe2を入信しました"
    watch(e1 && e2):