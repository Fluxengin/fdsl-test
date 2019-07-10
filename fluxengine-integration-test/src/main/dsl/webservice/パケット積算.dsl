event Webserviceパケットイベント:
  使用量: number

persister Webserviceパケット積算データ:
  使用量: number
  persist("webservice"):
    lifetime: today()

number Webservice累積パケットデータ: Webserviceパケット積算データ.使用量 + Webserviceパケットイベント.使用量

persist Webserviceパケット積算データ:
  使用量: Webservice累積パケットデータ
  watch(Webserviceパケットイベント):
