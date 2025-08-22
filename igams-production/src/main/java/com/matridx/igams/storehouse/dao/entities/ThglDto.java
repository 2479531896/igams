package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

/*
 * 退货管理
 *
 */
@Alias(value="ThglDto")
public class ThglDto extends ThglModel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4182501779049822850L;
	//经手人名称
    private String jsrmc;
    //销售类型名称
    private String xslxmc;
    //销售部门名称
    private String xsbmmc;
    //销售部门代码
    private String xsbmdm;
    //币种
    private String biz;
    //币种名称
    private String bizmc;
    //运送方式
    private String ysfs;
    // 运送方式名称
    private String ysfsmc;
    //业务员
    private String ywy;
    //业务员名称
    private String ywymc;
    //模糊查询全部
    private String entire;
    //区间查询单据开始日期
    private String djrqstart;
    //区间查询单据结束日期
    private String djrqend;
    //退货明细json
    private String thmx_json;
    //修改前退货明细json
    private String xgqthmx_json;
    //客户简称
    private String khjc;
    //修改前退货单号
    private String xgqthdh;
    //发货单号
    private String fhdh;
    //路径标记
    private String ljbj;
    private String xslxdm;
    private String khdm;
    private String khmc;
    private String suil;
    //U8用户code
    private String grouping;
    //
    private String zsxm;

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }

    public String getGrouping() {
        return grouping;
    }

    public void setGrouping(String grouping) {
        this.grouping = grouping;
    }
    public String getXslxdm() {
        return xslxdm;
    }

    public void setXslxdm(String xslxdm) {
        this.xslxdm = xslxdm;
    }

    public String getKhdm() {
        return khdm;
    }

    public void setKhdm(String khdm) {
        this.khdm = khdm;
    }

    public String getKhmc() {
        return khmc;
    }

    public void setKhmc(String khmc) {
        this.khmc = khmc;
    }

    public String getSuil() {
        return suil;
    }

    public void setSuil(String suil) {
        this.suil = suil;
    }

    public String getLjbj() {
        return ljbj;
    }

    public void setLjbj(String ljbj) {
        this.ljbj = ljbj;
    }

    public String getFhdh() {
        return fhdh;
    }

    public void setFhdh(String fhdh) {
        this.fhdh = fhdh;
    }

    public String getXgqthdh() {
        return xgqthdh;
    }

    public void setXgqthdh(String xgqthdh) {
        this.xgqthdh = xgqthdh;
    }
    public String getXgqthmx_json() {
        return xgqthmx_json;
    }

    public void setXgqthmx_json(String xgqthmx_json) {
        this.xgqthmx_json = xgqthmx_json;
    }

    public String getKhjc() {
        return khjc;
    }

    public void setKhjc(String khjc) {
        this.khjc = khjc;
    }

    public String getThmx_json() {
        return thmx_json;
    }

    public void setThmx_json(String thmx_json) {
        this.thmx_json = thmx_json;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public String getBizmc() {
        return bizmc;
    }

    public void setBizmc(String bizmc) {
        this.bizmc = bizmc;
    }

    public String getYsfs() {
        return ysfs;
    }

    public void setYsfs(String ysfs) {
        this.ysfs = ysfs;
    }

    public String getYsfsmc() {
        return ysfsmc;
    }

    public void setYsfsmc(String ysfsmc) {
        this.ysfsmc = ysfsmc;
    }

    public String getYwy() {
        return ywy;
    }

    public void setYwy(String ywy) {
        this.ywy = ywy;
    }

    public String getYwymc() {
        return ywymc;
    }

    public void setYwymc(String ywymc) {
        this.ywymc = ywymc;
    }

    public String getXsbmdm() {
        return xsbmdm;
    }

    public void setXsbmdm(String xsbmdm) {
        this.xsbmdm = xsbmdm;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getDjrqstart() {
        return djrqstart;
    }

    public void setDjrqstart(String djrqstart) {
        this.djrqstart = djrqstart;
    }

    public String getDjrqend() {
        return djrqend;
    }

    public void setDjrqend(String djrqend) {
        this.djrqend = djrqend;
    }

    public String getXsbmmc() {
        return xsbmmc;
    }

    public void setXsbmmc(String xsbmmc) {
        this.xsbmmc = xsbmmc;
    }

    public String getJsrmc() {
        return jsrmc;
    }

    public void setJsrmc(String jsrmc) {
        this.jsrmc = jsrmc;
    }

    public String getXslxmc() {
        return xslxmc;
    }

    public void setXslxmc(String xslxmc) {
        this.xslxmc = xslxmc;
    }
}
