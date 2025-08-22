package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.CurrentStockDto;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.dao.post.IDbglDao;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IDbglService;
import com.matridx.igams.storehouse.service.svcinterface.IDbmxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class DbglServiceImpl extends BaseBasicServiceImpl<DbglDto, DbglModel, IDbglDao> implements IDbglService{

	@Autowired
	IHwxxService hwxxService;
	
	@Autowired
	IDbmxService dbmxService;
	
	@Autowired
	IRdRecordService rdRecordService;
	
	@Autowired
	ICkhwxxService ckhwxxService;
	@Autowired
	RedisUtil redisUtil;
	@Value("${matridx.rabbit.systemreceiveflg:}")
	private String systemreceiveflg;
	@Value("${sqlserver.matridxds.flag:}")
	private String matridxdsflag;
	/**
	 * 判断调拨单号是否重复
	 * @return
	 */
	@Override
	public boolean isDbdhRepeat(String dbdh, String dbid) {
		DbglDto dbglDto = new DbglDto();
		dbglDto.setDbdh(dbdh);
		dbglDto.setDbid(dbid);
		List<DbglDto> dbglDtos = dao.queryByDbdh(dbglDto);
		return dbglDtos == null || dbglDtos.size() <= 0;
	}

	/**
	 * 调拨单保存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean allocationsave(DbglDto dbglDto) throws BusinessException {
		//调拨单主表新增
		dbglDto.setDbid(StringUtil.generateUUID());
		boolean result = insert(dbglDto);
		if(!result)
			throw new BusinessException("msg","调拨单新增失败！");
		//获取dbmx信息
		List<DbmxDto> dbmxList = JSON.parseArray(dbglDto.getDbmx_json(), DbmxDto.class);
			
		//存hwid
		StringBuilder ids = new StringBuilder();		
		for (DbmxDto dbmx : dbmxList) {
			dbmx.setDbid(dbglDto.getDbid());
			dbmx.setDbmxid(StringUtil.generateUUID());
			dbmx.setLrry(dbglDto.getLrry());
			ids.append(",").append(dbmx.getDchwid());
		}
		ids = new StringBuilder(ids.substring(1));
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setIds(ids.toString());
		List<HwxxDto> hwxxList = hwxxService.getDtoList(hwxxDto);
		
		//存修改的货物信息
		List<HwxxDto> hwxxList_mod = new ArrayList<>();
		//存新增的货物信息
		List<HwxxDto> hwxxList_add = new ArrayList<>();
		//存物料id
		StringBuilder ids_wlid = new StringBuilder();
		for (DbmxDto dbmxDto : dbmxList) {
			for (HwxxDto hwxxDto_t : hwxxList) {
				ids_wlid.append(",").append(hwxxDto_t.getWlid());
				if(dbmxDto.getDchwid().equals(hwxxDto_t.getHwid())) {
					HwxxDto hwxxDto_mod = new HwxxDto();
					hwxxDto_mod.setHwid(dbmxDto.getDchwid());
					hwxxDto_mod.setModkcl(dbmxDto.getDbsl());
					hwxxList_mod.add(hwxxDto_mod);
					HwxxDto hwxxDto_add = new HwxxDto();
					hwxxDto_add.setHwid(StringUtil.generateUUID()); //生成新增的hwxx主键id
					dbmxDto.setDrhwid(hwxxDto_add.getHwid()); //存入调入货物id
					dbmxDto.setDchw(hwxxDto_t.getKwbh()); //存入调出货位ID
					hwxxDto_add.setKcl(dbmxDto.getDbsl());
					hwxxDto_add.setWlid(hwxxDto_t.getWlid());
					hwxxDto_add.setCkid(dbglDto.getZrck());
					hwxxDto_add.setZt("99");
					hwxxDto_add.setKwbh(dbmxDto.getDrhw());
					hwxxDto_add.setSl(dbmxDto.getDbsl());
					hwxxDto_add.setScrq(hwxxDto_t.getScrq());
					hwxxDto_add.setYxq(hwxxDto_t.getYxq());
					hwxxDto_add.setScph(hwxxDto_t.getScph());
					hwxxDto_add.setZsh(hwxxDto_t.getZsh());
					hwxxDto_add.setLrry(dbglDto.getLrry());
					hwxxDto_add.setDhlxdm(hwxxDto_t.getDhlxdm());
					hwxxList_add.add(hwxxDto_add);				
				}
			}
		}
		String dhlxdm_t = hwxxList_add.get(0).getDhlxdm();
		for (HwxxDto hwxx_d : hwxxList_add) {
			if(StringUtil.isNotBlank(dhlxdm_t) && !dhlxdm_t.equals(hwxx_d.getDhlxdm())) {
				result = false;
			}
			if(StringUtil.isNotBlank(hwxx_d.getDhlxdm()) && !hwxx_d.getDhlxdm().equals(dhlxdm_t)) {
				result = false;
			}
		}
		if(!result) 
			throw new BusinessException("msg","不允许到货类型不一致的货物一起调拨！");
	
		//更新货物信息
		boolean result_mod = hwxxService.updateHwxxDtos(hwxxList_mod);
		if(!result_mod)
			throw new BusinessException("msg","修改货位库存量失败");
		//新增货物信息
		boolean result_add = hwxxService.insertHwxxs(hwxxList_add);
		if(!result_add)
			throw new BusinessException("msg","新增货物信息失败");		
		//新增调拨明细单
		boolean result_mx = dbmxService.insertList(dbmxList);
		if(!result_mx)
			throw new BusinessException("msg","新增调拨明细失败");
		
		//判断转出仓库转入仓库类型是否一致
		DbglDto dbglDto_h = dao.getDtoById(dbglDto.getDbid());
		if(!dbglDto_h.getZcckid().equals(dbglDto_h.getZrckid())) {
			ids_wlid = new StringBuilder(ids_wlid.substring(1));
			String ids_ckid = dbglDto_h.getZrckid()+","+dbglDto_h.getZcckid();
			CkhwxxDto ckhwxxDto = new CkhwxxDto();
			ckhwxxDto.setIds(ids_wlid.toString());
			ckhwxxDto.setCkids(ids_ckid);
			List<CkhwxxDto> ckhwxxList = ckhwxxService.getDtoListByWlids(ckhwxxDto);
			List<CkhwxxDto> ckhwxxMod = new ArrayList<>();
			List<CkhwxxDto> ckhwxxAdd = new ArrayList<>();
			//根据仓库类型和物料id分组查询调拨明细数据
			List<DbmxDto> dbmxs = dbmxService.queryGroupBy(dbglDto.getDbid());
			for (DbmxDto dbmxDto_s : dbmxs) {
				boolean addbj = true;
				for (CkhwxxDto ckhwxxDto_t : ckhwxxList) {
					//处理转出的库存
					if(dbmxDto_s.getWlid().equals(ckhwxxDto_t.getWlid()) && ckhwxxDto_t.getCkid().equals(dbglDto_h.getZcckid())) {
						ckhwxxDto_t.setKcl(dbmxDto_s.getDbzs());
						ckhwxxDto_t.setYdsbj("0"); // 1:加 0:减
						ckhwxxMod.add(ckhwxxDto_t);
					}else if(dbmxDto_s.getWlid().equals(ckhwxxDto_t.getWlid()) && ckhwxxDto_t.getCkid().equals(dbglDto_h.getZrckid())) {
						ckhwxxDto_t.setKcl(dbmxDto_s.getDbzs());
						ckhwxxDto_t.setYdsbj("1"); // 1:加 0:减
						ckhwxxMod.add(ckhwxxDto_t);
						addbj=false;
					}
				}
				if(addbj) {
					CkhwxxDto ckhwxxDto_c = new CkhwxxDto();
					ckhwxxDto_c.setCkhwid(StringUtil.generateUUID());
					ckhwxxDto_c.setYds("0");
					ckhwxxDto_c.setKcl(dbmxDto_s.getDbzs());
					ckhwxxDto_c.setWlid(dbmxDto_s.getWlid());
					ckhwxxDto_c.setCkqxlx(dbglDto_h.getZrckqxlx());
					ckhwxxDto_c.setCkid(dbglDto_h.getZrckid());
					ckhwxxAdd.add(ckhwxxDto_c);
				}
			}
			if(!CollectionUtils.isEmpty(ckhwxxMod)) {
				boolean result_kcmod=ckhwxxService.updateList(ckhwxxMod);
				if(!result_kcmod) {
					throw new BusinessException("msg","修改仓库货物信息失败");
				}
			}
			if(!CollectionUtils.isEmpty(ckhwxxAdd)) {
				boolean result_kcAdd = ckhwxxService.insertCkhwxxs(ckhwxxAdd);
				if(!result_kcAdd) {
					throw new BusinessException("msg","新增仓库货物信息失败");
				}				
			}
		}

		String dhlxdm = !CollectionUtils.isEmpty(hwxxList)?hwxxList.get(0).getDhlxdm():"";
		if(!"OA".equals(dhlxdm) && !"1".equals(systemreceiveflg) && StringUtil.isNotBlank(matridxdsflag)){
			if (!rdRecordService.determine_Entry(dbglDto.getDbrq())){
				throw new BusinessException("ICOM99019","该月份已结账，不允许再录入U8数据，请修改单据日期!");
			}
			//生成U8调拨单
			Map<String, Object> map = rdRecordService.greqtU8DbData(dbglDto);
			//更新调拨管理表关联id
			DbglDto dbglDto_t=(DbglDto) map.get("dbglDto");
			dbglDto_t.setXgry(dbglDto.getLrry());
			boolean result_dbgl = update(dbglDto_t);
			if(!result_dbgl)
				throw new BusinessException("msg","更新调拨U8关联id失败");
			//更新调拨明细表关联id
			List<DbmxDto> dbmxList_t=(List<DbmxDto>) map.get("dbmxList");
			boolean result_dbmx = dbmxService.updateDbmxDtos(dbmxList_t);
			if(!result_dbmx)
				throw new BusinessException("msg","更新明细U8关联id失败");
			//更新货物信息库存关联id
			List<HwxxDto> hwxxlist_t=(List<HwxxDto>) map.get("hwxxlist_t");
	        List<CurrentStockDto> t_addcurrentStockList=(List<CurrentStockDto>) map.get("t_addcurrentStockList");
	        if(!CollectionUtils.isEmpty(t_addcurrentStockList)) {
	        	for(CurrentStockDto currentStockDto : t_addcurrentStockList) {
	        		for(HwxxDto hwxxDto_h : hwxxlist_t) {
	        			if(currentStockDto.getcInvCode().equals(hwxxDto_h.getWlbm()) && currentStockDto.getcBatch().equals(hwxxDto_h.getScph())) {
	        				hwxxDto_h.setKcglid(String.valueOf(currentStockDto.getAutoID()));
	            		}
	        		}
	        	}
	        }
	        boolean result_g = hwxxService.updateHwxxDtos(hwxxlist_t);
			if(!result_g)
				throw new BusinessException("msg","更新货物信息库存关联id失败！");
		}		
		return true;
	}



	/**
	 * 处理保存
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean disposalPendingSave(DbglDto dbglDto) throws BusinessException {
		//调拨单主表新增
		dbglDto.setDbid(StringUtil.generateUUID());
		boolean result = insert(dbglDto);
		if(!result)
			throw new BusinessException("msg","调拨单新增失败！");
		//获取dbmx信息
		List<DbmxDto> dbmxList = JSON.parseArray(dbglDto.getDbmx_json(), DbmxDto.class);

		//存hwid
		StringBuilder ids = new StringBuilder();
		for (DbmxDto dbmx : dbmxList) {
			dbmx.setDbid(dbglDto.getDbid());
			dbmx.setDbmxid(StringUtil.generateUUID());
			dbmx.setLrry(dbglDto.getLrry());
			ids.append(",").append(dbmx.getDchwid());
		}
		ids = new StringBuilder(ids.substring(1));
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setIds(ids.toString());
		List<HwxxDto> hwxxList = hwxxService.getDtoList(hwxxDto);

		//存修改的货物信息
		List<HwxxDto> hwxxList_mod = new ArrayList<>();
		//存新增的货物信息
		List<HwxxDto> hwxxList_add = new ArrayList<>();
		//存物料id
		StringBuilder ids_wlid = new StringBuilder();
		for (DbmxDto dbmxDto : dbmxList) {
			for (HwxxDto hwxxDto_t : hwxxList) {
				ids_wlid.append(",").append(hwxxDto_t.getWlid());
				if(dbmxDto.getDchwid().equals(hwxxDto_t.getHwid())) {
					dbmxDto.setWlid(hwxxDto_t.getWlid());
					double yclsl = Double.parseDouble(hwxxDto_t.getYclsl())+Double.parseDouble(dbmxDto.getDbsl());

					HwxxDto hwxxDto_mod = new HwxxDto();
					hwxxDto_mod.setYclsl(Double.toString(yclsl));
					hwxxDto_mod.setXgry(dbglDto.getLrry());
					hwxxDto_mod.setHwid(dbmxDto.getDchwid());
					hwxxList_mod.add(hwxxDto_mod);
					HwxxDto hwxxDto_add = new HwxxDto();
					hwxxDto_add.setHwid(StringUtil.generateUUID()); //生成新增的hwxx主键id
					dbmxDto.setDrhwid(hwxxDto_add.getHwid()); //存入调入货物id
					dbmxDto.setDchw(hwxxDto_t.getCskw()); //存入调出货位ID
					hwxxDto_add.setKcl(dbmxDto.getDbsl());//确认调拨单新生成的hwxx数据的库存量为调拨数量
					hwxxDto_add.setWlid(hwxxDto_t.getWlid());
					hwxxDto_add.setCkid(dbglDto.getZrck());
					hwxxDto_add.setZt("99");
					hwxxDto_add.setKwbh(dbmxDto.getDrhw());
					hwxxDto_add.setSl(dbmxDto.getDbsl());
					hwxxDto_add.setScrq(hwxxDto_t.getScrq());
					hwxxDto_add.setYxq(hwxxDto_t.getYxq());
					hwxxDto_add.setScph(hwxxDto_t.getScph());
					hwxxDto_add.setZsh(hwxxDto_t.getZsh());
					hwxxDto_add.setLrry(dbglDto.getLrry());
					hwxxDto_add.setDhlxdm(hwxxDto_t.getDhlxdm());
					hwxxList_add.add(hwxxDto_add);
				}
			}
		}
		
		String dhlxdm_t = hwxxList_add.get(0).getDhlxdm();
		for (HwxxDto hwxx_d : hwxxList_add) {
			if(StringUtil.isNotBlank(dhlxdm_t) && !dhlxdm_t.equals(hwxx_d.getDhlxdm())) {
				result = false;
			}
			if(StringUtil.isNotBlank(hwxx_d.getDhlxdm()) && !hwxx_d.getDhlxdm().equals(dhlxdm_t)) {
				result = false;
			}
		}
		
		if(!result) 
			throw new BusinessException("msg","不允许到货类型不一致的货物一起调拨！");
		//更新货物信息
		boolean result_mod = hwxxService.updateHwxxDtos(hwxxList_mod);
		if(!result_mod)
			throw new BusinessException("msg","修改货位库存量失败");
		//新增货物信息
		boolean result_add = hwxxService.insertHwxxs(hwxxList_add);
		if(!result_add)
			throw new BusinessException("msg","新增货物信息失败");
		//新增调拨明细单
		boolean result_mx = dbmxService.insertList(dbmxList);
		if(!result_mx)
			throw new BusinessException("msg","新增调拨明细失败");

		//维护ckhwxx库存量
		DbglDto dbglDto_h = dao.getDtoById(dbglDto.getDbid());
		ids_wlid = new StringBuilder(ids_wlid.substring(1));
		String ids_ckids = dbglDto_h.getZcckid()+","+dbglDto_h.getZrckid();
		CkhwxxDto ckhwxxDto = new CkhwxxDto();
		ckhwxxDto.setIds(ids_wlid.toString());
		ckhwxxDto.setCkids(Arrays.asList(ids_ckids));
		List<CkhwxxDto> ckhwxxList = ckhwxxService.getDtoListByWlids(ckhwxxDto);
		List<CkhwxxDto> ckhwxxMod = new ArrayList<>();
		List<CkhwxxDto> ckhwxxAdd = new ArrayList<>();
		for (DbmxDto dbmxDto_s : dbmxList) {
			if (!CollectionUtils.isEmpty(ckhwxxList)) {
				for (CkhwxxDto ckhwxxDto_t : ckhwxxList) {
					if (dbmxDto_s.getWlid().equals(ckhwxxDto_t.getWlid())&&dbglDto_h.getZcckid().equals(ckhwxxDto_t.getCkid())) {
						ckhwxxDto_t.setKcl(dbmxDto_s.getDbsl());
						ckhwxxDto_t.setYdsbj("1"); // 1:加 0:减
						ckhwxxMod.add(ckhwxxDto_t);
					}
				}
			}else {
					CkhwxxDto ckhwxxDto_c = new CkhwxxDto();
					ckhwxxDto_c.setCkhwid(StringUtil.generateUUID());
					ckhwxxDto_c.setYds("0");
					ckhwxxDto_c.setKcl(dbmxDto_s.getDbsl());
					ckhwxxDto_c.setWlid(dbmxDto_s.getWlid());
					ckhwxxDto_c.setCkqxlx(dbglDto_h.getZrckqxlx());
					ckhwxxDto_c.setCkid(dbglDto_h.getZrckid());
					ckhwxxAdd.add(ckhwxxDto_c);
			}
		}
		if(!CollectionUtils.isEmpty(ckhwxxMod)) {
			boolean result_kcmod = ckhwxxService.updateList(ckhwxxMod);
			if (!result_kcmod) {
				throw new BusinessException("msg", "修改仓库货物信息失败");
			}
		}
		if(!CollectionUtils.isEmpty(ckhwxxAdd)) {
			boolean result_kcAdd = ckhwxxService.insertCkhwxxs(ckhwxxAdd);
			if(!result_kcAdd) {
				throw new BusinessException("msg","新增仓库货物信息失败");
			}
		}
		String dhlxdm = !CollectionUtils.isEmpty(hwxxList)?hwxxList.get(0).getDhlxdm():"";
		if(!"OA".equals(dhlxdm)  && StringUtil.isNotBlank(matridxdsflag)){
			//生成U8调拨单
			if (!rdRecordService.determine_Entry(dbglDto.getDbrq())){
				throw new BusinessException("ICOM99019","该月份已结账，不允许再录入U8数据，请修改单据日期!");
			}
			Map<String, Object> map = rdRecordService.greqtU8DbData(dbglDto);
			//更新调拨管理表关联id
			DbglDto dbglDto_t=(DbglDto) map.get("dbglDto");
			dbglDto_t.setXgry(dbglDto.getLrry());
			boolean result_dbgl = update(dbglDto_t);
			if(!result_dbgl)
				throw new BusinessException("msg","更新调拨U8关联id失败");
			//更新调拨明细表关联id
			List<DbmxDto> dbmxList_t=(List<DbmxDto>) map.get("dbmxList");
			boolean result_dbmx = dbmxService.updateDbmxDtos(dbmxList_t);
			if(!result_dbmx)
				throw new BusinessException("msg","更新明细U8关联id失败");
			//更新货物信息库存关联id
			List<HwxxDto> hwxxlist_t=(List<HwxxDto>) map.get("hwxxlist_t");
			List<CurrentStockDto> t_addcurrentStockList=(List<CurrentStockDto>) map.get("t_addcurrentStockList");
			if(!CollectionUtils.isEmpty(t_addcurrentStockList)) {
				for(CurrentStockDto currentStockDto : t_addcurrentStockList) {
					for(HwxxDto hwxxDto_h : hwxxlist_t) {
						if(currentStockDto.getcInvCode().equals(hwxxDto_h.getWlbm()) && currentStockDto.getcBatch().equals(hwxxDto_h.getZsh())) {
							hwxxDto_h.setKcglid(String.valueOf(currentStockDto.getAutoID()));
						}
					}
				}
			}
			boolean result_g = hwxxService.updateHwxxDtos(hwxxlist_t);

			if(!result_g)
				throw new BusinessException("msg","更新货物信息库存关联id失败！");
		}
		return true;
	}
	/**
	 * 生成调拨单
	 * @param
	 * @return
	 */
	@Override
	public String generateDbdh() {
		// 生成规则: DB-20201022-001 DB-年份日期-流水号 。
		String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		String prefix = "DB" + "-" + date + "-";
		// 查询流水号
		String serial = dao.getDbdhSerial(prefix);
		return prefix + serial;
	}

	@Override
	public DbglDto getDtoByDbid(DbglDto dbglDto) {
		return dao.getDtoByDbid(dbglDto);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean DeleteAllocations(DbglDto dbglDto) throws BusinessException {
		DbmxDto dbmxDto=new DbmxDto();
		dbmxDto.setIds(dbglDto.getIds());
		dbmxDto.setScry(dbglDto.getScry());
		List<DbmxDto> dbmxDtos=dbmxService.getDtoList(dbmxDto);
		List<DbglDto> dbglDtoList = dao.getDtoList(dbglDto);
		for (DbglDto dbglDtoT:dbglDtoList){
			StringBuilder ids_wlid = new StringBuilder();
			List<DbmxDto> dbmxs = dbmxService.queryGroupBy(dbglDtoT.getDbid());
			for (DbmxDto dbmxDto_s : dbmxs){
				ids_wlid.append(",").append(dbmxDto_s.getWlid());
			}
			String ids_ckid = dbglDtoT.getZrck()+","+dbglDtoT.getZcck();
			CkhwxxDto ckhwxxDto = new CkhwxxDto();
			ids_wlid = new StringBuilder(ids_wlid.substring(1));
			ckhwxxDto.setIds(ids_wlid.toString());
			ckhwxxDto.setCkids(ids_ckid);
			List<CkhwxxDto> ckhwxxList = ckhwxxService.getDtoListByWlids(ckhwxxDto);
			List<CkhwxxDto> ckhwxxMod = new ArrayList<>();
			for (DbmxDto dbmxDto_s : dbmxs) {
				for (CkhwxxDto ckhwxxDto_t : ckhwxxList) {
					//处理转出的库存
					if(dbmxDto_s.getWlid().equals(ckhwxxDto_t.getWlid()) && ckhwxxDto_t.getCkid().equals(dbglDtoT.getZcck())) {
						ckhwxxDto_t.setKcl(dbmxDto_s.getDbzs());
						ckhwxxDto_t.setYdsbj("1"); // 1:加 0:减
						ckhwxxMod.add(ckhwxxDto_t);
					}else if(dbmxDto_s.getWlid().equals(ckhwxxDto_t.getWlid()) && ckhwxxDto_t.getCkid().equals(dbglDtoT.getZrck())) {
						ckhwxxDto_t.setKcl(dbmxDto_s.getDbzs());
						ckhwxxDto_t.setYdsbj("0"); // 1:加 0:减
						ckhwxxMod.add(ckhwxxDto_t);
					}
				}
			}
			if(!CollectionUtils.isEmpty(ckhwxxMod)) {
				boolean result_kcmod=ckhwxxService.updateList(ckhwxxMod);
				if(!result_kcmod) {
					throw new BusinessException("msg","修改仓库货物信息失败");
				}
			}
		}
		boolean isSuccess= delete(dbglDto);
		if (isSuccess){
			if (dbmxService.delete(dbmxDto)){
				List<HwxxDto> hwxxDtos=new ArrayList<>();
				List<String> delHwids=new ArrayList<>();
				for (DbmxDto dbmxDto1:dbmxDtos){
					HwxxDto hwxxDto=new HwxxDto();
					hwxxDto.setHwid(dbmxDto1.getDchwid());
					BigDecimal kcl=new BigDecimal(0);
					BigDecimal dbsl=new BigDecimal(dbmxDto1.getDbsl());
					BigDecimal newkcl=kcl.subtract(dbsl);
					hwxxDto.setModkcl(String.valueOf(newkcl));
					hwxxDto.setXgry(dbglDto.getScry());
					hwxxDtos.add(hwxxDto);
					delHwids.add(dbmxDto1.getDrhwid());
				}
				if (!CollectionUtils.isEmpty(hwxxDtos)){
					isSuccess=hwxxService.updateKclList(hwxxDtos);
					if (isSuccess){
						HwxxDto hwxxDto=new HwxxDto();
						hwxxDto.setIds(delHwids);
						hwxxDto.setScry(dbglDto.getScry());
						if (!hwxxService.deleteHwxxDtos(hwxxDto)){
							throw new BusinessException("msg","删除货物信息失败！");
						}

					}else {
						throw new BusinessException("msg","更新货物库存量失败！");
					}
				}
			}else {
				throw new BusinessException("msg","删除调拨明细失败！");
			}
		}else {
			throw new BusinessException("msg","删除调拨信息失败！");
		}
		return true;
	}

}
