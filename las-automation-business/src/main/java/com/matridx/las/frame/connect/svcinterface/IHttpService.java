package com.matridx.las.frame.connect.svcinterface;

import java.util.Map;

import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.util.ChannelModel;

/**
 * 通用通道类，不再限于Netty的通道类，而是扩展为所有的通道
 */
public interface IHttpService extends IConnectService {
    /**
     * 发送消息
     * @return
     */
    boolean sendMessage(FrameModel frameModel,String info);
    
    /**
     * 获取通道信息
     * @return
     */
    Object getChannel();

    /**
     * 信息初始化
     * @param map
     * @return
     */
    boolean init(Map<String,Object> map);

    /**
     * 设置通道信息到服务类里
     * @param channelModel
     */
    void setChannelModel(ChannelModel channelModel);
}
