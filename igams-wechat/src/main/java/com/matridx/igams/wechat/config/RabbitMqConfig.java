package com.matridx.igams.wechat.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.enums.MQTypeEnum;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {
	
	@Autowired  
    private RabbitAdmin rabbitAdmin;
	
	/**
	 * 文档转换完成OK
	 */
	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private String DOC_OK = null;
	
	@Value("${matridx.rabbit.flg}")
	private String MATER_FLG = null;

	@Value("${matridx.rabbit.ssekey:}")
	private String SSE_KEY = null;

	//==============================================================================
	//创建延时队列：用于物流小程序派单未接单提醒
	@Bean
	public Queue wl_delay_queue(){
		Map<String,Object> map = new HashMap<>();
		map.put("x-dead-letter-exchange","wechat.wlwjd.send.exchange");
		map.put("x-dead-letter-routing-key","wechat.wlwjd.send.key");
		Queue wl_delay_queue = QueueBuilder.durable("wl_delay_queue").withArguments(map).build();
		rabbitAdmin.declareQueue(wl_delay_queue);
		return wl_delay_queue;
	}
	//创建延时交换机
	@Bean
	public DirectExchange wl_delay_exchange(){
		DirectExchange dExchange=new DirectExchange("wl_delay_exchange");  
        rabbitAdmin.declareExchange(dExchange);  
        return dExchange;
		//return new DirectExchange("wl_delay_exchange");
	}
	//延时队列绑定
	@Bean
	public Binding wlBindingDelayExchangeAndQueue(){
		Binding binding = BindingBuilder.bind(wl_delay_queue()).to(wl_delay_exchange()).with("wl_delay_key");
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	//创建普通队列
	@Bean
	public Queue wlwjdSend(){
		//物流未接单发送信息
		Queue queue = new Queue("wechat.wlwjd.send.queue");
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	//创建交换机
	@Bean
	public TopicExchange wlwjdSendExchange(){
		TopicExchange dExchange=new TopicExchange("wechat.wlwjd.send.exchange");  
        rabbitAdmin.declareExchange(dExchange);  
        return dExchange;
		//return new TopicExchange("wechat.wlwjd.send.exchange");
	}
	//创建交换机
	@Bean
	public TopicExchange sysExchange(){
		TopicExchange dExchange=new TopicExchange("wechat.exchange");
		rabbitAdmin.declareExchange(dExchange);
		return dExchange;
	}
	//绑定队列
	@Bean
	public Binding bindingExchangeWlwjdSend(Queue wlwjdSend, TopicExchange wlwjdSendExchange){
		Binding binding = BindingBuilder.bind(wlwjdSend).to(wlwjdSendExchange).with("wechat.wlwjd.send.key");
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	//==============================================================================
	/**
	 * 甲乙流项目文件处理
	 */
	@Bean
	public Queue fluFileDispose() {
		Queue queue = new Queue(MQTypeEnum.FLU_FILE_ANALYSIS.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}


	/**
	 * Z项目文件处理
	 * @return
	 */
	@Bean
	public Queue zFileDispose() {
		Queue queue = new Queue(MQTypeEnum.Z_FILE_DISPOSE.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 交换机与消息队列进行绑定，Z项目文件处理
	 * @param
	 * @param
	 * @return
	 */
	@Bean
	public Binding bindingExchangeFluFileDispose(Queue fluFileDispose, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(fluFileDispose).to(exchange).with(MQTypeEnum.FLU_FILE_ANALYSIS.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	/**
	 * 交换机与消息队列进行绑定，Z项目文件处理
	 * @param
	 * @param
	 * @return
	 */
	@Bean
	public Binding bindingExchangeZFileDispose(Queue zFileDispose, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(zFileDispose).to(exchange).with(MQTypeEnum.Z_FILE_DISPOSE.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	/**
	 * 文档转换完成OK
	 * @return
	 */
	@Bean
	public Queue docTranOkQueue() {
		Queue queue = new Queue(DOC_OK);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 物料修改
	 * @return
	 */
	@Bean
	public Queue materUpdateQueue() {
		Queue queue = new Queue("sys.igams.wlgl.update"+MATER_FLG);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 订阅消息
	 * @return
	 */
	@Bean
	public Queue wecharSubscibeQueue() {
		Queue queue = new Queue(MQTypeEnum.WECHAR_SUBSCIBE.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 取消订阅消息
	 * @return
	 */
	@Bean
	public Queue wecharUnsubscibeQueue() {
		Queue queue = new Queue(MQTypeEnum.WECHAR_UNSUBSCIBE.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	/**
	 * 检验申请消息
	 * @return
	 */
	@Bean
	public Queue addCheckApplyQueue() {
		Queue queue = new Queue(MQTypeEnum.ADD_CHECK_APPLY.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}

	/**
	 * 微信授权消息
	 * @return
	 */
	@Bean
	public Queue wechatAuthorizeQueue() {
		Queue queue = new Queue(MQTypeEnum.WECHAT_AUTHORIZE.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}

	/**
	 * 删除检验申请
	 * @return
	 */
	@Bean
	public Queue delCheckApplyQueue() {
		Queue queue = new Queue(MQTypeEnum.DEL_CHECK_APPLY.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	
	/**
	 * 文本消息
	 * @return
	 */
	@Bean
	public Queue wechatTextQueue() {
		Queue queue = new Queue(MQTypeEnum.WECHAT_TEXT.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	
	/**
	 * 菜单点击
	 * @return
	 */
	@Bean
	public Queue menuClickQueue() {
		Queue queue = new Queue(MQTypeEnum.MENU_CLICK.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册信息同步
	 * @return
	 */
	@Bean
	public Queue registerQueue() {
		Queue queue = new Queue(MQTypeEnum.REGISTER.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册送检新增消息
	 * @return
	 */
	@Bean
	public Queue addPartnerQueue() {
		Queue queue = new Queue(MQTypeEnum.ADD_PARTNER.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册送检修改消息
	 * @return
	 */
	@Bean
	public Queue modPartnerQueue() {
		Queue queue = new Queue(MQTypeEnum.MOD_PARTNER.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	
	/**
	 * 注册送检删除消息
	 * @return
	 */
	@Bean
	public Queue delPartnerQueue() {
		Queue queue = new Queue(MQTypeEnum.DEL_PARTNER.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	
	/**
	 * 注册送检结果消息
	 * @return
	 */
	@Bean
	public Queue resultInspectionQueue() {
		Queue queue = new Queue(MQTypeEnum.RESULT_INSPECTION.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	
	/**
	 * 注册送检结果说明消息
	 * @return
	 */
	@Bean
	public Queue commentInspectionQueue() {
		Queue queue = new Queue(MQTypeEnum.COMMENT_INSPECTION.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	
	/**
	 * 微信更新用户信息
	 * @return
	 */
	@Bean
	public Queue userModQueue() {
		Queue queue = new Queue(MQTypeEnum.WECHAR_USER_MOD.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	
	/**
	 * 上传到ftp服务器
	 * @return
	 */
	@Bean
	public Queue upLoadFtpQueue() {
		Queue queue = new Queue(MQTypeEnum.CONTRACE_BASIC_W2P.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	/**
	 * 文件转换成功消息
	 * @return
	 */
	@Bean
	public Queue changePdfQueue() {
		Queue queue = new Queue(MQTypeEnum.CONTRACE_BASIC_OK.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	/**
	 * 注册送检耐药性消息
	 * @return
	 */
	@Bean
	public Queue resistanceInspectionQueue() {
		Queue queue = new Queue(MQTypeEnum.RESISTANCE_INSPECTION.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册送检详细审核结果
	 * @return
	 */
	@Bean
	public Queue detailedInspectionQueue() {
		Queue queue = new Queue(MQTypeEnum.DETAILED_INSPECTION.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	
	/**
	 * 注册送检自免结果修改
	 * @return
	 */
	@Bean
	public Queue selfresultInspectionQueue() {
		Queue queue = new Queue(MQTypeEnum.MOD_INSPECTION_SELFRESULT.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册医院信息新增或修改
	 * @return
	 */
	@Bean
	public Queue modHospitalQueue() {
		Queue queue = new Queue(MQTypeEnum_pub.MOD_HOSPITAL.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	
	/**
	 * 注册医院信息删除
	 * @return
	 */
	@Bean
	public Queue delHospitalQueue() {
		Queue queue = new Queue(MQTypeEnum_pub.DEL_HOSPITAL.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 注册igams端的生信审核结果的rabbit
	 * @return
	 */
	@Bean
	public Queue detailedIgamsInspectionQueue() {
		Queue queue = new Queue(MQTypeEnum.DETAILED_IGAMS_INSPECTION.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	
	/**
	 * 注册送检验证修改
	 * @return
	 */
	@Bean
	public Queue modInspectionVerificationQueue() {
		Queue queue = new Queue(MQTypeEnum.MOD_INSPECTION_VERIFICATION.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	
	/**
	 * 注册送检验证新增
	 * @return
	 */
	@Bean
	public Queue addInspectionVerificationQueue() {
		Queue queue = new Queue(MQTypeEnum.ADD_INSPECTION_VERIFICATION.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	
	/**
	 * 注册送检验证新增
	 * @return
	 */
	@Bean
	public Queue delInspectionVerificationQueue() {
		Queue queue = new Queue(MQTypeEnum.DEL_INSPECTION_VERIFICATION.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	
	/**
	 * 注册送检临床指南新增消息(暂时不同步，后期可能需要)
	 * @return
	 */
	/*@Bean
	public Queue modInspectionGuideQueue() {
		return new Queue(MQTypeEnum.ADD_INSPECTION_GUIDE.getCode());
	}*/
	
	/**
	 * 调用接口信息
	 * @return
	 */
	@Bean
	public Queue addTransferQueue() {
		Queue queue = new Queue(MQTypeEnum.ADD_TRANSFER.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	
	/**
	 * 送检信息操作同步
	 * @return
	 */
	@Bean
	public Queue inspectionOperateQueue() {
		Queue queue = new Queue(MQTypeEnum.OPERATE_INSPECTION.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	
	/**
	 * 送检信息操作同步
	 * @return
	 */
	@Bean
	public Queue inspectOperateQueue() {
		Queue queue = new Queue(MQTypeEnum.OPERATE_INSPECT.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}

	/**
	 * 对接接口发送报告
	 * @return
	 */
	@Bean
	public Queue matchingReportSendQueue() {
		Queue queue = new Queue(MQTypeEnum.MATCHING_SEND_REPORT.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}
	/**
	 * sse发送消
	 * @return
	 */
	@Bean
	public Queue sseSendQueue() {
		Queue queue = new Queue(MQTypeEnum.SSE_SENDMSG.getCode()+SSE_KEY);
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * sse异常发送消
	 * @return
	 */
	@Bean
	public Queue sseExceptionSendQueue() {
		Queue queue = new Queue(MQTypeEnum.SSE_SENDMSG_EXCEPRION.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 交换机
	 * @return
	 */
	@Bean
	public TopicExchange exchange() {
		TopicExchange dExchange=new TopicExchange("wechat.exchange");  
        rabbitAdmin.declareExchange(dExchange);  
        return dExchange;
		//return new TopicExchange("wechat.exchange");
	}

	/**
	 * 交换机与消息队列进行绑定，对对接接口发送报告完成进行绑定
	 * @param matchingReportSendQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeMatchingReportSendQueue(Queue matchingReportSendQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(matchingReportSendQueue).to(exchange).with(MQTypeEnum.MATCHING_SEND_REPORT.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	@Bean
	public TopicExchange doc2pdf_exchange() {
		TopicExchange dExchange=new TopicExchange("doc2pdf_exchange");  
        rabbitAdmin.declareExchange(dExchange);  
        return dExchange;
		//return new TopicExchange("doc2pdf_exchange");
	}

	/**
	 * 交换机与消息队列进行绑定，对文件转换完成进行绑定
	 * @param docTranOkQueue
	 * @param doc2pdf_exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDocTranOkQueue(Queue docTranOkQueue, TopicExchange doc2pdf_exchange) {
		Binding binding = BindingBuilder.bind(docTranOkQueue).to(doc2pdf_exchange).with(DOC_OK);
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对合作伙伴新增消息进行绑定
	 * @param addPartnerQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeAddPartner(Queue addPartnerQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(addPartnerQueue).to(exchange).with(MQTypeEnum.ADD_PARTNER.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对合作伙伴修改消息进行绑定
	 * @param modPartnerQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeModPartner(Queue modPartnerQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(modPartnerQueue).to(exchange).with(MQTypeEnum.MOD_PARTNER.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对合作伙伴删除消息进行绑定
	 * @param delPartnerQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelPartner(Queue delPartnerQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(delPartnerQueue).to(exchange).with(MQTypeEnum.DEL_PARTNER.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对送检结果消息进行绑定
	 * @param resultInspectionQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeResultInspection(Queue resultInspectionQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(resultInspectionQueue).to(exchange).with(MQTypeEnum.RESULT_INSPECTION.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对送检结果说明消息进行绑定
	 * @param commentInspectionQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeCommentInspection(Queue commentInspectionQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(commentInspectionQueue).to(exchange).with(MQTypeEnum.COMMENT_INSPECTION.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对微信用户信息更改消息进行绑定
	 * @param userModQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeUserMod(Queue userModQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(userModQueue).to(exchange).with(MQTypeEnum.WECHAR_USER_MOD.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	
	/**
	 * 	交换机与消息队列进行绑定，对上传文件到服务器结果消息进行绑定
	 * @param changePdfQueue
	 * @param doc2pdf_exchange
	 * @return
	 */
	@Bean
    public Binding bindingExchangeUpLoadFtp(Queue changePdfQueue, TopicExchange doc2pdf_exchange) {
		Binding binding = BindingBuilder.bind(changePdfQueue).to(doc2pdf_exchange).with(MQTypeEnum.CONTRACE_BASIC_OK.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
    }
	
	/**
	 * 交换机与消息队列进行绑定，对送检耐药性消息进行绑定
	 * @param resistanceInspectionQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeResistanceInspection(Queue resistanceInspectionQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(resistanceInspectionQueue).to(exchange).with(MQTypeEnum.RESISTANCE_INSPECTION.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对送检详细审核结果进行绑定
	 * @param detailedInspectionQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDetailedInspection(Queue detailedInspectionQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(detailedInspectionQueue).to(exchange).with(MQTypeEnum.DETAILED_INSPECTION.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	
	/**
	 * 交换机与消息队列进行绑定，对送检自免结果进行绑定
	 * @param selfresultInspectionQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeSelfresultInspection(Queue selfresultInspectionQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(selfresultInspectionQueue).to(exchange).with(MQTypeEnum.MOD_INSPECTION_SELFRESULT.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对医院信息进行绑定
	 * @param modHospitalQueue
	 * @param exchange
	 * @return 
	 */
	@Bean
	public Binding bindingExchangeModHospital(Queue modHospitalQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(modHospitalQueue).to(exchange).with(MQTypeEnum_pub.MOD_HOSPITAL.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对医院信息进行绑定
	 * @param delHospitalQueue
	 * @param exchange
	 * @return 
	 */
	@Bean
	public Binding bindingExchangeDelHospital(Queue delHospitalQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(delHospitalQueue).to(exchange).with(MQTypeEnum_pub.DEL_HOSPITAL.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	/**
	 * 交换机与消息队列进行绑定，对送检验证修改进行绑定
	 * @param detailedIgamsInspectionQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDetailedIgamsInspection(Queue detailedIgamsInspectionQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(detailedIgamsInspectionQueue).to(exchange).with(MQTypeEnum.DETAILED_IGAMS_INSPECTION.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对送检验证修改进行绑定
	 * @param modInspectionVerificationQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeModInspectionVerification(Queue modInspectionVerificationQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(modInspectionVerificationQueue).to(exchange).with(MQTypeEnum.MOD_INSPECTION_VERIFICATION.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对送检验证修改进行绑定
	 * @param addInspectionVerificationQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeAddInspectionVerification(Queue addInspectionVerificationQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(addInspectionVerificationQueue).to(exchange).with(MQTypeEnum.ADD_INSPECTION_VERIFICATION.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对送检验证修改进行绑定
	 * @param delInspectionVerificationQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeDelInspectionVerification(Queue delInspectionVerificationQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(delInspectionVerificationQueue).to(exchange).with(MQTypeEnum.DEL_INSPECTION_VERIFICATION.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对送检临床指南新增消息进行绑定(暂时不同步，后期可能需要)
	 * @param queueMessage
	 * @param exchange
	 * @return
	 */
	/*@Bean
	public Binding bindingExchangeAddInspectionGuide(Queue modInspectionGuideQueue, TopicExchange exchange) {
		return BindingBuilder.bind(modInspectionGuideQueue).to(exchange).with(MQTypeEnum.ADD_INSPECTION_GUIDE.getCode());
	}*/
	
	/**
	 * 交换机与消息队列进行绑定，对调用接口信息进行绑定
	 * @param addTransferQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeAddTransferMessage(Queue addTransferQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(addTransferQueue).to(exchange).with(MQTypeEnum.ADD_TRANSFER.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对送检信息操作进行绑定
	 * @param inspectionOperateQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeInspectionOperate(Queue inspectionOperateQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(inspectionOperateQueue).to(exchange).with(MQTypeEnum.OPERATE_INSPECTION.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对送检信息操作进行绑定
	 * @param inspectOperateQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeInspectOperate(Queue inspectOperateQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(inspectOperateQueue).to(exchange).with(MQTypeEnum.OPERATE_INSPECT.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	/**
	 * 交换机与消息队列进行绑定，对微信授权消息操作进行绑定
	 * @param wechatAuthorizeQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeWechatAuthorize(Queue wechatAuthorizeQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(wechatAuthorizeQueue).to(exchange).with(MQTypeEnum.WECHAT_AUTHORIZE.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	/**
	 * 交换机与消息队列进行绑定，对微信授权消息操作进行绑定
	 * @param sseSendQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingSseSend(Queue sseSendQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(sseSendQueue).to(exchange).with(MQTypeEnum.SSE_SENDMSG.getCode()+SSE_KEY);
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	/**
	 * 交换机与消息队列进行绑定，对微信授权消息操作进行绑定
	 * @param sseExceptionSendQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingSseSendException(Queue sseExceptionSendQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(sseExceptionSendQueue).to(exchange).with(MQTypeEnum.SSE_SENDMSG_EXCEPRION.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	@Bean
	public Queue receiveInspectQueue() {
		Queue queue = new Queue("matridx.inspect.report.notify");
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	//创建交互机
	@Bean
	public TopicExchange receiveInspectExchange(){
		TopicExchange receiveInspectExchange=new TopicExchange("report.exchange");
		rabbitAdmin.declareExchange(receiveInspectExchange);
		return receiveInspectExchange;
	}
	@Bean
	public Binding bindingExchangeReceiveInspectQueue(Queue receiveInspectQueue, TopicExchange receiveInspectExchange) {
		Binding binding = BindingBuilder.bind(receiveInspectQueue).to(receiveInspectExchange).with("matridx.inspect.report.notify");
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
}
