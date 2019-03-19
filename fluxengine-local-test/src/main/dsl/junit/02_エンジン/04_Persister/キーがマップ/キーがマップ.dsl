event e1:
    code: string
    value: number

map key:
    key1: e1.code
    key2: e1.value

persister p1:
    message: string
    persist(key):

persist p1:
    message: "保存しました"
    watch(e1):