test 1 掲載データ-キャンペーンが1件投入する場合:
   2019/03/01 21:01:00:
       campaignStartServingStatusEvent:
           media: "YSS"              # 媒体
           adProjectSeq: 11001       # 広告案件SEQ
           adProject: "広告案件１０" # 広告案件
           accountId: 100            # アカウントID
           campaignId: 200           # キャンペーンID
test 2 掲載データ-キャンペーンが複数件投入する場合:
   2019/03/01 21:01:00:
       campaignStartServingStatusEvent:
           media: "YDN"              # 媒体
           adProjectSeq: 11002       # 広告案件SEQ
           adProject: "広告案件１０" # 広告案件
           accountId: 102            # アカウントID
           campaignId: 202           # キャンペーンID
   2019/03/01 21:02:00:
       campaignStartServingStatusEvent:
           media: "YDN"              # 媒体
           adProjectSeq: 11003       # 広告案件SEQ
           adProject: "広告案件１０" # 広告案件
           accountId: 103            # アカウントID
           campaignId: 203           # キャンペーンID