event e1:
    code: string

persister p1:
    persist(e1.code):

persist p1:
    watch(e1):