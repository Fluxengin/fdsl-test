event e:
    value: number

state s:
    s1:
        e: s2
    s2:
        e:
            e.value > 0: s3
            e.value == 0: s4
            else: s5
    s3:
    s4:
    s5:
    persist(1):
    watch(e):