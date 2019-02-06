# 現状、effect解析失敗：07_watchの引数間#f■詳細: c e.code というエラーが出る

event e:
    code: string
    value: number

rule r:
    e.value > 0:
    watch(e):

effect f:
    c: e.code
    v: e.value
    watch(r):
