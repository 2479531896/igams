package com.matridx.igams.web.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.WbcxDto;
import com.matridx.igams.web.dao.entities.WbcxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IWbcxDao extends BaseBasicDao<WbcxDto, WbcxModel>{
    /**
     *获取名称
     * @return
     */
    List<WbcxDto> getSsgsList();

}
