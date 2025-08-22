package com.matridx.igams.wechat.service.impl;



import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.ExpressDto;
import com.matridx.igams.wechat.dao.entities.ExpressModel;
import com.matridx.igams.wechat.dao.post.IExpressDao;
import com.matridx.igams.wechat.service.svcinterface.IExpressService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ExpressServiceImpl extends BaseBasicServiceImpl<ExpressDto, ExpressModel, IExpressDao> implements IExpressService {
    /**
     * 获取快递信息
     * @param expressDto
     * @return
     */
    public List<ExpressDto> getPagedDtoList(ExpressDto expressDto){
        return dao.getPagedDtoList(expressDto);
    }

    /**
     * 获取物流信息
     * @param expressDto
     * @return
     */
    public List<ExpressDto> getKdxx(ExpressDto expressDto){
        return dao.getKdxx(expressDto);
    }
}
