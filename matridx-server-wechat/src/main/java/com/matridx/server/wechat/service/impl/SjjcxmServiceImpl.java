package com.matridx.server.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusinessTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.SjjcxmDto;
import com.matridx.server.wechat.dao.entities.SjjcxmModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.post.ISjjcxmDao;
import com.matridx.server.wechat.dao.post.ISjxxDao;
import com.matridx.server.wechat.service.svcinterface.IHbsfbzService;
import com.matridx.server.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SjjcxmServiceImpl extends BaseBasicServiceImpl<SjjcxmDto, SjjcxmModel, ISjjcxmDao> implements ISjjcxmService{

	@Autowired
	RedisUtil redisUtil;
	@Autowired
	ISjxxDao sjxxDao;
	@Autowired
	IHbsfbzService hbsfbzService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Override
	public Boolean updateListNew(List<SjjcxmDto> list) {
		return dao.updateListNew(list)!=0;
	}
	/**
	 * 根据送检信息新增检测项目
	 * @param sjxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertBySjxx(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		String json = null;
		List<JcsjDto> jcxmList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
		try {
			if (sjxxDto.getJcxmids()!= null && sjxxDto.getJcxmids().size() > 0) {
				if(StringUtil.isNotBlank(sjxxDto.getJcxm())){
					sjxxDto.setScry(sjxxDto.getXgry());
					boolean flag = false;
					SjjcxmDto sjjcxmDto_t=new SjjcxmDto();
					sjjcxmDto_t.setSjid(sjxxDto.getSjid());
					//先获取当前送检的检测项目信息，判断是否存在 收费的项目，并且收费金额不为0的
					List<SjjcxmDto> dtoList = dao.getDtoList(sjjcxmDto_t);
					for (SjjcxmDto sjjcxmDto : dtoList) {
						if (StringUtil.isNotBlank(sjjcxmDto.getSfsf()) && "1".equals(sjjcxmDto.getSfsf()) && StringUtil.isNotBlank(sjjcxmDto.getSfje()) && !"0".equals(sjjcxmDto.getSfje())){
							flag = true;
							break;
						}
					}
					//先根据送检ID删除已有的所有送检检测项目信息
					int delNum = dao.deleteBySjxx(sjxxDto);
					int modNum = 0;
					List<SjjcxmDto> sjjcxmDtos = new ArrayList<SjjcxmDto>();
					List<SjjcxmDto> updateList = new ArrayList<SjjcxmDto>();
					//sjxxDto 为页面传递过来的信息，dtoById 为数据库当前保存的信息，前面已经都设置了应付金额，重新区分是否根据金额感觉没必要
					SjxxDto dtoById = sjxxDao.getDtoById(sjxxDto.getSjid());
					List<SjjcxmDto> list=(List<SjjcxmDto>) JSON.parseArray(sjxxDto.getJcxm(),SjjcxmDto.class);
					for (int i = 0; i < list.size(); i++) {
						SjjcxmDto sjjcxmDto = new SjjcxmDto();
						sjjcxmDto.setSjid(sjxxDto.getSjid());
						sjjcxmDto.setXh(String.valueOf(i+1));
						sjjcxmDto.setJcxmid(list.get(i).getJcxmid());
						sjjcxmDto.setSfsf(list.get(i).getSfsf());
						if(StringUtil.isNotBlank(list.get(i).getJczxmid())){
							sjjcxmDto.setJczxmid(list.get(i).getJczxmid());
						}
						//判断历史数据中是否有检测项目/子项目一致的数据，若一致，设置xmglid
						for (SjjcxmDto dto : dtoList) {
							if (dto.getJcxmid().equals(sjjcxmDto.getJcxmid())){
								if (((!StringUtil.isAnyBlank(dto.getJczxmid(),sjjcxmDto.getJczxmid()) && dto.getJczxmid().equals(sjjcxmDto.getJczxmid()))
										|| (StringUtil.isAllBlank(dto.getJczxmid(),sjjcxmDto.getJczxmid())))
										&& StringUtil.isNotBlank(dto.getXmglid())){
									list.get(i).setXmglid(dto.getXmglid());
								}
							}
						}
						if (StringUtil.isNotBlank(list.get(i).getXmglid())){
							sjjcxmDto.setXmglid(list.get(i).getXmglid());
							sjjcxmDto.setXgry(sjxxDto.getScry());
							//原本存在的记录里，将当前伙伴与之前伙伴对比，如果修改了则重新给予应付金额，否则不设置yfje
							if(dtoById!=null&&!dtoById.getDb().equals(sjxxDto.getDb())){
								if(StringUtil.isNotBlank(list.get(i).getYfje()))
									sjjcxmDto.setYfje(list.get(i).getYfje());
								else
									sjjcxmDto.setYfje("0");
							}
							updateList.add(sjjcxmDto);
						}else{
							sjjcxmDto.setXmglid(StringUtil.generateUUID());
							if(StringUtil.isBlank(sjjcxmDto.getSfsf())){
								sjjcxmDto.setSfsf("1");
							}
							sjjcxmDto.setLrry(sjxxDto.getLrry());
							sjjcxmDto.setYfje(list.get(i).getYfje());
							sjjcxmDtos.add(sjjcxmDto);
						}
					}
					//先更新新增的送检检测项目信息
					if(sjjcxmDtos!=null&&sjjcxmDtos.size()>0){
						int result = dao.insertSjjcxmDtos(sjjcxmDtos);
						if(result == 0)
							return false;
					}
					//再更新已经存在的送检检测项目信息
					if(updateList!=null&&updateList.size()>0){
						modNum = dao.revertData(updateList);
						if (modNum == 0)
							return false;
					}
					if (flag && (delNum != modNum || !CollectionUtils.isEmpty(sjjcxmDtos) ))
						sjxxDto.setClbj("0");
					sjjcxmDtos.addAll(updateList);
					json = JSONObject.toJSONString(sjjcxmDtos);
				}else{
					List<String> jcxmids = sjxxDto.getJcxmids();
					dao.deleteBySjxx(sjxxDto);
					List<SjjcxmDto> sjjcxmDtos = new ArrayList<SjjcxmDto>();
					//int num=1;
					String jcxmmc="";
					for (int i = 0; i < jcxmids.size(); i++) {
						/*if (StringUtil.isNotBlank(sjxxDto.getJczxm())){
							boolean isFind=false;
							String[] split = sjxxDto.getJczxm().split(",");
							for(String jczxmid:split){
								String fcsid="";
								String jczxmmc="";
								for(JcsjDto dto:jczxmList){
									if(dto.getCsid().equals(jczxmid)){
										fcsid=dto.getFcsid();
										jczxmmc=dto.getCsmc();
										break;
									}
								}
								if(StringUtil.isNotBlank(fcsid)&&fcsid.equals(jcxmids.get(i))){
									SjjcxmDto sjjcxmDto = new SjjcxmDto();
									sjjcxmDto.setXmglid(StringUtil.generateUUID());
									sjjcxmDto.setSjid(sjxxDto.getSjid());
									sjjcxmDto.setXh(String.valueOf(num));
									sjjcxmDto.setJcxmid(jcxmids.get(i));
									for(JcsjDto dto:jcxmList){
										if(dto.getCsid().equals(jcxmids.get(i))){
											jcxmmc+=","+dto.getCsmc()+"-"+jczxmmc;
											break;
										}
									}
									sjjcxmDto.setJczxmid(jczxmid);
									sjjcxmDtos.add(sjjcxmDto);
									num++;
									isFind=true;
								}
							}
							if(!isFind){
								SjjcxmDto sjjcxmDto = new SjjcxmDto();
								sjjcxmDto.setXmglid(StringUtil.generateUUID());
								sjjcxmDto.setSjid(sjxxDto.getSjid());
								sjjcxmDto.setXh(String.valueOf(num));
								sjjcxmDto.setJcxmid(jcxmids.get(i));
								for(JcsjDto dto:jcxmList){
									if(dto.getCsid().equals(jcxmids.get(i))){
										jcxmmc+=","+dto.getCsmc();
										break;
									}
								}
								sjjcxmDtos.add(sjjcxmDto);
								num++;
							}
						}else{*/
							SjjcxmDto sjjcxmDto = new SjjcxmDto();
							sjjcxmDto.setXmglid(StringUtil.generateUUID());
							sjjcxmDto.setSjid(sjxxDto.getSjid());
							sjjcxmDto.setXh(String.valueOf(i+1));
							sjjcxmDto.setJcxmid(jcxmids.get(i));
							for(JcsjDto dto:jcxmList){
								if(dto.getCsid().equals(jcxmids.get(i))){
									jcxmmc+=","+dto.getCsmc();
									break;
								}
							}
							sjjcxmDtos.add(sjjcxmDto);
						//}
					}
					int result = dao.insertSjjcxmDtos(sjjcxmDtos);
					if(result == 0)
						return false;
					json = JSONObject.toJSONString(sjjcxmDtos);
					if(StringUtil.isNotBlank(jcxmmc)){
						sjxxDto.setJcxmmc(jcxmmc.substring(1));
						boolean isResult = sjxxDao.updateJcxmmc(sjxxDto);
						if(!isResult)
							return false;
					}
				}
			} else if (sjxxDto.getSjjcxms() != null && sjxxDto.getSjjcxms().size() > 0) {
				List<SjjcxmDto> sjjcxmDtos = sjxxDto.getSjjcxms();
				dao.deleteBySjxx(sjxxDto);
				for (int i = 0; i < sjjcxmDtos.size(); i++) {
					sjjcxmDtos.get(i).setXmglid(StringUtil.generateUUID());
					sjjcxmDtos.get(i).setSjid(sjxxDto.getSjid());
					sjjcxmDtos.get(i).setXh(String.valueOf(i+1));
					String jczxmid = sjxxDto.getJczxm();
					if (StringUtil.isNotBlank(jczxmid)){
						sjjcxmDtos.get(i).setJczxmid(jczxmid);
					}
				}
				int result = dao.insertSjjcxmDtos(sjjcxmDtos);
				if(result == 0)
					return false;
				json = JSONObject.toJSONString(sjjcxmDtos);
			}
		} catch (Exception e) {
			List<String> strSjxx=sjxxDto.getJcxmids();
			if(strSjxx !=null &&  strSjxx.size()>0) {
				List<SjjcxmDto> sjjcxmDtos = new ArrayList<SjjcxmDto>();
				for (int i = 0; i < strSjxx.size(); i++) {
					SjjcxmDto sjjcxmDto = new SjjcxmDto();
					sjjcxmDto.setXmglid(StringUtil.generateUUID());
					sjjcxmDto.setSjid(sjxxDto.getSjid());
					sjjcxmDto.setXh(String.valueOf(i + 1));
					sjjcxmDto.setJcxmid(strSjxx.get(i));
					String jczxmid = sjxxDto.getJczxm();
					if (StringUtil.isNotBlank(jczxmid)){
						sjjcxmDto.setJczxmid(jczxmid);
					}
					sjjcxmDtos.add(sjjcxmDto);
				}
				dao.deleteBySjxx(sjxxDto);
				int result=dao.insertSjjcxmDtos(sjjcxmDtos);
				if (result==0){
					return false;
				}
				json = JSONObject.toJSONString(sjjcxmDtos);
			}
		}
		sjxxDto.setJcxm(json);
		return true;
	}

	/**
	 * 处理并判断检测项目是否变更
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException
	 */
	public boolean dealWithJcxmChange(SjxxDto sjxxDto) throws BusinessException {
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
		paramMap.add("ywid", sjxxDto.getSjid());
		paramMap.add("ywlx", BusinessTypeEnum.SJ.getCode());
		DBEncrypt dbEncrypt = new DBEncrypt();
		String url=dbEncrypt.dCode(applicationurl)+"/wechat/pay/closeOrders";
		Map<String,Object> map=  restTemplate.postForObject(url, paramMap, Map.class);
		if (map!=null && map.get("status") !=null && "success".equals(map.get("status").toString())){
			return true;
		}else {
			throw new BusinessException(map.get("message").toString());
		}
	}

	@Override
	public boolean syncInfo(SjxxDto sjxxDto) throws BusinessException {
		List<SjjcxmDto> jcxmlist=(List<SjjcxmDto>) JSON.parseArray(sjxxDto.getJcxm(), SjjcxmDto.class);
		List<SjjcxmDto> add_List = new ArrayList<>();
		List<SjjcxmDto> mod_List = new ArrayList<>();
		if (!CollectionUtils.isEmpty(jcxmlist)){
			dao.deleteBySjxx(sjxxDto);
			for (int i = jcxmlist.size()-1; i >= 0; i--) {
				if (StringUtil.isBlank(jcxmlist.get(i).getBs()))
					throw new BusinessException("msg","同步数据错误！");
				if ("1".equals(jcxmlist.get(i).getBs())){
					add_List.add(jcxmlist.get(i));
				}else {
					mod_List.add(jcxmlist.get(i));
				}
			}
		}
		if (!CollectionUtils.isEmpty(add_List)){
			int result=dao.insertSjjcxmDtos(add_List);
			if (result==0)
				throw new BusinessException("msg","更新数据出错！");

		}
		if (!CollectionUtils.isEmpty(mod_List)){
			int result=dao.revertData(mod_List);
			if (result==0){
				throw new BusinessException("msg","更新数据出错！");
			}
		}
		return true;
	}

	/**
	 * 根据送检信息修改检测项目
	 * @param sjxxDto
	 * @return
	 */
	/*
	 * @Override
	 * 
	 * @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	 * public boolean updateBySjxx(SjxxDto sjxxDto) { // TODO Auto-generated method
	 * stub //先删除再新增 dao.deleteBySjxx(sjxxDto); List<String> jcxmids =
	 * sjxxDto.getJcxmids(); if(jcxmids != null && jcxmids.size() > 0){
	 * List<SjjcxmDto> sjjcxmDtos = new ArrayList<SjjcxmDto>(); for (int i = 0; i <
	 * jcxmids.size(); i++) { SjjcxmDto sjjcxmDto = new SjjcxmDto();
	 * sjjcxmDto.setSjid(sjxxDto.getSjid()); sjjcxmDto.setXh(String.valueOf(i+1));
	 * sjjcxmDto.setJcxmid(jcxmids.get(i)); sjjcxmDtos.add(sjjcxmDto); } int result
	 * = dao.insertSjjcxmDtos(sjjcxmDtos); if(result == 0) return false; } return
	 * true; }
	 */
	
	/**
	 * 获取检测项目的String清单
	 * @param sjjcxmDtos
	 * @return
	 */
	public List<String> getStringList(SjjcxmDto sjjcxmDto){
		return dao.getStringList(sjjcxmDto);
	}

	/**
	 * 获取检测子项目的String清单
	 * @param sjjcxmDto
	 * @return
	 */
	public List<String> getJczxmString(SjjcxmDto sjjcxmDto){
		return dao.getJczxmString(sjjcxmDto);
	}

	/**
	 * 根据送检ID查询检测项目
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjjcxmDto> selectJcxmBySjid(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.selectJcxmBySjid(sjxxDto);
	}


	/**
	 * 根据sjid获取送检检测项目以及对应项目收费标准
	 * @param sjid
	 * @return
	 */
	public List<Map<String,Object>>  getDetectionPayInfo(String sjid){
		return dao.getDetectionPayInfo(sjid);
	}
	/**
	 * 根据送检信息更新检测项目
	 * @param sjjcxmDtos
	 * @return
	 */
	public int  updateSjjcxmDtos(List<SjjcxmDto> sjjcxmDtos){
		return dao.updateSjjcxmDtos(sjjcxmDtos);
	}

	/**
	 * 还原数据
	 * @param sjjcxmDtos
	 * @return
	 */
	public int revertData(List<SjjcxmDto> sjjcxmDtos){
		return dao.revertData(sjjcxmDtos);
	}
}
