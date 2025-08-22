package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="LlxxModel")
public class LlxxModel extends BaseBasicModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String llid;
    private String hwid;
    private String qlsl;
    private String qlry;
    private String bz;

    public String getLlid() {
        return llid;
    }

    public void setLlid(String llid) {
        this.llid = llid;
    }

    public String getHwid() {
        return hwid;
    }

    public void setHwid(String hwid) {
        this.hwid = hwid;
    }

    public String getQlsl() {
        return qlsl;
    }

    public void setQlsl(String qlsl) {
        this.qlsl = qlsl;
    }

    public String getQlry() {
        return qlry;
    }

    public void setQlry(String qlry) {
        this.qlry = qlry;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
}
