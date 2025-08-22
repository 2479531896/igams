package com.matridx.igams.common.chatai;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import com.matridx.igams.common.redis.RedisUtil;
import com.taobao.api.ApiException;
import io.reactivex.Flowable;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class QianWenChat {

    private Logger log = LoggerFactory.getLogger(QianWenChat.class);
    /**
     * 多轮对话
     * @param hhid  会话ID
     * @param str 会话问题
     * @return
     * @throws NoApiKeyException
     * @throws ApiException
     * @throws InputRequiredException
     */
    public Map<String,Object> callWithMessage(String hhid, String str, String apikey, RedisUtil redisUtil) throws NoApiKeyException, ApiException, InputRequiredException{
        Map<String,Object> map=new HashMap<>();
        /* 
        Constants.apiKey=apikey;
        Generation gen = new Generation();
        MessageManager msgManager = new MessageManager(10);//限制10轮
        Message systemMsg =Message.builder().role(Role.SYSTEM.getValue()).content("You are a helpful assistant.").build();
        List<Object> above=new ArrayList<>();
        //从redis根据会话ID获取上下文内容
        if(StringUtil.isNotBlank(hhid)){
            above=(List<Object>)redisUtil.get("qianwen.problem:"+hhid);

            if(!CollectionUtils.isEmpty(above)) {
                String str_above = String.valueOf(above.get(above.size() - 1));//获取最近的一条会话内容
                systemMsg=Message.builder().role(Role.SYSTEM.getValue()).content(str_above).build();
            }
        }
        //正文
         
        Message userMsg = Message.builder().role(Role.USER.getValue()).content(str).build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        GenerationParam param =
                GenerationParam.builder().model("qwen2-72b-instruct").messages(msgManager.get())
                        .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                        .topP(0.8)
                        .incrementalOutput(true)
                        .build();
        Flowable<GenerationResult> result = gen.streamCall(param);
        StringBuilder fullContent = new StringBuilder();
        result.blockingForEach(item->{
            fullContent.append(item.getOutput().getChoices().get(0).getMessage().getContent());
        });
        if(above==null)
            above=new ArrayList<>();
        above.clear();
        above.add(fullContent);
        hhid=(StringUtil.isBlank(hhid)?com.matridx.springboot.util.base.StringUtil.generateUUID():hhid);
        redisUtil.set("qianwen.problem:"+hhid,above,1800);//设置30分钟过期
        map.put("content",fullContent.toString());
        map.put("hhid",hhid);
        */
        return map;
    }

}
