package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.SjyctzDto;
import com.matridx.igams.common.dao.entities.SjyctzModel;
import com.matridx.igams.common.dao.post.ISjyctzDao;
import com.matridx.igams.common.service.svcinterface.ISjyctzService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SjyctzServiceImpl extends BaseBasicServiceImpl<SjyctzDto, SjyctzModel, ISjyctzDao> implements ISjyctzService{

    /* 获取用户列表 */
    public List<SjyctzDto> getYhjsList(SjyctzDto sjyctzDto){
        return dao.getYhjsList(sjyctzDto);
    }

    /**
     * 批量新增
     */
    public boolean insertList(List<SjyctzDto> list){
        return dao.insertList(list);
    }

}
