package com.matridx.igams.wechat.service.svcinterface;

import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface ISendMessageService {
    /**
     * @Description: 发送消息
     * @param message
     * @return Map<String,Object>
     * @Author: 郭祥杰
     * @Date: 2025/4/27 14:37
     */
    Map<String, Object> sendMessage(String message);
}
