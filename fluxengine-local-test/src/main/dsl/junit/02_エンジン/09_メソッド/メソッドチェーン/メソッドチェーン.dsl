string s1: "a,b,c,d,e"
bool b2: s1.split(",").join("").split("").join("/").endsWith("e")

list l3: [4, 3, 2, 1]
number n4: l3.avg()
string s5: l3.append(n4).sort().join(",")