package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.YhqjmxDto;
import com.matridx.igams.hrm.dao.entities.YhqjmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IYhqjmxDao extends BaseBasicDao<YhqjmxDto, YhqjmxModel> {
    /**
     * 批量更新
     */
    boolean updateList(List<YhqjmxDto> list);
    /**
     * 批量插入
     */
    boolean insertList(List<YhqjmxDto> list);
}
