# fluxengine-integration-test-batch-servlet
Dataflowを使用して結合テストを行う際に、バッチジョブの起動やテストデータの投入を行うためのプロジェクト。
主としてCIで使用し、fluxengine-integration-testと組み合わせて使われるが、ここでは単体の使用方法を説明する。

# Dependency
使用言語：java
ビルドツール：maven

# Version
Fluxengine 1.0.11

# SetUp

1. 本プロジェクトをチェックアウトする

2. fluxengine-integration-test-batch-servlet/src/main/webapp/WEB-INF/job.properties を適宜編集する

3. mavenでプロジェクトをデプロイする  
```mvn clean package appengine:deploy```

# Usage

- job.propertiesで定まっているイベントを送ってバッチジョブを起動する場合  
`https://(サーブレットのデプロイ先のホスト)/fluxengine-dataflow-batch?event=(イベントの名前空間)#(イベント名)` にGETリクエストを送る

- 任意のイベントを送ってバッチジョブを起動する場合  
`https://(サーブレットのデプロイ先のホスト)/fluxengine-integration-test-event` にPOSTリクエストを送る  
送信内容は、以下のような、JSONの配列

```
[
    {
        "eventName": "(イベント名)",
        "namespace": "(イベントの名前空間)",
        "createTime": "(作成日時)",
        "property": {
            (イベント独自のデータ...)
        }
    },
    ...
]
```

# Authors
Fluxengine株式会社

https://www.fluxengine.co.jp/
