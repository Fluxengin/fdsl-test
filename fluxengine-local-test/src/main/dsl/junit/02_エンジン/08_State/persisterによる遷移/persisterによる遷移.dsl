event e1:
    code: string
    value: number

persister p1:
    value: number
    persist(e1.code):

persist p1:
    value: e1.value
    watch(e1):

rule r1:
    e1.value == 0:
    e1.value != 0:
    watch(e1):

state st:
    s1:
        r1:
            p1.value > 0: s2
    s2:
    watch(r1):
    persist(e1.code):

#event e1:
#    code: string
#    value: number

#persister p1:
#    value: number
#    persist(e1.code):

#persist p1:
#    value: e1.value
#    watch(st2):

#rule r1:
#    st2.currentState == s3:
#    watch(st2):

#state st:
#    s1:
#        r1:
#            p1.value > 0: s2
#    s2:
#    watch(r1):
#    persist(e1.code):

#state st2:
#    s3:
#        e1:
#            e1.value > 0: s3
#    s4:
#    watch(e1):
#    persist(e1.code):