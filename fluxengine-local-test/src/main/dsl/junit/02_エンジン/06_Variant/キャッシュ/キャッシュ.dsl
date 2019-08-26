event e:
  key: string

string 回数カウントキャッシュON:
  get(e.key):
    cache: today()

string 回数カウントキャッシュOFF:
  get(e.key):
    cache: off
