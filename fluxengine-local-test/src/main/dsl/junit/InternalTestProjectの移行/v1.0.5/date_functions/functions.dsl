date d1_0: today()
date d1_1: today(+1d)
date d1_2: today(+1w)
date d1_3: today(+1m)
date d1_4: today(+1y)
date d1_5: today(-1d)
date d1_6: today(-1w)
date d1_7: today(-1m)
date d1_8: today(-1y)

bool b1_0:
    d1_0 == 2018/11/10: true
    else: false

bool b1_1:
    d1_1 == 2018/11/11: true
    else: false

bool b1_2:
    d1_2 == 2018/11/17: true
    else: false

bool b1_3:
    d1_3 == 2018/12/10: true
    else: false

bool b1_4:
    d1_4 == 2019/11/10: true
    else: false

bool b1_5:
    d1_5 == 2018/11/09: true
    else: false

bool b1_6:
    d1_6 == 2018/11/03: true
    else: false

bool b1_7:
    d1_7 == 2018/10/10: true
    else: false

bool b1_8:
    d1_8 == 2017/11/10: true
    else: false


date d2_0: this_week_first()
date d2_1: this_week_first(+1d)
date d2_2: this_week_first(+1w)
date d2_3: this_week_first(+1m)
date d2_4: this_week_first(+1y)
date d2_5: this_week_first(-1d)
date d2_6: this_week_first(-1w)
date d2_7: this_week_first(-1m)
date d2_8: this_week_first(-1y)

bool b2_0:
    d2_0 == 2018/11/05: true
    else: false

bool b2_1:
    d2_1 == 2018/11/06: true
    else: false

bool b2_2:
    d2_2 == 2018/11/12: true
    else: false

bool b2_3:
    d2_3 == 2018/12/05: true
    else: false

bool b2_4:
    d2_4 == 2019/11/05: true
    else: false

bool b2_5:
    d2_5 == 2018/11/04: true
    else: false

bool b2_6:
    d2_6 == 2018/10/29: true
    else: false

bool b2_7:
    d2_7 == 2018/10/05: true
    else: false

bool b2_8:
    d2_8 == 2017/11/05: true
    else: false

date d3_0: this_week_last()
date d3_1: this_week_last(+1d)
date d3_2: this_week_last(+1w)
date d3_3: this_week_last(+1m)
date d3_4: this_week_last(+1y)
date d3_5: this_week_last(-1d)
date d3_6: this_week_last(-1w)
date d3_7: this_week_last(-1m)
date d3_8: this_week_last(-1y)

bool b3_0:
    d3_0 == 2018/11/11: true
    else: false

bool b3_1:
    d3_1 == 2018/11/12: true
    else: false

bool b3_2:
    d3_2 == 2018/11/18: true
    else: false

bool b3_3:
    d3_3 == 2018/12/11: true
    else: false

bool b3_4:
    d3_4 == 2019/11/11: true
    else: false

bool b3_5:
    d3_5 == 2018/11/10: true
    else: false

bool b3_6:
    d3_6 == 2018/11/04: true
    else: false

bool b3_7:
    d3_7 == 2018/10/11: true
    else: false

bool b3_8:
    d3_8 == 2017/11/11: true
    else: false

date d4_0: this_month_first()
date d4_1: this_month_first(+1d)
date d4_2: this_month_first(+1w)
date d4_3: this_month_first(+1m)
date d4_4: this_month_first(+1y)
date d4_5: this_month_first(-1d)
date d4_6: this_month_first(-1w)
date d4_7: this_month_first(-1m)
date d4_8: this_month_first(-1y)

bool b4_0:
    d4_0 == 2018/11/01: true
    else: false

bool b4_1:
    d4_1 == 2018/11/02: true
    else: false

bool b4_2:
    d4_2 == 2018/11/08: true
    else: false

bool b4_3:
    d4_3 == 2018/12/01: true
    else: false

bool b4_4:
    d4_4 == 2019/11/01: true
    else: false

bool b4_5:
    d4_5 == 2018/10/31: true
    else: false

bool b4_6:
    d4_6 == 2018/10/25: true
    else: false

bool b4_7:
    d4_7 == 2018/10/01: true
    else: false

bool b4_8:
    d4_8 == 2017/11/01: true
    else: false

date d5_0: this_month_last()
date d5_1: this_month_last(+1d)
date d5_2: this_month_last(+1w)
date d5_3: this_month_last(+1m)
date d5_4: this_month_last(+1y)
date d5_5: this_month_last(-1d)
date d5_6: this_month_last(-1w)
date d5_7: this_month_last(-1m)
date d5_8: this_month_last(-1y)

bool b5_0:
    d5_0 == 2018/11/30: true
    else: false

bool b5_1:
    d5_1 == 2018/12/01: true
    else: false

bool b5_2:
    d5_2 == 2018/12/07: true
    else: false

bool b5_3:
    d5_3 == 2018/12/30: true
    else: false

bool b5_4:
    d5_4 == 2019/11/30: true
    else: false

bool b5_5:
    d5_5 == 2018/11/29: true
    else: false

bool b5_6:
    d5_6 == 2018/11/23: true
    else: false

bool b5_7:
    d5_7 == 2018/10/30: true
    else: false

bool b5_8:
    d5_8 == 2017/11/30: true
    else: false

date d6_0: this_year_first()
date d6_1: this_year_first(+1d)
date d6_2: this_year_first(+1w)
date d6_3: this_year_first(+1m)
date d6_4: this_year_first(+1y)
date d6_5: this_year_first(-1d)
date d6_6: this_year_first(-1w)
date d6_7: this_year_first(-1m)
date d6_8: this_year_first(-1y)

bool b6_0:
    d6_0 == 2018/01/01: true
    else: false

bool b6_1:
    d6_1 == 2018/01/02: true
    else: false

bool b6_2:
    d6_2 == 2018/01/08: true
    else: false

bool b6_3:
    d6_3 == 2018/02/01: true
    else: false

bool b6_4:
    d6_4 == 2019/01/01: true
    else: false

bool b6_5:
    d6_5 == 2017/12/31: true
    else: false

bool b6_6:
    d6_6 == 2017/12/25: true
    else: false

bool b6_7:
    d6_7 == 2017/12/01: true
    else: false

bool b6_8:
    d6_8 == 2017/01/01: true
    else: false

date d7_0: this_year_last()
date d7_1: this_year_last(+1d)
date d7_2: this_year_last(+1w)
date d7_3: this_year_last(+1m)
date d7_4: this_year_last(+1y)
date d7_5: this_year_last(-1d)
date d7_6: this_year_last(-1w)
date d7_7: this_year_last(-1m)
date d7_8: this_year_last(-1y)

bool b7_0:
    d7_0 == 2018/12/31: true
    else: false

bool b7_1:
    d7_1 == 2019/01/01: true
    else: false

bool b7_2:
    d7_2 == 2019/01/07: true
    else: false

bool b7_3:
    d7_3 == 2019/01/31: true
    else: false

bool b7_4:
    d7_4 == 2019/12/31: true
    else: false

bool b7_5:
    d7_5 == 2018/12/30: true
    else: false

bool b7_6:
    d7_6 == 2018/12/24: true
    else: false

bool b7_7:
    d7_7 == 2018/11/30: true
    else: false

bool b7_8:
    d7_8 == 2017/12/31: true
    else: false

bool b8_0:
    today() < 2018/11/10 || today() <= 2018/11/10 || today() > 2019/11/10 || today() >= 2019/11/10 || today() == 2018/11/10 || today() != 2018/11/10: true
    else: false

bool b8_1:
    this_week_first() < 2018/11/10 || this_week_first() <= 2018/11/10 || this_week_first() > 2019/11/10 || this_week_first() >= 2019/11/10 || this_week_first() == 2018/11/10 || this_week_first() != 2018/11/10: true
    else: false

bool b8_2:
    this_week_last() < 2018/11/10 || this_week_last() <= 2018/11/10 || this_week_last() > 2019/11/10 || this_week_last() >= 2019/11/10 || this_week_last() == 2018/11/10 || this_week_last() != 2018/11/10: true
    else: false

bool b8_3:
    this_month_first() < 2018/11/10 || this_month_first() <= 2018/11/10 || this_month_first() > 2019/11/10 || this_month_first() >= 2019/11/10 || this_month_first() == 2018/11/10 || this_month_first() != 2018/11/10: true
    else: false

bool b8_4:
    this_month_last() < 2018/11/10 || this_month_last() <= 2018/11/10 || this_month_last() > 2019/11/10 || this_month_last() >= 2019/11/10 || this_month_last() == 2018/11/10 || this_month_last() != 2018/11/10: true
    else: false

bool b8_5:
    this_year_first() < 2018/11/10 || this_year_first() <= 2018/11/10 || this_year_first() > 2019/11/10 || this_year_first() >= 2019/11/10 || this_year_first() == 2018/11/10 || this_year_first() != 2018/11/10: true
    else: false

bool b8_6:
    this_year_last() < 2018/11/10 || this_year_last() <= 2018/11/10 || this_year_last() > 2019/11/10 || this_year_last() >= 2019/11/10 || this_year_last() == 2018/11/10 || this_year_last() != 2018/11/10: true
    else: false

bool b8_7:
    now() < 2018/11/10 00:00:00 || now() <= 2018/11/10 00:00:00 || now() > 2019/11/10 00:00:00 || now() >= 2019/11/10 00:00:00 || now() == 2018/11/10 00:00:00 || now() != 2018/11/10 00:00:00: true
    else: false


