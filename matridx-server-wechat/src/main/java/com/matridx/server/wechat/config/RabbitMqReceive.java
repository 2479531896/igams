package com.matridx.server.wechat.config;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONArray;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.server.wechat.dao.entities.*;
import com.matridx.server.wechat.service.svcinterface.*;
import com.matridx.server.wechat.util.ExceptionWechartUtil;
import com.matridx.server.wechat.util.WeChatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.server.wechat.dao.entities.HbdwqxDto;
import com.matridx.server.wechat.dao.entities.SjbgsmDto;
import com.matridx.server.wechat.dao.entities.SjhbxxDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.SjxxjgDto;
import com.matridx.server.wechat.dao.entities.SjyzDto;
import com.matridx.server.wechat.dao.entities.SjzmjgDto;
import com.matridx.server.wechat.service.svcinterface.IBankService;
import com.matridx.server.wechat.service.svcinterface.IHbdwqxService;
import com.matridx.server.wechat.service.svcinterface.IPayinfoService;
import com.matridx.server.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.server.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.server.wechat.service.svcinterface.ISjlcznService;
import com.matridx.server.wechat.service.svcinterface.ISjnyxService;
import com.matridx.server.wechat.service.svcinterface.ISjwzxxService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.server.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.server.wechat.service.svcinterface.ISjyzService;
import com.matridx.server.wechat.service.svcinterface.ISjzmjgService;
import com.matridx.server.wechat.util.MqWechatType;
import com.matridx.server.wechat.util.SelectPayResultThread;
import com.matridx.springboot.util.base.StringUtil;

@Component
public class RabbitMqReceive {
	
	@Autowired
	ISjxxService sjxxService;
	@Autowired
	ISjsyglService sjsyglService;
	@Autowired
	IXmsyglService xmsyglService;
	@Autowired
	IHbsfbzService hbsfbzservice;
	@Autowired
	ISjwzxxService sjwzxxService;
	@Autowired
	ISjnyxService sjnyxService;
	@Autowired
	IXxdlcwglService xxdlcwglService;
	@Autowired
	ISjbgsmService sjbgsmService;
	@Autowired
	ISjxxjgService sjxxjgService;
	@Autowired
	ISjhbxxService sjhbxxService;
	@Autowired
	ISjlcznService sjlcznService;
	@Autowired
	ISjzmjgService sjzmjgService;
	@Autowired
	IYyxxService yyxxService;
	@Autowired
	ISjyzService sjyzService;
	@Autowired
	IPayinfoService payinfoService;
	@Autowired
	IBankService bankService;
	@Autowired
	IXtszService xtszService;
	@Autowired
	RestTemplate restTemplate;
	@Autowired	
	IHbdwqxService hbdwqxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IXxdyService xxdyService;
	@Autowired
	ISjjcxmService sjjcxmService;
	@Autowired
	ExceptionWechartUtil exceptionWechartUtil;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IWxyhService wxyhService;
	private Logger log = LoggerFactory.getLogger(RabbitMqReceive.class);
	@Autowired
	ISjycfkWechatService sjycfkService;
	@Autowired
	ISjycWechatService sjycService;
	@Autowired
	IHbxxzService hbxxzService;
	@Autowired
	ISjyctzWechatService sjyctzService;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;

	@Autowired
	ISjycStatisticsWechatService sjycStatisticsWechatService;

	@RabbitListener(queues = MqWechatType.RESULT_INSPECTION, containerFactory="defaultFactory")
	public void receiveResultInspection(String str) {
		log.error("Receive Result Inspection:"+str);
        try {
			//把数据录入到 igams_sjwzxx 送检信息里
            @SuppressWarnings("unchecked")
			Map<String,Object> mapType = JSON.parseObject(str,Map.class);
			sjwzxxService.receiveResultInspection(mapType);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.RESULT_INSPECTION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.RESULT_INSPECTION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }


	@RabbitListener(queues = MqWechatType.OPERATE_BASICDATA)
	public void receiveOperateBasicData(String str){
		log.error("Receive Operate Basicdata:"+str);
		try {
			@SuppressWarnings("unchecked")
			Map<String,Object> jsonMap = JSONObject.parseObject(str,Map.class);
			List<JcsjDto> jcsjDtos = JSONArray.parseArray(jsonMap.get("jcsjDtos").toString(),JcsjDto.class);
			String key = (String) jsonMap.get("key");
			if ("all".equals(key)){
				JcsjDto jcsjDto = jcsjDtos.get(0);
				jcsjService.deleteByJclb(jcsjDto);
				jcsjService.batchInsertJcsjDtos(jcsjDtos);
				//更新该基础类别的基础数据（list）
			}else if ("part".equals(key)){
				for (JcsjDto jcsjDto : jcsjDtos) {
					JcsjDto jcsjByCsid = jcsjService.getJcsjByCsid(jcsjDto);
					if (jcsjByCsid!=null){
						//新增
						jcsjService.updateJcsj(jcsjDto);
					}else {
						jcsjService.insertJcsj(jcsjDto);
					}
				}
			}
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.OPERATE_BASICDATA);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.OPERATE_BASICDATA);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}

	@RabbitListener(queues = MqWechatType.COMMENT_INSPECTION, containerFactory="defaultFactory")
	public void receiveCommentInspection(String str) {
        log.error("Receive Inspection Comment:"+str);
        try {
			//把数据录入到 igams_sjbgsm 信息里
			SjbgsmDto sjbgsmDto = JSONObject.parseObject(str, SjbgsmDto.class);
			sjbgsmService.receiveCommentInspection(sjbgsmDto);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.COMMENT_INSPECTION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.COMMENT_INSPECTION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	@RabbitListener(queues = MqWechatType.RESISTANCE_INSPECTION, containerFactory="defaultFactory")
	public void receiveResistanceInspection(String str) {
		log.error("Receive Resistance Inspection:"+str);
        try {
			//把数据录入到 igams_sjnyx 送检耐药性里
        	@SuppressWarnings("unchecked")
			Map<String,Object> mapType = JSON.parseObject(str,Map.class);
			sjnyxService.receiveResistanceInspection(mapType);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.RESISTANCE_INSPECTION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.RESISTANCE_INSPECTION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	@RabbitListener(queues = MqWechatType.DETAILED_INSPECTION, containerFactory="defaultFactory")
	public void receiveDetailedInspection(String str) {
		log.error("Receive Detailed Inspection:"+str);
        try {
			//把数据录入到 igams_sjxxjg 送检详细结果
			List<SjxxjgDto> sjxxjgDtos = JSONObject.parseArray(str, SjxxjgDto.class);
			sjxxjgService.receiveDetailedInspection(sjxxjgDtos);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.DETAILED_INSPECTION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.DETAILED_INSPECTION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	@RabbitListener(queues = MqWechatType.MOD_INSPECTION_SELFRESULT, containerFactory="defaultFactory")
	public void receiveModSelfresult(String str) {
		log.error("Receive ModSelfresult:"+str);
        try {
        	//把数据录入到 igams_sjzmjg 送检自免结果里
        	List<SjzmjgDto> sjzmjgDtos = JSONObject.parseArray(str, SjzmjgDto.class);
        	sjzmjgService.receiveModSelfresult(sjzmjgDtos);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.MOD_INSPECTION_SELFRESULT);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.MOD_INSPECTION_SELFRESULT);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	@RabbitListener(queues = MqWechatType.ADD_INSPECTION_VERIFICATION, containerFactory="defaultFactory")
	public void receiveAddVerification(String str) {
		log.error("Receive AddVerification:"+str);
        try {
			//把数据录入到 igams_sjyz 送检信息里
			SjyzDto sjyzDto = JSONObject.parseObject(str, SjyzDto.class);
			sjyzService.insert(sjyzDto);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.ADD_INSPECTION_VERIFICATION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.ADD_INSPECTION_VERIFICATION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	@RabbitListener(queues = MqWechatType.MOD_INSPECTION_VERIFICATION, containerFactory="defaultFactory")
	public void receiveModVerification(String str) {
		log.error("Receive ModVerification:"+str);
        try {
			//把数据录入到 igams_sjyz 送检信息里
			SjyzDto sjyzDto = JSONObject.parseObject(str, SjyzDto.class);
			sjyzService.update(sjyzDto);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.MOD_INSPECTION_VERIFICATION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.MOD_INSPECTION_VERIFICATION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	@RabbitListener(queues = MqWechatType.DEL_INSPECTION_VERIFICATION, containerFactory="defaultFactory")
	public void receiveDelVerification(String str) {
		log.error("Receive DelVerification:"+str);
        try {
			//把数据录入到 igams_sjyz 送检信息里
			SjyzDto sjyzDto = JSONObject.parseObject(str, SjyzDto.class);
			sjyzService.delete(sjyzDto);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.DEL_INSPECTION_VERIFICATION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.DEL_INSPECTION_VERIFICATION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	@RabbitListener(queues = MqWechatType.ADD_PARTNER, containerFactory="defaultFactory")
	public void receiveAddPartner(String str) {
		log.error("Receive AddPartner:"+str);
        try {
			//把数据录入到 igams_sjhbxx 合作伙伴信息里
        	SjhbxxDto sjhbxxDto = JSONObject.parseObject(str, SjhbxxDto.class);
        	sjhbxxService.receiveAddPartner(sjhbxxDto);
			hbdwqxService.deleteById(sjhbxxDto.getHbid());
			//再增加新的信息
			String[] jcdwids = sjhbxxDto.getJcdwids();
			List<HbdwqxDto> list = new ArrayList<>();
			for (int i = 0; i < jcdwids.length; i++) {
				HbdwqxDto hbdwqx = new HbdwqxDto();
				hbdwqx.setHbid(sjhbxxDto.getHbid());
				hbdwqx.setXh(Integer.toString(i+1));
				hbdwqx.setJcdw(jcdwids[i]);
				list.add(hbdwqx);
			}
			if(jcdwids.length > 0) {
				hbdwqxService.insertjcdw(list);
			}
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.ADD_PARTNER);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.ADD_PARTNER);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	@RabbitListener(queues = MqWechatType.MOD_PARTNER, containerFactory="defaultFactory")
	public void receiveModPartner(String str) {
		log.error("Receive ModPartner:"+str);
        try {
        	//把数据录入到 igams_sjhbxx 合作伙伴信息里
        	SjhbxxDto sjhbxxDto = JSONObject.parseObject(str, SjhbxxDto.class);
        	sjhbxxService.receiveModPartner(sjhbxxDto);
			hbdwqxService.deleteById(sjhbxxDto.getHbid());
			//再增加新的信息
			String[] jcdwids = sjhbxxDto.getJcdwids();
			List<HbdwqxDto> list = new ArrayList<HbdwqxDto>();
			for (int i = 0; i < jcdwids.length; i++) {
				HbdwqxDto hbdwqx = new HbdwqxDto();
				hbdwqx.setHbid(sjhbxxDto.getHbid());
				hbdwqx.setXh(Integer.toString(i+1));
				hbdwqx.setJcdw(jcdwids[i]);
				list.add(hbdwqx);
			}
			if(jcdwids.length > 0) {
				hbdwqxService.insertjcdw(list);
			}
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.MOD_PARTNER);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Receive ModPartner:"+e.getMessage());
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.MOD_PARTNER);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }

	/**
	 * 定时任务更细收费明细同步
	 * @param str
	 */
	@RabbitListener(queues = MqWechatType.MOD_PARTNER_TOLL, containerFactory="defaultFactory")
	public void receiveModPartnerToll(String str) {
		log.error("Receive ModPartnerToll:"+str);
		try {
			List<HbsfbzDto> list=(List<HbsfbzDto>)JSON.parseArray(str,HbsfbzDto.class);
			List<HbsfbzDto> list_add = new ArrayList<>();
			for (HbsfbzDto hbsfbzDto : list) {
				if(StringUtil.isNotBlank(hbsfbzDto.getInsertFlag()) && "1".equals(hbsfbzDto.getInsertFlag())){
					list_add.add(hbsfbzDto);
				}
			}

			if (!CollectionUtils.isEmpty(list)){
				//批量修改伙伴收费标准
				hbsfbzservice.updateList(list);
			}


			if (!CollectionUtils.isEmpty(list_add)){
				//批量修改伙伴收费标准
				hbsfbzservice.insertsfbz(list_add);
			}

			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.MOD_PARTNER_TOLL);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.MOD_PARTNER_TOLL);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}

	@RabbitListener(queues = MqWechatType.BatchMod_SFBZ, containerFactory="defaultFactory")
	public void receiveBatchModPartnerToll(String str) {
		log.error("Receive batchModPartnerToll:"+str);
		try {
			Map<String,Object> param=JSON.parseObject(str,Map.class);
			if (!CollectionUtils.isEmpty((List<HbsfbzDto>) param.get("modList"))){
				//批量修改伙伴收费标准
				hbsfbzservice.batchModSfbz((List<HbsfbzDto>) param.get("modList"));
			}
			if (!CollectionUtils.isEmpty((List<HbsfbzDto>) param.get("modList"))){
				//批量新增伙伴收费标准
				hbsfbzservice.insertsfbz((List<HbsfbzDto>) param.get("addList"));
			}
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.BatchMod_SFBZ);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.BatchMod_SFBZ);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	@RabbitListener(queues = MqWechatType.DEL_PARTNER, containerFactory="defaultFactory")
	public void receiveDelPartner(String str) {
		log.error("Receive DelPartner:"+str);
        try {
        	//把数据录入到 igams_sjhbxx 合作伙伴信息里
        	SjhbxxDto sjhbxxDto = JSONObject.parseObject(str, SjhbxxDto.class);
        	sjhbxxService.receiveDelPartner(sjhbxxDto);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.DEL_PARTNER);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.DEL_PARTNER);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }

	@RabbitListener(queues = MqWechatType.MOD_HOSPITAL, containerFactory="defaultFactory")
	public void receiveModHospital(String str) {
		log.error("Receive ModHospital:"+str);
        try {
			// 把数据更新到医院信息表中
        	YyxxDto yyxxDto = JSONObject.parseObject(str, YyxxDto.class);
        	if(yyxxDto != null && StringUtil.isNotBlank(yyxxDto.getDwid())) {
        		YyxxDto t_yyxxDto = yyxxService.getDtoById(yyxxDto.getDwid());
        		if(t_yyxxDto != null) { // 更新
        			yyxxService.update(yyxxDto);
        		}else { // 新增
        			yyxxService.insertDto(yyxxDto);
        		}
        	}
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.MOD_HOSPITAL);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.MOD_HOSPITAL);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	@RabbitListener(queues = MqWechatType.DEL_HOSPITAL, containerFactory="defaultFactory")
	public void receiveDelHospital(String str) {
		log.error("Receive DelHospital:"+str);
        try {
			// 把数据更新到医院信息表中
        	YyxxDto yyxxDto = JSONObject.parseObject(str, YyxxDto.class);
        	yyxxService.delete(yyxxDto);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.DEL_HOSPITAL);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.DEL_HOSPITAL);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	// 暂时不同步送检临床指南信息(可能后期需要)
	/*@RabbitListener(queues = MqType.ADD_INSPECTION_GUIDE)
	public void receiveAddInspectionGuide(String str) {
		log.error("Receive ModInspection:"+str);
        try {
			//把数据录入到 igams_sjxx 送检信息里
			SjlcznDto sjlcznDto = JSONObject.parseObject(str, SjlcznDto.class);
			sjlcznService.insertSjlczn(sjlcznDto);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.ADD_INSPECTION_GUIDE);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.ADD_INSPECTION_GUIDE);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }*/
	
	@RabbitListener(queues = MqWechatType.SELECT_PAY_RESULT, containerFactory="defaultFactory")
	public void selectPayResult(String str) {
		log.error("Receive selectPayResult:"+str);
        try {
        	// 开启查询支付结果的线程
        	SelectPayResultThread selectPayResultThread = new SelectPayResultThread(payinfoService, bankService, xtszService, restTemplate);
        	selectPayResultThread.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.SELECT_PAY_RESULT);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	/*----------------------------------------    整合rabbit    ----------------------------------------*/
	//设置队列单活 一个挂掉另一个继续消费（始终一个消费）
	//        Map<String, Object> args = new HashMap<>();
	//        args.put("x-single-active-consumer", true);
	//        Queue queue = new Queue(MQTypeEnum.RESULT_INSPECTION.getCode(), true, false, false, args);
	/*
		由于多消费者导致执行顺序出现问题，所以进行改造，来保证消息的顺序性。
		1.首先设置specialFactory 设置预取数为1，保证消息接收时的顺序性。否则在消息消费之前就已经把信息放到相应的服务器上，某一个服务器卡在，
		  其上所有消息都延迟，就无法保证消息的顺序执行。只是设置预取数为1，消费完成后才从rabbit服务器获取下一个消息，才能保证顺序。
		2.dealInspectionMsgBefore 先直接将消息有序存储消息到redis list中。为了防止多台服务器同时执行消费消息，除其中一台抢到锁以后，
		  其他的服务器在下一次抢锁中无法确保顺序，创建redis list，保存顺序。
		3.dealInspectionMsg 再从redis list中取出消息进行处理
			（1）加锁，去竞争锁
			（2）获取锁后，获取list里的第一条消息，判断消息是否是我要执行的消息，如果不是，释放锁 sleep 100ms：让它线程去执行判断 然后再去竞争锁
			（3）如果是，处理消息， 最后removeValueFromList删除所有符合条件的消息（消息是唯一的）（防止重复消费） 释放锁
	 */
	@RabbitListener(queues = MqWechatType.OPERATE_INSPECT, containerFactory="specialFactory")
	public void receiveInspection(String str) {
		log.error("Receive OPERATE_INSPECT:"+str);
        try {
        	if(StringUtil.isNotBlank(str) && str.length() > 13) {
				String lx = str.substring(13,21);
				String msg = str.substring(21);
        		if(RabbitEnum.INSP_ADD.getCode().equals(lx)) {
        			log.error("Receive INSP_ADD");
        			SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					dealInspectionMsgBefore(sjxxDto.getSjid(),str);
            	}else if(RabbitEnum.INSP_MOD.getCode().equals(lx)) {
            		log.error("Receive INSP_MOD");
        			SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					dealInspectionMsgBefore(sjxxDto.getSjid(),str);
				}else if(RabbitEnum.INSP_ADJ.getCode().equals(lx)) {
					log.debug("Receive INSP_ADJ");
					List<SjsyglDto> list=(List<SjsyglDto>)JSON.parseArray(msg,SjsyglDto.class);
					dealInspectionMsgBefore(list.get(0).getSjid(),str);
				}else if(RabbitEnum.BBSY_ADJ.getCode().equals(lx)) {
					log.debug("Receive BBSY_ADJ");
					SjsyglDto sjsyglDto=JSONObject.parseObject(msg,SjsyglDto.class);
					sjsyglService.adjustSaveSjxx(sjsyglDto);
				}else if(RabbitEnum.SJSY_MOD.getCode().equals(lx)) {
					log.debug("Receive SJSY_MOD");
					List<SjsyglDto> list=(List<SjsyglDto>)JSON.parseArray(msg,SjsyglDto.class);
					dealInspectionMsgBefore(list.get(0).getSjid(),str);
				}else if(RabbitEnum.SJSY_ADJ.getCode().equals(lx)) {
					log.debug("Receive SJSY_ADJ");
					List<SjsyglDto> list=(List<SjsyglDto>)JSON.parseArray(msg,SjsyglDto.class);
					dealInspectionMsgBefore(list.get(0).getSjid(),str);
				}else if(RabbitEnum.SJSY_ADD.getCode().equals(lx)) {
					log.debug("Receive SJSY_ADD");
					List<SjsyglDto> list=(List<SjsyglDto>)JSON.parseArray(msg,SjsyglDto.class);
					dealInspectionMsgBefore(list.get(0).getSjid(),str);
            	}else if(RabbitEnum.XMSY_MOD.getCode().equals(lx)) {
					log.debug("Receive XMSY_MOD");
					List<XmsyglDto> list=(List<XmsyglDto>)JSON.parseArray(msg,XmsyglDto.class);
					dealInspectionMsgBefore(list.get(0).getYwid(),str);
				}else if(RabbitEnum.INSP_DEL.getCode().equals(lx)) {
            		log.error("Receive INSP_DEL");
        			SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					dealInspectionMsgBefore(sjxxDto.getSjid(),str);
				}else if(RabbitEnum.AMOU_MOD.getCode().equals(lx)) {
					log.error("Receive AMOU_MOD");
					SjjcxmDto sjjcxmDto = JSONObject.parseObject(msg, SjjcxmDto.class);
					dealInspectionMsgBefore(sjjcxmDto.getSjid(),str);
            	}else if(RabbitEnum.INSP_CFM.getCode().equals(lx)) {
            		log.error("Receive INSP_CFM");
        			SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					dealInspectionMsgBefore(sjxxDto.getSjid(),str);
            	}else if(RabbitEnum.SJHB_MOD.getCode().equals(lx)) {
            		log.error("Receive SJHB_MOD");
        			SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
        			sjxxService.updateDB(sjxxDto);
            	}else if(RabbitEnum.CSKZ_UPD.getCode().equals(lx)) {
        			//（只同步是否收费信息）
            		log.error("Receive CSKZ_UPD");
        			SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
        			sjxxService.updateCskz(sjxxDto);
            	}else if(RabbitEnum.TSSQ_MOD.getCode().equals(lx)) {
					log.error("Receive TSSQ_MOD");
					SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					dealInspectionMsgBefore(sjxxDto.getSjid(),str);
				}else if(RabbitEnum.INSP_XHU.getCode().equals(lx)) {
					log.error("Receive INSP_XHU");
					SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					dealInspectionMsgBefore(sjxxDto.getSjid(),str);
				}else if(RabbitEnum.INSP_IMP.getCode().equals(lx)) {
					log.debug("Receive INSP_IMP");
					SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					dealInspectionMsgBefore(sjxxDto.getSjid(),str);
				}else if(RabbitEnum.SFJS_UPD.getCode().equals(lx)) {//更改送检的是否接收，接收人员，接收日期为null
					log.debug("Receive sfjs_upd");
					List<SjxxDto> list=(List<SjxxDto>)JSON.parseArray(msg,SjxxDto.class);
					sjxxService.updateSfjsInfo(list);
				}else if(RabbitEnum.YFJE_MODLIST.getCode().equals(lx)) {
					log.debug("Receive YFJE_MODLIST");
					List<SjjcxmDto> list=(List<SjjcxmDto>)JSON.parseArray(msg,SjjcxmDto.class);
					sjjcxmService.updateSjjcxmDtos(list);
				}else if(RabbitEnum.FKJE_MODLIST.getCode().equals(lx)) {
					log.debug("Receive FKJE_MODLIST");
					List<SjxxDto> list=(List<SjxxDto>)JSON.parseArray(msg,SjxxDto.class);
					sjxxService.updateFkje(list);
				}else if(RabbitEnum.HBXXZ_ADD.getCode().equals(lx)) {
					log.error("Receive HBXXZ_ADD");
					HbxxzDto hbxxzDto = JSONObject.parseObject(msg, HbxxzDto.class);
					hbxxzService.insertDto(hbxxzDto);
				}else if(RabbitEnum.HBXXZ_MOD.getCode().equals(lx)) {
					log.error("Receive HBXXZ_MOD");
					HbxxzDto hbxxzDto = JSONObject.parseObject(msg, HbxxzDto.class);
					hbxxzService.updateDto(hbxxzDto);
				}else if(RabbitEnum.HBXXZ_DEL.getCode().equals(lx)) {
					log.error("Receive HBXXZ_DEL");
					HbxxzDto hbxxzDto = JSONObject.parseObject(msg, HbxxzDto.class);
					hbxxzService.delete(hbxxzDto);
				}
        	}
		} catch (Exception e) {
			log.error(e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.OPERATE_INSPECT);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	//rabbit消息前处理
	public void dealInspectionMsgBefore(String keyId,String str) throws InterruptedException{
		String lockkey = RedisCommonKeyEnum.INSPECTION_WECHAT_LOCK.getKey()+keyId;
		String dataKey = RedisCommonKeyEnum.INSPECTION_WECHAT_LIST.getKey()+keyId;
		//将消息存储到list中
		redisUtil.lSet(dataKey, str, RedisCommonKeyEnum.INSPECTION_WECHAT_LIST.getExpire());
		dealInspectionMsg(lockkey,dataKey,str);
	}
	//处理rabbit消息
	public void dealInspectionMsg(String lockkey,String dataKey,String rabbitMsg) throws InterruptedException {
		while(!redisUtil.setIfAbsent(lockkey, "matridx", RedisCommonKeyEnum.INSPECTION_WECHAT_LOCK.getExpire(), TimeUnit.SECONDS)){
			Thread.sleep(50);
		}
		//获取list中的第一个消息
		Object obj = redisUtil.lGetIndex(dataKey, 0);
		//获取消息内容进行处理
		String str;
		//如果为空
		if(obj == null){
			//没有获取到，则不做任何处理
			redisUtil.del(lockkey);
			return;
		}else {
			str = String.valueOf(obj);
		}
		//如果不是我要执行的消息，不做处理
		if (!str.equals(rabbitMsg)){
			log.error("dealInspectionMsg not my message。当前内容:" + str);
			//释放锁
			redisUtil.del(lockkey);
			//让其他去获取锁
			Thread.sleep(100);
			//再次竞争,key 需要更改，否则陷入死循环
			String msg = str.substring(21);
			SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
			String new_keyId = sjxxDto.getSjid();
			String new_lockkey = RedisCommonKeyEnum.INSPECTION_WECHAT_LOCK.getKey() + new_keyId;
			String new_dataKey = RedisCommonKeyEnum.WECHAT_INSPECTION_LIST.getKey() + new_keyId;
			//再次竞争
			dealInspectionMsg(new_lockkey,new_dataKey,str);
		}else {
			try {
				String lx = str.substring(13, 21);
				String msg = str.substring(21);
				log.error("dealInspectionMsg {}", lx);
				if (RabbitEnum.INSP_ADD.getCode().equals(lx)) {
					SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					sjxxService.receiveAddInspection(sjxxDto);
				} else if (RabbitEnum.INSP_MOD.getCode().equals(lx)) {
					SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					sjxxDto.setFkje(null);
					sjxxDto.setFkrq(null);
					sjxxService.receiveModInspection(sjxxDto);
				} else if (RabbitEnum.INSP_ADJ.getCode().equals(lx)) {
					List<SjsyglDto> list = (List<SjsyglDto>) JSON.parseArray(msg, SjsyglDto.class);
					sjsyglService.updateList(list);
				} else if (RabbitEnum.SJSY_MOD.getCode().equals(lx)) {
					List<SjsyglDto> list = (List<SjsyglDto>) JSON.parseArray(msg, SjsyglDto.class);
					sjsyglService.syncRabbitMsg(list);
				} else if (RabbitEnum.SJSY_ADJ.getCode().equals(lx)) {
					List<SjsyglDto> list = (List<SjsyglDto>) JSON.parseArray(msg, SjsyglDto.class);
					sjsyglService.syncRabbitMsg(list);
				} else if (RabbitEnum.SJSY_ADD.getCode().equals(lx)) {
					List<SjsyglDto> list = (List<SjsyglDto>) JSON.parseArray(msg, SjsyglDto.class);
					sjsyglService.insertList(list);
				} else if (RabbitEnum.XMSY_MOD.getCode().equals(lx)) {
					List<XmsyglDto> list = (List<XmsyglDto>) JSON.parseArray(msg, XmsyglDto.class);
					xmsyglService.syncRabbitMsg(list);
				} else if (RabbitEnum.INSP_DEL.getCode().equals(lx)) {
					SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					sjxxService.receiveDelInspection(sjxxDto);
				} else if (RabbitEnum.AMOU_MOD.getCode().equals(lx)) {
					SjjcxmDto sjjcxmDto = JSONObject.parseObject(msg, SjjcxmDto.class);
					sjxxService.saveProjectAmount(sjjcxmDto);
				} else if (RabbitEnum.INSP_CFM.getCode().equals(lx)) {
					SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					sjxxService.receiveConfirmInspection(sjxxDto);
				} else if (RabbitEnum.TSSQ_MOD.getCode().equals(lx)) {
					SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					sjjcxmService.updateSjjcxmDtos(sjxxDto.getSjjcxms());
					sjxxService.updateTssq(sjxxDto);
				} else if (RabbitEnum.INSP_XHU.getCode().equals(lx)) {
					SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					sjxxDto.setTbbj(0);
					sjxxService.receiveModInspection(sjxxDto);
				} else if (RabbitEnum.INSP_IMP.getCode().equals(lx)) {
					SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					sjxxService.syncImportInspection(sjxxDto);
				}
			} catch (Exception e) {
				log.error("dealInspectionMsg 异常{}", str);
			} finally {
				//删除 防止重复处理
				redisUtil.removeValueFromList(dataKey, str);
				redisUtil.del(lockkey);
			}
		}
	}

	/*---------------------------------------    信息对应 ---------------------------------------*/
	//消息对应rabbit
	@RabbitListener(queues = MqWechatType.OPERATE_XXDY, containerFactory="defaultFactory")
	public void receiveOperateXxdy(String str) {
		log.error("Receive OPERATE_XXDY:"+str);
		try {
			if(StringUtil.isNotBlank(str) && str.length() > 8) {
				String lx = str.substring(0,8);
				String msg = str.substring(8);
				if(RabbitEnum.XXDY_ADD.getCode().equals(lx)) {
					log.error("Receive XXDY_ADD");
					XxdyDto xxdyDto = JSONObject.parseObject(msg, XxdyDto.class);
					xxdyService.insertDto(xxdyDto);
				}else if(RabbitEnum.XXDY_MOD.getCode().equals(lx)) {
					log.error("Receive XXDY_MOD");
					XxdyDto xxdyDto = JSONObject.parseObject(msg, XxdyDto.class);
					xxdyService.updateDto(xxdyDto);
				}else if(RabbitEnum.XXDY_DEL.getCode().equals(lx)) {
					log.error("Receive XXDY_DEL");
					XxdyDto xxdyDto = JSONObject.parseObject(msg, XxdyDto.class);
					xxdyService.delete(xxdyDto);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.OPERATE_XXDY);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}

	@RabbitListener(queues = MqWechatType.SSE_SENDMSG_EXCEPRION, containerFactory="defaultFactory")
	public void sseSend(String str) {
		if(StringUtil.isNotBlank(str)) {
			Map<String, Object> map = JSONObject.parseObject(str);
			if (map.get("type") != null) {
				try {
					switch (map.get("type").toString()) {
						case "hf":
							exceptionWechartUtil.addExceptionMessage(map.get("yhid").toString(),map.get("ycid").toString());
							Object exceptionConectMap_redis = redisUtil.get("EXCEPTION_CONNECT:" + map.get("yhid").toString());
							JSONObject exceptionConectMap = JSON.parseObject(exceptionConectMap_redis.toString());
							JSONObject exceptionlist = JSON.parseObject(exceptionConectMap.get("exceptionlist").toString());
							if (exceptionlist.get(map.get("ycid").toString()) != null) {
								JSONObject exceptionMap = JSON.parseObject(exceptionlist.get(map.get("ycid").toString()).toString());
								//从0到1发送模板消息
								if("1".equals(exceptionMap.get("ex_unreadcnt").toString())){
									//TODO
									// 发送模板消息
									WxyhDto wxyhDto=new WxyhDto();
									wxyhDto.setWxid( map.get("yhid").toString());
									wxyhDto=wxyhService.getDto(wxyhDto);
									Map<String, String> messageMap = new HashMap<>();
									SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
									Date date=new Date();
									String nowDate=sdf.format(date);
									SjycDto ycDto=JSONObject.parseObject(map.get("sjycDto").toString(),SjycDto.class);
									messageMap.put("keyword2", nowDate);
									messageMap.put("keyword1", ycDto.getYcbt());
									SjycfkWechatDto sjycfkWechatDto=JSONObject.parseObject(map.get("sjycfkDto").toString(),SjycfkWechatDto.class);
									messageMap.put("keyword3",sjycfkWechatDto.getFkxx() );
									SjycWechatDto sjycDto=new SjycWechatDto();
									sjycDto.setYcid(map.get("ycid").toString());
									sjycDto.setGzpt(wxyhDto.getWbcxdm());
									sjycDto=sjycService.getDto(sjycDto);

									String reporturl=menuurl+"/wechat/inspComplaintInfo?ywid="+sjycDto.getYwid()+"&ycid="+map.get("ycid").toString()+"&wxid="+wxyhDto.getWxid()+"&wbcxdm="+wxyhDto.getWbcxdm();
									messageMap.put("reporturl",reporturl);
									WeChatUtils.sendWeChatMessageMap(redisUtil,restTemplate, "TEMPLATE_EXCEPTION", map.get("yhid").toString(), wxyhDto.getWbcxdm(),messageMap);

								}
								//同步数据
								sjycfkService.insertDto(JSONObject.parseObject(map.get("sjycfkDto").toString(),SjycfkWechatDto.class));
							}
							break;
						case "xg":
							SjycWechatDto _sjycDto=JSONObject.parseObject(map.get("sjycDto").toString(),SjycWechatDto.class);
							sjycService.modSaveException(_sjycDto);
							if(_sjycDto.getSjycstatisticsids()!=null&&_sjycDto.getSjycstatisticsids().size()>0){
								SjycStatisticsWechatDto delDto=new SjycStatisticsWechatDto();
								delDto.setYcid(_sjycDto.getYcid());
								sjycStatisticsWechatService.delByYcid(delDto);
								List<SjycStatisticsWechatDto> sjycStatisticsDtoList=new ArrayList<>();
								for(String statisticsId:_sjycDto.getSjycstatisticsids()){
									if(StringUtil.isBlank(statisticsId)){
										continue;
									}
									SjycStatisticsWechatDto sjycStatisticsDto=new SjycStatisticsWechatDto();
									sjycStatisticsDto.setYcid(_sjycDto.getYcid());
									sjycStatisticsDto.setSjid(_sjycDto.getYwid());
									sjycStatisticsDto.setStatisticsid(StringUtil.generateUUID());
									sjycStatisticsDto.setJcsjcsid(statisticsId);
									sjycStatisticsDtoList.add(sjycStatisticsDto);
								}
								sjycStatisticsWechatService.insertList(sjycStatisticsDtoList);
							}
							break;
						case "zf":
							SjycWechatDto _sjycDto1=JSONObject.parseObject(map.get("sjycDto").toString(),SjycWechatDto.class);
							sjycService.modSaveException(_sjycDto1);
							break;
						case "finish":
							SjycWechatDto _sjycDto2=JSONObject.parseObject(map.get("sjycDto").toString(), SjycWechatDto.class);
							sjycService.finishYc(_sjycDto2);
							break;
						case "add":
							SjycWechatDto _sjycDto3=JSONObject.parseObject(map.get("sjycDto").toString(), SjycWechatDto.class);
							SjycWechatDto sjycWechatDto=sjycService.getDto(_sjycDto3);
							if(sjycWechatDto==null){
								sjycService.addSaveException(_sjycDto3);
								WxyhDto wxyhDto=new WxyhDto();
								wxyhDto.setWxid( _sjycDto3.getTwr());
								wxyhDto=wxyhService.getDto(wxyhDto);
								Map<String, String> messageMap = new HashMap<>();
								SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
								Date date=new Date();
								String nowDate=sdf.format(date);
								SjycDto ycDto=JSONObject.parseObject(map.get("sjycDto").toString(),SjycDto.class);
								messageMap.put("keyword2", nowDate);
								messageMap.put("keyword1", ycDto.getYcbt());
								messageMap.put("keyword3","您有一条新的异常");

								String reporturl=menuurl+"/wechat/inspComplaintInfo?ywid="+_sjycDto3.getYwid()+"&ycid="+_sjycDto3.getYcid()+"&wxid="+wxyhDto.getWxid()+"&wbcxdm="+wxyhDto.getWbcxdm();
								messageMap.put("reporturl",reporturl);
								WeChatUtils.sendWeChatMessageMap(redisUtil,restTemplate, "TEMPLATE_EXCEPTION", _sjycDto3.getTwr(), wxyhDto.getWbcxdm(),messageMap);
							}
							break;
					}
				} catch (Exception e) {
					log.error(e.getMessage());
					XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
					xxdlcwglDto.setCwid(StringUtil.generateUUID());
					xxdlcwglDto.setCwlx(MqWechatType.SSE_SENDMSG_EXCEPRION);
					if(str.length() > 4000){
						str = str.substring(0, 4000);
					}
					xxdlcwglDto.setYsnr(str);
					xxdlcwglService.insert(xxdlcwglDto);
				}
			}
		}
	}

	/**
	 * 伙伴启用同步
	 * @param str
	 */
	@RabbitListener(queues = MqWechatType.ENABLE_OR_DISABLE_PARTNER, containerFactory="defaultFactory")
	public void receiveEnableOrDisablePartner(String str) {
		log.error("Receive EnableOrDisablePartner:"+str);
		try {
			SjhbxxDto sjhbxxDto = JSONObject.parseObject(str, SjhbxxDto.class);
			if("0".equals(sjhbxxDto.getScbj())){
				sjhbxxService.enablepartner(sjhbxxDto);
			}else if("2".equals(sjhbxxDto.getScbj())){
				sjhbxxService.disablepartner(sjhbxxDto);
			}
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.MOD_PARTNER_TOLL);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.MOD_PARTNER_TOLL);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	/**
	 * 更新分析完成时间
	 * @param str
	 */
	@RabbitListener(queues = MqWechatType.ADD_FXWCSJ, containerFactory="defaultFactory")
	public void addFxwcsj(String str) {
		log.error("Receive ADD_FXWCSJ:"+str);
		try {
			Map<String,String> map = JSONObject.parseObject(str, Map.class);
			SjxxDto sjxxDto=new SjxxDto();
			sjxxDto.setYbbh(map.get("ybbh"));
			sjxxDto.setFxwcsj(map.get("fxwcsj"));
			sjxxService.updateFxwcsjByYbbh(sjxxDto);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.ADD_FXWCSJ);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqWechatType.ADD_FXWCSJ);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
}
