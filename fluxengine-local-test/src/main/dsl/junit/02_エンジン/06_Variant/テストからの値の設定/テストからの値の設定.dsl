event e1:
    value: number

number n1:
    e1.value > 0: 1
    else: 0

string s2:
    n1 == 1: "ON"
    else: "OFF"