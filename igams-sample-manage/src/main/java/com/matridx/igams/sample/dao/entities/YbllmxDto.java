package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;

/*
 *@date 2022年06月13日18:03
 *
 */
@Alias(value="YbllmxDto")
public class YbllmxDto extends YbllmxModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -9180482312490400942L;
	//位置
    private String wz;
    // 体积
    private String tj;
    //样本编号
    private String ybbh;
    //冰箱号
    private String bxh;
    // 抽屉号
    private String cth;
    // 盒子号
    private String hh;
    //样本类型名称
    private String yblxmc;
    //内部编码
    private String nbbm;
    //存储单位
    private String jcdwmc;
    //冰箱位置
    private String bxwz;
    //盒子位置
    private String hzwz;
    //抽屉位置
    private String ctwz;
    //盒子编号
    private String hzbh;

    public String getHzbh() {
        return hzbh;
    }

    public void setHzbh(String hzbh) {
        this.hzbh = hzbh;
    }

    public String getCtwz() {
        return ctwz;
    }

    public void setCtwz(String ctwz) {
        this.ctwz = ctwz;
    }

    public String getBxwz() {
        return bxwz;
    }

    public void setBxwz(String bxwz) {
        this.bxwz = bxwz;
    }

    public String getHzwz() {
        return hzwz;
    }

    public void setHzwz(String hzwz) {
        this.hzwz = hzwz;
    }

    public String getJcdwmc() {
        return jcdwmc;
    }

    public void setJcdwmc(String jcdwmc) {
        this.jcdwmc = jcdwmc;
    }

    public String getNbbm() {
        return nbbm;
    }

    public void setNbbm(String nbbm) {
        this.nbbm = nbbm;
    }

    public String getTj() {
        return tj;
    }

    public void setTj(String tj) {
        this.tj = tj;
    }

    public String getYbbh() {
        return ybbh;
    }

    public void setYbbh(String ybbh) {
        this.ybbh = ybbh;
    }

    public String getBxh() {
        return bxh;
    }

    public void setBxh(String bxh) {
        this.bxh = bxh;
    }

    public String getCth() {
        return cth;
    }

    public void setCth(String cth) {
        this.cth = cth;
    }

    public String getHh() {
        return hh;
    }

    public void setHh(String hh) {
        this.hh = hh;
    }

    public String getYblxmc() {
        return yblxmc;
    }

    public void setYblxmc(String yblxmc) {
        this.yblxmc = yblxmc;
    }

    public String getWz() {
        return wz;
    }

    public void setWz(String wz) {
        this.wz = wz;
    }
}
