package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxjgModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISjxxResStatisticDao extends BaseBasicDao<SjxxDto, SjxxjgModel>
{
    /**
     * 统计ResFirst™当日情况
     * @param
     * @return
     */
    Map<String, String> getRYbStatisByDay(SjxxDto sjxxDto);
    /**
     * ResFirst™日报统计
     * @return
     */
    List<Map<String, String>> getRYbxxByDay(SjxxDto sjxxDto);
    /**
     * 送检复检实验数
     * @param
     * @return
     */
    Map<String, Integer> getSjFjNum(SjxxDto sjxxDto);
    /**
     * @description 获取各个周期平均用时
     * @param sjxxDto
     * @return
     */
    List<Map<String, String>> getAvgTime(SjxxDto sjxxDto);
    /**
     * @description 获取各个周期个数
     * @param sjxxDto
     * @return
     */
    Map<String, String>  getLifeCount(SjxxDto sjxxDto);
    /**
     * @description 获取生命周期
     * @param sjxxDto
     * @return
     */
    List<Map<String, String>> getLifeCycle(SjxxDto sjxxDto);
    /**
     * @description 各周期标本用时
     * @param sjxxDto
     * @return
     */
    List<Map<String, String>> getLifeTimeCount(SjxxDto sjxxDto);
}
