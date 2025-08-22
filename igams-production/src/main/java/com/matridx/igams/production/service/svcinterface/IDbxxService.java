package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.DbxxDto;
import com.matridx.igams.production.dao.entities.DbxxModel;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface IDbxxService extends BaseBasicService<DbxxDto, DbxxModel> {
    boolean insertList(List<DbxxDto> dbxxDtoList);
}
