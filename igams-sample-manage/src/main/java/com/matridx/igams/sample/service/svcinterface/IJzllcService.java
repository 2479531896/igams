package com.matridx.igams.sample.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.JzllcDto;
import com.matridx.igams.sample.dao.entities.JzllcModel;

import java.util.List;

/**
 * {@code @author:JYK}
 */
public interface IJzllcService extends BaseBasicService<JzllcDto, JzllcModel> {
    /**
     * 领料车列表
     */
    List<JzllcDto> getLlcDtoList(JzllcDto jzllcDto);
    /**
     * 删除
     */
    boolean deleteByRyid(String ryid);
}
