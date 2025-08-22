package com.matridx.igams.common.enums;

public enum CommonCreditEnum {

    SW_YSZK_FK("SW_YSZK_FK","商务应收账款付款"),
    SW_YSZK_KP("SW_YSZK_KP","商务应收账款开票");

    private final String code;
    private final String value;

    CommonCreditEnum(String code, String value) {
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
        for (CommonCreditEnum enumi : values()) {
            if(enumi.getCode().equals(code)){
                return enumi.getValue();
            }
        }
        return null;
    }
}
