package com.matridx.las.netty.channel.server;

import java.util.ArrayList;
import java.util.List;

import com.matridx.las.netty.channel.server.handler.HttpRequestHandler;
import com.matridx.las.netty.channel.server.handler.TextWebSocketFrameHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class LasServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline = ch.pipeline();
		//对象传输处理[解码]
		/*pipeline.addLast(new ObjDecoder());
		pipeline.addLast(new MatridxProtocolHandler());
		pipeline.addLast(new TextProtocolHandler());
		pipeline.addLast(new ObjEncoder());
		*/
		pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(64*1024));
        pipeline.addLast(new ChunkedWriteHandler());
		List<String> exceptList = new ArrayList<String>();
		exceptList.add("/ws");
        pipeline.addLast(new HttpRequestHandler(exceptList));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new TextWebSocketFrameHandler());
        
	}

}
