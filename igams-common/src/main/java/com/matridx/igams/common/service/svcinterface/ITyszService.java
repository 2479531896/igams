package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.TyszDto;
import com.matridx.igams.common.dao.entities.TyszModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface ITyszService extends BaseBasicService<TyszDto, TyszModel>{


    /**
     * 获取资源菜单信息
     */
     List<TyszDto> getMenuList(TyszDto tyszDto);
    /**
     * 新增保存
     */
     boolean addSaveGeneralSetting(TyszDto tyszDto);
    /**
     * 修改保存
     */
     boolean modSaveGeneralSetting(TyszDto tyszDto);
    /**
     * 删除
     */
     boolean delGeneralSetting(TyszDto tyszDto);

    /**
     * 获取资源按钮信息
     */
     List<TyszDto> getButtonList(TyszDto tyszDto);
}
