package com.matridx.igams.hrm.dao.entities;


import org.apache.ibatis.type.Alias;

@Alias(value="KqqjxxDto")
public class KqqjxxDto extends KqqjxxModel {
    private String qjlxmc;
    private String rqstart;
    private String rqend;
    private String qjzsc;//当天请假总时长
    private String yhrq;//用户id和日期进行拼接

    public String getYhrq() {
        return yhrq;
    }

    public void setYhrq(String yhrq) {
        this.yhrq = yhrq;
    }

    public String getQjzsc() {
        return qjzsc;
    }

    public void setQjzsc(String qjzsc) {
        this.qjzsc = qjzsc;
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

    public String getQjlxmc() {
        return qjlxmc;
    }

    public void setQjlxmc(String qjlxmc) {
        this.qjlxmc = qjlxmc;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
