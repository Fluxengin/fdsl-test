list l1: ユーティリティ.createList("index")

list l3: l1.filter(value > 2)

list l3expected:
    - key: "index3"
      name: "ccc"
      value: 300