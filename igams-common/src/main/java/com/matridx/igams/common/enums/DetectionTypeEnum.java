package com.matridx.igams.common.enums;

/**
 * 实验类型枚举类
 * @author xinyf
 * @date 2023-2-17
 */
public enum DetectionTypeEnum {
    DETECT_SJ("DETECT_SJ"),//送检
    DETECT_FJ("DETECT_FJ"), //复检
    OTHER("OTHER")//其他，对账单上传专用
    ;

    private final String code;

    DetectionTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
