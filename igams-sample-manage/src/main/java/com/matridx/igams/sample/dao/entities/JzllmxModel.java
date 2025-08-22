package com.matridx.igams.sample.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value="JzllmxModel")
public class JzllmxModel extends BaseBasicModel {
    private String llmxid;//领料明细id
    private String llid;//领料id
    private String jzkcid;//菌种库存id
    private String qlsl;//请领数量
    private String cksl;//出库数量

    public String getLlmxid() {
        return llmxid;
    }

    public void setLlmxid(String llmxid) {
        this.llmxid = llmxid;
    }

    public String getLlid() {
        return llid;
    }

    public void setLlid(String llid) {
        this.llid = llid;
    }

    public String getJzkcid() {
        return jzkcid;
    }

    public void setJzkcid(String jzkcid) {
        this.jzkcid = jzkcid;
    }

    public String getQlsl() {
        return qlsl;
    }

    public void setQlsl(String qlsl) {
        this.qlsl = qlsl;
    }

    public String getCksl() {
        return cksl;
    }

    public void setCksl(String cksl) {
        this.cksl = cksl;
    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
