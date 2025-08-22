package com.matridx.las.home.dao.entities;

import com.matridx.las.netty.dao.entities.JkywzszDto;
import com.matridx.las.netty.dao.entities.YqxxinfosxDto;
import org.apache.ibatis.type.Alias;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Alias("YqxxInfoDto")
public class YqxxInfoDto extends YqxxInfoModel{

    private String yqid;
    private String yq;
    private String lx;
    private String yqmc;
    // 业务类型
    private String ywlx;
    //附件ID复数
    private List<String> fjids;
    private List<JkywzszDto> jkywzszDtoList;
    private List<YqxxinfosxDto> yqxxinfosxDtos;
    private Map<String,String> yqxxinfosxDtoMap = new HashMap<>();

    public Map<String, String> getYqxxinfosxDtoMap() {
        return yqxxinfosxDtoMap;
    }

    public void setYqxxinfosxDtoMap(Map<String, String> yqxxinfosxDtoMap) {
        this.yqxxinfosxDtoMap = yqxxinfosxDtoMap;
    }

    // 附件ID
    private String fjid;

    @Override
    public String getYqid() {
        return yqid;
    }

    @Override
    public void setYqid(String yqid) {
        this.yqid = yqid;
    }

    public String getYq() {
        return yq;
    }

    public void setYq(String yq) {
        this.yq = yq;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }

    public List<String> getFjids() {
        return fjids;
    }

    public void setFjids(List<String> fjids) {
        this.fjids = fjids;
    }

    public String getFjid() {
        return fjid;
    }

    public void setFjid(String fjid) {
        this.fjid = fjid;
    }

    public String getYqmc() {
        return yqmc;
    }

    public void setYqmc(String yqmc) {
        this.yqmc = yqmc;
    }

    public List<JkywzszDto> getJkywzszDtoList() {
        return jkywzszDtoList;
    }

    public void setJkywzszDtoList(List<JkywzszDto> jkywzszDtoList) {
        this.jkywzszDtoList = jkywzszDtoList;
    }

    public List<YqxxinfosxDto> getYqxxinfosxDtos() {
        return yqxxinfosxDtos;
    }

    public void setYqxxinfosxDtos(List<YqxxinfosxDto> yqxxinfosxDtos) {
        this.yqxxinfosxDtos = yqxxinfosxDtos;
    }

    private static final long serialVersionUID = 1L;
}
