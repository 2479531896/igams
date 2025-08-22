package com.matridx.igams.wechat.dao.entities;


import org.apache.ibatis.type.Alias;

@Alias(value="WbsjxxModel")
public class WbsjxxModel {
    private static final long serialVersionUID = 1L;

    //id
    private String id;
    //实际编码
    private String sjbm;
    //患者姓名
    private String hzxm;
    //外部编码（截取编码）
    private String wbbm;
    //送检id
    private String sjid;
    //检测项目id
    private String jcxmid;
    //住院号
    private String zyh;
    //样本类型
    private String yblx;
    //采样日期
    private String cyrq;
    //外部其他信息
    private String wbqtxx;
    //  信息json
    private String infojson;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSjbm() {
        return sjbm;
    }

    public void setSjbm(String sjbm) {
        this.sjbm = sjbm;
    }

    public String getHzxm() {
        return hzxm;
    }

    public void setHzxm(String hzxm) {
        this.hzxm = hzxm;
    }

    public String getWbbm() {
        return wbbm;
    }

    public void setWbbm(String wbbm) {
        this.wbbm = wbbm;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getJcxmid() {
        return jcxmid;
    }

    public void setJcxmid(String jcxmid) {
        this.jcxmid = jcxmid;
    }

    public String getZyh() {
        return zyh;
    }

    public void setZyh(String zyh) {
        this.zyh = zyh;
    }

    public String getYblx() {
        return yblx;
    }

    public void setYblx(String yblx) {
        this.yblx = yblx;
    }

    public String getCyrq() {
        return cyrq;
    }

    public void setCyrq(String cyrq) {
        this.cyrq = cyrq;
    }

    public String getWbqtxx() {
        return wbqtxx;
    }

    public void setWbqtxx(String wbqtxx) {
        this.wbqtxx = wbqtxx;
    }

    public String getInfojson() {
        return infojson;
    }

    public void setInfojson(String infojson) {
        this.infojson = infojson;
    }
}
