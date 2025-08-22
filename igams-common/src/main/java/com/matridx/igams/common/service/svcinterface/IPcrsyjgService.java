package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.PcrsyjgDto;
import com.matridx.igams.common.dao.entities.PcrsyjgModel;
import com.matridx.igams.common.dao.entities.WkmxPcrModel;

import java.util.List;
import java.util.Map;

public interface IPcrsyjgService extends BaseBasicService<PcrsyjgDto, PcrsyjgModel>{
 boolean savePcrsyjgDto(WkmxPcrModel wkmxPcrModel) ;

    /**
     * 通过角色ID获得检测单位信息
     */
    List<Map<String, String>> getJsjcdwByjsid(String dqjs);
}
