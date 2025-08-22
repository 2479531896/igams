package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.CkjgxxDto;
import com.matridx.igams.storehouse.dao.entities.CkjgxxModel;

public interface ICkjgxxService extends BaseBasicService<CkjgxxDto, CkjgxxModel>{

    /**
     * 权限设置保存
     */
    boolean batchpermitSaveCkxx(CkjgxxDto ckjgxxDto);

}
