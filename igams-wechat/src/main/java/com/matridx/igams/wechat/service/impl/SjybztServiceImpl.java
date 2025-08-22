package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjybztDto;
import com.matridx.igams.wechat.dao.entities.SjybztModel;
import com.matridx.igams.wechat.dao.post.ISjybztDao;
import com.matridx.igams.wechat.service.svcinterface.ISjybztService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SjybztServiceImpl extends BaseBasicServiceImpl<SjybztDto, SjybztModel, ISjybztDao> implements ISjybztService{

	/**
	 * 新增标本状态
	 * @param sjxxDto
	 * @return
	 */
	@Override
	/**
	 * 新增标本状态
	 * @param sjybztDtos
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertYbzt(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		dao.deleteById(sjxxDto.getSjid());
		List<String> strzts=sjxxDto.getZts();
		if(strzts!=null && strzts.size()>0) {
			List<SjybztDto> sjybztDtos= new ArrayList<>();
			for (int i = 0; i < strzts.size(); i++){
				SjybztDto sjybztDto=new SjybztDto();
				sjybztDto.setSjid(sjxxDto.getSjid());
				sjybztDto.setXh(String.valueOf(i+1));
				sjybztDto.setZt(strzts.get(i));
				sjybztDtos.add(sjybztDto);
			}
			dao.insertYbzt(sjybztDtos);
		}
		return true;
	}


	/**
	 * 通过送检id查询标本状态
	 * @param sjid
	 * @return
	 */
	@Override
	public List<String> getZtBysjid(String sjid){
		// TODO Auto-generated method stub
		return dao.getZtBysjid(sjid);
	}

	/**
	 * 标本质量合格率统计SQL
	 * @param sjybztDto
	 * @return
	 */
	@Override
	public SjybztDto getPercentOfPass(SjybztDto sjybztDto) {
		return dao.getPercentOfPass(sjybztDto);
	}

	/**
	 * 对不合格的标本进行统计，按照标本类型区分开，统计每个类型的不合格率
	 * @param sjybztDto
	 * @return
	 */
	@Override
	public List<SjybztDto> getPercentOfUnPass(SjybztDto sjybztDto) {
		return dao.getPercentOfUnPass(sjybztDto);
	}

}
