package com.matridx.igams.detection.molecule.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.XxdlcwglDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IXxdlcwglService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxModel;
import com.matridx.igams.detection.molecule.dao.entities.HzxxDto;
import com.matridx.igams.detection.molecule.dao.entities.WxbdglDto;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxmPJService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxmService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxxPJService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxxService;
import com.matridx.igams.detection.molecule.service.svcinterface.IHzxxService;
import com.matridx.igams.detection.molecule.service.svcinterface.IWxbdglService;
import com.matridx.igams.detection.molecule.util.DetMqType;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DetRabbitMqReceive {
	private final Logger log = LoggerFactory.getLogger(DetRabbitMqReceive.class);
	@Autowired
	IHzxxService hzxxService;
	@Autowired
	IFzjcxxService fzjcxxService;
	@Autowired
	IFzjcxxPJService fzjcxxPJService;
	@Autowired
	IFzjcxmService fzjcxmService;
	@Autowired
	IXxdlcwglService xxdlcwglService;
	@Autowired
	IWxbdglService wxbdglService;
	@Autowired
	DingTalkUtil talkUtil;

	@Autowired
	IXxglService xxglService;

	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IFzjcxmPJService fzjcxmPJService;
	/**
	 * 送检消息整合
	 */
	@SuppressWarnings("unchecked")
	@RabbitListener(queues = DetMqType.APPOINTMENT_DETECTION, containerFactory="defaultFactory")
	public void appointmentDetection(String str) {
		log.error("Receive APPOINTMENT_DETECTION:"+str);
        try {
        	if(StringUtil.isNotBlank(str) && str.length() > 8) {
        		String lx = str.substring(0,8);
        		String msg = str.substring(8);
				/*新冠取消预约*/
            	if(RabbitEnum.FZXX_DEL.getCode().equals(lx)) {
            		//log.error("Receive FZXX_DEL");
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
					fzjcxxService.cancelDetectionAppointment(fzjcxxDto);
            	}
				/*付款标记修改*/
				if(RabbitEnum.FKBJ_MOD.getCode().equals(lx)) {
					//log.error("Receive FKBJ_MOD");
					FzjcxxModel fzjcxxModel = JSONObject.parseObject(msg, FzjcxxModel.class);
					fzjcxxService.update(fzjcxxModel);
				}
				/*付款标记修改*/
				if(RabbitEnum.KFP_FAIL.getCode().equals(lx)) {
					//log.error("Receive KFP_FAIL");
					JcsjDto jcsjDto = new JcsjDto();
					List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DINGMESSAGETYPE.getCode());
					for (JcsjDto jcsjDto1:jcsjDtos) {
						if ("INVOICE_FAIL".equals(jcsjDto1.getCsdm())){
							jcsjDto = jcsjDto1;
							break;
						}
					}
					List<DdxxglDto> ddxxList=ddxxglService.selectByDdxxlx(jcsjDto.getCsdm());
//						String token =talkUtil.getToken();
					if(ddxxList.size()>0) {
						for (DdxxglDto ddxxglDto : ddxxList) {
							if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
								talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), xxglService.getMsg("ICOMM_KPSB001"), "发票开具失败！");
							}
						}
					}
				}
            	/*新冠新增预约 新增到患者信息表*/
            	if(RabbitEnum.HZXX_ADD.getCode().equals(lx)) {
            		//log.error("Receive HZXX_ADD");
					HzxxDto hzxxDto = JSONObject.parseObject(msg, HzxxDto.class);
					hzxxService.insertDetectionAppointmentHzxx(hzxxDto);
            	}
				/*新冠新增预约 新增到分子检测信息表*/
            	if(RabbitEnum.FZXX_ADD.getCode().equals(lx)) {
            		//log.error("Receive FZXX_ADD");
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
					fzjcxxService.insertDetectionAppointmentFzjcxx(fzjcxxDto);
            	}
				/*新冠修改预约 修改到患者信息表*/
            	if(RabbitEnum.HZXX_MOD.getCode().equals(lx)) {
            		//log.error("Receive HZXX_MOD");
					HzxxDto hzxxDto = JSONObject.parseObject(msg, HzxxDto.class);
					hzxxService.updateDetectionAppointmentHzxx(hzxxDto);
            	}
				/*新冠修改预约 修改到分子检测信息表*/
            	if(RabbitEnum.FZXX_MOD.getCode().equals(lx)) {
            		//log.error("Receive FZXX_MOD");
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
					fzjcxxService.updateDetectionAppointmentFzjcxx(fzjcxxDto);
            	}
				/*新增分子检测项目表*/
            	if(RabbitEnum.FZXM_ADD.getCode().equals(lx)) {
            		//log.error("Receive FZXM_ADD");
					FzjcxmDto fzjcxmDto = JSONObject.parseObject(msg, FzjcxmDto.class);
					fzjcxmService.insertFzjcxmDto(fzjcxmDto);
            	}
				/*新增分子检测项目表*/
            	if(RabbitEnum.FZXM_DEL.getCode().equals(lx)) {
            		//log.error("Receive FZXM_DEL");
					FzjcxmDto fzjcxmDto = JSONObject.parseObject(msg, FzjcxmDto.class);
					fzjcxmService.delFzjcxmByFzjcid(fzjcxmDto);
            	}
				//增加患者信息、新增分子检测信息、新增分子检测项目
				if(RabbitEnum.YYXX_ADD.getCode().equals(lx)) {
					//log.error("Receive YYXX_ADD");
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					HzxxDto hzxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("hzxxDto")), HzxxDto.class);
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxxDto")), FzjcxxDto.class);
					FzjcxmDto fzjcxmDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxmDto")), FzjcxmDto.class);
					hzxxService.insertDetectionAppointmentHzxx(hzxxDto);
					fzjcxxService.insertDetectionAppointmentFzjcxx(fzjcxxDto);
					fzjcxmService.insertFzjcxmDto(fzjcxmDto);
				}
				//修改患者信息、新增分子检测信息、新增分子检测项目
				if(RabbitEnum.YYXX_MOD.getCode().equals(lx)) {
					//log.error("Receive YYXX_MOD");
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					HzxxDto hzxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("hzxxDto")), HzxxDto.class);
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxxDto")), FzjcxxDto.class);
					FzjcxmDto fzjcxmDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxmDto")), FzjcxmDto.class);
					hzxxService.updateDetectionAppointmentHzxx(hzxxDto);
					fzjcxxService.insertDetectionAppointmentFzjcxx(fzjcxxDto);
					fzjcxmService.insertFzjcxmDto(fzjcxmDto);
				}
				//修改患者信息、修改分子检测信息、删除分子检测项目、新增分子检测项目
				if(RabbitEnum.YYXX_UPD.getCode().equals(lx)) {
					//log.error("Receive YYXX_UPD");
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					HzxxDto hzxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("hzxxDto")), HzxxDto.class);
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxxDto")), FzjcxxDto.class);
					FzjcxmDto fzjcxmDto_t = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxmDto_t")), FzjcxmDto.class);
					FzjcxmDto fzjcxmDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxmDto")), FzjcxmDto.class);
					hzxxService.updateDetectionAppointmentHzxx(hzxxDto);
					fzjcxxService.updateDetectionAppointmentFzjcxx(fzjcxxDto);
					fzjcxmService.delFzjcxmByFzjcid(fzjcxmDto_t);
					fzjcxmService.insertFzjcxmDto(fzjcxmDto);
				}
				//删除分子检测信息
				if(RabbitEnum.YYXX_DEL.getCode().equals(lx)) {
					//log.error("Receive YYXX_DEL");
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxxDto")), FzjcxxDto.class);
					/*去掉 取消预约 删除分子检测项目表的代码 2022-02-08*/
                    /*FzjcxmDto fzjcxmDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxmDto")), FzjcxmDto.class);
                    fzjcxxService.cancelDetectionAppointment(fzjcxxDto);
                    fzjcxmService.delFzjcxmByFzjcid(fzjcxmDto);*/
					fzjcxxService.cancelDetectionAppointment(fzjcxxDto);
				}
				//微信绑定管理修改
				if(RabbitEnum.WXBD_MOD.getCode().equals(lx)) {
					//log.error("Receive WXBD_MOD");
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					WxbdglDto wxbdglDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("wxbdglDto")), WxbdglDto.class);
					wxbdglService.updateSjhByWxid(wxbdglDto);
				}
				//微信绑定管理新增
				if(RabbitEnum.WXBD_ADD.getCode().equals(lx)) {
					//log.error("Receive WXBD_ADD");
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					WxbdglDto wxbdglDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("wxbdglDto")), WxbdglDto.class);
					wxbdglService.insertSjhAndWxid(wxbdglDto);
				}
				//生成新冠报告
				if(RabbitEnum.FZXX_REP.getCode().equals(lx)){
					//log.error("Receive FZXX_REP");
					List<FzjcxxDto> fzjcxxDtos = (List<FzjcxxDto>)JSONObject.parseObject(msg, List.class);
					if(fzjcxxDtos!=null && fzjcxxDtos.size()>0){
						for (FzjcxxDto dto : fzjcxxDtos) {
							FzjcxxDto fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(dto), FzjcxxDto.class);
							fzjcxxService.generateReport(fzjcxxDto);
						}
					}
				}
        	}
        	XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(DetMqType.APPOINTMENT_DETECTION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			log.error("异常");
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(DetMqType.APPOINTMENT_DETECTION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	/**
	 * 送检消息整合
	 */
	@SuppressWarnings("unchecked")
	@RabbitListener(queues = DetMqType.NORMAL_INSPECT_TOOA, containerFactory="defaultFactory")
	public void generalInspectionDetect(String str) {
		log.error("Receive NORMAL_INSPECT_TOOA:"+str);
        try {
        	if(StringUtil.isNotBlank(str) && str.length() > 8) {
        		String lx = str.substring(0,8);
        		String msg = str.substring(8);
				/*普检新增、修改信息*/
            	if(RabbitEnum.WEPJ_EDI.getCode().equals(lx)) {
            		log.error("Receive WEPJ_EDI");
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
					fzjcxxPJService.rabbitSaveFzjcxx(fzjcxxDto);
				}
				/*普检删除信息*/
            	if(RabbitEnum.WEPJ_DEL.getCode().equals(lx)) {
            		log.error("Receive WEPJ_DEL");
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
					fzjcxxDto.setIds(fzjcxxDto.getFzjcid());
					fzjcxxPJService.delHPVDetection(fzjcxxDto);
					FzjcxmDto fzjcxmDto = new FzjcxmDto();
					fzjcxmDto.setIds(fzjcxxDto.getIds());
					fzjcxmDto.setScry(fzjcxxDto.getScry());
					fzjcxmPJService.delFzjcxmByFzjc(fzjcxmDto);
            	}
        	}
        	XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(DetMqType.NORMAL_INSPECT_TOOA);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			log.error("异常");
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(DetMqType.NORMAL_INSPECT_TOOA);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
}
