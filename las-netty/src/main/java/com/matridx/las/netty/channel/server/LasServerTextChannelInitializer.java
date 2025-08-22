package com.matridx.las.netty.channel.server;

import com.matridx.las.netty.channel.server.handler.*;

import io.netty.handler.ipfilter.RuleBasedIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.codec.ObjDecoder;
import com.matridx.las.netty.channel.codec.ObjEncoder;
import com.matridx.las.netty.channel.codec.StrEncoder;
import com.matridx.las.netty.util.CommonChannelUtil;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;

@Service
@Sharable
public class LasServerTextChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	@Autowired
	CubicsProtocolHandler cubicsHandler;
	
	@Autowired
	AutoProtocolHandler autoHandler;
	
	@Autowired
	PcrProtocolHandler pcrHandler;
	
	@Autowired
	SeqProtocolHandler seqHandler;
	
	@Autowired
	AvgProtocolHandler avgHandler;
	@Autowired
	CmhProtocolHandler cmhHandler;
	@Autowired
	IpFilterRuleHandler ipFilterRuleHandler;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline = ch.pipeline();
		//RuleBasedIpFilter ruleBasedIpFilter = new RuleBasedIpFilter(false,ipFilterRuleHandler);
		//pipeline.addLast(ruleBasedIpFilter);
		// 基于换行符号
		pipeline.addLast(new ObjDecoder());
		// 管理channel上线下线
		pipeline.addLast(new CommonProtocolHandler());
		
		//CubicsProtocolHandler cubicsHandler = new CubicsProtocolHandler();
		CommonChannelUtil.addProtocol("1", cubicsHandler);
		pipeline.addLast(cubicsHandler);
		//AutoProtocolHandler autoHandler = new AutoProtocolHandler();
		CommonChannelUtil.addProtocol("2", autoHandler);
		pipeline.addLast(autoHandler);
		//PcrProtocolHandler pcrHandler = new PcrProtocolHandler();
		CommonChannelUtil.addProtocol("3", pcrHandler);
		pipeline.addLast(pcrHandler);
		//SeqProtocolHandler seqHandler = new SeqProtocolHandler();
		CommonChannelUtil.addProtocol("4", seqHandler);
		pipeline.addLast(seqHandler);
		//AvgProtocolHandler avgHandler = new AvgProtocolHandler();
		CommonChannelUtil.addProtocol("5", avgHandler);
		pipeline.addLast(avgHandler);
		CommonChannelUtil.addProtocol("6", cmhHandler);
		pipeline.addLast(cmhHandler);
		pipeline.addLast(new DefaultProtocolHandler());
		
		pipeline.addLast(new StrEncoder());
		pipeline.addLast(new ObjEncoder());
	}

}
