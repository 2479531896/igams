package com.matridx.igams.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.BqglDto;
import com.matridx.igams.wechat.service.svcinterface.IBqglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wechat")
public class WeChatTagController extends BaseController {

	@Autowired
	IBqglService bqglSerice;
	
	@Autowired
	IXxglService xxglService;
	
	/**
	 * 标签列表明细
	 * @param bqglDto
	 * @return
	 */
	@RequestMapping("/tag/pagedataListTag")
	@ResponseBody
	public Map<String,Object> pageGetListTag(BqglDto bqglDto){
		List<BqglDto> t_List = bqglSerice.getPagedDtoList(bqglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", bqglDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 标签新增
	 * @param bqglDto
	 * @return
	 */
	@RequestMapping(value ="/tag/addTag")
	public ModelAndView addAudit(BqglDto bqglDto){
		ModelAndView mav = new ModelAndView("wechat/menu/tag_edit");
		bqglDto.setFormAction("add");
		mav.addObject("bqglDto", bqglDto);
		return mav;
	}
	
	/**
	 * 标签新增保存
	 * @param bqglDto
	 * @return
	 */
	@RequestMapping(value="/tag/addSaveTag")
	@ResponseBody
	public Map<String, Object> addSaveTag(BqglDto bqglDto){
		//获取用户信息
		User user = getLoginInfo();
		bqglDto.setLrry(user.getYhid());
		
		boolean isSuccess = bqglSerice.addSaveTag(bqglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 标签修改
	 * @param bqglDto
	 * @return
	 */
	@RequestMapping(value ="/tag/modTag")
	public ModelAndView modTag(BqglDto bqglDto){
		ModelAndView mav = new ModelAndView("wechat/menu/tag_edit");
		bqglDto = bqglSerice.getDtoById(bqglDto.getBqid());
		bqglDto.setFormAction("mod");
		mav.addObject("bqglDto", bqglDto);
		return mav;
	}
	
	/**
	 * 标签修改保存
	 * @param bqglDto
	 * @return
	 */
	@RequestMapping(value="/tag/modSaveTag")
	@ResponseBody
	public Map<String, Object> modSaveAudit(BqglDto bqglDto){
		//获取用户信息
		User user = getLoginInfo();
		bqglDto.setXgry(user.getYhid());
		boolean isSuccess = bqglSerice.modSaveTag(bqglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 删除标签
	 * @param bqglDto
	 * @return
	 */
	@RequestMapping(value="/tag/delTag")
	@ResponseBody
	public Map<String, Object> delTag(BqglDto bqglDto){
		User user = getLoginInfo();
		bqglDto.setScry(user.getYhid());
		bqglDto.setScbj("1");
		boolean isSuccess = bqglSerice.delTag(bqglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 获取已创建标签信息
	 * @param bqglDto
	 * @return
	 */
	@RequestMapping(value ="/tag/getTag")
	@ResponseBody
	public Map<String, Object> getTag(BqglDto bqglDto){
		//获取用户信息
		User user = getLoginInfo();
		bqglDto.setXgry(user.getYhid());
		boolean isSuccess = bqglSerice.getTag(bqglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

}
