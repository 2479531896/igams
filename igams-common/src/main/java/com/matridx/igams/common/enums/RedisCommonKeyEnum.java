package com.matridx.igams.common.enums;

/**
 * @author WYX
 * @version 1.0
 * @className RedisCommonKeyEnum
 * @description Redis键
 * @date 14:07 2023/9/11
 **/
public enum RedisCommonKeyEnum {
    //假期管理员
    VACATION_ADMIN("VACATION_ADMIN",-1L),
    //首页
    HOME_PAGE("HOME_PAGE:",-1L),
    //个人显示页面
    HOME_PAGE_PERSION_PAGE("HOME_PAGE:personPage:",-1L),
    //所有组件
    HOME_PAGE_ALL_COMPONENT("HOME_PAGE:allComponent",-1L),
    //个人审核代办类别
    HOME_PAGE_AUDIT_TASK_WAITING("HOME_PAGE:personAuditTaskWaiting:",-1L),
    //用户培训信息 过期时间三天
    TRAIN_USERPXINFO("TRAIN:USERPXINFO:",3*24*60*60L),
    //培训对应工作ID 过期时间三天
    TRAIN_TRAINGZID("TRAIN:TRAINGZID:",3*24*60*60L),
    //培训信息 过期时间三天
    TRAIN_TRAINPXINFO("TRAIN:TRAINPXINFO:",3*24*60*60L),
    //红包明细 过期时间三天
    TRAIN_HBSZMX("TRAIN:HBSZMX:",3*24*60*60L),
    //个人考试信息 过期时间三小时
    TRAIN_GRKSGL("TRAIN:GRKSGL:",3*60*60L),
    //个人考试明细 过期时间三小时
    TRAIN_GRKSMX("TRAIN:GRKSMX:",3*60*60L),
    //红包排名 过期时间三十天
    TRAIN_ALLHBINFOPM("TRAIN:ALLHBINFOPM",3*24*60*60L),
    //培训题库题目类型的数量 过期时间三小时
    TRAIN_PXTKTMLX("TRAIN:PXTKTMLX:",3*60*60L),
    //考试明细信息 过期时间三小时
    TRAIN_KSMX("TRAIN:KSMX:",3*60*60L),
    //考试管理信息 过期时间三小时
    TRAIN_KSGL("TRAIN:KSGL:",3*60*60L),
    //奖品管理
    AWARD_JPGL("AWARD:JPGL:",-1),
    //奖品明细
    AWARD_JPMX("AWARD:JPMX:",-1),
    //奖品记录
    AWARD_RECORES("AWARD:RECORES:",-1),
    AWARD_RECORE_LIST("AWARD:RECORES_LIST:",-1),
    //文件流Redis(Base64)
    REDIS_FILE("REDIS_FILE:",-1),
    //送检操作锁85to86
    WECHAT_INSPECTION_LOCK("WECHAT_INSPECTION_LOCK:",60),
    //送检操作锁86to85
    INSPECTION_WECHAT_LOCK("INSPECTION_WECHAT_LOCK:",60),
    //送检操作队列消息 list（86to85） //过期3小时
    INSPECTION_WECHAT_LIST("INSPECTION_WECHAT_LIST:",24*60*60L),
    //送检操作队列消息 list（85to86） //过期3小时
    WECHAT_INSPECTION_LIST("WECHAT_INSPECTION_LIST:",24*60*60L),
    ;
    private String key;
    //过期时间
    private long expire;

    RedisCommonKeyEnum(String key, long expire) {
        this.key = key;
        this.expire = expire;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
