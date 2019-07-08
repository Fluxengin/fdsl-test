import getByString<"test">: variant/TestArgumentsVariant
import getByDate<2018/11/10>: variant/TestArgumentsVariant
import getByDateTime<2018/11/10 00:00:00>: variant/TestArgumentsVariant
import getByBooleanPrimitive<false>: variant/TestArgumentsVariant
import getByBoolean<true>: variant/TestArgumentsVariant
import getByInt<1>: variant/TestArgumentsVariant
import getByLongPrimitive<1>: variant/TestArgumentsVariant
import getByFloatPrimitive<1.1>: variant/TestArgumentsVariant
import getByDoublePrimitive<1.1>: variant/TestArgumentsVariant
import getByInteger<1>: variant/TestArgumentsVariant
import getByLong<1>: variant/TestArgumentsVariant
import getByFloat<1.1>: variant/TestArgumentsVariant
import getByDouble<1.1>: variant/TestArgumentsVariant
import getByBigInteger<1>: variant/TestArgumentsVariant
import getByBigDecimal<1.1>: variant/TestArgumentsVariant

list testList: ["a", "b", "c"]
map testMap: {a1: 1, a1: "a"}
import getByList<testList>: variant/TestArgumentsVariant
import getByBigListWithoutTemplate<testList>: variant/TestArgumentsVariant
import getByMap<testMap>: variant/TestArgumentsVariant
import getByMapWithutTemplate<testMap>: variant/TestArgumentsVariant

list testMapList: [ {a1: "x", a2: 1}, {a1: "y", a2: 2} ]

string str:  "a={0}, b={1}"
number test: 2
map testDeepMap:
   a1: "a"
   a2: 1.2
   a3: 2.0
   a4: "a={0}"
   a5: today()
   a6: now()
   a7: str.format("test1", "test2")
   a8: test * test
   a9: [1, 2, 3]
   a10: {inner1: "test"}

import getByMapList<testMapList>: variant/TestArgumentsVariant
import getByDeepMap<testDeepMap>: variant/TestArgumentsVariant

bool b1:
    getByString == "test": true
    else: false

bool b2:
    getByDate == 2018/11/10: true
    else: false

bool b3:
    getByDateTime == 2018/11/10 00:00:00 : true
    else: false

bool b4:
    getByBooleanPrimitive == false: true
    else: false

bool b5:
    getByBoolean == true: true
    else: false

bool b6:
    getByInt == 1: true
    else: false

bool b7:
    getByLongPrimitive == 1: true
    else: false

bool b8:
    getByFloatPrimitive == 1.1: true
    else: false

bool b9:
    getByDoublePrimitive == 1.1: true
    else: false

bool b10:
    getByInteger == 1: true
    else: false

bool b11:
    getByLong == 1: true
    else: false

bool b12:
    getByFloat == 1.1: true
    else: false

bool b13:
    getByDouble == 1.1: true
    else: false

bool b14:
    getByBigInteger == 1: true
    else: false

bool b15:
    getByBigDecimal == 1.1: true
    else: false

bool b16:
    getByList == testList: true
    else: false

bool b17:
    getByBigListWithoutTemplate == testList: true
    else: false

bool b18:
    getByMap == testMap: true
    else: false

bool b19:
    getByMapWithutTemplate == testMap: true
    else: false

bool b20:
    getByMapList == testMapList: true
    else: false

bool b21:
    getByDeepMap == testDeepMap: true
    else: false



bool b22:
    TestArgumentsUtility.getByString("test") == "test": true
    else: false

bool b23:
    TestArgumentsUtility.getByDate(2018/11/10) == 2018/11/10: true
    else: false

bool b24:
    TestArgumentsUtility.getByDateTime(2018/11/10 00:00:00) == 2018/11/10 00:00:00 : true
    else: false

bool b25:
    TestArgumentsUtility.getByBooleanPrimitive(false) == false: true
    else: false

bool b26:
    TestArgumentsUtility.getByBoolean(true) == true: true
    else: false

bool b27:
    TestArgumentsUtility.getByInt(1) == 1: true
    else: false

bool b28:
    TestArgumentsUtility.getByLongPrimitive(1) == 1: true
    else: false

bool b29:
    TestArgumentsUtility.getByFloatPrimitive(1.1) == 1.1: true
    else: false

bool b30:
    TestArgumentsUtility.getByDoublePrimitive(1.1) == 1.1: true
    else: false

bool b31:
    TestArgumentsUtility.getByInteger(1) == 1: true
    else: false

bool b32:
    TestArgumentsUtility.getByLong(1) == 1: true
    else: false

bool b33:
    TestArgumentsUtility.getByFloat(1.1) == 1.1: true
    else: false

bool b34:
    TestArgumentsUtility.getByDouble(1.1) == 1.1: true
    else: false

bool b35:
    TestArgumentsUtility.getByBigInteger(1) == 1: true
    else: false

bool b36:
    TestArgumentsUtility.getByBigDecimal(1.1) == 1.1: true
    else: false

bool b37:
    TestArgumentsUtility.getByList(testList) == testList: true
    else: false

bool b38:
    TestArgumentsUtility.getByBigListWithoutTemplate(testList) == testList: true
    else: false

bool b39:
    TestArgumentsUtility.getByMap(testMap) == testMap: true
    else: false

bool b40:
    TestArgumentsUtility.getByMapWithutTemplate(testMap) == testMap: true
    else: false

bool b41:
    TestArgumentsUtility.getByMapList(testMapList) == testMapList: true
    else: false

bool b42:
    TestArgumentsUtility.getByDeepMap(testDeepMap) == testDeepMap: true
    else: false

