package com.matridx.igams.common.enums;

public enum TwrTypeEnum {
    WECHAT("WECHAT","微信"),//未审核
    //项目状态专用
    WEB("WEB","OA"),//提交审核

    ;

    private final String code;
    private final String value;

    TwrTypeEnum(String code, String value) {
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
        for (TwrTypeEnum enumi : values()) {
            if(enumi.getCode().equals(code)){
                return enumi.getValue();
            }
        }
        return null;
    }
}
