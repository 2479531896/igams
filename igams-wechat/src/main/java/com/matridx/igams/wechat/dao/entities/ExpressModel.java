package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="ExpressModel")
public class ExpressModel extends BaseModel {
    private static final long serialVersionUID = 1L;
    //送检快递ID
    private String sjkdid;
    //快递单号
    private String mailno;
    //送检ID
    private String sjid;
    //快递发出时间
    private String starttime;
    //快递签收时间
    private String endtime;
    //快递状态，0为未签收 1已签收
    private String kdzt;

    public String getSjkdid() {
        return sjkdid;
    }

    public void setSjkdid(String sjkdid) {
        this.sjkdid = sjkdid;
    }

    public String getMailno() {
        return mailno;
    }

    public void setMailno(String mailno) {
        this.mailno = mailno;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getKdzt() {
        return kdzt;
    }

    public void setKdzt(String kdzt) {
        this.kdzt = kdzt;
    }
}
