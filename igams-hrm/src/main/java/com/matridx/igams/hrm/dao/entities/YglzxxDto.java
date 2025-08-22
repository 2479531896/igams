package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Alias(value = "YglzxxDto")
public class YglzxxDto extends YglzxxModel {
    //分布式标记
    private String prefix;
    //员工离职明细
    private String yglzmx_json;
    //附件ID复数
    private List<String> fjids;
    //附件标记
    private String fjbj;
    //附件
    List<FjcfbDto> fjcfbDtos;

    public List<String> getFjids() {
        return fjids;
    }

    public void setFjids(List<String> fjids) {
        this.fjids = fjids;
    }

    public String getFjbj() {
        return fjbj;
    }

    public void setFjbj(String fjbj) {
        this.fjbj = fjbj;
    }

    public List<FjcfbDto> getFjcfbDtos() {
        return fjcfbDtos;
    }

    public void setFjcfbDtos(List<FjcfbDto> fjcfbDtos) {
        this.fjcfbDtos = fjcfbDtos;
    }

    public String getYglzmx_json() {
        return yglzmx_json;
    }

    public void setYglzmx_json(String yglzmx_json) {
        this.yglzmx_json = yglzmx_json;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private static final long serialVersionUID = 1L;
}
