package com.matridx.igams.crm.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XsxlglDto")
public class XsxlglDto extends XsxlglModel{

    private String rqstart;
    private String rqend;
    private String dylx;//对应类型
    private String rqlx;//日期类型
    private String khid;//客户id
    private String rqY;//日期年

    private String entire;//查询内容

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getRqY() {
        return rqY;
    }

    public void setRqY(String rqY) {
        this.rqY = rqY;
    }

    public String getRqlx() {
        return rqlx;
    }

    public void setRqlx(String rqlx) {
        this.rqlx = rqlx;
    }

    public String getDylx() {
        return dylx;
    }

    public void setDylx(String dylx) {
        this.dylx = dylx;
    }

    public String getRqstart() {
        return rqstart;
    }

    public void setRqstart(String rqstart) {
        this.rqstart = rqstart;
    }

    public String getRqend() {
        return rqend;
    }

    public void setRqend(String rqend) {
        this.rqend = rqend;
    }

    public String getKhid() {
        return khid;
    }

    public void setKhid(String khid) {
        this.khid = khid;
    }
}
