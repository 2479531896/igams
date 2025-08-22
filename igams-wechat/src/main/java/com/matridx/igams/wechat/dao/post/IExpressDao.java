package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.ExpressDto;
import com.matridx.igams.wechat.dao.entities.ExpressModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IExpressDao extends BaseBasicDao<ExpressDto, ExpressModel> {

    /**
     * 获取快递信息
     * @param expressDto
     * @return
     */
    List<ExpressDto> getPagedDtoList(ExpressDto expressDto);
    /**
     * 获取物流信息
     * @param expressDto
     * @return
     */
    List<ExpressDto> getKdxx(ExpressDto expressDto);
}
