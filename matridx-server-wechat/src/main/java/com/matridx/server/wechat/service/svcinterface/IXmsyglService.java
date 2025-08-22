package com.matridx.server.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.XmsyglDto;
import com.matridx.server.wechat.dao.entities.XmsyglModel;

import java.util.List;

public interface IXmsyglService extends BaseBasicService<XmsyglDto, XmsyglModel>{


    /**
     * 删除
     * @param xmsyglDto
     * @return
     */
    Boolean delInfo(XmsyglDto xmsyglDto);

    /**
     * 更新删除标记
     * @param xmsyglDto
     * @return
     */
    Boolean modToNormal(XmsyglDto xmsyglDto);

    /**
     * del删除
     * @param xmsyglDto
     * @return
     */
    Boolean deleteInfo(XmsyglDto xmsyglDto);

    /**
     * 批量插入
     * @param list
     * @return
     */
    Boolean insertList(List<XmsyglDto> list);

    /**
     * Rabbit同步
     * @param list
     * @return
     */
    void syncRabbitMsg(List<XmsyglDto> list);
    /**
     * 批量更新
     * @param list
     * @return
     */
    boolean updateList(List<XmsyglDto> list);

}
