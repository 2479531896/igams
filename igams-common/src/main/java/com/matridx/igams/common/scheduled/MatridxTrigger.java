package com.matridx.igams.common.scheduled;

import java.util.Date;

import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.Trigger;
import com.matridx.igams.common.dao.entities.DsrwszDto;
import com.matridx.igams.common.service.svcinterface.IDsrwszService;

public class MatridxTrigger implements Trigger{
	
	private final IDsrwszService dsrwszService;
	
	private final DsrwszDto dsrwszDto;
	
	public MatridxTrigger(IDsrwszService t_dsrwszService,DsrwszDto t_dsrwszDto) {
		dsrwszService = t_dsrwszService;
		dsrwszDto = t_dsrwszDto;
	}
	
	@Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
    	
    	DsrwszDto t_dDsrwszDto = dsrwszService.getDto(dsrwszDto);
    	if(t_dDsrwszDto==null)
    		return null;
        // 触发器
        CronTrigger trigger = new CronTrigger(t_dDsrwszDto.getDsxx());
        return trigger.nextExecutionTime(triggerContext);
    }
}
