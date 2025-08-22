package com.matridx.igams.common.controller;

import com.matridx.igams.common.dao.entities.ShtxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.service.svcinterface.IShtxService;
import com.matridx.igams.common.service.svcinterface.IXtshService;
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
@RequestMapping("/systemmain")
public class SpyqController extends BaseController {
	@Autowired
	private IShtxService shtxService;
	@Autowired
	private IXtshService xtshService;
	
	@Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
	
	@Override
	public String getPrefix() {
		return urlPrefix;
	}

	/*
	 * 查看全部审批列表的一条信息
	 */
	@RequestMapping("/audit/viewAllSpyq")
	public ModelAndView showAllSpyq(ShtxDto shtxDto,HttpServletRequest httpServletRequest) {
		ModelAndView mav = new ModelAndView("systemmain/audit/spyq_view");
		//ywmc是中文，在请求参数中，感觉貌似不是很合理
		String ywmc = httpServletRequest.getParameter("ywmc");
		shtxDto = shtxService.getShyqxxByShid(shtxDto);
		shtxDto.setYwmc(ywmc);
		mav.addObject("shtxDto",shtxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/*
	 * 增加一个全部审批列表页面
	 */
	@RequestMapping("/audit/pageListAllSpyq")
	@ResponseBody
	public ModelAndView pageAllSpyqlist()
	{
		ModelAndView mav = new ModelAndView("systemmain/audit/spyq_list");
		List<ShtxDto> txlblist;
		txlblist = shtxService.getTxlbList();
//		mav.addObject("flag", "all");
		mav.addObject("txlblist",txlblist);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/*
	 * 数据库中取出全部审批列表数据
	 */
	@RequestMapping("/audit/pageGetListAllSpyq")
	@ResponseBody
	public Map<String,Object> pageGetListSpyqData(ShtxDto shtxDto) {
		Map<String,Object> map;
		map = shtxService.getPagedSpyq(shtxDto);
		map.put("total",shtxDto.getTotalNumber());
		return map;		
	}
	
	/*
	 * 测试定时任务
	 */
	@RequestMapping("/audit/spyqRemind")
	public boolean spyqRemind() {
		return shtxService.spyqRemind();
	}
	
//	======================个人============================
	/*
	 * 增加一个个人审批列表页面
	 */
	@RequestMapping("/audit/pageListOneSpyq")
	public ModelAndView pageGetListOneSpyqData()
	{
		ModelAndView mav = new ModelAndView("systemmain/audit/spyqPerson_list");
		List<ShtxDto> txlblist;
		txlblist = shtxService.getTxlbList();
//		mav.addObject("flag", "person");
		mav.addObject("txlblist",txlblist);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/*
	 * 数据库中取出个人审批列表数据
	 */
	@RequestMapping("/audit/pageGetListOneSpyq")
	@ResponseBody
	public Map<String,Object> pageGetListOneSpyq(ShtxDto shtxDto,HttpServletRequest httpServletRequest) {
		Map<String,Object> map;
		String txlbs = httpServletRequest.getParameter("txlbs");
		//获取用户信息
		User user = getLoginInfo(httpServletRequest);
		shtxDto.setDqshyh(user.getYhid());
		shtxDto.setDqshjs(user.getDqjs());
		map = shtxService.getPagedPersonSpyq(shtxDto,txlbs);
		map.put("total",shtxDto.getTotalNumber());
		return map;		
	}
	
	/*
	 * 查看个人审批列表的一条信息
	 */
	@RequestMapping("/audit/viewOneSpyq")
	public ModelAndView showOneSpyq(ShtxDto shtxDto,HttpServletRequest httpServletRequest) {
		ModelAndView mav = new ModelAndView("systemmain/audit/spyq_view");
        //ywmc是中文，在请求参数中，感觉貌似不是很合理
		String ywmc = httpServletRequest.getParameter("ywmc");
		shtxDto = shtxService.getShyqxxByShid(shtxDto);
		shtxDto.setYwmc(ywmc);
		mav.addObject("shtxDto",shtxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
//	=====================到货=============================
	/*
	 * 增加到货审批列表页面
	 */
	@RequestMapping("/audit/pageListDaoHuoSpyq")
	public ModelAndView pageListDaoHuoSpyq(XtshDto xtshDto)
	{
		ModelAndView mav = new ModelAndView("systemmain/audit/spyqDaoHuo_list");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("xtshDto", xtshDto);
		return mav;
	}
	
	/*
	 * 数据库到货审批列表数据
	 */
	@RequestMapping("/audit/getDaoHuoSpyqData")
	@ResponseBody
	public Map<String,Object> getDaoHuoSpyqData(XtshDto xtshDto) {
		Map<String,Object> map = new HashMap<>();
		List<XtshDto> dhlist;
		//获取用户信息
//		User user = getLoginInfo(httpServletRequest);
		xtshDto.setShlb("AUDIT_GOODS_ARRIVAL");
		dhlist = xtshService.getPagedDaoHuoSpyq(xtshDto);
		map.put("total",xtshDto.getTotalNumber());
		map.put("rows", dhlist);
		return map;		
	}
	
	/*
	 * 查看个人审批列表的一条信息
	 */
	@RequestMapping("/audit/viewDaoHuoSpyq")
	public ModelAndView viewDaoHuoSpyq(XtshDto xtshDto) {
		ModelAndView mav = new ModelAndView("systemmain/audit/spyqDaoHuo_view");
		xtshDto.setShlb("AUDIT_GOODS_ARRIVAL");
		xtshDto =xtshService.getDHspyqByShid(xtshDto);
		mav.addObject("xtshDto",xtshDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

}








