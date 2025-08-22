package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

/*
 *@date 2022年06月13日18:03
 *
 */
@Alias(value="YbllglDto")
public class YbllglDto extends YbllglModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4917793021589936333L;
	//领料人名称
    private String llrmc;
    //部门名称
    private String bmmc;

    //样本库存id
    private String ybkcid;

    private String ybllmx_json;

    //模糊查询全部
    private String entire;

    //发料人名称
    private String flrmc;
    private String ybbh;//样本编码
    private String yblx;//样本类型
    private String yblxmc;//样本类型名称
    private String[] yblxs;//样本类型名称多
    private String sqrqstart;//申请开始时间
    private String sqrqend;//申请结束时间
    private String shsj;//审核时间
    private String tj;
    private String bxh;
    private String cth;
    private String hh;
    private String wz;
    //删除后剩下的
    private String delllmxids;
    //增加的
    private String addmxkcids;

    private String xgqlldh;
    private String xgqflr;
    List<YbllmxDto> ybllmxDtos;
    //内部编码
    private String nbbm;
    //路径标记
    private String ljbj;
    //
    private String sjid;
    //存储单位名称
    private String jcdwmc;
    //角色存储单位限制
    private List<String> jcdwxz;
    //存储单位参数代码
    private String jcdwdm;
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

    public String getJcdwdm() {
        return jcdwdm;
    }

    public void setJcdwdm(String jcdwdm) {
        this.jcdwdm = jcdwdm;
    }

    public List<String> getJcdwxz() {
        return jcdwxz;
    }

    public void setJcdwxz(List<String> jcdwxz) {
        this.jcdwxz = jcdwxz;
    }

    public String getJcdwmc() {
        return jcdwmc;
    }

    public void setJcdwmc(String jcdwmc) {
        this.jcdwmc = jcdwmc;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getLjbj() {
        return ljbj;
    }

    public void setLjbj(String ljbj) {
        this.ljbj = ljbj;
    }

    public String getNbbm() {
        return nbbm;
    }

    public void setNbbm(String nbbm) {
        this.nbbm = nbbm;
    }

    public List<YbllmxDto> getYbllmxDtos() {
        return ybllmxDtos;
    }

    public void setYbllmxDtos(List<YbllmxDto> ybllmxDtos) {
        this.ybllmxDtos = ybllmxDtos;
    }

    public String getXgqflr() {
        return xgqflr;
    }

    public void setXgqflr(String xgqflr) {
        this.xgqflr = xgqflr;
    }

    public String getWz() {
        return wz;
    }

    public void setWz(String wz) {
        this.wz = wz;
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

    public String getTj() {
        return tj;
    }

    public void setTj(String tj) {
        this.tj = tj;
    }

    public String getYblxmc() {
        return yblxmc;
    }

    public void setYblxmc(String yblxmc) {
        this.yblxmc = yblxmc;
    }

    public String getXgqlldh() {
        return xgqlldh;
    }

    public void setXgqlldh(String xgqlldh) {
        this.xgqlldh = xgqlldh;
    }

    public String getDelllmxids() {
        return delllmxids;
    }

    public void setDelllmxids(String delllmxids) {
        this.delllmxids = delllmxids;
    }

    public String getAddmxkcids() {
        return addmxkcids;
    }

    public void setAddmxkcids(String addmxkcids) {
        this.addmxkcids = addmxkcids;
    }

    public String getYblx() {
        return yblx;
    }

    public void setYblx(String yblx) {
        this.yblx = yblx;
    }

    public String getYbbh() {
        return ybbh;
    }

    public void setYbbh(String ybbh) {
        this.ybbh = ybbh;
    }

    public String getShsj() {
        return shsj;
    }

    public void setShsj(String shsj) {
        this.shsj = shsj;
    }

    public String[] getYblxs() {
        return yblxs;
    }

    public void setYblxs(String[] yblxs) {
        this.yblxs = yblxs;
    }

    public String getSqrqstart() {
        return sqrqstart;
    }

    public void setSqrqstart(String sqrqstart) {
        this.sqrqstart = sqrqstart;
    }

    public String getSqrqend() {
        return sqrqend;
    }

    public void setSqrqend(String sqrqend) {
        this.sqrqend = sqrqend;
    }

    public String getFlrmc() {
        return flrmc;
    }

    public void setFlrmc(String flrmc) {
        this.flrmc = flrmc;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getYbkcid() {
        return ybkcid;
    }

    public void setYbkcid(String ybkcid) {
        this.ybkcid = ybkcid;
    }

    public String getYbllmx_json() {
        return ybllmx_json;
    }

    public void setYbllmx_json(String ybllmx_json) {
        this.ybllmx_json = ybllmx_json;
    }

    public String getBmmc() {
        return bmmc;
    }

    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }

    public String getLlrmc() {
        return llrmc;
    }

    public void setLlrmc(String llrmc) {
        this.llrmc = llrmc;
    }
}
