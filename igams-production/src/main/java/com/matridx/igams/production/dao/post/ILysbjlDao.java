package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface ILysbjlDao extends BaseBasicDao<LysbjlDto, LysbjlModel> {
    /**
     * @description 获取留样设备group数据
     */
    List<LysbjlDto> getDtoListGroup(LysbjlDto lysbjlDto);
}
