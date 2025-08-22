package com.matridx.igams.production.service.svcinterface;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.CljlxxDto;
import com.matridx.igams.production.dao.entities.CljlxxModel;
import com.matridx.igams.production.dao.entities.ShfkdjDto;

import java.util.List;

public interface ICljlxxService extends BaseBasicService<CljlxxDto, CljlxxModel> {
    //保存售后评论
    boolean saveComment(ShfkdjDto shfkdjDto) throws BusinessException;
    /**
     * 获取处理记录数据
     */
    List<CljlxxDto> getRecords(CljlxxDto cljlxxDto);
}
