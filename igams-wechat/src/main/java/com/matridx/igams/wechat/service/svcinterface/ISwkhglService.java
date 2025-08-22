package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SwkhglDto;
import com.matridx.igams.wechat.dao.entities.SwkhglModel;

import java.util.List;


public interface ISwkhglService extends BaseBasicService<SwkhglDto, SwkhglModel>{
    /**
     * 查询是否重复数据
     */
    List<SwkhglDto> getRepeatDtoList(SwkhglDto swkhglDto);

    /**
     * 更新对账字段
     * @param swkhglDto
     * @return
     */
    boolean updateDzzd(SwkhglDto swkhglDto);
}
