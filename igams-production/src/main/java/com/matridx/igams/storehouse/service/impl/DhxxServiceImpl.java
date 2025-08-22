package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.entities.SczlglDto;
import com.matridx.igams.production.dao.entities.SczlmxDto;
import com.matridx.igams.production.service.svcinterface.*;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.dao.post.IDhxxDao;
import com.matridx.igams.storehouse.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DhxxServiceImpl extends BaseBasicServiceImpl<DhxxDto, DhxxModel, IDhxxDao> implements IDhxxService,IAuditService{
	@Autowired
	IShgcService shgcService;
	
	@Autowired
	ICommonService commonservice;
	
	@Autowired
	IHwxxService hwxxService;
	
	@Autowired
	DingTalkUtil talkUtil;


	@Autowired
	IXxglService xxglService;

	@Autowired
	ICkxxService ckxxService;
	@Autowired
	ISczlmxService sczlmxService;
	@Autowired
	ISczlglService sczlglService;
	
	@Autowired
	IDdxxglService ddxxglService;
	
	@Autowired
	IJcsjService jcsjService;
	
	@Autowired
	IHtmxService htmxService;

//	@Autowired
//	IWlglService wlglServicel;
	
	@Autowired
	IQgmxService qgmxService;
	@Autowired
	ICkhwxxService ckhwxxService;
	
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	@Autowired
	IRdRecordService rdRecordService;
	@Autowired
	IRkcglService rkcglService;
	@Autowired
	IFjcfbService fjcfbService;

	@Value("${matridx.rabbit.systemreceiveflg:}")
	private String systemreceiveflg;
	@Value("${sqlserver.matridxds.flag:}")
	private String matridxdsflag;
	@Autowired
	IXtszService xtszService;

	private final Logger log = LoggerFactory.getLogger(DhxxServiceImpl.class);
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(DhxxDto dhxxDto) {
		if(StringUtils.isBlank(dhxxDto.getDhid())) {
			dhxxDto.setDhid(StringUtil.generateUUID());
		}
		dhxxDto.setZt(StatusEnum.CHECK_NO.getCode());
		int result = dao.insert(dhxxDto);
		return result != 0;
	}
	
	/**
	 * 到货列表（查询审核状态）
	 * 
	 * @param dhxxDto
	 * @return
	 */
	@Override
	public List<DhxxDto> getPagedDtoList(DhxxDto dhxxDto) {
		List<DhxxDto> list = dao.getPagedDtoList(dhxxDto);
		try {
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_ARRIVAL.getCode(), "zt", "dhid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_PENDING.getCode(), "zt", "dhid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return list;
	}
	
	/**
	 * 到货审核列表
	 * 
	 * @param dhxxDto
	 * @return
	 */
	@Override
	public List<DhxxDto> getPagedAuditDhxx(DhxxDto dhxxDto) {
		// 获取人员ID和履历号
		List<DhxxDto> t_sbyzList = dao.getPagedAuditDhxx(dhxxDto);
		if(CollectionUtils.isEmpty(t_sbyzList))
			return t_sbyzList;

		List<DhxxDto> sqList = dao.getAuditListDhxx(t_sbyzList);

		commonservice.setSqrxm(sqList);

		return sqList;
	}
	
	/**
	 * 判断到货单号是否重复
	 * @param dhdh
	 * @param dhid
	 * @return
	 */
	@Override
	public boolean isDhdhRepeat(String dhdh,String dhid) {
		if(StringUtils.isNotBlank(dhdh)) {
			DhxxDto dhxxDto=new DhxxDto();
			dhxxDto.setDhdh(dhdh);
			dhxxDto.setDhid(dhid);
			List<DhxxDto> dhxxDtos=dao.getListByDhdh(dhxxDto);
			return dhxxDtos == null || dhxxDtos.size() <= 0;
		}
		return true;
	}
	
	/**
	 * 保存到货信息
	 * @param dhxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addSaveArrivalGoods(DhxxDto dhxxDto) throws BusinessException{
		JcsjDto jcsjDto = jcsjService.getDtoById(dhxxDto.getRklb());
		//到货新增红冲单处理标记为1 待处理新增红冲单处理标记为0
		if ("CGHZ".equals(jcsjDto.getCskz3())){
			dhxxDto.setClbj("1");
		}
		//保存到货信息表
		boolean result=insertDto(dhxxDto);
		//文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(dhxxDto.getFjids())){
			for (int i = 0; i < dhxxDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(dhxxDto.getFjids().get(i),dhxxDto.getDhid());
				if(!saveFile)
					throw new BusinessException("ICOM00002","请购附件保存失败!");
			}
		}
		if(!result)
			return false;
		if("0".equals(jcsjDto.getCskz1())&&!"CGHZ".equals(jcsjDto.getCskz3())) {
			List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class);
			//修改请购单产品注册号
			List<QgmxDto> qgmxDtos = new ArrayList<>();
			//修改合同单产品注册号
			List<HtmxDto> htmxDtos = new ArrayList<>();
			//保存货物信息表
			if(!CollectionUtils.isEmpty(hwxxDtos)){
				for (HwxxDto hwxxDto : hwxxDtos) {
					hwxxDto.setDhid(dhxxDto.getDhid());
					hwxxDto.setLrry(dhxxDto.getLrry());
					hwxxDto.setCkid(dhxxDto.getCkid());
					hwxxDto.setDhrq(dhxxDto.getDhrq());
					if (StringUtil.isNotBlank(hwxxDto.getScph())) {
						hwxxDto.setScph(hwxxDto.getScph().trim());
					}
					hwxxDto.setZt(GoodsStatusEnum.GODDS_CHECK.getCode());
					if (StringUtil.isBlank(hwxxDto.getDhsl())) {
						throw new BusinessException("msg", hwxxDto.getWlbm() + "到货数量不允许为空！");
					}
					if (Double.parseDouble(hwxxDto.getDhsl()) <= 0) {
						throw new BusinessException("msg", hwxxDto.getWlbm() + "到货数量不得小于等于0，若该物料为到货，请删除！");
					}
					if (StringUtil.isNotBlank(hwxxDto.getHtbz())) {
						hwxxDto.setDhbz(hwxxDto.getDhbz() + "/合同明细备注：" + hwxxDto.getHtbz());
					}
					QgmxDto qgmxDto = new QgmxDto();
					qgmxDto.setQgmxid(hwxxDto.getQgmxid());
					qgmxDto.setCpzch(hwxxDto.getCpzch());
					qgmxDto.setXgry(dhxxDto.getLrry());
					qgmxDtos.add(qgmxDto);
					HtmxDto htmxDto = new HtmxDto();
					htmxDto.setHtmxid(hwxxDto.getHtmxid());
					htmxDto.setCpzch(hwxxDto.getCpzch());
					htmxDto.setXgry(dhxxDto.getLrry());
					htmxDtos.add(htmxDto);
				}
				htmxService.updateHtmxDtos(htmxDtos);
				qgmxService.updateCpzch(qgmxDtos);
			}
			boolean addHwxx=hwxxService.insertHwxxList(hwxxDtos);
			if(!addHwxx)
				throw new BusinessException("msg","更新货物信息失败！");
		}else if ("0".equals(jcsjDto.getCskz1())&&"CGHZ".equals(jcsjDto.getCskz3())){
			List<HwxxDto> upHwxxDtos = new ArrayList<>();
			List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class);
			//保存货物信息表
			if(!CollectionUtils.isEmpty(hwxxDtos)){
				String ckid = hwxxDtos.get(0).getCkid();
				for (HwxxDto dto : hwxxDtos) {
					if (Double.parseDouble(dto.getDhsl()) >= 0) {
						throw new BusinessException("msg", dto.getWlbm() + "红冲数量不得大于等于0！");
					}
					// if (StringUtil.isBlank(hwxxDtos.get(i).getRkid())){
					// 	throw new BusinessException("msg",hwxxDtos.get(i).getWlbm()+"未入库，不允许在到货列表做采购红字，请在待处理列表进行处理，或入库之后再进行处理。！");
					// }
					dto.setRkid(null);
					// //判断库存量是不是小于不合格数量，如果小于，不允许红冲。
					// int flag = new BigDecimal(hwxxDtos.get(i).getYhwkcl()).compareTo(new BigDecimal(hwxxDtos.get(i).getYhwbhgsl()));
					// if (flag<0){
					// 	throw new BusinessException("msg",hwxxDtos.get(i).getWlbm()+"库存量小于不合格数量,不允许做采购红冲单！");
					// }
					if (!ckid.equals(dto.getCkid())) {
						throw new BusinessException("msg", "仓库不同不允许一起到货！");
					}
					HwxxDto hwxxDto = new HwxxDto();
					hwxxDto.setHwid(dto.getHwid());
					hwxxDto.setXgry(dhxxDto.getLrry());
					hwxxDto.setByycls(dto.getDhsl());
					hwxxDto.setYclsbj("0");
					upHwxxDtos.add(hwxxDto);
					dto.setChhwid(dto.getHwid());
					dto.setHwid(StringUtil.generateUUID());
					dto.setDhid(dhxxDto.getDhid());
					if (StringUtil.isNotBlank(dto.getScph())) {
						dto.setScph(dto.getScph().trim());
					}
					dto.setLrry(dhxxDto.getLrry());
					// hwxxDtos.get(i).setCkid(dhxxDto.getCkid());
					dto.setDhrq(dhxxDto.getDhrq());
					dto.setZt(GoodsStatusEnum.GODDS_CHECK.getCode());
				}
				boolean addHwxx=hwxxService.insertHwxxList(hwxxDtos);
				if(!addHwxx)
					throw new BusinessException("msg","新增货物信息失败！");
				boolean upHwxx = hwxxService.updateHwxxDtos(upHwxxDtos);
				if(!upHwxx)
					throw new BusinessException("msg","更新货物信息失败！");
			}
		}else {
			List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getHwxx_json(), HwxxDto.class);
			List<SczlmxDto> sczlmxDtos = new ArrayList<>();
			Double yysl = 0d;
			for (HwxxDto hwxxDto : hwxxDtos) {
				hwxxDto.setXgry(dhxxDto.getLrry());
				hwxxDto.setDhid(dhxxDto.getDhid());
				hwxxDto.setCkid(dhxxDto.getCkid());
				hwxxDto.setRkry(dhxxDto.getLrry());
				hwxxDto.setDhrq(dhxxDto.getDhrq());
				if (StringUtil.isNotBlank(hwxxDto.getSbysid())){
					hwxxDto.setTkbj("1");
				}
				if (StringUtil.isNotBlank(hwxxDto.getScph())){
					hwxxDto.setScph(hwxxDto.getScph().trim());
				}
				hwxxDto.setSl(String.valueOf((Double.parseDouble(hwxxDto.getDhsl())-Double.parseDouble(StringUtil.isNotBlank(hwxxDto.getCythsl())?hwxxDto.getCythsl():"0"))));
				hwxxDto.setZt(GoodsStatusEnum.GODDS_CHECK.getCode());
				if (Double.parseDouble(hwxxDto.getDhsl())<=0){
					throw new BusinessException("msg",hwxxDto.getWlbm()+"到货数量不得小于等于0，若该物料为到货，请删除！");
				}
				if (StringUtil.isNotBlank(dhxxDto.getSczlid())){
					SczlmxDto sczlmxDto = new SczlmxDto();
					sczlmxDto.setSczlmxid(hwxxDto.getSczlmxid());
					sczlmxDto.setYysl(hwxxDto.getDhsl());
					sczlmxDto.setYyslbj("1");
					sczlmxDtos.add(sczlmxDto);
					yysl+=Double.parseDouble(hwxxDto.getDhsl());
				}
			}
			if (StringUtil.isNotBlank(dhxxDto.getSczlid())){
				SczlglDto sczlglDto = new SczlglDto();
				sczlglDto.setSczlid(dhxxDto.getSczlid());
				sczlglDto.setYysl(String.valueOf(yysl));
				sczlglDto.setYyslbj("1");
				boolean success = sczlglService.update(sczlglDto);
				if (!success)
					throw new BusinessException("msg","更新生产指令信息失败！");
				success = sczlmxService.updateList(sczlmxDtos);
				if (!success)
					throw new BusinessException("msg","更新生产指令明细信息失败！");
			}

			boolean result_hw = hwxxService.insertHwxxList(hwxxDtos);
			if(!result_hw) {
				throw new BusinessException("msg","新增货物信息失败！");
			}
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean purchaseAdd(DhxxDto dhxxDto) throws BusinessException{
		//到货新增红冲单处理标记为1 待处理新增红冲单处理标记为0
		dhxxDto.setClbj("0");
		//保存到货信息表
		boolean result=insertDto(dhxxDto);
		if(!result)
			return false;
		List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getHwxx_json(), HwxxDto.class);
		List<HwxxDto> hwxxDtoList = new ArrayList<>();
		for (HwxxDto hwxxDto : hwxxDtos) {
			HwxxDto hwxxDto1 = new HwxxDto();
			hwxxDto1.setHwid(hwxxDto.getHwid());
			hwxxDto1.setXgry(dhxxDto.getLrry());
			hwxxDto1.setByycls(hwxxDto.getDhsl());
			hwxxDto1.setYclsbj("0");
			hwxxDtoList.add(hwxxDto1);
			hwxxDto.setChhwid(hwxxDto.getHwid());
			hwxxDto.setHwid(null);
			hwxxDto.setLrry(dhxxDto.getLrry());
			hwxxDto.setDhid(dhxxDto.getDhid());
			hwxxDto.setDhrq(dhxxDto.getDhrq());
			hwxxDto.setDhbz(dhxxDto.getBz());
			hwxxDto.setZt(GoodsStatusEnum.GODDS_CHECK.getCode());
			if (Double.parseDouble(hwxxDto.getDhsl())>=0){
				throw new BusinessException("msg",hwxxDto.getWlbm()+"红冲数量不得大于等于0！");
			}
		}
		boolean result_hw = hwxxService.insertHwxxList(hwxxDtos);
		if(!result_hw) {
			throw new BusinessException("msg","新增货物信息失败！");
		}
		//更新货物信息
		boolean result_mod = hwxxService.updateHwxxDtos(hwxxDtoList);
		if(!result_mod)
			throw new BusinessException("msg","修改货物信息失败！");
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean purchaseMod(DhxxDto dhxxDto) throws BusinessException{

		//保存到货信息表
		int result=dao.update(dhxxDto);
		if(result<=0)
			throw new BusinessException("msg","修改到货信息失败！");
		HwxxDto dto = new HwxxDto();
		dto.setDhid(dhxxDto.getDhid());
		List<HwxxDto> hwxxDtoList = hwxxService.getListByChDhid(dto);
		List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getHwxx_json(), HwxxDto.class);
		List<HwxxDto> list_mod = new ArrayList<>();
		List<String> list_del = new ArrayList<>();
		//新增修改
		for (HwxxDto hwxxDto1 : hwxxDtos) {
			for (int i = hwxxDtoList.size()-1; i >= 0 ; i--) {
				if (!CollectionUtils.isEmpty(hwxxDtoList) && hwxxDtoList.get(i).getHwid().equals(hwxxDto1.getHwid()) ){
					hwxxDto1.setXgry(dhxxDto.getXgry());
					HwxxDto hwxxDto = new HwxxDto();
					hwxxDto.setHwid(hwxxDto1.getChhwid());
					hwxxDto.setXgry(dhxxDto.getXgry());
					hwxxDto.setByycls(String.valueOf(Double.parseDouble(hwxxDtoList.get(i).getHcsl())-Double.parseDouble(hwxxDto1.getDhsl())));
					hwxxDto.setYclsbj("1");
					list_mod.add(hwxxDto);
					hwxxDtoList.remove(i);
				}
			}
		}
		if(!CollectionUtils.isEmpty(hwxxDtoList)){
			//删除修改
			for (HwxxDto hwxxDto : hwxxDtoList) {
				HwxxDto dto1 = new HwxxDto();
				dto1.setHwid(hwxxDto.getChhwid());
				dto1.setXgry(dhxxDto.getXgry());
				dto1.setYclsbj("1");
				dto1.setByycls(hwxxDto.getHcsl());
				list_mod.add(dto1);
				list_del.add(hwxxDto.getHwid());
			}
		}
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setIds(list_del);
		hwxxDto.setScry(dhxxDto.getXgry());
		boolean updateHwxx;
		if (!CollectionUtils.isEmpty(hwxxDtos)){
			updateHwxx=hwxxService.updateHwxxDtos(hwxxDtos);
			if(!updateHwxx)
				throw new BusinessException("msg","修改货物信息失败！");
		}
		if (!CollectionUtils.isEmpty(list_mod)){
			updateHwxx=hwxxService.updateHwxxDtos(list_mod);
			if(!updateHwxx)
				throw new BusinessException("msg","修改货物已处理数失败！");
		}
		if (!CollectionUtils.isEmpty(list_del)){
			updateHwxx=hwxxService.deleteHwxxDtos(hwxxDto);
			if(!updateHwxx)
				throw new BusinessException("msg","删除货物信息失败！！");
		}
		return true;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean purchaseDel(DhxxDto dhxxDto) throws BusinessException{

		HwxxDto dto = new HwxxDto();
		dto.setIds(dhxxDto.getIds());
		List<HwxxDto> hwxxDtoList = hwxxService.getListByChDhid(dto);
		List<HwxxDto> list_mod = new ArrayList<>();
		List<String> list_del = new ArrayList<>();
		List<HtmxDto> htmxDtos = new ArrayList<>();
		List<CkhwxxDto> modCkhwxxDtos = new ArrayList<>();
		if(!CollectionUtils.isEmpty(hwxxDtoList)){
			//删除修改
			for (HwxxDto hwxxDto : hwxxDtoList) {
				HwxxDto dto1 = new HwxxDto();
				dto1.setHwid(hwxxDto.getChhwid());
				dto1.setXgry(dhxxDto.getScry());
				dto1.setYclsbj("1");
				dto1.setByycls(hwxxDto.getHcsl());
				list_del.add(hwxxDto.getHwid());
				if (StatusEnum.CHECK_PASS.getCode().equals(hwxxDto.getZt())){
					HtmxDto htmxDto = new HtmxDto();
					htmxDto.setHtmxid(hwxxDto.getHtmxid());
					htmxDto.setBydhsl(hwxxDto.getHcsl());
					htmxDto.setDhslbj("0");
					htmxDto.setXgry(dhxxDto.getScry());
					htmxDtos.add(htmxDto);
					if ("1".equals(hwxxDto.getClbj())){
						dto1.setModkcl(hwxxDto.getHcsl());
						CkhwxxDto ckhwxxDto = new CkhwxxDto();
						ckhwxxDto.setCkid(hwxxDto.getCkid());
						ckhwxxDto.setWlid(hwxxDto.getWlid());
						ckhwxxDto.setKcl(hwxxDto.getHcsl());
						ckhwxxDto.setKclbj("0");
						modCkhwxxDtos.add(ckhwxxDto);
					}
				}
				list_mod.add(dto1);
			}
		}
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setIds(list_del);
		hwxxDto.setScry(dhxxDto.getScry());
		boolean result;
		if (!CollectionUtils.isEmpty(list_mod)){
			result=hwxxService.updateHwxxDtos(list_mod);
			if(!result)
				throw new BusinessException("msg","修改货物已处理数失败！");
		}
		if (!CollectionUtils.isEmpty(modCkhwxxDtos)){
			boolean modCkHwxxKcl = ckhwxxService.updateByWlidAndCkid(modCkhwxxDtos);
			if (!modCkHwxxKcl){
				throw new BusinessException("msg","更新仓库货物信息库存量失败！");
			}
		}
		if (!CollectionUtils.isEmpty(list_del)){
			result=hwxxService.deleteHwxxDtos(hwxxDto);
			if(!result)
				throw new BusinessException("msg","删除货物信息失败！！");
		}
		if (!CollectionUtils.isEmpty(htmxDtos)){
			result = htmxService.updateHtmxDtos(htmxDtos);
			if(!result) {
				throw new BusinessException("msg","更新合同明细数量失败！");
			}
		}
		result = delete(dhxxDto);
		if(!result)
			throw new BusinessException("msg", "删除到货信息失败!");
		return true;
	}
	/**
	 * 修改保存到货信息
	 * @param dhxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean modSaveArrivalGoods(DhxxDto dhxxDto) throws BusinessException {
		//文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(dhxxDto.getFjids())){
			for (int i = 0; i < dhxxDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(dhxxDto.getFjids().get(i),dhxxDto.getDhid());
				if(!saveFile)
					throw new BusinessException("ICOM00002","请购附件保存失败!");
			}
		}
		if (StringUtil.isNotBlank(dhxxDto.getSczlid())){
			DhxxDto dhxx = dao.getDtoById(dhxxDto.getDhid());
			HwxxDto dto = new HwxxDto();
			dto.setDhid(dhxxDto.getDhid());
			List<HwxxDto> dtoList = hwxxService.getDtoList(dto);
			SczlglDto sczlglDto = new SczlglDto();
			sczlglDto.setSczlid(dhxx.getSczlid());
			Double yysl = 0d;
			List<SczlmxDto> sczlmxDtos = new ArrayList<>();
			for (HwxxDto hwxxDto : dtoList) {
				yysl+=Double.parseDouble(hwxxDto.getDhsl());
				SczlmxDto sczlmxDto = new SczlmxDto();
				sczlmxDto.setSczlmxid(hwxxDto.getSczlmxid());
				sczlmxDto.setYysl(hwxxDto.getDhsl());
				sczlmxDto.setYyslbj("0");
				sczlmxDtos.add(sczlmxDto);
			}
			sczlglDto.setYysl(String.valueOf(yysl));
			sczlglDto.setYyslbj("0");
			boolean success = sczlglService.update(sczlglDto);
			if (!success)
				return false;
			success = sczlmxService.updateList(sczlmxDtos);
			if (!success)
				return false;
		}
		//保存到货信息表
		int result=dao.update(dhxxDto);
		if(result<=0)
			return false;
		if ("TH".equals(dhxxDto.getDhlxdm())){
			return true;
		}
		JcsjDto jcsjDto = jcsjService.getDtoById(dhxxDto.getRklb());
		if("0".equals(jcsjDto.getCskz1())&&!"CGHZ".equals(jcsjDto.getCskz3())) {
			//修改保存货物信息表
			List<HwxxDto> hwxxDtos = dhxxDto.getHwxxDtos();
			List<HwxxDto> modhwxxDtos = new ArrayList<>();//需要修改的货物
			if(!CollectionUtils.isEmpty(hwxxDtos)){
				//修改请购单产品注册号
				List<QgmxDto> qgmxDtos = new ArrayList<>();
				//修改合同单产品注册号
				List<HtmxDto> htmxDtos = new ArrayList<>();
				for (HwxxDto hwxxDto : hwxxDtos) {
					hwxxDto.setDhid(dhxxDto.getDhid());
					hwxxDto.setXgry(dhxxDto.getXgry());
					hwxxDto.setDhrq(dhxxDto.getDhrq());
					hwxxDto.setCkid(dhxxDto.getCkid());
					if (StringUtil.isNotBlank(hwxxDto.getScph())) {
						hwxxDto.setScph(hwxxDto.getScph().trim());
					}
					if (Double.parseDouble(hwxxDto.getDhsl()) <= 0) {
						throw new BusinessException("msg", hwxxDto.getWlbm() + "到货数量不得小于等于0，若该物料为到货，请删除！");
					}
					modhwxxDtos.add(hwxxDto);
					QgmxDto qgmxDto = new QgmxDto();
					qgmxDto.setQgmxid(hwxxDto.getQgmxid());
					qgmxDto.setCpzch(hwxxDto.getCpzch());
					qgmxDto.setXgry(dhxxDto.getLrry());
					qgmxDtos.add(qgmxDto);
					HtmxDto htmxDto = new HtmxDto();
					htmxDto.setHtmxid(hwxxDto.getHtmxid());
					htmxDto.setCpzch(hwxxDto.getCpzch());
					htmxDto.setXgry(dhxxDto.getXgry());
					htmxDtos.add(htmxDto);
				}
				htmxService.updateHtmxDtos(htmxDtos);
				qgmxService.updateCpzch(qgmxDtos);
			}
			return hwxxService.updateHwxxList(modhwxxDtos,dhxxDto);
		}else if ("0".equals(jcsjDto.getCskz1())&&"CGHZ".equals(jcsjDto.getCskz3())){
			List<HwxxDto> upHwxxDtos = new ArrayList<>();
			List<HwxxDto> addHwxxDtos = new ArrayList<>();
			List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class);
			if (!CollectionUtils.isEmpty(hwxxDtos)) {
				List<HwxxDto> hwxxListByDhid = hwxxService.getListByDhid(dhxxDto.getDhid());
				//先将红冲数量还原
				for (HwxxDto hwxxDto : hwxxListByDhid) {
					HwxxDto upHwxx = new HwxxDto();
					upHwxx.setHwid(hwxxDto.getChhwid());
					upHwxx.setByycls(hwxxDto.getDhsl());
					upHwxx.setXgry(dhxxDto.getXgry());
					upHwxx.setYclsbj("1");
					upHwxxDtos.add(upHwxx);
				}
				String ckid = hwxxDtos.get(0).getCkid();
				//重新修改
				for (HwxxDto hwxxDto : hwxxDtos) {
					if (!ckid.equals(hwxxDto.getCkid())){
						throw new BusinessException("msg","仓库不同不允许一起到货！");
					}
					//剩下的是删除的
					hwxxListByDhid.removeIf(next -> next.getHwid().equals(hwxxDto.getHwid()));
					if (StringUtil.isNotBlank(hwxxDto.getChhwid())) {
						HwxxDto upHwxx = new HwxxDto();
						upHwxx.setHwid(hwxxDto.getChhwid());
						upHwxx.setXgry(dhxxDto.getXgry());
						upHwxx.setByycls(hwxxDto.getDhsl());
						upHwxx.setYclsbj("0");
						upHwxxDtos.add(upHwxx);
						hwxxDto.setRkid(null);
						upHwxxDtos.add(hwxxDto);
					} else {
						if (Double.parseDouble(hwxxDto.getDhsl()) >= 0) {
							throw new BusinessException("msg", hwxxDto.getWlbm() + "红冲数量不得大于等于0！");
						}
						// if (StringUtil.isBlank(hwxxDto.getRkid())) {
						// 	throw new BusinessException("msg", hwxxDto.getWlbm() + "未入库，不允许在到货列表做采购红字，请在待处理列表进行处理，或入库之后再进行处理。！");
						// }
						hwxxDto.setRkid(null);
						// //判断库存量是不是小于不合格数量，如果小于，不允许红冲。
						// int flag = new BigDecimal(hwxxDto.getYhwkcl()).compareTo(new BigDecimal(hwxxDto.getYhwbhgsl()));
						// if (flag<0){
						// 	throw new BusinessException("msg",hwxxDto.getWlbm()+"库存量小于不合格数量,不允许做采购红冲单！");
						// }
						HwxxDto upHwxx = new HwxxDto();
						upHwxx.setHwid(hwxxDto.getHwid());
						upHwxx.setXgry(dhxxDto.getXgry());
						upHwxx.setByycls(hwxxDto.getDhsl());
						upHwxx.setYclsbj("0");
						upHwxxDtos.add(upHwxx);
						hwxxDto.setChhwid(hwxxDto.getHwid());
						hwxxDto.setHwid(StringUtil.generateUUID());
						hwxxDto.setDhid(dhxxDto.getDhid());
						hwxxDto.setLrry(dhxxDto.getLrry());
						// hwxxDto.setCkid(dhxxDto.getCkid());
						hwxxDto.setDhrq(dhxxDto.getDhrq());
						if (StringUtil.isNotBlank(hwxxDto.getScph())){
							hwxxDto.setScph(hwxxDto.getScph().trim());
						}
						hwxxDto.setZt(GoodsStatusEnum.GODDS_CHECK.getCode());
						addHwxxDtos.add(hwxxDto);
					}
				}
				if (!CollectionUtils.isEmpty(addHwxxDtos)) {
					boolean addHwxx = hwxxService.insertHwxxList(addHwxxDtos);
					if (!addHwxx)
						throw new BusinessException("msg", "新增货物信息失败！");
				}
				if (!CollectionUtils.isEmpty(upHwxxDtos)) {
					boolean upHwxx = hwxxService.updateHwxxDtos(upHwxxDtos);
					if (!upHwxx)
						throw new BusinessException("msg", "更新货物信息失败！");
				}
				if (!CollectionUtils.isEmpty(hwxxListByDhid)) {
					HwxxDto hwxxDto_del = new HwxxDto();
					List<String> ids = new ArrayList<>();
					hwxxDto_del.setScry(dhxxDto.getXgry());
					for (HwxxDto hwxxDto : hwxxListByDhid) {
						ids.add(hwxxDto.getHwid());
					}
					hwxxDto_del.setIds(ids);
					boolean del = hwxxService.deleteHwxxDtos(hwxxDto_del);
					if (!del) {
						throw new BusinessException("msg", "删除货物信息失败！！");
					}
				}
			}
		}else {
			if(StringUtil.isNotBlank(dhxxDto.getHwxx_json())) {
				List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getHwxx_json(), HwxxDto.class);
				List<SczlmxDto> sczlmxDtos = new ArrayList<>();
				Double yysl = 0d;
				for (HwxxDto hwxxDto : hwxxDtos) {
					hwxxDto.setDhid(dhxxDto.getDhid());
					hwxxDto.setXgry(dhxxDto.getXgry());
					hwxxDto.setDhrq(dhxxDto.getDhrq());
					hwxxDto.setCkid(dhxxDto.getCkid());
					if (StringUtil.isNotBlank(hwxxDto.getSbysid())){
						hwxxDto.setTkbj("1");
					}
					if (StringUtil.isNotBlank(hwxxDto.getScph())){
						hwxxDto.setScph(hwxxDto.getScph().trim());
					}
					if (Double.parseDouble(hwxxDto.getDhsl())<=0){
						throw new BusinessException("msg",hwxxDto.getWlbm()+"到货数量不得小于等于0，若该物料为到货，请删除！");
					}

					if (StringUtil.isNotBlank(dhxxDto.getSczlid())){
						SczlmxDto sczlmxDto = new SczlmxDto();
						sczlmxDto.setSczlmxid(hwxxDto.getSczlmxid());
						sczlmxDto.setYysl(hwxxDto.getDhsl());
						sczlmxDto.setYyslbj("1");
						sczlmxDtos.add(sczlmxDto);
						yysl+=Double.parseDouble(hwxxDto.getDhsl());
					}
				}
				boolean updateHwxx=hwxxService.updateHwxxList(hwxxDtos,dhxxDto);
				if(!updateHwxx)
					return false;
				if (StringUtil.isNotBlank(dhxxDto.getSczlid())){
					SczlglDto sczlglDto = new SczlglDto();
					sczlglDto.setSczlid(dhxxDto.getSczlid());
					sczlglDto.setYysl(String.valueOf(yysl));
					sczlglDto.setYyslbj("1");
					boolean success = sczlglService.update(sczlglDto);
					if (!success)
						return false;
					success = sczlmxService.updateList(sczlmxDtos);
					return success;
				}
			}							
		}	
		return true;
	}
	
	/**
	 * 自动生成到货单号
	 * @return
	 */
	public String generateDhdh() {
		String year = DateUtils.getCustomFomratCurrentDate("yyyy");
		String date = DateUtils.getCustomFomratCurrentDate("MMdd");
		String prefix = "DH" + "-" + year + "-" + date + "-";
		// 查询流水号
		String serial = dao.getDhdhSerial(prefix);
		return prefix + serial;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		DhxxDto dhxxDto = (DhxxDto) baseModel;
		dhxxDto.setXgry(operator.getYhid());
		DhxxDto dtoById = getDtoById(dhxxDto.getDhid());
		boolean isSuccess;
		if ("cghz".equals(dtoById.getRklbdm())&&!"1".equals(dtoById.getClbj())){
			isSuccess = purchaseMod(dhxxDto);
		}else{
			List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class);
			dhxxDto.setHwxxDtos(hwxxDtos);
			dhxxDto.setHtid(dhxxDto.getHtnbbhs());
			isSuccess = modSaveArrivalGoods(dhxxDto);
		}
		return isSuccess;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
//		String token = talkUtil.getToken();
		String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
		String ICOMM_SH00017 = xxglService.getMsg("ICOMM_SH00017");
		String ICOMM_ZJ00001 = xxglService.getMsg("ICOMM_ZJ00001");
		String ICOMM_ZJ00002 = xxglService.getMsg("ICOMM_ZJ00002");
		String ICOMM_ZJ00003 = xxglService.getMsg("ICOMM_ZJ00003");
		String ICOMM_ZJ00004 = xxglService.getMsg("ICOMM_ZJ00004");
		String ICOMM_ZJ00005 = xxglService.getMsg("ICOMM_ZJ00005");
		String ICOMM_ZJ00006 = xxglService.getMsg("ICOMM_ZJ00006");
		String ICOMM_ZJ00007 = xxglService.getMsg("ICOMM_ZJ00007");
		String ICOMM_ZJ00008 = xxglService.getMsg("ICOMM_ZJ00008");
		String ICOMM_ZJ00009 = xxglService.getMsg("ICOMM_ZJ00009");
		String ICOMM_ZJ00010 = xxglService.getMsg("ICOMM_ZJ00010");
		String ICOMM_ZJ00011 = xxglService.getMsg("ICOMM_ZJ00011");
		String ICOMM_ZJ00012 = xxglService.getMsg("ICOMM_ZJ00012");
		for (ShgcDto shgcDto :shgcList){
			DhxxDto dhxxDto=new DhxxDto();
			dhxxDto.setDhid(shgcDto.getYwid());
			dhxxDto.setZsxm(operator.getZsxm());
			dhxxDto.setXgry(operator.getYhid());
			DhxxDto dhxxDto_t=getDtoById(dhxxDto.getDhid());
			dhxxDto_t.setZsxm(operator.getZsxm());
			HwxxDto t_hwxxDto=new HwxxDto();
			t_hwxxDto.setDhid(dhxxDto_t.getDhid());
			List<HwxxDto> hwxxDtos = hwxxService.getDtoList(t_hwxxDto);
			if(AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {//审核退回
				dhxxDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())){
					for (int i = 0; i < shgcDto.getSpgwcyDtos().size(); i++){
						if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
							talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
									shgcDto.getSpgwcyDtos().get(i).getYhid(),
									xxglService.getMsg("ICOMM_SH00040"),xxglService.getMsg("ICOMM_SH00034",
											operator.getZsxm(),shgcDto.getShlbmc() ,
											dhxxDto_t.getDhdh(),shgcDto.getShxx_shyj(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
			}else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {//审核通过
				XtszDto xtszDto = xtszService.getDtoById("inspection.authority");
				if ("cghz".equals(dhxxDto_t.getRklbdm())){

					List<DhxxDto> dhxxDtos = getDtoList(dhxxDto);
					if(!"1".equals(systemreceiveflg) && StringUtil.isNotBlank(matridxdsflag)){
						if (!rdRecordService.determine_Entry(dhxxDto_t.getDhrq())){
							throw new BusinessException("ICOM99019","该月份已结账，不允许再录入U8数据，请修改单据日期!");
						}
						Map<String, Object> map = rdRecordService.pendingU8Data(dhxxDto,dhxxDtos);
						List<HwxxDto> hwxxDtoList = (List<HwxxDto>) map.get("hwxxDtoList");
						//更新货物信息中 关联u8货物明细字段 和 关联u8库存字段
						boolean result_h = hwxxService.updateHwxxDtos(hwxxDtoList);
						if(!result_h) {
							throw new BusinessException("msg","更新货物信息关联id失败！");
						}

						List<HwxxDto> hwxxDto_hs = (List<HwxxDto>) map.get("hwxxDto_hs");
						//更新库存关联id
						boolean result_hs = hwxxService.updateByWlidAndZsh(hwxxDto_hs);
						if(!result_hs) {
							throw new BusinessException("msg","更新货物信息关联id失败！");
						}
						dhxxDto = (DhxxDto) map.get("dhxxDto");
					}

					List<HtmxDto> htmxDtos = new ArrayList<>();
					for (DhxxDto dto : dhxxDtos) {
						HtmxDto htmxDto = new HtmxDto();
						htmxDto.setHtmxid(dto.getHtmxid());
						htmxDto.setBydhsl(dto.getDhsl());
						htmxDto.setDhslbj("1");
						htmxDto.setXgry(dhxxDto.getXgry());
						htmxDtos.add(htmxDto);
					}
					boolean result_ht = htmxService.updateHtmxDtos(htmxDtos);
					if(!result_ht) {
						throw new BusinessException("msg","更新合同明细数量失败！");
					}
					//如果是到货新增的红冲单，修改原货物信息kcl和仓库货物信息kcl
					if ("1".equals(dhxxDto_t.getClbj())){
						List<HwxxDto> modHwxxDtos = new ArrayList<>();
						List<CkhwxxDto> modCkhwxxDtos = new ArrayList<>();
						for (DhxxDto dto : dhxxDtos) {
							HwxxDto hwxxDto = new HwxxDto();
							hwxxDto.setHwid(dto.getChhwid());
							hwxxDto.setAddkcl(dto.getDhsl());
							modHwxxDtos.add(hwxxDto);
							CkhwxxDto ckhwxxDto = new CkhwxxDto();
							ckhwxxDto.setCkid(dto.getCkid());
							ckhwxxDto.setWlid(dto.getWlid());
							ckhwxxDto.setKcl(dto.getDhsl());
							ckhwxxDto.setKclbj("1");
							modCkhwxxDtos.add(ckhwxxDto);
						}
						boolean modHwxxKcl = hwxxService.updateHwxxDtos(modHwxxDtos);
						if (!modHwxxKcl){
							throw new BusinessException("msg","更新货物信息库存量失败！");
						}
						boolean modCkHwxxKcl = ckhwxxService.updateByWlidAndCkid(modCkhwxxDtos);
						if (!modCkHwxxKcl){
							throw new BusinessException("msg","更新仓库货物信息库存量失败！");
						}
					}

					dhxxDto.setZt(StatusEnum.CHECK_PASS.getCode());
					//发送钉钉消息
					if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())){
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
										shgcDto.getSpgwcyDtos().get(i).getYhid(),
										xxglService.getMsg("ICOMM_SH00006"),xxglService.getMsg("ICOMM_SH00052",
												operator.getZsxm(),shgcDto.getShlbmc() ,
												dhxxDto_t.getDhdh(),shgcDto.getShxx_shyj(),
												DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}
				}else {
					hwxxService.createArrivalGoodsBack(hwxxDtos,GoodsBackTypeEnums.ARRIVALGOODS_BACK.getCode(),dhxxDto.getXgry());//是否发起退回申请
					// List<String> qgmxids=new ArrayList<>();
					List<HwxxDto> yq_hwxxDtos = new ArrayList<>();//仪器
					List<HwxxDto> sj_hwxxDtos = new ArrayList<>();//试剂
					List<HwxxDto> rk_hwxxDtos = new ArrayList<>();//直接入库
					List<HwxxDto> sb_hwxxDtos = new ArrayList<>();//仪器
					List<HwxxDto> hc_hwxxDtos = new ArrayList<>();//耗材
					List<HwxxDto> qgtzlist=new ArrayList<>();//用于通知请购人员
					List<HwxxDto> hwxxDtos_lb = new ArrayList<>();
					//判断到货明细是否存在ABC类
					boolean isABC = false;
					if(!CollectionUtils.isEmpty(hwxxDtos)){
						for(HwxxDto hwxxDto:hwxxDtos) {
							HwxxDto hwxxDto_lb = new HwxxDto();
							//默认不是abc类
							hwxxDto_lb.setHwid(hwxxDto.getHwid());
							hwxxDto_lb.setLbbj("0");
//						//通过wlid出啊寻质量类别
//						if (StringUtil.isNotBlank(hwxxDto.getLbcsdm())){
//							if (hwxxDto.getLbcsdm().equals("M")){
//								hwxxDto_lb.setLbbj("1");
//							}else{
//								hwxxDto_lb.setLbbj("0");
//							}
//						}
							double sl=Double.parseDouble(StringUtils.isNotBlank(hwxxDto.getDhsl()) ? hwxxDto.getDhsl() : "0")
									-Double.parseDouble(StringUtils.isNotBlank(hwxxDto.getCythsl()) ? hwxxDto.getCythsl() : "0")
									-Double.parseDouble(StringUtils.isNotBlank(hwxxDto.getZjthsl()) ? hwxxDto.getZjthsl() : "0");
							hwxxDto.setSl(Double.toString(sl));
							if(!CollectionUtils.isEmpty(qgtzlist)) {
								boolean sfbtqg=false;//是否为不同的请购单,false代表是不同的请购单，需要添加
								for (HwxxDto dto : qgtzlist) {
									if (StringUtil.isNotBlank(dto.getQgid()) && StringUtil.isNotBlank(hwxxDto.getQgid())) {
										if (hwxxDto.getQgid().equals(dto.getQgid())) {
											sfbtqg = true;
										}
									}
								}
								if(!sfbtqg)
									qgtzlist.add(hwxxDto);
							}else {
								qgtzlist.add(hwxxDto);
							}
							// qgmxids.add(hwxxDto.getQgmxid());
							if (!"TH".equals(dhxxDto_t.getDhlxdm())){
								//根据物料质量类别进行状态修改
								//如果为1，走仪器质量审核，如果为2，走试剂质量审核。则更新货物信息里的状态为02 质量检验。
								if("1".equals(hwxxDto.getLbcskz1()) || "2".equals(hwxxDto.getLbcskz1())) {
									isABC = true;
									if(StringUtils.isNotBlank(hwxxDto.getDhsl())) {
										if(Double.parseDouble(hwxxDto.getDhsl())>0) {
											hwxxDto.setZt(GoodsStatusEnum.GODDS_QUALITY.getCode());
											//如果为abc设置类别标记为1
											hwxxDto_lb.setLbbj("1");
										}
									}
									//如果为1，根据钉钉消息管理里的 GOODS_IN_CHECK_INSTRUMENT (仪器检测通知）通知，需要到货单号，货物明细的个数
									if("1".equals(hwxxDto.getLbcskz1())) {
										yq_hwxxDtos.add(hwxxDto);
									}
									//如果为2根据钉钉消息管理里的 GOODS_IN_CHECK_REAGENT(试剂检测通知）通知，需要到货单号，货物明细的个数
									else if("2".equals(hwxxDto.getLbcskz1())) {
										sj_hwxxDtos.add(hwxxDto);
									}
									hwxxDto_lb.setLbcskz(hwxxDto.getLbcskz1());
								}else if("0".equals(hwxxDto.getLbcskz1()) || StringUtils.isBlank(hwxxDto.getLbcskz1())){
									//如果为空或者0，不走质量审核，则更新货物信息表里的状态 为03 入库。数量 = 到货数量 - 初验退回数量 - 质检退回数量。同时设置取样量=0，准备走入库审核
									//同时根据钉钉消息管理里的 GOODS_IN_STORAGE_NOCHECK(无需检测的入库通知）通知，需要到货单号，货物明细的个数
									if(StringUtils.isNotBlank(hwxxDto.getDhsl())) {
										if(Double.parseDouble(hwxxDto.getDhsl())>0)
											hwxxDto.setZt(GoodsStatusEnum.GODDS_STORE.getCode());
									}
									hwxxDto.setQyl("0");
									rk_hwxxDtos.add(hwxxDto);
								}else if("3".equals(hwxxDto.getLbcskz1()) || "5".equals(hwxxDto.getLbcskz1())) {
									if(StringUtils.isNotBlank(hwxxDto.getDhsl())) {
										if(Double.parseDouble(hwxxDto.getDhsl())>0) {
											isABC = true;
											hwxxDto.setZt(GoodsStatusEnum.GODDS_QUALITY.getCode());
											//如果为abc设置类别标记为1
											hwxxDto_lb.setLbbj("1");
										}
									}
									//是设备 并且不是其他入库的关联设备 发送设备检验消息
									if("3".equals(hwxxDto.getLbcskz1())&&StringUtil.isBlank(hwxxDto.getSbysid())) {
										sb_hwxxDtos.add(hwxxDto);
									}
									if("5".equals(hwxxDto.getLbcskz1())) {
										hc_hwxxDtos.add(hwxxDto);
									}
									hwxxDto_lb.setLbcskz(hwxxDto.getLbcskz1());
								}
								//其他入库审核通过 关联设备
								if (StringUtil.isNotBlank(hwxxDto.getSbysid())){
									hwxxDto.setZt(GoodsStatusEnum.GODDS_STORE.getCode());
									hwxxDto_lb.setZt(GoodsStatusEnum.GODDS_STORE.getCode());
								}
							}
							if ("TH".equals(dhxxDto_t.getDhlxdm())){
								hwxxDto_lb.setLbcskz("4");
								hwxxDto_lb.setLbbj("1");
								hwxxDto.setZt(GoodsStatusEnum.GODDS_QUALITY.getCode());
							}
							hwxxDtos_lb.add(hwxxDto_lb);
						}
						//更新类别标记
						boolean result_lb = hwxxService.updateHwxxDtos(hwxxDtos_lb);
						if(!result_lb) {
							throw new BusinessException("msg","更新货物信息类别标记失败！");
						}
						dhxxDto.setHwxxDtos(hwxxDtos);
						//更新请购明细的到货日期和到货数量
						boolean updateQg = false;
						boolean updateHt = false;
						List<HwxxDto> hwxxDtos_t=new ArrayList<>();
						for (HwxxDto hwxxDto : hwxxDtos) {
							if (StringUtil.isNotBlank(hwxxDto.getQgmxid())||StringUtil.isNotBlank(hwxxDto.getHtmxid())){
								if (CollectionUtils.isEmpty(hwxxDtos_t)) {
									hwxxDto.setSl_t(hwxxDto.getDhsl());
									hwxxDtos_t.add(hwxxDto);
								}else {
									boolean flag = true;
									for (HwxxDto hwxxDto1 : hwxxDtos_t) {
										if ((StringUtil.isNotBlank(hwxxDto1.getQgmxid()) && hwxxDto1.getQgmxid().equals(hwxxDto.getQgmxid()))||
												(StringUtil.isNotBlank(hwxxDto1.getHtmxid()) && hwxxDto1.getHtmxid().equals(hwxxDto.getHtmxid()))) {
											flag = false;
											BigDecimal dhsl = new BigDecimal(hwxxDto.getDhsl());
											BigDecimal dhsl_t = new BigDecimal(hwxxDto1.getSl_t());
											hwxxDto1.setSl_t(dhsl.add(dhsl_t).toString());
										}
									}
									if (flag){
										hwxxDto.setSl_t(hwxxDto.getDhsl());
										hwxxDtos_t.add(hwxxDto);
									}
								}
							}
						}

						hwxxService.updateQgmxs(hwxxDtos_t);
						//更新合同明细的到货日期和到货数量
						hwxxService.updateHtmxs(hwxxDtos_t);
						boolean resulr_zt = hwxxService.updateHwxxDtos(hwxxDtos);
						if(!resulr_zt) {
							throw new BusinessException("msg","更新货物信息失败！");
						}
						dhxxDto.setZsxm(operator.getZsxm());
						boolean flag = xtszDto!=null && "0".equals(xtszDto.getSzz());
						//判断是否存在ABC类，存在录入U8，不存在不录入U8
						if(isABC||flag) {
							//生成请验单号
							String qydh = generteQydh(StringUtil.isNotBlank(hwxxDtos.get(0).getKzcs())?hwxxDtos.get(0).getKzcs():dhxxDto_t.getBmcskz(),dhxxDto_t.getDhrq());

							if(!"OA".equals(dhxxDto_t.getDhlxdm())&&!"TH".equals(dhxxDto_t.getDhlxdm()) && !"1".equals(systemreceiveflg) && StringUtil.isNotBlank(matridxdsflag)){
								if (!rdRecordService.determine_Entry(dhxxDto_t.getDhrq())){
									throw new BusinessException("ICOM99019","该月份已结账，不允许再录入U8数据，请修改单据日期!");
								}
								if("0".equals(dhxxDto_t.getCskz1())) {
									List<DhxxDto> dhxxDtos = getDtoList(dhxxDto);
									Map<String, Object> map = rdRecordService.addU8RkData(dhxxDto,dhxxDtos,flag);

									List<HwxxDto> hwxxDtoList = (List<HwxxDto>) map.get("hwxxDtoList");
									//更新货物信息中 关联u8货物明细字段 和 关联u8库存字段
									boolean result_h = hwxxService.updateHwxxDtos(hwxxDtoList);
									if(!result_h) {
										throw new BusinessException("msg","更新货物信息关联id失败！");
									}

									List<HwxxDto> hwxxDto_hs = (List<HwxxDto>) map.get("hwxxDto_hs");
									//更新库存关联id
									boolean result_hs = hwxxService.updateByWlidAndZsh(hwxxDto_hs);
									if(!result_hs) {
										throw new BusinessException("msg","更新货物信息关联id失败！");
									}

									dhxxDto = (DhxxDto) map.get("dhxxDto");
								}else {
									List<DhxxDto> dhxxDtos = getDtoList(dhxxDto_t);
									//其他入库，成品入库
									Map<String,Object> map=rdRecordService.addU8CprkData(dhxxDto_t,dhxxDtos,flag);
									List<HwxxDto> hwxxDtoList = (List<HwxxDto>) map.get("hwxxDtoList");
									//更新货物信息中 关联u8货物明细字段 和 关联u8库存字段
									boolean result_h = hwxxService.updateHwxxDtos(hwxxDtoList);
									if(!result_h) {
										throw new BusinessException("msg","更新货物信息关联id失败！");
									}
									List<HwxxDto> hwxxDto_hs = (List<HwxxDto>) map.get("hwxxDto_hs");
									//更新库存关联id
									boolean result_hs = hwxxService.updateByWlidAndZsh(hwxxDto_hs);
									if(!result_hs) {
										throw new BusinessException("msg","更新货物信息关联id失败！");
									}
									dhxxDto = (DhxxDto) map.get("dhxxDto");
								}
							}
							dhxxDto.setQydh(qydh); //存入请验单号
						}
						dhxxDto.setZt(StatusEnum.CHECK_PASS.getCode());
						if(xtszDto==null || "1".equals(xtszDto.getSzz())){
							if ("TH".equals(dhxxDto_t.getDhlxdm())){
								List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(DingMessageType.GOODS_IN_CHECK_THORGH.getCode());
								sendQualityMessage(ddxxglDtos,dhxxDto_t,hwxxDtos_lb,ICOMM_ZJ00009,ICOMM_ZJ00010);//发送退货质检消息
							}
							if(!CollectionUtils.isEmpty(yq_hwxxDtos)) {
								List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(DingMessageType.GOODS_IN_CHECK_INSTRUMENT.getCode());
								sendQualityMessage(ddxxglDtos,dhxxDto_t,yq_hwxxDtos,ICOMM_ZJ00001,ICOMM_ZJ00002);//发送仪器质检消息
							}
							if(!CollectionUtils.isEmpty(sj_hwxxDtos)) {
								List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(DingMessageType.GOODS_IN_CHECK_REAGENT.getCode());
								sendQualityMessage(ddxxglDtos,dhxxDto_t,sj_hwxxDtos,ICOMM_ZJ00003,ICOMM_ZJ00004);//发送试剂质检消息
							}
							if(!CollectionUtils.isEmpty(hc_hwxxDtos)) {
								List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(DingMessageType.GOODS_IN_CHECK_CONSUMABLE.getCode());
								sendQualityMessage(ddxxglDtos,dhxxDto_t,hc_hwxxDtos,ICOMM_ZJ00011,ICOMM_ZJ00012);//发送耗材质检消息
							}
							if(!CollectionUtils.isEmpty(rk_hwxxDtos)) {
								List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(DingMessageType.GOODS_IN_STORAGE_NOCHECK.getCode());
								sendQualityMessage(ddxxglDtos,dhxxDto_t,rk_hwxxDtos,ICOMM_ZJ00005,ICOMM_ZJ00006);//发送无需质检的入库通知
							}
							if(!CollectionUtils.isEmpty(sb_hwxxDtos)) {
								List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(DingMessageType.GOODS_IN_CHECK_DEVICE.getCode());
								if(!CollectionUtils.isEmpty(ddxxglDtos)){
									for (DdxxglDto ddxxglDto : ddxxglDtos) {
										if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
											// 内网访问
											String internalbtn = applicationurl + urlPrefix
													+ "/ws/arrivalGoods/arrivalGoods/arrivalGoodsWsView?dhid=" + dhxxDto_t.getDhid();
											// 外网访问
										/*String external=externalurl + urlPrefix + "/ws/purchase/purchase/purchaseWsView?qgid="
												+ qgglDto_t.getQgid() + "&htid=" + htglDto_ht.getHtid() + "&sign="
												+ sign;*/
											List<BtnJsonList> btnJsonLists = new ArrayList<>();
											BtnJsonList btnJsonList = new BtnJsonList();
											btnJsonList.setTitle("详细");
											btnJsonList.setActionUrl(internalbtn);
											btnJsonLists.add(btnJsonList);
											talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),ddxxglDto.getYhid(),ddxxglDto.getYhm(), ddxxglDto.getDdid(), ICOMM_ZJ00007, StringUtil.replaceMsg(ICOMM_ZJ00008,
													dhxxDto_t.getDhdh(), dhxxDto_t.getCkmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
										}
									}
								}
//						sendQualityMessage(ddxxglDtos,dhxxDto_t,sb_hwxxDtos,ICOMM_ZJ00007,ICOMM_ZJ00008);//发送设备检验消息
							}
						}
					}
					//更新类别标记

					if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())){
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
										shgcDto.getSpgwcyDtos().get(i).getYhid(),
										xxglService.getMsg("ICOMM_SH00006"),xxglService.getMsg("ICOMM_SH00052",
												operator.getZsxm(),shgcDto.getShlbmc() ,
												dhxxDto_t.getDhdh(),shgcDto.getShxx_shyj(),
												DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}
					if(xtszDto!=null && "0".equals(xtszDto.getSzz())){//没有质检权限，直接入库
						List<HwxxDto> hwxxs = hwxxService.queryByDhidGroup(t_hwxxDto);
						List<CkhwxxDto> modCkhwxxDtos = new ArrayList<>();
						List<CkhwxxDto> addCkhwxxDtos = new ArrayList<>();
						if(hwxxs!=null && !hwxxs.isEmpty()){
							for (HwxxDto hwxxDto:hwxxs){
								CkhwxxDto ckhwxxDto = new CkhwxxDto();
								ckhwxxDto.setKcl(hwxxDto.getKcl());
								if(StringUtil.isNotBlank(hwxxDto.getCkhwid())){
									ckhwxxDto.setCkhwid(hwxxDto.getCkhwid());
									modCkhwxxDtos.add(ckhwxxDto);
								}else{
									ckhwxxDto.setCkhwid(StringUtil.generateUUID());
									ckhwxxDto.setWlid(hwxxDto.getWlid());
									ckhwxxDto.setCkid(hwxxDto.getCkid());
									ckhwxxDto.setYds("0");
									addCkhwxxDtos.add(ckhwxxDto);
								}
							}
						}
						boolean result = false;
						if(modCkhwxxDtos!=null && !modCkhwxxDtos.isEmpty()){
							result = ckhwxxService.updateCkhwxxs(modCkhwxxDtos);
							if(!result){
								throw new BusinessException("msg","更新仓库货物信息失败！");
							}
						}
						if(addCkhwxxDtos!=null && !addCkhwxxDtos.isEmpty()){
							result = ckhwxxService.insertCkhwxxs(addCkhwxxDtos);
							if(!result){
								throw new BusinessException("msg","新增仓库货物信息失败！");
							}
						}
						t_hwxxDto.setZt("99");
						t_hwxxDto.setJyqxbj("0");
						result = hwxxService.updateByDhid(t_hwxxDto);
						if(!result){
							throw new BusinessException("msg","更新货物信息状态失败！");
						}
						List<HwxxDto> hwxxDtoList = dhxxDto.getHwxxDtos();
						if(hwxxDtoList!=null && !hwxxDtoList.isEmpty()){
							for (HwxxDto hwxxDto:hwxxDtoList){
								hwxxDto.setZt("99");
							}
							dhxxDto.setHwxxDtos(hwxxDtoList);
						}
					}
				}
			}else {//新增审核
				
				dhxxDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				//发送钉钉消息
				if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())){
					try{
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){	
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								if ("cghz".equals(dhxxDto_t.getRklbdm())){
									talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(),ICOMM_SH00003,StringUtil.replaceMsg(ICOMM_SH00017,
											operator.getZsxm(),shgcDto.getShlbmc() ,dhxxDto_t.getDhdh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
								}else{
									//获取下一步审核人用户名
									//内网访问
									@SuppressWarnings("deprecation")
									String internalbtn="page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/auditpage/arrivalgoods/arrivalGoodsAuditView&type=1&ywzd=dhid&ywid="+dhxxDto_t.getDhid()+"&auditType="+AuditTypeEnum.AUDIT_GOODS_ARRIVAL.getCode()+"&urlPrefix="+urlPrefix,StandardCharsets.UTF_8);
									List<BtnJsonList> btnJsonLists = new ArrayList<>();
									BtnJsonList btnJsonList = new BtnJsonList();
									btnJsonList.setTitle("小程序");
									btnJsonList.setActionUrl(internalbtn);
									btnJsonLists.add(btnJsonList);
									talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(),ICOMM_SH00003,StringUtil.replaceMsg(ICOMM_SH00017,
											operator.getZsxm(),shgcDto.getShlbmc() ,dhxxDto_t.getDhdh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
								}
							}
						}
					}catch(Exception e){
						log.error(e.getMessage());
					}
				}
				//发送钉钉消息--取消审核人员
				if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())){
					try{
						int size = shgcDto.getNo_spgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,dhxxDto_t.getDhdh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						log.error(e.getMessage());
					}
				}
			}
			dhxxDto.setRklb(dhxxDto_t.getRklb());
			if ("TH".equals(dhxxDto_t.getDhlxdm())){
				dhxxDto.setDhlxdm("TH");
			}
			modSaveArrivalGoods(dhxxDto);
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		if (shgcList == null || shgcList.isEmpty()) {
			return true;
		}
		if (auditParam.isCancelOpe()) {
			// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String dhid = shgcDto.getYwid();
				DhxxDto dhxxDto = new DhxxDto();
				dhxxDto.setXgry(operator.getYhid());
				dhxxDto.setDhid(dhid);
				dhxxDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				boolean isSuccess;
				DhxxDto dtoById = getDtoById(dhxxDto.getDhid());
				if ("cghz".equals(dtoById.getRklbdm())&&"1".equals(dtoById.getClbj())){
					isSuccess = purchaseMod(dhxxDto);
				}else{
					List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class);
					dhxxDto.setHwxxDtos(hwxxDtos);
					dhxxDto.setHtid(dhxxDto.getHtnbbhs());
					isSuccess = modSaveArrivalGoods(dhxxDto);
				}
				if(!isSuccess)
					return false;
//				if("1".equals(configflg) && isSuccess==true) {
//					htglDto.setPrefixFlg(prefixFlg);
//					amqpTempl.convertAndSend("sys.igams", "sys.igams.htgl.update"+systemflg,JSONObject.toJSONString(htglDto));
//				}
			}
		} else {
			// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String wlid = shgcDto.getYwid();
				DhxxDto dhxxDto = new DhxxDto();
				dhxxDto.setXgry(operator.getYhid());
				dhxxDto.setDhid(wlid);
				dhxxDto.setZt(StatusEnum.CHECK_NO.getCode());
				boolean isSuccess = update(dhxxDto);
				if(!isSuccess)
					return false;
//				if("1".equals(configflg) && isSuccess==true) {
//					htglDto.setPrefixFlg(prefixFlg);
//					amqpTempl.convertAndSend("sys.igams", "sys.igams.htgl.update"+systemflg,JSONObject.toJSONString(htglDto));
//				}
			}
		}
		return true;
	}


    /**
     * 导出
     * 
     * @param dhxxDto
     * @return
     */
    @Override
    public int getCountForSearchExp(DhxxDto dhxxDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(dhxxDto);
    }
    
    /**
     * 根据搜索条件获取导出信息
     * 
     * @param params
     * @return
     */
    public List<DhxxDto> getListForSearchExp(Map<String, Object> params) {
        DhxxDto dhxxDto = (DhxxDto) params.get("entryData");
        queryJoinFlagExport(params, dhxxDto);
        return dao.getListForSearchExp(dhxxDto);
    }

    /**
     * 根据选择信息获取导出信息
     * 
     * @param params
     * @return
     */
    public List<DhxxDto> getListForSelectExp(Map<String, Object> params) {
        DhxxDto dhxxDto = (DhxxDto) params.get("entryData");
        queryJoinFlagExport(params, dhxxDto);
        return dao.getListForSelectExp(dhxxDto);
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, DhxxDto dhxxDto) {
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
        dhxxDto.setSqlParam(sqlcs);
    }
    
	/**
	 * 检查能否删除到货信息
	 * @param dhxxDto
	 * @return
	 */
	public Map<String,Object> checkscqx(DhxxDto dhxxDto){
		Map<String,Object> map = new HashMap<>();
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setIds(dhxxDto.getIds());
		List<HwxxDto> hwxxlist=hwxxService.getListByDhids(hwxxDto);
		boolean result = true;
		if(!CollectionUtils.isEmpty(hwxxlist)){
			for(HwxxDto t_hwxxDto : hwxxlist) {
				if(StringUtils.isNotBlank(t_hwxxDto.getDhjyid())) {
					map.put("status", "fail");
					map.put("message", "有检验数据，删除失败！");
				}
				if(StringUtils.isNotBlank(t_hwxxDto.getRkid())) {
					map.put("status", "fail");
					map.put("message", "有入库数据，删除失败！");
				}
				if(StringUtil.isNotBlank(t_hwxxDto.getJyqxbj()) && "0".equals(t_hwxxDto.getJyqxbj())){
					BigDecimal kcl = new BigDecimal(StringUtil.isNotBlank(t_hwxxDto.getKcl())?t_hwxxDto.getKcl():"0");
					BigDecimal dhsl = new BigDecimal(StringUtil.isNotBlank(t_hwxxDto.getDhsl())?t_hwxxDto.getDhsl():"0")
							.subtract(new BigDecimal(StringUtil.isNotBlank(t_hwxxDto.getCythsl())?t_hwxxDto.getCythsl():"0"));
					if(kcl.compareTo(dhsl)!=0){
						result = false;
					}
				}
			}
		}
		if(!result){
			map.put("status", "fail");
			map.put("message", "库存量和到货数量不一致，删除失败！");
		}
		return map;
	}
	
	/**
	 * 发送质检通知
	 * @param ddxxglDtos
	 * @param dhxxDto
	 * @param hwxxDtos
	 * @param xxbt 消息标题
	 * @param xxnr 消息内容
	 */
	private void sendQualityMessage(List<DdxxglDto> ddxxglDtos,DhxxDto dhxxDto,List<HwxxDto> hwxxDtos,String xxbt,String xxnr) {
//		String token = talkUtil.getToken();
		if(!CollectionUtils.isEmpty(ddxxglDtos)){
			for (DdxxglDto ddxxglDto : ddxxglDtos) {
				if (StringUtil.isNotBlank(ddxxglDto.getYhm())) {
					//获取下一步审核人用户名
					//内网访问
					@SuppressWarnings("deprecation")
					String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/viewpage/arrivalgoods/arrivalGoodsView&type=1&dhid=" + dhxxDto.getDhid() + "&lbcskz1=" + hwxxDtos.get(0).getLbcskz1() + "&urlPrefix=" + urlPrefix,StandardCharsets.UTF_8);
					List<BtnJsonList> btnJsonLists = new ArrayList<>();
					BtnJsonList btnJsonList = new BtnJsonList();
					btnJsonList.setTitle("小程序");
					btnJsonList.setActionUrl(internalbtn);
					btnJsonLists.add(btnJsonList);
					talkUtil.sendCardMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), xxbt, StringUtil.replaceMsg(xxnr,
							dhxxDto.getDhdh(), dhxxDto.getDhrq(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
				}
			}
		}
	}

	/**
	 * 删除到货单
	 * @param dhxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteDhxx(DhxxDto dhxxDto) throws BusinessException{
		//根据ids查询要删除的到货信息
		List<DhxxDto> dhxxDtos = dao.getListBydhid(dhxxDto);
		//存要更新的请购明细信息
		List<QgmxDto> qgmxDtos = new ArrayList<>();
		//存要更新的合同明细信息
		List<HtmxDto> htmxDtos = new ArrayList<>();
		//存要更新的货物信息
		StringBuilder ids = new StringBuilder();
		List<CkhwxxDto> ckhwxxDtos = new ArrayList<>();
		for (DhxxDto dhxxDto_t : dhxxDtos) {
			if (StringUtil.isNotBlank(dhxxDto_t.getSczlid())){
				HwxxDto dto = new HwxxDto();
				dto.setDhid(dhxxDto_t.getDhid());
				List<HwxxDto> dtoList = hwxxService.getDtoList(dto);
				SczlglDto sczlglDto = new SczlglDto();
				sczlglDto.setSczlid(dhxxDto_t.getSczlid());
				Double yysl = 0d;
				List<SczlmxDto> sczlmxDtos = new ArrayList<>();
				for (HwxxDto hwxxDto : dtoList) {
					yysl+=Double.parseDouble(hwxxDto.getDhsl());
					SczlmxDto sczlmxDto = new SczlmxDto();
					sczlmxDto.setSczlmxid(hwxxDto.getSczlmxid());
					sczlmxDto.setYysl(hwxxDto.getDhsl());
					sczlmxDto.setYyslbj("0");
					sczlmxDtos.add(sczlmxDto);
				}
				sczlglDto.setYysl(String.valueOf(yysl));
				sczlglDto.setYyslbj("0");
				boolean success = sczlglService.update(sczlglDto);
				if (!success)
					throw new BusinessException("msg","更新生产指令信息失败！");
				success = sczlmxService.updateList(sczlmxDtos);
				if (!success)
					throw new BusinessException("msg","更新生产指令明细信息失败！");
			}

			QgmxDto qgmxDto = new QgmxDto();
			HtmxDto htmxDto = new HtmxDto();
			//判断是否审核成功
			if("80".equals(dhxxDto_t.getZt())){
				if(StringUtil.isNotBlank(dhxxDto_t.getQgmxid())) {
					qgmxDto.setDhrq(dhxxDto_t.getQgmxdhrq());
					qgmxDto.setQgmxid(dhxxDto_t.getQgmxid());
					BigDecimal qgdhsl = new BigDecimal(StringUtil.isNotBlank(dhxxDto_t.getQgmxdhsl())?dhxxDto_t.getQgmxdhsl():"0").subtract(new BigDecimal(StringUtil.isNotBlank(dhxxDto_t.getDhsl())?dhxxDto_t.getDhsl():"0"));
					//更新请购明细到货信息
					if(new BigDecimal(0).compareTo(qgdhsl)==0) {
						qgmxDto.setDhrq(null);
					}
					qgmxDto.setDhsl(qgdhsl.toString());	
					qgmxDto.setXgry(dhxxDto.getScry());
					if(StringUtil.isNotBlank(dhxxDto_t.getQgmxdhdhgl())) {
						String qgmxdhdhgl = dhxxDto_t.getQgmxdhdhgl().replace(","+dhxxDto_t.getDhdh(), "");
						qgmxDto.setDhdhgl(qgmxdhdhgl);
					}
					boolean isadd = true;
					if (!CollectionUtils.isEmpty(qgmxDtos)){
						for (QgmxDto dto : qgmxDtos) {
							if (dto.getQgmxid().equals(dhxxDto_t.getQgmxid())){
								isadd  = false;
								BigDecimal subtractdhsl = new BigDecimal(dto.getDhsl()).subtract(new BigDecimal(StringUtil.isNotBlank(dhxxDto_t.getDhsl()) ? dhxxDto_t.getDhsl() : "0"));
								dto.setDhsl(subtractdhsl.toString());
							}
						}
					}
					if (isadd){
						qgmxDtos.add(qgmxDto);
					}
				}
				if(StringUtil.isNotBlank(dhxxDto_t.getHtmxid())) {
					htmxDto.setDhrq(dhxxDto_t.getHtmxdhrq());				
					htmxDto.setHtmxid(dhxxDto_t.getHtmxid());				
					BigDecimal htdhsl = new BigDecimal(StringUtil.isNotBlank(dhxxDto_t.getHtmxdhsl())?dhxxDto_t.getHtmxdhsl():"0").subtract(new BigDecimal(StringUtil.isNotBlank(dhxxDto_t.getDhsl())?dhxxDto_t.getDhsl():"0"));			
					//更新合同明细到货信息
					if(new BigDecimal(0).compareTo(htdhsl)==0) {
						htmxDto.setDhrq(null);
					}
					htmxDto.setDhsl(htdhsl.toString());
					htmxDto.setXgry(dhxxDto.getScry());
					if(StringUtil.isNotBlank(dhxxDto_t.getHtmxdhdhgl())) {
						String htmxdhdhgl = dhxxDto_t.getHtmxdhdhgl().replace(","+dhxxDto_t.getDhdh(), "");
						htmxDto.setDhdhgl(htmxdhdhgl);
					}
					boolean isadd = true;
					if (!CollectionUtils.isEmpty(htmxDtos)){
						for (HtmxDto dto : htmxDtos) {
							if (dto.getHtmxid().equals(dhxxDto_t.getHtmxid())){
								isadd  = false;
								BigDecimal subtractdhsl = new BigDecimal(dto.getDhsl()).subtract(new BigDecimal(StringUtil.isNotBlank(dhxxDto_t.getDhsl()) ? dhxxDto_t.getDhsl() : "0"));
								dto.setDhsl(subtractdhsl.toString());
							}
						}
					}
					if (isadd){
						htmxDtos.add(htmxDto);
					}
				}
				HwxxDto hwxxDto = new HwxxDto();
				hwxxDto.setDhid(dhxxDto_t.getDhid());
				hwxxDto.setJyqxbj("0");
				List<HwxxDto> hwxxDtos = hwxxService.queryByDhidGroup(hwxxDto);

				if(hwxxDtos!=null && !hwxxDtos.isEmpty()){
					for(HwxxDto hwxxDto1:hwxxDtos){
						if(StringUtil.isNotBlank(hwxxDto1.getCkhwid())){
							CkhwxxDto ckhwxxDto = new CkhwxxDto();
							ckhwxxDto.setCkhwid(hwxxDto1.getCkhwid());
							ckhwxxDto.setKcl(hwxxDto1.getDhsl());
							ckhwxxDtos.add(ckhwxxDto);
						}
					}
				}
			}
			//存入到货信息
			ids.append(",").append(dhxxDto_t.getHwid());
		}
		if(ckhwxxDtos!=null && !ckhwxxDtos.isEmpty()){
			boolean ckhwxx_boolean = ckhwxxService.updateCkhwxxs(ckhwxxDtos);
			if(!ckhwxx_boolean){
				throw new BusinessException("msg", "更新仓库货物信息失败!");
			}
		}
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setScry(dhxxDto.getScry());
		ids = new StringBuilder(ids.substring(1));
		hwxxDto.setIds(ids.toString());
		RkcglDto rkcglDto = new RkcglDto();
		rkcglDto.setIds(ids.toString());
		//删除入库车数据
		rkcglService.delete(rkcglDto);
		if(!CollectionUtils.isEmpty(qgmxDtos)){
			boolean result_q = qgmxService.qgmxDtoModDhxx(qgmxDtos);
			if(!result_q)
				throw new BusinessException("msg", "更新请购明细信息失败!");
		}
		if(!CollectionUtils.isEmpty(htmxDtos)){
			boolean result_h = htmxService.htmxDtoModDhxx(htmxDtos);
			if(!result_h)
				throw new BusinessException("msg", "更新合同明细信息失败!");
		}
		boolean result_w = hwxxService.deleteHwxxDtos(hwxxDto);
		if(!result_w)
			throw new BusinessException("msg", "更新货物信息失败!");
		boolean result = delete(dhxxDto);
		if(!result)
			throw new BusinessException("msg", "删除到货信息失败!");
		shgcService.deleteByYwids(dhxxDto.getIds());
		return true;
	}
	
	/**
	 * 根据到货ids获取到货信息（共通页面）
	 * @param dhxxDto
	 * @return
	 */
	public List<DhxxDto> getCommonDtoListByDhids(DhxxDto dhxxDto){
		return dao.getCommonDtoListByDhids(dhxxDto);
	}

	/**
	 * 根据到货id更新确认人员/时间/标记信息
	 */
	@Override
	public boolean updateConfirmDhxx(DhxxDto dhxxDto) {
		return dao.updateConfirmDhxx(dhxxDto);
	}

	
	/**
	 * 	到货高级修改
	 * @param dhxxDto
	 * @return
	 */
	@Override
	public boolean advancedmodsave(DhxxDto dhxxDto) throws BusinessException {
		//更改主表信息
		boolean result = update(dhxxDto);
		
		rdRecordService.updateDhxxupdate(dhxxDto);
		if(!result) {
			throw new BusinessException("msg", "更新到货信息失败!");
		}
		//判断货物当前状态
		return true;
	}

	/**
	 * 查找到货列表的借用总量和归还总量
	 */
	@Override
	public List<DhxxDto> getJyzlAndGhzlList() {
		return dao.getJyzlAndGhzlList();
	}

	@Override
	public List<DhxxDto> getPagedDtoListDingTalk(DhxxDto dhxxDto) {
		return dao.getPagedDtoListDingTalk(dhxxDto);
	}

	/**
	 * 请验单号
	 */
	public String generteQydh(String jg_cskz1,String date) {
		// 生成规则: IT-2021-0823-01    部门参数扩展-年-月日-流水号(两位) 。
		String[] split = date.split("-");
		date=split[0]+"-"+split[1]+split[2];
		String prefix = "L-" + date + "-";
		//查询流水号
		String serial = dao.generteQydh(prefix);
		return prefix+serial;
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
		DhxxDto dhxxDto = new DhxxDto();
		dhxxDto.setIds(ids);
		List<DhxxDto> dtoList = dao.getListBydhid(dhxxDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(DhxxDto dto:dtoList){
				list.add(dto.getDhid());
			}
		}
		map.put("list",list);
		return map;
	}

	/**
	 * 出入库统计
	 * @param dhxxDto
	 * @return
	 */
	public List<DhxxDto> getCountStatistics(DhxxDto dhxxDto){
		return dao.getCountStatistics(dhxxDto);
	}
}
