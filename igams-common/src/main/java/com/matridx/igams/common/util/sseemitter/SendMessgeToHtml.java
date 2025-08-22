package com.matridx.igams.common.util.sseemitter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SendMessgeToHtml {

    private static final Logger logger = LoggerFactory.getLogger(SendMessgeToHtml.class);
    private static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public static void pushMessage(String commd) {
        Runnable newRunnable = new Runnable() {
            public void run() {
                pushMessageUtil(commd);
            }
        };
        cachedThreadPool.execute(newRunnable);
    }

    public static void pushMessageUtil(String commd) {

    }

}
