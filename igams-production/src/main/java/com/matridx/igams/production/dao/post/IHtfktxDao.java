package com.matridx.igams.production.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.HtfktxDto;
import com.matridx.igams.production.dao.entities.HtfktxModel;

import java.util.List;

@Mapper
public interface IHtfktxDao extends BaseBasicDao<HtfktxDto, HtfktxModel>{

    /**
     * 获取付款提醒信息
     */
    List<HtfktxDto> getListByHtid(HtfktxDto htfktxDto);

    /**
     * 批量更新合同付款提醒信息
     */
    boolean updateDtoList(List<HtfktxDto> list);

    /**
     * 删除合同付款提醒信息
     */
    boolean delDtoList(HtfktxDto htfktxDto);

    /**
     * 批量新增合同付款提醒信息
     */
    boolean insertDtoList(List<HtfktxDto> list);

    /**
     * 更新付款提醒信息
     */
    boolean updateHtfkid(HtfktxDto htfktxDto);

    /**
     * 更新htfkid为null
     */
    boolean updateHtfkidToNull(HtfktxDto htfktxDto);
    /**
     * 根据htid删除
     */
    void deleteByHtid(HtfktxDto htfktxDto);
}
