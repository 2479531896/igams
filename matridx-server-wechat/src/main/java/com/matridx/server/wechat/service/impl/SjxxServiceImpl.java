package com.matridx.server.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JcsjModel;
import com.matridx.igams.common.dao.post.IJcsjDao;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.BusinessTypeEnum;
import com.matridx.igams.common.enums.DetectionTypeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.server.wechat.dao.entities.*;
import com.matridx.server.wechat.dao.post.ISjxxDao;
import com.matridx.server.wechat.enums.MQWechatTypeEnum;
import com.matridx.server.wechat.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.xml.BasicXmlReader;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SjxxServiceImpl extends BaseBasicServiceImpl<SjxxDto, SjxxModel, ISjxxDao> implements ISjxxService{

	@Autowired
	ISjkjxxService sjkjxxService;

	@Autowired
	ISjysxxService sjysxxService;

	@Autowired
	ISjdwxxService sjdwxxService;

	@Autowired
	ISjjcxmService sjjcxmService;

	@Autowired
	ISjlczzService sjlczzService;

	@Autowired
	ISjgzbyService sjgzbyService;

	@Autowired
	ISjqqjcService sjqqjcService;

	@Autowired
	IFjcfbService fjcfbService;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ISjhbxxService sjhbxxService;
	@Autowired
	IHbsfbzService hbsfbzService;

	@Autowired
	IJcsjService jcsjService;

	@Autowired
	IXxglService xxglService;

	@Autowired
	IWxyhService wxyhService;

	@Autowired
	ISjqxService sjqxService;

	@Autowired
	IHbdwqxService hbdwqxService;

	@Autowired
	ICommonService commonService;
	@Autowired
	IPayinfoService payinfoService;

	@Autowired
	private RedisUtil redisUtil;

    @Autowired
    private ISjsyglService sjsyglService;
	@Autowired
	private IXmsyglService xmsyglService;

	@Autowired(required=false)
	private AmqpTemplate amqpTempl;

	@Value("${matridx.wechat.companyurl:}")
	private String companyurl;

	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;

	private Logger log = LoggerFactory.getLogger(SjxxServiceImpl.class);

	@Autowired
	private IJcsjDao jcsjDao;

	/**
	 * 插入送检信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(SjxxDto sjxxDto){
		sjxxDto.setSjid(StringUtil.generateUUID());
		int result = dao.insert(sjxxDto);
		if(result == 0)
			return false;
		return true;
	}

	/**
	 * 新增送检信息（患者页面）
	 * @param sjxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSaveReport(SjxxDto sjxxDto) throws BusinessException{
		// TODO Auto-generated method stub
		SjxxDto dto;
		if(StringUtil.isNotBlank(sjxxDto.getSjid())){
			SjxxDto sjxxDto_search = new SjxxDto();
			sjxxDto_search.setSjid(sjxxDto.getSjid());
			dto = dao.getDto(sjxxDto_search);
		} else {
			dto = dao.getDto(sjxxDto);
		}
		if (dto != null){
			if(StringUtil.isNotBlank(sjxxDto.getXgsj())&&!sjxxDto.getXgsj().equals(dto.getXgsj())){
				log.error("该送检信息已被修改，请刷新页面后重新操作！");
				throw new BusinessException("","该送检信息已被修改，请刷新页面后重新操作！");
			}
		}
		List<SjjcxmDto> t_sjjcxms=(List<SjjcxmDto>) JSON.parseArray(sjxxDto.getJcxm(),SjjcxmDto.class);
		Map<String,List<String>> map=new HashMap<>();
		if(StringUtil.isNotBlank(sjxxDto.getJczxm())){
			List<String> jczxmList=Arrays.asList(sjxxDto.getJczxm().split(","));
			map.put("jczxmList",jczxmList);
		}else{
			List<String> collect = t_sjjcxms.stream().filter(item -> StringUtil.isNotBlank(item.getJczxmid())).map(item -> item.getJczxmid()).collect(Collectors.toList());
			sjxxDto.setJczxm(String.join(",", collect));
			if (CollectionUtils.isEmpty(collect)){
				map.put("jczxmList",null);
			} else {
				map.put("jczxmList",collect);
			}
		}
		map.put("jcxmList",sjxxDto.getJcxmids());
		List<JcsjDto> jcsjDtoList=jcsjDao.getJcxmCsmcByCsid(map);
		String jcxmmcs=jcsjDtoList.stream().map(JcsjModel::getCsmc).collect(Collectors.joining(","));
		sjxxDto.setJcxmmc(jcxmmcs);
		sjxxDto.setLrry(sjxxDto.getWxid());
		sjxxDto.setXgry(sjxxDto.getWxid());
		sjxxDto.setScry(sjxxDto.getWxid());
		//只在微信端录入修改时根据科研项目判断判断并更新是否收费，86端不进行处理（86端可能会对是否收费进行申请维护，85端只有在未接收时才可进行修改）
		if (StringUtil.isNotBlank(sjxxDto.getKyxm())){
			JcsjDto kyxmDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.RESEARCH_PROJECT, sjxxDto.getKyxm());
			if (StringUtil.isNotBlank(kyxmDto.getCskz2())){
				sjxxDto.setSfsf(kyxmDto.getCskz2());
				if (t_sjjcxms!=null&&!CollectionUtils.isEmpty(t_sjjcxms)){
					for(SjjcxmDto sjjcxmDto:t_sjjcxms){
						sjjcxmDto.setSfsf(kyxmDto.getCskz2());
					}
				}
			}
		}
		//新增送检信息，需要根据送检区分的扩展参数先设置是否收费信息，然后再进行金额的初始化
		if(StringUtil.isBlank(sjxxDto.getSjid())&&StringUtil.isNotBlank(sjxxDto.getSjqf())){
			JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION, sjxxDto.getSjqf());
			if (StringUtil.isNotBlank(jcsjDto.getCskz2())){
				sjxxDto.setSfsf(jcsjDto.getCskz2());
				if (t_sjjcxms!=null&&!CollectionUtils.isEmpty(t_sjjcxms)){
					for(SjjcxmDto sjjcxmDto:t_sjjcxms){
						sjjcxmDto.setSfsf(jcsjDto.getCskz2());
					}
				}
			}
		}
		//根据伙伴设置应付金额，用于后期的应收款功能
		if (StringUtil.isNotBlank(sjxxDto.getDb())){
			// 若收费且金额为空时，获取默认金额
			if (!"0".equals(sjxxDto.getSfsf()))
			{
				//初始化应付金额和 总体的付款等金额信息。dto用于判断送检伙伴是否改变
				initHbsfxx(sjxxDto,t_sjjcxms);
			}
		}
		//重新放回修改后的json信息
		sjxxDto.setJcxm(JSON.toJSONString(t_sjjcxms));
		//新增送检信息
		if(StringUtil.isBlank(sjxxDto.getSjid())){
			if(StringUtil.isBlank(sjxxDto.getYbbh()))
				throw new BusinessException("","未输入标本编码！");
			//确认标本是否已经存在
			SjxxDto t_sjxxDto = new SjxxDto();
			t_sjxxDto.setYbbh(sjxxDto.getYbbh());
			t_sjxxDto.setWxid(sjxxDto.getWxid());
			t_sjxxDto = dao.getDto(t_sjxxDto);
			if(t_sjxxDto!=null)
				throw new BusinessException("","重复使用标本编号，请在新增页面重新扫码！");
			if ("true".equals(sjxxDto.getStatus())){
				sjxxDto.setZt("80");
				sjxxDto.setScbj("0");
			}
			//第一页新增、暂存时，直接置scbj为2
			else {
				sjxxDto.setScbj("2");
			}
			boolean result = insertDto(sjxxDto);
			if(!result) {
				log.error("送检信息保存未成功！");
				throw new BusinessException("","送检信息保存未成功！");
			}
			//新增送检检测项目
			result = sjjcxmService.insertBySjxx(sjxxDto);
			if(!result)
				throw new BusinessException("","送检检测项目信息保存未成功！");

			//新增关注病原
			sjgzbyService.insertBySjxx(sjxxDto);
			// 为发送消息而重新读取所有送检信息
			getAllSjxxOther(sjxxDto);
			// 发送消息
			RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.INSP_UPD.getCode() + JSONObject.toJSONString(sjxxDto));
		}else{
			//确认标本编号是否重复
			List<SjxxDto> sjxxDtos = dao.isYbbhRepeat(sjxxDto);
			if(sjxxDtos != null && sjxxDtos.size() > 0) {
				log.error("修改信息标本编号重复！");
				throw new BusinessException("","重复使用标本编号，请更改标本编号或重新扫码！");
			}
			//SjxxDto sjxxDto_x=dao.getDtoById(sjxxDto.getSjid());
			if ("true".equals(sjxxDto.getStatus())){
				sjxxDto.setZt("80");
				sjxxDto.setScbj("0");
			}
			else {
				String zt = dto.getZt();
				if (StatusEnum.CHECK_PASS.getCode().equals(zt)) {
					sjxxDto.setScbj("0");
				} else {
					sjxxDto.setScbj("2");
				}
			}

			if (StringUtil.isBlank(dto.getJsrq())){
				//若样本未被接收，更新送检检测项目和送检实验管理表数据，
				//若样本已被接收，不更新送检检测项目和送检实验管理表数据
				//修改送检检测项目
				boolean isSuccess = sjjcxmService.insertBySjxx(sjxxDto);
				if(!isSuccess)
					throw new BusinessException("","送检检测项目信息更新未成功！");
				
				int result = dao.update(sjxxDto);
				if(result == 0)
					throw new BusinessException("","送检信息更新未成功！");
				
				//重新从数据库里获取更改过信息的送检数据
				SjxxDto sjxxDto_t=new SjxxDto();
				sjxxDto_t.setSjid(sjxxDto.getSjid());
				sjxxDto_t= dao.getDto(sjxxDto_t);
				
				//更新项目实验数据和送检实验数据
				if(StringUtil.isBlank(sjxxDto_t.getJsrq()))
					addOrUpdateSyData(sjxxDto_t, sjxxDto);
			}else{
				int result = dao.update(sjxxDto);
				if(result == 0)
					throw new BusinessException("","送检信息更新未成功！");
			}
			//修改关注病原
			sjgzbyService.insertBySjxx(sjxxDto);
			SjxxDto t_sjxxDto = dao.getDtoById(sjxxDto.getSjid());
			// 为发送消息而重新读取所有送检信息
			getAllSjxxOther(t_sjxxDto);
			// 发送消息
			RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.INSP_UPD.getCode() + JSONObject.toJSONString(t_sjxxDto));
		}
		if(StringUtil.isNotBlank(sjxxDto.getWxid())) {
			SjkjxxDto sjkjxxDto = sjkjxxService.getDtoById(sjxxDto.getWxid());
			if(sjkjxxDto != null){
				boolean result = sjkjxxService.deleteById(sjxxDto.getWxid());
				if(!result)
					throw new BusinessException("","送检快捷信息更新未成功！");
			}
			//新增送检快捷信息
			boolean result = sjkjxxService.inserBySjxxDto(sjxxDto);
			if(!result)
				throw new BusinessException("","送检快捷信息更新未成功！");
		}
		return true;
	}
	
	/**
	 * 根据伙伴信息设置相应的应付金额和总体的付款金额
	 * @param sjxxDto
	 * @param t_sjjcxms
	 * @return
	 */
	private boolean initHbsfxx(SjxxDto sjxxDto,List<SjjcxmDto> t_sjjcxms) {

		//获取伙伴的相应项目的收费标准信息,并设置到检测项目中和sjxx里
		//考虑修改的时候，可能修改了项目，也可能修改了伙伴名称，所以每次进入需要重新设置
		HbsfbzDto hbsfbzDto=new HbsfbzDto();
		hbsfbzDto.setHbmc(sjxxDto.getDb());
		//获取伙伴的收费标准
		List<HbsfbzDto> hbsfbzDtos = hbsfbzService.getDtoList(hbsfbzDto);
		BigDecimal fkje = new BigDecimal("0");
		if(hbsfbzDtos!=null&&hbsfbzDtos.size()>0 && t_sjjcxms!=null && t_sjjcxms.size() > 0){
			for(SjjcxmDto sjjcxmDto:t_sjjcxms) {
				for(HbsfbzDto hbsfbzDto_t:hbsfbzDtos){
					if(sjjcxmDto.getJcxmid().equals(hbsfbzDto_t.getXm())){
						if(StringUtil.isNotBlank(sjjcxmDto.getJczxmid())){
							if(sjjcxmDto.getJczxmid().equals(hbsfbzDto_t.getZxm())){
								sjjcxmDto.setYfje(hbsfbzDto_t.getSfbz());
								fkje = fkje.add(new BigDecimal(hbsfbzDto_t.getSfbz()));
								break;
							}
						}else{
							sjjcxmDto.setYfje(hbsfbzDto_t.getSfbz());
							fkje = fkje.add(new BigDecimal(hbsfbzDto_t.getSfbz()));
							break;
						}
					}
				}
			}
			sjxxDto.setFkje(fkje.toString());
		}
		return true;
	}

	/**
	 * 点击上一步是保存现有信息
	 * @param sjxxDto
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSaveConsentBack(SjxxDto sjxxDto) {
		return addSaveConsent(sjxxDto,null);
	}

	/**
	 * 点击完成时保存现有信息，同时发布消息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSaveConsentComp(SjxxDto sjxxDto) {
		// 是否收费不为'是'时，判断是否为第三方
		/*if(!"1".equals(sjxxDto.getSfsf())) {
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
			SjxxDto t_sjxxDto = dao.getDtoById(sjxxDto.getSjid());
			paramMap.add("db", t_sjxxDto.getDb());
			DBEncrypt crypt = new DBEncrypt();
			String url = crypt.dCode(companyurl);
			@SuppressWarnings("unchecked")
			Map<String,String> map = restTemplate.postForObject(url+"/ws/dingtalk/isThirdParty", paramMap, Map.class);
			if(map != null && StringUtil.isNotBlank(map.get("flcskz2"))) {
				sjxxDto.setSfsf("1");
			}
		}*/
		SjxxDto sjxxDto_x=dao.getDtoById(sjxxDto.getSjid());
		String zt = StringUtil.isNotBlank(sjxxDto.getZt())?sjxxDto.getZt():sjxxDto_x.getZt();
		if(StatusEnum.CHECK_PASS.getCode().equals(zt)) {
			sjxxDto.setScbj("0");
		}else {
			sjxxDto.setScbj("2");
		}
		boolean isSuccess = addSaveConsent(sjxxDto,sjxxDto_x.getXgsj());
		if(!isSuccess)
			return isSuccess;

		//为发送消息而重新读取所有送检信息
		sjxxDto_x.setSjpdid(sjxxDto.getSjpdid());
		sjxxDto_x.setScbj(sjxxDto.getScbj());
		sjxxDto_x.setYblx(sjxxDto.getYblx());
		sjxxDto_x.setYblxmc(sjxxDto.getYblxmc());
		sjxxDto_x.setYbtj(sjxxDto.getYbtj());
		sjxxDto_x.setSjrq(sjxxDto.getSjrq());
		sjxxDto_x.setCyrq(sjxxDto.getCyrq());
		sjxxDto_x.setLczz(sjxxDto.getLczz());
		sjxxDto_x.setKpsq(sjxxDto.getKpsq());
		sjxxDto_x.setQqjc(sjxxDto.getQqjc());
		sjxxDto_x.setJqyy(sjxxDto.getJqyy());
		sjxxDto_x.setBz(sjxxDto.getBz());
		sjxxDto_x.setSfws(sjxxDto.getSfws());
		sjxxDto_x.setQqzd(sjxxDto.getQqzd());
		sjxxDto_x.setZt(sjxxDto.getZt());
		//为发送消息而重新读取所有送检信息
		getAllSjxxOther(sjxxDto_x);

		log.error("新增送检信息完成，send rabbit！");
		//发送消息
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(),  RabbitEnum.INSP_UPD.getCode() + JSONObject.toJSONString(sjxxDto_x));
		log.error("新增送检信息完成，send rabbit comp！");
		if (!StringUtil.isNotBlank(sjxxDto_x.getJsrq())){
			//更新项目实验数据和送检实验数据
			addOrUpdateSyData(sjxxDto_x,sjxxDto);
		}

		return true;
	}
	
	/**
	 * 根据页面传递的送检实验数据，结合原有的数据，原本就存在的检测类型数据，则进行更新，没有的数据则进行新增
	 * 涉及 项目实验管理表，送检实验管理表
	 * @param sjxxDto_x 修改前的送检信息
	 * @param sjxxDto 页面传递过来修改后的送检信息
	 * @return
	 */
	private boolean addOrUpdateSyData(SjxxDto sjxxDto_x,SjxxDto sjxxDto) {
		//为防止未设置id，删除数据太多
		if( sjxxDto==null || StringUtil.isBlank(sjxxDto.getSjid()))
			return false;
		
		//若样本未被接收，更新送检检测项目和送检实验管理表数据，
		//若样本已被接收，不更新送检检测项目和送检实验管理表数据
		String yblxdm="";
		String jcdwmc="";
		String sjqfdm="";
		if(StringUtil.isNotBlank(sjxxDto_x.getYblx())){
			JcsjDto yblxJcsj = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode(), sjxxDto_x.getYblx());
			if(yblxJcsj!=null){
				yblxdm = yblxJcsj.getCsdm();
			}
			//判断如果样本类型选择其他时，并且其他标本类型内容为"全血"或者"血浆"时，标本类型传"B"
			// 20240904 "其它"样本类型的csdm由XXX改为G
			if("XXX".equals(yblxdm) || "G".equals(yblxdm)){
				if(StringUtil.isNotBlank(sjxxDto_x.getYblxmc())){
					if("全血".equals(sjxxDto_x.getYblxmc())||"血浆".equals(sjxxDto_x.getYblxmc())){
						yblxdm="B";
					}
				}
			}
		}

		if(StringUtil.isNotBlank(sjxxDto_x.getJcdwmc())){
			jcdwmc=sjxxDto_x.getJcdwmc();
		}

		if(StringUtil.isNotBlank(sjxxDto_x.getSjqf())){
			JcsjDto sjqfJcsj = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode(), sjxxDto_x.getSjqf());
			if(sjqfJcsj!=null){
				sjqfdm = sjqfJcsj.getCsdm();
			}
		}
		if(StringUtil.isNotBlank(sjxxDto_x.getNbbm())){
			yblxdm=sjxxDto_x.getNbbm().substring(sjxxDto_x.getNbbm().length()-1);
		}
		sjxxDto_x.setYblxdm(yblxdm);
		sjxxDto_x.setJcdwmc(jcdwmc);
		sjxxDto_x.setSjqf(sjqfdm);
		if(StringUtil.isBlank(sjxxDto_x.getSjid()))
			return false;
		List<SjsyglDto> insertInfo =sjsyglService.getDetectionInfo(sjxxDto_x,null,DetectionTypeEnum.DETECT_SJ.getCode());
		XmsyglDto xmsyglDto_t = new XmsyglDto();
		xmsyglDto_t.setYwid(sjxxDto.getSjid());
		xmsyglDto_t.setScry(sjxxDto.getLrry());
		//为防止已有的 sjsygl 数据未处理（因为更改类型或者项目造成数据没有关联出来的情况)需要取一下当前ywid的实验数据
		SjsyglDto sjsyglDto_t=new SjsyglDto();
		sjsyglDto_t.setYwid(sjxxDto.getSjid());
		List<SjsyglModel> beforSy_list = sjsyglService.getModelList(sjsyglDto_t);

		xmsyglService.deleteInfo(xmsyglDto_t);
		xmsyglService.delInfo(xmsyglDto_t);
		if (!CollectionUtils.isEmpty(insertInfo)) {
			//获取到删除之前正常的送检实验数据  和 公用方法返回的list
			List<SjsyglDto> updateList = new ArrayList<>();
			List<String> strings = new ArrayList<>();
			List<XmsyglDto> insertDtos = new ArrayList<>();
			List<SjsyglDto> insertList = new ArrayList<>();
			List<String> ids = new ArrayList<>();
			//如果检测单位和主表修改后的一致 并且 是新增的项目 为了后面根据检测单位，检测类型分类设置 所以给他改为修改前主表的检测单位
			for (SjsyglDto sjsyglDto : insertInfo) {
				if (!sjsyglDto.getJcdw().equals(sjxxDto.getJcdw()) && StringUtil.isBlank(sjsyglDto.getSyglid()) && StringUtil.isNotBlank(sjxxDto.getJcdw())) {
					sjsyglDto.setJcdw(sjxxDto.getJcdw());
				}else if(StringUtil.isBlank(sjsyglDto.getJcdw())) {
					sjsyglDto.setJcdw(sjxxDto.getJcdw());
				}
				if(StringUtil.isNotBlank(sjsyglDto.getSyglid()))
                    ids.add(sjsyglDto.getSyglid());
			}
			if(beforSy_list != null && beforSy_list.size() > 0) {
				for(SjsyglModel sjsy:beforSy_list) {
					boolean isFindsy = false;
					for(String id:ids) {
						if(id.equals(sjsy.getSyglid())) {
							isFindsy = true;
							break;
						}
					}
					if(!isFindsy) {
						ids.add(sjsy.getSyglid());
					}
				}
			}
			
			sjsyglDto_t = new SjsyglDto();
			sjsyglDto_t.setIds(ids);
            //先保存原有数据
            List<SjsyglModel> befor_list = new ArrayList<>();
            if(ids.size() > 0) {
            	befor_list = sjsyglService.getModelList(sjsyglDto_t);
            }
			
			sjsyglDto_t.setSjid(sjxxDto.getSjid());
			//sjsyglDto_t.setLx(DetectionTypeEnum.DETECT_SJ.getCode());
			sjsyglDto_t.setScry(sjxxDto.getWxid());
			if(StringUtil.isNotBlank(sjsyglDto_t.getSjid()))
				sjsyglService.deleteInfo(sjsyglDto_t);
			if(ids.size() > 0) {
				sjsyglService.delInfo(sjsyglDto_t);
			}
			//list 通过检测单位分组
			Map<String, List<SjsyglDto>> map = insertInfo.stream().collect(Collectors.groupingBy(SjsyglDto::getJcdw));
			if (!CollectionUtils.isEmpty(map)) {
				Iterator<Map.Entry<String, List<SjsyglDto>>> entries = map.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry<String, List<SjsyglDto>> entry = entries.next();
					List<SjsyglDto> resultModelList = entry.getValue();
					String jcdw = entry.getKey();
					if (StringUtil.isNotBlank(jcdw) && !CollectionUtils.isEmpty(resultModelList)) {
						//在通过检测类型分组
						Map<String, List<SjsyglDto>> listMap = resultModelList.stream().collect(Collectors.groupingBy(SjsyglDto::getJclxid));
						if (!CollectionUtils.isEmpty(listMap)) {
							Iterator<Map.Entry<String, List<SjsyglDto>>> entryIterator = listMap.entrySet().iterator();
							while (entryIterator.hasNext()) {
								Map.Entry<String, List<SjsyglDto>> stringListEntry = entryIterator.next();
								String jclx = stringListEntry.getKey();
								List<SjsyglDto> sjsyglDtoList = stringListEntry.getValue();
								if (StringUtil.isNotBlank(jclx) && !CollectionUtils.isEmpty(sjsyglDtoList)) {
									sjsyglDtoList = sjsyglDtoList.stream().sorted(Comparator.comparing(SjsyglDto::getPx)).collect(Collectors.toList());
									Boolean flag = true;
									//用修改之前的实验数据和修改后的对比如果检测单位和检测类型都相等则不用新增，是修改
									for (SjsyglModel sjsyglModel : befor_list) {
										if (jcdw.equals(sjsyglModel.getJcdw()) && jclx.equals(sjsyglModel.getJclxid())) {
											SjsyglDto dto_t = new SjsyglDto();
											dto_t.setSyglid(sjsyglModel.getSyglid());
											dto_t.setScbj("0");
											dto_t.setXgry(sjxxDto.getWxid());
											if (!sjsyglModel.getJcdw().equals(sjxxDto.getJcdw())) {
												dto_t.setJcdw(sjxxDto.getJcdw());
											}
											String jcxmmc = "";
											String wksxbm = "";
											for (SjsyglDto sjsyglDto : sjsyglDtoList) {
												//判断项目实验明细是否存在  存在修改 不存在新增
												if (jcxmmc.indexOf(sjsyglDto.getJcxmbm()) == -1) {
													jcxmmc += "," + sjsyglDto.getJcxmbm();
													wksxbm += "," + sjsyglDto.getWksxbm();
												}
                                                //同一个项目但不同的对应ID（检测单位和标本类型不一样所造成），如果检测类型xmsygl表的数据应该重新创建
                                                if(sjsyglDto.getYwid().equals(sjxxDto.getSjid())) {
													if (StringUtil.isNotBlank(sjsyglDto.getXmsyglid())) {
														strings.add(sjsyglDto.getXmsyglid());
													} else {
														XmsyglDto xmsyglDto = new XmsyglDto();
														xmsyglDto.setXmsyglid(StringUtil.generateUUID());
														xmsyglDto.setSyglid(sjsyglModel.getSyglid());
														xmsyglDto.setLrry(sjxxDto.getWxid());
														xmsyglDto.setWkdm(sjsyglDto.getNbzbm());
														xmsyglDto.setDyid(sjsyglDto.getDyid());
														xmsyglDto.setYwlx(DetectionTypeEnum.DETECT_SJ.getCode());
														xmsyglDto.setJcxmid(sjsyglDto.getJcxmid());
														xmsyglDto.setJczxmid(sjsyglDto.getJczxmid());
														xmsyglDto.setYwid(sjsyglDto.getSjid());
														insertDtos.add(xmsyglDto);
													}
                                                }
											}
											dto_t.setDyid(sjsyglDtoList.get(0).getDyid());
											dto_t.setJcxmid(sjsyglDtoList.get(0).getJcxmid());
											dto_t.setJczxmid(sjsyglDtoList.get(0).getJczxmid());
											dto_t.setNbzbm(sjsyglDtoList.get(0).getNbzbm());
											dto_t.setXmmc(jcxmmc.length() > 0?jcxmmc.substring(1):"");
											dto_t.setWksxbm(wksxbm.length() > 0?wksxbm.substring(1):"");
											dto_t.setYwid(sjsyglDtoList.get(0).getYwid());
											updateList.add(dto_t);
											flag = false;
											break;
										}
									}
									if (flag) {
										String syglid = StringUtil.generateUUID();
										sjsyglDtoList.get(0).setSyglid(syglid);
										sjsyglDtoList.get(0).setYwlx(DetectionTypeEnum.DETECT_SJ.getCode());
										String jcxmmc = "";
										String wksxbm = "";
										for (SjsyglDto sjsyglDto : sjsyglDtoList) {
											if (jcxmmc.indexOf(sjsyglDto.getJcxmbm()) == -1) {
												jcxmmc += "," + sjsyglDto.getJcxmbm();
												wksxbm += ","+sjsyglDto.getWksxbm();
											}
											if(sjsyglDto.getYwid().equals(sjxxDto.getSjid())) {
												XmsyglDto xmsyglDto = new XmsyglDto();
												xmsyglDto.setXmsyglid(StringUtil.generateUUID());
												xmsyglDto.setSyglid(syglid);
												xmsyglDto.setWkdm(sjsyglDto.getNbzbm());
												xmsyglDto.setDyid(sjsyglDto.getDyid());
												xmsyglDto.setYwlx(DetectionTypeEnum.DETECT_SJ.getCode());
												xmsyglDto.setJcxmid(sjsyglDto.getJcxmid());
												xmsyglDto.setJczxmid(sjsyglDto.getJczxmid());
												xmsyglDto.setYwid(sjsyglDto.getSjid());
												xmsyglDto.setLrry(sjxxDto.getWxid());
												insertDtos.add(xmsyglDto);
											}
										}
										if (jcdw.equals(sjxxDto.getJcdw())) {
											sjsyglDtoList.get(0).setJcdw(sjxxDto.getJcdw());
											if ("1".equals(sjxxDto.getSfjs())) {
												sjsyglDtoList.get(0).setSfjs(sjxxDto.getSfjs());
												sjsyglDtoList.get(0).setJsrq(sjxxDto.getJsrq());
												sjsyglDtoList.get(0).setJsry(sjxxDto.getJsry());
											}
										}
										sjsyglDtoList.get(0).setYwid(sjxxDto.getSjid());
										sjsyglDtoList.get(0).setXmmc(jcxmmc.length() > 0?jcxmmc.substring(1):"");
										sjsyglDtoList.get(0).setWksxbm(wksxbm.length() > 0?wksxbm.substring(1):"");
										insertList.add(sjsyglDtoList.get(0));
									}
								}
							}
						}
					}
				}
			}
			if (!CollectionUtils.isEmpty(insertList))
				sjsyglService.insertList(insertList);
			if (!CollectionUtils.isEmpty(updateList)) {
				sjsyglService.modAllList(updateList);
			}
			if (!CollectionUtils.isEmpty(strings)) {
				XmsyglDto xmsyglDto = new XmsyglDto();
				xmsyglDto.setIds(strings);
				xmsyglDto.setXgry(sjxxDto.getWxid());
				xmsyglService.modToNormal(xmsyglDto);
			}
			if (!CollectionUtils.isEmpty(insertDtos))
				xmsyglService.insertList(insertDtos);
			
			sjsyglDto_t = new SjsyglDto();
			sjsyglDto_t.setSjid(sjxxDto.getSjid());

			List<SjsyglModel> list = sjsyglService.getModelList(sjsyglDto_t);
			list = list.stream()
					.collect(Collectors.toMap(
							SjsyglModel::getSyglid,
							Function.identity(),
							(existing, replacement) -> existing))
					.values()
					.stream()
					.collect(Collectors.toList());//去重
			RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.SJSY_MOD.getCode() + JSONObject.toJSONString(list));
			List<XmsyglModel> dtos = xmsyglService.getModelList(xmsyglDto_t);
			RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.XMSY_MOD.getCode() + JSONObject.toJSONString(dtos));
		}
		return true;
	}

	/**
	 * 根据送检ID获取其他相应的附属列表
	 * @param sjxxDto
	 */
	private void getAllSjxxOther(SjxxDto sjxxDto) {

		if(sjxxDto == null)
			return;

		//获取送检项目清单
		SjjcxmDto jcxmDto = new SjjcxmDto();
		jcxmDto.setSjid(sjxxDto.getSjid());
		List<SjjcxmDto> sjjcxmDtos = sjjcxmService.getDtoList(jcxmDto);
		sjxxDto.setSjjcxms(sjjcxmDtos);

		//获取送检症状清单
		SjlczzDto lczzDto = new SjlczzDto();
		lczzDto.setSjid(sjxxDto.getSjid());
		List<SjlczzDto> sjlczzDtos = sjlczzService.getDtoList(lczzDto);
		sjxxDto.setSjlczzs(sjlczzDtos);

		//获取送检前期检测清单
		SjqqjcDto sjqqjcDto = new SjqqjcDto();
		sjqqjcDto.setSjid(sjxxDto.getSjid());
		List<SjqqjcDto> sjqqjcDtos = sjqqjcService.getDtoList(sjqqjcDto);
		sjxxDto.setSjqqjcs(sjqqjcDtos);

		//获取送检关注病原清单
		SjgzbyDto sjgzbyDto = new SjgzbyDto();
		sjgzbyDto.setSjid(sjxxDto.getSjid());
		List<SjgzbyDto> sjgzbyDtos = sjgzbyService.getDtoList(sjgzbyDto);
		sjxxDto.setSjgzbys(sjgzbyDtos);

		//获取送检附件IDs
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		if(fjcfbDtos != null && fjcfbDtos.size() > 0){
			List<String> fjids = new ArrayList<>();
			for (int i = 0; i < fjcfbDtos.size(); i++) {
				fjids.add(fjcfbDtos.get(i).getFjid());
			}
			sjxxDto.setFjids(fjids);
		}
	}

	/**
	 * 新增送检信息（知情同意书）
	 * @param sjxxDto
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	private boolean addSaveConsent(SjxxDto sjxxDto,String xgsj) {
		// TODO Auto-generated method stub
		//根据送检ID修改信息
		if (StringUtil.isBlank(xgsj)){
			SjxxDto dto = dao.getDto(sjxxDto);
			if (dto != null){
				xgsj = dto.getXgsj();
			}
		}
		String openTime = sjxxDto.getXgsj();
		if (StringUtil.isNotBlank(xgsj) && StringUtil.isNotBlank(openTime) && !xgsj.equals(openTime)){
			return false;
		}
		boolean result = dao.updateSecMiniSjxx(sjxxDto);
		if(!result)
			return false;
		//新增送检临床症状
		boolean isSuccess = sjlczzService.insertBySjxx(sjxxDto);
		if(!isSuccess)
			return false;

		//新增送检前期检测
		sjqqjcService.insertBySjxx(sjxxDto);
		return true;
	}


	/**
	 * 根据送检标本编号查询送检信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public SjxxDto getDto(SjxxDto sjxxDto){
		if(StringUtil.isBlank(sjxxDto.getSjid()) && StringUtil.isBlank(sjxxDto.getYbbh()) && StringUtil.isBlank(sjxxDto.getNbbm()))
			return null;
		//获取送检信息
		SjxxDto resSjxxDto = dao.getDto(sjxxDto);
		if (resSjxxDto==null){
			log.error("未获取到对应送检信息！");
			return null;
		}

		if (resSjxxDto.getQtks() != null && resSjxxDto.getQtks() != "")
		{
			resSjxxDto.setKsmc(resSjxxDto.getKsmc() + "-" + resSjxxDto.getQtks());
		}
		if(resSjxxDto!=null) {

			getAllSjxxOther(resSjxxDto);

			StringBuffer sb = new StringBuffer("");
			StringBuffer sk = new StringBuffer("");
			if(resSjxxDto.getSjjcxms()!=null) {
				sb.setLength(0);
				sk.setLength(0);
				List<SjjcxmDto> sjjcxmDtos = resSjxxDto.getSjjcxms();
				for(int i =0;i<sjjcxmDtos.size();i++) {
					if(i!=0) {
						sb.append(",");
						sk.append(",");
					}
					sk.append(sjjcxmDtos.get(i).getCskz1());
					sb.append(sjjcxmDtos.get(i).getCsmc());
				}
				resSjxxDto.setJcxmmc(sb.toString());
				resSjxxDto.setCskz1(sk.toString());
			}
			if(resSjxxDto.getSjgzbys()!=null) {
				sb.setLength(0);
				List<SjgzbyDto> sjgzbyDtos = resSjxxDto.getSjgzbys();
				for(int i =0;i<sjgzbyDtos.size();i++) {
					if(i!=0)
						sb.append(",");
					sb.append(sjgzbyDtos.get(i).getCsmc());
				}
				resSjxxDto.setGzbymc(sb.toString());
			}
			if(resSjxxDto.getSjqqjcs()!=null) {
				sb.setLength(0);
				List<SjqqjcDto> sjqqjcDtos = resSjxxDto.getSjqqjcs();
				for(int i =0;i<sjqqjcDtos.size();i++) {
					if(i!=0)
						sb.append(",");
					sb.append(sjqqjcDtos.get(i).getCsmc());
					sb.append(" ");
					sb.append(sjqqjcDtos.get(i).getJcz());
				}
				resSjxxDto.setQqjcmc(sb.toString());
			}
			if(resSjxxDto.getSjlczzs()!=null) {
				sb.setLength(0);
				List<SjlczzDto> sjlczzDtos = resSjxxDto.getSjlczzs();
				for(int i =0;i<sjlczzDtos.size();i++) {
					if(i!=0)
						sb.append(",");
					sb.append(sjlczzDtos.get(i).getCsmc());
				}
				resSjxxDto.setLczzmc(sb.toString());
			}
		}

		return resSjxxDto;
	}

	/**
	 * 根据送检标本编号查询送检信息
	 * @param sjxxDto
	 * @return
	 */
	public SjxxDto getDto(SjxxDto sjxxDto,int flg){

		if(StringUtil.isBlank(sjxxDto.getSjid()) && StringUtil.isBlank(sjxxDto.getYbbh())
				&& StringUtil.isBlank(sjxxDto.getHzxm())&& StringUtil.isBlank(sjxxDto.getDdh())
				&& StringUtil.isBlank(sjxxDto.getYsdh()))
			return null;
		//获取送检信息
		SjxxDto resSjxxDto = dao.getDto(sjxxDto);

		if(resSjxxDto!=null) {
			resSjxxDto.setWxid(sjxxDto.getWxid());
			if(flg ==0 || flg ==3 ) {
				//获取送检项目清单
				SjjcxmDto jcxmDto = new SjjcxmDto();
				jcxmDto.setSjid(resSjxxDto.getSjid());
				
				List<SjjcxmDto> dtoList = sjjcxmService.getDtoList(jcxmDto);
				List<String> xmglids=new ArrayList<>();
				List<String> sjjcxmDtos=new ArrayList<>();
				if(dtoList!=null&&dtoList.size()>0){
					for(SjjcxmDto dto:dtoList){
						xmglids.add(dto.getXmglid());
						sjjcxmDtos.add(dto.getJcxmid());
					}
				}
				resSjxxDto.setJcxmids(sjjcxmDtos);
				resSjxxDto.setXmglids(xmglids);
				//获取送检子项目清单
				List<String> jczxmString = sjjcxmService.getJczxmString(jcxmDto);
				resSjxxDto.setJczxmids(jczxmString);

				List<JcsjDto> jczxmList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
				if(jczxmList!=null&&jczxmList.size()>0&&jczxmString!=null&&jczxmString.size()>0){
					String jczxmmc="";
					for(String jczxmid:jczxmString){
						for(JcsjDto jcsjDto:jczxmList){
							if(jczxmid.equals(jcsjDto.getCsid())){
								jczxmmc+=","+jcsjDto.getCsmc();
								break;
							}
						}
					}
					if(StringUtil.isNotBlank(jczxmmc)){
						resSjxxDto.setJczxmmc(jczxmmc.substring(1));
					}
				}

				//获取送检关注病原
				SjgzbyDto gzbyDto = new SjgzbyDto();
				gzbyDto.setSjid(resSjxxDto.getSjid());
				List<String> bys = sjgzbyService.getStringList(gzbyDto);
				resSjxxDto.setBys(bys);
			}else if(flg ==1 || flg ==3) {

				//获取送检症状清单
				SjlczzDto lczzDto = new SjlczzDto();
				lczzDto.setSjid(resSjxxDto.getSjid());
				List<String> sjlczzDtos = sjlczzService.getStringList(lczzDto);
				resSjxxDto.setLczzs(sjlczzDtos);
				//获取送检已检项目
				SjqqjcDto qqjcDto = new SjqqjcDto();
				qqjcDto.setSjid(resSjxxDto.getSjid());
				List<SjqqjcDto> sjqqjcDtos = sjqqjcService.getDtoListByJcsj(qqjcDto);
				resSjxxDto.setSjqqjcs(sjqqjcDtos);
				if(StringUtil.isBlank(resSjxxDto.getCyrq()))
					resSjxxDto.setCyrq(DateUtils.format(new Date()));
			}
		}

		return resSjxxDto;
	}

	/**
	 * 标本接收确认
	 * @param sjxxDto
	 * @return
	 */
	public boolean sampAcceptConfirm(SjxxDto sjxxDto) {

		if(sjxxDto== null || StringUtil.isBlank(sjxxDto.getSjid()) ||StringUtil.isBlank(sjxxDto.getYbbh())||StringUtil.isBlank(sjxxDto.getNbbm()))
			return false;

		int cnt = dao.updateConfirmInfo(sjxxDto);
		//发送消息
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(),  RabbitEnum.INSP_CFM.getCode() + JSONObject.toJSONString(sjxxDto));

		return cnt >0? true:false;
	}

	/**
	 *
	 */
	public List<SjxxDto> getPagedDtoList(SjxxDto sjxxDto){
		return dao.getPagedDtoList(sjxxDto);
	}

	/**
	 * 保存附件
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean saveFile(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		//文件复制到正式文件夹，插入信息至正式表
		if(sjxxDto.getFjids()!=null && sjxxDto.getFjids().size() > 0){
			String prefjidString = "";
			for (int i = 0; i < sjxxDto.getFjids().size(); i++) {
				String t_fjid = sjxxDto.getFjids().get(i);
				if(StringUtil.isNotBlank(t_fjid) && t_fjid.equals(prefjidString))
					continue;

				prefjidString = t_fjid;

				boolean saveFile = fjcfbService.save2RealFile(sjxxDto.getFjids().get(i),sjxxDto.getSjid());
				if(!saveFile)
					return false;
			}
		}
		// 附件排序
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		boolean result = fjcfbService.fileSort(fjcfbDto);
		if (!result) {
			log.error("外部编码："+sjxxDto.getYbbh()+" 附件排序失败！");
		}
		return true;
	}

	@Override
	public boolean saveScanFile(SjxxDto sjxxDto) {
		boolean success = false;
		for (int i = 0; i < sjxxDto.getFjids().size(); i++) {
			success = fjcfbService.save2RealFile(sjxxDto.getFjids().get(i),sjxxDto.getSjid());
			if (!success)
				return false;
		}
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.FILE_SAV.getCode() + JSONObject.toJSONString(sjxxDto));
		return true;
	}
	/**
	 * 保存本地新增信息至微信服务器
	 * @param sjxxDto
	 * @throws BusinessException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void receiveAddInspection(SjxxDto sjxxDto) throws BusinessException {
		// TODO Auto-generated method stub
		int result = dao.insert(sjxxDto);
		if(result == 0) {
			log.error("送检信息保存未成功！");
			throw new BusinessException("","送检信息保存未成功！");
		}
		//新增送检检测项目
		sjjcxmService.syncInfo(sjxxDto);
		//新增关注病原
		sjgzbyService.insertBySjxx(sjxxDto);
		//新增送检临床症状
		boolean isSuccess = sjlczzService.insertBySjxx(sjxxDto);
		if(!isSuccess){
			log.error("送检主诉信息保存未成功！");
			throw new BusinessException("","送检主诉信息保存未成功！");
		}
		//新增送检前期检测
		sjqqjcService.insertBySjxx(sjxxDto);
		/*if(StringUtil.isNotBlank(sjxxDto.getSjdw())) {
			//查询医生表，判断是否新增医生信息
			List<SjysxxDto> SjysxxDtos = sjysxxService.selectSjysxxDtoBySjys(sjxxDto);
			if(SjysxxDtos == null || SjysxxDtos.size() == 0){
				isSuccess = sjysxxService.insertBySjxxDto(sjxxDto);
				if(!isSuccess){
					log.error("医生信息更新保存未成功！");
					throw new BusinessException("","医生信息更新保存未成功！");
				}
			}
		}*/
	}

	/**
	 * 保存本地更新送检信息至微信服务器
	 * @param sjxxDto
	 * @throws BusinessException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void receiveModInspection(SjxxDto sjxxDto) throws BusinessException {
		// TODO Auto-generated method stub
		// 判断同步数据修改方法
		int result = 0;
		if(sjxxDto.getTbbj() == 1) {
			result = dao.updateForbiont(sjxxDto);
		}else {
			result = dao.update(sjxxDto);
		}
		if(result == 0){
			log.error("送检信息更新未成功！");
			throw new BusinessException("","送检信息更新未成功！");
		}
		//如果是否接收修改为否
		if ("0".equals(sjxxDto.getSfjs())){
			//修改实验管理的接收信息
			SjsyglDto sjsyglDto = new SjsyglDto();
			sjsyglDto.setSjid(sjxxDto.getSjid());
			sjsyglDto.setJsrq(sjxxDto.getJsrq());
			sjsyglDto.setSfjs(sjxxDto.getSfjs());
			sjsyglDto.setJsry(sjxxDto.getJsry());
			sjsyglDto.setXgry(sjxxDto.getXgry());
			sjsyglService.updateJsInfo(sjsyglDto);
		}
		//修改送检检测项目
		sjjcxmService.syncInfo(sjxxDto);

		//修改关注病原
		sjgzbyService.insertBySjxx(sjxxDto);
		//新增送检临床症状
		boolean isSuccess = sjlczzService.insertBySjxx(sjxxDto);
		if(!isSuccess){
			log.error("送检主诉信息保存未成功！");
			throw new BusinessException("","送检主诉信息保存未成功！");
		}
		//新增送检前期检测
		sjqqjcService.insertBySjxx(sjxxDto);
		/*if(StringUtil.isNotBlank(sjxxDto.getSjdw())) {
			//查询医生表，判断是否新增医生信息
			List<SjysxxDto> SjysxxDtos = sjysxxService.selectSjysxxDtoBySjys(sjxxDto);
			if(SjysxxDtos == null || SjysxxDtos.size() == 0){
				isSuccess = sjysxxService.insertBySjxxDto(sjxxDto);
				if(!isSuccess){
					log.error("医生信息更新保存未成功！");
					throw new BusinessException("","医生信息更新保存未成功！");
				}
			}
		}*/
	}

	/**
	 * 保存本地删除送检信息至微信服务器
	 * @param sjxxDto
	 * @throws BusinessException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void receiveDelInspection(SjxxDto sjxxDto) throws BusinessException {
		// TODO Auto-generated method stub
		boolean result = delete(sjxxDto);
		if(!result){
			log.error("送检信息删除未成功");
			throw new BusinessException("","送检信息删除未成功！");
		}
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public Boolean saveProjectAmount(SjjcxmDto sjjcxmDto) throws BusinessException {
		if (StringUtil.isBlank(sjjcxmDto.getSjid()))
			throw new BusinessException("msg","未获取到送检信息！");
		List<SjjcxmDto> list=(List<SjjcxmDto>) JSON.parseArray(sjjcxmDto.getJson(), SjjcxmDto.class);
		if (CollectionUtils.isEmpty(list))
			throw new BusinessException("msg","未获取到送检项目信息！");
		for (SjjcxmDto dto : list) {
			dto.setXgry(sjjcxmDto.getXgry());
		}
		Boolean result = sjjcxmService.updateListNew(list);
		if (!result)
			throw new BusinessException("msg","维护项目数据失败！");
		SjxxDto sjxxDto = new SjxxDto();
		sjxxDto.setXgry(sjjcxmDto.getXgry());
		sjxxDto.setSjid(sjjcxmDto.getSjid());
		sjxxDto.setClbj("1");
		result =  dao.updateDto(sjxxDto)!=0;
		if (!result)
			throw new BusinessException("msg","更新处理标记失败！");
		return true;
	}
	/**
	 * 保存本确认送检收样信息至微信服务器
	 * @param sjxxDto
	 * @throws BusinessException
	 */
	@Override
	public void receiveConfirmInspection(SjxxDto sjxxDto) throws BusinessException {
		// TODO Auto-generated method stub
		int result = dao.updateConfirmInfo(sjxxDto);
		if(result == 0){
			log.error("确认送检收样信息未成功");
			throw new BusinessException("","确认送检收样信息未成功！");
		}
		if (null != sjxxDto.getZts() && sjxxDto.getZts().size()>0){
			List<SjsyglDto> list = new ArrayList<>();
			for (String id : sjxxDto.getZts()) {
				SjsyglDto sjsyglDto = new SjsyglDto();
				sjsyglDto.setSyglid(id);
				sjsyglDto.setSfjs(sjxxDto.getSfjs());
				sjsyglDto.setJsry(sjxxDto.getXgry());
				sjsyglDto.setXgry(sjxxDto.getXgry());
				list.add(sjsyglDto);
			}
			sjsyglService.updateList(list);
		}
	}

	/**
	 * 根据状态，患者姓名和时间查询信息
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public SjxxDto extractUserInfo(SjxxDto sjxxDto) throws BusinessException {
		// TODO Auto-generated method stub
		String ybbh = sjxxDto.getYbbh();
		String hzxm = sjxxDto.getHzxm();
		String userid = "";
		if (StringUtil.isBlank(sjxxDto.getWxid())&&StringUtil.isBlank(sjxxDto.getDdid())){
			return null;
		}
		if(StringUtil.isNotBlank(sjxxDto.getWxid())){
			userid = sjxxDto.getWxid();
		}else if (StringUtil.isNotBlank(sjxxDto.getDdid())){
			userid = sjxxDto.getDdid();
		}
		List<String> lrrys = sjysxxService.getLrrylist(sjxxDto.getWxid(), sjxxDto.getDdid());
		if(lrrys == null || lrrys.size() == 0){
			lrrys = new ArrayList<String>();
			lrrys.add(userid);
		}
		sjxxDto.setLrrys(lrrys);
		if(StringUtil.isNotBlank(hzxm) && StringUtil.isBlank(ybbh)){
			List<SjxxDto> sjxxDtos = dao.getDtoList(sjxxDto);
			if(sjxxDtos == null || sjxxDtos.size() == 0)
				return null;
			for (int i = 0; i < sjxxDtos.size(); i++) {
				if(StringUtil.isBlank(sjxxDtos.get(i).getZt()) || !StatusEnum.CHECK_PASS.getCode().equals(sjxxDtos.get(i).getZt())){
					//有未完成数据返回
					return getDto(sjxxDtos.get(i));
				}
			}
			return insertByExtract(userid, ybbh, sjxxDtos.get(0));
		}else if(StringUtil.isNotBlank(ybbh) && StringUtil.isBlank(hzxm)){
			List<SjxxDto> sjxxDtos = dao.getDtoList(sjxxDto);
			// 判断是否调用金域接口获取信息
			if(sjxxDtos == null || sjxxDtos.size() == 0){
				int length = ybbh.length();
				String substring = ybbh.substring(0, 2);
				if(length == 10 && ("05".equals(substring) || "06".equals(substring) || "14".equals(substring))){
					//调用金域接口
					SjxxDto goldenData = getGoldenData(ybbh);
					goldenData.setLrry(userid);
					boolean result = insertDto(goldenData);
					if(!result)
						throw new BusinessException("","送检检测项目信息保存未成功！");
					//金域项目暂时无需设置应付金额等信息，因为该接口已基本废弃
					//新增送检检测项目
					result = sjjcxmService.insertBySjxx(sjxxDto);
					if(!result)
						throw new BusinessException("","送检检测项目信息保存未成功！");
					return dao.getDto(goldenData);
				}
				return null;
			}
			for (int i = 0; i < sjxxDtos.size(); i++) {
				if(StringUtil.isBlank(sjxxDtos.get(i).getZt()) || !StatusEnum.CHECK_PASS.getCode().equals(sjxxDtos.get(i).getZt())){
					//有未完成数据返回
					return getDto(sjxxDtos.get(i));
				}
			}
		}else if(StringUtil.isNotBlank(ybbh) && StringUtil.isNotBlank(hzxm)){
			//判断标本编号是否重复
			SjxxDto s_sjxxDto = new SjxxDto();
			s_sjxxDto.setYbbh(ybbh);
			s_sjxxDto = dao.getDto(s_sjxxDto);
			if(s_sjxxDto == null){
				SjxxDto t_sjxxDto = new SjxxDto();
				t_sjxxDto.setHzxm(hzxm);
				t_sjxxDto.setLrrys(sjxxDto.getLrrys());
				List<SjxxDto> sjxxDtos = dao.getDtoList(t_sjxxDto);
				if(sjxxDtos == null || sjxxDtos.size() == 0)
					return null;
				for (int i = 0; i < sjxxDtos.size(); i++) {
					if(StringUtil.isBlank(sjxxDtos.get(i).getZt()) || !StatusEnum.CHECK_PASS.getCode().equals(sjxxDtos.get(i).getZt())){
						//有未完成数据修改并返回
						sjxxDtos.get(i).setYbbh(ybbh);
						sjxxDtos.get(i).setXgry(userid);
						boolean result = update(sjxxDtos.get(i));
						if(!result){
							log.error(xxglService.getModelById("ICOM00002").getXxnr());
							throw new BusinessException("msg",xxglService.getModelById("ICOM00002").getXxnr());
						}
						return getDto(sjxxDtos.get(i));
					}
				}
				return insertByExtract(userid, ybbh, sjxxDtos.get(0));
			}else{
				List<SjxxDto> sjxxDtos = dao.getDtoList(sjxxDto);
				if(sjxxDtos != null && sjxxDtos.size() > 0){
					for (int i = 0; i < sjxxDtos.size(); i++) {
						if(StringUtil.isBlank(sjxxDtos.get(i).getZt()) || !StatusEnum.CHECK_PASS.getCode().equals(sjxxDtos.get(i).getZt())){
							//有未完成数据返回
							return getDto(sjxxDtos.get(i));
						}
					}
				}
			}
		}
		log.error(xxglService.getModelById("WCOM_WC00001").getXxnr());
		throw new BusinessException("msg",xxglService.getModelById("WCOM_WC00001").getXxnr());
	}

	/**
	 * 根据标本编号获取金域接口信息
	 * @param ybbh
	 * @return
	 * @throws BusinessException
	 */
	private SjxxDto getGoldenData(String ybbh) throws BusinessException{
		// 动态调用webservice接口
		log.error("getGoldenData 准备动态调用金域接口！");
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("https://extesb.kingmed.com.cn:4433/services/LB.SPECIMEN.SEND");
		try {
			QName name = new QName("http://cxf.esb.lb.kingmed.com/", "Login");
			Object[] objects = new Object[0];
			objects = client.invoke(name, "hzjysw", "hzjysw@2020.","001", true);
			if(objects != null && "0".equals(objects[0])){
				name = new QName("http://cxf.esb.lb.kingmed.com/", "GetRequestInfo4");
				Object[] t_objects = new Object[0];
				t_objects = client.invoke(name, objects[1], ybbh);
				if(t_objects != null && "0".equals(t_objects[0])){
					Map<String, Object> xmlMap = BasicXmlReader.readXmlToMap(t_objects[1].toString());
					if(xmlMap != null){
						log.error("readXmlToMap转换完成!");
						String requestCode = (String) xmlMap.get("RequestCode");// 金域条码 - 标本编号，外部编码
						String t_name = (String) xmlMap.get("Name");// 病人姓名
						String sex = (String) xmlMap.get("Sex");// 性别
						if("男".equals(sex)){
							sex = "1";
						}else if("女".equals(sex)){
							sex = "2";
						}else{
							sex = "3";
						}
						String age = (String) xmlMap.get("Age");// 年龄
						String ageUnit = (String) xmlMap.get("AgeUnit");// 年龄单位
						if(!"岁".equals(ageUnit) && !"个月".equals(ageUnit) && !"天".equals(ageUnit)){
							ageUnit = "无";
						}
						String patientTel = (String) xmlMap.get("PatientTel");// 病人电话
						String doctName = (String) xmlMap.get("DoctName");// 医生姓名
						if(StringUtil.isBlank(doctName)){
							doctName = "无";
						}
						String sectionOffice = (String) xmlMap.get("SectionOffice");// 医院及科室，-号分隔
						String bedNumber = (String) xmlMap.get("BedNumber");// 床号
						// String naturalItemName = (String) xmlMap.get("NaturalItemName");// 检测项目
						String naturalItem = (String) xmlMap.get("NaturalItem");// 检测项目代码 RNA：60648 DNA:60649
						if(naturalItem.contains("60648") && naturalItem.contains("60649")){
							naturalItem = "C";
						}else if(naturalItem.contains("60647")){
							naturalItem = "C";
						}else if(naturalItem.contains("60648")){
							naturalItem = "R";
						}else if(naturalItem.contains("60649")){
							naturalItem = "D";
						}else{
							log.error("naturalItem信息获取有误："+sectionOffice);
							throw new BusinessException("msg","naturalItem信息获取有误："+sectionOffice);
						}
						String specimenType = (String) xmlMap.get("SpecimenType");// 标本类型
						String address = (String) xmlMap.get("ADDRESS");// 标本体积
						String samplingDate = (String) xmlMap.get("SamplingDate");// 采样日期
						String diagnose = (String) xmlMap.get("Diagnose");// 临床症状和前期诊断
						//查询基础数据
						Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(
								new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.DETECT_TYPE,
										BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.DETECTION_UNIT});
						//科室处理
						String ks = null;
						String qtks= null;
						String sjdw = null;
						if(StringUtil.isNotBlank(sectionOffice)){
							String[] sectionOffices = sectionOffice.split("-", 2);
							sjdw = sectionOffices[0];
							List<SjdwxxDto> sjdwxxList = sjdwxxService.getSjdwxx();
							for (int l = 0; l < sjdwxxList.size(); l++) {
								if(sectionOffices.length < 2){
									if("1".equals(sjdwxxList.get(l).getKzcs())){
										ks = sjdwxxList.get(l).getDwid();
										qtks = "无";
										break;
									}
								}else{
									if(sjdwxxList.get(l).getDwmc().equals(sectionOffices[1])){
										ks = sjdwxxList.get(l).getDwid();
										qtks = null;
										break;
									}
									if("1".equals(sjdwxxList.get(l).getKzcs())){
										ks = sjdwxxList.get(l).getDwid();
										qtks = sectionOffices[1];
									}
								}

							}
						}
						//标本类型处理
						String yblx = null;
						String yblxmc = null;
						if(StringUtil.isNotBlank(specimenType)){
							if("全血".equals(specimenType)){
								specimenType = "外周血";
							}else if("尿".equals(specimenType)){
								specimenType = "尿液";
							}else if("痰".equals(specimenType)){
								specimenType = "痰液";
							}
							List<JcsjDto> samplelist = jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
							for (int l = 0; l < samplelist.size(); l++) {
								if(samplelist.get(l).getCsmc().equals(specimenType)){
									yblx = samplelist.get(l).getCsid();
									yblxmc = null;
									break;
								}
								if("1".equals(samplelist.get(l).getCskz1())){
									yblx = samplelist.get(l).getCsid();
									yblxmc = specimenType;
								}
							}
						}
						//检测项目处理
						List<JcsjDto> datectlist = jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
						List<String> jcxmids = new ArrayList<String>();
						for (int l = 0; l < datectlist.size(); l++) {
							if(datectlist.get(l).getCskz1().equals(naturalItem)){
								jcxmids.add(datectlist.get(l).getCsid());
								break;
							}
						}
						//检测单位固定 - 杭州
						List<JcsjDto> decetionlist = jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode());
						String jcdw = null;
						for (int l = 0; l < decetionlist.size(); l++) {
							if("1".equals(decetionlist.get(l).getCsdm())){
								jcdw = decetionlist.get(l).getCsid();
								break;
							}
						}
						// 快递类型固定 - 无
						List<JcsjDto> expressage = jclist.get(BasicDataTypeEnum.SD_TYPE.getCode());
						String kdlx = null;
						for (int l = 0; l < expressage.size(); l++) {
							if("1".equals(expressage.get(l).getCskz2())){
								kdlx = expressage.get(l).getCsid();
								break;
							}
						}
						log.error("参数组合完成！");
						SjxxDto t_sjxxDto = new SjxxDto();
						t_sjxxDto.setYbbh(requestCode);
						t_sjxxDto.setWbbm(requestCode);
						t_sjxxDto.setHzxm(t_name);
						t_sjxxDto.setXb(sex);
						t_sjxxDto.setNl(age);
						t_sjxxDto.setNldw(ageUnit);
						t_sjxxDto.setDh(patientTel);
						t_sjxxDto.setSjys(doctName);
						t_sjxxDto.setSjdw(sjdw);
						t_sjxxDto.setKs(ks);
						t_sjxxDto.setQtks(qtks);
						t_sjxxDto.setJcdw(jcdw);
						t_sjxxDto.setDb("金域");
						t_sjxxDto.setCwh(bedNumber);
						t_sjxxDto.setKdlx(kdlx);
						t_sjxxDto.setKdh("无");
						t_sjxxDto.setJcxmids(jcxmids);
						t_sjxxDto.setYblx(yblx);
						t_sjxxDto.setYblxmc(yblxmc);
						t_sjxxDto.setYbtj(address);
						t_sjxxDto.setCyrq(samplingDate);
						t_sjxxDto.setQqzd(diagnose);
						t_sjxxDto.setLczz(diagnose);
						return t_sjxxDto;
					}
				}else{
					log.error(t_objects!= null?t_objects.toString():"获取送检信息失败！");
					throw new BusinessException("msg",t_objects!= null?t_objects.toString():"获取送检信息失败！");
				}
			}else{
				log.error(objects!= null?objects.toString():"验证用户名失败！");
				throw new BusinessException("msg",objects!= null?objects.toString():"验证用户名失败！");
			}
		} catch (BusinessException e) {
			log.error(e.getMsg());
			throw new BusinessException("msg", e.getMsg());
		} catch (Exception e) {
			log.error(e.toString());
			throw new BusinessException("msg", e.toString());
		}
		return null;
	}

	/**
	 * 提取新增送检
	 * @param wxid
	 * @param ybbh
	 * @param sjxxDto
	 * @return
	 */
	private SjxxDto insertByExtract(String wxid, String ybbh, SjxxDto sjxxDto){
		SjxxDto s_sjxxDto = dao.extractUserInfo(sjxxDto);
		SjxxDto t_sjxxDto = getDto(s_sjxxDto);
		//如果有，新增一条数据，并返回
		s_sjxxDto.setYbbh(ybbh);
		s_sjxxDto.setSjid(StringUtil.generateUUID());
		s_sjxxDto.setLrry(wxid);
		s_sjxxDto.setSfsf("1");//提取新增是否收费设置为1
		s_sjxxDto.setSfjs(null);
		insertDto(s_sjxxDto);
		t_sjxxDto.setSjid(s_sjxxDto.getSjid());
		//新增送检检测项目
		/*boolean result = sjjcxmService.insertBySjxxDto(t_sjxxDto);
		if(!result)
			return null;*/
		//新增关注病原
		sjgzbyService.insertBySjxxDto(t_sjxxDto);
		//新增送检临床症状
		boolean result = sjlczzService.insertBySjxxDto(t_sjxxDto);
		if(!result)
			return null;
		//新增送检前期检测
		result = sjqqjcService.insertBySjxxDto(t_sjxxDto);
		if(!result)
			return null;
		return getDto(s_sjxxDto);
	}

	/**
	 * 保存修改金额(微信端)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean amountSaveEdit(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		int result = dao.amountSaveEdit(sjxxDto);
		if(result == 0)
			return false;
		return true;
	}

	/**
	 * 根据送检id获取物种信息
	 */
	@Override
	public List<SjwzxxDto> selectWzxxBySjid(SjxxDto sjxxDto) {
		return dao.selectWzxxBySjid(sjxxDto);
	}

	/**
	 * 根据订单号修改删除标记 
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean updateByDdh(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		int result = dao.updateByDdh(sjxxDto);
		if(result == 0)
			return false;
		return true;
	}

	/**
	 * 列表查询
	 * @param map
	 * @return
	 */
	@Override
	public List<SjxxDto> getListWithMap(Map<String,Object> map){
		return dao.getListWithMap(map);
	}
	/**
	 * 查询相同用户标本类型是否输入重复
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> isYblxRepeat(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.isYblxRepeat(sjxxDto);
	}

	/**
	 * 根据订单号查询送检信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public SjxxDto getDtoByDdh(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getDtoByDdh(sjxxDto);
	}

	/**
	 * 小程序获取送检清单
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getSjxxDtoList(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getSjxxDtoList(sjxxDto);
	}

	/**
	 * 查询相同的医生电话和患者姓名
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public int getcountByysdh(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		return dao.getcountByysdh(sjxxDto);
	}

	/**
	 * 根据送检信息查询出送检销售人员
	 * @return
	 */
	@Override
	public List<Map<String, String>> getLrry(){
		// TODO Auto-generated method stub
		return dao.getLrry();
	}

	/**
	 * 根据收费情况以周为单位进行统计收样(按照不收费的标本变化，收费标本的变化)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getSjxxWeekBySy(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		List<Map<String, String>> listMap=null;
		try {
			List<String> rqs= new ArrayList<String>();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
			for(int i=0;i<5;i++) {
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			List<Map<String, String>> resultMaps = dao.getSjxxWeekBySy(sjxxDto);

			String s_preRq = "";
			int sum =0,presum=0;
			for(int i=0;i<resultMaps.size();i++) {
				//日期有变化
				if(!s_preRq.equals(resultMaps.get(i).get("rq"))) {
					if(presum == 0 && i!=0) {
						resultMaps.get(i-1).put("bl", "0");
					}else if(presum != 0) {
						BigDecimal bg_sum = new BigDecimal(presum);
						resultMaps.get(i-1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum,0,RoundingMode.HALF_UP).toString());
					}

					presum = sum;
					s_preRq= resultMaps.get(i).get("rq");

					if("1".equals(resultMaps.get(i).get("sfjs"))) {
						sum = Integer.valueOf(resultMaps.get(i).get("num"));
					}else {
						sum =0;
					}
				}else if("1".equals(resultMaps.get(i).get("sfjs"))) {
					sum += Integer.valueOf(resultMaps.get(i).get("num"));
				}

			}
			if(presum == 0 && resultMaps.size() > 0) {
				resultMaps.get(resultMaps.size()-1).put("bl", "0");
			}else if(presum != 0) {
				BigDecimal bg_sum = new BigDecimal(presum);
				resultMaps.get(resultMaps.size()-1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum,0,RoundingMode.HALF_UP).toString());
			}

			return resultMaps;
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
		return listMap;
	}

	/**
	 * 根据收费情况进行统计收样(按照不收费的标本变化，收费标本的变化)
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, String>> getSjxxBySy(SjxxDto sjxxDto){
		try {
			List<String> rqs= new ArrayList<String>();
			if("day".equals(sjxxDto.getTj())) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
				Calendar endcalendar=Calendar.getInstance();
				if(StringUtil.isNotBlank(sjxxDto.getJsrqend())) {
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
				}else {
					endcalendar.setTime(new Date());
				}
				while(endcalendar.compareTo(calendar) >= 0) {
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.DATE, 1);
				}
			}else if("mon".equals(sjxxDto.getTj())) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqMstart()));

				Calendar endcalendar=Calendar.getInstance();
				if(StringUtil.isNotBlank(sjxxDto.getJsrqMend())) {
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqMend()));
				}else {
					endcalendar.setTime(new Date());
				}

				while(endcalendar.compareTo(calendar) >= 0) {
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.MONTH, 1);
				}
			}else if("year".equals(sjxxDto.getTj())) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqYstart()));

				Calendar endcalendar=Calendar.getInstance();
				if(StringUtil.isNotBlank(sjxxDto.getJsrqYend())) {
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqYend()));
				}else {
					endcalendar.setTime(new Date());
				}

				while(endcalendar.compareTo(calendar) >= 0) {
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.YEAR, 1);
				}
			}
			sjxxDto.setRqs(rqs);
			List<Map<String, String>> resultMaps = dao.getSjxxBySy(sjxxDto);

			String s_preRq = "";
			long sum =0,presum=0;
			for(int i=0;i<resultMaps.size();i++) {
				//日期有变化
				if(!s_preRq.equals(resultMaps.get(i).get("rq"))) {
					if(presum == 0 && i!=0) {
						resultMaps.get(i-1).put("bl", "0");
					}else if(presum != 0) {
						BigDecimal bg_sum = new BigDecimal(presum);
						resultMaps.get(i-1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum,0,RoundingMode.HALF_UP).toString());
					}

					presum = sum;

					s_preRq= resultMaps.get(i).get("rq");

					if("1".equals(resultMaps.get(i).get("sfjs"))) {
						sum = Integer.valueOf(resultMaps.get(i).get("num"));
					}else {
						sum =0;
					}
				}else if("1".equals(resultMaps.get(i).get("sfjs"))) {
					sum += Long.parseLong(resultMaps.get(i).get("num"));
				}

			}
			if(presum == 0 && resultMaps.size() > 0) {
				resultMaps.get(resultMaps.size()-1).put("bl", "0");
			}else if(presum != 0) {
				BigDecimal bg_sum = new BigDecimal(presum);
				resultMaps.get(resultMaps.size()-1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum,0,RoundingMode.HALF_UP).toString());
			}

			return resultMaps;
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据收费不收费和废弃标本做统计
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxByWeek(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		return dao.getYbxxByWeek(sjxxDto);
	}

	/**
	 * 查询当前销售人员送检单位的送检数量
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxBySjdw(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		List<String> sjdwList=dao.getSjdw(sjxxDto);
		List<Map<String,String>> sjdwMap=null;
		if(sjdwList!=null && sjdwList.size()>0) {
			sjxxDto.setSjdws(sjdwList);
			sjdwMap=dao.getYbxxBySjdw(sjxxDto);
		}
		return sjdwMap;
	}

	/**
	 * 获取查看报告列表
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getPagedReportList(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getPagedReportList(sjxxDto);
	}

	/**
	 * 修改临床反馈
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean updateFeedBack(SjxxDto sjxxDto){
		if(StringUtil.isBlank(sjxxDto.getSjid()))
			return false;
		// TODO Auto-generated method stub
		boolean result=dao.updateFeedBack(sjxxDto);
		//先删除进行过删除的附件
		if(sjxxDto.getDellist()!=null && sjxxDto.getDellist().size()>0) {
			FjcfbDto delFjcfb=new FjcfbDto();
			delFjcfb.setFjids(sjxxDto.getDellist());
			fjcfbService.delByFjids(delFjcfb);
		}
		if(sjxxDto.getFjids()!=null && sjxxDto.getFjids().size() > 0){
			for (int i = 0; i < sjxxDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(sjxxDto.getFjids().get(i),sjxxDto.getSjid());
				if(!saveFile)
					return false;
			}
		}
		//获取送检附件IDs
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_FEEDBACK.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		if(fjcfbDtos != null && fjcfbDtos.size() > 0){
			List<String> fjids = new ArrayList<String>();
			for (int i = 0; i < fjcfbDtos.size(); i++) {
				fjids.add(fjcfbDtos.get(i).getFjid());
			}
			sjxxDto.setFjids(fjids);
		}
		//查询送检信息，钉钉消息显示相关信息
		SjxxDto sjxx=dao.getDto(sjxxDto);
		sjxxDto.setYbbh(sjxx.getYbbh());
		sjxxDto.setHzxm(sjxx.getHzxm());
		sjxxDto.setYblxmc(sjxx.getYblxmc());
		//发送消息
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.FEED_BAC.getCode() + JSONObject.toJSONString(sjxxDto));
		return result;
	}

	/**
	 * 医生小程序获取送检清单
	 * @param sjxxDto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SjxxDto> getDocMiniInsplist(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		//根据微信ID获取角色查看权限
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
		paramMap.add("wxid", sjxxDto.getWxid());
		paramMap.add("ddid", sjxxDto.getDdid());
		DBEncrypt crypt = new DBEncrypt();
		String url = crypt.dCode(companyurl);
		List<String> lrrylist = restTemplate.postForObject(url+"/ws/getLrryList", paramMap, List.class);
		sjxxDto.setLrrys(lrrylist);
		// 判断送检查看权限：1.根据wxid查询unionid；2.根据unionid查询单位科室信息
		if(StringUtil.isNotBlank(sjxxDto.getWxid())) {
			WxyhDto wxyhDto = wxyhService.getDtoById(sjxxDto.getWxid());
			if(StringUtil.isNotBlank(wxyhDto.getUnionid())) {
				SjqxDto sjqxDto = new SjqxDto();
				sjqxDto.setWxid(wxyhDto.getUnionid());
				List<SjqxDto> sjqxList = sjqxService.getDtoList(sjqxDto);
				sjxxDto.setSjqxDtos(sjqxList);
			}
		}
		return dao.getDocMiniInsplist(sjxxDto);
	}


	/**
	 * 送检结果类型统计
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getDetectionResultStatis(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		List<SjxxDto> detectionResultStatis = dao.getDetectionResultStatis(sjxxDto);
		return checkDetectionResult(detectionResultStatis);
	}

	/**
	 * 判断结果类型
	 * @param list
	 * @return
	 */
	public List<Map<String, String>> checkDetectionResult(List<SjxxDto> list){
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		if(list != null && list.size() > 0){
			String[] arr = {"阴性","疑似","阳性","无"};
			for (int i = 0; i < arr.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				boolean add = true;
				for (int j = 0; j < list.size(); j++) {
					if(arr[i].equals(list.get(j).getJyjg())){
						add = false;
						map.put("jyjg", list.get(j).getJyjg());
						map.put("num", list.get(j).getNum());
						list.remove(j);
						break;
					}
				}
				if(add){
					map.put("jyjg", arr[i]);
					map.put("num", "0");
				}
				listMap.add(map);
			}
		}
		return listMap;
	}

	/**
	 * 查询没有进行过反馈的送检信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getNofeefback(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		return dao.getNofeefback(sjxxDto);
	}

	/**
	 * 分页查询送检单位和科室信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getPagedUnitList(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getPagedUnitList(sjxxDto);
	}

	/**
	 * 修改送检表支付信息(同步)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean modInspPayinfo(SjxxDto sjxxDto,String zfid) {
		// TODO Auto-generated method stub
		PayinfoDto payinfoDto = new PayinfoDto();
		payinfoDto.setYwlx(BusinessTypeEnum.SJ.getCode());
		payinfoDto.setYwid(sjxxDto.getSjid());
		payinfoDto.setZfid(zfid);
		List<PayinfoDto> payinfoDtos = payinfoService.selectPayOrdersSuccess(payinfoDto);
		if (payinfoDtos != null && payinfoDtos.size() > 0) {
			List<SjjcxmDto> sjjcxmDtos = new ArrayList<SjjcxmDto>();
			for (PayinfoDto pay : payinfoDtos) {
				if(StringUtil.isNotBlank(pay.getZfjcxm())){
					String zfjcxm = pay.getZfjcxm();
					@SuppressWarnings("unchecked")
					List<Map<String,Object>> list = JSON.parseObject(zfjcxm, List.class);
					if (list!=null && list.size()>0){
						for (Map<String,Object> map : list) {
							String jcxmid = (String) map.get("jcxmid");
							String jczxmid = (String) map.get("jczxmid");
							String je = (map.get("je") !=null && StringUtil.isNotBlank(map.get("je").toString()))? map.get("je").toString() : "0";
							if (StringUtil.isBlank(jczxmid)){
								//匹配sjjcxmDtos中jcxmid与jcxmid相同的记录，如果有则累加金额，如果没有则新增一条记录
								boolean flag = false;
								for (SjjcxmDto sjjcxmDto : sjjcxmDtos) {
									if (jcxmid.equals(sjjcxmDto.getJcxmid())){
										BigDecimal oldsfje = new BigDecimal(StringUtil.isNotBlank(sjjcxmDto.getSfje()) ? sjjcxmDto.getSfje() : "0");
										BigDecimal newsfje = new BigDecimal(je);
										sjjcxmDto.setSfje(oldsfje.add(newsfje).toString());
										if (StringUtil.isNotBlank(zfid)){
											sjjcxmDto.setBdsfje(oldsfje.add(newsfje).toString());
										}
										flag = true;
										break;
									}
								}
								if (!flag){
									SjjcxmDto sjjcxmDto = new SjjcxmDto();
									sjjcxmDto.setSjid(sjxxDto.getSjid());
									sjjcxmDto.setJcxmid(jcxmid);
									sjjcxmDto.setSfje(je);
									if (StringUtil.isNotBlank(zfid)){
										sjjcxmDto.setBdsfje(je);
									}
									sjjcxmDto.setFkrq(sjxxDto.getFkrq());
									sjjcxmDtos.add(sjjcxmDto);
								}
							}else {
								//匹配sjjcxmDtos中jcxmid与jczxmid相同的记录，如果有则累加金额，如果没有则新增一条记录
								boolean flag = false;
								for (SjjcxmDto sjjcxmDto : sjjcxmDtos) {
									if (jcxmid.equals(sjjcxmDto.getJcxmid()) && jczxmid.equals(sjjcxmDto.getJczxmid())){
										BigDecimal oldsfje = new BigDecimal(StringUtil.isNotBlank(sjjcxmDto.getSfje()) ? sjjcxmDto.getSfje() : "0");
										BigDecimal newsfje = new BigDecimal(je);
										sjjcxmDto.setSfje(oldsfje.add(newsfje).toString());
										if (StringUtil.isNotBlank(zfid)){
											sjjcxmDto.setBdsfje(oldsfje.add(newsfje).toString());
										}
										flag = true;
										break;
									}
								}
								if (!flag){
									SjjcxmDto sjjcxmDto = new SjjcxmDto();
									sjjcxmDto.setSjid(sjxxDto.getSjid());
									sjjcxmDto.setJcxmid(jcxmid);
									sjjcxmDto.setJczxmid(jczxmid);
									sjjcxmDto.setSfje(je);
									if (StringUtil.isNotBlank(zfid)){
										sjjcxmDto.setBdsfje(je);
									}
									sjjcxmDto.setFkrq(sjxxDto.getFkrq());
									sjjcxmDtos.add(sjjcxmDto);
								}
							}
						}
					}
				}
			}
			sjxxDto.setSjjcxms(sjjcxmDtos);
			int result = sjjcxmService.updateSjjcxmDtos(sjjcxmDtos);
			if(result == 0){
				log.error("modInspPayinfo 修改检测项目表失败！");
				return false;
			}
		}
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.PYIF_MOD.getCode() + JSONObject.toJSONString(sjxxDto));
		int result = dao.modInspPayinfo(sjxxDto);
		if(result == 0) {
			log.error("modInspPayinfo 修改送检表支付信息失败！");
			return false;
		}
		return true;
	}
	/**
	 * 查询角色检测单位限制
	 * @param jsid
	 * @return
	 */
	@Override
	public List<Map<String, String>> getJsjcdwByjsid(String jsid)
	{
		// TODO Auto-generated method stub
		return dao.getJsjcdwByjsid(jsid);
	}

	/**
	 * 查询科室信息
	 */
	@Override
	public List<SjdwxxDto> getSjdwxx()
	{
		// TODO Auto-generated method stub
		return dao.getSjdwxx();
	}

	/**
	 * 病原体列表
	 */
	@Override
	public List<SjxxDto> getPagedInspection(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getPagedInspection(sjxxDto);
	}

	/**
	 * 查询检测单位
	 * @param hbmc
	 * @return
	 */
	@Override
	public List<JcsjDto> getDetectionUnit(String hbmc) {
		// TODO Auto-generated method stub
		List<String> ids = null;
		if(StringUtil.isNotBlank(hbmc)) {
			// 查询伙伴权限信息
			ids = hbdwqxService.selectByHbmc(hbmc);
		}
		JcsjDto jcsjDto = new JcsjDto();
		// 查询检测单位
		jcsjDto.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
		jcsjDto.setIds(ids);
		List<JcsjDto> jcsjDtos = jcsjService.selectDetectionUnit(jcsjDto);
		if(jcsjDtos == null)
			jcsjDtos = new ArrayList<JcsjDto>();
		return jcsjDtos;
	}

	/**
	 * 查询最近一次送检信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public SjxxDto getLastInfo(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		SjxxDto t_sjxxDto = dao.getLastInfo(sjxxDto);
		if(t_sjxxDto == null)
			return new SjxxDto();
		return t_sjxxDto;
	}

	/**
	 * 根据检测项目查询检测子项目
	 * @param jcxmid
	 * @return
	 */
	@Override
	public List<JcsjDto> getSubDetect(String jcxmid) {
		// TODO Auto-generated method stub
		List<JcsjDto> jcsjDtos = new ArrayList<>();
		if(StringUtil.isNotBlank(jcxmid)) {
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setFcsid(jcxmid);
			jcsjDto.setJclb(BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
			jcsjDtos = jcsjService.getListByFid(jcsjDto);
		}
		return jcsjDtos;
	}

	/**
	 * 根据检测项目查询检测子项目(不包含禁用)
	 * @param jcxmid
	 * @return
	 */
	@Override
	public List<JcsjDto> getSubDetectWithoutDisabled(String jcxmid) {
		// TODO Auto-generated method stub
		List<JcsjDto> jcsjDtos = new ArrayList<>();
		if(StringUtil.isNotBlank(jcxmid)) {
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setFcsid(jcxmid);
			jcsjDto.setJclb(BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
			jcsjDtos = jcsjService.getDtoList(jcsjDto);
		}
		return jcsjDtos;
	}

	/**
	 * 清空检测子项目
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean emptySubDetect(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(sjxxDto.getSjid())) {
			int result = dao.emptySubDetect(sjxxDto);
			if(result > 0)
				return true;
		}
		return false;
	}

	/*获取未完善的送检信息*/
	@Override
	public List<SjxxDto> getPagedPerfectDtoList(SjxxDto sjxxDto) {
		return dao.getPagedPerfectDtoList(sjxxDto);
	}

	/**
	 * 查询接收人员列表
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getReceiveUserByDb(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getReceiveUserByDb(sjxxDto);
	}

	/**
	 * 发送微信消息
	 * @param templateid
	 * @param wxid
	 * @param title
	 * @param keyword1
	 * @param keyword2
	 * @param keyword3
	 * @param keyword4
	 * @param remark
	 * @param reporturl
	 */
	@Override
	public void sendWeChatMessage(String templateid, String wxid, String title, String keyword1, String keyword2, String keyword3, String keyword4, String remark, String reporturl){
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
		paramMap.add("templateid", templateid);
		paramMap.add("wxid",wxid);
		paramMap.add("title", title);// 标题
		paramMap.add("keyword1", keyword1);
		paramMap.add("keyword2", keyword2);
		paramMap.add("keyword3", keyword3);
		paramMap.add("keyword4", keyword4);
		paramMap.add("remark", remark);
		paramMap.add("reporturl", reporturl);
		RestTemplate t_restTemplate = new RestTemplate();
		// 让服务器发送信息到相应的微信里
		t_restTemplate.postForObject(menuurl + "/wechat/sendWeChatMessage", paramMap, String.class);
	}

	/**
	 * 修改合作伙伴
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean updateDB(SjxxDto sjxxDto)
	{
		boolean result = dao.updateDB(sjxxDto);
		return result;
	}

	/**
	 * 修改参数扩展（只同步是否收费信息）
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean updateCskz(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.updateCskz(sjxxDto);
	}
	/**
	 * 修改特殊申请
	 * @param sjxxDto
	 * @return
	 */
	public boolean updateTssq(SjxxDto sjxxDto){
		return dao.updateTssq(sjxxDto);
	}

	/**
	 * 特检导入同步85端
	 * @param sjxxDto
	 * @throws BusinessException
	 */
	public void syncImportInspection(SjxxDto sjxxDto) throws BusinessException{
		// TODO Auto-generated method stub
		int result = dao.insert(sjxxDto);
		if(result == 0) {
			log.error("送检信息保存未成功！");
			throw new BusinessException("","送检信息保存未成功！");
		}
		SjjcxmDto sjjcxmDto=new SjjcxmDto();
		sjjcxmDto.setXmglid(StringUtil.generateUUID());
		sjjcxmDto.setSjid(sjxxDto.getSjid());
		sjjcxmDto.setXh("1");
		sjjcxmDto.setJcxmid(sjxxDto.getJcxmid());
		sjjcxmDto.setJczxmid(sjxxDto.getJczxmid());
		//新增送检检测项目
		boolean isSuccess = sjjcxmService.insert(sjjcxmDto);
		if(!isSuccess){
			log.error("送检检测项目信息保存未成功！");
			throw new BusinessException("","送检检测项目信息保存未成功！");
		}
	}

	/**
	 * 修改检测项目名称
	 * @param sjxxDto
	 * @return
	 */
	public boolean updateJcxmmc(SjxxDto sjxxDto){
		return dao.updateJcxmmc(sjxxDto);
	}

	/**
	 * 修改检测单位
	 * @param sjxxDto
	 * @return
	 */
	public boolean updateJcdw(SjxxDto sjxxDto){
		return dao.updateJcdw(sjxxDto);
	}

	@Override
	public void updateSfjsInfo(List<SjxxDto> list) {
		dao.updateSfjsInfo(list);
	}
	@Override
	public void updateFxwcsjByYbbh(SjxxDto sjxxDto){
		 dao.updateFxwcsjByYbbh(sjxxDto);
	}

	public boolean updateFkje(List<SjxxDto> list){
		return  dao.updateFkje(list);
	}
}
