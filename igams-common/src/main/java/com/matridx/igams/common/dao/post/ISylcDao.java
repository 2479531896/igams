package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.SylcDto;
import com.matridx.igams.common.dao.entities.SylcModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISylcDao extends BaseBasicDao<SylcDto, SylcModel>{
    /**
     * 获取系统菜单
     */
    List<Map<String,Object>> getMenuList(Map<String,Object> map);
    /**
     * 获取系统操作代码
     */
    List<Map<String,Object>> getButtonList(Map<String,Object> map);
    /**
     * 获取首页展示数据
     */
    List<SylcDto> getAllData();


}
