# fluxengine-local-test
FluxengineのエンジンやDSLのテストをすることができます。

# Dependency
使用言語：java
ビルドツール：gradle

# Version
Fluxengine 1.0.9

# SetUp

1. 当プロジェクトをチェックアウト
2. プロジェクトディレクトリにて次のコマンドを実行し、ビルドを行う
```
gradlew build -x test
```

# Usage
テストDSLの実行

1. IDEからテストを実行する場合は、環境変数"CONF"に、confフォルダの絶対パスを設定する

```
    <キー> / <値>
    CONF / C:\Users\xxx\git\fluxengine-local-test\conf\
```

2. IDEからテストを実行する場合は、テストの実行構成のVM引数にlog4j2.xmlのパスを指定する

```
    例）-Dlog4j.configurationFile="file:\\\C:\Users\xxx\git\fluxengine-local-test\conf\log4j2.xml"
```

3. src/test/java以下のテストクラスに対して、テストを実行する
コマンドラインからテストを行うには、 `gradlew test` を実行する

4. 各テストケースの結果を確認する

# Creating tests
テストを作成する場合、src/main/dsl と src/test/dsl 配下を同じフォルダ構成、フォルダ名にして、以下の規則でフォルダ、ファイルを作成する
* main or test
	* dsl
		* 01_パーサ (ID_テスト観点大分類)
			* 01_スペースの使用 (ID_テスト観点中分類)
				* 関数の引数間 (ID_テスト観点小分類)
				  * 関数の引数間.dsl (ID_テスト観点小分類.dsl)

フォルダ階層の数は任意とする。

テストケースの名前は、「test テストID(連番) テストケースを端的に示す名前」とする。

```
test 1 スペースなし:
    2019/01/30 00:00:01:
        get n1:
        inspect:
            n1 == 123.5:

test 2 1つめの引数前にスペース:
    2019/01/30 00:00:01:
        get n2:
        inspect:
            n2 == 123.5:
```

# Authors
Fluxengine株式会社

https://www.fluxengine.co.jp/