package com.matridx.igams.common.enums;

/**
 * 一些公用的枚举类
 *
 * @time 20211203
 */
public enum GlobalParmEnum {

    //前端页面注册类型
    SAMPLE_COLLECTION("SAMPLE_COLLECTION", "收样上传"),//OA收样上传
    SPECIMEN_INFO("SPECIMEN_INFO", "标本"),//标本
    SPECIMEN_EXCEPTION("SPECIMEN_EXCEPTION", "标本异常上传"),//标本异常上传




    ;
    private final String code;
    private final String value;

    GlobalParmEnum(String code, String value) {
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
        for (GlobalParmEnum enumi : values()) {
            if (enumi.getCode().equals(code)) {
                return enumi.getValue();
            }
        }
        return null;
    }

}