package com.matridx.igams.hrm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.hrm.dao.entities.*;
import com.matridx.igams.hrm.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.collections.MapUtils;
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
import org.thymeleaf.util.ArrayUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/train")
public class TrainController extends BaseController{
	@Autowired
	IGzglService gzglService;
	@Autowired
	IPxtkglbService pxtkglbService;
	@Autowired
	ITkglService tkglService;
	@Autowired
	IPxglService pxglService;
	@Autowired
	IKsglService ksglService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ICommonService commonService;
	@Autowired
	IGrksmxService grksmxService;
	@Autowired
	IKsmxService ksmxService;
	@Autowired
	IGrksglService grksglService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ICommonDao commonDao;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IDsrwszService dsrwszService;
	@Autowired
	IPxqxszService pxqxszService;
	@Autowired
	IHbszService hbszService;
	@Autowired
	IHbszmxService hbszmxService;
	@Autowired
	IPxglxxService pxglxxService;
	@Autowired(required=false)
	private AmqpTemplate amqpTempl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.rabbit.flg:}")
	private String rabbitFlg;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	private final Logger log = LoggerFactory.getLogger(TrainController.class);
	
	@Override
	public String getPrefix() {
		return urlPrefix;
	}


	/**
	 * 已学习/未学习列表
	 */
	@RequestMapping("/task/minidataGetTrainList")
	@ResponseBody
	public Map<String,Object> getTrainList(GzglDto gzglDto){
		gzglDto.setSortName("yh.zsxm");
		gzglDto.setSortLastName("gzgl.ywmc");
		List<GzglDto> t_List = gzglService.getPagedTrainList(gzglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("list", t_List);
		result.put("total",t_List.size());
		return result;
	}

	/**
	 * 删除已学习/未学习列表中信息
	 */
	@RequestMapping("/task/delDetail")
	@ResponseBody
	public Map<String,Object> delDetail(GzglDto gzglDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		gzglDto.setScry(user.getYhid());
		boolean isSuccess = gzglService.delete(gzglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("status", isSuccess?"success":"fail");
		result.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return result;
	}
	/**
	 * 删除已学习/未学习列表中信息钉钉
	 */
	@RequestMapping("/task/minidataDelDetail")
	@ResponseBody
	public Map<String,Object> minidataDelDetail(GzglDto delDetail,HttpServletRequest request){
	return delDetail(delDetail,request);
	}

	/**
	 * 获取培训任务列表
	 */
	@RequestMapping("/task/minidataDingTrainTask")
	@ResponseBody
	public Map<String,Object> DingTrainTask(GzglDto gzglDto){
		if (StringUtil.isNotBlank(gzglDto.getGqsjbj())){
			gzglDto.setGqsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
		}
		List<GzglDto> t_List = gzglService.getDtoTrainList(gzglDto);
		if(!CollectionUtils.isEmpty(t_List)){
			for(GzglDto dto:t_List){
				if(StringUtil.isNotBlank(dto.getFjid())){
					dto.setTpwjlj("/ws/file/fileShow?acess_token="+gzglDto.getAccess_token()+"&ywlx="+BusTypeEnum.IMP_TRAIN_PICTURE.getCode()+"&fjid="+dto.getFjid());
				}
			}
		}
		Map<String,Object> result = new HashMap<>();
		result.put("list", t_List);
		return result;
	}


	/**
	 * 清单详情
	 */
	@RequestMapping(value="/task/minidataViewDetailedList")
	@ResponseBody
	public Map<String,Object> viewDetailedList(PxglDto pxglDto) {
		Map<String,Object> map= new HashMap<>();
		PxglDto pxglDto_t = pxglService.viewDetailedList(pxglDto);
		List<JcsjDto> dtos = redisUtil.hmgetDto("matridx_jcsj:TRAINCATEGORY");
		for(JcsjDto dto:dtos){
			if(dto.getCsid().equals(pxglDto_t.getPxlb())){
				pxglDto_t.setPxlb(dto.getCsmc());
			}
		}
		List<JcsjDto> jcsjs = redisUtil.hmgetDto("matridx_jcsj:SUBCLASSTRAINCATEGORY");
		for(JcsjDto dto:jcsjs){
			if(dto.getCsid().equals(pxglDto_t.getPxzlb())){
				pxglDto_t.setPxzlb(dto.getCsmc());
			}
		}
		FjcfbDto fjDto = new FjcfbDto();
		fjDto.setYwlx(BusTypeEnum.IMP_TRAIN.getCode());
		fjDto.setYwid(pxglDto_t.getPxid());
		List<FjcfbDto> fjlist = fjcfbService.getDtoList(fjDto);
		if(!CollectionUtils.isEmpty(fjlist)){
			for(FjcfbDto dto_t:fjlist){
				dto_t.setWjlj("/ws/file/fileShow?acess_token="+pxglDto.getAccess_token()+"&ywlx="+BusTypeEnum.IMP_TRAIN.getCode()+"&fjid="+dto_t.getFjid());
			}
		}
		map.put("pxglDto",pxglDto_t);
		map.put("fjs",fjlist);
		return map;
	}

	/**
	 * 跳转培训详情界面
	 */
	@RequestMapping(value="/task/viewTrainTask")
	@ResponseBody
	public Map<String,Object> viewTrainTask(PxglDto pxglDto) {
		Map<String,Object> map= new HashMap<>();
		PxglDto pxglDto_t = pxglService.viewTrainTask(pxglDto);
		FjcfbDto fjDto = new FjcfbDto();
		fjDto.setYwlx(BusTypeEnum.IMP_TRAIN.getCode());
		fjDto.setYwid(pxglDto_t.getPxid());
		List<FjcfbDto> fjlist = fjcfbService.getDtoList(fjDto);
		String pxjd = pxglDto_t.getPxjd();
		String[] split =null;
		if(StringUtil.isNotBlank(pxjd)){
			split = pxjd.split(",");
		}
		if(!CollectionUtils.isEmpty(fjlist)){
			for(FjcfbDto dto_t:fjlist){
				dto_t.setWjlj("/ws/file/fileShow?acess_token="+pxglDto.getAccess_token()+"&ywlx="+BusTypeEnum.IMP_TRAIN.getCode()+"&fjid="+dto_t.getFjid());
				if(!ArrayUtils.isEmpty(split)){
					for(String s:split){
						String fjid = s.split("_")[0];
						if(fjid.equals(dto_t.getFjid())){
							dto_t.setPxjd(s);
						}
					}
				}
			}
		}
		fjDto.setYwlx(BusTypeEnum.IMP_TRAIN_PICTURE.getCode());
		fjDto.setYwid(pxglDto_t.getPxid());
		FjcfbDto fj = fjcfbService.getDto(fjDto);
		if(fj!=null) {
			pxglDto_t.setTpwjlj("/ws/file/fileShow?acess_token=" + pxglDto.getAccess_token() + "&ywlx=" + BusTypeEnum.IMP_TRAIN_PICTURE.getCode() + "&fjid=" + fj.getFjid());
		}
		map.put("pxglDto",pxglDto_t);
		map.put("fjs",fjlist);
		return map;
	}
	/**
	 * 钉钉跳转培训详情界面
	 */
	@RequestMapping(value="/task/minidataViewTrainTask")
	@ResponseBody
	public Map<String,Object> minidataViewTrainTask(PxglDto pxglDto) {
		Map<String,Object> map= new HashMap<>();
		PxglDto pxglDto_t = pxglService.viewTrainTask(pxglDto);
		FjcfbDto fjDto = new FjcfbDto();
		fjDto.setYwlx(BusTypeEnum.IMP_TRAIN.getCode());
		fjDto.setYwid(pxglDto_t.getPxid());
		List<FjcfbDto> fjlist = fjcfbService.getDtoList(fjDto);
		String pxjd = pxglDto_t.getPxjd();
		String[] split =null;
		if(StringUtil.isNotBlank(pxjd)){
			split = pxjd.split(",");
		}
		if(!CollectionUtils.isEmpty(fjlist)){
			for(FjcfbDto dto_t:fjlist){
				dto_t.setWjlj("/ws/file/fileShow?acess_token="+pxglDto.getAccess_token()+"&ywlx="+BusTypeEnum.IMP_TRAIN.getCode()+"&fjid="+dto_t.getFjid());
				if(!ArrayUtils.isEmpty(split)){
					for(String s:split){
						String fjid = s.split("_")[0];
						if(fjid.equals(dto_t.getFjid())){
							dto_t.setPxjd(s);
						}
					}
				}
			}
		}
		fjDto.setYwlx(BusTypeEnum.IMP_TRAIN_PICTURE.getCode());
		fjDto.setYwid(pxglDto_t.getPxid());
		FjcfbDto fj = fjcfbService.getDto(fjDto);
		if(fj!=null) {
			pxglDto_t.setTpwjlj("/ws/file/fileShow?acess_token=" + pxglDto.getAccess_token() + "&ywlx=" + BusTypeEnum.IMP_TRAIN_PICTURE.getCode() + "&fjid=" + fj.getFjid());
		}
		map.put("pxglDto",pxglDto_t);
		map.put("fjs",fjlist);
		return map;
	}
	/**
	 * 考试记录清单
	 */
	@RequestMapping(value="/task/minidataViewPersonalTests")
	@ResponseBody
	public Map<String,Object> viewPersonalTests(GrksglDto grksglDto) {
		Map<String,Object> map= new HashMap<>();
		List<GrksglDto> grksglDtos=grksglService.getPersonalTests(grksglDto);
		map.put("list",grksglDtos);
		return map;
	}

	/**
	 * 保存学习情况
	 */
	@RequestMapping(value="/task/minidataSaveTrainSituation")
	@ResponseBody
	public void minidataSaveTrainSituation(GzglDto gzglDto) {
        log.error("工作ID：  "+gzglDto.getGzid()+"测试标记：  "+gzglDto.getSfcs()+"培训进度：  "+gzglDto.getPxjd()+"视频完成标记：  "+gzglDto.getSpwcbj());
		if("1".equals(gzglDto.getSfcs())){
			gzglService.update(gzglDto);
		}else{
			if("1".equals(gzglDto.getSpwcbj())){
				gzglDto.setZt("80");
				gzglDto.setTgbj("1");
				gzglService.update(gzglDto);
			}else{
				gzglService.update(gzglDto);
			}
		}
	}
	/**
	 * 获取考试说明
	 */
	@RequestMapping("/task/minidataGetTestDescription")
	@ResponseBody
	public Map<String,Object> getTestDescription(PxglDto pxglDto,HttpServletRequest request) {
		Map<String,Object> result = new HashMap<>();
		User user = getLoginInfo(request);
		PxglDto pxglDto_t;
		Object px = redisUtil.get(RedisCommonKeyEnum.TRAIN_TRAINPXINFO.getKey() + pxglDto.getPxid());
		log.error("---------------------minidataGetTestDescription-TRAINPXINFO_：" + px);
		if (px != null) {
			pxglDto_t = JSON.parseObject(String.valueOf(px), PxglDto.class);
		} else {
			pxglDto_t = pxglService.viewTrainTask(pxglDto);
		}
		Object userPxInfo = redisUtil.hget(RedisCommonKeyEnum.TRAIN_USERPXINFO.getKey() + pxglDto_t.getPxid(),user.getYhid());
		log.error("---------------------minidataGetTestDescription-USERPXINFO_：" + userPxInfo);
		if (userPxInfo!=null) {
			GrksglDto grksglDto_info = JSON.parseObject(String.valueOf(userPxInfo), GrksglDto.class);
			pxglDto_t.setKscs(grksglDto_info.getKscs());
		}else {
			if (StringUtil.isNotBlank(pxglDto_t.getKscs())) {
				int kscs = Integer.parseInt(pxglDto_t.getKscs());
				GrksglDto grksglDto = new GrksglDto();
				grksglDto.setGzid(pxglDto.getGzid());
				List<GrksglDto> grksglDtos = grksglService.getPersonalTests(grksglDto);
				int testCount = grksglDtos.size();
				pxglDto_t.setKscs(String.valueOf(kscs - testCount));
			}
		}
		result.put("pxglDto",pxglDto_t);
		return result;
	}

	/**
	 * 保存考试开始时间+获取题目明细并保存
	 */
	@RequestMapping("/task/minidataGetProblemDetails")
	@ResponseBody
	public Map<String,Object> getProblemDetails(GrksglDto grksglDto, HttpServletRequest request) {
		Map<String,Object> result = new HashMap<>();
		List<KsglDto> list_t =new ArrayList<>();
		List<KsglDto> duoxlist =new ArrayList<>();
		List<KsglDto> jdlist =new ArrayList<>();
		List<KsglDto> pdlist =new ArrayList<>();
		List<KsglDto> tklist =new ArrayList<>();
		log.error("getProblemDetails start.");
		// List<GrksglDto> timeoutList = grksglService.getTimeoutList(grksglDto);
		// if(timeoutList!=null&&timeoutList.size()>0){
		// 	List<String> ids=new ArrayList<>();
		// 	for(GrksglDto dto:timeoutList){
		// 		if(StringUtil.isNotBlank(dto.getGrksid())){
		// 			ids.add(dto.getGrksid());
		// 		}
		// 	}
		// 	if (!CollectionUtils.isEmpty(ids)){
		// 		grksglDto.setIds(ids);
		// 		GrksmxDto grksmxDto=new GrksmxDto();
		// 		grksmxDto.setIds(ids);
		// 		grksglService.delTimeOut(grksglDto);
		// 		grksmxService.delTimeOut(grksmxDto);
		// 	}
		// }
		User user = getLoginInfo(request);
		grksglDto.setRyid(user.getYhid());
		grksglDto.setGrksid(StringUtil.generateUUID());
		grksglDto.setKskssj(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		grksglDto.setLrry(grksglDto.getRyid());
		redisUtil.del(RedisCommonKeyEnum.TRAIN_GRKSGL.getKey()+grksglDto.getGzid());
		redisUtil.del(RedisCommonKeyEnum.TRAIN_GRKSMX.getKey()+grksglDto.getGzid());
		redisUtil.set(RedisCommonKeyEnum.TRAIN_GRKSGL.getKey()+grksglDto.getGzid(),JSON.toJSONString(grksglDto),RedisCommonKeyEnum.TRAIN_GRKSGL.getExpire());
		log.error("---------------------插入Redis的Grksgl数据为："+JSON.toJSONString(grksglDto));
		Map<Object, Object> hmget = redisUtil.hmget(RedisCommonKeyEnum.TRAIN_PXTKTMLX.getKey() + grksglDto.getPxid());

		log.error("getProblemDetails getPx_" + grksglDto.getPxid());
		if(MapUtils.isEmpty(hmget)){
			synchronized (GrksglDto.class) {
				log.error("getProblemDetails hmget init start.");
				Map<Object, Object> pxget = redisUtil.hmget(RedisCommonKeyEnum.TRAIN_PXTKTMLX.getKey() + grksglDto.getPxid());
				if(MapUtils.isEmpty(pxget)){
					PxtkglbDto pxtkglbDto=new PxtkglbDto();
					pxtkglbDto.setPxid(grksglDto.getPxid());
					List<PxtkglbDto> list = pxtkglbService.getListByPxid(pxtkglbDto);
					List<KsmxDto> ksmxDtos=new ArrayList<>();
					KsglDto ksglDto=new KsglDto();
					int danx=0;
					int duox=0;
					int jd=0;
					int tk=0;
					int pd=0;
					String preTkid = "";
					String preTmlx="";
					String preKsid = "";
					log.error("getProblemDetails init size:" + list.size());
					for (PxtkglbDto t_pxtkglbDto : list) {

						if (!preTkid.equals(t_pxtkglbDto.getTkid()) || !preTmlx.equals(t_pxtkglbDto.getTmlx())) {
							redisUtil.hset(RedisCommonKeyEnum.TRAIN_PXTKTMLX.getKey() + t_pxtkglbDto.getPxid(), t_pxtkglbDto.getTkid() + ":" + t_pxtkglbDto.getTmlx(), t_pxtkglbDto.getSl(),RedisCommonKeyEnum.TRAIN_PXTKTMLX.getExpire());
							preTkid = t_pxtkglbDto.getTkid();
							preTmlx = t_pxtkglbDto.getTmlx();
							//切换题库后，单选，多选的序号重新开始
							danx = 0;
							duox = 0;
							jd = 0;
							tk = 0;
							pd = 0;
						}

						if (!preKsid.equals(t_pxtkglbDto.getKsid())) {

							if (StringUtil.isNotBlank(preKsid)) {
								redisUtil.set(RedisCommonKeyEnum.TRAIN_KSMX.getKey() + ksglDto.getKsid(), JSON.toJSONString(ksmxDtos), RedisCommonKeyEnum.TRAIN_KSMX.getExpire());
							}
							//切换考试后明细和考试的内容需要重新初始化
							ksglDto = new KsglDto();
							ksmxDtos = new ArrayList<>();

							ksglDto.setTkid(t_pxtkglbDto.getTkid());
							ksglDto.setKsid(t_pxtkglbDto.getKsid());
							ksglDto.setTmlx(t_pxtkglbDto.getTmlxmc());
							ksglDto.setDa(t_pxtkglbDto.getDa());
							ksglDto.setTmlxmc(t_pxtkglbDto.getTmlx());
							ksglDto.setTmnr(t_pxtkglbDto.getTmnr());
							if (StringUtil.isNotBlank(t_pxtkglbDto.getFz())) {
								ksglDto.setFs(t_pxtkglbDto.getFz());
							} else {
								ksglDto.setFs(t_pxtkglbDto.getFs());
							}
							if ("SELECT".equals(t_pxtkglbDto.getTmlx())) {
								redisUtil.hset(RedisCommonKeyEnum.TRAIN_KSGL.getKey() + grksglDto.getPxid() + ":" + ksglDto.getTkid() + ":" + t_pxtkglbDto.getTmlx(), String.valueOf(danx), JSON.toJSONString(ksglDto),RedisCommonKeyEnum.TRAIN_KSGL.getExpire());
								danx += 1;
							} else if ("MULTIPLE".equals(t_pxtkglbDto.getTmlx())) {
								redisUtil.hset(RedisCommonKeyEnum.TRAIN_KSGL.getKey() + grksglDto.getPxid() + ":" + ksglDto.getTkid() + ":" + t_pxtkglbDto.getTmlx(), String.valueOf(duox), JSON.toJSONString(ksglDto),RedisCommonKeyEnum.TRAIN_KSGL.getExpire());
								duox += 1;
							} else if ("EXPLAIN".equals(t_pxtkglbDto.getTmlx())) {
								redisUtil.hset(RedisCommonKeyEnum.TRAIN_KSGL.getKey() + grksglDto.getPxid() + ":" + ksglDto.getTkid() + ":" + t_pxtkglbDto.getTmlx(), String.valueOf(jd), JSON.toJSONString(ksglDto),RedisCommonKeyEnum.TRAIN_KSGL.getExpire());
								jd += 1;
							} else if ("GAP".equals(t_pxtkglbDto.getTmlx())) {
								redisUtil.hset(RedisCommonKeyEnum.TRAIN_KSGL.getKey() + grksglDto.getPxid() + ":" + ksglDto.getTkid() + ":" + t_pxtkglbDto.getTmlx(), String.valueOf(tk), JSON.toJSONString(ksglDto),RedisCommonKeyEnum.TRAIN_KSGL.getExpire());
								tk += 1;
							} else if ("JUDGE".equals(t_pxtkglbDto.getTmlx())) {
								redisUtil.hset(RedisCommonKeyEnum.TRAIN_KSGL.getKey() + grksglDto.getPxid() + ":" + ksglDto.getTkid() + ":" + t_pxtkglbDto.getTmlx(), String.valueOf(pd), JSON.toJSONString(ksglDto),RedisCommonKeyEnum.TRAIN_KSGL.getExpire());
								pd += 1;
							}

							preKsid = t_pxtkglbDto.getKsid();
						}

						if ("SELECT".equals(t_pxtkglbDto.getTmlx()) || "MULTIPLE".equals(t_pxtkglbDto.getTmlx()) || "JUDGE".equals(t_pxtkglbDto.getTmlx())) {
							KsmxDto ksmxDto = new KsmxDto();
							ksmxDto.setKsid(t_pxtkglbDto.getKsid());
							ksmxDto.setKsmxid(t_pxtkglbDto.getKsmxid());
							ksmxDto.setXxdm(t_pxtkglbDto.getXxdm());
							ksmxDto.setXxnr(t_pxtkglbDto.getXxnr());
							ksmxDtos.add(ksmxDto);
						}
					}
					if(StringUtil.isNotBlank(preKsid)) {
						redisUtil.set(RedisCommonKeyEnum.TRAIN_KSMX.getKey()+ksglDto.getKsid(),JSON.toJSONString(ksmxDtos),RedisCommonKeyEnum.TRAIN_KSMX.getExpire());
					}

					log.error("getProblemDetails hmget init end.");
				}

			}
			hmget = redisUtil.hmget(RedisCommonKeyEnum.TRAIN_PXTKTMLX.getKey() + grksglDto.getPxid());
		}
		for (Map.Entry<Object, Object> entry : hmget.entrySet()) {

			log.error("getProblemDetails hmget.entrySet start.");
			
			int sl = JSON.parseObject(entry.getValue().toString(),Integer.class);
			//例 tkid:SELECT
			String tkid = entry.getKey().toString();
			Map<Object, Object> tmlist = redisUtil.hmget(RedisCommonKeyEnum.TRAIN_KSGL.getKey() +grksglDto.getPxid()+":"+tkid);
			if(tkid.contains("SELECT")){
				 list_t = ksglService.groupQuestions(sl, tmlist, list_t, grksglDto);
			}else if(tkid.contains("MULTIPLE")){
				duoxlist = ksglService.groupQuestions(sl, tmlist, duoxlist, grksglDto);
			}else if(tkid.contains("EXPLAIN")){
				jdlist = ksglService.groupQuestions(sl, tmlist, jdlist, grksglDto);
			}else if(tkid.contains("GAP")){
				tklist = ksglService.groupQuestions(sl, tmlist, tklist, grksglDto);
			}else if(tkid.contains("JUDGE")){
				pdlist = ksglService.groupQuestions(sl, tmlist, pdlist, grksglDto);
			}

			log.error("getProblemDetails hmget.entrySet end.");
		}
		list_t.addAll(duoxlist);
		list_t.addAll(tklist);
		list_t.addAll(pdlist);
		list_t.addAll(jdlist);
		log.error("getProblemDetails end.");
		log.error("返回的list大小为："+list_t.size());
		log.error("返回的list为："+JSON.toJSONString(list_t));
		result.put("list",list_t);
		return result;
	}
    /**
     * 保存考试结果并判断得分
     */
    @RequestMapping("/task/minidataSaveTestResults")
    @ResponseBody
    public Map<String,Object> saveTestResults(String jglist)  {
		Map<String,Object> map= new HashMap<>();
		String gzid;
		if(StringUtil.isNotBlank(jglist)){
            log.error("拿到的结果数据为："+jglist);
            JSONArray jsonArray = JSONArray.parseArray(jglist);
            if(!CollectionUtils.isEmpty(jsonArray)){
                gzid=jsonArray.getJSONObject(0).getString("gzid");
                amqpTempl.convertAndSend("sys.igams", preRabbitFlg+"sys.igams.grks.submit"+rabbitFlg,jglist);
                map.put("gzid",gzid);
            }else{
                log.error("未获取到答题结果数据");
            }
		}else{
			log.error("未获取到答题结果数据");
		}
		return map;

    }

	/**
	 * 保存考试结果并判断得分
	 */
	@RequestMapping("/task/minidataGetTestResult")
	@ResponseBody
	public Map<String,Object> getTestResult(String gzid, HttpServletRequest request)  {
		Map<String,Object> map= new HashMap<>();
		log.error("--------------------数据开始拿取----工作ID："+gzid+"--------");
		Object get = redisUtil.get(RedisCommonKeyEnum.TRAIN_GRKSGL.getKey() +gzid);
		GrksglDto grksglDto=new GrksglDto();
		if(get!=null) {
			grksglDto = JSON.parseObject(get.toString(), GrksglDto.class);
			map.put("grksglDto",grksglDto);
			if(StringUtil.isNotBlank(grksglDto.getZf())){
				log.error("--------------------数据拿取成功-------------------");
				log.error("总分:"+grksglDto.getZf()+"-----------");
			}else{
				log.error("--------------------总分为空-------------------");
				if (StringUtil.isNotBlank(gzid)&&StringUtil.isNotBlank(request.getParameter("grksid"))){
					grksglDto.setGzid(gzid);
					grksglDto.setGrksid(request.getParameter("grksid"));
					grksglDto = grksglService.getDtoByIdAndGzid(grksglDto);
					if (grksglDto!=null){
						map.put("grksglDto",grksglDto);
					}else {
						log.error("--------------------数据拿取失败getDtoByIdAndGzid-------------------");
					}
				}else {
					log.error("--------------------数据拿取失败-------------------");
				}
			}
		}else{
			if (StringUtil.isNotBlank(gzid)&&StringUtil.isNotBlank(request.getParameter("grksid"))){
				grksglDto.setGzid(gzid);
				grksglDto.setGrksid(request.getParameter("grksid"));
				grksglDto = grksglService.getDtoByIdAndGzid(grksglDto);
				if (grksglDto!=null){
					map.put("grksglDto",grksglDto);
				}else {
					log.error("--------------------数据拿取失败-------------------");
				}
			}else {
				log.error("--------------------数据拿取失败-------------------");
			}
		}
		return map;

	}

	/**
	 * 查看答案
	 */
	@RequestMapping("/task/viewAnswers")
	@ResponseBody
	public Map<String,Object> viewAnswers(GrksmxDto grksmxDto){
		Map<String,Object> map= new HashMap<>();
		List<GrksmxDto> dtoList = grksmxService.getListByGrksid(grksmxDto);
		for(int i=0;i<dtoList.size();i++){
			KsmxDto ksmxDto=new KsmxDto();
			ksmxDto.setKsid(dtoList.get(i).getKsid());
			dtoList.get(i).setXxlist(ksmxService.getDtoList(ksmxDto));
			dtoList.get(i).setXh(String.valueOf(i+1));
		}
		map.put("list",dtoList);
		return map;
	}

	/**
	 * 钉钉查看答案
	 */
	@RequestMapping("/task/minidataViewAnswers")
	@ResponseBody
	public Map<String,Object> minidataViewAnswers(GrksmxDto grksmxDto){
		return this.viewAnswers(grksmxDto);
	}

	/**
	 * 个人考试列表页面
	 */
	@RequestMapping("/test/pageListTest")
	public ModelAndView pageListTest(){
		ModelAndView mav = new ModelAndView("train/test/test_list");
		mav.addObject("urlPrefix", urlPrefix);
		Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListByType(new BasicDataTypeEnum[]{BasicDataTypeEnum.AFFILIATED_COMPANY});
		mav.addObject("ssgslist", jcsjlist.get(BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));
		return mav;
	}

	/**
	 * 个人考试列表
	 */
	@RequestMapping("/test/pageGetListTest")
	@ResponseBody
	public Map<String,Object> listTest(GrksglDto grksglDto){
		List<GrksglDto> list = grksglService.getPagedDtoList(grksglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", grksglDto.getTotalNumber());
		result.put("rows", list);
		return result;
	}

	/**
	 * 个人考试列表页面查看按钮
	 */
	@RequestMapping("/test/viewTest")
	public ModelAndView viewTest(GrksglDto grksglDto){
		ModelAndView mav = new ModelAndView("train/test/test_view");
		GrksglDto dto = grksglService.getDto(grksglDto);
		GrksmxDto grksmxDto=new GrksmxDto();
		grksmxDto.setGrksid(grksglDto.getGrksid());
		List<GrksmxDto> list = grksmxService.getListByGrksid(grksmxDto);
		List<GrksmxDto> jdlist=new ArrayList<>();
		List<GrksmxDto> xzlist=new ArrayList<>();
		for(GrksmxDto dto_t:list){
			if("GAP".equals(dto_t.getTmlx())||"EXPLAIN".equals(dto_t.getTmlx())){
				jdlist.add(dto_t);
			}else{
				xzlist.add(dto_t);
			}
		}
		mav.addObject("flag","0");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("grksglDto",dto);
		mav.addObject("jdlist",jdlist);
		mav.addObject("xzlist",xzlist);
		return mav;
	}

	@RequestMapping("/test/pagedataTest")
	public ModelAndView pagedataTest(GrksglDto grksglDto){
		return viewTest(grksglDto);
	}

	/**
	 * 个人考试列表删除
	 */
	@RequestMapping("/test/delTest")
	@ResponseBody
	public Map<String,Object> delTest(GrksglDto grksglDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		grksglDto.setScry(user.getYhid());
		grksglService.delete(grksglDto);
		GrksmxDto grksmxDto=new GrksmxDto();
		grksmxDto.setScry(user.getYhid());
		grksmxDto.setIds(grksglDto.getIds());
		boolean isSuccess = grksmxService.delete(grksmxDto);
		Map<String,Object> result = new HashMap<>();
		result.put("status", isSuccess?"success":"fail");
		result.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return result;
	}

	/**
	 * 导入页面
	 */
	@RequestMapping(value ="/test/pageImportProblems")
	public ModelAndView importProblems(){
		ModelAndView mav = new ModelAndView("train/test/test_import");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_TOPIC.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 培训记录导入页面
	 */
	@RequestMapping(value ="/train/pageImportTrainRecords")
	public ModelAndView pageImportTrainRecords(){
		ModelAndView mav = new ModelAndView("train/train/train_records_import");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_TRAIN_RECORDS.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 培训列表页面
	 */
	@RequestMapping("/test/pageListTrain")
	public ModelAndView pageListTrain(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("train/train/train_list");
		mav.addObject("flg",request.getParameter("flg"));
		mav.addObject("auditType", AuditTypeEnum.AUDIT_TRAIN.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListByType(new BasicDataTypeEnum[]{BasicDataTypeEnum.TRAINCATEGORY,BasicDataTypeEnum.AFFILIATED_COMPANY});
		mav.addObject("pxlblist", jcsjlist.get(BasicDataTypeEnum.TRAINCATEGORY.getCode()));
		mav.addObject("ssgslist", jcsjlist.get(BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));
		return mav;
	}

	/**
	 * 培训列表
	 */
	@RequestMapping("/test/pageGetListTrain")
	@ResponseBody
	public Map<String,Object> listTrain(HttpServletRequest request,PxglDto pxglDto){
		User user = getLoginInfo(request);
		pxglDto.setFzr(user.getYhid());
		List<PxglDto> list = pxglService.getPagedDtoList(pxglDto);
		if(!CollectionUtils.isEmpty(list)){
			for(PxglDto dto:list){
				if(StringUtil.isNotBlank(dto.getFjid())){
					dto.setTpwjlj("/ws/file/fileShow?acess_token="+pxglDto.getAccess_token()+"&ywlx="+BusTypeEnum.IMP_TRAIN_PICTURE.getCode()+"&fjid="+dto.getFjid());
				}
			}
		}
		Map<String,Object> result = new HashMap<>();
		result.put("total", pxglDto.getTotalNumber());
		result.put("rows", list);
		return result;
	}
	/**
	 * 培训列表
	 */
	@RequestMapping("/test/pagedataPxxx")
	@ResponseBody
	public Map<String,Object> pagedataPxxx(GzglDto gzglDto){
		List<GzglDto> list = gzglService.getTrainInfo(gzglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("rows", list);
		return result;
	}
	/**
	 * 钉钉接口培训列表
	 */
	@RequestMapping("/train/minidataListTrain")
	@ResponseBody
	public Map<String,Object> minidataListTrain(PxglDto pxglDto){
		List<PxglDto> list = pxglService.getPagedDtoList(pxglDto);
		if(!CollectionUtils.isEmpty(list)){
			for(PxglDto dto:list){
				if(StringUtil.isNotBlank(dto.getFjid())){
					dto.setTpwjlj("/ws/file/fileShow?acess_token="+pxglDto.getAccess_token()+"&ywlx="+BusTypeEnum.IMP_TRAIN_PICTURE.getCode()+"&fjid="+dto.getFjid());
				}
			}
		}
		Map<String,Object> result = new HashMap<>();
		result.put("total", pxglDto.getTotalNumber());
		result.put("rows", list);
		return result;
	}
	/**
	 * 查看按钮
	 */
	@RequestMapping("/test/viewTrain")
	public ModelAndView viewTrain(PxglDto pxglDto,HttpServletRequest request){
		ModelAndView mav = new ModelAndView("train/train/train_view");
		String flg = request.getParameter("flg");
		mav.addObject("flg",flg);
		if("1".equals(flg)){
			FjcfbDto spfjDto = new FjcfbDto();
			spfjDto.setYwlx(BusTypeEnum.IMP_TRAIN.getCode());
			spfjDto.setYwid(pxglDto.getPxid());
			List<FjcfbDto> fjlist = fjcfbService.getDtoList(spfjDto);
			if(!CollectionUtils.isEmpty(fjlist)){
				mav.addObject("fjlist", fjlist);
				mav.addObject("num",fjlist.size());
			}else{
				List<FjcfbDto> fjlist_t=new ArrayList<>();
				mav.addObject("fjlist", fjlist_t);
				mav.addObject("num",fjlist_t.size());
			}
		}else{
			PxglDto dto = pxglService.getDto(pxglDto);
			FjcfbDto tpfjDto = new FjcfbDto();
			tpfjDto.setYwlx(BusTypeEnum.IMP_TRAIN_PICTURE.getCode());
			tpfjDto.setYwid(pxglDto.getPxid());
			List<FjcfbDto> tpdto = fjcfbService.getDtoList(tpfjDto);
			if(!CollectionUtils.isEmpty(tpdto)){
				mav.addObject("tpfjDto", tpdto.get(0));
				mav.addObject("fjcfbDtos", tpdto);
			}else{
				FjcfbDto fjcfbDto_t=new FjcfbDto();
				List<FjcfbDto> fjcfbDto_ts = new ArrayList<>();
				mav.addObject("tpfjDto",fjcfbDto_t);
				mav.addObject("fjcfbDtos", fjcfbDto_ts);
			}
			mav.addObject("pxglDto",dto);
			PxtkglbDto pxtkglbDto=new PxtkglbDto();
			pxtkglbDto.setPxid(pxglDto.getPxid());
			List<PxtkglbDto> list = pxtkglbService.getDtoList(pxtkglbDto);
			mav.addObject("list",list);
			GzglDto gzglDto=new GzglDto();
			gzglDto.setYwid(pxglDto.getPxid());
			List<GzglDto> gzlist = gzglService.selectDistributedDtos(gzglDto);
			int ffgs=0;
			int wtggs=0;
			if(gzlist!=null&&!gzlist.isEmpty()){
				for (GzglDto gzglDto_t:gzlist) {
					ffgs++;
					if(("是".equals(gzglDto_t.getSfcs())&&"未通过".equals(gzglDto_t.getSftg()))||("否".equals(gzglDto_t.getSfcs())&&!"80".equals(gzglDto_t.getSftg()))){
						wtggs++;
					}
				}
			}
			mav.addObject("statistic",ffgs+"/"+wtggs);
			PxqxszDto pxqxszDto=new PxqxszDto();
			pxqxszDto.setPxid(pxglDto.getPxid());
			List<PxqxszDto> dtoList = pxqxszService.getDtoList(pxqxszDto);
			mav.addObject("bmList",dtoList);
			FjcfbDto qrCode = new FjcfbDto();
			qrCode.setYwlx(BusTypeEnum.IMP_TRAIN_QRCODE.getCode());
			qrCode.setYwid(pxglDto.getPxid());
			List<FjcfbDto> qrCodes = fjcfbService.getDtoList(qrCode);
			mav.addObject("qrCodes",qrCodes);
			gzglDto.setPxid(pxglDto.getPxid());
			List<GzglDto> dtoTrainSignInPeo = gzglService.getDtoTrainSignInPeo(gzglDto);
			mav.addObject("dtoTrainSignInPeo",dtoTrainSignInPeo);
		}
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("gzid",pxglDto.getGzid());
		mav.addObject("spwcbj",pxglDto.getSpwcbj());
		return mav;
	}

	/**
	 * 分发列表
	 */
	@RequestMapping("/test/pagedataDistributedData")
	@ResponseBody
	public Map<String,Object> pagedataDistributedData(GzglDto gzglDto){
		Map<String, Object> map= new HashMap<>();
		List<GzglDto> gzlist = gzglService.selectDistributedDtos(gzglDto);
		map.put("rows",gzlist);
		return map;
	}

	/**
	 * 分发按钮
	 */
	@RequestMapping("/test/taskDistribute")
	public ModelAndView taskDistribute(PxglDto pxglDto){
		ModelAndView mav = new ModelAndView("train/train/train_distribute");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_DISTRIBUTION_PERSONEL.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("pxglDto",pxglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("flag", "distribute");
		return mav;
	}

	/**
	 * 新增按钮
	 */
	@RequestMapping("/test/addTrain")
	public ModelAndView addTrain(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("train/train/train_edit");
		List<User> xtyhlist=commonDao.getUserList();
		mav.addObject("pxlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINCATEGORY.getCode()));
		mav.addObject("xtyhlist",xtyhlist);
		mav.addObject("tpywlx", BusTypeEnum.IMP_TRAIN_PICTURE.getCode());
		mav.addObject("spywlx", BusTypeEnum.IMP_TRAIN.getCode());
		mav.addObject("ssgslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));
		mav.addObject("pxfslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINING_METHODS.getCode()));
		HbszDto hbszDto=new HbszDto();
		mav.addObject("hbszlist",hbszService.getDtoList(hbszDto));
		mav.addObject("urlPrefix", urlPrefix);
		TkglDto tkglDto=new TkglDto();
		List<TkglDto> tklist = tkglService.getDtoList(tkglDto);
		mav.addObject("tklist",JSON.toJSONString(tklist));
		mav.addObject("url", "/train/question/pagedataListChoose?access_token=");
		mav.addObject("formAction", "pagedataAddSaveTrain");
		FjcfbDto tpfjDto = new FjcfbDto();
		mav.addObject("tpfjDto", tpfjDto);
		FjcfbDto spfjDto = new FjcfbDto();
		mav.addObject("spfjDto", spfjDto);
		PxglDto pxdto=new PxglDto();
		User user = getLoginInfo(request);
		//获取默认部门
		user = commonService.getUserInfoById(user);
		if (user != null) {
			pxdto.setBmmc(user.getJgmc());
			pxdto.setBm(user.getJgid());
		}
		mav.addObject("pxglDto", pxdto);
		mav.addObject("flag","add");
		return mav;
	}
	/**
	 * 获取题库信息
	 */
	@RequestMapping(value="/question/pagedataTk")
	@ResponseBody
	public Map<String,Object> pagedataTk(TkglDto tkglDto){
		Map<String, Object> map= new HashMap<>();
		List<TkglDto> tklist = tkglService.getDtoList(tkglDto);
		map.put("tklist",tklist);
		return map;
	}


	/**
	 * 修改按钮
	 */
	@RequestMapping("/test/modTrain")
	public ModelAndView modTrain(PxglDto pxglDto){
		ModelAndView mav = new ModelAndView("train/train/train_edit");
		List<User> xtyhlist=commonDao.getUserList();
		mav.addObject("pxlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINCATEGORY.getCode()));
		mav.addObject("xtyhlist",xtyhlist);
		mav.addObject("tpywlx", BusTypeEnum.IMP_TRAIN_PICTURE.getCode());
		mav.addObject("spywlx", BusTypeEnum.IMP_TRAIN.getCode());
		mav.addObject("ssgslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));
		mav.addObject("pxfslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINING_METHODS.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		TkglDto tkglDto=new TkglDto();
		List<TkglDto> tklist = tkglService.getDtoList(tkglDto);
		mav.addObject("tklist",JSON.toJSONString(tklist));
		mav.addObject("url", "/train/question/pagedataListChoose?pxid="+pxglDto.getPxid()+"&access_token=");
		mav.addObject("formAction", "pagedataModSaveTrain");
		PxglDto pxdto = pxglService.getDto(pxglDto);
		HbszDto hbszDto=new HbszDto();
		if (StringUtil.isNotBlank(pxdto.getHbszid())){
			hbszDto.setHbszid(pxdto.getHbszid());
		}
		mav.addObject("hbszlist",hbszService.getDtoList(hbszDto));
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setFcsid(pxdto.getPxlbcs());
		pxdto.setXgqspxz(pxdto.getSpxz());
		List<JcsjDto> pxzlblist = getSubcategory(jcsjDto);
		mav.addObject("pxzlblist", pxzlblist);
		mav.addObject("pxglDto", pxdto);
		FjcfbDto tpfjDto = new FjcfbDto();
		tpfjDto.setYwlx(BusTypeEnum.IMP_TRAIN_PICTURE.getCode());
		tpfjDto.setYwid(pxglDto.getPxid());
		List<FjcfbDto> dto = fjcfbService.getDtoList(tpfjDto);
		if(!CollectionUtils.isEmpty(dto)){
			mav.addObject("tpfjDto", dto.get(0));
			mav.addObject("fjcfbDtos", dto);
		}else{
			FjcfbDto fjcfbDto_t=new FjcfbDto();
			List<FjcfbDto> fjcfbDto_ts = new ArrayList<>();
			mav.addObject("tpfjDto",fjcfbDto_t);
			mav.addObject("fjcfbDtos", fjcfbDto_ts);
		}
		FjcfbDto spfjDto = new FjcfbDto();
		spfjDto.setYwlx(BusTypeEnum.IMP_TRAIN.getCode());
		spfjDto.setYwid(pxglDto.getPxid());
		List<FjcfbDto> fjlist = fjcfbService.getDtoList(spfjDto);
		if(!CollectionUtils.isEmpty(fjlist)){
			mav.addObject("fjlist", fjlist);
			mav.addObject("num",fjlist.size());
		}else{
			List<FjcfbDto> fjlist_t=new ArrayList<>();
			mav.addObject("fjlist", fjlist_t);
			mav.addObject("num",fjlist_t.size());
		}
		mav.addObject("flag","mod");
		return mav;
	}


	/**
	 * 复制按钮
	 */
	@RequestMapping("/test/copyTrain")
	public ModelAndView copyTrain(PxglDto pxglDto){
		ModelAndView mav = new ModelAndView("train/train/train_edit");
		List<User> xtyhlist=commonDao.getUserList();
		mav.addObject("pxlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINCATEGORY.getCode()));
		mav.addObject("xtyhlist",xtyhlist);
		mav.addObject("tpywlx", BusTypeEnum.IMP_TRAIN_PICTURE.getCode());
		mav.addObject("spywlx", BusTypeEnum.IMP_TRAIN.getCode());
		mav.addObject("ssgslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));
		mav.addObject("pxfslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINING_METHODS.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		TkglDto tkglDto=new TkglDto();
		List<TkglDto> tklist = tkglService.getDtoList(tkglDto);
		mav.addObject("tklist",JSON.toJSONString(tklist));
		mav.addObject("url", "/train/question/pagedataListChoose?pxid="+pxglDto.getPxid()+"&access_token=");
		mav.addObject("formAction", "pagedataCopySaveTrain");
		PxglDto pxdto = pxglService.getDto(pxglDto);
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setFcsid(pxdto.getPxlbcs());
		List<JcsjDto> pxzlblist = getSubcategory(jcsjDto);
		mav.addObject("pxzlblist", pxzlblist);
		mav.addObject("pxglDto", pxdto);
		HbszDto hbszDto=new HbszDto();
		mav.addObject("hbszlist",hbszService.getDtoList(hbszDto));
		FjcfbDto fjcfbDto_t=new FjcfbDto();
		List<FjcfbDto> fjcfbDto_ts = new ArrayList<>();
		mav.addObject("tpfjDto",fjcfbDto_t);
		mav.addObject("fjcfbDtos", fjcfbDto_ts);
		List<FjcfbDto> fjlist_t=new ArrayList<>();
		mav.addObject("fjlist", fjlist_t);
		mav.addObject("num",fjlist_t.size());
		return mav;
	}

	/**
	 * 培训列表删除按钮
	 */
	@RequestMapping("/test/delTrain")
	@ResponseBody
	public Map<String,Object> delTrain(PxglDto pxglDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		pxglDto.setScry(user.getYhid());
		boolean isSuccess =pxglService.delete(pxglDto);
		GzglDto gzglDto=new GzglDto();
		gzglDto.setIds(pxglDto.getIds());
		gzglDto.setScry(user.getYhid());
		gzglService.deleteByYwids(gzglDto);
		PxtkglbDto pxtkglbDto=new PxtkglbDto();
		pxtkglbDto.setScry(user.getYhid());
		pxtkglbDto.setIds(pxglDto.getIds());
		pxtkglbService.delete(pxtkglbDto);
		Map<String,Object> result = new HashMap<>();
		result.put("status", isSuccess?"success":"fail");
		result.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return result;
	}

	/**
	 * 清单新增页面
	 */
	@RequestMapping("/train/minidataAddView")
	@ResponseBody
	public Map<String,Object> addView(PxglDto pxglDto){
		Map<String,Object> result = new HashMap<>();
		List<String> ids = new ArrayList<>();
		if(StringUtil.isNotBlank(pxglDto.getPxid())) {
			GzglDto gzglDto=new GzglDto();
			gzglDto.setYwid(pxglDto.getPxid());
			gzglDto.setSortName("yh.zsxm");
			gzglDto.setSortLastName("gzgl.ywmc");
			List<GzglDto> t_List = gzglService.verification(gzglDto);
			for (GzglDto dto : t_List) {
				ids.add(dto.getYhid());
			}
		}
		pxglDto.setIds(ids);
		pxglDto.setSortName("zsxm");
		pxglDto.setSortLastName("jgmc");
		TkglDto tkglDto=new TkglDto();
		List<TkglDto> tklist = tkglService.getDtoList(tkglDto);
		List<User> xtyhlist=pxglService.getPagedDtoListOutByIds(pxglDto);
		Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListByType(new BasicDataTypeEnum[]{BasicDataTypeEnum.TRAINCATEGORY});
		result.put("pxlblist", jcsjlist.get(BasicDataTypeEnum.TRAINCATEGORY.getCode()));
		result.put("list",xtyhlist);
		result.put("tklist",tklist);
		return result;
	}

	/**
	 * 重新分发
	 */
	@RequestMapping("/train/pagedataReDistribution")
	@ResponseBody
	public Map<String,Object> reDistribution(GzglDto gzglDto,HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		User user = getLoginInfo(request);
		PxglDto pxglDto=new PxglDto();
		pxglDto.setGzid(gzglDto.getGzid());
		PxglDto pxglDto_t = pxglService.viewTrainTask(pxglDto);
		if(StringUtil.isNotBlank(pxglDto_t.getKscs())){
			int kscs = Integer.parseInt(pxglDto_t.getKscs());
			GrksglDto grksglDto=new GrksglDto();
			grksglDto.setGzid(pxglDto.getGzid());
			List<GrksglDto> grksglDtos=grksglService.getPersonalTests(grksglDto);
			int testCount = grksglDtos.size();
			if((kscs-testCount)==0){
				result.put("status", "fail");
				result.put("message","已无考试次数，无法重新分发！");
				return result;
			}
		}
		gzglDto.setScry(user.getYhid());
		gzglDto.setZt(StatusEnum.CHECK_NO.getCode());
		boolean isSuccess = gzglService.update(gzglDto);
		result.put("status", isSuccess?"success":"fail");
		result.put("message", isSuccess?"分发成功！":"分发失败！");
		return result;
	}

	/**
	 * 新增培训项目
	 */
	@RequestMapping("/train/pagedataAddSaveTrain")
	@ResponseBody
	public Map<String,Object> addSaveTrain(PxglDto pxglDto,HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		pxglDto.setPxid(null);
		PxglDto pxdto = pxglService.getDto(pxglDto);
		if(pxdto!=null){
			result.put("status","fail");
			result.put("message","此培训标题已存在，请修改后再试！");
			return result;
		}
		User user = getLoginInfo(request);
		pxglDto.setLrry(user.getYhid());
		boolean isSuccess;
		try {
			isSuccess = pxglService.insertPxglDto(pxglDto);
			result.put("status", isSuccess?"success":"fail");
			result.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			result.put("status", "fail");
			result.put("message", e.toString());
		}
		result.put("auditType", AuditTypeEnum.AUDIT_TRAIN.getCode());
		result.put("ywid", pxglDto.getPxid());
		result.put("urlPrefix", urlPrefix);
		return result;
	}


	/**
	 * 新增培训项目
	 */
	@RequestMapping("/train/minidataAddSaveTrain")
	@ResponseBody
	public Map<String,Object> minidataAddSaveTrain(PxglDto pxglDto,HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		pxglDto.setPxid(null);
		PxglDto pxdto = pxglService.getDto(pxglDto);
		if(pxdto!=null){
			result.put("status","fail");
			result.put("message","此培训标题已存在，请修改后再试！");
			return result;
		}
		User user = getLoginInfo(request);
		pxglDto.setLrry(user.getYhid());
		boolean isSuccess;
		try {
			isSuccess = pxglService.insertPxglDto(pxglDto);
			result.put("status", isSuccess?"success":"fail");
			result.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			result.put("status", "fail");
			result.put("message", e.toString());
		}
		return result;
	}

	/**
	 * 复制培训项目
	 */
	@RequestMapping("/train/pagedataCopySaveTrain")
	@ResponseBody
	public Map<String,Object> copySaveTrain(PxglDto pxglDto,HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		pxglDto.setPxid(null);
		PxglDto pxdto = pxglService.getDto(pxglDto);
		if(pxdto!=null){
			result.put("status","fail");
			result.put("message","此培训标题已存在，请修改后再试！");
			return result;
		}
		User user = getLoginInfo(request);
		pxglDto.setLrry(user.getYhid());
		boolean isSuccess;
		try {
			isSuccess = pxglService.insertPxglDto(pxglDto);
			result.put("status", isSuccess?"success":"fail");
			result.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			result.put("status", "fail");
			result.put("message", e.toString());
		}
		return result;
	}

	/**
	 * 修改培训项目
	 */
	@RequestMapping("/train/pagedataModSaveTrain")
	@ResponseBody
	public Map<String,Object> modSaveTrain(PxglDto pxglDto,HttpServletRequest request) throws BusinessException {
		Map<String,Object> result = new HashMap<>();
		User user = getLoginInfo(request);
		pxglDto.setXgry(user.getYhid());
		if(!CollectionUtils.isEmpty(pxglDto.getFjids())){
			for (int i = 0;i<pxglDto.getFjids().size();i++) {
				boolean saveFile = fjcfbService.save2RealFile(pxglDto.getFjids().get(i), pxglDto.getPxid());
				if(!saveFile){
					result.put("status","fail");
					result.put("message",xxglService.getModelById("ICOM00002").getXxnr());
					return result;
				}

			}
		}
        boolean isSuccess;
        if("1".equals(pxglDto.getFlag())){
            isSuccess = pxglService.update(pxglDto);
			if ("1".equals(pxglDto.getSffshb())){
				if (!pxglDto.getYhbszid().equals(pxglDto.getHbszid())) {
					DsrwszDto dsrwszDto = new DsrwszDto();
					if (StringUtil.isNotBlank(pxglDto.getYhbszid())) {
						HbszDto hbszDto = hbszService.getDtoById(pxglDto.getYhbszid());
						dsrwszDto.setRwid(hbszDto.getRwid());
						dsrwszDto = dsrwszService.getDto(dsrwszDto);
						if (null != dsrwszDto) {
							String string = "";
							dsrwszDto.setCs(string);
							boolean flag = dsrwszService.updateDsxx(dsrwszDto);
							if (!flag) {
								throw new BusinessException("msg", "修改定时任务失败!");
							}
						}
					}
					if (!pxglDto.getYhbszid().equals(pxglDto.getHbszid())) {
						HbszDto hbszDto1 = hbszService.getDtoById(pxglDto.getHbszid());
						DsrwszDto dsrwszDto1 = new DsrwszDto();
						dsrwszDto1.setRwid(hbszDto1.getRwid());
						dsrwszDto1 = dsrwszService.getDto(dsrwszDto1);
						if (null != dsrwszDto1) {
							String string1 = "pxid=" + pxglDto.getPxid();
							dsrwszDto1.setCs(string1);
							boolean flag1 = dsrwszService.updateDsxx(dsrwszDto1);
							if (!flag1) {
								throw new BusinessException("msg", "修改定时任务失败!");
							}
						}
					}
				}

			}else {
				if (StringUtil.isNotBlank(pxglDto.getYhbszid())){
					HbszDto hbszDto=hbszService.getDtoById(pxglDto.getYhbszid());
					DsrwszDto dsrwszDto=new DsrwszDto();
					dsrwszDto.setRwid(hbszDto.getRwid());
					dsrwszDto=dsrwszService.getDto(dsrwszDto);
					if (null!=dsrwszDto) {
						String string = "";
						dsrwszDto.setCs(string);
						boolean flag = dsrwszService.updateDsxx(dsrwszDto);
						if (!flag) {
							throw new BusinessException("msg", "修改定时任务失败!");
						}
					}
				}
			}
        }else{
            isSuccess = pxglService.updatePxglDto(pxglDto);
			if ("1".equals(pxglDto.getSffshb())){
				DsrwszDto dsrwszDto = new DsrwszDto();
				if (!pxglDto.getYhbszid().equals(pxglDto.getHbszid())) {
					if (StringUtil.isNotBlank(pxglDto.getYhbszid())) {
						HbszDto hbszDto = hbszService.getDtoById(pxglDto.getYhbszid());
						dsrwszDto.setRwid(hbszDto.getRwid());
						dsrwszDto = dsrwszService.getDto(dsrwszDto);
						if (null != dsrwszDto) {
							String string = "";
							dsrwszDto.setCs(string);
							boolean flag = dsrwszService.updateDsxx(dsrwszDto);
							if (!flag) {
								throw new BusinessException("msg", "修改定时任务失败!");
							}
						}
					}
					if (!pxglDto.getYhbszid().equals(pxglDto.getHbszid())) {
						HbszDto hbszDto1 = hbszService.getDtoById(pxglDto.getHbszid());
						DsrwszDto dsrwszDto1 = new DsrwszDto();
						dsrwszDto1.setRwid(hbszDto1.getRwid());
						dsrwszDto1 = dsrwszService.getDto(dsrwszDto1);
						if (null != dsrwszDto1) {
							String string1 = "pxid=" + pxglDto.getPxid();
							dsrwszDto1.setCs(string1);
							boolean flag1 = dsrwszService.updateDsxx(dsrwszDto1);
							if (!flag1) {
								throw new BusinessException("msg", "修改定时任务失败!");
							}
						}
					}
				}

		}else {
				if (StringUtil.isNotBlank(pxglDto.getYhbszid())){
					HbszDto hbszDto=hbszService.getDtoById(pxglDto.getYhbszid());
					DsrwszDto dsrwszDto=new DsrwszDto();
					dsrwszDto.setRwid(hbszDto.getRwid());
					dsrwszDto=dsrwszService.getDto(dsrwszDto);
					if (null!=dsrwszDto) {
						String string = "";
						dsrwszDto.setCs(string);
						boolean flag = dsrwszService.updateDsxx(dsrwszDto);
						if (!flag) {
							throw new BusinessException("msg", "修改定时任务失败!");
						}
					}
				}
			}
        }
        result.put("status", isSuccess?"success":"fail");
        result.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        result.put("auditType", AuditTypeEnum.AUDIT_TRAIN.getCode());
        result.put("ywid", pxglDto.getPxid());
        result.put("urlPrefix", urlPrefix);
        return result;
    }
	/**
	 * 钉钉修改培训项目
	 */
	@RequestMapping("/train/minidataModSaveTrain")
	@ResponseBody
	public Map<String,Object> minidataModSaveTrain(PxglDto pxglDto,HttpServletRequest request) throws BusinessException {
		return this.modSaveTrain(pxglDto,request);
	}

	/**
	 * 分发任务通知负责人并新增一条任务记录
	 */
	@RequestMapping("/train/pagedataAddSaveTaskDistribut")
	@ResponseBody
	public Map<String,Object> saveTaskDistribute(GzglDto gzglDto,HttpServletRequest request) {
		gzglDto.setQwwcsj(String.format(gzglDto.getQwwcsj(), "yyyy-MM-dd"));
		Map<String,Object> result = new HashMap<>();
		String fzr=gzglDto.getFzr();
		List<String> ids = gzglDto.getIds();
		if(!CollectionUtils.isEmpty(ids)){
			PxglDto pxglDto=new PxglDto();
			pxglDto.setIds(ids);
			List<PxglDto> dtoList = pxglService.getDtoList(pxglDto);
			String[] split = fzr.split(",");
			List<String> fzrIds = Arrays.asList(split);
			gzglDto.setIds(fzrIds);
			List<GzglDto> gzglDtoList=new ArrayList<>();
			for(PxglDto dto:dtoList){
				String jlbh="";
				if(StringUtil.isNotBlank(dto.getJgkzcs1())){
					String qwwcsj = gzglDto.getQwwcsj();
					String[] strings = qwwcsj.split("-");
					String rq = strings[0]+"-"+strings[1]+strings[2];
					GzglDto gzglDto_t=new GzglDto();
					gzglDto_t.setJlbh(dto.getJgkzcs1() + "-" + rq + "-");
					gzglDto_t.setYwid(dto.getPxid());
					String serial = gzglService.getJlbhSerial(gzglDto_t);
					jlbh=dto.getJgkzcs1() + "-" + rq + "-"+serial;
				}
				for(String yhid:fzrIds){
					GzglDto gzglDto_t=new GzglDto();
					gzglDto_t.setYwid(dto.getPxid());
					gzglDto_t.setFzr(yhid);
					gzglDto_t.setYwmc(dto.getPxbt());
					gzglDto_t.setRwmc(dto.getPxlb());
					gzglDto_t.setQwwcsj(gzglDto.getQwwcsj());
					gzglDto_t.setGzid(StringUtil.generateUUID());
					gzglDto_t.setZt(StatusEnum.CHECK_NO.getCode());
					gzglDto_t.setSffshb(dto.getSffshb());
					User yh=new User();
					yh.setYhid(yhid);
					User user_t = commonService.getUserInfoById(yh);
					gzglDto_t.setJgid(user_t.getJgid());
					gzglDto_t.setDdid(user_t.getDdid());
					gzglDto_t.setYhm(user_t.getYhm());
					gzglDto_t.setUrlqz(urlPrefix);
					gzglDto_t.setYwdz("/train/train/pagedataViewTrainDetails?pxid=" +gzglDto_t.getYwid() );
					gzglDto_t.setTgbj("0");
					gzglDto_t.setPxbt(dto.getPxbt());
					gzglDto_t.setPxlb(dto.getPxlb());
					gzglDto_t.setGlbj("1");
					gzglDto_t.setLrry(getLoginInfo(request).getYhid());
					if(StringUtil.isNotBlank(jlbh)){
						gzglDto_t.setJlbh(jlbh);
					}
					gzglDto_t.setGqsj(dto.getGqsj());
					gzglDtoList.add(gzglDto_t);
				}
			}
			boolean isSuccess =true;
			if(!CollectionUtils.isEmpty(gzglDtoList)){
				isSuccess = gzglService.insertList(gzglDtoList);
			}
			if(isSuccess&&!CollectionUtils.isEmpty(gzglDtoList)) {
				for (GzglDto dto : gzglDtoList) {
					if (!"1".equals(dto.getSffshb())) {
						String TabCur = "0";
						String external = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/learningpage/videolist/videolist&urlPrefix=" + urlPrefix + "&gzid=" + dto.getGzid() + "&ywid=" + dto.getYwid() + "&pxbt=" + dto.getPxbt() + "&TabCur=" + TabCur, StandardCharsets.UTF_8);
						List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
						OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
						btnJsonList.setTitle("小程序");
						btnJsonList.setActionUrl(external);
						btnJsonLists.add(btnJsonList);
						talkUtil.sendCardMessage(dto.getYhm(), dto.getYhid(), xxglService.getMsg("ICOMM_PX00001"), StringUtil.replaceMsg(xxglService.getMsg("ICOMM_PX00002"), dto.getPxbt(), dto.getPxlb(), gzglDto.getQwwcsj(), dto.getGqsj(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
					}
				}
			}
			result.put("status", isSuccess?"success":"fail");
			result.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return result;
	}
	/**
	 * 钉钉分发任务通知负责人并新增一条任务记录
	 */
	@RequestMapping("/train/minidataSaveTaskDistribute")
	@ResponseBody
	public Map<String,Object> minidataSaveTaskDistribute(GzglDto gzglDto,HttpServletRequest request) {
		return this.saveTaskDistribute(gzglDto,request);
	}
	/**
	 * 点击查看任务详情
	 */
	@RequestMapping("/train/pagedataViewTrainDetails")
	public ModelAndView viewTrainDetails(GrksglDto grksglDto){
		ModelAndView mav =null;
		if(StringUtil.isNotBlank(grksglDto.getPxid())){
			mav = new ModelAndView("train/train/train_view");
			PxglDto pxglDto=new PxglDto();
			pxglDto.setPxid(grksglDto.getPxid());
			PxglDto dto = pxglService.getDto(pxglDto);
			FjcfbDto tpfjDto = new FjcfbDto();
			tpfjDto.setYwlx(BusTypeEnum.IMP_TRAIN_PICTURE.getCode());
			tpfjDto.setYwid(pxglDto.getPxid());
			List<FjcfbDto> tpdto = fjcfbService.getDtoList(tpfjDto);
			if(!CollectionUtils.isEmpty(tpdto)){
				mav.addObject("tpfjDto", tpdto.get(0));
				mav.addObject("fjcfbDtos", tpdto);
			}else{
				FjcfbDto fjcfbDto_t=new FjcfbDto();
				List<FjcfbDto> fjcfbDto_ts = new ArrayList<>();
				mav.addObject("tpfjDto",fjcfbDto_t);
				mav.addObject("fjcfbDtos", fjcfbDto_ts);
			}
			FjcfbDto spfjDto = new FjcfbDto();
			spfjDto.setYwlx(BusTypeEnum.IMP_TRAIN.getCode());
			spfjDto.setYwid(pxglDto.getPxid());
			List<FjcfbDto> fjlist = fjcfbService.getDtoList(spfjDto);
			if(!CollectionUtils.isEmpty(fjlist)){
				mav.addObject("fjlist", fjlist);
				mav.addObject("num",fjlist.size());
			}else{
				List<FjcfbDto> fjlist_t=new ArrayList<>();
				mav.addObject("fjlist", fjlist_t);
				mav.addObject("num",fjlist_t.size());
			}
			mav.addObject("urlPrefix", urlPrefix);
			mav.addObject("pxglDto",dto);
			PxtkglbDto pxtkglbDto=new PxtkglbDto();
			pxtkglbDto.setPxid(pxglDto.getPxid());
			List<PxtkglbDto> list = pxtkglbService.getDtoList(pxtkglbDto);
			mav.addObject("list",list);
			GzglDto gzglDto=new GzglDto();
			gzglDto.setYwid(pxglDto.getPxid());
			List<GzglDto> gzlist = gzglService.selectDistributedDtos(gzglDto);
			int ffgs=0;
			int wtggs=0;
			if(gzlist!=null&&!gzlist.isEmpty()){
				for (GzglDto gzglDto_t:gzlist) {
					ffgs++;
					if(("是".equals(gzglDto_t.getSfcs())&&"未通过".equals(gzglDto_t.getSftg()))||("否".equals(gzglDto_t.getSfcs())&&!"80".equals(gzglDto_t.getSftg()))){
						wtggs++;
					}
				}
			}
			mav.addObject("statistic",ffgs+"/"+wtggs);
			PxqxszDto pxqxszDto=new PxqxszDto();
			pxqxszDto.setPxid(pxglDto.getPxid());
			List<PxqxszDto> dtoList = pxqxszService.getDtoList(pxqxszDto);
			mav.addObject("bmList",dtoList);
			FjcfbDto qrCode = new FjcfbDto();
			qrCode.setYwlx(BusTypeEnum.IMP_TRAIN_QRCODE.getCode());
			qrCode.setYwid(pxglDto.getPxid());
			List<FjcfbDto> qrCodes = fjcfbService.getDtoList(qrCode);
			mav.addObject("qrCodes",qrCodes);
			gzglDto.setPxid(pxglDto.getPxid());
			List<GzglDto> dtoTrainSignInPeo = gzglService.getDtoTrainSignInPeo(gzglDto);
			mav.addObject("dtoTrainSignInPeo",dtoTrainSignInPeo);
		}else if(StringUtil.isNotBlank(grksglDto.getGrksid())){
			mav = new ModelAndView("train/test/test_view");
			mav.addObject("flag","1");
			GrksglDto dto = grksglService.getDto(grksglDto);
			GrksmxDto grksmxDto=new GrksmxDto();
			grksmxDto.setGrksid(grksglDto.getGrksid());
			List<GrksmxDto> list = grksmxService.getListByGrksid(grksmxDto);
			List<GrksmxDto> jdlist=new ArrayList<>();
			List<GrksmxDto> xzlist=new ArrayList<>();
			for(GrksmxDto dto_t:list){
				if("GAP".equals(dto_t.getTmlx())||"EXPLAIN".equals(dto_t.getTmlx())){
					jdlist.add(dto_t);
				}else{
					xzlist.add(dto_t);
				}
			}
			mav.addObject("urlPrefix", urlPrefix);
			mav.addObject("grksglDto",dto);
			mav.addObject("jdlist",jdlist);
			mav.addObject("xzlist",xzlist);
		}
		return mav;
	}


	/**
	 * 选择用户表页面
	 */
	@RequestMapping("/user/pagedataListUserForChoose")
	public ModelAndView listUserForChoose(PxglDto pxglDto) {
		ModelAndView mav = new ModelAndView("train/train/list_UserForChoose");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("pxglDto", pxglDto);
		return mav;
	}

	/**
	 * 系统用户列表
	 */
	@RequestMapping(value="/user/pagedataGetListUser")
	@ResponseBody
	public Map<String,Object> getUserList(PxglDto pxglDto){
		Map<String, Object> map= new HashMap<>();
		List<User> yhlist=pxglService.getPagedDtoListXtyh(pxglDto);
		map.put("total", pxglDto.getTotalNumber());
		map.put("rows", yhlist);
		return map;
	}
	@RequestMapping(value = "/user/pagedataGetXtyhLitByYhms")
	@ResponseBody
	public Map<String, Object> pagedataGetXtyhLitByYhms(PxglDto pxglDto){
		pxglDto.setYhmorzsxms(Arrays.asList(pxglDto.getYhmorzsxms_str().split(",")));
		List<User> xtyhDtos = pxglService.getListByYhms(pxglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("xtyhDtos",xtyhDtos);
		return map;
	}
	/**
	 * 类别获取子类别
	 */
	@RequestMapping(value="/pagedataSubcategory")
	@ResponseBody
	public List<JcsjDto> getSubcategory(JcsjDto jcsjDto){
		List<JcsjDto> dtos = redisUtil.hmgetDto("matridx_jcsj:SUBCLASSTRAINCATEGORY");
		List<JcsjDto> pxzlblist = new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtos)){
			for(JcsjDto dto:dtos){
				if(StringUtil.isNotBlank(dto.getFcsid())&&dto.getFcsid().equals(jcsjDto.getFcsid())){
					pxzlblist.add(dto);
				}
			}
		}
		return pxzlblist;
	}
	/**
	 * 类别获取子类别
	 */
	@RequestMapping(value="/minidataGetSubcategory")
	@ResponseBody
	public List<JcsjDto> minidataGetSubcategory(JcsjDto jcsjDto){
		return getSubcategory(jcsjDto);
	}

	/**
	 * 题库列表页面
	 */
	@RequestMapping("/question/pageListQuestion")
	public ModelAndView pageListQuestion(){
		ModelAndView mav = new ModelAndView("train/question/question_list");
		mav.addObject("ssgslist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 题库列表
	 */
	@RequestMapping("/question/pageGetListQuestion")
	@ResponseBody
	public Map<String,Object> listQuestion(TkglDto tkglDto){
		List<TkglDto> list = tkglService.getPagedDtoList(tkglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", tkglDto.getTotalNumber());
		result.put("rows", list);
		return result;
	}

	/**
	 * 题库列表查看
	 */
	@RequestMapping("/question/viewQuestion")
	public ModelAndView viewQuestion(TkglDto tkglDto){
		ModelAndView mav = new ModelAndView("train/question/question_view");
		TkglDto dto = tkglService.getDto(tkglDto);
		mav.addObject("tkglDto",dto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 题目列表
	 */
	@RequestMapping("/question/pagedataQuestionList")
	@ResponseBody
	public Map<String,Object> getQuestionList(KsglDto ksglDto){
		Map<String,Object> result = new HashMap<>();
		List<KsglDto> list=new ArrayList<>();
		int total = 0;
		if(StringUtil.isNotBlank(ksglDto.getTkid())){
			list = ksglService.getListQuestions(ksglDto);
			KsglDto ksglDto_t = new KsglDto();
			ksglDto_t.setTkid(ksglDto.getTkid());
			List<KsglDto> listQuestions = ksglService.getListQuestions(ksglDto_t);
			total = listQuestions.size();
		}
		result.put("total", total);
		result.put("rows", list);
		return result;
	}
	/**
	 * 题目列表
	 */
	@RequestMapping("/question/pagedataQuestionListForMod")
	@ResponseBody
	public Map<String,Object> pagedataQuestionListForMod(KsglDto ksglDto){
		Map<String,Object> result = new HashMap<>();
		List<KsglDto> list=new ArrayList<>();
		int total = 0;
			if(StringUtil.isNotBlank(ksglDto.getTkid())){
			KsglDto ksglDto_t = new KsglDto();
			ksglDto_t.setTkid(ksglDto.getTkid());
			list = ksglService.getListQuestions(ksglDto_t);
			total = ksglDto_t.getTotalNumber();
		}
			result.put("total", total);
			result.put("rows", list);
			return result;
	}

	/**
	 * 获取题数
	 */
	@RequestMapping("/question/pagedataCountInfo")
	@ResponseBody
	public Map<String,Object> getCountInfo(KsglDto ksglDto){
		Map<String,Object> result = new HashMap<>();
		int count = ksglService.getCount(ksglDto);
		result.put("count",count);
		return result;
	}

	/**
	 * 选题列表
	 */
	@RequestMapping("/question/pagedataListChoose")
	@ResponseBody
	public Map<String,Object> getChooseList(PxtkglbDto pxtkglbDto){
		Map<String,Object> result = new HashMap<>();
		List<PxtkglbDto> list =new ArrayList<>();
		if(StringUtil.isNotBlank(pxtkglbDto.getPxid())){
			list = pxtkglbService.getDtoList(pxtkglbDto);
			for(PxtkglbDto dto:list){
				KsglDto ksglDto=new KsglDto();
				ksglDto.setTmlx(dto.getTmlx());
				ksglDto.setTkid(dto.getTkid());
				int count = ksglService.getCount(ksglDto);
				dto.setTkmc(dto.getTkid());
				dto.setZsl(String.valueOf(count));
			}
		}
		result.put("rows", list);
		return result;
	}


	/**
	 * 查看选项
	 */
	@RequestMapping("/question/pagedataViewOptions")
	public ModelAndView viewOptions(KsglDto ksglDto){
		ModelAndView mav = new ModelAndView("train/question/option_view");
		List<KsglDto> list = ksglService.getDtoList(ksglDto);
		mav.addObject("list",list);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 新增题库
	 */
	@RequestMapping("/question/addQuestion")
	public ModelAndView addQuestion() {
		ModelAndView mav = new ModelAndView("train/question/question_edit");
		TkglDto dto=new TkglDto();
		mav.addObject("tkglDto",dto);
		mav.addObject("tklxlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.QUESTIONBANK_TYPE.getCode()));
		mav.addObject("ssgslist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));
		mav.addObject("formAction", "/train/question/addSaveQuestion");
		mav.addObject("url", "/train/question/pagedataQuestionListForMod?access_token=");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 新增保存
	 */
	@RequestMapping("/question/addSaveQuestion")
	@ResponseBody
	public Map<String,Object> addSaveQuestion(TkglDto tkglDto,HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		User user = getLoginInfo(request);
		TkglDto dto = tkglService.getDto(tkglDto);
		if(dto!=null){
			result.put("status", "fail");
			result.put("message","名称为  "+ tkglDto.getTkmc()+ "  的题库已经存在");
		}else{
			tkglDto.setTkid(StringUtil.generateUUID());
			tkglDto.setLrry(user.getYhid());
			boolean isSuccess = tkglService.insert(tkglDto);
			result.put("status", isSuccess?"success":"fail");
			result.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			List<KsglDto> tmmxlist= JSON.parseArray(tkglDto.getTmmx_json(), KsglDto.class);
			if(!CollectionUtils.isEmpty(tmmxlist)){
				for(int i=0;i<tmmxlist.size();i++){
					tmmxlist.get(i).setTkid(tkglDto.getTkid());
					tmmxlist.get(i).setKsid(StringUtil.generateUUID());
					tmmxlist.get(i).setXh(String.valueOf(i+1));
					ksglService.insert(tmmxlist.get(i));
					if(StringUtil.isNotBlank(tmmxlist.get(i).getXxA())){
						KsmxDto ksmxDto=new KsmxDto();
						ksmxDto.setKsid(tmmxlist.get(i).getKsid());
						ksmxDto.setXxdm("A");
						ksmxDto.setXxnr(tmmxlist.get(i).getXxA());
						ksmxDto.setLrry(user.getYhid());
						ksmxDto.setKsmxid(StringUtil.generateUUID());
						ksmxService.insert(ksmxDto);
					}
					if(StringUtil.isNotBlank(tmmxlist.get(i).getXxB())){
						KsmxDto ksmxDto=new KsmxDto();
						ksmxDto.setKsid(tmmxlist.get(i).getKsid());
						ksmxDto.setXxdm("B");
						ksmxDto.setXxnr(tmmxlist.get(i).getXxB());
						ksmxDto.setLrry(user.getYhid());
						ksmxDto.setKsmxid(StringUtil.generateUUID());
						ksmxService.insert(ksmxDto);
					}
					if(StringUtil.isNotBlank(tmmxlist.get(i).getXxC())){
						KsmxDto ksmxDto=new KsmxDto();
						ksmxDto.setKsid(tmmxlist.get(i).getKsid());
						ksmxDto.setXxdm("C");
						ksmxDto.setXxnr(tmmxlist.get(i).getXxC());
						ksmxDto.setLrry(user.getYhid());
						ksmxDto.setKsmxid(StringUtil.generateUUID());
						ksmxService.insert(ksmxDto);
					}
					if(StringUtil.isNotBlank(tmmxlist.get(i).getXxD())){
						KsmxDto ksmxDto=new KsmxDto();
						ksmxDto.setKsid(tmmxlist.get(i).getKsid());
						ksmxDto.setXxdm("D");
						ksmxDto.setXxnr(tmmxlist.get(i).getXxD());
						ksmxDto.setLrry(user.getYhid());
						ksmxDto.setKsmxid(StringUtil.generateUUID());
						ksmxService.insert(ksmxDto);
					}
					if(StringUtil.isNotBlank(tmmxlist.get(i).getXxE())){
						KsmxDto ksmxDto=new KsmxDto();
						ksmxDto.setKsid(tmmxlist.get(i).getKsid());
						ksmxDto.setXxdm("E");
						ksmxDto.setXxnr(tmmxlist.get(i).getXxE());
						ksmxDto.setLrry(user.getYhid());
						ksmxDto.setKsmxid(StringUtil.generateUUID());
						ksmxService.insert(ksmxDto);
					}
					if(StringUtil.isNotBlank(tmmxlist.get(i).getXxF())){
						KsmxDto ksmxDto=new KsmxDto();
						ksmxDto.setKsid(tmmxlist.get(i).getKsid());
						ksmxDto.setXxdm("F");
						ksmxDto.setXxnr(tmmxlist.get(i).getXxF());
						ksmxDto.setLrry(user.getYhid());
						ksmxDto.setKsmxid(StringUtil.generateUUID());
						ksmxService.insert(ksmxDto);
					}
					if(StringUtil.isNotBlank(tmmxlist.get(i).getXxG())){
						KsmxDto ksmxDto=new KsmxDto();
						ksmxDto.setKsid(tmmxlist.get(i).getKsid());
						ksmxDto.setXxdm("G");
						ksmxDto.setXxnr(tmmxlist.get(i).getXxG());
						ksmxDto.setLrry(user.getYhid());
						ksmxDto.setKsmxid(StringUtil.generateUUID());
						ksmxService.insert(ksmxDto);
					}
					if(StringUtil.isNotBlank(tmmxlist.get(i).getXxH())){
						KsmxDto ksmxDto=new KsmxDto();
						ksmxDto.setKsid(tmmxlist.get(i).getKsid());
						ksmxDto.setXxdm("H");
						ksmxDto.setXxnr(tmmxlist.get(i).getXxH());
						ksmxDto.setLrry(user.getYhid());
						ksmxDto.setKsmxid(StringUtil.generateUUID());
						ksmxService.insert(ksmxDto);
					}
				}
			}
		}

		return result;
	}

	/**
	 * 修改题库
	 */
	@RequestMapping("/question/modQuestion")
	public ModelAndView modQuestion(TkglDto tkglDto) {
		ModelAndView mav = new ModelAndView("train/question/question_edit");
		TkglDto dto = tkglService.getDto(tkglDto);
		mav.addObject("tkglDto",dto);
		mav.addObject("tklxlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.QUESTIONBANK_TYPE.getCode()));
		mav.addObject("ssgslist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));
		mav.addObject("formAction", "/train/question/modSaveQuestion");
		mav.addObject("url", "/train/question/pagedataQuestionListForMod?tkid="+tkglDto.getTkid()+"&access_token=");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 修改保存
	 */
	@RequestMapping("/question/modSaveQuestion")
	@ResponseBody
	public Map<String,Object> modSaveQuestion(TkglDto tkglDto,HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		User user = getLoginInfo(request);
		tkglDto.setXgry(user.getYhid());
		boolean isSuccess;
		try {
			isSuccess = tkglService.updateTK(tkglDto);
			result.put("status", isSuccess?"success":"fail");
			result.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}catch (BusinessException e) {
			result.put("status", "fail");
			result.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("message", xxglService.getModelById("ICOM00002").getXxnr());
		}
		return result;
	}

	/**
	 * 删除题库
	 */
	@RequestMapping("/question/delQuestion")
	@ResponseBody
	public Map<String,Object> delQuestion(TkglDto tkglDto,HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		User user = getLoginInfo(request);
		tkglDto.setScry(user.getYhid());
		boolean isSuccess = tkglService.delete(tkglDto);
		result.put("status", isSuccess?"success":"fail");
		result.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return result;
	}

	/**
	 * 更新简答题得分
	 */
	@RequestMapping("/test/pagedataUpdateDf")
	@ResponseBody
	public Map<String,Object> updateDf(GrksmxDto grksmxDto,HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		User user = getLoginInfo(request);
		grksmxDto.setXgry(user.getYhid());
		grksmxService.updateDto(grksmxDto);
		int zf=0;
		List<GrksmxDto> list = grksmxService.getListByGrksid(grksmxDto);
		for(GrksmxDto dto:list){
			zf+=Integer.parseInt(dto.getDf());
		}
		GrksglDto grksglDto=new GrksglDto();
		grksglDto.setXgry(user.getYhid());
		grksglDto.setGrksid(grksmxDto.getGrksid());
		grksglDto.setZf(String.valueOf(zf));
		grksglService.update(grksglDto);
		result.put("zf", zf);
		return result;
	}

	/**
	 * 机构设置页面
	 */
	@RequestMapping(value = "/test/departmentSetTrain")
	public ModelAndView setUnit(PxglDto pxglDto){
		ModelAndView mav = new ModelAndView("train/train/train_departmentSet");
		mav.addObject("pxglDto", pxglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取机构列表（未选择）
	 */
	@RequestMapping(value="/train/pagedataGetListUnSelectJg")
	@ResponseBody
	public Map<String,Object> unSelectJgList(PxqxszDto pxqxszDto){
		List<PxqxszDto> otherjgxx=pxqxszService.getPagedUnSelectJgList(pxqxszDto);
		Map<String,Object> map = new HashMap<>();
		map.put("total", pxqxszDto.getTotalNumber());
		map.put("rows", otherjgxx);
		return map;
	}

	/**
	 * 获取机构列表(已选择)
	 */
	@RequestMapping(value="/train/pagedataGetListSelectedJg")
	@ResponseBody
	public Map<String,Object> pagedataGetListSelectedJg(PxqxszDto pxqxszDto){
		Map<String,Object> map = new HashMap<>();
		List<String> ids = pxqxszDto.getIds();
		List<PxqxszDto> otherjgxx=new ArrayList<>();
		if(ids.size()==1){
			otherjgxx=pxqxszService.getPagedSelectedJgList(pxqxszDto);
		}
		map.put("total", pxqxszDto.getTotalNumber());
		map.put("rows", otherjgxx);
		return map;
	}

	/**
	 * 添加机构
	 */
	@RequestMapping(value ="/train/pagedataSelectedJg")
	@ResponseBody
	public Map<String,Object> toSelectedJg(PxqxszDto pxqxszDto){
		try{
			Map<String,Object> map = new HashMap<>();
			List<PxqxszDto> list=new ArrayList<>();
			List<String> ids = pxqxszDto.getIds();
			if(!CollectionUtils.isEmpty(ids)){
				if(StringUtil.isNotBlank(pxqxszDto.getJgid())){
					for(String s:ids){
						PxqxszDto pxqxszDto_t=new PxqxszDto();
						pxqxszDto_t.setPxqxid(StringUtil.generateUUID());
						pxqxszDto_t.setJgid(pxqxszDto.getJgid());
						pxqxszDto_t.setPxid(s);
						list.add(pxqxszDto_t);
					}
				}
				List<String> jgids = pxqxszDto.getJgids();
				if(!CollectionUtils.isEmpty(jgids)){
					for(String s:ids){
						for(String jgid:jgids){
							PxqxszDto pxqxszDto_t=new PxqxszDto();
							pxqxszDto_t.setPxqxid(StringUtil.generateUUID());
							pxqxszDto_t.setJgid(jgid);
							pxqxszDto_t.setPxid(s);
							list.add(pxqxszDto_t);
						}
					}
				}
				boolean result = pxqxszService.insertList(list);
				map.put("status", result?"success":"fail");
				map.put("message", result?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
            }else{
				map.put("status", "fail");
				map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
            }
            return map;

        }catch(Exception e){
			Map<String,Object> map = new HashMap<>();
			map.put("status", "fail");
			map.put("message", e.getMessage());
			return map;
		}
	}

	/**
	 * 去除机构
	 */
	@RequestMapping(value="/train/pagedataToOptionalJg")
	@ResponseBody
	public Map<String,Object> toOptionalJg(PxqxszDto pxqxszDto){
		try{
			Map<String,Object> map = new HashMap<>();
			List<String> ids = pxqxszDto.getIds();
			if(!CollectionUtils.isEmpty(ids)){
				boolean result = pxqxszService.delete(pxqxszDto);
				map.put("status", result?"success":"fail");
				map.put("message", result?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
            }else{
				map.put("status", "fail");
				map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
            }
            return map;
        }catch(Exception e){
			Map<String,Object> map = new HashMap<>();
			map.put("status", "fail");
			map.put("message", e.getMessage());
			return map;
		}
	}
	/**
	 * 个人考试未完成列表页面
	 */
	@RequestMapping("/test/pageListIncomplete")
	public ModelAndView pageListIncomplete(){
		ModelAndView mav = new ModelAndView("train/test/inCompleteTest_list");
		mav.addObject("pxlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINCATEGORY.getCode()));//培训类别
		mav.addObject("pxzlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SUBCLASSTRAINCATEGORY.getCode()));//培训子类别
		mav.addObject("ssgslist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));//所属公司
		mav.addObject("urlPrefix", urlPrefix);

		return mav;
	}

	/**
	 * 个人考试列表
	 */
	@RequestMapping("/test/pageGetListIncomplete")
	@ResponseBody
	public Map<String,Object> pageGetListIncomplete(GrksglDto grksglDto){
		List<GrksglDto> list = grksglService.getPagedIncomplete(grksglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", grksglDto.getTotalNumber());
		result.put("rows", list);
		return result;
	}
	/**
	 * 个人考试未完成列表页面查看
	 */
	@RequestMapping("/test/viewIncomplete")
	public ModelAndView viewIncomplete(GrksglDto grksglDto){
		ModelAndView mav = new ModelAndView("train/test/inCompleteTest_view");
		grksglDto=grksglService.getDtoByPxidAndFzr(grksglDto);
		mav.addObject("grksglDto", grksglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 个人考试未完成列表考试明细
	 */
	@RequestMapping("/test/pagedataIncomplete")
	@ResponseBody
	public Map<String,Object> pagedataIncomplete(GrksglDto grksglDto){
		List<GrksglDto> list = grksglService.getDtoList(grksglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("rows", list);
		return result;
	}
	/**
	 * 个人考试列表设置提醒
	 */
	@RequestMapping("/train/remindTrain")
	@ResponseBody
	public Map<String,Object> remindInComplete(GrksglDto grksglDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess=grksglService.remindInComplete(grksglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM99029").getXxnr() : xxglService.getModelById("ICOM99030").getXxnr());
		return map;
	}

	/**
	 * 	审核列表
	 */
	@RequestMapping("/train/pageListTrainAudit")
	public ModelAndView pageListTrainAudit() {
		ModelAndView mav = new  ModelAndView("train/train/train_auditList");
		mav.addObject("pxlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINCATEGORY.getCode()));//培训类别
		mav.addObject("pxzlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SUBCLASSTRAINCATEGORY.getCode()));//培训子类别
		mav.addObject("ssgslist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));//所属公司
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 	审核列表
	 */
	@RequestMapping("/train/pageGetListTrainAudit")
	@ResponseBody
	public Map<String, Object> pageGetListTrainAudit(PxglDto pxglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(pxglDto);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(pxglDto.getDqshzt())) {
			DataPermission.add(pxglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "pxgl", "pxid",
					AuditTypeEnum.AUDIT_TRAIN);
		} else {
			DataPermission.add(pxglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "pxgl", "pxid",
					AuditTypeEnum.AUDIT_TRAIN);
		}
		DataPermission.addCurrentUser(pxglDto, getLoginInfo(request));
		List<PxglDto> listMap = pxglService.getPagedAuditTrain(pxglDto);
		map.put("total", pxglDto.getTotalNumber());
		map.put("rows", listMap);
		return map;
	}

	/**
	 * 培训统计
	 */
	@RequestMapping("/train/pageStatisticsTrain")
	public ModelAndView pageStatisticsTrain(){
		ModelAndView mav = new ModelAndView("train/train/train_stats");
		List<PxglDto> yearGroup = pxglService.getYearGroup();
		mav.addObject("yearlist", yearGroup);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 培训统计（钉钉接口）
	 */
	@RequestMapping("/train/pagedataStatisticsInter")
	@ResponseBody
	public Map<String,Object> pagedataStatisticsInter(){
		Map<String,Object> map = new HashMap<>();
		List<PxglDto> yearGroup = pxglService.getYearGroup();
		map.put("yearlist", yearGroup);
		return map;
	}

	/**
	 * 培训统计详情
	 */
	@RequestMapping("/train/pagedataTrainStatsInfo")
	@ResponseBody
	public Map<String,Object> pagedataTrainStatsInfo(PxglDto pxglDto){
		Map<String,Object> map = new HashMap<>();
		List<PxglDto> typeStatis = pxglService.getTypeStatis();
		List<PxglDto> countStatis = pxglService.getCountStatis(pxglDto);
		map.put("pxlb",typeStatis);
		map.put("pxyf",countStatis);
		return map;
	}

	/**
	 * 部门分发按钮
	 */
	@RequestMapping("/test/departDistributeTrain")
	public ModelAndView departDistributeTrain(PxglDto pxglDto){
		ModelAndView mav = new ModelAndView("train/train/train_distribute");
		FjcfbDto fjcfbDto = new FjcfbDto();
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("pxglDto",pxglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("flag", "departDistribute");
		return mav;
	}
	/**
	 * 培训统计学习数量
	 */
	@RequestMapping("/train/minidataGetTrainStaus")
	@ResponseBody
	public Map<String,Object> minidataGetTrainStaus(PxglDto pxglDto){
		Map<String,Object> map = new HashMap<>();
		pxglDto.setZt("00");
		Integer wwcsl = pxglService.getTrainStausCount(pxglDto);
		DsrwszDto dsrwszDto = new DsrwszDto();
		dsrwszDto.setZxl("pxglServiceImpl");
		dsrwszDto.setZxl("remindDueTasks");
		dsrwszDto = dsrwszService.getDsrwByZxlAndZxff(dsrwszDto);
		if (dsrwszDto!=null){
			String cs = dsrwszDto.getCs();
			String[] split = cs.split("=");
			pxglDto.setGqsj(split[1]);
		}else {
			pxglDto.setGqsj("3");
		}
		Integer gqsl = pxglService.getTrainStausCount(pxglDto);
		List<String> gqmcs = pxglService.getTrainGqPxBts(pxglDto);
		List<Map<String,Object>> wcwwcslmap = pxglService.getTrainStausGroup(pxglDto);
		List<Map<String,Object>> groupLbs = pxglService.getTrainGroupLb(pxglDto);
		map.put("groupLbs",groupLbs);
		map.put("wcwwcsl",wcwwcslmap);
		map.put("wwcsl",wwcsl);
		map.put("gqsl",gqsl);
		map.put("gqmcs",gqmcs);
		return map;
	}
	/**
	 * 个人红包列表页面
	 */
	@RequestMapping("/test/pageListRedPacket")
	public ModelAndView pageListRedPacket(){
		ModelAndView mav = new ModelAndView("train/redpacket/perRedPacket_list");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 个人红包列表
	 */
	@RequestMapping("/test/pageGetListRedPacket")
	@ResponseBody
	public Map<String,Object> pageGetListRedPacket(GrksglDto grksglDto){
		List<GrksglDto> list = grksglService.getPagedRedPacket(grksglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", grksglDto.getTotalNumber());
		result.put("rows", list);
		return result;
	}
	/**
	 * 钉钉个人红包列表
	 */
	@RequestMapping("/test/minidataGetListRedPacket")
	@ResponseBody
	public Map<String,Object> minidataGetListRedPacket(GrksglDto grksglDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		grksglDto.setRyid(user.getYhid());
		List<GrksglDto> list = grksglService.getPagedRedPacket(grksglDto);
		Double sum = grksglService.getRedPacketSum(grksglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", grksglDto.getTotalNumber());
		result.put("rows", list);
		result.put("sum", String.valueOf(sum));
		return result;
	}
	/**
	 * 个人红包列表查看页面
	 */
	@RequestMapping("/test/viewRedPacket")
	public ModelAndView viewRedPacket(GrksglDto grksglDto){
		ModelAndView mav = new ModelAndView("train/redpacket/perRedPacket_view");
		grksglDto=grksglService.getDtoRedPacketById(grksglDto.getGrksid());
		mav.addObject("grksglDto",grksglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 钉钉个人红包列表查看页面
	 */
	@RequestMapping("/test/minidataViewRedPacket")
	public ModelAndView minidataViewRedPacket(GrksglDto grksglDto){
		ModelAndView mav = new ModelAndView("train/redpacket/perRedPacket_view");
		grksglDto=grksglService.getDtoRedPacketById(grksglDto.getGrksid());
		mav.addObject("grksglDto",grksglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 更新是否发放字段
	 */
	@RequestMapping(value = "/test/releaseRedPacket")
	@ResponseBody
	public Map<String, Object> releaseRedPacket(GrksglDto grksglDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		grksglDto.setXgry(user.getYhid());
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = grksglService.updateRedPacket(grksglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 点击测试按钮处理信息
	 */
	@RequestMapping(value = "/test/minidataDealTrainInfo")
	@ResponseBody
	public Map<String, Object> minidataDealTrainInfo(PxglDto pxglDto,HttpServletRequest request){
		log.error("---------------------minidataDealTrainInfo参数-pxid：{} gzid:{}", pxglDto.getPxid(),pxglDto.getGzid());
		//获取用户信息
		User user = getLoginInfo(request);
		Map<String,Object> map = new HashMap<>();
		Object o = redisUtil.hget(RedisCommonKeyEnum.TRAIN_TRAINGZID.getKey() + pxglDto.getPxid(),user.getYhid());
		log.error("---------------------minidataDealTrainInfo-TRAINGZID_："+ o);
		GzglDto gzglDto_t;
		if (o!=null){
			gzglDto_t = JSON.parseObject(String.valueOf(o), GzglDto.class);
		}else {
			GzglDto gzglDto = new GzglDto();
			if(StringUtil.isNotBlank(pxglDto.getGzid())){
				gzglDto.setGzid(pxglDto.getGzid());
			}else{
				//红包是没有工作id的
				gzglDto.setFzr(user.getYhid());
				gzglDto.setYwid(pxglDto.getPxid());
			}
			gzglDto_t=gzglService.getGzInfoByFzrAndYwid(gzglDto);
		}
		//获取工作id
		map.put("gzglDto",gzglDto_t);
		if (gzglDto_t!=null) {
			if (StringUtil.isNotBlank(gzglDto_t.getGzid())) {
				pxglDto.setGzid(gzglDto_t.getGzid());
				PxglDto pxglDto_t;
				Object px = redisUtil.get(RedisCommonKeyEnum.TRAIN_TRAINPXINFO.getKey() + pxglDto.getPxid());
				log.error("---------------------minidataDealTrainInfo-TRAINPXINFO_：" + px);
				if (px != null) {
					pxglDto_t = JSON.parseObject(String.valueOf(px), PxglDto.class);
				} else {
					pxglDto_t = pxglService.viewTrainTask(pxglDto);
				}
				if (pxglDto_t != null) {
					pxglDto_t.setGzid(gzglDto_t.getGzid());
					GrksglDto grksglDto_pxInfo = null;
					Object userPxInfo = redisUtil.hget(RedisCommonKeyEnum.TRAIN_USERPXINFO.getKey() + pxglDto_t.getPxid(),user.getYhid());
					log.error("---------------------minidataDealTrainInfo-USERPXINFO_：" + userPxInfo);
					if (userPxInfo!=null) {
						GrksglDto grksglDto_info = JSON.parseObject(String.valueOf(userPxInfo), GrksglDto.class);
						pxglDto_t.setKscs(grksglDto_info.getKscs());
						pxglDto_t.setTgbj(grksglDto_info.getTgbj());
						grksglDto_pxInfo = grksglDto_info;
					}else {
						if (StringUtil.isNotBlank(pxglDto_t.getKscs())) {
							int kscs = Integer.parseInt(pxglDto_t.getKscs());
							GrksglDto grksglDto = new GrksglDto();
							grksglDto.setGzid(gzglDto_t.getGzid());
							List<GrksglDto> grksglDtos = grksglService.getPersonalTests(grksglDto);
							int testCount = grksglDtos.size();
							pxglDto_t.setKscs(String.valueOf(kscs - testCount));
						}
						GzglDto gzglDto_tg = gzglService.getGzDtoByGzid(gzglDto_t.getGzid());
						pxglDto_t.setTgbj(gzglDto_tg.getTgbj());
					}
					//题目信息
					map.put("pxglDto", pxglDto_t);
					if (StringUtil.isNotBlank(pxglDto_t.getHbszid())) {
						GrksglDto grksglDto = new GrksglDto();
						grksglDto.setPxid(pxglDto_t.getPxid());
						grksglDto.setRyid(gzglDto_t.getFzr());
						GrksglDto grksglDto_n = null;
						if (grksglDto_pxInfo!=null){
							if (StringUtil.isNotBlank(grksglDto_pxInfo.getJe())){
								//已经领取过红包
								map.put("isHave",true);
								return map;
							}else {
								if (StringUtil.isNotBlank(grksglDto_pxInfo.getZf())&&StringUtil.isNotBlank(grksglDto_pxInfo.getGrksid())){
									//是否有红包但没领的
									grksglDto_n = grksglDto_pxInfo;
								}
							}
						}else {
							GrksglDto grksglDto_t = grksglService.getSfHaveRedpacket(grksglDto);
							//已经领取过红包
							if (grksglDto_t!=null){
								map.put("isHave",true);
								return map;
							}
							//是否有红包但没领的
							grksglDto_n = grksglService.getSfHaveRedpacketNo(grksglDto);
						}
						log.error("---------------------minidataDealTrainInfo-grksglDto_n：" + JSON.toJSONString(grksglDto_n));
						if (grksglDto_n != null) {
							map.put("isHaveNo", true);
							Object hbszmxs = redisUtil.get(RedisCommonKeyEnum.TRAIN_HBSZMX.getKey() + pxglDto_t.getHbszid());
							log.error("---------------------minidataDealTrainInfo-HBSZMX_：" + hbszmxs);
							List<HbszmxDto> hbszmxDtos;
							if (hbszmxs!=null){
								hbszmxDtos = JSON.parseArray(String.valueOf(hbszmxs), HbszmxDto.class);
								if (CollectionUtils.isEmpty(hbszmxDtos)){
									map.put("result", false);
									map.put("sysl", -1);
									return map;
								}
								Iterator<HbszmxDto> iterator = hbszmxDtos.iterator();
								while (iterator.hasNext()){
									HbszmxDto next = iterator.next();
									BigDecimal zdf=new BigDecimal(next.getZdf());
									BigDecimal zf=new BigDecimal(grksglDto_n.getZf());
									// -1小于，0相等，1大于
									if (zdf.compareTo(zf) > 0){
										iterator.remove();
									}
								}
							}else {
								HbszmxDto hbszmxDto_t = new HbszmxDto();
								hbszmxDto_t.setHbszid(pxglDto_t.getHbszid());
								hbszmxDto_t.setDf(grksglDto_n.getZf());
								hbszmxDtos = hbszmxService.getDtoList(hbszmxDto_t);
								log.error("---------------------minidataDealTrainInfo-hbszmxDtos：" + JSON.toJSONString(hbszmxDtos));
							}
							//您这次没有获得红包，下次再接再厉！
							if (CollectionUtils.isEmpty(hbszmxDtos)) {
								map.put("result", false);
								map.put("sysl", -1);
								return map;
							}
							int zsl = 0;
							for (HbszmxDto hbszmxDto : hbszmxDtos) {
								int sysl = Integer.parseInt(StringUtil.isBlank(hbszmxDto.getSysl()) ? "0" : hbszmxDto.getSysl());
								sysl = Math.max(sysl, 0);
								zsl = zsl + sysl;
							}
							//很遗憾，红包已经被抢完了！
							if (zsl <= 0) {
								map.put("result", false);
								map.put("sysl", 0);
								return map;
							}
							//显示红包页面
							map.put("df", grksglDto_n.getZf());
							map.put("grksid", grksglDto_n.getGrksid());
							map.put("result", true);
							map.put("sysl", 1);
							return map;
						}
						map.put("isHave", false);
						map.put("isHaveNo", false);
					}
				} else {
					log.error("---------------------没有找到相关培训信息");
				}
			} else {
				log.error("---------------------没有找到相关工作信息");
			}
		}else {
			log.error("---------------------没有找到相关工作信息");
		}
		return map;
	}
	
	/**
	 * 手动清除培训缓存
	 */
	@RequestMapping("/train/clearRedis")
	@ResponseBody
	public Map<String,Object> clearRedis(){
		//清除培训缓存
		redisUtil.delLike(RedisCommonKeyEnum.TRAIN_USERPXINFO.getKey());
		redisUtil.delLike(RedisCommonKeyEnum.TRAIN_TRAINGZID.getKey());
		redisUtil.delLike(RedisCommonKeyEnum.TRAIN_TRAINPXINFO.getKey());
		redisUtil.delLike(RedisCommonKeyEnum.TRAIN_HBSZMX.getKey());
		redisUtil.delLike(RedisCommonKeyEnum.TRAIN_GRKSMX.getKey());
		redisUtil.delLike(RedisCommonKeyEnum.TRAIN_PXTKTMLX.getKey());
		redisUtil.delLike(RedisCommonKeyEnum.TRAIN_KSMX.getKey());
		redisUtil.delLike(RedisCommonKeyEnum.TRAIN_KSGL.getKey());
		redisUtil.delLike(RedisCommonKeyEnum.TRAIN_GRKSGL.getKey());
		redisUtil.del(RedisCommonKeyEnum.TRAIN_ALLHBINFOPM.getKey());
		Map<String,Object> result = new HashMap<>();
		result.put("status", "success");
		result.put("message", xxglService.getModelById("ICOM00003").getXxnr());
		return result;
	}
	/**
	 * 培训提醒
	 */
	@RequestMapping("/test/remindTrain")
	@ResponseBody
	public Map<String,Object> remindTrain(GrksglDto grksglDto) {
		Map<String,Object> result = new HashMap<>();
		boolean isSuccess=grksglService.remandTrain(grksglDto);
		result.put("status", isSuccess ?"success":"fail");
		result.put("message", isSuccess ?"提醒成功":"提醒失败");
		return result;
	}
	/**
	 * 获取关联文件
	 */
	@RequestMapping("/train/pagedataGetGlWj")
	@ResponseBody
	public Map<String, Object> pagedataGetGlWj(PxglxxDto pxglxxDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(pxglxxDto.getPxid())){
			pxglxxDto.setGllx("0");
			List<PxglxxDto> pxglxxDtos = pxglxxService.getDtoList(pxglxxDto);
			if(!CollectionUtils.isEmpty(pxglxxDtos)){
				List<String> ids = pxglxxDtos.stream().map(PxglxxDto::getGlid).distinct().collect(Collectors.toList());
				RestTemplate restTemplate = new RestTemplate();
				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
				paramMap.add("ids",StringUtil.join(ids,","));
				paramMap.add("access_token",pxglxxDto.getAccess_token());
				@SuppressWarnings("unchecked")
				Map<String,Object> result = restTemplate.postForObject(applicationurl+pxglxxDto.getFwqz()+"/production/document/pagedataGetDocumentDetails", paramMap, Map.class);
				map.put("rows", result != null ? result.get("wjglDtos") :  new ArrayList<>());
			}else {
				map.put("rows", new ArrayList<>());
			}
		}else {
			map.put("rows", new ArrayList<>());
		}
		return map;
	}
	/**
	 * 文件关联培训信息
	 */
	@RequestMapping("/train/pagedataGetTrainByWj")
	@ResponseBody
	public Map<String,Object> pagedataGetTrainByWj(PxglxxDto pxglxxDto){
		Map<String,Object> result = new HashMap<>();
		List<PxglxxDto> pxglxxDtos = pxglxxService.getTrainByWj(pxglxxDto);
		result.put("rows", pxglxxDtos);
		return result;
	}

	/**
	 * 更新记录编号
	 */
	@RequestMapping(value = "/train/pagedataUpdateJlbh")
	@ResponseBody
	public Map<String, Object> pagedataUpdateJlbh(GzglDto gzglDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		//获取用户信息
		User user = getLoginInfo(request);
		gzglDto.setXgry(user.getYhid());
		boolean isSuccess = gzglService.update(gzglDto);
		map.put("status", isSuccess?"success":"fail");
		return map;
	}
	/**
	 * 培训生成签到码
	 */
	@RequestMapping("/test/generatesignincodeTrain")
	public ModelAndView generatesignincodeTrain(PxglDto pxglDto){
		ModelAndView mav = new ModelAndView("train/train/train_generatesignincode");
		mav.addObject("pxglDto", pxglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 获取签到人员
	 */
	@RequestMapping("/train/pagedataGetQdyh")
	@ResponseBody
	public Map<String, Object> pagedataGetQdyh(GzglDto gzglDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(gzglDto.getPxid())){
			List<GzglDto> gzglDtos = gzglService.getDtoTrainSignInPeo(gzglDto);
			map.put("rows", gzglDtos);
		}else {
			map.put("rows", new ArrayList<>());
		}
		return map;
	}
	/**
	 * 培训生成签到码保存
	 */
	@RequestMapping("/test/generatesignincodeSaveTrain")
	@ResponseBody
	public Map<String, Object> generatesignincodeSaveTrain(PxglDto pxglDto,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		pxglDto.setLrry(user.getYhid());
		pxglDto.setWbcxid(user.getWbcxid());
		boolean isSuccess= false;
		try {
			isSuccess = pxglService.generatesignincodeSaveTrain(pxglDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 钉钉签到
	 */
	@RequestMapping("/test/minidataSignInTrainForDD")
	@ResponseBody
	public Map<String, Object> signInTrainForDD(PxglDto pxglDto,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		pxglDto.setLrry(getLoginInfo(request).getYhid());
		boolean isSuccess= false;
		try {
			isSuccess = pxglService.signInTrainForDD(pxglDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("signInResult", isSuccess?"签到成功":"签到失败");
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("signInResult", StringUtil.isNotBlank(e.getMsg())?e.getMsg():"签到失败");
		}
		map.put("signInTime",DateUtils.getCustomFomratCurrentDate("yyyy.MM.dd HH:mm:ss"));
		return map;
	}
	/**
	 * 培训结果页面
	 */
	@RequestMapping("/test/modresultTrain")
	public ModelAndView modresultTrain(PxglDto pxglDto){
		ModelAndView mav = new ModelAndView("train/train/train_modresult");
		GzglDto gzglDto = new GzglDto();
		gzglDto.setPxid(pxglDto.getPxid());
		List<GzglDto> gzglDtos = gzglService.getDtoTrainSignInPeo(gzglDto);
		mav.addObject("pxglDto", pxglDto);
		mav.addObject("gzglDtos", gzglDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 培训结果保存
	 */
	@RequestMapping("/test/modresultSaveTrain")
	@ResponseBody
	public Map<String, Object> modresultSaveTrain(PxglDto pxglDto,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		pxglDto.setXgry(getLoginInfo(request).getYhid());
		boolean isSuccess = pxglService.modresultSaveTrain(pxglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 生成试卷
	 */
	@RequestMapping("/test/generatetestpapersTrain")
	public ModelAndView generatetestpapersTrain(){
		ModelAndView mav = new ModelAndView("train/train/train_generatetestpapers");
		TkglDto tkglDto=new TkglDto();
		List<TkglDto> tklist = tkglService.getDtoList(tkglDto);
		mav.addObject("tklist",JSON.toJSONString(tklist));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 生成试卷
	 */
	@RequestMapping("/test/pagedataViewTestPapersTrain")
	public ModelAndView pagedataViewTestPapersTrain(PxglDto pxglDto){
		ModelAndView mav = new ModelAndView("train/train/train_view_generatetestpapers");
		List<KsglDto> tmmxlist= JSON.parseArray(pxglDto.getTmmx_json(), KsglDto.class);
		Map<String, String> map = new HashMap<>();
		map.put("SELECT","单选题");
		map.put("MULTIPLE","多选题");
		map.put("JUDGE","判断题");
		map.put("GAP","填空题");
		map.put("EXPLAIN","简答题");
		Map<String, List<KsglDto>> testPapers = new LinkedHashMap<>();
		List<String> ksids = new ArrayList<>();
		for (KsglDto ksglDto : tmmxlist) {
			List<KsglDto> ksglDtos = ksglService.getQuestions(ksglDto);
			testPapers.put(ksglDto.getTkid()+"_"+map.get(ksglDto.getTmlx())+"_"+ksglDto.getSl()+"_"+ksglDto.getFz()+"_"+ksglDto.getZf(),ksglDtos);
			ksids.addAll(ksglDtos.stream().map(KsglDto::getKsid).collect(Collectors.toList()));
		}
		KsmxDto ksmxDto = new KsmxDto();
		ksmxDto.setIds(ksids);
		List<KsmxDto> ksmxDtos = ksmxService.getDtoList(ksmxDto);
		//将明细放入题目
		int index = 1;
		for (String tmxx : testPapers.keySet()) {
			List<KsglDto> ksglDtos = testPapers.get(tmxx);
			for (KsglDto ksglDto : ksglDtos) {
				ksglDto.setXh(index+"");
				index++;
				ksglDto.setKsmxDtos(new ArrayList<>());
				for (KsmxDto dto : ksmxDtos) {
					if (ksglDto.getKsid().equals(dto.getKsid())){
						ksglDto.getKsmxDtos().add(dto);
					}
				}
			}
		}
		mav.addObject("testPapers", testPapers);
		return mav;
	}
	/**
	 * 培训提醒统计
	 */
	@RequestMapping("/train/minidataGetTrainRemindStatis")
	@ResponseBody
	public Map<String, Object> minidataGetTrainRemindStatis(PxglDto pxglDto) {
		return pxglService.getTrainRemindStatis(pxglDto);
	}
}
