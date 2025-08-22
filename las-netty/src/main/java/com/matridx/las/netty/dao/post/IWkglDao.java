package com.matridx.las.netty.dao.post;



import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.netty.dao.entities.WkglDto;
import com.matridx.las.netty.dao.entities.WkglModel;

@Mapper
public interface IWkglDao extends BaseBasicDao<WkglDto, WkglModel>{
    /**
     * 根据文库id返回信息
     * @param wkglDto
     * @return
     */
    public WkglDto getWkglDtoBywkid(WkglDto wkglDto) ;

    int insertWkgl(WkglDto wkglDto) ;
}
