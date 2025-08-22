package com.matridx.igams.experiment.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.OpennessTypeEnum;
import com.matridx.igams.common.enums.ProjectTypeEnum;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.experiment.dao.entities.MbglDto;
import com.matridx.igams.experiment.dao.entities.RwlyDto;
import com.matridx.igams.experiment.dao.entities.RwrqDto;
import com.matridx.igams.experiment.dao.entities.XmcyDto;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmjdrwDto;
import com.matridx.igams.experiment.dao.entities.XmjdxxDto;
import com.matridx.igams.experiment.service.svcinterface.IMbglService;
import com.matridx.igams.experiment.service.svcinterface.IRwlyService;
import com.matridx.igams.experiment.service.svcinterface.IRwrqService;
import com.matridx.igams.experiment.service.svcinterface.IXmcyService;
import com.matridx.igams.experiment.service.svcinterface.IXmglService;
import com.matridx.igams.experiment.service.svcinterface.IXmjdrwService;
import com.matridx.igams.experiment.service.svcinterface.IXmjdxxService;
import com.matridx.igams.experiment.service.svcinterface.IXmscxxService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/experiment")
public class ProjectManageController extends BaseController
{

	@Autowired
	IXmglService xmglService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IXmjdxxService xmjdxxService;
	@Autowired
	IXmjdrwService xmjdrwService;
	@Autowired
	IRwlyService rwlyService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IXmcyService xmcyService;
	@Autowired
	IMbglService mbglService;
	@Autowired
	IXmscxxService xmscxxService;
//	@Autowired
//	ITaskService taskService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IRwrqService rwrqService;
	@Autowired
	RedisUtil redisUtil;

	/**
	 * 任务进度
	 */
	@RequestMapping("/project/pageListProjectProgress")
	public ModelAndView pageListProjectProgress(){
		return new ModelAndView("experiment/project/project_progressList");
	}

	/**
	 * 任务进度数据
	 */
	@RequestMapping("/project/pagedataListProjectProgressInfo")
	@ResponseBody
	public Map<String,Object> pageListProjectProgressInfo(RwrqDto rwrqDto){
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		// 获得当前日期是一个星期的第几天
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		// 获得当前日期是一个星期的第几天
		int day = cal.get(Calendar.DAY_OF_WEEK);
		// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<String> stringList = new ArrayList<>();
		cal.add(Calendar.DATE,-8);
		for (int i = 0; i < 21; i++) {
			cal.add(Calendar.DATE,1);
			stringList.add(simpleDateFormat.format(cal.getTime()));
		}
		map.put("stringList",stringList);
		map.put("nowData",simpleDateFormat.format(new Date()));
		User user = getLoginInfo();
		rwrqDto.setYhid(user.getYhid());
		rwrqDto.setStartTime(stringList.get(0));
		rwrqDto.setEndTime(stringList.get(stringList.size() -1));
		List<RwrqDto> list = rwrqService.getProjectProgress(rwrqDto);
		map.put("list", list);
		return map;
	}

	/**
	 * 项目管理主页
	 */
	@RequestMapping(value = "/project/pageListProject")
	public ModelAndView pageListProject()
	{
		return new ModelAndView("experiment/project/project_index");
	}

	/**
	 * 项目管理文件页面
	 */
	@RequestMapping(value = "/project/pageGetListProject")
	public ModelAndView projectList(XmglDto xmglDto){
		ModelAndView mav = new ModelAndView("experiment/project/project_list");
		Map<String, Object> map = projectListMap(xmglDto);
		mav.addAllObjects(map);
		return mav;
	}

	/**
	 * 项目管理文件Map
	 */
	@RequestMapping(value = "/project/pagedataListProjectMap")
	@ResponseBody
	public Map<String,Object> projectListMap(XmglDto xmglDto){
		Map<String,Object> map = new HashMap<>();
		// 获取用户信息
		User user = getLoginInfo();
		xmglDto.setYhid(user.getYhid());
		if (xmglDto.getFxmid() != null)
		{
			XmglDto xmglDto2 = new XmglDto();
			xmglDto2.setXmid(xmglDto.getFxmid());
			List<XmglDto> xmgllist = xmglService.selectcatalogue(xmglDto2);
			map.put("xmgllist", xmgllist);
		}
		List<String> xmgkxs = Arrays.asList(OpennessTypeEnum.SHARE_PROJECT.getCode(),OpennessTypeEnum.PUBLIC_PROJECT.getCode());
		xmglDto.setXmgkxs(xmgkxs);
		List<XmglDto> xmglDtos = xmglService.getPersionDtoList(xmglDto);
		map.put("xmglDtos", xmglDtos);
		map.put("xmglDto", xmglDto);
		return map;
	}

	/**
	 * 全部项目页面
	 */
	@RequestMapping(value = "/project/pagedataAllProjectList")
	public ModelAndView allProjectList(XmglDto xmglDto){
		ModelAndView mav = new ModelAndView("experiment/project/project_list");
		Map<String, Object> map = allProjectListMap(xmglDto);
		mav.addAllObjects(map);
		return mav;
	}

	/**
	 * 全部项目页面Map
	 */
	@RequestMapping(value = "/project/pagedataAllProjectListMap")
	@ResponseBody
	public Map<String,Object> allProjectListMap(XmglDto xmglDto){
		Map<String,Object> map = new HashMap<>();
		// 获取用户信息
		User user = getLoginInfo();
		xmglDto.setYhid(user.getYhid());
		if (xmglDto.getFxmid() != null)
		{
			XmglDto xmglDto2 = new XmglDto();
			xmglDto2.setXmid(xmglDto.getFxmid());
			List<XmglDto> xmgllist = xmglService.selectcatalogue(xmglDto2);
			map.put("xmgllist", xmgllist);
		}
		List<String> xmgkxs = Arrays.asList(OpennessTypeEnum.PUBLIC_PROJECT.getCode(), OpennessTypeEnum.SHARE_PROJECT.getCode());
		xmglDto.setXmgkxs(xmgkxs);
		List<XmglDto> xmglDtos = xmglService.getPublicDtoList(xmglDto);
		map.put("xmglDtos", xmglDtos);
		map.put("xmglDto", xmglDto);
		return map;
	}

	/**
	 * 项目模板选择页面
	 */
	@RequestMapping(value = "/project/pagedataCreateProjectView")
	public ModelAndView createProjectView(XmglDto xmglDto){
		ModelAndView mav = new ModelAndView("experiment/project/project_create");
		Map<String, Object> map = createProjectMap(xmglDto);
		mav.addAllObjects(map);
		return mav;
	}

	/**
	 * 项目模板选择页面Map
	 */
	@RequestMapping(value = "/project/pagedataCreateProjectMap")
	@ResponseBody
	public Map<String,Object> createProjectMap(XmglDto xmglDto){
		Map<String,Object> map = new HashMap<>();
		List<MbglDto> mbgldtos = mbglService.getAllMb(new MbglDto());
		map.put("xmglDtos", mbgldtos);
		map.put("xmglDto", xmglDto);
		return map;
	}

	/**
	 * 新增项目页面
	 */
	@RequestMapping(value = "/project/pagedataAddProjectView")
	public ModelAndView addProjectView(XmglDto xmglDto){
		ModelAndView mav = new ModelAndView("experiment/project/project_add");
		Map<String, Object> map = addProjectMap(xmglDto);
		mav.addAllObjects(map);
		return mav;
	}
	/**
	 * 新增项目页面Map
	 */
	@RequestMapping(value = "/project/pagedataAddProjectMap")
	@ResponseBody
	public Map<String,Object> addProjectMap(XmglDto xmglDto){
		Map<String,Object> map = new HashMap<>();
		Map<String, List<JcsjDto>> grouplist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{ BasicDataTypeEnum.PROJECT_GROUP });
		map.put("grouplist", grouplist.get(BasicDataTypeEnum.PROJECT_GROUP.getCode()));
		// 获取项目公开类型
		List<OpennessTypeEnum> opennesslist = xmglService.getOpennessType();
		map.put("opennesslist", opennesslist);
		map.put("xmglDto", xmglDto);
		return map;
	}

	/**
	 * 新增文件夹页面
	 */
	@RequestMapping(value = "/project/pagedataAddFolderView")
	public ModelAndView addFolderView(XmglDto xmglDto){
		ModelAndView mav = new ModelAndView("experiment/project/project_addFolder");
		Map<String, Object> map = addFolderMap(xmglDto);
		mav.addAllObjects(map);
		return mav;
	}

	/**
	 * 新增文件夹页面
	 */
	@RequestMapping(value = "/project/pagedataAddFolderMap")
	@ResponseBody
	public Map<String,Object> addFolderMap(XmglDto xmglDto){
		Map<String,Object> map = new HashMap<>();
		// 获取项目公开类型
		List<OpennessTypeEnum> opennesslist = xmglService.getOpennessType();
		map.put("opennesslist", opennesslist);
		map.put("xmglDto", xmglDto);
		return map;
	}

	/**
	 * 新增文件夹保存
	 */
	@RequestMapping(value = "/project/pagedataAddSaveFolder")
	@ResponseBody
	public Map<String, Object> addSaveFolder(XmglDto xmglDto){
		User user = getLoginInfo();
		xmglDto.setLrry(user.getYhid());
		xmglDto.setXmlb(ProjectTypeEnum.FOLDER.getCode());
		xmglDto.setXmgkx(OpennessTypeEnum.PRIVATE_PROJECT.getCode());
		boolean isSuccess = xmglService.addSaveFolder(xmglDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		map.put("xmglDto", xmglDto);
		return map;
	}

	/**
	 * 选择模板新增项目，项目阶段和任务
	 */
	@RequestMapping(value = "/project/pagedataAddSaveProject")
	@ResponseBody
	public Map<String, Object> addSaveProject(XmglDto xmglDto)
	{
		User user = getLoginInfo();
		xmglDto.setLrry(user.getYhid());
		xmglDto.setXmlb(ProjectTypeEnum.PROJECT.getCode());
		boolean isSuccess = xmglService.addSaveXm(xmglDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		map.put("xmglDto", xmglDto);
		return map;
	}

	/**
	 * 删除项目或文件夹
	 */
	@RequestMapping(value = "/project/pagedataDelProject")
	@ResponseBody
	public Map<String, Object> delProject(XmglDto xmglDto)
	{
		User user = getLoginInfo();
		xmglDto.setScry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		// 判断删除权限
		boolean isSuccess = xmglService.delPermission(xmglDto);
		if (!isSuccess)
		{
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOMM_EX00001").getXxnr());
			return map;
		}
		isSuccess = xmglService.deleteByXmid(xmglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 项目编辑页面
	 */
	@RequestMapping(value = "/project/pagedataModProject")
	public ModelAndView modProject(XmglDto xmglDto){
		ModelAndView mav = new ModelAndView("experiment/project/project_edit");
		Map<String, Object> map = modProjectMap(xmglDto);
		mav.addAllObjects(map);
		return mav;
	}

	/**
	 * 项目编辑页面Map
	 */
	@RequestMapping(value = "/project/pagedataModProjectMap")
	@ResponseBody
	public Map<String, Object> modProjectMap(XmglDto xmglDto){
		Map<String, Object> map = new HashMap<>();
		XmglDto t_xmglDto = xmglService.getDto(xmglDto);
		Map<String, List<JcsjDto>> grouplist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{ BasicDataTypeEnum.PROJECT_GROUP });
		map.put("grouplist", grouplist.get(BasicDataTypeEnum.PROJECT_GROUP.getCode()));
		// 获取项目公开类型
		List<OpennessTypeEnum> opennesslist = xmglService.getOpennessType();
		map.put("opennesslist", opennesslist);
		map.put("xmglDto", t_xmglDto);
		return map;
	}

	/**
	 * 项目编辑保存
	 */
	@RequestMapping(value = "/project/pagedataModSaveProject")
	@ResponseBody
	public Map<String, Object> modSaveProject(XmglDto xmglDto){
		// 获取用户信息
		User user = getLoginInfo();
		xmglDto.setXgry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess = xmglService.modSaveProject(xmglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 修改父项目为公开状态
	 */
	@RequestMapping(value = "/project/updateFxmOpenness")
	@ResponseBody
	public Map<String, Object> updateFxmOpenness(XmglDto xmglDto){
		// 获取用户信息
		User user = getLoginInfo();
		xmglDto.setXgry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		xmglDto.setXmgkx(OpennessTypeEnum.PUBLIC_PROJECT.getCode());
		boolean isSuccess = xmglService.updateFxmOpenness(xmglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 编辑项目成员页面
	 */
	@RequestMapping(value = "/project/pagedataEditMemberView")
	public ModelAndView editMemberView(XmglDto xmglDto){
		ModelAndView mav = new ModelAndView("experiment/project/project_memberEdit");
		Map<String, Object> map = editMemberMap(xmglDto);
		mav.addAllObjects(map);
		return mav;
	}

	/**
	 * 编辑项目成员页面
	 */
	@RequestMapping(value = "/project/pagedataEditMemberMap")
	@ResponseBody
	public Map<String, Object> editMemberMap(XmglDto xmglDto){
		Map<String, Object> map = new HashMap<>();
		List<XmglDto> cyxx = xmglService.selectXtyh(xmglDto);
		map.put("XmglDto", cyxx);
		// 查询项目已有成员
		List<XmcyDto> selmemebers = xmcyService.selectSelMember(xmglDto);
		map.put("selmemebers", selmemebers);
		// 查询项目未选用户
		List<XmcyDto> unselmemebers = xmcyService.selectUnSelMember(xmglDto);
		map.put("unselmemebers", unselmemebers);
		XmglDto t_xmglDto = xmglService.getDto(xmglDto);
		map.put("xmglDto", t_xmglDto);
		return map;
	}

	/**
	 * 获取已选项目成员
	 */
	@RequestMapping(value = "/project/pagedataGetMemberList")
	@ResponseBody
	public Map<String, Object> getMemberList(XmglDto xmglDto){
		List<XmcyDto> selmemebers = xmcyService.selectSelMember(xmglDto);
		Map<String, Object> result = new HashMap<>();
		result.put("rows", selmemebers);
		return result;
	}

	/**
	 * 编辑项目成员保存
	 */
	@RequestMapping(value = "/project/pagedataEditSaveMember")
	@ResponseBody
	public Map<String, Object> editSaveMember(XmglDto xmglDto){
		// 获取用户信息
		User user = getLoginInfo();
		xmglDto.setXgry(user.getYhid());
		boolean isSuccess = xmcyService.editSaveMember(xmglDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 项目移动到上级目录保存
	 */
	@RequestMapping(value = "/project/projectReturnTop")
	@ResponseBody
	public Map<String, Object> projectReturnTop(XmglDto xmglDto){
		// 获取用户信息
		User user = getLoginInfo();
		xmglDto.setXgry(user.getYhid());
		boolean isSuccess = xmglService.projectReturnTop(xmglDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 项目归类保存
	 */
	@RequestMapping(value = "/project/projectPacking")
	@ResponseBody
	public Map<String, Object> projectPacking(XmglDto xmglDto){
		// 获取用户信息
		User user = getLoginInfo();
		xmglDto.setXgry(user.getYhid());
		boolean isSuccess = xmglService.projectPacking(xmglDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 项目阶段页面
	 */
	@RequestMapping(value = "/project/pagedataListStage")
	public ModelAndView pageListStage(XmglDto xmglDto){
		ModelAndView mav = new ModelAndView("experiment/project/project_stage");
		Map<String,Object> map = pageListStageMap(xmglDto);
		mav.addAllObjects(map);
		return mav;
	}

	/**
	 * 项目阶段页面
	 */
	@RequestMapping(value = "/project/pagedataListStageMap")
	@ResponseBody
	public Map<String,Object> pageListStageMap(XmglDto xmglDto){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		xmglDto.setYhid(user.getYhid());
		List<XmjdxxDto> xmjdxxlist = xmjdxxService.selectStageByXmid(xmglDto);
		map.put("xmjdxxlist", xmjdxxlist);
		XmglDto t_xmglDto = xmglService.getDto(xmglDto);
		t_xmglDto.setYhid(user.getYhid());
		String mintime;
		XmjdrwDto xmjdrwDto = new XmjdrwDto();
		xmjdrwDto.setXmid(xmglDto.getXmid());
		xmjdrwDto.setZt(xmglDto.getZt());
		if(StringUtil.isNotBlank(xmglDto.getMyflg())){
			xmjdrwDto.setYhid(user.getYhid());
		}
		if(StringUtil.isNotBlank(xmglDto.getStarttime())){
			mintime = xmglDto.getStarttime();
			xmjdrwDto.setStarttime(xmglDto.getStarttime());
		}else{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.WEEK_OF_YEAR,-3);
			Date date = calendar.getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			mintime= simpleDateFormat.format(date);
			xmjdrwDto.setStarttime(mintime);
		}
		if(StringUtil.isNotBlank(xmglDto.getEndtime())){
			xmjdrwDto.setEndtime(xmglDto.getEndtime());
		}

		List<XmjdrwDto> xmjdrwDtos = xmjdrwService.getXmrwxxs(xmjdrwDto);
		XmglDto xmcyDto=new XmglDto();
		xmcyDto.setXmid(xmjdrwDto.getXmid());
		List<XmcyDto> xmcylist=xmcyService.selectSelMember(xmcyDto);
		map.put("xmglDto", t_xmglDto);
		map.put("mintime", mintime);
		map.put("xmjdxxDtos", JSONObject.toJSONString(xmjdxxlist));
		map.put("xmjdrwDtos", JSONObject.toJSONString(xmjdrwDtos));
		map.put("xmcyDtos", JSONObject.toJSONString(xmcylist));
		return map;
	}

	/**
	 * 项目阶段页面
	 */
	@RequestMapping(value = "/project/pagedataGanttListStage")
	@ResponseBody
	public Map<String,Object> ganttListStage(XmglDto xmglDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		xmglDto.setYhid(user.getYhid());
		List<XmjdxxDto> xmjdxxlist = xmjdxxService.selectStageByXmid(xmglDto);

//		String mintime = xmjdxxService.selectMinTimeByXmid(xmglDto);
		String mintime;
		XmjdrwDto xmjdrwDto = new XmjdrwDto();
		xmjdrwDto.setXmid(xmglDto.getXmid());
		xmjdrwDto.setFzrmc(xmglDto.getFzrmc());
		xmjdrwDto.setRwmc(xmglDto.getRwmc());
		xmjdrwDto.setZt(xmglDto.getZt());
		if(StringUtil.isNotBlank(xmglDto.getMyflg())){
			xmjdrwDto.setYhid(user.getYhid());
		}
		if(StringUtil.isNotBlank(xmglDto.getStarttime())){
			mintime = xmglDto.getStarttime();
			xmjdrwDto.setStarttime(xmglDto.getStarttime());
		}else{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.WEEK_OF_YEAR,-3);
			Date date = calendar.getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			mintime= simpleDateFormat.format(date);
			xmjdrwDto.setStarttime(mintime);
		}
		if(StringUtil.isNotBlank(xmglDto.getEndtime())){
			xmjdrwDto.setEndtime(xmglDto.getEndtime());
		}
		List<XmjdrwDto> xmjdrwDtos = xmjdrwService.selectXmrwxxs(xmjdrwDto);
		map.put("mintime", mintime);
		map.put("xmjdxxDtos", JSONObject.toJSONString(xmjdxxlist));
		map.put("xmjdrwDtos", JSONObject.toJSONString(xmjdrwDtos));
		return map;
	}

	/**
	 * 项目新增任务页面
	 */
	@RequestMapping(value = "/project/pagedataAddProjectTask")
	public ModelAndView pagedataAddProjectTask(XmjdrwDto xmjdrwDto) {
		ModelAndView mav = new ModelAndView("experiment/project/project_taskEdit");
		Map<String, Object> map = pagedataAddProjectTaskMap(xmjdrwDto);
		mav.addAllObjects(map);
		return mav;
	}

	/**
	 * 项目新增任务页面Map
	 */
	@RequestMapping(value = "/project/pagedataAddProjectTaskMap")
	@ResponseBody
	public Map<String,Object> pagedataAddProjectTaskMap(XmjdrwDto xmjdrwDto) {
		Map<String,Object> map = new HashMap<>();
		// 查询项目成员
		XmglDto xmglDto = new XmglDto();
		xmglDto.setXmid(xmjdrwDto.getXmid());
		List<XmcyDto> xmcyDtolist = xmcyService.selectSelMember(xmglDto);
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]
				{ BasicDataTypeEnum.TASK_LABEL, BasicDataTypeEnum.TASK_RANK });
		xmjdrwDto.setFormAction("pagedataAdd");
		//获取项目任务文件类型
		xmjdrwDto.setYwlx(BusTypeEnum.IMP_PROJECT.getCode());
		XmjdxxDto xmjdxxDto=new XmjdxxDto();
		xmjdxxDto.setXmjdid(xmjdrwDto.getXmjdid());
        XmjdxxDto dto = xmjdxxService.getDto(xmjdxxDto);
		Object oncoxtsz=redisUtil.hget("matridx_xtsz","task.stage.cycle");
		if(oncoxtsz!=null) {
			XtszDto xtszDto = JSON.parseObject(String.valueOf(oncoxtsz), XtszDto.class);
			if(StringUtil.isNotBlank(xtszDto.getSzz())){
				String szz=xtszDto.getSzz();
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(szz));
				String time = DateUtils.format(calendar.getTime(), "yyyy-MM-dd");
				xmjdrwDto.setJzrq(time);
			}
		}
		map.put("xmjdxxDto", dto);
		map.put("xmjdrwDto", xmjdrwDto);
		map.put("xmcyDtolist", xmcyDtolist);
		map.put("task_label", jclist.get(BasicDataTypeEnum.TASK_LABEL.getCode()));
		map.put("task_rank", jclist.get(BasicDataTypeEnum.TASK_RANK.getCode()));
		return map;
	}

	/**
	 * 项目任务查看页面
	 */
	@RequestMapping(value = "/project/pagedataViewProjectTask")
	public ModelAndView pagedataViewProjectTask(XmjdrwDto xmjdrwDto)
	{
		ModelAndView mav = new ModelAndView("experiment/project/project_taskView");
		Map<String, Object> map = pagedataViewProjectTaskMap(xmjdrwDto);
		mav.addAllObjects(map);
		return mav;
	}
	/**
	 * 项目任务查看页面Map
	 */
	@RequestMapping(value = "/project/pagedataViewProjectTaskMap")
	@ResponseBody
	public Map<String,Object> pagedataViewProjectTaskMap(XmjdrwDto xmjdrwDto){
		Map<String,Object> map = new HashMap<>();
		XmjdrwDto rwxx=xmjdrwService.getDtoById(xmjdrwDto.getRwid());
		XmglDto xmglDto = new XmglDto();
		xmglDto.setXmid(rwxx.getXmid());
		List<XmjdxxDto> jdlist=xmjdxxService.selectStageByXmid(xmglDto);
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]
				{ BasicDataTypeEnum.TASK_LABEL, BasicDataTypeEnum.TASK_RANK });
		XmjdrwDto xmjdrwDtos = xmjdrwService.getDtoById(xmjdrwDto.getRwid());
		map.put("xmjdxxDto", jdlist);
		map.put("xmjdrwDto", xmjdrwDtos);
		map.put("task_label", jclist.get(BasicDataTypeEnum.TASK_LABEL.getCode()));
		map.put("task_rank", jclist.get(BasicDataTypeEnum.TASK_RANK.getCode()));
		//根据任务ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(xmjdrwDtos.getRwid());
		if(fjcfbDtos != null){
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
				fjcfbDto.setWjmhz(wjmhz);
			}
		}
		map.put("fjcfbDtos",fjcfbDtos);
		return map;
	}

	/**
	 * 钉钉端项目任务查看页面
	 */
	@RequestMapping(value = "/project/viewProjectTaskdd")
	@ResponseBody
	public Map<String,Object> viewProjectSubTaskdd(XmjdrwDto xmjdrwDto)
	{
		Map<String,Object> map = new HashMap<>();
		XmjdrwDto rwxx=xmjdrwService.getDtoById(xmjdrwDto.getRwid());
		XmglDto xmglDto = new XmglDto();
		xmglDto.setXmid(rwxx.getXmid());
		List<XmjdxxDto> jdlist=xmjdxxService.selectStageByXmid(xmglDto);
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]
				{ BasicDataTypeEnum.TASK_LABEL, BasicDataTypeEnum.TASK_RANK });
		XmjdrwDto xmjdrwDtos = xmjdrwService.getDtoById(xmjdrwDto.getRwid());
		map.put("xmjdxxDto", jdlist);
		map.put("xmjdrwDto", xmjdrwDtos);
		map.put("task_label", jclist.get(BasicDataTypeEnum.TASK_LABEL.getCode()));
		map.put("task_rank", jclist.get(BasicDataTypeEnum.TASK_RANK.getCode()));
		//根据任务ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(xmjdrwDtos.getRwid());
		if(fjcfbDtos != null){
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
				fjcfbDto.setWjmhz(wjmhz);
				//fjcfbDtos.get(i).setSign(commonService.getSign(fjcfbDtos.get(i).getFjid()));
			}
		}
		map.put("fjcfbDtos",fjcfbDtos);
		return map;
	}

	/**
	 * 项目任务查看子任务
	 */
	@RequestMapping(value = "/project/pagedataViewProjectZtask")
	@ResponseBody
	public Map<String,Object> viewProjectZTask(XmjdrwDto xmjdrwDto){
		Map<String, Object> map = new HashMap<>();
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]
		{ BasicDataTypeEnum.TASK_LABEL, BasicDataTypeEnum.TASK_RANK });
		XmjdrwDto xmjdrwDtos = xmjdrwService.getDtoById(xmjdrwDto.getRwid());
		map.put("xmjdrwDto", xmjdrwDtos);
		map.put("task_label", jclist.get(BasicDataTypeEnum.TASK_LABEL.getCode()));
		map.put("task_rank", jclist.get(BasicDataTypeEnum.TASK_RANK.getCode()));
		//根据任务ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(xmjdrwDtos.getRwid());
		if(fjcfbDtos != null){
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
				fjcfbDto.setWjmhz(wjmhz);
			}
		}
		map.put("fjcfbDtos",fjcfbDtos);
		return map;
	}

	/**
	 * 修改时查询当前任务信息
	 */
	@RequestMapping(value = "/project/pagedataModProjectSubTask")
	public ModelAndView modProjectSubTask(XmjdrwDto xmjdrwDto){
		ModelAndView mav = new ModelAndView("experiment/project/project_subtask");
		Map<String, Object> map = modProjectSubTaskMap(xmjdrwDto);
		mav.addAllObjects(map);
		return mav;
	}
	/**
	 * 修改时查询当前任务信息
	 */
	@RequestMapping(value = "/project/pagedataModProjectSubTaskMap")
	@ResponseBody
	public Map<String, Object> modProjectSubTaskMap(XmjdrwDto xmjdrwDto){
		Map<String, Object> map = new HashMap<>();
		XmglDto xmglDto = new XmglDto();
		xmglDto.setXmid(xmjdrwDto.getXmid());
		List<XmcyDto> xmcyDtolist = xmcyService.selectSelMember(xmglDto);
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]
				{ BasicDataTypeEnum.TASK_LABEL, BasicDataTypeEnum.TASK_RANK });
		XmjdrwDto xmjdrwDtos = xmjdrwService.getDtoById(xmjdrwDto.getRwid());
		if(xmjdrwDtos!=null) {
			for (int i = 0; i < xmjdrwDtos.getRwlyDtos().size(); i++){
				List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwid(xmjdrwDtos.getRwlyDtos().get(i).getLyid());
				if(fjcfbDtos!=null && fjcfbDtos.size()>0) {
					xmjdrwDtos.getRwlyDtos().get(i).setFj_List(fjcfbDtos);
				}
			}
		}
		List<XmjdrwDto> xmjdxxDtos = xmjdrwService.selectStageByRwid(xmjdrwDto);
		map.put("xmjdxxDtos", xmjdxxDtos);
		//获取项目任务文件类型
		if (xmjdrwDtos != null) {
			xmjdrwDtos.setYwlx(BusTypeEnum.IMP_PROJECT.getCode());
		}
		map.put("xmjdrwDto", xmjdrwDtos);
		map.put("xmcyDtolist", xmcyDtolist);
		map.put("task_label", jclist.get(BasicDataTypeEnum.TASK_LABEL.getCode()));
		map.put("task_rank", jclist.get(BasicDataTypeEnum.TASK_RANK.getCode()));
		//根据任务模板ID查询附件表信息
		List<FjcfbDto> m_fjcfbDtos = null;
		if (xmjdrwDtos != null) {
			m_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(xmjdrwDtos.getRwmbid());
		}
		if(m_fjcfbDtos != null){
			for (FjcfbDto m_fjcfbDto : m_fjcfbDtos) {
				String wjmhz = m_fjcfbDto.getWjm().substring(m_fjcfbDto.getWjm().lastIndexOf(".") + 1);
				m_fjcfbDto.setWjmhz(wjmhz);
			}
		}
		map.put("m_fjcfbDtos",m_fjcfbDtos);
		//根据任务ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = null;
		if (xmjdrwDtos != null) {
			fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(xmjdrwDtos.getRwid());
		}
		if(fjcfbDtos != null){
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
				fjcfbDto.setWjmhz(wjmhz);
			}
		}
		map.put("fjcfbDtos",fjcfbDtos);
		return map;
	}

	/**
	 * 查看子任务
	 */
	@RequestMapping(value = "/project/ModProjectZRW")
	@ResponseBody
	public Map<String, Object> ModProjectZRW(XmjdrwDto xmjdrwDto)
	{
		XmjdrwDto xmjdrwDtos = xmjdrwService.getDtoById(xmjdrwDto.getRwid());
		Map<String, Object> map = new HashMap<>();
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]
		{ BasicDataTypeEnum.TASK_LABEL, BasicDataTypeEnum.TASK_RANK });
		map.put("task_label", jclist.get(BasicDataTypeEnum.TASK_LABEL.getCode()));
		map.put("task_rank", jclist.get(BasicDataTypeEnum.TASK_RANK.getCode()));
		map.put("xmjdrwDto", xmjdrwDtos);
		//根据任务模板ID查询附件表信息
		List<FjcfbDto> m_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(xmjdrwDtos.getRwmbid());
		if(m_fjcfbDtos != null){
			for (FjcfbDto m_fjcfbDto : m_fjcfbDtos) {
				String wjmhz = m_fjcfbDto.getWjm().substring(m_fjcfbDto.getWjm().lastIndexOf(".") + 1);
				m_fjcfbDto.setWjmhz(wjmhz);
			}
		}
		map.put("m_fjcfbDtos",m_fjcfbDtos);
		//根据任务ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(xmjdrwDtos.getRwid());
		if(fjcfbDtos != null){
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
				fjcfbDto.setWjmhz(wjmhz);
			}
		}
		map.put("fjcfbDtos",fjcfbDtos);
		return map;
	}

	/**
	 * 修改项目任务
	 */
	@RequestMapping(value = "/project/pagedataSaveProjectSubTask")
	@ResponseBody
	public Map<String, Object> saveProjectSubTask(XmjdrwDto xmjdrwDto)
	{
		User user = getLoginInfo();
		xmjdrwDto.setXgry(user.getYhid());
		boolean isSuccess = xmjdrwService.updateGzglAndXmrw(xmjdrwDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 新增项目任务信息
	 */
	@RequestMapping(value = "/project/pagedataAddSaveProjectTask")
	@ResponseBody
	public Map<String, Object> addSaveProjectTask(XmjdrwDto xmjdrwDto){
		// 获取用户信息
		User user = getLoginInfo();
		xmjdrwDto.setLrry(user.getYhid());
		boolean isSuccess = xmjdrwService.addSaveProjectTask(xmjdrwDto);
		Map<String, Object> map = new HashMap<>();
		if (isSuccess){
			map.put("zrwid", xmjdrwDto.getRwid());
			map.put("zrwmc", xmjdrwDto.getRwmc());
		}
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 项目任务重新排序
	 */
	@RequestMapping(value = "/project/pagedataTaskSort")
	@ResponseBody
	public Map<String, Object> taskSort(XmjdrwDto xmjdrwDto){
		Map<String, Object> map = new HashMap<>();
		// 获取用户信息
		User user = getLoginInfo();
		xmjdrwDto.setXgry(user.getYhid());
		xmjdrwService.modJdtime(xmjdrwDto);
		XmjdrwDto t_xmjdrwDto = new XmjdrwDto();
		t_xmjdrwDto.setIds(xmjdrwDto.getIds());
		t_xmjdrwDto.setPrejdid(xmjdrwDto.getPrejdid());
		boolean isSuccess = xmjdrwService.taskSort(t_xmjdrwDto);
		if(!isSuccess){
			map.put("status","fail");
			map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
		}
		if(xmjdrwDto.getNewids() != null && xmjdrwDto.getNewids().size() > 0){
			t_xmjdrwDto.setIds(xmjdrwDto.getNewids());
			t_xmjdrwDto.setXmjdid(xmjdrwDto.getXmjdid());
			t_xmjdrwDto.setRwid(xmjdrwDto.getRwid());
			isSuccess = xmjdrwService.taskSort(t_xmjdrwDto);
		}
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 新增任务留言
	 */
	@RequestMapping(value = "/project/addSaveComment")
	@ResponseBody
	public Map<String, Object> addSaveComment(RwlyDto rwlyDto)
	{
		// 获取用户信息
		User user = getLoginInfo();
		rwlyDto.setLrry(user.getYhid());
		boolean isSuccess = rwlyService.insertDto(rwlyDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 删除任务
	 */
	@RequestMapping(value = "/project/pagedataSelectrw")
	@ResponseBody
	public Map<String, Object> deleterw(XmjdrwDto xmjdrwDto)
	{
		User user=getLoginInfo();
		xmjdrwDto.setScry(user.getYhid());
		boolean isSuccess = xmjdrwService.deleterw(xmjdrwDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 完成任务
	 */
	@RequestMapping(value = "/project/pagedataPerform")
	@ResponseBody
	public Map<String, Object> perform(XmjdrwDto xmjdrwDto)
	{
		User user = getLoginInfo();
		xmjdrwDto.setXgry(user.getYhid());
		boolean isSuccess = xmjdrwService.updateJdrwZt(xmjdrwDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 完成任务
	 */
	@RequestMapping(value = "/project/pagedataStartask")
	@ResponseBody
	public Map<String, Object> StartTask(XmjdrwDto xmjdrwDto){
		boolean isSuccess = xmjdrwService.StartTask(xmjdrwDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 删除阶段
	 */
	@RequestMapping(value = "/project/pagedataDelStage")
	@ResponseBody
	public Map<String, Object> delStage(XmjdxxDto xmjdxxDto){
		boolean isSuccess = xmjdxxService.delStage(xmjdxxDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 新建项目阶段
	 */
	@RequestMapping(value = "/project/pagedataAddSaveStage")
	@ResponseBody
	public Map<String, Object> addSaveStage(XmjdxxDto xmjdxxDto)
	{
		// 获取用户信息
		User user = getLoginInfo();
		xmjdxxDto.setLrry(user.getYhid());
		int xh = xmjdxxService.getXh(xmjdxxDto);
		xh++;
		xmjdxxDto.setXh(xh + "");
		boolean isSuccess = xmjdxxService.insertDto(xmjdxxDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 编辑项目阶段页面
	 */
	@RequestMapping(value = "/project/pagedataModProjectStageView")
	public ModelAndView modProjectStageView(XmjdxxDto xmjdxxDto)
	{
		ModelAndView mav = new ModelAndView("experiment/project/project_stageEdit");
		Map<String, Object> map = modProjectStageMap(xmjdxxDto);
		mav.addAllObjects(map);
		return mav;
	}
	/**
	 * 编辑项目阶段页面Map
	 */
	@RequestMapping(value = "/project/pagedataModProjectStageMap")
	@ResponseBody
	public Map<String,Object> modProjectStageMap(XmjdxxDto xmjdxxDto)
	{
		Map<String, Object> map = new HashMap<>();
		XmjdxxDto t_xmjdxxDto = xmjdxxService.getDto(xmjdxxDto);
		map.put("xmjdxxDto", t_xmjdxxDto);
		return map;
	}

	/**
	 * 修改阶段信息
	 */
	@RequestMapping(value = "/project/pagedataModSaveProjectStage")
	@ResponseBody
	public Map<String, Object> modSaveProjectStage(XmjdxxDto xmjdxxDto){
		User user = getLoginInfo();
		xmjdxxDto.setXgry(user.getYhid());
		boolean isSuccess = xmjdxxService.modSaveProjectStage(xmjdxxDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 项目阶段重新排序
	 */
	@RequestMapping(value = "/project/pagedataStageSort")
	@ResponseBody
	public Map<String, Object> stageSort(XmjdxxDto xmjdxxDto){
		// 获取用户信息
		User user = getLoginInfo();
		xmjdxxDto.setXgry(user.getYhid());
		boolean isSuccess = xmjdxxService.stageSort(xmjdxxDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 根据输入内容查询系统用户
	 */
	@RequestMapping(value = "/project/pagedataSelectXmcy")
	@ResponseBody
	public Map<String, Object> getXmcy(XmglDto xmglDto){
		Map<String, Object> map = new HashMap<>();
		List<XmglDto> cyxx = xmglService.selectXtyh(xmglDto);
		map.put("xmglDtos", cyxx);
		map.put("xmglDto", xmglDto);
		return map;
	}

	/**
	 * 回收站页面
	 */
	@RequestMapping(value = "/project/pagedataRecoverList")
	public ModelAndView recoverList(XmglDto xmglDto)
	{
		ModelAndView mav = new ModelAndView("experiment/project/project_recover");
		Map<String, Object> map = recoverListMap(xmglDto);
		mav.addAllObjects(map);
		return mav;
	}
	/**
	 * 回收站页面Map
	 */
	@RequestMapping(value = "/project/pagedataRecoverListMap")
	@ResponseBody
	public Map<String, Object> recoverListMap(XmglDto xmglDto)
	{

		Map<String, Object> map = new HashMap<>();
		// 获取用户信息
		User user = getLoginInfo();
		xmglDto.setYhid(user.getYhid());
		List<XmglDto> xmglDtos = xmscxxService.getRecoverDtoList(xmglDto);
		map.put("xmglDtos", xmglDtos);
		map.put("xmglDto", xmglDto);
		return map;
	}

	/**
	 * 彻底删除项目
	 */
	@RequestMapping(value = "/project/pagedataCompleteDelProject")
	@ResponseBody
	public Map<String, Object> completeDelProject(XmglDto xmglDto)
	{
		User user = getLoginInfo();
		xmglDto.setScry(user.getYhid());
		boolean isSuccess = xmglService.completeDelProject(xmglDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 还原项目
	 */
	@RequestMapping(value = "/project/pagedataRecoverProject")
	@ResponseBody
	public Map<String, Object> recoverProject(XmglDto xmglDto)
	{
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		xmglDto.setScry(user.getYhid());
		// 查询fxmid是否存在
		boolean isSuccess = xmglService.confirmFxmDto(xmglDto);
		if (!isSuccess)
		{
			map.put("status", "fail");
			map.put("msg", xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		}
		// 还原
		isSuccess = xmglService.recoverProject(xmglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 还原项目到根目录
	 */
	@RequestMapping(value = "/project/pagedataRecoverProjectPath")
	@ResponseBody
	public Map<String, Object> recoverProjectPath(XmglDto xmglDto)
	{
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		xmglDto.setScry(user.getYhid());
		// 还原
		boolean isSuccess = xmglService.recoverProjecPath(xmglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 个人工作任务表里修改当前任务的阶段信息
	 */
	@RequestMapping(value = "/project/modWorkTask")
	public ModelAndView modWorkTask(XmjdrwDto xmjdrwDto){
		ModelAndView mav = new ModelAndView("experiment/project/project_tasksubmit");
		Map<String, Object> map = modWorkTaskMap(xmjdrwDto);
		mav.addAllObjects(map);
		return mav;
	}

	/**
	 * 个人工作任务表里修改当前任务的阶段信息Map
	 */
	@RequestMapping(value = "/project/modWorkTaskMap")
	@ResponseBody
	public Map<String,Object> modWorkTaskMap(XmjdrwDto xmjdrwDto){
		Map<String,Object> map = new HashMap<>();
		XmjdrwDto t_xmjdrwDto = xmjdrwService.getDtoById(xmjdrwDto.getRwid());
		map.put("xmjdrwDto", t_xmjdrwDto);
		if (t_xmjdrwDto != null && StringUtil.isNotBlank(t_xmjdrwDto.getXmid())){
			XmjdxxDto xmjdxxDto = new XmjdxxDto();
			xmjdxxDto.setXmid(t_xmjdrwDto.getXmid());
			List<XmjdxxDto> xmjdxxlist = xmjdxxService.getDtoList(xmjdxxDto);
			map.put("xmjdxxlist", xmjdxxlist);
		}
		//根据任务ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(xmjdrwDto.getRwid());
		if(fjcfbDtos != null){
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
				fjcfbDto.setWjmhz(wjmhz);
			}
		}
		map.put("fjcfbDtos",fjcfbDtos);
		return map;
	}

	/**
	 * 个人工作任务表里修改研发子任务信息
	 */
	@RequestMapping(value = "/project/modWorkSubtask")
	public ModelAndView modWorkSubtask(XmjdrwDto xmjdrwDto){
		ModelAndView mav = new ModelAndView("experiment/project/project_subtasksubmit");
		Map<String, Object> map = modWorkSubtaskMap(xmjdrwDto);
		mav.addAllObjects(map);
		return mav;
	}
	/**
	 * 个人工作任务表里修改研发子任务信息
	 */
	@RequestMapping(value = "/project/modWorkSubtaskMap")
	@ResponseBody
	public Map<String, Object> modWorkSubtaskMap(XmjdrwDto xmjdrwDto){
		Map<String, Object> map = new HashMap<>();
		XmjdrwDto t_xmjdrwDto = xmjdrwService.getDtoById(xmjdrwDto.getRwid());
		map.put("xmjdrwDto", t_xmjdrwDto);
		if (t_xmjdrwDto != null && StringUtil.isNotBlank(t_xmjdrwDto.getXmid())){
			XmjdxxDto xmjdxxDto = new XmjdxxDto();
			xmjdxxDto.setXmid(t_xmjdrwDto.getXmid());
			List<XmjdxxDto> xmjdxxlist = xmjdxxService.getDtoList(xmjdxxDto);
			map.put("xmjdxxlist", xmjdxxlist);
		}
		//根据任务ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(xmjdrwDto.getRwid());
		if(fjcfbDtos != null){
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
				fjcfbDto.setWjmhz(wjmhz);
			}
		}
		map.put("fjcfbDtos",fjcfbDtos);
		return map;
	}
	/**
	 * 修改任务描述
	 */
	@RequestMapping(value = "/project/pagedataUpdateRwmsById")
	@ResponseBody
	public Map<String, Object> pagedataUpdateRwmsById(XmjdrwDto xmjdrwDto)
	{
		User user = getLoginInfo();
		xmjdrwDto.setXgry(user.getYhid());
		boolean isSuccess = xmjdrwService.updateDto(xmjdrwDto);
		Map<String, Object> map = new HashMap<>();
		if (isSuccess)
		{
			map.put("rwms", xmjdrwDto.getRwms());
		}
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 新增任务留言
	 */
	@RequestMapping(value = "/project/pagedataAddRwly")
	public @ResponseBody Map<String, Object> AddRwly(RwlyDto rwlyDto){
		User user = getLoginInfo();
		rwlyDto.setLrry(user.getYhid());
		rwlyDto.setLyid(StringUtil.generateUUID());
		boolean isSuccess = rwlyService.insertDto(rwlyDto);
		Map<String, Object> map = new HashMap<>();
		if (isSuccess){
			RwlyDto rwlyDtos = rwlyService.getDtoById(rwlyDto.getLyid());
			if (rwlyDtos != null){
				if ("PERSON_MASSAGE".equals(rwlyDtos.getLylx())){
					rwlyDtos.setColor("#72A17F");
				} else{
					rwlyDtos.setColor("#A3A3A3");
				}
				map.put("lysj", rwlyDtos.getLysj());
				map.put("lyry", rwlyDtos.getLyry());
				map.put("lylx", rwlyDtos.getLylx());
				map.put("lyxx", rwlyDtos.getLyxx());
				map.put("color", rwlyDtos.getColor());
			}
			List<FjcfbDto> fjcfbDtos= null;
			if (rwlyDtos != null) {
				fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(rwlyDtos.getLyid());
			}
			if(fjcfbDtos!=null&&fjcfbDtos.size()>0) {
				
				map.put("fjcfbDtos",fjcfbDtos);
			}
		}
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 点击显示全部留言和点击显示最近留言
	 */
	@RequestMapping(value = "/project/pagedataGetRwlyDto")
	public @ResponseBody Map<String, Object> getRwlyDto(RwlyDto rwlyDto){
		List<RwlyDto> rwlylist = rwlyService.getDtoList(rwlyDto);
		Map<String, Object> map = new HashMap<>();
		if (rwlylist != null && rwlylist.size() > 0){
			for (RwlyDto dto : rwlylist) {
				if ("PERSON_MASSAGE".equals(dto.getLylx())) {
					dto.setColor("#72A17F");
				} else {
					dto.setColor("#A3A3A3");
				}
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(dto.getLyid());
				if (fjcfbDtos != null && fjcfbDtos.size() > 0) {
					dto.setFj_List(fjcfbDtos);
				}
			}
			map.put("rwlylist", rwlylist);
		}

		return map;
	}

	/**
	 * 编辑任务日期页面
	 */
	@RequestMapping(value = "/project/pagedataModProjectTaskTime")
	public ModelAndView modProjectTaskTime(RwrqDto rwrqDto){
		ModelAndView mav = new ModelAndView("experiment/project/project_taskTime");
		Map<String, Object> map = modProjectTaskTimeMap(rwrqDto);
		mav.addAllObjects(map);
		return mav;
	}
	/**
	 * 编辑任务日期页面Map
	 */
	@RequestMapping(value = "/project/pagedataModProjectTaskTimeMap")
	@ResponseBody
	public Map<String, Object> modProjectTaskTimeMap(RwrqDto rwrqDto){
		Map<String, Object> map = new HashMap<>();
		//查询任务日期表
		List<RwrqDto> rwrqDtos = rwrqService.getDtoList(rwrqDto);
		map.put("rwrqDto", rwrqDto);
		map.put("rwrqDtos", rwrqDtos);
		return map;
	}
	
	/**
	 * 编辑任务日期保存
	 */
	@RequestMapping(value = "/project/pagedataModSaveTaskTime")
	@ResponseBody
	public Map<String, Object> modSaveTaskTime(RwrqDto rwrqDto){
		User user = getLoginInfo();
		rwrqDto.setXgry(user.getYhid());
		boolean isSuccess = rwrqService.modSaveTaskTime(rwrqDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 项目甘特图高级筛选
	 */
	@RequestMapping(value = "/project/pagedataSearchData")
	public ModelAndView searchData()
	{
		return new ModelAndView("experiment/project/project_search");
	}

	/**
	 * 编辑任务日期页面
	 */
	@RequestMapping(value = "/project/pagedataGetRwrq")
	@ResponseBody
	public Map<String, Object> pagedataGetRwrq(RwrqDto rwrqDto){
		Map<String, Object> map = new HashMap<>();
		//查询任务日期表
		RwrqDto dto = rwrqService.getDto(rwrqDto);
		map.put("rwrqDto", dto);
		return map;
	}

	/**
	 * 编辑任务日期保存
	 */
	@RequestMapping(value = "/project/pagedataModSaveRwrq")
	@ResponseBody
	public Map<String, Object> pagedataModSaveRwrq(RwrqDto rwrqDto){
		User user = getLoginInfo();
		rwrqDto.setXgry(user.getYhid());
		int isSuccess = rwrqService.modRqxxByRwidAndXmjdid(rwrqDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess>0 ? "success" : "fail");
		map.put("message", isSuccess>0 ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
