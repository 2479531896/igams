package com.matridx.igams.experiment.service.impl;

import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.experiment.dao.entities.NgsglDto;
import com.matridx.igams.experiment.dao.entities.NgsglModel;
import com.matridx.igams.experiment.dao.entities.NgsglWsDto;
import com.matridx.igams.experiment.dao.post.INgsglDao;
import com.matridx.igams.experiment.service.svcinterface.INgsglService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class NgsglServiceImpl extends BaseBasicServiceImpl<NgsglDto, NgsglModel, INgsglDao> implements INgsglService{

	@Autowired
	IJcsjService jcsjService;
	
	private final Logger log = LoggerFactory.getLogger(NgsglServiceImpl.class);
	/**
	 * 接收ngs信息并保存
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSaveNgsDtos(List<NgsglWsDto> list) {
		boolean result=false;
		try {
			if(list != null && list.size()>0) {
				List<NgsglDto> add_list= new ArrayList<>();
				List<Map<String, Object>> maps = new ArrayList<>();
				for (NgsglWsDto ngsglWsDto : list) {
					if (ngsglWsDto != null) {
						Map<String, Object> map = new HashMap<>();
						//处理内部编码格式，截取内部编码
						String nbbm = ngsglWsDto.getSampleName();
						if (StringUtil.isNotBlank(nbbm) && !(nbbm.startsWith("NC-") || nbbm.startsWith("PC-")|| nbbm.startsWith("DC-"))) {
							String[] strings = nbbm.split(" ");//中文输入法，可能存在空格丢失情况
							nbbm = strings[0];
							//截取空格，但可能出现中文输入下，空格丢失情况，此时手动截取内部编码
							if (StringUtil.isNotBlank(strings[0]) && strings.length == 1) {
								if (strings[0].length() >= 14 && (strings[0].startsWith("DY") || strings[0].startsWith("DX"))) {//科研编号DY开头，14位长度
									nbbm = strings[0].substring(0, 14);
									List<String> nbbm_ts = dao.getSjxxByLikeNbbm(nbbm);
									if (CollectionUtils.isNotEmpty(nbbm_ts) && nbbm_ts.size() == 1) {
										nbbm = nbbm_ts.get(0);
									}
								} else if (strings[0].length() >= 15 && strings[0].startsWith("MD")) {
									nbbm = strings[0].substring(0, 15);
									List<String> nbbm_ts = dao.getSjxxByLikeNbbm(nbbm);
									if (CollectionUtils.isNotEmpty(nbbm_ts) && nbbm_ts.size() == 1) {
										nbbm = nbbm_ts.get(0);
									}
								} else if (strings[0].length() >= 13) {//普通内部编号，13位长度
									nbbm = strings[0].substring(0, 13);
								}else if (strings[0].length() >= 11) {//内部编号，11位长度
									nbbm = strings[0].substring(0, 11);
								}
							}
						}
						map.put("sampleName", ngsglWsDto.getSampleName());
						map.put("InspectionUnitName", ngsglWsDto.getInspectionUnitName());
						map.put("StartTime", ngsglWsDto.getStartTime());
						maps.add(map);
						NgsglDto ngsglDto = new NgsglDto();
						ngsglDto.setNgsid(StringUtil.generateUUID());
						ngsglDto.setJcdwmc(ngsglWsDto.getInspectionUnitName());
						//根据基础类别和参数名称查询检测单位
						JcsjDto jcsjDto = new JcsjDto();
						jcsjDto.setCsmc(ngsglWsDto.getInspectionUnitName());
						jcsjDto.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
						jcsjDto = jcsjService.getDtoByCsmcAndJclb(jcsjDto);
						if (jcsjDto != null) {
							ngsglDto.setJcdw(jcsjDto.getCsid());
						}
						ngsglDto.setSbid(ngsglWsDto.getDeviceAddress());
						ngsglDto.setCzqx(ngsglWsDto.getAccountId());
						ngsglDto.setCjsj(ngsglWsDto.getCreated());
						ngsglDto.setJcid(ngsglWsDto.getOrderId());
						ngsglDto.setSjh(ngsglWsDto.getReagent());
						ngsglDto.setZbf(ngsglWsDto.getPreparationMethod());
						ngsglDto.setWgdz(ngsglWsDto.getMACAddress());
						ngsglDto.setSylx(ngsglWsDto.getExperimentTypeDesc());
						//制备编码
						if ("RNA".equals(ngsglWsDto.getPreparationMethod())) {
							ngsglDto.setZbbm("R");
						} else {
							ngsglDto.setZbbm("D");
						}
						ngsglDto.setNbbm(nbbm);
						ngsglDto.setTcsjph(ngsglWsDto.getReagentBox());
						ngsglDto.setJksjph(ngsglWsDto.getBuildReagent());
						ngsglDto.setJth(ngsglWsDto.getBiomarker());
						if (!"0001-01-01 00:00:00".equals(ngsglWsDto.getStartTime()))
							ngsglDto.setKssj(ngsglWsDto.getStartTime());
						if (!"0001-01-01 00:00:00".equals(ngsglWsDto.getCompleteTime()))
							ngsglDto.setJssj(ngsglWsDto.getCompleteTime());
						ngsglDto.setSfcg(ngsglWsDto.getSuccessStatus());
						ngsglDto.setBz(ngsglWsDto.getNote());
						ngsglDto.setLrry(ngsglWsDto.getAccountName());
						NgsglDto t_ngsglDto = dao.getDtoByJcid(ngsglDto);
						Date date = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						//如果为2次发送，则更新
						String kssj = ngsglWsDto.getStartTime();
						String jssj = ngsglWsDto.getCompleteTime();
						if ("0001-01-01 00:00:00".equals(kssj))
							kssj = "";
						if ("0001-01-01 00:00:00".equals(jssj))
							jssj = "";
						if (t_ngsglDto != null) {
							String rznr;
							if (StringUtils.isNotBlank(t_ngsglDto.getRz())) {
								rznr = t_ngsglDto.getRz() + "\r\n" + "----修改----开始时间:" + kssj + "　　结束时间:" + jssj + "　　是否成功:" + ngsglWsDto.getIsSucceed();
							} else {
								rznr = "----修改----开始时间:" + kssj + "　　结束时间:" + jssj + "　　是否成功:" + ngsglWsDto.getIsSucceed();
							}
							if ("0".equals(ngsglWsDto.getIsSucceed()) && StringUtils.isNotBlank(ngsglWsDto.getNote()))
								rznr = rznr + "　　错误信息:" + ngsglWsDto.getNote();
							rznr = rznr + "　　记录时间:" + formatter.format(date);
							ngsglDto.setRz(rznr);
							ngsglDto.setXgry(ngsglWsDto.getAccountName());
							ngsglDto.setNgsid(t_ngsglDto.getNgsid());
							int update_result = dao.update(ngsglDto);
							result = update_result > 0;
						} else {
							String rznr = "----新增----开始时间:" + kssj + "　　结束时间:" + jssj + "　　是否成功:" + ngsglWsDto.getIsSucceed();
							if ("1".equals(ngsglWsDto.getIsSucceed()))
								rznr = rznr + "　　错误信息:" + ngsglWsDto.getNote();
							rznr = rznr + "　　记录时间:" + formatter.format(date);
							ngsglDto.setRz(rznr);
							add_list.add(ngsglDto);
						}
					}
				}
				if(add_list.size()>0) {
					result=dao.addSaveNgsDtos(add_list);
//					if (maps.size()>0){
//						MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
//						RestTemplate restTemplate=new RestTemplate();
//						paramMap.add("json", JSON.toJSONString(maps));
//						String reString = restTemplate.postForObject(applicationurl+"/ws/inspection/modExperimentDate", paramMap, String.class);
//					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage());
			return false;
		}
		return result;
	}
	
	/**
	 * 根据制备编码和内部编码获取相应信息
	 */
	public List<NgsglDto> getDtoByZbbmAndNbbm(NgsglDto ngsglDto) {
		return dao.getDtoByZbbmAndNbbm(ngsglDto);
	}
	
	/**
	 * 根据检测ID获取ngs信息
	 */
	public NgsglDto getDtoByJcid(NgsglDto ngsglDto) {
		return dao.getDtoByJcid(ngsglDto);
	}

	
	/**
	 * 获取所有ngs信息
	 */
	public List<NgsglDto> getPageNgsglDto(NgsglDto ngsglDto) {
		return dao.getPageNgsglDto(ngsglDto);
	}

	/**
	 * 根据ngsid获取ngs详细信息
	 */
	public NgsglDto getNgsByngsid(String ngsid) {
		return dao.getDtoByngsid(ngsid);
	}
	

	/**
	 * 高级筛选是否成功
	 */
	@Override
	public List<NgsglDto> queryBySfcg() {
		// TODO Auto-generated method stub
		return dao.queryBySfcg();
	}
	/**
	 * 导出
	 */
	@Override
	public int getCountForSearchExp(NgsglDto ngsglDto, Map<String, Object> params) {
		return dao.getCountForSearchExp(ngsglDto);
	}
	/**
	 * 根据搜索条件获取导出信息
	 */
	public List<NgsglDto> getListForSearchExp(Map<String,Object> params){
		NgsglDto ngsglDto = (NgsglDto)params.get("entryData");
		queryJoinFlagExport(params,ngsglDto);
		return dao.getListForSearchExp(ngsglDto);
	}
	
	/**
	 * 根据选择信息获取导出信息
	 */
	public List<NgsglDto> getListForSelectExp(Map<String,Object> params){
		NgsglDto ngsglDto = (NgsglDto)params.get("entryData");
		queryJoinFlagExport(params,ngsglDto);
		return dao.getListForSelectExp(ngsglDto);
	}
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String,Object> params,NgsglDto ngsglDto){
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
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
		ngsglDto.setSqlParam(sqlcs);
	}

}
