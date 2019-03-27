effector ff:
    a1:
        a11: number
        a12: string
    a2:
        a21: date
        a22:
            a221: string
            a222: string
    a3: list
    a4:
        -
            a41: string
            a42: number
            a43: date

event e1:
    code: string
    value: number

string svalue: "value "

effect ff:
    a1:
        a11: e1.value + 1
        a12: e1.code + "code"
    a2:
        a21: today()
        a22:
            a221: "constant"
            a222: svalue + e1.value
    a3:
        - "list"
        - "of"
        - "string"
    a4:
        - a41: "a41-1"
          a42: 1
          a43: 2019/03/26
        - a41: "a41-2"
          a42: 2
          a43: 2019/03/27
    watch(e1):