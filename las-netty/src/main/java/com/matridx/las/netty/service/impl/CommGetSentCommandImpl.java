package com.matridx.las.netty.service.impl;

import com.matridx.las.netty.channel.command.BaseCommand;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.MlpzDto;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.las.netty.dao.post.IMlpzDao;
import com.matridx.las.netty.dao.post.ISjlcDao;
import com.matridx.las.netty.enums.MedicalRobotProcessEnum;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.service.svcinterface.ICommGetSentCommand;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommGetSentCommandImpl implements ICommGetSentCommand {
@Autowired
private IMlpzDao mlpzDao;
@Autowired
private ISjlcDao sjlcDao;
    /**
     * 获取发送命令的list
     */
    public List<SjlcDto> getEventList(String sjlc){
        SjlcDto sjlcDto = new SjlcDto();
        sjlcDto.setLclx(sjlc);
        //获取流程里的事件
        List<SjlcDto> sjlcDtoList = sjlcDao.getSjlcList(sjlcDto);
        if(sjlcDtoList!=null&&sjlcDtoList.size()>0){
            return sjlcDtoList;
        }
        return null;
    }
    //调用发送命令
    public boolean getCommandList(SjlcDto sjlcDto, String theadName, BaseCommand bsaeCommand, Map<String, String> ma){
        //是否需要释放占用或者释放仪器，机器人

        MlpzDto  mlpzDto = new MlpzDto();
        mlpzDto.setSjid(sjlcDto.getSjid());

        //获取流程里的事件
        List<MlpzDto> mlpzDtoList = mlpzDao.getMlpzList(mlpzDto);
        // 将list的消息都发出去
        if (mlpzDtoList == null) {
            return false;
        }
        if (mlpzDtoList.size() == 0) {
            return false;
        }
        String yqid = "";
        String jqrid = "";
        //获取发送的历史仪器的map
        if (ma == null) {
            ma = new HashMap<String, String>();
        }

        try {
            if (sjlcDto.getSfzyyq().equals("1")&&StringUtil.isNotBlank(sjlcDto.getZyyqlx())) {
                // 判断并获取仪器
                yqid = bsaeCommand.getDeviceIdByCommd(sjlcDto.getZyyqlx());
                ma.put(sjlcDto.getZyyqlx(), yqid);
            }
            if (sjlcDto.getSfzyjqr().equals("1")) {
                // 判断并获取仪器
                jqrid = bsaeCommand.getFreeRobot();
                ma.put(Command.AGV.toString(), jqrid);
            }
            //调用下一步
            //如果是第一次调用，则将map放入。后面加入机器人也加入其中（这里的基本逻辑是，运行中的机器，只会出现在一个map中，这里的map只记录还没有归还的）
            //这里的逻辑是：因为在交互过程中会有三个仪器，在第三个仪器结束后，必须要找到前面那个仪器继续运行，所以就需要记录下来一个流程里到底用过哪些仪器
            /*
             * 这里记录的逻辑是：1,在每个流程开始的地方，这里使用MedicalRobotProcessEnum的第一步来判断，添加map（协议：仪器id）。
             * 通过发送的消息类来传递，如果不传递，当仪器运行完释放掉，又会新加入map中，到时候就找不到map了。这里释放掉时，要移除map中的仪器。但是仪器主动发来消息时是没发传递map的
             * 当仪器主动发来消息时，仪器移动没被释放掉，所以可以在通过仪器找到对应的map。所有流程结束后需要删除map
             */
            if (sjlcDto.getLclx().equals(MedicalRobotProcessEnum.MEDICAL_ROBOT_MOVETOLIBRARY.getCode())) {
                InstrumentStateGlobal.setInstrumentUsedList(ma);
            }
            if (sjlcDto.getLclx().equals(MedicalRobotProcessEnum.MEDICAL_ROBOT_MOVETODISPENSER.getCode())) {
                InstrumentStateGlobal.setInstrumentUsedList(ma);
            }

        } catch (Exception e) {
            // new ExceptionHandlingUtil(jsonObject2);
            //log.error(e.getMessage());
            return false;

        }

        return true;
    }
}
