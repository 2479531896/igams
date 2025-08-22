package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.*;

import java.util.List;


public interface IKctbcwxxService extends BaseBasicService<KctbcwxxDto, KctbcwxxModel> {

    /**
     * 新增保存同步库存错误信息
     */
    boolean insertKctbcwxxList(List<KctbcwxxDto> list);
}