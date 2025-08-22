package com.matridx.server.wechat.control;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.server.wechat.dao.entities.CpwjDto;
import com.matridx.server.wechat.dao.entities.HtglDto;
import com.matridx.server.wechat.dao.entities.HtmxDto;
import com.matridx.server.wechat.dao.entities.QgglDto;
import com.matridx.server.wechat.dao.entities.QgmxDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.WbzxDto;
import com.matridx.server.wechat.service.svcinterface.ICpwjService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.server.wechat.service.svcinterface.IWbzxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/ws")
public class WechatWsController extends BaseController{

	@Autowired
	IWbzxService wbzxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ICommonService commonService;
	@Autowired
	IShgcService shgcService;
	@Autowired
	ISjxxService sjxxService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IShlcService shlcService;
	@Autowired
	IShxxService shxxService;
	@Autowired
	ICpwjService cpwjService;
	@Value("${matridx.wechat.companyurl:}")
	private String companyurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.wechat.oaurl:}")
	private String oaurl;
	
	private Logger log = LoggerFactory.getLogger(WechatWsController.class);
	
    @Override
	public String getPrefix(){
		return urlPrefix;
	}
	
	/**
	 * 获取微信资讯列表页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkIpToLab")
	public ModelAndView checkIpToLab(HttpServletRequest request) {
		
		ModelAndView mav=new ModelAndView("common/view/redirect");
		
		String ipString = request.getRemoteAddr();
		log.error(" checkIpToLab:" + ipString);
		if(ipString.startsWith("124.160.226.76") || ipString.startsWith("60.191.45.242")) {
			mav.addObject("url", "http://172.17.60.191");
		}else {
			mav.addObject("url", "https://medlab.matridx.com");
		}
		
		return mav;
	}


	/**
	 * 获取微信资讯列表页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/news/getWechatNews")
	public ModelAndView getWechatNews(HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("common/view/display_view");
		String zxlx = request.getParameter("zxlx");
		String zxzlx = request.getParameter("zxzlx");
		String view_url = "/ws/news/getWechatNewsList?zxlx="+zxlx+"&zxzlx="+zxzlx+"&start=0&count=10";
		mav.addObject("view_url", view_url);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 根据资讯类型和资讯子类型获取微信资讯列表
	 * @param wbzxDto
	 * @return
	 */
	@RequestMapping("/news/getWechatNewsList")
	@ResponseBody
	public ModelAndView getWxzxList(WbzxDto wbzxDto){
		List<JcsjDto> zxzlxlist= new ArrayList<>();
		//添加默认导航页(用于页面体现)
		JcsjDto mrjcsjDto=new JcsjDto();
		mrjcsjDto.setCsmc("全部");
		zxzlxlist.add(mrjcsjDto);
		JcsjDto zxlx=jcsjService.getDtoById(wbzxDto.getZxlx());
		if(zxlx!=null) {
			wbzxDto.setZxlxmc(zxlx.getCsmc());
			if(("null").equals(wbzxDto.getZxlx())) {
				wbzxDto.setZxlx(null);
			}else {
				JcsjDto jcsjDto=new JcsjDto();
				jcsjDto.setFcsid(wbzxDto.getZxlx());
				List<JcsjDto> zlxlist=jcsjService.getJcsjDtoList(jcsjDto);
				zxzlxlist.addAll(zlxlist);
			}
			if(("null").equals(wbzxDto.getZxzlx())) {
				wbzxDto.setZxzlx(null);
			}
		}
		ModelAndView mav=new ModelAndView("wechat/wechatnews/wechatnews_ws_list");
		List<WbzxDto> list=wbzxService.getWsWechatNewsList(wbzxDto);
		mav.addObject("wbzxlist", list);
		mav.addObject("wbzxDto", wbzxDto);
		mav.addObject("zxzlxlist", zxzlxlist);
		mav.addObject("start", list.size());
		return mav;
	}
	
	/**
	 * 下拉查询微信资讯信息
	 * @param wbzxDto
	 * @return
	 */
	@RequestMapping("/news/getMoreWechatNews")
	@ResponseBody
	public Map<String,Object> getMoreWechatNews(WbzxDto wbzxDto){
		Map<String,Object> map= new HashMap<>();
		List<WbzxDto> list=wbzxService.getWsWechatNewsList(wbzxDto);
		map.put("list", list);
		map.put("count",list.size());
		return map;
	}
	
	/**
	 * 显示审核页面
	 * @return
	 */
	@RequestMapping(value ="/auditProcess/audit")
	public ModelAndView audit(ShgcDto shgcDto){
		ModelAndView mav = new ModelAndView("globalweb/systemcheck/audit_process");
		// 获取当前审核过程
		ShgcDto t_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwid());
		if(t_shgcDto == null) {
            return mav;
        }
		//审核业务页面
		t_shgcDto.setBusiness_url(shgcDto.getBusiness_url());
		t_shgcDto.setYwzd(shgcDto.getYwzd());
		// 获取的审核流程列表
		ShlcDto shlcParam = new ShlcDto();
		shlcParam.setShid(t_shgcDto.getShid());
		shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
		List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
		// 退回列表
		List<ShlcDto> backShlcList = new ArrayList<>();
		ShlcDto startLc = new ShlcDto();
		startLc.setGwmc("用户");
		startLc.setLcxh("-1");
		backShlcList.add(startLc);
		for (ShlcDto shlc : shlcList) {
			// 当流程序号小于现流程序号则放入退回列表
			if (t_shgcDto.getXlcxh() != null
					&& Integer.parseInt(shlc.getLcxh()) < Integer.parseInt(t_shgcDto.getXlcxh())) {
				shlc.setAudited(true);
				backShlcList.add(shlc);
			} else if (shlc.getLcxh().equals(t_shgcDto.getXlcxh())) {// 相等则是当前流程
				// 当前流程做标记
				shlc.setCurrent(true);// 当前流程
				shlc.setAudited(true); //已审核流程
				// 最后一步标志
				if (t_shgcDto.getXlcxh() != null
						&& shlcList.get(shlcList.size() - 1).getLcxh().equals(t_shgcDto.getXlcxh())) {
					t_shgcDto.setLastStep(true);
				}
				//判断如果流程类别不为空，则代表需要显示特殊页面
				if(StringUtil.isNotBlank(shlc.getLclb())) {
					JcsjDto jcsjDto = jcsjService.getDtoById(shlc.getLclb());
					//审核业务页面
					t_shgcDto.setBusiness_url(urlPrefix+jcsjDto.getCskz1());
				}
				break;// 跳出for循环
			}
		}
		ShxxDto shxxParam = new ShxxDto();
		shxxParam.setShlb(shgcDto.getShlb());
		shxxParam.setYwid(shgcDto.getYwid());
		List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
		mav.addObject("shgcDto", t_shgcDto);
		mav.addObject("backShlcList", backShlcList);
		mav.addObject("shlcList", shlcList);
		mav.addObject("shxxList", shxxList);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}	
	
	/**
	 * 8085端查看请购信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/external/getRequestUrl")
	@ResponseBody
	public ModelAndView getRequestUrl(HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("wechat/purchase/purchase_auditView");
		String qgid=request.getParameter("qgid");
		String urlPrefix=request.getParameter("urlPrefix");
//		String url=request.getParameter("url");//request.getParameter("url")赋值冗余，故注释，2023/10/26
		String url;
		DBEncrypt crypt = new DBEncrypt();
	    url=crypt.dCode(oaurl);
//		url = "http://172.17.52.146:8086";////////////////////
		RestTemplate restTemplate = new RestTemplate();
		log.error(" getRequestUrl:" + url+urlPrefix+"/ws/production/purchase/getPurchaseForWechat?qgid="+qgid+"&zt=80");
		@SuppressWarnings("unchecked")
		//zt字段必须是80，为了过滤掉取消请购和拒绝审批的物料，同时状态不为80的不计入金额计算
		Map<String,Object> map=restTemplate.getForObject(url+urlPrefix+"/ws/production/purchase/getPurchaseForWechat?qgid="+qgid+"&zt=80",Map.class, "");
		QgglDto qgglDto = JSON.parseObject(JSON.toJSONString(map.get("qgglDto")), QgglDto.class);
		List<QgmxDto> t_list= new ArrayList<>();
		
		BigDecimal zjg=new BigDecimal("0");
		@SuppressWarnings("unchecked")
		List<Object> list = JSON.parseObject(JSON.toJSONString(map.get("qgmxlist")), List.class);
		DecimalFormat df4 = new DecimalFormat("#,##0.00");
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				String jg=JSON.parseObject(JSON.toJSONString(list.get(i)), QgmxDto.class).getJg();
				String sl=JSON.parseObject(JSON.toJSONString(list.get(i)), QgmxDto.class).getSl();
				if(StringUtils.isNotBlank(jg) && StringUtils.isNotBlank(sl)) {
					zjg=zjg.add(new BigDecimal(jg).multiply(new BigDecimal(sl)));
					JSON.parseObject(JSON.toJSONString(list.get(i)), QgmxDto.class).setJg(df4.format(new BigDecimal(jg)));
				}
				t_list.add(JSON.parseObject(JSON.toJSONString(list.get(i)), QgmxDto.class));
			}
		}
		zjg=zjg.setScale(2, BigDecimal.ROUND_UP);
		String t_zjg=df4.format(zjg);//千位符分隔
		if(qgglDto==null) {
			qgglDto=new QgglDto();
		}
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("qgmxlist", t_list);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("url",url);
		mav.addObject("zjg", t_zjg);
		
		List<FjcfbDto> fjcfbDtos= new ArrayList<>();
		List<FjcfbDto> fjcfblist = JSON.parseObject(JSON.toJSONString(map.get("t_fjcfbDtos")), List.class);
		if(fjcfblist!=null && fjcfblist.size()>0) {
			for(int i=0;i<fjcfblist.size();i++) {
				fjcfbDtos.add(JSON.parseObject(JSON.toJSONString(fjcfblist.get(i)), FjcfbDto.class));
			}
			String wjm=fjcfbDtos.get(0).getWjm();
			String fjid=fjcfbDtos.get(0).getFjid();
			mav.addObject("wjm", wjm);
			try {
				String sign = URLEncoder.encode(commonService.getSign(wjm),"UTF-8");
				mav.addObject("sign", sign);
				sign = URLEncoder.encode(commonService.getSign(fjid),"UTF-8");
				mav.addObject("wssign", sign);
				mav.addObject("t_fjcfbDtos", fjcfbDtos);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//下面部分为审核历史页面需要的数据
		List<ShlcDto> shlcList= new ArrayList<>();
		List<Object> shlclist = JSON.parseObject(JSON.toJSONString(map.get("shlcList")),List.class);
		ShgcDto shgcDto = JSON.parseObject(JSON.toJSONString(map.get("shgcDto")), ShgcDto.class);
		if(shlclist!=null && shlclist.size()>0) {
			for(int i=0;i<shlclist.size();i++) {
				ShlcDto shlc=JSON.parseObject(JSON.toJSONString(shlclist.get(i)), ShlcDto.class);
				shlcList.add(shlc);
			}
		}
		
		List<ShxxDto> shxxList= new ArrayList<>();
		List<Object> shxxlist = JSON.parseObject(JSON.toJSONString(map.get("shxxList")),List.class);
		if(shxxlist!=null && shxxlist.size()>0) {
			for(int i=0;i<shxxlist.size();i++) {
				ShxxDto shxx=JSON.parseObject(JSON.toJSONString(shxxlist.get(i)), ShxxDto.class);
				shxxList.add(shxx);
			}
		}
		mav.addObject("shgcDto", shgcDto);
        mav.addObject("shlcList", shlcList);
        mav.addObject("shxxList", shxxList);		
		return mav;
	}
	
	
	/**
	 * 查看请购明细详细信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/external/getMorePurchase")
	@ResponseBody
	public Map<String,Object> getDetailedPurchase(HttpServletRequest request){
//		Map<String,Object> t_map=new HashMap<String, Object>();//'t_map' 的内容已更新，但从未被使用，故注释，2023/10/26
		String qgmxid=request.getParameter("qgmxid");
		String urlPrefix=request.getParameter("urlPrefix");
		String url=request.getParameter("url");
		RestTemplate restTemplate = new RestTemplate();
		@SuppressWarnings("unchecked")
		Map<String,Object> map=restTemplate.getForObject(url+urlPrefix+"/ws/production/viewMorePurchase?qgmxid="+qgmxid,Map.class, "");
		//'t_map' 的内容已更新，但从未被使用，故注释，2023/10/26
//		QgmxDto qgmxDto = JSON.parseObject(JSON.toJSONString(map.get("QgmxDto")), QgmxDto.class);
//		List<FjcfbDto> fjcfbDtos=new ArrayList<FjcfbDto>();
//		List<FjcfbDto> fjcfblist = JSON.parseObject(JSON.toJSONString(map.get("mx_fjcfbDtos")), List.class);
//		if(fjcfblist!=null && fjcfblist.size()>0) {
//			for(int i=0;i<fjcfblist.size();i++) {
//				fjcfbDtos.add(JSON.parseObject(JSON.toJSONString(fjcfblist.get(i)), FjcfbDto.class));
//			}
//			String wjm=fjcfbDtos.get(0).getWjm();
//			String fjid=fjcfbDtos.get(0).getFjid();
//			t_map.put("wjm", wjm);
//			try {
//				String sign = URLEncoder.encode(commonService.getSign(wjm),"UTF-8");
//				t_map.put("sign", sign);
//				sign = URLEncoder.encode(commonService.getSign(fjid),"UTF-8");
//				t_map.put("wssign", sign);
//				t_map.put("t_fjcfbDtos", fjcfbDtos);
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		t_map.put("QgmxDto", qgmxDto);
		return map;
	}
	
	/**
	 * 8085查看合同信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/external/getContractUrl")
	@ResponseBody
	public ModelAndView getContractUrl(HttpServletRequest request) {
		
		String htid=request.getParameter("htid");
		String urlPrefix=request.getParameter("urlPrefix");
//		String url=request.getParameter("url");//request.getParameter("url")赋值冗余，故注释，2023/10/26
		String url;
		RestTemplate restTemplate = new RestTemplate();
		String ipString = request.getRemoteAddr();
        log.error(" checkIpToLab:" + ipString);
		/*if(ipString.startsWith("101.69.228.218")) {
			url = "http://172.17.60.191";
		}else {
			url = "https://medlab.matridx.com";
		}*/
        DBEncrypt crypt = new DBEncrypt();
        url=crypt.dCode(oaurl);
		log.error(" getContractUrl:" + url+urlPrefix+"/ws/production/contract/getContractForwechat?htid="+htid);
		@SuppressWarnings("unchecked")
		Map<String,Object> map=restTemplate.getForObject(url+urlPrefix+"/ws/production/contract/getContractForwechat?htid="+htid,Map.class, "");
		log.error(" getContractUrl--result:" + map==null? "-1":String.valueOf(map.size()));
		HtglDto htglDto = JSON.parseObject(JSON.toJSONString(map.get("htglDto")), HtglDto.class);
        List<HtmxDto> t_list= new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<Object> list = JSON.parseObject(JSON.toJSONString(map.get("htmxlist")), List.class);
        if(list!=null && list.size()>0) {
            for(int i=0;i<list.size();i++) {
                t_list.add(JSON.parseObject(JSON.toJSONString(list.get(i)), HtmxDto.class));
            }
        }
		List<FjcfbDto> fjcfbDtos= new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<FjcfbDto> fjcfblist = JSON.parseObject(JSON.toJSONString(map.get("t_fjcfbDtos")), List.class);
		if(fjcfblist!=null && fjcfblist.size()==1) {
			for(int i=0;i<fjcfblist.size();i++) {
				fjcfbDtos.add(JSON.parseObject(JSON.toJSONString(fjcfblist.get(i)), FjcfbDto.class));
			}
			String wjm=fjcfbDtos.get(0).getWjm();
			String fjid=fjcfbDtos.get(0).getFjid();
			ModelAndView mav=new ModelAndView("common/view/display_view");
			mav.addObject("urlPrefix", "");
			int begin=wjm.indexOf(".");
			int end=wjm.length();
			String type=wjm.substring(begin,end);
			String View_url="/ws/external/viewFilePage?url="+url+"&fjid="+ fjid+"&urlPrefix=" + urlPrefix + "&fileType=" + type;
			String newUrl;
			try {
				newUrl = StringUtil.urlEncodeToString(View_url);
				mav.addObject("view_url", newUrl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mav;
		}
		if(fjcfblist!=null && fjcfblist.size()>1) {
			ModelAndView mav=new ModelAndView("wechat/purchase/contract_dingtalkView");
			for(int i=0;i<fjcfblist.size();i++) {
				fjcfbDtos.add(JSON.parseObject(JSON.toJSONString(fjcfblist.get(i)), FjcfbDto.class));
			}
			String wjm=fjcfbDtos.get(0).getWjm();
			String fjid=fjcfbDtos.get(0).getFjid();
			mav.addObject("wjm", wjm);
			try {
				String sign = URLEncoder.encode(commonService.getSign(wjm),"UTF-8");
				mav.addObject("sign", sign);
				sign = URLEncoder.encode(commonService.getSign(fjid),"UTF-8");
				mav.addObject("wssign", sign);
				mav.addObject("t_fjcfbDtos", fjcfbDtos);
				mav.addObject("urlPrefix", urlPrefix);
				mav.addObject("url",url);
		        mav.addObject("htglDto", htglDto);
		        mav.addObject("htmxlist", t_list);
				return mav;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
		
		
	}
	
	/**
	 * 钉钉审批查看8086端附件 
	 * @param request
	 * @return
	 */
	@RequestMapping("/external/viewFilePage")
	public ModelAndView viewFilePage(HttpServletRequest request) {
		/*
		 * 跨域访问，暂时废弃。但现有速度有点慢
		String ipString = request.getRemoteAddr();
		String url_pre = "";
		
		if(ipString.startsWith("101.69.228.218") || ipString.startsWith("172.17.5")) {
			if("http://101.69.228.218:8092".equals(url))
				url_pre = "http://172.17.60.190";
			else if("http://101.69.228.218:8093".equals(url)){
				url_pre = "http://172.17.60.191";
			}
		}else {
			url_pre = url;
		}
		String url_full = url_pre + urlPrefix + "/ws/file/getwechatFileInfo?fjid=" + fjid;
		*/
		
		String fileType=request.getParameter("fileType").toLowerCase();
		
		if(".pdf".equals(fileType)||"doc".equals(fileType)||"docx".equals(fileType)||"xls".equals(fileType)||"xlsx".equals(fileType)) {
			ModelAndView mav = new ModelAndView("common/file/viewer");
			String fjid=request.getParameter("fjid");
			String urlPrefix=request.getParameter("urlPrefix");
			String url=request.getParameter("url");
			
			String url_full = "/ws/external/viewFileOtherServer?fileUrl=" +url + urlPrefix + "/ws/file/getFileInfo?fjid=" + fjid;
			mav.addObject("file",url_full);
			
			return mav;
		}else if(".jpg".equals(fileType) || ".jpeg".equals(fileType) || ".jfif".equals(fileType) || ".png".equals(fileType)) {
			ModelAndView mav= new ModelAndView("wechat/view/imagepreview");
			String fjid=request.getParameter("fjid");
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setFjid(fjid);
			mav.addObject("fjcfbDto", fjcfbDto);
			
			String urlPrefix=request.getParameter("urlPrefix");
			String url=request.getParameter("url");
			//String url_full = url + urlPrefix + "/ws/file/getwechatFileInfo?fjid=" + fjid;
			String url_full = "/ws/external/viewFileOtherServer?fileUrl=" +url + urlPrefix + "/ws/file/getFileInfo?fjid=" + fjid;
			
			mav.addObject("file", url_full);
			
			return mav;
		}
		else {
			return commonService.jumpDocumentError(); 
		}
	}
	
	/**
	 * 获取8086上的数据然后返回给页面
	 * @param request
	 * @param response
	 */
	@RequestMapping("/external/viewFileOtherServer")
	@ResponseBody
	public void viewFileOtherServer(HttpServletRequest request,HttpServletResponse response) {
		
		String fileUrl=request.getParameter("fileUrl");
		
		log.error("lwj-viewFileOtherServer-" + fileUrl);
		//byte[] buffer = new byte[1024];
        //BufferedInputStream bis = null;
        
        OutputStream os = null; //输出流
        try {
            
            os = response.getOutputStream();

    		RestTemplate restTemplate = new RestTemplate();
    		//接收文件流，把文件信息放在内存中，适合小文件
    		ResponseEntity<byte[]> rsp = restTemplate.getForEntity(fileUrl, byte[].class);
            
            /*
            //定义请求头的接收类型   大文件接收
            RequestCallback requestCallback = request.getHeaderNames()
                        .setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
            
            //对响应进行流式处理而不是将其全部加载到内存中
            restTemplate.execute(fileUrl, HttpMethod.GET, requestCallback, clientHttpResponse -> {
               Files.copy(clientHttpResponse.getBody(), Paths.get(targetPath));
               return null;
            });
            */
            
            byte[] filebytes = rsp.getBody();
            
            //int size = filebytes.length;
            
            os.write(filebytes);
            os.flush();
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.error("lwj-viewFileOtherServer-end");
        try {
            //bis.close();
            os.flush();
            os.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
    		log.error("lwj-viewFileOtherServer-" + e);
        }
	}
	
//	/**
//	 * 钉钉审批查看8086端图片
//	 * @param request
//	 * @param fjcfbDto
//	 * @return
//	 */
//	@RequestMapping("/external/pripreview")
//	public ModelAndView pripreview(HttpServletRequest request, FjcfbDto fjcfbDto) {
//		ModelAndView mav= new ModelAndView("common/file/imagepreview");
//		mav.addObject("fjcfbDto", fjcfbDto);
//		return mav;
//	}
	
	@RequestMapping("/external/downloadFile")
	@ResponseBody
	public void downloadFile(HttpServletRequest request,HttpServletResponse response) {
		String fjid=request.getParameter("fjid");
		DBEncrypt crypt = new DBEncrypt();
		String t_url = crypt.dCode(companyurl) + "/ws/miniprogram/downloadDocFile?fjid="+fjid;
		HttpHeaders headers = new HttpHeaders();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<byte[]> entity = restTemplate.exchange(t_url, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);
		byte[] result = entity.getBody();
		if(result!=null) {
			InputStream inputStream = null;
			OutputStream outputStream = null;
			try {
				inputStream = new ByteArrayInputStream(result);
				outputStream = response.getOutputStream();
				int len;
				byte[] buf = new byte[1024];
				while ((len = inputStream.read(buf, 0, 1024)) != -1) {
					outputStream.write(buf, 0, len);
				}
				outputStream.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (inputStream != null) {
                        inputStream.close();
                    }
					if (outputStream != null) {
                        outputStream.close();
                    }
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 扫码上传复检
	 * @return
	 */
	@RequestMapping("/scanUploadFile")
	public ModelAndView scanUploadFile(String wbcxdm) {
		ModelAndView mav =new ModelAndView("wechat/sjxx/uploadSampleCollectionFile");
		mav.addObject("ywlx", BusTypeEnum.IMP_INSPECTION_CONFIRM.getCode());
		mav.addObject("wbcxdm", wbcxdm);
		return mav;
	}

	/**
	 * 通过内部编码获取信息
	 * @return
	 */
	@RequestMapping("/getPagInfo")
	@ResponseBody
	public Map<String, Object> getPagInfo(SjxxDto sjxxDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(sjxxDto.getNbbm())){
			SjxxDto dtoVague = sjxxService.getDto(sjxxDto);
			if (null != dtoVague){
				map.put("status","success");
				map.put("dto",dtoVague);
			}else{
				map.put("status","fail");
				map.put("message","扫码获取信息失败！");
			}
		}else{
			map.put("status","fail");
			map.put("message","扫码获取信息失败！");
		}
		return map;
	}


	/**
	 * 保存送检附件
	 * @return
	 */
	@RequestMapping("/savePageInfo")
	@ResponseBody
	public Map<String, Object> savePageInfo(SjxxDto sjxxDto) {
		Map<String, Object> map = new HashMap<>();

		if (StringUtil.isNotBlank(sjxxDto.getSjid()) && CollectionUtils.isNotEmpty(sjxxDto.getFjids())){
			boolean success = sjxxService.saveScanFile(sjxxDto);
			if(!success){
				map.put("status","fail");
				map.put("message","附件保存失败！");
				return map;
			}
		}else{
			map.put("status","fail");
			map.put("message","数据丢失保存失败！");
		}
		return map;
	}
	/**
	 * 产品说明书查看 页面
	 * @return
	 */
	@RequestMapping("/productManual/viewProductManual/{cpdm}/{bbh}")
	public ModelAndView viewProductManual(@PathVariable("cpdm") String cpdm, @PathVariable("bbh") String bbh) {
		ModelAndView mav = new ModelAndView("wechat/productManual/productManual_Wsview");
		CpwjDto cpwjDto_sel = new CpwjDto();
		cpwjDto_sel.setCpdm(cpdm);
		cpwjDto_sel.setBbh(bbh);
		cpwjDto_sel.setSfgk("1");
		CpwjDto cpwjDto_res = cpwjService.getDtoByBbhAndCpdm(cpwjDto_sel);
		if (cpwjDto_res==null){
			mav.addObject("sfczwj","0");
			return mav;
		}
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_PRODUCT_MANUAL.getCode());
		fjcfbDto.setYwid(cpwjDto_res.getCpwjid());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		mav.addObject("cpwjDto",cpwjDto_res);
		mav.addObject("sfczwj","1");
		return mav;
	}
}
