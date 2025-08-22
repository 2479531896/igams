package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("XzdbcglModel")
public class XzdbcglModel extends BaseBasicModel {
    private static final long serialVersionUID = 1L;
    //行政库存id
    private String xzkcid;
    private String ryid;

    public String getXzkcid() {
        return xzkcid;
    }

    public void setXzkcid(String xzkcid) {
        this.xzkcid = xzkcid;
    }

    public String getRyid() {
        return ryid;
    }

    public void setRyid(String ryid) {
        this.ryid = ryid;
    }
}
