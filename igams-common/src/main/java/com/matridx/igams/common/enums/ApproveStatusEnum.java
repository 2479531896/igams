package com.matridx.igams.common.enums;
/**
 * 审批结果枚举类
 */
public enum ApproveStatusEnum {
    NEW("NEW","新创建"),
    RUNNING("RUNNING","审批中"),
    TERMINATED("TERMINATED","被终止"),
    COMPLETED("COMPLETED","完成"),
    CANCELED("CANCELED","取消");

    public final String name;
    public final String code;

    ApproveStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
