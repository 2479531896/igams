package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="ExpressDto")
public class ExpressDto extends ExpressModel{
    private static final long serialVersionUID = 1L;

    //标本编号
    private String ybbh;
    //接收时间
    private String accepttime;
    //备注
    private String remark;
    //快递发出开始时间
    private String kdfcstart;
    //快递发出结束时间
    private String kdfcend;
    //快递接收开始时间
    private String kdjsstart;
    //快递接收结束时间
    private String kdjsend;

    public String getKdfcstart() {
        return kdfcstart;
    }

    public void setKdfcstart(String kdfcstart) {
        this.kdfcstart = kdfcstart;
    }

    public String getKdfcend() {
        return kdfcend;
    }

    public void setKdfcend(String kdfcend) {
        this.kdfcend = kdfcend;
    }

    public String getKdjsstart() {
        return kdjsstart;
    }

    public void setKdjsstart(String kdjsstart) {
        this.kdjsstart = kdjsstart;
    }

    public String getKdjsend() {
        return kdjsend;
    }

    public void setKdjsend(String kdjsend) {
        this.kdjsend = kdjsend;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAccepttime() {
        return accepttime;
    }

    public void setAccepttime(String accepttime) {
        this.accepttime = accepttime;
    }

    public String getYbbh() {
        return ybbh;
    }

    public void setYbbh(String ybbh) {
        this.ybbh = ybbh;
    }
}
