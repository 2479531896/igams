package com.matridx.igams.common.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpIOException;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.DdxxglModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.MQQueueModel;
import com.matridx.igams.common.dao.post.IDdxxglDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.util.MQUtils;
import com.matridx.igams.common.util.DingTalkUtil;

@Service
public class DdxxglServiceImpl extends BaseBasicServiceImpl<DdxxglDto, DdxxglModel, IDdxxglDao> implements IDdxxglService{

	private final Logger log = LoggerFactory.getLogger(XxglServiceImpl.class);
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private DingTalkUtil talkUtil;
	@Autowired
	private MQUtils mqUtil;
	@Autowired
	private IJcsjService jcsjService;
	/**
	 * 根据钉钉消息类型查询用户信息
	 */
	@Override
	public List<DdxxglDto> selectByDdxxlx(String ddxxlx) {
		// TODO Auto-generated method stub
		return dao.selectByDdxxlx(ddxxlx);
	}
	
	/**
	 * 查询系统用户
	 */
	public List<DdxxglDto> getXtyh(){
		return dao.getXtyh();
	}

	/**
	 * 根据部门ID和审批类型查询审批流程
	 */
	@Override
	public List<DdxxglDto> getProcessByJgid(String jgid,String splx) {
		// TODO Auto-generated method stub
		return dao.getProcessByJgid(jgid,splx);
	}


	/**
	 * @author lifan 
	 * 发送MQ消息个数提醒(定时任务) 发送钉钉和微信提醒
	 */
	public void sendMQMessageCount(List<MQQueueModel> queueList) {
		log.info("发送Mq消息个数任务开始执行");
		ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
		List<DdxxglDto> ddxxglDtolist;
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb("DINGMESSAGETYPE");
		jcsjDto.setCsdm("MQ_MESSAGECOUNT_NOTICE");
		ddxxglDtolist = selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
		Iterator<DdxxglDto> ddxxIterator = ddxxglDtolist.iterator();
		// 发送钉钉与微信消息
//		String token = talkUtil.getToken();
		List<Map<String, String>> receivers = new ArrayList<>();
		while (ddxxIterator.hasNext()) {
			Map<String, String> userMap = new HashMap<>();
			DdxxglDto ddxx = ddxxIterator.next();
			if (StringUtils.isNotBlank(ddxx.getDdid())) {
				userMap.put("ddid", ddxx.getDdid());
			}
			if (StringUtils.isNotBlank(ddxx.getWechatid())) {
				userMap.put("wxid", ddxx.getWechatid());
			}
			receivers.add(userMap);
		}
		try {
			StringBuilder message = new StringBuilder();
			String title = "MQ消息数量过多";
			boolean sendFlag = false;
			for (MQQueueModel queue : queueList) {
				if (queue.getQueueName() == null) {
					continue;
				}
				Map<String, Integer> mqCount = mqUtil.getMQCountMap(queue.getQueueName());
				int messagesCount = mqCount.get("messages");// 消息总数
				int readyCount = mqCount.get("messages_ready");
				int unackCount = mqCount.get("messages_unacknowledged");
				log.info(queue.getQueueName() + "消息总数量" + messagesCount + "个，未消费数量" + readyCount + "个，未确认数量"
						+ unackCount);
				// 获取队列中的消息个数
				if (messagesCount >= 3) {
					if (!sendFlag) {
						sendFlag = true;
					}
					message.append(queue.getQueueName()).append("消息数量有").append(messagesCount).append("个，");
				}
			}
			if (!sendFlag) {
				return;
			}
			message.append("请注意查看。");
			talkUtil.sendOutMessage(receivers, message.toString(), title, null);
		} catch (AmqpIOException | IOException e) {
			e.printStackTrace();
			talkUtil.sendOutMessage(receivers, "RabbitMQ服务器存在异常，请前往" + connectionFactory.getHost() + "服务器查看",
					"RabbitMQ服务器异常", null);
			log.error("RabbitMQ异常");
		} finally {
			log.info("发送Mq消息个数任务执行结束");
		}
	}
	
	/**
	 * 根据审批类型查询钉钉审批机构
	 */
	public List<DdxxglDto> getDingtalkAuditDep(String splx){
		return dao.getDingtalkAuditDep(splx);
	}
	
	/**
	 * 根据审批ID获取钉钉审批流程信息
	 */
	public DdxxglDto getProcessBySpid(String spid) {
		return dao.getProcessBySpid(spid);
	}
}
