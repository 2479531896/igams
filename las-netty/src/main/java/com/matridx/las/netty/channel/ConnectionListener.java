package com.matridx.las.netty.channel;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import lombok.SneakyThrows;

public class ConnectionListener implements ChannelFutureListener{

	private NettyTextClient nettyClient;
	
	public ConnectionListener(NettyTextClient nettyClient) { 
		this.nettyClient = nettyClient;
		
	}
	
	@Override
	public void operationComplete(ChannelFuture channelFuture) throws Exception {
		// TODO Auto-generated method stub
		if(!channelFuture.isSuccess()) {
			System.out.println("----客户端重连----");
			final EventLoop loop = channelFuture.channel().eventLoop();
			loop.schedule(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    nettyClient.connect("localhost",8094);
                }
            }, 5, TimeUnit.SECONDS);
			
		}
	}

}
