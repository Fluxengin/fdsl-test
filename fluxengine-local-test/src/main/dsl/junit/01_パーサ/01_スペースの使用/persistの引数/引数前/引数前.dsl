event e:
    value: number

persister p:
    c: string
    persist( e.value):

persist p:
    c: "abc"
    watch( e):

bool b: p.exists()
