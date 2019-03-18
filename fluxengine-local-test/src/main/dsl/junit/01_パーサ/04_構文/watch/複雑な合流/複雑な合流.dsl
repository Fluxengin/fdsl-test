event e1:
    value: number

rule r1:
    e1.value < 0:
    watch(e1):

rule r2:
    e1.value > 1000:
    watch(e1):

rule r3:
    e1.value != 1100:
    watch(e1):

persister p1:
    value: number
    persist("key"):

persist p1:
    value: e1.value
    watch((r1, r2) && r3):
