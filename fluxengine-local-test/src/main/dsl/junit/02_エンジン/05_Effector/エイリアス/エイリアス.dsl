effector 通知:
    message: string

event e1:
    value: number


effect 通知 as 通知1:
    message: "通知1が実行されました"
    watch(e1):

effect 通知 as 通知2:
    message: "通知2が実行されました"
    watch(e1):

effect 通知 as 通知3:
    message: "通知3が実行されました"
    watch(e1):
