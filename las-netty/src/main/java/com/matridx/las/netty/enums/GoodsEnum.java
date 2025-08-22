package com.matridx.las.netty.enums;

public enum GoodsEnum {

    TIP_CLAW("3","枪头夹爪"),
    SEQ_CLAW("1","SEQ夹爪"),
    EP_CLAW("2","EP夹爪"),
    EP_EIGHT_CLAW("4","EP八连管夹爪"),
    BOX_CLAW("5","卡盒夹爪"),
    EIGHT_CLAW("6","八连管夹爪"),
    EIGHT_LID_CLAW("7","八连冠盖夹爪"),
    BOX_Vehicle_CLAW("8","卡盒载具夹爪"),
    OPENDOOR_CLAW("9","开关门夹爪"),
    
    HWLQ("4","后物料区"),
    QWLQ("1","前物料区"),
    
    RIGHT_EP("1","右EP管"),
    LEFT_EP("2","左EP管"),
    RIGHT_TIP("3","右枪头"),
    LEFT_TIP("4","左枪头"),
    BOX_CIBS("5","卡盒"),
    
    MOVE_PIC("0","移动建库仪区分"),
    TAKE_PIC("1","开关门取放料建库仪区分"),
    
	TYAR_ONE("1","AGV平台托盘1的位置"),
	TYAR_TWO("2","AGV平台托盘2的位置"),
	AGVDESKTOP_GUNHEAD("5","枪头载具在机器人平台位置"),
	AGVEP_VEHICLE_ONE("3","EP管载具1"),
	
	MOVE_CUBICS("2","移动到建库仪工作区间"),
	PUTIN_CARDBOX_TOCUBICS("5","卡盒放入建库仪"),
	
	PIC_FRONTMATERIAL("1","前料区拍照"),
	
	FRONTMATERIAL_GUNHEAD("8","前料区枪头载具");
    private String code;
    private String value;

    private GoodsEnum(String code, String value) {
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
        for (GoodsEnum enumi : values()) {
            if(enumi.getCode().equals(code)){
                return enumi.getValue();
            }
        }
        return null;
    }
}
