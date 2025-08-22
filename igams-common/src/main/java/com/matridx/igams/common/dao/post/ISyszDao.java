package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.SyszDto;
import com.matridx.igams.common.dao.entities.SyszModel;

import java.util.List;

@Mapper
public interface ISyszDao extends BaseBasicDao<SyszDto, SyszModel>{
    /**
     *  获取首页要展示的页面
     */
    List<SyszDto> getHomePage(SyszDto syszDto);
    /*
        新增保存首页设置
     */
    boolean insertSyszDtos(List<SyszDto> syszDtos);
}
