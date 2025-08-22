package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.WbaqyzDto;
import com.matridx.igams.common.dao.entities.WbaqyzModel;
import java.util.List;

@Mapper
public interface IWbaqyzDao extends BaseBasicDao<WbaqyzDto, WbaqyzModel>{
    /**
     * 查询列表信息
     */
    List<WbaqyzDto> getPageWbaqyzDtoList(WbaqyzDto wbaqyzDto);

    /**
     * 根据伙伴id获取伙伴安全验证
     */
    List<WbaqyzDto> queryByHbid(String hbid);


    int updateDtoByIndex(WbaqyzDto wbaqyzDto);

}
