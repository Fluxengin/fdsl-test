enum ON_OFF:
    0: OFF
    1: ON

event ユーザイベント:
    userId: number
    userName: string
    availableFunctions:
        機能1: ON_OFF
        機能2: ON_OFF
        機能3: ON_OFF
    状態: ON_OFF

string s1:
    ユーザイベント.availableFunctions.機能1 == ON: "機能1ON"
    else: "機能1OFF"