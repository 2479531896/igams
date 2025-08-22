package com.matridx.las.netty.channel.command;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.enums.YesNotEnum;
import com.matridx.las.netty.dao.entities.MlpzDto;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.las.netty.dosjback.EventCheckskipback;
import com.matridx.las.netty.dosjback.EventCommonback;
import com.matridx.las.netty.dosjbackimpl.EventFlowBack;
import com.matridx.las.netty.dosjbackimpl.EventFlowLoopBackHand;
import com.matridx.las.netty.enums.GlobalrouteEnum;
import com.matridx.las.netty.enums.GoodsEnum;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.service.svcinterface.IMlpzService;
import com.matridx.las.netty.util.ChangeEpUtil;
import com.matridx.las.netty.util.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.enums.RobotStatesEnum;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.global.RobotManagementGlobal;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.util.CommonChannelUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.data.redis.connection.stream.MapRecord;

public class BaseCommand extends ACommand {

    private Logger log = LoggerFactory.getLogger(BaseCommand.class);

    // 异步列表
    List<FrameModel> syncList = new ArrayList<FrameModel>();
    // 日志信息
    private String loggerMsg;
    // 异常处理标记位 0：无信息 1:重试 2:终止，线程置为ERROR 3:忽略，忽略当前标本
    private int errorflg = -1;
    // 是否为同步处理命令
    //private boolean syncReceive = true;
    // 异步命令队列
    private Map<String, BlockingQueue<FrameModel>> cmdDic = new HashMap<String, BlockingQueue<FrameModel>>();
    // 异步事件队列
    private Map<String, BlockingQueue<SjlcDto>> eventDic = new HashMap<String, BlockingQueue<SjlcDto>>();
    // 执行中的异步命令信息，用于保存正在处理的命令信息，方便执行回调方法
    private Map<String, FrameModel> excutingCmdDic = new HashMap<String, FrameModel>();
    //已经发送命令
    private Map<String, BlockingQueue<FrameModel>> haveSendcmdDic = new HashMap<String, BlockingQueue<FrameModel>>();
    //已经发送的事件
    private Map<String, BlockingQueue<SjlcDto>> haveSendeventDic = new HashMap<String, BlockingQueue<SjlcDto>>();
    //唯一id
    private String unid = StringUtil.generateUUID();
    //记录正在被发消息的事件
    private SjlcDto thisSjlcDto;

    public int getErrorflg() {
        return errorflg;
    }

    public void setErrorflg(int errorflg) {
        this.errorflg = errorflg;
    }

    public String getLoggerMsg() {
        return loggerMsg;
    }

    public void setLoggerMsg(String loggerMsg) {
        this.loggerMsg = loggerMsg;
    }

    public Map<String, BlockingQueue<FrameModel>> getCmdDic() {
        return cmdDic;
    }

    public void setCmdDic(Map<String, BlockingQueue<FrameModel>> cmdDic) {
        this.cmdDic = cmdDic;
    }

    public Map<String, BlockingQueue<SjlcDto>> getEventDic() {
        return eventDic;
    }

    public void setEventDic(Map<String, BlockingQueue<SjlcDto>> eventDic) {
        this.eventDic = eventDic;
    }

    public Map<String, FrameModel> getExcutingCmdDic() {
        return excutingCmdDic;
    }

    public void setExcutingCmdDic(Map<String, FrameModel> excutingCmdDic) {
        this.excutingCmdDic = excutingCmdDic;
    }

    /**
     * 删除线程名称的值
     * @param threadName
     */
    public void removeExcutingCmdDicByThreadName(String threadName) {
        excutingCmdDic.remove(threadName);
    }
    /**
     * 通过线程获取FrameModel
     * @param threadName
     */
    public FrameModel getExcutingCmdDicFrameModelByThreadName(String threadName) {
       return excutingCmdDic.get(threadName);
    }
    public SjlcDto getThisSjlcDto() {
        return thisSjlcDto;
    }

    public void setThisSjlcDto(SjlcDto thisSjlcDto) {
        this.thisSjlcDto = thisSjlcDto;
    }

    public Map<String, BlockingQueue<FrameModel>> getHaveSendcmdDic() {
        return haveSendcmdDic;
    }

    public void setHaveSendcmdDic(Map<String, BlockingQueue<FrameModel>> haveSendcmdDic) {
        this.haveSendcmdDic = haveSendcmdDic;
    }

    public Map<String, BlockingQueue<SjlcDto>> getHaveSendeventDic() {
        return haveSendeventDic;
    }

    public void setHaveSendeventDic(Map<String, BlockingQueue<SjlcDto>> haveSendeventDic) {
        this.haveSendeventDic = haveSendeventDic;
    }

    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    //是否抓取夹爪
    private boolean isTakeClaw = false;

    /**
     * Comment 同步底层命令链表，用于同步异步命令cmdDic执行
     *
     * @param baseCommand
     */
    public void updateCmdDic(BaseCommand baseCommand) {
        this.cmdDic = baseCommand.cmdDic;
    }

    /**
     * 发送命令给下位机，并内部组合成CmdMode命令
     *
     * @param deviceID 设备id
     * @param msgType  命令类型
     * @param cmdCode  命令代码
     * @param param    命令参数
     * @return
     */
    public boolean sendByCmdModel(String command, String deviceID, String msgType, String cmdCode, String[] param) {
        return sendByCmdModel(command, deviceID, msgType, cmdCode, param, null, false);
    }

    /**
     * 发送命令给下位机，并内部组合成CmdMode命令
     *
     * @param deviceID 设备ID
     * @param msgType  命令类型
     * @param cmdCode  命令代码
     * @param param    命令参数
     * @param callFun  回调函数
     * @param sync     是否异步
     * @return
     */
    public boolean sendByCmdModel(String command, String deviceID, String msgType, String cmdCode, String[] param, ICallBack callFun,
                                  boolean sync) {
        return sendByCmdModel(command, deviceID, msgType, cmdCode, param, callFun, sync, false);
    }

    /**
     * 发送命令给下位机，并内部组合成CmdMode命令
     *
     * @param deviceID 设备ID
     * @param msgType  命令类型
     * @param cmdCode  命令代码
     * @param param    命令参数
     * @param callFun  回调函数
     * @param sync     是否异步
     * @param lastSync 是否为最后一步
     * @return
     */
    public boolean sendByCmdModel(String command, String deviceID, String msgType, String cmdCode, String[] param, ICallBack callFun,
                                  boolean sync, boolean lastSync) {
        return sendByCmdModel(command, deviceID, msgType, cmdCode, param, callFun, sync, lastSync, "main");
    }

    /**
     * 发送命令给下位机，并内部组合成CmdMode命令
     *
     * @param deviceID   设备ID
     * @param msgType    命令类型
     * @param cmdCode    命令代码
     * @param param      命令参数
     * @param callFun    回调函数
     * @param sync       是否异步
     * @param lastSync   是否为最后一步
     * @param threadName 线程名称
     * @return
     */
    public boolean sendByCmdModel(String command, String deviceID, String msgType, String cmdCode, String[] param, ICallBack callFun,
                                  boolean sync, boolean lastSync, String threadName) {
        return sendByCmdModel(command, deviceID, msgType, cmdCode, param, callFun, sync, lastSync, threadName, 10000, 60000, null, null, false, 1, null, null, null);
    }

    /**
     * 发送命令给下位机，并内部组合成CmdMode命令
     *
     * @param deviceID      设备id
     * @param msgType       命令类型
     * @param cmdCode       命令代码
     * @param param         命令参数
     * @param callFun       回调函数
     * @param sync          是否异步
     * @param lastSync      是否为最后的异步操作
     * @param threadName    线程名称
     * @param firsttimeout  第一次返回超时时间
     * @param secondtimeout 第二次返回超时时间
     * @param howTimes      第几次发送这条消息
     * @param lcid          流程id
     * @param passId        建库仪通道id
     * @param bz
     * @return
     */
    public boolean sendByCmdModel(String command, String deviceID, String msgType, String cmdCode, String[] param, ICallBack callFun,
                                  boolean sync, boolean lastSync, String threadName, int firsttimeout, int secondtimeout, String gcid, Map<String, String> map, boolean isFirstSend, Integer howTimes, String lcid, String passId, String bz) {
         return sendByNewThread(new FrameModel(command, deviceID, msgType, cmdCode, param, sync, lastSync, callFun, threadName,
                firsttimeout, secondtimeout, gcid, map, isFirstSend, howTimes, lcid, passId, bz));
    }

    /**
     * 执行命令，用于继承类调用公用方法
     *
     * @param sendModel
     * @return
     */
    public boolean sendByNewThread(FrameModel sendModel) {
        try {
            String threadName = sendModel.getThreadName();
            // 如果为主线程，则采用当时等待
            if ("main".equals(threadName) || StringUtil.isBlank(threadName)) {
                return excuteCmd(sendModel);
            }
            // 如果为新线程，则非同一个线程不进行阻塞
            // 为减少线程，特改成模拟线程方式
            else {
                // 当同样的线程名称还在执行的情况下，那么当前命令直接忽略，主要用于每秒执行温度查询，可能反馈有点慢 2021-04-06
                FrameModel checkModel = excutingCmdDic.get(threadName);
                 log.info(this.toString());

                if (checkModel != null) {
                    log.info(String.format("BaseCommand 因线程：%s 还在执行中，故忽略该命令。", threadName));
                    return true;
                }
                BlockingQueue<FrameModel> t_cmdList = cmdDic.get(threadName);
                if (t_cmdList == null) {
                    t_cmdList = new LinkedBlockingQueue<FrameModel>();
                    cmdDic.put(threadName, t_cmdList);
                    log.info(String.format("初始化异步命令队列里的信息,threadName：%s，字典数量为%d", threadName, cmdDic.size()));
                }

                sendModel.setSendType(true);
                if (!sendModel.isFirstSend()) {//如果立即发送,不加入队列
                    t_cmdList.add(sendModel);
                }
                log.info(String.format("增加异步命令队列里的命令信息,threadName：%s，数量为%d 命令%s", threadName, t_cmdList.size(), sendModel.getCommand()+"&S"+sendModel.getDeviceID()+sendModel.getMsgType()+(sendModel.isSyncHope()?"S":"A"+" "+sendModel.getCmd()+" "+sendModel.getCmdParam())));

                // 如果为最后一个异步命令
                if (sendModel.isLastSync()) {
                    // 当同样的线程名称还在执行的情况下，那么当前命令直接忽略，主要用于每秒执行温度查询，可能反馈有点慢 2021-04-06 end
                    //立即发送不计入队列，也不从队列拿，直接发送掉
                    //todo 另加方法，添加多个命令
                    FrameModel t_cmdModel;
                    if (sendModel.isFirstSend()) {
                        t_cmdModel = sendModel;
                    } else {
                        t_cmdModel = t_cmdList.take();
                    }
                    BlockingQueue<FrameModel> t_havaSendcmdList = haveSendcmdDic.get(threadName);
                    if (t_havaSendcmdList == null) {
                        t_havaSendcmdList = new LinkedBlockingQueue<FrameModel>();
                        haveSendcmdDic.put(threadName, t_havaSendcmdList);
                        log.info(String.format("初始化异步命令已发送队列里的信息,threadName：%s，字典数量为%d", threadName, haveSendcmdDic.size()));
                    }
                    t_havaSendcmdList.add(t_cmdModel);

                    // 把命令信息保存到正在执行中的字典中，方便后面回调使用
                    excutingCmdDic.put(threadName, t_cmdModel);
                    // 根据命令是否为异步采用返回处理是否为异步
                    log.info(String.format("命令信息:%s，回调异步信息：%s", t_cmdModel.getCmd(), String.valueOf(t_cmdModel.isReceiveSync())));
                    // 注册自己，用于接收异步命令返回
                    CommonChannelUtil.registerCmdObserver(t_cmdModel, threadName, this);
                    log.info("BaseCommand", String.format("开始执行异步命令队列里的信息,threadName：%s，数量为%d", threadName, t_cmdList.size()));
                    // 因为是第一个，直接开始执行命令
                    Map<String, Object> resultMap = CommonChannelUtil.sendMessage(t_cmdModel);
                    if (resultMap == null || !"success".equals(resultMap.get("status")))
                        return false;
                }
                // 需讨论为防止多线程直接删除了队列，应该怎么处理，暂时考虑在回调方法里先删除字典信息，sleep 100，然后确认队列里是否还有信息，然后删除
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }


    /**
     * 空命令，等待所有命令结束 Comment 等待主线程
     *
     * @return
     */
    public boolean waitForThread() {
        return waitForThread("main");
    }

    /**
     * 空命令，等待所有命令结束 Comment 等待指定线程的命令结
     *
     * @param threadName
     * @return
     */
    public boolean waitForThread(String threadName) {
        return waitForThread(threadName, true, false, true);
    }

    /**
     * 空命令，等待所有命令结束
     *
     * @param threadName
     * @param secRet
     * @param sync
     * @param lastSync
     * @return
     */
    public boolean waitForThread(String threadName, boolean secRet, boolean sync, boolean lastSync) {
        FrameModel cmdModel = new FrameModel("", null, sync, lastSync, null, threadName);
        return excuteCmd(cmdModel, false);
    }

    /**
     * 执行命令信息 ,默认为真实发送命令
     *
     * @param sendModel 命令信息
     * @return
     */
    private boolean excuteCmd(FrameModel sendModel) {
        sendModel.setSendType(true);
        return excuteCmd(sendModel, true);
    }

    /**
     * 执行命令信息
     *
     * @param sendModel  命令信息
     * @param isRealSend 是否真实发送命令，用于一些只是用于等待命令结束的情况
     * @return
     */
    private boolean excuteCmd(FrameModel sendModel, boolean isRealSend) {
        try {


            // 如果采用异步方式
            if (sendModel.isSync()) {
                boolean result;
                // 如果只是等待同步命令，则不发送任何信息
                if (isRealSend) {
                    sendModel.setSendType(true);
                    syncList.add(sendModel);
                    // 执行命令
                    Map<String, Object> resultMap = CommonChannelUtil.sendMessage(sendModel);
                    if (resultMap == null || !"success".equals(resultMap.get("status")))
                        return false;
                    log.info(String.format("发送异步命令：%s  id:%s", sendModel.getMsgInfo(), sendModel.getDeviceID()));

                    // 同步等待第一次返回
                    FrameModel receiveFrame;
                    // 接收第一次命令
                    receiveFrame = readCmdResponseByTimes(1, sendModel, sendModel.getFirstTimeout());

                    // 处理返回值，如果遇到异常信息则返回
                    result = onExecutReceive(1, receiveFrame, sendModel.getCallFunc(), sendModel);

                    log.info(String.format("第一次接收异步命令：%s  %s", sendModel.getMsgInfo(), sendModel.getDeviceID()));
                    if (!result) {
                        return false;
                    }
                }

                // 如果为最后一个异步命令
                if (sendModel.isLastSync()) {
                    // 如果为最后一个异步命令，则等待所有命令结束
                    log.info(String.format("最后一个异步命令。异步队列剩余：%s  ", syncList.size()));
                    if (syncList.size() > 0) {
                        List<String> errorList = waitForAllBack(syncList, sendModel.getSecondTimeout());
                        if (errorList != null && errorList.size() > 0) {
                            return false;
                        }
                    }
                    log.info("最后一个异步命令。异步队列结束。开始异步线程（命令字典）等待。 ");
                    while (cmdDic != null) {
                        // 如果指定了线程，那么等待响应的异步线程结束
                        BlockingQueue<FrameModel> t_cmdList = cmdDic.get(sendModel.getThreadName());
                        if (t_cmdList != null) {
                            // 如果已经停止，则不再等待，直接退出
                            if (CommonChannelUtil.getSystemState().equals(GlobalrouteEnum.SYSTEM_STATE_STOP.getCode()))
                                break;
                            // 执行异步命令出现异常
                            if (sendModel.isError()) {
                                cmdDic.remove(sendModel.getThreadName());
                                excutingCmdDic.remove(sendModel.getThreadName());
                                return false;
                            }
                            // 等待线程结束
                            log.info(String.format("等待线程结束3。线程名称：%s  ", sendModel.getThreadName()));
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                log.error(String.format("等待线程结束3。sleep出错：%s  ", e.getLocalizedMessage()));
                            }
                        } else {
                            // 异步命令执行完成
                            break;
                        }
                    }
                    log.info("最后一个异步命令。异步线程结束。 ");
                }
                // 非最后一个异步命令，则继续执行下一个命令，返回结果全在最后一个异步里进行确认
            } else {
                // 两次返回，但采用同步命令
                // 如果发现异步列表有数据，则证明调用错误，先等待异步完成后再执行同步命令
                // 为支持两个命令同时发生，有一个命令清单里要进行同步，故特意清除该处限制
                // 如果为最后一个命令，则意味则需要等待异步线程结束，再执行自己的命令
                if (sendModel.isLastSync()) {
                    // 如果没有指定线程，则等待所有的异步线程结束
                    if ("main".equals(sendModel.getThreadName())) {
                        log.info("同步最后一个命令。同步等待异步线程（命令字典）。 ");
                        while (true) {
                            // 命令执行到最后一个，虽然队列里已经不存在了，但字典还存在
                            // 只有当命令已经执行完了，字典才会删除
                            if (cmdDic.size() > 0) {
                                // 如果已经停止，则不再等待，直接退出
                                if (CommonChannelUtil.getSystemState().equals(GlobalrouteEnum.SYSTEM_STATE_STOP.getCode()))
                                    break;

                                String t_str = "";
                                for (String item : cmdDic.keySet()) {
                                    t_str += item + ":" + cmdDic.get(item).size() + "  ";
                                }
                                // 等待线程结束
                                log.info(String.format("等待线程结束1。%s ,线程名称：%s,剩余线程:%s  ", cmdDic.size(), sendModel.getThreadName(), t_str));
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    log.error(String.format("等待线程结束1。sleep出错：%s  ", e.getLocalizedMessage()));
                                }
                            } else {
                                // 异步命令执行完成
                                break;
                            }
                        }
                        log.info("BaseCommand", "同步最后一个命令。等待异步线程结束。 ");
                    } else {
                        while (cmdDic != null) {
                            // 如果指定了线程，那么等待响应的异步线程结束
                            BlockingQueue<FrameModel> t_cmdList = cmdDic.get(sendModel.getThreadName());
                            if (t_cmdList != null) {
                                // 如果已经停止，则不再等待，直接退出
                                if (CommonChannelUtil.getSystemState().equals(GlobalrouteEnum.SYSTEM_STATE_STOP.getCode()))
                                    break;
                                // 等待线程结束
                                log.info(String.format("等待线程结束2。线程名称：%s", sendModel.getThreadName()));
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    log.error(String.format("等待线程结束2。sleep出错：%s  ", e.getLocalizedMessage()));
                                }
                            } else {
                                // 异步命令执行完成
                                break;
                            }
                        }
                    }
                }
                // 如果只是等待同步命令，则不发送任何信息
                if (isRealSend) {
                    // 执行命令
                    CommonChannelUtil.sendMessage(sendModel);
                    log.info(String.format("发送同步命令：%s  id:%s ", sendModel.getMsgInfo(), sendModel.getDeviceID()));
                    FrameModel receiveFrame;
                    boolean result = false;
                    // 等待第一次返回
                    receiveFrame = readCmdResponseByTimes(1, sendModel, sendModel.getFirstTimeout());
                    if (receiveFrame != null)
                        log.info(String.format("第一次接收同步命令： %s id:%s", receiveFrame.getMsgInfo(), receiveFrame.getDeviceID()));

                    // 处理返回值，如果遇到异常信息则返回
                    result = onExecutReceive(1, receiveFrame, sendModel.getCallFunc(), sendModel);

                    if (!result)
                        return false;
                    // 第二次返回
                    receiveFrame = readCmdResponseByTimes(2, sendModel, sendModel.getSecondTimeout());
                    if (receiveFrame != null)
                        log.info(String.format("第二次接收同步命令： %s id:%s", receiveFrame.getMsgInfo(), receiveFrame.getDeviceID()));

                    // 处理返回值，如果遇到异常信息则返回
                    result = onExecutReceive(2, receiveFrame, sendModel.getCallFunc(), sendModel);

                    if (!result)
                        return false;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * comment 推送信息到队列中(立即回调帧队列)
     *
     * @return
     * @throws Exception
     */
    public boolean putRecvQueue(Byte commond, String frameId) throws Exception {
        BlockingQueue<FrameModel> recvQueue = CommonChannelUtil.protocols.get(commond.toString()).recvQueues.get(frameId);
        if (recvQueue == null) {
            recvQueue = new LinkedBlockingQueue<FrameModel>();
        }
        CommonChannelUtil.protocols.get(commond.toString()).recvQueues.put(frameId, recvQueue);
        return true;
    }

    /**
     * comment 推送信息到队列中(二次回调帧队列)
     *
     * @return
     * @throws Exception
     */
    public boolean putSecRecvQueue(Byte commond, String frameId) throws Exception {
        BlockingQueue<FrameModel> secRecvQueue = CommonChannelUtil.protocols.get(commond.toString()).secRecvQueues.get(frameId);
        if (secRecvQueue == null) {
            secRecvQueue = new LinkedBlockingQueue<FrameModel>();
        }
        CommonChannelUtil.protocols.get(commond.toString()).secRecvQueues.put(frameId, secRecvQueue);
        return true;
    }

    /**
     * Comment 循环确认命令是否结束
     *
     * @param cmdList
     * @param timeout
     * @return
     */
    private List<String> waitForAllBack(List<FrameModel> cmdList, int timeout) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        long span;
        double elapsedMilliseconds = 0.0;
        List<String> errorList = new ArrayList<String>();
        FrameModel receiveFrame;
        int currentTimeOut = timeout;

        while (cmdList.size() > 0) {
            FrameModel currentModel = cmdList.get(0);
            if (timeout >= 0) {
                span = Calendar.getInstance().getTimeInMillis() - startTime;
                elapsedMilliseconds = Math.abs(span);
                if (elapsedMilliseconds > timeout) {
                    // 设备通信超时，则直接采用100毫秒的超时时间进行读取
                    currentTimeOut = 100;
                } else {
                    // 如果没有超时，则采用规定的超时时间减去 已用时间进行读取
                    currentTimeOut = timeout - (int) elapsedMilliseconds;
                    // 如果低于100则采用最低100 进行读取
                    if (currentTimeOut < 100)
                        currentTimeOut = 100;
                }
            }

            // 第二次返回,超时时间减少剩余的时间
            receiveFrame = readCmdResponseByTimes(2, currentModel, currentTimeOut);

            if (receiveFrame == null) {
                errorList.add("设备" + currentModel.getDeviceID() + "通信时间超时！");
                // 如果设置了回调方法，则调用回调，反馈超时情况
                if (currentModel.getCallFunc() != null) {
                    onExecutReceive(2, receiveFrame, currentModel.getCallFunc(), currentModel);
                }
                cmdList.remove(currentModel);
                continue;
            } else {
                log.info("BaseCommand", String.format("循环确认命令是否结束。第二次接收命令：%s  id:%s", receiveFrame.getMsgInfo(), receiveFrame.getDeviceID()));
                cmdList.remove(currentModel);
                // 处理返回值，如果遇到异常信息则返回
                boolean result = onExecutReceive(2, receiveFrame, currentModel.getCallFunc(), currentModel);

                if (!result) {
                    errorList.add("设备" + currentModel.getDeviceID() + "二次接收命令后出错！");
                    continue;
                }
            }
        }

        return errorList;
    }

    /**
     * comment 获取指定返回次数的相应帧 comment 默认超时时间为30S
     *
     * @param receiveNum 返回帧标记 1：立即回复 2：二次返回
     * @param sendFrame
     * @return
     */
    public FrameModel readCmdResponseByTimes(int receiveNum, FrameModel sendFrame) {
        return readCmdResponseByTimes(receiveNum, sendFrame, 30000);
    }

    /**
     * comment 获取指定返回次数的相应帧
     *
     * @param receiveNum 返回帧标记 1：立即回复 2：二次返回
     * @param timeout    超时时间
     * @return
     */
    public FrameModel readCmdResponseByTimes(int receiveNum, FrameModel sendFrame, int timeout) {
        if (receiveNum <= 0) {
            return null;
        }

        FrameModel readframe = null;
        try {
            String frameId = CommonChannelUtil.getFrameIdFromSend(sendFrame);
            // 两次异步返回多帧的情况，因无法区分，暂不考虑，所以while (true)只考虑多帧短帧，或者一次长帧
            if (receiveNum == 2) {
                BlockingQueue<FrameModel> secRecvQueue = CommonChannelUtil.protocols.get(sendFrame.getCommand()).secRecvQueues.get(frameId);
                while (true) {
                    // 阻塞读取
                    //readframe = secRecvQueue.poll(timeout,TimeUnit.MILLISECONDS);
                    readframe = secRecvQueue.take();

                    log.info("第二次：阻塞完成");
                    // 读取超时，则直接返回
                    if (readframe == null) {
                        log.error("第二次：readframe为空");
                        // 清空
                        this.excutingCmdDic.clear();
                        return null;
                    }
                    FrameModel o_canFrameModel = this.excutingCmdDic.get(sendFrame.getFrameID());
                    if (o_canFrameModel != null) {
                        this.excutingCmdDic.remove(sendFrame.getFrameID());
                    }

                    break;
                }
            } else if (receiveNum == 1) {
                BlockingQueue<FrameModel> recvQueue = CommonChannelUtil.protocols.get(sendFrame.getCommand()).recvQueues.get(frameId);
                while (true) {
                    // 阻塞读取
                    readframe = recvQueue.take();
                    log.info("第一次堵塞结束");
                    // 读取超时，则直接返回
                    if (readframe == null) {
                        this.excutingCmdDic.clear();
                        return null;
                    }
                    return readframe;
                }
            } else {
                readframe = null;
            }
        } catch (Exception e) {
            log.error(e.toString());
        }

        return readframe;
    }

    /**
     * Comment 处理Netty的返回的信息，同时确认是否回调函数
     *
     * @param times         返回次数：1 第一次 2 第二次
     * @param recFrameModel 返回的帧信息
     * @param callBack      回调类
     * @param sendModel     发送信息
     * @return
     */
    private boolean onExecutReceive(int times, FrameModel recFrameModel, ICallBack callBack, FrameModel sendModel) {
        // 如果设置了回调类
        if (callBack != null) {
            boolean result = callBack.callfunc(times, recFrameModel, sendModel, this.thisSjlcDto);
            if (result) {
                return true;
            } else {
                return false;
            }
        } else {
            return executReceive(times, recFrameModel, sendModel);
        }
    }

    /*-----------   实现接口方法   -------------*/

    /**
     * 异步操作里，各协议完成后调用此处，进行下一步的命令
     */
    public boolean onReceive(Object obj) {
        try {
            Map<String, Object> param = (HashMap<String, Object>) obj;
            String threadName;
            FrameModel receiveFrame;
            Object o_threadName = param.get("threadName");
            Object o_frame = param.get("frame");
            System.err.println(this);
            threadName = (String) o_threadName;
            receiveFrame = (FrameModel) o_frame;
            BlockingQueue<FrameModel> t_cmdList = cmdDic.get(threadName);
            if (t_cmdList != null)
                log.info(String.format("命令里异步确认t_cmdList 是否还有值，有则执行下一个,现存个数：%s，threadName为%s", t_cmdList.size(), o_threadName));
            else
                log.info("命令里异步确认t_cmdList 无值");

            // 先针对返回的信息
            FrameModel t_cmdModel = excutingCmdDic.get(threadName);
            // log.info(String.format("返回命令：%s，事件为%s",t_cmdModel.getMlid(),thisSjlcDto.getSjid()));
            if (t_cmdModel != null) {

                //将发送此消息的baseCommand对象，和线程名称放进接收对象
                t_cmdModel.setSendBaseCommand(this);
                t_cmdModel.setSendThreadName(threadName);
                if (!"OK".equals(receiveFrame.getCmd())) {
                    //todo配置重发
                    log.error("命令出错：" + receiveFrame.getCommand() + "&" + receiveFrame.getFrameID() + " " + receiveFrame.getCmdParam());
                    return false;
                }
                boolean result = onExecutReceive(2, receiveFrame, t_cmdModel.getCallFunc(), t_cmdModel);
                if (!result) {
                    t_cmdModel.setMsgInfo(receiveFrame.getMsgType());
                    t_cmdModel.setError(true);
                    log.info(String.format("异步线程命令的回调方法执行错误。 threadName为 %s", threadName));
                    return false;
                }
            } else {
                log.info("字典里不存在 相应的线程：", threadName);
            }

            // 再执行下一条命令
            if (t_cmdList != null && t_cmdList.size() > 0) {
                t_cmdModel = t_cmdList.take();
                BlockingQueue<FrameModel> t_havaSendcmdList = haveSendcmdDic.get(threadName);
                if (t_havaSendcmdList != null) {
                    t_havaSendcmdList.add(t_cmdModel);
                }

                // 把命令信息保存到正在执行中的字典中，方便后面回调使用
                excutingCmdDic.put(threadName, t_cmdModel);

                log.info("命令里异步确认发送命令,注册观察者,threadName:" + threadName + ",CmdParam" + t_cmdModel.getCmdParam());
                // CommonConfig.Singleton.OutPutlog.infoebug("命令里异步确认发送命令,注册观察者,
                // threadName为{0}", o_threadName);
                // 注册自己，用于接收异步命令返回
                CommonChannelUtil.registerCmdObserver(t_cmdModel, threadName, this);

                // 执行下一个命令
                CommonChannelUtil.sendMessage(t_cmdModel);
                log.info(String.format("返回命令：%s，事件为%s", t_cmdModel.getMlid(), thisSjlcDto.getSjid()));
            } else if (t_cmdList != null && t_cmdList.size() == 0) {
                cmdDic.remove(threadName);
                excutingCmdDic.remove(threadName);
                haveSendcmdDic.remove(threadName);
                log.info(String.format("命令里异步确认t_cmdList 无值，清空字典信息 threadName为%s", threadName));
                //检测ma todo ma
                log.info("一个事件结束 流程类型:"+thisSjlcDto.getLclx()+" 》 》"+"事件id"+thisSjlcDto.getSjid()+" 》 》"+InstrumentStateGlobal.getAllInstrumentUsedList().toJSONString());
                //一个事件结束，调用回调
                EventCommonback sjCommonback = null;
                if (StringUtil.isNotBlank(thisSjlcDto.getSjhd())) {
                    sjCommonback = (EventCommonback) SpringUtil.getBean(thisSjlcDto.getSjhd());
                }
                JSONObject job = null;
                if (sjCommonback != null) {
                    Map<String, String> ma = new HashMap<>();
                    if (Command.CUBICS.toString().equals(t_cmdModel.getCommand())) {
                        ma = InstrumentStateGlobal.getInstrumentUsedListMap(t_cmdModel.getCommand(), t_cmdModel.getPassId());
                    } else {
                        ma = InstrumentStateGlobal.getInstrumentUsedListMap(t_cmdModel.getCommand(), t_cmdModel.getDeviceID());
                    }
                    if (ma == null) {
                        ma = new HashMap<>();
                    }
                    job = sjCommonback.handBack(thisSjlcDto, receiveFrame, ma);

                    if ("true".equals(job.get("special")) && job.get("special") != null) {
                        t_cmdModel = (FrameModel) job.get("model");
                    }
                    if ("false".equals(job.get("res"))) {
                        t_cmdModel.setMsgInfo(receiveFrame.getMsgType());
                        t_cmdModel.setError(true);
                        log.info(String.format("异步线程事件的回调方法执行错误。 threadName为 %s", threadName));
                        return false;
                    }
                }
                // 判断是否循环
                Map<String, String> ma;

                if ("1".equals(thisSjlcDto.getSfxh())) {
                    //检查是都需要循环,循环的话，map里的值不删除
                    if (Command.CUBICS.toString().equals(t_cmdModel.getCommand())) {
                        ma = InstrumentStateGlobal.getInstrumentUsedListMap(t_cmdModel.getCommand(), t_cmdModel.getPassId());
                    } else {
                        ma = InstrumentStateGlobal.getInstrumentUsedListMap(t_cmdModel.getCommand(), t_cmdModel.getDeviceID());
                    }
                    if(ma==null){
                        ma  = new HashMap<>();
                    }
                    boolean isLoop = new EventFlowLoopBackHand().checkLoop(thisSjlcDto, ma);
                    if (isLoop) {
                        sendCommandListThread(true, thisSjlcDto, threadName,  ma);
                        return true;
                    }

                } else {
                    //在这里归还机器人和仪器信息
                    ma = EventFlowBack.returnBackYqandjqr(t_cmdModel, thisSjlcDto);
                }
                if (job != null && job.get("ma") != null) {
                    ma = (Map<String, String>) job.get("ma");
                }
                //检测ma todo ma
                log.info("处理完释放机器人和仪器 事件类型:"+thisSjlcDto.getLclx()+" 》 》"+"流程id"+thisSjlcDto.getSjid()+" 》 》"+InstrumentStateGlobal.getAllInstrumentUsedList().toJSONString());
                //添加判断是否有下一个事件
                BlockingQueue<SjlcDto> t_sjlcList = eventDic.get(threadName);
                //添加判断此事件是否执行
                while (true) {

                    if (t_sjlcList != null && t_sjlcList.size() > 0) {
                        //查看当前的
                        //调用查询命令的流程，并把流程里的命令发送
                        SjlcDto sjlcDto = t_sjlcList.take();
                        BlockingQueue<SjlcDto> t_havaSendsjlcList = haveSendeventDic.get(threadName);
                        if (t_havaSendsjlcList != null) {
                            t_havaSendsjlcList.add(sjlcDto);
                        }
                        //下一个事件，则把当前事件改为
                        this.thisSjlcDto = sjlcDto;
                        //判断是否执行当前得事件
                        if (StringUtils.isNotBlank(thisSjlcDto.getPdzxcs())) {
                            EventCheckskipback checkSkipback = (EventCheckskipback) SpringUtil.getBean(thisSjlcDto.getPdzxcs());
                            boolean isskip = checkSkipback.checkSkip(thisSjlcDto, t_cmdModel, ma);
                            if (!isskip) {
                                continue;
                            } else {
                                sendCommandListThread(true, sjlcDto, threadName,  ma);
                                break;
                            }
                        } else {
                            sendCommandListThread(true, sjlcDto, threadName,  ma);
                            break;
                        }

                    } else {
                        //流程完毕移除流程
                        eventDic.remove(threadName);
                        log.info("eventDicd长度："+eventDic.size()+",threadName："+threadName);
                        //流程完毕后，将此流程从系统里移除
                        if (eventDic.isEmpty()) {
                            CommonChannelUtil.removeLcBaseCommandList(this);
                        }
                        //log.info("流程中确认t_sjlcList无值，清空字典信息 threadName：" + threadName);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error(String.format("onReceiveCan 方法执行异常%s", e.getMessage()));
            log.error(e.getMessage());
        }
        return true;
    }

    /**
     * Comment 执行时发生异常时的处理
     *
     * @return
     */
    @Override
    public boolean exceptionHappend() {
        return true;
    }

    /**
     * Comment执行开始
     *
     * @param throwException 是否抛出异常
     * @return
     */
    @Override
    protected boolean executing(boolean throwException) {
        return true;
    }

    /**
     * Comment 处理Netty的返回的信息
     *
     * @param times         返回次数：1 第一次 2 第二次
     * @param recFrameModel 返回的帧信息
     * @return
     */
    public boolean executReceive(int times, FrameModel recFrameModel, FrameModel sendModel) {
        if (recFrameModel == null) {
            log.error(String.format("BaseCommand 通信超时 DevceId:%s  ", sendModel.getDeviceID()));
            return false;
        }
        return true;
    }

    /**
     * Comment 前置处理
     *
     * @return
     */
    @Override
    public boolean preDeal() {
        return true;
    }

    /**
     * Comment重新请求
     *
     * @return
     */
    @Override
    public boolean retry() {
        return true;
    }

    /*-----------   未调用方法   -------------*/

    /**
     * 判断指定命令线程是否已经结束，即刻返回
     *
     * @param threadName
     * @return
     */
    public boolean isCmdThreadEnd(String threadName) {
        BlockingQueue<FrameModel> t_cmdList = cmdDic.get(threadName);
        if (t_cmdList == null || t_cmdList.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Comment 强制结束命令 Comment 结束命令是指清空当前的所有命令，但后续还是可以继续运行  所以应该只需清空队列，而无需传递停止命令到协议里
     *
     * @param flag
     */
    public void stopCommand(boolean flag) {
        for (String cmdkey : cmdDic.keySet()) {
            BlockingQueue<FrameModel> lists = cmdDic.get(cmdkey);
            cmdDic.remove(cmdkey);
            lists.clear();
        }
    }

    /**
     * Comment 泵异常时保存信息到数据库
     *
     * @param deviceLocation
     * @param posX
     * @param posY
     * @param posZ
     * @return
     */
    public boolean PumpErrorSave(String deviceLocation, String posX, String posY, String posZ) {
        return true;
    }

    // 在一个流程开始前获取并检查某个协议中的设备
    public String getDeviceIdByCommd(String commd) throws Exception {
        // 先检查需要的仪器有空闲的吗
        // 这里可以从队列中取的同协议下的空闲的仪器
        //新加区分建库仪
        BlockingQueue<String> queue = null;
        // 获取空闲仪器的 id，如果没有等待，知道有空闲仪器为止

        //String deviceId = queue.take();
        String deviceId = "";
        MapRecord<String, Object, Object> mapRecord = InstrumentStateGlobal.getYQQueueFromRedis(commd);
        Map<Object, Object> objectMap = mapRecord.getValue();
        for (Object obj : objectMap.keySet()) {
            deviceId = obj.toString();
            InstrumentStateGlobal.removeYqQueuesFromRedis(commd, mapRecord.getId().toString());
            break;
        }
        // 修改仪器状态为预备启用
        InstrumentStateGlobal.changeInstrumentState(commd, deviceId, InstrumentStatusEnum.STATE_WORK.getCode());
        return deviceId;
    }

    // 在一个流程开始前获取空闲的机器人
    public String getFreeRobot() throws Exception {
        //机器人任务数+1
        RobotManagementGlobal.setRemainingTasks(1, true);
        //先查看机器人是否在充电
       /* if (RobotStatesEnum.ROBOT_CHARGE.getCode().equals(RobotManagementGlobal.getRobotState())
                ||RobotStatesEnum.ROBOT_CHARGE.getCode().equals( RobotManagementGlobal.getRobotState())) {
            String devicedid = CommonChannelUtil.getDeviceIDByCommand(Command.AGV.toString());
            if (StringUtil.isBlank(devicedid)) {
                log.info("机器人未注册,等待机器人上线");
                //return "false";
            } else {
                RobotManagementGlobal.getAndSendElc(devicedid);
            }
        }*/
        //BlockingQueue<String> queue = RobotManagementGlobal.getRobotQueue();
        // 获取空闲仪器的 id，如果没有等待，知道有空闲仪器为止
        //String deviceId = queue.take();
        String deviceId = "";

        // 这里可以从队列中取的空闲的机器人
        MapRecord<String, Object, Object> mapRecord = InstrumentStateGlobal.getYQQueueFromRedis(Command.AGV.toString());
        Map<Object, Object> objectMap = mapRecord.getValue();
        for (Object obj : objectMap.keySet()) {
            deviceId = obj.toString();
            InstrumentStateGlobal.removeYqQueuesFromRedis(Command.AGV.toString(),mapRecord.getId().toString());
            break;
        }
        // 修改仪器状态为预备启用
        InstrumentStateGlobal.changeInstrumentState(Command.AGV.toString(),deviceId, RobotStatesEnum.ROBOT_BUSY.getCode());
        return deviceId;
    }

    public boolean putEventToQueue(SjlcDto sjlcDto, String threadName, Map<String, String> ma) throws InterruptedException {
        BlockingQueue<SjlcDto> t_eventList = eventDic.get(threadName);
        if (t_eventList == null) {
            t_eventList = new LinkedBlockingQueue<SjlcDto>();
            eventDic.put(threadName, t_eventList);
            log.info("开始执行事件队列里的信息threadName：" + threadName + "事件数量：" + eventDic.size());
        }
        t_eventList.add(sjlcDto);

        log.info("增加事件队列里的事件信息,threadName：" + threadName + "事件数量：" + t_eventList.size());
        // 如果为最后一个流程
        if ("1".equals(sjlcDto.getSfzhyg())) {
            SjlcDto sjlcDtoLast = t_eventList.take();
            BlockingQueue<SjlcDto> t_haveSendeventList = haveSendeventDic.get(threadName);
            if (t_haveSendeventList == null) {
                t_haveSendeventList = new LinkedBlockingQueue<SjlcDto>();
                haveSendeventDic.put(threadName, t_haveSendeventList);
                log.info("已执行执行事件队列里的信息threadName：" + threadName + "事件数量：" + haveSendeventDic.size());
            }
            t_haveSendeventList.add(sjlcDtoLast);
            this.thisSjlcDto = sjlcDtoLast;
            //调用发送命令的方法
            sendCommandListThread(true, sjlcDtoLast, threadName,  ma);
            log.info("开始执行事件队列里的信息threadName：" + threadName + "事件数量：" + t_eventList.size());
        }
        return true;

    }

    /**
     * 调用发送命令
     *
     * @param sjlcDto
     * @param theadName
     * @param ma
     * @return
     */
    public boolean sendCommandList(SjlcDto sjlcDto, String theadName, Map<String, String> ma) {
        //是否需要释放占用或者释放仪器，机器人

        MlpzDto mlpzDto = new MlpzDto();
        mlpzDto.setSjid(sjlcDto.getSjid());
        IMlpzService mlpzService = (IMlpzService) SpringUtil.getBean("mlpzServiceImpl");
        //获取事件里的命令
        List<MlpzDto> mlpzDtoList = mlpzService.getMlpzList(mlpzDto);
        // 将list的消息都发出去
        if (mlpzDtoList == null||mlpzDtoList.size() == 0) {
            return false;
        }

        String yqid = "";
        String jqrid = "";
        //获取发送的历史仪器的map
        if (ma == null) {
            ma = new HashMap<String, String>();
            InstrumentStateGlobal.setInstrumentUsedList(ma);
        }
        try {
            if ("1".equals(sjlcDto.getSfzyyq()) && StringUtil.isNotBlank(sjlcDto.getZyyqlx())) {
                // 判断并获取仪器
                //为了避免循环，不需要重获取
                if (StringUtil.isBlank(ma.get(sjlcDto.getZyyqlx()))|| YesNotEnum.YES.getCode().equals(sjlcDto.getSfxyq())) {
                    yqid = getDeviceIdByCommd(sjlcDto.getZyyqlx());
                    InstrumentStateGlobal.setInstrumentUsedList_Map(ma, sjlcDto.getZyyqlx(), yqid);
                } else {
                    yqid = ma.get(sjlcDto.getZyyqlx());
                }

            }
            if ("1".equals(sjlcDto.getSfzyjqr())) {
                // 判断并获取仪器
                if (StringUtil.isBlank(ma.get(Command.AGV.toString()))|| YesNotEnum.YES.getCode().equals(sjlcDto.getSfxjqr())) {
                    jqrid = getFreeRobot();
                    InstrumentStateGlobal.setInstrumentUsedList_Map(ma, Command.AGV.toString(), jqrid);
                    ma.put(Command.AGV.toString(), jqrid);
                } else {
                    jqrid = ma.get(Command.AGV.toString());
                }
            }
            //检测ma todo ma
            log.info("加入事件的 发送的地方，占用完仪器后的ma 事件类型:"+thisSjlcDto.getLclx()+" 》 》"+"事件sjid"+thisSjlcDto.getSjid()+" 》 》"+InstrumentStateGlobal.getAllInstrumentUsedList().toJSONString());

            Map<String,Object> map = new HashMap<>();
            map.put("yqinstAndRobotUsedMap", ma);
            map.put("robotDeviceID", jqrid);
            map.put("theadName", theadName);
            sendCmdSModelByEvent(mlpzDtoList, map, sjlcDto);
        } catch (Exception e) {
            // new ExceptionHandlingUtil(jsonObject2);
            //log.error(e.getMessage());
            log.error(e.getMessage());

            return false;

        }

        return true;
    }

    /**
     * 公用查数据库调用发送接口方法
     *
     * @param isyn
     * @param sjlcDto
     * @param theadName
     * @param ma
     * @return
     */
    public boolean sendCommandListThread(boolean isyn, SjlcDto sjlcDto, String theadName, Map<String, String> ma) {
        if (isyn) {
            Runnable newRunnable = new Runnable() {
                public void run() {
                    sendCommandList(sjlcDto, theadName, ma);
                }
            };
            cachedThreadPool.execute(newRunnable);
        } else {
            sendCommandList(sjlcDto, theadName, ma);
        }
        return true;
    }

    /**
     * 公用查数据库调用发送接口方法
     *
     * @param list
     * @param map
     * @return
     */
    public boolean sendCmdSModelByEvent(List<MlpzDto> list, Map<String,Object> map, SjlcDto sjlcDto) {
        // 将list的消息都发出去
        if (list == null || list.size() == 0) {
            return false;
        }
        Object o_theadName = map.get("theadName");
        String theadName = "";
        if (o_theadName != null) {
            theadName = (String) o_theadName;
        } else {
            theadName = Thread.currentThread().getName();
        }
        //获取发送的历史仪器的map
        Object yqinstAndRobotUsedMap =  map.get("yqinstAndRobotUsedMap");
        Map<String, String> ma = new HashMap<>();
        if ( yqinstAndRobotUsedMap!= null) {
            ma =  (Map<String, String>)yqinstAndRobotUsedMap;
        }
        try {
            for (MlpzDto mlpzDto : list) {
                //建库仪通道号
                String cusId = "";
                String deviceID = ma.get(mlpzDto.getXylx());
                if (Command.CUBICS.toString().equals(mlpzDto.getXylx())) {
                    String deviceID1 = CommonChannelUtil.getDeviceIDByCubsChannel(deviceID);
                    if (StringUtil.isBlank(deviceID1)) {
                        throw new Exception("找不到建库仪的仪器id，建库一通道号未：" + deviceID);
                    }
                    cusId = deviceID;
                    deviceID = deviceID1;
                }
                // 是否是主线程
                // 获取回调函数
                String callBackName = mlpzDto.getHdlx();
                ICallBack callBack = (ICallBack) SpringUtil.getBean(callBackName);
                Integer dyc = 10000;
                Integer dec = 60000;
                if (StringUtil.isNotBlank(mlpzDto.getDycfhcssj())) {
                    dyc = Integer.valueOf(mlpzDto.getDycfhcssj());
                }
                if (StringUtil.isNotBlank(mlpzDto.getDecfhcssj())) {
                    dec = Integer.valueOf(mlpzDto.getDecfhcssj());
                }
                String[] parm = new String[]{""};
                //判断是否需要传参
                if ("1".equals(mlpzDto.getSfcc())) {
                    parm[0] = mlpzDto.getMlcs();
                } else {
                    if (StringUtil.isBlank(sjlcDto.getClassname())) {
                        if (StringUtil.isNotBlank(mlpzDto.getClassname())) {
                            parm = getArr(parm, mlpzDto.getClassname(), mlpzDto, sjlcDto, ma);
                        }
                    } else {
                        parm = getArr(parm, sjlcDto.getClassname(), mlpzDto, sjlcDto, ma);
                    }
                }
                log.error(String.format("流程id%s事件id%s,发送命令为%s,命令备注%s", sjlcDto.getLclx(), sjlcDto.getSjid(), mlpzDto.getMldm(), mlpzDto.getBz()));
                //如果机器人身上有EP管夹爪并且sjlc是更换EP管流程
                if ("1".equals(sjlcDto.getSfghep())) {
                    if(changeEpParam(parm,mlpzDto,sjlcDto)){
                        continue;
                    }
                }
                // 发送消息
                sendByCmdModel(mlpzDto.getXylx(), deviceID, mlpzDto.getMllx(), mlpzDto.getMldm(),
                        parm, callBack, "1".equals(mlpzDto.getSfyb()),
                        "1".equals(mlpzDto.getSfzhyb()), theadName, dyc,
                        dec, mlpzDto.getMlid(), ma, false, 1, sjlcDto.getLcid(), cusId, mlpzDto.getBz());
            }
        } catch (Exception e) {
            // new ExceptionHandlingUtil(jsonObject2);
            log.error(e.getMessage());
            return false;

        }

        return true;
    }

    /**
     * 更换EP管命令公共方法
     * @param parm
     * @param mlpzDto
     * @param sjlcDto
     */
    private boolean changeEpParam(String[] parm,MlpzDto mlpzDto,SjlcDto sjlcDto){
        boolean isReturn =false;
        //更换ep管载具 需要的EP管载具名字
        String params = sjlcDto.getParam();
        //更换ep管载具 需要的机器人身上EP管载具位置的名字
        String agvParams = sjlcDto.getAgvparam();
        List<String> paramList = new ArrayList<>();
        List<String> agvParamList = new ArrayList<>();
        //获取EP管载具MAP
        if (StringUtil.isBlank(params)) {
            paramList = null;
        } else if (params.contains(",")) {
            paramList = Arrays.asList(params.replaceAll("\\s+","").split(","));
        } else {
            paramList.add(params);
        }
        if (agvParams.contains(",")) {
            agvParamList = Arrays.asList(agvParams.replaceAll("\\s+","").split(","));
        } else {
            agvParamList.add(agvParams);
        }
        Map<String, Object> epMap = null;
        //取物料
        if (mlpzDto.getQybs().contains("t_")) {
            epMap = ChangeEpUtil.changeEpCommon_take(paramList,agvParamList);
        } else if (mlpzDto.getQybs().contains("d_")) {
            //放物料
            epMap = ChangeEpUtil.changeEpCommon_drop(paramList,agvParamList);
        }
        // t_ 取物料  d_放物料   _d 还夹爪  _t 取夹爪

        if ("t_d".equals(mlpzDto.getQybs())||  "d_d".equals(mlpzDto.getQybs())) {
            //TODO 判断当前夹爪是否一致
            if(StringUtil.isNotBlank(InstrumentMaterialGlobal.getEpClaw())){
                if (StringUtil.isNotBlank(InstrumentMaterialGlobal.getAvgQy()) && InstrumentMaterialGlobal.getAvgQy().equals(epMap.get("qy").toString())) {
                    //true 机器人身上有夹爪 false机器人身上没有夹爪
                    this.isTakeClaw = true;
                    isReturn=true;
                }else{
                    this.isTakeClaw = false;
                }
            }else{
                //第一次执行不执行放夹爪命令
                if ((StringUtil.isNotBlank(mlpzDto.getQybs()) && "t_d".equals(mlpzDto.getQybs())) || (StringUtil.isNotBlank(mlpzDto.getQybs()) && "d_d".equals(mlpzDto.getQybs()))) {
                    isReturn=true;
                }
            }


        }//不还夹爪，也不取夹爪
         else if ((StringUtil.isNotBlank(mlpzDto.getQybs()) && "t_t".equals(mlpzDto.getQybs())) || (StringUtil.isNotBlank(mlpzDto.getQybs()) && "d_t".equals(mlpzDto.getQybs()))) {
           if(StringUtil.isBlank(InstrumentMaterialGlobal.getEpClaw())){
               if (this.isTakeClaw) {
                   isReturn=true;
               }else{
                   this.isTakeClaw = true;
               }
           }else{
               isReturn=true;
           }

        }
        //_0 根据当前区域判断是否执行移动和拍照方法
        else if ((StringUtils.isNotBlank(mlpzDto.getQybs()) && mlpzDto.getQybs().contains("t_0")) || (StringUtils.isNotBlank(mlpzDto.getQybs()) && mlpzDto.getQybs().contains("d_0"))) {
            if (StringUtils.isNotBlank(InstrumentMaterialGlobal.getAvgQy()) && InstrumentMaterialGlobal.getAvgQy().equals(epMap.get("qy").toString())) {
                isReturn=true;
            } else {
                this.isTakeClaw = false;
                parm[0] = epMap.get("qy").toString();
            }
        }
        //通用事件，判断不同物料区，执行不同的命令
        else if (StringUtils.isNotBlank(mlpzDto.getQybs()) && mlpzDto.getQybs().contains("wl")) {
            if (( mlpzDto.getQybs().contains("t_" + epMap.get("qy").toString()))
                    || (mlpzDto.getQybs().contains("d_" + epMap.get("qy").toString()))) {
                String[] quArr = mlpzDto.getQybs().split("_");
                if (quArr[1].equals(epMap.get("qy").toString())) {
                    parm[0] = epMap.get("wlq").toString();
                } else {
                    isReturn=true;
                }
            } else {
                isReturn=true;
            }
        }
        //机器人身上EP管载具的位置
        else if ((StringUtil.isNotBlank(mlpzDto.getQybs()) && mlpzDto.getQybs().contains("t_avg")) || (StringUtil.isNotBlank(mlpzDto.getQybs()) && mlpzDto.getQybs().contains("d_avg"))) {
            parm[0] = epMap.get("desk").toString();
        }

        return isReturn;
    }
    /**
     * 获取参数方法
     * @param arr 数组参数
     * @param className 方法名称
     * @param mlpzDto
     * @param sjlcDto
     * @param ma
     * @return
     * @throws Exception
     */
    private String[] getArr(String[] arr, String className, MlpzDto mlpzDto, SjlcDto sjlcDto, Map<String, String> ma) throws Exception {
        //Class<?> clazz=Class.forName("com.matridx.las.netty.receivehandleimpl."+className);
        try {
            Object obj = SpringUtil.getBean(className);

            Method[] declaredMethods = obj.getClass().getDeclaredMethods();
            ;
            if (StringUtil.isNotBlank(mlpzDto.getMethodname())) {
                for (Method method : declaredMethods) {
                    if (mlpzDto.getMethodname().equals(method.getName())) {
                        Object invokeResult = method.invoke(obj, mlpzDto, sjlcDto, ma);
                        String paramLx = "";
                        paramLx = StringUtil.isBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
                        if (paramLx != null && paramLx.contains("agv")) {
                            arr = (String[]) invokeResult;
                        } else {
                            arr[0] = JSONObject.toJSONString(invokeResult);
                        }
                        break;
                    }
                }
            }
        }catch (Exception e){
            log.error(String.format("放置参数错误%s", e.getMessage()));
            log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
            log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
            log.error(String.format("出错命令id%s", mlpzDto.getMlid()));
            log.error(String.format("出错命令备注%s", mlpzDto.getBz()));
        }

        return arr;
    }

    public String getUnid() {
        return unid;
    }

    public void setUnid(String unid) {
        this.unid = unid;
    }
}
