package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.GlwjxxDto;
import com.matridx.igams.production.dao.entities.GlwjxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IGlwjxxDao extends BaseBasicDao<GlwjxxDto, GlwjxxModel>{

    /**
     * @description 新增关联文件信息
     */
    void insertGlwjxxDtos(List<GlwjxxDto> glwjxxDtos);
}
