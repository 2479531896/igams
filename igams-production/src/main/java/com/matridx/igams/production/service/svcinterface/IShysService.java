package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.ShysDto;
import com.matridx.igams.production.dao.entities.ShysModel;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface IShysService extends BaseBasicService<ShysDto, ShysModel> {
    boolean insertDtoList(List<ShysDto> shysDtoList);
    boolean updateDispose(ShysDto shysDto);
}
