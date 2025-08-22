package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias("SczlmxModel")
public class SczlmxModel extends BaseBasicModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String sczlmxid;//生产指令明细id
    private String sczlid;//生产指令id
    private String wlid;//物料id
    private String xqjhmxid;//需求计划明细id
    private String cpxqid;//产品需求id
    private String scsl;//生产数量
    private String xlh;//序列号
    private String zt;//状态
    private String yysl;//引用数量
    private String yjwcsj;//预计完成时间
    private String shsj;//审核时间
    private String jlbh;//记录编号

    public String getJlbh() {
        return jlbh;
    }

    public void setJlbh(String jlbh) {
        this.jlbh = jlbh;
    }

    public String getShsj() {
        return shsj;
    }

    public void setShsj(String shsj) {
        this.shsj = shsj;
    }

    public String getYjwcsj() {
        return yjwcsj;
    }

    public void setYjwcsj(String yjwcsj) {
        this.yjwcsj = yjwcsj;
    }

    public String getYysl() {
        return yysl;
    }

    public void setYysl(String yysl) {
        this.yysl = yysl;
    }

    public String getSczlmxid() {
        return sczlmxid;
    }

    public void setSczlmxid(String sczlmxid) {
        this.sczlmxid = sczlmxid;
    }

    public String getSczlid() {
        return sczlid;
    }

    public void setSczlid(String sczlid) {
        this.sczlid = sczlid;
    }

    public String getWlid() {
        return wlid;
    }

    public void setWlid(String wlid) {
        this.wlid = wlid;
    }

    public String getXqjhmxid() {
        return xqjhmxid;
    }

    public void setXqjhmxid(String xqjhmxid) {
        this.xqjhmxid = xqjhmxid;
    }

    public String getCpxqid() {
        return cpxqid;
    }

    public void setCpxqid(String cpxqid) {
        this.cpxqid = cpxqid;
    }

    public String getScsl() {
        return scsl;
    }

    public void setScsl(String scsl) {
        this.scsl = scsl;
    }

    public String getXlh() {
        return xlh;
    }

    public void setXlh(String xlh) {
        this.xlh = xlh;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }
}
