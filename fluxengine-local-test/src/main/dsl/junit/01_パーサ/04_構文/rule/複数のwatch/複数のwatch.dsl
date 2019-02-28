effector f:
    v: number

event e1:
    code: string
    
event e2:
    value: number

rule r:
    e2.value > 0:
    watch(e1):
    watch(e2):

effect f:
    v: 1
    watch(r):