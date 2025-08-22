package com.matridx.server.wechat.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.server.wechat.enums.MQWechatTypeEnum;

@Configuration
public class RabbitMqConfig {
	@Autowired
	private RabbitAdmin rabbitAdmin;
	/**
	 * 注册订阅消息
	 * @return
	 */
	@Bean
	public Queue subscibeQueue() {
		return new Queue(MQWechatTypeEnum.WECHAR_SUBSCIBE.getCode());
	}

	/**
	 * 注册订阅取消消息
	 * @return
	 */
	@Bean
	public Queue unSubscibeQueue() {
		return new Queue(MQWechatTypeEnum.WECHAR_UNSUBSCIBE.getCode());
	}
	
	/**
	 * 注册文本消息
	 * @return
	 */
	@Bean
	public Queue wechatTextQueue() {
		return new Queue(MQWechatTypeEnum.WECHAT_TEXT.getCode());
	}

	/**
	 * 微信授权消息
	 * @return
	 */
	@Bean
	public Queue wechatAuthorizeQueue() {
		return new Queue(MQWechatTypeEnum.WECHAT_AUTHORIZE.getCode());
	}
	/**
	 * 注册菜单点击消息
	 * @return
	 */
	@Bean
	public Queue menuClickQueue() {
		return new Queue(MQWechatTypeEnum.MENU_CLICK.getCode());
	}
	
	@Bean
	public Queue upLoadFtpQueue() {
		return new Queue(MQWechatTypeEnum.CONTRACE_BASIC_W2P.getCode());
	}
	
	@Bean
	public Queue docpdfQueue() {
		return new Queue(MQWechatTypeEnum.CONTRACT_TOPIC.getCode());
	} 
	
	/**
	 * 注册结果
	 * @return
	 */
	@Bean
	public Queue registerQueue() {
		return new Queue(MQWechatTypeEnum.REGISTER_SEND.getCode());
	}

	@Bean
	public Queue ssesendQueue() {
		Queue queue = new Queue(MQWechatTypeEnum.SSE_SENDMSG_EXCEPRION.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;
	}

	/**
	 * 启用伙伴信息
	 * @return
	 */
	@Bean
	public Queue enableOrDisablePartnerQueue() {
		Queue queue = new Queue(MQTypeEnum.ENABLE_OR_DISABLE_PARTNER.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}

	/**
	 * 同步分析完成时间
	 * @return
	 */
	@Bean
	public Queue addFxwcsj() {
		Queue queue = new Queue(MQTypeEnum.ADD_FXWCSJ.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}

	/**
	 * 伙伴收费修改
	 * @return
	 */
	@Bean
	public Queue modPartnerTollQueue() {
		Queue queue = new Queue(MQWechatTypeEnum.MOD_PARTNER_TOLL.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	/**
	 * 伙伴收费修改
	 * @return
	 */
	@Bean
	public Queue batchmodSfbzQueue() {
		Queue queue = new Queue(MQTypeEnum.BatchMod_SFBZ.getCode());
		rabbitAdmin.declareQueue(queue);
		return queue;

	}
	/**
	 * 注册微信用户更新
	 * @return
	 */
	@Bean
	public Queue wecharUserModQueue() {
		return new Queue(MQWechatTypeEnum.WECHAR_USER_MOD.getCode());
	}
	
	/**
	 * 注册查询支付结果
	 * @return
	 */
	@Bean
	public Queue selectPayResultQueue() {
		return new Queue(MQWechatTypeEnum.SELECT_PAY_RESULT.getCode());
	}
	
	/**
	 * 注册调用接口信息新增
	 * @return
	 */
	@Bean
	public Queue addJkdymxQueue() {
		return new Queue(MQWechatTypeEnum.ADD_TRANSFER.getCode());
	}

	
	/**
	 * 送检信息操作同步
	 * @return
	 */
	@Bean
	public Queue inspectionOperateQueue() {
		return new Queue(MQWechatTypeEnum.OPERATE_INSPECTION.getCode());
	}
	
	/**
	 * 送检信息操作同步
	 * @return
	 */
	@Bean
	public Queue inspectOperateQueue() {
		return new Queue(MQWechatTypeEnum.OPERATE_INSPECT.getCode());
	}
	
	/**
	 * 交换机
	 * @return
	 */
	@Bean
    public TopicExchange exchange() {
        return new TopicExchange("wechat.exchange");
    }
	
	/**
	 * 交换机与消息队列进行绑定，对订阅消息进行绑定
	 * @param subscibeQueue
	 * @param exchange
	 * @return
	 */
	@Bean
    public Binding bindingExchangeSubscibe(Queue subscibeQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(subscibeQueue).to(exchange).with(MQWechatTypeEnum.WECHAR_SUBSCIBE.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
    }

	/**
	 * 交换机与消息队列进行绑定，对订阅取消消息进行绑定
	 * @param unSubscibeQueue
	 * @param exchange
	 * @return
	 */
	@Bean
    public Binding bindingExchangeUnSubscibe(Queue unSubscibeQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(unSubscibeQueue).to(exchange).with(MQWechatTypeEnum.WECHAR_UNSUBSCIBE.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	/**
	 * 交换机与消息队列进行绑定，对文本消息进行绑定
	 * @param wechatTextQueue
	 * @param exchange
	 * @return
	 */
	@Bean
    public Binding bindingExchangeWechatText(Queue wechatTextQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(wechatTextQueue).to(exchange).with(MQWechatTypeEnum.WECHAT_TEXT.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
    }
	
	/**
	 * 交换机与消息队列进行绑定，对菜单点击消息进行绑定
	 * @param menuClickQueue
	 * @param exchange
	 * @return
	 */
	@Bean
    public Binding bindingExchangeMenuClick(Queue menuClickQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(menuClickQueue).to(exchange).with(MQWechatTypeEnum.MENU_CLICK.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
    }



	/**
	 * 交换机与消息队列进行绑定，伙伴收费修改
	 * @return
	 */
	@Bean
	public Binding bindingExchangeModPartnerToll(Queue modPartnerTollQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(modPartnerTollQueue).to(exchange).with(MQWechatTypeEnum.MOD_PARTNER_TOLL.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	/**
	 * 交换机与消息队列进行绑定，pilia	伙伴收费修改
	 * @return
	 */
	@Bean
	public Binding bindingExchangeBatchmodSfbz(Queue batchmodSfbzQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(batchmodSfbzQueue).to(exchange).with(MQTypeEnum.BatchMod_SFBZ.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	/**
	 * 	交换机与消息队列进行绑定，对上传文件到服务器结果消息进行绑定
	 * @param upLoadFtpQueue
	 * @param exchange
	 * @return
	 */
	@Bean
    public Binding bindingExchangeUpLoadFtp(Queue upLoadFtpQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(upLoadFtpQueue).to(exchange).with(MQWechatTypeEnum.CONTRACE_BASIC_W2P.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
    }
	
	/**
	 * 	交换机与消息队列进行绑定，对上传文件到服务器结果消息进行绑定
	 * @param docpdfQueue
	 * @param exchange
	 * @return
	 */
	@Bean
    public Binding bindingExchangeDocpdf(Queue docpdfQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(docpdfQueue).to(exchange).with(MQWechatTypeEnum.CONTRACT_TOPIC.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
    }
	
	/**
	 * 交换机与消息队列进行绑定，对注册信息进行绑定
	 * @param registerQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeRegister(Queue registerQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(registerQueue).to(exchange).with(MQWechatTypeEnum.REGISTER_SEND.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对微信用户信息进行绑定
	 * @param wecharUserModQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeWecharUserMod(Queue wecharUserModQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(wecharUserModQueue).to(exchange).with(MQWechatTypeEnum.WECHAR_USER_MOD.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对查询支付结果进行绑定
	 * @param selectPayResultQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeSelectPayResult(Queue selectPayResultQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(selectPayResultQueue).to(exchange).with(MQWechatTypeEnum.SELECT_PAY_RESULT.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
	
	/**
	 * 交换机与消息队列进行绑定，对调用接口信息新增进行绑定
	 * @param addJkdymxQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeAddJkdymx(Queue addJkdymxQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(addJkdymxQueue).to(exchange).with(MQWechatTypeEnum.ADD_TRANSFER.getCode());
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
		Binding binding = BindingBuilder.bind(inspectionOperateQueue).to(exchange).with(MQWechatTypeEnum.OPERATE_INSPECTION.getCode());
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
		Binding binding = BindingBuilder.bind(inspectOperateQueue).to(exchange).with(MQWechatTypeEnum.OPERATE_INSPECT.getCode());
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
		Binding binding = BindingBuilder.bind(wechatAuthorizeQueue).to(exchange).with(MQWechatTypeEnum.WECHAT_AUTHORIZE.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	/**
	 * 交换机与消息队列进行绑定，对微信授权消息操作进行绑定
	 * @return
	 */
	@Bean
	public Queue receiveOperateBasicDataQueue() {
		return new Queue(MQWechatTypeEnum.OPERATE_BASICDATA.getCode());
	}

	/**
	 * 交换机与消息队列进行绑定，对基础数据消息进行绑定
	 * @param receiveOperateBasicDataQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingExchangeReceiveOperateBasicDataQueue(Queue receiveOperateBasicDataQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(receiveOperateBasicDataQueue).to(exchange).with(MQWechatTypeEnum.OPERATE_BASICDATA.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	//----------信息对应同步-------------

	@Bean
	public Queue xxdyOperateQueue() {
		return new Queue(MQWechatTypeEnum.OPERATE_XXDY.getCode());
	}

	@Bean
	public Binding bindingExchangeXxdy(Queue xxdyOperateQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(xxdyOperateQueue).to(exchange).with(MQWechatTypeEnum.OPERATE_XXDY.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	/**
	 * 交换机与消息队列进行绑定，对微信授权消息操作进行绑定
	 * @param ssesendQueue
	 * @param exchange
	 * @return
	 */
	@Bean
	public Binding bindingSseSendException(Queue ssesendQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(ssesendQueue).to(exchange).with(MQWechatTypeEnum.SSE_SENDMSG_EXCEPRION.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;

	}

	/**
	 * 交换机与消息队列进行绑定，伙伴启用
	 * @return
	 */
	@Bean
	public Binding bindingExchangeEnableOrDisablePartner(Queue enableOrDisablePartnerQueue, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(enableOrDisablePartnerQueue).to(exchange).with(MQWechatTypeEnum.ENABLE_OR_DISABLE_PARTNER.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}

	/**
	 * 交换机与消息队列进行绑定，更新85端分析完成时间
	 * @return
	 */
	@Bean
	public Binding bindingExchangeAddFxwcsj(Queue addFxwcsj, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(addFxwcsj).to(exchange).with(MQWechatTypeEnum.ADD_FXWCSJ.getCode());
		rabbitAdmin.declareBinding(binding);
		return binding;
	}
}
