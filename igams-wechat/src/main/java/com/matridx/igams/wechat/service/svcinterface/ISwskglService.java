package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SwskglDto;
import com.matridx.igams.wechat.dao.entities.SwskglModel;

public interface ISwskglService extends BaseBasicService<SwskglDto, SwskglModel> {

    /**
     * 获取总金额
     */
    String getTotalAmount(String yszkid);
    /**
     * 删除
     */
    boolean delBusinessReceipts(SwskglDto swskglDto);

}
