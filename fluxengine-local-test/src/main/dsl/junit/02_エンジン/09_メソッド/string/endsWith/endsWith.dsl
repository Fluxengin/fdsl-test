string s1: "abc{0}fgh"
string s2: s1.format("de")

bool b1: s2.endsWith("h")
bool b2: s2.endsWith("fgh")
bool b3: s2.endsWith("efgh")
bool b4: s2.endsWith("abcdefgh")
bool b5: s2.endsWith("0abcdefgh")