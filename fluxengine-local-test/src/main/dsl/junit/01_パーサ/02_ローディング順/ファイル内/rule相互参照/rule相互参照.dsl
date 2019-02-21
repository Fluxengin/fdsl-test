event e:
    value: number

rule r1:
    e.value > 0:
    watch(r2, e):

rule r2:
    e.value > 0:
    watch(r1):
