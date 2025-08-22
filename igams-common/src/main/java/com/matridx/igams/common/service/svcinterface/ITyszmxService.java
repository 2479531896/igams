package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.TyszmxDto;
import com.matridx.igams.common.dao.entities.TyszmxModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;

public interface ITyszmxService extends BaseBasicService<TyszmxDto, TyszmxModel>{

    /**
     * 批量新增
     */
     boolean insertList(List<TyszmxDto> list);

    /**
     * 删除废弃数据
     */
     boolean delAbandonedData(TyszmxDto tyszmxDto);

    /**
     * 获取表中全部数据
     */
     List<TyszmxDto> getAllList();

    /**
     * 根据fnrid获取数据
     * @param tyszmxDto
     * @return
     */
    List<TyszmxDto>getListByFnrid(TyszmxDto tyszmxDto);

    /**
     * 通用获取请求后台的高级筛选需要串入一个key
     * @param tyszmxDto
     * @return
     */
    List<Map<String,String>> getCommonGjsx(TyszmxDto tyszmxDto);
}
