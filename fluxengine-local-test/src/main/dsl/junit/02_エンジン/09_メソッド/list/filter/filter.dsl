list l1:
  - keyA: 100
    keyB: "a"
    keyC: true
  - keyA: 200
    keyB: "b"
    keyC: false
    keyD: "keyD"
  - keyA: 300
    keyB: "c"

list l2: l1.filter(keyA < 300)
list l3: l1.filter(keyD == "keyD")
list l4: l1.filter(keyA < 300 || keyB == "c")
list l5: l1.filter(keyC == false && keyA < 200)