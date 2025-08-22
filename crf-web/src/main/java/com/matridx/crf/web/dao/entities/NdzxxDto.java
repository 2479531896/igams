package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="NdzxxDto")
public class NdzxxDto extends NdzxxModel{

    private static final long serialVersionUID = 1L;
    //总数
    private String num;
    //附件ID复数
    private List<String> fjids;
    //文件类型
    private String ywlx;
    //附件ID复数(word)
    private List<String> w_fjids;
    //附件ID复数 去人源
    private List<String> fjids_q;
    //附件ID复数 去人源(word)
    private List<String> w_fjids_q;
    //附件ID复数
    private List<String> w_fjids_z;
    //文件类型(word)
    private String w_ywlx;
    //文件类型 去人源
    private String ywlx_q;
    //文件类型 去人源(word)
    private String w_ywlx_q;
    //自免报告类型
    private String w_ywlx_z;
    //转归id
    private String zgid;
    //检验项目
    private String jyxm;
    //数据值
    private String sjz;
    //导出关联标记位//所选择的字段
    private String SqlParam;
    private String [] kjywblss;
    private String [] cyzts;
    private String [] brlbs;
    private String [] xbs;
    private String [] jzkss;
    private String[] kjywjjtzls;
    //开始日期(出院时间)
    private String cysjstart;
    //结束日期(出院时间)
    private String cysjend;

    public String[] getKjywblss() {
        return kjywblss;
    }

    public void setKjywblss(String[] kjywblss) {
        this.kjywblss = kjywblss;
    }

    public String[] getCyzts() {
        return cyzts;
    }

    public void setCyzts(String[] cyzts) {
        this.cyzts = cyzts;
    }

    public String[] getBrlbs() {
        return brlbs;
    }

    public void setBrlbs(String[] brlbs) {
        this.brlbs = brlbs;
    }

    public String[] getXbs() {
        return xbs;
    }

    public void setXbs(String[] xbs) {
        this.xbs = xbs;
    }

    public String[] getJzkss() {
        return jzkss;
    }

    public void setJzkss(String[] jzkss) {
        this.jzkss = jzkss;
    }

    public String[] getKjywjjtzls() {
        return kjywjjtzls;
    }

    public void setKjywjjtzls(String[] kjywjjtzls) {
        this.kjywjjtzls = kjywjjtzls;
    }

    public String getCysjstart() {
        return cysjstart;
    }

    public void setCysjstart(String cysjstart) {
        this.cysjstart = cysjstart;
    }

    public String getCysjend() {
        return cysjend;
    }

    public void setCysjend(String cysjend) {
        this.cysjend = cysjend;
    }

    public String getSqlParam() {
        return SqlParam;
    }

    public void setSqlParam(String sqlParam) {
        SqlParam = sqlParam;
    }

    public String getJyxm() {
        return jyxm;
    }

    public void setJyxm(String jyxm) {
        this.jyxm = jyxm;
    }

    public String getSjz() {
        return sjz;
    }

    public void setSjz(String sjz) {
        this.sjz = sjz;
    }

    public String getZgid() {
        return zgid;
    }

    public void setZgid(String zgid) {
        this.zgid = zgid;
    }

    public String getW_ywlx() {
        return w_ywlx;
    }

    public void setW_ywlx(String w_ywlx) {
        this.w_ywlx = w_ywlx;
    }

    public String getYwlx_q() {
        return ywlx_q;
    }

    public void setYwlx_q(String ywlx_q) {
        this.ywlx_q = ywlx_q;
    }

    public String getW_ywlx_q() {
        return w_ywlx_q;
    }

    public void setW_ywlx_q(String w_ywlx_q) {
        this.w_ywlx_q = w_ywlx_q;
    }

    public String getW_ywlx_z() {
        return w_ywlx_z;
    }

    public void setW_ywlx_z(String w_ywlx_z) {
        this.w_ywlx_z = w_ywlx_z;
    }

    public List<String> getW_fjids() {
        return w_fjids;
    }

    public void setW_fjids(List<String> w_fjids) {
        this.w_fjids = w_fjids;
    }

    public List<String> getFjids_q() {
        return fjids_q;
    }

    public void setFjids_q(List<String> fjids_q) {
        this.fjids_q = fjids_q;
    }

    public List<String> getW_fjids_q() {
        return w_fjids_q;
    }

    public void setW_fjids_q(List<String> w_fjids_q) {
        this.w_fjids_q = w_fjids_q;
    }

    public List<String> getW_fjids_z() {
        return w_fjids_z;
    }

    public void setW_fjids_z(List<String> w_fjids_z) {
        this.w_fjids_z = w_fjids_z;
    }

    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }

    public List<String> getFjids() {
        return fjids;
    }

    public void setFjids(List<String> fjids) {
        this.fjids = fjids;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "NdzxxDto{}";
    }
}
