# fluxengine-integration-housekeep
Dataflowを使用して不要データの削除を行うためのプロジェクト。
主としてCIで使用し、fluxengine-integration-testと組み合わせて不要データの削除が正しく機能するかを確認するために使用されるが、ここでは単体の使用方法を説明する。

# Dependency
使用言語：java
ビルドツール：maven

# Version
Fluxengine 1.1.0

# SetUp

1. 本プロジェクトをチェックアウトする

2. 以下のファイルを適宜編集する
    - fluxengine-integration-test-housekeep/fluxengine_housekeep.env
    - fluxengine-integration-test-housekeep/src/main/resources/persisterDataStore.properties
    - fluxengine-integration-test-housekeep/src/main/webapp/WEB-INF/housekeepJob.properties

3. fluxengine-integration-test-housekeep/src/main/resources 配下のファイルを fluxengine-integration-test-housekeep/conf にコピーする

4. fluxengine_housekeep.sh を実行し、バッチジョブをデプロイする (引数のfluxengine_housekeep.envは絶対パスで記述すること)  
```./fluxengine_housekeep.sh $(pwd)/fluxengine_housekeep.env```

5. mavenでサーブレットをデプロイする  
```mvn clean package appengine:deploy```

# Usage

`https://(サーブレットのデプロイ先のホスト)/fluxengine-dataflow-housekeep` にGETリクエストを送ることで、Housekeepのジョブが実行される

# Authors
Fluxengine株式会社

https://www.fluxengine.co.jp/
