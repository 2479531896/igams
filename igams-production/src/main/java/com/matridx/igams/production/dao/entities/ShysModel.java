package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias("ShysModel")
public class ShysModel extends BaseBasicModel {
    private static final long serialVersionUID = 1L;

    private String id;
    private String autoid;
    private String shys;
    private String cszt; //0超时 1处理正常
    private String csbz;

    public String getCszt() {
        return cszt;
    }

    public void setCszt(String cszt) {
        this.cszt = cszt;
    }

    public String getCsbz() {
        return csbz;
    }

    public void setCsbz(String csbz) {
        this.csbz = csbz;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAutoid() {
        return autoid;
    }

    public void setAutoid(String autoid) {
        this.autoid = autoid;
    }

    public String getShys() {
        return shys;
    }

    public void setShys(String shys) {
        this.shys = shys;
    }
}
