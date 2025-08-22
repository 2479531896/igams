package com.matridx.igams.experiment.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.CxyxxDto;
import com.matridx.igams.experiment.dao.entities.CxyxxModel;
import com.matridx.igams.experiment.dao.post.ICxyxxDao;
import com.matridx.igams.experiment.service.svcinterface.ICxyxxService;

@Service
public class CxyxxServiceImpl extends BaseBasicServiceImpl<CxyxxDto, CxyxxModel, ICxyxxDao> implements ICxyxxService{

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	IJcsjService jcsjService;
	/**
	 * 更新测序仪信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateCxyxx(CxyxxDto cxyxxDto) throws BusinessException {
		String token = "eba3f34c24943e1c";//token固定永久有效
		RestTemplate resttemplate = new RestTemplate();
		List<Map<String,Object>> cxylist;
		//获取生信部测序仪列表
		String getcxxyurl = "http://bcl.matridx.top/BCL/api/machine/";
		cxylist=resttemplate.getForObject(getcxxyurl +"?token="+token, List.class);
		List<CxyxxDto> cxyxxlist= new ArrayList<>();
		List<Map<String,String>> cxyxx_jcsj_list= new ArrayList<>();
		List<JcsjDto> cxyAddList = new ArrayList<>();//用于存储新增的测序仪基础数据
		List<JcsjDto> jcsj_cxys = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SEQUENCER_CODE.getCode());
		List<JcsjDto> jcsj_jcdws = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());

		if(cxylist!=null && cxylist.size()>0) {
			for (Map<String, Object> stringObjectMap : cxylist) {
				CxyxxDto t_cxyxxDto = new CxyxxDto();
				t_cxyxxDto.setCxyid(String.valueOf(stringObjectMap.get("id")));
				t_cxyxxDto.setCxybh(String.valueOf(stringObjectMap.get("uid")));
				t_cxyxxDto.setMc(String.valueOf(stringObjectMap.get("name")));
				t_cxyxxDto.setCzxt(String.valueOf(stringObjectMap.get("os")));
				t_cxyxxDto.setXh(String.valueOf(stringObjectMap.get("model")));
				if ((boolean) stringObjectMap.get("index2_reversed")) {
					t_cxyxxDto.setJt2bj("1");
				} else {
					t_cxyxxDto.setJt2bj("0");
				}
				t_cxyxxDto.setCs(String.valueOf(stringObjectMap.get("organization")));
				t_cxyxxDto.setCxxhsj(String.valueOf(stringObjectMap.get("expect_seq_hour")));
				t_cxyxxDto.setScbj((boolean)stringObjectMap.get("active")?"0":"2");
				cxyxxlist.add(t_cxyxxDto);
				//判断基础数据中测序仪信息的状态，如果不存在则打算新增，存在则更新 2025-07-03
				boolean is_exist=false;
				for (JcsjDto jcsjDto : jcsj_cxys) {
					if(jcsjDto.getCsdm().equals(t_cxyxxDto.getCxybh())) {
						is_exist =true;
						break;
					}
				}
				if(!is_exist) {
					if("2".equals(t_cxyxxDto.getScbj()))
						continue;
					JcsjDto jcsjDto=new JcsjDto();
					jcsjDto.setCsid(StringUtil.generateUUID());
					jcsjDto.setJclb(BasicDataTypeEnum.SEQUENCER_CODE.getCode());
					jcsjDto.setCsdm(String.valueOf(stringObjectMap.get("uid")));
					jcsjDto.setCsmc(String.valueOf(stringObjectMap.get("name")));
					//确认测序仪所属单位
					for (JcsjDto dw_jcsjDto : jcsj_jcdws) {
						if(dw_jcsjDto.getCsdm().equals(t_cxyxxDto.getCs())) {
							jcsjDto.setFcsid(dw_jcsjDto.getCsid());
							break;
						}
					}
					jcsjDto.setCspx("100");
					jcsjDto.setSfmr("0");
					jcsjDto.setSfgb("0");
					jcsjDto.setLrry("测序仪更新");
					cxyAddList.add(jcsjDto);
				}else {
					Map<String,String> cxy_jcsj_map=new HashMap<>();
					cxy_jcsj_map.put("mc",t_cxyxxDto.getMc());
					cxy_jcsj_map.put("cxybh",t_cxyxxDto.getCxybh());
					cxy_jcsj_map.put("cxydw",t_cxyxxDto.getCs());
					//确认测序仪所属单位
					for (JcsjDto dw_jcsjDto : jcsj_jcdws) {
						if(dw_jcsjDto.getCsdm().equals(t_cxyxxDto.getCs())) {
							cxy_jcsj_map.put("fcsid",dw_jcsjDto.getCsid());
							break;
						}
					}
					cxy_jcsj_map.put("scbj",(boolean)stringObjectMap.get("active")?"0":"1");
					cxyxx_jcsj_list.add(cxy_jcsj_map);
				}
			}
		}

		boolean result_add=false;
		if(cxyxx_jcsj_list.size()>0){
			if(cxyxx_jcsj_list.size()>10){
				int length = (int)Math.ceil((double)cxyxx_jcsj_list.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<Map<String,String>> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = cxyxx_jcsj_list.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(cxyxx_jcsj_list.get(j));
					}
					result_add = dao.updateCxyJcsj(list);
					if(!result_add) {
						throw new BusinessException("msg", "更新测序仪基础数据信息失败！");
					}
				}
			}else{
				result_add = dao.updateCxyJcsj(cxyxx_jcsj_list);
				if(!result_add) {
					throw new BusinessException("msg", "更新测序仪基础数据信息失败！");
				}
			}
		}
		if(cxyxxlist.size()>0){
			dao.delete(cxyxxDto);//先删除
			if(cxyxxlist.size()>10){
				int length = (int)Math.ceil((double)cxyxxlist.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<CxyxxDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = cxyxxlist.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(cxyxxlist.get(j));
					}
					result_add = dao.insertCxyFromOut(list);
					if(!result_add) {
						throw new BusinessException("msg", "新增测序仪信息失败！");
					}
				}
			}else{
				result_add = dao.insertCxyFromOut(cxyxxlist);
				if(!result_add) {
					throw new BusinessException("msg", "新增测序仪信息失败！");
				}
			}
		}
		if (!cxyAddList.isEmpty()) {
			jcsjService.insertList(cxyAddList);
		}

		return result_add;
	}

	/**
	 * 定时更新测序仪信息
	 */
	public void setTimeToupdateCxyxx() throws BusinessException {
		CxyxxDto cxyxxDto=new CxyxxDto();
		updateCxyxx(cxyxxDto);
	}

	public List<CxyxxDto> getDtoByMcOrCxybh(CxyxxDto cxyxxDto){
		return dao.getDtoByMcOrCxybh(cxyxxDto);
	}
}
