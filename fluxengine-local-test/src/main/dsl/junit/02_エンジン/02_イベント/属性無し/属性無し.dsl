event e1:

persister p1:
    value: number
    persist("a"):

persist p1:
    value: 100
    watch(e1):