package com.matridx.igams.bioinformation.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.bioinformation.dao.entities.DlfxjgDto;
import com.matridx.igams.bioinformation.dao.entities.DlfxjgModel;

import java.util.List;

@Mapper
public interface IDlfxjgDao extends BaseBasicDao<DlfxjgDto, DlfxjgModel>{

    /**
     * 批量插入毒力分析结果数据
     */
    boolean insertList(List<DlfxjgDto> dlfxjglist);

    /***
     * 根据文库测序id和bbh
     */
    List<DlfxjgDto> getDtoListByWkcxId(DlfxjgDto dlfxjgDto);
    /**
     * 毒力结果导出
     */
    List<DlfxjgDto> getListForExp(DlfxjgDto dlfxjgDto);
}
