package com.matridx.server.wechat.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.SjzmjgDto;
import com.matridx.server.wechat.dao.entities.SjzmjgModel;
import com.matridx.server.wechat.dao.post.ISjzmjgDao;
import com.matridx.server.wechat.service.svcinterface.ISjzmjgService;

@Service
public class SjzmjgServiceImpl extends BaseBasicServiceImpl<SjzmjgDto, SjzmjgModel, ISjzmjgDao> implements ISjzmjgService{
	
	private Logger log = LoggerFactory.getLogger(SjzmjgServiceImpl.class);
	
	/**
	 * list新增送检自免结果
	 * @param list
	 * @return
	 */
	@Override
	public boolean insertSjzmjg(List<SjzmjgDto> list) {
		// TODO Auto-generated method stub
		return dao.insertSjzmjg(list);
	}

	/**
	 * 根据送检ID删除送检自免结果
	 * @param sjzmjgDto
	 * @return
	 */
	@Override
	public boolean deleteSjzmjg(SjzmjgDto sjzmjgDto) {
		// TODO Auto-generated method stub
		return dao.deleteSjzmjg(sjzmjgDto);
	}

	/**
	 * 同步修改送检自免结果
	 * @param sjzmjgDtos
	 * @throws BusinessException 
	 */
	@Override
	public void receiveModSelfresult(List<SjzmjgDto> sjzmjgDtos) throws BusinessException {
		// TODO Auto-generated method stub
		if(sjzmjgDtos != null && sjzmjgDtos.size() > 0) {
			//新增自免结果之前先删除之前的自免结果
			SjzmjgDto sjzyjgDto=new SjzmjgDto();
			sjzyjgDto.setSjid(sjzmjgDtos.get(0).getSjid());
			sjzyjgDto.setScry(sjzmjgDtos.get(0).getXgry());
			dao.deleteSjzmjg(sjzyjgDto);
			//新增自免结果信息
			boolean isSuccess = dao.insertSjzmjg(sjzmjgDtos);
			if(!isSuccess){
				log.error("送检自免结果保存未成功！");
				throw new BusinessException("","送检自免结果保存未成功！");
			}
		}
	}

}
