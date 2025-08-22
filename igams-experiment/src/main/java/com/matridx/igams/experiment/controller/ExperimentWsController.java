package com.matridx.igams.experiment.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JkdymxDto;
import com.matridx.igams.common.dao.entities.WkmxPcrModel;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.InvokingChildTypeEnum;
import com.matridx.igams.common.enums.InvokingTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJkdymxService;
import com.matridx.igams.experiment.dao.entities.NgsglWsDto;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmjdrwDto;
import com.matridx.igams.experiment.dao.entities.XmjdxxDto;
import com.matridx.igams.experiment.dao.entities.ZdhJkwdjlDto;
import com.matridx.igams.experiment.dao.entities.ZdhYbmxDto;
import com.matridx.igams.experiment.dao.entities.ZdhYbxxDto;
import com.matridx.igams.experiment.dao.entities.ZdhYhxxDto;
import com.matridx.igams.experiment.service.svcinterface.INgsglService;
import com.matridx.igams.experiment.service.svcinterface.IWkglService;
import com.matridx.igams.experiment.service.svcinterface.IWkmxService;
import com.matridx.igams.experiment.service.svcinterface.IXmjdrwService;
import com.matridx.igams.experiment.service.svcinterface.IXmjdxxService;
import com.matridx.igams.experiment.service.svcinterface.IZdhJkwdjlService;
import com.matridx.igams.experiment.service.svcinterface.IZdhYbmxService;
import com.matridx.igams.experiment.service.svcinterface.IZdhYbxxService;
import com.matridx.igams.experiment.service.svcinterface.IZdhYhxxService;
import com.matridx.igams.experiment.service.svcinterface.IZdhYqycxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ws")
public class ExperimentWsController {

	@Autowired
	IXmjdrwService xmjdrwService;
	@Autowired
	IXmjdxxService xmjdxxService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	INgsglService ngsglService;
	@Autowired
	IWkglService wkglService;
	@Autowired
	IWkmxService wkmxService;
	@Autowired
	IJkdymxService jkdymxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ICommonService commonService;
	@Autowired
	IZdhYbxxService zdhYbxxService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IZdhJkwdjlService zdhJkwdjlService;
	@Autowired
	IZdhYhxxService zdhYhxxService;
	@Autowired
	IZdhYbmxService ybmxService;
	@Autowired
	IZdhYqycxxService zdhYqycxxService;
	private final Logger log = LoggerFactory.getLogger(ExperimentWsController.class);
	
	/**
	 * 个人工作任务表里修改当前任务的阶段信息
	 */
	@RequestMapping(value = "/experiment/project/modWorkTask")
	public ModelAndView modWorkTask(XmjdrwDto xmjdrwDto){
		ModelAndView mav = new ModelAndView("experiment/project/project_tasksubmit");
		XmjdrwDto t_xmjdrwDto = xmjdrwService.getDtoById(xmjdrwDto.getRwid());
		mav.addObject("xmjdrwDto", t_xmjdrwDto);
		if (t_xmjdrwDto != null && StringUtil.isNotBlank(t_xmjdrwDto.getXmid())){
			XmjdxxDto xmjdxxDto = new XmjdxxDto();
			xmjdxxDto.setXmid(t_xmjdrwDto.getXmid());
			List<XmjdxxDto> xmjdxxlist = xmjdxxService.getDtoList(xmjdxxDto);
			mav.addObject("xmjdxxlist", xmjdxxlist);
		}
		//根据任务ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(xmjdrwDto.getRwid());
		if(fjcfbDtos != null){
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
				fjcfbDto.setWjmhz(wjmhz);
			}
		}
		mav.addObject("fjcfbDtos",fjcfbDtos);
		return mav;
	}
	
	/**
	 * 保存NGS信息
	 */
	@RequestMapping("/experiment/addSaveNgsData")
	@ResponseBody
	public Map<String,Object> getMgsData(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		try {
			String str_data=request.getParameter("Ngs_Data");
			String json_data="["+str_data+"]";
			log.error("---请求IP地址---:"+request.getRemoteAddr());
			log.error("---获取NGS信息---json_data:"+json_data);
			List<NgsglWsDto> list= JSONObject.parseArray(json_data, NgsglWsDto.class);
			boolean result;
			if(list!=null && list.size()>0) {
				result=ngsglService.addSaveNgsDtos(list);
				if(result) {
					map.put("status", "success");
					map.put("message", "保存成功!");
				}else {
					map.put("status", "fail");
					map.put("message", "保存失败!");
				}
			}else {
				map.put("status", "fail");
				map.put("errorCode", "未正常获取到数据！");
			}
		} catch (Exception e) {
			// TODO: handle exception
			map.put("status", "fail");
			map.put("message", e.toString());
		}
		return map;
	}
	/**
	 * 个人工作任务表里修改当前任务的阶段信息
	 */
	@RequestMapping(value = "/library/getResultPcr")
	@ResponseBody
	public Map<String, Object> getResultPcr(HttpServletRequest request) throws IOException{
		Map<String,Object> map = new HashMap<>();
		JkdymxDto jkdymxDto=new JkdymxDto();
		BufferedReader in;
        in = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder pcr_result = new StringBuilder();
        String getLine;
        while ((getLine = in.readLine()) != null) {
        	pcr_result.append(getLine);
        }
        in.close();
		if(StringUtil.isBlank(pcr_result.toString())) {
			map.put("status", "fail");
			map.put("errorCode", "未正常获取到数据！");
			return map;
		}
		WkmxPcrModel wkmxPcrModel = JSONObject.parseObject(pcr_result.toString(), WkmxPcrModel.class);
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			jkdymxDto.setLxqf("recv");
			jkdymxDto.setDysj(dateFm.format(new Date()));
			//jkdymxDto.setYwid(inspinfo.getYbbh());
			jkdymxDto.setNr(pcr_result.toString());
			jkdymxDto.setDydz(request.getRequestURI());
			jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_WKMX.getCode());
			jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_PCR_RESULT.getCode());
			jkdymxDto.setSfcg("2");
			
			boolean result = wkmxService.getWkmxDtoFromPcr(wkmxPcrModel);

			if(result) {
				map.put("status", "success");
				map.put("errorCode", "保存成功!");
			}else {
				map.put("status", "fail");
				map.put("errorCode", "保存失败!");
			}
			jkdymxService.insertJkdymxDto(jkdymxDto);//添加接口调用明细数据

		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorCode", e.toString());
			log.error(e.toString());
		}
		return map;
	}

	/**
	 * 钉钉端项目任务查看页面
	 */
	@RequestMapping(value = "/projectdd/viewProjectTaskdd")
	public ModelAndView viewProjectSubTaskdd(XmjdrwDto xmjdrwDto)
	{
		ModelAndView mav = new ModelAndView("experiment/project/project_taskViewdd");
		XmjdrwDto rwxx=xmjdrwService.getDtoById(xmjdrwDto.getRwid());
		XmglDto xmglDto = new XmglDto();
		xmglDto.setXmid(rwxx.getXmid());
		List<XmjdxxDto> jdlist=xmjdxxService.selectStageByXmid(xmglDto);
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]
				{ BasicDataTypeEnum.TASK_LABEL, BasicDataTypeEnum.TASK_RANK });
		XmjdrwDto xmjdrwDtos = xmjdrwService.getDtoById(xmjdrwDto.getRwid());
		mav.addObject("xmjdxxDto", jdlist);
		mav.addObject("xmjdrwDto", xmjdrwDtos);
		mav.addObject("task_label", jclist.get(BasicDataTypeEnum.TASK_LABEL.getCode()));
		mav.addObject("task_rank", jclist.get(BasicDataTypeEnum.TASK_RANK.getCode()));
		//根据任务ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(xmjdrwDtos.getRwid());
		if(fjcfbDtos != null){
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
				fjcfbDto.setWjmhz(wjmhz);
				fjcfbDto.setSign(commonService.getSign(fjcfbDto.getFjid()));
			}
		}
		mav.addObject("fjcfbDtos",fjcfbDtos);
		return mav;
	}

	/**
	 * 获取文库数据并插入
	 */
	@RequestMapping("/library/operateLibraryInfo")
	@ResponseBody
	public Map<String,Object> operateLibraryInfo(HttpServletRequest request){
		return wkglService.syncLibraryInfo(request);
	}
	/**
	 * 实验室外部标本信息
	 * @param ybxxDto
	 * @return
	 */
	@RequestMapping(value="/sample/samplelist")
	public ModelAndView getSamplelistPage(ZdhYbxxDto ybxxDto){
		ModelAndView mav=new ModelAndView("experiment/sample/sample_experimentList");
		ybxxDto.setSyzt("1");
		List<ZdhYbxxDto> list=zdhYbxxService.getDtoList(ybxxDto);
		mav.addObject("jcdwlist", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位List
		mav.addObject("list",list);
		return mav;
	}

	/**
	 * 实验室外部标本信息
	 * @param ybxxDto
	 * @return
	 */
	@RequestMapping(value="/sample/samplelistdata")
	@ResponseBody
	public Map<String,Object> samplelistdata(ZdhYbxxDto ybxxDto){
		Map<String,Object> map=new HashMap<>();
		List<ZdhYbxxDto> list=zdhYbxxService.getDtoList(ybxxDto);
		map.put("list",list);
		return map;
	}
	/**
	 * 样本实验信息上报
	 */
	@RequestMapping("/equipment/statusReporting")
	@ResponseBody
	public Map<String,Object> statusReporting(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String jsonString = request.getParameter("experiment");
		if(StringUtil.isNotBlank(jsonString)){
			try {
				boolean result = zdhYbxxService.statusReporting(jsonString);
				map.put("status",result?"success":"fail");
				map.put("message",result?"保存成功!":"保存失败!");
			} catch (BusinessException e) {
				map.put("status","fail");
				map.put("message","保存失败!");
			}
		}else {
			map.put("status","fail");
			map.put("message","保存失败!");
		}
		return map;
	}

	/**
	 * 建库仪温度上报
	 */
	@RequestMapping("/equipment/temperatureReporting")
	@ResponseBody
	public Map<String,Object> temperatureReporting(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String jsonString = request.getParameter("temperatureList");
		if(StringUtil.isNotBlank(jsonString)){
			try {
				boolean result = zdhJkwdjlService.temperatureReporting(jsonString);
				map.put("status",result?"success":"fail");
				map.put("message",result?"保存成功!":"保存失败!");
			} catch (BusinessException e) {
				map.put("status","fail");
				map.put("message","保存失败!");
			}
		}else {
			map.put("status","fail");
			map.put("message","保存失败!");
		}
		return map;
	}

	/**
	 * @Description: 仪器异常上报
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/6/19 10:55
	 */
	@RequestMapping("/equipment/exceptionReporting")
	@ResponseBody
	public Map<String,Object> exceptionReporting(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String jsonString = request.getParameter("exceptionList");
		if(StringUtil.isNotBlank(jsonString)){
			try {
				boolean result = zdhYqycxxService.exceptionReporting(jsonString);
				map.put("status",result?"success":"fail");
				map.put("message",result?"保存成功!":"保存失败!");
			}catch (BusinessException e) {
				map.put("status","fail");
				map.put("message","保存失败!");
			}
		}else{
			map.put("status","fail");
			map.put("message","保存失败!");
		}
		return map;
	}

/**
	 * 实验室外部标本信息
	 * @param ybxxDto
	 * @return
	 */
	@RequestMapping(value="/mobile/sample/sampleinfo")
	public ModelAndView getMobileSampleInfoPage(ZdhYbxxDto ybxxDto){
		ModelAndView mav=new ModelAndView("experiment/sample/sample_info");
		ZdhYbxxDto dto = zdhYbxxService.getDto(ybxxDto);
		mav.addObject("ybxxDto",dto);
		return mav;
	}

	/**
	 * 实验室外部建库温度
	 * @param ybxxDto
	 * @return
	 */
	@RequestMapping(value="/mobile/sample/temperatureinfo")
	public ModelAndView getMobileTemperatureInfoPage(ZdhYbxxDto ybxxDto){
		ModelAndView mav=new ModelAndView("experiment/sample/temperature_info");
		mav.addObject("ybxxDto",ybxxDto);
		return mav;
	}
	/**
	 * 实验室外部建库温度数据
	 * @param ybxxDto
	 * @return
	 */
	@RequestMapping(value="/mobile/sample/temperatureinfoData")
	@ResponseBody
	public Map<String,Object> getMobileTemperatureInfoPageData(ZdhYbxxDto ybxxDto){
		Map<String,Object> map = new HashMap<>();
		ZdhYbxxDto dto = zdhYbxxService.getDto(ybxxDto);
		ZdhJkwdjlDto jkwdjlDto = new ZdhJkwdjlDto();
		jkwdjlDto.setYbxxid(ybxxDto.getYbxxid());
		List<ZdhJkwdjlDto> dtoList = zdhJkwdjlService.getDtoList(jkwdjlDto);
		List<String> temperatureList = dtoList.stream().map(item -> item.getWd()).collect(Collectors.toList());
		List<String> dateList = dtoList.stream().map(item -> item.getSj()).collect(Collectors.toList());
		map.put("title",dto.getBbbm()+"建库温度");
		map.put("dateList",dateList);
		map.put("temperatureList",temperatureList);
		return map;
	}
	
	/**
	 * 查看样本流程信息
	 * @param ybmxDto
	 * @return
	 */
	@RequestMapping("/sample/viewSampleFlowpath")
	@ResponseBody
	public ModelAndView viewSampleFlowpath(ZdhYbmxDto ybmxDto){
		ModelAndView mav=new ModelAndView("experiment/sample/sample_experimentView");
		List<ZdhYbmxDto> ybmxDtos=ybmxService.getDtosByYbid(ybmxDto);
		mav.addObject("ybmxDto",ybmxDto);
		mav.addObject("ybmxDtos",ybmxDtos);
		return mav;
	}

	/**
	 * 生成二维码
	 * @param csid 基础数据csid
	 * @return String 二维码本地地址
	 */
	@RequestMapping(value="/qrCode/qrCodeGenerate")
	@ResponseBody
	public String qrCodeGenerate(String csid){
		return zdhYhxxService.qrCodeGenerate(csid);
	}

	/**
	 * 解析二维码,并进入指定页面
	 * /ws/qrCode/qrCodeAnalysis?id=xxxx
	 * @param id 必传
	 * @return
	 */
	@RequestMapping(value="/qrCode/qrCodeAnalysis")
	public ModelAndView qrCodeAnalysis(String id,String wechatid,HttpServletRequest request){
		ModelAndView mav = new ModelAndView("common/redirect/redirectPage");
		Map<String, Object> map = zdhYhxxService.qrCodeAnalysis(id,wechatid,request);
		mav.addAllObjects(map);
		return mav;
	}


	/**
	 * 检查wechatid是否绑定过
	 * @param wechatid
	 * @return String id
	 */
	@RequestMapping(value="/qrCode/checkWechatIdBound")
	@ResponseBody
	public Map<String,Object> checkWechatIdBound(String wechatid){
		Map<String,Object> map = new HashMap<>();
		ZdhYhxxDto dto = new ZdhYhxxDto();
		dto.setWechatid(wechatid);
		ZdhYhxxDto zdhYhxxDto = zdhYhxxService.getDto(dto);
		if (zdhYhxxDto != null){
			map.put("id",zdhYhxxDto.getJcsjid());
			map.put("zt",zdhYhxxDto.getZt());
		}
		return map;
	}
}
