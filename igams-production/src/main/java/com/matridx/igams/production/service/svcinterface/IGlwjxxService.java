package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.GlwjxxDto;
import com.matridx.igams.production.dao.entities.GlwjxxModel;

import java.util.List;

public interface IGlwjxxService extends BaseBasicService<GlwjxxDto, GlwjxxModel>{
    /**
     * 新增关联文件信息
     */
    void insertGlwjxxDtos(List<GlwjxxDto> glwjxxDtos);
}
