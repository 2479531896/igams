package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.JcsqmxDto;
import com.matridx.igams.wechat.dao.entities.JcsqmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IJcsqmxDao extends BaseBasicDao<JcsqmxDto, JcsqmxModel>{

    /**
     * 批量新增
     * @param list
     * @return
     */
    boolean insertList(List<JcsqmxDto> list);

    /**
     * 验证标本
     * @param jcsqmxDto
     * @return
     */
    List<JcsqmxDto> verifySamples(JcsqmxDto jcsqmxDto);

}
