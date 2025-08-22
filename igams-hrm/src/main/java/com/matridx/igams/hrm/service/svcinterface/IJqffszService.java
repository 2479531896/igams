package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.JqffszDto;
import com.matridx.igams.hrm.dao.entities.JqffszModel;
import java.util.List;

/**
 * @author:JYK
 */
public interface IJqffszService extends BaseBasicService<JqffszDto, JqffszModel> {
    /**
     * @description 新增保存假期发放设置
     */
    boolean addSaveVacationGrant(JqffszDto jqffszDto) throws BusinessException;
    /**
     * @description 获取可发放假期设置
     */
    List<JqffszDto> getListWithDistribute(JqffszDto jqffszDto);

    //如果是当年最后一天 清空ljed(累计额度),nxzyed(年限转移额度)
    boolean updateToNull(JqffszDto jqffszDto);
    /*
        修改假期发放设置累计额度
     */
    boolean updateLjeds(List<JqffszDto> jqffszDtos);
}
