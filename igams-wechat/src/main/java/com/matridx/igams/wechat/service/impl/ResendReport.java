package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.service.svcinterface.ISjxxWsService;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResendReport {

private Logger log = LoggerFactory.getLogger(ResendReport.class);
	
	private RedisUtil redisUtil;
	private String key;
	private String rabbitType;
	private List<SjxxDto> sjxxDtos;
	private IFjcfbService fjcfbService;
	private AmqpTemplate amqpTempl;

	private String yhm;
	private List<JcsjDto> jcsj_lxs;
	
	public void init(String key,String rabbitType,List<SjxxDto> sjxxDtos,RedisUtil redisUtil,IFjcfbService fjcfbService,AmqpTemplate amqpTempl){
		this.key = key;
		this.rabbitType = rabbitType;
		this.sjxxDtos = sjxxDtos;
		this.redisUtil = redisUtil;
		this.fjcfbService = fjcfbService;
		this.amqpTempl = amqpTempl;
	}
	
	public void init2(String key, List<SjxxDto> sjxxDtos, RedisUtil redisUtil, String yhm,List<JcsjDto> jcsj_lxs){
		this.key = key;
		this.sjxxDtos = sjxxDtos;
		this.redisUtil = redisUtil;
		this.yhm = yhm;
		this.jcsj_lxs = jcsj_lxs;
	}
	
	/**
	 * 处理报告重新发送(旧)
	 */
	public void resendReportDeal() {
		// TODO Auto-generated method stub
		log.info("-------resendReportDeal-------处理报告重新发送线程开启------");
		redisUtil.hset("EXP_:_"+ key,"Fin", false,3600);
		for (int i = 0; i < sjxxDtos.size(); i++) {
			redisUtil.hset("EXP_:_"+key, "Cnt", String.valueOf(i));
			if(isCanceled(key)){//取消了则结束方法
				log.info("-------isCanceled------取消发送-------");
				return;
			}
			DBEncrypt crypt = new DBEncrypt();
			String wjljjm=crypt.dCode(sjxxDtos.get(i).getWjlj());
			boolean sendFlg=fjcfbService.sendWordFile(wjljjm);
			
			if(sendFlg) {
				Map<String,String> pdfMap= new HashMap<>();
				pdfMap.put("wordName", crypt.dCode(sjxxDtos.get(i).getFwjm()));
				pdfMap.put("fwjlj",sjxxDtos.get(i).getFwjlj());
				pdfMap.put("fjid",sjxxDtos.get(i).getFjid());
				pdfMap.put("ywlx",sjxxDtos.get(i).getCskz3()+"_"+sjxxDtos.get(i).getCskz1());
				pdfMap.put("MQDocOkType",rabbitType);
				pdfMap.put("gzlxmc", sjxxDtos.get(i).getGzlxmc());
				//删除PDF
				FjcfbDto fjcfbDto_t=new FjcfbDto();
				fjcfbDto_t.setYwlx(pdfMap.get("ywlx"));
				fjcfbDto_t.setYwid(sjxxDtos.get(i).getSjid());
				fjcfbService.deleteByYwidAndYwlx(fjcfbDto_t);
				//发送Rabbit消息转换pdf
				amqpTempl.convertAndSend("doc2pdf_exchange",MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(pdfMap));
				redisUtil.hset("EXP_:_"+key, "Cnt", String.valueOf(i+1));
			}else {
				log.error(" 复制文件到文件转换服务器时出错，未成功把文件发送到服务器那边。wjlj:" + wjljjm);
			}
			
		}
		redisUtil.hset("EXP_:_"+key, "Fin",true);
	}
	
	/**
	 * 处理报告重新发送(新) -- 重新生成word
	 */
	public void resendNewReportDeal() throws BusinessException {
		log.error("------- resendNewReportDeal -------处理报告重新发送线程开启------");
		redisUtil.hset("EXP_:_"+ key,"Fin", false);
		for (int i = 0; i < sjxxDtos.size(); i++) {
			redisUtil.hset("EXP_:_"+key, "Cnt", String.valueOf(i));
			if(isCanceled(key)){//取消了则结束方法
				log.error("-------isCanceled------取消发送-------");
				return;
			}
			log.error("------- resendNewReportDeal -------开始发送报告------");
			
			ISjxxWsService sjxxWsService = (ISjxxWsService)ServiceFactory.getService("sjxxWsServiceImpl");//获取方法所在class
			// 重新发送报告
			sjxxWsService.resendNewReport(sjxxDtos.get(i), yhm,jcsj_lxs);
			
			log.error("------- resendNewReportDeal -------重新报告发送接收------");
		}
		redisUtil.hset("EXP_:_"+key, "Fin",true);
	}
	
	/**
	 * 判断是否被取消
	 * @param key
	 * @return
	 */
	private boolean isCanceled(String key){
		boolean isCanceled = false;
		if(redisUtil.hget("EXP_:_"+key, "isCancel") != null){
			isCanceled = (Boolean) redisUtil.hget("EXP_:_" + key, "isCancel");
		}
		return isCanceled;
	}
	

}
