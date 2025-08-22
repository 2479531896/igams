package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * 商务合同管理(IgamsSwhtgl)实体类
 *
 * @author makejava
 * @since 2023-04-04 13:52:16
 */
@Alias(value="SwhtglModel")
public class SwhtglModel extends BaseBasicModel {
    private static final long serialVersionUID = 1L;
    /**
     * 合同ID
     */
    private String htid;
    /**
     * 合同分类;基础数据，业务合同/备案
     */
    private String htfl;

    /**
     * 合同分类;基础数据，业务合同/备案
     */
    private String htzfl;
    /**
     * 合同编号
     */
    private String htbh;
    /**
     * 是否履行
     */
    private String sflx;
    /**
     * 合同名称
     */
    private String htmc;
    /**
     * 合同风险程度，基础数据已有
     */
    private String htfx;
    /**
     * 签订对象；客户ID
     */
    private String qddx;
    /**
     * 用章类型,公章/合同章;基础数据
     */
    private String yzlx;
    /**
     * 销售类别;服务/其他;基础数据
     */
    private String xslb;
    /**
     * 合同份数
     */
    private String htfs;
    /**
     * 金额
     */
    private String je;
    /**
     * 合同开始日期
     */
    private String htksrq;
    /**
     * 合同结束日期
     */
    private String htjsrq;
    /**
     * 合同邮寄信息
     */
    private String htyjxx;
    /**
     * 盖章状态;0/1,上传双章合同变为1
     */
    private String gzzt;
    /**
     * 合同状态;00:签订中;10:已签订;20:履行中;30:即将逾期;40:已结束
     */
    private String htzt;
    /**
     * 状态;00:未提交:10:审核中;15:审核不通过;80:审核通过
     */
    private String zt;
    /**
     * 备注，存放优惠政策等信息
     */
    private String bz;
    /**
     * 终止日期
     */
    private String zzrq;
    /**
     * 终止原因
     */
    private String zzyy;
    private String spr;//审批人

	/**
     * 负责人
     */
    private String fzr;

    /**
     * 优惠政策
     */
    private String yhzc;

    /**
     * 是否处理
     */
    private String sfcl;

    /**
     * 终端客户
     */
    private String zdkh;

    public String getZdkh() {
        return zdkh;
    }

    public void setZdkh(String zdkh) {
        this.zdkh = zdkh;
    }

    public String getSfcl() {
        return sfcl;
    }

    public void setSfcl(String sfcl) {
        this.sfcl = sfcl;
    }

    public String getYhzc() {
        return yhzc;
    }

    public void setYhzc(String yhzc) {
        this.yhzc = yhzc;
    }

    public String getFzr() {
        return fzr;
    }

    public void setFzr(String fzr) {
        this.fzr = fzr;
    }
	
    public String getSpr() {
        return spr;
    }

    public void setSpr(String spr) {
        this.spr = spr;
    }

    /**
     * 签订主体
     */
    private String qdzt;
    /**
     * 授权区域
     */
    private String sqqy;
    /**
     * 联系人
     */
    private String lxr;
    /**
     * 联系电话
     */
    private String lxdh;

    public String getSqqy() {
        return sqqy;
    }

    public void setSqqy(String sqqy) {
        this.sqqy = sqqy;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getQdzt() {
        return qdzt;
    }

    public void setQdzt(String qdzt) {
        this.qdzt = qdzt;
    }

    public String getHtzfl() {
        return htzfl;
    }

    public void setHtzfl(String htzfl) {
        this.htzfl = htzfl;
    }

    public String getZzrq() {
        return zzrq;
    }

    public void setZzrq(String zzrq) {
        this.zzrq = zzrq;
    }

    public String getZzyy() {
        return zzyy;
    }

    public void setZzyy(String zzyy) {
        this.zzyy = zzyy;
    }

    public String getHtid() {
        return htid;
    }

    public void setHtid(String htid) {
        this.htid = htid;
    }

    public String getHtfl() {
        return htfl;
    }

    public void setHtfl(String htfl) {
        this.htfl = htfl;
    }

    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    public String getSflx() {
        return sflx;
    }

    public void setSflx(String sflx) {
        this.sflx = sflx;
    }

    public String getHtmc() {
        return htmc;
    }

    public void setHtmc(String htmc) {
        this.htmc = htmc;
    }

    public String getHtfx() {
        return htfx;
    }

    public void setHtfx(String htfx) {
        this.htfx = htfx;
    }

    public String getQddx() {
        return qddx;
    }

    public void setQddx(String qddx) {
        this.qddx = qddx;
    }

    public String getYzlx() {
        return yzlx;
    }

    public void setYzlx(String yzlx) {
        this.yzlx = yzlx;
    }

    public String getXslb() {
        return xslb;
    }

    public void setXslb(String xslb) {
        this.xslb = xslb;
    }

    public String getHtfs() {
        return htfs;
    }

    public void setHtfs(String htfs) {
        this.htfs = htfs;
    }

    public String getJe() {
        return je;
    }

    public void setJe(String je) {
        this.je = je;
    }

    public String getHtyjxx() {
        return htyjxx;
    }

    public void setHtyjxx(String htyjxx) {
        this.htyjxx = htyjxx;
    }

    public String getGzzt() {
        return gzzt;
    }

    public void setGzzt(String gzzt) {
        this.gzzt = gzzt;
    }

    public String getHtzt() {
        return htzt;
    }

    public void setHtzt(String htzt) {
        this.htzt = htzt;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getHtksrq() {
        return htksrq;
    }

    public void setHtksrq(String htksrq) {
        this.htksrq = htksrq;
    }

    public String getHtjsrq() {
        return htjsrq;
    }

    public void setHtjsrq(String htjsrq) {
        this.htjsrq = htjsrq;
    }
}

