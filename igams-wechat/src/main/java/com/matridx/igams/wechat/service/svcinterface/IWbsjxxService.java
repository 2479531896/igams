package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.WbsjxxDto;
import com.matridx.igams.wechat.dao.entities.WbsjxxModel;

import java.util.List;

public interface IWbsjxxService extends BaseBasicService<WbsjxxDto, WbsjxxModel> {

    /**
     * 查询相似样本
     * @param wbsjxxDto
     * @return
     */
    List<WbsjxxDto> getSimilarDtoList(WbsjxxDto wbsjxxDto);
    /**
     * 根据外部编码查询送检信息
     * @param wbsjxxDto
     * @return
     */
    List<WbsjxxDto> getListByWbbm(WbsjxxDto wbsjxxDto);

    /**
     * 根据lastwbbm获取送检报告
     * @param wbsjxxDto
     * @return
     */
    List<WbsjxxDto> getListByLastWbbm(WbsjxxDto wbsjxxDto);
    /**
     * 根据sjid和jcxm查询外部送检信息
     * @param wbsjxxDto
     * @return
     */
    WbsjxxDto getDtoBySjidAndJcxm(WbsjxxDto wbsjxxDto);
    /**
     * 根据sjid查询外部送检信息List
     * @param sjid
     * @return
     */
    List<WbsjxxDto> getListBySjid(String sjid);



    /**
     * 数据合并复制wbsjxx得sjid
     * @param sjid1
     * @param sjid2
     * @param user
     */
    void copyWbsjxx(String sjid1 ,String sjid2, User user);

	int updateInfojsonBySjid(WbsjxxDto wbsjxxDto);

    int updateList(List<WbsjxxDto> wbsjxxs);
	
	WbsjxxDto getDtoBySjid(String sjid);

}
