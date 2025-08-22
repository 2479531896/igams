package com.matridx.igams.sample.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.sample.dao.entities.YbllcDto;
import com.matridx.igams.sample.dao.entities.YbllcModel;
import com.matridx.igams.sample.dao.post.IYbllcDao;
import com.matridx.igams.sample.service.svcinterface.IYbllcService;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 *@date 2022年06月13日17:08
 *
 */
@Service
public class YbllcServiceImpl extends BaseBasicServiceImpl<YbllcDto, YbllcModel, IYbllcDao> implements IYbllcService {
    //通过人员id获取领料明细
    @Override
    public List<YbllcDto> getYbLlcDtoList(YbllcDto ybllcDto) {
        return dao.getYbLlcDtoList(ybllcDto);
    }
}
