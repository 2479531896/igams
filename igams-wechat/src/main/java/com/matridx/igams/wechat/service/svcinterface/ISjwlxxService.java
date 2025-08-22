package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjpdglDto;
import com.matridx.igams.wechat.dao.entities.SjwlxxDto;
import com.matridx.igams.wechat.dao.entities.SjwlxxModel;

import java.util.List;
import java.util.Map;

public interface ISjwlxxService extends BaseBasicService<SjwlxxDto, SjwlxxModel>{
    /**
     * 送达录入保存
     * @param sjwlxxDto
     * @return
     */
    boolean arriveSaveLr(SjwlxxDto sjwlxxDto, User user);
	
    /**
     * 根据wlid获取送检物流信息
     * @param sjwlxxDto
     * @return
     */
    SjwlxxDto getSjwlxxDtoById(SjwlxxDto sjwlxxDto);

    /**
     * 取件保存
     * @param sjwlxxDto
     * @return
     */
    boolean pickUpSaveInfo(SjwlxxDto sjwlxxDto,User user) throws RuntimeException;

    /**
     * 取消接单
     * @param sjwlxxDto
     * @return
     */
    boolean cancelReceipt(SjwlxxDto sjwlxxDto,String SfTD);

    /**
     * 获取物流信息
     * @param sjwlxxDto
     * @return
     */
    List<SjwlxxDto> getWlxxList(SjwlxxDto sjwlxxDto);

    /**
     * 接单保存
     * @param sjwlxxDto
     * @return
     */
    void orderLrSave(SjwlxxDto sjwlxxDto, SjpdglDto sjpdglDto, String s);

    /**
     * 获取派单信息
     * @return
     */
    Map<String,Object> generateWlxx(SjwlxxDto sjwlxxDto);

    /**
     * 统计标本运输时间
     * @param map
     * @return
     */
    List<Map<String,Object>> getTransportationTimeListByRq(Map<String,Object> map);
    /**
     * @Description TODO 
     * @Param [sjwlxxDto]
     * @Return java.util.List<java.lang.String>
     */
    List<SjwlxxDto> getSjwlxxsByJsfsAndYh(SjwlxxDto sjwlxxDto);
    /**
     * 根据送检团单id获取物流信息
     *
     * @return
     */
	 List<SjwlxxDto> getDtoListById(String sjtdid);
    /**
     * 根据送检团单id获取物流信息
     */
    List<SjwlxxDto> getSjwlxxsBySjtdid(SjwlxxDto sjwlxxDto);
}
