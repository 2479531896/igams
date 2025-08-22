package com.matridx.las.frame.netty.channel.server.handler;

import com.matridx.las.frame.connect.util.ConnectUtil;
import com.matridx.springboot.util.base.StringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.netty.channel.ChannelHandler.Sharable;
import com.matridx.las.frame.connect.channel.service.NettyChannelService;
import com.matridx.las.frame.connect.svcinterface.IConnectService;
import com.matridx.las.frame.connect.util.CommonChannelUtil;
import com.matridx.las.frame.netty.channel.domain.protocol.DefaultProtocol;

/**
 * 基础协议类  框架协议类 -》BaseProtocolHandler -》DefaultProtocolHandler
 * 													MatridxProtocolHandler -》CommonProtocolHandler，PcrProtocolHandler等
 * 
 * 由基础协议类对仪器设备的上线和下线信息进行处理（也可采用 extends的基础类进行。当前考虑信息内容分开)
 * 
 * 
 * @author linwu
 *
 */
@Service
@Sharable
public class DefaultProtocolHandler extends BaseProtocolHandler<DefaultProtocol> {
	// 系统区域设置
	@Value("${matridx.netty.area:01}")
	private String netty_area;
	// 是否桥接
	@Value("${matridx.netty.bridgingflg:false}")
	private boolean bridgingflg;
	//系统名称
	@Value("${matridx.netty.name:}")
	private String netty_name;
	
	Logger log = LoggerFactory.getLogger(DefaultProtocolHandler.class);
	//广播用通道列表
	//public ChannelGroup socketChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DefaultProtocol msg) throws Exception {
		log.error(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " DefaultProtocolHandler 接收消息的处理器：" + this.getClass().getName() + " 消息内容：" + msg.getInfo());
		
		//这里主要做的是消息通知，对相应消息进行广播，用于聊天使用，可以屏蔽
		/*for (Channel channel : socketChannels) {
			if (channel != incoming){
				channel.writeAndFlush(new DefaultProtocol(msg.getInfo()));
			} else {
				channel.writeAndFlush(new DefaultProtocol("[you] "+msg.getInfo()));
			}
		}*/
	}

	/**
	 * 由基础协议类对仪器设备的上线和下线信息进行处理（也可采用 extends的基础类进行。当前考虑信息内容分开)
	 * 在channelActive：channel().isActive()总是正确的  只有当channel().isActive()为true时，才能向另一方发送消息
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();

		NettyChannelService nettychannel = new NettyChannelService();
		nettychannel.setChannel(incoming);

		InetSocketAddress ipSocket = (InetSocketAddress)incoming.remoteAddress();
		String clientIp = ipSocket.getAddress().getHostAddress();
		
		boolean result =  CommonChannelUtil.addChannel(netty_area,clientIp,nettychannel);

		log.info("客户端ip地址：{} netty_name:{}",clientIp,netty_name);
		if(result) {
			log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " Default:"+incoming.remoteAddress() +" 在线");
		}else{
			log.error(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " Default:"+incoming.remoteAddress() +" 重复在线");
		}
		//如果桥接，则发送信息到主服务器上
		if(bridgingflg) {
			Map<String, String> paramMap= new HashMap<>();
			paramMap.put("netty_name", StringUtil.isNotBlank(netty_name)?netty_name:netty_area);
			paramMap.put("netty_area", netty_area);
			paramMap.put("clientIp", clientIp);
			//发送信息到主服务器
			ConnectUtil.mainChannelActive(paramMap);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		NettyChannelService nettychannel = new NettyChannelService();
		nettychannel.setChannel(incoming);

		InetSocketAddress ipSocket = (InetSocketAddress)incoming.remoteAddress();
		String clientIp = ipSocket.getAddress().getHostAddress();
		IConnectService channelService = CommonChannelUtil.removeChannel(netty_area,clientIp,nettychannel);
		//如果桥接，则发送信息到主服务器上
		if(bridgingflg) {
			Map<String, String> paramMap= new HashMap<>();
			paramMap.put("netty_name", StringUtil.isNotBlank(netty_name)?netty_name:netty_area);
			paramMap.put("netty_area", netty_area);
			paramMap.put("clientIp", clientIp);
			if(channelService != null) {
				paramMap.put("commanddeviceid", channelService.getChannelModel().getCommanddeviceid());
				paramMap.put("protocol", channelService.getChannelModel().getProtocol());
			}
			//发送信息到主服务器
			ConnectUtil.mainChannelInactive(paramMap);
		}
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " Default:"+incoming.remoteAddress()+"掉线");
	}
}
