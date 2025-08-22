package com.matridx.las.frame.connect.enums;
/*
    设备状态
 */
public enum ChannelStatusEnum {

    FREE ( "01","空闲"),
    BUSY ( "02","繁忙"),
    ERROR ( "99","错误"),

    ;
    private String code;
    private String value;

    private ChannelStatusEnum(String code, String value) {
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
        for (ChannelStatusEnum enumi : values()) {
            if(enumi.getCode().equals(code)){
                return enumi.getValue();
            }
        }
        return null;
    }
}
