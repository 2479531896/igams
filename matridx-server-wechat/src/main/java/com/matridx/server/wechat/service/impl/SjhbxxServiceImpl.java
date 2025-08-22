package com.matridx.server.wechat.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.HbsfbzDto;
import com.matridx.server.wechat.dao.entities.SjhbxxDto;
import com.matridx.server.wechat.dao.entities.SjhbxxModel;
import com.matridx.server.wechat.dao.entities.SjjcxmDto;
import com.matridx.server.wechat.dao.entities.SjjcxmModel;
import com.matridx.server.wechat.dao.post.ISjhbxxDao;
import com.matridx.server.wechat.service.svcinterface.IHbsfbzService;
import com.matridx.server.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class SjhbxxServiceImpl extends BaseBasicServiceImpl<SjhbxxDto, SjhbxxModel, ISjhbxxDao> implements ISjhbxxService{
	
	@Autowired
	private IHbsfbzService hbsfbzservice;
	
	private Logger log = LoggerFactory.getLogger(SjhbxxServiceImpl.class);
	
	@Override
	public SjhbxxDto getDto(SjhbxxDto sjhbxxDto){
		
		SjhbxxDto t_sjhbxxDto = dao.getDto(sjhbxxDto);
		
		if(t_sjhbxxDto != null) {
			HbsfbzDto t_hbsfbzDto = new HbsfbzDto();
			t_hbsfbzDto.setHbid(t_sjhbxxDto.getHbid());
			
			List<SjjcxmDto> sjjcxmDtos = sjhbxxDto.getSjxms();
			
			if(sjjcxmDtos != null) {
				for(SjjcxmModel sjjcxmModel:sjjcxmDtos) {
					
					t_hbsfbzDto.setXm(sjjcxmModel.getJcxmid());
					HbsfbzDto hbsfbzDto = hbsfbzservice.getDto(t_hbsfbzDto);
					
					if(hbsfbzDto != null) {
						t_sjhbxxDto.setSfbz(addsf(hbsfbzDto.getSfbz(),t_sjhbxxDto.getSfbz()));
					}else {
						HbsfbzDto d_hbsfbzDto = hbsfbzservice.getDefaultDto(t_hbsfbzDto);
						if(d_hbsfbzDto != null) {
							t_sjhbxxDto.setSfbz(addsf(d_hbsfbzDto.getSfbz(),t_sjhbxxDto.getSfbz()));
						}
					}
				}
			}
		}else{
			t_sjhbxxDto = new SjhbxxDto();
			List<SjjcxmDto> sjjcxmDtos = sjhbxxDto.getSjxms();
			
			if(sjjcxmDtos != null) {
				for(SjjcxmModel sjjcxmModel:sjjcxmDtos) {
					HbsfbzDto t_hbsfbzDto = new HbsfbzDto();
					t_hbsfbzDto.setXm(sjjcxmModel.getJcxmid());
					HbsfbzDto d_hbsfbzDto = hbsfbzservice.getDefaultDto(t_hbsfbzDto);
					if(d_hbsfbzDto != null) {
						t_sjhbxxDto.setSfbz(addsf(d_hbsfbzDto.getSfbz(),t_sjhbxxDto.getSfbz()));
					}
				}
			}
		}
		
		return t_sjhbxxDto;
	}
	
	/**
	 * 合计计算收费
	 * @param prestr
	 * @param aftstr
	 * @return
	 */
	private String addsf(String prestr,String aftstr) {
		BigDecimal big1 = new BigDecimal("0");
		BigDecimal big2 = new BigDecimal("0");
		if(StringUtil.isNotBlank(prestr)) {
			try {
				big1 = new BigDecimal(prestr);
			}catch(Exception e) {
				
			}
		}
		if(StringUtil.isNotBlank(aftstr)) {
			try {
				big2 = new BigDecimal(aftstr);
			}catch(Exception e) {
				
			}
		}
		
		return big1.add(big2).toString();
	}

	/**
	 * 保存本地删除信息至微信服务器
	 * @param sjhbxxDto
	 * @throws BusinessException 
	 */
	@Override
	public void receiveDelPartner(SjhbxxDto sjhbxxDto) throws BusinessException {
		// TODO Auto-generated method stub
		boolean isSuccess = dao.deletepartner(sjhbxxDto);
		if(!isSuccess){
			log.error("合作伙伴信息保存未成功！");
			throw new BusinessException("","合作伙伴信息保存未成功！");
		}
	}

	/**
	 * 保存本地新增信息至微信服务器
	 * @param sjhbxxDto
	 * @throws BusinessException 
	 */
	@Override
	public void receiveAddPartner(SjhbxxDto sjhbxxDto) throws BusinessException {
		// TODO Auto-generated method stub
		// 86同步85
		int result = dao.insertDto(sjhbxxDto);
		if(result == 0){
			log.error("合作伙伴信息保存未成功！");
			throw new BusinessException("","合作伙伴信息保存未成功！");
		}
		boolean success = insertsfbz(sjhbxxDto);
		if (!success){
			log.error("伙伴收费标准保存未成功！");
			throw new BusinessException("","伙伴收费标准保存未成功！");
		}
	}

	/**
	 * 添加收费标准
	 * @param sjhbxxDto
	 * @return
	 */
	public boolean insertsfbz(SjhbxxDto sjhbxxDto) {
		List<HbsfbzDto> hbsfbzDto=sjhbxxDto.getHbsfbzs();
		if (hbsfbzDto != null){
			for (int i = hbsfbzDto.size()-1; i >=0; i--) {
				if( StringUtil.isNotBlank(hbsfbzDto.get(i).getSfbz()) || StringUtil.isNotBlank(hbsfbzDto.get(i).getTqje())  || StringUtil.isNotBlank(hbsfbzDto.get(i).getXm()) || StringUtil.isNotBlank(hbsfbzDto.get(i).getZxm()) || StringUtil.isNotBlank(hbsfbzDto.get(i).getKsrq()) || StringUtil.isNotBlank(hbsfbzDto.get(i).getJsrq()) ){
					hbsfbzDto.get(i).setHbid(sjhbxxDto.getHbid());
				}else {
					hbsfbzDto.remove(i);
                }
			}
			if(hbsfbzDto.size() > 0) {
                return hbsfbzservice.insertsfbz(hbsfbzDto);
			}
		}
		return true;
	}

	/**
	 * 保存本地更新信息至微信服务器
	 * @param sjhbxxDto
	 * @throws BusinessException 
	 */
	@Override
	public void receiveModPartner(SjhbxxDto sjhbxxDto) throws BusinessException {
		// TODO Auto-generated method stub
		int result = dao.updatePageEvent(sjhbxxDto);
		if(result == 0){
			log.error("合作伙伴信息保存未成功！");
			throw new BusinessException("","合作伙伴信息保存未成功！");
		}
		//删除原有收费标准再重新添加
		hbsfbzservice.deleteById(sjhbxxDto.getHbid());
		boolean success = insertsfbz(sjhbxxDto);
		if (!success){
			log.error("伙伴收费标准保存未成功！");
			throw new BusinessException("","伙伴收费标准保存未成功！");
		}

	}

	/**
	 * 通过hbid查询 合作伙伴
	 * @param list
	 * @return
	 */
	@Override
	public List<String> getHbmcByHbid(List<String> list){
		// TODO Auto-generated method stub
		return dao.getHbmcByHbid(list);
	}

	/**
	 * 查询伙伴为'无'的用户信息
	 * @return
	 */
	@Override
	public List<SjhbxxDto> getXtyhByHbmc() {
		// TODO Auto-generated method stub
		return dao.getXtyhByHbmc();
	}
	/**
	 * 启用合作伙伴
	 * @param sjhbxxDto
	 * @return
	 */
	@Override
	public boolean enablepartner(SjhbxxDto sjhbxxDto) {
		return dao.enablepartner(sjhbxxDto);
	}
	/**
	 * 停用合作伙伴
	 * @param sjhbxxDto
	 * @return
	 */
	@Override
	public boolean disablepartner(SjhbxxDto sjhbxxDto) {
		return dao.disablepartner(sjhbxxDto);
	}
}
