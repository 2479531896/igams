package com.matridx.server.wechat.dao.entities;


import org.apache.ibatis.type.Alias;

/**
 * 送检异常统计dto
 */
@Alias(value="SjycStatisticsWechatDto")
public class SjycStatisticsWechatDto extends SjycStatisticsWechatModel {

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
