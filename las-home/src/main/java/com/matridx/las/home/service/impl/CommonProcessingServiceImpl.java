package com.matridx.las.home.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.las.home.dao.entities.YqxxDto;
import com.matridx.las.home.dao.entities.YqxxModel;
import com.matridx.las.home.dao.post.IYqxxDao;
import com.matridx.las.home.service.svcinterface.ICommonProcessingService;
import com.matridx.las.netty.channel.command.BaseCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CommonProcessingServiceImpl extends BaseBasicServiceImpl<YqxxDto, YqxxModel, IYqxxDao> implements ICommonProcessingService {

    private Logger log = LoggerFactory.getLogger(BaseCommand.class);

    @Override
    public boolean stopOrTimeout(String type) {
        try{}catch (Exception e){
            log.error(e.getMessage());
            return  false;
        }
        return true;
    }
}
