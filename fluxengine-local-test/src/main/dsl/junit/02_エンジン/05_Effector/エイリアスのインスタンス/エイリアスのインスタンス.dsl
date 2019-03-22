effector ログ出力:
    message: string

event e1:
    dummy: string

effect ログ出力 as ログ出力1:
    message: "ログ出力1から出力"
    watch(e1):

effect ログ出力 as ログ出力2:
    message: "ログ出力2から出力"
    watch(e1):