package com.matridx.igams.hrm.service.impl;


import com.matridx.igams.common.service.BaseBasicServiceImpl;

import com.matridx.igams.hrm.dao.entities.KsmxDto;
import com.matridx.igams.hrm.dao.entities.KsmxModel;
import com.matridx.igams.hrm.dao.post.IKsmxDao;
import com.matridx.igams.hrm.service.svcinterface.IKsmxService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KsmxServiceImpl extends BaseBasicServiceImpl<KsmxDto, KsmxModel, IKsmxDao> implements IKsmxService {
    /**
     * 选项列表
     */
    public List<KsmxDto> getDtoList(){
        return dao.getDtoList();
    }

    /**
     * 根据选项代码获取对应的选项内容
     */
    public KsmxDto getXxnrByXxdm(KsmxDto ksmxDto){
        return dao.getXxnrByXxdm(ksmxDto);
    }
    /**
     * 根据tkid删除对应选项
     */
    public void deleteByTkid(KsmxDto ksmxDto){
        dao.deleteByTkid(ksmxDto);
    }

    /**
     * 批量新增
     */
    public boolean insertList(List<KsmxDto> ksmxDtos){
        return dao.insertList(ksmxDtos);
    }

}
