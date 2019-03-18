event e1:
    message: string

rule 条件なしルール:
    watch(e1):

effector f1:
    message: string

effect f1:
    message: e1.message
    watch(条件なしルール):