persist p:
    amount: 1
    watch(e):

event e:
    k: string
    v: number

number n: (1) + p.amount

persister p:
    amount: number
    persist("key"):