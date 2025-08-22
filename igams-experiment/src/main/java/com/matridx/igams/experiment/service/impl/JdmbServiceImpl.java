package com.matridx.igams.experiment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.JdmbDto;
import com.matridx.igams.experiment.dao.entities.JdmbModel;
import com.matridx.igams.experiment.dao.entities.MbglDto;
import com.matridx.igams.experiment.dao.entities.RwmbDto;
import com.matridx.igams.experiment.dao.post.IJdmbDao;
import com.matridx.igams.experiment.dao.post.IRwmbDao;
import com.matridx.igams.experiment.service.svcinterface.IJdmbService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class JdmbServiceImpl extends BaseBasicServiceImpl<JdmbDto, JdmbModel, IJdmbDao> implements IJdmbService{
	@Autowired
	private IRwmbDao rwmbdao;
	/** 
	 * 插入阶段模板信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(JdmbDto jdmbDto){
		jdmbDto.setJdid(StringUtil.generateUUID());
		int maxxh=dao.getXh(jdmbDto);
		maxxh++; 
		jdmbDto.setXh(maxxh+"");
		int result = dao.insert(jdmbDto);
		return result != 0;
	}

	/**
	 * 查询默认阶段模板信息
	 */
	@Override
	public List<JdmbDto> selectDefaultStage() {
		// TODO Auto-generated method stub
		return dao.selectDefaultStage();
	}

	/**
	 * 根据模板ID新增阶段模板
	 */
	@Override
	public boolean insertByMbid(List<JdmbDto> jdmbDtos) {
		// TODO Auto-generated method stub
		return dao.insertByMbid(jdmbDtos);
	}

	/**
	 * 根据当前模板id查询所有阶段
	 */
	@Override
	public List<JdmbDto> selectStageByID(MbglDto mbglDto){
		// TODO Auto-generated method stub
		List<JdmbDto> jdmblist=dao.selectStageByID(mbglDto);
		if (jdmblist.size()>0){
			for (JdmbDto jdmbDto : jdmblist) {
				List<RwmbDto> rwmblist = rwmbdao.getrwmbbyid(jdmbDto.getJdid());
				for (RwmbDto rwmbDto : rwmblist) {
					int zrwcount = rwmbdao.getcount(rwmbDto.getMbid());
					rwmbDto.setZrwcount(zrwcount + "");
				}
				jdmbDto.setRwmbmc(rwmblist);
			}
		}
		return jdmblist;
	}


	/**
	 * 修改阶段名称
	 */
	@Override
	public boolean updatejdmc(JdmbDto jdmbDto){
		// TODO Auto-generated method stub
		return dao.updatejdmc(jdmbDto);
	}
	
	/**
	 * 查询默认阶段模板
	 */
	@Override
	public List<JdmbDto> selectmrjdmb(){
		return dao.selectmrjdmb();
	}

	/**
	 * 添加对应模板id的默认阶段模板
	 */
	@Override
	public boolean insertdefaultJdmb(JdmbDto jdmbDto) {
		return dao.insertdefaultJdmb(jdmbDto);
	}
	
	/**
	 * 阶段模板排序
	 */
	@Override
	public boolean stageSort(JdmbDto jdmbDto) {
		// TODO Auto-generated method stub
		int result = dao.stageSort(jdmbDto);
		return result != 0;
	}

	/**
	 * 根据模板ID查询阶段模板信息
	 */
	@Override
	public List<JdmbDto> selectJdmbByMbid(String mbid) {
		// TODO Auto-generated method stub
		return dao.selectJdmbByMbid(mbid);
	}
}
