package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.storehouse.dao.entities.CkglDto;
import com.matridx.igams.storehouse.dao.entities.CkglModel;
import com.matridx.igams.storehouse.dao.entities.CkhwxxDto;
import com.matridx.igams.storehouse.dao.entities.CkmxDto;
import com.matridx.igams.storehouse.dao.entities.FhglDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.post.ICkglDao;
import com.matridx.igams.storehouse.service.svcinterface.ICkglService;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.ICkmxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CkglServiceImpl extends BaseBasicServiceImpl<CkglDto, CkglModel, ICkglDao> implements ICkglService,IAuditService {
	@Autowired
	private ICkmxService ckmxService;
	@Autowired
	private IHwxxService hwxxService;
	@Autowired
	private ICkhwxxService ckhwxxService;
	@Autowired
	IRdRecordService rdRecordService;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ICommonService commonService;
	@Autowired
	IShgcService shgcService;
	private final Logger log = LoggerFactory.getLogger(CkglServiceImpl.class);
	/**
	 * 获取列表信息
	 */
	@Override
	public List<CkglDto> getPagedDtoList(CkglDto ckglDto){
		List<CkglDto> list = dao.getPagedDtoList(ckglDto);
		try {
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_DELIVERY.getCode(), "zt", "ckid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return list;
	}
	/**
	 * 获取出库单数据
	 * @param ckglDto
	 * @return
	 */
	public List<CkglDto> getOutBoundOrder(CkglDto ckglDto){
		return dao.getOutBoundOrder(ckglDto);
	}

	@Override
	public List<CkglDto> getDtoListByCkdh(CkglDto ckglDto) {
		return dao.getDtoListByCkdh(ckglDto);
	}

	@Override
	public List<CkglDto> getPagedAuditCkglDto(CkglDto ckglDto) {
		// 获取人员ID和履历号
		List<CkglDto> list = dao.getPagedAuditCkglDto(ckglDto);
		if (CollectionUtils.isEmpty(list))
			return list;
		List<CkglDto> sqList = dao.getAuditListCkglDto(list);
		commonService.setSqrxm(sqList);
		return sqList;
	}

	/**
	 * 自动生成出库单号
	 * @param
	 * @return
	 */
	public String generateDjh(String prefix) {
		return dao.getDjhSerial(prefix);
	}

	/**
	 * 获取出库单条数据
	 * @param ckglDto
	 * @return
	 */
	public CkglDto getDtoByCkid(CkglDto ckglDto){
		return dao.getDtoByCkid(ckglDto);
	}

	/**
	 * 新增保存到货明细信息
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertCkgls(List<CkglDto> ckglDtos) {
		int result = dao.insertCkgls(ckglDtos);
		return result>0;
	}

	/**
	 * 通过llid分组查询
	 * @return
	 */
	@Override
	public List<CkglDto> groupByCkid(String llid) {
		return dao.groupByCkid(llid);
	}

	/**
	 * 批量更新关联id
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateCkgls(List<CkglDto> ckglDtos) {
		int result = dao.updateCkgls(ckglDtos);
		return result>0;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean delOutDepot(CkglDto ckglDto) {
		return dao.delOutDepot(ckglDto)!=0;
	}

	@Override
	public String generateCkjlbh(String s) {
		// 生成规则: IT-20210820-01    部门参数扩展-年份日期-流水号 。
		String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		String prefix = s + "-" + date + "-";
		//查询流水号
		String serial = dao.generateCkjlbh(prefix);
		return prefix +  serial;
	}

	/**
	 * 查询出库信息
	 * @param
	 * @return
	 */
	@Override
	public List<CkglDto> getDtoByCkids(CkglDto ckglDto) {
		// TODO Auto-generated method stub
		return dao.getDtoByCkids(ckglDto);
	}
	
	/**
	 * 更具领料id删除出库单
	 * @param
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteByLlid(CkglDto ckglDto) {
		return dao.deleteByLlid(ckglDto)>0;
	}
	
	/**
	 * 更具领料id获取出库信息
	 * @param
	 * @return
	 */
	@Override
	public List<CkglDto> getDtoByLlids(CkglDto ckglDto) {
		// TODO Auto-generated method stub
		return dao.getDtoByLlids(ckglDto);
	}
	
	/**
	 * 批量更新出库信息
	 * @param
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateCkglDtos(List<CkglDto> ckglDtos) {
		// TODO Auto-generated method stub
		return dao.updateCkglDtos(ckglDtos)>0;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addOutDepotPunch(CkglDto ckglDto) throws BusinessException {
		//出库信息保存
		boolean success = dao.insert(ckglDto)!=0;
		if (!success)
			throw new BusinessException("msg","新增出库单失败");
		List<HwxxDto> hwxxDtoList = JSONObject.parseArray(ckglDto.getHwxx_json(), HwxxDto.class);
		List<CkmxDto> ckmxDtos = new ArrayList<>();
		List<CkmxDto> ckmxModDtos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(hwxxDtoList)){
			for (HwxxDto hwxxDto : hwxxDtoList) {
				//货物信息组装
				hwxxDto.setHwid(StringUtil.generateUUID());
				hwxxDto.setLrry(ckglDto.getLrry());
				hwxxDto.setZt("05");
				//出库明细信息组装
				CkmxDto ckmxDto = new CkmxDto();
				//出库yds  yhcs 修改
				CkmxDto ckmxMod = new CkmxDto();
				if (StringUtil.isNotBlank(hwxxDto.getCkmxid())){
					ckmxDto.setYymxckd(hwxxDto.getCkmxid());
					ckmxMod.setCkmxid(hwxxDto.getCkmxid());
					ckmxMod.setYds(hwxxDto.getSl());
					ckmxMod.setYdsbj("1");
					ckmxMod.setXgry(ckglDto.getLrry());
					ckmxModDtos.add(ckmxMod);
				}
				ckmxDto.setHwid(hwxxDto.getHwid());
				ckmxDto.setCkmxid(StringUtil.generateUUID());
				ckmxDto.setCkid(ckglDto.getCkid());
				ckmxDto.setCksl(hwxxDto.getSl());
				ckmxDto.setLrry(ckglDto.getLrry());
				ckmxDtos.add(ckmxDto);
			}
			if (!CollectionUtils.isEmpty(ckmxModDtos)){
				success = ckmxService.updateCkmxList(ckmxModDtos);
				if (!success)
					throw new BusinessException("msg","出库明细信息更新失败！");
			}
			if (!CollectionUtils.isEmpty(hwxxDtoList)){
				success = hwxxService.insertHwxxs(hwxxDtoList);
				if (!success)
					throw new BusinessException("msg","货物信息新增失败！");
			}else{
				throw new BusinessException("msg","货物信息为空！");
			}
            success = ckmxService.insertckmxs(ckmxDtos);
            if (!success)
                throw new BusinessException("msg","出库明细信息新增失败！");

        }else{
			throw new BusinessException("msg","未获取到明细信息！");
		}
		return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean modOutDepotPunch(CkglDto ckglDto) throws BusinessException {
		//出库信息修改保存
		Boolean success = dao.update(ckglDto)!=0;
		if (!success)
			throw new BusinessException("msg","修改出库单失败");
		//根据出库id获取之前的数据
		CkmxDto ckmxDto_t = new CkmxDto();
		ckmxDto_t.setCkid(ckglDto.getCkid());
		List<CkmxDto> dtoMxList = ckmxService.getDtoMxList(ckmxDto_t);
		List<HwxxDto> hwxxDtoList = JSONObject.parseArray(ckglDto.getHwxx_json(), HwxxDto.class);
		//1.出库明细表判断有没有新增操作  根据现在的货物信息list和数据库查出来的数据进行比较，拿出货物id不同的
		//拿出来的分为新增和删除，剩下的则未修改的
		List<HwxxDto> add = new ArrayList<>();
		List<CkmxDto> ckmxDtos_add = new ArrayList<>();
		List<CkmxDto> ckmxDtos_mod = new ArrayList<>();
		List<HwxxDto> mod = new ArrayList<>();
		List<CkmxDto> ckmxModDtos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(hwxxDtoList)){
			for (HwxxDto hwxxDto : hwxxDtoList) {
				if (StringUtil.isNotBlank(hwxxDto.getHwid())){
//					mod.add(hwxxDto);
					if (!CollectionUtils.isEmpty(dtoMxList)){
						for (int i = 0; i < dtoMxList.size(); i++) {
							if (hwxxDto.getHwid().equals(dtoMxList.get(i).getHwid())){
								hwxxDto.setYclsl(dtoMxList.get(i).getCksl());
								mod.add(hwxxDto);
								dtoMxList.remove(i);
								i--;
							}
						}
					}
				}else{
					add.add(hwxxDto);
				}
			}
		}else{
			throw new BusinessException("msg","未获取到明细信息！");
		}
		//若存在新增
		if (!CollectionUtils.isEmpty(add)){
			for (HwxxDto hwxxDto : add) {
				//货物信息组装
				hwxxDto.setHwid(StringUtil.generateUUID());
				hwxxDto.setLrry(ckglDto.getXgry());
				hwxxDto.setXgry(ckglDto.getXgry());
				hwxxDto.setZt("05");
				//出库明细信息组装
				CkmxDto ckmxDto = new CkmxDto();
				//出库yds  yhcs 修改
				CkmxDto ckmxMod = new CkmxDto();
				if (StringUtil.isNotBlank(hwxxDto.getCkmxid())){
					ckmxDto.setYymxckd(hwxxDto.getCkmxid());
					ckmxMod.setCkmxid(hwxxDto.getCkmxid());
					ckmxMod.setYds(hwxxDto.getSl());
					ckmxMod.setYdsbj("1");
					ckmxMod.setXgry(ckglDto.getXgry());
					ckmxModDtos.add(ckmxMod);
				}
				ckmxDto.setHwid(hwxxDto.getHwid());
				ckmxDto.setCkmxid(StringUtil.generateUUID());
				ckmxDto.setCkid(ckglDto.getCkid());
				ckmxDto.setCksl(hwxxDto.getSl());
				ckmxDto.setLrry(ckglDto.getXgry());
				ckmxDto.setXgry(ckglDto.getXgry());
				ckmxDtos_add.add(ckmxDto);
			}
			success = ckmxService.insertckmxs(ckmxDtos_add);
			if (!success)
				throw new BusinessException("msg","出库明细信息新增失败！");
			success = hwxxService.insertHwxxs(add);
			if (!success)
				throw new BusinessException("msg","货物信息新增失败！");
		}
		//若存在修改
		if (!CollectionUtils.isEmpty(mod)){
			for (HwxxDto hwxxDto : mod) {
				//出库明细数据组装
				CkmxDto ckmxDto = new CkmxDto();
				//出库yds  yhcs 修改
				CkmxDto ckmxMod = new CkmxDto();
				if (StringUtil.isNotBlank(hwxxDto.getYymxckd())){
					ckmxMod.setCkmxid(hwxxDto.getYymxckd());
					ckmxMod.setYds(String.valueOf(Double.parseDouble(hwxxDto.getSl()) - Double.parseDouble(hwxxDto.getYclsl())));
					ckmxMod.setYdsbj("1");
					ckmxMod.setXgry(ckglDto.getXgry());
					ckmxModDtos.add(ckmxMod);
				}
				ckmxDto.setXgry(ckglDto.getXgry());
				ckmxDto.setCksl(hwxxDto.getSl());
				ckmxDto.setCkmxid(hwxxDto.getCkmxid());
				ckmxDtos_mod.add(ckmxDto);
				//货物信息组装
				hwxxDto.setYclsl(null);
				hwxxDto.setXgry(ckglDto.getXgry());
			}
			//做修改处理
			success = ckmxService.updateCkmxList(ckmxDtos_mod);
			if (!success)
				throw new BusinessException("msg","出库明细信息修改失败！");
			success = hwxxService.updateHwxxDtos(mod);
			if (!success)
				throw new BusinessException("msg","货物信息修改失败！");
		}
		//若存在删除   dtoMxList
		if (!CollectionUtils.isEmpty(dtoMxList)){
			//做删除操作
			HwxxDto hwxxDto = new HwxxDto();
			hwxxDto.setIds(new ArrayList<>());
			hwxxDto.setScry(ckglDto.getXgry());
			CkmxDto ckmxDto = new CkmxDto();
			ckmxDto.setIds(new ArrayList<>());
			ckmxDto.setScry(ckglDto.getXgry());
			ckmxDto.setScbj("1");
			for (CkmxDto dto : dtoMxList) {
				hwxxDto.getIds().add(dto.getHwid());
				ckmxDto.getIds().add(dto.getCkmxid());
				CkmxDto ckmxMod = new CkmxDto();
				if (StringUtil.isNotBlank(dto.getYymxckd())){
					ckmxMod.setCkmxid(dto.getYymxckd());
					ckmxMod.setYds(dto.getCksl());
					ckmxMod.setYdsbj("0");
					ckmxMod.setXgry(ckglDto.getXgry());
					ckmxModDtos.add(ckmxMod);
				}
			}
			success = ckmxService.deleteByCkmxids(ckmxDto);
			if (!success)
				throw new BusinessException("msg","出库明细信息修改失败！");
			success = hwxxService.deleteHwxxDtos(hwxxDto);
			if (!success)
				throw new BusinessException("msg","货物信息删除失败！");
		}
		if (!CollectionUtils.isEmpty(ckmxModDtos)){
			success = ckmxService.updateCkmxList(ckmxModDtos);
			if (!success)
				throw new BusinessException("msg","出库明细信息更新失败！");
		}
		return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean delOutDepotPunch(CkglDto ckglDto) throws BusinessException {
		Boolean success = dao.delete(ckglDto)!=0;
		if (!success)
			throw new BusinessException("msg","修改出库单删除失败！");
		//根据出库id获取之前的数据
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setIds(new ArrayList<>());
		hwxxDto.setScry(ckglDto.getScry());
		CkmxDto ckmxDto = new CkmxDto();
		ckmxDto.setIds(new ArrayList<>());
		ckmxDto.setScry(ckglDto.getScry());
		ckmxDto.setScbj("1");
		ckmxDto.setCkids(ckglDto.getIds());
		List<CkmxDto> ckmxModDtos = new ArrayList<>();
		List<CkmxDto> dtoMxList = ckmxService.getDtoMxList(ckmxDto);
		List<CkhwxxDto> modCkhwxxDtos = new ArrayList<>();
		List<HwxxDto> modHwxxDtos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(dtoMxList)){
			//做删除操作
			for (CkmxDto dto : dtoMxList) {
				if (!"WW".equals(dto.getCklbcsdm())&&!"HZ".equals(dto.getCklbcsdm())){
					throw new BusinessException("msg","不允许删除出库类别为"+dto.getCklbcsmc()+"的单子："+dto.getZckdh());
				}
				if ("HZ".equals(dto.getCklbcsdm())){
					hwxxDto.getIds().add(dto.getHwid());
				}
				ckmxDto.getIds().add(dto.getCkmxid());
				if (StatusEnum.CHECK_PASS.getCode().equals(dto.getCkzt())){
					CkhwxxDto ckhwxxDto = new CkhwxxDto();
					ckhwxxDto.setCkid(dto.getHwckid());
					ckhwxxDto.setWlid(dto.getWlid());
					if ("WW".equals(dto.getCklbcsdm())){
						ckhwxxDto.setKclbj("1");
						HwxxDto hwxxDto_mod = new HwxxDto();
						hwxxDto_mod.setHwid(dto.getHwid());
						hwxxDto_mod.setAddkcl(dto.getCkmxcksl());
						modHwxxDtos.add(hwxxDto_mod);
					}else {
						ckhwxxDto.setKclbj("0");
						CkmxDto ckmxMod = new CkmxDto();
						ckmxMod.setCkmxid(dto.getYymxckd());
						ckmxMod.setYhcs(dto.getCkmxcksl());
						ckmxMod.setHcsbj("0");
						ckmxModDtos.add(ckmxMod);
					}
					ckhwxxDto.setKcl(dto.getCksl());
					modCkhwxxDtos.add(ckhwxxDto);
				}else{
					if (StringUtil.isNotBlank(dto.getYymxckd())){
						CkmxDto ckmxMod = new CkmxDto();
						ckmxMod.setCkmxid(dto.getYymxckd());
						ckmxMod.setYds(dto.getCksl());
						ckmxMod.setYdsbj("0");
						ckmxMod.setXgry(ckglDto.getScry());
						ckmxModDtos.add(ckmxMod);
					}
					if ("WW".equals(dto.getCklbcsdm())){
						HwxxDto hwxxDto_mod = new HwxxDto();
						hwxxDto_mod.setHwid(dto.getHwid());
						hwxxDto_mod.setYds(dto.getCkmxcksl());
						hwxxDto_mod.setYdsbj("0");
						modHwxxDtos.add(hwxxDto_mod);
					}
				}
			}
		}
		if (!CollectionUtils.isEmpty(modHwxxDtos)){
			boolean isSuccess = hwxxService.updateHwxxDtos(modHwxxDtos);
			if (!isSuccess){
				throw new BusinessException("msg","同步货物信息库存量或预定数失败！");
			}
		}
		if (!CollectionUtils.isEmpty(modCkhwxxDtos)){
			boolean isSuccess = ckhwxxService.updateByWlidAndCkid(modCkhwxxDtos);
			if (!isSuccess){
				throw new BusinessException("msg","同步仓库货物消息库存量失败！");
			}
		}
		if ( !CollectionUtils.isEmpty(ckmxDto.getIds())){
			success = ckmxService.deleteByCkmxids(ckmxDto);
			if (!success)
				throw new BusinessException("msg","出库明细信息修改失败！");
		}
		if ( !CollectionUtils.isEmpty(hwxxDto.getIds())){
			success = hwxxService.deleteHwxxDtos(hwxxDto);
			if (!success)
				throw new BusinessException("msg","货物信息删除失败！");
		}
		if (!CollectionUtils.isEmpty(ckmxModDtos)){
			success = ckmxService.updateCkmxList(ckmxModDtos);
			if (!success)
				throw new BusinessException("msg","出库明细信息更新失败！");
		}
		return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
		CkglDto ckglDto = (CkglDto) baseModel;
		ckglDto.setXgry(operator.getYhid());
		boolean isSuccess = true;
		if (StringUtil.isBlank(ckglDto.getDdbj())){
			isSuccess = modOutDepotPunch(ckglDto);
		}
		return isSuccess;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
//		String token = talkUtil.getToken();
		String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
		String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
		String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
		String ICOMM_SH00087 = xxglService.getMsg("ICOMM_SH00087");
		for (ShgcDto shgcDto : shgcList) {
			CkglDto ckglDto = new CkglDto();
			ckglDto.setCkid(shgcDto.getYwid());

			ckglDto.setXgry(operator.getYhid());
			CkglDto ckglDto1 = dao.getDtoByCkid(ckglDto);
			shgcDto.setSqbm(ckglDto1.getSsjg());
			List<SpgwcyDto> spgwcyDtos = commonService.siftJgList(shgcDto.getSpgwcyDtos(), ckglDto1.getSsjg());
			// 审核退回
			if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
				// 由于合同提交时直接发起了钉钉审批，所以审核不通过时直接采用钉钉的审批拒绝消息即可
				ckglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				// 发送钉钉消息
				if (!CollectionUtils.isEmpty(spgwcyDtos)) {
					for (SpgwcyDto spgwcyDto : spgwcyDtos) {
						if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
							talkUtil.sendWorkMessage(spgwcyDto.getYhm(),
									spgwcyDto.getYhid(), ICOMM_SH00026,
									xxglService.getMsg("ICOMM_SH00088", operator.getZsxm(), shgcDto.getShlbmc(),
											ckglDto.getCkdh(), ckglDto.getJgmc(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
				// 审核通过
			} else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
				if ("领料出库".equals(ckglDto1.getCklbmc())||"委外出库".equals(ckglDto1.getCklbmc())||"出库红字".equals(ckglDto1.getCklbmc())){
					if (!rdRecordService.determine_Entry(ckglDto1.getCkrq())){
						throw new BusinessException("ICOM99019","该月份已结账，不允许再录入U8数据，请修改单据日期!");
					}
				}
				ckglDto.setZt(StatusEnum.CHECK_PASS.getCode());
				ckglDto1.setZsxm(operator.getZsxm());
				ckglDto1.setXgry(ckglDto.getXgry());

				Map<String, Object> map = rdRecordService.addU8DataByOutDepot(ckglDto1);
				CkglDto ckgl = (CkglDto) map.get("ckgl");
				ckglDto.setGlid(ckgl.getGlid());
				ckglDto.setU8ckdh(ckgl.getU8ckdh());
				@SuppressWarnings("unchecked")
				List<CkmxDto> ckmxs = (List<CkmxDto>) map.get("ckmxList");
//				boolean result_ck = ckmxService.updateCkmxList(ckmxs);
//				if(!result_ck) {
//					throw new BusinessException("msg","批量更新出库明细关联id失败！");
//				}
				CkmxDto ckmxDto1 = new CkmxDto();
				ckmxDto1.setCkid(ckglDto.getCkid());
				List<CkmxDto> dtoMxList = ckmxService.getDtoMxList(ckmxDto1);
				List<CkhwxxDto> add = new ArrayList<>();
				List<HwxxDto> hwxxDtos = new ArrayList<>();
				List<CkhwxxDto> mod = new ArrayList<>();
				for (CkmxDto ckmxDto : dtoMxList) {
					CkmxDto ckmxMod = new CkmxDto();
					if (StringUtil.isNotBlank(ckmxDto.getYymxckd())){
						ckmxMod.setCkmxid(ckmxDto.getYymxckd());
						ckmxMod.setYds(ckmxDto.getCksl());
						ckmxMod.setYdsbj("0");
						ckmxMod.setHcsbj("1");
						ckmxMod.setYhcs(ckmxDto.getCksl());
						ckmxMod.setXgry(ckglDto.getXgry());
						ckmxs.add(ckmxMod);
					}
					CkhwxxDto ckhwxxDto = new CkhwxxDto();
					ckhwxxDto.setCkqxlx(ckmxDto.getCkqxlx());
					ckhwxxDto.setWlid(ckmxDto.getWlid());
					ckhwxxDto.setCkid(ckmxDto.getHwckid());
					HwxxDto hwxxDto = new HwxxDto();
					hwxxDto.setHwid(ckmxDto.getHwid());
					hwxxDto.setZt("99");
					hwxxDtos.add(hwxxDto);
					CkhwxxDto dtoByWlidAndCkqx = ckhwxxService.getDtoByWlidAndCkid(ckhwxxDto);
					if (null!= dtoByWlidAndCkqx){//updateByCkhwid
						CkhwxxDto modCkhwxxDto = new CkhwxxDto();
						modCkhwxxDto.setCkhwid(dtoByWlidAndCkqx.getCkhwid());
						modCkhwxxDto.setKcl(ckmxDto.getCksl());
						modCkhwxxDto.setKclbj("1");
						mod.add(modCkhwxxDto);
					}else{ //insertCkhwxxs
						ckhwxxDto.setKcl(ckmxDto.getCksl());
						ckhwxxDto.setYds("0");
						ckhwxxDto.setCkhwid(StringUtil.generateUUID());
						add.add(ckhwxxDto);
					}
				}
				boolean  result_ck;
				if (!CollectionUtils.isEmpty(ckmxs)){
					result_ck = ckmxService.updateCkmxList(ckmxs);
					if (!result_ck)
						throw new BusinessException("msg","出库明细信息更新失败！");
				}
				if (!CollectionUtils.isEmpty(mod)){
					result_ck = ckhwxxService.updateByCkhwid(mod);
					if (!result_ck){
						throw new BusinessException("msg","更新库存量失败！");
					}
				}
				if (!CollectionUtils.isEmpty(add)){
					result_ck = ckhwxxService.insertCkhwxxs(add);
					if (!result_ck){
						throw new BusinessException("msg","更新库存量失败！");
					}
				}
				result_ck = hwxxService.updateHwxxDtos(hwxxDtos);
				if (!result_ck)
					throw new BusinessException("货物信息修改失败！");

				if (!CollectionUtils.isEmpty(spgwcyDtos)) {
					for (SpgwcyDto spgwcyDto : spgwcyDtos) {
						if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
							talkUtil.sendWorkMessage(spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00006, StringUtil.replaceMsg(ICOMM_SH00087,
									operator.getZsxm(), shgcDto.getShlbmc(), ckglDto1.getCkdh(), ckglDto1.getJgmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}

			}else {
				ckglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				String ICOMM_SH00086 = xxglService.getMsg("ICOMM_SH00086");
				// 发送钉钉消息
				if (!CollectionUtils.isEmpty(spgwcyDtos)) {
					try {
						for (SpgwcyDto spgwcyDto : spgwcyDtos) {
							if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
								talkUtil.sendWorkMessage(spgwcyDto.getYhm(),
										spgwcyDto.getYhid(), ICOMM_SH00003, StringUtil.replaceMsg(ICOMM_SH00086,
												operator.getZsxm(), shgcDto.getShlbmc(), ckglDto1.getCkdh(), ckglDto1.getJgmc(),
												DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}

				//发送钉钉消息--取消审核人员
				if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())){
					int size = shgcDto.getNo_spgwcyDtos().size();
					for(int i=0;i<size;i++){
						if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
							talkUtil.sendWorkMessage(shgcDto.getNo_spgwcyDtos().get(i).getYhm(), 
									shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",shgcDto.getShlbmc() ,ckglDto.getCkdh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
			}
			update(ckglDto);
		}

		return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
		if (shgcList == null || shgcList.isEmpty()) {
			return true;
		}
		if (auditParam.isCancelOpe()) {
			// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String fhid = shgcDto.getYwid();
				CkglDto ckglDto = new CkglDto();
				ckglDto.setXgry(operator.getYhid());
				ckglDto.setCkid(fhid);
				ckglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				modOutDepotPunch(ckglDto);
			}
		} else {
			// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String ckid = shgcDto.getYwid();
				CkglDto ckglDto = new CkglDto();
				ckglDto.setXgry(operator.getYhid());
				ckglDto.setCkid(ckid);
				ckglDto.setZt(StatusEnum.CHECK_NO.getCode());
				boolean success = dao.update(ckglDto) != 0;
				if (!success)
					throw new BusinessException("更新主表信息失败！");
			}
		}
		return true;
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
		return null;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
		Map<String, Object> map =new HashMap<>();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
		CkglDto ckglDto = new CkglDto();
		ckglDto.setIds(ids);
		List<CkglDto> dtoList = dao.getDtoByCkids(ckglDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(CkglDto dto:dtoList){
				list.add(dto.getCkid());
			}
		}
		map.put("list",list);
		return map;
	}

	/**
	 * 委外出库保存
	 * @param ckglDto
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean saveOutsourceDelivery(CkglDto ckglDto) throws BusinessException {
		ckglDto.setCkid(StringUtil.generateUUID());
		ckglDto.setZt(StatusEnum.CHECK_PASS.getCode());
		int insert = dao.insert(ckglDto);
		if(insert==0){
			throw new BusinessException("msg","新增出库单失败");
		}
		List<HwxxDto> hwxxDtos = JSON.parseArray(ckglDto.getCkmx_json(), HwxxDto.class);
		if(!CollectionUtils.isEmpty(hwxxDtos)){
			List<CkmxDto> list=new ArrayList<>();
			List<CkhwxxDto> ckhwlist=new ArrayList<>();
			for(HwxxDto hwxxDto:hwxxDtos){
				hwxxDto.setXgry(ckglDto.getLrry());
				hwxxDto.setModkcl(hwxxDto.getSl());
				CkmxDto ckmxDto=new CkmxDto();
				ckmxDto.setCkmxid(StringUtil.generateUUID());
				ckmxDto.setCkid(ckglDto.getCkid());
				ckmxDto.setWwhtmxid(hwxxDto.getHtmxid());
				ckmxDto.setHwid(hwxxDto.getHwid());
				ckmxDto.setCksl(hwxxDto.getSl());
				ckmxDto.setLrry(ckglDto.getLrry());
				ckmxDto.setCpjgmxid(hwxxDto.getCpjgmxid());
				list.add(ckmxDto);
				CkhwxxDto ckhwxxDto=new CkhwxxDto();
				ckhwxxDto.setCkhwid(hwxxDto.getCkhwid());
				ckhwxxDto.setKcl(hwxxDto.getSl());
				ckhwxxDto.setKclbj("0");
				ckhwlist.add(ckhwxxDto);
			}
			boolean isSuccess = ckmxService.insertckmxs(list);
			if(!isSuccess){
				throw new BusinessException("新增出库明细失败！");
			}
			isSuccess = hwxxService.updateKclList(hwxxDtos);
			if(!isSuccess){
				throw new BusinessException("更新货物库存量失败！");
			}
			isSuccess =ckhwxxService.updateByCkhwid(ckhwlist);
			if(!isSuccess){
				throw new BusinessException("更新仓库货物库存量失败！");
			}
			Map<String, Object> map = rdRecordService.addU8DataByOutDepot(ckglDto);
			CkglDto ckgl = (CkglDto) map.get("ckgl");
			ckglDto.setGlid(ckgl.getGlid());
			ckglDto.setU8ckdh(ckgl.getU8ckdh());
			ckglDto.setXgry(ckglDto.getLrry());
			isSuccess = update(ckglDto);
			if(!isSuccess){
				throw new BusinessException("新增U8失败！");
			}
			@SuppressWarnings("unchecked")
			List<CkmxDto> ckmxs = (List<CkmxDto>) map.get("ckmxList");
			isSuccess = ckmxService.updateCkmxList(ckmxs);
			if(!isSuccess){
				throw new BusinessException("更新U8明细关联ID失败！");
			}
		}

		return true;
	}

	@Override
	public List<CkglDto> selectCkglDtoByFhids(FhglDto fhglDto) {
		return dao.selectCkglDtoByFhids(fhglDto);
	}
}
