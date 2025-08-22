package com.matridx.igams.wechat.service.svcinterface;


import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SysxxDto;
import com.matridx.igams.wechat.dao.entities.SysxxModel;

import java.util.List;

public interface ISysxxService extends BaseBasicService<SysxxDto, SysxxModel> {
    /**
     * 根据对方实验室名称获取实验室信息
     * @param dfsysmc
     * @return
     */
    SysxxDto getSysxxDtoByDfsysmc(String dfsysmc);

    /**
     * 获取列表数据
     */
    List<SysxxDto> getPageSysxxDtoList(SysxxDto sysxxDto);
}
