package com.matridx.igams.hrm.dao.entities;

import com.dingtalk.api.response.OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * @author:JYK
 */
@Alias(value = "JqffjlDto")
public class JqffjlDto extends JqffjlModel{
    private String jqlxmc;//假期类型
    private String yhm;//用户名
    private String zsxm;//姓名
    private String[] ffszids;
    private List<String> yhids;//用户ids
    private List<String> kjqyhids;//空假期
    private List<String> ddids;//钉钉ids
    private List<OapiLeaveQuotaUserListVo> oapiLeaveQuotaUserListVos;//假期额度
    private String czrddid;//操作人钉钉id
    private String lrrymc;//录入人员名称
    private String[] nds;//年度s
    private String[] jqlxs;//假期类型s
    private String kssjstart;
    private String kssjend;
    private String yxqstart;
    private String yxqend;
    private String entire;
    private List<String> yhjqids;//用户假期ids
    private String ddid;//钉钉id
    private String jqlxcskz2;//假期类型参数扩展2 假期code
    private String yhjqze;//用户假期总额
    private String yhyyed;//用户已用额度
    private String yhsyed;//用户剩余额度
    private String yhddze;//用户钉钉总额
    private String yhddsyed;//用户钉钉剩余额度
    private String yhjqkssj;//用户假期开始时间
    private String yhjqjssj;//用户假期结束时间

    public String getYhjqkssj() {
        return yhjqkssj;
    }

    public void setYhjqkssj(String yhjqkssj) {
        this.yhjqkssj = yhjqkssj;
    }

    public String getYhjqjssj() {
        return yhjqjssj;
    }

    public void setYhjqjssj(String yhjqjssj) {
        this.yhjqjssj = yhjqjssj;
    }

    public String getYhddze() {
        return yhddze;
    }

    public void setYhddze(String yhddze) {
        this.yhddze = yhddze;
    }

    public String getYhddsyed() {
        return yhddsyed;
    }

    public void setYhddsyed(String yhddsyed) {
        this.yhddsyed = yhddsyed;
    }

    public String getYhsyed() {
        return yhsyed;
    }

    public void setYhsyed(String yhsyed) {
        this.yhsyed = yhsyed;
    }

    public String getYhjqze() {
        return yhjqze;
    }

    public void setYhjqze(String yhjqze) {
        this.yhjqze = yhjqze;
    }

    public String getYhyyed() {
        return yhyyed;
    }

    public void setYhyyed(String yhyyed) {
        this.yhyyed = yhyyed;
    }

    public String getJqlxcskz2() {
        return jqlxcskz2;
    }

    public void setJqlxcskz2(String jqlxcskz2) {
        this.jqlxcskz2 = jqlxcskz2;
    }

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public List<String> getYhjqids() {
        return yhjqids;
    }

    public void setYhjqids(List<String> yhjqids) {
        this.yhjqids = yhjqids;
    }

    public String getKssjstart() {
        return kssjstart;
    }

    public void setKssjstart(String kssjstart) {
        this.kssjstart = kssjstart;
    }

    public String getKssjend() {
        return kssjend;
    }

    public void setKssjend(String kssjend) {
        this.kssjend = kssjend;
    }

    public String getYxqstart() {
        return yxqstart;
    }

    public void setYxqstart(String yxqstart) {
        this.yxqstart = yxqstart;
    }

    public String getYxqend() {
        return yxqend;
    }

    public void setYxqend(String yxqend) {
        this.yxqend = yxqend;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String[] getNds() {
        return nds;
    }

    public void setNds(String[] nds) {
        this.nds = nds;
    }

    public String[] getJqlxs() {
        return jqlxs;
    }

    public void setJqlxs(String[] jqlxs) {
        this.jqlxs = jqlxs;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getCzrddid() {
        return czrddid;
    }

    public void setCzrddid(String czrddid) {
        this.czrddid = czrddid;
    }

    public List<String> getDdids() {
        return ddids;
    }

    public void setDdids(List<String> ddids) {
        this.ddids = ddids;
    }

    public List<String> getKjqyhids() {
        return kjqyhids;
    }

    public void setKjqyhids(List<String> kjqyhids) {
        this.kjqyhids = kjqyhids;
    }

    public List<OapiLeaveQuotaUserListVo> getOapiLeaveQuotaUserListVos() {
        return oapiLeaveQuotaUserListVos;
    }

    public void setOapiLeaveQuotaUserListVos(List<OapiLeaveQuotaUserListVo> oapiLeaveQuotaUserListVos) {
        this.oapiLeaveQuotaUserListVos = oapiLeaveQuotaUserListVos;
    }

    public List<String> getYhids() {
        return yhids;
    }

    public void setYhids(List<String> yhids) {
        this.yhids = yhids;
    }

    public String[] getFfszids() {
        return ffszids;
    }

    public void setFfszids(String[] ffszids) {
        this.ffszids = ffszids;
    }

    public String getJqlxmc() {
        return jqlxmc;
    }

    public void setJqlxmc(String jqlxmc) {
        this.jqlxmc = jqlxmc;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }
}
