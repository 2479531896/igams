package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author WYX
 * @version 1.0
 * @className HbszmxDto
 * @description TODO
 * @date 10:08 2022/12/30
 **/
@Alias(value="HbszmxDto")
public class HbszmxDto extends HbszmxModel{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8393471742248225929L;
	//得分
    private String df;
    //通知群参数扩展1
    private String tzqcskz1;
    public String getTzqcskz1() {
        return tzqcskz1;
    }

    public void setTzqcskz1(String tzqcskz1) {
        this.tzqcskz1 = tzqcskz1;
    }

    public String getDf() {
        return df;
    }

    public void setDf(String df) {
        this.df = df;
    }
}
