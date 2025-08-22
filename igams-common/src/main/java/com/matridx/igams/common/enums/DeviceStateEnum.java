package com.matridx.igams.common.enums;

/**
 * @author WYX
 * @version 1.0
 * @className DeviceStateEnum
 * @description TODO
 * @date 16:18 2023/6/8
 **/
public enum DeviceStateEnum {
    LEAVE("闲置", "00"),
    USEING("使用", "01"),
    SCRAPPED("已报废", "02"),
    SALE("售出","03"),
    INSTOCK("在库", "04"),
    HANDING("移交中", "05"),
    METERING("计量中", "06"),
    VERIFICATION("验证中", "07"),
    UPKEEP("维修/保养中", "08"),
    SCRAP("报废中", "09"),
    PUT("投放", "10"),
    OUTSTOCK("退库", "11");

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String name;
    private String code;
    DeviceStateEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
