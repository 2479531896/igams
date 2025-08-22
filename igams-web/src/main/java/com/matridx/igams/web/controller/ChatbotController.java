package com.matridx.igams.web.controller;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import io.reactivex.Flowable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : 郭祥杰
 * @date :
 */
@RestController
@RequestMapping("/ws")
public class ChatbotController {
	
	private Logger log = LoggerFactory.getLogger(ChatbotController.class);
	
    @Value("${matridx.aliyunBL.appId:}")
    private String appId;
    @Value("${matridx.aliyunBL.apiKey:}")
    private String apiKey;
    /**
     * 创建线程池
     **/
    ThreadPoolExecutor executor = new ThreadPoolExecutor(10,20,60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );
    /**
     * 实现 chat 接口，支持流式返回数据
     *
     * @param query
     * @return
     */
    @RequestMapping(value = "/chatBot/chat", method = RequestMethod.POST)
    public ResponseBodyEmitter streamData(@RequestBody String query) {
    	try {
	        ResponseBodyEmitter emitter = new ResponseBodyEmitter(180000L);
	        executor.execute(() -> {
	            try {
	                JsonObject jsonObject = new JsonParser().parse(query).getAsJsonObject();
	                streamCall(emitter, jsonObject.get("prompt").getAsString());
	            } catch (NoApiKeyException | InputRequiredException e) {
	                e.printStackTrace();
	                emitter.completeWithError(e);
	            }
	        });
	        return emitter;
    	}catch(Exception e) {
    		log.error(e.getMessage());
    	}
    	log.error("网页助手未正常运行，输入信息未：" + query);
    	return null;
    }
    /**
     * 调用百炼应用，封装流式返回数据
     * 返回数据格式
     * id:1
     * event:result
     * :HTTP_STATUS/200
     * data:{"output":{"session_id":"xxx","finish_reason":"null","text":"相关的问题"}}
     *
     * @param emitter
     * @param query
     * @throws NoApiKeyException
     * @throws InputRequiredException
     */
    public void streamCall(ResponseBodyEmitter emitter, String query) throws NoApiKeyException, InputRequiredException {
        // appId 填入百炼应用 ID
        DBEncrypt p = new DBEncrypt();
        ApplicationParam param = ApplicationParam.builder()
                .appId(p.dCode(appId))
                .apiKey(p.dCode(apiKey))
                .prompt(query)
                .incrementalOutput(true)
                .build();

        Application application = new Application();
        Flowable<ApplicationResult> result = application.streamCall(param);
        AtomicInteger counter = new AtomicInteger(0);
        result.blockingForEach(data -> {
            int newValue = counter.incrementAndGet();
            String resData = "id:" + newValue + "\nevent:result\n:HTTP_STATUS/200\ndata:" + new Gson().toJson(data) + "\n\n";
            emitter.send(resData.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            if ("stop".equals(data.getOutput().getFinishReason())) {
                emitter.complete();
            }
        });

    }
}
