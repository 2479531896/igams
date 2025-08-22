package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.WbsjxxDto;
import com.matridx.igams.wechat.dao.entities.WbsjxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IWbsjxxDao extends BaseBasicDao<WbsjxxDto, WbsjxxModel>{

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
     * 根据sjid查询外部送检信息
     * @param sjid
     * @return
     */
    WbsjxxDto getDtoBySjid(String sjid);
    /**
     * 根据sjid查询外部送检信息List
     * @param sjid
     * @return
     */
    List<WbsjxxDto> getListBySjid(String sjid);

    int copyUpdate(WbsjxxDto wbsjxxDto);

    int updateInfojsonBySjid(WbsjxxDto wbsjxxDto);

    int updateList(List<WbsjxxDto> wbsjxxs);
}
