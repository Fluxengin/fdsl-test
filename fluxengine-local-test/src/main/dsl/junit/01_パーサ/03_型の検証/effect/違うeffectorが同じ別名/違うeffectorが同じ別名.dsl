effector f1:
    message: string

effector f2:
    message: string

event e1:
    value: number

effect f1 as 通知:
    message: "f1の通知"
    watch(e1):

effect f2 as 通知:
    message: "f2の通知"
    watch(e1):
