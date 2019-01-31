# fluxengine-local-test
FluxengineのエンジンやDSLのテストをすることができます。

# Dependency
使用言語：java
ビルドツール：gradle & maven

# Version
Fluxengine 1.0.1

# SetUp

1. 当プロジェクトをチェックアウト
2. プロジェクトディレクトリにて次のコマンドを実行する
```
mvn install
```

# Usage
テストDSLの実行
  以下eclipseで動作させる前提での内容です

  1. /fluxengine-local-test/src/main/java/jp/co/fluxengine/apptest/DslTestExecutor.java の実行構成を開く (メニュー＞実行＞実行構成）

  2. 引数タブのVM引数にSetUpで取得したresourcesフォルダのlog4j2.xmlパスを設定する
```
    例）-Dlog4j.configurationFile="file:\\\C:\Users\xxx\git\fluxengine-local-test\conf\log4j2.xml"
```
  3. 環境タブの環境変数に以下を設定する
```
    <キー> / <値>
    CONF / C:\Users\xxx\git\fluxengine-local-test\conf\
```
  4. DslTestExecutor.java を実行する

  5. /fluxengine-local-test/out/test-result.json の中に、各ケースが"結果":"true"になることを確認する

# Creating tests
テストを作成する場合、src/main/dslとsrc/test/dsl配下を同じフォルダ構成、フォルダ名にして、以下の規則でフォルダ、ファイルを作成する
* main or test
	* dsl
		* 01_パーサ (ID_テスト観点大分類)
			* 01_スペースの使用 (ID_テスト観点中分類)
				* 01_関数の引数間.dsl (ID_テスト観点小分類.dsl)

各*.dslファイル内は、その観点に沿ったテストケースを任意の数記述数する。

テストケースの名前は、「ID_テストケースを端的に示す名前」とする。
```
01_スペースなし:
    2019/01/30 00:00:01:
        get n1:
        inspect:
            n1 == 123.5:
02_1つめの引数前にスペース:
    2019/01/30 00:00:01:
        get n2:
        inspect:
            n2 == 123.5:
```

# Authors
Fluxengine株式会社

https://www.fluxengine.co.jp/