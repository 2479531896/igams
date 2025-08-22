package com.matridx.las.netty.dosjbackimpl;

import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.global.CubsParmGlobal;
import com.matridx.las.netty.global.RobotManagementGlobal;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.util.CommonChannelUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EventFlowBack {
    static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    /**
     * 此方法用于一个时间结束，归还仪器用
     *
     * @param sendModel
     * @param sjlcDto
     * @return
     */
    public static Map<String, String> returnBackYqandjqr(FrameModel sendModel, SjlcDto sjlcDto) {
        //获取流程仪器的map,建库仪不放入
        Map<String, String> ma = null;
        if (!sendModel.getCommand().toString().equals(Command.CUBICS.toString())) {
            ma = InstrumentStateGlobal.getInstrumentUsedListMap(sendModel.getCommand(), sendModel.getDeviceID());
        }else {
        	ma = InstrumentStateGlobal.getInstrumentUsedListMap(sendModel.getCommand(), sendModel.getPassId());
        }
        if (ma == null) {
          return  null;
        }
        if ("1".equals(sjlcDto.getSfghyq())) {
            if (sjlcDto.getGhyqlx() != null) {

                if (sjlcDto.getGhyqlx().equals(Command.CUBICS.toString())) {
                    String deviceid =  ma.get(sjlcDto.getGhyqlx());

                    InstrumentStateGlobal.changeInstrumentState(sjlcDto.getGhyqlx(),deviceid, InstrumentStatusEnum.STATE_FREE.getCode());
                    //CommonChannelUtil.removeCubs(sendModel.getDeviceID(), sendModel.getPassId());


                    InstrumentStateGlobal.removeInstrumentUsedList_Map(ma,Command.CUBICS.toString(),deviceid);
                    if(deviceid!=null){
                        //CubsParmGlobal.putCubQueues(ma.get(sjlcDto.getGhyqlx()));
                        CubsParmGlobal.putCubQueuesFromRedis(deviceid);
                    }
                    ma.remove(Command.CUBICS.toString());
                } else {
                    String deviceid =  ma.get(sjlcDto.getGhyqlx());
                    InstrumentStateGlobal.changeInstrumentState(sjlcDto.getGhyqlx(),deviceid, InstrumentStatusEnum.STATE_FREE.getCode());
                    //yqztxxService.updateStYqztxx(yqid, InstrumentStatusEnum.STATE_FREE.getCode());
                    //InstrumentStateGlobal.putInstrumentQueues(sjlcDto.getGhyqlx(), ma.get(sjlcDto.getGhyqlx()));
                    InstrumentStateGlobal.removeInstrumentUsedList_Map(ma,sjlcDto.getGhyqlx(),deviceid);
                    InstrumentStateGlobal.putInstrumentQueuesToRedis(sjlcDto.getGhyqlx(),deviceid);
                    ma.remove(sjlcDto.getGhyqlx());

                }
            }
        }
        //机器人归还
        if (sjlcDto.getSfghjqr().equals("1")) {
            //去充电了也要将其从map中移除
            //新启一个线程
            String deviceid =  ma.get(Command.AGV.toString());
            InstrumentStateGlobal.removeInstrumentUsedList_Map(ma,Command.AGV.toString(),deviceid);
            InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.AGV.toString(),deviceid);
			ma.remove(Command.AGV.toString());
        }
        if (ma != null && ma.isEmpty()) {
            InstrumentStateGlobal.removeInstrumentUsetMap(ma);
        }

        return ma;
    }

}