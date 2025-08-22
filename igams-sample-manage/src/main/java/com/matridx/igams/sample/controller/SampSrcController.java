package com.matridx.igams.sample.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DeviceTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.sample.dao.entities.*;
import com.matridx.igams.sample.service.svcinterface.ISbglService;
import com.matridx.igams.sample.service.svcinterface.IYbglService;
import com.matridx.igams.sample.service.svcinterface.IYblyService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/sample")
public class SampSrcController extends BaseController {
	
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
	 * 标本来源列表
	 */
	@RequestMapping(value ="/stock/pageListSampSrc")
	public ModelAndView pageListSampleSource(){
		ModelAndView mav = new ModelAndView("sample/stock/sampsrc_list");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.UNIT_SOURCE});
		mav.addObject("dwlist", jclist.get(BasicDataTypeEnum.UNIT_SOURCE.getCode()));
		return mav;
	}
	
	/**
	 * 标本来源列表明细
	 */
	@RequestMapping(value ="/stock/pageGetListSampSrc")
	@ResponseBody
	public Map<String,Object> listSampleSource(YblyDto yblyDto){
		List<YblyDto> t_List = yblyService.getPagedDtoList(yblyDto);
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", yblyDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 未处理标本来源列表
	 */
	@RequestMapping(value ="/stock/pageListUntreatedSampSrc")
	public ModelAndView pageListUntreatedSampSrc(){
        return new ModelAndView("sample/stock/sampsrc_untreatedlist");
	}
	
	/**
	 * 未处理标本来源列表
	 */
	@RequestMapping(value ="/stock/pageGetListUntreatedSampSrc")
	@ResponseBody
	public Map<String,Object> pageGetListUntreatedSampSrc(YblyDto yblyDto){
		List<String> notZts = new ArrayList<>();
		notZts.add(StatusEnum.CHECK_PASS.getCode());
		notZts.add(StatusEnum.CHECK_UNPASS.getCode());
		yblyDto.setNotzts(notZts);
		List<YblyDto> t_List = yblyService.getPagedDtoList(yblyDto);
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", yblyDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 标本来源查看
	 */
	@RequestMapping(value="/stock/viewSampSrc")
	public ModelAndView viewSampleSource(YblyModel yblyModel){
		ModelAndView mav = new ModelAndView("sample/stock/sampsrc_view");
		YblyDto yblyDto = yblyService.getDtoById(yblyModel.getLyid());
		mav.addObject("yblyDto", yblyDto);
		
		if("00".equals(yblyDto.getRklx())){ 
			List<YbglDto> ybglDtos = ybglService.getDtoByLyid(yblyDto.getLyid());
			if(ybglDtos!=null && !ybglDtos.isEmpty()){
				YbglDto ybglDto = ybglDtos.get(0);
				ybglDto.setQswzdm(StringUtil.changeToPos(ybglDto.getCfs(), ybglDto.getQswz()));
				ybglDto.setJswzdm(StringUtil.changeToPos(ybglDto.getCfs(), ybglDto.getJswz()));
				mav.addObject("ybglDto", ybglDtos.get(0));
			}else {
				mav.addObject("ybglDto", new YbglDto());
			}
		}else{
			YbglDto ybglDto = new YbglDto();
			mav.addObject("ybglDto", ybglDto);
			List<YbglDto> ybglDtos = ybglService.getDtoByLyid(yblyDto.getLyid());
			if(ybglDtos != null && !ybglDtos.isEmpty()){
                for (YbglDto t_YbglDto : ybglDtos) {
                    t_YbglDto.setQswzdm(StringUtil.changeToPos(t_YbglDto.getCfs(), t_YbglDto.getQswz()));
                    t_YbglDto.setJswzdm(StringUtil.changeToPos(t_YbglDto.getCfs(), t_YbglDto.getJswz()));
                }
				mav.addObject("ybglDtos", ybglDtos);
			}
		}
		
		return mav;
	}
	
	/**
	 * 标本来源新增页面
	 */
	@RequestMapping(value="/stock/addSampSrc")
	public ModelAndView addSampleSource(){
		
		ModelAndView mav = new ModelAndView("sample/stock/sampsrc_edit");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SAMPLE_TYPE,BasicDataTypeEnum.UNIT_SOURCE});

		mav.addObject("yblxlist", jclist.get(BasicDataTypeEnum.SAMPLE_TYPE.getCode()));
		mav.addObject("dwlist", jclist.get(BasicDataTypeEnum.UNIT_SOURCE.getCode()));
		
		SbglDto sbglDto = new SbglDto();
		sbglDto.setSblx(DeviceTypeEnum.BX.getCode());
		List<SbglDto> bxList = sbglService.getDeviceList(sbglDto);
		mav.addObject("bxlist", bxList);
		
		YblyDto yblyDto = new YblyDto();
		yblyDto.setFormAction("add");
		yblyDto.setRklx("00");
		mav.addObject("yblyDto", yblyDto);
		
		YbglDto ybglDto = new YbglDto();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar cal = Calendar.getInstance();
		String yf = format.format(cal.getTime());
		ybglDto.setHqsj(yf);
		mav.addObject("ybglDto", ybglDto);
		return mav;
	}
	
	/**
	 * 标本来源新增保存
	 */
	@RequestMapping(value="/stock/addSaveSampSrc")
	@ResponseBody
	public Map<String, Object> addSaveSampleSource(YblyDto yblyDto,YbglModel ybglModel){
		//获取用户信息
		User user = getLoginInfo();
		yblyDto.setLrry(user.getYhid());
		ybglModel.setLrry(user.getYhid());
		
		boolean isSuccess = yblyService.insert(yblyDto,ybglModel);
		Map<String,Object> map = new HashMap<>();
		StringBuilder mString= new StringBuilder();
		if(ybglModel.getYbbhs()!=null && !ybglModel.getYbbhs().isEmpty()) {
			for(int i=0;i<ybglModel.getYbbhs().size();i++) {
				mString.append("<br/>标本编号：<span style='color:red'>").append(ybglModel.getYbbhs().get(i)).append("</span>");
			}
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr() + "<br/>来源编号：<span style='color:red'>"+yblyDto.getLybh() + "</span>" + mString):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 标本来源修改
	 */
	@RequestMapping(value="/stock/modSampSrc")
	public ModelAndView modSampleSource(YblyModel yblyModel){
		ModelAndView mav = new ModelAndView("sample/stock/sampsrc_edit");
		
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SAMPLE_TYPE,BasicDataTypeEnum.UNIT_SOURCE});

		mav.addObject("yblxlist", jclist.get(BasicDataTypeEnum.SAMPLE_TYPE.getCode()));
		mav.addObject("dwlist", jclist.get(BasicDataTypeEnum.UNIT_SOURCE.getCode()));
		
		SbglDto sbglDto = new SbglDto();
		sbglDto.setSblx(DeviceTypeEnum.BX.getCode());
		List<SbglDto> bxList = sbglService.getDeviceList(sbglDto);
		mav.addObject("bxlist", bxList);
		
		YblyDto yblyDto = yblyService.getDtoById(yblyModel.getLyid());
		if(yblyDto == null){
			yblyDto = new YblyDto(); 
		}
		yblyDto.setFormAction("mod");
		mav.addObject("yblyDto", yblyDto);
		
		if("00".equals(yblyDto.getRklx())){
			List<YbglDto> ybglDtos = ybglService.getDtoByLyid(yblyDto.getLyid());
			if(ybglDtos!=null)
				mav.addObject("ybglDto", ybglDtos.get(0));
			if (ybglDtos != null && StringUtil.isNotBlank(ybglDtos.get(0).getBxid())) {
				sbglDto.setSblx(DeviceTypeEnum.CT.getCode());
				sbglDto.setFsbid(ybglDtos.get(0).getBxid());
				List<SbglDto> ctList = sbglService.getDeviceList(sbglDto);
				mav.addObject("ctlist", ctList);
			}
			if (ybglDtos != null && StringUtil.isNotBlank(ybglDtos.get(0).getChtid())) {
				sbglDto.setSblx(DeviceTypeEnum.HZ.getCode());
				sbglDto.setFsbid(ybglDtos.get(0).getChtid());
				List<SbglDto> hzList = sbglService.getDeviceList(sbglDto);
				mav.addObject("hzlist", hzList);
			}
			if (ybglDtos != null && StringUtil.isNotBlank(ybglDtos.get(0).getHzid())) {

				mav.addObject("wzlist", sbglService.getPositionList(ybglDtos.get(0)));
			}
		}else{
			YbglDto ybglDto = new YbglDto();
			mav.addObject("ybglDto", ybglDto);
		}
		return mav;
	}
	
	/**
	 * 标本来源修改保存
	 */
	@RequestMapping(value="/stock/modSaveSampSrc")
	@ResponseBody
	public Map<String, Object> modSaveSampleSource(YblyDto yblyDto,YbglModel ybglModel){
		User user = getLoginInfo();
		yblyDto.setXgry(user.getYhid());
		ybglModel.setXgry(user.getYhid());
		boolean isSuccess = yblyService.update(yblyDto,ybglModel);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 删除标本来源
	 */
	@RequestMapping(value="/stock/delSampSrc")
	@ResponseBody
	public Map<String, Object> delSampSrc(YblyDto yblyDto){
		User user = getLoginInfo();
		yblyDto.setScry(user.getYhid());
		boolean isSuccess = yblyService.delete(yblyDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 废弃未处理标本
	 */
	@RequestMapping(value="/stock/discardSamp")
	@ResponseBody
	public Map<String, Object> discardSamp(YblyDto yblyDto){
		User user = getLoginInfo();
		yblyDto.setXgry(user.getYhid());
		yblyDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
		boolean isSuccess = yblyService.updateZtByIds(yblyDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
