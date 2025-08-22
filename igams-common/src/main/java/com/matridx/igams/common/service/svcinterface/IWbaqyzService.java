package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.WbaqyzDto;
import com.matridx.igams.common.dao.entities.WbaqyzModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface IWbaqyzService extends BaseBasicService<WbaqyzDto, WbaqyzModel>{
    /**
     * 获取页面数据
     */
    List<WbaqyzDto> getPageWbaqyzDtoList(WbaqyzDto wbaqyzDto);
    /**
     * 根据伙伴id获取伙伴安全验证
     */
    List<WbaqyzDto> queryByHbid(String hbid);


     boolean updateDtoByIndex(WbaqyzDto wbaqyzDto);



}
