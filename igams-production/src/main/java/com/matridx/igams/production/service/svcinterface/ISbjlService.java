package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.SbjlDto;
import com.matridx.igams.production.dao.entities.SbjlModel;
import com.matridx.igams.production.dao.entities.SbysDto;

import java.text.ParseException;
import java.util.List;

/**
 * @author:JYK
 */
public interface ISbjlService extends BaseBasicService<SbjlDto, SbjlModel> {
    /**
     * 获取列表中的部门
     */
    List<SbjlDto> getDepartmentList();
    /**
     * 获取管理人员
     */
    List<SbjlDto> getGlryList();
    /**
     * 审核列表
     */
    List<SbjlDto> getPagedAuditMetering(SbjlDto sbjlDto);
    /**
     * 计量保存
     */
    boolean meteringSaveEquipmentAcceptance(SbysDto sbysDto,SbjlDto sbjlDto) throws BusinessException, ParseException;
    boolean delSaveDeviceMetering(SbjlDto sbjlDto,String flag);
}
