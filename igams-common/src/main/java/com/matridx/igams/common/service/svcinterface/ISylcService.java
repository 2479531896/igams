package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.SylcDto;
import com.matridx.igams.common.dao.entities.SylcModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;

public interface ISylcService extends BaseBasicService<SylcDto, SylcModel>{

    /**
     * 获取系统菜单
     */
    List<Map<String,Object>> getMenuList(Map<String,Object> map);
    /**
     * 获取系统操作代码
     */
    List<Map<String,Object>> getButtonList(Map<String,Object> map);
    /**
     * 新增保存
     */
    boolean addSaveHomepageProcess(SylcDto sylcDto);
    /**
     * 修改保存
     */
    boolean modSaveHomepageProcess(SylcDto sylcDto);
    /**
     * 删除
     */
    boolean delHomepageProcess(SylcDto sylcDto);
    /**
     * 获取首页展示数据
     */
    List<SylcDto> getAllData();
}
