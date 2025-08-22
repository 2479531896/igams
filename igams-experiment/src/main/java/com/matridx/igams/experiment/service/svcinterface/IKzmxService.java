package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.KzmxDto;
import com.matridx.igams.experiment.dao.entities.KzmxModel;

import java.util.List;

public interface IKzmxService extends BaseBasicService<KzmxDto, KzmxModel>{

    /**
     * 验证是否存在相同syglid
     */
    KzmxDto verifySame(KzmxDto kzmxDto);
    /**
     * 新增前校验内部编号是否存在
     */
    List<String> checkNbbms(KzmxDto kzmxDto) throws BusinessException;

    /**
     * 新增保存扩增信息
     */
    boolean addSaveAmplification(KzmxDto kzmxDto) throws BusinessException;
    /**
     * 修改保存扩增信息
     */
    boolean modSaveAmplification(KzmxDto kzmxDto) throws BusinessException;

    /**
     * 根据ids移除扩增时间
     */
    boolean removeKzsjByIds(KzmxDto kzmxDto);

}
