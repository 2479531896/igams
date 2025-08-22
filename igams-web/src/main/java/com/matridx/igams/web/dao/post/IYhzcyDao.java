package com.matridx.igams.web.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.YhzcyDto;
import com.matridx.igams.web.dao.entities.YhzcyModel;

import java.util.List;

@Mapper
public interface IYhzcyDao extends BaseBasicDao<YhzcyDto, YhzcyModel>{
    /**
     * 用户组添加成员
     * @param yhzcyDto
     * @return
     */
    boolean toYhzcySelected(YhzcyDto yhzcyDto);

    /**
     * 用户组添加成员
     * @param yhzcyDto
     * @return
     */
    boolean toYhzcyOptional(YhzcyDto yhzcyDto);

    /**
     * 删除组下的所有成员
     * @param ids
     * @return
     */
    boolean deleteByIds(List<String> ids);
}
