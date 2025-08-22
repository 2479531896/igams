package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.ZsxxDto;
import com.matridx.igams.bioinformation.dao.entities.ZsxxModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;


public interface IZsxxService extends BaseBasicService<ZsxxDto, ZsxxModel>{
    /**
     * 根据ids查询list
     */
    List<ZsxxDto> getDtoListByIds(List<String> ids,String wkcxid);

    /**
     * 删除
     */
    boolean deleteDto(ZsxxDto zsxxDto);

}
