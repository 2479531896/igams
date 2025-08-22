package com.matridx.igams.common.enums;

/**
 * 打印类型/模板枚举类
 */
public enum PrintEnum {
    PRINT_XGLR("PRINT_XGLR"), // 新冠录入类型
    PRINT_XGQY("PRINT_XGQY"), //新冠取样确认类型
    PRINT_BARCODE("PRINT_BARCODE"), //条形码打印
    ;

    private final String code;

    PrintEnum(String code) {
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
