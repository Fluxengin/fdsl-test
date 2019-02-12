event e1:
    code: string
    value: number

effector f1:
    c: string
    v: number

rule r1_1:
    e1.value > 0:
    watch(e1):

rule r1_2:
    e1.value > 0:
    watch( r1_1):

rule r1_3:
    e1.value > 0:
    watch(r1_2 ):

effect f1:
    c: e1.code
    v: e1.value
    watch( r1_3 ):

event e2:
    code: string
    value: number

effector f2:
    c: string
    v: number

rule r2_1:
    e2.value > 0:
    watch(e2):

rule r2_2:
    e2.code == "code01":
    watch(e2):

rule r2_3:
    e2.value > 0:
    watch(r2_1,r2_2):

rule r2_4:
    e2.value > 0:
    watch( r2_2,r2_3):

rule r2_5:
    e2.value > 0:
    watch(r2_3 ,r2_4):

rule r2_6:
    e2.value > 0:
    watch( r2_4 ,r2_5):

rule r2_7:
    e2.value > 0:
    watch(r2_5, r2_6):

rule r2_8:
    e2.value > 0:
    watch(r2_6,r2_7 ):

effect f2:
    c: e2.code
    v: e2.value
    watch(r2_7, r2_8 ):


event e3:
    code: string
    value: number

effector f3:
    c: string
    v: number

rule r3_1:
    e3.value > 0:
    watch(e3):

rule r3_2:
    e3.code == "code03":
    watch(e3):

rule r3_3:
    e3.value > 0:
    watch(r3_1&&r3_2):

rule r3_4:
    e3.value > 0:
    watch( r3_2&&r3_3):

rule r3_5:
    e3.value > 0:
    watch(r3_3 &&r3_4):

rule r3_6:
    e3.value > 0:
    watch( r3_4 &&r3_5):

rule r3_7:
    e3.value > 0:
    watch(r3_5&& r3_6):

rule r3_8:
    e3.value > 0:
    watch(r3_6&&r3_7 ):

effect f3:
    c: e3.code
    v: e3.value
    watch(r3_7&& r3_8 ):


event e4:
    code: string
    value: number

effector f4:
    c: string
    v: number

rule r4_1:
    e4.value > 0:
    watch(e4):

rule r4_2:
    e4.code == "code01":
    watch(e4):

rule r4_3:
    e4.value < 100:
    watch(e4):

rule r4_4:
    e4.value > 0:
    watch(r4_1,r4_2,r4_3):

rule r4_5:
    e4.value > 0:
    watch( r4_2,r4_3,r4_4):

rule r4_6:
    e4.value > 0:
    watch(r4_3 ,r4_4,r4_5):

rule r4_7:
    e4.value > 0:
    watch( r4_4 ,r4_5,r4_6):

rule r4_8:
    e4.value > 0:
    watch(r4_5, r4_6,r4_7):

rule r4_9:
    e4.value > 0:
    watch(r4_6,r4_7 ,r4_8):

rule r4_10:
    e4.value > 0:
    watch(r4_7, r4_8 ,r4_9):

rule r4_11:
    e4.value > 0:
    watch(r4_8,r4_9, r4_10):

rule r4_12:
    e4.value > 0:
    watch(r4_9,r4_10,r4_11 ):

effect f4:
    c: e4.code
    v: e4.value
    watch(r4_10,r4_11, r4_12 ):
        

event e5:
    code: string
    value: number

effector f5:
    c: string
    v: number

rule r5_1:
    e5.value > 0:
    watch(e5):

rule r5_2:
    e5.code == "code 05":
    watch(e5):

rule r5_3:
    e5.value < 100:
    watch(e5):

rule r5_4:
    e5.value > 0:
    watch(r5_1&&r5_2&&r5_3):

rule r5_5:
    e5.value > 0:
    watch( r5_2&&r5_3&&r5_4):

rule r5_6:
    e5.value > 0:
    watch(r5_3 &&r5_4&&r5_5):

rule r5_7:
    e5.value > 0:
    watch( r5_4 &&r5_5&&r5_6):

rule r5_8:
    e5.value > 0:
    watch(r5_5&& r5_6&&r5_7):

rule r5_9:
    e5.value > 0:
    watch(r5_6&&r5_7 &&r5_8):

rule r5_10:
    e5.value > 0:
    watch(r5_7&& r5_8 &&r5_9):

rule r5_11:
    e5.value > 0:
    watch(r5_8&&r5_9&& r5_10):

rule r5_12:
    e5.value > 0:
    watch(r5_9&&r5_10&&r5_11 ):

effect f5:
    c: e5.code
    v: e5.value
    watch(r5_10&&r5_11&& r5_12 ):
        