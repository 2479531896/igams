package com.matridx.igams.detection.molecule.enums;

public enum MxRequestEnum {
    MX_ReQUEST_INFO_000000("000000","成功"),
    MX_ReQUEST_INFO_010001("010001","报文内容格式解析错误"),
    MX_ReQUEST_INFO_010002("010002","报文版本与地址版本不一致"),
    MX_ReQUEST_INFO_010003("010003","接口地址已失效"),
    MX_ReQUEST_INFO_010004("010004","安全验证错误，报文签名验证失败"),
    MX_ReQUEST_INFO_010005("010005","安全验证错误，报文解密失败"),
    MX_ReQUEST_INFO_020999("020999","失败，请稍后再试！"),
    MX_ReQUEST_INFO_110001("110001","内部系统异常，暂不能提供服务"),
    MX_ReQUEST_INFO_110999("110999","系统错误，请稍后再试"),
    ;
    private final String code;
    private final String value;

    MxRequestEnum(String code,String value) {
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
        for (MxRequestEnum enumi : values()) {
            if(enumi.getCode().equals(code)){
                return enumi.getValue();
            }
        }
        return null;
    }
}
