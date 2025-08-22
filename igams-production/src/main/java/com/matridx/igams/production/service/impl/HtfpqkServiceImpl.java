package com.matridx.igams.production.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.production.dao.entities.HtfpmxDto;
import com.matridx.igams.production.service.svcinterface.IHtfpmxService;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.HtfpqkDto;
import com.matridx.igams.production.dao.entities.HtfpqkModel;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.post.IHtfpqkDao;
import com.matridx.igams.production.service.svcinterface.IHtfpqkService;
import com.matridx.igams.production.service.svcinterface.IHtglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.util.CollectionUtils;

@Service
public class HtfpqkServiceImpl extends BaseBasicServiceImpl<HtfpqkDto, HtfpqkModel, IHtfpqkDao> implements IHtfpqkService{

	@Autowired
	IHtglService htglService;

	@Autowired
	DingTalkUtil talkUtil;

	@Autowired
	IFjcfbService fjcfbService;

	@Autowired
	IHtfpmxService htfpmxService;
	
	/**
	 * 合同发票新增保存
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertFpqk(HtfpqkDto htfpqkDto) throws BusinessException {
		// 获取合同信息
		HtglDto htglDto = htglService.getDtoById(htfpqkDto.getHtid());
		htfpqkDto.setLrry(htfpqkDto.getLrry());
		htfpqkDto.setHtid(htglDto.getHtid());
		htfpqkDto.setHtfpid(StringUtil.generateUUID());
		int countFk = dao.insert(htfpqkDto);
		if (countFk == 0)
			return false;
		List<HtfpmxDto> htfpmxlist=new ArrayList<>();
		if(!CollectionUtils.isEmpty(htfpqkDto.getIds())){
			for(int i=0;i<htfpqkDto.getIds().size();i++){
				HtfpmxDto htfpmxDto=new HtfpmxDto();
				htfpmxDto.setFpmxid(StringUtil.generateUUID());
				htfpmxDto.setHtfpid(htfpqkDto.getHtfpid());
				htfpmxDto.setHtmxid(htfpqkDto.getIds().get(i));
				htfpmxDto.setFpje(htfpqkDto.getFpje());
				htfpmxlist.add(htfpmxDto);
			}
			htfpmxService.insertList(htfpmxlist);
		}
		//获取合同已有发票
		BigDecimal yyfpnum;
		if(htglDto.getFpje()==null){
			yyfpnum= new BigDecimal("0");
		}else{
			yyfpnum= new BigDecimal(htglDto.getFpje());
		}
		//获取发票金额
		BigDecimal fpjenum = new BigDecimal(htfpqkDto.getFpje());
		// 存入新的合同发票金额
		BigDecimal result = yyfpnum.add(fpjenum).setScale(2, RoundingMode.HALF_UP);
		if(StringUtils.isNotBlank(htglDto.getYfje())){
			// 已付金额
			BigDecimal yfje = new BigDecimal(htglDto.getYfje());
			// 总金额
			BigDecimal zje = new BigDecimal(htglDto.getZje());
			if(yfje.compareTo(zje) > -1 && result.compareTo(zje) > -1){
				htglDto.setWcbj("1");
			}
		}
		htglDto.setFpje(result.toString());
		htglDto.setXgry(htfpqkDto.getLrry());
		int count = htglService.updateContract(htglDto);
		//文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(htfpqkDto.getFjids())){
			for (int i = 0; i < htfpqkDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(htfpqkDto.getFjids().get(i),htfpqkDto.getHtfpid());
				if(!saveFile)
					throw new BusinessException("ICOM00002","请购附件保存失败!");
			}
		}
		return count != 0;
	}
	
	/**
	 * 查看合同发票情况列表
	 */
	public List<HtfpqkDto> getFpqkList(HtfpqkDto htfpqkDto){
		return dao.getFpqkList(htfpqkDto);
	}
	
	/**
	 * 合同发票情况修改
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateFpqk(HtfpqkDto htfpqkDto)  throws BusinessException {
		// 获取合同信息
		HtglDto htglDto = htglService.getDtoById(htfpqkDto.getHtid());
		// 获取合同已有发票
		BigDecimal yyfpnum = new BigDecimal(htglDto.getFpje());
		//查询原发票信息
		HtfpqkDto old_htfpqkDto=dao.getDtoById(htfpqkDto.getHtfpid());
		//获取原发票金额
		BigDecimal oldfpnum= new BigDecimal(old_htfpqkDto.getFpje());
		//获取现修改金额
		BigDecimal nowfpnum= new BigDecimal(htfpqkDto.getFpje());
		BigDecimal result = yyfpnum.subtract(oldfpnum).add(nowfpnum).setScale(2, RoundingMode.HALF_UP);
		if(StringUtils.isNotBlank(htglDto.getYfje())){
			// 已付金额
			BigDecimal yfje = new BigDecimal(htglDto.getYfje());
			// 总金额
			BigDecimal zje = new BigDecimal(htglDto.getZje());
			if(yfje.compareTo(zje) > -1 && result.compareTo(zje) > -1){
				htglDto.setWcbj("1");
			}else{
				htglDto.setWcbj("0");
			}
		}
		htglDto.setFpje(result.toString());
		htglDto.setXgry(htfpqkDto.getXgry());
		int count = htglService.updateContract(htglDto);
		//文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(htfpqkDto.getFjids())){
			for (int i = 0; i < htfpqkDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(htfpqkDto.getFjids().get(i),htfpqkDto.getHtfpid());
				if(!saveFile)
					throw new BusinessException("ICOM00002","请购附件保存失败!");
			}
		}
		if (count == 0)
			return false;
		return dao.updateFpqk(htfpqkDto);
	}
	
	/**
	 * 删除合同发票情况
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteFpqk(HtfpqkDto htfpqkDto) {
		// 获取合同信息
		HtglDto htglDto = htglService.getDtoById(htfpqkDto.getHtid());
		// 获取合同发票金额
		BigDecimal yyfpnum = new BigDecimal(htglDto.getFpje());
		//查询原发票信息
		HtfpqkDto old_htfpqkDto=dao.getDtoById(htfpqkDto.getHtfpid());
		// 获取发票金额
		BigDecimal fpjenum = new BigDecimal(old_htfpqkDto.getFpje());
		// 存入新的合同发票金额
		BigDecimal result = yyfpnum.subtract(fpjenum).setScale(2, RoundingMode.HALF_UP);
		if(StringUtils.isNotBlank(htglDto.getYfje())){
			// 已付金额
			BigDecimal yfje = new BigDecimal(htglDto.getYfje());
			// 总金额
			BigDecimal zje = new BigDecimal(htglDto.getZje());
			if(yfje.compareTo(zje) > -1 && result.compareTo(zje) > -1){
				htglDto.setWcbj("1");
			}else{
				htglDto.setWcbj("0");
			}
		}
		htglDto.setFpje(result.toString());
		htglDto.setXgry(htfpqkDto.getLrry());
		int count = htglService.updateContract(htglDto);
		if (count == 0)
			return false;
		boolean delFpqk= dao.deleteFpqk(htfpqkDto);
		if(!delFpqk)
			return false;
		HtfpmxDto htfpmxDto=new HtfpmxDto();
		htfpmxDto.setHtfpid(htfpqkDto.getHtfpid());
		return htfpmxService.delete(htfpmxDto);
	}

	/**
	 * 发送合同发票信息
	 */
	public boolean sendInvoiceMessage(List<DdxxglDto> ddxxglDtos, HtfpqkDto htfpqkDto, String xxbt, String xxnr) {
		HtglDto htglDto = new HtglDto();
		htglDto.setHtid(htfpqkDto.getHtid());
		List<HtfpqkDto> htfpqkDtoList = dao.getFpqk(htfpqkDto);
		if (htfpqkDtoList.size() == 1){
			String htnbbh = htglService.getDto(htglDto).getHtnbbh();
//				String token = talkUtil.getToken();
			if(!CollectionUtils.isEmpty(ddxxglDtos)) {
				for (DdxxglDto ddxxglDto : ddxxglDtos) {
					if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
						talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), xxbt, StringUtil.replaceMsg(xxnr,
								htnbbh, htfpqkDto.getFphm(), htfpqkDtoList.get(0).getFpzlmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
					}
				}
			}
		}
		return true;
	}

	/**
	 * 根据合同ID,发票号码，发票种类查找
	 */
	@Override
	public List<HtfpqkDto> getFpqk(HtfpqkDto htfpqkDto) {
		return  dao.getFpqk(htfpqkDto);
	}




}
