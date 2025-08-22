package com.matridx.las.netty.util;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.matridx.las.netty.channel.command.BaseCommand;
import com.matridx.las.netty.dao.entities.CubisUpParamModel;
import com.matridx.las.netty.enums.GlobalrouteEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.IObserver;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.channel.server.handler.MatridxProtocolHandler;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.springboot.util.base.StringUtil;
import io.netty.channel.Channel;

public class CommonChannelUtil {

    private static Logger log = LoggerFactory.getLogger(CommonChannelUtil.class);

    // 暂停标记
    private static String systemState="0";


    public static String getSystemState() {
        return systemState;
    }

    public static void setSystemState(String systemState) {
        CommonChannelUtil.systemState = systemState;
    }
    // 锁对象
    private static Object object = new Object();
    // 通道管理(已注册)
    private static Map<String, ChannelModel> registerChannels = new HashMap<String, ChannelModel>();
    // 建库仪已上线的通到
    private static Map<String, List<String>> registerCubics = new HashMap<String, List<String>>();
    // 通道管理(未注册)
    private static Map<String, Channel> unRegisterChannels = new HashMap<String, Channel>();
    // 协议类信息
    public static Map<String, MatridxProtocolHandler<?>> protocols = new HashMap<String, MatridxProtocolHandler<?>>();
    // 通道管理(已经注册的)
    private static Map<String, Channel> doRegisterChannels = new HashMap<String, Channel>();
    //添加流程管理存的是basecommand队对像,即每启动一个流程就放进来，流程结束清除
    private static List<BaseCommand> lcBaseCommandList = new ArrayList<>();
//    //读取到的帧队列(一次返回)
//    protected static Map<String,BlockingQueue<FrameModel>> recvQueues = new HashMap<>();
//    //读取到的帧队列(二次返回)
//    protected static Map<String,BlockingQueue<FrameModel>> secRecvQueues = new HashMap<>();

    /**
     * 新增协议信息处理类
     *
     * @param type    1、2、3 ...
     * @param handler
     */
    public static void addProtocol(String type, MatridxProtocolHandler<?> handler) {
        CommonChannelUtil.protocols.put(type, handler);
    }
    public static Map<String, MatridxProtocolHandler<?>> getProtocols() {
        return protocols;
    }
    /**
     * commend 设备上线时，把相应的通道记录到 未注册列表里
     *
     * @param incoming
     * @return
     */
    public static boolean addChannel(Channel incoming) {
        // 判断，加入到 list 中
        String key = String.valueOf(incoming.remoteAddress());
        synchronized (object) {
            // 如果未包含，
            if (!unRegisterChannels.containsKey(key)) {
                unRegisterChannels.put(key, incoming);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * commend 处理分发上线的通道，注明通道的deviceID和 协议类型
     *
     * @param protocol
     * @param incoming
     * @param recModel
     * @return
     */
    public static boolean handleChannel(String protocol, Channel incoming, FrameModel recModel) {
        // 判断，加入到 list 中
        String key = String.valueOf(incoming.remoteAddress());
        synchronized (object) {
            Channel unChnChannel = unRegisterChannels.get(key);

            // 如果未包含，
            if (unChnChannel == null) {
                ChannelModel model = null;
                for (String Channelkey : registerChannels.keySet()) {
                    if (key.equals(registerChannels.get(Channelkey).getAddress())) {
                        model = registerChannels.get(Channelkey);
                        break;
                    }
                }
                if (model == null) {
                    return false;
                } else {
                    if (!recModel.getCommanddeviceid().equals(model.getCommanddeviceid())) {
                        if (!registerChannels.containsKey(recModel.getCommanddeviceid())) {
                            ChannelModel channelModel = new ChannelModel();
                            channelModel.setChannel(incoming);
                            channelModel.setProtocol(protocol);
                            channelModel.setAddress(key);
                            channelModel.setDeviceId(recModel.getDeviceID());
                            channelModel.setCommanddeviceid(recModel.getCommanddeviceid());
                            registerChannels.put(recModel.getCommanddeviceid(), channelModel);
                        }

                    }
                }

            } else if (unChnChannel != null) {
                if (!registerChannels.containsKey(recModel.getCommanddeviceid())) {
                    ChannelModel model = new ChannelModel();
                    model.setChannel(unChnChannel);
                    model.setProtocol(protocol);
                    model.setAddress(key);
                    model.setDeviceId(recModel.getDeviceID());
                    model.setCommanddeviceid(recModel.getCommanddeviceid());
                    registerChannels.put(recModel.getCommanddeviceid(), model);
                }
                //doRegisterChannels.put(key, incoming);
                unRegisterChannels.remove(key);
                // 将建库仪的通道放到注册当中
                if (Command.CUBICS.toString().equals(recModel.getCommand())) {
                }

            }
        }
        return true;
    }


    /**
     * commend 设备下线后 ，删除相应的通道记录
     *
     * @param incoming
     * @return
     */
    public static boolean removeChannel(Channel incoming) {
        // 判断，从 list 中移除
        String key = String.valueOf(incoming.remoteAddress());
        synchronized (object) {
            // 如果未包含
            if (unRegisterChannels.containsKey(key)) {
                unRegisterChannels.remove(key);
            }
            List<String> channelList = new ArrayList<>();
            for (String commanddeviceid : registerChannels.keySet()) {
                ChannelModel model = registerChannels.get(commanddeviceid);
                if (model.getChannel() == incoming) {
                    channelList.add(commanddeviceid);

                    //break;
                }
            }
            for (String commanddeviceid : channelList) {
                ChannelModel model = registerChannels.get(commanddeviceid);
                registerChannels.remove(commanddeviceid);
                // 同时注销仪器的状态和队列
                //如果是建库仪则需要将通道全部下线
                if (Command.CUBICS.toString().equals(model.getProtocol())) {
                    List<String> cubsList = getCubsList(commanddeviceid.substring(1, commanddeviceid.length()));
                    //注销时移除所有的建库仪通道
                    registerCubics.remove(commanddeviceid);
                    if (cubsList != null && cubsList.size() > 0) {
                        for (String cus : cubsList) {
                            InstrumentStateGlobal.changeInstrumentState(model.getProtocol(), cus, InstrumentStatusEnum.STATE_OFFLINE.getCode());
                            InstrumentStateGlobal.removeYqQueuesFromRedis1(Command.CUBICS.toString(), cus);
                        }
                    }
                } else {
                    String dvid = commanddeviceid.substring(1, commanddeviceid.length());
                    InstrumentStateGlobal.changeInstrumentState(model.getProtocol(), dvid, InstrumentStatusEnum.STATE_OFFLINE.getCode());
                    InstrumentStateGlobal.removeYqQueuesFromRedis1(model.getProtocol(), dvid);
                }

            }
        }

        return true;
    }

    /**
     * 发送netty消息公用方法
     *
     * @param sendModel
     */
    public static Map<String, Object> sendMessage(FrameModel sendModel) {
        try {
            while (GlobalrouteEnum.SYSTEM_STATE_SUSPEND.getCode().equals(CommonChannelUtil.getSystemState())) {
                Thread.sleep(100);
            }
        } catch (Exception e) {

        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "fail");

        if (sendModel == null)
            return map;

        String params = "";
        String[] cmdParam = sendModel.getCmdParam();
        if (cmdParam != null && cmdParam.length > 0) {
            for (int i = 0; i < cmdParam.length; i++) {
                if (StringUtil.isNotBlank(cmdParam[i])) {
                    params += " " + cmdParam[i];
                }
            }
            if (StringUtil.isBlank(params)) {
                params = "";
            }
            sendModel.setMsgInfo(params);
        }

        ChannelModel channelModel = registerChannels.get(sendModel.getCommanddeviceid());

        if (channelModel == null) {
            log.error("已注册的channel为空！");
            map.put("message", "已注册的channel为空！");
            return map;
        }

        generateFrameID(sendModel);

        Map<String, BlockingQueue<FrameModel>> recvQueuesN = protocols.get(sendModel.getCommand()).recvQueues;
        Map<String, BlockingQueue<FrameModel>> secRecvQueuesN = protocols.get(sendModel.getCommand()).secRecvQueues;

        // 发送消息之前确认第一次返回的队列是否为空，如果为空，则设置空队列
        //String frameId = CommonChannelUtil.getFrameIdFromSend(sendModel);
        String frameId = sendModel.getFrameID();

        BlockingQueue<FrameModel> recvQueue = recvQueuesN.get(frameId);
        if (recvQueue == null) {
            recvQueue = new LinkedBlockingQueue<FrameModel>();
            recvQueuesN.put(frameId, recvQueue);
        }
        // 发送消息之前确认第二次返回的队列是否为空，如果为空，则设置空队列
        BlockingQueue<FrameModel> secRecvQueue = secRecvQueuesN.get(sendModel.getFrameID());
        if (secRecvQueue == null) {
            secRecvQueue = new LinkedBlockingQueue<FrameModel>();
            secRecvQueuesN.put(frameId, secRecvQueue);
        }

        channelModel.getChannel().writeAndFlush(
                sendModel.getCommand() + "&" + sendModel.getFrameID() + " " + sendModel.getCmd() + params);
        log.info(sendModel.getCommand() + "&" + sendModel.getFrameID() + " " + sendModel.getCmd() + params);

        map.put("status", "success");
        return map;
    }

    /**
     * 发送netty消息公用方法
     *
     * @param channel
     * @param sendModel
     */
    public static Map<String, Object> sendMessageByChannel(Channel channel, FrameModel sendModel) {
        try {
            while (GlobalrouteEnum.SYSTEM_STATE_SUSPEND.getCode().equals(CommonChannelUtil.getSystemState())) {
                Thread.sleep(100);
            }
        } catch (Exception e) {

        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "fail");
        if (sendModel == null)
            return map;
        String params = "";
        String[] cmdParam = sendModel.getCmdParam();
        if (cmdParam != null && cmdParam.length > 0) {
            for (int i = 0; i < cmdParam.length; i++) {
                params += " " + cmdParam[i];
            }
            // msgInfo.substring(1);
            sendModel.setMsgInfo(params);
        }

        generateFrameID(sendModel);
        channel.writeAndFlush(
                sendModel.getCommand() + "&" + sendModel.getFrameID() + " " + sendModel.getCmd() + params);
        System.err.println(sendModel.getCommand() + "&" + sendModel.getFrameID() + " " + sendModel.getCmd() + params);
        log.info(sendModel.getCommand() + "&" + sendModel.getFrameID() + " " + sendModel.getCmd() + params);

        map.put("status", "success");
        return map;
    }

    /**
     * command 根据发送消息设置帧ID
     *
     * @param sendModel
     * @return
     */
    public static boolean generateFrameID(FrameModel sendModel) {
        String deviceId = sendModel.getDeviceID();
        StringBuffer sBuffer = new StringBuffer();
        if (sendModel.isSendType())
            sBuffer.append("S");
        else {
            sBuffer.append("R");
        }
        sBuffer.append(deviceId);
        String msgType = sendModel.getMsgType();
        if (StringUtil.isBlank(msgType))
            msgType = "00";
        sBuffer.append(msgType);
        if (sendModel.isSyncHope()) {
            sBuffer.append("A");
        } else {
            sBuffer.append("S");
        }

        sendModel.setFrameID(sBuffer.toString());
        return true;
    }

    /**
     * 注册观察者
     *
     * @param t_cmdModel
     * @param threadName
     * @param observer
     */
    public static void registerCmdObserver(FrameModel t_cmdModel, String threadName, IObserver observer) {
        ChannelModel channelModel = registerChannels.get(t_cmdModel.getCommanddeviceid());
        if (t_cmdModel.getCommanddeviceid() == null) {
            log.info(String.format("仪器" + t_cmdModel.getCommanddeviceid() + "为null"));
        }
        MatridxProtocolHandler<?> handler = CommonChannelUtil.protocols.get(channelModel.getProtocol());
        if (t_cmdModel.getCommand().equals(Command.CUBICS.toString()) && StringUtil.isNotBlank(t_cmdModel.getPassId())) {
            handler.registerCmdObserver(threadName, t_cmdModel.getCommanddeviceid() + t_cmdModel.getPassId(), observer);
        } else {
            handler.registerCmdObserver(threadName, t_cmdModel.getCommanddeviceid(), observer);
        }
    }

    /**
     * 从发送消息里获取返回的帧ID
     *
     * @param sendModel
     * @return
     */
    public static String getFrameIdFromSend(FrameModel sendModel) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("R");
        sBuffer.append(sendModel.getDeviceID());
        String msgType = sendModel.getMsgType();
        if (StringUtil.isBlank(msgType))
            msgType = "00";
        sBuffer.append(msgType);
        if (sendModel.isSyncHope()) {
            sBuffer.append("A");
        } else {
            sBuffer.append("S");
        }

        return sBuffer.toString();
    }

    /**
     * 从已注册的通道中获取指定的设备id
     *
     * @param commd
     * @return
     */
    public static String getDeviceIDByCommand(String commd) {
        // 获取设备id
        for (String commanddeviceid : registerChannels.keySet()) {
            // 判断是这个协议的设备
            ChannelModel cModel = registerChannels.get(commanddeviceid);
            String p = cModel.getProtocol();
            // 找第一个
            if (p.equals(commd)) {
                return commanddeviceid.substring(1, commanddeviceid.length());
            }
        }
        return "";
    }

    /**
     * 注册时，放入建库仪通道
     *
     * @param device
     * @param list
     * @return
     */
    public static void putCubs(String device, List<CubisUpParamModel> list) {
        synchronized (registerCubics) {
            //统一加上协议号
            String cdevice = Command.CUBICS.toString() + device;
            List<String> l = new ArrayList<String>();
            registerCubics.put(cdevice, l);
            if (list == null || list.size() < 1) return;
            for (CubisUpParamModel cus : list) {
                if (StringUtil.isBlank(cus.getCubsChannel())) return;
                if (l.contains(cus.getCubsChannel())) {
                    log.error("已经存在重复的通道id，不再继续加入：" + cus.getCubsChannel());
                } else {
                    l.add(cus.getCubsChannel());
                }
            }
        }
    }

    public static boolean removeCubs(String device, String cubsid) {
        synchronized (registerCubics) {
            //统一加上协议号
            String cdevice = Command.CUBICS.toString() + device;
            List<String> l = CommonChannelUtil.registerCubics.get(cdevice);
            if (l == null) {
                l = new ArrayList<String>();
                return true;
            }
            l.remove(cubsid);
            return true;
        }
    }

    //获取建库仪通道
    public static List<String> getCubsList(String device) {
        //统一加上协议号
        String cdevice = Command.CUBICS.toString() + device;
        return CommonChannelUtil.registerCubics.get(cdevice);
    }

    //通过建库仪通道号，获取建库仪id
    public static String getDeviceIDByCubsChannel(String cusId) {
        // 获取设备id
        for (String cdevice : registerCubics.keySet()) {
            // 判断是这个协议的设备
            List<String> list = registerCubics.get(cdevice);
            if (list != null && list.size() > 0) {
                for (String cs : list) {
                    if (cusId.equals(cs)) {
                        return cdevice.substring(1, cdevice.length());
                    }

                }
            }
        }
        return "";
    }

    public static List<BaseCommand> getLcBaseCommandList() {
        return lcBaseCommandList;
    }

    public static void setLcBaseCommandList(List<BaseCommand> lcBaseCommandList) {

        CommonChannelUtil.lcBaseCommandList = lcBaseCommandList;
    }

    public static void addLcBaseCommandList(BaseCommand baseCommand) {
        lcBaseCommandList.add(baseCommand);
    }

    public static void removeLcBaseCommandList(BaseCommand baseCommand) {
        lcBaseCommandList.remove(baseCommand);

    }

    public static Map<String, ChannelModel> getRegisterChannels() {
        return registerChannels;
    }


}
