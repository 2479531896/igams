package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.MxxxDto;
import com.matridx.igams.wechat.dao.entities.MxxxModel;

public interface IMxxxService extends BaseBasicService<MxxxDto, MxxxModel>{

    /**
     * 生成订单号
     * @param
     * @return
     */
    String generateDdh();

    /**
     * 插入mxxx数据
     * @param
     * @return
     */
    boolean insertInto(MxxxModel mxxxModel);

    /**
     * 更改mxxx数据
     * @param
     * @return
     */
    boolean updateInfo(MxxxModel mxxxModel);

    /**
     * 根据订单查询你信息
     */
    MxxxDto getDtoByOrderNo(MxxxDto mxxxDto);

    void sendMxMessage(String mxid);

}
