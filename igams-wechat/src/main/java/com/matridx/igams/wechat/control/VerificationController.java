package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjyzDto;
import com.matridx.igams.wechat.dao.entities.SjyzjgDto;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjyzService;
import com.matridx.igams.wechat.service.svcinterface.ISjyzjgService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.encrypt.GenerateFileForPCR;
import com.matridx.springboot.util.encrypt.Plate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/verification")
public class VerificationController extends BaseController {
	@Autowired
    ISjyzService sjyzService;
	@Autowired
    ISjxxService sjxxService;
	@Autowired
    IJcsjService jcsjService;
	@Autowired
    IXxglService xxglService;
	@Autowired
    ISjyzjgService sjyzjgService;
	@Autowired
    IFjcfbService fjcfbService;
	@Autowired
    IHbqxService hbqxService;
	@Autowired
    ISjhbxxService sjhbxxservice;
	@Autowired
    IGrszService grszService;
	@Autowired
	IJgxxService jgxxService;
	@Autowired
    RedisUtil redisUtil;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	@Value("${matridx.fileupload.releasePath:}")
	private String exportFilePath;
//	@Value("${matridx.ftpPcr.addr:}")
//	String addr;
//	@Value("${matridx.ftpPcr.port:}")
//	Integer pcrport;
//	@Value("${matridx.ftpPcr.username:}")
//	String username;
//	@Value("${matridx.ftpPcr.password:}")
//	String password;

	private Logger log = LoggerFactory.getLogger(VerificationController.class);

	/**
	 * 送检验证列表
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/pageListVerifi")
	public ModelAndView pageListVerifi(SjyzDto sjyzDto) {
		ModelAndView mav=new ModelAndView("wechat/verification/verification_list");
		sjyzDto.setAuditType(AuditTypeEnum.AUDIT_VERIFICATION.getCode());
		//根据系统审核审核类别查找审核流程
		List<XtshDto> xtshDtos = sjyzService.getSjyzShlc(sjyzDto);//获取验证流程的审核岗位步骤
		Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.VERIFICATION_TYPE, BasicDataTypeEnum.WECGATSAMPLE_TYPE, BasicDataTypeEnum.DETECT_TYPE, BasicDataTypeEnum.VERIFICATION_RESULT, BasicDataTypeEnum.DISTINGUISH, BasicDataTypeEnum.CLIENT_NOTICE});
		mav.addObject("verificationresultList", jcsjlist.get(BasicDataTypeEnum.VERIFICATION_RESULT.getCode()));//送检验证结果
		mav.addObject("verificationList", jcsjlist.get(BasicDataTypeEnum.VERIFICATION_TYPE.getCode()));//送检验证类型
    	mav.addObject("samplelist", jcsjlist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
    	mav.addObject("detectlist", jcsjlist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
    	mav.addObject("distinguishList", jcsjlist.get(BasicDataTypeEnum.DISTINGUISH.getCode()));//区分
		mav.addObject("clientnoticeList", jcsjlist.get(BasicDataTypeEnum.CLIENT_NOTICE.getCode()));//客户通知
		mav.addObject("detectionUnit", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		mav.addObject("sjyzDto", sjyzDto);
		mav.addObject("xtshDtos", xtshDtos);
		return mav;
	}
	
	/**
	 * 列表数据
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/pageGetListVerifi")
	@ResponseBody
	public Map<String,Object> getListVerifi(SjyzDto sjyzDto, HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		if("1".equals(sjyzDto.getLoad_flag())) {
			List<String> userids= new ArrayList<>();
			if(user.getYhid()!=null) {
				userids.add(user.getYhid());
			}
			if(user.getDdid()!=null) {
				userids.add(user.getDdid());
			}
			if(user.getWechatid()!=null) {
				userids.add(user.getWechatid());
			}
			if(userids.size()>0) {
				sjyzDto.setUserids(userids);
			}
			//判断伙伴权限
			List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
			if(hbqxList!=null && hbqxList.size()>0) {
				List<String> hbmcList=sjhbxxservice.getHbmcByHbid(hbqxList);
				if(hbmcList!=null  && hbmcList.size()>0) {
					sjyzDto.setSjhbs(hbmcList);
				}
			}
			List<JcsjDto> matridx_jcsj_client_notice = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CLIENT_NOTICE.getCode());
			for (JcsjDto jcsjDto : matridx_jcsj_client_notice) {
				if ("1".equals(jcsjDto.getCskz1())){
					sjyzDto.setKhtz(jcsjDto.getCsid());
					break;
				}
			}
		}
		List<Map<String,String>> jcdwList=sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(jcdwList!=null&&jcdwList.size() > 0){
			if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				sjyzDto.setJsid(user.getDqjs());
				List<String> jcdws= new ArrayList<>();
				for (int i = 0; i < jcdwList.size(); i++) {
					if(jcdwList.get(i).get("jcdw")!=null) {
						jcdws.add(jcdwList.get(i).get("jcdw"));
					}
				}
				if(jcdws.size()>0) {
					sjyzDto.setJcdwxz(jcdws);
				}
			}
		}

		//高级筛选增加，审核岗位xlcxh和审核岗位名称进行数据查找
		//根据系统审核审核类别查找审核流程
		if (sjyzDto.getXlcxhs() != null && sjyzDto.getXlcxhs().length >0){
			List<XtshDto> xtshDtos = sjyzService.getSjyzShlc(sjyzDto);//获取验证流程的审核岗位步骤
			List<String> spgwids = new ArrayList<>();
			for (XtshDto xtshDto: xtshDtos){
				spgwids.add(xtshDto.getGwid());
			}
			sjyzDto.setGwids(spgwids);
		}
		List<SjyzDto> sjyzList=sjyzService.getPagedList(sjyzDto);
		map.put("total", sjyzDto.getTotalNumber());
		map.put("rows", sjyzList);
		screenClassColumns(request,map);
		return map;
	}

	/**
	 * 异步获取基础数据子类别
	 * @return
	 */
	@RequestMapping(value ="/verification/ansyGetJcsjListAndJl")
	@ResponseBody
	public Map<String,Object> ansyGetJcsjListAndJl(JcsjDto jcsjDto){
		Map<String,Object> map= new HashMap<>();
		List<JcsjDto> jcsjDtos = jcsjService.getJcsjDtoListAndJl(jcsjDto);
		Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.VERIFICATION_RESULT});
		List<JcsjDto> jcsjDtoList = jcsjlist.get(BasicDataTypeEnum.VERIFICATION_RESULT.getCode()); //报告结果
		map.put("jcsjDtos",jcsjDtos);
		map.put("jcsjDtoList",jcsjDtoList);
		return map;
	}
	
	/**
	 * 修改界面
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/modVerifi")
	public ModelAndView modVerifi(SjyzDto sjyzDto) {
		ModelAndView mav=new ModelAndView("wechat/verification/verification_add");
		SjyzDto sjyzDto_t=sjyzService.getDto(sjyzDto);
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(sjyzDto_t.getSjid());
		SjxxDto sjxxDto_t=sjxxService.getDto(sjxxDto);
		Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.VERIFICATION_TYPE, BasicDataTypeEnum.VERIFICATION_RESULT, BasicDataTypeEnum.DISTINGUISH, BasicDataTypeEnum.CLIENT_NOTICE});
		mav.addObject("verificationList", jcsjlist.get(BasicDataTypeEnum.VERIFICATION_TYPE.getCode()));//送检验证类型
		mav.addObject("verificationresultList", jcsjlist.get(BasicDataTypeEnum.VERIFICATION_RESULT.getCode()));//送检验证结果
		mav.addObject("distinguishList", jcsjlist.get(BasicDataTypeEnum.DISTINGUISH.getCode()));//区分
		mav.addObject("clientnoticeList", jcsjlist.get(BasicDataTypeEnum.CLIENT_NOTICE.getCode()));//客户通知
		mav.addObject("sjxxDto", sjxxDto_t);
		mav.addObject("sjyzDto", sjyzDto_t);
		mav.addObject("formAction", "/verification/verification/modSaveAuditVerify");
		return mav;
	}
	
	/**
	 * 送检验证审核页面
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/modAuditVerify")
	public ModelAndView modAuditVerify(SjyzDto sjyzDto) {
		ModelAndView mav=new ModelAndView("wechat/verification/verification_modSubmit");
		SjyzDto sjyzDto_t=sjyzService.getDto(sjyzDto);
		sjyzDto_t.setAuditType(AuditTypeEnum.AUDIT_VERIFICATION.getCode());
		sjyzDto_t.setYwlx(BusTypeEnum.IMP_VERIFIFCATION.getCode());
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(sjyzDto_t.getSjid());
		SjxxDto sjxxDto_t=sjxxService.getDto(sjxxDto);
		List<JcsjDto> distinguishList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DISTINGUISH.getCode());
		if (StringUtil.isNotBlank(sjxxDto_t.getNbbm())){
			String lastCode = sjxxDto_t.getNbbm();
			lastCode = lastCode.substring(lastCode.length()-1);
			if ("B".equals(lastCode) || "b".equals(lastCode)){
				List<JcsjDto> lsdistinguishList = new ArrayList<>();
				for (JcsjDto distinguish : distinguishList) {
					if (!"REM".equals(distinguish.getCskz1())){
						lsdistinguishList.add(distinguish);
					}
				}
				distinguishList = lsdistinguishList;
			}
		}
		mav.addObject("distinguishList", distinguishList);//区分
		SjyzjgDto sjyzjgDto=new SjyzjgDto();
		sjyzjgDto.setYzid(sjyzDto.getYzid());
		List<SjyzjgDto> yzmxlist=sjyzjgService.getDtoList(sjyzjgDto);
//		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(sjyzDto.getYzid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.getFjcfbDtoByYwid(sjyzDto.getYzid());
		List<FjcfbDto> fjcfbDtos1 = new ArrayList<>();//附件删除标记为0
		List<FjcfbDto> fjcfbDtos2 = new ArrayList<>();//附件删除标记为2
		for (FjcfbDto fj : fjcfbDtos){
			if ("0".equals(fj.getScbj())){
				fjcfbDtos1.add(fj);
			}else {
				fjcfbDtos2.add(fj);
			}
		}
		mav.addObject("verificationList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.VERIFICATION_TYPE.getCode()));//送检验证类型
		mav.addObject("verificationresultList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.VERIFICATION_RESULT.getCode()));//送检验证结果
		mav.addObject("clientnoticeList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.CLIENT_NOTICE.getCode()));//客户通知
		mav.addObject("detectionList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		User user = getLoginInfo();
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.REAGENT_SETTINGS.getCode()+"_"+sjyzDto_t.getYzlb(),PersonalSettingEnum.PREIMER_NUMBER.getCode()});
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
		GrszDto grszxx_s = grszDtoMap.get(PersonalSettingEnum.REAGENT_SETTINGS.getCode()+"_"+sjyzDto_t.getYzlb());
		GrszDto grszxx_ywbh = grszDtoMap.get(PersonalSettingEnum.PREIMER_NUMBER.getCode());
		if(StringUtil.isBlank(sjyzDto_t.getSjph()) && grszxx_s!=null) {
			sjyzDto_t.setSjph(grszxx_s.getSzz());
			mav.addObject("sjxsbj", "1");
		}else{
			mav.addObject("sjxsbj", "0");
		}
		if(StringUtil.isBlank(sjyzDto_t.getYwbh()) && grszxx_ywbh!=null) {
			sjyzDto_t.setYwbh(grszxx_ywbh.getSzz());
			mav.addObject("ywxsbj", "1");
		}else{
			mav.addObject("ywxsbj", "0");
		}
		mav.addObject("sjxxDto", sjxxDto_t);
		mav.addObject("sjyzDto", sjyzDto_t);
		mav.addObject("formAction", "/verification/verification/modSaveAuditVerify");
		//根据验证id查询附件表信息
//		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("fjcfbDtos",fjcfbDtos1);
		mav.addObject("fjcfbDtos2",fjcfbDtos2);
		mav.addObject("yzmxlist", yzmxlist);
		if(StringUtil.isNotBlank(sjyzDto.getBtnFlg())) {
			mav.addObject("btnFlg",sjyzDto.getBtnFlg());
		}else {
			mav.addObject("btnFlg","audit");
			mav.addObject("url", "/verification/verification/commonModJcdw");
		}
		return mav;
	}
	
	/**
	 * 修改保存
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/modSaveAuditVerify")
	@ResponseBody
	public Map<String,Object> modSaveAuditVerify(SjyzDto sjyzDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.REAGENT_SETTINGS.getCode()+"_"+sjyzDto.getYzlb(),PersonalSettingEnum.PREIMER_NUMBER.getCode()});
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
		GrszDto grszxx_s = grszDtoMap.get(PersonalSettingEnum.REAGENT_SETTINGS.getCode()+"_"+sjyzDto.getYzlb());
		grszDto.setSzlb(PersonalSettingEnum.REAGENT_SETTINGS.getCode()+"_"+sjyzDto.getYzlb());
		if (grszxx_s == null) {
			grszDto.setSzz(sjyzDto.getSjph());
			grszDto.setLrry(user.getYhid());
			grszService.insertDto(grszDto);
		} else {
			if (grszxx_s.getSzz()==null || !grszxx_s.getSzz().equals(sjyzDto.getSjph())) {
				grszDto.setSzz(sjyzDto.getSjph());
				grszService.updateByYhidAndSzlb(grszDto);
			}
		}
		GrszDto grszDto_yw = new GrszDto();
		grszDto_yw.setYhid(user.getYhid());
		grszDto_yw.setSzlb(PersonalSettingEnum.PREIMER_NUMBER.getCode());
		GrszDto grszxx_ywbh = grszDtoMap.get(PersonalSettingEnum.PREIMER_NUMBER.getCode());
		if (grszxx_ywbh == null) {
			grszDto_yw.setSzz(sjyzDto.getYwbh());
			grszDto_yw.setLrry(user.getYhid());
			grszService.insertDto(grszDto_yw);
		} else {
			if (grszxx_ywbh.getSzz()==null || !grszxx_ywbh.getSzz().equals(sjyzDto.getYwbh())) {
				grszDto_yw.setSzz(sjyzDto.getYwbh());
				grszService.updateByYhidAndSzlb(grszDto_yw);
			}
		}
		List<JcsjDto> distinguishList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DISTINGUISH.getCode());
		for (JcsjDto distinguish : distinguishList) {
			if (StringUtil.isNotBlank(sjyzDto.getQf())&&sjyzDto.getQf().equals(distinguish.getCsid())){
				sjyzDto.setAuditType(distinguish.getCskz2());
				break;
			}
		}
		sjyzDto.setXgry(user.getYhid());
		sjyzDto.setXsbj("1");
		SjyzDto sjyzDto1 = sjyzService.getDto(sjyzDto);
		sjyzDto.setZt(sjyzDto1.getZt());
		boolean isSuccess=sjyzService.updateSjyzxx(sjyzDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid",sjyzDto.getYzid());
		map.put("auditType",sjyzDto.getAuditType());
		return map;
	}
	/**
	 * 重新读取PCR文件
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/rereadVerify")
	@ResponseBody
	public Map<String,Object> rereadVerify(SjyzDto sjyzDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		sjyzDto.setXgry(user.getYhid());
		try {
			boolean isSuccess = sjyzService.rereadVerify(sjyzDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (IOException e) {
			map.put("status", "fail");
			map.put("message", "解析文件失败！");
		}
		return map;
	}
	/**
	 * 查看页面
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/viewVerifi")
	public ModelAndView viewVerifi(SjyzDto sjyzDto) {
		ModelAndView mav=new ModelAndView("wechat/verification/verification_view");
		//查询送检验证信息	
		SjyzDto sjyzDto_t=sjyzService.getDto(sjyzDto);
		//查询送检信息
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(sjyzDto_t.getSjid());
		SjxxDto sjxxDto_t=sjxxService.getDto(sjxxDto);
		if(sjxxDto_t==null){
			sjxxDto_t = new SjxxDto();
		}
		if("1".equals(sjxxDto_t.getYyxxCskz1())) {
			sjxxDto_t.setHospitalname(sjxxDto_t.getHospitalname()+"-"+sjxxDto_t.getSjdwmc());
		}
		List<FjcfbDto> fjcfbDtos = fjcfbService.getFjcfbDtoByYwid(sjyzDto.getYzid());
		List<FjcfbDto> fjcfbDtos1 = new ArrayList<>();//附件删除标记为0
		List<FjcfbDto> fjcfbDtos2 = new ArrayList<>();//附件删除标记为2
		for (FjcfbDto fj : fjcfbDtos){
			if ("0".equals(fj.getScbj())){
				fjcfbDtos1.add(fj);
			}else {
				fjcfbDtos2.add(fj);
			}
		}
		SjyzjgDto sjyzjgDto=new SjyzjgDto();
		sjyzjgDto.setYzid(sjyzDto.getYzid());
		List<SjyzjgDto> yzmxlist=sjyzjgService.getDtoList(sjyzjgDto);
		mav.addObject("sjxxDto", sjxxDto_t);
		mav.addObject("sjyzDto", sjyzDto_t);
//		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("fjcfbDtos", fjcfbDtos1);
		mav.addObject("fjcfbDtos2", fjcfbDtos2);
		mav.addObject("yzmxlist", yzmxlist);
		return mav;
	}
	
	/**
	 * 删除
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/delVerifi")
	@ResponseBody
	public Map<String,Object> delVerifi(SjyzDto sjyzDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		sjyzDto.setScry(user.getYhid());
		boolean isSuccess=sjyzService.delete(sjyzDto);
		SjyzjgDto sjyzjgDto = new SjyzjgDto();
		sjyzjgDto.setScry(user.getYhid());
		sjyzjgDto.setIds(sjyzDto.getIds());
		if (StringUtil.isNotBlank(sjyzDto.getYzid())){
			sjyzjgDto.setYzid(sjyzDto.getYzid());
		}
		sjyzjgService.deleteByYzid(sjyzjgDto);
		//同步至微信服务器
		if(isSuccess) {
			if (StringUtil.isNotBlank(menuurl))
				amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.DEL_INSPECTION_VERIFICATION.getCode(), JSONObject.toJSONString(sjyzDto));
		}
		
		map.put("status",isSuccess?"success":"fail"); 
		map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr()); 
		return map;
	}
	
	/**
	 * 重新提交审核
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/resubmitVerifi")
	public ModelAndView resubmitVerifi(SjyzDto sjyzDto) {
		ModelAndView mav=new ModelAndView("wechat/verification/verification_add");
		SjyzDto sjyzDto_t=sjyzService.getDto(sjyzDto);
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(sjyzDto_t.getSjid());
		SjxxDto sjxxDto_t=sjxxService.getDto(sjxxDto);
		List<JcsjDto> distinguishList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DISTINGUISH.getCode());
		if (StringUtil.isNotBlank(sjxxDto_t.getNbbm())){
			String lastCode = sjxxDto_t.getNbbm();
			lastCode = lastCode.substring(lastCode.length()-1);
			if ("B".equals(lastCode) || "b".equals(lastCode)){
				List<JcsjDto> lsdistinguishList = new ArrayList<>();
				for (JcsjDto distinguish : distinguishList) {
					if (!"REM".equals(distinguish.getCskz1())){
						lsdistinguishList.add(distinguish);
					}
				}
				distinguishList = lsdistinguishList;
			}
		}
		mav.addObject("distinguishList", distinguishList);//区分
		sjxxDto_t.setJcdw(sjyzDto_t.getJcdw());
		SjyzjgDto sjyzjgDto=new SjyzjgDto();
		sjyzjgDto.setYzid(sjyzDto.getYzid());
		List<SjyzjgDto> yzmxlist=sjyzjgService.getDtoList(sjyzjgDto);
		mav.addObject("yzmxlist", yzmxlist);
		//Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListByType(new BasicDataTypeEnum[]{BasicDataTypeEnum.VERIFICATION_TYPE, BasicDataTypeEnum.VERIFICATION_RESULT, BasicDataTypeEnum.DISTINGUISH, BasicDataTypeEnum.CLIENT_NOTICE, BasicDataTypeEnum.DETECTION_UNIT});
		mav.addObject("verificationList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.VERIFICATION_TYPE.getCode()));//送检验证类型
		mav.addObject("verificationresultList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.VERIFICATION_RESULT.getCode()));//送检验证结果
		mav.addObject("clientnoticeList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.CLIENT_NOTICE.getCode()));//客户通知
		mav.addObject("detectionList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("sjxxDto", sjxxDto_t);
		mav.addObject("sjyzDto", sjyzDto_t);
		mav.addObject("xsbj", "0");
		mav.addObject("formAction", "/verification/verification/modSaveAuditVerify");
		mav.addObject("btnFlg","default");
		return mav;
	}
	
	/**
	 * 送检验证审核列表
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/pageListVerifi_audit")
	public ModelAndView pageListVerifi_audit(SjyzDto sjyzDto) {
		ModelAndView mav=new ModelAndView("wechat/verification/verification_auditList");
		Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.VERIFICATION_TYPE, BasicDataTypeEnum.WECGATSAMPLE_TYPE, BasicDataTypeEnum.DETECT_TYPE, BasicDataTypeEnum.VERIFICATION_RESULT, BasicDataTypeEnum.DISTINGUISH, BasicDataTypeEnum.CLIENT_NOTICE});
		mav.addObject("verificationresultList", jcsjlist.get(BasicDataTypeEnum.VERIFICATION_RESULT.getCode()));//送检验证结果
		mav.addObject("verificationList", jcsjlist.get(BasicDataTypeEnum.VERIFICATION_TYPE.getCode()));//送检验证类型
		mav.addObject("samplelist", jcsjlist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
		mav.addObject("detectlist", jcsjlist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
		mav.addObject("distinguishList", jcsjlist.get(BasicDataTypeEnum.DISTINGUISH.getCode()));//区分
		mav.addObject("clientnoticeList", jcsjlist.get(BasicDataTypeEnum.CLIENT_NOTICE.getCode()));//客户通知
		mav.addObject("detectionUnit", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		return mav;
	}
	
	/**
	 * 送检验证审核列表
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/pageGetListVerifi_audit")
	@ResponseBody
	public Map<String,Object> getListVerifi_audit(SjyzDto sjyzDto, HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(sjyzDto);
		List<AuditTypeEnum> auditTypes = new ArrayList<>();
		auditTypes.add(AuditTypeEnum.AUDIT_VERIFICATION);
		auditTypes.add(AuditTypeEnum.AUDIT_LAB_VERIFICATION);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(sjyzDto.getDqshzt())){
			DataPermission.add(sjyzDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "sjyz", "yzid", auditTypes);
		} else{
			DataPermission.add(sjyzDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "sjyz", "yzid", auditTypes);
		}
		User user=getLoginInfo();
		List<Map<String,String>> jcdwList=sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(jcdwList!=null && jcdwList.size() > 0) {
			if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				sjyzDto.setJsid(user.getDqjs());
				List<String> jcdws= new ArrayList<>();
				for (int i = 0; i < jcdwList.size(); i++) {
					if(jcdwList.get(i).get("jcdw")!=null) {
						jcdws.add(jcdwList.get(i).get("jcdw"));
					}
				}
				if(jcdws.size()>0) {
					sjyzDto.setJcdwxz(jcdws);
				}
			}
		}
		List<SjyzDto> listMap = sjyzService.getPagedAuditSjyz(sjyzDto);
		
		map.put("total", sjyzDto.getTotalNumber());
		map.put("rows", listMap);
		screenClassColumns(request,map);
		return map;
	}
	
	/**
	 * 送检验证审核
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/audit_Verifi")
	public ModelAndView audit_Verifi(SjyzDto sjyzDto) {
		ModelAndView mav=new ModelAndView("wechat/verification/verification_add");
		SjyzDto sjyzDto_t=sjyzService.getDto(sjyzDto);
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(sjyzDto_t.getSjid());
		SjxxDto sjxxDto_t=sjxxService.getDto(sjxxDto);
		sjxxDto_t.setJcdw(sjyzDto_t.getJcdw());
		SjyzjgDto sjyzjgDto=new SjyzjgDto();
		sjyzjgDto.setYzid(sjyzDto.getYzid());
		List<SjyzjgDto> yzmxlist=sjyzjgService.getDtoList(sjyzjgDto);
		Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListByType(new BasicDataTypeEnum[]{BasicDataTypeEnum.VERIFICATION_TYPE, BasicDataTypeEnum.VERIFICATION_RESULT, BasicDataTypeEnum.DISTINGUISH, BasicDataTypeEnum.CLIENT_NOTICE, BasicDataTypeEnum.DETECTION_UNIT});
		mav.addObject("verificationList", jcsjlist.get(BasicDataTypeEnum.VERIFICATION_TYPE.getCode()));//送检验证类型
		mav.addObject("verificationresultList", jcsjlist.get(BasicDataTypeEnum.VERIFICATION_RESULT.getCode()));//送检验证结果
		mav.addObject("distinguishList", jcsjlist.get(BasicDataTypeEnum.DISTINGUISH.getCode()));//区分
		mav.addObject("clientnoticeList", jcsjlist.get(BasicDataTypeEnum.CLIENT_NOTICE.getCode()));//客户通知
		mav.addObject("detectionList", jcsjlist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("sjxxDto", sjxxDto_t);
		User user = getLoginInfo();
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.REAGENT_SETTINGS.getCode()+"_"+sjyzDto_t.getYzlb(),PersonalSettingEnum.PREIMER_NUMBER.getCode()});
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
		GrszDto grszxx_s = grszDtoMap.get(PersonalSettingEnum.REAGENT_SETTINGS.getCode() + "_" + sjyzDto_t.getYzlb());
		GrszDto grszxx_ywbh = grszDtoMap.get(PersonalSettingEnum.PREIMER_NUMBER.getCode());
		if(StringUtil.isBlank(sjyzDto_t.getSjph()) && grszxx_s!=null) {
			sjyzDto_t.setSjph(grszxx_s.getSzz());
			mav.addObject("sjxsbj", "1");
		}else{
			mav.addObject("sjxsbj", "0");
		}
		if(StringUtil.isBlank(sjyzDto_t.getYwbh()) && grszxx_ywbh!=null) {
			sjyzDto_t.setYwbh(grszxx_ywbh.getSzz());
			mav.addObject("ywxsbj", "1");
		}else{
			mav.addObject("ywxsbj", "0");
		}
		mav.addObject("sjyzDto", sjyzDto_t);
		mav.addObject("formAction", "/verification/verification/modSaveAuditVerify");
		mav.addObject("yzmxlist", yzmxlist);
		mav.addObject("xsbj", "1");
        mav.addObject("btnFlg","audit");
		mav.addObject("url", "/verification/verification/commonModJcdw");
		return mav;
	}
	
	/**
	 * 钉钉查看页面
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/dingTalkverification/minidataViewVerifi")
	@ResponseBody
	public Map<String,Object> minidataViewVerifi(SjyzDto sjyzDto, HttpServletRequest request) {
		Map<String,Object> map= new HashMap<>();
		//查询送检验证信息
		SjyzDto sjyzDto_t=sjyzService.getDto(sjyzDto);
		SjxxDto sjxxDto_t = new SjxxDto();
		List<FjcfbDto> fjcfbDtos = null;
		List<SjyzjgDto> yzmxlist = null;
		if(sjyzDto_t != null) {
			//查询送检信息
			SjxxDto sjxxDto=new SjxxDto();
			sjxxDto.setSjid(sjyzDto_t.getSjid());
			sjxxDto_t=sjxxService.getDto(sjxxDto);
			fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(sjyzDto.getYzid());
			SjyzjgDto sjyzjgDto=new SjyzjgDto();
			sjyzjgDto.setYzid(sjyzDto.getYzid());
			yzmxlist=sjyzjgService.getDtoList(sjyzjgDto);
		}else {
			sjyzDto_t = new SjyzDto();
		}
		map.put("sjxxDto", sjxxDto_t);
		map.put("sjyzDto", sjyzDto_t);
		map.put("fjcfbDtos", fjcfbDtos);
		map.put("yzmxlist", yzmxlist);
		map.put("shlb", AuditTypeEnum.AUDIT_VERIFICATION.getCode());
		screenClassColumns(request,map);
		return map;
	}
	
	/**
	 * 修改检测单位
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/commonModJcdw")
	@ResponseBody
	public Map<String,Object> updateJcdw(SjyzDto sjyzDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		sjyzDto.setXgry(user.getYhid());
		boolean result=sjyzService.update(sjyzDto);
		if (StringUtil.isNotBlank(sjyzDto.getJcdw())){
			if(result) {
				sjyzService.sendUpdateJcdwMessage(sjyzDto);
			}
		}
		map.put("status", result);
		return map;
	}
	
	/**
	 * 钉钉小程序修改检测单位
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/minidataUpdateJcdw")
	@ResponseBody
	public Map<String,Object> minidataUpdateJcdw(SjyzDto sjyzDto, HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo(request);
		sjyzDto.setXgry(user.getYhid());
		boolean result=sjyzService.update(sjyzDto);
		if(result) {
			sjyzService.sendUpdateJcdwMessage(sjyzDto);
		}
		map.put("status", result);
		map.put("message", result?"修改成功！":"修改失败！");
		return map;
	}	

	/**
	 * 送检验证审核列表生成对接PCR文档
	 * @param sjyzDto
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/verification/pcrdockingfilePcr")
	public ModelAndView pcrVerification(SjyzDto sjyzDto) {
		ModelAndView mav=new ModelAndView("wechat/verification/verification_pcr");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.PCRMACHINE_ID, BasicDataTypeEnum.PCR_TEMPLATE});
		for (JcsjDto jcsjDto : jclist.get("PCRMACHINE_ID")){
			if ("Gentier 96R".equals(jcsjDto.getCsmc())){
				sjyzDto.setMachineId(jcsjDto.getCsdm());
			}
		}
		mav.addObject("machineIdList", jclist.get("PCRMACHINE_ID"));//机器ID
		mav.addObject("sjyzDto", sjyzDto);
		return mav;
	}

	/**
	 * 根据验证IDS获取数据
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/pagedataSjyzList")
	@ResponseBody
	public List<SjyzDto> pagedataSjyzList(SjyzDto sjyzDto) {
		List<SjyzDto> sjyzDtos = new ArrayList<>();
		if (sjyzDto.getIds().size()>0){
			sjyzDtos = sjyzService.getDtoByIds(sjyzDto);
		}
		return sjyzDtos;
	}

	/**
	 * 生成对接文档
	 * @param sjyzDto
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/verification/pcrdockingfileSavePcr")
	@ResponseBody
	public Map<String,Object> verificationPCRFile(SjyzDto sjyzDto){
		Map<String,Object> map= new HashMap<>();
		List<Plate> platelist = new ArrayList<>();
		GenerateFileForPCR tool = new GenerateFileForPCR();
		//List<SjyzDto> sjyzlist = new ArrayList<SjyzDto>();
		String createfilepath = null;
		String yzmc = sjyzDto.getYzdm()+sjyzDto.getHzxm();

		//文库PCR模板的基础数据
		JcsjDto wkDto = new JcsjDto();
		wkDto.setCsdm("YZ");
		wkDto.setJclb("PCR_TEMPLATE");		
		wkDto = jcsjService.getDtoByCsdmAndJclb(wkDto);
		if(wkDto == null) {
				map.put("status","fail");
				map.put("message","验证的PCR模板信息不存在");
				return map;
		}
		//文库PCR模板的附件存放信息
		FjcfbDto wk_fjDto=new FjcfbDto();
		wk_fjDto.setYwid(wkDto.getCsid());
		wk_fjDto.setYwlx(BusTypeEnum.IMP_PCR_TEMEPLATE.getCode());
		List<FjcfbDto> wk_fj=fjcfbService.selectFjcfbDtoByYwidAndYwlx(wk_fjDto);
		//文库存放路径解码
		DBEncrypt p = new DBEncrypt();
		String wk = wk_fj.get(0).getWjlj();
		String wjlj = p.dCode(wk);//检查一下文件路径
		
		String[] nbbmlist =  sjyzDto.getNbbms();
		List<String> xhlist = Arrays.asList(sjyzDto.getXhs());
		for(int i=0; i < xhlist.size(); i++) {
			Plate plate = new Plate();
			plate.setSampleName(nbbmlist[i].split("/").length>1?nbbmlist[i].split("/")[1]:"");
			plate.setSampleUid(nbbmlist[i].split("/")[0]);
			plate.setXh(xhlist.get(i));
			if ( plate.getSampleUid().contains("NC-") ){
				plate.setType("negative");
			}else if( plate.getSampleUid().contains("PC-") ||plate.getSampleUid().contains("DC-")){
				plate.setType("positive");
			}else{
				plate.setType("normal");
			}
			if ( Integer.parseInt(plate.getXh())>=1 && Integer.parseInt(plate.getXh())<=48 ){
				//1-48 结核非结核类型
				plate.setSampleType("MTM");
			}else{
				//49-96 耶氏/曲霉菌/..类型
				plate.setSampleType("ACP");
			}
			platelist.add(plate);
		}
		try {
			createfilepath = tool.createFileForYz(exportFilePath+BusTypeEnum.IMP_PCR_TEMEPLATE.getCode()+"/", sjyzDto.getMachineId(), wjlj, platelist);
			//上传文件到ftp
//			FTPClient ftp = FtpUtil.connectDir(sjyzDto.getSrlj(), addr, pcrport, username, password);
//			File file1 = new File(createfilepath);
//			FtpUtil.upload(file1,ftp);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
 			map.put("status","fail");
 			map.put("message",e.getMessage());
 			return map;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			map.put("status","fail");
			map.put("message",e.getMessage());
		}
		map.put("status","success");
 		map.put("message",xxglService.getModelById("ICOM00001").getXxnr());
 		map.put("wjm",createfilepath.substring(createfilepath.lastIndexOf("/")+1));
 		map.put("wjlj",wjlj);//模板文件路径
 		map.put("newWjlj",createfilepath);//生成的文件的路径
		return map;
	}	
	
	/**
	 * 送检验证列表的PCR对接文件下载
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/file/pagedataPredownPcrFile")
	public String predownloadFile(HttpServletRequest request,HttpServletResponse response){
		String wjm = request.getParameter("wjm");
		String newWjlj = request.getParameter("newWjlj");
		File downloadFile = new File(newWjlj); 
		try {
			if(wjm != null){
				wjm = URLEncoder.encode(wjm, "utf-8");
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setHeader("content-type", "application/octet-stream");
		response.setContentLength((int) downloadFile.length());
        //指明为下载
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + wjm);
        
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        InputStream iStream;
        OutputStream os = null; //输出流
        try {
        	iStream = new FileInputStream(newWjlj);
            os = response.getOutputStream();
            bis = new BufferedInputStream(iStream);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                os.flush();
                i = bis.read(buffer);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if(bis!=null)
                bis.close();
            if(os != null)
            {
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
	}


	/**
	 * 确认按钮
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping(value="/verification/confirmVerifi")
	public ModelAndView confirmVerifi(SjyzDto sjyzDto){
		ModelAndView mav=new ModelAndView("wechat/verification/verification_confirm");
		if(StringUtil.isNotBlank(sjyzDto.getYzid())){
			SjyzDto sjyzDto_t=sjyzService.getDto(sjyzDto);
			SjxxDto sjxxDto=new SjxxDto();
			sjxxDto.setSjid(sjyzDto_t.getSjid());
			SjxxDto sjxxDto_t=sjxxService.getDto(sjxxDto);
			mav.addObject("sjxxDto", sjxxDto_t);
		}else{
			SjxxDto sjxxDto=new SjxxDto();
			mav.addObject("sjxxDto", sjxxDto);
		}
		Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListByType(new BasicDataTypeEnum[]{BasicDataTypeEnum.VERIFICATION_TYPE, BasicDataTypeEnum.VERIFICATION_RESULT, BasicDataTypeEnum.DISTINGUISH, BasicDataTypeEnum.CLIENT_NOTICE, BasicDataTypeEnum.DETECTION_UNIT});
		mav.addObject("verificationList", jcsjlist.get(BasicDataTypeEnum.VERIFICATION_TYPE.getCode()));//送检验证类型
		mav.addObject("verificationresultList", jcsjlist.get(BasicDataTypeEnum.VERIFICATION_RESULT.getCode()));//送检验证结果
		mav.addObject("distinguishList", jcsjlist.get(BasicDataTypeEnum.DISTINGUISH.getCode()));//区分
		mav.addObject("clientnoticeList", jcsjlist.get(BasicDataTypeEnum.CLIENT_NOTICE.getCode()));//客户通知
		mav.addObject("detectionList", jcsjlist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("formAction", "/verification/verification/modSaveAuditVerify");
		mav.addObject("xsbj", "1");
		mav.addObject("btnFlg","audit");
		mav.addObject("url", "/verification/verification/commonModJcdw");
		mav.addObject("AuditType", AuditTypeEnum.AUDIT_VERIFICATION.getCode());
		mav.addObject("sjyzDto", sjyzDto);
		return mav;
	}

	/**
	 * 通过样本编号获取信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/verification/getInfoByYbbh")
	@ResponseBody
	public Map<String,Object> getInfoByYbbh(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		User user = getLoginInfo(request);
		sjxxDto.setJsid(user.getJsid());
		SjxxDto sjxxDto_t=sjxxService.getInfo(sjxxDto);
		SjyzDto sjyzDto=new SjyzDto();
		sjyzDto.setYzid(sjxxDto_t.getYzid());
		SjyzDto sjyzDto_t=sjyzService.getDto(sjyzDto);
		sjxxDto_t.setJcdw(sjyzDto_t.getJcdw());
		SjyzjgDto sjyzjgDto=new SjyzjgDto();
		sjyzjgDto.setYzid(sjyzDto.getYzid());
		List<SjyzjgDto> yzmxlist=sjyzjgService.getDtoList(sjyzjgDto);
		map.put("sjxxDto", sjxxDto_t);
		sjyzDto_t.setAuditType(AuditTypeEnum.AUDIT_VERIFICATION.getCode());
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.REAGENT_SETTINGS.getCode(),PersonalSettingEnum.PREIMER_NUMBER.getCode()});
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
		GrszDto grszxx_s = grszDtoMap.get(PersonalSettingEnum.REAGENT_SETTINGS.getCode());
		GrszDto grszxx_ywbh = grszDtoMap.get(PersonalSettingEnum.PREIMER_NUMBER.getCode());
		if(StringUtil.isBlank(sjyzDto_t.getSjph()) && grszxx_s!=null) {
			sjyzDto_t.setSjph(grszxx_s.getSzz());
			map.put("sjxsbj", "1");
		}else{
			map.put("sjxsbj", "0");
		}
		if(StringUtil.isBlank(sjyzDto_t.getYwbh()) && grszxx_ywbh!=null) {
			sjyzDto_t.setYwbh(grszxx_ywbh.getSzz());
			map.put("ywxsbj", "1");
		}else{
			map.put("ywxsbj", "0");
		}
		map.put("sjyzDto", sjyzDto_t);
		map.put("yzmxlist", yzmxlist);
		return map;
	}
	/**
	 * 验证类别改变重新获取试剂批号
	 * @return
	 */
	@RequestMapping("/verification/pagedataMessageInfo")
	@ResponseBody
	public Map<String,Object> getMessageInfo(SjyzDto sjyzDto){
		Map<String,Object> map=new HashMap<>();
		User user = getLoginInfo();
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlb(PersonalSettingEnum.REAGENT_SETTINGS.getCode()+"_"+sjyzDto.getYzlb());
		GrszDto grszxx_s = grszService.selectGrszDtoByYhidAndSzlb(grszDto);
		map.put("grszxx",grszxx_s);
		return map;
	}
    /**
     * 获取验证 未完成报告数
     * @return
     */
    @RequestMapping("/verification/pagedataUnFinishedReportCount")
    @ResponseBody
    public Map<String,Object> pagedataUnFinishedReportCount(){
        Map<String,Object> map=new HashMap<>();
        Integer wwcbgs = sjyzService.getUnFinishedReportCount();
        map.put("wwcbgs",wwcbgs);
        return map;
    }
	/**
	 * 获取验证 未完成报告数明细
	 * @return
	 */
	@RequestMapping("/verification/pagedataUnFinishedReportList")
	@ResponseBody
	public Map<String,Object> pagedataUnFinishedReportList(){
		Map<String,Object> map=new HashMap<>();
		List<Map<String, Object>> unFinishedReportList = sjyzService.getUnFinishedReportList();
		map.put("unFinishedReportList",unFinishedReportList);
		return map;
	}
}

