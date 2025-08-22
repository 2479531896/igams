package com.matridx.las.netty.channel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.matridx.las.netty.channel.codec.StrEncoder;
import com.matridx.las.netty.util.CommonChannelUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class NettyTestClient {
	
    public String host = "127.0.0.1"; // ip地址
    public int port = 8094; // 端口
    
    // 通过nio方式来接收连接和处理连接
    private EventLoopGroup group = new NioEventLoopGroup();
    
    public static NettyTestClient nettyTestClient = new NettyTestClient();

    /**唯一标记 */
    private boolean initFalg = true;

    public static void main(String[] args) {
    	nettyTestClient.run();
    }
    
    /**
     * Netty创建全部都是实现自AbstractBootstrap。 客户端的是Bootstrap，服务端的则是 ServerBootstrap。
     **/
    public void run() {
    	connect(new Bootstrap(), group);
    }
	
	public void connect(Bootstrap bootstrap, EventLoopGroup eventLoopGroup) {
		
		ChannelFuture f = null;
		try {
			if (bootstrap != null) {
				bootstrap.group(eventLoopGroup);
				//举例说明一个新的 Channel 如何接收进来的连接
				bootstrap.channel(NioSocketChannel.class);
				bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
				// 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// TODO Auto-generated method stub
	 					ChannelPipeline pipeline = ch.pipeline();
	 					
	 					// 基于换行符号
	 					// pipeline.addLast(new LineBasedFrameDecoder(64*1024));
	 					pipeline.addLast(new StrEncoder());
	 					pipeline.addLast(new SimpleChannelInboundHandler<ByteBuf>()	{
	 					 	@Override
	 					    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf s) throws Exception {
	 					 		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"服务端发来的消息："+s.toString(CharsetUtil.UTF_8));
	 					 		String a = s.toString(CharsetUtil.UTF_8);
	 					 		if(a.contains("S") && a.contains("AB")) {
	 					 			new Thread(new Runnable() {
	 					 				@Override
	 					 				public void run() {
	 					 					try {
												TimeUnit.SECONDS.sleep(10);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
	 					 					System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"AB准备回调！！");
	 					 					String testMsg1 = "5&R005OwS RTR";
	 		 	 					 		//ctx.channel().writeAndFlush(testMsg1);
	 		 	 					 		
	 		 	 				 			//String testMsg2 = "5&R005OWS AB ok";
	 		 	 				 		//	ctx.channel().writeAndFlush(testMsg2);
	 		 	 				 			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"AB回调结束！！");
	 					 				}
	 					 			}).start();
	 					 		}
	 					 		if(a.contains("S") && a.contains("CD")) {
	 					 			new Thread(new Runnable() {
	 					 				@Override
	 					 				public void run() {
	 					 					try {
												TimeUnit.SECONDS.sleep(5);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
	 					 					System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"CD准备回调！！");
	 					 					String testMsg1 = "2&R002CDS RTR";
	 		 	 					 		//ctx.channel().writeAndFlush(testMsg1);
	 		 	 					 		
	 		 	 				 			String testMsg2 = "2&R002CDS CD ok";
	 		 	 				 			//ctx.channel().writeAndFlush(testMsg2);
	 		 	 				 			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"CD回调结束！！");
	 					 				}
	 					 			}).start();
	 					 		}
	 					 		ctx.flush();
	 					    }
	 					 	
	 					    @Override
	 					    public void channelActive(ChannelHandlerContext ctx) throws Exception {
	 					        Channel incoming = ctx.channel();
	 					        String testMsg = "2&S002RZA RZ"; 
	 					        incoming.writeAndFlush(testMsg);
	 					        ctx.flush();
	 					        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "发送注册消息！");
	 					    }
	 					    
	 						/**
	 						 * 处理断开重连
	 						 */
	 						@Override
	 						public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	 							System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" 与服务器连接断开，尝试重新连接...");
	 						    // 立即重连
	 						    connect(new Bootstrap(), eventLoopGroup);  
	 							super.channelInactive(ctx);  
	 						}
	 						
	 						@Override
	 						public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
	 							if (evt instanceof IdleStateEvent) {
	 								IdleStateEvent event = (IdleStateEvent) evt;
	 								System.out.println(ctx.channel().remoteAddress() + "，超时类型：" + event.state());
	 								// sendPingMsg(ctx);
	 							} else {
	 								super.userEventTriggered(ctx, evt);
	 							}
	 						}
	 						
	 					});
					}
                });
                bootstrap.remoteAddress(host, port);
                f = bootstrap.connect()
                	.addListener((ChannelFuture futureListener) -> {
	                    final EventLoop eventLoop = futureListener.channel().eventLoop();
	                    if (!futureListener.isSuccess()) {
	                        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" 与服务端断开连接!在10s之后准备尝试重连!");
	                        eventLoop.schedule(() -> connect(new Bootstrap(), eventLoop), 10, TimeUnit.SECONDS);
	                    }else {
	                    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" 与服务端连接成功!");
	                    }
	                });
                if(initFalg){
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" Netty客户端启动成功!");
                    initFalg = false;
                }
                Channel channel = f.sync().channel();
     			// String testMsg1 = "1&测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 结束！";
     			// channel.writeAndFlush(testMsg1);
     			// channel.writeAndFlush(MsgUtil.buildAutoProtocol(channel.id().toString(), "Auto协议。消息长度比较长的测试，最小应该要分成几帧的情况才可以"));
     			// channel.writeAndFlush(MsgUtil.buildCubicsProtocol(channel.id().toString(), "Cubics协议。消息长度比较长的测试，最小应该要分成几帧的情况才可以"));
     			
     			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                // 阻塞
     			while(true){
                    channel.writeAndFlush(in.readLine() + "\r\n");
                }
//     			// 阻塞
//                f.sync().channel().closeFuture();
            }
        } catch (Exception e) {
            System.out.println("客户端连接失败!"+e.getMessage());
        }
//		finally {
// 			//关闭主线程组
//        	eventLoopGroup.shutdownGracefully();
// 			System.out.print("finally");
// 		}
	}
}
