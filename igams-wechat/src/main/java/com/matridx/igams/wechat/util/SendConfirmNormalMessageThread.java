package com.matridx.igams.wechat.util;

import com.matridx.igams.wechat.service.impl.SendConfirmMessage;

public class SendConfirmNormalMessageThread extends Thread{
	private SendConfirmMessage sendConfirmMessage;

	public SendConfirmNormalMessageThread(SendConfirmMessage sendConfirmMessage){
		this.sendConfirmMessage = sendConfirmMessage;
		
	}

	@Override
	public void run() {
		sendConfirmMessage.sendConfirmNormalMessage();
	}
}
