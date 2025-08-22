package com.matridx.las.netty.channel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.matridx.las.netty.channel.codec.StrEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class NettyTextClient {
	public static void main(String[] args){
		new NettyTextClient("localhost", 8094);
    }
	
	NettyTextClient(String host, int port){
		connect(host, port);
	}
	
	public void connect(String host, int port) {
 		//用来处理已经被接收的连接
		EventLoopGroup workGroup = new NioEventLoopGroup();
 		
 		/*try {
 			Bootstrap Bootstrap = new Bootstrap();
 			Bootstrap.group(workGroup)
 			//举例说明一个新的 Channel 如何接收进来的连接
 			.channel(NioSocketChannel.class)
 			// 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
 			.handler(new ChannelInitializer<SocketChannel>() {
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
 					 		if(a.contains("S")) {
								String testMsg1 = "1&R009OWS RTR";
 	 					 		//ctx.channel().writeAndFlush(testMsg1);
 	 					 		
 	 				 			String testMsg2 = "1&R009OWS AB ok"; 
 	 				 			//ctx.channel().writeAndFlush(testMsg2);
 					 		}
 					    }
 					});
 				}
			});
 			
 			Channel channel = Bootstrap.connect(host, port)
 					.addListener(new ConnectionListener(this))
 					.sync().channel();
 			
 			// String testMsg1 = "1&测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！测试消息！ 结束！";
 			// channel.writeAndFlush(testMsg1);
 			// channel.writeAndFlush(MsgUtil.buildAutoProtocol(channel.id().toString(), "Auto协议。消息长度比较长的测试，最小应该要分成几帧的情况才可以"));
 			// channel.writeAndFlush(MsgUtil.buildCubicsProtocol(channel.id().toString(), "Cubics协议。消息长度比较长的测试，最小应该要分成几帧的情况才可以"));
 			 String testMsg = "2&S009RZA RZ"; 
 			channel.writeAndFlush(testMsg);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                channel.writeAndFlush(in.readLine() + "\r\n");
            }
 			
 		}catch (Exception e) {
 			System.out.print(e.getMessage());
 		}finally {
 			//关闭主线程组
// 			workGroup.shutdownGracefully();
 			System.out.print("finally");
 		}*/
	}
}
