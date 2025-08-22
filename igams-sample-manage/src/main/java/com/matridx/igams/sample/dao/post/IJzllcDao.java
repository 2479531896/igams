package com.matridx.igams.sample.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.sample.dao.entities.JzllcDto;
import com.matridx.igams.sample.dao.entities.JzllcModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * {@code @author:JYK}
 */
@Mapper
public interface IJzllcDao extends BaseBasicDao<JzllcDto, JzllcModel> {
    /**
     * 领料车列表
     */
    List<JzllcDto> getLlcDtoList(JzllcDto jzllcDto);
    /**
     * 删除
     */
    boolean deleteByRyid(String ryid);
}
