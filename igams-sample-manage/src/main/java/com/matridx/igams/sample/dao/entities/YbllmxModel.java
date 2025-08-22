package com.matridx.igams.sample.dao.entities;


import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "YbllmxModel")
public class YbllmxModel extends BaseModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5689078615884532070L;
	//领料明细id
    private String llmxid;
    //领料id
    private String llid;
    //样本库存id
    private String ybkcid;

    public String getLlmxid() {
        return llmxid;
    }

    public void setLlmxid(String llmxid) {
        this.llmxid = llmxid;
    }

    public String getYbkcid() {
        return ybkcid;
    }

    public void setYbkcid(String ybkcid) {
        this.ybkcid = ybkcid;
    }

    public String getLlid() {
        return llid;
    }

    public void setLlid(String llid) {
        this.llid = llid;
    }
}
