package com.matridx.igams.experiment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.post.IGrszDao;
import com.matridx.igams.common.enums.DetectionTypeEnum;
import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.SysjglDto;
import com.matridx.igams.experiment.dao.entities.TqglDto;
import com.matridx.igams.experiment.dao.entities.TqmxDto;
import com.matridx.igams.experiment.dao.entities.TqmxModel;
import com.matridx.igams.experiment.dao.post.ITqglDao;
import com.matridx.igams.experiment.dao.post.ITqmxDao;
import com.matridx.igams.experiment.service.svcinterface.ISysjglService;
import com.matridx.igams.experiment.service.svcinterface.ITqmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.CommonUtil;
import com.matridx.springboot.util.encrypt.Encrypt;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TqmxServiceImpl extends BaseBasicServiceImpl<TqmxDto, TqmxModel, ITqmxDao> implements ITqmxService{

	Logger log= LoggerFactory.getLogger(TqmxServiceImpl .class);
	@Autowired
	ITqglDao tqglDao;
	@Autowired
	IGrszDao grszDao;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Autowired
	private ISysjglService sysjglService;
	
	/**
	 * 校验内部编号是否存在
	 */
	@Override
	public List<String> exitnbbh(TqmxDto tqmxDto) throws BusinessException {
		List<String> nbbms= new ArrayList<>();
		List<String> notexitnbbhs= new ArrayList<>();
		if(tqmxDto.getNbbhs()!=null && tqmxDto.getNbbhs().length>0) {
			for(int i=0;i<tqmxDto.getNbbhs().length;i++) {
				//int nbbhlength=tqmxDto.getNbbhs()[i].length();
				String nbbh = CommonUtil.getNbbmByNbbm(tqmxDto.getNbbhs()[i]);
				nbbms.add(nbbh);
			}
			String[] nbbhjh=new String[nbbms.size()];
			nbbms.toArray(nbbhjh);
			tqmxDto.setNbbms(nbbhjh);
			List<TqmxDto> wksjxx=dao.getSjxxByNbbh(tqmxDto);
			List<String> exitnbbhlist= new ArrayList<>();
			for (TqmxDto dto : wksjxx) {
				if (StringUtil.isNotBlank(dto.getSjid())) {
					exitnbbhlist.add(dto.getNbbh());
				}
			}
			for(int j=0;j<tqmxDto.getNbbms().length;j++) {
				boolean result=exitnbbhlist.contains(tqmxDto.getNbbms()[j]);
				if(!result) {
					notexitnbbhs.add(tqmxDto.getNbbms()[j]);
				}
			}
		}
		return notexitnbbhs;
	}

	/**
	 * 新增保存提取浓度信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addSaveTqxx(TqmxDto tqmxDto) throws BusinessException {
		TqglDto tqglDto=new TqglDto();
		tqglDto.setLrry(tqmxDto.getLrry());
		if("1".equals(tqmxDto.getSfgx())){
			tqglDto.setZt(StatusEnum.CHECK_PASS.getCode());
		}else{
			tqglDto.setZt(StatusEnum.CHECK_NO.getCode());
		}
		tqglDto.setMc(tqmxDto.getMc());
		tqglDto.setJcdw(tqmxDto.getJcdw());
		tqglDto.setTqrq(tqmxDto.getTqrq());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		tqglDto.setLrsj(sdf.format(new Date()));
		tqglDto.setTqsj(tqmxDto.getTqsjStr());
		if(StringUtils.isNotBlank(tqmxDto.getTqid()))
			tqglDto.setTqid(tqmxDto.getTqid());
		if(StringUtils.isBlank(tqglDto.getTqid())) {
			tqglDto.setTqid(StringUtil.generateUUID());
		}
		int xh=tqglDao.getXh(tqglDto);
		tqglDto.setXh(xh+"");
		tqglDto.setSjph(tqmxDto.getSjph());
		tqglDto.setCzr(tqmxDto.getCzr());
		tqglDto.setHdr(tqmxDto.getHdr());
		boolean tqglresult=tqglDao.insert(tqglDto) > 0;
		if(!tqglresult)
			return false;
		//添加个人设置
		GrszDto grszDto_t=new GrszDto();
		grszDto_t.setYhid(tqmxDto.getLrry());
		grszDto_t.setSzlb(PersonalSettingEnum.TEST_PLACE.getCode());
		GrszDto grszDto=grszDao.selectGrszDtoByYhidAndSzlb(grszDto_t);
		GrszDto grszDto2=new GrszDto();
		grszDto2.setSzid(StringUtil.generateUUID());
		grszDto2.setYhid(tqmxDto.getLrry());
		grszDto2.setSzlb(PersonalSettingEnum.TEST_PLACE.getCode());
		grszDto2.setSzz(tqmxDto.getJcdw());
		grszDto2.setGlxx(tqmxDto.getYhmc());
		grszDto2.setLrry(tqmxDto.getLrry());
		if(grszDto==null) {//如果为空就新增
			boolean result= grszDao.insert(grszDto2) > 0;
			if(!result)
				return false;
		}else {//如果不为空就修改
			if(grszDto.getSzz()==null || !grszDto.getSzz().equals(grszDto2.getSzz())) {
				grszDto2.setXgry(tqmxDto.getLrry());
				boolean result=grszDao.updateByYhidAndSzlb(grszDto2);
				if(!result)
					return false;
			}
		}
		//添加提取明细
		List<TqmxDto> tqsjxx=dao.getSjxxByNbbms(tqmxDto);
		List<TqmxDto> tqfjxx=dao.getFjxxByNbbms(tqmxDto);
		List<TqmxDto> tqxxlist=new ArrayList<TqmxDto>();
		List<String> sjids=new ArrayList<>();
		List<String> fjids=new ArrayList<>();
		for(int i=0;i<tqmxDto.getNbbhs().length;i++) {
			TqmxDto tqmxdto = new TqmxDto();
			tqxxlist.add(tqmxdto);
			//如果拆分后取第一部分长度为11则是正常内部编码，直接取。
			tqxxlist.get(i).setNbbh(tqmxDto.getNbbhs()[i]);
			String nbbh = CommonUtil.getNbbmByNbbm(tqmxDto.getNbbhs()[i]);
			if(tqmxDto.getNbbhs()[i].contains("-REM"))
				tqxxlist.get(i).setHzbj("REM");
			if(tqmxDto.getNbbhs()[i].contains("-DNA")){
				tqxxlist.get(i).setJcxmdm("D");
			}else if(tqmxDto.getNbbhs()[i].contains("-RNA")){
				tqxxlist.get(i).setJcxmdm("R");
			}else if(tqmxDto.getNbbhs()[i].contains("-tNGS") || tqmxDto.getNbbhs()[i].contains("-TBtNGS") || tqmxDto.getNbbhs()[i].contains("-HS")){
				tqxxlist.get(i).setJcxmdm("C");
			}else if(tqmxDto.getNbbhs()[i].startsWith("F")){
				tqxxlist.get(i).setJcxmdm("F");
			}
			tqxxlist.get(i).setXh(tqmxDto.getXhs()[i]);
			if (StringUtil.isNotBlank(tqmxDto.getNbzbms()[i]) && !tqmxDto.getNbzbms()[i].equals("XX/XX")){
				tqxxlist.get(i).setTqdm(tqmxDto.getNbzbms()[i]);
			}
			if (tqmxDto.getSyglids().length>i && StringUtil.isNotBlank(tqmxDto.getSyglids()[i])){
				tqxxlist.get(i).setSyglid(tqmxDto.getSyglids()[i]);
			}
			tqxxlist.get(i).setJkyl(tqmxDto.getJkyls()[i]);
			tqxxlist.get(i).setHcyyl(tqmxDto.getHcyyls()[i]);
			tqxxlist.get(i).setTqid(tqglDto.getTqid());
			tqxxlist.get(i).setTqmxid(StringUtil.generateUUID());
			tqxxlist.get(i).setLrry(tqmxDto.getLrry());
			tqxxlist.get(i).setJcdw(tqmxDto.getJcdw());
			tqxxlist.get(i).setTqrq(tqmxDto.getTqrq());
			if(tqmxDto.getXsnds()!=null && tqmxDto.getXsnds().length>0) {
				tqxxlist.get(i).setXsnd(tqmxDto.getXsnds()[i]);
			}
			if(tqmxDto.getHsnds()!=null && tqmxDto.getHsnds().length>0) {
				tqxxlist.get(i).setHsnd(tqmxDto.getHsnds()[i]);
			}
			if(tqmxDto.getCdnas()!=null && tqmxDto.getCdnas().length>0) {
				tqxxlist.get(i).setCdna(tqmxDto.getCdnas()[i]);
			}
			if(tqmxDto.getTqykws()!=null && tqmxDto.getTqykws().length>0) {
				tqxxlist.get(i).setTqykw(tqmxDto.getTqykws()[i]);
			}
			if(tqmxDto.getSpikes()!=null && tqmxDto.getSpikes().length>0) {
				tqxxlist.get(i).setSpike(tqmxDto.getSpikes()[i]);
			}
			if(tqmxDto.getTqybms()!=null && tqmxDto.getTqybms().length>0) {
				tqxxlist.get(i).setTqybm(tqmxDto.getTqybms()[i]);
			}
			if("1".equals(tqmxDto.getSfgx())){
				if(!CollectionUtils.isEmpty(tqsjxx)) {
                    for (TqmxDto dto : tqsjxx) {
                        if (dto.getNbbh().equals(nbbh)) {
                            tqxxlist.get(i).setSjid(dto.getSjid());
                            if (!sjids.contains(dto.getSjid())) {
                                sjids.add(dto.getSjid());
                            }
                        }
                    }
				}
				if(!CollectionUtils.isEmpty(tqfjxx)) {
					for(int j=0;j<tqfjxx.size();j++) {
						if(tqfjxx.get(j).getNbbh().equals(nbbh)) {
							if(!fjids.contains(tqfjxx.get(j).getFjid())){
								fjids.add(tqfjxx.get(j).getFjid());
							}
						}
					}
				}
			}
		}
		boolean addsavewkxx=dao.insertTqmx(tqxxlist);
		if(!addsavewkxx)
			return false;
		if("1".equals(tqmxDto.getSfgx())){
			updateSjsyglSyrq(tqmxDto);
			if(!CollectionUtils.isEmpty(sjids)){
				//调用接口发送消息
				toSendExperimentMessage(sjids,tqmxDto,"sj");
			}
			if(!CollectionUtils.isEmpty(fjids)){
				//调用接口发送消息
				toSendExperimentMessage(fjids,tqmxDto,"fj");
			}
		}
		SysjglDto sysjglDto = new SysjglDto();
		sysjglDto.setYwid(tqmxDto.getTqid());
		sysjglDto.setScry(tqmxDto.getXgry());
		sysjglService.delete(sysjglDto);
		return sysjglService.insertSysjglList(tqmxDto.getSysjStr(),"TQ");
	}

	/**
	 * 提取新增发送实验通知
	 */
	public void toSendExperimentMessage(List<String> ids,TqmxDto tqmxDto,String flag){
		//调用接口发送消息
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("ids", JSON.toJSONString(ids));
		paramMap.add("flag", flag);
		paramMap.add("access_token",tqmxDto.getAccess_token());
		String url=applicationurl+"/experimentS/experimentS/pagedataSendMessage";
		restTemplate.postForObject(url, paramMap, Map.class);
	}
	/**
	 * 获取送检信息
	 */
	 public List<TqmxDto> getInfoByNbbm(TqmxDto tqmxDto){
		return dao.getInfoByNbbm(tqmxDto);
	}
	
	/**
	 * 根据内部编号查询提取明细
	 */
	public List<TqmxDto> gettqmxByNbbh(TqmxDto tqmxDto){
		return dao.gettqmxByNbbh(tqmxDto);
	}
	
	/**
	 * 根据提取id获取提取信息
	 */
	public List<TqmxDto> getTqmxByTqid(TqmxDto tqmxDto){
		return dao.getTqmxByTqid(tqmxDto);
	}

	@Override
	public List<TqmxDto> getTqmxByTqidByPrint(TqmxDto tqmxDto) {
		return dao.getTqmxByTqidByPrint(tqmxDto);
	}

	/**
	 * 获取实验信息
	 */
	public List<TqmxDto> getInfoBySyglids(TqmxDto tqmxDto){
		return dao.getInfoBySyglids(tqmxDto);
	}

	@Override
	public List<TqmxDto> getInfoByZdpb(TqmxDto tqmxDto) {
		return dao.getInfoByZdpb(tqmxDto);
	}

	@Override
	public List<TqmxDto> getExtractInfoZdpb(Map<String, Object> map) {
		return dao.getExtractInfoZdpb(map);
	}

	@Override
	public List<TqmxDto> getWkInfoListZdpb(Map<String, Object> map) {
		return dao.getWkInfoListZdpb(map);
	}

	@Override
	public Map<String, String> getCountByjcdw(Map<String,Object> map) {
		return dao.getCountByjcdw(map);
	}

	/**
	 * 更新提取浓度信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateTqmx(TqmxDto tqmxDto) throws BusinessException {
		//修改提取列表
		TqglDto tqglDto=new TqglDto();
		tqglDto.setXgry(tqmxDto.getXgry());
		if("1".equals(tqmxDto.getSfgx())){
			tqglDto.setZt(StatusEnum.CHECK_PASS.getCode());
		}else{
			tqglDto.setZt(StatusEnum.CHECK_NO.getCode());
		}
		tqglDto.setMc(tqmxDto.getMc());
		tqglDto.setJcdw(tqmxDto.getJcdw());
		tqglDto.setTqid(tqmxDto.getTqid());
		tqglDto.setTqrq(tqmxDto.getTqrq());
		tqglDto.setSjph(tqmxDto.getSjph());
		//tqglDto.setTqsj(tqmxDto.getTqsjStr());
		tqglDto.setCzr(tqmxDto.getCzr());
		tqglDto.setHdr(tqmxDto.getHdr());

		//需要保留原有数据，只是针对页面新增的内容进行替换 2025-08-05
		String tqsj = tqmxDto.getTqsjStr();
		if(StringUtil.isNotBlank(tqsj)){
			TqglDto old_tqglDto = tqglDao.getDtoById(tqglDto.getTqid());
			String old_tqsj = old_tqglDto.getTqsj();
			Map<String,String> tqsjMap = JSONObject.parseObject(tqsj,Map.class);
			Map<String,String> old_tqsjMap = JSONObject.parseObject(old_tqsj,Map.class);
			if(old_tqsjMap==null)
				old_tqsjMap	= new HashMap<>();
			for(String key:tqsjMap.keySet()){
				if(StringUtil.isNotBlank(tqsjMap.get(key))){
					old_tqsjMap.put(key,tqsjMap.get(key));
				}
			}
			tqglDto.setTqsj(JSONObject.toJSONString(old_tqsjMap));
		}

		boolean updatetqgl=tqglDao.updateTqgl(tqglDto);
		if(!updatetqgl)
			return false;
		//修改个人设置表
		GrszDto grszDto_t=new GrszDto();
		grszDto_t.setYhid(tqmxDto.getXgry());
		grszDto_t.setSzlb(PersonalSettingEnum.TEST_PLACE.getCode());
		GrszDto grszDto=grszDao.selectGrszDtoByYhidAndSzlb(grszDto_t);
		if(grszDto==null) {
			GrszDto grszDto2=new GrszDto();
			grszDto2.setSzid(StringUtil.generateUUID());
			grszDto2.setYhid(tqmxDto.getLrry());
			grszDto2.setSzlb(PersonalSettingEnum.TEST_PLACE.getCode());
			grszDto2.setSzz(tqmxDto.getJcdw());
			grszDto2.setGlxx(tqmxDto.getYhmc());
			grszDto2.setYhid(tqmxDto.getXgry());
			boolean result=grszDao.insert(grszDto2) > 0;
			if(!result)
				return false;
		}else if(grszDto.getSzz()==null || !grszDto.getSzz().equals(tqmxDto.getJcdw())) {//判断如果检测单位改变，修改个人设置
			GrszDto grszDto2=new GrszDto();
			grszDto2.setSzid(StringUtil.generateUUID());
			grszDto2.setYhid(tqmxDto.getLrry());
			grszDto2.setSzlb(PersonalSettingEnum.TEST_PLACE.getCode());
			grszDto2.setSzz(tqmxDto.getJcdw());
			grszDto2.setGlxx(tqmxDto.getYhmc());
			grszDto2.setYhid(tqmxDto.getXgry());
			boolean result=grszDao.updateByYhidAndSzlb(grszDto2);
			if(!result)
				return false;
		}
		//修改提取明细列表
		List<TqmxDto> tqsjxx= new ArrayList<>();
		List<TqmxDto> tqfjxx=new ArrayList<>();
		if(tqmxDto.getNbbms()!=null && tqmxDto.getNbbms().length>0) {
			tqsjxx=dao.getSjxxByNbbms(tqmxDto);
			tqfjxx=dao.getFjxxByNbbms(tqmxDto);
		}
		List<TqmxDto> tqxxlist=new ArrayList<TqmxDto>();
		List<String> sjids=new ArrayList<>();
		List<String> fjids=new ArrayList<>();
		for(int i=0;i<tqmxDto.getNbbhs().length;i++) {
			TqmxDto tqmxdto = new TqmxDto();
			tqxxlist.add(tqmxdto);
			String nbbh = CommonUtil.getNbbmByNbbm(tqmxDto.getNbbhs()[i]);
			tqxxlist.get(i).setNbbh(tqmxDto.getNbbhs()[i]);
			if(tqmxDto.getNbbhs()[i].contains("-REM"))
				tqxxlist.get(i).setHzbj("REM");
			if(tqmxDto.getNbbhs()[i].contains("-DNA")){
				tqxxlist.get(i).setJcxmdm("D");
			}else if(tqmxDto.getNbbhs()[i].contains("-RNA")){
				tqxxlist.get(i).setJcxmdm("R");
			}else if(tqmxDto.getNbbhs()[i].contains("-tNGS") || tqmxDto.getNbbhs()[i].contains("-TBtNGS") || tqmxDto.getNbbhs()[i].contains("-HS")){
				tqxxlist.get(i).setJcxmdm("C");
			}else if(tqmxDto.getNbbhs()[i].startsWith("F")){
				tqxxlist.get(i).setJcxmdm("F");
			}
			if (tqmxDto.getNbzbms().length>i && StringUtil.isNotBlank(tqmxDto.getNbzbms()[i]) && !tqmxDto.getNbzbms()[i].equals("XX/XX")){
				tqxxlist.get(i).setTqdm(tqmxDto.getNbzbms()[i]);
			}

			if (tqmxDto.getSyglids().length>i && StringUtil.isNotBlank(tqmxDto.getSyglids()[i])){
				tqxxlist.get(i).setSyglid(tqmxDto.getSyglids()[i]);
			}
			tqxxlist.get(i).setXh(tqmxDto.getXhs()[i]);
			tqxxlist.get(i).setJkyl(tqmxDto.getJkyls()[i]);
			tqxxlist.get(i).setHcyyl(tqmxDto.getHcyyls()[i]);
			tqxxlist.get(i).setTqid(tqglDto.getTqid());
			tqxxlist.get(i).setTqmxid(tqmxDto.getTqmxids()[i]);
			tqxxlist.get(i).setLrry(tqmxDto.getXgry());
			tqxxlist.get(i).setJcdw(tqmxDto.getJcdw());
			tqxxlist.get(i).setTqrq(tqmxDto.getTqrq());
			if(tqmxDto.getXsnds()!=null && tqmxDto.getXsnds().length>0) {
				tqxxlist.get(i).setXsnd(tqmxDto.getXsnds()[i]);
			}
			if(tqmxDto.getHsnds()!=null && tqmxDto.getHsnds().length>0) {
				tqxxlist.get(i).setHsnd(tqmxDto.getHsnds()[i]);
			}
			if(tqmxDto.getCdnas()!=null && tqmxDto.getCdnas().length>0) {
				tqxxlist.get(i).setCdna(tqmxDto.getCdnas()[i]);
			}
			if(tqmxDto.getTqykws()!=null && tqmxDto.getTqykws().length>0) {
				tqxxlist.get(i).setTqykw(tqmxDto.getTqykws()[i]);
			}
			if(tqmxDto.getSpikes()!=null && tqmxDto.getSpikes().length>0) {
				tqxxlist.get(i).setSpike(tqmxDto.getSpikes()[i]);
			}
			if(tqmxDto.getXsbss()!=null && tqmxDto.getXsbss().length>0) {
				tqxxlist.get(i).setXsbs(tqmxDto.getXsbss()[i]);
			}
			if(tqmxDto.getTqybms()!=null && tqmxDto.getTqybms().length>0) {
				tqxxlist.get(i).setTqybm(tqmxDto.getTqybms()[i]);
			}
			if("1".equals(tqmxDto.getSfgx())){
				if(!CollectionUtils.isEmpty(tqsjxx)) {
					for (TqmxDto dto : tqsjxx) {
						if (dto.getNbbh().equals(nbbh)) {
							tqxxlist.get(i).setSjid(dto.getSjid());
							if (!sjids.contains(dto.getSjid())) {
								sjids.add(dto.getSjid());
							}
						}
					}
				}
				if(!CollectionUtils.isEmpty(tqfjxx)){
					for(int j=0;j<tqfjxx.size();j++) {
						if(tqfjxx.get(j).getNbbh().equals(nbbh)) {
							if(!fjids.contains(tqfjxx.get(j).getFjid())){
								fjids.add(tqfjxx.get(j).getFjid());
							}
						}
					}
				}
			}
		}
		tqmxDto.setIds(tqmxDto.getTqid());
		dao.deleteByTqid(tqmxDto);//先删除原有明细数据
		boolean addsavewkxx=dao.insertTqmx(tqxxlist);
		if(!addsavewkxx) {
			return false;
		}
		if("1".equals(tqmxDto.getSfgx())){
			//更新实验室日等并返回没有实验日期的数据
			List<TqmxDto> allTqmxDtos = updateSjsyglSyrq(tqmxDto);
			List<String> sj_ids = new ArrayList<>();
			List<String> fj_ids = new ArrayList<>();
			for (TqmxDto tqmxDto_all : allTqmxDtos) {
				//送检的实验日期
				if (DetectionTypeEnum.DETECT_SJ.getCode().equals(tqmxDto_all.getLx()) && !sj_ids.contains(tqmxDto_all.getSjid())){
					sj_ids.add(tqmxDto_all.getSjid());
				}
				//复检的实验日期
				if (DetectionTypeEnum.DETECT_FJ.getCode().equals(tqmxDto_all.getLx()) && !fj_ids.contains(tqmxDto_all.getFjid())){
					fj_ids.add(tqmxDto_all.getFjid());
				}
			}

			if(!CollectionUtils.isEmpty(sj_ids)){
				//调用接口发送消息
				toSendExperimentMessage(sj_ids,tqmxDto,"sj");
			}
			if(!CollectionUtils.isEmpty(fj_ids)){
				//调用接口发送消息
				toSendExperimentMessage(fj_ids,tqmxDto,"fj");
			}
		}
		SysjglDto sysjglDto = new SysjglDto();
		sysjglDto.setYwid(tqmxDto.getTqid());
		sysjglDto.setScry(tqmxDto.getXgry());
		sysjglService.delete(sysjglDto);
		return sysjglService.insertSysjglList(tqmxDto.getSysjStr(),"TQ");
	}

	/**
	 * 删除提取明细数据
	 */
	public boolean deleteByTqid(TqmxDto tqmxDto) {
		return dao.deleteByTqid(tqmxDto);
	}
	
	/**
	 * 根据送检id获取提取信息
	 */
	public List<TqmxDto> getTqxxBySjid(String sjid){
		return dao.getTqxxBySjid(sjid);
	}
	
	/**
	 * 通过删除标记删除提取明细数据
	 */
	public boolean delByTqid(TqmxDto tqmxDto) {
		return dao.delByTqid(tqmxDto);
	}
	
	/**
	 * 根据提取ID和序号获取提取信息
	 */
	public TqmxDto getDtoByIdAndXh(TqmxDto tqmxDto) {
		return dao.getDtoByIdAndXh(tqmxDto);
	}
	
	/**
	 * 提取明细临时保存
	 */
	public boolean temporarySave(TqmxDto tqmxDto) {
		return dao.temporarySave(tqmxDto);
	}
	
	/**
	 * 提取明细临时修改
	 */
	public boolean updateByIdAndXh(TqmxDto tqmxDto) {
		return dao.updateByIdAndXh(tqmxDto);
	}
	
	/**
	 * 根据提取ID删除提取明细数据
	 */
	public boolean delTqmxDto(TqmxDto tqmxDto) {
		return dao.delTqmxDto(tqmxDto);
	}
	
	/**
	 * 根据内部编码获取检测项目信息
	 */
	public TqmxDto getSjDtoByNbbh(TqmxDto tqmxDto) {
		return dao.getSjDtoByNbbh(tqmxDto);
	}

	public List<TqmxDto> updateSjsyglSyrq(TqmxDto tqmxDto){
		//更新实验管理日期
		List<TqmxDto> syUpdateDtos = new ArrayList<>();
		Date date=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TqmxDto> list=dao.getSjsyglByNbbms(tqmxDto);
		//所有实验日期更新数据
		List<TqmxDto> allTqmxDtos= new ArrayList<>();
		for (TqmxDto dto : list) {
			if (StringUtil.isBlank(dto.getSyrq())){
				dto.setSyrq(dateFormat.format(date));
				syUpdateDtos.add(dto);
			}
			if (StringUtil.isNotBlank(dto.getJclxcskz1())) {//如果检测类型的扩展参数不为空
				TqmxDto tqmxDto1 = getSyInfo(dto, dateFormat.format(date));
				tqmxDto1.setSyrq(null);
				allTqmxDtos.add(tqmxDto1);
			}
		}

		if (!CollectionUtils.isEmpty(allTqmxDtos)){
			//更新送检实验日期
			List<TqmxDto> sjUpdateDtos = new ArrayList<>();
			//更新复检实验日期
			List<TqmxDto> fjUpdateDtos = new ArrayList<>();
			for (TqmxDto tqmxDto_all : allTqmxDtos) {
					//复检的实验日期
				if (DetectionTypeEnum.DETECT_FJ.getCode().equals(tqmxDto_all.getLx())){
					fjUpdateDtos.add(tqmxDto_all);
				}
				//送检的实验日期
				if (DetectionTypeEnum.DETECT_SJ.getCode().equals(tqmxDto_all.getLx())){
					sjUpdateDtos.add(tqmxDto_all);
				}
			}
			if (!CollectionUtils.isEmpty(syUpdateDtos)){
				dao.updateSyList(syUpdateDtos);
			}
			if (!CollectionUtils.isEmpty(sjUpdateDtos)){
				dao.updateSjList(sjUpdateDtos);
			}
			if (!CollectionUtils.isEmpty(fjUpdateDtos)){
				dao.updateFjList(fjUpdateDtos);
			}
		}
		return syUpdateDtos;
	}
	public TqmxDto getSyInfo(TqmxDto tqmxDto,String data){
		TqmxDto dto = new TqmxDto();
		dto.setYwid(tqmxDto.getYwid());
		dto.setLx(tqmxDto.getLx());
		dto.setJclxcskz1(tqmxDto.getJclxcskz1());
		if ("D".equals(tqmxDto.getJclxcskz1()) && StringUtil.isBlank(tqmxDto.getDsyrq())){//如果检测类型的扩展参数1为D
			dto.setDsyrq(data);//插入D实验日期
		}else if ("R".equals(tqmxDto.getJclxcskz1()) && StringUtil.isBlank(tqmxDto.getRsyrq())){//如果检测类型的扩展参数1为R
			dto.setRsyrq(data);//插入R实验日期
		}else if ("C".equals(tqmxDto.getJclxcskz1()) || "T".equals(tqmxDto.getJclxcskz1())){//如果检测类型的扩展参数1为C/T
			if ("D".equals(tqmxDto.getJcxmcskz1()) && StringUtil.isBlank(tqmxDto.getDsyrq())){//如果检测项目的扩展参数1为D
				dto.setDsyrq(data);//插入D实验日期
			}else if ("R".equals(tqmxDto.getJcxmcskz1()) && StringUtil.isBlank(tqmxDto.getRsyrq())){//如果检测项目的扩展参数1为R
				dto.setRsyrq(data);//插入R实验日期
			}else if ("C".equals(tqmxDto.getJcxmcskz1())){//如果检测项目的扩展参数1为C
				if(StringUtil.isBlank(tqmxDto.getDsyrq())){
					dto.setDsyrq(data);//插入D实验日期
				}
				if(StringUtil.isBlank(tqmxDto.getDsyrq())){
					dto.setRsyrq(data);//插入R实验日期
				}
			}else {
				if(StringUtil.isBlank(tqmxDto.getQtsyrq())){
					dto.setQtsyrq(data);//插入其他实验日期
				}
			}
		}else {
			if(StringUtil.isBlank(tqmxDto.getQtsyrq())){
				dto.setQtsyrq(data);//插入其他实验日期
			}
		}
		return dto;
	}
}
