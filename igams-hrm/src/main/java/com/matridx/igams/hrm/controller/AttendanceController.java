package com.matridx.igams.hrm.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.hrm.dao.entities.BcglDto;
import com.matridx.igams.hrm.dao.entities.BtglDto;
import com.matridx.igams.hrm.dao.entities.YhcqtzDto;
import com.matridx.igams.hrm.service.svcinterface.IBcglService;
import com.matridx.igams.hrm.service.svcinterface.IBtglService;
import com.matridx.igams.hrm.service.svcinterface.IKqqjxxService;
import com.matridx.igams.hrm.service.svcinterface.IYhcqtzService;
import com.matridx.igams.hrm.service.svcinterface.IYhkqxxService;
import com.matridx.igams.hrm.dao.entities.KqqjxxDto;
import com.matridx.igams.hrm.dao.entities.YhkqxxDto;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.service.svcinterface.IXxglService;
@Controller
@RequestMapping("/attendance")
public class AttendanceController extends BaseController{

	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	IYhcqtzService yhcqtzService;
	@Autowired
	IYhkqxxService yhkqxxService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ICommonDao commonDao;
	@Autowired
	IKqqjxxService kqqjxxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IBtglService btglService;
	@Autowired
	IBcglService bcglService;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}

	/**
	 * 用户考勤管理
	 */
	@RequestMapping(value="/attendance/pageListAttendance")
	public ModelAndView getAttendancePageList() {
		ModelAndView mav=new ModelAndView("attendance/attendance_list");
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}

	
	/**
	 * 用户出勤列表
	 */
	@RequestMapping(value="/attendance/pageGetListAttendance")
	@ResponseBody
	public Map<String,Object> getAttendancePageList(YhcqtzDto yhcqtzDto){
		List<YhcqtzDto> yhcqlist=yhcqtzService.getPagedDtoList(yhcqtzDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", yhcqtzDto.getTotalNumber());
		result.put("rows", yhcqlist);
		return result;
	}
	
	/**
	 * 用户出勤新增页面
	 */
	@RequestMapping(value="/attendance/addAttendance")
	@ResponseBody
	public ModelAndView addAttendance(YhcqtzDto yhcqtzDto) {
		ModelAndView mav=new ModelAndView("attendance/attendance_add");
		List<User> xtyhlist=commonDao.getUserList();
		yhcqtzDto.setFormAction("add");
		mav.addObject("YhcqtzDto", yhcqtzDto);
		mav.addObject("xtyhlist", xtyhlist);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	
	/**
	 * 用户出勤新增保存
	 */
	@RequestMapping(value="/attendance/addSaveAttendance")
	@ResponseBody
	public Map<String,Object> addSaveAttendance(YhcqtzDto yhcqtzDto){
		User user = getLoginInfo();
		yhcqtzDto.setLrry(user.getYhid());
		yhcqtzDto.setYyhid(yhcqtzDto.getYhid());
		YhcqtzDto checkyh=yhcqtzService.getCqxxByYhid(yhcqtzDto);
		Map<String,Object> map = new HashMap<>();
		//校验用户是否已经存在
		if(checkyh!=null) {
			map.put("status","fail");
			map.put("message","用户已存在!");
			return map;
		}
		boolean isSuccess = yhcqtzService.insertDto(yhcqtzDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 用户出勤修改页面
	 */
	@RequestMapping(value="/attendance/modAttendance")
	@ResponseBody
	public ModelAndView modAttendance(YhcqtzDto yhcqtzDto) {
		ModelAndView mav=new ModelAndView("attendance/attendance_add");
		List<User> xtyhlist=commonDao.getUserList();
		yhcqtzDto.setYhid(yhcqtzDto.getYyhid());
		YhcqtzDto yhcqxx=yhcqtzService.getCqxxByYhid(yhcqtzDto);
		yhcqtzDto.setSjid(yhcqxx.getSjid());
		yhcqtzDto.setBz(yhcqxx.getBz());
		yhcqtzDto.setFormAction("mod");
		mav.addObject("YhcqtzDto", yhcqtzDto);
		mav.addObject("xtyhlist", xtyhlist);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	
	/**
	 * 用户出勤修改保存
	 */
	@RequestMapping(value="/attendance/modSaveAttendance")
	@ResponseBody
	public Map<String,Object> modSaveAttendance(YhcqtzDto yhcqtzDto){
		User user = getLoginInfo();
		yhcqtzDto.setXgry(user.getYhid());
		Map<String,Object> map = new HashMap<>();
		Map<String,Object> resmap=yhcqtzService.updateYhcqtz(yhcqtzDto);
		if((boolean) resmap.get("checkresult")) {
			map.put("status","fail");
			map.put("message",resmap.get("msg"));
			return map;
		}
		boolean isSuccess=(boolean) resmap.get("result");
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 用户出勤信息查看
	 */
	@RequestMapping(value="/attendance/viewAttendance")
	public ModelAndView viewAttendance(YhcqtzDto yhcqtzDto) {
		ModelAndView mav=new ModelAndView("attendance/attendance_view");
		yhcqtzDto.setYyhid(yhcqtzDto.getYhid());
		YhcqtzDto yhcqxx=yhcqtzService.getCqxxByYhid(yhcqtzDto);
		mav.addObject("YhcqtzDto", yhcqxx);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}

	/**
	 * 删除用户出勤信息

	 */
	@RequestMapping(value="/attendance/delAttendance")
	@ResponseBody
	public Map<String,Object> delAttendance(YhcqtzDto yhcqtzDto){
		boolean isSuccess = yhcqtzService.delYhcqtzxx(yhcqtzDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 更新考勤按钮
	 */
	@RequestMapping(value ="/attendance/updatecheckPersonal")
	public ModelAndView updateCheck(YhkqxxDto yhkqxxDto){
		ModelAndView mav = new ModelAndView("attendance/updatecheck_edit");
		YhkqxxDto yhkqxxDto_t=new YhkqxxDto();
		if(!yhkqxxDto.getKqid().equals("null")){
			yhkqxxDto_t=yhkqxxService.getKqxxByKqid(yhkqxxDto);
		}else{
			yhkqxxDto_t.setKqid(yhkqxxDto.getKqid());
		}
		List<User> xtyhlist=commonDao.getUserList();
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("xtyhlist", xtyhlist);
		mav.addObject("yhkqxx", yhkqxxDto_t);
		return mav;
	}

	/**
	 * 用户考勤管理
	 */
	@RequestMapping(value="/attendance/pageListAttendanceNews")
	public ModelAndView pageListAttendanceNews() {
		ModelAndView mav=new ModelAndView("attendance/yhkqxx_list");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 考勤信息列表
	 */
	@RequestMapping(value="/attendance/pageGetListAttendanceNews")
	@ResponseBody
	public Map<String,Object> getAttendanceNewsList(YhkqxxDto yhkqxxDto){
		List<YhkqxxDto> kqxxlist=yhkqxxService.getPagedDtoList(yhkqxxDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", yhkqxxDto.getTotalNumber());
		result.put("rows", kqxxlist);
		return result;
	}

	/**
	 * 考勤信息查看
	 */
	@RequestMapping(value="/attendance/viewKqxx")
	public ModelAndView viewKqxx(YhkqxxDto yhkqxxDto) {
		ModelAndView mav=new ModelAndView("attendance/yhkqxx_view");
		YhkqxxDto kqxx=yhkqxxService.getKqxxByKqid(yhkqxxDto);
		if(kqxx.getGzsc()!=null&& !Objects.equals(kqxx.getGzsc(), "")){
			kqxx.setGzsc(kqxx.getGzsc()+"小时");
		}
		if(kqxx.getSc()!=null&& !Objects.equals(kqxx.getSc(), "")){
			kqxx.setSc(kqxx.getSc()+"小时");
		}
		mav.addObject("yhkqxxDto", kqxx);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 考勤信息新增页面
	 */
	@RequestMapping(value="/attendance/addKqxx")
	@ResponseBody
	public ModelAndView addKqxx(YhkqxxDto yhkqxxDto) {
		ModelAndView mav=new ModelAndView("attendance/yhkqxx_add");
		List<User> xtyhlist=commonDao.getUserList();
		yhkqxxDto.setFormAction("add");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.HOLIDAY_TYPE,BasicDataTypeEnum.ATTENDANCE_STATUS});
		mav.addObject("holidaylist", jclist.get(BasicDataTypeEnum.HOLIDAY_TYPE.getCode()));
		mav.addObject("status", jclist.get(BasicDataTypeEnum.ATTENDANCE_STATUS.getCode()));
		mav.addObject("yhkqxxDto", yhkqxxDto);
		mav.addObject("xtyhlist", xtyhlist);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 删除用户出勤信息
	 */
	@RequestMapping(value="/attendance/delKqxx")
	@ResponseBody
	public Map<String,Object> delKqxx(YhkqxxDto yhkqxxDto){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		yhkqxxDto.setScry(user.getYhid());
		List<String> kqids=yhkqxxDto.getIds();
		for(String kqid:kqids){
			YhkqxxDto dtobyKqid = yhkqxxService.getByKqid(kqid);
			KqqjxxDto kqqjxxDto=new KqqjxxDto();
			kqqjxxDto.setYhid(dtobyKqid.getYhid());
			kqqjxxDto.setRq(dtobyKqid.getRq());
			boolean isSuccess =kqqjxxService.delQjxx(kqqjxxDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		}
		boolean isSuccess = yhkqxxService.delKqxx(yhkqxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}


	/**
	 * 考勤信息新增保存
	 */
	@RequestMapping(value="/attendance/addSaveKqxx")
	@ResponseBody
	public Map<String,Object> addSaveKqxx(YhkqxxDto yhkqxxDto){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		yhkqxxDto.setLrry(user.getYhid());
		yhkqxxDto.setRq(yhkqxxDto.getDkrq());
		if(StringUtil.isNotBlank(yhkqxxDto.getGzsc())){
			yhkqxxDto.setGzsc(yhkqxxDto.getGzsc().replace("小时",""));
		}
		if(StringUtil.isNotBlank(yhkqxxDto.getSc())){
			yhkqxxDto.setSc(yhkqxxDto.getSc().replace("小时",""));
		}
		YhkqxxDto kqxxByKqid = yhkqxxService.getKqxxByKqid(yhkqxxService.getKqid(yhkqxxDto));
		if(kqxxByKqid!=null) {
			map.put("status","fail");
			map.put("message","该用户这一天已有记录!");
			return map;
		}
		if(StringUtil.isNotBlank(yhkqxxDto.getQjlx())){
			KqqjxxDto kqqjxxDto=new KqqjxxDto();
			kqqjxxDto.setYhid(yhkqxxDto.getYhid());
			kqqjxxDto.setRq(yhkqxxDto.getDkrq());
			kqqjxxDto.setQjlx(yhkqxxDto.getQjlx());
			kqqjxxDto.setQjkssj(yhkqxxDto.getQjkssj());
			kqqjxxDto.setQjjssj(yhkqxxDto.getQjjssj());
			kqqjxxDto.setQjsc(yhkqxxDto.getQjsc());
			kqqjxxDto.setSpid(StringUtil.generateUUID());
			boolean isSuccess = kqqjxxService.insert(kqqjxxDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}
		yhkqxxDto.setKqid(StringUtil.generateUUID());
		boolean isSuccess = yhkqxxService.insert(yhkqxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 考勤信息修改页面
	 */
	@RequestMapping(value="/attendance/modKqxx")
	@ResponseBody
	public ModelAndView modKqxx(YhkqxxDto yhkqxxDto) {
		ModelAndView mav=new ModelAndView("attendance/yhkqxx_add");
		List<User> xtyhlist=commonDao.getUserList();
		YhkqxxDto kqxxByKqid = yhkqxxService.getKqxxByKqid(yhkqxxDto);
		if(kqxxByKqid.getGzsc()!=null&& !Objects.equals(kqxxByKqid.getGzsc(), "")){
			kqxxByKqid.setGzsc(kqxxByKqid.getGzsc()+"小时");
		}
		if(kqxxByKqid.getSc()!=null&& !Objects.equals(kqxxByKqid.getSc(), "")){
			kqxxByKqid.setSc(kqxxByKqid.getSc()+"小时");
		}
		kqxxByKqid.setFormAction("mod");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.HOLIDAY_TYPE,BasicDataTypeEnum.ATTENDANCE_STATUS});
		mav.addObject("holidaylist", jclist.get(BasicDataTypeEnum.HOLIDAY_TYPE.getCode()));
		mav.addObject("status", jclist.get(BasicDataTypeEnum.ATTENDANCE_STATUS.getCode()));
		mav.addObject("yhkqxxDto", kqxxByKqid);
		mav.addObject("xtyhlist", xtyhlist);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 用户出勤修改保存
	 */
	@RequestMapping(value="/attendance/modSaveKqxx")
	@ResponseBody
	public Map<String,Object> modSaveKqxx(YhkqxxDto yhkqxxDto){
		User user = getLoginInfo();
		yhkqxxDto.setXgry(user.getYhid());
		yhkqxxDto.setYhid(yhkqxxDto.getYyhid());
		if(StringUtil.isNotBlank(yhkqxxDto.getGzsc())){
			yhkqxxDto.setGzsc(yhkqxxDto.getGzsc().replace("小时",""));
		}
		if(StringUtil.isNotBlank(yhkqxxDto.getSc())){
			yhkqxxDto.setSc(yhkqxxDto.getSc().replace("小时",""));
		}
		Map<String,Object> map = new HashMap<>();
		if(StringUtil.isNotBlank(yhkqxxDto.getQjlx())){
			KqqjxxDto kqqjxxDto=new KqqjxxDto();
			kqqjxxDto.setYhid(yhkqxxDto.getYhid());
			kqqjxxDto.setRq(yhkqxxDto.getDkrq());
			kqqjxxDto.setQjlx(yhkqxxDto.getQjlx());
			kqqjxxDto.setQjkssj(yhkqxxDto.getQjkssj());
			kqqjxxDto.setQjjssj(yhkqxxDto.getQjjssj());
			kqqjxxDto.setQjsc(yhkqxxDto.getQjsc());
			kqqjxxDto.setSpid(yhkqxxDto.getSpid());
			boolean isSuccess = kqqjxxService.updateSj(kqqjxxDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}
		boolean isSuccess = yhkqxxService.updateSj(yhkqxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 点击类型展示页面
	 */
	@RequestMapping(value="/attendance/viewLeave")
	public ModelAndView viewLeave(KqqjxxDto kqqjxxDto) {
		ModelAndView mav=new ModelAndView("attendance/leaveNew_view");
		List<KqqjxxDto> kqqjxxDtos = kqqjxxService.viewLeaveNews(kqqjxxDto);
		mav.addObject("kqqjxxDtos",kqqjxxDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 点击类型展示页面
	 */
	@RequestMapping(value="/attendance/pagedataViewLeave")
	public ModelAndView pagedataViewLeave(KqqjxxDto kqqjxxDto) {
		return this.viewLeave(kqqjxxDto);
	}

	/**
	 * 用户考勤统计列表
	 */
	@RequestMapping(value="/attendance/pageListAttendanceStatis")
	public ModelAndView pageListAttendanceStatis(YhkqxxDto yhkqxxDto) {
		ModelAndView mav=new ModelAndView("attendance/attendanceStatis_list");
		yhkqxxDto.setRqstart(DateUtils.getCustomFomratCurrentDate("yyyy-MM")+"-01");
		yhkqxxDto.setRqend(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
		mav.addObject("yhkqxxDto",yhkqxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 用户考勤统计列表数据
	 */
	@RequestMapping(value="/attendance/pageGetListAttendanceStatis")
	@ResponseBody
	public Map<String,Object> pageGetListAttendanceStatis(YhkqxxDto yhkqxxDto){
		List<YhkqxxDto> yhkqxxDtos = yhkqxxService.getPagedStatis(yhkqxxDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", yhkqxxDto.getTotalNumber());
		result.put("rows", yhkqxxDtos);
		return result;
	}
	/**
	 * 日历页面
	 */
	@RequestMapping("/attendance/pagedataViewAttendanceInfo")
	public ModelAndView pagedataViewAttendanceInfo(YhkqxxDto yhkqxxDto) {
		ModelAndView mav=new ModelAndView("attendance/calendar_attendance");
		mav.addObject("yhkqxxDto",yhkqxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 获取个人用户考勤数据
	 */
	@RequestMapping(value="/attendance/pagedataKqxxByYh")
	@ResponseBody
	public Map<String,Object> pagedataKqxxByYh(YhkqxxDto yhkqxxDto){
		List<YhkqxxDto> yhkqxxDtos = yhkqxxService.getDtoListByYh(yhkqxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("yhkqxxDtos",yhkqxxDtos);
		return map;
	}
	/**
	 * 用户考勤统计查看
	 */
	@RequestMapping("/attendance/viewAttendanceStatis")
	public ModelAndView viewAttendanceStatis(YhkqxxDto yhkqxxDto) {
		ModelAndView mav=new ModelAndView("attendance/attendanceStatis_view");
		mav.addObject("yhid", yhkqxxDto.getYhid());
		mav.addObject("rqstart", yhkqxxDto.getRqstart());
		mav.addObject("rqend", yhkqxxDto.getRqend());
		yhkqxxDto = yhkqxxService.getDtoByYhAndRq(yhkqxxDto);
		mav.addObject("yhkqxxDto", yhkqxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 用户考勤补贴信息
	 */
	@RequestMapping(value="/attendance/pagedataGetSubsidy")
	@ResponseBody
	public Map<String,Object> pagedataGetSubsidy(YhkqxxDto yhkqxxDto){
		List<YhkqxxDto> yhkqxxDtos = yhkqxxService.getPagedSubsidy(yhkqxxDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", yhkqxxDto.getTotalNumber());
		result.put("rows", yhkqxxDtos);
		return result;
	}

	/**
	 *补贴管理列表
	 */
	@RequestMapping("/attendance/pageListSubsidys")
	public ModelAndView pageListSubsidys() {
		ModelAndView mav=new ModelAndView("attendance/subsidys_list");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 补贴管理列表数据
	 */
	@RequestMapping(value="/attendance/pageGetListSubsidys")
	@ResponseBody
	public Map<String,Object> pageGetListSubsidys(BtglDto btglDto){
		Map<String,Object> map = new HashMap<>();
		List<BtglDto> rows= btglService.getPagedDtoList(btglDto);
		map.put("rows",rows);
		map.put("total",btglDto.getTotalNumber());
		return map;
	}
	/**
	 *补贴管理列表查看
	 */
	@RequestMapping("/attendance/viewSubsidys")
	public ModelAndView viewSubsidys(BtglDto btglDto) {
		ModelAndView mav=new ModelAndView("attendance/subsidys_view");
		btglDto=btglService.getDtoById(btglDto.getBtglid());
		mav.addObject("btglDto",btglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 *补贴管理列表新增
	 */
	@RequestMapping("/attendance/addSubsidys")
	public ModelAndView addSubsidys(BtglDto btglDto) {
		ModelAndView mav=new ModelAndView("attendance/subsidys_add");
		if (StringUtil.isNotBlank(btglDto.getBtglid())){
			btglDto=btglService.getDtoById(btglDto.getBtglid());
			btglDto.setFormAction("modSaveSubsidys");
		}else {
			btglDto.setFormAction("addSaveSubsidys");
		}
		mav.addObject("btglDto",btglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 *补贴管理列表修改
	 */
	@RequestMapping("/attendance/modSubsidys")
	public ModelAndView modSubsidys(BtglDto btglDto) {
		return addSubsidys(btglDto);
	}
	/**
	 * 补贴管理列表新增修改保存
	 */
	@RequestMapping(value="/attendance/addSaveSubsidys")
	@ResponseBody
	public Map<String,Object> addSaveSubsidys(BtglDto btglDto){
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo();
		boolean isSuccess= btglService.addSaveSubsidys(btglDto,user);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 补贴管理列表新增修改保存
	 */
	@RequestMapping(value="/attendance/modSaveSubsidys")
	@ResponseBody
	public Map<String,Object> modSaveSubsidys(BtglDto btglDto){
		return addSaveSubsidys(btglDto);
	}
	/**
	 *补贴管理列表删除
	 */
	@RequestMapping("/attendance/delSubsidys")
	@ResponseBody
	public Map<String,Object> delSubsidys(BtglDto btglDto) {
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo();
		btglDto.setScry(user.getLrry());
		boolean isSuccess= btglService.delete(btglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	/**
	 *班次列表
	 */
	@RequestMapping("/attendance/pageListScheduling")
	public ModelAndView pageListScheduling() {
		ModelAndView mav=new ModelAndView("attendance/scheduling_list");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 班次列表数据
	 */
	@RequestMapping(value="/attendance/pageGetListScheduling")
	@ResponseBody
	public Map<String,Object> pageGetListScheduling(BcglDto bcglDto){
		Map<String,Object> map = new HashMap<>();
		List<BcglDto> rows= bcglService.getPagedDtoList(bcglDto);
		map.put("rows",rows);
		map.put("total",bcglDto.getTotalNumber());
		return map;
	}
	/**
	 *班次列表查看
	 */
	@RequestMapping("/attendance/viewScheduling")
	public ModelAndView viewScheduling(BcglDto bcglDto) {
		ModelAndView mav=new ModelAndView("attendance/scheduling_view");
		bcglDto=bcglService.getDtoById(bcglDto.getBcglid());
		mav.addObject("bcglDto",bcglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 班次同步
	 */
	@RequestMapping(value="/attendance/synchronousScheduling")
	@ResponseBody
	public Map<String,Object> synchronousScheduling(){
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo();
		boolean isSuccess= bcglService.synchronousSavescheduling(user);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 补贴设置
	 */
	@RequestMapping("/attendance/subsidysetScheduling")
	public ModelAndView subsidysetScheduling(BcglDto bcglDto) {
		ModelAndView mav=new ModelAndView("attendance/scheduling_subsidyset");
		mav.addObject("bcglDto",bcglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 补贴数据
	 */
	@RequestMapping(value="/attendance/pagedataSubsidys")
	@ResponseBody
	public Map<String,Object> pagedataSubsidys(BtglDto btglDto){
		Map<String,Object> map = new HashMap<>();
		List<BtglDto> btglDtos=btglService.getPagedDtoList(btglDto);
		map.put("total",btglDto.getTotalNumber());
		map.put("rows", btglDtos);
		return map;
	}
	/**
	 * 补贴设置保存
	 */
	@RequestMapping(value="/attendance/subsidysetSaveScheduling")
	@ResponseBody
	public Map<String,Object> subsidysetSaveScheduling(BcglDto bcglDto){
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo();
		bcglDto.setXgry(user.getYhid());
		boolean isSuccess=bcglService.update(bcglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
