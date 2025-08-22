package com.matridx.las.home.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.las.home.service.svcinterface.IGlobalprocessService;
import com.matridx.las.netty.channel.command.BaseCommand;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.ICallBack;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.FrameModelToHtml;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.las.netty.enums.GlobalrouteEnum;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.util.CommonChannelUtil;
import com.matridx.las.netty.util.SpringUtil;
import com.matridx.las.netty.util.snapshot.SnapShotManagementGlobal;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class GlobalprocessServiceImpl implements IGlobalprocessService {
    @Autowired
    private SnapShotManagementGlobal snapShotManagementGlobal;
    private static final Logger logger = LoggerFactory.getLogger(GlobalprocessServiceImpl.class);

    /**
     * 获取所有的流程信息
     *
     * @return
     */
    @Override
    public Map<String, Object> showGlobalprocessDate() {
        Map<String, Object> map = new HashMap<>();
        //封装空闲队列
        List<String> listCubs = snapShotManagementGlobal.getQueceRange("CubisGroup");
        List<String> listAgv = snapShotManagementGlobal.getQueceRange("lassGroup");
        List<String> listSeq = snapShotManagementGlobal.getQueceRange("SeqGroup");
        List<String> listAuto = snapShotManagementGlobal.getQueceRange("AutoGroup");
        List<String> listPcr = snapShotManagementGlobal.getQueceRange("PcrGroup");
        List<String> listChm = snapShotManagementGlobal.getQueceRange("CmhGroup");
        List<Map<String, Object>> listQuen = new ArrayList<>();
        Map<String, Object> mapCubs = new HashMap<>();
        Map<String, Object> mapAgv = new HashMap<>();
        Map<String, Object> mapSeq = new HashMap<>();
        Map<String, Object> mapAuto = new HashMap<>();
        Map<String, Object> mapPcr = new HashMap<>();
        Map<String, Object> mapChm = new HashMap<>();
        mapCubs.put("dllx", "1(建库仪)");
        mapCubs.put("dlz", listCubs);
        mapAgv.put("dllx", "5(机器人)");
        mapAgv.put("dlz", listAgv);
        mapAuto.put("dllx", "2(配置仪)");
        mapAuto.put("dlz", listAuto);
        mapPcr.put("dllx", "3(荧光定量仪)");
        mapPcr.put("dlz", listPcr);
        mapChm.put("dllx", "6(加盖机)");
        mapChm.put("dlz", listChm);
        mapSeq.put("dllx", "4(测序仪)");
        mapSeq.put("dlz", listSeq);
        listQuen.add(mapAgv);
        listQuen.add(mapAuto);
        listQuen.add(mapChm);
        listQuen.add(mapPcr);
        listQuen.add(mapCubs);
        listQuen.add(mapSeq);
        //流程的InstrumentUsedList值
        List<Object> list2 = snapShotManagementGlobal.getInstrumentUsedListAll();
        List<Map<String, Object>> listUsedList = new ArrayList<>();
        int i = 0;
        for (Object o : list2) {
            Map<String, Object> mapN = new HashMap<>();
            mapN.put("xh", i);
            mapN.put("map", o);
            listUsedList.add(mapN);
            i++;
        }
        // 编写流程对像封装
        List<Map<String, Object>> listBase = snapShotManagementGlobal.getAllCommand();
        map.put("queuelist", listQuen);
        map.put("baselist", listBase);
        map.put("usedlist", listUsedList);
        map.put("message", "操作成功");
        map.put("status", "success");
        logger.info(JSONObject.toJSONString(map));
        return map;
    }

    /**
     * 保存InstrumentUsedList到redis
     *
     * @param list
     * @return
     */
    @Override
    public Map<String, Object> saveInstrumentUsedList(List<Map<String,String>> list) {
        Map<String, Object> mapResult = new HashMap<>();
        if (list != null && list.size() > 0) {
            Map<String,String> map = new HashMap<>();
            for(Map<String,String> ma:list){
                String key = "";
                String value ="";
                for(String s:ma.keySet()){
                    if("key".equals(s)) {
                        key = ma.get(s);
                    }
                    if("value".equals(s)) {
                        value=ma.get(s);
                    }
                }
                if(StringUtil.isNotBlank(key)&&StringUtil.isNotBlank(value)){
                    map.put(key,value);
                }
            }
            try {
                String id = map.get("id");
               if(id==null||StringUtil.isBlank(id.toString())){
                   mapResult.put("message", "此条信息得id为空");
                   mapResult.put("status", "fail");
                   return mapResult;
               }
             boolean result =   InstrumentStateGlobal.modInstrumentUsedListMap(map);
                if(!result){
                    mapResult.put("message", "未找到信息，不能修改");
                    mapResult.put("status", "fail");
                    return mapResult;
                }

            } catch (Exception e) {
                mapResult.put("message", e.getMessage());
                mapResult.put("status", "fail");
                return mapResult;
            }

        }
        mapResult.put("message", "操作成功");
        mapResult.put("status", "success");
        return mapResult;
    }

    /**
     * 保存空闲队列到redis
     *
     * @param list
     * @return
     */
    @Override
    public Map<String, Object> saveFreeQueue(List<Map<String,String>> list) {
        Map<String, Object> mapResult = new HashMap<>();
        if (list != null) {
            try {
                List<String> list1 = new ArrayList<>();
                String comm = "";
                String commQueId = "";
                for (Map<String,String> cm : list) {
                    for(String k:cm.keySet()){
                        String va = cm.get(k);
                        if (StringUtil.isNotBlank(va)&&!va.contains("(")) {
                           list1.add(va);
                        }else{
                            comm = va;
                        }
                    }

                }
                if (StringUtil.isNotBlank(comm)) {
                    String group = "";
                    List<String> listQue = new ArrayList<>();
                    if (comm.contains(Command.CUBICS.toString())) {//建库仪
                        comm = Command.CUBICS.toString();
                        listQue = snapShotManagementGlobal.getQueceRange("CubisGroup");
                    } else if (comm.contains(Command.AUTO.toString())) {
                        comm = Command.AUTO.toString();
                        listQue = snapShotManagementGlobal.getQueceRange("AutoGroup");
                    } else if (comm.contains(Command.PCR.toString())) {
                        comm = Command.PCR.toString();
                        listQue = snapShotManagementGlobal.getQueceRange("PcrGroup");
                    } else if (comm.contains(Command.CMH.toString())) {
                        comm = Command.CMH.toString();
                        listQue = snapShotManagementGlobal.getQueceRange("CmhGroup");
                    } else if (comm.contains(Command.AGV.toString())) {
                        comm = Command.AGV.toString();
                        listQue = snapShotManagementGlobal.getQueceRange("lassGroup");
                    } else if (comm.contains(Command.SEQ.toString())) {
                        comm = Command.SEQ.toString();
                        listQue = snapShotManagementGlobal.getQueceRange("SeqGroup");
                    }
                    //删除
                    for (String cm : listQue) {
                        if (!list1.contains(cm)) {
                            InstrumentStateGlobal.removeYqQueuesFromRedis1(comm, cm);
                        }
                    }
                    //添加
                    for (String cm : list1) {
                        if (!listQue.contains(cm)) {
                            InstrumentStateGlobal.putInstrumentQueuesToRedis(comm, cm);
                        }
                    }

                } else {
                    mapResult.put("message", "未找到保存仪器类型");
                    mapResult.put("status", "fail");
                    return mapResult;
                }

            } catch (Exception e) {
                mapResult.put("message", e.getMessage());
                mapResult.put("status", "fail");
                return mapResult;
            }

        }
        mapResult.put("message", "操作成功");
        mapResult.put("status", "success");
        return mapResult;
    }



    /**
     * 删除命令信息
     * @param id
     * @param threadName
     * @param mlid
     * @return
     */
    @Override
    public Map<String, Object> removeCmd(String id, String threadName,String mlid) {
        Map<String, Object> mapResult = new HashMap<>();
        if (StringUtils.isBlank(id)) {
            mapResult.put("message", "id为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        if (StringUtils.isBlank(threadName)) {
            mapResult.put("message", "threadName为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        List<BaseCommand> baseCommandList = CommonChannelUtil.getLcBaseCommandList();
        BaseCommand baseCommand = null;
        for (BaseCommand b : baseCommandList) {
            if (b.getUnid().equals(id)) {
                baseCommand = b;
                break;
            }
        }

        if (baseCommand != null) {
            Map<String, BlockingQueue<FrameModel>> cmdDic = baseCommand.getCmdDic();
            if (cmdDic != null && cmdDic.keySet().size() > 0) {
                BlockingQueue<FrameModel> frameModels = cmdDic.get(threadName);
                if (frameModels != null && frameModels.size() > 0) {
                    Boolean isHave = false;
                    for (FrameModel frameModel1 : frameModels) {
                        if (mlid.equals(frameModel1.getMlid())) {
                            frameModels.remove(frameModel1);
                            isHave = true;
                            break;
                        }
                    }
                    if(!isHave){
                        mapResult.put("message", "已发送得消息不能删除");
                        mapResult.put("status", "success");
                        return mapResult;
                    }
                }else{
                    mapResult.put("message", "未找到命令消息不能删除");
                    mapResult.put("status", "success");
                    return mapResult;
                }
            }
        }else{
            mapResult.put("message", "未找到命令消息不能删除");
            mapResult.put("status", "success");
            return mapResult;
        }
        mapResult.put("message", "操作成功");
        mapResult.put("status", "success");
        return mapResult;
    }

    /**
     * 发送单个消息
     *
     * @param id
     * @param frameModel
     * @return
     */
    @Override
    public Map<String, Object> sendCmd(String id, FrameModel frameModel) {
        Map<String, Object> mapResult = new HashMap<>();
        try {
            if (frameModel != null && StringUtil.isNotBlank(id.toString())) {
                snapShotManagementGlobal.sendMessageAgain(frameModel, id.toString());
            }

        } catch (Exception e) {
            mapResult.put("message", e.getMessage());
            mapResult.put("status", "fail");
            return mapResult;
        }


        mapResult.put("message", "操作成功");
        mapResult.put("status", "success");
        return mapResult;
    }


    /**
     * 发送命令
     */
    @Override
    public Map<String, Object> sendMessageBythreadname(String id, String threadName) {
        Map<String, Object> mapResult = new HashMap<>();
        try {
            if (StringUtil.isNotBlank(threadName) && StringUtil.isNotBlank(id.toString())) {
                boolean reslut =  snapShotManagementGlobal.sendMessageBythreadname(threadName, id.toString());
                if(!reslut){
                    mapResult.put("message", "保存失败");
                    mapResult.put("status", "fail");
                    return mapResult;
                }
            }

        } catch (Exception e) {
            mapResult.put("message", e.getMessage());
            mapResult.put("status", "fail");
            return mapResult;
        }


        mapResult.put("message", "操作成功");
        mapResult.put("status", "success");
        return mapResult;
    }

    /**
     * 命令修改
     *
     * @param frameModelToHtml
     * @return
     */
    @Override
    public Map<String, Object> modCmdDic( FrameModelToHtml frameModelToHtml) {

        Map<String, Object> mapResult = new HashMap<>();
        String id= frameModelToHtml.getId();
        String threadName= frameModelToHtml.getThreadName();
        mapResult.put("message", "操作成功");
        mapResult.put("status", "success");
        if (StringUtils.isBlank(id)) {
            mapResult.put("message", "id为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        if (StringUtils.isBlank(threadName)) {
            mapResult.put("message", "threadName为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        List<BaseCommand> baseCommandList = CommonChannelUtil.getLcBaseCommandList();
        BaseCommand baseCommand = null;
        for (BaseCommand b : baseCommandList) {
            if (b.getUnid().equals(id)) {
                baseCommand = b;
                break;
            }
        }
        if (baseCommand != null) {
            BlockingQueue<FrameModel> cmdblockingQueue = baseCommand.getCmdDic().get(threadName);
            if (cmdblockingQueue != null && cmdblockingQueue.size() > 0) {
                boolean isHave = false;
                for (FrameModel frameModel : cmdblockingQueue) {
                    if (frameModelToHtml.getMlid().equals(frameModel.getMlid())) {
                        BeanUtils.copyProperties(frameModelToHtml, frameModel);
                        //将回调函数复制
                        String callBackName = frameModelToHtml.getCallFunc();
                        if (StringUtil.isNotBlank(callBackName)) {
                            ICallBack callBack = (ICallBack) SpringUtil.getBean(callBackName);
                            frameModel.setCallFunc(callBack);
                        }
                        isHave = true;
                        break;
                    }

                }
                if(!isHave){
                    mapResult.put("message", "已发送得消息不能删除");
                    mapResult.put("status", "success");
                    return mapResult;
                }
            }

        }
        return mapResult;
    }

    /**
     * 命令新增
     *
     * @param frameModelToHtml
     * @return
     */
    @Override
    public Map<String, Object> addCmdDic( FrameModelToHtml frameModelToHtml) {

        Map<String, Object> mapResult = new HashMap<>();
        String id= frameModelToHtml.getId();
        String threadName= frameModelToHtml.getThreadName();
        if (StringUtils.isBlank(id)) {
            mapResult.put("message", "id为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        if (StringUtils.isBlank(threadName)) {
            mapResult.put("message", "threadName为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        List<BaseCommand> baseCommandList = CommonChannelUtil.getLcBaseCommandList();
        BaseCommand baseCommand = null;
        for (BaseCommand b : baseCommandList) {
            if (b.getUnid().equals(id)) {
                baseCommand = b;
                break;
            }
        }
        if (baseCommand != null) {
            BlockingQueue<FrameModel> cmdblockingQueue = baseCommand.getCmdDic().get(threadName);
            if (cmdblockingQueue == null) {
                mapResult.put("message", "队列不存在");
                mapResult.put("status", "fail");
                return mapResult;
            }
            FrameModel frameModel = new FrameModel();
            BeanUtils.copyProperties(frameModelToHtml, frameModel);
            //将回调函数复制
            String callBackName = frameModelToHtml.getCallFunc();
            if (StringUtil.isNotBlank(callBackName)) {
                ICallBack callBack = (ICallBack) SpringUtil.getBean(callBackName);
                frameModel.setCallFunc(callBack);
            }
            //为了把新加得放在最前面
            BlockingQueue<FrameModel> cmdblockingQueueNew = new LinkedBlockingQueue<>();
            cmdblockingQueueNew.add(frameModel);
            cmdblockingQueueNew.addAll(cmdblockingQueue);
            baseCommand.getCmdDic().put(threadName,cmdblockingQueueNew);
        }
        mapResult.put("message", "操作成功");
        mapResult.put("status", "success");
        return mapResult;
    }



    @Override
    public Map<String, Object> modQueueIndex(String id, String threadName, String type) {
        Map<String, Object> mapResult = new HashMap<>();
        logger.info("id:"+id+",threadName:"+threadName+",type:"+type);
        if (StringUtils.isBlank(id)) {
            mapResult.put("message", "id为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        if (StringUtils.isBlank(threadName)) {
            mapResult.put("message", "threadName为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        Map<String, Object> map = snapShotManagementGlobal.getCmdAndEvenListBythreadName(id, threadName);
        if (type.equals(GlobalrouteEnum.CMD_SEND_TYPE_CMD.getCode())) {
            Object cmdDicO = map.get("cmdDic");
            List<Object> cmdDic = new ArrayList<Object>();
            if (cmdDicO != null) {
                cmdDic = (List) cmdDicO;
            }
            Object haveSendcmdDicO = map.get("haveSendcmdDic");
            List<Object> haveSendcmdDic = new ArrayList<Object>();
            if (haveSendcmdDicO != null) {
                haveSendcmdDic = (List) haveSendcmdDicO;
            }
            haveSendcmdDic.addAll(cmdDic);

            mapResult.put("cmdDic", haveSendcmdDic);
        } else if (type.equals(GlobalrouteEnum.CMD_SEND_TYPE_EVEN.getCode())) {
            Object eventDicO = map.get("eventDic");
            List eventDic = new ArrayList();
            if (eventDicO != null) {
                eventDic = (List) eventDicO;
            }
            Object haveSendeventDicO = map.get("haveSendeventDic");
            List haveSendeventDic = new ArrayList();
            if (haveSendeventDicO != null) {
                haveSendeventDic = (List) haveSendeventDicO;
            }
            haveSendeventDic.addAll(eventDic);

            mapResult.put("eventDic", haveSendeventDic);
        }
        mapResult.put("message", "操作成功");
        mapResult.put("status", "success");
        return mapResult;
    }

    public Map<String, Object> showCmdEvenList(String id, String threadName, String type) {
        Map<String, Object> mapResult = new HashMap<>();
        if (StringUtils.isBlank(id)) {
            mapResult.put("message", "id为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        if (StringUtils.isBlank(threadName)) {
            mapResult.put("message", "threadName为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        Map<String, Object> map = snapShotManagementGlobal.getCmdAndEvenListBythreadName(id, threadName);
        if (type.equals(GlobalrouteEnum.CMD_SEND_TYPE_CMD.getCode())) {
            mapResult.put("haveSendcmdDic", map.get("haveSendcmdDic"));
        } else if (type.equals(GlobalrouteEnum.CMD_SEND_TYPE_EVEN.getCode())) {
            mapResult.put("eventDic", map.get("eventDic"));
        }
        mapResult.put("message", "操作成功");
        mapResult.put("status", "success");
        return mapResult;
    }
    @Override
    public Map<String, Object> removeEventDic(String id, String threadName, String lcid) {
        Map<String, Object> mapResult = new HashMap<>();
        if (StringUtils.isBlank(id)) {
            mapResult.put("message", "id为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        if (StringUtils.isBlank(threadName)) {
            mapResult.put("message", "threadName为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        if (StringUtils.isBlank(lcid)) {
            mapResult.put("message", "lcid为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        List<BaseCommand> baseCommandList = CommonChannelUtil.getLcBaseCommandList();
        BaseCommand baseCommand = null;
        for (BaseCommand b : baseCommandList) {
            if (b.getUnid().equals(id)) {
                baseCommand = b;
                break;
            }
        }
        if (baseCommand != null) {
            Map<String, BlockingQueue<SjlcDto>> eventDic = baseCommand.getEventDic();
            if (eventDic != null && eventDic.keySet().size() > 0) {
                BlockingQueue<SjlcDto> sjlcDtoL = eventDic.get(threadName);
                if (sjlcDtoL != null && sjlcDtoL.size() > 0) {
                    for (SjlcDto sjlcDto1 : sjlcDtoL) {
                        if (lcid.equals(sjlcDto1.getLcid())) {
                            sjlcDtoL.remove(sjlcDto1);
                            mapResult.put("message", "操作成功");
                            mapResult.put("status", "success");
                            return mapResult;
                        }

                    }
                }

            }
        }
        mapResult.put("message", "操作失败");
        mapResult.put("status", "fail");
        return mapResult;


    }

    @Override
    public Map<String, Object> modEventDic(String id, String threadName, SjlcDto sjlcDto) {
        Map<String, Object> mapResult = new HashMap<>();
        if (StringUtils.isBlank(id)) {
            mapResult.put("message", "id为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        if (StringUtils.isBlank(threadName)) {
            mapResult.put("message", "threadName为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        List<BaseCommand> baseCommandList = CommonChannelUtil.getLcBaseCommandList();
        BaseCommand baseCommand = null;
        for (BaseCommand b : baseCommandList) {
            if (b.getUnid().equals(id)) {
                baseCommand = b;
                break;
            }
        }
        if (baseCommand != null) {
            Map<String, BlockingQueue<SjlcDto>> eventDic = baseCommand.getEventDic();
            if (eventDic != null && eventDic.keySet().size() > 0) {
                BlockingQueue<SjlcDto> sjlcDtoL = eventDic.get(threadName);
                if (sjlcDtoL != null && sjlcDtoL.size() > 0) {
                    for (SjlcDto sjlcDto1 : sjlcDtoL) {
                        if (sjlcDto.getLcid().equals(sjlcDto1.getLcid())) {
                            BeanUtils.copyProperties(sjlcDto1, sjlcDto);
                            break;
                        }

                    }
                }

            }
        }
        mapResult.put("message", "操作成功");
        mapResult.put("status", "success");
        return mapResult;
    }
    @Override
    public Map<String, Object> addEventDic(String id, String threadName, SjlcDto sjlcDto) {
        Map<String, Object> mapResult = new HashMap<>();
        if (StringUtils.isBlank(id)) {
            mapResult.put("message", "id为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        if (StringUtils.isBlank(threadName)) {
            mapResult.put("message", "threadName为空");
            mapResult.put("status", "fail");
            return mapResult;
        }
        List<BaseCommand> baseCommandList = CommonChannelUtil.getLcBaseCommandList();
        BaseCommand baseCommand = null;
        for (BaseCommand b : baseCommandList) {
            if (b.getUnid().equals(id)) {
                baseCommand = b;
                break;
            }
        }
        if (baseCommand != null) {
            Map<String, BlockingQueue<SjlcDto>> eventDic = baseCommand.getEventDic();
            if (eventDic != null && eventDic.keySet().size() > 0) {
                BlockingQueue<SjlcDto> sjlcDtoL = eventDic.get(threadName);
                if (sjlcDtoL != null) {
                    //为了把新加得放在最前面
                    BlockingQueue<SjlcDto> sjlcDtoLNew = new LinkedBlockingQueue<>();
                    sjlcDtoLNew.add(sjlcDto);
                    sjlcDtoLNew.addAll(sjlcDtoL);
                    baseCommand.getEventDic().put(threadName,sjlcDtoLNew);
                    mapResult.put("message", "操作成功");
                    mapResult.put("status", "success");
                    return mapResult;
                }

            }
        }
        mapResult.put("message", "未添加成功");
        mapResult.put("status", "fail");
        return mapResult;

    }

    @Override
    public Map<String, Object> sendEventDicBythreadname(String id, String threadName,String command,String deviceid) {
        Map<String, Object> mapResult = new HashMap<>();
        try {
            if (StringUtil.isNotBlank(threadName) && StringUtil.isNotBlank(id.toString())) {
                boolean reslut =  snapShotManagementGlobal.sendEvenBythreadname(threadName, id.toString(),command,deviceid);
                if(!reslut){
                    mapResult.put("message", "保存失败");
                    mapResult.put("status", "fail");
                    return mapResult;
                }
            }

        } catch (Exception e) {
            mapResult.put("message", e.getMessage());
            mapResult.put("status", "fail");
            return mapResult;
        }


        mapResult.put("message", "操作成功");
        mapResult.put("status", "success");
        return mapResult;
    }
}
