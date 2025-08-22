package com.matridx.igams.common.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.service.svcinterface.IPcrsyglService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.post.IPcrsyjgDao;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IPcrsyjgService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PcrsyjgServiceImpl extends BaseBasicServiceImpl<PcrsyjgDto, PcrsyjgModel, IPcrsyjgDao>
		implements IPcrsyjgService {
	@Autowired
	private IJcsjService jcsjService;

	@Autowired
	IPcrsyglService pcrsyglService;

	private final Logger log = LoggerFactory.getLogger(PcrsyjgServiceImpl.class);

	/**
	 * 除100 保留两位小数
	 */
	public String exceptOneHundred(String value){
		if (StringUtil.isNotBlank(value)){
			if ("-100".equals(value)){
				return "-";
			}else{
				BigDecimal Hundred = new BigDecimal("100");
				BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(value)).divide(Hundred,2, RoundingMode.HALF_UP);
				return String.valueOf(bigDecimal);
			}
		}
		return "-";
	}
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean savePcrsyjgDto(WkmxPcrModel wkmxPcrModel) {
		log.error("---------------天隆列表数据开始-----------------");
		//pcr实验管理表新增数据
		PcrsyglDto pcrsyglDto = new PcrsyglDto();
		pcrsyglDto.setPcrsyglid(StringUtil.generateUUID());
		pcrsyglDto.setIsyz(wkmxPcrModel.getIspcr());
		// 转换一下实验室名称转成id
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
		jcsjDto.setCsmc(wkmxPcrModel.getJcdw());
		JcsjDto jsDto = jcsjService.getByAndCsdm(jcsjDto);
		String jcdw = wkmxPcrModel.getJcdw();
		if (jsDto != null)
			jcdw = jsDto.getCsid();
		pcrsyglDto.setJcdw(jcdw);
		pcrsyglService.insert(pcrsyglDto);
		//pcr实验结果表新增数据
		List<WkmxPcrResultModel> result = wkmxPcrModel.getResult();
		List<PcrsyjgDto> ls = new ArrayList<>();
		if (result!=null && !result.isEmpty()){
			for (WkmxPcrResultModel resultModel:result) {
				PcrsyjgDto pcrsyjgDto = new PcrsyjgDto();
				pcrsyjgDto.setJcdw(jcdw);
				pcrsyjgDto.setPcrsyglid(pcrsyglDto.getPcrsyglid());
				pcrsyjgDto.setDlysymxid(StringUtil.generateUUID());
				pcrsyjgDto.setCtvalue(exceptOneHundred(resultModel.getCtVaule()));
				pcrsyjgDto.setConcentration(resultModel.getConcentration()==null?"":resultModel.getConcentration());
				pcrsyjgDto.setConcentrationunit(resultModel.getConcentrationUnit()==null?"":resultModel.getConcentrationUnit());
				pcrsyjgDto.setStdconcentration(resultModel.getStdConcentration()==null?"":resultModel.getStdConcentration());
				pcrsyjgDto.setReferencedye(resultModel.getReferenceDye()==null?"":resultModel.getReferenceDye());
				pcrsyjgDto.setSampleuid(resultModel.getSampleUID()==null?"":resultModel.getSampleUID());
				pcrsyjgDto.setReplicatedgroup(resultModel.getReplicatedGroup()==null?"":resultModel.getReplicatedGroup());
				pcrsyjgDto.setQcresult(resultModel.getQcResult()==null?"":resultModel.getQcResult());
				pcrsyjgDto.setWellhindex(resultModel.getWellHIndex()==null?"":resultModel.getWellHIndex());
				pcrsyjgDto.setWellvindex(resultModel.getWellVIndex()==null?"":resultModel.getWellVIndex());
				pcrsyjgDto.setWell(resultModel.getWell()==null?"":resultModel.getWell());
				pcrsyjgDto.setSamplenumber(resultModel.getSampleNumber()==null?"":resultModel.getSampleNumber());
				pcrsyjgDto.setSamplename(resultModel.getSampleName()==null?"":resultModel.getSampleName());
				pcrsyjgDto.setSampletype(resultModel.getSampleType()==null?"":resultModel.getSampleType());
				pcrsyjgDto.setDyename(resultModel.getDyeName()==null?"":resultModel.getDyeName());
				pcrsyjgDto.setGenename(resultModel.getGeneName()==null?"":resultModel.getGeneName());
				ls.add(pcrsyjgDto);
			}
			dao.insertListPcr(ls);
			log.error("---------------天隆列表数据结束----------------");
		}



//		Map<String, List<WkmxPcrResultModel>> map = result.stream().collect(Collectors.groupingBy(WkmxPcrResultModel::getWell));
//		if (null != map && map.size()>0){
//			List<PcrsyjgDto> ls = new ArrayList<PcrsyjgDto>();
//			// 转换一下实验室名称转成id
//			JcsjDto jcsjDto = new JcsjDto();
//			jcsjDto.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
//			jcsjDto.setCsmc(wkmxPcrModel.getJcdw());
//			JcsjDto jsDto = jcsjService.getByAndCsdm(jcsjDto);
//			String jcdw = wkmxPcrModel.getJcdw();
//			if (jsDto != null)
//				jcdw = jsDto.getCsid();
//			Iterator<Map.Entry<String, List<WkmxPcrResultModel>>> entries = map.entrySet().iterator();
//			while (entries.hasNext()) {
//				Map.Entry<String,  List<WkmxPcrResultModel>> entry = entries.next();
//				String key = entry.getKey();
//				List<WkmxPcrResultModel> resultModelList = entry.getValue();
//				if (null != resultModelList && resultModelList.size() > 0){
//					PcrsyjgDto pcrsyjgDto = new PcrsyjgDto();
//					pcrsyjgDto.setJcdw(jcdw);
//					pcrsyjgDto.setDlysymxid(StringUtil.generateUUID());
//					pcrsyjgDto.setConcentration(resultModelList.get(0).getConcentration());
//					pcrsyjgDto.setConcentrationunit(resultModelList.get(0).getConcentrationUnit());
//					pcrsyjgDto.setStdconcentration(resultModelList.get(0).getStdConcentration());
////					pcrsyjgDto.setReferencedye(resultModelList.get(0).getReferenceDye());
//					pcrsyjgDto.setSampleuid(resultModelList.get(0).getSampleUID());
//					pcrsyjgDto.setReplicatedgroup(resultModelList.get(0).getReplicatedGroup());
//					pcrsyjgDto.setQcresult(resultModelList.get(0).getQcResult());
//					pcrsyjgDto.setWellhindex(resultModelList.get(0).getWellHIndex());
//					pcrsyjgDto.setWellvindex(resultModelList.get(0).getWellVIndex());
//					pcrsyjgDto.setWell(resultModelList.get(0).getWell());
//					pcrsyjgDto.setSamplenumber(resultModelList.get(0).getSampleNumber());
//					pcrsyjgDto.setSamplename(resultModelList.get(0).getSampleName());
//					pcrsyjgDto.setSampletype(resultModelList.get(0).getSampleType());
//					pcrsyjgDto.setIsyz(wkmxPcrModel.getIspcr());
//					pcrsyjgDto.setJssj(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//					for (WkmxPcrResultModel resultModel:resultModelList) {
//						if (StringUtil.isNotBlank(resultModel.getGeneName())){
//							pcrsyjgDto.setGenename(resultModel.getGeneName());
//						}
//						if (StringUtil.isNotBlank(resultModel.getReferenceDye()) && StringUtil.isNotBlank(resultModel.getCtVaule())){
//							switch (resultModel.getReferenceDye()){
//								case "Cy5":
//									pcrsyjgDto.setCy5(resultModel.getCtVaule());
//									break;
//								case "FAM":
//									pcrsyjgDto.setFam(resultModel.getCtVaule());
//									break;
//								case "HEX":
//									pcrsyjgDto.setHex(resultModel.getCtVaule());
//									break;
//								case "ROX":
//									pcrsyjgDto.setRox(resultModel.getCtVaule());
//									break;
//							}
//						}
//					}
//					ls.add(pcrsyjgDto);
//				}
//
//			}
//			dao.insertListPcr(ls);
		return true;
	}

	/**
	 * 通过角色ID获得角色检测单位信息
	 */
	@Override
	public List<Map<String, String>> getJsjcdwByjsid(String dqjs) {
		return dao.getJsjcdwByjsid(dqjs);
	}
}
