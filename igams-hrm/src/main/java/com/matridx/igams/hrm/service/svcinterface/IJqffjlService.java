package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.JqffjlDto;
import com.matridx.igams.hrm.dao.entities.JqffjlModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface IJqffjlService extends BaseBasicService<JqffjlDto, JqffjlModel> {
    /**
     * @description 新增保存假期发放记录
     */
    boolean addSaveVacationRecord(JqffjlDto jqffjlDto) throws BusinessException;
    /**
     * @description 确认保存假期发放记录ONE
     */
    boolean confirmSaveOne(JqffjlDto jqffjlDto) throws BusinessException;
    /**
     * @description 确认保存假期发放记录TWO
     */
    boolean confirmSaveTwo(JqffjlDto jqffjlDto) throws BusinessException;
    /**
     * @description 获取所有年度
     */
    List<String> getAllNd();
    /**
     * @description 删除假期发放记录
     */
    boolean delVacationRecord(JqffjlDto jqffjlDto) throws BusinessException;
    /**
     * @description 不一致后 确认删除假期发放记录
     */
    boolean confirmDelVacationRecord(JqffjlDto jqffjlDto) throws BusinessException;
}
