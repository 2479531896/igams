package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "DockerModel")
public class DockerModel extends BaseModel {


    private String dockerid;//		dockerid
    private String jgid;//		结果id
    private String inspect;//		容器详情
    private String logs;//		日志
    private String zt;//		状态 0 运行中  1运行结束   2异常


    public String getDockerid() {
        return dockerid;
    }

    public void setDockerid(String dockerid) {
        this.dockerid = dockerid;
    }

    public String getJgid() {
        return jgid;
    }

    public void setJgid(String jgid) {
        this.jgid = jgid;
    }

    public String getInspect() {
        return inspect;
    }

    public void setInspect(String inspect) {
        this.inspect = inspect;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
