package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author WYX
 * @version 1.0
 * @className JxtxmxModel
 * @description TODO
 * @date 9:13 2023/3/24
 **/
@Alias(value = "JxtxmxModel")
public class JxtxmxModel extends BaseModel {
    private String jxtxid;//绩效提醒id
    private String jxtxmxid;//绩效提醒明细id
    private String ryid;//人员id

    public String getJxtxid() {
        return jxtxid;
    }

    public void setJxtxid(String jxtxid) {
        this.jxtxid = jxtxid;
    }

    public String getJxtxmxid() {
        return jxtxmxid;
    }

    public void setJxtxmxid(String jxtxmxid) {
        this.jxtxmxid = jxtxmxid;
    }

    public String getRyid() {
        return ryid;
    }

    public void setRyid(String ryid) {
        this.ryid = ryid;
    }


    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
