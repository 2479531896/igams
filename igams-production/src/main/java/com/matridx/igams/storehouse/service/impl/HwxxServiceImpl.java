package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.GoodsBackTypeEnums;
import com.matridx.igams.common.enums.GoodsStatusEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.production.dao.entities.CurrentStockDto;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.matridxsql.CurrentStockDao;
import com.matridx.igams.production.service.svcinterface.IHtmxService;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.storehouse.dao.entities.DhthclDto;
import com.matridx.igams.storehouse.dao.entities.DhxxDto;
import com.matridx.igams.storehouse.dao.entities.HwllmxDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.HwxxModel;
import com.matridx.igams.storehouse.dao.post.IHwxxDao;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IDhthclService;
import com.matridx.igams.storehouse.service.svcinterface.IHwllmxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class HwxxServiceImpl extends BaseBasicServiceImpl<HwxxDto, HwxxModel, IHwxxDao> implements IHwxxService{
	@Autowired
	IShgcService shgcService;
	
	@Autowired
	IDhthclService dhthclService;
	
	@Autowired
	ICommonService commonService;

	@Autowired
	ICkhwxxService ckhwxxService;
	
	@Autowired
	IQgmxService qgmxService;
	
	@Autowired
	IHtmxService htmxService;
	@Autowired
	private IFjcfbService fjcfbService;
	@Autowired
	CurrentStockDao currentStockDao;
	@Autowired
	IHwllmxService hwllmxService;
	@Autowired
	IJcsjService jcsjService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	@Autowired
	RedisUtil redisUtil;
	private final Logger log = LoggerFactory.getLogger(HwxxServiceImpl.class);

	/**
	 * 待检验列表
	 * @param
	 * @return
	 */
	@Override
	public List<HwxxDto> getPagedDtoDjyList(HwxxDto hwxxDto) {
		List<HwxxDto> list_s = dao.getPagedDtoDjyList(hwxxDto);
		//根据类别判断是查询试剂的还是仪器的,"1"查询仪器，"2"查询试剂
		if ("1".equals(hwxxDto.getLbcskz1())) {
			try {
				shgcService.addShgcxxByYwid(list_s, AuditTypeEnum.AUDIT_GOODS_INSTRUMENT.getCode(), "jyzt", "hwid",
						new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}
		if ("2".equals(hwxxDto.getLbcskz1())) {
			try {
				shgcService.addShgcxxByYwid(list_s, AuditTypeEnum.AUDIT_GOODS_REAGENT.getCode(), "jyzt", "hwid",
						new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}
		if ("4".equals(hwxxDto.getLbcskz1())) {
			try {
				shgcService.addShgcxxByYwid(list_s, AuditTypeEnum.AUDIT_GOODS_THORGH.getCode(), "jyzt", "hwid",
						new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}
		if ("5".equals(hwxxDto.getLbcskz1())) {
			try {
				shgcService.addShgcxxByYwid(list_s, AuditTypeEnum.AUDIT_GOODS_CONSUMABLE.getCode(), "jyzt", "hwid",
						new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}
		return list_s;
	}

	@Override
	public List<HwxxDto> getGZDtoList(HwxxDto hwxxDto) {
		return dao.getGZDtoList(hwxxDto);
	}

	@Override
	public List<HwxxDto> getHwxxByRkid(HwxxDto hwxxDto) {
		return dao.getHwxxByRkid(hwxxDto);
	}

	@Override
	public boolean copyInsertHwxxList(List<HwxxDto> list) {
		return dao.copyInsertHwxxList(list);
	}

	@Override
	public List<HwxxDto> getCkInfo(HwxxDto hwxxDto) {
		return dao.getCkInfo(hwxxDto);
	}

	@Override
	public List<HwxxDto> getDtoListByLlid(HwxxDto hwxxDto) {
		return dao.getDtoListByLlid(hwxxDto);
	}
	/**
	 * 获取待处理列表
	 * @param hwxxDto
	 * @return
	 */
	public List<HwxxDto> getPagedDtoDclList(HwxxDto hwxxDto){
		return dao.getPagedDtoDclList(hwxxDto);
	}

	/**
	 * 待处理列表查看
	 * @param hwxxDto
	 * @return
	 */
	public HwxxDto viewPendingDetail(HwxxDto hwxxDto){
		return dao.viewPendingDetail(hwxxDto);
	}
	/**
	 * 根据hwid查看仓库货物信息
	 * @param hwxxDto
	 * @return
	 */
	public HwxxDto getCkhwxxByHwid(HwxxDto hwxxDto){
		return dao.getCkhwxxByHwid(hwxxDto);
	}
	/**
	 * 根据合同明细ids获取入库明细数据
	 * @param htmxDot
	 * @return
	 */
	public List<HwxxDto> getListByHtmxids(HtmxDto htmxDot){
		return dao.getListByHtmxids(htmxDot);
	}
	/**
	 * 待入库列表
	 */
	@Override
	public List<HwxxDto> getPagedStockPending(HwxxDto hwxxDto) {
		// TODO Auto-generated method stub
		return dao.getPagedStockPending(hwxxDto);
	}
	
	/**
	 * 待入库列表查通过hwid看一条信息
	 * @param hwxxDto
	 * @return
	 */
	@Override
	public HwxxDto getOneByHwid(HwxxDto hwxxDto) {
		// TODO Auto-generated method stub
		return dao.getOneByHwid(hwxxDto);
	}

	@Override
	public List<HwxxDto> getInfoByDhjyid(HwxxDto hwxxDto) {
		return dao.getInfoByDhjyid(hwxxDto);
	}

	@Override
	public List<HwxxDto> getDtoByWlbmAndCkdmAndKwbhdmAndscph(HwxxDto hwxxDto) {
		return dao.getDtoByWlbmAndCkdmAndKwbhdmAndscph(hwxxDto);
	}

	/**
	 * 新增保存到货明细信息
	 * @param list
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertHwxxList(List<HwxxDto> list){
		if(!CollectionUtils.isEmpty(list)) {
			//生成验收单号
			String date = list.get(0).getDhrq();
			String[] split = date.split("-");
			date=split[0]+"-"+split[1]+split[2];
			String prefix;
			prefix = "L-" + date + "-";
			String serial = dao.greatYsdh(prefix);
			int intSerial=Integer.parseInt(serial);
			for (HwxxDto hwxxDto : list) {
				if (intSerial < 10) {
					hwxxDto.setYsdh(prefix + "0" + intSerial);
				} else {
					hwxxDto.setYsdh(prefix + intSerial);
				}
				intSerial = intSerial + 1;
				if (StringUtils.isBlank(hwxxDto.getHwid()))
					hwxxDto.setHwid(StringUtil.generateUUID());
				hwxxDto.setZt(GoodsStatusEnum.GODDS_CHECK.getCode());
			}
		}
		return dao.insertHwxxList(list);
	}
	/**
	 * 修改保存
	 * @param dhxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateHwxxList(List<HwxxDto> hwxxDtos,DhxxDto dhxxDto) {
		if(!CollectionUtils.isEmpty(hwxxDtos) && StringUtils.isNotBlank(dhxxDto.getDhid())) {
			//获取新增的货物信息
			List<HwxxDto> addHwxxDtos=new ArrayList<>();
			for(int i=hwxxDtos.size()-1;i>=0;i--) {
				hwxxDtos.get(i).setLrry(dhxxDto.getLrry());
				hwxxDtos.get(i).setXgry(dhxxDto.getXgry());
				hwxxDtos.get(i).setDhid(dhxxDto.getDhid());
				hwxxDtos.get(i).setScry(dhxxDto.getScry());
				hwxxDtos.get(i).setCkid(dhxxDto.getCkid());
				if(StringUtils.isBlank(hwxxDtos.get(i).getHwid())) {
					hwxxDtos.get(i).setLrry(dhxxDto.getXgry());
					addHwxxDtos.add(hwxxDtos.get(i));
					hwxxDtos.remove(i);
				}
			}
			List<HwxxDto> selectHwxxDtos=dao.getListByDhid(dhxxDto.getDhid());
			//获取删除的到货明细
			List<String> delHwxxids = new ArrayList<>();
			// List<HwxxDto> delHwxxDtos = new ArrayList<>();
			for (int i = selectHwxxDtos.size()-1; i >= 0; i--) {
				boolean isDel = true;
				for (HwxxDto hwxxDto : hwxxDtos) {
					if (selectHwxxDtos.get(i).getHwid().equals(hwxxDto.getHwid())) {
						isDel = false;
						break;
					}
				}
				if(isDel) {
					delHwxxids.add(selectHwxxDtos.get(i).getHwid());
					selectHwxxDtos.get(i).setXgry(dhxxDto.getXgry());
					// delHwxxDtos.add(selectHwxxDtos.get(i));
				}
			}
			boolean result;
			if(!CollectionUtils.isEmpty(addHwxxDtos)) {
				result = insertHwxxList(addHwxxDtos);
				if(!result)
					return false;
			}
			HwxxDto hwxxDto = new HwxxDto();
			hwxxDto.setIds(delHwxxids);
			hwxxDto.setScbj("1");
			hwxxDto.setScry(dhxxDto.getXgry());
			result = deleteHwxxDtos(hwxxDto);
			if(!result)
				return false;
			JcsjDto jcsjDto = jcsjService.getDtoById(dhxxDto.getRklb());
			if("0".equals(jcsjDto.getCskz1())) {
				if(!CollectionUtils.isEmpty(hwxxDtos)) {
					//获取修改的合同明细(剩余的htmxDtos)
					result = updateHwxxDtos(hwxxDtos);
					return result;
				}
			}		
		}
		return true;
	}

	
	/**
	 * 批量删除到货明细信息
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteHwxxDtos(HwxxDto hwxxDto) {
		// TODO Auto-generated method stub
		if(!CollectionUtils.isEmpty(hwxxDto.getIds())) {
			return dao.deleteHwxxDtos(hwxxDto);
		}
		return true;
	}
	
	/**
	 * 批量修改合同明细信息
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateHwxxDtos(List<HwxxDto> hwxxDtos) {
		// TODO Auto-generated method stub
		if(!CollectionUtils.isEmpty(hwxxDtos)) {
			int result = dao.updateHwxxDtos(hwxxDtos);
			return result != 0;
		}
		return true;
	}

	@Override
	public boolean updateHwInfos(List<HwxxDto> hwxxDtos) {
		return dao.updateHwInfos(hwxxDtos)!= 0;
	}

	/**
	 * 导出
	 * 
	 * @return
	 */
	@Override
	public int getCountForSearchExp(HwxxDto hwxxDto, Map<String, Object> params) {
		return dao.getCountForSearchExp(hwxxDto);
	}
	
	/**
	 * 根据搜索条件获取导出信息
	 * 
	 * @return
	 */
	public List<HwxxDto> getListForSearchExp(Map<String, Object> params) {
		HwxxDto hwxxDto = (HwxxDto) params.get("entryData");
		queryJoinFlagExport(params, hwxxDto);
		return dao.getListForSearchExp(hwxxDto);
	}

	/**
	 * 根据选择信息获取导出信息
	 * 
	 * @return
	 */
	public List<HwxxDto> getListForSelectExp(Map<String, Object> params) {
		HwxxDto hwxxDto = (HwxxDto) params.get("entryData");
		queryJoinFlagExport(params, hwxxDto);
		return dao.getListForSelectExp(hwxxDto);
	}

	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String, Object> params, HwxxDto hwxxDto) {
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
		for (DcszDto dcszDto : choseList) {
			if (dcszDto == null || dcszDto.getDczd() == null)
				continue;

			sqlParam.append(",");
			if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		String sqlcs = sqlParam.toString();
		hwxxDto.setSqlParam(sqlcs);
	}

	/**
	 * 更新货物信息里入库信息s
	 * 
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateRkglDtos(List<HwxxDto> hwxxDtos) {
		int result = dao.updateRkglDtos(hwxxDtos);
		return result > 0;
	}
	/**
	 * 获取货物选择列表
 	 * @return
	 */
	@Override
	public List<HwxxDto> chooseListHwxx(HwxxDto hwxxDto) {
		//获取入库货物信息
		List<HwxxDto> hwxxDtos = dao.getDtoList(hwxxDto);
		if(!CollectionUtils.isEmpty(hwxxDto.getHwids())) {
			//查询不重复的货物信息
			HwxxDto hwxxDto_t = new HwxxDto();
			hwxxDto_t.setIds(hwxxDto.getHwids());
			List<HwxxDto> hwxxDtos_t = dao.getDtoList(hwxxDto_t);
			//合并两个集合
			hwxxDtos.addAll(hwxxDtos_t);
			//赋值初始化过程省略
			return hwxxDtos.stream().collect(
			            Collectors.collectingAndThen(
			                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(HwxxDto::getHwid))), ArrayList::new)
			);
		}else {
			return hwxxDtos;
		}		
	}
	
	/**
	 * 根据合同明细ID获取到货信息
	 * @return
	 */
	public List<HwxxDto> getDtoByHtmxid(HwxxDto hwxxDto){
		return dao.getDtoByHtmxid(hwxxDto);
	}
	/**
	 * 根据ids查询货物信息
	 * @param hwxxDto
	 * @return
	 */
	public List<HwxxDto> getHwxxByids(HwxxDto hwxxDto){
		return dao.getHwxxByids(hwxxDto);
	}

	@Override
	public List<HwxxDto> getHwInfoByCkids(HwxxDto hwxxDto) {
		return dao.getHwInfoByCkids(hwxxDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateDhjyidEmpty(HwxxDto hwxxDto) {
		// TODO Auto-generated method stub
        dao.updateDhjyidEmpty(hwxxDto);
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int updateDhjyidEmptyByDhjyids(HwxxDto dto) {
		return dao.updateDhjyidEmptyByDhjyids(dto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateHwxxByDhjyids(HwxxDto dto) {
        dao.updateHwxxByDhjyids(dto);
    }

	@Override
	public List<HwxxDto> getListByDhjyid(HwxxDto dto) {
		List<HwxxDto> t_List = dao.getListByDhjyid(dto);
		if(!CollectionUtils.isEmpty(t_List)){
			// 查询附件信息
			for (HwxxDto hwxxDto : t_List) {
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(hwxxDto.getDhjyid());
				fjcfbDto.setZywid(hwxxDto.getHwid());
				List<FjcfbDto> fjcfbDtos = fjcfbService.getListByZywid(fjcfbDto);
				if(!CollectionUtils.isEmpty(fjcfbDtos)) {
					hwxxDto.setFjbj("0");
				}
				if (StringUtil.isBlank(hwxxDto.getFjbj())){
					List<FjcfbDto> fjcfbDtos_t = fjcfbService.selectFjcfbDtoByYwid(hwxxDto.getHwid());
					if (!CollectionUtils.isEmpty(fjcfbDtos_t)){
						hwxxDto.setFjbj("0");
					}
				}
			}
		}
		return t_List;
	}
	/**
     * 更新请购明细到货数量和到货日期
     *
     * @param hwxxDtos
     */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateQgmxs(List<HwxxDto> hwxxDtos) {
		List<HwxxDto> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(hwxxDtos)) {
			for(HwxxDto hwxxDto : hwxxDtos) {
				QgmxDto qgmxDto=qgmxService.getDtoById(hwxxDto.getQgmxid());
				if(qgmxDto!=null) {
					double dhsl=StringUtils.isNotBlank(qgmxDto.getDhsl()) ? Double.parseDouble(qgmxDto.getDhsl()) : 0.00;
					double zdhsl=dhsl+Double.parseDouble(hwxxDto.getSl_t());
					hwxxDto.setZdhsl(Double.toString(zdhsl));
					String dhdhgl=StringUtils.isNotBlank(qgmxDto.getDhdhgl()) ? (qgmxDto.getDhdhgl()+","+hwxxDto.getDhdh()) : hwxxDto.getDhdh();
					hwxxDto.setDhdhglqg(dhdhgl);
				}
				list.add(hwxxDto);
			}
		}
        dao.updateQgmxs(list);
    }
	
	/**
     * 更新合同明细到货数量和到货日期
     *
     * @param hwxxDtos
     */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateHtmxs(List<HwxxDto> hwxxDtos) {
		List<HwxxDto> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(hwxxDtos)) {
			for(HwxxDto hwxxDto : hwxxDtos) {
				HtmxDto htmxDto=htmxService.getDtoById(hwxxDto.getHtmxid());
				if(htmxDto!=null) {
					double dhsl=StringUtils.isNotBlank(htmxDto.getDhsl()) ? Double.parseDouble(htmxDto.getDhsl()) : 0.00;
					double zdhsl=dhsl+Double.parseDouble(hwxxDto.getSl_t());
					hwxxDto.setZdhsl(Double.toString(zdhsl));
					String dhdhgl=StringUtils.isNotBlank(htmxDto.getDhdhgl()) ? (htmxDto.getDhdhgl()+","+hwxxDto.getDhdh()) : hwxxDto.getDhdh();
					hwxxDto.setDhdhglht(dhdhgl);
				}
				list.add(hwxxDto);
			}
		}
        dao.updateHtmxs(list);
    }
	
	/**
	 * 是否发起退回申请
	 * @param hwxxDtos
	 * @param type
	 * @param yhid
	 * @throws BusinessException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void createArrivalGoodsBack(List<HwxxDto> hwxxDtos,String type,String yhid) throws BusinessException {
		if(!CollectionUtils.isEmpty(hwxxDtos)) {
			for(HwxxDto hwxxDto : hwxxDtos) {
				if((GoodsBackTypeEnums.ARRIVALGOODS_BACK.getCode().equals(type)&&StringUtils.isNotBlank(hwxxDto.getCythsl()) && Double.parseDouble(hwxxDto.getCythsl()) > 0)
					||(GoodsBackTypeEnums.QUALITYCHECK_BACK.getCode().equals(type)&&StringUtils.isNotBlank(hwxxDto.getZjthsl()) && Double.parseDouble(hwxxDto.getZjthsl()) > 0)) {
					DhthclDto dhthclDto = new DhthclDto();
					dhthclDto.setHwid(hwxxDto.getHwid());
					dhthclDto.setLx(type);
					dhthclDto = dhthclService.getDtoByHwidAndLx(dhthclDto);
					if(dhthclDto != null) {
						//如果已有数据则更新该条数据的数量字段。
						dhthclDto.setXgry(yhid);
						//到货退回
						if(GoodsBackTypeEnums.ARRIVALGOODS_BACK.getCode().equals(type)) {
							dhthclDto.setSl(hwxxDto.getCythsl());
						}
						//质检退回
						else if(GoodsBackTypeEnums.QUALITYCHECK_BACK.getCode().equals(type)) {
							dhthclDto.setSl(hwxxDto.getZjthsl());
						}
						dhthclService.update(dhthclDto);
					}else {
						//如果没有数据，则新增一条数据。状态为10，并提交退回处理审核(AUDIT_GOODS_BACK)
						DhthclDto t_dhthclDto=new DhthclDto();
						t_dhthclDto.setHwid(hwxxDto.getHwid());
						t_dhthclDto.setLx(type);
						//到货退回
						if(GoodsBackTypeEnums.ARRIVALGOODS_BACK.getCode().equals(type)) {
							t_dhthclDto.setSl(hwxxDto.getCythsl());
						}
						//质检退回
						else if(GoodsBackTypeEnums.QUALITYCHECK_BACK.getCode().equals(type)) {
							t_dhthclDto.setSl(hwxxDto.getZjthsl());
						}
						t_dhthclDto.setLrry(yhid);
						t_dhthclDto.setHtmxid(hwxxDto.getHtmxid());
						t_dhthclDto.setQgmxid(hwxxDto.getQgmxid());
						t_dhthclDto.setWlid(hwxxDto.getWlid());
						t_dhthclDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
						dhthclService.insertDto(t_dhthclDto);
						//提交审核
						//提交复检申请
						ShgcDto shgcDto=new ShgcDto();
						shgcDto.setExtend_1("[\""+t_dhthclDto.getDhthid()+"\"]");
						shgcDto.setShlb(AuditTypeEnum.AUDIT_GOODS_BACK.getCode());
						User user=new User();
						user.setYhid(yhid);
						user=commonService.getUserInfoById(user);
						try {
							shgcService.checkAndCommit(shgcDto,user);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							throw new BusinessException("ICOM99019",e.getMsg());
						}
					}
				}
			}
		}
	}
	
	/**
	 * 根据Dhids查询到货信息
	 * @param hwxxDto
	 * @return
	 */
	public List<HwxxDto> getListByDhids(HwxxDto hwxxDto){
		return dao.getListByDhids(hwxxDto);
	}

	@Override
	public List<HwxxDto> getDtoListByrkids(HwxxDto hwxxDto) {
		return dao.getDtoListByrkids(hwxxDto);
	}

	/**
	 * 判断追溯号是否重复
	 * @return
	 */
	@Override
	public String judgeZsh(List<HwxxDto> hwxxDtos) {
		String message="success";
		boolean result = true;
		for (int i = hwxxDtos.size()-1; i >= 0; i--) {			
			for (int j = 0; j < hwxxDtos.size(); j++) {
				if(i != j && hwxxDtos.get(i).getWlbm().equals(hwxxDtos.get(j).getWlbm()) && hwxxDtos.get(i).getZsh().equals(hwxxDtos.get(j).getZsh())) {
					message = "物料：" + hwxxDtos.get(i).getWlbm() + "追溯号重复，请重新输入！";
					result = false;
					break;
				}
			}
			if(!result) {
				break;
			}
		}		
		List<HwxxDto> hwxxDtos_t = dao.queryByZshAndWlbm(hwxxDtos);
		if(!CollectionUtils.isEmpty(hwxxDtos_t)) {
			String wlbm ="";
			for (HwxxDto hwxxDto : hwxxDtos_t) {
				wlbm = hwxxDto.getWlbm();
			}
			message = "物料：" + wlbm + "追溯号重复，请重新输入！";
		}
		return message;
	}
	
	 /**
	  * 根据zt和dhid获取货物信息
	  * @param hwxxDto
	  * @return
	  */
	 public List<HwxxDto> getHwxxByZtAndDhids(HwxxDto hwxxDto){
		 return dao.getHwxxByZtAndDhids(hwxxDto);
	 }
	 
	/**
	 * 根据到货ID获取货物信息
	 * @param dhid
	 * @return
	 */
	public List<HwxxDto> getListByDhid(String dhid){
		return dao.getListByDhid(dhid);
	}
	
	/**
	 * 根据入库ID查询货物信息
	 * @param rkid
	 * @return
	 */
	public List<HwxxDto> getDtoListById(String rkid){
		return dao.getDtoListById(rkid);
	}

 	/**
	  *	根据检验id获取领料信息
	  * @param hwxxDto
	  * @return
	  */
	@Override
	public List<HwxxDto> getDtoByDhjyid(HwxxDto hwxxDto) {
		// TODO Auto-generated method stub
		return dao.getDtoByDhjyid(hwxxDto);
	}

	@Override
	public List<HwxxDto> getDtoListByJyId(String jyid) {
		// TODO Auto-generated method stub
		return dao.getDtoListByJyId(jyid);
	}
	
	 /**
	  * 批量更新货物信息调拨明细关联ID
	  * @param list
	  * @return
	  */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	 public boolean updateDbmxId(List<HwxxDto> list) {
		 return dao.updateDbmxId(list);
	 }

	 /**
	  *	根据追溯号物料id分组查询
	  * @return
	  */
	@Override
	public List<HwxxDto> queryByDhid(String dhid) {
		// TODO Auto-generated method stub
		return dao.queryByDhid(dhid);
	}

	 /**
	  * 通过追溯号物料id批量更新库存关联id
	  * @param list
	  * @return
	  */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateByWlidAndZsh(List<HwxxDto> list) {
		// TODO Auto-generated method stub
		return dao.updateByWlidAndZsh(list)>0;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateRksl(List<HwxxDto> list) {
		int result = dao.updateRksl(list);
		return result>0;
	}
	
	 /**
	  * 根据入库ID获取货物信息(根据物料和追溯号分组)
	  * @param hwxxDto
	  * @return
	  */
	 public List<HwxxDto> queryByRkid(HwxxDto hwxxDto){
		 return dao.queryByRkid(hwxxDto);
	 }
	 
	 /**
	  * 根据检验ID获取货物信息(根据物料和追溯号分组)
	  * @param hwxxDto
	  * @return
	  */
	 public List<HwxxDto> queryByJyid(HwxxDto hwxxDto){
		 return dao.queryByJyid(hwxxDto);
	 }

	 /**
	  * 根据物料id获取物料信息
	  * @param hwxxDto
	  * @return
	  */
	@Override
	public List<HwxxDto> queryWlglDtosByWlid(HwxxDto hwxxDto) {
		List<HwxxDto> hwxxDtos = dao.queryWlglDtosByWlid(hwxxDto);
		//遍历生成hwid
		for (HwxxDto hwxxDto_t : hwxxDtos) {
			hwxxDto_t.setHwid(StringUtil.generateUUID());
		}
		return hwxxDtos;
	}
    
	/**
	 * 到货列表货物借用信息
	 */
	@Override
	public List<HwxxDto> getBorrowList(HwxxDto hwxxDto) {
		return dao.getBorrowList(hwxxDto);
	}

	/**
	 * 到货列表货物归还信息
	 */
	@Override
	public List<HwxxDto> getReturnBackList(HwxxDto hwxxDto) {
		return dao.getReturnBackList(hwxxDto);
	}

	/**
	 * 更新归还的货物信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateHwxxReturn(List<HwxxDto> hwxxDtos) {
		return dao.updateHwxxReturn(hwxxDtos);
	}

	/**
	 * 更新借用的货物信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateHwxxBorrow(List<HwxxDto> hwxxDtos) {
		return dao.updateHwxxBorrow(hwxxDtos);
	}

	/**
	 * 获取借用归还总量
	 */
	@Override
	public HwxxDto getJyGhzlByDhid(HwxxDto hw_jyzl) {
		return dao.getJyGhzlByDhid(hw_jyzl);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void updateByHwId(HwxxDto hwxxDto) {
        dao.updateByHwId(hwxxDto);
    }

	/**
	 * 根据物料id获取货物信息
	 * @param hwxxDto
	 * @return
	 */
	@Override
	public List<HwxxDto> queryHWxx(HwxxDto hwxxDto,String dqjs) {
		if (!"1".equals(dqjs)){
			hwxxDto.setJsid(dqjs);
		}
		List<HwxxDto> hwxxs;
		if (StringUtil.isNotBlank(hwxxDto.getFhid())){
			String fhid  = hwxxDto.getFhid();
			hwxxDto.setFhid("");
			hwxxs = dao.queryHWxxIsFh(hwxxDto);
			List<HwxxDto> hwxxs_cl = new ArrayList<>();
			if (!CollectionUtils.isEmpty(hwxxs)){
				for (int i = 0; i <hwxxs.size() ; i++) {
					if (StringUtil.isNotBlank(hwxxs.get(i).getFhid()) && !fhid.equals(hwxxs.get(i).getFhid())){
						hwxxs_cl.add(hwxxs.get(i));
						hwxxs.remove(i);
						i--;
					}
				}
				if (!CollectionUtils.isEmpty(hwxxs_cl)){
					for (HwxxDto hwxx : hwxxs) {
						for (int j = 0; j < hwxxs_cl.size(); j++) {
							if (hwxx.getHwid().equals(hwxxs_cl.get(j).getHwid())) {
								hwxxs_cl.remove(j);
								j--;
							}
						}
					}
					for (HwxxDto dto : hwxxs_cl) {
						dto.setKlsl(String.valueOf(Double.parseDouble(dto.getKlsl()) - Double.parseDouble(dto.getFhsl())));
					}
					List<HwxxDto> hwxxDtoList = new ArrayList<>();
					if(!CollectionUtils.isEmpty(hwxxs_cl)) {
						do{
							hwxxDtoList.add(hwxxs_cl.get(0));
							hwxxs_cl = delByTarget(hwxxs_cl,hwxxs_cl.get(0));
						}while (!CollectionUtils.isEmpty(hwxxs_cl));
					}
					hwxxs.addAll(hwxxDtoList);
				}
			}
		}else{
			hwxxs = dao.queryHWxx(hwxxDto);
		}
		HwllmxDto hwllmxDto = new HwllmxDto();
		if (StringUtil.isNotBlank(hwxxDto.getLlid()) && StringUtils.isBlank(hwxxDto.getCopyflg())){
			hwllmxDto.setLlid(hwxxDto.getLlid());
			List<HwllmxDto> hwllmxs = hwllmxService.getDtoList(hwllmxDto);
			for (HwxxDto hwxxDto_t : hwxxs) {
				for (HwllmxDto hwllmxDto_t : hwllmxs) {
					if(hwxxDto_t.getHwid().equals(hwllmxDto_t.getHwid())) {
						hwxxDto_t.setQlsl(hwllmxDto_t.getQlsl());
//						Double Klsl = Double.parseDouble(StringUtil.isNotBlank(hwxxDto_t.getKlsl())?hwxxDto_t.getKlsl():"0")+Double.parseDouble(StringUtil.isNotBlank(hwllmxDto_t.getQlsl())?hwllmxDto_t.getQlsl():"0");
//						hwxxDto_t.setKlsl(String.valueOf(Klsl));
						hwxxDto_t.setLlmxid(hwllmxDto_t.getLlmxid());
						break;
					}
				}
			}
		}
		return hwxxs;
	}

	/**
	 * 根据元素删除
	 * @param hwxxDtos
	 * @param hwxxDto
	 * @return
	 */
	public List<HwxxDto> delByTarget(List<HwxxDto> hwxxDtos,HwxxDto hwxxDto){
		List<HwxxDto> hwxxDtoList = new ArrayList<>();
		if (CollectionUtils.isEmpty(hwxxDtos))
			return hwxxDtoList;
		for (HwxxDto dto : hwxxDtos) {
			if (!dto.getHwid().equals(hwxxDto.getHwid()))
				hwxxDtoList.add(dto);
		}
		return hwxxDtoList;
	}

	/**
	 * 更新其他出入库明细ID
	 * @param list
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateqtcrk(List<HwxxDto> list){
		return dao.updateqtcrk(list);
	}
	/**
	 * 获取追溯号流水
	 * @return
	 */
	@Override
	public String getcCodeSerialZsh(String prefix) {
		// 查询流水号
		String serial = dao.getcCodeSerialZsh(prefix);
		if(StringUtils.isBlank(serial))
			return "001";
		int index=Integer.parseInt(serial.substring(8));
		return "00"+ (index + 1);
	}
	
	@Override
	public List<HwxxDto> getHwxxListByDhid(HwxxDto hwxxDto) {
		return dao.getHwxxListByDhid(hwxxDto);
	}

	@Override
	public String getHwxxjgdh(String sqbm) {
		// TODO Auto-generated method stub
		return dao.getHwxxjgdh(sqbm);
	}

	@Override
	public List<HwxxDto> selectHWxx(HwxxDto hwxxDto) {
		return dao.selectHWxx(hwxxDto);
	}
	/**
	 * 全部库存列表
	 *
	 * @param hwxxDto
	 * @return
	 */
	public List<HwxxDto> getPagedDtoAllStockList(HwxxDto hwxxDto){
		return  dao.getPagedDtoAllStockList(hwxxDto);
	}
	/**
	 * 根据hwid查看货物全部信息
	 * @param hwxxDto
	 * @return
	 */
	public HwxxDto viewStockDto(HwxxDto hwxxDto){
		return dao.viewStockDto(hwxxDto);
	}

	/**
	 * 从数据库分页获取导出数据
	 * @param
	 * @return
	 */
	public List<HwxxDto> getStockListForSelectExp(Map<String, Object> params){
		HwxxDto hwxxDto = (HwxxDto) params.get("entryData");
		queryJoinFlagExport(params,hwxxDto);
		return dao.getStockListForSelectExp(hwxxDto);
	}

	/**
	 * 根据搜索条件获取导出条数
	 * @param hwxxDto
	 * @return
	 */
	public int getStockCountForSearchExp(HwxxDto hwxxDto,Map<String, Object> params){
		return dao.getStockCountForSearchExp(hwxxDto);
	}
	/**
	 * 从数据库分页获取导出数据
	 * @param
	 * @return
	 */
	public List<HwxxDto> getStockListForSearchExp(Map<String, Object> params){
		HwxxDto hwxxDto = (HwxxDto) params.get("entryData");
		queryJoinFlagExport(params, hwxxDto);
		return dao.getStockListForSearchExp(hwxxDto);

	}

	
	/**
	 * 批量新增
	 * @param
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertHwxxs(List<HwxxDto> list) {
		if(!CollectionUtils.isEmpty(list)) {
			for (HwxxDto hwxxDto : list) {
				if (StringUtils.isBlank(hwxxDto.getHwid()))
					hwxxDto.setHwid(StringUtil.generateUUID());
			}
		}
		return dao.insertHwxxList(list);
	}

	/**
	 * 根据到货检验id查询
	 * @param
	 * @return
	 */
	@Override
	public List<HwxxDto> queryByDhjyid(HwxxDto hwxxDto) {
		// TODO Auto-generated method stub
		return dao.queryByDhjyid(hwxxDto);
	}

	@Override
	public List<HwxxDto> getListByChDhid(HwxxDto hwxxDto) {
		return dao.getListByChDhid(hwxxDto);
	}
	@Override
	public HwxxDto getScphAndBxsjByHwid(String hwid) {
		return dao.getScphAndBxsjByHwid(hwid);
	}

	/**
	 * 请购数量统计
	 */
	public List<HwxxDto> getPurchaseStatistics(String year){
		return dao.getPurchaseStatistics(year);
	}
		/**
	 * 根据入库id查询
	 * @param
	 * @return
	 */
	@Override
	public List<HwxxDto> getListRkId(HwxxDto hwxxDto) {
		return dao.getListRkId(hwxxDto);
	}
	@Override
	public HwxxDto queryByRkxx(HwxxDto hwxxDto) {
		return dao.queryByRkxx(hwxxDto);
	}

	@Override
	public List<HwxxDto> getDtolistByQgmxid(HwxxDto hwxxDto) {
		return dao.getDtolistByQgmxid(hwxxDto);
	}

	@Override
	public List<HwxxDto> getDtoListByDhid(HwxxDto hwxxDto) {
		return dao.getDtoListByDhid(hwxxDto);
	}
	/**
	 * 根据物料id查找信息
	 */
	public List<HwxxDto> getPagedListByWlid(HwxxDto hwxxDto){
		return dao.getPagedListByWlid(hwxxDto);
	}

	/**
	 * 更新库存量
	 * @param list
	 * @return
	 */
	public boolean updateKclList(List<HwxxDto> list){
		return dao.updateKclList(list);
	}
	public boolean updateDhsls(List<HwxxDto> hwxxDtoList) {
		return dao.updateDhsls(hwxxDtoList);
	}

	@Override
	public HwxxDto getDtoByHwid(String hwid) {
		return dao.getDtoByHwid(hwid);
	}

	@Override
	public List<HwxxDto> getDtoListByRkid(HwxxDto hwxxDto) {
		return dao.getDtoListByRkid(hwxxDto);
	}

	@Override
	public List<HwxxDto> getWlCountGroupByCk() {
		return dao.getWlCountGroupByCk();
	}

	@Override
	public List<HwxxDto> getWlSockByCkid(HwxxDto hwxxDto) {
		return dao.getWlSockByCkid(hwxxDto);
	}

	@Override
	public List<HwxxDto> getWlSockxxByCkidAndWlid(HwxxDto hwxxDto) {
		return dao.getWlSockxxByCkidAndWlid(hwxxDto);
	}

	@Override
	public List<HwxxDto> getDtoByDhjyidWithYck(HwxxDto hwxxDto) {
		return dao.getDtoByDhjyidWithYck(hwxxDto);
	}

	@Override
	public List<HwxxDto> getHwxxByHtmxidsWithHz(HwxxDto hwxxDto) {
		return dao.getHwxxByHtmxidsWithHz(hwxxDto);
	}

	@Override
	public List<HwxxDto> getHwxxWithHz(HwxxDto hwxxDto) {
		return dao.getHwxxWithHz(hwxxDto);
	}
	@Override
	public List<HwxxDto> getWlkcInfoGroupBy(HwxxDto hwxxDto) {
		return dao.getWlkcInfoGroupBy(hwxxDto);
	}

	@Override
	public List<HwxxDto> getLastestHq(HwxxDto hwxxDto) {
		return dao.getLastestHq(hwxxDto);
	}

	@Override
	public List<HwxxDto> selectFiveMonthsLlsl(Map<String, Object> map) {
		return dao.selectFiveMonthsLlsl(map);
	}

	@Override
	public List<HwxxDto> selectZtsl(HwxxDto hwxxDto) {
		return dao.selectZtsl(hwxxDto);
	}

	@Override
	public List<HwxxDto> getPagedGlsbList(HwxxDto hwxxDto) {
		return dao.getPagedGlsbList(hwxxDto);
	}

	@Override
	public List<HwxxDto> getHwxxByJcghids(HwxxDto hwxxDto) {
		return dao.getHwxxByJcghids(hwxxDto);
	}

	@Override
	public List<HwxxDto> getDtoListWithCertificate(HwxxDto hwxxDto) {
		return dao.getDtoListWithCertificate(hwxxDto);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean uploadSaveInspectionGoods(HwxxDto hwxxDto) {
		if(!CollectionUtils.isEmpty(hwxxDto.getFjids())) {
			for (int i = 0; i < hwxxDto.getFjids().size(); i++) {
				fjcfbService.save2RealFile(hwxxDto.getFjids().get(i), hwxxDto.getHwid());
			}
		}
		return true;
	}

	@Override
	public boolean updateForSbys(HwxxDto hwxxDto) {
		return dao.updateForSbys(hwxxDto);
	}

	@Override
	public List<HwxxDto> queryBycBatch(HwxxDto hwxxDto) {
		return dao.queryBycBatch(hwxxDto);
	}

	@Override
	public List<HwxxDto> orderByQueryByDhjyid(HwxxDto hwxxDto) {
		return dao.orderByQueryByDhjyid(hwxxDto);
	}

	@Override
	public List<HwxxDto> queryByDhidGroup(HwxxDto hwxxDto) {
		return dao.queryByDhidGroup(hwxxDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateByDhid(HwxxDto hwxxDto) {
		return dao.updateByDhid(hwxxDto)>0;
	}

	/**
	 * @Description: 根据角色id查询库存
	 * @param hwxxDto
	 * @return java.util.List<com.matridx.igams.storehouse.dao.entities.HwxxDto>
	 * @Author: 郭祥杰
	 * @Date: 2025/8/18 16:16
	 */
	@Override
	public List<HwxxDto> getPagedDtoByJsid(HwxxDto hwxxDto) {
		return dao.getPagedDtoByJsid(hwxxDto);
	}

	/**
	 * @Description: 更新库存
	 * @param hwxxDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2025/8/19 16:39
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateHwxxDtoByHwid(HwxxDto hwxxDto) {
		return dao.updateHwxxDtoByHwid(hwxxDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateKclDtoByLLid(HwxxDto hwxxDto) {
		return dao.updateKclDtoByLLid(hwxxDto);
	}


	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public void taskQueryStock(){
		//查询货物信息库存
		List<HwxxDto> hwxxDtoList = dao.queryStock();
		List<Map<String,String>> lsitMap = new ArrayList<>();
		List<CurrentStockDto> currentStockDaoList = currentStockDao.getDtoAllBySl();
		for(HwxxDto hwxxDto:hwxxDtoList){
			boolean flg = false;
			for(CurrentStockDto currentStockDto:currentStockDaoList){
				if(hwxxDto.getWlbm().equals(currentStockDto.getcInvCode())
						&& hwxxDto.getCkdm().equals(currentStockDto.getcWhCode())
						&& (StringUtil.isNotBlank(hwxxDto.getScph())?hwxxDto.getScph():"/")
						.equals(StringUtil.isNotBlank(currentStockDto.getcBatch())?currentStockDto.getcBatch():"/")){
					flg = true;
					BigDecimal kcl = new BigDecimal(hwxxDto.getKcl());
					BigDecimal iquantity = new BigDecimal(currentStockDto.getiQuantity());
					if(kcl.compareTo(iquantity)!=0){
						Map<String,String> map = new HashMap<>();
						map.put("wlbm",hwxxDto.getWlbm());
						map.put("ckdm",hwxxDto.getCkdm());
						map.put("scph",hwxxDto.getScph());
						map.put("kcl",hwxxDto.getKcl());
						map.put("iquantity",new BigDecimal(currentStockDto.getiQuantity()).setScale(2, RoundingMode.HALF_UP).toString());
						lsitMap.add(map);
					}
				}
			}
			if(!flg){
				Map<String,String> map = new HashMap<>();
				map.put("wlbm",hwxxDto.getWlbm());
				map.put("ckdm",hwxxDto.getCkdm());
				map.put("scph",hwxxDto.getScph());
				map.put("kcl",hwxxDto.getKcl());
				map.put("iquantity","0");
				lsitMap.add(map);
			}
		}
		for(CurrentStockDto currentStockDto:currentStockDaoList){
			boolean u8flg = false;
			for(HwxxDto hwxxDto:hwxxDtoList){
				if(hwxxDto.getWlbm().equals(currentStockDto.getcInvCode())
						&& hwxxDto.getCkdm().equals(currentStockDto.getcWhCode())
						&& (StringUtil.isNotBlank(hwxxDto.getScph())?hwxxDto.getScph():"/")
						.equals(StringUtil.isNotBlank(currentStockDto.getcBatch())?currentStockDto.getcBatch():"/")){
					u8flg = true;
				}
			}
			if(!u8flg){
				Map<String,String> map = new HashMap<>();
				map.put("wlbm",currentStockDto.getcInvCode());
				map.put("ckdm",currentStockDto.getcWhCode());
				map.put("scph",currentStockDto.getcBatch());
				map.put("kcl","0");
				map.put("iquantity",new BigDecimal(currentStockDto.getiQuantity()).setScale(2, RoundingMode.HALF_UP).toString());
				lsitMap.add(map);
			}
		}
		List<HwxxDto> hwxxDtoListS = dao.queryCkhwxxAndHwxx();
		List<Map<String,String>> hwxxMap = new ArrayList<>();
		for (HwxxDto hwxxDto:hwxxDtoListS){
			Map<String,String> map = new HashMap<>();
			map.put("wlbm",hwxxDto.getWlbm());
			map.put("ckdm",hwxxDto.getCkdm());
			map.put("scph",hwxxDto.getScph());
			map.put("kcl",hwxxDto.getKcl());
			map.put("hwkcl",hwxxDto.getHwkcl());
			hwxxMap.add(map);
		}
		if(!lsitMap.isEmpty() || (hwxxMap!=null && !hwxxMap.isEmpty())){
			if(!lsitMap.isEmpty()){
				redisUtil.set("QUERY_STOCK_MESSAGE", JSONObject.toJSONString(lsitMap));
			}
			if(hwxxMap!=null && !hwxxDtoListS.isEmpty()){
				redisUtil.set("QUERY_OA_STOCK_MESSAGE", JSONObject.toJSONString(hwxxMap));
			}
			String ICOMM_KCTX001 = xxglService.getMsg("ICOMM_KCTX001");
			List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.QUERY_STOCK_MESSAGE.getCode());
			for (DdxxglDto ddxxglDto : ddxxglDtolist) {
				// 内网访问
				String internalbtn =applicationurl + urlPrefix + "/ws/querystock/viewQueryStock";
				List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
				OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
				btnJsonList.setTitle("查看");
				btnJsonList.setActionUrl(internalbtn);
				btnJsonLists.add(btnJsonList);
				talkUtil.sendCardMessage(ddxxglDto.getYhm(),ddxxglDto.getDdid(),ICOMM_KCTX001,ICOMM_KCTX001,
						btnJsonLists,"1");
			}
		}
	}
}
