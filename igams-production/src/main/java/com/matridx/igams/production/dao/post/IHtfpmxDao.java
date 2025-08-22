package com.matridx.igams.production.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.HtfpmxDto;
import com.matridx.igams.production.dao.entities.HtfpmxModel;

import java.util.List;

@Mapper
public interface IHtfpmxDao extends BaseBasicDao<HtfpmxDto, HtfpmxModel>{

    /**
     * 合同发票明细保存
     */
    void insertList(List<HtfpmxDto> list);
}
