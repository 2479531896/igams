package com.matridx.las.netty.enums;
/**
 * Redis存储的名称类型
 * @time 20211204
 */
public enum RedisStorageEnum {

	CUBICS_GROUP_FREE_QUEUE ( "CubisGroup","建库仪空闲队列"),
	AUTO_GROUP_FREE_QUEUE ( "AutoGroup","配置仪空闲队列"),
	PCR_GROUP_FREE_QUEUE ( "PcrGroup","荧光定量空闲队列"),
	CHM_GROUP_FREE_QUEUE ( "CmhGroup","加盖机空闲队列"),
	SEQ_GROUP_FREE_QUEUE ( "SeqGroup","测序仪空闲队列"),
	AGV_GROUP_FREE_QUEUE ( "lassGroup","机器人空闲队列"),
	INSTRUMENTUSEDLIST_FREE_QUEUE ( "InstrumentUsedList","正在使用的仪器"),
	HAVAUPCUBIS( "havaUpCubis","已上报的建库仪通道"),
	FRONT_MATERIAL_AREA( "fma","前物料区"),
	CUBICS ( "cubics","建库仪"),
	CHM ( "chm","加盖机"),
	AUTO_DESK ( "auto_desk","后物料区"),
	AUTOIDWKID ( "autoIdWkid","建库仪文库管理id"),
	AUTO ( "auto","配置仪"),
	AGV ( "agv","机器人"),
	PCR ( "pcr","荧光定量仪"),
	SEQ ( "seq","测序仪"),
	ISAUTOWORK ( "IsAutoWork","正在使用的仪器"),
	ISAUTOWORK1 ( "IsAutoWork1","正在使用的仪器"),
	ISSTARTWORK ( "IsStartWork","正在使用的仪器"),
	ISWORKLC ( "IsWorklc","正在使用的仪器"),

	EP_MAP_WZ ( "wzEpMap","物料台上的EP载具"),
	EP_MAP_DESK ( "deskEpMap","机器人上的EP载具"),
	EP_AGV_DESK1 ( "ep1","机器人上的EP载具名称"),
	EP_AGV_DESK2 ( "ep2","机器人上的EP载具名称"),
	EP_NAME_EMPTY("ep1","空ep管的名称"),
	EP_NAME_HS("ep2","核酸EP管的代码"),
	EP_NAME_WK("ep3","文库EP管的代码"),

	;
		private String code;
		private String value;

		private RedisStorageEnum(String code, String value) {
			this.value = value;
			this.code = code;
		}

		public String getValue() {
			return value;
		}

		public String getCode(){
			return code;
		}

		public static String getValueByCode(String code){
			for (RedisStorageEnum enumi : values()) {
				if(enumi.getCode().equals(code)){
					return enumi.getValue();
				}
			}
			return null;
		}

	}