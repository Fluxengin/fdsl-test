test 1 状態遷移の分岐にpersisterを使える:
    2019/03/25 00:00:02:
        inspect:
            p1.exists() == false:
            p1.value == 0:
            st.currentState == s1:
    2019/03/26 00:00:01:
        e1:
            code: "01"
            value: 1
        inspect:
            p1.exists():
            p1.value == 1:
            st.currentState == s2:
