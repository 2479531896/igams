package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.ExpressDto;
import com.matridx.igams.wechat.dao.entities.ExpressModel;

import java.util.List;

public interface IExpressService extends BaseBasicService<ExpressDto, ExpressModel> {
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
