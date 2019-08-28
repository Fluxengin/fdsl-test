enum enum文字:
    "apple": リンゴ
    "orange": オレンジ
    "banana": バナナ

event イベント1:
    success: bool
    player_info:
        name: string
        age: number
        birthday: date
        registration_datetime: datetime
        extra_info:
            - number
    memo:
        - string
    kind: enum文字

number n1:
    イベント1.player_info.registration_datetime >= 2019/01/01 00:00:00: 1
    else: 2

string s1:
    イベント1.memo.count() > 0: "メモあり"
    else: "メモなし"

bool b1:
    イベント1.kind == enum文字.リンゴ : true
    else: false