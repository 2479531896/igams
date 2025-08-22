package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.DxglDto;
import com.matridx.igams.common.dao.entities.DxglModel;

import java.util.Map;

public interface IDxglService extends BaseBasicService<DxglDto, DxglModel>{
    /**
     * 更新短信管理信息
     */
    boolean updateDxgl(DxglDto dxglDto);

    /**
     * 发送短信验证码（调用发送短信接口）
     * @param sjh 手机号
     * @param yzm 验证码
     * @param signName 配置文件 signName
     * @param templateCode 配置文件 templateCode
     * @param time 发送间隔时间 单位秒
     * @param fscs 限制每天发送次数
     */
    Map<String,Object> sendCode(String sjh, String yzm, String signName, String templateCode, String time, String fscs);

    /**
     * 检查验证码
     * @param sjh 手机号
     * @param yzm 验证码
     * @param time 验证码过期时间
     */
    Map<String,Object> checkCode(String sjh, String yzm, String time);

    /**
     * 发送短信消息（调用发送短信接口）
     * @param sjh 手机号
     * @param templateParam 消息内容(组装后的)
     * @param signName 配置文件 signName
     * @param templateCode 配置文件 templateCode
     */
    Map<String,Object> sendMsgDetection(String sjh,String templateParam, String signName, String templateCode);

    /**
     * 发送短信消息（替换消息内容调用发送短信接口）
     * @param phoneNum 手机号
     * @param msgId 消息id
     * @param msgMap 消息内容
     * @param signName 配置文件 signName
     * @param templateCode 配置文件 templateCode
     */
    Map<String,Object> sendReplaceMsg(String phoneNum, String msgId, Map<String,Object> msgMap, String signName, String templateCode);
}
