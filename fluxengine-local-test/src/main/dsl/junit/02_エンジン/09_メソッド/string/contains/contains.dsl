string s1: "a={0}, b={1}, c={2}"

string s2: s1.format("10", "11", "12")
bool b2: s2.contains("b=11")
bool b3: s2.contains("a=10, b=11, c=12")
bool b4: s2.contains("a=10, b=11, c=12,")