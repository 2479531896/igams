package com.matridx.igams.crm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="XsxlglModel")
public class XsxlglModel extends BaseModel {
    private String sjid;
    private String yhid;
    private List<String> sjhbs;

    public List<String> getSjhbs() {
        return sjhbs;
    }

    public void setSjhbs(List<String> sjhbs) {
        this.sjhbs = sjhbs;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }
}
