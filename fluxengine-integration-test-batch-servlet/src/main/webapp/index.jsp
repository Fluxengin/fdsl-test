<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<link href='//fonts.googleapis.com/css?family=Marmelad' rel='stylesheet'
	type='text/css'>
<title>Hello Fluxengine Integration Test Batch Servlet</title>
</head>
<body>
	<h1>Hello Fluxengine Integration Test Batch Servlet -- GAE!</h1>

	<p>Hello Fluxengine Integration Test Batch Servletのページです</p>

	<h2>Usage:</h2>
	<ol>
		<li><p>DSLをDataStoreに登録してください。</p></li>
		<li><p>
				次のURLにGETを実行してください：<br />
				/fluxengine-dataflow-batch?event=<イベントのネームスペース#イベント名>
			</p></li>
		<li><p>エラーが返ってこなければ成功です。</p></li>
	</ol>
</body>
</html>
