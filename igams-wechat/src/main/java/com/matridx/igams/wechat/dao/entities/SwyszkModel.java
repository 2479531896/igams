package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;



/**
 * 商务应收账款(IgamsSwyszk)实体类
 *
 * @author makejava
 * @since 2023-04-12 10:17:46
 */
@Alias(value="SwyszkModel")
public class SwyszkModel extends BaseBasicModel {
    private static final long serialVersionUID = 1L;
    /**
     * 应收账款ID
     */
    private String yszkid;
    /**
     * 账单周期
     */
    private String zdzq;
    /**
     * 区域
     */
    private String qy;
    /**
     * 对账客户
     */
    private String dzkh;
    /**
     * 客户性质
     */
    private String khxz;
    /**
     * 对接销售
     */
    private String djxs;
    /**
     * 回款周期
     */
    private String hkzq;
    /**
     * 汇款主体
     */
    private String hkzt;
    /**
     * 结算金额
     */
    private String jsje;
    /**
     * 实付金额
     */
    private String sfje;
    /**
     * 开票日期
     */
    private String kprq;
    /**
     * 开票号码
     */
    private String kphm;
    /**
     * 开票金额
     */
    private String kpje;
    /**
     * 回款金额
     */
    private String hkje;
    /**
     * 未回款金额
     */
    private String whkje;
    /**
     * 返款金额
     */
    private String fkje;
    /**
     * 未返款金额
     */
    private String wfkje;
    /**
     * 是否逾期
     */
    private String sfyq;
    /**
     * 是否结清
     */
    private String sfjq;

    /**
     * 快递费用
     */
    private String kdfy;
    /**
     * 伙伴分类
     */
    private String fl;

    /**
     * 到款日期
     */
    private String dkrq;
    /**
     * 是否结算
     */
    private String sfjs;
    /**
     * 开票数量
     */
    private String kpsl;
    /**
     * 已开票数量
     */
    private String ykpsl;
    /**
     * 付款数量
     */
    private String fksl;
    /**
     * 已付款数量
     */
    private String yfksl;
    /**
     * 逾期通知时间
     */
    private String yqtzsj;
    /**
     * 备注
     */
    private String bz;

    private String jxje;

    public String getJxje() {
        return jxje;
    }

    public void setJxje(String jxje) {
        this.jxje = jxje;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getYqtzsj() {
        return yqtzsj;
    }

    public void setYqtzsj(String yqtzsj) {
        this.yqtzsj = yqtzsj;
    }

    public String getKpsl() {
        return kpsl;
    }

    public void setKpsl(String kpsl) {
        this.kpsl = kpsl;
    }

    public String getYkpsl() {
        return ykpsl;
    }

    public void setYkpsl(String ykpsl) {
        this.ykpsl = ykpsl;
    }

    public String getFksl() {
        return fksl;
    }

    public void setFksl(String fksl) {
        this.fksl = fksl;
    }

    public String getYfksl() {
        return yfksl;
    }

    public void setYfksl(String yfksl) {
        this.yfksl = yfksl;
    }

    public String getDkrq() {
        return dkrq;
    }

    public void setDkrq(String dkrq) {
        this.dkrq = dkrq;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getKdfy() {
        return kdfy;
    }

    public void setKdfy(String kdfy) {
        this.kdfy = kdfy;
    }

    public String getSfjq() {
        return sfjq;
    }

    public void setSfjq(String sfjq) {
        this.sfjq = sfjq;
    }

    public String getYszkid() {
        return yszkid;
    }

    public void setYszkid(String yszkid) {
        this.yszkid = yszkid;
    }

    public String getZdzq() {
        return zdzq;
    }

    public void setZdzq(String zdzq) {
        this.zdzq = zdzq;
    }

    public String getQy() {
        return qy;
    }

    public void setQy(String qy) {
        this.qy = qy;
    }

    public String getDzkh() {
        return dzkh;
    }

    public void setDzkh(String dzkh) {
        this.dzkh = dzkh;
    }

    public String getKhxz() {
        return khxz;
    }

    public void setKhxz(String khxz) {
        this.khxz = khxz;
    }

    public String getDjxs() {
        return djxs;
    }

    public void setDjxs(String djxs) {
        this.djxs = djxs;
    }

    public String getHkzq() {
        return hkzq;
    }

    public void setHkzq(String hkzq) {
        this.hkzq = hkzq;
    }

    public String getHkzt() {
        return hkzt;
    }

    public void setHkzt(String hkzt) {
        this.hkzt = hkzt;
    }

    public String getJsje() {
        return jsje;
    }

    public void setJsje(String jsje) {
        this.jsje = jsje;
    }

    public String getSfje() {
        return sfje;
    }

    public void setSfje(String sfje) {
        this.sfje = sfje;
    }

    public String getKprq() {
        return kprq;
    }

    public void setKprq(String kprq) {
        this.kprq = kprq;
    }

    public String getKphm() {
        return kphm;
    }

    public void setKphm(String kphm) {
        this.kphm = kphm;
    }

    public String getKpje() {
        return kpje;
    }

    public void setKpje(String kpje) {
        this.kpje = kpje;
    }

    public String getHkje() {
        return hkje;
    }

    public void setHkje(String hkje) {
        this.hkje = hkje;
    }

    public String getWhkje() {
        return whkje;
    }

    public void setWhkje(String whkje) {
        this.whkje = whkje;
    }

    public String getFkje() {
        return fkje;
    }

    public void setFkje(String fkje) {
        this.fkje = fkje;
    }

    public String getWfkje() {
        return wfkje;
    }

    public void setWfkje(String wfkje) {
        this.wfkje = wfkje;
    }

    public String getSfyq() {
        return sfyq;
    }

    public void setSfyq(String sfyq) {
        this.sfyq = sfyq;
    }


    public String getSfjs() {
        return sfjs;
    }

    public void setSfjs(String sfjs) {
        this.sfjs = sfjs;
    }
}

