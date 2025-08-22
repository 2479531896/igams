package com.matridx.igams.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjwlxxDto;
import com.matridx.igams.wechat.dao.entities.SjwlxxModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISjwlxxDao extends BaseBasicDao<SjwlxxDto, SjwlxxModel>{


    /**
     * 根据wlid获取送检物流信息
     * @param sjwlxxDto
     * @return
     */
    SjwlxxDto getSjwlxxDtoById(SjwlxxDto sjwlxxDto);

    /**
     * 获取物流信息
     * @param sjwlxxDto
     * @return
     */
    List<SjwlxxDto> getWlxxList(SjwlxxDto sjwlxxDto);

    /**
     * 根据送检派单ID取第一条物流信息数据（上级物流ID为空）
     * @param sjwlxxDto_t
     * @return
     */
    SjwlxxDto getWlxxBySjpdidAndSjwlid(SjwlxxDto sjwlxxDto_t);

    /**
     * 统计标本运输时间
     * @param map
     * @return
     */
    List<Map<String,Object>> getTransportationTimeListByRq(Map<String,Object> map);
    /**
     * @Description TODO 
     * @Param [sjwlxxDto]
     * @Return java.util.List<com.matridx.igams.wechat.dao.entities.SjwlxxDto>
     */
    List<SjwlxxDto> getSjwlxxsByJsfsAndYh(SjwlxxDto sjwlxxDto);
    List<SjwlxxDto> getDtoListByIds(List<String> ids);

    int updateWlxx(SjwlxxDto sjwlxxDto_t);

	/**
     * 根据送检团单id获取物流信息
     */
    List<SjwlxxDto> getSjwlxxsBySjtdid(SjwlxxDto sjwlxxDto);

    int updateTd(SjwlxxDto sjwlxxDto_d);
    /**
     *
     * 取消接单时将接单时间和接单人置为空
     */
    boolean updateJdrAndJdsj(String wlid);
    /**
     * @description 获取取件时间
     * @param wlid
     * @return
     */
    SjwlxxDto getQjsj(String wlid);
}
