package com.matridx.igams.common.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.RyczxgDto;
import com.matridx.igams.common.dao.entities.RyczxgModel;
import com.matridx.igams.common.dao.post.IRyczxgDao;
import com.matridx.igams.common.service.svcinterface.IRyczxgService;

@Service
public class RyczxgServiceImpl extends BaseBasicServiceImpl<RyczxgDto, RyczxgModel, IRyczxgDao> implements IRyczxgService{

	/**
	 * 新增或修改人员操作习惯
	 */
	@Override
	public boolean insertOrUpdate(RyczxgDto ryczxgDto) {
		// TODO Auto-generated method stub
		RyczxgDto t_ryczxgDto = dao.getDto(ryczxgDto);
		if(t_ryczxgDto != null){
			//修改
			t_ryczxgDto.setCzpl(String.valueOf(Integer.parseInt(t_ryczxgDto.getCzpl())+1));
			int result = dao.update(t_ryczxgDto);
			return result != 0;
		}else{
			//新增
			ryczxgDto.setCzpl("1");
			int result = dao.insert(ryczxgDto);
			return result != 0;
		}
	}

}
