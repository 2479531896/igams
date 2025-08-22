package com.matridx.igams.wechat.util;

import com.matridx.igams.wechat.service.impl.SendConfirmMessage;

public class SendConfirmMessageThread extends Thread{
private SendConfirmMessage sendConfirmMessage;

	public SendConfirmMessageThread(SendConfirmMessage sendConfirmMessage){
		this.sendConfirmMessage = sendConfirmMessage;
		
	}

	@Override
	public void run() {
		sendConfirmMessage.sendConfirmMessage();
	}
}
