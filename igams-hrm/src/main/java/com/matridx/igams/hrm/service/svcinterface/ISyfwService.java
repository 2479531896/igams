package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.SyfwDto;
import com.matridx.igams.hrm.dao.entities.SyfwModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface ISyfwService extends BaseBasicService<SyfwDto, SyfwModel> {
    /**
     * @description 新增保存适用范围
     */
    boolean addSaveSyfw(SyfwDto syfwDto) throws BusinessException;
    /**
     * @description 批量插入适用范围信息
     */
    boolean insertSyfwDtos(List<SyfwDto> syfwDtos);
    /**
     * @description 批量修改适用范围信息
     */
    boolean updateSyfwDtos(List<SyfwDto> syfwDtos);
    /**
     * @description 获取适用范围信息
     */
    List<SyfwDto> getDtoListByJxmbid(SyfwDto syfwDto);
    /**
     * @description 通过绩效模板id获取适用范围
     */
    List<SyfwDto> getPagedDtoListByJxmbid(SyfwDto syfwDto);
    /**
     * 根据绩效模板ids删除
     */
    void deleteByJxmbids(SyfwDto syfwDto);
}
