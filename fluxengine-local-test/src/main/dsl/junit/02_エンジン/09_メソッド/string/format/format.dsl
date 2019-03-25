string s1: "a={0}, b={1}, c={1}"

string s2: s1.format("A") # 引数が少ない
string s3: s1.format("A", "B", "C") # 引数が多い
