import ユーザー情報<日別パケットイベント.端末ID>: variant/ユーザー情報

event 日別パケットイベント:
    端末ID: string
    使用量: number
    日時: datetime

read 日別データ:
    端末ID: string
    日時: string
    使用量: number
    watch(日別パケットイベント):
    get(日別パケットイベント.端末ID):

persister 日別積算データ:
    使用量: number
    persist(ユーザー情報.ユーザーID):
        lifetime: today()

number 日別累積パケットデータ: 日別積算データ.使用量 + 日別データ.使用量

persist 日別積算データ:
    使用量: 日別累積パケットデータ
    watch(日別データ):