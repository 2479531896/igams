package com.matridx.las.frame.netty.channel.server;

import com.matridx.las.frame.netty.channel.server.handler.*;

import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.las.frame.connect.util.CommonChannelUtil;
import com.matridx.las.frame.netty.channel.codec.ObjDecoder;
import com.matridx.las.frame.netty.channel.codec.ObjEncoder;
import com.matridx.las.frame.netty.channel.codec.StrEncoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.ArrayList;
import java.util.List;

@Service
@Sharable
public class LasServerTextChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	@Autowired
	CubicsProtocolHandler cubicsHandler;
	@Autowired
	PcrProtocolHandler pcrHandler;
	@Autowired
	CommonProtocolHandler commonHandler;
	@Autowired
	DefaultProtocolHandler defaultHandler;
	
	@Autowired
	IpFilterRuleHandler ipFilterRuleHandler;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline = ch.pipeline();

		// 管理channel上线下线
		// 基于换行符号
		pipeline.addLast(new ObjDecoder());
		pipeline.addLast(defaultHandler);
		CommonChannelUtil.addProtocol("0", commonHandler);
		pipeline.addLast(commonHandler);
		CommonChannelUtil.addProtocol("1", cubicsHandler);
		pipeline.addLast("cubicsHandler",cubicsHandler);
		CommonChannelUtil.addProtocol("3", pcrHandler);
		pipeline.addLast("pcrHandler",pcrHandler);

		pipeline.addLast(new StrEncoder());
		pipeline.addLast(new ObjEncoder());
		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(64*1024));
		pipeline.addLast(new ChunkedWriteHandler());
		List<String> exceptList = new ArrayList<String>();
		exceptList.add("/ws");
		pipeline.addLast(new HttpRequestHandler(exceptList));
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		pipeline.addLast("websocket",new TextWebSocketFrameHandler());

	}

}
