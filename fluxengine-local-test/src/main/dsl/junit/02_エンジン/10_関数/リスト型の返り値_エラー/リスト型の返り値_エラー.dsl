list l1: ユーティリティ.createList("index")

list l2: l1.filter(name.startsWith("a"))

list l2expected:
    - key: "index1"
      name: "a"
      value: 1
    - key: "index2"
      name: "aa"
      value: 2
