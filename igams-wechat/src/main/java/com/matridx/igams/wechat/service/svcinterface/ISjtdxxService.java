package com.matridx.igams.wechat.service.svcinterface;


import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjtdxxDto;
import com.matridx.igams.wechat.dao.entities.SjtdxxModel;
import com.matridx.igams.wechat.dao.entities.SjwlxxDto;

public interface ISjtdxxService extends BaseBasicService<SjtdxxDto, SjtdxxModel> {

    boolean modSaveTdxx(SjwlxxDto sjwlxxDto) throws BusinessException;
}
