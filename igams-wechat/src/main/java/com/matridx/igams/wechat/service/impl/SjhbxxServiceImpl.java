package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.NoticeTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.HbbgmbglDto;
import com.matridx.igams.wechat.dao.entities.HbdwqxDto;
import com.matridx.igams.wechat.dao.entities.HbhzkhDto;
import com.matridx.igams.wechat.dao.entities.HbsfbzDto;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjhbxxModel;
import com.matridx.igams.wechat.dao.entities.Sjhbxx_Dto;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjjcxmModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import com.matridx.igams.wechat.dao.post.IHbhzkhDao;
import com.matridx.igams.wechat.dao.post.ISjhbxxDao;
import com.matridx.igams.wechat.service.svcinterface.IHbbgmbglService;
import com.matridx.igams.wechat.service.svcinterface.IHbdwqxService;
import com.matridx.igams.wechat.service.svcinterface.IHbsfbzService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.IWxyhService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SjhbxxServiceImpl extends BaseBasicServiceImpl<SjhbxxDto, SjhbxxModel, ISjhbxxDao> implements ISjhbxxService{
	@Autowired
	private IHbsfbzService hbsfbzservice;
	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
	@Autowired
	IWxyhService wxyhService;
	@Autowired
	private IHbdwqxService hbdwqxService;
	@Autowired
	private IHbhzkhDao hbhzkhDao;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IHbbgmbglService hbbgmbglService;
	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	DingTalkUtil talkUtil;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	
	/**
	 * 查询合作伙伴
	 */
	@Override
	public List<SjhbxxDto> getDB(){
		// TODO Auto-generated method stub
		return dao.getDB();
	}
	
	/**
	 * 添加合作伙伴和收费标准
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertAll(SjhbxxDto sjhbxxDto){
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(sjhbxxDto.getHbid())){
			sjhbxxDto.setHbid(StringUtil.generateUUID());
			int result=dao.insertDto(sjhbxxDto);
			if (result==0){
				return false;
			}
			List<HbbgmbglDto> dtos = (List<HbbgmbglDto>) JSON.parseArray(sjhbxxDto.getBgmb_json(), HbbgmbglDto.class);
			if(dtos!=null&&dtos.size()>0){
				List<HbbgmbglDto> list=new ArrayList<>();
				for(HbbgmbglDto dto:dtos){
					if(StringUtil.isNotBlank(dto.getBgmbid())){
						dto.setHbid(sjhbxxDto.getHbid());
						list.add(dto);
					}
				}
				boolean isSuccess = hbbgmbglService.insertList(list);
				if (!isSuccess){
					return false;
				}
			}
		}
		boolean result1=insertsfbz(sjhbxxDto);
		if (!result1){
			return false;
		}
		boolean result2 = insertjcdw(sjhbxxDto);
		if (CollectionUtils.isNotEmpty(sjhbxxDto.getKhids())){
			List<HbhzkhDto> list = new ArrayList<>();
			for (String khid : sjhbxxDto.getKhids()) {
				HbhzkhDto hbhzkhDto = new HbhzkhDto();
				if(sjhbxxDto.getZykh().equals(khid)){
					hbhzkhDto.setZykh("1");
				}
				hbhzkhDto.setHbid(sjhbxxDto.getHbid());
				hbhzkhDto.setKhid(khid);
				list.add(hbhzkhDto);
			}
			hbhzkhDao.insertList(list);
		}

		if (!result2){
			return false;
		}
		amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.ADD_PARTNER.getCode(),  JSONObject.toJSONString(sjhbxxDto));
		return true;
	}
	
	/**
	 * 添加检测单位
	 * @param sjhbxxDto
	 * @return
	 */
	public boolean insertjcdw(SjhbxxDto sjhbxxDto) {
		String[] jcdwids = sjhbxxDto.getJcdwids();
		List<HbdwqxDto> list = new ArrayList<>();
		for (int i = 0; i < jcdwids.length; i++) {
			HbdwqxDto hbdwqx = new HbdwqxDto();	
			hbdwqx.setHbid(sjhbxxDto.getHbid());
			hbdwqx.setXh(Integer.toString(i+1));
			hbdwqx.setJcdw(jcdwids[i]);
			list.add(hbdwqx);
		}
		if(jcdwids.length > 0) {
            return hbdwqxService.insertjcdw(list);
    	}
		return true;
	}
	
	/**
	 * 添加收费标准
	 * @param sjhbxxDto
	 * @return
	 */
	public boolean insertsfbz(SjhbxxDto sjhbxxDto) {
		List<HbsfbzDto> hbsfbzDto=sjhbxxDto.getHbsfbzs();
		if (hbsfbzDto != null){
			for (int i = hbsfbzDto.size()-1; i >=0; i--) {
				if( StringUtil.isNotBlank(hbsfbzDto.get(i).getSfbz()) || StringUtil.isNotBlank(hbsfbzDto.get(i).getTqje())  || StringUtil.isNotBlank(hbsfbzDto.get(i).getXm()) || StringUtil.isNotBlank(hbsfbzDto.get(i).getZxm()) || StringUtil.isNotBlank(hbsfbzDto.get(i).getKsrq()) || StringUtil.isNotBlank(hbsfbzDto.get(i).getJsrq()) ){
					hbsfbzDto.get(i).setHbid(sjhbxxDto.getHbid());
					hbsfbzDto.get(i).setLrry(sjhbxxDto.getLrry());
					hbsfbzDto.get(i).setHbsfbzid(StringUtil.generateUUID());
				}else {
					hbsfbzDto.remove(i);
				}
			}
			if(hbsfbzDto.size() > 0) {
                return hbsfbzservice.insertsfbz(hbsfbzDto);
			}
		}
		return true;
	}
	/**
	 * 批量修改收费标准（有则修改，无则跳过）
	 * @param sjhbxxDto
	 * @return
	 */
	public Map<String,Object> batchModSfbz(SjhbxxDto sjhbxxDto) {
		Map<String,Object> map = new HashMap<>();
		Map<String,Object> param = new HashMap<>();
		List<HbsfbzDto> hbsfbzDtos=new ArrayList<>();
		List<HbsfbzDto> list=(List<HbsfbzDto>) JSON.parseArray(sjhbxxDto.getSfbz_json(),HbsfbzDto.class);
		for (int i=0; i<list.size(); i++){
			if( StringUtil.isNotBlank(list.get(i).getSfbz()) || StringUtil.isNotBlank(list.get(i).getTqje()) || StringUtil.isNotBlank(list.get(i).getKsrq()) || StringUtil.isNotBlank(list.get(i).getJsrq()) ){
				hbsfbzDtos.add(list.get(i));
			}
		}
		List<HbsfbzDto> modList = new ArrayList<>();
		List<HbsfbzDto> zxmisnullList = new ArrayList<>();
		List<HbsfbzDto> zxmisnotnullList = new ArrayList<>();
		for (String hbid: sjhbxxDto.getIds()){
			for (HbsfbzDto hbsfbzDto : hbsfbzDtos){
				HbsfbzDto dto = new HbsfbzDto();
				dto.setHbid(hbid);
				dto.setXm(hbsfbzDto.getXm());
				dto.setSfbz(hbsfbzDto.getSfbz());
				dto.setTqje(hbsfbzDto.getTqje());
				dto.setZxm(hbsfbzDto.getZxm());
				dto.setKsrq(hbsfbzDto.getKsrq());
				dto.setJsrq(hbsfbzDto.getJsrq());
				dto.setLrry(sjhbxxDto.getLrry());
				modList.add(dto);
				if (StringUtil.isNotBlank(dto.getZxm())){
					zxmisnotnullList.add(dto);
				}else{
					zxmisnullList.add(dto);
				}
			}
		}
		//更新之前，先检查数据中是否包含商务合同明细有效且状态为10or30的数据，包含则返回不做更新操作，由于xm可能出现null，无法使用相等，也无法使用in，故将xm为null和不为null的分开查询
		List<HbsfbzDto> hbsfbzDtoList = new ArrayList<>();
		List<HbsfbzDto> hbsfbzDtoList1 = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(zxmisnotnullList))
			hbsfbzDtoList = hbsfbzservice.getYxAndZt(zxmisnotnullList);
		if (CollectionUtils.isNotEmpty(zxmisnullList))
			hbsfbzDtoList1 = hbsfbzservice.getYxAndZtZxmIsNull(zxmisnullList);
		if (CollectionUtils.isNotEmpty(hbsfbzDtoList)||CollectionUtils.isNotEmpty(hbsfbzDtoList1)){
			String htbh="";
			for (HbsfbzDto hbsfbzDto:hbsfbzDtoList) {
				if (StringUtil.isNotBlank(hbsfbzDto.getHtbh()))
					htbh = htbh+","+hbsfbzDto.getHtbh();
			}
			for (HbsfbzDto hbsfbzDto:hbsfbzDtoList1) {
				if (StringUtil.isNotBlank(hbsfbzDto.getHtbh()))
					htbh = htbh+","+hbsfbzDto.getHtbh();
			}
			if (StringUtil.isNotBlank(htbh))
				htbh = htbh.substring(1);
			map.put("status","fail");
			map.put("message","请先将一下合同的明细信息设置为无效，合同单号如下："+htbh);
			return map;
		}
		if (CollectionUtils.isNotEmpty(modList)){
			//将页面的所有数据都做更新，根据hbid和xm及zxm相等的更新
			hbsfbzservice.batchModSfbz(modList);
			//伙伴没有的收费标准做新增处理，这句必须在更新操作以后执行，因为使用多字段做差集筛选除需要更新的
			List<HbsfbzDto> addList = hbsfbzservice.getAddFromTmp(modList);
			if (CollectionUtils.isNotEmpty(addList)){
				for(HbsfbzDto hbsfbzDto:addList){
					hbsfbzDto.setHbsfbzid(StringUtil.generateUUID());
				}
				hbsfbzservice.insertsfbz(addList);
			}
			param.put("modList",modList);
			param.put("addList",addList);
			amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.BatchMod_SFBZ.getCode(),  JSONObject.toJSONString(param));
		}
		map.put("status","success");
		map.put("message",xxglService.getModelById("ICOM00001").getXxnr());
		return map;
	}
	/**
	 * 删除合作伙伴
	 * @param sjhbxxDto
	 * @return
	 */
	@Override
	public boolean deletepartner(SjhbxxDto sjhbxxDto){
		// TODO Auto-generated method stub
		boolean result=dao.deletepartner(sjhbxxDto);
		amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.DEL_PARTNER.getCode(),  JSONObject.toJSONString(sjhbxxDto));
        return result;
    }
	
	
	/**
	 * 修改所有
	 * @param sjhbxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateAll(SjhbxxDto sjhbxxDto) {
		//更新合作伙伴信息
		dao.updatePageEvent(sjhbxxDto);
		//删除原有伙伴报告模板
		hbbgmbglService.deleteById(sjhbxxDto.getHbid());
		List<HbbgmbglDto> dtos = (List<HbbgmbglDto>) JSON.parseArray(sjhbxxDto.getBgmb_json(), HbbgmbglDto.class);
		if(dtos!=null&&dtos.size()>0){
			List<HbbgmbglDto> list=new ArrayList<>();
			for(HbbgmbglDto dto:dtos){
				if(StringUtil.isNotBlank(dto.getBgmbid())){
					dto.setHbid(sjhbxxDto.getHbid());
					list.add(dto);
				}
			}
			hbbgmbglService.insertList(list);
		}
	    //删除原有收费标准再重新添加
		hbsfbzservice.deleteById(sjhbxxDto.getHbid());
	   	insertsfbz(sjhbxxDto);
	    //删除原有检测单位再重新添加
	   	hbdwqxService.deleteById(sjhbxxDto.getHbid());
	   	HbhzkhDto hbhzkhDto = new HbhzkhDto();
		hbhzkhDto.setHbid(sjhbxxDto.getHbid());
	   	hbhzkhDao.delete(hbhzkhDto);
		if (CollectionUtils.isNotEmpty(sjhbxxDto.getKhids())){
			List<HbhzkhDto> list = new ArrayList<>();
			for (String khid : sjhbxxDto.getKhids()) {
				HbhzkhDto dto = new HbhzkhDto();
				if(sjhbxxDto.getZykh().equals(khid)){
					dto.setZykh("1");
				}
				dto.setHbid(sjhbxxDto.getHbid());
				dto.setKhid(khid);
				list.add(dto);
			}
			hbhzkhDao.insertList(list);
		}
	   	insertjcdw(sjhbxxDto);
		amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MOD_PARTNER.getCode(),  JSONObject.toJSONString(sjhbxxDto));
		return true;
	}
	/**
	 * 查询用户id
	 * @return
	 */
	@Override
	public List<SjhbxxDto> getXtyh(){
		// TODO Auto-generated method stub
		return dao.getXtyh();
	}
	
	@Override
	public SjhbxxDto getDto(SjhbxxDto sjhbxxDto){
		
		SjhbxxDto t_sjhbxxDto = dao.getDto(sjhbxxDto);
		
		if(t_sjhbxxDto != null) {
			HbsfbzDto t_hbsfbzDto = new HbsfbzDto();
			t_hbsfbzDto.setHbid(t_sjhbxxDto.getHbid());
			
			List<SjjcxmDto> sjjcxmDtos = sjhbxxDto.getSjxms();
			
			if(sjjcxmDtos != null) {
				for(SjjcxmModel sjjcxmModel:sjjcxmDtos) {
					
					t_hbsfbzDto.setXm(sjjcxmModel.getJcxmid());
					HbsfbzDto hbsfbzDto = hbsfbzservice.getDto(t_hbsfbzDto);
					
					if(hbsfbzDto != null) {
						t_sjhbxxDto.setSfbz(addsf(hbsfbzDto.getSfbz(),t_sjhbxxDto.getSfbz()));
					}else {
						HbsfbzDto d_hbsfbzDto = hbsfbzservice.getDefaultDto(t_hbsfbzDto);
						if(d_hbsfbzDto != null) {
							t_sjhbxxDto.setSfbz(addsf(d_hbsfbzDto.getSfbz(),t_sjhbxxDto.getSfbz()));
						}
					}
				}
			}
		}else{
			t_sjhbxxDto = new SjhbxxDto();
			List<SjjcxmDto> sjjcxmDtos = sjhbxxDto.getSjxms();
			
			if(sjjcxmDtos != null) {
				for(SjjcxmModel sjjcxmModel:sjjcxmDtos) {
					HbsfbzDto t_hbsfbzDto = new HbsfbzDto();
					t_hbsfbzDto.setXm(sjjcxmModel.getJcxmid());
					HbsfbzDto d_hbsfbzDto = hbsfbzservice.getDefaultDto(t_hbsfbzDto);
					if(d_hbsfbzDto != null) {
						t_sjhbxxDto.setSfbz(addsf(d_hbsfbzDto.getSfbz(),t_sjhbxxDto.getSfbz()));
					}
				}
			}
		}
		
		return t_sjhbxxDto;
	}
	
	/**
	 * 合计计算收费
	 * @param prestr
	 * @param aftstr
	 * @return
	 */
	private String addsf(String prestr,String aftstr) {
		BigDecimal big1 = new BigDecimal("0");
		BigDecimal big2 = new BigDecimal("0");
		if(StringUtil.isNotBlank(prestr)) {
			try {
				big1 = new BigDecimal(prestr);
			}catch(Exception e) {
				
			}
		}
		if(StringUtil.isNotBlank(aftstr)) {
			try {
				big2 = new BigDecimal(aftstr);
			}catch(Exception e) {
				
			}
		}
		
		return big1.add(big2).toString();
	}

	/**
	 * 查询伙伴为'无'的用户信息
	 * @return
	 */
	@Override
	public List<SjhbxxDto> getXtyhByHbmc() {
		// TODO Auto-generated method stub
		return dao.getXtyhByHbmc();
	}
	
	/**
	 * 查询合作伙伴是否存在
	 * @param sjhbxxDto
	 * @return
	 */
	@Override
	public SjhbxxDto selectSjhb(SjhbxxDto sjhbxxDto) {
		return dao.selectSjhb(sjhbxxDto);
	}
	
	/**
	 * 获取合作伙伴发送方式
	 */
	@Override
	public List<String> getSendMode(){
		List<String> sendmodelist = new ArrayList<>();
		for (NoticeTypeEnum dir : NoticeTypeEnum.values()) { 
			sendmodelist.add(dir.getCode());
		}
		return sendmodelist;
	}

	/**
	 * 根据接受日期获取代表信息(周报)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjhbxxDto> selectDtoByJsrq(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.selectDtoByJsrq(sjxxDto);
	}

	/**
	 * 查询合作伙伴分类信息
	 * @return
	 */
	@Override
	public List<SjhbxxDto> selectFl() {
		// TODO Auto-generated method stub
		SjhbxxDto sjhbxxDto = new SjhbxxDto();
		sjhbxxDto.setJclb(BasicDataTypeEnum.CLASSIFY.getCode());
		List<SjhbxxDto> sjhbxxDtos = dao.selectFl(sjhbxxDto);
		if(sjhbxxDtos != null){
			List<SjhbxxDto> fllist = new ArrayList<>();
			List<Sjhbxx_Dto> zfllist = new ArrayList<>();
			List<Sjhbxx_Dto> sjhblist = new ArrayList<>();
			String prefl = "";
			String prezfl = "";
			for (int i = 0; i < sjhbxxDtos.size(); i++) {
				String jsonString = JSONObject.toJSONString(sjhbxxDtos.get(i));
				JSONObject parseObject = JSONObject.parseObject(jsonString);
				Sjhbxx_Dto sjhbxx_Dto = JSONObject.toJavaObject(parseObject, Sjhbxx_Dto.class);
				if(prefl.equals(sjhbxxDtos.get(i).getFl())){
					sjhblist.add(sjhbxx_Dto);
					if(prezfl != null && !prezfl.equals(sjhbxxDtos.get(i).getZfl())){
						zfllist.add(sjhbxx_Dto);
					}
				}else{
					if(i > 0){
						sjhbxxDtos.get(i-1).setZfllist(zfllist);
						sjhbxxDtos.get(i-1).setSjhblist(sjhblist);
						fllist.add(sjhbxxDtos.get(i-1));
						zfllist = new ArrayList<>();
						sjhblist = new ArrayList<>();
					}
					zfllist.add(sjhbxx_Dto);
					sjhblist.add(sjhbxx_Dto);
				}
				if(i == sjhbxxDtos.size()-1){
					sjhbxxDtos.get(i).setZfllist(zfllist);
					sjhbxxDtos.get(i).setSjhblist(sjhblist);
					fllist.add(sjhbxxDtos.get(i));
				}
				
				prefl = sjhbxxDtos.get(i).getFl();
				prezfl = sjhbxxDtos.get(i).getZfl();
			}
			return fllist;
		}
		return new ArrayList<>();
	}


	/**
	 * 查询合作伙伴分类信息 新
	 * @return
	 */
	@Override
	public List<SjhbxxDto> getSjhbFlAndZfl(){
		List<JcsjDto> jc_fllist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.CLASSIFY.getCode());
		List<JcsjDto> jc_zfllist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SUBCLASSIFICATION.getCode());
		List<SjhbxxDto> fllist = new ArrayList<>();
		for (JcsjDto fl : jc_fllist) {
			SjhbxxDto sjhbxxDto = new SjhbxxDto();
			sjhbxxDto.setFl(fl.getCsid());
			sjhbxxDto.setFlmc(fl.getCsmc());
			sjhbxxDto.setFldm(fl.getCsdm());
			fllist.add(sjhbxxDto);
		}
		for (JcsjDto zfl : jc_zfllist) {
			for (SjhbxxDto sjhbxxDto : fllist) {
				if (sjhbxxDto.getFl().equals(zfl.getFcsid())){
					List<Sjhbxx_Dto> zfllist = sjhbxxDto.getZfllist();
					Sjhbxx_Dto sjhbxx_Dto = new Sjhbxx_Dto();
					sjhbxx_Dto.setZfl(zfl.getCsid());
					sjhbxx_Dto.setZflmc(zfl.getCsmc());
					sjhbxx_Dto.setZfldm(zfl.getCsdm());
					if (zfllist != null && zfllist.size()>0){
						zfllist.add(sjhbxx_Dto);
					}else {
						zfllist = new ArrayList<>();
						zfllist.add(sjhbxx_Dto);
					}
					sjhbxxDto.setZfllist(zfllist);
					break;
				}
			}
		}
		return fllist;
	}

	/**
	 * 验证合作伙伴是否已经存在
	 * @param sjhbxxDto
	 * @return
	 */
	@Override
	public int getCountSjhb(SjhbxxDto sjhbxxDto){
		// TODO Auto-generated method stub
		return dao.getCountSjhb(sjhbxxDto);
	}
	
	/**
	 * 通过hbid查询 合作伙伴
	 * @param list
	 * @return
	 */
	@Override
	public List<String> getHbmcByHbid(List<String> list){
		// TODO Auto-generated method stub
		return dao.getHbmcByHbid(list);
	}

	/**
	 * 查询伙伴统计信息
	 * @param sjhbxxDto
	 * @return
	 */
	@Override
	public List<SjhbxxDto> getTjDtoList(SjhbxxDto sjhbxxDto) {
		// TODO Auto-generated method stub
		return dao.getTjDtoList(sjhbxxDto);
	}

	/**
	 * 微信用户查询伙伴权限分类信息 
	 * @param sjhbxxDto
	 * @return
	 */
	@Override
	public List<SjhbxxDto> selectFlByHbqx(SjhbxxDto sjhbxxDto)
	{
		// TODO Auto-generated method stub
		sjhbxxDto.setJclb(BasicDataTypeEnum.CLASSIFY.getCode());
		return dao.selectFlByHbqx(sjhbxxDto);
	}

	/**
	 * 微信周报伙伴分类查询
	 * @param sjhbxxDto
	 * @return
	 */
	@Override
	public List<SjhbxxDto> selectFlByWeekly(SjhbxxDto sjhbxxDto)
	{
		// TODO Auto-generated method stub
		sjhbxxDto.setJclb(BasicDataTypeEnum.CLASSIFY.getCode());
		List<SjhbxxDto> sjhbxxDtos = dao.selectFlByHbqx(sjhbxxDto);
		if(sjhbxxDtos != null){
			List<SjhbxxDto> fllist = new ArrayList<>();
			List<Sjhbxx_Dto> zfllist = new ArrayList<>();
			List<Sjhbxx_Dto> sjhblist = new ArrayList<>();
			String prefl = "";
			String prezfl = "";
			for (int i = 0; i < sjhbxxDtos.size(); i++) {
				String jsonString = JSONObject.toJSONString(sjhbxxDtos.get(i));
				JSONObject parseObject = JSONObject.parseObject(jsonString);
				Sjhbxx_Dto sjhbxx_Dto = JSONObject.toJavaObject(parseObject, Sjhbxx_Dto.class);
				if(prefl.equals(sjhbxxDtos.get(i).getFl())){
					sjhblist.add(sjhbxx_Dto);
					if(prezfl != null && !prezfl.equals(sjhbxxDtos.get(i).getZfl())){
						zfllist.add(sjhbxx_Dto);
					}
				}else{
					if(i > 0){
						sjhbxxDtos.get(i-1).setZfllist(zfllist);
						sjhbxxDtos.get(i-1).setSjhblist(sjhblist);
						fllist.add(sjhbxxDtos.get(i-1));
						zfllist = new ArrayList<>();
						sjhblist = new ArrayList<>();
					}
					zfllist.add(sjhbxx_Dto);
					sjhblist.add(sjhbxx_Dto);
				}
				if(i == sjhbxxDtos.size()-1){
					sjhbxxDtos.get(i).setZfllist(zfllist);
					sjhbxxDtos.get(i).setSjhblist(sjhblist);
					fllist.add(sjhbxxDtos.get(i));
				}
				
				prefl = sjhbxxDtos.get(i).getFl();
				prezfl = sjhbxxDtos.get(i).getZfl();
			}
			return fllist;
		}
		return new ArrayList<>();
	}

	/**
	 * 根据用户id伙伴权限查询伙伴统计信息 
	 * @param sjhbxxDto
	 * @return
	 */
	@Override
	public List<SjhbxxDto> getSjhbDtoListByHbqx(SjhbxxDto sjhbxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getSjhbDtoListByHbqx(sjhbxxDto);
	}
	/**
	 * 查询不为空的伙伴统计信息 
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getnotNullDb(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getnotNullDb(sjxxDto);
	}
	/**
	 * 查询不为空的伙伴统计信息 (接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getnotNullDbByJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getnotNullDbByJsrq(sjxxDto);
	}

	/**
	 * 查询代表信息(权限)
	 * @param ddid
	 * @param wxid
	 * @return
	 */
	@Override
	public List<SjhbxxDto> getDbList(String ddid, String wxid) {
		// TODO Auto-generated method stub
		List<SjhbxxDto> dbList = new ArrayList<>();
		SjxxDto sjxxDto = new SjxxDto();
		if(StringUtil.isNotBlank(ddid)){
			sjxxDto.setDdid(ddid);
			dbList = dao.getDbByDdid(sjxxDto);
		}
		if(StringUtil.isNotBlank(wxid)){
			WxyhDto wxyhDto  = new WxyhDto();
			wxyhDto.setWxid(wxid);
			//查有系统用户的
			List<WxyhDto> wxyhlist = wxyhService.getListBySameId(wxyhDto);
			if(wxyhlist!= null && wxyhlist.size() > 0){
				dbList = dao.getDbByWxid(wxyhlist);
			}
		}
		return dbList;
	}
	
	/**
	 * 恢复删除伙伴
	 * @param sjhbxxDto
	 * @return
	 */
	public  boolean resumepartner(SjhbxxDto sjhbxxDto) {
		return dao.resumepartner(sjhbxxDto);
	}

	/**
	 * 查询伙伴为无的报告模板
	 * @return
	 */
	@Override
	public List<SjhbxxDto> getBgmbByHbmc(SjhbxxDto sjhbxxDto) {
		// TODO Auto-generated method stub
		return dao.getBgmbByHbmc(sjhbxxDto);
	}

	/**
	 * 启用合作伙伴
	 * @param sjhbxxDto
	 * @return
	 */
	@Override
	public boolean enablepartner(SjhbxxDto sjhbxxDto) {
		boolean result=dao.enablepartner(sjhbxxDto);
		sjhbxxDto.setScbj("0");
		amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.ENABLE_OR_DISABLE_PARTNER.getCode(),  JSONObject.toJSONString(sjhbxxDto));
        return result;

    }
	/**
	 * 停用合作伙伴
	 * @param sjhbxxDto
	 * @return
	 */
	@Override
	public boolean disablepartner(SjhbxxDto sjhbxxDto) {
		boolean result=dao.disablepartner(sjhbxxDto);
		sjhbxxDto.setScbj("2");
		amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.ENABLE_OR_DISABLE_PARTNER.getCode(),  JSONObject.toJSONString(sjhbxxDto));
        return result;
    }

	/**
	 * 更新直销的cskz3为1
	 * @return
	 */
	public boolean updateCskz3(){
		return dao.updateCskz3();
	}
	/**
	 * 更新大区信息
	 * @return
	 */
	public boolean updateDqxx(){
		return dao.updateDqxx();
	}
	/**
	 * 查询重复绑定大区的送检伙伴
	 * @return
	 */
	public List<SjhbxxDto> getRepeatHbxx(){
		return dao.getRepeatHbxx();
	}

	@Override
	public List<SjhbxxDto> getWbcxDtoList() {
		return dao.getWbcxDtoList();
	}

	/**
	 * 根据选择信息获取导出信息
	 * @param params
	 * @return
	 */
	public List<SjhbxxDto> getListForSelectExp(Map<String,Object> params){
		SjhbxxDto sjhbxxDto = (SjhbxxDto)params.get("entryData");
		queryJoinFlagExport(params,sjhbxxDto);
		return dao.getListForSelectExp(sjhbxxDto);
	}


	/**
	 * 根据搜索条件获取导出条数
	 *
	 * @param params
	 * @return
	 */
	public int getCountForSearchExp(SjhbxxDto sjhbxxDto,Map<String, Object> params) {
        return dao.getCountForSearchExp(sjhbxxDto);
	}
	/**
	 * 根据搜索条件分页获取导出信息
	 *
	 * @param params
	 * @return
	 */

	public List<SjhbxxDto> getListForSearchExp(Map<String, Object> params)
	{
		SjhbxxDto sjhbxxDto = (SjhbxxDto)params.get("entryData");
		queryJoinFlagExport(params,sjhbxxDto);

        return dao.getListForSearchExp(sjhbxxDto);
	}
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String,Object> params,SjhbxxDto sjhbxxDto){
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuffer sqlParam = new StringBuffer();
		for(DcszDto dcszDto:choseList){
			if(dcszDto == null || dcszDto.getDczd() == null)
				continue;

			sqlParam.append(",");
			if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		String sqlcs=sqlParam.toString();
		sjhbxxDto.setSqlParam(sqlcs);
	}

	/**
	 * 根据用户获取平台信息
	 * @param str
	 * @return
	 */
	public List<String> getPtgsByYhid(String str){
		return dao.getPtgsByYhid(str);
	}

	/**
	 * 根据平台归属查询伙伴统计信息
	 * @param sjhbxxDto
	 * @return
	 */
	public List<SjhbxxDto> getListFromXxdy(SjhbxxDto sjhbxxDto){
		return dao.getListFromXxdy(sjhbxxDto);
	}

	/**
	 * 查询不为空的伙伴统计信息 (接收日期)
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, Object>> getNotNullDbFromXxdy(SjxxDto sjxxDto){
		return dao.getNotNullDbFromXxdy(sjxxDto);
	}


	/**
	 * 用户已锁定伙伴通知
	 */
	public void sendLockedUserInfo(){
		List<SjhbxxDto> lockedUserList = dao.getLockedUserList();
		if(lockedUserList!=null&&!lockedUserList.isEmpty()){
			StringBuilder ids= new StringBuilder();
			for(SjhbxxDto sjhbxxDto:lockedUserList){
				ids.append(",").append(sjhbxxDto.getHbid());
			}
			if(StringUtil.isNotBlank(ids.toString())){
				ids = new StringBuilder(ids.substring(1));
			}
			int size = lockedUserList.size();
			StringBuilder info= new StringBuilder();
			if(size>1){
				info.append(lockedUserList.get(0).getHbmc()).append(",").append(lockedUserList.get(1).getHbmc());
			}else{
				info.append(lockedUserList.get(0).getHbmc());
			}
			List<DdxxglDto> ddxxList=ddxxglService.selectByDdxxlx(DingMessageType.LOCKED_USER_PARTNER.getCode());
			if(ddxxList!=null&&!ddxxList.isEmpty()){
				for (DdxxglDto ddxxglDto : ddxxList) {
					if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {// 内网访问
						String internalbtn = applicationurl + "/ws/partner/getLockedUserView?ids=" + ids;
						List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
						OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
						btnJsonList.setTitle("详细");
						btnJsonList.setActionUrl(internalbtn);
						btnJsonLists.add(btnJsonList);
						talkUtil.sendCardMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(),  xxglService.getMsg("ICOMM_HB00001"), StringUtil.replaceMsg( xxglService.getMsg("ICOMM_HB00002"),
								DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"),String.valueOf(info), String.valueOf(size)), btnJsonLists, "1");
					}
				}
			}
		}
	}

	/**
	 * 备份伙伴信息
	 */
	public void backUpPartnerInfo(){
		String backUpPartnerMaxDate = dao.getBackUpPartnerMaxDate();
		dao.backUpPartnerInfo(backUpPartnerMaxDate);
	}
}
