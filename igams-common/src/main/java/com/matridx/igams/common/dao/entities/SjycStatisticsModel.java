package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * 送检异常model
 */
@Alias(value="SjycStatisticsModel")
public class SjycStatisticsModel extends BaseModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8457184864270294940L;
	/**
     * 主键
     */
    private String statisticsid;
    /**
     * 异常id
     */
    private String ycid;
    /**
     * 送检id
     */
    private String sjid;
    /**
     * 基础数据csid
     */
    private String jcsjcsid;

    public String getStatisticsid() {
        return statisticsid;
    }

    public void setStatisticsid(String statisticsid) {
        this.statisticsid = statisticsid;
    }

    public String getYcid() {
        return ycid;
    }

    public void setYcid(String ycid) {
        this.ycid = ycid;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getJcsjcsid() {
        return jcsjcsid;
    }

    public void setJcsjcsid(String jcsjcsid) {
        this.jcsjcsid = jcsjcsid;
    }
}
