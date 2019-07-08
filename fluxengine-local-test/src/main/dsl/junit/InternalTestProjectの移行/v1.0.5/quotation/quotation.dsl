string s1_1: "a"
string s1_2: "'a"
string s1_3: "'a'"
string s1_4: "a'b'c"
string s1_5: "a'"
string s1_6: "''"
string s1_7: "'"
string s1_8: ""
string s1_9: "'''"

bool b1_1:
   s1_1 == "a" && s1_1.contains("a") && s1_1.startsWith("a") && s1_1.endsWith("a"): true
   else: false

bool b1_2:
   s1_2 == "'a" && s1_2.contains("'a") && s1_2.startsWith("'a") && s1_2.endsWith("'a"): true
   else: false

bool b1_3:
   s1_3 == "'a'" && s1_3.contains("'a'") && s1_3.startsWith("'a'") && s1_3.endsWith("'a'"): true
   else: false

bool b1_4:
   s1_4 == "a'b'c" && s1_4.contains("a'b'c") && s1_4.startsWith("a'b'c") && s1_4.endsWith("a'b'c"): true
   else: false

bool b1_5:
   s1_5 == "a'" && s1_5.contains("a'") && s1_5.startsWith("a'") && s1_5.endsWith("a'"): true
   else: false

bool b1_6:
   s1_6 == "''" && s1_6.contains("''") && s1_6.startsWith("''") && s1_6.endsWith("''"): true
   else: false

bool b1_7:
   s1_7 == "'" && s1_7.contains("'") && s1_7.startsWith("'") && s1_7.endsWith("'"): true
   else: false

bool b1_8:
   s1_8 == "" && s1_8.contains("") && s1_8.startsWith("") && s1_8.endsWith(""): true
   else: false

bool b1_9:
   s1_9 == "'''" && s1_9.contains("'''") && s1_9.startsWith("'''") && s1_9.endsWith("'''"): true
   else: false

list expectedSplitResult1: ["a", "b", "c"]
list splitResult1: s1_4.split("'")

bool b1_10:
    splitResult1 == expectedSplitResult1: true
    else: false

list expectedSplitResult2: ["", "", "", ""]
list splitResult2: s1_9.split("'")

bool b1_11:
    splitResult2 == expectedSplitResult2: true
    else: false

list l1: ["a", "'a", "'a'", "a'b'c", "a'", "''", "'", "", "'''"]
bool b1_12:
    l1.contains("a") && l1.contains("'a") && l1.contains("'a'") && l1.contains("a'b'c") && l1.contains("a'") && l1.contains("''") && l1.contains("'") && l1.contains("") && l1.contains("'''"): true
    else: false

string joinResult1: l1.join(",")
bool b1_13:
    joinResult1 == "a,'a,'a',a'b'c,a','',',,'''": true
    else: false

list l2:
    - {name: "a"}
    - {name: "'a"}
    - {name: "'a'"}
    - {name: "a'b'c"}
    - {name: "a'"}
    - {name: "''"}
    - {name: "'"}
    - {name: ""}
    - {name: "'''"}

list l2_exp_1: [{name: "a"}]
list l2_exp_2: [{name: "'a"}]
list l2_exp_3: [{name: "'a'"}]
list l2_exp_4: [{name: "a'b'c"}]
list l2_exp_5: [{name: "a'"}]
list l2_exp_6: [{name: "''"}]
list l2_exp_7: [{name: "'"}]
list l2_exp_8: [{name: ""}]
list l2_exp_9: [{name: "'''"}]

list l2_1: l2.filter(name == "a")
list l2_2: l2.filter(name == "'a")
list l2_3: l2.filter(name == "'a'")
list l2_4: l2.filter(name == "a'b'c")
list l2_5: l2.filter(name == "a'")
list l2_6: l2.filter(name == "''")
list l2_7: l2.filter(name == "'")
list l2_8: l2.filter(name == "")
list l2_9: l2.filter(name == "'''")

bool b1_14:
    l2_1 == l2_exp_1 && l2_2 == l2_exp_2 && l2_3 == l2_exp_3 && l2_4 == l2_exp_4 && l2_5 == l2_exp_5 && l2_6 == l2_exp_6 && l2_7 == l2_exp_7 && l2_8 == l2_exp_8 && l2_9 == l2_exp_9: true
    else: false

string s3_1: 'a'
string s3_2: '"a'
string s3_3: '"a"'
string s3_4: 'a"b"c'
string s3_5: 'a"'
string s3_6: '""'
string s3_7: '"'
string s3_8: ''
string s3_9: '"""'

bool b3_1:
   s3_1 == 'a' && s3_1.contains('a') && s3_1.startsWith('a') && s3_1.endsWith('a'): true
   else: false

bool b3_2:
   s3_2 == '"a' && s3_2.contains('"a') && s3_2.startsWith('"a') && s3_2.endsWith('"a'): true
   else: false

bool b3_3:
   s3_3 == '"a"' && s3_3.contains('"a"') && s3_3.startsWith('"a"') && s3_3.endsWith('"a"'): true
   else: false

bool b3_4:
   s3_4 == 'a"b"c' && s3_4.contains('a"b"c') && s3_4.startsWith('a"b"c') && s3_4.endsWith('a"b"c'): true
   else: false

bool b3_5:
   s3_5 == 'a"' && s3_5.contains('a"') && s3_5.startsWith('a"') && s3_5.endsWith('a"'): true
   else: false

bool b3_6:
   s3_6 == '""' && s3_6.contains('""') && s3_6.startsWith('""') && s3_6.endsWith('""'): true
   else: false

bool b3_7:
   s3_7 == '"' && s3_7.contains('"') && s3_7.startsWith('"') && s3_7.endsWith('"'): true
   else: false

bool b3_8:
   s3_8 == '' && s3_8.contains('') && s3_8.startsWith('') && s3_8.endsWith(''): true
   else: false

bool b3_9:
   s3_9 == '"""' && s3_9.contains('"""') && s3_9.startsWith('"""') && s3_9.endsWith('"""'): true
   else: false

list expectedSplitResult3: ['a', 'b', 'c']
list splitResult3: s3_4.split('"')

bool b3_10:
    splitResult3 == expectedSplitResult3: true
    else: false

list expectedSplitResult4: ['', '', '', '']
list splitResult4: s3_9.split('"')

bool b3_11:
    splitResult4 == expectedSplitResult4: true
    else: false

list l3: ['a', '"a', '"a"', 'a"b"c', 'a"', '""', '"', '', '"""']
bool b3_12:
    l3.contains('a') && l3.contains('"a') && l3.contains('"a"') && l3.contains('a"b"c') && l3.contains('a"') && l3.contains('""') && l3.contains('"') && l3.contains('') && l3.contains('"""'): true
    else: false

string joinResult3: l3.join(",")
bool b3_13:
    joinResult3 == 'a,"a,"a",a"b"c,a","",",,"""': true
    else: false

list l4:
    - {name: 'a'}
    - {name: '"a'}
    - {name: '"a"'}
    - {name: 'a"b"c'}
    - {name: 'a"'}
    - {name: '""'}
    - {name: '"'}
    - {name: ''}
    - {name: '"""'}

list l4_exp_1: [{name: 'a'}]
list l4_exp_2: [{name: '"a'}]
list l4_exp_3: [{name: '"a"'}]
list l4_exp_4: [{name: 'a"b"c'}]
list l4_exp_5: [{name: 'a"'}]
list l4_exp_6: [{name: '""'}]
list l4_exp_7: [{name: '"'}]
list l4_exp_8: [{name: ''}]
list l4_exp_9: [{name: '"""'}]

list l4_1: l4.filter(name == 'a')
list l4_2: l4.filter(name == '"a')
list l4_3: l4.filter(name == '"a"')
list l4_4: l4.filter(name == 'a"b"c')
list l4_5: l4.filter(name == 'a"')
list l4_6: l4.filter(name == '""')
list l4_7: l4.filter(name == '"')
list l4_8: l4.filter(name == '')
list l4_9: l4.filter(name == '"""')

bool b3_14:
    l4_1 == l4_exp_1 && l4_2 == l4_exp_2 && l4_3 == l4_exp_3 && l4_4 == l4_exp_4 && l4_5 == l4_exp_5 && l4_6 == l4_exp_6 && l4_7 == l4_exp_7 && l4_8 == l4_exp_8 && l4_9 == l4_exp_9: true
    else: false

string template1: "1={0}, 2={1}, 3={2}, 4={3}, 5={4}, 6={5}, 7={6}, 8={7}, 9={8}"
string s5_1: template1.format("a", "'a", "'a'", "a'b'c", "a'", "''", "'", "", "'''")
bool b5_1:
    s5_1 == "1=a, 2='a, 3='a', 4=a'b'c, 5=a', 6='', 7=', 8=, 9='''": true
    else: false

string template2: '1={0}, 2={1}, 3={2}, 4={3}, 5={4}, 6={5}, 7={6}, 8={7}, 9={8}'
string s5_2: template2.format('a', '"a', '"a"', 'a"b"c', 'a"', '""', '"', '', '"""')
bool b5_2:
    s5_2 == '1=a, 2="a, 3="a", 4=a"b"c, 5=a", 6="", 7=", 8=, 9="""': true
    else: false

string template3: 'a={0}'
string s5_3: template3.format("'")
bool b5_3:
    s5_3 == "a='": true
    else: false

string template4: "a={0}"
string s5_4: template4.format('"')
bool b5_4:
    s5_4 == 'a="': true
    else: false

# errors
# string s2_1: """
# string s2_2: ""a"
# string s2_3: ""a""
# string s2_4: "a""
# string s2_5: """"
# string s2_6: "'""
# string s2_7: "'"'"
# string s2_11: "'

# string s4_1: '''
# string s4_2: ''a'
# string s4_3: ''a''
# string s4_4: 'a''
# string s4_5: ''''
# string s4_6: '"''
# string s4_7: '"'"'
# string s4_11: '"