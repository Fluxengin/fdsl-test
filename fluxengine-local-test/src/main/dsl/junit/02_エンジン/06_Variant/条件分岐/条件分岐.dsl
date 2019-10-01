event e:
  code: string
  value: number

number r:
  get():

number n: e.value * 2

number m:
  e.code == "01": (r + 1) * 3
  e.code == "02": n * n
  else: r * e.value