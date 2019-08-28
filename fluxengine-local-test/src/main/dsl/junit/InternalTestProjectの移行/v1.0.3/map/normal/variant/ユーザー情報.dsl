#端末IDから現在日時のユーザー情報を取得
export struct ユーザー情報<端末ID>:
    ユーザーID: string
    パケット上限: number
    getbyParameters(端末ID):

string str:  "a={0}, b={1}"
number n: 2

map m_variant:
   a1: "a"
   a2: 1
   a3: 2
   a4: 3
   a5: 4
   a6: 5


export map case_4_map:
   a1: m_variant.a1
   a2: m_variant.a2
   a3: m_variant.a3
   a4: m_variant.a4
   a5: m_variant.a5
   a6: m_variant.a6