test 1 複雑な属性も扱える:
    2019/03/26 00:00:01:
        inspect:
            HogeStrc.name == "hoge":
            HogeStrc.kind == "variant":
            HogeStrc.optionalInfo.records.join(",") == "record1,record2":
            HogeStrc.optionalInfo.keyValues.join(",") == "{value=10.01, key=key1},{value=-100, key=key2}":