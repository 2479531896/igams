package com.matridx.las.netty.enums;

public enum AreaEnum {
    //前端页面注册类型
    FMA_AREA("1", "前物料区"),//前物料区
    AUTO_DESK_AREA("3", "后物料区");//后物料区


    private String code;
    private String value;

    private AreaEnum(String code, String value) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public static String getValueByCode(String code) {
        for (AreaEnum enumi : values()) {
            if (enumi.getCode().equals(code)) {
                return enumi.getValue();
            }
        }
        return null;
    }
}
