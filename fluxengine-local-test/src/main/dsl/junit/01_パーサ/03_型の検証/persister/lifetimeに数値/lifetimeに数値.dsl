event e:
    code: string

persister p:
    v: number
    persist(e.code):
        lifetime: 1

persist p:
    v: 0.1
    watch(e):