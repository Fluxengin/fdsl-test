persister ユーザ情報:
    ID: number
    名前: string
    オプション情報:
        郵便番号: string
        住所: string
        電話番号: string
        操作履歴:
            - map
    誕生日: date
    ユーザ作成日時: datetime
    最終アクセス日時: datetime
    persist(ユーザ作成.ID):

event ユーザ作成:
    ID: number
    名前: string
    郵便番号: string
    住所: string
    電話番号: string
    誕生日: date

persist ユーザ情報:
    ID: ユーザ作成.ID
    名前: ユーザ作成.名前
    オプション情報:
        郵便番号: ユーザ作成.郵便番号
        住所: ユーザ作成.住所
        電話番号: ユーザ作成.電話番号
        操作履歴:
            - 操作名: "ユーザ作成"
              実行日時: now()
    誕生日: ユーザ作成.誕生日
    ユーザ作成日時: now()
    最終アクセス日時: now()
    watch(ユーザ作成):
