package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.MxtzglDto;
import com.matridx.igams.wechat.dao.entities.MxtzglModel;

import java.util.List;

public interface IMxtzglService extends BaseBasicService<MxtzglDto, MxtzglModel>{
    /**
     * 发送通知
     * @param
     * @return
     */
    boolean sendMessage(MxtzglDto mxtzglDto);
    /**
     * 多条添加通知记录
     * @param list
     * @return
     */
    boolean insertList(List<MxtzglDto> list);

    /**
     * 获取镁信通知信息
     * @param mxtzglDto
     * @return
     */
    List<MxtzglDto> getListById(MxtzglDto mxtzglDto);

}
