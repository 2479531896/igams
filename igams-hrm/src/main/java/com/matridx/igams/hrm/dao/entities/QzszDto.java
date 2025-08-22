package com.matridx.igams.hrm.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author WYX
 * @version 1.0
 * @className CsszDto
 * @description TODO
 * @date 15:30 2023/1/29
 **/
@Alias(value="QzszDto")
public class QzszDto extends QzszModel{
    //岗位名称
    private String gwmc;
    private List<String> jxmbids;

    public List<String> getJxmbids() {
        return jxmbids;
    }
    public void setJxmbids(String jxmbids) {
        List<String> list = new ArrayList<String>();
        if(StringUtil.isNotBlank(jxmbids)) {
            String[] str = jxmbids.split(",");
            list = Arrays.asList(str);
        }
        this.jxmbids = list;
    }
    public void setJxmbids(List<String> jxmbids) {
        if (!CollectionUtils.isEmpty(jxmbids)){
            for(int i=0;i<jxmbids.size();i++){
                jxmbids.set(i,jxmbids.get(i).replace("[", "").replace("]", "").trim());
            }
        }
        this.jxmbids = jxmbids;
    }

    public String getGwmc() {
        return gwmc;
    }

    public void setGwmc(String gwmc) {
        this.gwmc = gwmc;
    }
}
