string s1: "abcdefghijklmnopqrstuvwxyz"

map m1:
  n1: 0
  n2: 26

string s2: s1.substring(0, m1.n2)
string s3: s1.substring(1, 25)
string s4: s1.substring( 5,5 )
string s5: s1.substring(26, 26)
string s6: ""
string s7: s6.substring(2, 5)

string s8: s1.substring(10)
string s9: s1.substring(25)
string s10: s1.substring(26)
string s11: s6.substring(m1.n1)

event e1:
  begin: number
  end: number

string s12: s1.substring(e1.begin, e1.end)

persister p1:
  begin: number
  end: number
  persist("key"):

persist p1:
  begin: p1.begin + e1.begin
  end: p1.end + e1.end
  watch(e1):

string s13: s1.substring(p1.begin, p1.end)