package com.matridx.igams.detection.molecule.util;

import com.matridx.igams.detection.molecule.service.impl.AliHealthGetOrder;

public class AliHealthOrderThread extends Thread{
private final AliHealthGetOrder aliHealthGetOrder;

	public AliHealthOrderThread(AliHealthGetOrder aliHealthGetOrder){
		this.aliHealthGetOrder = aliHealthGetOrder;
		
	}

	@Override
	public void run() {
		aliHealthGetOrder.getOrderDetails();
	}
}
