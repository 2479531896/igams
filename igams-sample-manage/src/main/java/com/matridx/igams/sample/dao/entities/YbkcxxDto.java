package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

/*
 *@date 2022年06月06日12:28
 *
 */
@Alias(value = "YbkcxxDto")
public class YbkcxxDto extends YbkcxxModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ybkcid;
    private String sjid;
    private String bxid;
    private String chtid;
    private String hzid;
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
    //样本类型
    private String yblx;

    private String lrsjstart;
    private String lrsjend;
    private String entire;
    // 录入人员名称
    private String lrrymc;
    //冰箱号s
    private String[] bxhs;
    // 抽屉号s
    private String[] cths;
    // 盒子号s
    private String[] hhs;

    //导出关联标记位//所选择的字段
    private String sqlParam;
    //样本类型名称
    private String yblxmc;

    private String ybmx_json;
    private String addkcid;
    //预定标记
    private String ydbj;
    //关注指标
    private String gzzb;
    //疑似指标
    private String yszb;
    //内部编码
    private String nbbm;
    //样本录入类型
    private String yblrlx;
    //样本录入类型名称
    private String yblrlxmc;
   //样本类型多
    private String[] yblxs;
    //样本录入类型多
    private String[] yblrlxs;
	//体积小
    private String volumeMin;
    //体积大
    private String volumeMax;
    //已存放数
    private String ycfs;
    //修改前hzid
    private String xgqhzid;
    //修改前wz
    private String xgqwz;
    //备注
    private String bz;
    //存储单位
    private String jcdw;
    //盒子修改时间
    private String hzxgsj;
    //存储单位
    private String jcdwmc;
    //角色存储单位限制
    private List<String> jcdwxz;
    //存储单位ids
    private String[] jcdws;
    //存储单位参数代码
    private String jcdwdm;
    //盒子ids
    private List<String> hzids;
    //调出位置
    private String dcwz;
    //调入位置
    private String drwz;
    //调出sbh
    private String dcsbh;
    //调入sbh
    private String drsbh;

    //库存状态多
    private String[] ybkczts;
    //冰箱位置
    private String bxwz;
    //盒子位置
    private String hzwz;
    //抽屉位置
    private String ctwz;
    //调出冰箱
    private String dcbxid;
    //调出抽屉
    private String dcchtid;
    //调出盒子
    private String dchzid;
    //调入冰箱
    private String drbxid;
    //调入抽屉
    private String drchtid;
    //调入盒子
    private String drhzid;
    //调出盒子的已存放数
    private String dcycfs;
    //样本调拨id
    private String ybdbid;

    public String getYbdbid() {
        return ybdbid;
    }

    public void setYbdbid(String ybdbid) {
        this.ybdbid = ybdbid;
    }

    public String getDcycfs() {
        return dcycfs;
    }

    public void setDcycfs(String dcycfs) {
        this.dcycfs = dcycfs;
    }

    public String getDcbxid() {
        return dcbxid;
    }

    public void setDcbxid(String dcbxid) {
        this.dcbxid = dcbxid;
    }

    public String getDcchtid() {
        return dcchtid;
    }

    public void setDcchtid(String dcchtid) {
        this.dcchtid = dcchtid;
    }

    public String getDchzid() {
        return dchzid;
    }

    public void setDchzid(String dchzid) {
        this.dchzid = dchzid;
    }

    public String getDrbxid() {
        return drbxid;
    }

    public void setDrbxid(String drbxid) {
        this.drbxid = drbxid;
    }

    public String getDrchtid() {
        return drchtid;
    }

    public void setDrchtid(String drchtid) {
        this.drchtid = drchtid;
    }

    public String getDrhzid() {
        return drhzid;
    }

    public void setDrhzid(String drhzid) {
        this.drhzid = drhzid;
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

    public String[] getYbkczts() {
        return ybkczts;
    }

    public void setYbkczts(String[] ybkczts) {
        this.ybkczts = ybkczts;
    }

    public String getDcwz() {
        return dcwz;
    }

    public void setDcwz(String dcwz) {
        this.dcwz = dcwz;
    }

    public String getDrwz() {
        return drwz;
    }

    public void setDrwz(String drwz) {
        this.drwz = drwz;
    }

    public List<String> getHzids() {
        return hzids;
    }

    public void setHzids(List<String> hzids) {
        this.hzids = hzids;
    }

    public String getJcdwdm() {
        return jcdwdm;
    }

    public void setJcdwdm(String jcdwdm) {
        this.jcdwdm = jcdwdm;
    }

    public String[] getJcdws() {
        return jcdws;
    }

    public void setJcdws(String[] jcdws) {
        this.jcdws = jcdws;
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

    public String getHzxgsj() {
        return hzxgsj;
    }

    public void setHzxgsj(String hzxgsj) {
        this.hzxgsj = hzxgsj;
    }

    public String getJcdw() {
        return jcdw;
    }

    public void setJcdw(String jcdw) {
        this.jcdw = jcdw;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getXgqwz() {
        return xgqwz;
    }

    public void setXgqwz(String xgqwz) {
        this.xgqwz = xgqwz;
    }

    public String getXgqhzid() {
        return xgqhzid;
    }

    public void setXgqhzid(String xgqhzid) {
        this.xgqhzid = xgqhzid;
    }
    public String getYcfs() {
        return ycfs;
    }

    public void setYcfs(String ycfs) {
        this.ycfs = ycfs;
    }

    public String getVolumeMin() {
        return volumeMin;
    }

    public void setVolumeMin(String volumeMin) {
        this.volumeMin = volumeMin;
    }

    public String getVolumeMax() {
        return volumeMax;
    }

    public void setVolumeMax(String volumeMax) {
        this.volumeMax = volumeMax;
    }

    public String[] getYblrlxs() {
        return yblrlxs;
    }

    public void setYblrlxs(String[] yblrlxs) {
        this.yblrlxs = yblrlxs;
    }

    public String[] getYblxs() {
        return yblxs;
    }

    public void setYblxs(String[] yblxs) {
        this.yblxs = yblxs;
    }


    public String getYblrlx() {
        return yblrlx;
    }

    public void setYblrlx(String yblrlx) {
        this.yblrlx = yblrlx;
    }

    public String getYblrlxmc() {
        return yblrlxmc;
    }

    public void setYblrlxmc(String yblrlxmc) {
        this.yblrlxmc = yblrlxmc;
    }

    public String getNbbm() {
        return nbbm;
    }

    public void setNbbm(String nbbm) {
        this.nbbm = nbbm;
    }

    public String getGzzb() {
        return gzzb;
    }

    public void setGzzb(String gzzb) {
        this.gzzb = gzzb;
    }

    public String getYszb() {
        return yszb;
    }

    public void setYszb(String yszb) {
        this.yszb = yszb;
    }

    public String getYdbj() {
        return ydbj;
    }

    public void setYdbj(String ydbj) {
        this.ydbj = ydbj;
    }

    public String getAddkcid() {
        return addkcid;
    }

    public void setAddkcid(String addkcid) {
        this.addkcid = addkcid;
    }

    public String getYbmx_json() {
        return ybmx_json;
    }

    public void setYbmx_json(String ybmx_json) {
        this.ybmx_json = ybmx_json;
    }

    public String getYblxmc() {
        return yblxmc;
    }

    public void setYblxmc(String yblxmc) {
        this.yblxmc = yblxmc;
    }

    public void setBxhs(String[] bxhs) {
        this.bxhs = bxhs;
        for (int i = 0; i < bxhs.length; i++){
            this.bxhs[i]=this.bxhs[i].replace("'","");
        }
    }
    public void setCths(String[] cths) {
        this.cths = cths;
        for (int i = 0; i < cths.length; i++){
            this.cths[i]=this.cths[i].replace("'","");
        }
    }

    public void setHhs(String[] hhs) {
        this.hhs = hhs;
        for (int i = 0; i < hhs.length; i++){
            this.hhs[i]=this.hhs[i].replace("'","");
        }
    }
    public String[] getBxhs() {
        return bxhs;
    }

    public String[] getCths() {
        return cths;
    }

    public String[] getHhs() {
        return hhs;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getYblx() {
        return yblx;
    }

    public void setYblx(String yblx) {
        this.yblx = yblx;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getYbbh() {
        return ybbh;
    }

    public void setYbbh(String ybbh) {
        this.ybbh = ybbh;
    }

    public String getYbkcid() {
        return ybkcid;
    }

    public void setYbkcid(String ybkcid) {
        this.ybkcid = ybkcid;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getBxid() {
        return bxid;
    }

    public void setBxid(String bxid) {
        this.bxid = bxid;
    }

    public String getChtid() {
        return chtid;
    }

    public void setChtid(String ctid) {
        this.chtid = ctid;
    }

    public String getHzid() {
        return hzid;
    }

    public void setHzid(String hzid) {
        this.hzid = hzid;
    }

    public String getWz() {
        return wz;
    }

    public void setWz(String wz) {
        this.wz = wz;
    }

    public String getTj() {
        return tj;
    }

    public void setTj(String tj) {
        this.tj = tj;
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

    public String getLrsjstart() {
        return lrsjstart;
    }

    public void setLrsjstart(String lrsjstart) {
        this.lrsjstart = lrsjstart;
    }

    public String getLrsjend() {
        return lrsjend;
    }

    public void setLrsjend(String lrsjend) {
        this.lrsjend = lrsjend;
    }

    public String getDcsbh() {
        return dcsbh;
    }

    public void setDcsbh(String dcsbh) {
        this.dcsbh = dcsbh;
    }

    public String getDrsbh() {
        return drsbh;
    }

    public void setDrsbh(String drsbh) {
        this.drsbh = drsbh;
    }
}
