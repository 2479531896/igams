package com.matridx.igams.hrm.controller;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGzglService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.hrm.dao.entities.YghmcDto;
import com.matridx.igams.hrm.service.svcinterface.ITgxxService;
import com.matridx.igams.hrm.service.svcinterface.IYghmcService;
import com.matridx.igams.hrm.service.svcinterface.IYhjqService;
import com.matridx.igams.hrm.service.svcinterface.IYhkqxxService;
import com.matridx.igams.hrm.service.svcinterface.IZwjsxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/ws")
public class HrmWSControl extends BaseController {
	@Autowired
	IFjcfbService fjcfbService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	IYghmcService yghmcService;
	@Autowired
	IZwjsxxService zwjsxxService;
	@Autowired
	ITgxxService tgxxService;
	@Autowired
	IYhkqxxService yhkqxxService;
	@Autowired
	IYhjqService yhjqService;
	@Autowired
	DingTalkUtil dingTalkUtil;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IGzglService gzglService;
	private final Logger log = LoggerFactory.getLogger(HrmWSControl.class);

	/**
	 * 合同到期提醒员工信息查看
	 */
	@RequestMapping("/roster/viewDqRoster")
	public ModelAndView viewDqRoster() {
		YghmcDto yghmcDto = new YghmcDto();
		yghmcDto.setDqtx("1");
		yghmcDto.setRqc("45");
		List<YghmcDto> dtoList = yghmcService.getDtoList(yghmcDto);
		ModelAndView mav = new ModelAndView("train/roster/roster_dqlist");
		mav.addObject("yghmcDtos", dtoList);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	/**
	 * 钉钉职位晋升审核回调
	 */
	@RequestMapping("/hrm/aduitPostPromotionCallback")
	@ResponseBody
	public Map<String,Object> aduitPostPromotionCallback(HttpServletRequest request) {
		String processInstanceId = request.getParameter("processInstanceId");
		String processCode = request.getParameter("processCode");
		String wbcxid = request.getParameter("wbcxid");
		boolean result;
		Map<String,Object> map=new HashMap<>();
		try {
			result = zwjsxxService.aduitPostPromotionCallback(processInstanceId,processCode,wbcxid);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}
	/**
	 * 钉钉调岗审核回调
	 */
	@RequestMapping("/hrm/aduitJobAdjustmentCallback")
	@ResponseBody
	public Map<String,Object> aduitJobAdjustmentCallback(HttpServletRequest request) {
		String processInstanceId = request.getParameter("processInstanceId");
		String processCode = request.getParameter("processCode");
		String wbcxid = request.getParameter("wbcxid");
		boolean result;
		Map<String,Object> map= new HashMap<>();
		try {
			result = tgxxService.aduitJobAdjustmentCallback(processInstanceId,processCode,wbcxid);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}
	/**
	 * 传递参数,更新请假信息
	 */
	@RequestMapping(value="/web/updateLeaveNews")
	@ResponseBody
	public Map<String,Object> updateLeaveNews(HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult approveInfo = dingTalkUtil.getApproveInfo(request.getParameter("wbcxid"), request.getParameter("processInstanceId"));
		String result = approveInfo.getResult();
		String status=approveInfo.getStatus();
		//原钉钉实例id   只有销假的才有此id
		String mainProcessInstanceId=approveInfo.getMainProcessInstanceId();
		boolean isSuccess=false;
		if (StringUtil.isBlank(mainProcessInstanceId)&&"agree".equals(result)){
			isSuccess=yhkqxxService.updateCallBackAskForLeave(approveInfo,request);
			if (isSuccess){
				isSuccess=yhjqService.updateUserHolidaysStatus(request);
			}
		}else if (StringUtil.isBlank(mainProcessInstanceId)&&"RUNNING".equals(status)){
			isSuccess=yhjqService.updateCallBackAskForLeave(approveInfo,request);
		}else if ((StringUtil.isBlank(mainProcessInstanceId))&&(("".equals(result)&&"TERMINATED".equals(status))||"refuse".equals(result))
		){
			isSuccess=yhjqService.rollBackHolidays(request);
		}else if (StringUtil.isNotBlank(mainProcessInstanceId)&&"agree".equals(result)){
			isSuccess=yhjqService.reportBackHolidays(mainProcessInstanceId);
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	@RequestMapping("/task/minidataDingTrainNext")
	@ResponseBody
	public Map<String,Object> minidataDingTrainNext(GzglDto gzglDto){
		if (StringUtil.isNotBlank(gzglDto.getGqsjbj())){
			gzglDto.setGqsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
		}
		Map<String,Object> result = new HashMap<>();
		result.put("flag","0");
		result.put("flagNext","0");
		GzglDto gzglDtoS = new GzglDto();
		gzglDtoS.setDdid(gzglDto.getDdid());
		gzglDtoS.setZlbdm(gzglDto.getZlbdm());
		gzglDtoS.setGqsj(gzglDto.getGqsj());
		gzglDtoS.setGqsjbj(gzglDto.getGqsjbj());
		gzglDtoS.setListFlag("1");
		if("0".equals(gzglDto.getListFlag())){
			String xh = (Integer.parseInt(gzglDto.getXh())-1)+"";
			gzglDtoS.setXh(xh);
		}else {
			gzglDtoS.setXh(gzglDto.getXh());
		}
		String countNext = gzglService.getStringTrainFjid(gzglDtoS);
		gzglDtoS.setListFlag("0");
		if("0".equals(gzglDto.getListFlag())){
			gzglDtoS.setXh(gzglDto.getXh());
		}else {
			String xh = (Integer.parseInt(gzglDto.getXh())+1)+"";
			gzglDtoS.setXh(xh);
		}
		String countOn = gzglService.getStringTrainFjid(gzglDtoS);
		if("0".equals(gzglDto.getListFlag()) && StringUtil.isNotBlank(countOn)){
			if(Integer.valueOf(countOn)>0){
				GzglDto gzglDtoT = gzglService.getDtoTrainFjid(gzglDto);
				String pre_fileid = urlPrefix+"/common/file/getFileInfoFlg?fjid=" + gzglDtoT.getFjid() + "&access_token=" + gzglDto.getAccess_token();
				result.put("pre_fileid", pre_fileid);
				result.put("fjid", gzglDtoT.getFjid());
				result.put("xh", gzglDtoT.getXh());
			}
		}
		if("1".equals(gzglDto.getListFlag()) && StringUtil.isNotBlank(countNext)){
			if(Integer.valueOf(countNext)>0){
				GzglDto gzglDtoT = gzglService.getDtoTrainFjid(gzglDto);
				String pre_fileid = urlPrefix+"/common/file/getFileInfoFlg?fjid=" + gzglDtoT.getFjid() + "&access_token=" + gzglDto.getAccess_token();
				result.put("pre_fileid", pre_fileid);
				result.put("fjid", gzglDtoT.getFjid());
				result.put("xh", gzglDtoT.getXh());
			}
		}
		if(Integer.valueOf(countOn)<1){
			result.put("flag","1");
		}
		if(Integer.valueOf(countNext)<1){
			result.put("flagNext","1");
		}
		return result;
	}
}