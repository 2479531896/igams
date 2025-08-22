package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.TyszmxDto;
import com.matridx.igams.common.dao.entities.TyszmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ITyszmxDao extends BaseBasicDao<TyszmxDto, TyszmxModel>{

    /**
     * 批量新增
     * list
     * 
     */
     boolean insertList(List<TyszmxDto> list);
    /**
     * 删除废弃数据
     * tyszmxDto
     * 
     */
     boolean delAbandonedData(TyszmxDto tyszmxDto);

    /**
     * 获取表中全部数据
     * 
     */
     List<TyszmxDto> getAllList();

     List<TyszmxDto> getListByFnrid(TyszmxDto tyszmxDto) ;

    List<Map<String,String>> getAllJgList();

    List<Map<String,String>> getAllWbcxList();
    List<Map<String,String>> getCrmFilterData(Map<String,Object> map);

    List<Map<String,String>> getAllYblc();

    List<Map<String,String>> getAllYbzlc();
}
