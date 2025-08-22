package com.matridx.igams.storehouse.service.svcinterface;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.WlxxDto;
import com.matridx.igams.storehouse.dao.entities.WlxxModel;

import java.util.List;


public interface IWlxxService extends BaseBasicService<WlxxDto, WlxxModel>{

    boolean logisticsupholdSave(WlxxDto wlxxDto) throws BusinessException;

    List<WlxxDto> getDtoListById(String ywid);

    boolean signforSave(WlxxDto wlxxDto) throws BusinessException;

    boolean signforconfirmSave(WlxxDto wlxxDto) throws BusinessException;
}
