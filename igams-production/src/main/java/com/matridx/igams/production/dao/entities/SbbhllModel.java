package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "SbbhllModel")
public class SbbhllModel extends BaseBasicModel {
    private String llid;//履历id
    private String sbysid;//设备验收id
    private String ysbbh;//原设备编号
    private String ygdzcbh;//原固定资产编号
    private String bgsj;//变更时间

    public String getLlid() {
        return llid;
    }

    public void setLlid(String llid) {
        this.llid = llid;
    }

    public String getSbysid() {
        return sbysid;
    }

    public void setSbysid(String sbysid) {
        this.sbysid = sbysid;
    }

    public String getYsbbh() {
        return ysbbh;
    }

    public void setYsbbh(String ysbbh) {
        this.ysbbh = ysbbh;
    }

    public String getYgdzcbh() {
        return ygdzcbh;
    }

    public void setYgdzcbh(String ygdzcbh) {
        this.ygdzcbh = ygdzcbh;
    }

    public String getBgsj() {
        return bgsj;
    }

    public void setBgsj(String bgsj) {
        this.bgsj = bgsj;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
