package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.YszkmxDto;
import com.matridx.igams.wechat.dao.entities.YszkmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface IYszkmxDao extends BaseBasicDao<YszkmxDto, YszkmxModel> {

    /**
     * 批量插入
     */
    int insertList(List<YszkmxDto> list);
    /**
     * 查询对账明细
     * @param params
     * @return
     */
    List<Map<String,Object>> getDtoListOptimize(Map<String, Object> params);
}

