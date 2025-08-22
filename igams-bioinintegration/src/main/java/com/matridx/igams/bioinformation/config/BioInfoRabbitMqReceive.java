package com.matridx.igams.bioinformation.config;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.bioinformation.dao.entities.ReceiveFileModel;
import com.matridx.igams.bioinformation.service.svcinterface.ICnvFileService;
import com.matridx.igams.bioinformation.service.svcinterface.IMngsFileParsingService;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.XxdlcwglDto;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IXxdlcwglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BioInfoRabbitMqReceive {

	@Autowired
	IXxdlcwglService xxdlcwglService;
	@Value("${matridx.rabbit.flg:}")
	String rabbitFlg;
	@Value("${matridx.rabbit.preflg:}")
	String preRabbitFlg;
	@Autowired
	ICnvFileService cnvFileService;
	@Autowired
	IMngsFileParsingService mngsFileParsingService;
	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	RedisUtil redisUtil;
	@Autowired(required=false)
	AmqpTemplate amqpTempl;
	private final Logger log = LoggerFactory.getLogger(BioInfoRabbitMqReceive.class);
	/**
	 * 文件解析
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.document.parse${matridx.rabbit.flg:}"))
	public void documentParse(String str){
		try {
			SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
			String format1 = simpleDateFormat1.format(new Date());
			ReceiveFileModel receiveFileModel = JSONObject.parseObject(str, ReceiveFileModel.class);
			String lx=DingMessageType.BIOINFORMATION_ERROR.getCode();
			Object bioinformation_error = redisUtil.get(lx);
			SimpleDateFormat date_Format = new SimpleDateFormat("yyyy-MM-dd");
			String s_chipDate = "";

			if(StringUtil.isNotBlank(receiveFileModel.getChip())){
				boolean flagsdf=true;
				String t_chipdata = receiveFileModel.getChip();
				String datesdf = "";
				if(t_chipdata.startsWith("20")) {
					if(t_chipdata.length()>=8)
						datesdf = receiveFileModel.getChip().substring(0,8);
					else
						datesdf = receiveFileModel.getChip().substring(0,6) + "01";
				}else{
					datesdf = "20" + receiveFileModel.getChip().substring(0,6);
				}
				try{
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
					// 设置为非宽容模式，这样可以更严格地解析日期
					simpleDateFormat.setLenient(false);
					Date d_chipdate = simpleDateFormat.parse(datesdf);
					s_chipDate = date_Format.format(d_chipdate);
				}catch (Exception e){
					flagsdf=false;
				}
				if(!flagsdf){
					try{
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
						// 设置为非宽容模式，这样可以更严格地解析日期
						simpleDateFormat.setLenient(false);
						Date d_chipdate = simpleDateFormat.parse(datesdf.substring(0,6));
						s_chipDate = date_Format.format(d_chipdate);
					}catch (Exception e){
						flagsdf=false;
					}
				}

				if(!flagsdf){
					log.error(receiveFileModel.getChip()+"芯片名不符合规范");
					return ;
				}
			}
			if(bioinformation_error!=null){
				List<String> errorList = (List<String>) bioinformation_error;
				boolean flag=true;
				for(String s:errorList){
					if (s.equals(receiveFileModel.getChip() + "_" + receiveFileModel.getSample() + "_" + receiveFileModel.getProgram())) {
						flag = false;
						break;
					}
				}
				if(flag){
					String date ="";
					log.error("documentParse 芯片信息:"+str);
					if(StringUtil.isNotBlank(receiveFileModel.getChip())){
						date = s_chipDate+" "+format1;
					}
					if(StringUtil.isNotBlank(receiveFileModel.getSample())){
						String[] strings = receiveFileModel.getSample().split("-");
						String string = strings[strings.length - 1];
						String date1 = mngsFileParsingService.findDate(strings, string, strings.length - 1);
						date = s_chipDate +" "+format1;
						date=date.replace("-","");
					}

					mngsFileParsingService.FileParseingByType(receiveFileModel,date);
					cnvFileService.FileParseingByType(receiveFileModel,date);
				}else{
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg+"sys.igams.document.parse"+rabbitFlg, str);
				}
			}else{
				String date ="";
				log.error("documentParse2 芯片信息:"+str);
				if(StringUtil.isNotBlank(receiveFileModel.getChip())){
					date = s_chipDate+" "+format1;
				}
				if(StringUtil.isNotBlank(receiveFileModel.getSample())){
					String[] strings = receiveFileModel.getSample().split("-");
					String string = strings[strings.length - 1];
					String date1 = mngsFileParsingService.findDate(strings, string, strings.length - 1);
					date = date1+" "+format1;
				}
				cnvFileService.FileParseingByType(receiveFileModel,date);
				mngsFileParsingService.FileParseingByType(receiveFileModel,date);
			}
		} catch (Exception e) {
//			log.error("document.parse ERROR :"+e.getMessage());
			if(e.getMessage().contains("dtoNew没有找到")){
				log.error(" dtoNew没有找到  "+"message:"+e.getMessage());
				return;
			}
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.document.parse");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
			String lx=DingMessageType.BIOINFORMATION_ERROR.getCode();
			Object bioinformation_error = redisUtil.get(lx);
			ReceiveFileModel receiveFileModel = JSONObject.parseObject(str, ReceiveFileModel.class);
			if(bioinformation_error==null){
				List<DdxxglDto> ddxxList=ddxxglService.selectByDdxxlx(lx);
//				String token =talkUtil.getToken();
				if(ddxxList!=null&&!ddxxList.isEmpty()) {
					for (DdxxglDto ddxxglDto : ddxxList) {
						if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
							talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), "通知", "文库编号：" + receiveFileModel.getSample() + "版本号" + receiveFileModel.getProgram() + "的样本结果文件解析出错！");
						}
					}
				}
				List<String> list=new ArrayList<>();
				list.add(receiveFileModel.getChip()+"_"+receiveFileModel.getSample()+"_"+receiveFileModel.getProgram());
				redisUtil.set(lx,list,3600);

				Object RABBITMQ_REPEAT_FLG = redisUtil.get("RABBITMQ_NOTREPEAT_FLG");
				log.error("document.parse ERROR :"+e.getMessage() + " RABBITMQ_REPEAT_FLG:" + (String)RABBITMQ_REPEAT_FLG);
				if(RABBITMQ_REPEAT_FLG!=null && "1".equals((String)RABBITMQ_REPEAT_FLG))
					return;
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg+"sys.igams.document.parse"+rabbitFlg, str);
			}else{
				List<String> errorList = (List<String>) bioinformation_error;
				boolean flag=true;
				for(String s:errorList){
					if (s.equals(receiveFileModel.getChip() + "_" + receiveFileModel.getSample() + "_" + receiveFileModel.getProgram())) {
						flag = false;
						break;
					}
				}
				if(flag){
					List<DdxxglDto> ddxxList=ddxxglService.selectByDdxxlx(lx);
//					String token =talkUtil.getToken();
					if(ddxxList!=null&&!ddxxList.isEmpty()) {
						for (DdxxglDto ddxxglDto : ddxxList) {
							if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
								talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), "通知", "文库编号：" + receiveFileModel.getSample() + "版本号" + receiveFileModel.getProgram() + "的样本结果文件解析出错！");
							}
						}
					}
					errorList.add(receiveFileModel.getChip()+"_"+receiveFileModel.getSample()+"_"+receiveFileModel.getProgram());
					redisUtil.set(lx,errorList,3600);
					
					Object RABBITMQ_REPEAT_FLG = redisUtil.get("RABBITMQ_NOTREPEAT_FLG");
					log.error("document.parse ERROR2 :"+e.getMessage() + " RABBITMQ_REPEAT_FLG:" + (String)RABBITMQ_REPEAT_FLG);
					if(RABBITMQ_REPEAT_FLG!=null && "1".equals((String)RABBITMQ_REPEAT_FLG))
						return;
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg+"sys.igams.document.parse"+rabbitFlg, str);
				}else{
					Object RABBITMQ_REPEAT_FLG = redisUtil.get("RABBITMQ_NOTREPEAT_FLG");
					log.error("document.parse ERROR3 :"+e.getMessage() + " RABBITMQ_REPEAT_FLG:" + (String)RABBITMQ_REPEAT_FLG);
					if(RABBITMQ_REPEAT_FLG!=null && "1".equals((String)RABBITMQ_REPEAT_FLG))
						return;
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg+"sys.igams.document.parse"+rabbitFlg, str);
				}
			}

		}
	}


}
