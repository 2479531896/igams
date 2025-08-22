package com.matridx.igams.sample.dao.entities;


import org.apache.ibatis.type.Alias;
import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YbllcModel")
public class YbllcModel extends BaseModel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3107029254892411597L;
	//人员id
    private String ryid;
    //样本库存id
    private String ybkcid;

    public String getRyid() {
        return ryid;
    }

    public void setRyid(String ryid) {
        this.ryid = ryid;
    }

    public String getYbkcid() {
        return ybkcid;
    }

    public void setYbkcid(String ybkcid) {
        this.ybkcid = ybkcid;
    }

}
