package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "YghmcModel")
public class YghmcModel extends BaseModel {
    //员工花名册id
    private String yghmcid;
    //用户id
    private String yhid;
    //性别
    private String sex;
    //民族
    private String mz;
    //身份证
    private String sfz;
    //年龄
    private String nl;
    //证件有效期
    private String zjyxq;
    //出生日期
    private String csrq;
    //出生月份
    private String csyf;
    //学历
    private String xl;
    //毕业院校
    private String byyx;
    //专业
    private String zy;
    //手机号码
    private String sjhm;
    //入职日期
    private String rzrq;
    //转正日期
    private String zzrq;
    //参加工作时间
    private String cjgzsj;
    //司龄
    private String sl;
    //婚育状况
    private String hyzk;
    //政治面貌
    private String zzmm;
    //职称
    private String zc;
    //籍贯
    private String jg;
    //身份证地址
    private String sfzdz;
    //户籍性质
    private String hjxz;
    //现居地址
    private String xjdz;
    //紧急联系人
    private String jjlxr;
    //紧急联系人电话
    private String lxrdh;
    //首次合同起始日
    private String schtqsr;
    //首次合同到期日
    private String schtdqr;
    //银行卡号
    private String yhkh;
    //开户行
    private String khh;
    //办公地点
    private String bgdd;
    //是否签订保密协议
    private String sfqdbmxy;
    //是否离职
    private String sflz;
    //离职日期
    private String lzrq;
    //现合同起始日
    private String xhtqsr;
    //现合同到期日
    private String xhtdqr;
    //到期提醒
    private String dqtx;
    //所属公司
    private String ssgs;
    //上级部门
    private String sjbm;
    //直属主管
    private String zszg;
    //第一级部门
    private String dyjbm;
    //第二级部门
    private String dejbm;
    //第三级部门
    private String dsjbm;
//外部程序id
    private String wbcxid;

    public String getWbcxid() {
        return wbcxid;
    }

    public void setWbcxid(String wbcxid) {
        this.wbcxid = wbcxid;
    }
    public String getDejbm() {
        return dejbm;
    }

    public void setDejbm(String dejbm) {
        this.dejbm = dejbm;
    }

    public String getDsjbm() {
        return dsjbm;
    }

    public void setDsjbm(String dsjbm) {
        this.dsjbm = dsjbm;
    }

    public String getDyjbm() {
        return dyjbm;
    }

    public void setDyjbm(String dyjbm) {
        this.dyjbm = dyjbm;
    }

    public String getSsgs() {
        return ssgs;
    }

    public void setSsgs(String ssgs) {
        this.ssgs = ssgs;
    }

    public String getSjbm() {
        return sjbm;
    }

    public void setSjbm(String sjbm) {
        this.sjbm = sjbm;
    }

    public String getZszg() {
        return zszg;
    }

    public void setZszg(String zszg) {
        this.zszg = zszg;
    }

    public String getDqtx() {
        return dqtx;
    }

    public void setDqtx(String dqtx) {
        this.dqtx = dqtx;
    }

    public String getXhtqsr() {
        return xhtqsr;
    }

    public void setXhtqsr(String xhtqsr) {
        this.xhtqsr = xhtqsr;
    }

    public String getXhtdqr() {
        return xhtdqr;
    }

    public void setXhtdqr(String xhtdqr) {
        this.xhtdqr = xhtdqr;
    }

    public String getYghmcid() {
        return yghmcid;
    }

    public void setYghmcid(String yghmcid) {
        this.yghmcid = yghmcid;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getSfz() {
        return sfz;
    }

    public void setSfz(String sfz) {
        this.sfz = sfz;
    }

    public String getNl() {
        return nl;
    }

    public void setNl(String nl) {
        this.nl = nl;
    }

    public String getZjyxq() {
        return zjyxq;
    }

    public void setZjyxq(String zjyxq) {
        this.zjyxq = zjyxq;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getCsyf() {
        return csyf;
    }

    public void setCsyf(String csyf) {
        this.csyf = csyf;
    }

    public String getXl() {
        return xl;
    }

    public void setXl(String xl) {
        this.xl = xl;
    }

    public String getByyx() {
        return byyx;
    }

    public void setByyx(String byyx) {
        this.byyx = byyx;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }

    public String getRzrq() {
        return rzrq;
    }

    public void setRzrq(String rzrq) {
        this.rzrq = rzrq;
    }

    public String getZzrq() {
        return zzrq;
    }

    public void setZzrq(String zzrq) {
        this.zzrq = zzrq;
    }

    public String getCjgzsj() {
        return cjgzsj;
    }

    public void setCjgzsj(String cjgzsj) {
        this.cjgzsj = cjgzsj;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getHyzk() {
        return hyzk;
    }

    public void setHyzk(String hyzk) {
        this.hyzk = hyzk;
    }

    public String getZzmm() {
        return zzmm;
    }

    public void setZzmm(String zzmm) {
        this.zzmm = zzmm;
    }

    public String getZc() {
        return zc;
    }

    public void setZc(String zc) {
        this.zc = zc;
    }

    public String getJg() {
        return jg;
    }

    public void setJg(String jg) {
        this.jg = jg;
    }

    public String getSfzdz() {
        return sfzdz;
    }

    public void setSfzdz(String sfzdz) {
        this.sfzdz = sfzdz;
    }

    public String getHjxz() {
        return hjxz;
    }

    public void setHjxz(String hjxz) {
        this.hjxz = hjxz;
    }

    public String getXjdz() {
        return xjdz;
    }

    public void setXjdz(String xjdz) {
        this.xjdz = xjdz;
    }

    public String getJjlxr() {
        return jjlxr;
    }

    public void setJjlxr(String jjlxr) {
        this.jjlxr = jjlxr;
    }

    public String getLxrdh() {
        return lxrdh;
    }

    public void setLxrdh(String lxrdh) {
        this.lxrdh = lxrdh;
    }

    public String getSchtqsr() {
        return schtqsr;
    }

    public void setSchtqsr(String schtqsr) {
        this.schtqsr = schtqsr;
    }

    public String getSchtdqr() {
        return schtdqr;
    }

    public void setSchtdqr(String schtdqr) {
        this.schtdqr = schtdqr;
    }

    public String getYhkh() {
        return yhkh;
    }

    public void setYhkh(String yhkh) {
        this.yhkh = yhkh;
    }

    public String getKhh() {
        return khh;
    }

    public void setKhh(String khh) {
        this.khh = khh;
    }

    public String getBgdd() {
        return bgdd;
    }

    public void setBgdd(String bgdd) {
        this.bgdd = bgdd;
    }

    public String getSfqdbmxy() {
        return sfqdbmxy;
    }

    public void setSfqdbmxy(String sfqdbmxy) {
        this.sfqdbmxy = sfqdbmxy;
    }

    public String getSflz() {
        return sflz;
    }

    public void setSflz(String sflz) {
        this.sflz = sflz;
    }

    public String getLzrq() {
        return lzrq;
    }

    public void setLzrq(String lzrq) {
        this.lzrq = lzrq;
    }
    private static final long serialVersionUID = 1L;
}
