package com.matridx.igams.common.enums;

/**
 * 打印类型/模板枚举类
 */
public enum QualityComplaintsEnum {
    COMPLAINTS_INSPECTION("COMPLAINTS_INSPECTION","送检质量投诉"), // 送检质量投诉类型
    COMPLAINTS_EXCEPTION("COMPLAINTS_EXCEPTION","异常质量投诉"); //异常质量投诉类型

    private final String code;
    private final String value;

    QualityComplaintsEnum(String code, String value) {
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
        for (QualityComplaintsEnum enumi : values()) {
            if(enumi.getCode().equals(code)){
                return enumi.getValue();
            }
        }
        return null;
    }
}
