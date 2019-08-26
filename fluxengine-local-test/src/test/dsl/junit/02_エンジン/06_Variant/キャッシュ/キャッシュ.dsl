test 1 キャッシュがONの場合は2回評価してもプラグイン呼び出しは1回:
  2019/08/26 00:00:01:
    e:
      key: "cacheon"
    inspect:
      回数カウントキャッシュON == "cacheon1":
  2019/08/26 00:00:02:
    e:
      key: "cacheon"
    inspect:
      回数カウントキャッシュON == "cacheon1":

test 2 キャッシュがOFFの場合は2回評価するとプラグイン呼び出しは2回:
  2019/08/26 00:00:01:
    e:
      key: "cacheoff"
    inspect:
      回数カウントキャッシュOFF == "cacheoff1":
  2019/08/26 00:00:02:
    e:
      key: "cacheoff"
    inspect:
      回数カウントキャッシュOFF == "cacheoff2":
