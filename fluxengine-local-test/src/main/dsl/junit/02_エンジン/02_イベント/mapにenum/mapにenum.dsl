# 最初はFLAG_OFF, FLAG_ONではなくOFF, ONにしていたが、
# YAMLの仕様でOFF→false, ON→trueに変換されるため、ON, OFFは使ってはいけないとのこと
enum ON_OFF:
    0: FLAG_OFF
    1: FLAG_ON

event ユーザイベント:
    userId: number
    userName: string
    availableFunctions:
        機能1: ON_OFF
        機能2: ON_OFF
        機能3: ON_OFF
    状態: ON_OFF

string s1:
    ユーザイベント.availableFunctions.機能1 == FLAG_ON: "機能1ON"
    else: "機能1OFF"