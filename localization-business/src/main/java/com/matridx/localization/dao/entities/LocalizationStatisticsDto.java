package com.matridx.localization.dao.entities;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

@Data
public class LocalizationStatisticsDto implements Serializable, Cloneable {
    private final Logger log = LoggerFactory.getLogger(LocalizationStatisticsDto.class);

    private String year;//年
    private String month;//月
    private String week;//日
    private String day;//天
    private String tjlx;//统计类型
    private boolean isInit;//是否初始化页面
    private Calendar calendar;//日历
    private String sqlKey; //统计sql的关键字
    private String ageRange; //年龄段
    private String microorganismName; //微生物中文名称
    private String allergyGeneName;//耐药基因名称
    private String sampleType;//样本类型名称
    private List<String> sjhbs;//送检伙伴多
    private List<String> jcdwdms;//检测单位代码
    private List<String> jcdws;//检测单位多
    @Override
    public LocalizationStatisticsDto clone() {
        try {
            return (LocalizationStatisticsDto) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("LocalizationStatisticsDto Clone error", e);
            return null;
        }
    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
