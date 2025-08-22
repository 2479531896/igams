package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias("XqjhmxModel")
public class XqjhmxModel extends BaseBasicModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String xqjhmxid;//需求计划明细id
    private String xqjhid;//需求计划id
    private String wlid;//物料id
    private String sl;//数量
    private String scsl;//生产数量
    private String yq;//批次和批量要求
    private String sfsc;//是否生产
    public String getSfsc() {
        return sfsc;
    }

    public void setSfsc(String sfsc) {
        this.sfsc = sfsc;
    }

    public String getXqjhmxid() {
        return xqjhmxid;
    }

    public void setXqjhmxid(String xqjhmxid) {
        this.xqjhmxid = xqjhmxid;
    }

    public String getXqjhid() {
        return xqjhid;
    }

    public void setXqjhid(String xqjhid) {
        this.xqjhid = xqjhid;
    }

    public String getWlid() {
        return wlid;
    }

    public void setWlid(String wlid) {
        this.wlid = wlid;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getScsl() {
        return scsl;
    }

    public void setScsl(String scsl) {
        this.scsl = scsl;
    }

    public String getYq() {
        return yq;
    }

    public void setYq(String yq) {
        this.yq = yq;
    }
}
