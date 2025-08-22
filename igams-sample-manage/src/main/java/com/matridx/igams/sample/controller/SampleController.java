package com.matridx.igams.sample.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DeviceTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.sample.dao.entities.SbglDto;
import com.matridx.igams.sample.dao.entities.YbglDto;
import com.matridx.igams.sample.dao.entities.YblyDto;
import com.matridx.igams.sample.service.svcinterface.ISbglService;
import com.matridx.igams.sample.service.svcinterface.IYbglService;
import com.matridx.igams.sample.service.svcinterface.IYblyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sample")
public class SampleController extends BaseController{
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	IJcsjService jcsjService;
	
	@Autowired
	IYblyService yblyService;
	
	@Autowired
	ISbglService sbglService;
	
	@Autowired
	IYbglService ybglService;
	
	/**
	 * 标本列表页面
	 */
	@RequestMapping(value ="/stock/pageListSamp")
	public ModelAndView pageListSamp(){
		ModelAndView mav = new ModelAndView("sample/stock/samp_list");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.SAMPLE_TYPE});
		mav.addObject("yblxlist", jclist.get(BasicDataTypeEnum.SAMPLE_TYPE.getCode()));
		List<JcsjDto> lyflist = new ArrayList<>();
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setCsid("0");
		jcsjDto.setCsmc("外部");
		lyflist.add(jcsjDto);
		JcsjDto jcsjDto2 = new JcsjDto();
		jcsjDto2.setCsid("1");
		jcsjDto2.setCsmc("内部");
		lyflist.add(jcsjDto2);
		mav.addObject("lyflist",lyflist);
		
		YbglDto t_ybglDto = new YbglDto();
		t_ybglDto.setAuditType(AuditTypeEnum.AUDIT_SAMPAPPLY.getCode());
		mav.addObject("ybglDto",t_ybglDto);
		return mav;
	}
	
	/**
	 * 标本列表
	 */
	@RequestMapping(value ="/stock/pageGetListSamp")
	@ResponseBody
	public Map<String,Object> pageGetListSamp(YbglDto ybglDto){
		List<YbglDto> t_List = ybglService.getPagedDtoList(ybglDto);
		
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", ybglDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 标本统计数据，根据年月和标本类型获取 相应时间段的使用情况（只针对一个统计）
	 */
	@RequestMapping(value ="/stock/pagedataAnsyStatisSampUse")
	@ResponseBody
	public Map<String,Object> pagedataAnsyStatisSampUse(YbglDto ybglDto){
		
		//构造成Json的格式传递

        return ybglService.getStatisSampUse(ybglDto);
	}
	
	/**
	 * 标本统计数据（针对整个统计页面）
	 */
	@RequestMapping(value ="/stock/pagedataSjxxStatisByTj")
	@ResponseBody
	public Map<String,Object> pagedataSjxxStatisByTj(YbglDto ybglDto){
		
		//构造成Json的格式传递

        return ybglService.getStatisSample(ybglDto);
	}

	/**
	 * 标本查看
	 */
	@RequestMapping(value="/stock/viewSamp")
	public ModelAndView viewSamp(YbglDto ybglDto){
		ModelAndView mav = new ModelAndView("sample/stock/samp_view");
		YbglDto t_ybglDto = ybglService.getDtoById(ybglDto.getYbid());
		mav.addObject("ybglDto", t_ybglDto);
		
		return mav;
	}
	
	/**
	 * 标本新增
	 */
	@RequestMapping(value ="/stock/dealSamp")
	public ModelAndView dealSamp(YbglDto ybglDto){
		ModelAndView mav = new ModelAndView("sample/stock/samp_add");
		YblyDto t_yblyDto = yblyService.getDtoById(ybglDto.getLyid());
		ybglDto.setFormAction("add");
		ybglDto.setLybh(t_yblyDto.getLybh());
		ybglDto.setLyid(t_yblyDto.getLyid());
		ybglDto.setYblxmc(t_yblyDto.getYblxmc());
		ybglDto.setYblx(t_yblyDto.getYblx());
		mav.addObject("ybglDto", ybglDto);
		
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SAMPLE_TYPE});

		mav.addObject("yblxlist", jclist.get(BasicDataTypeEnum.SAMPLE_TYPE.getCode()));
		
		SbglDto sbglDto = new SbglDto();
		sbglDto.setSblx(DeviceTypeEnum.BX.getCode());
		List<SbglDto> bxList = sbglService.getDeviceList(sbglDto);
		mav.addObject("bxlist", bxList);
		
		return mav;
	}
	
	/**
	 * 标本新增保存
	 */
	@RequestMapping(value = "/stock/dealSaveSamp")
	@ResponseBody
	public Map<String, Object> dealSaveSamp(YbglDto ybglDto){
		//获取用户信息
		User user = getLoginInfo();
		ybglDto.setLrry(user.getYhid());
		List<String> ybbhs= ybglService.insertYbDto(ybglDto);
		
		boolean isSuccess = false;
		StringBuilder msg = new StringBuilder();
		if(ybbhs !=null && !ybbhs.isEmpty()){
			isSuccess = true;
            for (String ybbh : ybbhs) {
                msg.append("<br/>标本编号：<span style='color:red'>");
                msg.append(ybbh);
                msg.append("</span>");
            }
		}
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr() + msg):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 标本修改
	 */
	@RequestMapping(value = "/stock/modSamp")
	public ModelAndView modSamp(YbglDto ybglDto){
		ModelAndView mav = new ModelAndView("sample/stock/samp_edit");
		YbglDto t_ybglDto = ybglService.getDto(ybglDto);
		t_ybglDto.setFormAction("mod");
		mav.addObject("ybglDto", t_ybglDto);
		
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SAMPLE_TYPE});

		mav.addObject("yblxlist", jclist.get(BasicDataTypeEnum.SAMPLE_TYPE.getCode()));
		//冰箱列表
		SbglDto sbglDto = new SbglDto();
		sbglDto.setSblx(DeviceTypeEnum.BX.getCode());
		List<SbglDto> bxList = sbglService.getDeviceList(sbglDto);
		mav.addObject("bxlist", bxList);
		//抽屉列表
		sbglDto.setSblx(DeviceTypeEnum.CT.getCode());
		sbglDto.setFsbid(t_ybglDto.getBxid());
		List<SbglDto> ctList = sbglService.getDeviceList(sbglDto);
		mav.addObject("ctlist", ctList);
		//盒子列表
		sbglDto.setSblx(DeviceTypeEnum.HZ.getCode());
		sbglDto.setFsbid(t_ybglDto.getChtid());
		List<SbglDto> hzList = sbglService.getDeviceList(sbglDto);
		mav.addObject("hzlist", hzList);
		//位置信息
		mav.addObject("wzlist", sbglService.getPositionList(t_ybglDto));
		
		return mav;
	}
	
	/**
	 * 标本修改保存
	 */
	@RequestMapping(value = "/stock/modSaveSamp")
	@ResponseBody
	public Map<String, Object> modSaveSamp(YbglDto ybglDto){
		//获取用户信息
		User user = getLoginInfo();
		ybglDto.setXgry(user.getYhid());
		boolean isSuccess = ybglService.update(ybglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 标本删除
	 */
	@RequestMapping(value = "/stock/delSamp")
	@ResponseBody
	public Map<String, Object> delSamp(YbglDto ybglDto){
		//获取用户信息
		User user = getLoginInfo();
		ybglDto.setScry(user.getYhid());
		boolean isSuccess = ybglService.delete(ybglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 标本导入页面
	 */
	@RequestMapping(value ="/stock/pageImportSamp")
	public ModelAndView pageImportSamp(){
		ModelAndView mav = new ModelAndView("sample/stock/samp_import");
		
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SAMPLE.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		
		return mav;
	}
	
	/**
	 * 标本统计页面
	 */
	@RequestMapping(value ="/stock/pageListStatis")
	public ModelAndView pageListStatis(){
		
		return ybglService.getStatisPage();
	}
}
