package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.GzlcglDto;
import com.matridx.igams.common.dao.entities.GzpzModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.entities.XxglModel;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.HabitsTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.enums.TaskMassageTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/systemmain")
public class TaskController extends BaseController{
//	public static final String CHECK = "check";
	public static final String MSG = "message";	//返回信息
	public static final String RESULT = "result";	//执行结果
	@Autowired
	IGzglService gzglService;
	@Autowired
	IGzpzService gzpzService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IGzlcglService gzlcglService;
	@Autowired
	IGrszService grszService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	ICommonDao commonDao;
	@Autowired
	DingTalkUtil talkUtil;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlprefix;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IXtszService xtszService;
	@Override
	public String getPrefix() {
		return urlprefix;
	}
	
	/**
	 * 个人任务列表页面
	 */
	@RequestMapping("/task/pageListTask")
	public ModelAndView pageListSerialRule(){
 		ModelAndView mav = new ModelAndView("systemmain/task/task_list");
		mav.addObject("urlprefix", urlprefix);
		return mav;
	}


	/**
	 * 任务统计
	 */
	@RequestMapping("/task/pageStatistics")
	public ModelAndView pageStatistics(){
		ModelAndView mav = new ModelAndView("systemmain/task/task_stats");
		mav.addObject("urlprefix", urlprefix);
		return mav;
	}


	/**
	 * 任务
	 */
	@RequestMapping("/task/pageListTaskStatistics")
	public ModelAndView taskStatistics(){
		ModelAndView mav = new ModelAndView("systemmain/task/task_statistics");
		mav.addObject("urlPrefix", urlprefix);
		return mav;
	}

	/**
	 * 任务统计详情
	 */
	@RequestMapping("/task/pagedataGetListTaskStatsInfo")
	@ResponseBody
	public Map<String,Object> taskStatsInfo(GzglDto gzglDto){
		Map<String,Object> map = new HashMap<>();
		List<GzglDto> dtoList = gzglService.getTaskStatistics(gzglDto);
		map.put("task",dtoList);
		List<GzglDto> list = gzglService.getDepartTaskStatistics(gzglDto);
		map.put("depart",list);
		return map;
	}


	/**
	 * 任务统计详情
	 */
	@RequestMapping("/task/pagedataStatsInfo")
	@ResponseBody
	public Map<String,Object> pagedataStatsInfo(HttpServletRequest request,GzglDto gzglDto){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		gzglDto.setYhid(user.getYhid());
		List<GzglDto> gzglDtos = gzglService.getStatisDtoList(gzglDto);
		if ( null != gzglDtos && !gzglDtos.isEmpty()){
			map.put("bm",gzglDtos.get(0).getJgmc());
		}
		Map<String, List<GzglDto>> collect;
		if ("10".equals(gzglDto.getZt())){
			collect = gzglDtos.stream().filter(item-> StringUtil.isNotBlank(item.getQrrymc())).collect(Collectors.groupingBy(GzglDto::getQrrymc));
		}else {
			collect = gzglDtos.stream().filter(item-> StringUtil.isNotBlank(item.getZsxm())).collect(Collectors.groupingBy(GzglDto::getZsxm));
		}
		List<GzglDto> gzglDtoList = new ArrayList<>();
		for (Map.Entry<String, List<GzglDto>> entry : collect.entrySet()) {
			entry.getValue().size();
			GzglDto dto = new GzglDto();
			dto.setZsxm(entry.getKey());
			dto.setWwcsl(String.valueOf(entry.getValue().size()));
			gzglDtoList.add(dto);
		}
		map.put("gzglDtoList",gzglDtoList);
		map.put("wwczs",gzglDtos.size());

		return map;
	}

	/**
	 * 任务统计详情点击
	 */
	@RequestMapping("/task/pagedataStatsInfoList")
	public ModelAndView pagedataStatsInfoList(GzglDto gzglDto){
		ModelAndView mav = new ModelAndView("systemmain/task/task_stats_list");
		User user = getLoginInfo();
		gzglDto.setYhid(user.getYhid());
		List<GzglDto> gzglDtos = gzglService.getStatisDtoList(gzglDto);
		mav.addObject("dtoList", gzglDtos);
		return mav;
	}

	/**
	 * 部门任务列表
	 */
	@RequestMapping("/task/pageListTable")
	public ModelAndView pageListTable(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		cal.add(Calendar.MONTH, -2);
		ModelAndView mav = new ModelAndView("systemmain/task/task_table");
		mav.addObject("lrsjstart",simpleDateFormat.format(cal.getTime()));
		return mav;
	}

	/**
	 * 部门任务列表数据
	 */
	@RequestMapping("/task/pageGetListTable")
	@ResponseBody
	public Map<String,Object> pageGetListTable(GzglDto gzglDto){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		gzglDto.setYhid(user.getYhid());
		List<GzglDto> gzglDtos = gzglService.getTabInfo(gzglDto);
		map.put("list",gzglDtos);
		return map;
	}
	
	/**
	 * 个人任务列表
	 */
	@RequestMapping("/task/pageGetListTask")
	@ResponseBody
	public Map<String,Object> listTask(GzglDto gzglDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		gzglDto.setYhid(user.getYhid());
		if(StringUtil.isBlank(gzglDto.getFzr())){
			//获取用户信息
			gzglDto.setFzr(user.getYhid());
			gzglDto.setQrry(user.getYhid());
		}
		List<GzglDto> t_List = gzglService.getPagedDtoList(gzglDto);
		Map<String,Object> result = new HashMap<>();
		GzglDto gzglDto_t=new GzglDto();
		gzglDto_t.setFzr(user.getYhid());
		Map<String,String> map=gzglService.getStatistics(gzglDto_t);
		result.put("zs", String.valueOf(map.get("zs")));
		result.put("cssl", String.valueOf(map.get("cssl")));
		result.put("ljsl", String.valueOf(map.get("ljsl")));
		result.put("total", gzglDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 任务确认历史页面
	 */
	@RequestMapping("/task/pagedataTaskHistory")
	public ModelAndView pagedataTaskHistory(GzglDto gzglDto){
		ModelAndView mav = new ModelAndView("systemmain/task/task_viewRwqrls");
		List<GzlcglDto> gzlcglDtos = gzlcglService.getDtoListByGzid(gzglDto.getGzid());
		if(gzlcglDtos!=null && !gzlcglDtos.isEmpty()) {
			for (GzlcglDto gzlcglDto : gzlcglDtos) {
				//根据工作ID查询附件表信息
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(gzlcglDto.getLcid());
				fjcfbDto.setYwlx(BusTypeEnum.IMP_CONFIRM.getCode());
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
				gzlcglDto.setFjcfbList(fjcfbDtos);
				if (StringUtil.isNotBlank(gzlcglDto.getShyj())) {
					gzlcglDto.setShyj(gzlcglDto.getShyj().replaceAll("\\r\\n", "</br>")
							.replaceAll("\\n", "</br>")
							.replaceAll("\\r", "</br>"));
				}
			}
		}
		mav.addObject("gzlcglDtos", gzlcglDtos);
		mav.addObject("urlprefix", urlprefix);
		return mav;
	}
	
	/**
	 * 总任务列表页面
	 */
	@RequestMapping("/task/pageListCommonTask")
	public ModelAndView pageListCommonTask(GzglDto gzglDto){
		ModelAndView mav = new ModelAndView("systemmain/task/task_commonlist");
		List<GzglDto> taskNames=gzglService.selectTaskNames(gzglDto);
		List<GzglDto> institutions=gzglService.selectInstitution(gzglDto);
		mav.addObject("taskNames",taskNames);
		mav.addObject("institutions",institutions);
		mav.addObject("urlprefix", urlprefix);
		return mav;
	}
	
	/**
	 * 总任务列表
	 */
	@RequestMapping("/task/pageGetListCommonTask")
	@ResponseBody
	public Map<String,Object> listCommonTask(GzglDto gzglDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		gzglDto.setYhid(user.getYhid());
		List<GzglDto> t_List = gzglService.getPagedDtoList(gzglDto);
		Map<String,Object> result = new HashMap<>();
		Map<String,String> map=gzglService.getStatistics(gzglDto);
		result.put("zs", String.valueOf(map.get("zs")));
		result.put("cssl", String.valueOf(map.get("cssl")));
		result.put("ljsl", String.valueOf(map.get("ljsl")));
		result.put("total", gzglDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 查看页面
	 */
	@RequestMapping("/task/viewTask")
	public ModelAndView viewTask(GzglDto gzglDto){
		ModelAndView mav = new ModelAndView("systemmain/task/task_view");
		GzglDto t_gzglDto = gzglService.getDtoById(gzglDto.getGzid());
		t_gzglDto.setYwlx(BusTypeEnum.IMP_TASK.getCode());
		mav.addObject("gzglDto", t_gzglDto);
		List<GzglDto> rwlyDtos = gzglService.getRwlyDtos(gzglDto);
        List<GzglDto> czDtos = new ArrayList<>();
        List<GzglDto> lyDtos = new ArrayList<>();
		if(rwlyDtos!=null&& !rwlyDtos.isEmpty()){
		    for(GzglDto dto:rwlyDtos){
                if(TaskMassageTypeEnum.DEFAULT_MASSAGE.getCode().equals(dto.getLylx())){
                    czDtos.add(dto);
                }else if(TaskMassageTypeEnum.PERSON_MASSAGE.getCode().equals(dto.getLylx())){
                    lyDtos.add(dto);
                }
            }
        }
		mav.addObject("rwlyDtos", czDtos);
        mav.addObject("lyDtos", lyDtos);
		List<GzglDto> rwrqDtos = gzglService.getRwrqDtos(gzglDto);
		mav.addObject("rwrqDtos", rwrqDtos);
		//根据工作ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = gzglService.selectFjcfbDtoByGzid(gzglDto.getGzid());
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("urlprefix", urlprefix);
		return mav;
	}
	
	/**
	 * 任务修改页面
	 */
	@RequestMapping("/task/modTask")
	public ModelAndView modTask(GzglDto gzglDto){
		ModelAndView mav = new ModelAndView("systemmain/task/task_mod");
		GzglDto t_gzglDto = gzglService.getDtoById(gzglDto.getGzid());
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.TASK_URGENT});
		mav.addObject("urgentlist", jclist.get(BasicDataTypeEnum.TASK_URGENT.getCode()));
		mav.addObject("gzglDto", t_gzglDto);
		mav.addObject("urlprefix", urlprefix);
		return mav;
	}
	
	/**
	 * 任务修改保存
	 */
	@RequestMapping(value = "/task/modSaveTask")
	@ResponseBody
	public Map<String, Object> modSaveTask(GzglDto gzglDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		gzglDto.setXgry(user.getYhid());
		
		boolean isSuccess = gzglService.update(gzglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 任务修改保存(无按钮权限)
	 */
	@RequestMapping(value = "/task/pagedataModSaveTask")
	@ResponseBody
	public Map<String, Object> pagedataModSaveTask(GzglDto gzglDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		gzglDto.setXgry(user.getYhid());

		boolean isSuccess = gzglService.update(gzglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 进度提交页面
	 */
	@RequestMapping("/task/progresssubmitTask")
	public ModelAndView progresssubmitTask(GzglDto gzglDto,HttpServletRequest req){
		ModelAndView mav = new ModelAndView("systemmain/task/task_progress");
		User user = getLoginInfo(req);
		String newJzrq="";
		Object oncoxtsz=redisUtil.hget("matridx_xtsz","task.stage.cycle");
		if(oncoxtsz!=null) {
			XtszDto xtszDto = JSON.parseObject(String.valueOf(oncoxtsz), XtszDto.class);
			if(StringUtil.isNotBlank(xtszDto.getSzz())){
				String szz=xtszDto.getSzz();
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(szz));
				newJzrq = DateUtils.format(calendar.getTime(), "yyyy-MM-dd");
			}
		}
		mav.addObject("newJzrq", newJzrq);
		GzglDto t_gzglDto = gzglService.getDtoById(gzglDto.getGzid());
		GzpzModel gzpzModel = null;
		GzglDto rwrq =new GzglDto();
		if(t_gzglDto!=null && StringUtil.isNotBlank(t_gzglDto.getGzlx())) {
			gzpzModel = gzpzService.getModelById(t_gzglDto.getGzlx());
		}
		if(t_gzglDto!=null && StringUtil.isNotBlank(t_gzglDto.getXmjdid())) {
			GzglDto rwrqDto = gzglService.getRwrq(t_gzglDto,user.getYhid());
			if(rwrqDto!=null){
				rwrq=rwrqDto;
			}
		}
		if(StringUtil.isBlank(rwrq.getJzrq())){
			rwrq.setJzrq(newJzrq);
		}
		mav.addObject("rwrqDto", rwrq);
		if(gzpzModel == null)
			gzpzModel = new GzpzModel();
		mav.addObject("gzpzModel", gzpzModel);

		if (t_gzglDto != null) {
			t_gzglDto.setYwlx(BusTypeEnum.IMP_TASK.getCode());
		}
		mav.addObject("gzglDto", t_gzglDto);
		//根据工作ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = gzglService.selectFjcfbDtoByGzid(gzglDto.getGzid());
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("urlprefix", urlprefix);
		return mav;
	}

	/**
	 * 任务计划保存
	 */
	@RequestMapping(value = "/task/viewSaveTask")
	@ResponseBody
	public Map<String, Object> viewSaveTask(GzglDto gzglDto,HttpServletRequest req){
		Map<String,Object> map = new HashMap<>();

		if (CollectionUtils.isEmpty(gzglDto.getList())){
			map.put("status", "fail");
			map.put("message", "未获取到数据!");
			return map;
		}
		//获取用户信息
		User user = getLoginInfo(req);
		for (GzglDto dto : gzglDto.getList()) {
			if(StringUtil.isBlank(dto.getRqid())){
				map.put("status", "fail");
				map.put("message", "数据错误！");
				return map;
			}
			dto.setXgry(user.getYhid());
		}
		boolean isSuccess = gzglService.updateRwrqList(gzglDto.getList());
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 进度提交保存
	 */
	@RequestMapping(value = "/task/progresssubmitSaveTask")
	@ResponseBody
	public Map<String, Object> progressSaveTask(GzglDto gzglDto,HttpServletRequest req){
		//获取用户信息
		User user = getLoginInfo(req);
		gzglDto.setXgry(user.getYhid());
		
		boolean isSuccess = gzglService.progressSaveTask(gzglDto,req);
		
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 选择负责人页面
	 */
	@RequestMapping("/task/pagedataListFzr")
	public ModelAndView pageListFzr(){
		ModelAndView mav = new ModelAndView("systemmain/task/task_forward");
		mav.addObject("urlprefix", urlprefix);
		return mav;
	}
	
	/**
	 * 任务进度提交和确认弹出页面
	 */
	@RequestMapping("/task/pagedataCommonListUserPage")
	public ModelAndView pagelistQrr(GzglDto gzglDto) {
		ModelAndView mav = new ModelAndView("systemmain/task/task_listFzr");
		mav.addObject("GzglDto", gzglDto);
		mav.addObject("urlprefix", urlprefix);
		return mav;
	}
	/**
	 * 选择负责人列表
	 */
	@RequestMapping("/task/commonListUser")
	@ResponseBody
	public Map<String, Object> listFzr(GzglDto gzglDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		gzglDto.setYhid(user.getYhid());
		gzglDto.setQf(HabitsTypeEnum.USER_HABITS.getCode());
		List<GzglDto> t_List = gzglService.getPagedFzrList(gzglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", gzglDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 根据输入信息查询用户真实姓名
	 */
	@RequestMapping(value = "/task/pagedataCommonSelectUser")
	@ResponseBody
	public Map<String, Object> selectTaskFzr(GzglDto gzglDto){
		List<GzglDto> f_gzglDtos = gzglService.selectTaskFzr(gzglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("f_gzglDtos", f_gzglDtos);
		return map;
	}
	
	/**
	 * 任务删除
	 */
	@RequestMapping(value = "/task/delTask")
	@ResponseBody
	public Map<String, Object> delTask(GzglDto gzglDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		gzglDto.setScry(user.getYhid());
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = false;
		try {
			isSuccess = gzglService.delete(gzglDto,request);
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00004").getXxnr()+e.getMessage());
			return map;
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 任务删除(无按钮权限)
	 */
	@RequestMapping(value = "/task/pagedataDelTask")
	@ResponseBody
	public Map<String, Object> pagedataDelTask(GzglDto gzglDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		gzglDto.setScry(user.getYhid());
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = false;
		try {
			isSuccess = gzglService.deleteDto(gzglDto);
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00004").getXxnr()+e.getMessage());
			return map;
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 任务转发页面
	 */
	@RequestMapping("/task/taskcareTask")
	public ModelAndView taskcareTask(GzglDto gzglDto){
		ModelAndView mav = new ModelAndView("systemmain/task/task_care");
		List<GzglDto> gzglDtos = gzglService.selectDtoByIds(gzglDto.getIds());
		mav.addObject("gzglDtos", gzglDtos);
		mav.addObject("zt", gzglDto.getZt());
		mav.addObject("urlprefix", urlprefix);
		return mav;
	}
	
	/**
	 * 任务转发保存
	 */
	@RequestMapping(value = "/task/taskcareSaveTask")
	@ResponseBody
	public Map<String, Object> taskCareSaveTask(GzglDto gzglDto,HttpServletRequest req){
		//获取用户信息
		Map<String, Object> data = new HashMap<>();
		Boolean result = false;
		try{
			User user = getLoginInfo(req);
			gzglDto.setXgry(user.getYhid());
			gzglDto.setTszmc(user.getZsxm());
			Map<String,Object> map = new HashMap<>();
			if (StatusEnum.CHECK_SUBMIT.getCode().equals(gzglDto.getZt())){
				GzglDto dto = new GzglDto();
				dto.setIds(gzglDto.getGzid());
				List<GzglDto> list = gzglService.getDtoList(dto);
				for(GzglDto gzglDto_t:list){
					if(gzglDto_t.getFzr().equals(gzglDto.getFzr())){
						map.put("status","fail");
						map.put("message", "确认人员不得与负责人一致！");
						return map;
					}
				}
			}
			boolean isSuccess = gzglService.taskcareSaveTask(gzglDto,req);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			String mess = e.getMsg();
			XxglModel xxglModel = xxglService.getModelById(e.getMsgId());
			String idMsg = xxglModel==null?"":xxglModel.getXxnr();
			data.put("status", "fail");
			data.put(MSG, idMsg + (StringUtil.isNotBlank(idMsg) && StringUtil.isNotBlank(mess) ? "<br/>" : "")
					+ (StringUtil.isNotBlank(mess) ? mess : ""));
		}
		data.put(RESULT, result);
		return data;
	}
	
	/**
	 * 任务确认列表页面
	 */
	@RequestMapping("/task/pageListTaskConfirm")
	public ModelAndView pageListTaskConfirm(){
		ModelAndView mav = new ModelAndView("systemmain/task/task_listConfirm");
		mav.addObject("urlprefix", urlprefix);
		return mav;
	}
	
	/**
	 * 任务确认列表
	 */
	@RequestMapping("/task/pageGetListTaskConfirm")
	@ResponseBody
	public Map<String,Object> pageGetListTaskConfirm(GzglDto gzglDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		gzglDto.setQrry(user.getYhid());
		gzglDto.setFzr(user.getYhid());
		List<GzglDto> t_List = gzglService.getPagedConfirmList(gzglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", gzglDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	/**
	 * 任务确认历史记录列表
	 */
	@RequestMapping("/task/pagedataTaskConfirmed")
	@ResponseBody
	public Map<String,Object> pageGetListTaskConfirmed(GzlcglDto gzlcglDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		gzlcglDto.setLrry(user.getYhid());
		List<GzlcglDto> t_List = gzlcglService.getPagedTaskConfirmedList(gzlcglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", gzlcglDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 任务确认页面
	 */
	@RequestMapping("/task/confirmTask")
	public ModelAndView confirmTask(GzglDto gzglDto,HttpServletRequest request){
		ModelAndView mav = new ModelAndView("systemmain/task/task_confirm");
		User user = getLoginInfo(request);
		GzglDto t_gzglDto = gzglService.getDtoById(gzglDto.getGzid());
		GzglDto rwrq =new GzglDto();
		if(t_gzglDto!=null && StringUtil.isNotBlank(t_gzglDto.getXmjdid())) {
			GzglDto rwrqDto = gzglService.getRwrq(t_gzglDto,user.getYhid());
			if(rwrqDto!=null){
				rwrq=rwrqDto;
			}
		}
		Object oncoxtsz=redisUtil.hget("matridx_xtsz","task.stage.cycle");
		String newJzrq="";
		if(oncoxtsz!=null) {
			XtszDto xtszDto = JSON.parseObject(String.valueOf(oncoxtsz), XtszDto.class);
			if(StringUtil.isNotBlank(xtszDto.getSzz())){
				String szz=xtszDto.getSzz();
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(szz));
				newJzrq = DateUtils.format(calendar.getTime(), "yyyy-MM-dd");
			}
		}
		mav.addObject("newJzrq", newJzrq);
		if(StringUtil.isBlank(rwrq.getJzrq())){
			rwrq.setJzrq(newJzrq);
		}
		mav.addObject("rwrqDto", rwrq);
		mav.addObject("gzglDto", t_gzglDto);
		GzpzModel gzpzModel = null;
		if(t_gzglDto!=null && StringUtil.isNotBlank(t_gzglDto.getGzlx())) {
			gzpzModel = gzpzService.getModelById(t_gzglDto.getGzlx());
		}

		if(gzpzModel == null)
			gzpzModel = new GzpzModel();
		mav.addObject("gzpzModel", gzpzModel);
		//根据工作ID查询附件表信息
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(gzglDto.getGzid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_TASK.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		//查询个人设置表默认确认人员
		GrszDto grszDto = grszService.selectByYhid(user.getYhid());
		if(grszDto == null){
			grszDto = new GrszDto();
		}
		mav.addObject("grszDto",grszDto);
		mav.addObject("ywlx",BusTypeEnum.IMP_CONFIRM.getCode());
		mav.addObject("urlprefix", urlprefix);
		return mav;
	}
	
	/**
	 * 任务确认保存
	 */
	@RequestMapping("/task/confirmSaveTask")
	@ResponseBody
	public Map<String, Object> confirmSaveTask(GzglDto gzglDto,HttpServletRequest req){
		Map<String,Object> map = new HashMap<>();
		//获取用户信息
		User user = getLoginInfo(req);
		gzglDto.setXgry(user.getYhid());
		gzglDto.setLrry(user.getYhid());
		gzglDto.setTszmc(user.getZsxm());
		boolean isSuccess = gzglService.confirmSaveTask(gzglDto,req);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 批量任务确认页面
	 */
	@RequestMapping("/task/batchconfirmTask")
	public ModelAndView batchconfirmTask(GzglDto gzglDto){
		ModelAndView mav = new ModelAndView("systemmain/task/task_batchconfirm");
		mav.addObject("gzglDto", gzglDto);
		mav.addObject("urlprefix", urlprefix);
		return mav;
	}
	
	/**
	 * 批量任务确认保存
	 */
	@RequestMapping("/task/batchconfirmSaveTask")
	@ResponseBody
	public Map<String, Object> batchconfirmSaveTask(GzglDto gzglDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		gzglDto.setXgry(user.getYhid());
		gzglDto.setLrry(user.getYhid());
		
		boolean isSuccess = gzglService.batchconfirmSaveTask(gzglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 确认任务转交
	 */
	@RequestMapping("/task/confirmcareTask")
	public ModelAndView confirmcareTask(GzglDto gzglDto){
		ModelAndView mav = new ModelAndView("systemmain/task/task_confirmcare");
		List<User> xtyhlist=commonDao.getUserList();
		mav.addObject("xtyhlist",xtyhlist);
		mav.addObject("gzglDto", gzglDto);
		mav.addObject("urlPrefix", urlprefix);
		return mav;
	}


	/**
	 * 确认任务转交保存
	 */
	@RequestMapping("/task/confirmcareSaveTask")
	@ResponseBody
	public Map<String, Object> confirmcareSaveTask(GzglDto gzglDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		//获取用户信息
		User user = getLoginInfo(request);
		User user_t = new User();
		user_t.setYhid(gzglDto.getQrry());
		User t_user = commonDao.getUserInfoById(user_t);
		gzglDto.setXgry(user.getYhid());
		gzglDto.setLrry(user.getYhid());
		List<GzglDto> list = gzglService.getDtoList(gzglDto);
		for(GzglDto gzglDto_t:list){
			if(gzglDto_t.getFzr().equals(gzglDto.getQrry())){
				map.put("status","fail");
				map.put("message", "确认人员不得与负责人一致！");
				return map;
			}
		}
		boolean isSuccess = gzglService.updateConfirmPerson(gzglDto,list,t_user);
		//发送钉钉消息
		if(isSuccess){
			if(t_user!=null){
				for(GzglDto t_gzglDto:list){
					if(!StringUtil.isBlank(t_gzglDto.getQrrydd())){
						talkUtil.sendWorkMessage(t_user.getYhm(), t_user.getDdid(), xxglService.getMsg("ICOMM_SH00009"),xxglService.getMsg("ICOMM_SH00001",t_gzglDto.getZsxm(),t_gzglDto.getYwmc() ,t_gzglDto.getRwmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
					}
				}
			}
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 修改备注
	 */
	@RequestMapping("/task/pagedataUpdateBz")
	@ResponseBody
	public Map<String,String> updateBz(GzglDto gzglDto){
		Map<String,String> map=new HashMap<>();
			boolean result=gzglService.update(gzglDto);
			if(result) {
				map.put("state","success");
				map.put("bz", gzglDto.getBz());
			}else {
				map.put("state","faile");
				map.put("bz", gzglDto.getBz());
			}
		return map;
	}
	
	/**
	 * 日历页面
	 */
	@RequestMapping("/calendar/pageListCalendarView")
	public ModelAndView pageListCalendarView() {
		return new ModelAndView("systemmain/task/calendar");
	}
	
	/**
	 * 日历查询当前任务
	 */
	@RequestMapping("/task/pagedataGzglByfzr")
	@ResponseBody
	public Map<String,Object> pagedataGzglByfzr(GzglDto gzglDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		gzglDto.setFzr(user.getYhid());
		List<GzglDto> gzglList=gzglService.getGzglByfzr(gzglDto);
		map.put("gzglList", gzglList);
		return map;
	}
	/**
	 * 我发起的任务列表页面
	 */
	@RequestMapping("/task/pageListPersonalTask")
	public ModelAndView pageListTask(){
		ModelAndView mav = new ModelAndView("systemmain/task/pertask_list");
		mav.addObject("urlprefix", urlprefix);
		return mav;
	}
	@RequestMapping("/task/pageGetListPersonalTask")
	@ResponseBody
	public Map<String,Object> listPersonalTask(GzglDto gzglDto, HttpServletRequest request){
		if(StringUtil.isBlank(gzglDto.getLrry())){
			//获取用户信息
			User user = getLoginInfo(request);
			gzglDto.setLrry(user.getYhid());
		}
		Map<String,Object> map = new HashMap<>();
		List<GzglDto> Grrwlist=gzglService.getPagedDtoList(gzglDto);
		map.put("total",gzglDto.getTotalNumber());
		map.put("rows",Grrwlist);
		return map;
	}

	/**
	 * 任务计划页面
	 */
	@RequestMapping("/task/taskPlanFunction")
	public ModelAndView taskPlanFunction(GzglDto gzglDto,HttpServletRequest request){
		ModelAndView mav = new ModelAndView("systemmain/task/task_taskPlan");
		User user = getLoginInfo(request);
		GzglDto rwrq = gzglService.getRwrq(gzglDto,user.getYhid());
		if(rwrq==null){
			rwrq=new GzglDto();
		}
		GzglDto xmjdxx = gzglService.getXmjdxx(gzglDto.getXmjdid());
		if(xmjdxx!=null){
			gzglDto.setJdmc(xmjdxx.getJdmc());
		}
		mav.addObject("urlprefix", urlprefix);
		mav.addObject("gzglDto", gzglDto);
		mav.addObject("rwrqDto", rwrq);
		return mav;
	}

	/**
	 * 任务计划保存
	 */
	@RequestMapping("/task/pagedataSaveTaskPlan")
	@ResponseBody
	public Map<String,Object> pagedataSaveTaskPlan(GzglDto gzglDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		gzglDto.setXgry(user.getYhid());
		boolean isSuccess = gzglService.modRwrqByRwidAndXmjdid(gzglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 根据培训id和人员id来获取工作管理表的信息
	 */
	@RequestMapping("/task/minidataTaskByYwidAndFzr")
	@ResponseBody
	public Map<String,Object> minidataTaskByYwidAndFzr(GzglDto gzglDto){
		List<String> fzr=new ArrayList<>();
		fzr.add(gzglDto.getYhid());
		gzglDto.setIds(fzr);
		Map<String,Object> map = new HashMap<>();
		GzglDto gzglDto_t=gzglService.getVerificationDto(gzglDto);
		map.put("gzglDto",gzglDto_t);
		return map;
	}

	@RequestMapping("/task/pageGetListPersonalPoll")
	@ResponseBody
	public Map<String,Object> pageGetListPersonalPoll(GzglDto gzglDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		super.setCzdmList(request,map);
		super.setTyszList(request,map);
		Object oncoxtsz=redisUtil.hget("matridx_xtsz","taskpool.user");
		if(oncoxtsz!=null){
			XtszDto xtszDto = JSON.parseObject(String.valueOf(oncoxtsz), XtszDto.class);
			if(StringUtil.isNotBlank(xtszDto.getSzz())){
				List<String> yhms = Arrays.asList(xtszDto.getSzz().split(","));
				gzglDto.setYhms(yhms);
				List<GzglDto> list = gzglService.getPagedDtoList(gzglDto);
				map.put("rows",list);
				map.put("total",gzglDto.getTotalNumber());
			}
		}else{
			map.put("rows",new ArrayList<>());
			map.put("total","0");
		}
		return map;
	}

	/**
	 * 领取任务
	 * @param gzglDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/task/receiveTask")
	@ResponseBody
	public Map<String,Object> receiveTask(GzglDto gzglDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = false;
		User user = getLoginInfo();
		gzglDto.setXgry(user.getYhid());
		GzglDto dto = new GzglDto();
		dto.setQrry(user.getYhid());
		dto.setFzr(user.getYhid());
		GzglDto gzgl = gzglService.getDtoById(gzglDto.getGzid());
		Object oncoxtsz=redisUtil.hget("matridx_xtsz","taskpool.user");
		List<String> yhms = new ArrayList<>();
		if(oncoxtsz!=null){
			XtszDto xtszDto = JSON.parseObject(String.valueOf(oncoxtsz), XtszDto.class);
			if(StringUtil.isNotBlank(xtszDto.getSzz())){
				yhms = Arrays.asList(xtszDto.getSzz().split(","));
			}
		}
		if(StatusEnum.CHECK_NO.getCode().equals(gzgl.getZt())){
			//任务为未完成状态，判断当前负责人是否为系统设置用户，不是且不是当前登录人员则说明已被领取
			if(!yhms.contains(gzgl.getFzryhm())&&!user.getYhid().equals(gzgl.getFzr())){
				map.put("status", "fail");
				map.put("message", "当前任务已被他人领取,请刷新页面!");
				map.put(RESULT, isSuccess);
				return map;
			}
		}else if(StatusEnum.CHECK_SUBMIT.getCode().equals(gzglDto.getZt())){
			//任务为确认中状态，判断当前确认人是否为系统设置用户，不是且不是当前登录人员则说明已被领取
			if(!yhms.contains(gzgl.getQrryhm())&&!user.getYhid().equals(gzgl.getQrry())){
				map.put("status", "fail");
				map.put("message", "当前任务已被他人领取,请刷新页面!");
				map.put(RESULT, isSuccess);
				return map;
			}
		}
		List<GzglDto> t_list = gzglService.getConfirmList(dto);//获取已接未完成
		XtszDto xtszDto = xtszService.getDtoById("task.max.fractionlimitation");
		String failMessage = "";
		BigDecimal fsnum=new BigDecimal("0");//接取任务分数
		BigDecimal maxfs=new BigDecimal(xtszDto.getSzz());//最大分数限制
		BigDecimal yyfs =new BigDecimal("0");//已有分数
		if(!"EMERGENT".equals(gzgl.getRwjbdm())){
			if(!CollectionUtils.isEmpty(t_list)){
				for (GzglDto gzglDto1 : t_list) {
					if(StringUtil.isNotBlank(gzglDto1.getJdfs())){
						yyfs=yyfs.add(new BigDecimal(gzglDto1.getJdfs()));
					}else if(StringUtil.isNotBlank(gzglDto1.getFs())){
						yyfs=yyfs.add(new BigDecimal(gzglDto1.getFs()));
					}
				}
			}
			if(StringUtil.isNotBlank(gzgl.getJdfs())){
				fsnum=fsnum.add(new BigDecimal(gzgl.getJdfs()));
			}else if(StringUtil.isNotBlank(gzgl.getFs())){
				fsnum=fsnum.add(new BigDecimal(gzgl.getFs()));
			}
			//已有分数大于限制分数  并且手里有未完成的任务
			if(yyfs.add(fsnum).compareTo(maxfs)>0&&!CollectionUtils.isEmpty(t_list)) {
				map.put("status", "fail");
				map.put("message", "已达到最大领取分数上限，领取失败，请先完成已有任务！");
				map.put(RESULT, isSuccess);
				return map;
			}
		}
		if (StatusEnum.CHECK_SUBMIT.getCode().equals(gzglDto.getZt()) && gzgl.getFzr().equals(user.getYhid())){
			map.put("status","fail");
			if (StringUtil.isBlank(failMessage)){
				failMessage += gzgl.getYwmc() +"　领取失败！确认人员不得与负责人一致！";
			} else {
				failMessage += "<br/>" +gzgl.getYwmc() +"　领取失败！确认人员不得与负责人一致！";
			}
			map.put("message",failMessage);
			return map;
		}
		boolean isInProgress;//true:当前为任务准备、任务分配、任务进行中 状态；false:当前为任务确认、任务测试 状态
		String jdxh = gzgl.getJdxh();
		String jdmc = gzgl.getJdmc();
		String[] inProgressXh = {"1","2"};
		String[] inProgressMc = {"准备","分配","进行"};
		String[] inConfirmingXh = {"3","4"};
		String[] inConfirmingMc = {"确认","测试"};
		//判断jdxh是否在inProgress中，如果在，则任务进行中状态，否则为确认中状态
		if(Arrays.asList(inProgressXh).contains(jdxh) || Arrays.stream(inProgressMc).anyMatch(jdmc::contains)){
			//当前为任务准备、任务分配、任务进行中 状态，采用任务转发逻辑
			isInProgress = true;
		} else if (Arrays.asList(inConfirmingXh).contains(jdxh) || Arrays.stream(inConfirmingMc).anyMatch(jdmc::contains)) {
			//当前为任务确认、任务测试 状态，采用确认转发逻辑
			isInProgress = false;
		} else {
			if (StringUtil.isBlank(failMessage)){
				failMessage += gzgl.getYwmc() +"　领取失败！任务阶段存在问题，请确认！";
			} else {
				failMessage += "<br/>" + gzgl.getYwmc() +"　领取失败！任务阶段存在问题，请确认！";
			}
			map.put("status", "fail");
			map.put("message", failMessage);
			map.put(RESULT, isSuccess);
			return map;
		}
		try {
			boolean claimSuccess = gzglService.claimTask(gzgl, isInProgress, user, request);
			if (claimSuccess){
				isSuccess = claimSuccess;
			} else {
				if (StringUtil.isBlank(failMessage)){
					failMessage += gzgl.getYwmc() +"　领取失败！";
				} else {
					failMessage += "<br/>" + gzgl.getYwmc() +"　领取失败！";
				}
			}
		} catch (BusinessException e) {
			if (StringUtil.isBlank(failMessage)){
				failMessage += gzgl.getYwmc() +"　领取失败！" + e.getMessage();
			} else {
				failMessage += "<br/>" + gzgl.getYwmc() +"　领取失败！" + e.getMessage();
			}
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(StringUtil.isNotBlank(failMessage)?"部分任务领取成功！"+failMessage:"领取成功!"):"领取失败!");
		map.put(RESULT, isSuccess);
		return map;
	}
}
