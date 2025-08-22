package com.matridx.igams.wechat.util;

import com.matridx.igams.wechat.service.impl.SendPCRAudit;

public class SendPCRAuditThread extends Thread{
private SendPCRAudit sendPCRAudit;

	public SendPCRAuditThread(SendPCRAudit sendPCRAudit){
		this.sendPCRAudit = sendPCRAudit;
		
	}

	@Override
	public void run() {
		sendPCRAudit.sendPCRAudit();
	}
}
