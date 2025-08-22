package com.matridx.igams.common.dao.entities;


import org.apache.ibatis.type.Alias;

/**
 * 送检异常统计dto
 */
@Alias(value="SjycStatisticsDto")
public class SjycStatisticsDto extends SjycStatisticsModel{

    /**
	 * 
	 */
	private static final long serialVersionUID = -71911666090941998L;
	/**
     * 主键ids 查询用
     */
    private String statisticsids;

    public String getStatisticsids() {
        return statisticsids;
    }

    public void setStatisticsids(String statisticsids) {
        this.statisticsids = statisticsids;
    }
}
