package com.matridx.igams.hrm.service.svcinterface;


import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.KsmxDto;
import com.matridx.igams.hrm.dao.entities.KsmxModel;


import java.util.List;


public interface IKsmxService extends BaseBasicService<KsmxDto, KsmxModel> {

    /**
     * 选项列表
     */
    List<KsmxDto> getDtoList();
    /**
     * 根据选项代码获取对应的选项内容
     */
     KsmxDto getXxnrByXxdm(KsmxDto ksmxDto);
    /**
     * 根据tkid删除对应选项
     */
    void deleteByTkid(KsmxDto ksmxDto);

    /**
     * 批量新增
     */
    boolean insertList(List<KsmxDto> ksmxDtos);
}
