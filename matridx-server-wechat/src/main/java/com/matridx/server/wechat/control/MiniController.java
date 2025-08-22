package com.matridx.server.wechat.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.matridx.server.wechat.dao.entities.SjbgsmDto;
import com.matridx.server.wechat.dao.entities.SjdwxxDto;
import com.matridx.server.wechat.dao.entities.SjhbxxDto;
import com.matridx.server.wechat.dao.entities.SjjcxmDto;
import com.matridx.server.wechat.dao.entities.SjkjxxDto;
import com.matridx.server.wechat.dao.entities.SjlczzDto;
import com.matridx.server.wechat.dao.entities.SjqqjcDto;
import com.matridx.server.wechat.dao.entities.SjsyglDto;
import com.matridx.server.wechat.dao.entities.SjwzxxDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.WbzxDto;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.server.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.server.wechat.service.svcinterface.ISjdwxxService;
import com.matridx.server.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.server.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.server.wechat.service.svcinterface.ISjkjxxService;
import com.matridx.server.wechat.service.svcinterface.ISjlczzService;
import com.matridx.server.wechat.service.svcinterface.ISjsyglService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.server.wechat.service.svcinterface.IWbzxService;
import com.matridx.server.wechat.service.svcinterface.IWxyhService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.server.wechat.enums.IdentityTypeEnum;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Controller
@RequestMapping("/wechat")
public class MiniController extends BaseController {

	@Autowired
	ISjxxService sjxxService;
	
	@Autowired
	IFjcfbService fjcfbService;
	
	@Autowired
	ISjbgsmService sjbgsmservice;
	
	@Autowired
	IWxyhService wxyhService;
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	ISjlczzService sjlczzService;
	
	@Autowired
	ISjjcxmService sjjcxmService;
	
	@Autowired
	ISjhbxxService sjhbxxService;
	
	@Autowired
	IJcsjService jcsjService;

	@Autowired
	ISjkjxxService sjkjxxService;
	
	@Autowired
	ISjdwxxService sjdwxxService;
	
	@Autowired
	IWbzxService wxzxService;
	
	@Autowired
	ICommonService commonService;
	
	@Autowired
	IXtszService xtszService;
	@Autowired
	ISjsyglService sjsyglService;
	
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	
	private Logger logger = LoggerFactory.getLogger(MiniController.class);
	
	/**
	 * 小程序获取显示标记
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getInspectFlg")
	@ResponseBody
	public Map<String, Object> getInspectFlg(){
		Map<String,Object> map = new HashMap<>();
		XtszDto xtszDto = xtszService.getDtoById("mini.program.flg");
		map.put("flg", xtszDto.getSzz());
		return map;
	}
	
	/**
	 * 小程序修改微信用户表信息
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/updateUserInfo")
	@ResponseBody
	public Map<String, Object> updateUserInfo(WxyhDto wxyhDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess;
		try {
			isSuccess = wxyhService.modSaveWeChatUser(wxyhDto);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			map.put("status","fail");
			map.put("message",e.getMsg());
			return map;
		}
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 小程序获取微信用户表信息
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getUserInfo")
	@ResponseBody
	public Map<String, Object> getUserInfo(WxyhDto wxyhDto){
		Map<String,Object> map = new HashMap<>();
		WxyhDto t_wxyhDto = wxyhService.getDtoById(wxyhDto.getWxid());
		map.put("wxyhDto", t_wxyhDto);
		//获取身份类型
		Map<String, String> identitymap = wxyhService.getIdentityTypeMap();
		map.put("identitymap", identitymap);
		return map;
	}
	
	/**
	 * 小程序获取送检列表
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getInspectInfoList")
	@ResponseBody
	public Map<String, Object> getUserInfoList(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		List<SjxxDto> sjxxlist = sjxxService.getPagedDtoList(sjxxDto);
		map.put("sjxxlist", sjxxlist);
		return map;
	}
	
	/**
	 * 小程序获取送检信息
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getInspectInfo")
	@ResponseBody
	public Map<String, Object> getUserInfo(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		SjxxDto re_sjxxDto = sjxxService.getDto(sjxxDto);
		//获取临床症状，检测项目
		map.put("sjxxDto", re_sjxxDto);
		List<SjlczzDto> sjlczzDtos = sjlczzService.selectLczzBySjid(re_sjxxDto);
		map.put("clinicallist", sjlczzDtos);
		List<SjjcxmDto> sjjcxmDtos = sjjcxmService.selectJcxmBySjid(re_sjxxDto);
		map.put("sjjcxmDtos", sjjcxmDtos);
		map.put("status", re_sjxxDto != null?"success":"fail");
		map.put("message", re_sjxxDto != null?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		
		//前端需要查询收费信息，然后送检信息里也显示为未收费的时候，查询相应的金额
		if(re_sjxxDto != null){
			if(StringUtil.isNotBlank(sjxxDto.getSfflg()) && !"1".equals(re_sjxxDto.getFkbj())) {
				if(StringUtil.isBlank(re_sjxxDto.getFkje())){
					SjhbxxDto sjhbxxDto = new SjhbxxDto();
					sjhbxxDto.setSjxms(sjjcxmDtos);
					sjhbxxDto.setHbmc(re_sjxxDto.getDb());
					SjhbxxDto re_sjhbxxDto = sjhbxxService.getDto(sjhbxxDto);
					if(re_sjhbxxDto!=null) {
                        re_sjxxDto.setFy(re_sjhbxxDto.getSfbz());
                    }
				}else{
					re_sjxxDto.setFy(re_sjxxDto.getFkje());
				}
				
			}else if("1".equals(re_sjxxDto.getFkbj())){
				re_sjxxDto.setFy("该项目已付款 ");
			}
		}
		
		return map;
	}
	
	/**
	 * 小程序查看检测报告
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/getMiniReportView")
	public ModelAndView getMiniReportView(SjxxDto sjxxDto){
		//判断标本编号和患者姓名是否对应
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_getUserInfo");
		SjxxDto sjxxjcbgDto = sjxxService.getDto(sjxxDto);
		if(sjxxjcbgDto != null) {
			//查看送检结果
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjxxjcbgDto.getSjid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_C_TEMEPLATE_D.getCode());
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		    mav=new ModelAndView("wechat/informed/wechat_view");
		    List<SjwzxxDto> wzxx=sjxxService.selectWzxxBySjid(sjxxjcbgDto);
		    //查看附件
		    FjcfbDto fjcfbDto_fj=new FjcfbDto();
		    fjcfbDto_fj.setYwid(sjxxjcbgDto.getSjid());
		    fjcfbDto_fj.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		    List<FjcfbDto> fjcfbDtos_fjs = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto_fj);
		    //查看送检报告说明
			SjbgsmDto sjbgsmdto=new SjbgsmDto();
			sjbgsmdto.setSjid(sjxxjcbgDto.getSjid());
			List<SjbgsmDto> sjbgsmxx=sjbgsmservice.selectSjbgBySjid(sjbgsmdto);
			mav.addObject("sjbgsmDto",sjbgsmxx);
		    mav.addObject("fjcfbDtos",fjcfbDtos);
		    mav.addObject("fjcfbDtos_fjs",fjcfbDtos_fjs);
			mav.addObject("DaobyYbbh",sjxxjcbgDto);
			mav.addObject("Sjwzxx", wzxx);
			String sfsj="0";
			List<SjsyglDto> sjsyglDtos = sjsyglService.getViewDetectData(sjxxjcbgDto.getSjid());
			if(sjsyglDtos!=null&&sjsyglDtos.size()>0){
				for(SjsyglDto dto:sjsyglDtos){
					if(StringUtil.isNotBlank(dto.getSjsj())){
						sfsj="1";
						break;
					}
				}
			}
			mav.addObject("sfsj", sfsj);
			mav.addObject("sjsyglDtos", sjsyglDtos);
			mav.addObject("viewFlg","1");
		}else {
			mav=new ModelAndView("wechat/informed/wechat_outerror");
		}
		DBEncrypt crypt = new DBEncrypt();
		mav.addObject("applicationurl", crypt.dCode(applicationurl));
		return mav;
	}
	
	/**
	 * 小程序查看报告
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getMiniInspectReportView")
	public ModelAndView getMiniInspectReportView(SjxxDto sjxxDto,WxyhDto wxyhDto){
		ModelAndView mav = null;
		if(IdentityTypeEnum.DOCTOR.getCode().equals(sjxxDto.getSflx())){
			//跳转医生
			mav = new ModelAndView("wechat/informed/wechat_viewreportBydoctor");
		}else if(IdentityTypeEnum.SALES.getCode().equals(sjxxDto.getSflx())){
			//跳转销售
			mav = new ModelAndView("wechat/informed/wechat_viewReportBySales");
		}
		mav.addObject("sign", commonService.getSign());
		mav.addObject("wxyhDto",wxyhDto);
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}
	
	/**
	 * 小程序查询送检清单信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/getMiniInsplist")
	@ResponseBody
	public Map<String, Object> getMiniInsplist(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		List<SjxxDto> sjxxDtos = sjxxService.getSjxxDtoList(sjxxDto);
		map.put("sjxxDtos", sjxxDtos);
		return map;
	}
	
	/**
	 * 小程序保存修改金额
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/miniprogram/modAmount")
	@ResponseBody
	public Map<String, Object> modAmount(SjxxDto sjxxDto){
		sjxxDto.setXgry(sjxxDto.getWxid());
		boolean isSuccess = sjxxService.amountSaveEdit(sjxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("sjxxDto", sjxxDto);
		return map;
	}
	
	/**
	 * 小程序送检第一页初始化
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getInspectOne")
	@ResponseBody
	public Map<String, Object> getInspectOne(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		Map<String, List<JcsjDto>> g_jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.GENDER_TYPE,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.PATHOGENY_TYPE});
		map.put("genderlist", g_jclist.get(BasicDataTypeEnum.GENDER_TYPE.getCode()));
		map.put("detectlist", g_jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));
		map.put("pathogenylist", g_jclist.get(BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));
		List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
		map.put("ksxxlist", sjdwlist);
		if(StringUtil.isNotBlank(sjxxDto.getSjid())){
			sjxxDto = sjxxService.getDto(sjxxDto);
			if(sjxxDto == null) {
                sjxxDto = new SjxxDto();
            }
			//根据文件ID查询附件表信息
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjxxDto.getSjid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			map.put("fjcfbDtos",fjcfbDtos);
		}else{
			//查询快捷信息
			if(!StringUtil.isBlank(sjxxDto.getWxid())){
				SjkjxxDto sjkjxxDto = sjkjxxService.getDtoById(sjxxDto.getWxid());
				if(sjkjxxDto != null){
					SjxxDto t_sjxxDto = new SjxxDto();
					t_sjxxDto.setSjdw(sjkjxxDto.getSjdw());
					t_sjxxDto.setKs(sjkjxxDto.getKs());
					t_sjxxDto.setQtks(sjkjxxDto.getQtks());
					t_sjxxDto.setSjys(sjkjxxDto.getSjys());
					t_sjxxDto.setYsdh(sjkjxxDto.getYsdh());
					t_sjxxDto.setJcdw(sjkjxxDto.getJcdw());
					t_sjxxDto.setWxid(sjxxDto.getWxid());
					sjxxDto = t_sjxxDto;
				}
			}
		}
		//获取文件类型
		sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		map.put("sjxxDto", sjxxDto);
		return map;
	}
	
	/**
	 * 小程序提取送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/extractInspect")
	@ResponseBody
	public Map<String, Object> extractInspect(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		try {
			SjxxDto t_sjxxDto = sjxxService.extractUserInfo(sjxxDto);
			if(t_sjxxDto != null){
				//获取文件类型
				t_sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
				//根据文件ID查询附件表信息
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(t_sjxxDto.getSjid());
				fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
				map.put("fjcfbDtos",fjcfbDtos);
			}
			map.put("sjxxDto", t_sjxxDto);
			map.put("status", t_sjxxDto != null?"success":"fail");
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
		}
		return map;
	}
	
	/**
	 * 小程序送检第一步保存
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/saveInspectOne")
	@ResponseBody
	public Map<String, Object> saveInspectOne(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		try {
			logger.error("saveInspectOne wxid:" + sjxxDto.getWxid() + " ddid:" + sjxxDto.getDdid());
			boolean isSuccess = sjxxService.addSaveReport(sjxxDto);
			if(isSuccess) {
				map.put("sjxxDto", sjxxDto);
				sjxxService.saveFile(sjxxDto);
				sjxxDto = sjxxService.getDto(sjxxDto);
				if(sjxxDto == null) {
                    sjxxDto = new SjxxDto();
                }
				//根据文件ID查询附件表信息
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(sjxxDto.getSjid());
				fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
				map.put("fjcfbDtos",fjcfbDtos);
				map.put("sjxxDto", sjxxDto);
				map.put("status", "success");
				map.put("message", xxglService.getModelById("ICOM00001").getXxnr());
			}else{
				map.put("status", "fail");
				map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
		}
		return map;
	}
	
	/**
	 * 小程序送检第二页初始化
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getInspectTwo")
	@ResponseBody
	public Map<String, Object> getInspectTwo(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		Map<String, List<JcsjDto>> w_jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.CLINICAL_TYPE,BasicDataTypeEnum.INVOICE_APPLICATION});
		map.put("samplelist", w_jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
		map.put("clinicallist", w_jclist.get(BasicDataTypeEnum.CLINICAL_TYPE.getCode()));
		map.put("sfkplist",w_jclist.get(BasicDataTypeEnum.INVOICE_APPLICATION.getCode()));
		sjxxDto = sjxxService.getDto(sjxxDto,1);
		map.put("sjxxDto", sjxxDto);
		return map;
	}
	
	/**
	 * 小程序送检返回第一步
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/backInspectOne")
	@ResponseBody
	public Map<String, Object> backInspectOne(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		List<SjqqjcDto> sjqqjsDtos = getSjqqjcs(sjxxDto);
		if(sjqqjsDtos != null && sjqqjsDtos.size() > 0){
			sjxxDto.setSjqqjcs(sjqqjsDtos);
		}
		if(StringUtils.isNotBlank(sjxxDto.getWxid())) {
			sjxxDto.setLrry(sjxxDto.getWxid());
			sjxxDto.setXgry(sjxxDto.getWxid());
		}else {
			sjxxDto.setLrry(sjxxDto.getDdid());
			sjxxDto.setXgry(sjxxDto.getDdid());
		}
		boolean isSuccess = sjxxService.addSaveConsentBack(sjxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("sjxxDto", sjxxDto);
		return map;
	}
	
	/**
	 * 获取前期检测信息
	 * @param sjxxDto
	 * @return
	 */
	private List<SjqqjcDto> getSjqqjcs(SjxxDto sjxxDto){
		List<String> qqjcids = sjxxDto.getQqjcids();
		List<String> jczs = sjxxDto.getJczs();
		if(qqjcids != null && qqjcids.size() > 0){
			List<SjqqjcDto> sjqqjsDtos = new ArrayList<>();
			for (int i = 0; i < qqjcids.size(); i++) {
				SjqqjcDto sjqqjcDto = new SjqqjcDto();
				sjqqjcDto.setJcz(jczs.get(i));
				sjqqjcDto.setYjxm(qqjcids.get(i));
				sjqqjsDtos.add(sjqqjcDto);
			}
			return sjqqjsDtos;
		}
		return null;
	}
	
	/**
	 * 小程序送检第二步保存
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/saveInspectTwo")
	@ResponseBody
	public Map<String, Object> saveInspectTwo(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		List<SjqqjcDto> sjqqjsDtos = getSjqqjcs(sjxxDto);
		if(sjqqjsDtos != null && sjqqjsDtos.size() > 0){
			sjxxDto.setSjqqjcs(sjqqjsDtos);
		}
		//判断相同用户标本类型是否输入重复
		SjxxDto i_sjxxDto = sjxxService.getDto(sjxxDto,1);
		i_sjxxDto.setYblx(sjxxDto.getYblx());
		List<SjxxDto> sjxxDtos = sjxxService.isYblxRepeat(i_sjxxDto);
		if(sjxxDtos != null && sjxxDtos.size() > 0){
			//保存现有信息
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOMM_SJ00007").getXxnr());
			return map;
		}
		sjxxDto.setLrry(sjxxDto.getWxid());
		sjxxDto.setXgry(sjxxDto.getWxid());
		sjxxDto.setZt(StatusEnum.CHECK_PASS.getCode());
		sjxxDto.setScbj("0");
		boolean isSuccess = sjxxService.addSaveConsentComp(sjxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("sjxxDto", sjxxDto);
		return map;
	}
	
	/**
	 * 上传临时文件
	 * @param fjcfbDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/miniprogram/saveImportFile")
	@ResponseBody
	public Map<String, Object> saveImportFile(FjcfbDto fjcfbDto,HttpServletRequest request){
		Map<String, Object> result = new HashMap<String,Object>();
		try{
			List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(((MultipartHttpServletRequest) request).getFile("file").getName());
			MultipartFile[] imp_file = new MultipartFile[files.size()];
			files.toArray(imp_file);
			if(imp_file!=null&& imp_file.length>0){
				User user = null;
				fjcfbService.save2TempFile(imp_file, fjcfbDto,user);
				
				result.put("status", "success");
				result.put("fjcfbDto", fjcfbDto);
				result.put("filePath", request.getParameter("filePath"));
			}else{
				result.put("status", "fail");
				result.put("msg", "未获取文件");
			}
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("msg", e.getMessage());
		}
		return result;
	}
	
	/**
	 * 小程序获取资讯列表
	 * @param wxzxDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getNewsList")
	@ResponseBody
	public Map<String, Object> getNewsList(WbzxDto wxzxDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		List<WbzxDto> wxzxDtos = wxzxService.getDtoList(wxzxDto);
		map.put("wxzxDtos", wxzxDtos);
		return map;
	}
	
	/**
	 * 小程序获取资讯信息
	 * @param wxzxDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getNewsInfo")
	@ResponseBody
	public Map<String, Object> getNewsInfo(WbzxDto wxzxDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		WbzxDto t_wxzxDto = wxzxService.getDtoById(wxzxDto.getZxid());
		map.put("wxzxDto", t_wxzxDto);
		return map;
	}
}
