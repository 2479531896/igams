package com.matridx.las.netty.channel.server.handler;

import java.util.List;

import org.springframework.stereotype.Service;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

/**
 * 处理普通网页请求
 * @author linwu
 *
 */

public class HttpRequestHandler extends BaseProtocolHandler<FullHttpRequest>{

	private final List<String> exceptList;
	private WebSocketServerHandshaker handshaker;
	
	public HttpRequestHandler(List<String> exceptList) {
		this.exceptList = exceptList;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		// TODO Auto-generated method stub
		// 转发到下一个 ChannelInboundHandler。retain() 是必要的，因为 channelRead() 完成后，
		//它会调用 FullHttpRequest 上的 release() 来释放其资源。
		boolean isExcept = false;
		if(exceptList!=null && exceptList.size() >0) {
			for (int i = 0; i < exceptList.size(); i++) {
				if(exceptList.get(i).equalsIgnoreCase(request.getUri())) {
					isExcept =true;
					break;
				}
			}
		}
		if (isExcept) {
			//handshaker.handshake(ctx.channel(), request);
			ctx.fireChannelRead(request.retain());				  //2
		} else {
			//普通网页处理
			//处理消息
			ctx.writeAndFlush("");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		Channel incoming = ctx.channel();
		System.out.println("Client:"+incoming.remoteAddress()+"异常");
		// 当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}
}
