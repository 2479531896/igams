package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author WYX
 * @version 1.0
 * @className JxtxModel
 * @description TODO
 * @date 9:10 2023/3/24
 **/
@Alias(value = "JxtxModel")
public class JxtxModel extends BaseModel {
    private String jxtxid;//绩效提醒id
    private String khzq;//考核周期
    private String txjb;//提醒级别0：员工  1：上级
    private String txy;//提醒月
    private String txrq;//提醒日期

    public String getJxtxid() {
        return jxtxid;
    }

    public void setJxtxid(String jxtxid) {
        this.jxtxid = jxtxid;
    }

    public String getKhzq() {
        return khzq;
    }

    public void setKhzq(String khzq) {
        this.khzq = khzq;
    }

    public String getTxjb() {
        return txjb;
    }

    public void setTxjb(String txjb) {
        this.txjb = txjb;
    }

    public String getTxy() {
        return txy;
    }

    public void setTxy(String txy) {
        this.txy = txy;
    }

    public String getTxrq() {
        return txrq;
    }

    public void setTxrq(String txrq) {
        this.txrq = txrq;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
