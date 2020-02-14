string s1: "a,b,c,d,e"
bool b2: s1.split(",").join("").split("").join("/").endsWith("e/")

list l3: [4, 3, 2, 1]
number n4: l3.avg()
string s5: l3.append(n4).sort().join(",")

string s6: "1,2,3,4,5,6,7,8,9,10"
string s7: s6.substring(2).substring(2,16).substring(0, 13).split(",").join("/").substring(1)