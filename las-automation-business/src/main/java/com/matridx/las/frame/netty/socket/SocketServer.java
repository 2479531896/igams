package com.matridx.las.frame.netty.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    private static Logger log = LoggerFactory.getLogger(SocketServer.class);
    public void createSocketServer()  {
        try {
            // 创建ServerSocket对象，指定监听的端口号
            ServerSocket serverSocket = new ServerSocket(8089);
            // 监听客户端的连接请求
            Socket clientSocket = serverSocket.accept();
            // 获取输入输出流
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String message;

            while (true) {
                // 读取客户端发送的信息
                message = in.readLine();

                if (message.equalsIgnoreCase("exit")) {
                    // 如果接收到终止标志，退出循环
                    break;
                }

                System.out.println("收到客户端消息：" + message);

                // 发送响应给客户端
                out.println("已收到你的消息：" + message);
            }

            // 关闭连接
            clientSocket.close();
            serverSocket.close();

        }catch (Exception e){
            log.error("socket连接所谓");
        }
    }
}
