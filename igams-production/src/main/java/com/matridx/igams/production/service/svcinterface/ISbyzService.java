package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.dao.entities.SbyzDto;
import com.matridx.igams.production.dao.entities.SbyzModel;

import java.text.ParseException;
import java.util.List;

/**
 * @author:JYK
 */
public interface ISbyzService extends BaseBasicService<SbyzDto, SbyzModel> {
    /**
     * 审核列表
     */
    List<SbyzDto> getPagedAuditTesting(SbyzDto sbyzDto);
    /**
     * 验证保存
     */
    boolean checkingSaveEquipmentAcceptance(SbysDto sbysDto,SbyzDto sbyzDto, User user) throws BusinessException, ParseException;
    /**
     * 删除保存
     */
    boolean delSaveDeviceTest(SbyzDto sbyzDto,String flag);
    List<SbyzDto> getDepartmentList(SbyzDto sbyzDto);
    /**
     * 获取管理人员
     */
    List<SbyzDto> getGlryList();
}
