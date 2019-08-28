test 1 s1:
    2018/11/10 21:00:00: {sevEvent: {accountId: 71002, keywordMatchType: "A", keyword: "C", media: "YSS"}, inspect: {p.skmt == "A":}}
test 1 s2:
    2018/11/10 00:12:34: {sevEvent: {accountId: 71002, keywordMatchType: "A", keyword: "C", media: "YSS"}, inspect: {p.skmt == "A":}}
test 1 s3:
    2018/11/10 21:00:00: {sevEvent: {accountId: 71002, keywordMatchType: "A", keyword: "C", media: "YSS"}, inspect: {p.skmt == "A":}}
    2018/11/10 00:12:34: {sevEvent: {accountId: 71002, keywordMatchType: "A", keyword: "C", media: "YSS"}, inspect: {p.skmt == "A":}}


test 1 s4:
  2018/11/10 00:12:34:
      sevEvent:
          accountId: 71002
          keywordMatchType: "A"
          keyword: "C"
          media: "YSS"
      inspect:
          p.skmt  == "A":

test 1 s5:
  2018/11/10 21:00:00:
      sevEvent:
          accountId: 71002
          keywordMatchType: "A"
          keyword: "C"
          media: "YSS"
      inspect:
          p.skmt  == "A":

test 1 s6:
  2018/11/10 00:12:34:
     sevEvent:
         accountId: 71002
         keywordMatchType: "A"
         keyword: "C"
         media: "YSS"
     inspect:
         p.skmt  == "A":

  2018/11/10 21:00:00:
     sevEvent:
         accountId: 71002
         keywordMatchType: "A"
         keyword: "C"
         media: "YSS"
     inspect:
         p.skmt  == "A":

test 2 s1:
  2018/11/10 21:00:00:
     sevEvent:
         accountId: 71002
         keywordMatchType: "A"
         keyword: "C"
         media: "YSS"
     inspect:
         test.a1 == 1000:

test 2 s2:
  2018/11/10 21:00:00:
     sevEvent:
         accountId: 71002
         keywordMatchType: "A"
         keyword: "C"
         media: "YSS"
     inspect:
         b1 == true:

test 2 s3:
  2018/11/10 21:00:00:
     sevEvent:
         accountId: 71002
         keywordMatchType: "A"
         keyword: "C"
         media: "YSS"
     inspect:
         b2 == true:

test 2 s4:
  2018/11/10 21:00:00:
     sevEvent:
         accountId: 71002
         keywordMatchType: "A"
         keyword: "C"
         media: "YSS"
     inspect:
         b3 == true:


test 2 s5:
  2018/11/10 21:00:00:
     sevEvent:
         accountId: 71002
         keywordMatchType: "A"
         keyword: "C"
         media: "YSS"
     inspect:
         result == []:

