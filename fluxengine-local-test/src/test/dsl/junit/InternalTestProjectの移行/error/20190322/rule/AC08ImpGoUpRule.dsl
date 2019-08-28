 test 1 s1:
   2018/11/10 00:12:34:
       ImpGoUpEvent:
         accountId: 4000               # アカウントID
         campaignId: 04001             # キャンペーンID
         cost1: 10                 # 前日）のキャンペーン.search_impression_share
         cost2: 20                 # 前々日）のキャンペーン.search_impression_share
         campaignName: "campName08"          # イベント名
       inspect:
            holderState.currentState == "over":