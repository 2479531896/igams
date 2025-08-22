package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.BfplszDto;
import com.matridx.igams.wechat.dao.entities.BfplszModel;

import java.util.List;

public interface IBfplszService extends BaseBasicService<BfplszDto, BfplszModel> {


    /**
     * 批量新增
     */
    List<BfplszDto> getPagedToBeVisitedList(BfplszDto bfplszDto);
    /**
     * 保存
     */
   boolean SaveFrequencySetting(BfplszDto bfplszDto);
    /**
     * 判断并更改拜访标记（公共方法）
     */
    public boolean determineAndModVisitMarker(BfplszDto bfplszDto) throws Exception;
}
