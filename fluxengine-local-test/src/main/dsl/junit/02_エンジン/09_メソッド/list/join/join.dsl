list l1: [1, 2, 3]
list l2: ["a", "b", "c"]

string separator: " , "

string s1: l1.join(",")
string s2: l1.join(separator)
string s3: l1.join(l2.join(""))
