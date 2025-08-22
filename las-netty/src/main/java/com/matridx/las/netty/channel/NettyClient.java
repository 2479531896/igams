package com.matridx.las.netty.channel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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

public class NettyClient {
	public static void main(String[] args){

 		//用来处理已经被接收的连接
		EventLoopGroup workGroup = new NioEventLoopGroup();
 		
 		try {
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
// 					pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,4,0,4));
// 					pipeline.addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()));
// 					pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
// 					pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
 					pipeline.addLast(new StrEncoder());
 					pipeline.addLast("handler", new SimpleChannelInboundHandler<ByteBuf>()	{
 						@Override
 					    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf s) throws Exception {
 					        System.out.println("接收信息： " + s.toString(CharsetUtil.UTF_8));
 					       ctx.channel().writeAndFlush("接受到信息handler："+s);
 						}
 					});
 				}
			});
 			int port = 8094;
 			if(args!= null&& args.length > 0 )
 			{
 				port = Integer.parseInt(args[0]);
 			}
 			Channel channel = Bootstrap.connect("localhost",port).sync().channel();
// 			channel.config().setWriteBufferHighWaterMark(1024*1024*8);
 			channel.writeAndFlush("1&S01001RZS RZ ");
 			channel.writeAndFlush("1&S01001SRS START {\"result\":[{\"wz\":\"1,1\",\"ybbh\":\"11cs\",\"jcdw\":\"\"}],\"backdata\":\"djcsy:1,wkid:null,ispcr:1,djc:1\"}");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                channel.writeAndFlush(in.readLine());
            }
 			
 		}catch (Exception e) {
 			System.out.print(e.getMessage());
 		}finally {
 			//关闭主线程组
 			workGroup.shutdownGracefully();
 			System.out.print("finally");
 		}
    }
}
