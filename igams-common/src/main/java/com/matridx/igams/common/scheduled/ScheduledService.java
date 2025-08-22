package com.matridx.igams.common.scheduled;

import java.util.Arrays;
import java.util.List;

import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import com.matridx.igams.common.dao.entities.DsrwszDto;
import com.matridx.igams.common.service.svcinterface.IDsrwszService;

//废弃 2023-06-02 因为没有调度器功能，故改成CronTaskRegistrar 处理
public class ScheduledService implements SchedulingConfigurer{
	@Value("${matridx.systemflg.remindflg:}")
	private String remindflg;

	@Value("${matridx.systemflg.remindtype:}")
	private String remindtype;
	final static Logger logger = LoggerFactory.getLogger(ScheduledService.class);
	
	//使同一个线程中串行执行,当定时任务增多，如果一个任务卡死，会导致其他任务也无法执行。
	
	
	/*
	* 第一位，表示秒，取值0-59
	* 第二位，表示分，取值0-59
	* 第三位，表示小时，取值0-23
	* 第四位，日期天/日，取值1-31
	* 第五位，日期月份，取值1-12
	* 第六位，星期，取值1-7，星期一，星期二...，注：不是第1周，第二周的意思
	          另外：1表示星期天，2表示星期一。
	* 第7为，年份，可以留空，取值1970-2099
	*/
	/*
	 * @Scheduled(cron = "0/5 * * * * ?") public void scheduled(){
	 * logger.info("=====>>>>>使用cron  {}",System.currentTimeMillis()); }
	 */
	//这个注解在容器启动时便会生效,5秒执行一次任务。如果任务时间长，则会一直启动，不会间隔.
	/*
	 * @Scheduled(fixedRate = 5000) public void scheduled1() {
	 * logger.info("=====>>>>>使用fixedRate{}", System.currentTimeMillis()); }
	 */
    //设定上一个任务结束后多久执行下一个任务，改属性可以配合initialDelay
	/*
	 * @Scheduled(fixedDelay = 5000) public void scheduled2() {
	 * logger.info("=====>>>>>fixedDelay{}",System.currentTimeMillis()); }
	 */
	
	@Autowired
	private IDsrwszService dsrwszService;

	//基于接口创建定时器，主要是为了从数据库读取定时设置来启动任务
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		// TODO Auto-generated method stub
		/*
		 * taskRegistrar.addTriggerTask( //1.添加任务内容(Runnable) () ->
		 * System.out.println("执行动态定时任务: " + System.currentTimeMillis()),
		 * //2.设置执行周期(Trigger) triggerContext -> { //2.1 从数据库获取执行周期 String cron =
		 * cronMapper.getCron(); //2.2 合法性校验. if (StringUtil.isNoneBlank(cron)) {
		 * z
		 * //2.3 返回执行周期(Date) return new
		 * CronTrigger(cron).nextExecutionTime(triggerContext); } } );
		 */
		if(!"0".equals(remindflg)) {
			//remindflg为0不执行，其他情况如为1或者null执行
			DsrwszDto dsrwszDto = new DsrwszDto();
			List<DsrwszDto> dsrwszs = dsrwszService.getDtoList(dsrwszDto);
			if(dsrwszs!=null && !dsrwszs.isEmpty()) {
                for (DsrwszDto dsrwsz : dsrwszs) {
                    if (StringUtil.isNotBlank(remindtype) && !Arrays.asList(remindtype.split(",")).contains(dsrwsz.getRemindtype()))
                        continue;
                    taskRegistrar.addTriggerTask(doTask(dsrwsz), getTrigger(dsrwsz));
                }
			}
		}
	}


	
	/**
	  * 业务执行方法
     * @return
     */
    public Runnable doTask(DsrwszDto dsrwszDto) {
    	
        return new ScheduleRunnable(dsrwszDto);
    } 
    
    /**
	  * 业务触发器
     * @return
     */
    private Trigger getTrigger(DsrwszDto dsrwszDto) {
    	//String cron="0/10 * * * * ?";
    	return new MatridxTrigger(dsrwszService,dsrwszDto);
    }
}
