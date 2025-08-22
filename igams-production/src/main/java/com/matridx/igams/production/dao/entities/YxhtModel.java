package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "YxhtModel")
public class YxhtModel extends BaseBasicModel {
    private String htid;        //合同id
    private String htbh;        //合同编号
    private String htmc;        //合同名称
    private String htrq;        //合同日期
    private String ssywy;        //所属业务员
    private String khid;        //客户id
    private String zd;        //终端
    private String htje;        //合同金额
    private String htzk;        //合同折扣
    private String htlx;        //合同类型
    private String htqx;        //合同期限
    private String hkfs;        //回款方式
    private String szht;        //双章合同
    private String htfxcd;        //合同风险程度
    private String xslb;        //销售类别
    private String yzlb;        //用章类别
    private String htyjxx;        //合同邮寄信息
    private String bz;        //备注
    private String zt;        //状态
    private String zsld;        //直属领导
    private String htsl;//合同数量
    private String htzt;//合同主体
    private String htjbr;//合同经办人
    private String htjbbm;//合同经办部门
    private String htgsbm;//合同归属部门
    private String htqdzt;//合同签订状态
    private String yxhtlx;

    public String getYxhtlx() {
        return yxhtlx;
    }

    public void setYxhtlx(String yxhtlx) {
        this.yxhtlx = yxhtlx;
    }

    public String getHtqdzt() {
        return htqdzt;
    }

    public void setHtqdzt(String htqdzt) {
        this.htqdzt = htqdzt;
    }

    public String getHtsl() {
        return htsl;
    }
    public void setHtsl(String htsl) {
        this.htsl = htsl;
    }

    public String getHtzt() {
        return htzt;
    }

    public void setHtzt(String htzt) {
        this.htzt = htzt;
    }

    public String getHtjbr() {
        return htjbr;
    }

    public void setHtjbr(String htjbr) {
        this.htjbr = htjbr;
    }

    public String getHtjbbm() {
        return htjbbm;
    }

    public void setHtjbbm(String htjbbm) {
        this.htjbbm = htjbbm;
    }

    public String getHtgsbm() {
        return htgsbm;
    }

    public void setHtgsbm(String htgsbm) {
        this.htgsbm = htgsbm;
    }
    public String getZsld() {
        return zsld;
    }

    public void setZsld(String zsld) {
        this.zsld = zsld;
    }

    public String getHtid() {
        return htid;
    }

    public void setHtid(String htid) {
        this.htid = htid;
    }

    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    public String getHtmc() {
        return htmc;
    }

    public void setHtmc(String htmc) {
        this.htmc = htmc;
    }

    public String getHtrq() {
        return htrq;
    }

    public void setHtrq(String htrq) {
        this.htrq = htrq;
    }

    public String getSsywy() {
        return ssywy;
    }

    public void setSsywy(String ssywy) {
        this.ssywy = ssywy;
    }

    public String getKhid() {
        return khid;
    }

    public void setKhid(String khid) {
        this.khid = khid;
    }

    public String getZd() {
        return zd;
    }

    public void setZd(String zd) {
        this.zd = zd;
    }

    public String getHtje() {
        return htje;
    }

    public void setHtje(String htje) {
        this.htje = htje;
    }

    public String getHtzk() {
        return htzk;
    }

    public void setHtzk(String htzk) {
        this.htzk = htzk;
    }

    public String getHtlx() {
        return htlx;
    }

    public void setHtlx(String htlx) {
        this.htlx = htlx;
    }

    public String getHtqx() {
        return htqx;
    }

    public void setHtqx(String htqx) {
        this.htqx = htqx;
    }

    public String getHkfs() {
        return hkfs;
    }

    public void setHkfs(String hkfs) {
        this.hkfs = hkfs;
    }

    public String getSzht() {
        return szht;
    }

    public void setSzht(String szht) {
        this.szht = szht;
    }

    public String getHtfxcd() {
        return htfxcd;
    }

    public void setHtfxcd(String htfxcd) {
        this.htfxcd = htfxcd;
    }

    public String getXslb() {
        return xslb;
    }

    public void setXslb(String xslb) {
        this.xslb = xslb;
    }

    public String getYzlb() {
        return yzlb;
    }

    public void setYzlb(String yzlb) {
        this.yzlb = yzlb;
    }

    public String getHtyjxx() {
        return htyjxx;
    }

    public void setHtyjxx(String htyjxx) {
        this.htyjxx = htyjxx;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
