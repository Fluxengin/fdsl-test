export persister p1<key>:
    x: number
    persist(key):

import p2<"xx">: rule/file2

export number n: 1 + p2.y