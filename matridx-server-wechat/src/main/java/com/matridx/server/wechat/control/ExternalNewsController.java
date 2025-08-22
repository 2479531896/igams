package com.matridx.server.wechat.control;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.server.wechat.dao.entities.WbzxDto;
import com.matridx.server.wechat.service.svcinterface.IWbzxService;

@Controller
@RequestMapping(value = "/news")
public class ExternalNewsController extends BaseController{

	@Autowired
	IWbzxService wbzxService;
	
	@Autowired
	IJcsjService jcsjService;
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	ICommonService commonService;
	
	
	/**
	 * 跳转微信资讯页面
	 * @return
	 */
	@RequestMapping(value = "/news/pageListWechatNews")
	public ModelAndView getWechatNewsPage() {
        return new ModelAndView("wechat/wechatnews/wechatnews_list");
	}
	
	/**
	 * 获取微信资讯列表
	 * @param wbzxDto
	 * @return
	 */
	@RequestMapping("/news/getWechatNewsList")
	@ResponseBody
	public Map<String,Object> getWechatNewsList(WbzxDto wbzxDto){
		Map<String,Object> map = new HashMap<>();
		List<WbzxDto> list = wbzxService.getPagedDtoList(wbzxDto);
		map.put("total", wbzxDto.getTotalNumber());
    	map.put("rows",  list);
    	return map;
	}
	
	/**
	 * 微信资讯新增界面
	 * @param wbzxDto
	 * @return
	 */
	@RequestMapping("/news/addWechatNews")
	public ModelAndView getaddWechatNewsPage(WbzxDto wbzxDto) {
		ModelAndView mav = new ModelAndView("wechat/wechatnews/wechatnews_add");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.NEWS_CLASS});
		wbzxDto.setFormAction("add");
		mav.addObject("zxlxlist", jclist.get(BasicDataTypeEnum.NEWS_CLASS.getCode()));
		mav.addObject("wbzxDto", wbzxDto);
		return mav;
	}
	
	/**
	 * 微信资讯新增保存
	 * @param wbzxDto
	 * @return
	 */
	@RequestMapping("/news/addSaveWechatNews")
	@ResponseBody
	public Map<String,Object> addSaveWechatNews(WbzxDto wbzxDto){
		User user=getLoginInfo();
		wbzxDto.setLrry(user.getYhid());
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=wbzxService.insertDto(wbzxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 微信资讯修改界面
	 * @param wbzxDto
	 * @return
	 */
	@RequestMapping("/news/modWechatNews")
	public ModelAndView getmodWechatNewsPage(WbzxDto wbzxDto) {
		ModelAndView mav = new ModelAndView("wechat/wechatnews/wechatnews_add");
		wbzxDto=wbzxService.getDto(wbzxDto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.NEWS_CLASS});
		wbzxDto.setFormAction("mod");
		mav.addObject("zxlxlist", jclist.get(BasicDataTypeEnum.NEWS_CLASS.getCode()));
		mav.addObject("wbzxDto", wbzxDto);
		return mav;
	}
	
	/**
	 * 微信资讯修改保存
	 * @param wbzxDto
	 * @return
	 */
	@RequestMapping("/news/modSaveWechatNews")
	@ResponseBody
	public Map<String,Object> modSaveWechatNews(WbzxDto wbzxDto){
		User user=getLoginInfo();
		wbzxDto.setXgry(user.getYhid());
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=wbzxService.update(wbzxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 删除微信资讯
	 * @param wbzxDto
	 * @return
	 */
	@RequestMapping("/news/delWechatNews")
	@ResponseBody
	public Map<String,Object> delWechatNews(WbzxDto wbzxDto){
		User user=getLoginInfo();
		wbzxDto.setScry(user.getYhid());
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=wbzxService.delete(wbzxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 查看微信资讯
	 * @param wbzxDto
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/news/viewWechatNews")
	@ResponseBody
	public ModelAndView viewWechatNews(WbzxDto wbzxDto) throws UnsupportedEncodingException {
		ModelAndView mav=new ModelAndView("wechat/wechatnews/wechatnews_view");
		wbzxDto=wbzxService.getDtoById(wbzxDto.getZxid());
		String zxlx=wbzxDto.getZxlx();
		String zxzlx=wbzxDto.getZxzlx();
		if(StringUtils.isNotBlank(zxlx)) {
            zxlx=URLEncoder.encode(commonService.getSign(wbzxDto.getZxlx()),"UTF-8");
        }
		if(StringUtils.isNotBlank(zxzlx)) {
            zxzlx=URLEncoder.encode(commonService.getSign(wbzxDto.getZxzlx()),"UTF-8");
        }
		wbzxDto.setZxqddz("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0878828416fa84fe&redirect_uri=https%3A%2F%2Fservice.matridx.com%2Fws%2Fnews%2FgetWechatNews&zxlx="+zxlx+"&zxzlx="+zxzlx+"&response_type=code&scope=snsapi_base&state=STATE＃wechat_redirect");
		mav.addObject("wbzxDto",wbzxDto);
		return mav;
	}
}
