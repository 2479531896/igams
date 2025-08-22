package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * 伙伴合作客户(IgamsHbhzkh)实体类
 *
 * @author makejava
 * @since 2023-04-13 09:59:38
 */
@Alias(value="HbhzkhDto")
public class HbhzkhDto extends HbhzkhModel {
    private static final long serialVersionUID = 1L;

    private String gsmc;//公司名称
    private String gwmc;//岗位名称
    private String hbmc;//伙伴名称
    private String fl;//伙伴分类
    private String yhid;//用户ID

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getHbmc() {
        return hbmc;
    }

    public void setHbmc(String hbmc) {
        this.hbmc = hbmc;
    }

    public String getGwmc() {
        return gwmc;
    }

    public void setGwmc(String gwmc) {
        this.gwmc = gwmc;
    }

    public String getGsmc() {
        return gsmc;
    }

    public void setGsmc(String gsmc) {
        this.gsmc = gsmc;
    }
}

