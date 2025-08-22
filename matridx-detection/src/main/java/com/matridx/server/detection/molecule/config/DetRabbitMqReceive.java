package com.matridx.server.detection.molecule.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.matridx.igams.common.enums.BusinessTypeEnum;
import com.matridx.server.detection.molecule.dao.entities.*;
import com.matridx.server.detection.molecule.service.svcinterface.IBkyyrqService;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxmPJService;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxxPJService;
import com.matridx.server.wechat.dao.entities.HzxxDto;
import com.matridx.server.wechat.dao.entities.PayinfoDto;
import com.matridx.server.wechat.service.svcinterface.IBankService;
import com.matridx.server.wechat.service.svcinterface.IPayinfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.XxdlcwglDto;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.service.svcinterface.IXxdlcwglService;
import com.matridx.server.detection.molecule.dao.post.IFzjcxmDao;
import com.matridx.server.detection.molecule.dao.post.IFzjcxxDao;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcjgService;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxmService;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxxService;
import com.matridx.server.detection.molecule.service.svcinterface.IHzxxtService;
import com.matridx.server.detection.molecule.util.DetMqType;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Component
public class DetRabbitMqReceive {
	private Logger log = LoggerFactory.getLogger(DetRabbitMqReceive.class);
	@Autowired
	IBankService bankService;
	@Autowired
	IPayinfoService payinfoService;
	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	IXxdlcwglService xxdlcwglService;
	
	@Autowired
	IHzxxtService hzxxService;
	@Autowired
	IFzjcxmService fzjcxmService;
	@Autowired
	IFzjcxxService fzjcxxService;
	@Autowired
	IFzjcjgService fzjcjgService;
	@Autowired
	IBkyyrqService bkyyrqService;
	@Autowired
	IFzjcxmDao fzjcxmDao;
	@Autowired
	IFzjcxxDao fzjcxxDao;
	@Autowired
	IFzjcxxPJService fzjcxxPJService;
	@Autowired
	IFzjcxmPJService fzjcxmPJService;
	
	// 送检消息整合
	@RabbitListener(queues = DetMqType.APPOINTMENT_DETECT, containerFactory="defaultFactory")
	public void receiveInspection(String str) {
		log.error("85端接收同步:"+str);
        try {
        	if(StringUtil.isNotBlank(str) && str.length() > 8) {
        		String lx = str.substring(0,8);
        		String msg = str.substring(8);
        		if(RabbitEnum.HZXX_ADD.getCode().equals(lx)) {
        			log.error("receive HZXX_ADD");
        			HzxxtDto hzxxDto = JSONObject.parseObject(msg, HzxxtDto.class);
            		hzxxService.insertDto(hzxxDto);
            	}else if(RabbitEnum.FKBJ_MOD.getCode().equals(lx)) {
					/*付款标记修改*/
					log.error("Receive FKBJ_MOD");
					FzjcxxModel fzjcxxModel = JSONObject.parseObject(msg, FzjcxxModel.class);
					fzjcxxService.update(fzjcxxModel);
					// 关闭其它订单
					PayinfoDto payinfoDto = new PayinfoDto();
					payinfoDto.setYwid(fzjcxxModel.getFzjcid());
					payinfoDto.setYwlx(BusinessTypeEnum.XG.getCode());
					List<PayinfoDto> list = payinfoService.selectPayOrders(payinfoDto);
					for (int i = 0; i < list.size(); i++) {
						bankService.closeOrder(restTemplate, list.get(i));
					}
				}else if (RabbitEnum.XGFK_MOD.getCode().equals(lx)){
					//检测列表修改功能
					log.error("Receive XGFK_MOD");
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					List<FzjcxxDto> list = JSONArray.parseArray(JSON.toJSONString(rabbitMap.get("fzjcxxList")), FzjcxxDto.class);
					if(list!=null&&list.size()>0){
						fzjcxxService.updateList(list);
					}
				}else if(RabbitEnum.HZXX_MOD.getCode().equals(lx)) {
            		log.error("receive HZXX_MOD");
            		HzxxtDto hzxxDto = JSONObject.parseObject(msg, HzxxtDto.class);
            		hzxxService.updateDto(hzxxDto);
				}else if(RabbitEnum.FZXX_ADD.getCode().equals(lx)) {
					log.error("receive FZXX_ADD");
            		FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
            		fzjcxxDao.insertDto(fzjcxxDto);
				}else if(RabbitEnum.FZXX_MOD.getCode().equals(lx)) {
					log.error("receive FZXX_MOD");
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
					if(fzjcxxDto.getFzjcid()!=null){
						fzjcxxDao.updateConfirm(fzjcxxDto);
					}else{
						fzjcxxDao.updateBatchConfirm(fzjcxxDto);
					}
				}else if(RabbitEnum.FZXX_DEL.getCode().equals(lx)) {
					log.error("receive FZXX_DEL");
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
					fzjcxxDao.delCovidDetails(fzjcxxDto);
				}else if(RabbitEnum.FZXX_DIS.getCode().equals(lx)) {
					log.error("receive FZXX_DIS");
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
					fzjcxxDao.discardCovidDetails(fzjcxxDto);
				}else if(RabbitEnum.FZXX_CON.getCode().equals(lx)) {
					log.error("receive FZXX_CON");
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
					fzjcxxDao.updateConfirm(fzjcxxDto);
				}else if(RabbitEnum.FZXX_HQR.getCode().equals(lx)) {
					log.error("receive FZXX_HQR");
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
					fzjcxxDao.UpdateSfqrByYbbh(fzjcxxDto);
				}else if(RabbitEnum.FZXM_ADD.getCode().equals(lx)) {
					log.error("receive FZXM_ADD");
            		FzjcxmDto fzjcxmDto = JSONObject.parseObject(msg, FzjcxmDto.class);
            		fzjcxmDao.insertDto(fzjcxmDto);
				}else if(RabbitEnum.FZXM_MOD.getCode().equals(lx)) {
					log.error("receive FZXM_MOD");
					FzjcxmDto fzjcxmDto = JSONObject.parseObject(msg, FzjcxmDto.class);
					fzjcxmDao.updateDto(fzjcxmDto);
				}else if(RabbitEnum.FZXM_DEL.getCode().equals(lx)) {
					log.error("receive FZXM_DEL");
					FzjcxxDto fzjcxxDto  = JSONObject.parseObject(msg, FzjcxxDto.class);
					fzjcxmDao.deleteFzjcxmByFzjcid(fzjcxxDto.getFzjcid());
				}else if(RabbitEnum.FZJG_MOD.getCode().equals(lx)) {
					log.error("receive FZJG_MOD");
            		FzjcjgDto fzjcjgDto = JSONObject.parseObject(msg, FzjcjgDto.class);
            		List<FzjcjgDto> jglist=fzjcjgDto.getJglist();
            		//先删除再新增
            		fzjcjgService.delDtoListByIds(fzjcjgDto);
					fzjcjgService.addDtoList(jglist);
					//向igams_fzjcxx表中添加结果名称
					List<FzjcjgDto> fzjcjgDtos=fzjcjgService.getDtosList(fzjcjgDto.getIds());
					FzjcxxDto fzjcxxDto=new FzjcxxDto();
					fzjcxxDto.setIds(fzjcjgDto.getIds());
					if(fzjcjgDtos!=null && fzjcjgDtos.size()>0){
						fzjcxxDto.setJcjgmc(fzjcjgDtos.get(0).getJcjgmc());
						//更新分子检测表，检测结果字段
						fzjcxxService.updateJcjg(fzjcxxDto);
					}
				}else if(RabbitEnum.FZJC_EDI.getCode().equals(lx)) {
					log.error("receive FZJC_EDI");
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
					fzjcxxDao.saveFzjcxxInfo(fzjcxxDto);
				}else if(RabbitEnum.HZXX_UPD.getCode().equals(lx)) {
					log.error("receive HZXX_UPD");
					HzxxtDto hzxxDto = JSONObject.parseObject(msg, HzxxtDto.class);
					hzxxService.updateDto(hzxxDto);

//					log.error("receive HZXX_UPD");
//					HzxxtDto hzxxDto = JSONObject.parseObject(msg, HzxxtDto.class);
//					hzxxService.updateCovidPatient(hzxxDto);
				}else if (RabbitEnum.SFLR_SUB.getCode().equals(lx)){
					//身份确认页面保存
					log.error("Receive SFLR_SUB");
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					FzjcxxDto fzjcxxDto = new FzjcxxDto();
                    //更新患者信息的是否确认标记
					HzxxtDto hzxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("hzxxDto")), HzxxtDto.class);
					hzxxService.updateSfqr(hzxxDto);
					//判断分子检测信息是新增还是更改
					if (rabbitMap.containsKey("fzjcxxDto_add")){//新增
						fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxxDto_add")), FzjcxxDto.class);
						fzjcxxDao.insertDto(fzjcxxDto);
					}else {//修改
						fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxxDto_mod")), FzjcxxDto.class);
						fzjcxxDao.updateConfirm(fzjcxxDto);
					}
					//删除关联一条分子检测信息的所有检测项目
					fzjcxmDao.deleteFzjcxmByFzjcid(fzjcxxDto.getFzjcid());
					//重新添加检测项目
					String[] fzxmlist = ((JSONArray)rabbitMap.get("fzxmlist")).toArray(new String[]{});
//					List<FzjcxmDto>fzjcxmDtoList = (List<FzjcxmDto>) rabbitMap.get("fzjcxmDtoList");
					List<FzjcxmDto> fzjcxmDtoList = JSONArray.parseArray(JSON.toJSONString(rabbitMap.get("fzjcxmDtoList")), FzjcxmDto.class);
					for (FzjcxmDto fzxmDto: fzjcxmDtoList) {
						fzjcxmDao.insertDto(fzxmDto);
					}
				}else if (RabbitEnum.YYJC_MOD.getCode().equals(lx)){
					//检测列表修改功能
					log.error("Receive YYJC_MOD");
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					FzjcxxDto fzjcxxDto = new FzjcxxDto();
					fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxxDto")), FzjcxxDto.class);
					fzjcxxDao.saveEditCovid(fzjcxxDto);
					//删除关联一条分子检测信息的所有检测项目
					fzjcxmDao.deleteFzjcxmByFzjcid(fzjcxxDto.getFzjcid());
					//重新添加检测项目
					List<FzjcxmDto> fzjcxmDtoList = JSONArray.parseArray(JSON.toJSONString(rabbitMap.get("fzjcxmDtoList")), FzjcxmDto.class);
					for (FzjcxmDto fzxmDto: fzjcxmDtoList) {
						fzjcxmDao.insertDto(fzxmDto);
					}
				}else if(RabbitEnum.YYXX_ADD.getCode().equals(lx)) {
					log.error("Receive YYXX_ADD");
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					if ("yes".equals(rabbitMap.get("isHzxxAdd"))){
						HzxxtDto hzxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("hzxxDto")), HzxxtDto.class);
						FzjcxxDto fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxxDto")), FzjcxxDto.class);
						FzjcxmDto fzjcxmDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxmDto")), FzjcxmDto.class);
						hzxxService.insertDto(hzxxDto);
						fzjcxxService.insertDto(fzjcxxDto);
						fzjcxmService.insertDto(fzjcxmDto);
					}else {
						FzjcxxDto fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxxDto")), FzjcxxDto.class);
						FzjcxmDto fzjcxmDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxmDto")), FzjcxmDto.class);
						fzjcxxService.insertDto(fzjcxxDto);
						fzjcxmService.insertDto(fzjcxmDto);
					}
               }else if(RabbitEnum.YYXX_MOD.getCode().equals(lx)) {
					log.error("Receive YYXX_MOD");
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					HzxxtDto hzxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("hzxxDto")), HzxxtDto.class);
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxxDto")), FzjcxxDto.class);
					FzjcxmDto fzjcxmDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxmDto")), FzjcxmDto.class);
					hzxxService.updateDetectionAppointmentHzxx(hzxxDto);
					fzjcxxService.insertDetectionAppointmentFzjcxx(fzjcxxDto);
					fzjcxmService.insertFzjcxmDto(fzjcxmDto);
				}else if(RabbitEnum.YYXX_UPD.getCode().equals(lx)) {
					log.error("Receive YYXX_UPD");//只修改患者信息地址以及预约检测日期，不必修改检测项目
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					HzxxtDto hzxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("hzxxDto")), HzxxtDto.class);
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxxDto")), FzjcxxDto.class);
					hzxxService.updateDetectionAppointmentHzxx(hzxxDto);
					fzjcxxService.updateDetectionAppointmentFzjcxx(fzjcxxDto);
				} else if(RabbitEnum.YYXX_DEL.getCode().equals(lx)) {
					log.error("Receive YYXX_DEL");//删除分子检测信息、删除分子检测项目
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxxDto")), FzjcxxDto.class);
					/*去掉 取消预约、修改预约 删除分子检测项目表的代码 2022-02-08*/
					/*FzjcxmDto fzjcxmDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxmDto")), FzjcxmDto.class);
					fzjcxxService.cancelDetectionAppointment(fzjcxxDto);
					fzjcxmService.delFzjcxmByFzjcid(fzjcxmDto);*/
					fzjcxxService.cancelDetectionAppointment(fzjcxxDto);
				}else if (RabbitEnum.YYRQ_ADD.getCode().equals(lx)){
					log.error("Receive YYRQ_ADD");
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
//					List<BkyyrqDto> bkyyrqDtos = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("bkyyrqDtos")),List<BkyyrqDto>.class);
					List<BkyyrqDto> bkyyrqDtos = JSONArray.parseArray(JSON.toJSONString(rabbitMap.get("bkyyrqDtos")),BkyyrqDto.class);
//					BkyyrqDto bkyyrqDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("bkyyrqDto")), BkyyrqDto.class);
					Map<String, Object> map = bkyyrqService.insertBkyyrqDtoList(bkyyrqDtos);
					log.error(JSON.toJSONString(map));
				}else if (RabbitEnum.YYRQ_MOD.getCode().equals(lx)){
					log.error("Receive YYRQ_MOD");
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					BkyyrqDto bkyyrqDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("bkyyrqDto")), BkyyrqDto.class);
					Map<String, Object> map = bkyyrqService.updateBkyyrqDto(bkyyrqDto);
					log.error(JSON.toJSONString(map));
				}else if (RabbitEnum.YYRQ_DEL.getCode().equals(lx)){
					log.error("Receive YYRQ_DEL");
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					BkyyrqDto bkyyrqDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("bkyyrqDto")), BkyyrqDto.class);
					boolean isSuccess = bkyyrqService.delUnAppDateDetails(bkyyrqDto);
					log.error(isSuccess?"success！":"fail！");
				}else if(RabbitEnum.AHYY_UPD.getCode().equals(lx)) {
					log.error("Receive AHYY_UPD");//阿里 调用接口 预约修改
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxxDto")), FzjcxxDto.class);
					/*去掉 阿里平台 取消预约、修改预约 删除分子检测项目表的代码 2022-02-08*/
					/*FzjcxmDto fzjcxmDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxmDto")), FzjcxmDto.class);*/
					fzjcxxService.delCovidDetails(fzjcxxDto);
					/*去掉 阿里平台 取消预约、修改预约 删除分子检测项目表的代码 2022-02-08*/
					/*fzjcxmService.delFzjcxmByFzjcid(fzjcxmDto);*/
				}else if(RabbitEnum.GHYY_UPD.getCode().equals(lx)) {
					log.error("Receive GHYY_UPD");//橄榄枝 调用接口 预约修改
					Map<String, Object> rabbitMap = JSONObject.parseObject(msg, Map.class);
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(JSON.toJSONString(rabbitMap.get("fzjcxxDto")), FzjcxxDto.class);
					fzjcxxService.updateAppDate(fzjcxxDto);
				}else if(RabbitEnum.ALDD_UPD.getCode().equals(lx)) {
					log.error("Receive ALDD_UPD");//橄榄枝 调用接口 预约修改
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
					fzjcxxService.updateAliOrder(fzjcxxDto);
				}else if(RabbitEnum.FZXX_BAC.getCode().equals(lx)){//钉钉小程序混检扫码页面删除操作数据回滚
					log.error("Receive FZXX_BAC");
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
					//回滚分子检测信息
					fzjcxxService.callbackFzjcxx(fzjcxxDto);
					//回滚客户是否确认信息
					HzxxtDto hzxxDto=new HzxxtDto();
					hzxxDto.setHzid(fzjcxxDto.getHzid());
					hzxxDto.setSfqr("0");
					hzxxService.updateDto(hzxxDto);
				}else if(RabbitEnum.BGWC_UPD.getCode().equals(lx)){
					log.error("Receive BGWC_UPD");
					FzjcxxDto fzjcxxDto = JSONObject.parseObject(msg, FzjcxxDto.class);
					//更新报告完成时间
					fzjcxxService.updateBgwcsj(fzjcxxDto);
				}
        	}
        	XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(DetMqType.APPOINTMENT_DETECT);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			log.error("============receive 异常"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(DetMqType.APPOINTMENT_DETECT);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	/*
    	普检信息同步至85
 	*/
	@RabbitListener(queues = DetMqType.NORMAL_INSPECT_TOWECHAT, containerFactory="defaultFactory")
	public void receiveNormalInspectionToWechat(String str) {
		log.error("Receive NORMAL_INSPECT_TOWECHAT:"+str);
		try {
			if(StringUtil.isNotBlank(str) && str.length() > 8) {
				String lx = str.substring(0,8);
				String msg = str.substring(8);
				if(RabbitEnum.PJXX_ADD.getCode().equals(lx)) {
					log.debug("Receive PJXX_ADD");
					FzjcxxDto fzjcxxDto = JSON.parseObject(msg, FzjcxxDto.class);
					fzjcxxPJService.addOrModSaveToWechat(fzjcxxDto);
				}else if (RabbitEnum.PJXX_MOD.getCode().equals(lx)){
					log.debug("Receive PJXX_MOD");
					FzjcxxDto fzjcxxDto = JSON.parseObject(msg, FzjcxxDto.class);
					fzjcxxPJService.addOrModSaveToWechat(fzjcxxDto);
				}else if (RabbitEnum.PJXX_DEL.getCode().equals(lx)){
					log.debug("Receive PJXX_DEL");
					FzjcxxDto fzjcxxDto = JSON.parseObject(msg, FzjcxxDto.class);
					fzjcxxPJService.delPJDetection(fzjcxxDto);
				}else if (RabbitEnum.PJXX_DET.getCode().equals(lx)){
					log.debug("Receive PJXX_DET");
					FzjcxxDto fzjcxxDto = JSON.parseObject(msg, FzjcxxDto.class);
					fzjcxxPJService.updateSyzt(fzjcxxDto);
				}else if (RabbitEnum.PJXX_ACT.getCode().equals(lx)){
					log.debug("Receive PJXX_ACT");
					FzjcxxDto fzjcxxDto = JSON.parseObject(msg, FzjcxxDto.class);
					fzjcxxPJService.acceptSaveSamplePJ(fzjcxxDto);
				}else if (RabbitEnum.PJXX_RUM.getCode().equals(lx)){
					log.debug("Receive PJXX_RUM");
					FzjcjgDto fzjcjgDto = JSON.parseObject(msg, FzjcjgDto.class);
					fzjcxxPJService.resultModSavePJ(fzjcjgDto);
				}else if (RabbitEnum.PJXX_AUT.getCode().equals(lx)){
					log.debug("Receive PJXX_AUT");
					FzjcxmDto fzjcxmDto = JSON.parseObject(msg, FzjcxmDto.class);
					fzjcxmPJService.updateZt(fzjcxmDto);
				}else if (RabbitEnum.PJXX_AUB.getCode().equals(lx)){
					log.debug("Receive PJXX_AUT");
					FzjcxxDto fzjcxxDto = JSON.parseObject(msg, FzjcxxDto.class);
					fzjcxxPJService.updatePjBgrqAndBgwcs(fzjcxxDto);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(DetMqType.NORMAL_INSPECT_TOWECHAT);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
}
