package com.matridx.las.netty.service.impl;

import com.matridx.igams.common.enums.YesNotEnum;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.global.CommandParmGlobal;
import com.matridx.las.netty.service.svcinterface.StopAndTimeoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StopAndTimeoutServiceImpl implements StopAndTimeoutService {

    private static final Logger logger = LoggerFactory.getLogger(StopAndTimeoutServiceImpl.class);


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean stopAndTimeOut(String type) {
        //做成记录一条命令,当暂停时，记录此命令，不发送
        try {
            //暂停 
            if(InstrumentStatusEnum.STATE_PAUSE.equals(type)){
                //将状态改为暂停
                CommandParmGlobal.setIsSuspend(YesNotEnum.YES.getCode());
                //发送消息将所有的正在运行的仪器停止

            }else{

            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }
}
