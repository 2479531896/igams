package com.matridx.igams.production.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.HtmxModel;
import com.matridx.igams.production.dao.post.IHtmxDao;
import com.matridx.igams.production.service.svcinterface.IHtmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.springframework.util.CollectionUtils;

@Service
public class HtmxServiceImpl extends BaseBasicServiceImpl<HtmxDto, HtmxModel, IHtmxDao> implements IHtmxService{
	
	@Autowired
	DingTalkUtil talkUtil;
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	IJcsjService jcsjService;
	
	@Autowired
	IDdxxglService ddxxglService;
	@Lazy
	@Autowired
	IRdRecordService rdRecordService;
	/**
	 * 根据合同ID获取合同明细信息
	 */
	@Override
	public List<HtmxDto> getListByHtid(String htid) {
		// TODO Auto-generated method stub
		return dao.getListByHtid(htid);
	}

	/**
	 * 更新合同明细信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateHtmxDtos(List<HtmxDto> htmxDtos, HtglDto htglDto) {
		// TODO Auto-generated method stub
		if(!CollectionUtils.isEmpty(htmxDtos) && StringUtil.isNotBlank(htglDto.getHtid())) {
			//获取新增的合同明细
			List<HtmxDto> addHtmxDtos = new ArrayList<>();
			List<HtmxDto> selectHtmxDtos = dao.getListByHtid(htglDto.getHtid());
			for (int i = htmxDtos.size()-1; i >= 0; i--) {
				htmxDtos.get(i).setHtid(htglDto.getHtid());
				htmxDtos.get(i).setXgry(htglDto.getXgry());
				htmxDtos.get(i).setLrry(htglDto.getLrry());
				htmxDtos.get(i).setScry(htglDto.getScry());
				htmxDtos.get(i).setSuil(htglDto.getSl());
				//处理原计划到货日期
				for(HtmxDto htmxDto : selectHtmxDtos){
					if(htmxDto.getHtmxid().equals(htmxDtos.get(i).getHtmxid()))
						htmxDtos.get(i).setYjhdhrq(htmxDto.getJhdhrq());
				}
				if(StringUtil.isBlank(htmxDtos.get(i).getHtmxid())) {
					htmxDtos.get(i).setHtmxid(htmxDtos.get(i).getHtmxidbj());
					addHtmxDtos.add(htmxDtos.get(i));
					htmxDtos.remove(i);
				}
			}
			//获取删除的合同明细
			List<String> delHtmxids = new ArrayList<>();
			List<HtmxDto> delHtmxDtos = new ArrayList<>();
			for (int i = selectHtmxDtos.size()-1; i >= 0; i--) {
				boolean isDel = true;
				for (HtmxDto htmxDto : htmxDtos) {
					if (selectHtmxDtos.get(i).getHtmxid().equals(htmxDto.getHtmxid())) {
						isDel = false;
						break;
					}
				}
				if(isDel) {
					delHtmxids.add(selectHtmxDtos.get(i).getHtmxid());
					selectHtmxDtos.get(i).setXgry(htglDto.getXgry());
					delHtmxDtos.add(selectHtmxDtos.get(i));
				}
			}
			boolean result = insertHtmxDtos(addHtmxDtos);
			if(!result)
				return false;
			HtmxDto htmxDto = new HtmxDto();
			htmxDto.setIds(delHtmxids);
			htmxDto.setScbj("1");
			htmxDto.setScry(htglDto.getScry());
			result = deleteHtmxDtos(htmxDto);
			if(!result)
				return false;
			List<HtmxDto> preHtmxDtos = null;
			if(!CollectionUtils.isEmpty(htmxDtos)) {
				// 获取修改的合同明细信息
				preHtmxDtos = getUpdateHtmxDtos(htmxDtos);
				//获取修改的合同明细(剩余的htmxDtos)
				result = updateHtmxDtos(htmxDtos);
				if(!result)
					return false;
			}
			//补充合同不修改请购数量
			if (StringUtil.isBlank(htglDto.getBchtid())){
				// 审核通过前请购明细数量修改
				result = updatePurchasePreQuantity(addHtmxDtos, delHtmxDtos, preHtmxDtos);
			}
			return result;
		}
		return true;
	}

	/**
	 * 审核通过前请购明细数量修改
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updatePurchasePreQuantity(List<HtmxDto> addHtmxDtos, List<HtmxDto> delHtmxDtos, List<HtmxDto> preHtmxDtos) {
		// TODO Auto-generated method stub
		// 新增合同明细请购数量修改 yds += sl
		if(!CollectionUtils.isEmpty(addHtmxDtos)) {
			dao.updateQuantityAdd(addHtmxDtos);
		}
		// 删除合同明细请购数量修改 yds -= sl
		if(!CollectionUtils.isEmpty(delHtmxDtos)) {
			dao.updateQuantityDel(delHtmxDtos);
		}
		// 修改合同明细请购数量修改 yds += sl-presl
		if(!CollectionUtils.isEmpty(preHtmxDtos)) {
			dao.updateQuantityAdd(preHtmxDtos);
		}
		return true;
	}

	/**
	 * 批量修改合同明细信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateHtmxDtos(List<HtmxDto> htmxDtos) {
		// TODO Auto-generated method stub
		if(!CollectionUtils.isEmpty(htmxDtos)) {
			int result = dao.updateHtmxDtos(htmxDtos);
			return result != 0;
		}
		return true;
	}

	/**
	 * 批量删除合同明细信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteHtmxDtos(HtmxDto htmxDto) {
		// TODO Auto-generated method stub
		if(!CollectionUtils.isEmpty(htmxDto.getIds())) {
			int result = dao.deleteHtmxDtos(htmxDto);
			return result != 0;
		}
		return true;
	}

	/**
	 * 批量新增合同明细信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertHtmxDtos(List<HtmxDto> addHtmxDtos) {
		// TODO Auto-generated method stub
		if(!CollectionUtils.isEmpty(addHtmxDtos)) {
			int result = dao.insertHtmxDtos(addHtmxDtos);
			return result != 0;
		}
		return true;
	}

	/**
	 * 含税单价统计
	 */
	@Override
	public List<Map<String,Object>> statisticTaxPrice(HtmxDto htmxDto) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> mapList = dao.statisticTaxPrice(htmxDto);
		List<Map<String, Object>> newMapList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(mapList)){
			for (int i = 0; i <mapList.size(); i++) {
				if (newMapList.size()==10){
					break;
				}
				if (i==0){
					Map<String, Object> map = new HashMap<>();
					map.put("tjmc",mapList.get(i).get("tjmc"));
					map.put("tjsl",mapList.get(i).get("tjsl"));
					map.put("kssj",mapList.get(i).get("cjrq"));
					map.put("jssj",mapList.get(i).get("cjrq")+"@1");
					newMapList.add(map);
				}else {
					Map<String, Object> sjmap = newMapList.get(newMapList.size() - 1);
					Map<String, Object> map_db = mapList.get(i);
					if (sjmap.get("tjsl").equals(map_db.get("tjsl"))) {
						//连续出现次数
						String cxcs = String.valueOf(sjmap.get("jssj")).split("@")[1];
						sjmap.put("jssj",map_db.get("cjrq")+"@"+(Integer.parseInt(cxcs)+1));
					}else {
						Map<String, Object> map = new HashMap<>();
						map.put("tjmc", mapList.get(i).get("tjmc"));
						map.put("tjsl", mapList.get(i).get("tjsl"));
						map.put("kssj", mapList.get(i).get("cjrq"));
						map.put("jssj",mapList.get(i).get("cjrq")+"@1");
						newMapList.add(map);
					}
				}
			}
			for (Map<String, Object> map : newMapList) {
				String[] jssjs = String.valueOf(map.get("jssj")).split("@");
				if (jssjs[0].equals(map.get("kssj"))){
					map.put("xname","("+map.get("kssj")+" "+jssjs[1]+"次)");
				}else {
					map.put("xname","("+map.get("kssj")+"~"+jssjs[0]+" "+jssjs[1]+"次)");
				}
				map.put("tjmc",map.get("xname"));
				map.remove("kssj");
				map.remove("jssj");
			}
			newMapList = newMapList.stream().sorted((map1, map2) -> {
				String time1 = map1.get("xname").toString();
				String  time2 = map2.get("xname").toString();
				return time2.compareTo(time1);
			}).collect(Collectors.toList());
		}
		return newMapList;
	}

	/**
	 * 采购物料周期统计
	 */
	@Override
	public List<Map<String, Object>> statisticCycle(HtmxDto htmxDto) {
		// TODO Auto-generated method stub
		return dao.statisticCycle(htmxDto);
	}

	/**
	 * 导出
	 */
	@Override
	public int getCountForSearchExp(HtmxDto htmxDto, Map<String, Object> params) {
		return dao.getCountForSearchExp(htmxDto);
	}
	/**
	 * 根据搜索条件获取导出信息
	 */
	public List<HtmxDto> getListForSearchExp(Map<String,Object> params){
		HtmxDto htmxDto = (HtmxDto)params.get("entryData");
		queryJoinFlagExport(params,htmxDto);
		return dao.getListForSearchExp(htmxDto);
	}
	
	/**
	 * 根据选择信息获取导出信息
	 */
	public List<HtmxDto> getListForSelectExp(Map<String,Object> params){
		HtmxDto htmxDto = (HtmxDto)params.get("entryData");
		queryJoinFlagExport(params,htmxDto);
		return dao.getListForSelectExp(htmxDto);
	}
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String,Object> params,HtmxDto htmxDto){
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
		htmxDto.setSqlParam(sqlcs);
	}

	/**
	 * 查询数量校验信息(物料)
	 */
	@Override
	public List<HtmxDto> selectCheckWlglInfo(List<HtmxDto> htmxDtos) {
		// TODO Auto-generated method stub
		return dao.selectCheckWlglInfo(htmxDtos);
	}

	/**
	 * 查询数量校验信息(请购明细)
	 */
	@Override
	public List<HtmxDto> selectCheckQgmxInfo(List<HtmxDto> htmxDtos) {
		// TODO Auto-generated method stub
		return dao.selectCheckQgmxInfo(htmxDtos);
	}

	/**
	 * 新增合同明细请购数量修改(审核后)
	 */
	@Override
	public boolean updateQuantityAddLast(List<HtmxDto> addHtmxDtos) {
		// TODO Auto-generated method stub
		if(!CollectionUtils.isEmpty(addHtmxDtos)) {
			dao.updateQuantityAddLast(addHtmxDtos);
		}
		return true;
	}

	/**
	 * 删除合同明细请购数量修改(审核后)
	 */
	@Override
	public boolean updateQuantityDelLast(List<HtmxDto> delHtmxDtos) {
		// TODO Auto-generated method stub
		if(!CollectionUtils.isEmpty(delHtmxDtos)) {
			dao.updateQuantityDelLast(delHtmxDtos);
		}
		return true;
	}

	/**
	 * 获取修改的合同明细信息
	 */
	@Override
	public List<HtmxDto> getUpdateHtmxDtos(List<HtmxDto> htmxDtos) {
		// TODO Auto-generated method stub
		return dao.getUpdateHtmxDtos(htmxDtos);
	}

	/**
	 * 审核通过时修改请购明细数量
	 */
	@Override
	public boolean updateQuantityComp(List<HtmxDto> htmxDtos) {
		// TODO Auto-generated method stub
		if(!CollectionUtils.isEmpty(htmxDtos)) {
			dao.updateQuantityComp(htmxDtos);
		}
		return true;
	}

	/**
	 * 过程维护（通过ids修改）
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public boolean modSaveMaintenance(HtmxDto htmxDto) {
		boolean result = dao.modSaveMaintenance(htmxDto);
		if(result) {
//			String token = talkUtil.getToken();
			for (String string : htmxDto.getIds()) {
				HtmxDto htmxDto_t = dao.getDtoById(string);
				String kdlx;
				if("1".equals(htmxDto_t.getCskz2())) {
					kdlx=htmxDto_t.getKdlxmc();
				}else {						
					kdlx=htmxDto_t.getKdmc();
				}
				JcsjDto jcsjDto = new JcsjDto();
				jcsjDto.setJclb("DINGMESSAGETYPE");
				jcsjDto.setCsdm("LOGISTICS_WAREHOUSE_NOTICE");
				List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
				if(!CollectionUtils.isEmpty(ddxxglDtos)) {
					for (DdxxglDto ddxxglDto : ddxxglDtos) {
						if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
							talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), xxglService.getMsg("ICOMM_WL00001"),
									xxglService.getMsg("ICOMM_WL00002", htmxDto_t.getDjh(), htmxDto_t.getHtnbbh(),
											htmxDto_t.getWlbm(), htmxDto_t.getWlmc(), htmxDto_t.getSl(), kdlx, htmxDto_t.getKddh(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
				if(StringUtil.isNotBlank(htmxDto_t.getDdid())) {				
					talkUtil.sendWorkMessage(htmxDto_t.getSqryhm(), htmxDto_t.getDdid(), xxglService.getMsg("ICOMM_WL00001"),
							xxglService.getMsg("ICOMM_WL00002", htmxDto_t.getDjh(), htmxDto_t.getHtnbbh(),
									htmxDto_t.getWlbm(),htmxDto_t.getWlmc(), htmxDto_t.getSl(),kdlx,htmxDto_t.getKddh(),
							DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
				}
			}
		}
		return result;
	}
	
	/**
	 * 更新合同信息至请购明细表
	 */
	public boolean updateQgmxDtos(List<HtmxDto> list) {
		return dao.updateQgmxDtos(list);
	}
	
	/**
	 * 获取合同明细列表
	 */
	public List<HtmxDto> getHtmxList(HtmxDto htmxDto){
		return dao.getHtmxList(htmxDto);
	}

	@Override
	public int updateHtmxDhsl(HtmxDto htmxDto) {
		return dao.updateHtmxDhsl(htmxDto);
	}

	@Override
	public boolean htmxDtoModDhxx(List<HtmxDto> htmxDtos) {
		// TODO Auto-generated method stub
		int result = dao.htmxDtoModDhxx(htmxDtos);
		return result > 0;
	}
	
    
    /**
     * 根据合同ids获取合同信息（共通页面）
     */
    public List<HtmxDto> getCommonDtoListByHtmxids(HtmxDto htmxDto){
        return dao.getCommonDtoListByHtmxids(htmxDto);
    }

	/**
	 * 根据合同明细ID更新u8mxid
	 */
	public void updateU8mxid(HtmxDto htmxDto){
        dao.updateU8mxid(htmxDto);
    }

	/**
	 * 根据是否发票维护获取合同明细信息
	 */
	public List<HtmxDto> getListForInvoice(HtmxDto htmxDto){
		return dao.getListForInvoice(htmxDto);
	}

	/**
	 * 批量修改是否发票维护标记
	 */
	public void updateSffpwh(HtmxDto htmxDto){
		dao.updateSffpwh(htmxDto);
	}

	/**
	 * 根据合同ID获取请购信息
	 */
	public List<HtmxDto> getPurchaseDetails(HtmxDto htmxDto){
		return dao.getPurchaseDetails(htmxDto);
	}
	/**
	 * 获取合同明细列表数据
	 */
	public List<HtmxDto> getPageListContractDetail(HtmxDto htmxDto){
		return dao.getPageListContractDetail(htmxDto);
	}
	/**
	 * 获取合同明细关联生产结构数据
	 */
	public List<HtmxDto> getContractDetailWithStructure(HtmxDto htmxDto){
		return dao.getContractDetailWithStructure(htmxDto);
	}
	@Override
	public List<HtmxDto> getCpjgxxs(HtmxDto htmxDto) {
		return dao.getCpjgxxs(htmxDto);
	}

	@Override
	public List<HtmxDto> getCpjgByWlids(HtmxDto htmxDto) {
		return dao.getCpjgByWlids(htmxDto);
	}

	@Override
	public List<HtmxDto> getListByHtidDingTalk(String htid) {
		return dao.getListByHtidDingTalk(htid);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean openContract(HtmxDto htmxDto) throws BusinessException {
		htmxDto.setHzt("1");
		boolean update = dao.updateOpenClose(htmxDto);
		if (!update){
			throw new BusinessException("msg","修改行状态失败！");
		}
		HtmxDto dtoById = dao.getDtoByHtmxId(htmxDto.getHtmxid());
		List<HtmxDto> htmxDtos = dao.getHtmxListByHtid(dtoById.getHtid());
		boolean flag = false;
		for (HtmxDto dto : htmxDtos) {
			if ("0".equals(dto.getHzt())) {
				flag = true;
				break;
			}
		}
		if (flag){
			dtoById.setcState("2");
		}else {
			dtoById.setcState("1");
		}
		dtoById.setDkrymc(null);
		if ("0".equals(dtoById.getHtlx())){
			boolean isSuccess =  rdRecordService.updatePO_PomainAndPO_Podetails(dtoById);
			if (!isSuccess){
				throw new BusinessException("msg","同步U8数据失败！");
			}
		}else if ("2".equals(dtoById.getHtlx())){
			boolean isSuccess =  rdRecordService.updateOM_MOMainAndOM_MODetails(dtoById);
			if (!isSuccess){
				throw new BusinessException("msg","同步U8数据失败！");
			}
		}
		return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean closeContract(HtmxDto htmxDto) throws BusinessException {
		htmxDto.setHzt("0");
		boolean update = dao.updateOpenClose(htmxDto);
		if (!update){
			throw new BusinessException("msg","修改行状态失败！");
		}
		HtmxDto dtoById = dao.getDtoByHtmxId(htmxDto.getHtmxid());
		dtoById.setcState("2");
		dtoById.setGbrymc(htmxDto.getGbrymc());
		if ("0".equals(dtoById.getHtlx())){
			boolean isSuccess =  rdRecordService.updatePO_PomainAndPO_Podetails(dtoById);
			if (!isSuccess){
				throw new BusinessException("msg","同步U8数据失败！");
			}
		}else if ("2".equals(dtoById.getHtlx())){
			boolean isSuccess =  rdRecordService.updateOM_MOMainAndOM_MODetails(dtoById);
			if (!isSuccess){
				throw new BusinessException("msg","同步U8数据失败！");
			}
		}
		return true;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean closeContracts(HtmxDto htmxDto) throws BusinessException {
		htmxDto.setHzt("0");
		boolean update = dao.updateOpenCloses(htmxDto);
		if (!update){
			throw new BusinessException("msg","修改行状态失败！");
		}
		htmxDto.setcState("2");
		if ("0".equals(htmxDto.getHtlx())){
			boolean isSuccess =  rdRecordService.updatePO_PomainAndPO_Podetails(htmxDto);
			if (!isSuccess){
				throw new BusinessException("msg","同步U8数据失败！");
			}
		}else if ("2".equals(htmxDto.getHtlx())){
			boolean isSuccess =  rdRecordService.updateOM_MOMainAndOM_MODetails(htmxDto);
			if (!isSuccess){
				throw new BusinessException("msg","同步U8数据失败！");
			}
		}
		return true;
	}
	@Override
	public List<HtmxDto> getDhxx(HtmxDto htmxDto) {
		return dao.getDhxx(htmxDto);
	}

	@Override
	public List<String> getSfccdg(HtmxDto htmxDto) {
		return dao.getSfccdg(htmxDto);
	}

	@Override
	public List<HtmxDto> getPagedHisContract(HtmxDto htmxDto) {
		return dao.getPagedHisContract(htmxDto);
	}

	@Override
	public boolean insertFrameworks(List<HtmxDto> htmxDtos) {
		return dao.insertFrameworks(htmxDtos);
	}

	@Override
	public boolean updateScbj(HtmxDto htmxDto) {
		return dao.updateScbj(htmxDto);
	}

	@Override
	public HtmxDto getHisContract(HtmxDto htmxDto) {
		return dao.getHisContract(htmxDto);
	}

	@Override
	public List<HtmxDto> getFrameworkContractDetail(HtmxDto htmxDto) {
		return dao.getFrameworkContractDetail(htmxDto);
	}
	@Override
	public List<HtmxDto> getPagedtContractArrivalInfo(HtmxDto htmxDto) {
		return dao.getPagedtContractArrivalInfo(htmxDto);
	}

	@Override
	public List<HtmxDto> getNotAllArrival(HtmxDto htmxDto) {
		return dao.getNotAllArrival(htmxDto);
	}

	@Override
	public List<HtmxDto> getOriginalContractDetails(HtmxDto htmxDto) {
		return dao.getOriginalContractDetails(htmxDto);
	}

	@Override
	public List<HtmxDto> getOriginalContractU8(String htid) {
		return dao.getOriginalContractU8(htid);
	}

	@Override
	public List<HtmxDto> queryBchtmxDto(HtmxDto htmxDto) {
		return dao.queryBchtmxDto(htmxDto);
	}

	@Override
	public List<HtmxDto> getByQgidAndHtid(HtmxDto htmxDto) {
		return dao.getByQgidAndHtid(htmxDto);
	}

	@Override
	public List<HtmxDto> getByQgid(HtmxDto htmxDto) {
		return dao.getByQgid(htmxDto);
	}
}
