package com.matridx.las.frame.netty.channel.server;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Component
public class LasNettyTextServer {
	
	@Value("${matridx.netty.port:8094}")
	private int port;
	
	@Value("${matridx.netty.boss_size:1}")
	private int bossSize;
	
	@Value("${matridx.netty.work_size:200}")
	private int workSize;
	
	@Value("${matridx.systemflg.hostip:127.0.0.1}")
	private String hostip;
	
	@Autowired
	LasServerTextChannelInitializer lasServerTextInit;
	
	Logger log = LoggerFactory.getLogger(LasNettyTextServer.class);
	
	private Channel serverChannel;

    public void start() throws Exception {
        
        // TODO Auto-generated method stub
 		//用来接收进来的连接,接收到连接，就会把连接信息注册到‘worker’上
 		EventLoopGroup bossGroup = new NioEventLoopGroup(bossSize);
 		//用来处理已经被接收的连接
 		EventLoopGroup workGroup = new NioEventLoopGroup(workSize);
 		
 		try {
 			ServerBootstrap sBootstrap = new ServerBootstrap();
 			sBootstrap.group(bossGroup, workGroup)
 			//举例说明一个新的 Channel 如何接收进来的连接
 			.channel(NioServerSocketChannel.class)
 			//目的是帮助使用者配置一个新的 Channel
 			.childHandler(lasServerTextInit)
 			//设置队列大小
 			.option(ChannelOption.SO_BACKLOG, 128)
 			.option(ChannelOption.SO_RCVBUF, 20*1024*1024)
 			.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))
 			// 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
 			.childOption(ChannelOption.SO_KEEPALIVE, true);
 			//绑定端口,开始接收进来的连接
 			ChannelFuture future = sBootstrap.bind(port).sync();
 			
 			this.serverChannel = future.channel();
 			
 			log.error("Netty的文本服务器启动开始监听端口: {}", port);
 			// 等待服务器  socket 关闭 。
 			// 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
 			serverChannel.closeFuture().sync();
 			
 			log.error("Netty文本服务器关闭？");
 			
 		}catch (Exception e) {
 			// TODO: handle exception
 			log.error(e.getMessage());
 		}finally {
 			//关闭主线程组
 			workGroup.shutdownGracefully();
 			//关闭工作线程组
 			bossGroup.shutdownGracefully();
 			log.error("关闭 Netty BootsrapRunner");
 		}
    }

    @PreDestroy
    public void stop() throws Exception {
    	if(serverChannel!=null) {
	        serverChannel.close();
	        serverChannel.parent().close();
    	}
    }

}
