package com.matridx.igams.sample.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DeviceTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.sample.dao.entities.SbglDto;
import com.matridx.igams.sample.dao.entities.SbglModel;
import com.matridx.igams.sample.dao.entities.SbkxglDto;
import com.matridx.igams.sample.service.svcinterface.ISbglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sample")
public class DeviceController extends BaseController{

	@Autowired
	ISbglService sbglService;
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	IJcsjService jcsjService;

	@Autowired
	RedisUtil redisUtil;

	@RequestMapping(value = "/device/pagedataAddSb")
	public ModelAndView addSb(SbglDto sbglDto){
		ModelAndView mav = new ModelAndView("sample/device/device_edit");
		sbglDto.setFormAction("add");
		mav.addObject("sbglDto", sbglDto);
		if(DeviceTypeEnum.CT.getCode().equals(sbglDto.getSblx())){
			Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE});
			mav.addObject("yblxlist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
		}
		mav.addObject("jcdwlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//存储单位
		return mav;
	}
	
	@RequestMapping(value = "/device/pagedataSaveSbadd")
	@ResponseBody
	public Map<String, Object> addSaveSb(SbglModel sbglModel){
		//获取用户信息
		User user = getLoginInfo();
		sbglModel.setLrry(user.getYhid());
		boolean isSuccess = sbglService.insert(sbglModel);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	@RequestMapping(value = "/device/pagedataModSb")
	public ModelAndView modSb(SbglDto sbglDto){
		ModelAndView mav = new ModelAndView("sample/device/device_edit");
		SbglDto t_sbglDto = sbglService.getDto(sbglDto);
		t_sbglDto.setFormAction("mod");
		mav.addObject("sbglDto", t_sbglDto);
		if(DeviceTypeEnum.CT.getCode().equals(sbglDto.getSblx())){
			Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE});
			mav.addObject("yblxlist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
		}
        mav.addObject("jcdwlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//存储单位
		return mav;
	}
	
	@RequestMapping(value = "/device/pagedataSaveSbmod")
	@ResponseBody
	public Map<String, Object> modSaveSb(SbglModel sbglModel){
		//获取用户信息
		User user = getLoginInfo();
		sbglModel.setXgry(user.getYhid());
		boolean isSuccess = sbglService.update(sbglModel);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	@RequestMapping(value = "/device/pagedataDelSb")
	@ResponseBody
	public Map<String, Object> delSb(SbglModel sbglModel){
		//获取用户信息
		User user = getLoginInfo();
		sbglModel.setScry(user.getYhid());
		boolean isSuccess = sbglService.delete(sbglModel);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	@RequestMapping(value = "/device/pageListDeviceTree")
	@ResponseBody
	public ModelAndView pageListDeviceTree(){
        //List<DeviceDto> deviceDtos = bxglService.getDeviceList();
		//mav.addObject("deviceDtos", deviceDtos);
		return new ModelAndView("sample/device/device_list");
	}
	
	@RequestMapping(value = "/device/pageGetListDeviceTree")
	@ResponseBody
	public List<SbglDto> pageGetListAllDeviceTree(){
		SbglDto sbglDto = new SbglDto();
		return sbglService.getAllDeviceList(sbglDto);
	}
	
	@RequestMapping(value ="/device/pagedataAnsyGetDeviceList")
	@ResponseBody
	public List<SbglDto> ansyGetDeviceTree(SbglDto sbglDto){
		return sbglService.getDeviceList(sbglDto);
	}
	
	@RequestMapping(value ="/device/pagedataAnsyGetRecommendPos")
	@ResponseBody
	public Map<String, Object> pagedataAnsyGetRecommendPos(SbkxglDto sbkxglDto){
		return sbglService.getRecommendInfo(sbkxglDto);
	}
	
	@RequestMapping(value ="/device/pagedataAnsyGetPosition")
	@ResponseBody
	public Map<String,Object> ansyGetPosition(SbglDto sbglDto){
		return sbglService.getHzPosition(sbglDto);
	}

	/*
	* 通过父设备id获取子设备
	* */
	@RequestMapping(value ="/device/pagedataSbListByFsbid")
	@ResponseBody
	public List<SbglDto> pagedataSbListByFsbid(SbglDto sbglDto){
		return sbglService.getSbListByFsbid(sbglDto);
	}
	/*
	 * 通过存储单位获取冰箱
	 * */
	@RequestMapping(value ="/device/pagedataSbListByJcdw")
	@ResponseBody
	public List<SbglDto> pagedataSbListByJcdw(SbglDto sbglDto){
		return sbglService.getSbListByJcdw(sbglDto);
	}

}
