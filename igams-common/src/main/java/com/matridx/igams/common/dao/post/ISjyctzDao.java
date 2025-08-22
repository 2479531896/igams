package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.SjyctzDto;
import com.matridx.igams.common.dao.entities.SjyctzModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISjyctzDao extends BaseBasicDao<SjyctzDto, SjyctzModel>{

    /* 获取用户列表 */
    List<SjyctzDto> getYhjsList(SjyctzDto sjyctzDto);

    /**
     * 批量新增
     */
    boolean insertList(List<SjyctzDto> list);

}
