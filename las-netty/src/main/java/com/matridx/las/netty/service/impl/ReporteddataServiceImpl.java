package com.matridx.las.netty.service.impl;


import com.matridx.las.netty.dao.entities.YblcsjDto;
import com.matridx.las.netty.service.svcinterface.IReporteddataService;
import com.matridx.las.netty.service.svcinterface.IYblcsjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReporteddataServiceImpl implements IReporteddataService {

@Autowired
private IYblcsjService yblcsjService;
    @Override
    public boolean reportedYblcsjList(List<YblcsjDto> list) {
        List<YblcsjDto> list1 = yblcsjService.getYsybxxDtoList(list);
        //发送消息

        return true;
    }
}
