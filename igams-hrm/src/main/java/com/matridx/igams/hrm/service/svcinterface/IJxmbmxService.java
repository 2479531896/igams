package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.JxmbmxDto;
import com.matridx.igams.hrm.dao.entities.JxmbmxModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface IJxmbmxService extends BaseBasicService<JxmbmxDto, JxmbmxModel> {
    /**
     * 获取绩效明细
     */
    List<JxmbmxDto> getJxmbmxList(JxmbmxDto jxmbmxDto);

    /**
     * 批量插入模板明细
     */
    boolean insertMbmxList(List<JxmbmxDto> list);
    /**
     * 根据绩效模板id删除绩效模板明细
     */
    boolean deleteByJxmbid(String jxmbid);
    /**
     * @description 获取树结构个人绩效明细数据
     */
    List<JxmbmxDto> getNewDtoList(List<JxmbmxDto> jxmbmxDtos);
}
