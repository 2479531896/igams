package com.matridx.igams.sample.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.sample.dao.entities.SbglDto;
import com.matridx.igams.sample.dao.entities.SbkxglDto;
import com.matridx.igams.sample.dao.entities.SbkxglModel;
import com.matridx.igams.sample.dao.entities.YbglDto;
import com.matridx.igams.sample.dao.post.ISbglDao;
import com.matridx.igams.sample.dao.post.ISbkxglDao;
import com.matridx.igams.sample.dao.post.IYbglDao;
import com.matridx.igams.sample.service.svcinterface.ISbkxglService;

@Service
public class SbkxglServiceImpl extends BaseBasicServiceImpl<SbkxglDto, SbkxglModel, ISbkxglDao> implements ISbkxglService{

	private final Logger logger = LoggerFactory.getLogger(SbkxglServiceImpl.class);
	
	@Autowired
	ISbglDao sbglDao;
	
	@Autowired
	IYbglDao ybglDao;
	
	/**
	 * 根据所占用的位置，重新根据空闲表数据
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateLessSbkx(SbkxglDto sbkxglDto){
		
		List<SbkxglModel> kxList = dao.getModelByWz(sbkxglDto);
		
		if(kxList == null || kxList.size() != 1)
			return false;
		
		SbkxglModel t_SbkxglModel = kxList.get(0);
		//存放位置
		int cfqswz = Integer.parseInt(sbkxglDto.getCfqswz());
		int cfjswz = Integer.parseInt(sbkxglDto.getCfjswz());
		//空闲情况
		int qswz = Integer.parseInt(t_SbkxglModel.getQswz());
		int kxs = Integer.parseInt(t_SbkxglModel.getKxs());
		//从头开始存放
		if(cfqswz == qswz){
			//如果到最后结束
			if(cfjswz == (qswz + kxs -1)){
				//删除空闲信息
				t_SbkxglModel.setScry(sbkxglDto.getXgry());
				dao.delete(t_SbkxglModel);
			}
			//如果存到到中间
			else{
				//更新空闲信息，从结束后位置开始
				t_SbkxglModel.setQswz(String.valueOf(cfjswz+1));
				t_SbkxglModel.setKxs(String.valueOf(qswz + kxs - cfjswz -1));
				t_SbkxglModel.setXgry(sbkxglDto.getXgry());
				dao.update(t_SbkxglModel);
			}
		}
		//从中间开始存放
		else if(cfqswz > qswz){
			//如果到最后结束
			if(cfjswz == (qswz + kxs -1)){
				//更新空闲信息，到结束前位置
				t_SbkxglModel.setKxs(String.valueOf(kxs - cfqswz + qswz));
				t_SbkxglModel.setXgry(sbkxglDto.getXgry());
				dao.update(t_SbkxglModel);
			}
			//如果存到到中间
			else{
				/*//更新空闲信息，到结束前位置
				t_SbkxglModel.setKxs(String.valueOf(kxs - cfqswz + qswz));
				t_SbkxglModel.setXgry(sbkxglDto.getXgry());
				dao.update(t_SbkxglModel);
				//同时新增一个空闲位置，放在最后
				SbkxglModel new_SbkxglModel = new SbkxglModel();
				new_SbkxglModel.setSbid(t_SbkxglModel.getSbid());
				new_SbkxglModel.setFsbid(t_SbkxglModel.getFsbid());
				new_SbkxglModel.setQswz(String.valueOf(cfjswz+1));
				new_SbkxglModel.setKxs(String.valueOf(qswz + kxs -1 - cfjswz ));
				new_SbkxglModel.setYblx(t_SbkxglModel.getYblx());
				new_SbkxglModel.setLrry(sbkxglDto.getXgry());
				dao.insertNew(new_SbkxglModel);*/
				recountSbkx(t_SbkxglModel.getSbid());
			}
		}
		
		return true;
	}
	

	/**
	 * 重新更新空闲表数据
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean recountSbkx(String sbid){
		
		dao.deleteById(sbid);
		//获取设备信息
		SbglDto sbglDto = new SbglDto();
		sbglDto.setSbid(sbid);
		sbglDto = sbglDao.getSbYbDto(sbglDto);
		sbglDto.setLrry("sql");
		//获取标本信息
		YbglDto ybglDto = new YbglDto();
		ybglDto.setHzid(sbid);
		ybglDto.setSortName("qswz");
		ybglDto.setSortOrder("asc");
		List<YbglDto> ybglDtos = ybglDao.getDtoList(ybglDto);
		
		List<SbkxglDto> sbkxglDtos = getKxPositionList(Integer.parseInt(sbglDto.getCfs()),ybglDtos,sbglDto);
		
		if(sbkxglDtos!=null && !sbkxglDtos.isEmpty()){

            for (SbkxglDto sbkxglDto : sbkxglDtos) {
                dao.insert(sbkxglDto);
            }
		}
		
		return true;
	}
	
	/**
	 * 根据总数转换成位置信息，并根据空闲信息进行标记
	 */
	private List<SbkxglDto> getKxPositionList(int total,List<YbglDto> ybList,SbglDto samp){
		try{
			List<String> t_yblist= changeNumList(ybList);
			List<SbkxglDto> result = new ArrayList<>();
			SbkxglDto t_SbkxglDto = new SbkxglDto();
			int i_start = 1;
			int xh =1;
			for(int i=1;i<=total;i++){
				
				if(t_yblist!=null&& !t_yblist.isEmpty()){
					int i_yb = Integer.parseInt(t_yblist.get(0));
					if(i_yb == i){
						if(i_start != i){
							t_SbkxglDto.setQswz(String.valueOf(i_start));
							t_SbkxglDto.setKxs(String.valueOf(i - i_start));
							t_SbkxglDto.setSbid(samp.getSbid());
							t_SbkxglDto.setFsbid(samp.getFsbid());
							t_SbkxglDto.setYblx(samp.getYblx());
							t_SbkxglDto.setLrry(samp.getLrry());
							t_SbkxglDto.setXh(String.valueOf(xh));
							xh ++;
							result.add(t_SbkxglDto);
							
							t_SbkxglDto = new SbkxglDto();
						}
						i_start = i + 1; 
						t_yblist.remove(0);
					}else if(i_yb < i){
						while(true){
							if(t_yblist==null||t_yblist.isEmpty())
								break;
							int t_yb = Integer.parseInt(t_yblist.get(0));
							if(t_yb > i)
								break;
							t_yblist.remove(0);
						}
					}
				}
			}
			if(i_start <= total){
				t_SbkxglDto.setQswz(String.valueOf(i_start));
				t_SbkxglDto.setKxs(String.valueOf(total - i_start + 1));
				t_SbkxglDto.setSbid(samp.getSbid());
				t_SbkxglDto.setFsbid(samp.getFsbid());
				t_SbkxglDto.setYblx(samp.getYblx());
				t_SbkxglDto.setLrry(samp.getLrry());
				t_SbkxglDto.setXh(String.valueOf(xh));
				
				result.add(t_SbkxglDto);
			}
			return result;
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 根据空闲信息转换成空闲格子列表
	 */
	private List<String> changeNumList(List<YbglDto> ybList){
		if(ybList!=null){
			List<String> result = new ArrayList<>();
			for (YbglDto ybDto : ybList) {
				int i_qswz = Integer.parseInt(ybDto.getQswz());
				int i_sl = Integer.parseInt(ybDto.getSl());
				for(int i=0;i<i_sl;i++){
					result.add(String.valueOf(i_qswz + i));
				}
			}
			return result;
		}
		return null;
	}
}
