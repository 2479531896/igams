package com.matridx.igams.hrm.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.KqqjxxDto;
import com.matridx.igams.hrm.dao.entities.KqqjxxModel;
import com.matridx.igams.hrm.dao.post.IKqqjxxDao;
import com.matridx.igams.hrm.service.svcinterface.IKqqjxxService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KqqjxxServiceImpl extends BaseBasicServiceImpl<KqqjxxDto, KqqjxxModel, IKqqjxxDao> implements IKqqjxxService {
    /**
     * 更新出勤退勤信息
     */
    public boolean updateSj(KqqjxxDto kqqjxxDto){
        return dao.updateSj(kqqjxxDto);
    }
    /**
     * 获取用户请假信息
     */
    public KqqjxxDto getSpid(KqqjxxDto kqqjxxDto){
        return dao.getSpid(kqqjxxDto);
    }
    /**
     * 获取请假信息
     */
    public List<KqqjxxDto> viewLeaveNews(KqqjxxDto kqqjxxDto){
        return dao.viewLeaveNews(kqqjxxDto);
    }

    /**
     * 删除信息
     */
    public boolean delQjxx(KqqjxxDto kqqjxxDto){
        return dao.delQjxx(kqqjxxDto);
    }

    @Override
    public List<KqqjxxDto> getDtoListForKsAndJs(KqqjxxDto kqqjxxDto) {
        return dao.getDtoListForKsAndJs(kqqjxxDto);
    }
}
