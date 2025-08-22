package com.matridx.crf.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matridx.crf.web.dao.entities.*;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.crf.web.dao.post.IQmngsndzxxjlDao;
import com.matridx.crf.web.service.svcinterface.IQmngsdmxqService;
import com.matridx.crf.web.service.svcinterface.IQmngsndzxxjlService;
import com.matridx.crf.web.service.svcinterface.IQmngsshService;
import com.matridx.crf.web.service.svcinterface.IQmngsxcgService;
import com.matridx.crf.web.service.svcinterface.IQmngsyzzbService;

@Service
public class QmngsndzxxjlServiceImpl extends BaseBasicServiceImpl<QmngsndzxxjlDto, QmngsndzxxjlModel, IQmngsndzxxjlDao> implements IQmngsndzxxjlService{
	@Autowired
	IQmngsdmxqService qmngsdmxqService;
	@Autowired
	IQmngsyzzbService qmngsyzzbService;
	@Autowired
	IQmngsshService qmngsshService;
	@Autowired
	IQmngsxcgService qmngsxcgService;
	@Autowired
	RedisUtil redisUtil;
	/**
	 * 获取患者记录
	 * 
	 * @return
	 */
	@Override
	public Map<String, Object> getHzjl(String hzid) {
		Map<String, Object> map = new HashMap<String, Object>();
		QmngsndzxxjlDto qmngsndzxxjl = new QmngsndzxxjlDto();
		qmngsndzxxjl.setQmngshzid(hzid);
		//获取患者记录
		List<QmngsndzxxjlDto> qmngsndzxxjls= dao.queryById(qmngsndzxxjl);
		//获取血脉动气
		List<QmngsdmxqDto> qmngsdmxqList = qmngsdmxqService.queryDmxq(hzid);
		//获取炎症指标
		List<QmngsyzzbDto> qmngsyzzbList = qmngsyzzbService.queryYzzb(hzid);
		//获取生化配置
		List<QmngsshDto> qmngsshList = qmngsshService.queryShpz(hzid);
		//获取血常规
		List<QmngsxcgDto> qmngsxcgList = qmngsxcgService.queryXcg(hzid);
		
		//区分日期
		QmngsndzxxjlDto qmngsndzxxjla = new QmngsndzxxjlDto(); //第一天
		QmngsndzxxjlDto qmngsndzxxjlb = new QmngsndzxxjlDto(); //第三天
		QmngsndzxxjlDto qmngsndzxxjlc = new QmngsndzxxjlDto(); //第五天
		QmngsndzxxjlDto qmngsndzxxjld = new QmngsndzxxjlDto(); //第七天
		QmngsndzxxjlDto qmngsndzxxjle = new QmngsndzxxjlDto(); //第二十八天
		
		//血脉动气
		List<QmngsdmxqDto> qmngsdmxqsa = new ArrayList<QmngsdmxqDto>();  //第一天 
		List<QmngsdmxqDto> qmngsdmxqsb = new ArrayList<QmngsdmxqDto>(); //第三天
		List<QmngsdmxqDto> qmngsdmxqsc = new ArrayList<QmngsdmxqDto>(); //第五天 
		List<QmngsdmxqDto> qmngsdmxqsd = new ArrayList<QmngsdmxqDto>(); //第七天 
		List<QmngsdmxqDto> qmngsdmxqse = new ArrayList<QmngsdmxqDto>(); //第二十八天 
		//炎症指标
		List<QmngsyzzbDto> qmngsyzzbsa = new ArrayList<QmngsyzzbDto>();//第一天 
		List<QmngsyzzbDto> qmngsyzzbsb = new ArrayList<QmngsyzzbDto>();//第三天
		List<QmngsyzzbDto> qmngsyzzbsc = new ArrayList<QmngsyzzbDto>();//第五天 
		List<QmngsyzzbDto> qmngsyzzbsd = new ArrayList<QmngsyzzbDto>();//第七天 
		List<QmngsyzzbDto> qmngsyzzbse = new ArrayList<QmngsyzzbDto>();//第二十八天 
		//生化配置
		List<QmngsshDto> qmngsshsa = new ArrayList<QmngsshDto>();//第一天 
		List<QmngsshDto> qmngsshsb = new ArrayList<QmngsshDto>();//第三天
		List<QmngsshDto> qmngsshsc = new ArrayList<QmngsshDto>();//第五天 
		List<QmngsshDto> qmngsshsd = new ArrayList<QmngsshDto>();//第七天 
		List<QmngsshDto> qmngsshse = new ArrayList<QmngsshDto>();//第二十八天 
		//血常规
		List<QmngsxcgDto> qmngsxcgsa = new ArrayList<QmngsxcgDto>();//第一天 
		List<QmngsxcgDto> qmngsxcgsb = new ArrayList<QmngsxcgDto>();//第三天
		List<QmngsxcgDto> qmngsxcgsc = new ArrayList<QmngsxcgDto>();//第五天 
		List<QmngsxcgDto> qmngsxcgsd = new ArrayList<QmngsxcgDto>();//第七天 
		List<QmngsxcgDto> qmngsxcgse = new ArrayList<QmngsxcgDto>();//第二十八天 
		
		//组装动脉血气
		if(qmngsdmxqList!=null && qmngsdmxqList.size()>0) {
			for (QmngsdmxqDto qmngsdmxqDto : qmngsdmxqList) {
				if("1".equals(qmngsdmxqDto.getJldjt())) {
					qmngsdmxqsa.add(qmngsdmxqDto);
				}
				if("3".equals(qmngsdmxqDto.getJldjt())) {
					qmngsdmxqsb.add(qmngsdmxqDto);
				}
				if("5".equals(qmngsdmxqDto.getJldjt())) {
					qmngsdmxqsc.add(qmngsdmxqDto);
				}
				if("7".equals(qmngsdmxqDto.getJldjt())) {
					qmngsdmxqsd.add(qmngsdmxqDto);
				}
				if("28".equals(qmngsdmxqDto.getJldjt())) {
					qmngsdmxqse.add(qmngsdmxqDto);
				}
			}
		}
		
		//组装炎症指标
		if(qmngsyzzbList!=null && qmngsyzzbList.size()>0) {
			for (QmngsyzzbDto qmngsyzzbDto : qmngsyzzbList) {
				if("1".equals(qmngsyzzbDto.getJldjt())) {
					qmngsyzzbsa.add(qmngsyzzbDto);
				}
				if("3".equals(qmngsyzzbDto.getJldjt())) {
					qmngsyzzbsb.add(qmngsyzzbDto);
				}
				if("5".equals(qmngsyzzbDto.getJldjt())) {
					qmngsyzzbsc.add(qmngsyzzbDto);
				}
				if("7".equals(qmngsyzzbDto.getJldjt())) {
					qmngsyzzbsd.add(qmngsyzzbDto);
				}
				if("28".equals(qmngsyzzbDto.getJldjt())) {
					qmngsyzzbse.add(qmngsyzzbDto);
				}
			}
		}
		
		//组装生化配置
		if(qmngsshList!=null && qmngsshList.size()>0) {
			for (QmngsshDto qmngsshDto : qmngsshList) {
				if("1".equals(qmngsshDto.getJldjt())) {
					qmngsshsa.add(qmngsshDto);
				}
				if("3".equals(qmngsshDto.getJldjt())) {
					qmngsshsb.add(qmngsshDto);
				}
				if("5".equals(qmngsshDto.getJldjt())) {
					qmngsshsc.add(qmngsshDto);
				}
				if("7".equals(qmngsshDto.getJldjt())) {
					qmngsshsd.add(qmngsshDto);
				}
				if("28".equals(qmngsshDto.getJldjt())) {
					qmngsshse.add(qmngsshDto);
				}
			}
		}
		
		//组装血常规
		if(qmngsxcgList!=null && qmngsxcgList.size()>0) {
			for (QmngsxcgDto qmngsxcgDto : qmngsxcgList) {
				if("1".equals(qmngsxcgDto.getJldjt())) {
					qmngsxcgsa.add(qmngsxcgDto);
				}
				if("3".equals(qmngsxcgDto.getJldjt())) {
					qmngsxcgsb.add(qmngsxcgDto);
				}
				if("5".equals(qmngsxcgDto.getJldjt())) {
					qmngsxcgsc.add(qmngsxcgDto);
				}
				if("7".equals(qmngsxcgDto.getJldjt())) {
					qmngsxcgsd.add(qmngsxcgDto);
				}
				if("28".equals(qmngsxcgDto.getJldjt())) {
					qmngsxcgse.add(qmngsxcgDto);
				}
			}
		}
		
		
		
		if(qmngsndzxxjls!=null && qmngsndzxxjls.size()>0) {
			for (QmngsndzxxjlDto qmngsndzxxjlDto : qmngsndzxxjls) { 
				if(StringUtil.isNotBlank(qmngsndzxxjlDto.getJldjt())) {
					if("1".equals(qmngsndzxxjlDto.getJldjt())) {
						qmngsndzxxjla=qmngsndzxxjlDto;
					}
					if("3".equals(qmngsndzxxjlDto.getJldjt())) {
						qmngsndzxxjlb=qmngsndzxxjlDto; 
					}
					if("5".equals(qmngsndzxxjlDto.getJldjt())) {
						qmngsndzxxjlc=qmngsndzxxjlDto; 
					}
					if("7".equals(qmngsndzxxjlDto.getJldjt())) {
						qmngsndzxxjld=qmngsndzxxjlDto; 
					}
					if("28".equals(qmngsndzxxjlDto.getJldjt())) {
						qmngsndzxxjle=qmngsndzxxjlDto; 
					}					
				}
			}
		}
		
		qmngsndzxxjla.setDmxqs(qmngsdmxqsa);
		qmngsndzxxjlb.setDmxqs(qmngsdmxqsb);
		qmngsndzxxjlc.setDmxqs(qmngsdmxqsc);
		qmngsndzxxjld.setDmxqs(qmngsdmxqsd);
		qmngsndzxxjle.setDmxqs(qmngsdmxqse);
		
		qmngsndzxxjla.setYzzbs(qmngsyzzbsa);
		qmngsndzxxjlb.setYzzbs(qmngsyzzbsb);
		qmngsndzxxjlc.setYzzbs(qmngsyzzbsc);
		qmngsndzxxjld.setYzzbs(qmngsyzzbsd);
		qmngsndzxxjle.setYzzbs(qmngsyzzbse);
		
		qmngsndzxxjla.setShs(qmngsshsa);
		qmngsndzxxjlb.setShs(qmngsshsb);
		qmngsndzxxjlc.setShs(qmngsshsc);
		qmngsndzxxjld.setShs(qmngsshsd);
		qmngsndzxxjle.setShs(qmngsshse);
		
		qmngsndzxxjla.setXcgs(qmngsxcgsa);
		qmngsndzxxjlb.setXcgs(qmngsxcgsb);
		qmngsndzxxjlc.setXcgs(qmngsxcgsc);
		qmngsndzxxjld.setXcgs(qmngsxcgsd);
		qmngsndzxxjle.setXcgs(qmngsxcgse);
		
		map.put("qmngsndzxxjlsa", qmngsndzxxjla);
		map.put("qmngsndzxxjlsb", qmngsndzxxjlb);
		map.put("qmngsndzxxjlsc", qmngsndzxxjlc);
		map.put("qmngsndzxxjlsd", qmngsndzxxjld);
		map.put("qmngsndzxxjlse", qmngsndzxxjle);
		return map;
	}

	/**
	 * 选中导出
	 * @param params
	 * @return
	 */
	public List<QmngsndzxxjlDto> getListForSelectExp(Map<String, Object> params){
		QmngsndzxxjlDto qmngsndzxxjlDto = (QmngsndzxxjlDto) params.get("entryData");
		queryJoinFlagExport(params,qmngsndzxxjlDto);
		List<QmngsndzxxjlDto> list = dao.getListForSelectExp(qmngsndzxxjlDto);
		if(list!=null&&list.size()>0){
			List<JcsjDto> jwbsList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.QMNGSCOMPLICATION));
			List<JcsjDto> grbwList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.QMNGSANTIBACTERIALHISTORY));
			for(QmngsndzxxjlDto qmngsndzxxjlDto_t:list){
				if(com.matridx.springboot.util.base.StringUtil.isNotBlank(qmngsndzxxjlDto_t.getJwhbz())){
					String jwbs="";
					String[] split = qmngsndzxxjlDto_t.getJwhbz().split(",");
					for(String s:split){
						for(JcsjDto dto:jwbsList){
							if(s.equals(dto.getCsid())){
								jwbs+=","+dto.getCsmc();
								break;
							}
						}
					}
					if(com.matridx.springboot.util.base.StringUtil.isNotBlank(jwbs)){
						qmngsndzxxjlDto_t.setJwhbz(jwbs.substring(1));
					}
				}
				if(com.matridx.springboot.util.base.StringUtil.isNotBlank(qmngsndzxxjlDto_t.getGrbw())){
					String grbw="";
					String[] split = qmngsndzxxjlDto_t.getGrbw().split(",");
					for(String s:split){
						for(JcsjDto dto:grbwList){
							if(s.equals(dto.getCsid())){
								grbw+=","+dto.getCsmc();
								break;
							}
						}
					}
					if(com.matridx.springboot.util.base.StringUtil.isNotBlank(grbw)){
						qmngsndzxxjlDto_t.setGrbw(grbw.substring(1));
					}
				}

			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String,Object> params, QmngsndzxxjlDto qmngsndzxxjlDto){
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuffer sqlParam = new StringBuffer();
		for(DcszDto dcszDto:choseList){
			if(dcszDto == null || dcszDto.getDczd() == null)
				continue;

			sqlParam.append(",");
			if(com.matridx.springboot.util.base.StringUtil.isNotBlank(dcszDto.getSqlzd())){
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		String sqlcs=sqlParam.toString();
		qmngsndzxxjlDto.setSqlParam(sqlcs);
	}
}
