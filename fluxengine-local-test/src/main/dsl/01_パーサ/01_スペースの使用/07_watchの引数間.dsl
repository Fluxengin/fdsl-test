effector f:
    c: string
    v: number

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
