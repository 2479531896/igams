package com.matridx.igams.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.FjsqyyDto;
import com.matridx.igams.wechat.dao.entities.FjsqyyModel;

import java.util.List;

@Mapper
public interface IFjsqyyDao extends BaseBasicDao<FjsqyyDto, FjsqyyModel>{

    /**
     * 保存复检原因信息
     * @param list
     * @return
     */
    boolean addDtoList(List<FjsqyyDto> list);
}
