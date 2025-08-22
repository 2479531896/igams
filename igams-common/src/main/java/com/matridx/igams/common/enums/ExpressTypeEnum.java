package com.matridx.igams.common.enums;

//快递业务枚举类
public enum ExpressTypeEnum {
    SJXX("SJXX"),//送检信息
    SJZZBG("SJZZBG"),//送检纸质报告
    ;

    private final String code;
    ExpressTypeEnum(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }
}
