package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GrlbzdszDto;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JkdymxDto;
import com.matridx.igams.common.dao.entities.LbzdszDto;
import com.matridx.igams.common.dao.entities.LscxszDto;
import com.matridx.igams.common.dao.entities.SjycDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.BusinessTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.DetectionTypeEnum;
import com.matridx.igams.common.enums.EntityTypeEnum;
import com.matridx.igams.common.enums.GlobalParmEnum;
import com.matridx.igams.common.enums.InvokingChildTypeEnum;
import com.matridx.igams.common.enums.InvokingTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.enums.UserHabitTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGrlbzdszService;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJkdymxService;
import com.matridx.igams.common.service.svcinterface.ILbzdszService;
import com.matridx.igams.common.service.svcinterface.ILscxszService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.service.svcinterface.IYyxxService;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.common.util.WechatCommonUtils;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.FjsqyyDto;
import com.matridx.igams.wechat.dao.entities.FxsjglDto;
import com.matridx.igams.wechat.dao.entities.HbsfbzDto;
import com.matridx.igams.wechat.dao.entities.NyysxxDto;
import com.matridx.igams.wechat.dao.entities.PayinfoDto;
import com.matridx.igams.wechat.dao.entities.SjbgsmDto;
import com.matridx.igams.wechat.dao.entities.SjdlxxDto;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjjcjgDto;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjkdxxDto;
import com.matridx.igams.wechat.dao.entities.SjkzxxDto;
import com.matridx.igams.wechat.dao.entities.SjnyxDto;
import com.matridx.igams.wechat.dao.entities.SjsyglDto;
import com.matridx.igams.wechat.dao.entities.SjsyglModel;
import com.matridx.igams.wechat.dao.entities.SjtssqDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxjgDto;
import com.matridx.igams.wechat.dao.entities.SjybztDto;
import com.matridx.igams.wechat.dao.entities.SjyzDto;
import com.matridx.igams.wechat.dao.entities.SjzmjgDto;
import com.matridx.igams.wechat.dao.entities.SjzzsqDto;
import com.matridx.igams.wechat.dao.entities.WbsjxxDto;
import com.matridx.igams.wechat.dao.entities.WzysxxDto;
import com.matridx.igams.wechat.dao.entities.XmsyglDto;
import com.matridx.igams.wechat.dao.entities.XmsyglModel;
import com.matridx.igams.wechat.dao.entities.YhsyxgjlDto;
import com.matridx.igams.wechat.service.impl.ResendReport;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.IFjsqyyService;
import com.matridx.igams.wechat.service.svcinterface.IFxsjglService;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.IHbsfbzService;
import com.matridx.igams.wechat.service.svcinterface.INyysxxService;
import com.matridx.igams.wechat.service.svcinterface.IPayinfoService;
import com.matridx.igams.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.igams.wechat.service.svcinterface.ISjdlxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjdwxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjgzbyService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.igams.wechat.service.svcinterface.ISjkdxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjkzxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjlczzService;
import com.matridx.igams.wechat.service.svcinterface.ISjnyxService;
import com.matridx.igams.wechat.service.svcinterface.ISjqqjcService;
import com.matridx.igams.wechat.service.svcinterface.ISjsyglService;
import com.matridx.igams.wechat.service.svcinterface.ISjtssqService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxWsService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjybztService;
import com.matridx.igams.wechat.service.svcinterface.ISjyzService;
import com.matridx.igams.wechat.service.svcinterface.ISjzmjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjzzsqService;
import com.matridx.igams.wechat.service.svcinterface.IWbsjxxService;
import com.matridx.igams.wechat.service.svcinterface.IWzysxxService;
import com.matridx.igams.wechat.service.svcinterface.IXmsyglService;
import com.matridx.igams.wechat.service.svcinterface.IYhsyxgjlService;
import com.matridx.igams.wechat.util.MatchingUtil;
import com.matridx.igams.wechat.util.ResendReportThread;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * /inspection/recheck/submitRecheckAudit	ybbh,access_token,yy	--外部提交复检审核（生信系统提交）
 * @author yao
 *
 */
@Controller
@RequestMapping("/inspection")
public class InspectionController extends BaseController{
	@Autowired
	private ISjxxService sjxxservice;
	@Autowired
	private IFxsjglService fxsjglService;
	@Autowired
	private ISjsyglService sjsyglService;
	@Autowired
	private IXmsyglService xmsyglService;
	@Autowired
	IShgcService shgcService;
	@Autowired
	private ISjxxWsService sjxxWsService;
	@Autowired
	private IXxglService xxglservice;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	private ISjjcxmService sjjcxmservice;
	@Autowired
	private IXxdyService xxdyService;
	@Autowired
	private ISjqqjcService sjqqjcservice;
	@Autowired
	private ISjgzbyService sjgzbyservice;
	@Autowired
	private ISjlczzService sjlczzservice;
	@Autowired
	private IFjcfbService fjcfbService;
	@Autowired
	private ISjhbxxService sjhbxxservice;
	@Autowired
	private ISjbgsmService sjbgsmservice;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
	@Autowired
	private ISjybztService sjybztService;
	@Autowired
	private ILbzdszService lbzdszService;
	@Autowired
	private IGrlbzdszService grlbzdszService;
	@Autowired
	private ISjnyxService sjnyxService;
	@Autowired
	private IFjsqService fjsqService;
	@Autowired
	private ISjxxjgService sjxxjgService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Autowired
	private ICommonService commonService;
	private Logger log = LoggerFactory.getLogger(InspectionController.class);
	@Autowired
	private IGrszService grszService;
	@Autowired
	private ISjyzService sjyzService;
	@Autowired
	private ISjzmjgService sjzmjgService;
	@Autowired
	private IHbqxService hbqxService;
	@Autowired
	private IPayinfoService payinfoService;
	@Autowired
	private IFjsqyyService fjsqyyService;
	@Autowired
	private ISjzzsqService sjzzsqService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IYhsyxgjlService yhsyxgjlService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	WechatCommonUtils wechatCommonUtils;
	@Autowired
	ISjkdxxService sjkdxxService;
	@Autowired
	private ISjtssqService sjtssqService;
	@Autowired
	private ISjjcjgService sjjcjgService;
	@Autowired
	private ISjkzxxService sjkzxxService;
	@Autowired
	private IHbsfbzService hbsfbzService;
	@Autowired
	private IWbsjxxService wbsjxxService;

	@Autowired
	private IWzysxxService wzysxxService;

	@Autowired
	private INyysxxService nyysxxService;
	@Autowired
	private IYyxxService yyxxService;
	@Autowired
	private ISjdwxxService sjdwxxService;

	@Value("${matridx.dingtalk.jumpdingtalkurl:}")
	private String jumpdingtalkurl;
	//http://dx.matridx.com:8000
	@Value("${matridx.wechat.bioaudurl:}")
	private String bioaudurl;

	@Autowired
	private ILscxszService lscxszService;

	@Autowired
	private ISjdlxxService sjdlxxService;
	@Autowired
	IJkdymxService jkdymxService;
	@Autowired
	ISjbgsmService sjbgsmService;
	@Value("${matridx.adk.logid:}")
	private String ADK_LOGID;
	@Value("${matridx.adk.password:}")
	private String ADK_PASSWORD;
	@Value("${matridx.adk.requesturl:}")
	private String ADK_REQUESTURL;
	@Value("${matridx.yuhuangding.wsdlurl:}")
	private String yuhuangding_wsdlurl;

	/**
	 * 测试使用方法
	 */
	@RequestMapping(value="/test/kdhtest")
	@ResponseBody
	public boolean test(){
        return sjxxservice.kdhRemind();
	}

	/**
	 * 测试使用方法
	 */
	@RequestMapping(value="/test/jdtest")
	@ResponseBody
	public boolean jdtest(){
        return sjxxservice.expressRemind();
	}

	/**
	* 跳转页面
	* @return
	*/
	@RequestMapping(value="/inspection/pageListInspection")
	public ModelAndView pageListInspection(SjxxDto sjxxDto,HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_List");
		User user=getLoginInfo();
		List<SjdwxxDto> sjdwxxlist=sjxxservice.getSjdw();
		mav.addObject("sjdwxxlist", sjdwxxlist);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.FIRST_SJXXKZ,
		BasicDataTypeEnum.SECOND_SJXXKZ,BasicDataTypeEnum.THIRD_SJXXKZ,BasicDataTypeEnum.FOURTH_SJXXKZ,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.SD_TYPE,
		BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.STAMP_TYPE,BasicDataTypeEnum.CLASSIFY,BasicDataTypeEnum.INSPECTION_DIVISION,BasicDataTypeEnum.RESEARCH_PROJECT,BasicDataTypeEnum.PLATFORM_OWNERSHIP,BasicDataTypeEnum.PLATFORM_OWNERSHIP_ORIGIN});
		List<JcsjDto> detectlist = jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
		mav.addObject("samplelist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
		List<JcsjDto> kyxmList = jclist.get(BasicDataTypeEnum.RESEARCH_PROJECT.getCode());
		mav.addObject("kylist", kyxmList);//科研项目
		List<String> kylxList = new ArrayList<>();
		for (JcsjDto kyxm : kyxmList) {
			String kylx = StringUtil.isNotBlank(kyxm.getCskz1()) ? kyxm.getCskz1() : "其它";
			boolean isIn = false;
			for (String kylxmc : kylxList) {
				if (kylxmc.equals(kylx)){
					isIn = true;
					break;
				}
			}
			if (!isIn){
				kylxList.add(kylx);
			}
		}
		mav.addObject("zyid",request.getParameter("zyid"));
		mav.addObject("querydivision",request.getParameter("querydivision"));
		mav.addObject("kylxList", kylxList);//科研项目类型
		mav.addObject("cskz1List", jclist.get(BasicDataTypeEnum.FIRST_SJXXKZ.getCode()));
    	mav.addObject("cskz2List", jclist.get(BasicDataTypeEnum.SECOND_SJXXKZ.getCode()));
    	mav.addObject("cskz3List", jclist.get(BasicDataTypeEnum.THIRD_SJXXKZ.getCode()));
    	mav.addObject("cskz4List", jclist.get(BasicDataTypeEnum.FOURTH_SJXXKZ.getCode()));
    	mav.addObject("expressage", jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
    	mav.addObject("stamplist", jclist.get(BasicDataTypeEnum.STAMP_TYPE.getCode()));//盖章类型
    	mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
    	mav.addObject("sjhbflList", jclist.get(BasicDataTypeEnum.CLASSIFY.getCode()));//合作伙伴分类
    	mav.addObject("sjqfList", jclist.get(BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));//送检区分
    	mav.addObject("xsbmList", jclist.get(BasicDataTypeEnum.PLATFORM_OWNERSHIP.getCode()));//销售部门
		mav.addObject("ptgsList", jclist.get(BasicDataTypeEnum.PLATFORM_OWNERSHIP_ORIGIN.getCode()));//平台归属
    	// 查询合作伙伴
    	//List<SjhbxxDto> sjhbxxlist = sjhbxxservice.getDtoList(null);
    	//mav.addObject("sjhbxxlist", sjhbxxlist);
		String zdYwid = "INSPECT";
		if ("2".equals(sjxxDto.getSingle_flag())){
			zdYwid = "AIDIKANG_INSPECT";
		}
    	GrlbzdszDto grlbzdszDto = new GrlbzdszDto();
    	grlbzdszDto.setYhid(getLoginInfo().getYhid());
    	grlbzdszDto.setYwid(zdYwid);
    	LbzdszDto lbzdszDto = new LbzdszDto();
    	lbzdszDto.setYwid(zdYwid);
    	lbzdszDto.setYhid(user.getYhid());
    	lbzdszDto.setJsid(user.getDqjs());
		List<LbzdszDto> choseList = grlbzdszService.getChoseList(grlbzdszDto);
		List<LbzdszDto> waitList = lbzdszService.getWaitList(lbzdszDto);
//		List<String> zdList = lbzdszService.getSjxxZdList();//从列表字段设置表中获取默认设置为 1显示、0隐藏、3角色限制、9主键的SQL字段
		String xszdlist = "";
		for (LbzdszDto lbzdszdto: choseList) {
			xszdlist = xszdlist+","+lbzdszdto.getXszd();
		}
		for (LbzdszDto lbzdszdto: waitList) {
			xszdlist = xszdlist+","+lbzdszdto.getXszd();
		}
		String limitColumns = "";
		if (StringUtil.isNotBlank(xszdlist)){
			limitColumns = "{'sjxxDto':'"+xszdlist.substring(1)+"'}";
		}
		//查看角色是否有该字段权限
		Map<String, String> map = new HashMap<>();
		map.put("jsid",user.getDqjs());
		map.put("ywid","STATISTICS");
		List<LbzdszDto> lbzdszDtos = lbzdszService.getXszdQx(map);
		String xszds = "";
		for (LbzdszDto lbzdszDto_t : lbzdszDtos) {
			xszds = xszds+","+lbzdszDto_t.getXszd();
		}
		String jcxmids = "";
		List<JcsjDto> jcxmlist = new ArrayList<>();
		if (!CollectionUtils.isEmpty(detectlist)&&!CollectionUtils.isEmpty(sjxxDto.getJcxmids())){
			for (JcsjDto jcsjDto : detectlist) {
				for (String jcxmid : sjxxDto.getJcxmids()) {
					if (jcsjDto.getCsdm().equals(jcxmid)){
						jcxmids = jcxmids+","+jcsjDto.getCsid();
						jcxmlist.add(jcsjDto);
					}
				}
			}
			if (jcxmids.length()>0){
				jcxmids = jcxmids.substring(1);
			}
		}
		String jclx="";
		List<JcsjDto> jclxlist = new ArrayList<>();
		if (!CollectionUtils.isEmpty(detectlist)&&StringUtil.isNotBlank(sjxxDto.getJclx())){
			for (JcsjDto jcsjDto : detectlist) {
				if (sjxxDto.getJclx().contains(jcsjDto.getCsdm())){
					jclx =jclx+","+jcsjDto.getCsid();
				}
			}
			jclx=jclx.substring(1);
		}
		if (!CollectionUtils.isEmpty(jcxmlist)){
			mav.addObject("detectlist", jcxmlist);//检测项目
		}else {
			mav.addObject("detectlist", detectlist);//检测项目
		}
		mav.addObject("sjxxDto",sjxxDto);
		mav.addObject("jcxmids",jcxmids);
		mav.addObject("jclx",jclx);
		mav.addObject("xszds",xszds);
		mav.addObject("limitColumns",limitColumns);
		mav.addObject("choseList", choseList);
		mav.addObject("waitList", waitList);
		return mav;
	}

	/**
	* 送检信息列表
	* @param sjxxDto
	* @return
	*/
	@RequestMapping(value="/inspection/pageGetListInspection")
	@ResponseBody
	public Map<String, Object> pageGetListInspection(SjxxDto sjxxDto,HttpServletRequest request){
		User user=getLoginInfo();
		List<SjxxDto> sjxxlist;
		List<Map<String,String>> jcdwList=sjxxservice.getJsjcdwByjsid(user.getDqjs());
		if(!CollectionUtils.isEmpty(jcdwList) && "1".equals(jcdwList.get(0).get("dwxdbj"))) {
			//判断是否显示个人清单
			if("1".equals(sjxxDto.getSingle_flag()) || "2".equals(sjxxDto.getSingle_flag())) {
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
					sjxxDto.setUserids(userids);
				}

				//判断伙伴权限
				List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
				if(hbqxList!=null && hbqxList.size()>0) {
					List<String> hbmcList=sjhbxxservice.getHbmcByHbid(hbqxList);
					if(hbmcList!=null  && hbmcList.size()>0) {
						sjxxDto.setSjhbs(hbmcList);
					}
				}
			}
			List<String> strList= new ArrayList<>();

			for (int i = 0; i < jcdwList.size(); i++){
				if(jcdwList.get(i).get("jcdw")!=null) {
					strList.add(jcdwList.get(i).get("jcdw"));
				}
			}
			if(strList!=null && strList.size()>0) {
				sjxxDto.setJcdwxz(strList);
				Map<String,Object> params = sjxxservice.pareMapFromDto(sjxxDto);
				String s_sjhbfl = request.getParameter("sjhbfls");
				if(StringUtil.isNotBlank(s_sjhbfl)) {
					String[] sjhbfls = s_sjhbfl.split(",");
					for (int i = 0; i < sjhbfls.length; i++){
						sjhbfls[i] = sjhbfls[i].replace("'","");
					}
					params.put("sjhbfls", sjhbfls);
				}
				String kylxs = request.getParameter("kylxs");
				if(StringUtil.isNotBlank(kylxs)) {
					String[] s_kylxs = kylxs.split(",");
					for (int i = 0; i < s_kylxs.length; i++){
						s_kylxs[i] = s_kylxs[i].replace("'","");
					}
					params.put("kylxs", s_kylxs);
				}
				String s_jczxms = request.getParameter("jczxms");
				if(StringUtil.isNotBlank(s_jczxms)) {
					String[] jczxms = s_jczxms.split(",");
					for (int i = 0; i < jczxms.length; i++){
						jczxms[i] = jczxms[i].replace("'","");
					}
					params.put("jczxms", jczxms);
				}
				sjxxlist=sjxxservice.getDtoListOptimize(params);
				sjxxDto.setTotalNumber((int)params.get("totalNumber"));
			}else {
				sjxxlist= new ArrayList<>();
			}
		}else {
			Map<String,Object> params = sjxxservice.pareMapFromDto(sjxxDto);
			String s_sjhbfl = request.getParameter("sjhbfls");
			if(StringUtil.isNotBlank(s_sjhbfl)) {
				String[] sjhbfls = s_sjhbfl.split(",");
				for (int i = 0; i < sjhbfls.length; i++){
					sjhbfls[i] = sjhbfls[i].replace("'","");
				}
				params.put("sjhbfls", sjhbfls);
			}
			String kylxs = request.getParameter("kylxs");
			if(StringUtil.isNotBlank(kylxs)) {
				String[] s_kylxs = kylxs.split(",");
				for (int i = 0; i < s_kylxs.length; i++){
					s_kylxs[i] = s_kylxs[i].replace("'","");
				}
				params.put("kylxs", s_kylxs);
			}
			String s_jczxms = request.getParameter("jczxms");
			if(StringUtil.isNotBlank(s_jczxms)) {
				String[] jczxms = s_jczxms.split(",");
				for (int i = 0; i < jczxms.length; i++){
					jczxms[i] = jczxms[i].replace("'","");
				}
				params.put("jczxms", jczxms);
			}
			sjxxlist=sjxxservice.getDtoListOptimize(params);
			sjxxDto.setTotalNumber((int)params.get("totalNumber"));
		}
		Map<String, Object> map= new HashMap<>();
		//判断是否有实付金额字段权限，有就计算
		if (StringUtil.isNotBlank(sjxxDto.getSfzjezdqx())&&"1".equals(sjxxDto.getSfzjezdqx())){
			SjxxDto sjxxDto_z = sjxxservice.getSfzjeAndTkzje(sjxxDto);
			map.put("sfzje", sjxxDto_z.getSfzje());
		}
		if (StringUtil.isNotBlank(sjxxDto.getTkzjezdqx())&&"1".equals(sjxxDto.getTkzjezdqx())){
			SjxxDto sjxxDto_z = sjxxservice.getSfzjeAndTkzje(sjxxDto);
			map.put("tkzje", sjxxDto_z.getTkzje());
		}
		map.put("total", sjxxDto.getTotalNumber());
		map.put("rows", sjxxlist);
		//需要筛选钉钉字段的，请调用该方法
		screenClassColumns(request,map);
		return map;
	}
	/**
	 * 其他调用查看送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/pagedataViewSjxx")
	public ModelAndView pagedataViewSjxx(SjxxDto sjxxDto) {
		return this.viewSjxx(sjxxDto);
	}
	/**
	* 查看送检信息
	* @param sjxxDto
	* @return
	*/
	@RequestMapping(value="/inspection/viewSjxx")
	public ModelAndView viewSjxx(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_ListView");
		List<SjnyxDto> sjnyx=sjnyxService.getNyxBySjid(sjxxDto);
		SjxxDto sjxxDto2=sjxxservice.getDto(sjxxDto);
		List<SjwzxxDto> sjwzxx=sjxxservice.selectWzxxBySjid(sjxxDto2);
		if(sjwzxx!=null && sjwzxx.size()>0) {
			String xpxx=sjwzxx.get(0).getXpxx();//由于一个标本中的物种芯片信息相同，取其一
			mav.addObject("Xpxx", xpxx);
		}

//        if(("Z6").equals(sjxxDto2.getCskz1()) || ("Z12").equals(sjxxDto2.getCskz1()) || ("Z18").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())) {
        if(("Z").equals(sjxxDto2.getCskz1()) || ("Z6").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())) {
        	SjzmjgDto sjzmjgDto=new SjzmjgDto();
        	sjzmjgDto.setSjid(sjxxDto.getSjid());
			List<SjzmjgDto> sjzmList=sjzmjgService.getDtoList(sjzmjgDto);
        	mav.addObject("sjzmList", sjzmList);
        	mav.addObject("KZCS",sjxxDto2.getCskz1());
        	log.error(sjxxDto2.getCskz1());
        	log.error(JSON.toJSONString(sjzmList));
        }
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());
		fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionPdfYwlxs());

		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionWordYwlxs());
		List<FjcfbDto> zhwj=fjcfbService.selectzhpdf(fjcfbDto);
		fjcfbDto.setYwlxs(null);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		//收样附件
		fjcfbDto.setYwlxs(null);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION_CONFIRM.getCode());
		List<FjcfbDto> b_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		SjbgsmDto sjbgsmdto=new SjbgsmDto();
		sjbgsmdto.setSjid(sjxxDto.getSjid());
		List<SjbgsmDto> sjbgsmxx=sjbgsmservice.selectSjbgBySjid(sjbgsmdto);


		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE});
		List<JcsjDto> jcxmlist=jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> t_jcxmlist= new ArrayList<>();//用于结果页
		//List<JcsjDto> c_jcxmlist= new ArrayList<>();//用于详细页

		if(jcxmlist!=null && jcxmlist.size()>0) {
			for(int i=0;i<jcxmlist.size();i++) {

				boolean sftj=false;//判断对应该检测项目的报告说明和附件是否存在，若其中一个存在添加该项目
				//查看页面自勉报告不显示问题 // 判断WORD附件 不判断PDF附件  auther:zhanghan  2020/12/22 
//				if(("Z6").equals(sjxxDto2.getCskz1())|| ("Z8").equals(sjxxDto2.getCskz1()) || ("Z12").equals(sjxxDto2.getCskz1()) || ("Z18").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())|| ("T3").equals(sjxxDto2.getCskz1()) || ("T6").equals(sjxxDto2.getCskz1()) || ("K").equals(sjxxDto2.getCskz1())|| ("X").equals(sjxxDto2.getCskz1())|| ("Y").equals(sjxxDto2.getCskz1())|| ("G").equals(sjxxDto2.getCskz1())) {
				if(("Z").equals(sjxxDto2.getCskz1()) ||("Z6").equals(sjxxDto2.getCskz1())|| ("F").equals(sjxxDto2.getCskz1())|| ("T3").equals(sjxxDto2.getCskz1()) || ("T6").equals(sjxxDto2.getCskz1()) || ("K").equals(sjxxDto2.getCskz1())|| ("X").equals(sjxxDto2.getCskz1())|| ("Y").equals(sjxxDto2.getCskz1())|| ("G").equals(sjxxDto2.getCskz1())) {
					if(zhwj!=null && zhwj.size()>0) {
						for (int j = 0; j < zhwj.size(); j++) {
							if(zhwj.get(j).getYwlx().equals((jcxmlist.get(i).getCskz3()+"_"+jcxmlist.get(i).getCskz1()+"_WORD"))) {
								sftj=true;
								break;
							}
						}
					}
				}
				if(!sftj&&sjbgsmxx!=null && sjbgsmxx.size()>0) {
					boolean isFindSub = false;
					for(int j=0;j<sjbgsmxx.size();j++) {
						if(jcxmlist.get(i).getCsid().equals(sjbgsmxx.get(j).getJcxmid())) {
							//确认是否为 检测子项目的数据，因为框架不想调整，所以如果是，则直接设置，并进行下一个
							if(StringUtil.isNotBlank(sjbgsmxx.get(j).getJczlx()) ) {
								Object o_sub_jcsj = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode(), sjbgsmxx.get(j).getJczlx());
								if (o_sub_jcsj == null)
									continue;
								JcsjDto sub_jcsj_dto = JSONObject.parseObject(o_sub_jcsj.toString(), JcsjDto.class);

								Object o_jcsj = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), sub_jcsj_dto.getFcsid());
								if (o_jcsj != null) {
									JcsjDto jcsj_dto = JSONObject.parseObject(o_jcsj.toString(), JcsjDto.class);
									sub_jcsj_dto.setFcsdm(jcsj_dto.getCsdm());
									sub_jcsj_dto.setFcsmc(jcsj_dto.getCsmc());
								}

								t_jcxmlist.add(sub_jcsj_dto);
								isFindSub = true;
							}else{
								sftj=true;
								break;
							}
						}
					}
					if(isFindSub)
						continue;
				}else if(!sftj&&t_fjcfbDtos!=null && t_fjcfbDtos.size()>0) {
					String cskz3 = jcxmlist.get(i).getCskz3();
					String cskz1 = jcxmlist.get(i).getCskz1();
					for(int j=0;j<t_fjcfbDtos.size();j++) {
						if(t_fjcfbDtos.get(j).getYwlx().equals((cskz3==null?"":cskz3.replace("_ONCO", "") + cskz1==null?"":cskz1))) {
							sftj=true;
							break;
						}
					}
				}
				if(!sftj&&fjcfbDtos!=null && fjcfbDtos.size()>0) {
					String cskz3 = jcxmlist.get(i).getCskz3();
					String cskz1 = jcxmlist.get(i).getCskz1();
					for(int j=0;j<fjcfbDtos.size();j++) {
						if(fjcfbDtos.get(j).getYwlx().equals((cskz3==null?"":cskz3.replace("_ONCO", "") + cskz1==null?"":cskz1)) || fjcfbDtos.get(j).getYwlx().equals((cskz3==null?"":cskz3) +"_"+ (cskz1==null?"":cskz1))) {
							sftj=true;
							break;
						}
					}
				}
				if(sftj)
					t_jcxmlist.add(jcxmlist.get(i));
			}
		}

		SjxxjgDto sjxxjgDto=new SjxxjgDto();
		sjxxjgDto.setSjid(sjxxDto.getSjid());
		List<SjxxjgDto> getJclxCount=sjxxjgService.getJclxCount(sjxxjgDto);
		//查看当前复检申请信息
		FjsqDto fjsqDto=new FjsqDto();
		String[] zts= {StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_PASS.getCode()};
		fjsqDto.setZts(zts);
		fjsqDto.setSjid(sjxxDto2.getSjid());
		List<FjsqDto> fjsqList=fjsqService.getListBySjid(fjsqDto);
		FjcfbDto fjcfbDto1 = new FjcfbDto();
		fjcfbDto1.setYwid(sjxxDto.getSjid());
		fjcfbDto1.setYwlx(BusTypeEnum.IMP_FILE_RFS_TEMEPLATE.getCode());
		List<FjcfbDto> fjcfbDtoList = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto1);
		SjjcjgDto sjjcjgDto=new SjjcjgDto();
		sjjcjgDto.setYwid(sjxxDto.getSjid());
		List<SjjcjgDto> dtoList = sjjcjgService.getDtoList(sjjcjgDto);
		String nyjy="";
		if(dtoList!=null&&dtoList.size()>0){
			for(SjjcjgDto dto:dtoList){
				if("001".equals(dto.getJldm())){//判断阳性物种
					if(dto.getJcjgdm().contains("NY")){//耐药物种处理
						nyjy=nyjy+","+dto.getJcjgmc();
					}
				}
			}
		}
		if(StringUtil.isNotBlank(nyjy)){
			nyjy=nyjy.substring(1);
		}
		SjsyglDto sjsyglDto=new SjsyglDto();
		sjsyglDto.setYwid(sjxxDto.getSjid());
		List<SjsyglDto> sjsyglDtos = sjsyglService.getSyxxViewByYwid(sjsyglDto);
		List<SjyzDto> yzxxList = sjyzService.getDtoListBySjid(sjxxDto.getSjid());
		mav.addObject("nyjys",nyjy);
		mav.addObject("rfs_fjcfbDtos",fjcfbDtoList);
		mav.addObject("fjsqList", fjsqList);
		mav.addObject("yzxxList", yzxxList);
        mav.addObject("SjxxjgList", getJclxCount);
		mav.addObject("SjnyxDto", sjnyx);
		mav.addObject("zhwjpdf", zhwj);
		mav.addObject("sjbgsmList",sjbgsmxx);
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		mav.addObject("sjxxDto", sjxxDto2);
		mav.addObject("Sjwzxx", sjwzxx);
        mav.addObject("jcxmlist",t_jcxmlist);
        mav.addObject("b_fjcfbDtos", b_fjcfbDtos);
        mav.addObject("sjsyglDtos", sjsyglDtos);
		FxsjglDto fxsjglDto = new FxsjglDto();
		fxsjglDto.setYwid(sjxxDto.getSjid());
		List<FxsjglDto> dtoList1 = fxsjglService.getDtoList(fxsjglDto);
		mav.addObject("fxsjglDtos", dtoList1);
		List<WbsjxxDto> wbsjxxDtos = wbsjxxService.getListBySjid(sjxxDto.getSjid());
		mav.addObject("wbsjxxDtos", wbsjxxDtos);

		if(bioaudurl.endsWith("/"))
			bioaudurl = bioaudurl.substring(0, bioaudurl.length() - 1);
		mav.addObject("bioaudurl",bioaudurl);

		//获取项目名称
		/*SjjcxmDto sjjcxmDto = new SjjcxmDto();
		sjjcxmDto.setSjid(sjxxDto.getSjid());
		List<SjjcxmDto> xmdtoList = sjjcxmservice.getDtoList(sjjcxmDto);
		String jczxmmc	="";
		for(SjjcxmDto t_sjjcxmDto:xmdtoList){
			jczxmmc += t_sjjcxmDto.getJcxmmc() + ""+ t_sjjcxmDto.getJczxmmc() + ",";
		}*/
		return mav;
	}

	/**
	* 修改操作
	* @param sjxxDto
	* @return
	*/
	@RequestMapping(value="/inspection/modSjxx")
	public ModelAndView modSjxx(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_ListSave");
		SjxxDto t_sjxxDto=sjxxservice.getDtoById(sjxxDto.getSjid());
		List<SjdwxxDto> sjdwxxList= sjxxservice.getSjdw();//查询出所有送检单位；
		if (StringUtil.isNotBlank(t_sjxxDto.getSyrq()) || StringUtil.isNotBlank(t_sjxxDto.getDsyrq()) || StringUtil.isNotBlank(t_sjxxDto.getQtsyrq()))
			mav.addObject("sybj", "1");

		SjjcxmDto sjjcxmDto = new SjjcxmDto();
		sjjcxmDto.setSjid(sjxxDto.getSjid());
		List<SjjcxmDto> dtoList = sjjcxmservice.getDtoList(sjjcxmDto);
		List<String> jcxmids = new ArrayList<>();
		List<String> jczxmids = new ArrayList<>();

		for (SjjcxmDto dto : dtoList) {
			jcxmids.add(dto.getJcxmid());
			jczxmids.add(dto.getJczxmid());
		}
		t_sjxxDto.setJcxmids(jcxmids);//把查询出来的送检项目set到送检信息中去；
		List<JcsjDto> jczxmlist= new ArrayList<>();
		if (!CollectionUtils.isEmpty(jcxmids)){
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setFcsidList(jcxmids);
			jczxmlist = jcsjService.getListByFid(jcsjDto);
		}
		mav.addObject("dtoList", JSONObject.toJSONString(dtoList));
		mav.addObject("jczxmids", jczxmids);
		mav.addObject("jczxmlist", jczxmlist);

		t_sjxxDto.setBys(sjgzbyservice.getGzby(sjxxDto.getSjid()));//把查询出来的关注病原set到送检信息中去；
		t_sjxxDto.setLczzs(sjlczzservice.getLczz(sjxxDto.getSjid()));//把查询出来的临床症状set到送检信息中去；
		t_sjxxDto.setSjqqjcs(sjqqjcservice.getJcz(sjxxDto.getSjid()));//查询前期检测项目；
		t_sjxxDto.setZts(sjybztService.getZtBysjid(sjxxDto.getSjid()));
		t_sjxxDto.setFormAction("modSaveSjxx");
		t_sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		t_sjxxDto.setXg_flg(sjxxDto.getXg_flg());
		List<FjcfbDto> fjcfbDtos=sjxxservice.selectFjByWjid(sjxxDto.getSjid());

		SjkzxxDto sjkzxxDto_t = sjkzxxService.getDtoById(sjxxDto.getSjid());
		if(sjkzxxDto_t!=null){
			mav.addObject("sjkzxxDto", sjkzxxDto_t);
		}else{
			mav.addObject("sjkzxxDto", new SjkzxxDto());
		}
		mav.addObject("divisionListJson",JSONObject.toJSONString(redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode())) );//送检区分
		mav.addObject("sjxxDto", t_sjxxDto);
		mav.addObject("sjdwxxList", sjdwxxList);
		mav.addObject("clincallist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.CLINICAL_TYPE.getCode()));//临床症状
    	mav.addObject("pathogenylist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));//病原
    	mav.addObject("samplelist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
    	mav.addObject("datectlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
    	mav.addObject("detectionlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_TYPE.getCode()));//前期检测
    	mav.addObject("samplestate", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SAMPLESTATE.getCode()));//标本状态
    	mav.addObject("expressage", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
    	mav.addObject("self_projectList", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SELFEXEMPTION_PROJECT.getCode()));//自免项目
     	mav.addObject("self_destinationList", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//自免目的地
     	mav.addObject("divisionList",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode()) );//送检区分
		mav.addObject("invoiceList",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.INVOICE_APPLICATION.getCode()));//开票申请
		mav.addObject("refundlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.REFUND_METHOD.getCode()));// 退款方式
		mav.addObject("kyxmlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.RESEARCH_PROJECT.getCode()));// 科研项目
		List<JcsjDto>jcdwList=redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		Optional<JcsjDto> optional=jcdwList.stream().filter(x->x.getCsid().equals(t_sjxxDto.getJcdw())).findFirst();
		mav.addObject("ADD_ALLOWED", "1");
		if(optional.isPresent()){
			JcsjDto jcsjDto1=optional.get();
			if(StringUtil.isNotBlank(jcsjDto1.getCskz5())){
				JSONObject jsonObject=JSONObject.parseObject(jcsjDto1.getCskz5());
				if(jsonObject.get("ADD_ALLOWED")!=null&&"0".equals(jsonObject.get("ADD_ALLOWED"))){
					mav.addObject("ADD_ALLOWED", "0");
				}
			}else{
				mav.addObject("ADD_ALLOWED", "0");
			}
		}
		List<JcsjDto>  jcdwList1=jcdwList.stream().filter(x->"1".equals(StringUtil.isBlank(x.getCskz5())?"0":JSON.parseObject(x.getCskz5()).get("ADD_ALLOWED"))).filter(e->e.getScbj().equals("0")).toList();
		Optional<JcsjDto> optional_t=jcdwList.stream().filter(x->x.getCsid().equals(t_sjxxDto.getJcdw())).findFirst();
		List<JcsjDto> newList=new ArrayList<>(jcdwList1);
		if(optional_t.isPresent()){
			newList.add(optional.get());
		}
		mav.addObject("decetionlist", newList);//检测单位
		if(t_sjxxDto.getJcxmids() != null && t_sjxxDto.getJcxmids().size() > 0) {
			mav.addObject("subdetectlist", sjxxservice.getSubDetect(t_sjxxDto.getJcxmids().get(0)));
		}else {
			mav.addObject("subdetectlist", null);
		}
     	mav.addObject("fjcfbDtos",fjcfbDtos);
     	mav.addObject("syywlx",BusTypeEnum.IMP_INSPECTION_CONFIRM.getCode());
		FjcfbDto fjDto = new FjcfbDto();
		fjDto.setYwid(t_sjxxDto.getSjid());
		fjDto.setYwlx(BusTypeEnum.IMP_INSPECTION_CONFIRM.getCode());
		List<FjcfbDto> fjcfbList = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjDto);
		for (FjcfbDto fjcfbDto : fjcfbList) {
			String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
			fjcfbDto.setWjmhz(wjmhz);
		}
		mav.addObject("fjcfbList",fjcfbList);
		List<WbsjxxDto> wbsjxxDtos = wbsjxxService.getListBySjid(sjxxDto.getSjid());
		mav.addObject("wbsjxxDtos", wbsjxxDtos);
		return mav;
	}


	/**
	 * 处理操作
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/dealSjxx")
	public ModelAndView dealSjxx(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_ListDeal");
		SjxxDto t_sjxxDto=sjxxservice.getDtoById(sjxxDto.getSjid());
		t_sjxxDto.setFormAction("dealSaveSjxx");
		//获取检测项目和检测子项目
		SjjcxmDto sjjcxmDto = new SjjcxmDto();
		sjjcxmDto.setSjid(sjxxDto.getSjid());
		List<SjjcxmDto> sjjcxmDtos = sjjcxmservice.getDtoList(sjjcxmDto);
		List<String> jcxmids = new ArrayList<>();
		List<String> jczxmids = new ArrayList<>();
		for (SjjcxmDto dto : sjjcxmDtos) {
			jcxmids.add(dto.getJcxmid());
			jczxmids.add(dto.getJczxmid());
		}
		t_sjxxDto.setJcxmids(jcxmids);//把查询出来的送检项目set到送检信息中去；
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setFcsidList(jcxmids);
		List<JcsjDto> jczxmlist= new ArrayList<>();
		if (!CollectionUtils.isEmpty(jcxmids)){
			jczxmlist = jcsjService.getListByFid(jcsjDto);
		}
		//获取送检区分代码
		JcsjDto sjqf = redisUtil.hgetDto("matridx_jcsj:"+BasicDataTypeEnum.INSPECTION_DIVISION.getCode(),t_sjxxDto.getSjqf());
		mav.addObject("sjqfdm", sjqf.getCsdm());
		//获取送检实验信息
		SjsyglDto sjsyglDto=new SjsyglDto();
		sjsyglDto.setSjid(sjxxDto.getSjid());
		sjsyglDto.setLx(DetectionTypeEnum.DETECT_SJ.getCode());
		List<SjsyglDto> sjsyglDtos = sjsyglService.getDtoList(sjsyglDto);
		mav.addObject("sjjcxmDtos", JSON.toJSONString(sjjcxmDtos));
		mav.addObject("sjsyglDtos", sjsyglDtos);
		mav.addObject("jczxmids", jczxmids);
		mav.addObject("jczxmlist", jczxmlist);//检测子项目List
		mav.addObject("yblxlist", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型List
		mav.addObject("jcxmlist", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目List
		mav.addObject("jcdwlist", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位List
		mav.addObject("sjxxDto",t_sjxxDto);
		return mav;
	}

	/**
	 * 获取调整数据
	 * @return
	 */
	@RequestMapping(value="/inspection/dealSaveSjxx")
	@ResponseBody
	public Map<String, Object> dealSaveSjxx(SjxxDto sjxxDto,String sjsyglJson){
		User user=getLoginInfo();
		Map<String, Object> map= new HashMap<>();
		try {
			List<SjsyglDto> sjsyglDtos = new ArrayList<>();
			if (StringUtil.isNotBlank(sjsyglJson)){
				sjsyglDtos = (List<SjsyglDto>) JSON.parseArray(sjsyglJson, SjsyglDto.class);
			}
			sjxxservice.dealSaveInfo(sjxxDto,sjsyglDtos,user.getYhid());
		} catch (Exception e) {
			map.put("status","fail");
			map.put("message","保存失败！"+e.getMessage());
			return map;
		}
		map.put("status","success");
		map.put("message","保存成功！");
		return map;
	}

	/**
	 * 调整操作
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/adjustSjxx")
	public ModelAndView adjustSjxx(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_adjust");
//		SjxxDto dto = sjxxservice.getDto(sjxxDto);
//		HbdwqxDto hbdwqxDto = new HbdwqxDto();
//		hbdwqxDto.setHbid(dto.getHbid());
		//单位不做限制，直接去基础数据检测单位
//		List<HbdwqxDto> dwList = hbdwqxService.getJcdwSelectedList(hbdwqxDto);
		sjxxDto.setFormAction("adjustSaveSjxx");
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("dwList", JSONObject.toJSONString(redisUtil.lgetDto(("List_matridx_jcsj:"+BasicDataTypeEnum.DETECTION_UNIT))));
		mav.addObject("url", "/inspection/inspection/pagedataAdjustInfo?sjid="+sjxxDto.getSjid());
		return mav;
	}

	/**
	 * 获取调整数据
	 * @return
	 */
	@RequestMapping(value="/inspection/pagedataAdjustInfo")
	@ResponseBody
	public Map<String, Object> pagedataAdjustInfo(SjsyglDto sjsyglDto){
		Map<String, Object> map= new HashMap<>();
		sjsyglDto.setLxmc(DetectionTypeEnum.DETECT_SJ.getCode());
		List<SjsyglDto> dtoList = sjsyglService.getDtoList(sjsyglDto);
		map.put("rows",dtoList);
		return map;
	}
	/**
	 * 钉钉获取调整数据
	 * @return
	 */
	@RequestMapping(value="/inspection/minidataAdjustInfo")
	@ResponseBody
	public Map<String, Object> minidataAdjustInfo(SjxxDto sjxxDto,SjsyglDto sjsyglDto){
		Map<String, Object> map= new HashMap<>();
		sjxxDto = sjxxservice.getDto(sjxxDto);
		sjsyglDto.setLxmc(DetectionTypeEnum.DETECT_SJ.getCode());
		List<SjsyglDto> dtoList = sjsyglService.getDtoList(sjsyglDto);
		map.put("dwList",redisUtil.lgetDto(("List_matridx_jcsj:"+BasicDataTypeEnum.DETECTION_UNIT)));
		map.put("sjsyglDtos",dtoList);
		map.put("sjxxDto",sjxxDto);
		return map;
	}
	/**
	 * 钉钉保存调整数据
	 * @return
	 */
	@RequestMapping(value="/inspection/minidataSaveAdjustInfo")
	@ResponseBody
	public Map<String, Object> minidataSaveAdjustInfo(SjsyglDto sjsyglDto,String sjid){
		Map<String,Object> map = new HashMap<>();
		List<SjsyglDto> list = (List<SjsyglDto>) JSON.parseArray(sjsyglDto.getSy_json(), SjsyglDto.class);
		boolean isJcdwSame = true;
		String jcdw = list.get(0).getJcdw();
		for(SjsyglDto dto:list){
			dto.setXgry(sjsyglDto.getXgry());
			if (isJcdwSame && !jcdw.equals(dto.getJcdw())){
				isJcdwSame = false;
			}
		}
		//更新实验管理表数据
		Boolean isSuccess = sjsyglService.updateList(list);
		if (isSuccess && isJcdwSame && StringUtil.isNotBlank(sjid)){
			//若所有检测单位都相同，则更新送检信息的检测单位
			SjxxDto sjxxDto = new SjxxDto();
			sjxxDto.setSjid(sjid);
			sjxxDto.setJcdw(jcdw);
			sjxxDto.setXgry(sjsyglDto.getXgry());
			sjxxservice.updateJcdw(sjxxDto);
		}
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?"保存成功！":"保存失败！");
		List<SjsyglDto> sjsyglDtos=sjsyglService.getDtoList(sjsyglDto);
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_ADJ.getCode() + JSONObject.toJSONString(sjsyglDtos));
		return map;
	}
	/**
	 * 获取调整数据
	 * @return
	 */
	@RequestMapping(value="/inspection/adjustSaveSjxx")
	@ResponseBody
	public Map<String, Object> adjustSaveSjxx(SjsyglDto sjsyglDto){
		Map<String, Object> map= new HashMap<>();
		List<SjsyglDto> list=(List<SjsyglDto>)JSON.parseArray(sjsyglDto.getJson(),SjsyglDto.class);
		User user=getLoginInfo();
		for (SjsyglDto dto : list) {
			dto.setXgry(user.getYhid());
		}
		sjsyglService.updateList(list);
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_ADJ.getCode() + JSONObject.toJSONString(list));
		map.put("status", "success");
		map.put("message",  xxglservice.getModelById("ICOM00001").getXxnr());
		return map;
	}
	/**
	 * 高级修改操作
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/advancedmodSjxx")
	public ModelAndView advancedmodSjxx(SjxxDto sjxxDto) {
		return modSjxx(sjxxDto);
	}

	/**
	* 外部调用修改送检信息接口
	* @param sjxxDto
	* @return
	*/
	@RequestMapping(value="/inspection/commOutEdit")
	public ModelAndView commOutEdit(SjxxDto sjxxDto,HttpServletRequest request) {
		String access_token=request.getParameter("access_token");
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_ListSave");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.PATHOGENY_TYPE,BasicDataTypeEnum.CLINICAL_TYPE,BasicDataTypeEnum.DETECTION_TYPE,BasicDataTypeEnum.SAMPLESTATE,BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.SELFEXEMPTION_PROJECT,BasicDataTypeEnum.INSPECTION_DIVISION,BasicDataTypeEnum.INVOICE_APPLICATION});
		SjxxDto t_sjxxDto=sjxxservice.getDtoForbiont(sjxxDto);
		if(t_sjxxDto != null){
			List<SjdwxxDto> sjdwxxList= sjxxservice.getSjdw();//查询出所有送检单位；
			t_sjxxDto.setJcxmids(sjjcxmservice.getSjjcxm(t_sjxxDto.getSjid()));//把查询出来的送检项目set到送检信息中去；
			t_sjxxDto.setBys(sjgzbyservice.getGzby(t_sjxxDto.getSjid()));//把查询出来的关注病原set到送检信息中去；
			t_sjxxDto.setLczzs(sjlczzservice.getLczz(t_sjxxDto.getSjid()));//把查询出来的临床症状set到送检信息中去；
			t_sjxxDto.setSjqqjcs(sjqqjcservice.getJcz(t_sjxxDto.getSjid()));//查询前期检测项目；
			t_sjxxDto.setZts(sjybztService.getZtBysjid(t_sjxxDto.getSjid()));
			t_sjxxDto.setFormAction("UpdateSjxx");
			t_sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
			t_sjxxDto.setXg_flg("1");
			List<FjcfbDto> fjcfbDtos=sjxxservice.selectFjByWjid(t_sjxxDto.getSjid());
			SjkzxxDto sjkzxxDto_t = sjkzxxService.getDtoById(t_sjxxDto.getSjid());
			if(sjkzxxDto_t!=null){
				mav.addObject("sjkzxxDto", sjkzxxDto_t);
			}else{
				mav.addObject("sjkzxxDto", new SjkzxxDto());
			}
			mav.addObject("sjxxDto", t_sjxxDto);
			mav.addObject("sjdwxxList", sjdwxxList);
			mav.addObject("clincallist", jclist.get(BasicDataTypeEnum.CLINICAL_TYPE.getCode()));//临床症状
	    	mav.addObject("pathogenylist", jclist.get(BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));//病原
	    	mav.addObject("samplelist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
	    	mav.addObject("datectlist", jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
	    	mav.addObject("detectionlist", jclist.get(BasicDataTypeEnum.DETECTION_TYPE.getCode()));//前期检测
	    	mav.addObject("samplestate", jclist.get(BasicDataTypeEnum.SAMPLESTATE.getCode()));//标本状态
	    	mav.addObject("expressage", jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
	    	mav.addObject("decetionlist", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));
	    	mav.addObject("self_projectList", jclist.get(BasicDataTypeEnum.SELFEXEMPTION_PROJECT.getCode()));//自免项目
	     	mav.addObject("self_destinationList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//自免目的地
	     	mav.addObject("divisionList",jclist.get(BasicDataTypeEnum.INSPECTION_DIVISION.getCode()) );//送检区分
			mav.addObject("invoiceList",jclist.get(BasicDataTypeEnum.INVOICE_APPLICATION.getCode()) );//开票申请
	     	mav.addObject("fjcfbDtos",fjcfbDtos);
	     	if(t_sjxxDto.getJcxmids() != null && t_sjxxDto.getJcxmids().size() > 0) {
				mav.addObject("subdetectlist", sjxxservice.getSubDetect(t_sjxxDto.getJcxmids().get(0)));
			}else {
				mav.addObject("subdetectlist", null);
			}
	    	mav.addObject("access_token", access_token);
		}else{
			mav=new ModelAndView("wechat/sjxx/sjxx_outError");
		}
		return mav;
	}

	/**
	* 执行修改操作
	* @return
	*/
	@RequestMapping(value="/inspection/modSaveSjxx")
	@ResponseBody
	public Map<String, Object> UpdateSjxx(SjxxDto sjxxDto,SjkzxxDto sjkzxxDto,HttpServletRequest request){
		List<String> ygzbys = sjgzbyservice.getGzby(sjxxDto.getSjid());
		sjxxDto.setYgzbys(ygzbys);
		Map<String, Object> map= new HashMap<>();
		User user=getLoginInfo();
		sjxxDto.setXgry(user.getYhid());
		sjkzxxDto.setXgry(user.getYhid());
		//去除界面输入的标本编码和外部编码空格 2025-0630
		if(StringUtil.isNotBlank(sjxxDto.getYbbh())){
			sjxxDto.setYbbh(sjxxDto.getYbbh().trim());
		}
		if(StringUtil.isNotBlank(sjxxDto.getWbbm())){
			sjxxDto.setWbbm(sjxxDto.getWbbm().trim());
		}
		if(sjxxDto.getDb().contains("艾迪康")){
			if(StringUtil.isNotBlank(sjxxDto.getWbbm())){
				sjxxDto.setWbbm(sjxxDto.getWbbm().toUpperCase());
			}
		}
		SjxxDto sjxxDto_t=sjxxservice.getDtoById(sjxxDto.getSjid());
		if (StringUtil.isNotBlank(sjxxDto_t.getXgsj()) && !sjxxDto_t.getXgsj().equals(sjxxDto.getXgsj())){
			map.put("status", "fail");
			map.put("message", "数据已被其他人修改，请重新打开页面进行修改！");
			return map;
		}
		boolean isSuccess=false;
		if(StringUtil.isNotBlank(sjxxDto.getKyxm())){
			List<JcsjDto> kyxmList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RESEARCH_PROJECT.getCode());
			for (JcsjDto kyxm : kyxmList) {
				if (sjxxDto.getKyxm().equals(kyxm.getCsid())){
					sjxxDto.setSfsf(kyxm.getCskz2());
					List<SjjcxmDto> jcxmlist=(List<SjjcxmDto>) JSON.parseArray(sjxxDto.getJcxm(), SjjcxmDto.class);
					if (jcxmlist!=null&&!CollectionUtils.isEmpty(jcxmlist)){
						for(SjjcxmDto dto:jcxmlist){
							dto.setSfsf(kyxm.getCskz2());
						}
					}
					break;
				}
			}
		}
		//普通修改
		if(StringUtil.isNotBlank(sjxxDto.getSjqf()) && StringUtil.isNotBlank(sjxxDto_t.getSjqf()) && !sjxxDto_t.getSjqf().equals(sjxxDto.getSjqf()) && "1".equals(sjxxDto.getXg_flg())){
			List<JcsjDto> sjqfList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
			for (JcsjDto sjqf : sjqfList) {
				if (sjxxDto.getSjqf().equals(sjqf.getCsid())){
					if ("0".equals(sjqf.getCskz2())){
						sjxxDto.setSfsf(sjqf.getCskz2());
						List<SjjcxmDto> jcxmlist=(List<SjjcxmDto>) JSON.parseArray(sjxxDto.getJcxm(), SjjcxmDto.class);
						if (jcxmlist!=null&&!CollectionUtils.isEmpty(jcxmlist)){
							for(SjjcxmDto dto:jcxmlist){
								dto.setSfsf(sjqf.getCskz2());
							}
						}
						sjxxDto.setJcxm(JSON.toJSONString(jcxmlist));
					}
					break;
				}
			}
		}
		if (StringUtil.isNotBlank(request.getParameter("wbsjxxsJson"))){
			List<WbsjxxDto> wbsjxxsJson = JSON.parseArray(request.getParameter("wbsjxxsJson"), WbsjxxDto.class);
			wbsjxxService.updateList(wbsjxxsJson);
		}
		try {
			List<SjxxDto> dtosByWbbm = new ArrayList<>();
			if (StringUtil.isNotBlank(sjxxDto.getWbbm())){
				dtosByWbbm = sjxxservice.getDtosByWbbm(sjxxDto);
			}
			if (!CollectionUtils.isEmpty(dtosByWbbm)) {
				map.put("status", isSuccess ? "success" : "fail");
				map.put("message", isSuccess ? xxglservice.getModelById("ICOM00001").getXxnr() : "外部编码已经存在,不能重复使用！");
			} else {
				int countybbh = sjxxservice.getCountByybbh(sjxxDto);
				if (countybbh > 0) {
					map.put("status", isSuccess ? "success" : "fail");
					map.put("message", isSuccess ? xxglservice.getModelById("ICOM00001").getXxnr() : "标本编号已经存在,不能重复使用！");
				} else {
					if (StringUtil.isNotBlank(sjxxDto.getNbbm())) {
						int count = sjxxservice.getCountBynbbm(sjxxDto);
						if (count > 0) {
							map.put("status", isSuccess ? "success" : "fail");
							map.put("message", isSuccess ? xxglservice.getModelById("ICOM00001").getXxnr() : "内部编码已存在，不能重复使用！");
						} else {
							Map<String, Object> objectMap  = sjxxservice.UpdateSjxx(sjxxDto,user,sjkzxxDto);
							map.put("status", objectMap.get("status"));
							map.put("message", objectMap.get("message"));
						}
					} else {
						Map<String, Object> objectMap  = sjxxservice.UpdateSjxx(sjxxDto,user,sjkzxxDto);
						map.put("status", objectMap.get("status"));
						map.put("message", objectMap.get("message"));
					}
				}
			}
			map.put("ygzbys",sjxxDto.getYgzbys());
			map.put("bys",sjxxDto.getBys());
			map.put("jcdw",sjxxDto.getJcdw());
			map.put("sjid",sjxxDto.getSjid());
			map.put("jcxmids",sjxxDto.getJcxmids());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", e.toString());
			log.error(e.getMessage());
		}

		return map;
	}
	/**
	 * 添加送检信息
	 * @return
	 */
	@RequestMapping(value="/inspection/addSjxx")
	public ModelAndView addSjxx(){
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_ListSave");
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		sjxxDto.setSjqqjcs(sjqqjcservice.getJcz(sjxxDto.getSjid()));//查询前期检测项目；
		sjxxDto.setFormAction("addSaveSjxx");
		SjkzxxDto sjkzxxDto=new SjkzxxDto();
		mav.addObject("divisionListJson",JSONObject.toJSONString(redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode())));//送检区分
    	mav.addObject("samplelist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
    	mav.addObject("genderlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.GENDER_TYPE.getCode()));//性别
    	mav.addObject("clincallist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.CLINICAL_TYPE.getCode()));//临床症状
    	mav.addObject("pathogenylist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));//病原
       	mav.addObject("datectlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
       	mav.addObject("samplestate", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SAMPLESTATE.getCode()));//标本状态
     	mav.addObject("expressage", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
		List<JcsjDto>jcdwList=redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		jcdwList=jcdwList.stream().filter(x->"1".equals(StringUtil.isBlank(x.getCskz5())?"0":JSON.parseObject(x.getCskz5()).get("ADD_ALLOWED"))).toList();
     	mav.addObject("decetionlist", jcdwList);//检测单位
		List<SjdwxxDto> sjdwxxList= sjxxservice.getSjdw();
		mav.addObject("sjdwxxList",sjdwxxList);
		sjxxDto.setSfzmjc("0");
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("sjkzxxDto", sjkzxxDto);
		mav.addObject("divisionList",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));//送检区分
		mav.addObject("invoiceList",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.INVOICE_APPLICATION.getCode()));//开票申请
		mav.addObject("kyxmlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.RESEARCH_PROJECT.getCode()));// 科研项目
		mav.addObject("syywlx",BusTypeEnum.IMP_INSPECTION_CONFIRM.getCode());
		//获取文件类型
		return mav;
	}

	/**
	 * 得到区分子项目
	 * @param jcsjDto
	 * @return
	 */
	@RequestMapping(value="/getQfzxm")
	@ResponseBody
	public List<JcsjDto> getQfzxm(JcsjDto jcsjDto){
        return jcsjService.getJcsjDtoList(jcsjDto);
	}
	/**
	 * 提交添加保存
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/addSaveSjxx")
	@ResponseBody
	public Map<String, Object> addSaveSjxx(SjxxDto sjxxDto,SjkzxxDto sjkzxxDto){
		Map<String, Object> map= new HashMap<>();
		User user=getLoginInfo();
		sjxxDto.setLrry(user.getYhid());
		sjkzxxDto.setLrry(user.getYhid());
		sjxxDto.setJcbj("0");
		sjxxDto.setZt(StatusEnum.CHECK_PASS.getCode());
		sjxxDto.setSfws("3");
		boolean isSuccess=false;
		//去除界面输入的标本编码和外部编码空格 2025-0630
		if(StringUtil.isNotBlank(sjxxDto.getYbbh())){
			sjxxDto.setYbbh(sjxxDto.getYbbh().trim());
		}
		if(StringUtil.isNotBlank(sjxxDto.getWbbm())){
			sjxxDto.setWbbm(sjxxDto.getWbbm().trim());
		}
		if(sjxxDto.getDb().contains("艾迪康")){
			if(StringUtil.isNotBlank(sjxxDto.getWbbm())){
				sjxxDto.setWbbm(sjxxDto.getWbbm().toUpperCase());
			}
		}
		List<SjxxDto> dtosByWbbm = new ArrayList<>();
		if (StringUtil.isNotBlank(sjxxDto.getWbbm())){
			dtosByWbbm = sjxxservice.getDtosByWbbm(sjxxDto);
		}
		if (!CollectionUtils.isEmpty(dtosByWbbm)) {
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglservice.getModelById("ICOM00001").getXxnr() : "外部编码已经存在,不能重复使用！");
		} else {
			int countybbh=sjxxservice.getCountByybbh(sjxxDto);
			if(countybbh > 0) {
				map.put("status",isSuccess?"success":"fail");
				map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():"标本编号已经存在,不能重复使用！");
			}else {
				if(StringUtil.isNotBlank(sjxxDto.getNbbm())) {
					int count=sjxxservice.getCountBynbbm(sjxxDto);
					if(count > 0) {
						map.put("status",isSuccess?"success":"fail");
						map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():"内部编码已经存在,不能重复使用！");
					}else {
						isSuccess=sjxxservice.AddSjxx(sjxxDto,sjkzxxDto);
						map.put("status",isSuccess?"success":"fail");
						map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
					}
				}else {
					isSuccess=sjxxservice.AddSjxx(sjxxDto,sjkzxxDto);
					map.put("status",isSuccess?"success":"fail");
					map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
				}
			}
		}
		map.put("ygzbys",sjxxDto.getYgzbys());
		map.put("bys",sjxxDto.getBys());
		map.put("jcdw",sjxxDto.getJcdw());
		map.put("sjid",sjxxDto.getSjid());
		map.put("jcxmids",sjxxDto.getJcxmids());
		return map;
	}

	/**
	 * 删除送检记录
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/delSjxx")
	@ResponseBody
	public Map<String, Object> delSjxx(SjxxDto sjxxDto){
		User user=getLoginInfo();
		sjxxDto.setScry(user.getYhid());
		boolean isSuccess=sjxxservice.deleteDto(sjxxDto);
		Map<String, Object> map= new HashMap<>();
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
		return map;
	}
	/**
	 * 检验结果导入
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/pathogen/uploadReport")
	public ModelAndView uploadReport(SjxxDto sjxxDto){
		ModelAndView mav=new ModelAndView("wechat/sjxx/pathogen_uploadReport");
		sjxxDto = sjxxservice.getDto(sjxxDto);
		mav.addObject("detectlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目

		// 根据检测项目获取文件类型
		sjxxDto.setYwlx(sjxxDto.getCskz3()+"_"+sjxxDto.getCskz1());
		sjxxDto.setW_ywlx(sjxxDto.getCskz3()+"_"+sjxxDto.getCskz1()+"_WORD");
		sjxxDto.setYwlx_q(sjxxDto.getCskz3()+"_REM_"+sjxxDto.getCskz1());
		sjxxDto.setW_ywlx_q(sjxxDto.getCskz3()+"_REM_"+sjxxDto.getCskz1()+"_WORD");
		sjxxDto.setW_ywlx_z(BusTypeEnum.IMP_WORD_DETECTION_SELF.getCode());
//		sjxxDto.setPdf_ywlx_x(BusTypeEnum.IMP_REPORT_TEMEPLATE_XPERT.getCode());
//		sjxxDto.setPdf_ywlx_g(BusTypeEnum.IMP_REPORT_TEMEPLATE_GM.getCode());
//		sjxxDto.setPdf_ywlx_y(BusTypeEnum.IMP_REPORT_TEMEPLATE_GXM.getCode());
		mav.addObject("sjxxDto",sjxxDto);
		return mav;
	}

	/**
	 * 检验结果导入保存
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/pathogen/uploadSaveReport")
	@ResponseBody
	public Map<String, Object> uploadSaveReport(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		try {
			//获取用户信息
			User user = getLoginInfo();
			sjxxDto.setLrry(user.getYhid());
			sjxxDto.setXgry(user.getYhid());
			boolean isSuccess = sjxxservice.uploadSaveReport(sjxxDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("status", "fail");
			map.put("message", e.toString());
		}
		return map;
	}

	@RequestMapping(value="/inspection/getDB")
	public ModelAndView getDB(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_modDb");
		List<SjhbxxDto> sjhbList=sjhbxxservice.getDB();
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("sjhbList", sjhbList);
		return mav;
	}

	@RequestMapping(value="/inspection/updateDB")
	@ResponseBody
	public Map<String, Object> updateDB(SjxxDto sjxxDto) {
		boolean isSuccess=sjxxservice.updateDB(sjxxDto);
		Map<String, Object> map= new HashMap<>();
		map.put("status", isSuccess?"success":"fail");map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 送检收样确认
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/pathogen/confirmSjxx")
	public ModelAndView confirmSjxx(SjxxDto sjxxDto){
		ModelAndView mav=new ModelAndView("wechat/sjxx/inspection_confirm");
		User user=getLoginInfo();
		GrszDto grszDto_t=new GrszDto();
		grszDto_t.setYhid(user.getYhid());
		grszDto_t.setSzlbs(new String[]{PersonalSettingEnum.PRINT_ADDRESS.getCode(),PersonalSettingEnum.WHETHER_TO_PRINT.getCode()});
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto_t);
		grszDto_t = grszDtoMap.get(PersonalSettingEnum.PRINT_ADDRESS.getCode());
		if(grszDto_t==null) {
			mav.addObject("grszDto", new GrszDto());
		}else {
			if (StringUtil.isBlank(grszDto_t.getDysl())){
				grszDto_t.setDysl("6");
			}
			mav.addObject("grszDto", grszDto_t);
		}
		GrszDto grszDto_print = grszDtoMap.get(PersonalSettingEnum.WHETHER_TO_PRINT.getCode());
		if(grszDto_print==null) {
			grszDto_print = new GrszDto();
			grszDto_print.setSzz("1");
		}
		mav.addObject("grszDto_print", grszDto_print);
		Map<String,Object> map= new HashMap<>();
		map.put("ywlx", BusTypeEnum.IMP_INSPECTION_CONFIRM.getCode());
		map.put("flagmc", GlobalParmEnum.SAMPLE_COLLECTION.getValue());
		map.put("flag", GlobalParmEnum.SAMPLE_COLLECTION.getCode());
		if(sjxxDto.getSjid()!=null) {
			sjxxDto=sjxxservice.getDto(sjxxDto);
			sjxxDto.setZts(sjybztService.getZtBysjid(sjxxDto.getSjid()));
			if (null != sjxxDto){
				map.put("sjid", sjxxDto.getSjid());
				map.put("nbbm", sjxxDto.getNbbm());
				map.put("jcxmmc", sjxxDto.getJcxmmc());
				map.put("yblxmc", sjxxDto.getYblxmc());
				map.put("ybbh", sjxxDto.getYbbh());
				map.put("hzxm", sjxxDto.getHzxm());
			}
		}
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		List<FjcfbDto> t_fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SAMPLESTATE});
		DBEncrypt p = new DBEncrypt();
		String dingtalkurl = p.dCode(jumpdingtalkurl);
		String key = GlobalParmEnum.SAMPLE_COLLECTION.getCode()+":"+user.getYhid()+"_"+new Date().getTime();
		String dingtalkbtn = dingtalkurl+"page=/pages/index/index"+URLEncoder.encode("?toPageUrl=/pages/index/sjsyfjpage/sjsyfjpage&key="+key,StandardCharsets.UTF_8);

		mav.addObject("key", key);
		mav.addObject("dingtalkurl", dingtalkbtn);
		mav.addObject("switchoverurl", applicationurl+"/ws/pathogen/pagedataUploadFile?key="+key);
		redisUtil.set(key,JSON.toJSONString(map),5000);
		mav.addObject("flag", GlobalParmEnum.SAMPLE_COLLECTION.getCode());
		mav.addObject("ybztlist", jclist.get(BasicDataTypeEnum.SAMPLESTATE.getCode()));
		mav.addObject("sjxxDto",sjxxDto);
		mav.addObject("yhid",user.getYhid());
		mav.addObject("ywlx", BusTypeEnum.IMP_INSPECTION_CONFIRM.getCode());
		mav.addObject("sjxxDto",sjxxDto);
		return mav;
	}

	/**
	 * 送检收样确认
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/pathogen/minidataInspectionConfirm")
	@ResponseBody
	public Map<String,Object> minidataInspectionConfirm(SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		GrszDto grszDto_t=new GrszDto();
		grszDto_t.setYhid(sjxxDto.getYhid());
		grszDto_t.setSzlb(PersonalSettingEnum.PRINT_ADDRESS.getCode());
		grszDto_t=grszService.selectGrszDtoByYhidAndSzlb(grszDto_t);
		if(sjxxDto.getSjid()!=null) {
			sjxxDto=sjxxservice.getDto(sjxxDto);
		}
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SAMPLESTATE});
		sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION_CONFIRM.getCode());
		map.put("ybztlist", jclist.get(BasicDataTypeEnum.SAMPLESTATE.getCode()));
		map.put("sjxxDto",sjxxDto);
		if(grszDto_t==null) {
			map.put("grszDto", new GrszDto());
		}else {
			map.put("grszDto", grszDto_t);
		}
		return map;
	}

	/**
	 * 获取送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/pathogen/pagedataSjxxInfo")
	@ResponseBody
	public Map<String, Object> pagedataSjxxInfo(SjxxDto sjxxDto) {
		Map<String,Object> map = new HashMap<>();
		SjxxDto dto = sjxxservice.getDto(sjxxDto);
		map.put("dto", dto);
		return map;
	}
	/**
	 *
	 * @return
	 */
	@RequestMapping("/inspection/pagedataAjustView")
	public ModelAndView pagedataAjustView(SjxxDto sjxxDto) {
		ModelAndView mav =new ModelAndView("wechat/sjxx/sjxx_ajustList");
		mav.addObject("sjxxDto",sjxxDto);
		SjjcxmDto sjjcxmDto = new SjjcxmDto();
		sjjcxmDto.setSjid(sjxxDto.getSjid());
		List<SjjcxmDto> dtoList = sjjcxmservice.getDtoList(sjjcxmDto);
		mav.addObject("dtoList",dtoList);
		return mav;
	}
	/**
	 * 获取信息对应信息
	 * @paramti't
	 * @return
	 */
	@RequestMapping("/inspection/pagedataGetInfo")
	@ResponseBody
	public Map<String, Object> pagedataGetInfo(XxdyDto xxdyDto) {
		Map<String,Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(xxdyDto.getYxxid())){
			String[] strings = xxdyDto.getYxxid().split("-");
			xxdyDto.setYxxid(strings[0]);
			if (StringUtil.isNotBlank(strings[1]) && !"null".equals(strings[1]))
				xxdyDto.setZid(strings[1]);
			List<XxdyDto> list = xxdyService.getPagedInfoDtoList(xxdyDto);
			map.put("total",xxdyDto.getTotalNumber());
			map.put("rows",list);
		}
		return map;
	}

	/**
	 * 获取信息对应信息
	 * @paramti't
	 * @return
	 */
	@RequestMapping("/inspection/pagedataModInfo")
	@ResponseBody
	public Map<String, Object> pagedataModInfo(SjsyglDto sjsyglDto) {
		Map<String,Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(sjsyglDto.getJcxm())){
			log.error("确认调整按钮：sjid-"+sjsyglDto.getSjid());
			String[] strings = sjsyglDto.getJcxm().split("-");
			sjsyglDto.setJcxmid(strings[0]);
			sjsyglDto.setJczxmid(strings[1]);
			//sjsyglDto.setLx(DetectionTypeEnum.DETECT_SJ.getCode());
			sjsyglService.deleteInfoAjust(sjsyglDto);
			User user=getLoginInfo();
			List<SjsyglDto> list=(List<SjsyglDto>)JSON.parseArray(sjsyglDto.getJson(),SjsyglDto.class);
			for (SjsyglDto dto1 : list) {
				dto1.setYwid(dto1.getSjid());
				dto1.setSyglid(StringUtil.generateUUID());
				dto1.setLrry(user.getYhid());
				//dto1.setLx(DetectionTypeEnum.DETECT_SJ.getCode());
			}
			if (!CollectionUtils.isEmpty(list)){
				sjsyglService.insertList(list);
				map.put("status", "success");
				map.put("message", "保存成功！");
				List<SjsyglModel> dtos = sjsyglService.getModelList(sjsyglDto);
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.SJSY_ADJ.getCode() + JSONObject.toJSONString(dtos));
				return map;
			}
		}
		map.put("status", "fail");
		map.put("message", xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}


	/**
	* 获取送检信息
	* @param sjxxDto
	* @return
	*/
	@RequestMapping(value="/pathogen/pagedataInspection")
	@ResponseBody
	public Map<String, Object> getInspection(SjxxDto sjxxDto) throws BusinessException {
		String key = sjxxDto.getQfmc();
		sjxxDto.setQfmc(null);
		Map<String,Object> map = new HashMap<>();
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SAMPLESTATE});
		map.put("ybztlist", jclist.get(BasicDataTypeEnum.SAMPLESTATE.getCode()));//标本状态
		//若样本编号长度大于等于12则采用模糊查询
		SjxxDto t_sjxxDto;
		if(sjxxDto.getYbbh().length()>=12){
			t_sjxxDto=sjxxservice.getDtoVague(sjxxDto);//TODO
		}else{
			t_sjxxDto=sjxxservice.getDto(sjxxDto);//TODO
		}
		if(t_sjxxDto== null) {
			sjxxDto.setJstj("1");
			sjxxDto.setPrint_flg("0");
			map.put("sjxxDto", sjxxDto);
			return map;
		}
		if(StringUtil.isBlank(t_sjxxDto.getJsrq())) {
			t_sjxxDto.setJstj("1");
		}
		if(t_sjxxDto.getNbbm()==null ||"".equals(t_sjxxDto.getNbbm())) {
			String nbbm = sjxxservice.generateNbbm(t_sjxxDto);
			
			//String t_nbbm = sjxxservice.getNbbm(t_sjxxDto);
			//log.error("内部编号(新规则)编码："+ nbbm + " 旧编码：" + t_nbbm + " sjid:" + t_sjxxDto.getSjid() + " 编码result:" + String.valueOf(t_nbbm.equals(nbbm)));
			
			if (StringUtil.isNotBlank(nbbm)){
				t_sjxxDto.setNbbm(nbbm);
				t_sjxxDto.setPrint_flg("1");
			}else {
				t_sjxxDto.setPrint_flg("0");
			}
		}else {
			t_sjxxDto.setPrint_flg("0");
		}
		if("1".equals(t_sjxxDto.getYyxxCskz1())) {
			t_sjxxDto.setHospitalname(t_sjxxDto.getHospitalname()+"-"+t_sjxxDto.getJcdwmc());
		}
		map.put("sjxxDto", t_sjxxDto);
		SjkzxxDto dtoById = sjkzxxService.getDtoById(t_sjxxDto.getSjid());
		map.put("sjkzxxDto", dtoById);
		User user=getLoginInfo();
		SjsyglDto sjsyglDto = new SjsyglDto();
		sjsyglDto.setSjid(t_sjxxDto.getSjid());
		List<Map<String,String>> jcdwList=sjxxservice.getJsjcdwByjsid(user.getDqjs());
//		List<String> list = new ArrayList<>();
		List<SjsyglDto> dtoList = new ArrayList<>();
		sjsyglDto.setSfjs("null");
        sjsyglDto.setYwlx(DetectionTypeEnum.DETECT_SJ.getCode());
		if(jcdwList!=null && jcdwList.size() > 0) {
//			for (Map<String, String> stringMap : jcdwList) {
//				if(stringMap.get("jcdw")!=null) {
//					list.add(stringMap.get("jcdw"));
//				}
//			}
			if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				List<String> jcdwlist = new ArrayList<>();
				for(int i=0; i<jcdwList.size(); i++) {
					if(jcdwList.get(i).get("jcdw") != null) {
						jcdwlist.add(jcdwList.get(i).get("jcdw"));
					}
				}
				if(!CollectionUtils.isEmpty(jcdwlist)) {
					sjsyglDto.setJcdwxz(jcdwlist);
					dtoList = sjsyglService.getDtoList(sjsyglDto);
				}
			}else {
				dtoList = sjsyglService.getDtoList(sjsyglDto);
			}
		}else {
			dtoList = sjsyglService.getDtoList(sjsyglDto);
		}
		//根据jclxid进行分组
		List<SjsyglDto> sjsyglDtoList=new ArrayList<>();
		Map<String, List<SjsyglDto>> listMap = dtoList.stream().collect(Collectors.groupingBy(SjsyglDto::getJclxid));
		if (!CollectionUtils.isEmpty(listMap)){
			Iterator<Map.Entry<String, List<SjsyglDto>>> entryIterator = listMap.entrySet().iterator();
			while (entryIterator.hasNext()) {
				Map.Entry<String, List<SjsyglDto>> stringListEntry = entryIterator.next();
				//String jclx = stringListEntry.getKey();
				List<SjsyglDto> sjsyglDtoLists = stringListEntry.getValue();
				if(!CollectionUtils.isEmpty(sjsyglDtoLists))
					sjsyglDtoList.add(sjsyglDtoLists.get(0));
			}
		}
		map.put("dtoList", sjsyglDtoList);
		Object hget = redisUtil.get(key);
		if (hget!=null){
			Map<String,Object> result = JSON.parseObject((String) hget);
			result.put("sjid", t_sjxxDto.getSjid());
			result.put("nbbm", t_sjxxDto.getNbbm());
			result.put("jcxmmc", t_sjxxDto.getJcxmmc());
			result.put("yblxmc", t_sjxxDto.getYblxmc());
			result.put("ybbh", t_sjxxDto.getYbbh());
			result.put("hzxm", t_sjxxDto.getHzxm());
			redisUtil.del(key);
			redisUtil.set(key,JSON.toJSONString(result),5000);
		}
		return map;
	}

	/**
	 * 获取送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/pathogen/pagedataGetConfirmInfo")
	@ResponseBody
	public Map<String, Object> pagedataGetConfirmInfo(SjxxDto sjxxDto) {
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo();
		List<Map<String,String>> jcdwList=sjxxservice.getJsjcdwByjsid(user.getDqjs());
		String jcdw;
		if (!CollectionUtils.isEmpty(jcdwList)&&("1".equals(jcdwList.get(0).get("dwxdbj"))) && StringUtil.isNotBlank(jcdwList.get(0).get("jcdwmc"))){
			jcdw = jcdwList.get(0).get("jcdwmc");
		}else{
			jcdw =sjxxDto.getJcdwmc();
		}
		if(StringUtil.isNotBlank(sjxxDto.getSjqfmc()) && "科研".equals(sjxxDto.getSjqfmc())){
			sjxxDto.setSjqf("RESEARCH");
		}
		sjxxDto.setJcdwmc(jcdw);
		if (StringUtil.isNotBlank(jcdw)){
			//筛选实验数据的时候需要去除已经出过报告的送检信息，以及已经过时效的复检加测信息
			//清除之前的数据，重新设置sjid查询所有复检信息
			FjsqDto fjsqDto_t = new FjsqDto();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String nowDate=sdf.format(new Date());
			fjsqDto_t.setDsyrq(nowDate);
			fjsqDto_t.setSjid(sjxxDto.getSjid());
			List<FjsqDto> resultFjDtos = fjsqService.getSyxxByFj(fjsqDto_t);
			if(resultFjDtos!=null && resultFjDtos.size() > 0) {
				List<String> ids = new ArrayList<String>();
				for(FjsqDto res_FjsqDto:resultFjDtos) {
					ids.add(res_FjsqDto.getFjid());
				}
				sjxxDto.setIds(ids);
			}
			List<SjsyglDto> list = sjsyglService.getDetectionInfo(sjxxDto,null, DetectionTypeEnum.DETECT_SJ.getCode());
			for (SjsyglDto sjsyglDto : list) {
				sjsyglDto.setSyglid(StringUtil.generateUUID());
			}
			map.put("dtoList", list);
		}
		return map;
	}
	/**
	 * 送检收样确认保存
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/pathogen/minidataConfirmSaveSjxx")
	@ResponseBody
	public Map<String, Object> inspectionSaveConfirm(SjxxDto sjxxDto,HttpServletRequest request,GrszDto grszDto){
		Map<String, Object> map = new HashMap<>();
		SjxxDto dtoById=sjxxservice.getDtoById(sjxxDto.getSjid());
		if (StringUtil.isNotBlank(dtoById.getXgsj()) && !dtoById.getXgsj().equals(sjxxDto.getXgsj())){
			map.put("status", "fail");
			map.put("message", "数据已被其他人修改，请重新收样！");
			return map;
		}
		String bbsdwd = request.getParameter("bbsdwd");
		SjkzxxDto sjkzxxDto=new SjkzxxDto();
		sjkzxxDto.setBbsdwd(bbsdwd);
		sjkzxxDto.setSjid(sjxxDto.getSjid());
		User user = getLoginInfo();
		List<String> strings = sjxxDto.getIds();
		sjxxDto.setIds(new ArrayList<>());
		if ("1".equals(sjxxDto.getYblx_flg())) {
			List<SjsyglDto> list = JSONObject.parseArray(sjxxDto.getState(),SjsyglDto.class);
			if(list != null && list.size() > 0){
				for (SjsyglDto sjsyglDto : list) {
					sjsyglDto.setLrry(user.getYhid());
					sjsyglDto.setYwlx(DetectionTypeEnum.DETECT_SJ.getCode());
					sjsyglDto.setYwid(sjsyglDto.getSjid());
				}
			}
			//筛选实验数据的时候需要去除已经出过报告的送检信息，以及已经过时效的复检加测信息
			//清除之前的数据，重新设置sjid查询所有复检信息
			FjsqDto fjsqDto_t = new FjsqDto();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String nowDate=sdf.format(new Date());
			fjsqDto_t.setDsyrq(nowDate);
			fjsqDto_t.setSjid(sjxxDto.getSjid());
			List<FjsqDto> resultFjDtos = fjsqService.getSyxxByFj(fjsqDto_t);
			if(resultFjDtos!=null && resultFjDtos.size() > 0) {
				List<String> ids = new ArrayList<String>();
				for(FjsqDto res_FjsqDto:resultFjDtos) {
					ids.add(res_FjsqDto.getFjid());
				}
				sjxxDto.setIds(ids);
			}
			List<SjsyglDto> insertInfo =sjsyglService.getDetectionInfo(sjxxDto,null,DetectionTypeEnum.DETECT_SJ.getCode());

			if (!CollectionUtils.isEmpty(insertInfo)){
				//更新项目实验数据和送检实验数据
				boolean isSuccess = sjxxservice.addOrUpdateSyData(insertInfo,sjxxDto,user.getYhid());
				if (isSuccess){
					SjsyglDto sjsyglDto_t=new SjsyglDto();
					sjsyglDto_t.setSjid(sjxxDto.getSjid());
					//sjsyglDto_t.setLx(DetectionTypeEnum.DETECT_SJ.getCode());
					List<SjsyglModel> sjsylist = sjsyglService.getModelList(sjsyglDto_t);
					RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.SJSY_MOD.getCode() + JSONObject.toJSONString(sjsylist));
					XmsyglDto xmsyglDto_t = new XmsyglDto();
					xmsyglDto_t.setYwid(sjxxDto.getSjid());
					List<XmsyglModel> dtos = xmsyglService.getModelList(xmsyglDto_t);
					RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.XMSY_MOD.getCode() + JSONObject.toJSONString(dtos));
				}
			}
			sjxxDto.setYblx_flg(null);
			sjxxDto.setState(null);
			//RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.SJSY_MOD.getCode() + JSONObject.toJSONString(list));
		}
		String nbzbm = "";
		if (null != sjxxDto.getJclxs() && sjxxDto.getJclxs().size() >0){
			for (String jclx : sjxxDto.getJclxs()) {
				nbzbm += " "+jclx.split("/")[0];
			}
			sjxxDto.setJclxs(null);
		}
		boolean isSuccess = false;
		try {
			// 处理个人设置信息
			setGrszInfo(request, grszDto, user, map);

			map.put("sfdy", request.getParameter("sfdy"));
			// 获取用户信息
			sjxxDto.setSfjs(sjxxDto.getSfsy());
			sjxxDto.setSftj(sjxxDto.getSfsy());
			sjxxDto.setXgry(user.getYhid());
			// nbbm保存时，需要再次确认当前内部编码的流水号是否为最新的流水号 2020/10/13  张晗
			String successMessage = xxglservice.getModelById("ICOM00001").getXxnr();
			if (StringUtil.isNotBlank(sjxxDto.getNbbm())) {
				// 判断是不是科研标本 科研标本 MDY019- 开头
				int index = sjxxDto.getNbbm().indexOf("-");
				if (index == -1) { // 如果不是科研标本
					if ("1".equals(sjxxDto.getPrint_flg())) {// 如果打印标记为 1，说明是新生成的nbbm，需要进行验证
						String confirm_nbbm = sjxxservice.generateNbbm(sjxxDto);
						
						if (confirm_nbbm != null) {
							// 内部编码后缀 ：因为有可能手动修改最后一位，所以要先截取出来保存。
							String suffix;
							int length;
							// 20240904 "其它"样本类型的csdm由XXX改为G
							if(sjxxDto.getNbbm().contains("XXX") || sjxxDto.getNbbm().contains("G")){
								suffix = sjxxDto.getNbbm().substring(sjxxDto.getNbbm().length() - 3);
								length= sjxxDto.getNbbm().length()-3;
							}else{
								suffix = sjxxDto.getNbbm().substring(sjxxDto.getNbbm().length() - 1);
								length= sjxxDto.getNbbm().length()-1;
							}
							String prefix;
							// 截取重新生成的内部编码 ：因为最后一位有可能修改，所有截取掉 不进行后续的比较用
							String subConfirmNbbm;
							String subNbbm;
							if(length<=10){
								prefix = sjxxDto.getNbbm().substring(0,2);
								subConfirmNbbm = confirm_nbbm.substring(2, 10);
								subNbbm = sjxxDto.getNbbm().substring(2, 10);
							}else{
								prefix = sjxxDto.getNbbm().substring(0,4);
								subConfirmNbbm = confirm_nbbm.substring(4, 12);
								subNbbm = sjxxDto.getNbbm().substring(4, 12);
							}
							// 判断确认的内部编码的流水号是否等于 页面传递的内部编码流水号
							if (!subConfirmNbbm.equals(subNbbm)) {// 如果不相等
								// 采用最新的流水号 subConfirmNbbm+suffix 中间+后缀
								String newNbbm = prefix + subConfirmNbbm + suffix;
								sjxxDto.setNbbm(newNbbm);
								// 发送消息通知操作人员
								successMessage = successMessage + "  当前内部编码流水号已经存在,已为您替换为最新的内部编码 [" + newNbbm + "]";
							}
						}
					}
				}
				int count = sjxxservice.getCountBynbbm(sjxxDto);
				if (count > 0) {
					map.put("status", "fail");
					map.put("message", "内部编码已经存在，不能重复使用！");
				} else {
					isSuccess = sjxxservice.inspectionSaveConfirm(sjxxDto,strings,sjkzxxDto);
					map.put("status", isSuccess ? "success" : "fail");
					map.put("message", isSuccess ? (successMessage) : xxglservice.getModelById("ICOM00002").getXxnr());
				}
			} else {
				isSuccess = sjxxservice.inspectionSaveConfirm(sjxxDto,strings,sjkzxxDto);
				map.put("status", isSuccess ? "success" : "fail");
				map.put("message", isSuccess ? (successMessage) : xxglservice.getModelById("ICOM00002").getXxnr());
			}
			// 发送错误时，比如内部编码已经存在的时候，打印机不应该打印条码
			if (isSuccess) {
				if ("1".equals(sjxxDto.getSfjs())) {
//					String sign = commonService.getSign(sjxxDto.getNbbm());
					int fontflg = 1;
					if (sjxxDto.getNbbm().length() > 10) {
						fontflg = 2;
					}
//					sign = URLEncoder.encode(sign, "UTF-8");
					//原本打算采用flag 是打印版本标记  1:原来的2行（不打印project字段） 2：三行  ，之后改为如果打印两行则只传递两个数据
					if ("1".equals(map.get("sz_flg"))){
						String url = ":8082/Print?code=" + (sjxxDto.getNbbm().startsWith("MD")?sjxxDto.getNbbm().substring(2):sjxxDto.getNbbm()) + "&sign=1" + "&num="+(StringUtil.isNotBlank(grszDto.getDysl())?grszDto.getDysl():"6")+"&fontflg=" + fontflg
								+ "&flg=2&word=" + (StringUtil.isNotBlank(nbzbm)?nbzbm.substring(1):"")+ "&qrcode=" + sjxxDto.getNbbm() + "&project=" + sjxxDto.getYbbh() + "&rownum=3";
						map.put("print", url);
						log.error("打印地址：" + url);
					}else{
						String url = ":8081/Print?code=" + (sjxxDto.getNbbm().startsWith("MD")?sjxxDto.getNbbm().substring(2):sjxxDto.getNbbm()) + "&sign=1"  + "&num="+(StringUtil.isNotBlank(grszDto.getDysl())?grszDto.getDysl():"6")+"&fontflg=" + fontflg
								+ "&flg=2&word=" + (StringUtil.isNotBlank(nbzbm)?nbzbm.substring(1):"")+ "&qrcode=" + sjxxDto.getNbbm() + "&project=" +sjxxDto.getYbbh() + "&rownum=3";
						map.put("print", url);
						log.error("打印地址：" + url);
					}

				}
				//判断是否为先声标本，如果是则通知先声
				isXianshengSample(dtoById);
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
			log.error("接收报错：" + e.getMessage());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", e.toString());
			log.error("接收报错：" + e.getMessage());
		}
		return map;
	}

	/**
	 * 获取送检结果信息
	 * @param sjxxDto
	 */
	@RequestMapping(value = "/pathogen/minidataGetInspectionResult")
	@ResponseBody
	public void minidataGetInspectionResult(SjxxDto sjxxDto){
		sjxxservice.getInspectionResult(sjxxDto);
	}

	/**
	 * 送检列表查看界面跳转免责声明
	 * @return
	 */
	@RequestMapping(value = "/sjxx/pagedataMzsm")
	public ModelAndView getMzsm() {
        return new ModelAndView("wechat/sjxx/sjxx_mzsm");
	}
	/**
	 * 送检列表查看界面跳转参考指标解释
	 * @return
	 */
	@RequestMapping(value = "/sjxx/getckzbjs")
	public ModelAndView getCkzbjs() {
        return new ModelAndView("wechat/sjxx/sjxx_ckzbjs");
	}

	/**
	 * 删除附件信息
	 * @param fjcfbDto
	 * @return
	 */
	@RequestMapping(value = "/pathogen/pagedataDelFile")
	@ResponseBody
	public Map<String, Object> delFile(FjcfbDto fjcfbDto){
		//获取用户信息
		User user = getLoginInfo();
		fjcfbDto.setScry(user.getYhid());
		boolean isSuccess = fjcfbService.delFile(fjcfbDto);
		if(isSuccess){
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("fjcfbDto", fjcfbDto);

			RestTemplate t_restTemplate = new RestTemplate();
			t_restTemplate.postForObject(menuurl + "/wechat/delInspReportFile", paramMap, String.class);
		}
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 点击检测跳转页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/detection/detectionMod")
	public ModelAndView detectionMod(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_detection");
		List<SjxxDto> sjxxlist=sjxxservice.selectSjxxBySjids(sjxxDto);
		SjxxDto firstsjxx=sjxxlist.get(0);
		List<SjxxDto> sjjcxmlist=sjxxservice.checkJcxm(sjxxDto);
		sjxxDto.setFormAction("/inspection/detection/detectionSaveMod");
		mav.addObject("sjjcxm", sjjcxmlist.get(0));
		/* SjxxDto sjxxDtos=sjxxservice.selectjcbjByid(sjxxDto); */
		mav.addObject("sjxxDto", sjxxDto);
		//查看第一条
		mav.addObject("sjxx", firstsjxx);
		return mav;
	}

	/**
	 * 点击检测跳转页面前校验所选数据检测项目是否相同
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/detection/pagedataCheckJcxm")
	@ResponseBody
	public boolean checkJcxm(SjxxDto sjxxDto) {
		List<SjxxDto> sjjcxmlist=sjxxservice.checkJcxm(sjxxDto);
        return sjjcxmlist == null || sjjcxmlist.size() <= 1;
	}

	/**
	 * 修改检测标记
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/detection/detectionSaveMod")
	@ResponseBody
	public  Map<String, Object> updateJcbjByid(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		sjxxDto.setXgry(user.getYhid());
		try {
			Map<String,Object> checkSyrq=sjxxservice.checkSyrq(sjxxDto);
			if("fail".equals(checkSyrq.get("status"))) {
				return checkSyrq;
			}
			boolean isSuccess = sjxxservice.updateJcbjByid(sjxxDto);
			RestTemplate t_restTemplate=new RestTemplate();

			String reString = t_restTemplate.getForObject(applicationurl+"/experiment/library/pagedataCreateTask?access_token="+request.getParameter("access_token")+"&sjids="+String.join(",",sjxxDto.getIds()), String.class);
			log.error("批量创建任务单："+reString);
			map.put("auditType", AuditTypeEnum.AUDIT_SAMPNUM.getCode());
			map.put("ywid", sjxxDto.getCwid());
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message",e.getMsg());
		}

		return map;
	}

	/**
	 * 发送消息页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/sendMassage")
	public ModelAndView sendMassage(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_sendMessage");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SEND_TYPE});
		mav.addObject("sendType", jclist.get(BasicDataTypeEnum.SEND_TYPE.getCode()));//发送类型
		mav.addObject("sjxxDto",sjxxDto);
		return mav;
	}

	/**
	 * 耐药发送报告
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/sendreportInfo")
	public ModelAndView sendreportInfo(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_sendreport");
		SjxxDto dto = sjxxservice.getDto(sjxxDto);
		String jclx = "";
		List<JcsjDto> list = redisUtil.lgetDto("All_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
		for (JcsjDto jcsjDto : list) {
			if ("072".equals(jcsjDto.getCsdm())){
				jclx = jcsjDto.getCsid();
				break;
			}
		}
		if (StringUtil.isNotBlank(dto.getSjid()) && StringUtil.isNotBlank(jclx)){
			dto.setJclx(jclx);
			dto.setXg_flg("1");
			List<SjnyxDto> sjnyList = sjnyxService.getNyxBySjidAndJclx(dto);
			List<SjwzxxDto> sjwzxxList = sjxxservice.selectWzxxBySjidAndJclx(dto);
			WzysxxDto wzysxxDto=new WzysxxDto();
			wzysxxDto.setJclx(jclx);
			wzysxxDto.setSjid(dto.getSjid());
			List<WzysxxDto> wzysxxDtoList = wzysxxService.getLlh(wzysxxDto);
			List<WzysxxDto> zkxxDtoList=new ArrayList<>();
			if(!CollectionUtils.isEmpty(wzysxxDtoList)){
				//获取最新履历的数据
				WzysxxDto zkll=wzysxxDtoList.get(0);
				wzysxxDto.setLlh(zkll.getLlh());
				wzysxxDto.setSjid(null);
				zkxxDtoList = wzysxxService.getDtoList(wzysxxDto);
				if(!CollectionUtils.isEmpty(zkxxDtoList)){//筛选出NC以及PC的数据
					for(int i=zkxxDtoList.size()-1;i>=0;i--){
						if(!zkxxDtoList.get(i).getNbbm().startsWith("NC")&&!zkxxDtoList.get(i).getNbbm().startsWith("PC")){
							zkxxDtoList.remove(i);
						}
					}
				}
			}
			List<String> ywlxs=new ArrayList<>();
			ywlxs.add(BusTypeEnum.IMP_FILE_JHNY_TEMEPLATE.getCode());
			ywlxs.add(BusTypeEnum.IMP_FILE_JHNY_INFO_TEMEPLATE.getCode());//该文件只在页面上显示，报告不显示，
			mav.addObject("sjnyList",sjnyList);
			mav.addObject("sjwzxxList",sjwzxxList);
			mav.addObject("wzysxxList",wzysxxDtoList);
			mav.addObject("zkxx_json",!CollectionUtils.isEmpty(zkxxDtoList) ? JSON.toJSONString(zkxxDtoList) : JSON.toJSONString(new ArrayList<>()));
			mav.addObject("sjwzxx_json",!CollectionUtils.isEmpty(sjwzxxList) ? JSON.toJSONString(sjwzxxList) : JSON.toJSONString(new ArrayList<>()));
			mav.addObject("sjnyx_json",JSON.toJSONString(sjnyList.stream().filter(e->StringUtil.isNotBlank(e.getSjid())).collect(Collectors.toList())));
			FjcfbDto fjcfbDto=new FjcfbDto();
			fjcfbDto.setYwid(dto.getSjid());
			fjcfbDto.setYwlxs(ywlxs);
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			mav.addObject("fjcfbDtos",fjcfbDtos);
		}
		User yh = getLoginInfo();
		List<User> xtyhDtos = commonService.getShryAndJyryList(getLoginInfo());
		List<User> jyr_list = new ArrayList<>();
		List<User> shr_list = new ArrayList<>();
		Map<String, List<User>> collect = xtyhDtos.stream().collect(Collectors.groupingBy(User::getMrfz));
		Iterator<Map.Entry<String, List<User>>> entries = collect.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, List<User>> entry = entries.next();
			List<User> dtoList = entry.getValue();
			if ("JYRY".equals( entry.getKey())){
				jyr_list = dtoList;
			}
			if ("SHRY".equals( entry.getKey())){
				shr_list = dtoList;
			}
		}
		List<SjxxDto> similarlist=sjxxservice.getSimilarList(dto);
		mav.addObject("similarlist",similarlist);
		mav.addObject("jyr_list", jyr_list);
		mav.addObject("shr_list", shr_list);
		mav.addObject("jclx",jclx);
		if(bioaudurl.endsWith("/"))
			bioaudurl = bioaudurl.substring(0, bioaudurl.length() - 1);
		mav.addObject("bioaudurl",bioaudurl);
		//查询个人设置数据
		GrszDto grszDto=new GrszDto();
		grszDto.setYhid(yh.getYhid());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.TB_AUDIT.getCode(),PersonalSettingEnum.TB_INSPECT.getCode()});
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
		GrszDto grszDto_TB_AUDIT = grszDtoMap.get(PersonalSettingEnum.TB_AUDIT.getCode());
		if(grszDto_TB_AUDIT != null && StringUtil.isBlank(sjxxDto.getShr())) {
			sjxxDto.setShr(grszDto_TB_AUDIT.getYhid());
		}
		GrszDto grszDto_TB_INSPECT = grszDtoMap.get(PersonalSettingEnum.TB_INSPECT.getCode());
		if(grszDto_TB_INSPECT != null && StringUtil.isBlank(sjxxDto.getJyr())) {
			sjxxDto.setJyr(grszDto_TB_INSPECT.getYhid());
		}
		mav.addObject("sjxxDto",dto);

		//tNGS结果查看并设置过滤物种
		sjxxDto.setYbbh(dto.getYbbh());
		dealTngsData(mav,sjxxDto,jclx);
		return mav;
	}

	public void dealTngsData(ModelAndView mav,SjxxDto sjxxDto,String jclx){
		List<SjwzxxDto> sjwzxxDtos=new ArrayList<>();
		List<JcsjDto> jclxlist = redisUtil.lgetDto("All_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
		for(JcsjDto jcsjDto:jclxlist){
			if("063".equals(jcsjDto.getCsdm())){
				jclx=jcsjDto.getCsid();
				sjxxDto.setJclx(jclx);
			}
		}
		//获取信息对应设置的tngs+tbt合并发送时需要过滤的tngs结核物种id
		XxdyDto xxdyDto=new XxdyDto();
		xxdyDto.setDylxcsdm("TNGS_TB_SPECIES_FILTER");
		List<XxdyDto> xxdyDtos=xxdyService.getDtoList(xxdyDto);
		String xxdys="";
		if(!CollectionUtils.isEmpty(xxdyDtos)){
			for(XxdyDto xxdyDto_t:xxdyDtos){
				xxdys=xxdys+","+xxdyDto_t.getDyxx();
			}
			xxdys=xxdys.substring(1);
		}
		sjwzxxDtos=sjxxservice.selectWzxxBySjidAndJclx(sjxxDto);
		if(!CollectionUtils.isEmpty(sjwzxxDtos)) {
			for (SjwzxxDto sjwzxxDto : sjwzxxDtos) {
				if (xxdys.contains(sjwzxxDto.getWzid())) {//若存在则过滤
					sjwzxxDto.setSfhb("0");
				}
			}
		}
		mav.addObject("tngs_sjwzxxDtos",!CollectionUtils.isEmpty(sjwzxxDtos) ? sjwzxxDtos : new ArrayList<>());
		List<SjwzxxDto> sjnyxList=sjnyxService.getNyxMapBySjid(sjxxDto);
		mav.addObject("tngs_sjnyxList",!CollectionUtils.isEmpty(sjnyxList) ? sjnyxList : new ArrayList<>());
		List<SjdlxxDto> sjdlxxDtos=sjdlxxService.getDtoBySjidAndJclx(sjxxDto);
		mav.addObject("tngs_sjdlxxDtos",!CollectionUtils.isEmpty(sjdlxxDtos) ? sjdlxxDtos : new ArrayList<>());
		//获取接口调用明细数据
		SjbgsmDto sjbgsmDto=new SjbgsmDto();
		sjbgsmDto.setSjid(sjxxDto.getSjid());
		sjbgsmDto.setJclx(jclx);
		List<SjbgsmDto> sjbgsmDtos=sjbgsmService.selectBySjbgsmDto(sjbgsmDto);
		mav.addObject("tngs_sffs",!CollectionUtils.isEmpty(sjbgsmDtos)? "1" : "0");
	}

	@RequestMapping("inspection/pagedataOtherNyxxPage")
	public ModelAndView pagedataOtherNyxxPage(SjnyxDto sjnyxDto){
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_otherNy");
		List<SjnyxDto> sjnyxDtos=sjnyxService.getSamePositionNy(sjnyxDto);
		mav.addObject("sjnyxDtos",sjnyxDtos);
		return mav;
	}


	@RequestMapping("inspection/pagedataLl")
	@ResponseBody
	public ModelAndView viewLl(WzysxxDto wzysxxDto){
		List<WzysxxDto> wzysxxDtos=wzysxxService.getDtoList(wzysxxDto);
		if(!CollectionUtils.isEmpty(wzysxxDtos)){//过滤掉NC/PC
			for(int i=wzysxxDtos.size()-1;i>=0;i--){
				if(wzysxxDtos.get(i).getNbbm().startsWith("NC")||wzysxxDtos.get(i).getNbbm().startsWith("PC")){
					wzysxxDtos.remove(i);
				}
			}
		}
		NyysxxDto nyysxxDto=new NyysxxDto();
		nyysxxDto.setJclx(wzysxxDto.getJclx());
		nyysxxDto.setSjid(wzysxxDto.getSjid());
		nyysxxDto.setLlh(wzysxxDto.getLlh());
		List<NyysxxDto> nyysxxDtos=nyysxxService.getDtoList(nyysxxDto);
		List<NyysxxDto> nyysxxDtoList=nyysxxService.getNyysxxList(nyysxxDto);
		if(!CollectionUtils.isEmpty(nyysxxDtoList)){
			String bbh=nyysxxDtoList.get(0).getBbh();
			String json=nyysxxDtoList.get(0).getJson();
			if("v2.0".equals(bbh)){
				ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_sendreportLl_v2");
				List<Map<String,String>> list= JSONArray.parseObject(json,new TypeReference<List<Map<String, String>>>() {});
                if(!CollectionUtils.isEmpty(list)){//显示处理
                    for(Map<String,String> map:list){
                        if(StringUtil.isNotBlank(map.get("突变结果"))){//突变结果不为空，截取_前面的内容作为突变基因内容
                            map.put("突变基因",map.get("突变结果").split("_")[0]);
                        }
                    }
                }
				filterSjnyxList(list,wzysxxDtos);
				mav.addObject("bbh",bbh);
				mav.addObject("nyysxxDtos",list);
				mav.addObject("wzysxxDtos",wzysxxDtos);
				return mav;
			}
		}
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_sendreportLl");
		mav.addObject("nyysxxDtos",nyysxxDtos);
		mav.addObject("bbh","");
		mav.addObject("wzysxxDtos",wzysxxDtos);
		return mav;
	}

	public void filterSjnyxList(List<Map<String,String>> list,List<WzysxxDto> wzysxxDtos){
		boolean ntm=false;//该部分判断若有改动需要前后端一起改
		boolean tb=false;
		for(WzysxxDto wzysxxDto:wzysxxDtos){
			if("36809".equals(wzysxxDto.getWzid()) && !"[]".equals(wzysxxDto.getNtm()) && StringUtil.isNotBlank(wzysxxDto.getNtm())){//若没检出该物种，则ntm中耐药不发送
				ntm=true;
                continue;
			}
			if(!"[]".equals(wzysxxDto.getTb()) && StringUtil.isNotBlank(wzysxxDto.getTb())){
				tb=true;
                continue;
			}
		}
		if(!org.apache.commons.collections.CollectionUtils.isEmpty(list)){
			for(int i=(list.size()-1);i>=0;i--){
				Map<String,String> map = list.get(i);
				if("ntm".equals(map.get("分类")) && !ntm){//过滤ntm
					list.remove(i);
					continue;
				}
				if("tb".equals(map.get("分类")) && !tb){//过滤tb
					list.remove(i);
					continue;
				}
			}
		}
	}

	@RequestMapping("/inspection/pagedataViewBlast")
	@ResponseBody
	public Map<String,Object> viewBlast(SjwzxxDto sjwzxxDto){
		String str_blast="";
		Map<String,Object> map=new HashMap<>();
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(sjwzxxDto.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_JHNY_RESULT_TEMEPLATE.getCode());
		List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		if(!CollectionUtils.isEmpty(fjcfbDtos)){
			FjcfbDto fjcfbDto1=fjcfbDtos.get(0);
			str_blast=sjxxservice.dealBlast(sjwzxxDto,fjcfbDto1);
		}
		map.put("str",str_blast);
		return map;
	}


	/**
	 * 耐药发送报告
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/sendreportSaveInfo")
	@ResponseBody
	public  Map<String,Object> sendreportSaveInfo(SjxxDto sjxxDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		if (StringUtil.isNotBlank(sjxxDto.getSjid())){
			String bcbj = request.getParameter("bcbj");
			try {
				sjxxWsService.editSaveWzxx(sjxxDto,request.getParameter("sjwzxx_json"));
				sjxxWsService.editSaveNyx(sjxxDto,request.getParameter("sjnyx_json"));
				//如果不是保存按钮
				if (!"1".equals(bcbj)){
					String jclx = "";
					List<String> hbjcxms=new ArrayList<>();//tngs+tbt
					List<String> jclxs = new ArrayList<>();
					List<JcsjDto> jclxlist = redisUtil.lgetDto("All_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
					for (JcsjDto jcsjDto : jclxlist) {
						if ("IMP_REPORT_SEQ_TNGS_N".equals(jcsjDto.getCskz3())){
							jclx = jcsjDto.getCsid();
							jclxs.add(jcsjDto.getCsid());
						}
						//泛感染
						if("IMP_REPORT_SEQ_TNGS_E".equals(jcsjDto.getCskz3())){
							jclxs.add(jcsjDto.getCsid());
							hbjcxms.add(jcsjDto.getCsid());
						}
						//TBtNGS+tNGS泛感染
						if("IMP_REPORT_SEQ_TNGS_G".equals(jcsjDto.getCskz3())){
							hbjcxms.add(jcsjDto.getCsid());
						}
					}
					sjxxDto.setJclxs(jclxs);
					sjxxDto.setJclx(jclx);
					//通过履历验证是否有接收到生信上传的文件,查询物种原始信息
					WzysxxDto wzysxxDto=new WzysxxDto();
					wzysxxDto.setSjid(sjxxDto.getSjid());
					wzysxxDto.setJclx(jclx);
					List<WzysxxDto> wzysxxDtoList = wzysxxService.getLlh(wzysxxDto);
					//此处校验了必须有TBT结果
					if(CollectionUtils.isEmpty(wzysxxDtoList)){
						map.put("status","fail");
						map.put("message","还未上传TBT实验结果数据，请等待上传后再发送报告！");
						return map;
					}
					// 增加接口调用明细保存
					JkdymxDto jkdymxDto = new JkdymxDto();
					jkdymxDto.setLxqf("recv");
					jkdymxDto.setDysj(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					jkdymxDto.setNr(JSON.toJSONString(sjxxDto));
					jkdymxDto.setDydz(request.getRequestURI());
					jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_INSPECTION.getCode());
					jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_REPORT.getCode());
					jkdymxDto.setSfcg("2");
					List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
					Optional<JcsjDto> tb_optional = list.stream().filter(item -> "C".equals(item.getCskz1()) && "IMP_REPORT_SEQ_TNGS_N".equals(item.getCskz3())).findFirst();
					Optional<JcsjDto> tb_tngs_optional = list.stream().filter(item -> "C".equals(item.getCskz1()) && "IMP_REPORT_SEQ_TNGS_G".equals(item.getCskz3())).findFirst();
					SjxxDto dto = sjxxservice.getDto(sjxxDto);
					boolean isSuccess=false;
					if (tb_tngs_optional.isPresent() && "2".equals(bcbj)){//发送时存tb，合并发送存tb+tngs
						//检查是否为TBT+tngs项目，若不是，则返回。
						boolean check=sjxxWsService.checkSend(dto,hbjcxms);
						if(!check){
							map.put("status","fail");
							map.put("message","该标本不是TBT+tNGS泛感染项目，无法合并发送报告！");
							return map;
						}
						jkdymxDto.setZywid(tb_tngs_optional.get().getCsid());
						isSuccess = sjxxWsService.mergeSendReportFile(sjxxDto,dto,user);
					}else{
						jkdymxDto.setZywid(tb_optional.get().getCsid());
						isSuccess = sjxxWsService.sendReportFile(sjxxDto,dto);
						jkdymxDto.setYwid(dto.getYbbh());
						if (isSuccess){
							jkdymxDto.setSfcg("1");
						} else{
							jkdymxDto.setSfcg("0");
						}
						jkdymxService.insertJkdymxDto(jkdymxDto);//添加接口调用明细数据
					}
				}
				//查询个人设置数据
				GrszDto grszDto=new GrszDto();
				grszDto.setYhid(user.getYhid());
				grszDto.setSzlbs(new String[]{PersonalSettingEnum.TB_AUDIT.getCode(),PersonalSettingEnum.TB_INSPECT.getCode()});
				Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
				String shr = StringUtil.isNotBlank(sjxxDto.getShr()) ? sjxxDto.getShr().split("-")[0] : "";
				GrszDto grszxx_shr = grszDtoMap.get(PersonalSettingEnum.TB_AUDIT.getCode());
				if (grszxx_shr == null) {
					grszDto.setSzz(shr);
					grszDto.setLrry(user.getYhid());
					grszDto.setSzlb(PersonalSettingEnum.TB_AUDIT.getCode());
					grszService.insertDto(grszDto);
				} else {
					if (grszxx_shr.getSzz()==null  || !grszxx_shr.getSzz().equals(shr)) {
						grszDto.setSzz(shr);
						grszService.updateByYhidAndSzlb(grszDto);
					}
				}


				String jyr = StringUtil.isNotBlank(sjxxDto.getJyr()) ? sjxxDto.getJyr().split("-")[0] : "";
				GrszDto grszxx_jyr = grszDtoMap.get(PersonalSettingEnum.TB_INSPECT.getCode());
				if (grszxx_jyr == null) {
					grszDto.setSzz(jyr);
					grszDto.setLrry(user.getYhid());
					grszDto.setSzlb(PersonalSettingEnum.TB_INSPECT.getCode());
					grszService.insertDto(grszDto);
				} else {
					if (grszxx_jyr.getSzz()==null  || !grszxx_jyr.getSzz().equals(jyr)) {
						grszDto.setSzz(jyr);
						grszService.updateByYhidAndSzlb(grszDto);
					}
				}
				map.put("status","success");
				map.put("message","1".equals(bcbj)?"保存成功！":"发送成功！");
			} catch (BusinessException e) {
				map.put("status","fail");
				map.put("message",e.getMessage());
			}
		}else{
			map.put("status","fail");
			map.put("message","获取标本信息失败！");
		}
		return map;
	}
	/**
	 * 发送消息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/sendSaveMassage")
	@ResponseBody
	public Map<String,Object> saveMassage(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		try {
			boolean isSuccess = sjxxservice.sendMessage(sjxxDto);
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglservice.getModelById("ICOM99024").getXxnr():xxglservice.getModelById("ICOM99025").getXxnr());
		} catch (BusinessException e) {
			map.put("status","fail");
			map.put("message", e.toString());
		}
		return map;
	}

	/**
	 * 送检报告导入
	 * @return
	 */
	@RequestMapping("/inspection/pageImport")
	public ModelAndView sjxxImport() {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_import");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMPORT_INSPECTION.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		return mav;
	}

	/**
	 *扩展参数修改
	 * @return
	 */
	@RequestMapping("/inspection/extendCskzView")
	public ModelAndView extendCskzView(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_cskz");
		List<SjxxDto> sjxxDtos=sjxxservice.getSjxxCskz(sjxxDto);
		Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.FIRST_SJXXKZ,BasicDataTypeEnum.SECOND_SJXXKZ,BasicDataTypeEnum.THIRD_SJXXKZ,BasicDataTypeEnum.FOURTH_SJXXKZ});
		mav.addObject("sjkz1list", jcsjlist.get(BasicDataTypeEnum.FIRST_SJXXKZ.getCode()));
		mav.addObject("sjkz2list", jcsjlist.get(BasicDataTypeEnum.SECOND_SJXXKZ.getCode()));
		mav.addObject("sjkz3list", jcsjlist.get(BasicDataTypeEnum.THIRD_SJXXKZ.getCode()));
		mav.addObject("sjkz4list", jcsjlist.get(BasicDataTypeEnum.FOURTH_SJXXKZ.getCode()));
		mav.addObject("ksxxList", sjxxservice.getSjdw());
		if(sjxxDtos!=null && sjxxDtos.get(0)!=null) {
			sjxxDto.setCskz1(sjxxDtos.get(0).getCskz1());
			sjxxDto.setCskz2(sjxxDtos.get(0).getCskz2());
			sjxxDto.setCskz3(sjxxDtos.get(0).getCskz3());
			sjxxDto.setCskz4(sjxxDtos.get(0).getCskz4());
			sjxxDto.setCskz5(sjxxDtos.get(0).getCskz5());
			if (sjxxDtos.size()>0){
				boolean isKsSame = true;
				boolean isQtksSame = true;
				String qtks = StringUtil.isNotBlank(sjxxDtos.get(0).getQtks())?sjxxDtos.get(0).getQtks():"";
				String ks = StringUtil.isNotBlank(sjxxDtos.get(0).getKs())?sjxxDtos.get(0).getKs():"";
				for (SjxxDto sjxxDto_t : sjxxDtos) {
					if (!ks.equals(sjxxDto_t.getKs())){
						isKsSame = false;
					}
					if (!qtks.equals(sjxxDto_t.getQtks())){
						isQtksSame = false;
					}
					if (!isKsSame && !isQtksSame){
						break;
					}
				}
				if (isKsSame){
					sjxxDto.setKs(ks);
				}
				if (isQtksSame){
					sjxxDto.setQtks(qtks);
				}
			}
		}
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}

	/**
	 * 参数扩展保存
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/extendSaveCskzView")
	@ResponseBody
	public Map<String,Object> cskzSave(SjxxDto sjxxDto){
		User user=getLoginInfo();
		sjxxDto.setXgry(user.getYhid());
		Map<String,Object> map= new HashMap<>();
		//校验如果是否收费选择了自免时，检查选择的标本中检测项目是否符合要求
		if("2".equals(sjxxDto.getSfsf()) || "3".equals(sjxxDto.getSfsf())) {
			List<SjxxDto> list=sjxxservice.selectSjxxBySjids(sjxxDto);
			if(list!=null && list.size()>0) {
				for(int i=0;i<list.size();i++) {
					if("D".equals(list.get(i).getCskz1()) || "R".equals(list.get(i).getCskz1())) {
						map.put("status", "fail");
						map.put("message", "标本编号为"+list.get(i).getYbbh()+"的标本是否收费不符合要求!");
						return map;
					}
				}
			}
		}
		boolean isSuccess=sjxxservice.updateCskz(sjxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}


	/**
	 * 获取复检页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/recheckSjxx")
	public ModelAndView  recheckSjxx(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_recheck");
		SjxxDto sjxxDtos=sjxxservice.getDto(sjxxDto);
		if("1".equals(sjxxDtos.getYyxxCskz1())) {
			sjxxDtos.setSjdwmc(sjxxDtos.getHospitalname()+"--"+sjxxDtos.getSjdwmc());
		}
		SjybztDto sjybztDto = new SjybztDto();
		sjybztDto.setSjid(sjxxDto.getSjid());
		String xzbj="";
		sjybztDto.setYbztCskz1("S");
		List<SjybztDto> sjybztDtos = sjybztService.getDtoList(sjybztDto);
		if(sjybztDtos!=null && sjybztDtos.size()>0) {
			xzbj = "1";

		}
		List<JcsjDto> detectlist= redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
//		if(sjxxDtos.getJc_cskz2()!=null) {
//			JcsjDto jcsjDto=new JcsjDto();
//			String[] cskz2=sjxxDtos.getJc_cskz2().split(",");
//			jcsjDto.setCsdms(cskz2);
//			jcsjDto.setJclb(BasicDataTypeEnum.DETECT_TYPE.getCode());
//			detectlist=jcsjService.getJcsjDtoList(jcsjDto);
//		}
		//查询去人源检测项目  2020/09/11  张晗    
		//获取去人源项目不再根据scbj为3，更改为所选送检信息的原检测项目的cskz3加上_REM modify  姚嘉伟  2020/09/18
		//<!--select>
		JcsjDto jcsj_REMDto=new JcsjDto();
		if(sjxxDtos.getCskz3()!=null && !sjxxDtos.getCskz3().endsWith("_REM")) {
			String cskz3=sjxxDtos.getCskz3()+"_REM";
			jcsj_REMDto.setCskz3(cskz3);
		}else {
			jcsj_REMDto.setCskz3(sjxxDtos.getCskz3());
		}
		jcsj_REMDto.setJclb(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> REMList=jcsjService.getInstopDtoList(jcsj_REMDto);
		detectlist.addAll(REMList);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.RECHECK,BasicDataTypeEnum.RECHECK_REASON});
		List<JcsjDto> recheckType = jclist.get(BasicDataTypeEnum.RECHECK.getCode());
		//若检测项目为ResFirst项目，则类别仅复测申请，加测项目仅ResFirst
//		if (StringUtil.isNotBlank(sjxxDtos.getJcxmmc()) && "ResFirst".equals(sjxxDtos.getJcxmmc())){
//			for(JcsjDto jc : recheckType){
//				if ("RECHECK".equals(jc.getCsdm())){
//					recheckType.clear();
//					recheckType.add(jc);
//					break;
//				}
//			}
//			for(JcsjDto jc : detectlist){
//				if ("047".equals(jc.getCsdm())){
//					detectlist.clear();
//					detectlist.add(jc);
//					break;
//				}
//			}
//		}

		//复检项目采用基础数据
		//<!--select>
		mav.addObject("RecheckType", recheckType); //检测项目
        mav.addObject("fjyylist",jclist.get(BasicDataTypeEnum.RECHECK_REASON.getCode()));//复检原因
		mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		//<!-- select>
		mav.addObject("detectlist", detectlist);//检测项目
		mav.addObject("sjxxDto", sjxxDtos);
		mav.addObject("xzbj", xzbj);
		return mav;
	}

	/**
	 * 钉钉端复检申请
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/minidataAddRecheckDingtalk")
	@ResponseBody
	public Map<String,Object>  addRecheckDingtalk(SjxxDto sjxxDto,FjsqDto fjsqDto,HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.RECHECK,BasicDataTypeEnum.RECHECK_REASON});
		List<JcsjDto> fjyyDtos=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RECHECK_REASON.getCode());
		List<JcsjDto> fjyylist=new ArrayList<>();
		FjsqyyDto fjsqyyDto=new FjsqyyDto();
		fjsqyyDto.setFjid(fjsqDto.getFjid());
		List<FjsqyyDto> fjsqyyDtos=fjsqyyService.getDtoList(fjsqyyDto);
		if(StringUtil.isNotBlank(fjsqDto.getFjid())) {
			FjsqDto fjsqDto_t=fjsqService.getDtoById(fjsqDto.getFjid());
			if(fjsqDto_t!=null) {
				map.put("fjsqDto", fjsqDto_t);
				sjxxDto.setSjid(fjsqDto_t.getSjid());
				if(fjsqyyDtos!=null && fjsqyyDtos.size()>0){
					for(int i=0;i<fjyyDtos.size();i++){
						if(fjsqDto_t.getLx().equals(fjyyDtos.get(i).getFcsid())) {
							fjyylist.add(fjyyDtos.get(i));
						}
						for(FjsqyyDto t_fjsqyyDto : fjsqyyDtos){
							if(t_fjsqyyDto.getYy().equals(fjyyDtos.get(i).getCsid())){
								fjyyDtos.get(i).setChecked("1");//是否选中
							}
						}
					}
				}
			}else{
				fjyylist=jclist.get(BasicDataTypeEnum.RECHECK_REASON.getCode());
			}
		}else{
			fjyylist=jclist.get(BasicDataTypeEnum.RECHECK_REASON.getCode());
		}
		SjxxDto sjxxDtos=sjxxservice.getDto(sjxxDto);
		SjybztDto sjybztDto = new SjybztDto();
	    sjybztDto.setSjid(sjxxDto.getSjid());
	    String xzbj="";
	    sjybztDto.setYbztCskz1("S");
	    List<SjybztDto> sjybztDtos = sjybztService.getDtoList(sjybztDto);
	    if(sjybztDtos!=null && sjybztDtos.size()>0) {
	      xzbj = "1";
	    }
		List<JcsjDto> detectlist= redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
//		if(sjxxDtos.getJc_cskz2()!=null) {
//			JcsjDto jcsjDto=new JcsjDto();
//			String[] cskz2=sjxxDtos.getJc_cskz2().split(",");
//			jcsjDto.setCsdms(cskz2);
//			jcsjDto.setJclb(BasicDataTypeEnum.DETECT_TYPE.getCode());
//			detectlist=jcsjService.getJcsjDtoList(jcsjDto);
//		}
		//获取去人源项目不再根据scbj为3，更改为所选送检信息的原检测项目的cskz3加上_REM modify  姚嘉伟  2020/09/18

		JcsjDto jcsj_REMDto=new JcsjDto();
		if(sjxxDtos.getCskz3()!=null && !sjxxDtos.getCskz3().endsWith("_REM")) {
			String cskz3=sjxxDtos.getCskz3()+"_REM";
			jcsj_REMDto.setCskz3(cskz3);
		}else {
			jcsj_REMDto.setCskz3(sjxxDtos.getCskz3());
		}
		jcsj_REMDto.setJclb(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> REMList=jcsjService.getInstopDtoList(jcsj_REMDto);
		detectlist.addAll(REMList);
		//复检项目采用基础数据
		map.put("recheckType", jclist.get(BasicDataTypeEnum.RECHECK.getCode())); //检测项目
		map.put("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		map.put("fjyyList", fjyylist);//复检原因
		map.put("detectlist", detectlist);//检测项目
		map.put("subdetectlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode()));//检测子项目
		map.put("sjxxDto", sjxxDtos);
		map.put("xzbj", xzbj);//判断量仅一次
		screenClassColumns(request,map);
		return map;
	}

	/**
	 * 复检保存
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/inspection/recheckSaveSjxx")
	@ResponseBody
	public Map<String,Object> recheckSaveSjxx(FjsqDto fjsqDto){
		Map<String, Object> map = new HashMap<>();
		try {
			User user = getLoginInfo();
			boolean isSuccess = false;
			Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.RECHECK, BasicDataTypeEnum.DETECT_TYPE});
			List<JcsjDto> list = jclist.get(BasicDataTypeEnum.RECHECK.getCode());
			List<JcsjDto> jcxmList = jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
			JcsjDto jcsj_jcxm = new JcsjDto();
			String shlx = "";
			String cskz1 = "";
			String csdm = "";
			for (JcsjDto dto : jcxmList) {
				if (dto.getCsid().equals(fjsqDto.getJcxm())) {
					cskz1 = dto.getCskz1();
					jcsj_jcxm = dto;
					break;
				}
			}
			for (JcsjDto dto : list) {
				if (dto.getCsid().equals(fjsqDto.getLx())) {
					csdm = dto.getCsdm();
					break;
				}
			}
			//当复测加测项目为RFS项目时走RFS复测审核流程
			if ("F".equals(cskz1)) {
				shlx = AuditTypeEnum.AUDIT_RFS_FC.getCode();
			} else {
				for (JcsjDto dto : list) {
					if (dto.getCsid().equals(fjsqDto.getLx())) {
						if ("RECHECK".equals(dto.getCsdm())) {
							shlx = AuditTypeEnum.AUDIT_RECHECK.getCode();
						} else if ("ADDDETECT".equals(dto.getCsdm())) {
							shlx = AuditTypeEnum.AUDIT_ADDCHECK.getCode();
						} else if ("REM".equals(dto.getCsdm())) {
							shlx = AuditTypeEnum.AUDIT_ADDCHECK_REM.getCode();
						} else if ("LAB_RECHECK".equals(dto.getCsdm())) {
							shlx = AuditTypeEnum.AUDIT_LAB_RECHECK.getCode();
						} else if ("LAB_ADDDETECT".equals(dto.getCsdm())) {
							shlx = AuditTypeEnum.AUDIT_LAB_ADDCHECK.getCode();
						} else if ("LAB_REM".equals(dto.getCsdm())) {
							shlx = AuditTypeEnum.AUDIT_LAB_ADDCHECK_REM.getCode();
						}
						break;
					}
				}
			}
			fjsqDto.setShlx(shlx);
			if (AuditTypeEnum.AUDIT_ADDCHECK.getCode().equals(shlx)) {
				SjxxDto sjxxDto = sjxxservice.getDtoById(fjsqDto.getSjid());
				if (sjxxDto != null && StringUtil.isNotBlank(fjsqDto.getSfff()) && "1".equals(fjsqDto.getSfff()) && StringUtil.isNotBlank(sjxxDto.getDb())) {
					HbsfbzDto hbsfbzDto = new HbsfbzDto();
					hbsfbzDto.setHbmc(sjxxDto.getDb());
					List<HbsfbzDto> hbsfbzDtos = hbsfbzService.getDtoList(hbsfbzDto);
					if (hbsfbzDtos != null && hbsfbzDtos.size() > 0) {
						for (HbsfbzDto dto : hbsfbzDtos) {
							if (fjsqDto.getJcxm().equals(dto.getXm())) {
								if (StringUtil.isNotBlank(fjsqDto.getJczxm())) {
									if (fjsqDto.getJczxm().equals(dto.getZxm())) {
										fjsqDto.setYfje(dto.getSfbz());
										break;
									}
								} else {
									fjsqDto.setYfje(dto.getSfbz());
									break;
								}
							}
						}
					}
				}
			}
			//先检查加测的项目是否和已有的项目有冲突，如果有冲突，则不允许加测。如今病原体列表复测加测页面只是不允许加测和原项目相同的项目，还需要增加不允许加测已加测的项目
			// 此处会根据项目来判断是否在原有项目里，才决定是否可以复测，但当前逻辑有点复杂，所以放开不再显示，复测可以不重叠，由人工判断，所以注释掉
//		try {
//			isSuccess = fjsqService.checkCanFj(fjsqDto,shlx,jcsj_jcxm,csdm,list);
//        } catch (BusinessException e) {
//			map.put("status","fail");
//			map.put("message",e.getMessage());
//			return map;
//        }
			List<String> lxs = new ArrayList<>();
			if (StringUtil.isNotBlank(csdm)) {
				String[] strings = csdm.split("_");
				for (JcsjDto dto : list) {
					if (dto.getCsdm().indexOf(strings[strings.length - 1]) != -1) {
						lxs.add(dto.getCsid());
					}
				}
			}
			FjsqDto fjsqDto_t = new FjsqDto();
			fjsqDto_t.setSjid(fjsqDto.getSjid());
			// 直接根据项目来判断是否存在相同项目的记录，不再区分是复测还是加测或者某一类的加测
			//fjsqDto_t.setFjlxs(lxs);
			fjsqDto_t.setJcxmdm(jcsj_jcxm.getCsdm());
			//另外不再执行两次sql，一次返回list 就可以了
			//int num=fjsqService.getCount(fjsqDto_t);//查询当前送检信息是否已经存在
			List<FjsqDto> fjsqDtos = fjsqService.getDtoList(fjsqDto_t);
			if (fjsqDtos != null && fjsqDtos.size() > 0) {
				for (int i = 0; i < fjsqDtos.size(); i++) {
				/*if(StatusEnum.CHECK_NO.getCode().equals(fjsqDtos.get(i).getZt())){
					map.put("status",isSuccess?"success":"fail");
					map.put("message",isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():"该信息已经申请过，请找到该条记录进行提交审核！");
					return map;
				}*/
					if (StatusEnum.CHECK_SUBMIT.getCode().equals(fjsqDtos.get(i).getZt())) {
						map.put("status", "fail");
						map.put("message", "该信息正在审核中！");
						return map;
					}
					if (!StatusEnum.CHECK_PASS.getCode().equals(fjsqDtos.get(i).getZt())
							&& !StatusEnum.CHECK_UNPASS.getCode().equals(fjsqDtos.get(i).getZt())
							&& !StatusEnum.CHECK_NO.getCode().equals(fjsqDtos.get(i).getZt())) {
						map.put("status", "fail");
						map.put("message", "已存在同样项目的信息，并且状态！");
						return map;
					}
				}
			}
			//else if(num==0){//如果num=0，说明不存在，直接进行添加操作
			fjsqDto.setLrry(user.getYhid());
			fjsqDto.setXgry(user.getYhid());
			fjsqDto.setZt(StatusEnum.CHECK_NO.getCode());
			if (StringUtil.isBlank(fjsqDto.getFjid())) {
				fjsqDto.setFjid(StringUtil.generateUUID());
			}
			isSuccess = fjsqService.insertFjsq(fjsqDto);
			//保存原因
			if (fjsqDto.getYys() != null && fjsqDto.getYys().length > 0) {
				List<FjsqyyDto> fjsqyyList = new ArrayList<>();
				for (int i = 0; i < fjsqDto.getYys().length; i++) {
					FjsqyyDto fjsqyyDto = new FjsqyyDto();
					fjsqyyDto.setFjid(fjsqDto.getFjid());
					fjsqyyDto.setYy(fjsqDto.getYys()[i]);
					fjsqyyList.add(fjsqyyDto);
				}
				fjsqyyService.addDtoList(fjsqyyList);
			}
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglservice.getModelById("ICOM00001").getXxnr() : xxglservice.getModelById("ICOM00002").getXxnr());
			map.put("ywid", fjsqDto.getFjid());
			map.put("auditType", shlx);

			SjxxDto sjxxDto = new SjxxDto();
			sjxxDto.setSjid(fjsqDto.getSjid());
			sjxxDto = sjxxservice.getDto(sjxxDto);

			fjsqService.addOrUpdateSyData(fjsqDto, sjxxDto);
			if (isSuccess)
				sjxxservice.checkSybgSfwc();
		}catch(Exception e){
			map.put("status", "fail");
			map.put("message", "复检加测保存时出现错误，错误信息为" + e.getMessage());
			log.error("复检保存时出现错误，错误信息为" + e.getMessage());
		}
		return map;
	}

	/**
	 * 复检保存(手机端)
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/dingtalk/recheckSaveSjxx")
	@ResponseBody
	public Map<String,Object> dingtalkRecheckSaveSjxx(FjsqDto fjsqDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=false;
		List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RECHECK.getCode());
		String csdm="";
		for(JcsjDto dto:list){
			if(dto.getCsid().equals(fjsqDto.getLx())){
				csdm=dto.getCsdm();
				break;
			}
		}
		List<String> lxs=new ArrayList<>();
		if(StringUtil.isNotBlank(csdm)){
			String[] strings = csdm.split("_");
			for(JcsjDto dto:list){
				if(dto.getCsdm().indexOf(strings[strings.length-1])!=-1){
					lxs.add(dto.getCsid());
				}
			}
		}
		FjsqDto fjsqDto_t=new FjsqDto();
		fjsqDto_t.setSjid(fjsqDto.getSjid());
		fjsqDto_t.setFjlxs(lxs);
		int num=fjsqService.getCount(fjsqDto_t);//查询当前送检信息是否已经存在
		fjsqDto.setShlx(AuditTypeEnum.AUDIT_DING_RECHECK.getCode());
		if(num>0){ //num>0说明已经存在，然后去判断状态
			List<FjsqDto> fjsqDtos = fjsqService.getDtoList(fjsqDto_t);
			if(fjsqDtos!=null && fjsqDtos.size() > 0) {
				map.put("auditType",AuditTypeEnum.AUDIT_DING_RECHECK);
				for (int i = 0; i < fjsqDtos.size(); i++) {
					if(StatusEnum.CHECK_NO.getCode().equals(fjsqDtos.get(i).getZt())){
						map.put("status","success");
						map.put("ywid",fjsqDtos.get(i).getFjid());
						return map;
					}
					if(StatusEnum.CHECK_SUBMIT.getCode().equals(fjsqDtos.get(i).getZt())){
						map.put("status",isSuccess?"success":"fail");
						map.put("message",isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():"该信息正在审核中！");
						return map;
					}
					//如果状态是审核不通过，则进行修改操作
					if(StatusEnum.CHECK_UNPASS.getCode().equals(fjsqDtos.get(i).getZt())){
						fjsqDtos.get(i).setXgry(fjsqDto.getLrry());
						fjsqDtos.get(i).setZt(StatusEnum.CHECK_NO.getCode());
						fjsqDtos.get(i).setSfff(fjsqDto.getSfff());
						isSuccess = fjsqService.updateZt(fjsqDtos.get(i));
						map.put("ywid",fjsqDtos.get(i).getFjid());
						map.put("status",isSuccess?"success":"fail");
						map.put("message",isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():"状态修改失败！");
						return map;
					}
					if(!StatusEnum.CHECK_PASS.getCode().equals(fjsqDtos.get(i).getZt()) && !StatusEnum.CHECK_UNPASS.getCode().equals(fjsqDtos.get(i).getZt())){
						return map;
					}
				}
				//如果状态是审核通过，则新增一条记录
				fjsqDto.setZt(StatusEnum.CHECK_NO.getCode());
				if(StringUtil.isBlank(fjsqDto.getFjid())){
					fjsqDto.setFjid(StringUtil.generateUUID());
				}
				isSuccess=fjsqService.insertFjsq(fjsqDto);
				map.put("status",isSuccess?"success":"fail");
				map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
				map.put("ywid",fjsqDto.getFjid());
			}
		}else if(num==0){
			//如果num=0，说明不存在，直接进行添加操作
			fjsqDto.setZt(StatusEnum.CHECK_NO.getCode());
			if(StringUtil.isBlank(fjsqDto.getFjid())){
				fjsqDto.setFjid(StringUtil.generateUUID());
			}
			isSuccess=fjsqService.insertFjsq(fjsqDto);
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
			map.put("ywid",fjsqDto.getFjid());
			map.put("auditType",AuditTypeEnum.AUDIT_DING_RECHECK);
		}
		if(isSuccess)
			sjxxservice.checkSybgSfwc();
		return map;
	}

	/**
	 * 外部提交复检申请
	 * @param request
	 * @param br
	 * @return
	 */
	@RequestMapping(value="/recheck/submitRecheckAudit")
	@ResponseBody
	public Map<String, Object> submitRecheckAudit(HttpServletRequest request,BufferedReader br){
		//request.getb
		String inputLine;
		String str = "";
		log.error("生信提交复测!");
		Map<String,Object> map = new HashMap<>();
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			log.error("接收内容：" + str);
			br.close();
			if(StringUtil.isBlank(str)) {
				map.put("status", "fail");
				map.put("errorCode", "未正常获取到数据！");
				log.error("未正常获取到数据！");
			}else{
				boolean result = sjxxservice.submitRecheckAudit(str, request);
				if(result){
					map.put("status","success");
					map.put("errorCode", 0);
				}else{
					map.put("status", "fail");
					map.put("errorCode", "保存失败！");
					log.error("保存失败！");
					log.error(str);
				}
			}
		} catch (IOException e) {
			map.put("status", "fail");
			map.put("errorCode", "IO获取数据异常！");
			log.error("IO获取数据异常！" + e.toString());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorCode", e.getMessage());
			log.error(e.toString());
		}
		return map;
	}

	/**
	 * 获取送检提取信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/pagedataTqxx")
	@ResponseBody
	public Map<String,Object> gettqxx(SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		MultiValueMap<String, Object> tqparamMap = new LinkedMultiValueMap<>();
		//调用igams-experiment中的接口，获取浓度提取信息
		tqparamMap.add("sjid", sjxxDto.getSjid());
		tqparamMap.add("access_token",sjxxDto.getAccess_token());
		String tqurl=applicationurl+"/experiment/potency/commGetTqxxBySjid";
		RestTemplate t_restTemplate = new RestTemplate();
		@SuppressWarnings("unchecked")
		List<Object> tqlist=t_restTemplate.postForObject(tqurl, tqparamMap, List.class);
		map.put("tqxx", tqlist);
		//调用igams-experiment中的接口，获取文库信息
		MultiValueMap<String, Object> wkparamMap = new LinkedMultiValueMap<>();
		wkparamMap.add("sjid", sjxxDto.getSjid());
		wkparamMap.add("access_token", sjxxDto.getAccess_token());
		String wkurl=applicationurl+"/experiment/library/commGetWkmxBySjid";
		@SuppressWarnings("unchecked")
		List<Object> wklist=t_restTemplate.postForObject(wkurl, wkparamMap, List.class);
		map.put("wkxx",wklist);
		//调用igams-experiment中的接口，获取文库上机信息
		if(wklist!=null && wklist.size()>0){
			MultiValueMap<String, Object> wksjparamMap = new LinkedMultiValueMap<>();
			wksjparamMap.add("sjid",sjxxDto.getSjid());
			wksjparamMap.add("access_token", sjxxDto.getAccess_token());
			String wksjurl=applicationurl+"/experiment/library/commGetWksjxxBySjid";
			@SuppressWarnings("unchecked")
			List<Object> wksjgllist=t_restTemplate.postForObject(wksjurl, wksjparamMap, List.class);
			map.put("wksjxx",wksjgllist);
		}
		return map;
	}

	/**
	 * 获取送检检测结果
	 * @param
	 * @return
	 */
	@RequestMapping(value="/inspection/pagedataSjjcjg")
	@ResponseBody
	public Map<String,Object> pagedataSjjcjg(SjjcjgDto sjjcjgDto){
		Map<String,Object> return_map= new HashMap<>();
		List<SjjcjgDto> dtoList = sjjcjgService.getDtoList(sjjcjgDto);
		List<SjjcjgDto> dtoList_jl = new ArrayList<>();
		List<SjjcjgDto> dtoList_zk = new ArrayList<>();
		List<SjjcjgDto> dtoList_yin = new ArrayList<>();
		List<SjjcjgDto> dtoList_yang = new ArrayList<>();
		List<SjjcjgDto> dtoList_hui = new ArrayList<>();
		if (null != dtoList && dtoList.size() >0){

			for (int i = 0; i <dtoList.size() ; i++) {
				if (dtoList.get(i).getJcjgmc().startsWith("NC") ){
					if("阳性".equals(dtoList.get(i).getJlmc())){
						dtoList.get(i).setStyleFlag("1");
					}
					dtoList_zk.add(dtoList.get(i));
				}else if(dtoList.get(i).getJcjgmc().startsWith("PC")){
					if("阴性".equals(dtoList.get(i).getJlmc())){
						dtoList.get(i).setStyleFlag("1");
					}
					dtoList_zk.add(dtoList.get(i));
				}else{
					dtoList_jl.add(dtoList.get(i));
				}
			}

			if(dtoList_zk!=null&&dtoList_zk.size() > 0){
				SjjcjgDto sjjcjgDto_avg1=new SjjcjgDto();
				sjjcjgDto_avg1.setJcjgmc("NC-av1");
				sjjcjgDto_avg1.setJlmc("原始数值");
				SjjcjgDto sjjcjgDto_avg2=new SjjcjgDto();
				sjjcjgDto_avg2.setJcjgmc("NC-av2");
				sjjcjgDto_avg2.setJlmc("原始数值");
				for (int i = 0; i <dtoList.size() ; i++) {
					if ("A".equals(dtoList.get(i).getJcjgcskz4())){
						sjjcjgDto_avg1.setJgsz(dtoList.get(i).getPjz());
					}else if("B".equals(dtoList.get(i).getJcjgcskz4())){
						sjjcjgDto_avg2.setJgsz(dtoList.get(i).getPjz());
					}
				}
				dtoList_zk.add(sjjcjgDto_avg1);
				dtoList_zk.add(sjjcjgDto_avg2);
			}
			Map<String, List<SjjcjgDto>> map = dtoList_jl.stream().collect(Collectors.groupingBy(SjjcjgDto::getJlmc));
			if (null != map && map.size()>0){
				Iterator<Map.Entry<String, List<SjjcjgDto>>> entries = map.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry<String, List<SjjcjgDto>> entry = entries.next();
					if ("阴性".equals(entry.getKey())){
						dtoList_yin = entry.getValue();
					}else if ("阳性".equals(entry.getKey())){
						dtoList_yang = entry.getValue();
					}else {
						dtoList_hui = entry.getValue();
					}
				}
			}
		}
		return_map.put("dtoList_zk", dtoList_zk);
		return_map.put("dtoList_yin", sjjcjgService.streamList(dtoList_yin));
		return_map.put("dtoList_yang", sjjcjgService.streamList(dtoList_yang));
		return_map.put("dtoList_hui", sjjcjgService.streamList(dtoList_hui));
		return return_map;
	}

	/**
	 * 获取NC送检检测结果
	 * @param
	 * @return
	 */
	@RequestMapping(value="/inspection/pagedataSjjcjgNC")
	@ResponseBody
	public Map<String,Object> pagedataSjjcjgNC(SjjcjgDto sjjcjgDto){
		Map<String,Object> return_map= new HashMap<>();
		List<SjjcjgDto> dtoList = sjjcjgService.getDtoList(sjjcjgDto);
		if (null!= dtoList && dtoList.size()>0){
			dtoList.sort(new Comparator<>() {
                             @Override
                             public int compare(SjjcjgDto sjjcjgDto, SjjcjgDto sjjcjgDtoq) {
                                 return Double.compare(Double.parseDouble(sjjcjgDtoq.getNcjsz()), Double.parseDouble(sjjcjgDto.getNcjsz()));
                             }
                         }
			);
		}
		return_map.put("dtoList", dtoList);
		return return_map;
	}

	/**
	 * 跳转反馈界面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/feedbackSjxx")
	public ModelAndView feedbackSjxx(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_feedback");
		SjxxDto sjxxDto2=sjxxservice.getDto(sjxxDto);
		sjxxDto2.setYwlx(BusTypeEnum.IMP_FEEDBACK.getCode());
		//查看附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto2.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_FEEDBACK.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("sjxxDto", sjxxDto2);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		return mav;
	}

	/**
	 * 保存临床反馈
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/feedbackSaveSjxx")
	@ResponseBody
	public Map<String,Object> feedbackSaveSjxx(SjxxDto sjxxDto){
		User user=getLoginInfo();
		sjxxDto.setXgry(user.getYhid());
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=sjxxservice.updateFeedBack(sjxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 根据送检ID获取标本详细结果
	 * @param sjxxjgDto
	 * @return
	 */
	@RequestMapping(value = "/getspeciessjxxjg")
	@ResponseBody
	public Map<String,Object> getSpeciesSjxxjg(SjxxjgDto sjxxjgDto){
		Map<String, Object> map= new HashMap<>();
		List<SjxxjgDto>	sjxxjglist=sjxxjgService.getxxjgByFjdidIsNull(sjxxjgDto);
		if(sjxxjglist!=null && sjxxjglist.size()>0) {
			List<SjxxjgDto> sjxxjgcllist=sjxxjgService.getxxInSpecies(sjxxjglist);
			List<SjxxjgDto> finalsjxxjglist= new ArrayList<>();
			if(sjxxjgcllist!=null && sjxxjgcllist.size() > 0) {
				int presize=0;
				for(int i=0;i<sjxxjgcllist.size();i++) {
					int size=sjxxjgcllist.get(i).getNp().length();
					  if(i == 0){
				            finalsjxxjglist.add(sjxxjgcllist.get(i));
				          }else{
				            if(presize < size){
				              finalsjxxjglist.set(finalsjxxjglist.size()-1, sjxxjgcllist.get(i));
				            }else{
				              finalsjxxjglist.add(sjxxjgcllist.get(i));
				            }
				          }
					presize=size;
				}
				map.put("rows", finalsjxxjglist);
			}else {
				map.put("rows",new ArrayList<SjxxjgDto>());
			}
		}
		map.put("total",sjxxjgDto.getTotalNumber());
		return map;
	}

	/**
	 * 根据送检ID获取标本详细结果
	 * @param sjxxjgDto
	 * @return
	 */
	@RequestMapping(value = "/getgenussjxxjg")
	@ResponseBody
	public Map<String,Object> getGenusSjxxjg(SjxxjgDto sjxxjgDto){
		Map<String, Object> map= new HashMap<>();
		List<SjxxjgDto>	sjxxjglist=sjxxjgService.getxxjgByFjdidIsNull(sjxxjgDto);
		if(sjxxjglist!=null && sjxxjglist.size() > 0) {
			List<SjxxjgDto> genuslist=sjxxjgService.getxxInGenus(sjxxjglist);
			if(genuslist != null && genuslist.size() > 0) {
				map.put("rows",genuslist);
			}else {
				map.put("rows",new ArrayList<SjxxjgDto>());
			}
		}else {
			map.put("rows",new ArrayList<SjxxjgDto>());
		}
		map.put("total",sjxxjgDto.getTotalNumber());
		return map;
	}

	/**
	 * 选择下载报告压缩包条件
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/inspection/reportdownloadZip")
	public ModelAndView reportdownloadZip(SjxxDto sjxxDto, HttpServletRequest request){
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_reportDownload");
		String flg = request.getParameter("flg");
		mav.addObject("flg", flg);
		return mav;
	}

	/**
	 * 根据条件下载报告压缩包
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/inspection/commonReportdownloadSaveZip")
	@ResponseBody
	public Map<String,Object> reportZipDownloadOut(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("status", "fail");
		User user=getLoginInfo();
		//根据角色查询到角色检测单位信息
		List<Map<String,String>> jcdwList = sjxxservice.getJsjcdwByjsid(user.getDqjs());
		if(jcdwList!=null && jcdwList.size() > 0) {
			//判断检测单位是否为1（单位限制）
			if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				//取出检测单位为一个List
				List<String> strList = new ArrayList<>();
				for (int i = 0; i < jcdwList.size(); i++){
					if(jcdwList.get(i).get("jcdw")!=null) {
						strList.add(jcdwList.get(i).get("jcdw"));
					}
				}
				//如果检测单位不为空，进行查询。
				if(strList!=null && strList.size()>0) {
					sjxxDto.setJcdwxz(strList);
				}else {
					//如果检测单位为空，直接返回空。没有查看权限
					map.put("error", "当前用户没有操作权限!");
					return map;
				}
			}
		}
		String flg = request.getParameter("flg");
		if(StringUtil.isNotBlank(flg) && "1".equals(flg)) {
			List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
			if(hbqxList!=null && hbqxList.size()>0) {
				List<String> hbmcList=sjhbxxservice.getHbmcByHbid(hbqxList);
				if(hbmcList!=null  && hbmcList.size()>0) {
					sjxxDto.setDbs(hbmcList);
					map = sjxxservice.reportZipDownloadSjhb(sjxxDto, request);
				}
			}
		}else {
			map = sjxxservice.reportZipDownload(sjxxDto, request);
		}
		return map;
	}

	@RequestMapping("/inspection/print_Nbbm")
	@ResponseBody
	public Map<String,Object> print_Nbbm(SjxxDto sjxxDto,HttpServletRequest request){
		String ip = request.getHeader("X-Forwarded-For");
    	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                sjxxDto.setPrint_demise_ip(ip);
            }
        }
		Map<String,Object> map= new HashMap<>();
		SjxxDto sjxxDto_s=sjxxservice.getSjxxDto(sjxxDto);
		map.put("sjxxDto", sjxxDto_s);
		return map;
	}

	/**
	 * 查看异常信息列表
	 * @return
	 */
	@RequestMapping("/inspection/exceptionlistView")
	public ModelAndView exceptionlistView(SjxxDto sjxxDto,HttpServletRequest request) {
		ModelAndView mav =new ModelAndView("wechat/sjxx/sjxx_exceptionList");
		User user=getLoginInfo();
		SjycDto sjycDto=new SjycDto();
		sjycDto.setSjid(sjxxDto.getSjid());
		sjycDto.setYhid(user.getYhid());
		sjycDto.setMrtwr(user.getYhid());
		sjycDto.setMrtwrjs(user.getDqjs());
		sjycDto.setYwlx(BusTypeEnum.IMP_EXCEPTION.getCode());
		mav.addObject("sjycDto", sjycDto);
		mav.addObject("modqx", "0");
		return mav;
	}


	/**
	 * 添加送检验证
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/verifSjxx")
	public ModelAndView  addverification(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/verification/verification_add");
		SjxxDto sjxxDto_t=sjxxservice.getDto(sjxxDto);
		List<JcsjDto> distinguishList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DISTINGUISH.getCode());
		if (StringUtil.isNotBlank(sjxxDto_t.getNbbm())){
			String lastCode = sjxxDto_t.getNbbm();
			lastCode = lastCode.substring(lastCode.length()-1);
			if ("B".equals(lastCode) || "b".equals(lastCode)){
				mav.addObject("isB", true);
				List<JcsjDto> lsdistinguishList = new ArrayList<>();
				for (JcsjDto distinguish : distinguishList) {
					if (!"REM".equals(distinguish.getCskz1())){
						lsdistinguishList.add(distinguish);
					}
				}
				distinguishList = lsdistinguishList;
			}
		}else {
			String yblx = sjxxDto_t.getYblx();
			List<JcsjDto> yblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
			for (JcsjDto yblxJcsj : yblxList) {
				if (yblx.equals(yblxJcsj.getCsid())) {
					String yblxcsdm = yblxJcsj.getCsdm();
					if ("B".equals(yblxcsdm) || "b".equals(yblxcsdm)){
						mav.addObject("isB", true);
						List<JcsjDto> lsdistinguishList = new ArrayList<>();
						for (JcsjDto distinguish : distinguishList) {
							if (!"REM".equals(distinguish.getCskz1())){
								lsdistinguishList.add(distinguish);
							}
						}
						distinguishList = lsdistinguishList;
					}
				}
			}
		}
		mav.addObject("distinguishList", distinguishList);//区分
		mav.addObject("verificationList", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.VERIFICATION_TYPE.getCode()));//送检验证类型
		mav.addObject("verificationresultList", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.VERIFICATION_RESULT.getCode()));//送检验证类型
		mav.addObject("clientnoticeList", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CLIENT_NOTICE.getCode()));//客户通知
		mav.addObject("detectionList", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("sjxxDto", sjxxDto_t);
		SjyzDto sjyzDto=new SjyzDto();
		mav.addObject("sjyzDto", sjyzDto);
		mav.addObject("formAction", "/inspection/inspection/minidataVerifSaveSjxx");
		mav.addObject("btnFlg","default");
		return mav;
	}

	/**
	 * 小程序跳转送检验证录入界面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/minidataAddVerificationPage")
	@ResponseBody
	public Map<String,Object>  minidataAddVerificationPage(SjxxDto sjxxDto,HttpServletRequest request) {
		Map<String,Object> map= new HashMap<>();
		SjxxDto sjxxDto_t=sjxxservice.getDto(sjxxDto);
		SjyzDto sjyzDto=new SjyzDto();
		List<SjyzDto> sjyzList = new ArrayList<>();
		if(sjxxDto.getYzid()!=null) {
			sjyzDto=sjyzService.getDtoById(sjxxDto.getYzid());
		}
		if(sjxxDto.getSjid()!=null) {
			sjyzList = sjyzService.getDtoListBySjid(sjxxDto.getSjid());
		}
		if(sjyzDto==null) {
			sjyzDto=new SjyzDto();
		}else {
			sjxxDto_t.setJcdw(sjyzDto.getJcdw());
		}
		sjyzDto.setAuditType(AuditTypeEnum.AUDIT_VERIFICATION.getCode());
		map.put("sjxxDto",sjxxDto_t);
		map.put("sjyzDto", sjyzDto);
		map.put("sjyzList",sjyzList);
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
		map.put("distinguishList", distinguishList);//区分
		map.put("clientnoticeList", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CLIENT_NOTICE.getCode()));//客户通知
		map.put("detectionList", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		screenClassColumns(request,map);
		map.put("verificationList", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.VERIFICATION_TYPE.getCode()));//送检验证类型
		map.put("verifyresultList", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.VERIFY_RESULT.getCode()));//送检验证结果
		map.put("verificationresultList", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.VERIFICATION_RESULT.getCode()));//送检报告结果
		map.put("wecgatsampletypeList", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//送检报告结果
		return map;
	}

	/**
	 * 送检验证保存
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/inspection/minidataVerifSaveSjxx")
	@ResponseBody
	public Map<String,Object> verifSaveSjxx(SjyzDto sjyzDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=false;
		List<String> strlist= new ArrayList<>();
		List<JcsjDto> distinguishList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DISTINGUISH.getCode());
		for (JcsjDto distinguish : distinguishList) {
			if (StringUtil.isNotBlank(sjyzDto.getQf())&&sjyzDto.getQf().equals(distinguish.getCsid())){
				sjyzDto.setAuditType(distinguish.getCskz2());
				break;
			}
		}
		strlist.add(StatusEnum.CHECK_NO.getCode());
		strlist.add(StatusEnum.CHECK_SUBMIT.getCode());
		sjyzDto.setZts(strlist);
		int num=sjyzService.getCount(sjyzDto);
		if(num>0) {
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():"当前验证类别已在审核队列中!");
		}else {
			User user=getLoginInfo();
			sjyzDto.setLrry(user.getYhid());
			sjyzDto.setZt(StatusEnum.CHECK_NO.getCode());
			sjyzDto.setXsbj("1");
			isSuccess=sjyzService.insertDto(sjyzDto);
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
			//获取内部编码最后一位
			if(StringUtil.isNotBlank(sjyzDto.getNbbm())){
				String suffix = sjyzDto.getNbbm().substring(sjyzDto.getNbbm().length() - 1);
				if(!"B".equals(suffix)){//当内部编码的最后一位为B，就是血液的时候，不再自动发起PCR
					map.put("ywid",sjyzDto.getYzid());
					map.put("auditType",sjyzDto.getAuditType());
				}
			}else{
				map.put("ywid",sjyzDto.getYzid());
				map.put("auditType",sjyzDto.getAuditType());
			}
		}
		return map;
	}

	/**
	 * 调用金域接口获取数据
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/pagedataGoldenData")
	@ResponseBody
	public Map<String, Object> getGoldenData(SjxxDto sjxxDto){
        return sjxxWsService.getGoldenData(sjxxDto);
	}

	/**
	 * 重发报告选择
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/resendReport")
	public ModelAndView  resendReport(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_resend");
		SjbgsmDto sjbgsmDto=new SjbgsmDto();
		sjbgsmDto.setIds(sjxxDto.getIds());
		List<SjbgsmDto> sjbgsmDtos = sjbgsmservice.selectGroupByJclx(sjbgsmDto);

		List<JcsjDto> jcsjlist=new ArrayList<>();
		for(SjbgsmDto dto:sjbgsmDtos){
			if(StringUtil.isNotBlank(dto.getJczlx()))
			{
				Object o_sub_jcsj = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode(), dto.getJczlx());
				if(o_sub_jcsj==null)
					continue;
				JcsjDto sub_jcsj_dto = JSONObject.parseObject(o_sub_jcsj.toString(), JcsjDto.class);
				Object o_jcsj = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), dto.getJclx());
				if(o_jcsj==null)
					continue;
				JcsjDto jcsj_dto = JSONObject.parseObject(o_jcsj.toString(), JcsjDto.class);
				sub_jcsj_dto.setFcsdm(jcsj_dto.getCsdm());
				sub_jcsj_dto.setFcsmc(jcsj_dto.getCsmc());
				jcsjlist.add(sub_jcsj_dto);
			}else{
				Object o_jcsj = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), dto.getJclx());
				JcsjDto jcsj_dto = JSONObject.parseObject(o_jcsj.toString(), JcsjDto.class);
				jcsjlist.add(jcsj_dto);
			}
		}
		mav.addObject("jcsjlist",jcsjlist);
		mav.addObject("sjxxDto",sjxxDto);
		return mav;
	}

	/**
	 * 数据合并
	 * @return
	 */
	@RequestMapping("/inspection/dataMerging")
	@ResponseBody
	public Map<String,Object> dataMerging(SjxxDto sjxxDto){

		Map<String,Object>map=new HashMap<>();
		map.put("status","success");
		List<String> ids=sjxxDto.getIds();
		SjxxDto sjxxDto_1=new SjxxDto();
		sjxxDto_1.setSjid(ids.get(0));
		SjxxDto sjxxDto_get1=sjxxservice.getSjxxDto(sjxxDto_1);
		SjxxDto sjxxDto_2=new SjxxDto();
		sjxxDto_2.setSjid(ids.get(1));
		SjxxDto sjxxDto_get2=sjxxservice.getSjxxDto(sjxxDto_2);
		if(!(sjxxDto_get1.getHzxm().equals(sjxxDto_get2.getHzxm()))||!(sjxxDto_get1.getDb().equals(sjxxDto_get2.getDb()))){
			map.put("status","error");
			map.put("message","两条记录患者或者合作伙伴不一致");
			return map;
		}
		if(!StringUtil.isNotBlank(sjxxDto_get2.getBgrq())&&!StringUtil.isNotBlank(sjxxDto_get1.getBgrq())){
			map.put("status","error");
			map.put("message","两条记录都没有报告日期");
			return map;
		}
		if(StringUtil.isNotBlank(sjxxDto_get2.getBgrq())&&StringUtil.isNotBlank(sjxxDto_get1.getBgrq())){
			map.put("status","error");
			map.put("message","两条记录都已经有报告日期");
			return map;
		}
		SjkzxxDto sjkzxxDto_1=sjkzxxService.getDtoById(sjxxDto_get1.getSjid());
		SjkzxxDto sjkzxxDto_2=sjkzxxService.getDtoById(sjxxDto_get2.getSjid());
		User user=getLoginInfo();
		try {
			if ((StringUtil.isNotBlank(sjxxDto_get1.getWbbm()) && StringUtil.isNotBlank(sjxxDto_get1.getNbbm()))
			|| (StringUtil.isNotBlank(sjxxDto_get2.getWbbm()) && StringUtil.isNotBlank(sjxxDto_get2.getNbbm()))){

				map.put("message","数据合并出错,可能存在重复合并!若要强制合并,请去除含有内部编码样本的外部编码!");
			} else {
				if(StringUtil.isNotBlank(sjxxDto_get1.getBgrq()) && !StringUtil.isNotBlank(sjxxDto_get2.getBgrq())){
					sjxxservice.copySjxx(sjxxDto_get1,sjxxDto_get2,user);
					if(sjkzxxDto_1!=null&&sjkzxxDto_2!=null){
						sjkzxxService.copySjkzxx(sjkzxxDto_1,sjkzxxDto_2,user);
					}
					wbsjxxService.copyWbsjxx(sjxxDto_get1.getSjid(),sjxxDto_get2.getSjid(),user);
				}else if(!StringUtil.isNotBlank(sjxxDto_get1.getBgrq()) && StringUtil.isNotBlank(sjxxDto_get2.getBgrq())){
					sjxxservice.copySjxx(sjxxDto_get2,sjxxDto_get1,user);
					if(sjkzxxDto_1!=null&&sjkzxxDto_2!=null){
						sjkzxxService.copySjkzxx(sjkzxxDto_2,sjkzxxDto_1,user);
					}
					wbsjxxService.copyWbsjxx(sjxxDto_get2.getSjid(),sjxxDto_get1.getSjid(),user);
				}
				map.put("message","需要生信重新发送报告，**因为样本编号已更改，无法对应，无法采用病原体的重新发送功能");
			}
		}catch (Exception e){
			map.put("status","error");
			log.error("整合错误"+e.getMessage());
			map.put("message","整合错误请联系管理员");
		}

		return map;
	}

	/**
	 * 批量重发报告
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/resendSaveReport")
	@ResponseBody
	public Map<String, Object> dealBatchResendReport(HttpServletRequest request, SjxxDto sjxxDto){
		List<String> jclxs=new ArrayList<>();
		List<String> jczlxs=new ArrayList<>();
		List<JcsjDto> jcsj_lxs=new ArrayList<>();
		if(StringUtil.isNotBlank(sjxxDto.getJclx())){
			String[] split = sjxxDto.getJclx().split(",");
			//切分后先区分是子项目还是项目
			for(String jclx:split){
				Object o_sub_jcsj = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode(), jclx);
				if(o_sub_jcsj!=null) {
					jczlxs.add(jclx);
					jcsj_lxs.add(JSONObject.parseObject(o_sub_jcsj.toString(), JcsjDto.class));
					continue;
				}else{
					Object o_jcsj = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), jclx);
					if(o_jcsj==null)
						continue;
					jcsj_lxs.add(JSONObject.parseObject(o_jcsj.toString(), JcsjDto.class));
					jclxs.add(jclx);
				}
			}
		}
		Map<String,Object> params = new HashMap<>();
		params.put("ids", sjxxDto.getIds());
		params.put("jclxs", jclxs);
		params.put("jczlxs", jczlxs);
		User user = getLoginInfo();
		sjxxDto.setYhm(user.getYhm());
		Map<String, Object> map = new HashMap<>();
		if(sjxxDto.getIds() != null && sjxxDto.getIds().size() > 0) {
			// 判断调用接口是否有报告信息
			List<SjxxDto> sjxxDtos  = sjxxservice.selectReportInfo(params);
			if(sjxxDtos != null && sjxxDtos.size() > 0) {
				String key = String.valueOf(System.currentTimeMillis());
				map.put("key", key);
				map.put("totalCount", sjxxDtos.size());
				// 开启一个线程发送报告
				ResendReport resendReport = new ResendReport();
				resendReport.init2(key, sjxxDtos, redisUtil, sjxxDto.getYhm(),jcsj_lxs);
				ResendReportThread resendReportThread = new ResendReportThread(resendReport);
				resendReportThread.start();
				map.put("status", "success");
				map.put("message", "发送成功！");
			}else {
				map.put("status", "fail");
				map.put("message", "选中记录中没有相关报告信息！");
			}
		}else {
			map.put("status", "fail");
			map.put("message", "未获取到选中信息！");
		}
		return map;
	}

	/**
	 * 查看送检待发送报告
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/pagedataQuerySjxx")
	@ResponseBody
	public Map<String, Object> querySjxx(SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		List<SjxxDto> sjxxDtos_t = new ArrayList<>();
		//临时定死 
		List<String> dbs = new ArrayList<String>();
		dbs.add("上海六院");
		dbs.add("安徽省立");
		dbs.add("艾迪康实验室");
		sjxxDto.setDbs(dbs);
		
		Integer sjxxNum = sjxxservice.querysjxxnum(sjxxDto);
		Integer fjNum = sjxxservice.queryWordNum(sjxxDto);
		Integer fjyzNumBefore=sjxxservice.queryFjAndYzBeforeNum(sjxxDto);
		Integer fjyzNumAfter=sjxxservice.queryFjAndYzAfterNum(sjxxDto);
		if(sjxxNum > 0 ) {
			sjxxDtos_t = sjxxservice.querySjxx(sjxxDto);
		}
		if (fjyzNumBefore>0 && "fjyzNumbefore".equals(sjxxDto.getFlg_qf())){
			sjxxDtos_t = sjxxservice.queryFjAndYzBefore(sjxxDto);
		}
		if (fjyzNumAfter>0 && "fjyzNumAfter".equals(sjxxDto.getFlg_qf())){
			sjxxDtos_t = sjxxservice.queryFjAndYzAfter(sjxxDto);
		}
		map.put("fjyzNumBefore",fjyzNumBefore);
		map.put("fjyzNumAfter",fjyzNumAfter);
		map.put("sjxxNum", sjxxNum);
		map.put("fjNum", fjNum);
		map.put("sjxxDtos_t", sjxxDtos_t);
		return map;
	}


	/**
	 * 待发送报告总数
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/pagedataTotal")
	@ResponseBody
	public Map<String, Object> getTotal(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		String querydivision=request.getParameter("querydivision");
		String zyid=request.getParameter("zyid");
		//临时定死 2024-03-28
		List<String> dbs = new ArrayList<String>();
		dbs.add("上海六院");
		dbs.add("安徽省立");
		dbs.add("艾迪康实验室");
		sjxxDto.setDbs(dbs);
		
		Integer sjxxNum = sjxxservice.querysjxxnum(sjxxDto);
		Integer fjNum = sjxxservice.queryWordNum(sjxxDto);
		Integer fjyzNumBefore=sjxxservice.queryFjAndYzBeforeNum(sjxxDto);
		Integer fjyzNumAfter=sjxxservice.queryFjAndYzAfterNum(sjxxDto);
		map.put("sjxxNum", sjxxNum);
		map.put("fjNum", fjNum);
		map.put("fjyzNumBefore",fjyzNumBefore);
		map.put("fjyzNumAfter",fjyzNumAfter);
		if("LISTREPORT".equals(querydivision)){
			List<Map<String,Object>> listmap=new ArrayList<>();
			LscxszDto lscxszDto=new LscxszDto();
			lscxszDto.setCxmc(zyid+"-listmap");
			List<LscxszDto> lscxszDtos = lscxszService.getDtoListByCode(lscxszDto);
			if(!CollectionUtils.isEmpty(lscxszDtos)){
				for(LscxszDto t_lscxszDto:lscxszDtos){
					String cxdmone = lscxszService.dealStatisticsQuerySql(t_lscxszDto, user.getYhid());
					t_lscxszDto.setCxdm(cxdmone);
					String cxdm = lscxszService.dealQuerySql(t_lscxszDto, user.getYhid());
					lscxszDto.setCxdm(cxdm);
					List<HashMap<String, Object>> listResult = lscxszService.getQueryResult(lscxszDto);
					if(!CollectionUtils.isEmpty(listResult)){
						listResult.get(0).put("listtable",t_lscxszDto.getCxmc().replace("listmap","listtable"));//点击显示列表关联标记
						listmap.add(listResult.get(0));
					}
				}
			}
			map.put("listmap", listmap);
		}
		return map;
	}

	@RequestMapping(value = "/inspection/pagedataQueryListTable")
	@ResponseBody
	public Map<String,Object> pagedataQueryListTable(LscxszDto lscxszDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		List<LscxszDto> lscxszDtos = lscxszService.getDtoListByCode(lscxszDto);
		if(!CollectionUtils.isEmpty(lscxszDtos)){
			String cxdmone = lscxszService.dealStatisticsQuerySql(lscxszDtos.get(0), user.getYhid());
			lscxszDtos.get(0).setCxdm(cxdmone);
			String cxdm = lscxszService.dealQuerySql(lscxszDtos.get(0), user.getYhid());
			lscxszDto.setCxdm(cxdm);
			List<HashMap<String, Object>> listResult = lscxszService.getQueryResult(lscxszDto);
			map.put("listtable",listResult);
		}
		return map;
	}

	/**
	 * 查看上传Word待发送报告
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/pagedataQueryWord")
	@ResponseBody
	public Map<String, Object> queryWord(SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		List<SjxxDto> sjxxDtos_t = new ArrayList<>();
		//临时定死 2024-03-28
		List<String> dbs = new ArrayList<String>();
		dbs.add("上海六院");
		dbs.add("安徽省立");
		dbs.add("艾迪康实验室");
		sjxxDto.setDbs(dbs);
		
		Integer sjxxNum = sjxxservice.querysjxxnum(sjxxDto);
		int fjNum = sjxxservice.queryWordNum(sjxxDto);
		if(fjNum > 0) {
			sjxxDtos_t = sjxxservice.queryWord(sjxxDto);
		}
		map.put("sjxxNum", sjxxNum);
		map.put("fjNum", fjNum);
		map.put("sjxxDtos_t", sjxxDtos_t);
		return map;
	}

	/**
	 * 退款确认页面
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/inspection/refundPay")
	public ModelAndView refundPay(SjxxDto sjxxDto, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/sjxx/sjxx_payRefund");
		SjxxDto t_sjxxDto = sjxxservice.getDto(sjxxDto);
		mav.addObject("sjxxDto", t_sjxxDto);
		// 查询送检退款信息
		List<PayinfoDto> payinfoDtos = payinfoService.selectByYwxx(t_sjxxDto.getSjid(), BusinessTypeEnum.SJ.getCode());
		mav.addObject("payinfoDtos", payinfoDtos);
		return mav;
	}

	/**
	 * 生成退款订单
	 * @param payinfoDto
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/inspection/refundSavePay")
	@ResponseBody
	public Map<String, Object> payRefundApply(PayinfoDto payinfoDto, HttpServletRequest request){
		User user=getLoginInfo();
		payinfoDto.setLrry(user.getYhid());
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		String sign = commonService.getSign(payinfoDto.getZfid());
		payinfoDto.setSign(sign);
		payinfoDto.setYwlx(BusinessTypeEnum.SJ.getCode());
		paramMap.add("payinfoDto", payinfoDto);
		RestTemplate t_restTemplate = new RestTemplate();
		log.error("退款申请访问服务器：" + menuurl + "/wechat/pay/localRefundApply zfid:" + payinfoDto.getZfid());
		String str = t_restTemplate.postForObject(menuurl + "/wechat/pay/localRefundApply", paramMap, String.class);
		return JSON.parseObject(str, HashMap.class);
	}

	/**
	 * 根据检测项目查询检测子项目
	 * @param jcxmid
	 * @return
	 */
	@RequestMapping("/inspection/pagedataSubDetect")
	@ResponseBody
	public Map<String, Object> getSubDetect(String jcxmid){
		Map<String,Object> map = new HashMap<>();
		List<JcsjDto> jcsjlist;
		if(StringUtil.isNotBlank(jcxmid)){
			jcsjlist = sjxxservice.getSubDetect(jcxmid);
		}else{
			jcsjlist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
		}
		map.put("subdetectlist", jcsjlist);
		return map;
	}

	/**
	 * 根据检测项目查询已发送报告的检测子项目
	 * @param jcxmid
	 * @return
	 */
	@RequestMapping("/inspection/pagedataResendSubDetect")
	@ResponseBody
	public Map<String, Object> getResendSubDetect(String jcxmid){
		Map<String,Object> map = new HashMap<>();
		List<JcsjDto> jcsjlist;
		if(StringUtil.isNotBlank(jcxmid)){
			jcsjlist = sjxxservice.getSubDetect(jcxmid);
		}else{
			jcsjlist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
		}
		map.put("subdetectlist", jcsjlist);
		return map;
	}

	/**
	 * 清空检测检测子项目
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/pagedataEmptySubDetect")
	@ResponseBody
	public Map<String, Object> emptySubDetect(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = sjjcxmservice.emptySubDetect(sjxxDto.getSjid());
		map.put("status", isSuccess?"success":"fail");
		return map;
	}
	/**
	 * 特检导入页面
	 * @return
	 */
	@RequestMapping(value ="/inspection/pageImportInspection")
	public ModelAndView importReport(){
		ModelAndView mav = new ModelAndView("wechat/sjxx/specialInspection_import");
		User user=getLoginInfo();
		GrszDto grszDto_t=new GrszDto();
		grszDto_t.setYhid(user.getYhid());
		grszDto_t.setSzlb(PersonalSettingEnum.HOSPITAL_SELECT.getCode());
		grszDto_t=grszService.selectGrszDtoByYhidAndSzlb(grszDto_t);
		String sjdw = "";
		String sjdwmc = "";
		if(grszDto_t==null) {
			mav.addObject("grszDto", new GrszDto());
		}else {
			String szz = grszDto_t.getSzz();
			if (StringUtil.isNotBlank(szz)){
				Map<String,String> settingJson = JSON.parseObject(szz, Map.class);
				if (settingJson != null && StringUtil.isNotBlank(settingJson.get("sjdw"))){
					sjdw = settingJson.get("sjdw");
				}
				if (settingJson != null && StringUtil.isNotBlank(settingJson.get("sjdwmc"))){
					sjdwmc = settingJson.get("sjdwmc");
				}
			}
			mav.addObject("sjdw", sjdw);
			mav.addObject("sjdwmc", sjdwmc);
		}
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SPECIALINSPECTION.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		return mav;
	}

	/**
	 * wzzk导入页面
	 * @return
	 */
	@RequestMapping(value ="/inspection/pageImportWzzk")
	public ModelAndView importReport_wzzk(){
		ModelAndView mav = new ModelAndView("wechat/sjxx/wzzkInspection_import");
		User user=getLoginInfo();
		GrszDto grszDto_t=new GrszDto();
		grszDto_t.setYhid(user.getYhid());
		grszDto_t.setSzlb(PersonalSettingEnum.HOSPITAL_SELECT.getCode());
		grszDto_t=grszService.selectGrszDtoByYhidAndSzlb(grszDto_t);
		String sjdw = "";
		String sjdwmc = "";
		if(grszDto_t==null) {
			mav.addObject("grszDto", new GrszDto());
		}else {
			String szz = grszDto_t.getSzz();
			if (StringUtil.isNotBlank(szz)){
				Map<String,String> settingJson = JSON.parseObject(szz, Map.class);
				if (settingJson != null && StringUtil.isNotBlank(settingJson.get("sjdw"))){
					sjdw = settingJson.get("sjdw");
				}
				if (settingJson != null && StringUtil.isNotBlank(settingJson.get("sjdwmc"))){
					sjdwmc = settingJson.get("sjdwmc");
				}
			}
			mav.addObject("sjdw", sjdw);
			mav.addObject("sjdwmc", sjdwmc);
		}
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_WZZK.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		return mav;
	}
	/**
	 * 特检导入页面
	 * @return
	 */
	@RequestMapping(value ="/inspection/pageDownloadInspectionReport")
	public ModelAndView pageDownloadInspectionReport(){
		ModelAndView mav = new ModelAndView("wechat/sjxx/inspectionReport_download");
		User user=getLoginInfo();
		GrszDto grszDto_t=new GrszDto();
		grszDto_t.setYhid(user.getYhid());
		grszDto_t.setSzlb(PersonalSettingEnum.HOSPITAL_SELECT.getCode());
		grszDto_t=grszService.selectGrszDtoByYhidAndSzlb(grszDto_t);
		String sjdw = "";
		String sjdwmc = "";
		if(grszDto_t==null) {
			mav.addObject("grszDto", new GrszDto());
		}else {
			String szz = grszDto_t.getSzz();
			if (StringUtil.isNotBlank(szz)){
				Map<String,String> settingJson = JSON.parseObject(szz, Map.class);
				if (settingJson != null && StringUtil.isNotBlank(settingJson.get("sjdw"))){
					sjdw = settingJson.get("sjdw");
				}
				if (settingJson != null && StringUtil.isNotBlank(settingJson.get("sjdwmc"))){
					sjdwmc = settingJson.get("sjdwmc");
				}
			}
			mav.addObject("sjdw", sjdw);
			mav.addObject("sjdwmc", sjdwmc);
		}
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SPECIALINSPECTION.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		return mav;
	}

	/**
	 * 压缩下载
	 *
	 * @param httpResponse
	 * @return
	 */
	@RequestMapping("/inspection/pagedataZipDownload")
	@ResponseBody
	public Map<String,Object> pagedataZipDownload(HttpServletRequest request, HttpServletResponse httpResponse) {
		Map<String,Object> map=new HashMap<>();
		try {
			map=sjxxservice.packageZipAndDownload(request,httpResponse);
		} catch (IOException e) {
			log.error("导出出错");
		}
		return map;
	}

	/**
	 * 提交送检验证审核
	 * @return
	 */
	@RequestMapping(value ="/auditProcess/pagedataPCRAudit")
	@ResponseBody
	public Map<String,Object> PCRAudit(SjxxDto sjxxDto, HttpServletRequest request){
		return sjxxservice.sendPCRAudit(sjxxDto);
	}

	/**
	 * 废弃送检记录，前端js中检查是否有接收日期，接收日期为空的才允许scbj置为1，不为空不允许删除
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/discardSjxx")
	@ResponseBody
	public Map<String, Object> discardSjxx(SjxxDto sjxxDto){
		User user=getLoginInfo();
		sjxxDto.setScry(user.getYhid());
		List<String> list = sjxxservice.getJsrqIsNull(sjxxDto);//由ids查找出接收日期为null的可废弃数据
		sjxxDto.setIds(list);
		boolean isSuccess=sjxxservice.delete(sjxxDto);
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_DEL.getCode() + JSONObject.toJSONString(sjxxDto));
		Map<String, Object> map= new HashMap<>();
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 添加特殊申请
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/specialapplyApplication")
	public ModelAndView  addApplication(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/application/application_edit");
		SjxxDto sjxxDto_t=sjxxservice.getDto(sjxxDto);
		SjtssqDto sjtssqDto=new SjtssqDto();
		sjtssqDto.setYbbh(sjxxDto_t.getYbbh());
		sjtssqDto.setNbbm(sjxxDto_t.getNbbm());
		sjtssqDto.setHzxm(sjxxDto_t.getHzxm());
		sjtssqDto.setXbmc(sjxxDto_t.getXbmc());
		sjtssqDto.setDb(sjxxDto_t.getDb());
		sjtssqDto.setJcxmmc(sjxxDto_t.getJcxmmc());
		sjtssqDto.setYblxmc(sjxxDto_t.getYblxmc());
		sjtssqDto.setDwjc(sjxxDto_t.getDwjc());
		sjtssqDto.setBgrq(sjxxDto_t.getBgrq());
		sjtssqDto.setSjid(sjxxDto_t.getSjid());
		sjxxDto_t.setAuditType(AuditTypeEnum.AUDIT_FREESAMPLES.getCode());
		List<JcsjDto> dtoList =new ArrayList<>();
		SjjcxmDto sjjcxmDto=new SjjcxmDto();
		sjjcxmDto.setSjid(sjxxDto_t.getSjid());
		List<SjjcxmDto> sjjcxmList=sjjcxmservice.getDtoList(sjjcxmDto);
		mav.addObject("sjjcxmList",sjjcxmList);
		mav.addObject("jcsjlist", dtoList);
		mav.addObject("sjtssqDto", sjtssqDto);
		mav.addObject("applylist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.APPLICATION_ITEM.getCode()));
		mav.addObject("formAction", "/inspection/inspection/specialapplySaveApplication");
		return mav;
	}
	/**
	 * 申请保存接口
	 * @param sjtssqDto
	 * @return
	 */
	@RequestMapping(value ="/inspection/specialapplySaveApplication")
	@ResponseBody
	public Map<String,Object> applySaveInterface(SjtssqDto sjtssqDto, HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		//boolean isSuccess=false;
		List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.APPLICATION_ITEM.getCode());
		if(list!=null&&list.size()>0){
			for(JcsjDto dto:list){
				if(sjtssqDto.getSqxm().equals(dto.getCsid())){
					sjtssqDto.setSqxmdm(dto.getCsdm());
					sjtssqDto.setSqxmmc(dto.getCsmc());
				}
			}
		}
		if(StringUtil.isNotBlank(sjtssqDto.getJcxmid())){
			String[] xminfo = sjtssqDto.getJcxmid().split("-");
			if(xminfo.length>1){
				sjtssqDto.setJcxmid(xminfo[0]);
				sjtssqDto.setJczxmid(xminfo[1]);
			}
		}
		if(StringUtil.isNotBlank(sjtssqDto.getSjid())){
			SjxxDto sjxxDto=new SjxxDto();
			sjxxDto.setSjid(sjtssqDto.getSjid());
			SjxxDto sjxxDto_t=sjxxservice.getDto(sjxxDto);
			if("FREE".equals(sjtssqDto.getSqxmdm())){
				if(StringUtil.isNotBlank(sjxxDto_t.getBgrq())){
					map.put("status","fail");
					map.put("message","已有报告日期，不允许申请免费项目!");
					return map;
				}
			}else{
				if(StringUtil.isNotBlank(sjxxDto_t.getJsrq())){
					map.put("status","fail");
					map.put("message","已有接收日期，不允许申请VIP/PK项目!");
					return map;
				}
			}
		}
		User user=getLoginInfo();
		sjtssqDto.setLrry(user.getYhid());
		map=sjtssqService.insertSjtssq(sjtssqDto);
		return map;
	}

	

	/**
	 * 纸质报告申请列表 页面初始化
	 */
	@RequestMapping("/pageListPaperReportsApply")
	public ModelAndView  pageListPaperReportsApply(SjzzsqDto sjzzsqDto) {
		ModelAndView mav=new ModelAndView("wechat/paperReportsApply/paperReportsApply_list");
		mav.addObject("kdlxlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));
		mav.addObject("sjzzsqDto",sjzzsqDto);
		return mav;
	}

	/**
	 * 纸质报告申请列表 数据查询
	 */
	@RequestMapping("/pageGetListPaperReportsApply")
	@ResponseBody
	public Map<String, Object>  pageGetListPaperReportsApply(SjzzsqDto sjzzsqDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user=getLoginInfo();
		List<SjzzsqDto> sjzzsqDtoList=new ArrayList<>();
		List<Map<String,String>> jcdwList=sjxxservice.getJsjcdwByjsid(user.getDqjs());
        if(jcdwList != null && jcdwList.size() > 0) {
			if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				//判断是否显示个人清单
				if("1".equals(sjzzsqDto.getSingle_flag())) {
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
						sjzzsqDto.setUserids(userids);
					}
	
					//判断伙伴权限
					List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
					if(hbqxList!=null && hbqxList.size()>0) {
						List<String> hbmcList=sjhbxxservice.getHbmcByHbid(hbqxList);
						if(hbmcList!=null  && hbmcList.size()>0) {
							sjzzsqDto.setSjhbs(hbmcList);
						}
					}
				}
				List<String> strList= new ArrayList<>();
				for (int i = 0; i < jcdwList.size(); i++){
					if(jcdwList.get(i).get("jcdw")!=null) {
						strList.add(jcdwList.get(i).get("jcdw"));
					}
				}
				if(strList!=null && strList.size()>0) {
					sjzzsqDto.setJcdwxz(strList);
					sjzzsqDtoList=sjzzsqService.getPagedDtoListPaperReportsApply(sjzzsqDto);
				}
			}else {
				sjzzsqDtoList = sjzzsqService.getPagedDtoListPaperReportsApply(sjzzsqDto);
			}
        }else {
			sjzzsqDtoList = sjzzsqService.getPagedDtoListPaperReportsApply(sjzzsqDto);
		}
		try{
			shgcService.addShgcxxByYwid(sjzzsqDtoList, AuditTypeEnum.AUDIT_PAPERREPORTAPPLY.getCode(), "zt", "zzsqid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("total",sjzzsqDto.getTotalNumber());
		map.put("rows",sjzzsqDtoList);
		return map;
	}

	/**
	 * 纸质报告申请列表 查看
	 */
	@RequestMapping("/viewPaperReportsApply")
	public ModelAndView  viewPaperReportsApply(SjzzsqDto sjzzsqDto) {
		ModelAndView mav=new ModelAndView("wechat/paperReportsApply/paperReportsApply_view");
		SjzzsqDto dto = sjzzsqService.getDto(sjzzsqDto);
		mav.addObject("sjzzsqDto",dto);
		mav.addObject("flag","view");
		return mav;
	}

	/**
	 * 纸质报告申请列表 审核
	 */
	@RequestMapping("/auditPaperReportsApply")
	public ModelAndView  auditPaperReportsApply(SjzzsqDto sjzzsqDto) {
		ModelAndView mav=new ModelAndView("wechat/paperReportsApply/paperReportsApply_view");
		SjzzsqDto dto = sjzzsqService.getDto(sjzzsqDto);
		mav.addObject("sjzzsqDto",dto);
		mav.addObject("expressage",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
		mav.addObject("flag","audit");
		return mav;
	}

	/**
	 * 纸质报告申请列表 批量审核
	 */
	@RequestMapping("/batchauditPaperReportsApply")
	public ModelAndView  batchauditPaperReportsApply(SjzzsqDto sjzzsqDto) {
		ModelAndView mav=new ModelAndView("wechat/paperReportsApply/paperReportsApply_batchAudit");
		mav.addObject("sjzzsqDto",sjzzsqDto);
		mav.addObject("expressage",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
		return mav;
	}

	/**
	 * 纸质报告申请列表 批量审核保存
	 */
	@RequestMapping("/batchauditSavePaperReportsApply")
	@ResponseBody
	public Map<String, Object>  batchauditSavePaperReportsApply(SjkdxxDto sjkdxxDto) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess=false;
		List<String> ywids=sjkdxxDto.getYwids();
		if(ywids!=null&&ywids.size()>0&&StringUtil.isNotBlank(sjkdxxDto.getKdlx())){
			sjkdxxDto.setIds(ywids);
			sjkdxxDto.setYwlx(EntityTypeEnum.ENTITY_SJZZSQDTO.getCode());
			List<SjkdxxDto> sjkdxxDtos=sjkdxxService.getDtoList(sjkdxxDto);
			//判断sjkdxx表中是否存在此ywid,存在则更新，不存在则新增
			List<SjkdxxDto> list=new ArrayList<>();
			for(String s:ywids){
				if(sjkdxxDtos!=null&&sjkdxxDtos.size()>0){
					boolean isFind=false;
					SjkdxxDto sjkdxxDto_t=new SjkdxxDto();
					for(SjkdxxDto dto:sjkdxxDtos){
						if(s.equals(dto.getYwid())){
							isFind=true;
							sjkdxxDto_t=dto;
							break;
						}
					}
					if(isFind){
						sjkdxxDto_t.setKdlx(sjkdxxDto.getKdlx());
						sjkdxxDto_t.setJdlx(sjkdxxDto.getJdlx());
						list.add(sjkdxxDto_t);
					}else{
						sjkdxxDto_t.setSjkdid(StringUtil.generateUUID());
						sjkdxxDto_t.setKdlx(sjkdxxDto.getKdlx());
						sjkdxxDto_t.setJdlx(sjkdxxDto.getJdlx());
						sjkdxxDto_t.setYwlx(EntityTypeEnum.ENTITY_SJZZSQDTO.getCode());
						list.add(sjkdxxDto_t);
					}
				}else{
					SjkdxxDto sjkdxxDto_t=new SjkdxxDto();
					sjkdxxDto_t.setSjkdid(StringUtil.generateUUID());
					sjkdxxDto_t.setYwid(s);
					sjkdxxDto_t.setKdlx(sjkdxxDto.getKdlx());
					sjkdxxDto_t.setJdlx(sjkdxxDto.getJdlx());
					sjkdxxDto_t.setYwlx(EntityTypeEnum.ENTITY_SJZZSQDTO.getCode());
					list.add(sjkdxxDto_t);
				}
			}
			isSuccess=sjkdxxService.insertOrUpdateList(list);
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 钉钉 纸质报告申请 审核接口
	 */
	@RequestMapping("/minidataAuditInterface")
	@ResponseBody
	public Map<String, Object>  minidataAuditInterface(SjzzsqDto sjzzsqDto) {
		Map<String, Object> map = new HashMap<>();
		SjzzsqDto dto = sjzzsqService.getDto(sjzzsqDto);
		map.put("sjzzsqDto",dto);
		map.put("expressage",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
		return map;
	}

	/**
	 * 纸质报告申请列表 新增
	 */
	@RequestMapping("/addPaperapply")
	public ModelAndView  addPaperapply(SjzzsqDto sjzzsqDto) {
		ModelAndView mav=new ModelAndView("wechat/paperReportsApply/paperReportsApply_edit");
		mav.addObject("action","/inspection/addSavePaperapply");
		mav.addObject("sjzzsqDto",sjzzsqDto);
		return mav;
	}


	/**
	 * 病原体列表纸质申请
	 */
	@RequestMapping("/paperapplyAdd")
	public ModelAndView  paperapplyAdd(SjzzsqDto sjzzsqDto) {
		return addPaperapply(sjzzsqDto);
	}
	/**
	 * 病原体列表纸质申请保存
	 */
	@RequestMapping("/addSavePaperapply")
	@ResponseBody
	public Map<String, Object> paperapplySaveAdd(SjzzsqDto sjzzsqDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		sjzzsqDto.setLrry(user.getYhid());
		sjzzsqDto.setZt("10");

		if(sjzzsqDto.getIds()!=null&&sjzzsqDto.getIds().size()>0){
			String sjids = sjzzsqService.getSjidsByZt(sjzzsqDto);
			for(String s:sjzzsqDto.getIds()){
				Map<String, String>sjxxMap=sjxxservice.getAllSjxxOther(s);
				String cskz3s=sjxxMap.get("cskz3s");
				boolean flag=true;
				if(StringUtil.isNotBlank(cskz3s)){
					String[] cskz3Arr=cskz3s.split(",");
					for(String cskz3:cskz3Arr){
						if(StringUtil.isNotBlank(cskz3)){
							if(!(cskz3.indexOf("RFS")!=-1||cskz3.indexOf("TNGS")!=-1)){
								flag=false;
								break;
							}
						}
					}
				}
				if(flag){
					SjxxDto sjxxDto=new SjxxDto();
					sjxxDto.setSjid(s);
					SjxxDto dto = sjxxservice.getDto(sjxxDto);
					map.put("status", "fail");
					map.put("message", "标本编号为"+dto.getYbbh()+"纸质申请不允许申请F项目以及TNGS项目");
					return map;
				}
			}
			if(StringUtil.isNotBlank(sjids)){
				for(String s:sjzzsqDto.getIds()){
					if(sjids.indexOf(s)!=-1){
						SjxxDto sjxxDto=new SjxxDto();
						sjxxDto.setSjid(s);
						SjxxDto dto = sjxxservice.getDto(sjxxDto);
						map.put("status", "fail");
						map.put("message", "标本编号为"+dto.getYbbh()+"存在状态中的数据，无法再次新增！");
						return map;
					}
				}
			}

		}else{
			String cskz3s=sjzzsqDto.getCskz3s();
			boolean flag=true;
			if(StringUtil.isNotBlank(cskz3s)){
				String[] cskz3Arr=cskz3s.split(",");
				for(String cskz3:cskz3Arr){
					if(StringUtil.isNotBlank(cskz3)){
						if(!(cskz3.indexOf("RFS")!=-1||cskz3.indexOf("TNGS")!=-1)){
							flag=false;
							break;
						}
					}
				}
			}
			if(flag){
				map.put("status", "fail");
				map.put("message", "纸质申请不允许申请F项目以及TNGS项目");
				return map;
			}
			if(StringUtil.isNotBlank(sjzzsqDto.getSjid())){
				SjzzsqDto dto = sjzzsqService.getDto(sjzzsqDto);
				if(dto!=null){
					map.put("status", "fail");
					map.put("message", "当前标本编号存在状态中的数据，无法再次新增！");
					return map;
				}
			}
		}
		List<String> zzsqids=new ArrayList<>();
		boolean isSuccess = sjzzsqService.addSavePaperReportsApply(sjzzsqDto,user,zzsqids);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());

		if(zzsqids!=null&&zzsqids.size()>0){
			map.put("ywid",zzsqids);
		}else {
			map.put("ywid",sjzzsqDto.getZzsqid());
		}
		map.put("auditType", AuditTypeEnum.AUDIT_PAPERREPORTAPPLY.getCode());
		return map;
	}

	/**
	 * 纸质报告申请列表 修改
	 */
	@RequestMapping("/modPaperReportsApply")
	public ModelAndView  modPaperReportsApply(SjzzsqDto sjzzsqDto) {
		ModelAndView mav=new ModelAndView("wechat/paperReportsApply/paperReportsApply_edit");
		SjzzsqDto dto = sjzzsqService.getDto(sjzzsqDto);
		mav.addObject("sjzzsqDto",dto);
		mav.addObject("action","/inspection/modSavePaperReportsApply");
		return mav;
	}

	/**
	 * 纸质报告申请列表 修改
	 */
	@RequestMapping("/modSavePaperReportsApply")
	@ResponseBody
	public Map<String, Object> modSavePaperReportsApply(SjzzsqDto sjzzsqDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		sjzzsqDto.setXgry(user.getYhid());
		String cskz3s=sjzzsqDto.getCskz3s();
		boolean flag=true;
		if(StringUtil.isNotBlank(cskz3s)){
			String[] cskz3Arr=cskz3s.split(",");
			for(String cskz3:cskz3Arr){
				if(StringUtil.isNotBlank(cskz3)){
					if(!(cskz3.indexOf("RFS")!=-1||cskz3.indexOf("TNGS")!=-1)){
						flag=false;
						break;
					}
				}
			}
		}
		if(flag){
			map.put("status", "fail");
			map.put("message", "纸质申请不允许申请F项目以及TNGS项目");
			return map;
		}
		boolean isSuccess = sjzzsqService.modSavePaperReportsApply(sjzzsqDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid",sjzzsqDto.getZzsqid());
		map.put("auditType", AuditTypeEnum.AUDIT_PAPERREPORTAPPLY.getCode());
		return map;
	}

	/**
	 * 纸质报告申请列表 删除
	 */
	@RequestMapping("/delPaperReportsApply")
	@ResponseBody
	public Map<String, Object> delPaperReportsApply(SjzzsqDto sjzzsqDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		sjzzsqDto.setScry(user.getYhid());
		boolean isSuccess = sjzzsqService.delete(sjzzsqDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 纸质报告申请列表 锁定
	 */
	@RequestMapping("/lockPaperReportsApply")
	@ResponseBody
	public Map<String, Object> lockPaperReportsApply(SjzzsqDto sjzzsqDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		sjzzsqDto.setSdry(user.getYhid());
		boolean isSuccess = sjzzsqService.lock(sjzzsqDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?"锁定成功！" : "锁定失败！");
		return map;
	}

	/**
	 * 纸质报告申请列表 取消锁定
	 */
	@RequestMapping("/cancellockPaperReportsApply")
	@ResponseBody
	public Map<String, Object> cancelLockPaperReportsApply(SjzzsqDto sjzzsqDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		List<String> ids = sjzzsqDto.getIds();
		List<String> sqids = new ArrayList<>();
		if(ids!=null&&ids.size()>0){
			for(String s:ids){
				String[] str = s.split("_");
				if(str[2].equals(user.getYhid())){
					sqids.add(str[0]);
				}else{
					map.put("status", "fail");
					map.put("message","标本编号为 "+str[1]+" 的数据不是您本人锁定，所以你无权取消！");
					return map;
				}
			}
		}
		sjzzsqDto.setIds(sqids);
		boolean isSuccess = sjzzsqService.cancelLock(sjzzsqDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?"取消锁定成功！" : "取消锁定失败！");
		return map;
	}

	/**
	 * 	审核列表
	 * @return
	 */
	@RequestMapping("/pageListAuditPaperReportsApply")
	public ModelAndView pageListAuditPaperReportsApply() {
        return new  ModelAndView("wechat/paperReportsApply/paperReportsApply_auditList");
	}

	/**
	 * 	审核列表
	 * @param sjzzsqDto
	 * @return
	 */
	@RequestMapping("/pageGetListAuditPaperReportsApply")
	@ResponseBody
	public Map<String, Object> pageGetListAuditPaperReportsApply(SjzzsqDto sjzzsqDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(sjzzsqDto);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(sjzzsqDto.getDqshzt())) {
			DataPermission.add(sjzzsqDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "sjzzsq", "zzsqid",
					AuditTypeEnum.AUDIT_PAPERREPORTAPPLY);
		} else {
			DataPermission.add(sjzzsqDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "sjzzsq", "zzsqid",
					AuditTypeEnum.AUDIT_PAPERREPORTAPPLY);
		}
		DataPermission.addCurrentUser(sjzzsqDto, getLoginInfo(request));
		List<SjzzsqDto> listMap = sjzzsqService.getPagedAuditPaperReports(sjzzsqDto);
		map.put("total", sjzzsqDto.getTotalNumber());
		map.put("rows", listMap);
		return map;
	}

	/**
	 * 钉钉 纸质报告新增接口
	 */
	@RequestMapping("/minidataAddInterface")
	@ResponseBody
	public Map<String, Object> minidataAddInterface(SjxxDto sjxxDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		SjxxDto t_sjxxDto=sjxxservice.getDto(sjxxDto);
		map.put("sjxxDto",t_sjxxDto);
		User user = getLoginInfo(request);
		YhsyxgjlDto yhsyxgjlDto=new YhsyxgjlDto();
		yhsyxgjlDto.setYhid(user.getYhid());
		yhsyxgjlDto.setYwlx(UserHabitTypeEnum.USER_HABIT_SJZZSQ.getCode());
		List<YhsyxgjlDto> dtoList = yhsyxgjlService.getDtoList(yhsyxgjlDto);
		map.put("yhsyxgjlDtos",dtoList);
		return map;
	}

	/**
	 * 钉钉 纸质报告申请列表 查看接口
	 */
	@RequestMapping("/minidataViewInterface")
	@ResponseBody
	public Map<String, Object> viewInterface(SjzzsqDto sjzzsqDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		SjzzsqDto dto = sjzzsqService.getDto(sjzzsqDto);
		map.put("sjzzsqDto",dto);
		return map;
	}

	@RequestMapping("/minidataGetSjzzsqBySjid")
	@ResponseBody
	public Map<String, Object> getSjzzsqBySjid(SjzzsqDto sjzzsqDto) {
		Map<String, Object> map = new HashMap<>();
		List<SjzzsqDto> dtoList = sjzzsqService.getSjzzsqBySjid(sjzzsqDto);
		map.put("sjzzsqList",dtoList);
		return map;
	}

	/**
	 * 钉钉 纸质报告申请列表 修改接口
	 */
	@RequestMapping("/minidataModInterface")
	@ResponseBody
	public Map<String, Object> modInterface(SjzzsqDto sjzzsqDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		SjzzsqDto dto = sjzzsqService.getDto(sjzzsqDto);
		map.put("sjzzsqDto",dto);
		return map;
	}

	/**
	 * 纸质报告申请列表 取消订单
	 */
	@RequestMapping("/cancelorderPaperReportsApply")
	@ResponseBody
	public Map<String, Object> cancelorderPaperReportsApply(SjzzsqDto sjzzsqDto) {
		User user = getLoginInfo();
		sjzzsqDto.setScry(user.getYhid());
		return sjzzsqService.cancelOrder(sjzzsqDto,user);
	}

	/**
	 * 纸质报告申请列表 重新下单
	 */
	@RequestMapping("/createorderPaperReportsApply")
	@ResponseBody
	public ModelAndView createorderPaperReportsApply(SjzzsqDto sjzzsqDto) {
		ModelAndView mav=new ModelAndView("wechat/paperReportsApply/paperReportsApply_edit");
		//User user = getLoginInfo();
		SjzzsqDto dto = sjzzsqService.getDto(sjzzsqDto);
		mav.addObject("sjzzsqDto",dto);
		mav.addObject("flag","kdorder");
		mav.addObject("expressage",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
		mav.addObject("action","/inspection/createordersavePaperReportsApply");
		return mav;
	}

	@RequestMapping("/createordersavePaperReportsApply")
	@ResponseBody
	public Map<String, Object> createordersavePaperReportsApply(SjzzsqDto sjzzsqDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		SjzzsqDto dto = sjzzsqService.getDto(sjzzsqDto);
		dto.setKdlx(sjzzsqDto.getKdlx());
		if (StringUtil.isBlank(dto.getKdlx())){
			map.put("status", "fail");
			map.put("message","快递类型未选择");
			return map;
		}
		dto.setXgry(user.getYhid());
		map = sjzzsqService.createOrder(dto,user);
		boolean isSuccess = (boolean) map.get("status");
		map.put("status", isSuccess?"success":"fail");
		map.put("message", map.get("message"));
		return map;
	}

	/**
	 * 纸质报告申请列表 打包
	 */
	@RequestMapping("/packPaperReportsApply")
	public ModelAndView  packPaperReportsApply(SjzzsqDto sjzzsqDto) {
		ModelAndView mav=new ModelAndView("wechat/paperReportsApply/paperReportsApply_pack");
		User user=getLoginInfo();
        String bhs = sjzzsqService.getBhsByIds(sjzzsqDto);
        if(StringUtil.isBlank(bhs)){
            String s = sjzzsqService.generateBh(user);
            sjzzsqDto.setBh(s);
        }else{
            String[] split = bhs.split(",");
            sjzzsqDto.setBh(split[0]);
        }
        mav.addObject("formAction","/inspection/packSavePaperReportsApply");
		mav.addObject("sjzzsqDto",sjzzsqDto);
		return mav;
	}

    /**
     * 纸质报告申请列表 打包保存
     */
    @RequestMapping("/packSavePaperReportsApply")
    @ResponseBody
    public Map<String, Object> packSavePaperReportsApply(SjzzsqDto sjzzsqDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        if("cancel".equals(sjzzsqDto.getCz_flg())){
            boolean isSuccess = sjzzsqService.cancelPack(sjzzsqDto);
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?"取消打包成功！" : "取消打包失败！");
        }else{
            sjzzsqDto.setDbry(user.getYhid());
            boolean isSuccess = sjzzsqService.pack(sjzzsqDto);
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?"打包成功！" : "打包失败！");
        }
        return map;
    }

	/**
	 * 报告清单 数据查询
	 */
	@RequestMapping("/pagedataListReport")
	@ResponseBody
	public Map<String, Object>  getPageReportList(SjzzsqDto sjzzsqDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		List<SjzzsqDto> pageReportList = sjzzsqService.getPageReportList(sjzzsqDto);
		map.put("total",sjzzsqDto.getTotalNumber());
		map.put("rows",pageReportList);
		return map;
	}

	/**
	 * 质控流程 分析前标本质量合格率统计SQL
	 */
	@RequestMapping("/percentOfPass")
	@ResponseBody
	public Map<String, Object> percentOfPass(SjybztDto sjybztDto) {
		Map<String, Object> map = new HashMap<>();
        SjybztDto sjybzt = sjybztService.getPercentOfPass(sjybztDto);//送检样本状态要传入送检接受开始时间和接受结束时间
        map.put("sjybztDto",sjybzt);
		return map;
	}

	/**
	 * 质控流程 需要对不合格的标本进行统计，按照标本类型区分开，统计每个类型的不合格率，能根据接收日期进行选择
	 */
	@RequestMapping("/percentOfUnPass")
	@ResponseBody
	public Map<String, Object> percentOfUnPass(SjybztDto sjybztDto) {
		Map<String, Object> map = new HashMap<>();
		List<SjybztDto> sjybztDtoList = sjybztService.getPercentOfUnPass(sjybztDto);//送检样本状态要传入送检接受开始时间和接受结束时间
		map.put("sjybztDtoList",sjybztDtoList);
		return map;
	}

	/**
	 * 小程序送检伙伴限制送检清单
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/inspection/commonListInspection")
	@ResponseBody
	public Map<String,Object> commonListInspection(SjxxDto sjxxDto,HttpServletRequest request){
		User user=getLoginInfo();
		List<SjxxDto> sjxxlist;
		List<Map<String,String>> jcdwList=sjxxservice.getJsjcdwByjsid(user.getDqjs());
		if(jcdwList!=null&&jcdwList.size() > 0){
			if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				//判断是否显示个人清单
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
					sjxxDto.setUserids(userids);
				}

				//判断伙伴权限
				List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
				if(hbqxList!=null && hbqxList.size()>0) {
					List<String> hbmcList=sjhbxxservice.getHbmcByHbid(hbqxList);
					if(hbmcList!=null  && hbmcList.size()>0) {
						sjxxDto.setSjhbs(hbmcList);
					}
				}
				List<String> strList= new ArrayList<>();
				for (int i = 0; i < jcdwList.size(); i++){
					if(jcdwList.get(i).get("jcdw")!=null) {
						strList.add(jcdwList.get(i).get("jcdw"));
					}
				}
				if(strList!=null && strList.size()>0) {
					sjxxDto.setJcdwxz(strList);
					Map<String,Object> params = sjxxservice.pareMapFromDto(sjxxDto);
					String s_sjhbfl = request.getParameter("sjhbfls");
					if(StringUtil.isNotBlank(s_sjhbfl)) {
						String[] sjhbfls = s_sjhbfl.split(",");
						for (int i = 0; i < sjhbfls.length; i++){
							sjhbfls[i] = sjhbfls[i].replace("'","");
						}
						params.put("sjhbfls", sjhbfls);
					}
					sjxxlist=sjxxservice.getDtoListOptimize(params);
					sjxxDto.setTotalNumber((int)params.get("totalNumber"));
				}else {
					sjxxlist= new ArrayList<>();
				}
			}else {
				Map<String,Object> params = sjxxservice.pareMapFromDto(sjxxDto);
				String s_sjhbfl = request.getParameter("sjhbfls");
				if(StringUtil.isNotBlank(s_sjhbfl)) {
					String[] sjhbfls = s_sjhbfl.split(",");
					for (int i = 0; i < sjhbfls.length; i++){
						sjhbfls[i] = sjhbfls[i].replace("'","");
					}
					params.put("sjhbfls", sjhbfls);
				}
				String kylxs = request.getParameter("kylxs");
				if(StringUtil.isNotBlank(kylxs)) {
					String[] s_kylxs = kylxs.split(",");
					for (int i = 0; i < s_kylxs.length; i++){
						s_kylxs[i] = s_kylxs[i].replace("'","");
					}
					params.put("kylxs", s_kylxs);
				}
				sjxxlist=sjxxservice.getDtoListOptimize(params);
				sjxxDto.setTotalNumber((int)params.get("totalNumber"));
			}
		}else {
			Map<String,Object> params = sjxxservice.pareMapFromDto(sjxxDto);
			String s_sjhbfl = request.getParameter("sjhbfls");
			if(StringUtil.isNotBlank(s_sjhbfl)) {
				String[] sjhbfls = s_sjhbfl.split(",");
				for (int i = 0; i < sjhbfls.length; i++){
					sjhbfls[i] = sjhbfls[i].replace("'","");
				}
				params.put("sjhbfls", sjhbfls);
			}
			String kylxs = request.getParameter("kylxs");
			if(StringUtil.isNotBlank(kylxs)) {
				String[] s_kylxs = kylxs.split(",");
				for (int i = 0; i < s_kylxs.length; i++){
					s_kylxs[i] = s_kylxs[i].replace("'","");
				}
				params.put("kylxs", s_kylxs);
			}
			sjxxlist=sjxxservice.getDtoListOptimize(params);
			sjxxDto.setTotalNumber((int)params.get("totalNumber"));
		}

		Map<String, Object> map= new HashMap<>();
		//判断是否有实付金额字段权限，有就计算
		if (StringUtil.isNotBlank(sjxxDto.getSfzjezdqx())&&"1".equals(sjxxDto.getSfzjezdqx())){
			SjxxDto sjxxDto_z = sjxxservice.getSfzjeAndTkzje(sjxxDto);
			map.put("sfzje", sjxxDto_z.getSfzje());
		}
		if (StringUtil.isNotBlank(sjxxDto.getTkzjezdqx())&&"1".equals(sjxxDto.getTkzjezdqx())){
			SjxxDto sjxxDto_z = sjxxservice.getSfzjeAndTkzje(sjxxDto);
			map.put("tkzje", sjxxDto_z.getTkzje());
		}
		map.put("total", sjxxDto.getTotalNumber());
		if(!CollectionUtils.isEmpty(sjxxlist)){
			SjxxDto paramDto=new SjxxDto();
			List<String> ybbhs=new ArrayList<>();
			for(SjxxDto sjxxDto1:sjxxlist){
				if(StringUtil.isNotBlank(sjxxDto1.getYbbh())){
					ybbhs.add(sjxxDto1.getYbbh());
				}
			}
			paramDto.setYbbhs(ybbhs);
			List<SjxxDto>xjsjList=sjxxservice.getQdListByYbbh(paramDto);
			if(!CollectionUtils.isEmpty(xjsjList)){
				for(SjxxDto xjsjDto:xjsjList){
					for(SjxxDto sjxxDto1:sjxxlist){
						if(xjsjDto.getSjid().equals(sjxxDto1.getSjid())){
							sjxxDto1.setFxwcsj(xjsjDto.getFxwcsj());
						}
					}
				}
			}
		}
		map.put("rows", sjxxlist);
		//需要筛选钉钉字段的，请调用该方法
		screenClassColumns(request,map);
		return map;
	}

	/**
	 * 查看送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/viewmoreSjxx")
	public ModelAndView detailviewSjxx(SjxxDto sjxxDto) {
		ModelAndView mav=viewSjxx(sjxxDto);
		mav.addObject("flag", "1");
		return mav;
	}


//	/**
//	 * 保存数据至redis
//	 * @param
//	 * @return
//	 */
//	@RequestMapping(value="/inspection/pagedataSaveInfo")
//	@ResponseBody
//	public Map<String, Object> pagedataSaveInfo(String userId,String sjid,String flag) {
//		Map<String,Object> map = new HashMap<String,Object>();
//		String key =  GlobalParmEnum.SPECIMEN_INFO.getCode()+":"+userId;
//		Object hget = redisUtil.get(key);
//		if (hget!=null){
//			redisUtil.del(key);
//		}
//		redisUtil.set(key,flag+":"+sjid,43200L);
//		map.put("status", "success");
//		return map;
//	}

	/**
	 * 保存数据至redis
	 * @param
	 * @return
	 */
	@RequestMapping(value="/inspection/pagedataFlushedInfo")
	@ResponseBody
	public Map<String, Object> pagedataFlushedInfo(String key) {
		Map<String,Object> map = new HashMap<>();
		Object hget = redisUtil.get( key);
		if (hget!=null){
			String[] strings = key.split("_");
			String newKey = strings[0]+"_"+strings[1]+"_"+new Date().getTime();
			Map<String,Object> result = JSON.parseObject((String) hget);
			result.put("ssekey",null);
			redisUtil.del(key);
			redisUtil.set(newKey,JSON.toJSONString(result),5000);
			map.put("key",newKey);
			DBEncrypt p = new DBEncrypt();
			String dingtalkurl = p.dCode(jumpdingtalkurl);
			String dingtalkbtn = dingtalkurl+"page=/pages/index/index"+URLEncoder.encode("?toPageUrl=/pages/index/sjsyfjpage/sjsyfjpage&key="+newKey,StandardCharsets.UTF_8);
			map.put("dingtalkurl", dingtalkbtn);
			map.put("switchoverurl", applicationurl+"/ws/pathogen/pagedataUploadFile?key="+newKey);
		}
		return map;
	}


	/**
	 * 获取送检实验信息
	 *
	 * @param sjsyglDto
	 * @return
	 */
	@RequestMapping("/inspection/pagedataGetSjsyInfo")
	@ResponseBody
	public Map<String, Object> pagedataGetSjsyInfo(SjsyglDto sjsyglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		List<String> jcdws =new ArrayList<>();

		User user=getLoginInfo();

		String[] strings = sjsyglDto.getNbbm().split(" ");
		SjxxDto sjxxDto=new SjxxDto();
		if (strings!=null && strings.length>0){
			sjxxDto.setNbbm(strings[0]);
		}
		SjxxDto dto = sjxxservice.getDto(sjxxDto);
		List<SjsyglDto> list = new ArrayList<>();
		List<Map<String,Object>> tqmxList = new ArrayList<>();
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());

		jcdws.add(sjsyglDto.getJcdw());

		if("1".equals(user.getDqjsdwxdbj())) {
			Map<String, String> paramMap = new HashMap<String,String>();
			paramMap.put("jsid", user.getDqjs());
			List<Map<String, String>> dwlist = commonService.getJsjcdwDtoList(paramMap);
			if(dwlist!=null&&dwlist.size()>0){
				for(Map<String, String> jcsjDto:dwlist){
					if("0z".equals(jcsjDto.get("csdm"))){
						jcdws.add(jcsjDto.get("jcdw"));
					}else if("0W".equals(jcsjDto.get("csdm"))){
						jcdws.add(jcsjDto.get("jcdw"));
					}
				}
			}
		}else {
			if(jcdwList!=null&&jcdwList.size()>0){
				for(JcsjDto jcsjDto:jcdwList){
					if("0z".equals(jcsjDto.getCsdm())){
						jcdws.add(jcsjDto.getCsid());
					}else if("0W".equals(jcsjDto.getCsdm())){
						jcdws.add(jcsjDto.getCsid());
					}
				}
			}
		}
		
		if(jcdws!=null&&jcdws.size()>0){
			sjsyglDto.setJcdws(jcdws.toArray(new String[jcdws.size()]));
		}

		if(dto!=null){
			sjsyglDto.setSjid(dto.getSjid());
			list =sjsyglService.getWkInfoList(sjsyglDto);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sjid",dto.getSjid());
			params.put("jcdws",sjsyglDto.getJcdws());
			if(StringUtil.isNotBlank(sjsyglDto.getQtsyrq()))
				params.put("rq",sjsyglDto.getQtsyrq());
			else {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String nowDate=sdf.format(new Date());
				params.put("rq",nowDate);
			}
			tqmxList=sjsyglService.getExtractInfo(params);
		}
		//当前台同时传递了后缀信息后，则进行匹配，没有匹配到的时候，无视检测单位进行查询，如果还是没有找到，则打算在上级列表里查找相应的项目代码
		String hz = request.getParameter("hz");
		if(StringUtil.isNotBlank(hz)){
			boolean isFind=false;
			for (SjsyglDto t_sjsyglDto:list){
				if(hz.equals(t_sjsyglDto.getKzcs1()))
				{
					isFind=true;
					break;
				}
			}
			if(!isFind){
				String[] t_jcdws =new String[0];
				sjsyglDto.setJcdws(t_jcdws);
				list =sjsyglService.getWkInfoList(sjsyglDto);
			}
		}
		map.put("list", list);
		map.put("tqmxList", tqmxList);
		return map;
	}

	/**
	 * 打印
	 * @return
	 */
	@RequestMapping(value="/inspection/printSjxx")
	public ModelAndView printSjxx(SjxxDto sjxxDto){
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_print");
		SjxxDto dto = sjxxservice.getDto(sjxxDto);
		List<SjsyglDto> list = new ArrayList<>();
		if(dto!=null){
			SjsyglDto sjsyglDto = new SjsyglDto();
			sjsyglDto.setSjid(dto.getSjid());
			sjsyglDto.setSfjs("1");
			list=sjsyglService.getDtoList(sjsyglDto);
		}
		mav.addObject("sjxxDto",dto);
		mav.addObject("list",list);
		return mav;
	}
	/**
	 * 清单发送给商务部
	 * @param
	 * @return
	 */
	@RequestMapping("/inspection/pagedataGetDdUncollectedViewByHb")
	@ResponseBody
	public Map<String,Object>pagedataGetDdUncollectedViewByHb(HttpServletRequest request,SjxxDto sjxxDto) {
		//ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_ddUncollectedView");
		Map<String,Object>map=new HashMap<>();
		User user=getLoginInfo();
		List<SjxxDto> sjxxlist;
		List<Map<String,String>> jcdwList=sjxxservice.getJsjcdwByjsid(user.getDqjs());
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
			sjxxDto.setUserids(userids);
		}

		//判断伙伴权限
		List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
		if(hbqxList!=null && hbqxList.size()>0) {
			List<String> hbmcList=sjhbxxservice.getHbmcByHbid(hbqxList);
			if(hbmcList!=null  && hbmcList.size()>0) {
				sjxxDto.setSjhbs(hbmcList);
			}
		}
		List<String> strList= new ArrayList<>();
		if(jcdwList!=null&&jcdwList.size()>0&&"1".equals(jcdwList.get(0).get("dwxdbj"))) {
			for (int i = 0; i < jcdwList.size(); i++) {
				if (jcdwList.get(i).get("jcdw") != null) {
					strList.add(jcdwList.get(i).get("jcdw"));
				}
			}
		}
		if(strList!=null && strList.size()>0) {
			sjxxDto.setJcdwxz(strList);
		}



		sjxxlist = sjxxservice.getPagedDtoBySf(sjxxDto);
        map.put("total", sjxxDto.getTotalNumber());
        map.put("rows", sjxxlist);
		screenClassColumns(request,map);
		return map;
	}


	/**
	 * 打印保存
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/inspection/printSaveSjxx")
	@ResponseBody
	public Map<String, Object> printSaveSjxx(SjxxDto sjxxDto,HttpServletRequest request,GrszDto grszDto){
		Map<String, Object> map = new HashMap<>();
		//User user = getLoginInfo();
		String nbzbm = "";
		if (null != sjxxDto.getJclxs() && sjxxDto.getJclxs().size() >0){
			for (String jclx : sjxxDto.getJclxs()) {
				nbzbm += " "+jclx.split("/")[0];
			}
			sjxxDto.setJclxs(null);
		}
		//boolean isSuccess = false;
		try {
			map.put("sz_flg", grszDto.getSzz());
			String sign = commonService.getSign(sjxxDto.getNbbm());
			int fontflg = 1;
			if (sjxxDto.getNbbm().length() > 10) {
				fontflg = 2;
			}
			//sign = URLEncoder.encode(sign, "UTF-8");
			if ("1".equals(map.get("sz_flg"))){
				String url = ":8082/Print?code=" + (sjxxDto.getNbbm().startsWith("MD")?sjxxDto.getNbbm().substring(2):sjxxDto.getNbbm()) + "&sign=" + sign + "&num="+(StringUtil.isNotBlank(grszDto.getDysl())?grszDto.getDysl():"6")+"&fontflg=" + fontflg
						+ "&word=" + (StringUtil.isNotBlank(nbzbm)?nbzbm.substring(1):"")+ "&qrcode=" + sjxxDto.getNbbm() + "&project=" + sjxxDto.getYbbh() + "&rownum=3";
				map.put("print", url);
				log.error("打印地址：" + url);
			}else{
				String url = ":8081/Print?code=" + (sjxxDto.getNbbm().startsWith("MD")?sjxxDto.getNbbm().substring(2):sjxxDto.getNbbm()) + "&sign=" + sign + "&num="+(StringUtil.isNotBlank(grszDto.getDysl())?grszDto.getDysl():"6")+"&fontflg=" + fontflg
						+ "&word=" + (StringUtil.isNotBlank(nbzbm)?nbzbm.substring(1):"")+ "&qrcode=" + sjxxDto.getNbbm() + "&project=" +sjxxDto.getYbbh() + "&rownum=3";
				map.put("print", url);
				log.error("打印地址：" + url);
			}
			map.put("status","success");
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", e.toString());
		}
		return map;
	}

	/**
	 * 检测项目列表
	 * @param sjjcxmDto
	 * @return
	 */
	@RequestMapping(value="/inspection/pagedataInspectionItems")
	@ResponseBody
	public Map<String, Object> pagedataInspectionItems(SjjcxmDto sjjcxmDto){
		Map<String, Object> map = new HashMap<>();
		List<SjjcxmDto> pagedDtoList = sjjcxmservice.getPagedDtoList(sjjcxmDto);
		map.put("total", sjjcxmDto.getTotalNumber());
		map.put("rows", pagedDtoList);
		return map;
	}
	/**
	 * 根据传递信息获取实验管理信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/pagedataGetDetectionInfo")
	@ResponseBody
	public Map<String, Object> pagedataGetDetectionInfo(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		List<SjsyglDto> sjsyglDtos = sjsyglService.getDealDetectionInfo(sjxxDto, "DETECT_SJ");
		map.put("jcdwlist", redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位List
		map.put("sjsyglDtos", sjsyglDtos);
		return map;
	}


	/**
	 * 数据获取
	 * @return
	 */
	@RequestMapping("/inspection/dataGettingInit")
	public ModelAndView dataGettingInit(){
		ModelAndView mav = addSjxx();
		mav.addObject("isAidikangPage",true);
		return mav;
	}
	/**
	 * 数据获取
	 * @return
	 */
	@RequestMapping("/inspection/pagedataGettingInfo")
	@ResponseBody
	public Map<String, Object> dataGettingInfo(String hospSampleID,String addFlag){
		Map<String, Object> map = new HashMap<>();
		MatchingUtil matchingUtil = new MatchingUtil();
		try {
			if(StringUtil.isBlank(addFlag)) {
				Map<String, Object> yuhuangdingMap = new HashMap<>();
				yuhuangdingMap.put("hospSampleID",hospSampleID);
				yuhuangdingMap.put("method","selectById");
				yuhuangdingMap.put("yuhuangding_wsdlurl",yuhuangding_wsdlurl);
				Map<String, Object> infoMap = matchingUtil.yuhuangdingInfo(yuhuangdingMap);
				if (infoMap.containsKey("status") && "success".equals(infoMap.get("status"))){
					SjxxDto sjxxDto = matchingUtil.dealYuhuangdingInfo(infoMap,
							redisUtil,
							sjxxservice,
							sjkzxxService,
							yyxxService,
							sjdwxxService,
							xxdyService
					);
					map.put("sjxxDto",sjxxDto);
					map.put("status","success");
				} else {
					map.put("status","fail");
					map.put("message","获取样本信息失败!"+(infoMap.containsKey("message") && StringUtil.isNotBlank((String)infoMap.get("message"))?infoMap.get("message"):""));
				}
			}else {
				Map<String,Object> adkMap = matchingUtil.queryADKSample(hospSampleID,ADK_LOGID,ADK_PASSWORD,ADK_REQUESTURL);
				if(adkMap!=null){
					if("error".equals(adkMap.get("code"))){
						map.put("status","fail");
						map.put("message","艾迪康系统反馈信息：" + adkMap.get("message"));
					}else {
						SjxxDto sjxxDto = matchingUtil.adkAssembleData(adkMap,redisUtil,sjxxservice,yyxxService,xxdyService);
						sjxxDto.setYbbh(hospSampleID);
						sjxxDto.setWbbm(hospSampleID);
						map.put("sjxxDto",sjxxDto);
						map.put("status","success");
					}
				}else{
					map.put("status","fail");
					map.put("message","配置信息有误!");
				}
			}
		} catch (Exception e) {
			map.put("status","fail");
			map.put("message","获取样本信息失败!"+e.getMessage());
		}
		return map;
	}

	/**
	 * 设置个人设置信息
	 * @param request
	 * @param grszDto
	 * @param user
	 * @param map
	 */
	private void setGrszInfo(HttpServletRequest request, GrszDto grszDto, User user, Map<String, Object> map) {
		GrszDto grszDto_t = new GrszDto();
		grszDto_t.setYhid(user.getYhid());
		grszDto_t.setSzlbs(new String[]{PersonalSettingEnum.PRINT_ADDRESS.getCode(),
				PersonalSettingEnum.WHETHER_TO_PRINT.getCode()});
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto_t);
		if ("1".equals(grszDto.getGrsz_flg())) {
			// 添加个人设置
			grszDto.setSzlb(PersonalSettingEnum.PRINT_ADDRESS.getCode());
			GrszDto grsz_pa = grszDtoMap.get(PersonalSettingEnum.PRINT_ADDRESS.getCode());
			if (grsz_pa == null) {
				grszDto.setSzid(StringUtil.generateUUID());
				grszService.insertDto(grszDto);
			} else {
				if (grsz_pa.getSzz()==null || !grsz_pa.getSzz().equals(grszDto.getSzz())) {
					grszDto.setSzz(grsz_pa.getSzz());
					grszService.updateByYhidAndSzlb(grszDto);
				}
			}
		}
		map.put("sz_flg", grszDto.getSzz());
		if ("1".equals(request.getParameter("grsz_flg_two"))) {
			grszDto.setSzlb(PersonalSettingEnum.WHETHER_TO_PRINT.getCode());
			//收样确认 是否打印
			GrszDto grszDto_print = grszDtoMap.get(PersonalSettingEnum.WHETHER_TO_PRINT.getCode());
			if (grszDto_print == null) {
				grszDto.setSzid(StringUtil.generateUUID());
				grszDto.setSzz(request.getParameter("sfdy"));
				grszService.insertDto(grszDto);
			} else {
				if (grszDto_print.getSzz()==null || !grszDto_print.getSzz().equals(request.getParameter("sfdy"))) {
					grszDto.setSzz(request.getParameter("sfdy"));
					grszService.updateByYhidAndSzlb(grszDto);
				}
			}
		}
	}

	/**
	 *  判断是否是先声的样本，并通知先声
	 * @param dtoById
	 * @return
	 */
	private boolean isXianshengSample(SjxxDto dtoById) {
		if(StringUtil.isNotBlank(dtoById.getWbbm())){
			if ("hospitalXiansheng".equals(dtoById.getLrry()) && StringUtil.isNotBlank(dtoById.getWbbm())){
				try {
					Map<String,Object> rabbitInfoMap = new HashMap<>();
					rabbitInfoMap.put("ybbh", dtoById.getYbbh());
					rabbitInfoMap.put("wbbm", dtoById.getWbbm());
					rabbitInfoMap.put("sjid", dtoById.getSjid());
					rabbitInfoMap.put("method","confirmById");
					rabbitInfoMap.put("lrry","hospitalXiansheng");
					amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MATCHING_SEND_REPORT.getCode(),JSONObject.toJSONString(rabbitInfoMap));
					log.error("接收确认开始通知至先声LIS系统。");
				} catch (Exception e) {
					log.error("接收确认开始通知至先声LIS系统。-----错误："+e.getMessage());
					return false;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * @Description: 艾迪康新增
	 * @param
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2025/6/23 13:33
	 */
	@RequestMapping(value="/inspection/adkaddSjxx")
	public ModelAndView adkaddSjxx(){
		ModelAndView mav = addSjxx();
		mav.addObject("isAidikangPage",true);
		mav.addObject("addFlag",true);
		return mav;
	}
}
