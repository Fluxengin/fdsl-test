# fluxengine-integration-test
Dataflowを使用して結合テストを行うためのプロジェクト。
主としてCIで使用する。

# Dependency
使用言語：java
ビルドツール：gradle

# Version
Fluxengine 1.0.11

# SetUp

1. 本プロジェクトをチェックアウトする

2. DSLを登録する
    1. fluxengine-integration-test/conf/dslDataStore.properties を適宜編集する

    2. テストしたいDSLのルートディレクトリ (例: fluxengine-integration-test/src/main/dsl) を、fluxengine-integration-test/dsl にコピーする

    3. 依存性をコピーしてプロジェクトをビルドする  
```./gradlew build copyDependencies -x test```

    4. ビルドしたjarと以下のjarを、fluxengine-integration-test/lib にコピーする
        - fluxengine-core-\*.jar
        - fluxengine-core-dsl-register-\*.jar
        - fluxengine-core-parser-\*.jar
        - fluxengine-core-persist-manager-\*.jar

    5. fluxengine-dsl-register/tools にある regisdsl.sh を fluxengine-integration-test にコピーする

    6. fluxengine-dsl-register/target/lib 配下のjarファイルをクラスパスに加え、regisdsl.sh を実行する  
    DSLのバージョンと適用開始日は、適切なものに変更する
```env CLASSPATH=~/state-engine/fluxengine-dsl-register/target/lib/* ./regisdsl.sh 1 20190918```

3. Dataflowにジョブをデプロイする
    1. fluxengine-integration-test/conf 配下の以下のファイルを、適宜編集する
        - dataflow_job_publisher_sample.env
        - dslDataStore.properties
        - effectorDataStore.properties
        - illegalEventDataStore.properties
        - persisterDataStore.properties
        - publisher.properties
        - cloud-sql.properties
        - fluxengine.properties

    2. 依存性をコピーしてプロジェクトをビルドする  
```./gradlew build copyDependencies -x test```

    3. ビルドしたjarと以下のjarを、fluxengine-integration-test/lib にコピーする 
        - fluxengine-dataflow-\*.jar

    4. fluxengine-dataflow/target/lib 配下のjarファイルをクラスパスに加え、regisdsl.sh を実行する  
        1. バッチジョブのデプロイ  
```env CLASSPATH=~/state-engine/fluxengine-dataflow/target/lib/* ./dataflow_job_publisher.sh batch conf/dataflow_job_publisher_sample.env debug```
        2. ストリーミングジョブのデプロイ  
```env CLASSPATH=~/state-engine/fluxengine-dataflow/target/lib/* ./dataflow_job_publisher.sh stream conf/dataflow_job_publisher_sample.env debug```

4. fluxengine-integration-test-batch-servletをデプロイする  
(詳細はfluxengine-integration-test-batch-servletのREADMEを参照)

5. fluxengine-integration-test/lib を空にし、以下のjarをコピーする
    - fluxengine-local-test-runner-\*.jar
    - fluxengine-remote-test-runner-\*.jar
    - fluxengine-event-publisher-\*.jar

6. テストに必要な環境変数をセットする  
(必要な環境変数は、テストによって異なる)

# Usage

テストを実行する  
```./gradlew test```

# Authors
Fluxengine株式会社

https://www.fluxengine.co.jp/
