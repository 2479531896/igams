package com.matridx.las.netty.enums;

/**
 * 一些公用的枚举类
 *
 * @time 20211203
 */
public enum GlobalParmEnum {

    //前端页面注册类型
    HTML_TYPE_ALL("instrumentdetails", "实验室首页"),//实验室首页
    HTML_TYPE_INPUT("htmlinpt", "样本录入界面"),//样本录入界面
    HTML_TYPE_CHN("channel", "通道号设置界面"),//通道号设置界面

    CUBIS_CHANNEL_NUM("4", "一个建库仪通道数量"),//
    GUN_TOTAL_NUM("25", "枪头初始化总数"),
    EP_TOTAL_NUM("40", "EP初始化总数"),
    OC_TOTAL_NUM("6", "八连管初始化总数"),
    OC_ONE_NUM("8", "一个八连管孔数"),
    COVER_TOTAL_NUM("10", "初始化加盖机盖的数量"),
    AUTO_START_NUM("1", "满多少个开始配置仪"),
	AGV_EP_WZ1("3", "机器人1号EP管位置"),
	AGV_EP_WZ2("4", "机器人2号EP管位置"),
    AGV_QT_WZ("1", "机器人2号枪头管位置"),
    AGV_BLG_WZ("5", "机器人八连管位置"),
    AGV_TRAY_WZ1("1", "机器人1号托盘位置"),
    AGV_TRAY_WZ2("2", "机器人2号托盘位置"),

    YQ_PROPERTY_TIME("time", "仪器属性，开始时间"),
    YQ_PROPERTY_COUNT("count", "仪器属性，数量"),
    YQ_PROPERTY_CHANNEL("channel", "仪器属性，通道数"),


    ;
    private String code;
    private String value;

    private GlobalParmEnum(String code, String value) {
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