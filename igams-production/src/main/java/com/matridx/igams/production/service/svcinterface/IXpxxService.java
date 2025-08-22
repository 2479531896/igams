package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.XpxxDto;
import com.matridx.igams.production.dao.entities.XpxxModel;

import java.util.Map;

public interface IXpxxService extends BaseBasicService<XpxxDto, XpxxModel>{

    /**
     * 新增芯片信息
     */
    boolean addSaveChipinfo(XpxxDto xpxxDto);
    /**
     * 修改芯片信息
     */
    boolean modSaveChipinfo(XpxxDto xpxxDto);

    void getChipQcScoreAndSave(Map<String,Object> map);
}
