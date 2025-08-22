package com.matridx.igams.experiment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.experiment.dao.entities.JdmbDto;
import com.matridx.igams.experiment.dao.entities.MbglDto;
import com.matridx.igams.experiment.dao.entities.RwmbDto;
import com.matridx.igams.experiment.service.svcinterface.IJdmbService;
import com.matridx.igams.experiment.service.svcinterface.IMbglService;
import com.matridx.igams.experiment.service.svcinterface.IRwmbService;

@Controller
@RequestMapping("/experiment")
public class TemplateManageController extends BaseController{
	
	@Autowired
	IMbglService mbglService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IJdmbService jdmbService;
	@Autowired
	IRwmbService rwmbService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IFjcfbService fjcfbService;
	
	/**
	 * 模板管理文件页面
	 */
	@RequestMapping(value ="/template/pagedataTemplateList")
	public ModelAndView templateList(MbglDto mbglDto){
		ModelAndView mav = new ModelAndView("experiment/template/template_list");
		//获取用户信息
		User user = getLoginInfo();
		mbglDto.setLrry(user.getYhid());
		List<MbglDto> mbgldtos=mbglService.getAllMb(mbglDto);
		mav.addObject("mbglDto", mbgldtos);
		return mav;
	}
	
	/**
	 * 新增模板
	 */
	@RequestMapping(value="/template/pagedataAddSaveTemplate")
	@ResponseBody
	public Map<String, Object> addSaveTemplate(MbglDto mbglDto){
		//获取用户信息
		User user = getLoginInfo();
		mbglDto.setLrry(user.getYhid());
		Map<String,Object> map = new HashMap<>();
		boolean mbmcexit=mbglService.selectMbidByMbmc(mbglDto);
		if(!mbmcexit) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOMM_MB00001").getXxnr());
			return map;
		}
		boolean isSuccess = mbglService.addSaveTemplate(mbglDto);
		map.put("mbglDto", mbglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 新增阶段模板
	 */
	@RequestMapping(value="/template/pagedataAddSaveStage")
	@ResponseBody
	public Map<String, Object> addSaveStage(JdmbDto jdmbDto){
		//获取用户信息
		User user = getLoginInfo();
		jdmbDto.setLrry(user.getYhid());
		boolean isSuccess = jdmbService.insertDto(jdmbDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 跳转阶段模板界面
	 */
	@RequestMapping(value="/template/pagedataTemplateListStage")
	@ResponseBody
	public ModelAndView addmodelSaveTask(MbglDto mbglDto) {
		List<JdmbDto> jdmblist=jdmbService.selectStageByID(mbglDto);
		ModelAndView mav=new ModelAndView("experiment/template/template_stage");
		mav.addObject("mbglDto", mbglDto);
		mav.addObject("jdmblist",jdmblist);
		return mav;
		
	}
	/**
	 * 新增模板任务界面
	 */
	@RequestMapping(value="/template/pagedataAddTemplateTask")
	@ResponseBody
	public ModelAndView addplan(RwmbDto rwmbDto) {
		ModelAndView mav=new ModelAndView("experiment/template/template_task");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.TASK_LABEL,BasicDataTypeEnum.TASK_RANK});
		rwmbDto.setFormAction("pagedataAddSaveTask");
		mav.addObject("task_label", jclist.get(BasicDataTypeEnum.TASK_LABEL.getCode()));
		mav.addObject("task_rank", jclist.get(BasicDataTypeEnum.TASK_RANK.getCode()));
		//获取模板文件类型
		rwmbDto.setYwlx(BusTypeEnum.IMP_TEMPLATE.getCode());
		mav.addObject("rwmbDto",rwmbDto);
		return mav;
	}
	
	/**
	 * 新增任务模板
	 */
	@RequestMapping(value="/template/pagedataAddSaveTask")
	@ResponseBody
	public Map<String, Object> addSaveTask(RwmbDto rwmbDto){
		//获取用户信息
		User user = getLoginInfo();
		rwmbDto.setLrry(user.getYhid());
		boolean isSuccess = rwmbService.addSaveTask(rwmbDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 删除模板
	 */
	@RequestMapping(value="/template/pagedataDelTemplate")
	@ResponseBody
	public Map<String, Object> delMod(MbglDto mbgldto) {
		User user = getLoginInfo();
		mbgldto.setScry(user.getYhid());
		boolean isSuccess = mbglService.delete(mbgldto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 跳转模板编辑界面
	 */
	@RequestMapping(value="/template/pagedataEditTemplateTask")
	public ModelAndView editTemplate(MbglDto mbgldto) {
		ModelAndView mav=new ModelAndView("experiment/template/template_templateEdit");
		mbgldto.setFormAction("pagedataSaveEditTemplate");
		MbglDto mbglxx=mbglService.getModById(mbgldto);
		mav.addObject("mbglaction", mbgldto);
		mav.addObject("mbgldto", mbglxx);
		return mav;
	}
	/**
	 * 编辑保存模板信息
	 */
	@RequestMapping(value="/template/pagedataSaveEditTemplate")
	@ResponseBody
	public Map<String,Object> SaveEditTemplte(MbglDto mbgldto){
		User user = getLoginInfo();
		mbgldto.setXgry(user.getYhid());
		boolean isSuccess=mbglService.update(mbgldto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 根据阶段id删除当前阶段
	 */
	@RequestMapping(value="/template/pagedataDeljd")
	@ResponseBody
	public Map<String,Object> deljd(JdmbDto jdmbDto){
		User user = getLoginInfo();
		jdmbDto.setScry(user.getYhid());
		boolean isSuccess=jdmbService.updateDto(jdmbDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status",isSuccess?"success":"fail"); 
		map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr()); 
		return map;
	}
	
	/**
	 * 修改阶段名称
	 */
	@RequestMapping(value="/template/updatejdm")
	@ResponseBody
	public Map<String,Object> updatejdm(JdmbDto jdmbDto){
		User user = getLoginInfo();
		jdmbDto.setXgry(user.getYhid());
		boolean isSuccess=jdmbService.updatejdmc(jdmbDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status",isSuccess?"success":"fail"); 
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr()); 
		return map;
	}
	
	/**
	 * 修改任务模板
	 */
	@RequestMapping(value="/template/pagedataModtasktemplate")
	@ResponseBody
	public ModelAndView  modtasktemplate(RwmbDto rwmbDto) {
		ModelAndView mav=new ModelAndView("experiment/template/template_task");
		RwmbDto rwmbDtos=rwmbService.getDtoById(rwmbDto.getRwmbid());
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.TASK_LABEL,BasicDataTypeEnum.TASK_RANK});
		rwmbDtos.setFormAction("pagedataUpdatetasktemplate");
		//获取模板文件类型
		rwmbDtos.setYwlx(BusTypeEnum.IMP_TEMPLATE.getCode());
		mav.addObject("rwmbDto",rwmbDtos);
		mav.addObject("task_label", jclist.get(BasicDataTypeEnum.TASK_LABEL.getCode()));
		mav.addObject("task_rank", jclist.get(BasicDataTypeEnum.TASK_RANK.getCode()));
		//根据任务模板ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(rwmbDto.getRwmbid());
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
	 * 修改任务模板保存
	 */
	@RequestMapping(value="/template/pagedataUpdatetasktemplate")
	@ResponseBody
	public Map<String,Object> updateTaskTemplate(RwmbDto rwmbDto){
		User user = getLoginInfo();
		rwmbDto.setXgry(user.getYhid());
		boolean isSuccess = rwmbService.updateTaskTemplate(rwmbDto);
		Map<String, Object> map= new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr()); 
		return map;
	}
	
	/**
	 * 删除当前任务
	 */
	@RequestMapping(value="/template/pagedataDeltasktemplate")
	@ResponseBody
	public Map<String,Object> deltasktemplate(RwmbDto rwmbDto){
		User user = getLoginInfo();
		rwmbDto.setScry(user.getYhid());
		boolean isSuccess = rwmbService.updatescbj(rwmbDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 修改页面
	 */
	@RequestMapping(value="/template/pagedataModstage")
	@ResponseBody
	public ModelAndView modstage(JdmbDto jdmbDto){
		ModelAndView mav=new ModelAndView("experiment/template/template_modstage");
		JdmbDto jdmbDtos=jdmbService.getDtoById(jdmbDto.getJdid());
		mav.addObject("jdmbDto",jdmbDtos);
		return mav;
	}
	
	/**
	 * 保存修改阶段模板
	 */
	@RequestMapping(value="/template/pagedataModAllstage")
	@ResponseBody
	public Map<String, Object> modAllstage(JdmbDto jdmbDto){
		User user = getLoginInfo();
		jdmbDto.setXgry(user.getYhid());
		boolean isSuccess=jdmbService.updatejdmc(jdmbDto);
		Map<String, Object> map= new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr()); 
		return map;
	}
	
	/**
	 * 阶段模板重新排序
	 */
	@RequestMapping(value="/template/pagedataStageSort")
	@ResponseBody
	public Map<String, Object> stageSort(JdmbDto jdmbDto){
		// 获取用户信息
		User user = getLoginInfo();
		jdmbDto.setXgry(user.getYhid());
		boolean isSuccess = jdmbService.stageSort(jdmbDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 任务模板重新排序
	 */
	@RequestMapping(value="/template/pagedataTaskSort")
	@ResponseBody
	public Map<String, Object> taskSort(RwmbDto rwmbDto){
		// 获取用户信息
		User user = getLoginInfo();
		rwmbDto.setXgry(user.getYhid());
		boolean isSuccess = rwmbService.taskSort(rwmbDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
