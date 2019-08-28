date d: today()

export string 文字列<端末ID>:
    get(端末ID):
       cache: ユーティリティ.weekday()

export number 数値<端末ID>:
    get(端末ID):
       cache: today()

export struct List情報<端末ID>:
    ユーザーID: string
    パケット上限: number
    get(端末ID):
       cache: today()