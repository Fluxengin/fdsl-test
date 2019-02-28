# 同じimport文を2回かいても仕様上OKとのこと
import s: export
import s: export

string s1: s + "_imported"