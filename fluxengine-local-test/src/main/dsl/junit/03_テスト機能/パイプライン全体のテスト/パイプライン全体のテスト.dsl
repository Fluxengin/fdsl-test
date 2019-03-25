event e:
    cd: string
    value: number

persister p:
    value: number
    persist(e.cd):
        lifetime: today()

effector f:
    msg: string
    currentDatetime: datetime

number n: e.value + p.value

persist p:
    value: n
    watch(e):

rule r:
    n > 100:
    watch(e):

effect f:
    msg: "message"
    currentDatetime: now()
    watch(r):