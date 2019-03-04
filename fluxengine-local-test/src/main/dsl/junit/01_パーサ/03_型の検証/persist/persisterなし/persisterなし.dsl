event e:
    code: string
    value: number

persist p:
    c: "abc"
    v: e.value
    watch(e):