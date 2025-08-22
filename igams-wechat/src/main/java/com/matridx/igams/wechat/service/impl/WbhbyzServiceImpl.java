package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.matridx.igams.common.dao.entities.DwblxxDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.WbaqyzDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IOutWardService;
import com.matridx.igams.common.service.svcinterface.IWbaqyzService;
import com.matridx.igams.wechat.dao.entities.SjbgsmDto;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.WbhbyzDto;
import com.matridx.igams.wechat.dao.entities.WbhbyzModel;
import com.matridx.igams.wechat.dao.post.IWbhbyzDao;
import com.matridx.igams.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.igams.wechat.service.svcinterface.IWbhbyzService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WbhbyzServiceImpl extends BaseBasicServiceImpl<WbhbyzDto, WbhbyzModel, IWbhbyzDao> implements IWbhbyzService, IOutWardService {
	private static final Logger log = LoggerFactory.getLogger(WbhbyzServiceImpl.class);
	@Autowired
	private ISjbgsmService sjbgsmService;

	@Autowired
	private IWbaqyzService wbaqyzService;

	@Autowired(required=false)
	private AmqpTemplate amqpTempl;

	@Autowired
	IFjcfbService fjcfbService;

	@Autowired
	ICommonService commonService;
	@Autowired
	RedisUtil redisUtil;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	@Value("${matridx.rabbit.flg:}")
	private String rabbitFlg;
//	private Logger log = LoggerFactory.getLogger(WbhbyzServiceImpl.class);

	/**
	 * 根据代码查询合作伙伴
	 * @param code
	 * @return
	 */
	@Override
	public List<WbhbyzDto> getSjhbByCode(String code) {
		// TODO Auto-generated method stub
		Object o = redisUtil.get("Wbhbyz:" + code);
		try {
			if ( o != null){
				return JSON.parseArray(o.toString(),WbhbyzDto.class);
			}
		} catch (Exception e) {
			log.error("redis获取数据异常！");
		}
		List<WbhbyzDto> list = dao.getSjhbByCode(code);
		redisUtil.set("Wbhbyz:" + code, JSON.toJSONString(list),21600);
		return list;
	}
	/**
	 * 查询合作伙伴
	 * @return
	 */
	@Override
	public List<WbhbyzDto> getSjhbAll() {
		// TODO Auto-generated method stub
		return dao.getSjhbAll();
	}

	@Override
	public List<WbhbyzDto> getListByCode(WbhbyzDto wbhbyzDto) {
		return dao.getListByCode(wbhbyzDto);
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public Boolean modInfo(WbaqyzDto wbaqyzDto){
		DBEncrypt dBEncrypt = new DBEncrypt();
		wbaqyzDto.setKey(dBEncrypt.eCode(wbaqyzDto.getKey()));
		wbaqyzDto.setWord(dBEncrypt.eCode(wbaqyzDto.getWord()));
		wbaqyzService.updateDto(wbaqyzDto);
		WbhbyzDto wbhbyzDto = new WbhbyzDto();
		wbhbyzDto.setCode(wbaqyzDto.getCode());
		wbhbyzDto.setBeforeCode(wbaqyzDto.getBeforeCode());
		dao.update(wbhbyzDto);
		commonService.updateXtyh(wbaqyzDto.getCode(),wbaqyzDto.getKey(),wbaqyzDto.getBeforeCode(),wbaqyzDto.getMc());
		return true;
	}


	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public Boolean delInfo(WbaqyzDto wbaqyzDto) throws BusinessException {
		WbhbyzDto wbhbyzDto = new WbhbyzDto();
		wbhbyzDto.setIds(wbaqyzDto.getIds());
		dao.delete(wbhbyzDto);
		boolean isSuccess= wbaqyzService.delete(wbaqyzDto);
		if (!isSuccess){
			throw new BusinessException("msg","删除主表信息失败！");
		}else{
			commonService.deleteXtyh(wbaqyzDto.getIds());
		}
		return true;
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public Boolean addSavewbaqyzpartner(SjhbxxDto sjhbxxDto) throws BusinessException {
		if (!StringUtil.isNotBlank(sjhbxxDto.getCode()))
			throw new BusinessException("msg","为获取到code！");
		WbhbyzDto wbhbyzDto = new WbhbyzDto();
		wbhbyzDto.setCode(sjhbxxDto.getCode());
		dao.delete(wbhbyzDto);
		redisUtil.del("Wbhbyz:"+sjhbxxDto.getCode());
		if (StringUtil.isNotBlank(sjhbxxDto.getT_xzrys())){
			String[] res = sjhbxxDto.getT_xzrys().split(",");
			List<WbhbyzDto> wbhbyzDtos = new ArrayList<>();
			for (String re : res) {
				WbhbyzDto dto=new WbhbyzDto();
				dto.setCode(sjhbxxDto.getCode());
				dto.setHbid(re);
				wbhbyzDtos.add(dto);
			}
			dao.insertPartner(wbhbyzDtos);
			redisUtil.set("Wbhbyz:" + sjhbxxDto.getCode(), JSONObject.toJSONString(wbhbyzDtos), 21600);
		}
		return true;
	}
	@Override
	public boolean getInfoParameters(String str) {
		List<WbhbyzDto> wbhbyzDtos =(List<WbhbyzDto>) JSON.parseArray(str,WbhbyzDto.class);
//		WbhbyzDto wbhbyzDto1=dao.getInfoByHbid(wbhbyzDtos.get(0).getHbid());
		SjbgsmDto sjbgsmDto = new SjbgsmDto();
		sjbgsmDto.setSjid(wbhbyzDtos.get(0).getSjid());
		sjbgsmDto.setCsid(wbhbyzDtos.get(0).getCsid());
		List<SjbgsmDto> sjbgsmDtos = sjbgsmService.selectSjbgBySjid(sjbgsmDto);
		List<DwblxxDto> dwblxxDtos = new ArrayList<>();
		if (null != sjbgsmDtos && sjbgsmDtos.size()>0){
			for (SjbgsmDto sjbgsmDto1:sjbgsmDtos) {
				DwblxxDto dwblxxDto = new DwblxxDto();
				dwblxxDto.setHzxm(sjbgsmDto1.getHzxm());
				dwblxxDto.setXb(sjbgsmDto1.getXb());
				dwblxxDto.setNl(sjbgsmDto1.getNl());
				dwblxxDto.setJclxmc(sjbgsmDto1.getJcxmmc());
				dwblxxDto.setJg(sjbgsmDto1.getGgzdzb());
				dwblxxDto.setYsjg(sjbgsmDto1.getYszb());
				dwblxxDto.setBbbh(sjbgsmDto1.getYbbh());
				dwblxxDto.setDh(sjbgsmDto1.getDh());
				dwblxxDto.setNbbm(sjbgsmDto1.getNbbm());
				dwblxxDto.setSjdw(sjbgsmDto1.getHospitalname());
				dwblxxDto.setKsmc(sjbgsmDto1.getKsmc());
				dwblxxDto.setSjys(sjbgsmDto1.getSjys());
				dwblxxDto.setBblxmc(sjbgsmDto1.getYblxmc());
				dwblxxDto.setCyrq(sjbgsmDto1.getCyrq());
				dwblxxDto.setJsrq(sjbgsmDto1.getJsrq());
				dwblxxDto.setBgrq(sjbgsmDto1.getBgrq());
				dwblxxDtos.add(dwblxxDto);
			}
		}
		String ywlx = sjbgsmDtos.get(0).getCskz3()+"_"+sjbgsmDtos.get(0).getJclx() ;
		if (StringUtil.isNotBlank(wbhbyzDtos.get(0).getWord())){
			ywlx += "_WORD";
		}
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(ywlx);
		fjcfbDto.setYwid(wbhbyzDtos.get(0).getSjid());
		FjcfbDto fjcfbModel = fjcfbService.getDto(fjcfbDto);
		DBEncrypt encrypt = new DBEncrypt();
		FileSystemResource fileSystemResource = null;
		if (null!=fjcfbModel){
			String filePath = encrypt.dCode(fjcfbModel.getWjlj());
			if(StringUtil.isNotBlank(filePath)){
				fileSystemResource = new FileSystemResource(filePath);
			}
		}
		if (null != wbhbyzDtos){
			if (StringUtil.isNotBlank(wbhbyzDtos.get(0).getAddress())){
				RestTemplate restTemplate=new RestTemplate();
				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
				//送检信息
				SimplePropertyPreFilter filter = new SimplePropertyPreFilter(DwblxxDto.class, "hzxm","xb","nl","jclxmc","jg","ysjg","bbbh","nbbm", "dh", "sjdw", "ksmc", "sjys", "bblxmc", "cyrq", "jsrq", "bgrq");
				SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;
				paramMap.add("parameters", JSONObject.toJSONString(dwblxxDtos,listFilters));
				if (null!=fileSystemResource){
					paramMap.add("file", fileSystemResource);
					try{
						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.MULTIPART_FORM_DATA);
						HttpEntity<MultiValueMap<String,Object>> requestEntity  = new HttpEntity<>(paramMap, headers);
						String reString = restTemplate.postForObject(wbhbyzDtos.get(0).getAddress(), requestEntity, String.class);
						if (!"OK".equals(reString)) {
							sendMessage(wbhbyzDtos);
						}
					}catch (Exception e){
						sendMessage(wbhbyzDtos);
					}
				}
			}
		}
		return true;
	}

	public boolean sendMessage(List<WbhbyzDto> wbhbyzDtos){
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> parameters = new HashMap<>();
		Integer cwcs = wbhbyzDtos.get(0).getCwcs();
		if (StringUtil.isBlank(String.valueOf(cwcs))){
			cwcs = 0;
		}else{
			cwcs += 1;
		}
		if (cwcs >3){
			return true;
		}
		String str = String.valueOf(this.getClass());
		String[] temps = str.split("\\.");
		String method = null;
		if (null!= temps && temps.length > 0){
			String temp = temps[temps.length-1];
			if (StringUtil.isNotBlank(temp)){
				String strBegin = temp.substring(0,1).toLowerCase();
				String strEnd = temp.substring(1);
				method = strBegin+strEnd;
			}
		}
		if (StringUtil.isBlank(method)){
			return true;
		}
		parameters.put("cwcs",cwcs);
		parameters.put("sjid",wbhbyzDtos.get(0).getSjid());
		parameters.put("csid",wbhbyzDtos.get(0).getCsid());
		parameters.put("address",wbhbyzDtos.get(0).getAddress());
		parameters.put("word",StringUtil.isNotBlank(wbhbyzDtos.get(0).getWord())?wbhbyzDtos.get(0).getWord():"");
		parameters.put("hbid",wbhbyzDtos.get(0).getHbid());
		map.put("method",method);
		map.put("parameters","["+JSONObject.toJSONString(parameters)+"]");
		amqpTempl.convertAndSend("xx_delay_exchange", preRabbitFlg+"xx_delay_key"+rabbitFlg, JSONObject.toJSONString(map), new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				message.getMessageProperties().setExpiration("600000");
				return message;
			}
		});
		return true;
	}
}
