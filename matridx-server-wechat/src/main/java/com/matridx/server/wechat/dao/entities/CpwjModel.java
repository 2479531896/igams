package com.matridx.server.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "CpwjModel")
public class CpwjModel extends BaseModel {
    private String cpwjid;//产品文件id
    private String cpmc;//产品名称
    private String cpdm;//产品代码
    private String bbh;//版本号
    private String gxsj;//更新时间
    private String sfgk;//是否公开（0 否 1 是）
    private String xh;//序号

    public String getCpwjid() {
        return cpwjid;
    }

    public void setCpwjid(String cpwjid) {
        this.cpwjid = cpwjid;
    }

    public String getCpmc() {
        return cpmc;
    }

    public void setCpmc(String cpmc) {
        this.cpmc = cpmc;
    }

    public String getCpdm() {
        return cpdm;
    }

    public void setCpdm(String cpdm) {
        this.cpdm = cpdm;
    }

    public String getBbh() {
        return bbh;
    }

    public void setBbh(String bbh) {
        this.bbh = bbh;
    }

    public String getGxsj() {
        return gxsj;
    }

    public void setGxsj(String gxsj) {
        this.gxsj = gxsj;
    }

    public String getSfgk() {
        return sfgk;
    }

    public void setSfgk(String sfgk) {
        this.sfgk = sfgk;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
