package com.matridx.igams.common.scheduled;

import com.matridx.igams.common.dao.entities.DsrwszDto;
import com.matridx.igams.common.service.svcinterface.IDsrwszService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CronTaskRegistrar implements DisposableBean {

    private final Map<String, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>(16);

    @Autowired
    private TaskScheduler taskScheduler;
    @Value("${matridx.systemflg.remindflg:}")
    private String remindflg;
    @Value("${matridx.systemflg.remindtype:}")
    private String remindtype;
    @Autowired
    private IDsrwszService dsrwszService;


    final static Logger log = LoggerFactory.getLogger(ScheduledService.class);

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
    @PostConstruct
    public void BeginCronTaskRegistrar(){
        log.error("ScheduleRunnable BeginCronTaskRegistrar remindflg:"+remindflg);
    	if("1".equals(remindflg)) {
	        //使同一个线程中串行执行,当定时任务增多，如果一个任务卡死，会导致其他任务也无法执行。
	        DsrwszDto dsrwszDto = new DsrwszDto();
	        //根据 remindtype 查询相应定时任务 为空则查询全部 假设三个系统 remindtype配置 包含 数据库remindtype的值即可
	        dsrwszDto.setRemindtype(remindtype);
	        List<DsrwszDto> dsrwszs = dsrwszService.getDtoList(dsrwszDto);
	        if(!CollectionUtils.isEmpty(dsrwszs)) {
                for (DsrwszDto dsrwsz : dsrwszs) {
                	try {
	                    ScheduledTask customScheduledTask1 = scheduledTasks.get(dsrwsz.getRwid());
	                    if (customScheduledTask1 != null) {
	                        log.error("{}---对应的任务已存在，请勿重复创建，如需重复创建，请先执行删除后在尝试新建任务", dsrwsz.getRwid());
	                        break;
	                    }
	                    // addTriggerTask(new TriggerTask(task, trigger));
	                    ScheduledTask customScheduledTask = new ScheduledTask();
	                    customScheduledTask.future = this.taskScheduler.schedule(this.doTask(dsrwsz), this.getTrigger(dsrwsz));
	                    scheduledTasks.put(dsrwsz.getRwid(), customScheduledTask);
                	}catch(Exception e) {
                		log.error("定时任务初始化出错  ScheduleRunnable BeginCronTaskRegistrar。" + e.getMessage());
                	}
                }
	        }
    	}
   }

    public  void addTriggerTask(DsrwszDto dsrwszDto){
        log.error("ScheduleRunnable addTriggerTask---");
        ScheduledTask customScheduledTask = new ScheduledTask();
        customScheduledTask.future = this.taskScheduler.schedule(this.doTask(dsrwszDto), this.getTrigger(dsrwszDto));
        scheduledTasks.put(dsrwszDto.getRwid(),customScheduledTask);
    }


    public void removeTriggerTask(String key) {
        if (StringUtil.isBlank(key)){
            log.error("key不能为空");
            return;
        }
        ScheduledTask scheduledTask = scheduledTasks.get(key);
        if (scheduledTask == null){
            log.error("{}对应的任务不存在，请勿重复删除",key);
        }else {
            scheduledTask.cancel();
            scheduledTasks.remove(key);
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

    @Override
    public void destroy() {

    }
}