event e:
    enabled: bool

effect f:
    c: "123"
    v:
        e.enabled: 1
        else: -1
    watch(e):