package com.matridx.igams.sample.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value="JzllcModel")
public class JzllcModel extends BaseBasicModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String jzkcid;//菌种库id
    private String ryid;//人员id
    public String getJzkcid() {
        return jzkcid;
    }

    public void setJzkcid(String jzkcid) {
        this.jzkcid = jzkcid;
    }

    public String getRyid() {
        return ryid;
    }

    public void setRyid(String ryid) {
        this.ryid = ryid;
    }

}
