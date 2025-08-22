package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.BfdxlxrglDto;
import com.matridx.igams.wechat.dao.entities.BfdxlxrglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IBfdxlxrglDao extends BaseBasicDao<BfdxlxrglDto,BfdxlxrglModel>{

    /**
     * 批量新增
     */
    boolean insertList(List<BfdxlxrglDto> list);
    /**
     * 批量修改
     */
    boolean updateList(List<BfdxlxrglDto> list);
    /**
     * 合并
     */
    boolean mergeList(List<BfdxlxrglDto> list);
}
