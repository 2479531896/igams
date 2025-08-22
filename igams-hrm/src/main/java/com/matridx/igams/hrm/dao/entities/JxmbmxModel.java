package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("JxmbmxModel")
public class JxmbmxModel extends BaseModel {
    //绩效模板明细id
    private String jxmbmxid;
    //绩效模板id
    private String jxmbid;
    //考核指标
    private String khzb;
    //实施评分标准和考核细则说明
    private String sm;
    //分数
    private String fs;
    //序号
    private String xh;
    //指标等级
    private String zbdj;
    //指标上级id
    private String zbsjid;

    public String getJxmbmxid() {
        return jxmbmxid;
    }

    public void setJxmbmxid(String jxmbmxid) {
        this.jxmbmxid = jxmbmxid;
    }

    public String getJxmbid() {
        return jxmbid;
    }

    public void setJxmbid(String jxmbid) {
        this.jxmbid = jxmbid;
    }

    public String getKhzb() {
        return khzb;
    }

    public void setKhzb(String khzb) {
        this.khzb = khzb;
    }

    public String getSm() {
        return sm;
    }

    public void setSm(String sm) {
        this.sm = sm;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getZbdj() {
        return zbdj;
    }

    public void setZbdj(String zbdj) {
        this.zbdj = zbdj;
    }

    public String getZbsjid() {
        return zbsjid;
    }

    public void setZbsjid(String zbsjid) {
        this.zbsjid = zbsjid;
    }
    private static final long serialVersionUID = 1L;
}
