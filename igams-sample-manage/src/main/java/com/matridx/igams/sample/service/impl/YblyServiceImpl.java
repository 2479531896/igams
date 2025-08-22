package com.matridx.igams.sample.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.entities.LshkDto;
import com.matridx.igams.common.enums.SerialNumTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ILshkService;
import com.matridx.igams.sample.dao.entities.SbkxglDto;
import com.matridx.igams.sample.dao.entities.YbglDto;
import com.matridx.igams.sample.dao.entities.YbglModel;
import com.matridx.igams.sample.dao.entities.YblyDto;
import com.matridx.igams.sample.dao.entities.YblyModel;
import com.matridx.igams.sample.dao.post.IYbglDao;
import com.matridx.igams.sample.dao.post.IYblyDao;
import com.matridx.igams.sample.service.svcinterface.ISbkxglService;
import com.matridx.igams.sample.service.svcinterface.IYblyService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class YblyServiceImpl extends BaseBasicServiceImpl<YblyDto, YblyModel, IYblyDao> implements IYblyService{
	
	private final Logger logger = LoggerFactory.getLogger(YblyServiceImpl.class);
	
	@Autowired
	IYbglDao ybglDao;
	
	@Autowired
	ISbkxglService sbkxglService;
	
	@Autowired
	ILshkService lshkservice;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insert(YblyModel yblyModel){
		String lyid = StringUtil.generateUUID();
		yblyModel.setLyid(lyid);
		int result = dao.insert(yblyModel);
		return result > 0;
	}
	
	/**
	 * 分页查询
	 */
	@Override
	public List<YblyDto> getPagedDtoList(YblyDto yblyDto){
		List<YblyDto> yblyDtos = dao.getPagedDtoList(yblyDto);

        return dao.selectByDtos(yblyDtos);
	}
	
	/**
	 * 根据页面的入库类型，同时插入标本来源信息和标本管理信息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insert(YblyDto yblyDto,YbglModel ybglModel){
		int result = 0;
		try{
			String lyid = StringUtil.generateUUID();
			yblyDto.setLyid(lyid);
			//生成内部用流水号
			LshkDto lshkDto = new LshkDto();
			lshkDto.setYblx(yblyDto.getYblxdm());
			lshkDto.setYbbhrq(yblyDto.getYbbhrq());
			String yblybh = lshkservice.doMakeSerNum(SerialNumTypeEnum.SAMPSRC_CODE, lshkDto);
			yblyDto.setLybh(yblybh);
			//正式库的标本都算已处理完
			if("00".equals(yblyDto.getRklx()))
				yblyDto.setZt(StatusEnum.CHECK_PASS.getCode());
			else
				yblyDto.setZt(StatusEnum.CHECK_NO.getCode());
			result = dao.insert(yblyDto);
			//入正式库的情况，要同时保存到标本管理系统中
			if(result > 0 && "00".equals(yblyDto.getRklx())){
				ybglModel.setLyid(lyid);
				//保存原数量
				ybglModel.setYsl(ybglModel.getSl());
				//标本来源类型固定成 0 外部
				ybglModel.setYblylx("0");
				ybglModel.setYblx(yblyDto.getYblx());
				//状态为为提交
				ybglModel.setZt(StatusEnum.CHECK_PASS.getCode());

				//若标本类型为企参盘，循环添加
				List<String> ybbhs= new ArrayList<>();
				if(("1").equals(yblyDto.getSfqcp())) {
					for(int i=0;i<ybglModel.getIds().size();i++) {
						String ybid = StringUtil.generateUUID();
						ybglModel.setYbid(ybid);
						ybglModel.setSl("1");
						ybglModel.setHzid(ybglModel.getIds().get(i));
						//生成内部用流水号
						lshkDto = new LshkDto();
						lshkDto.setYbly(yblyDto.getLyd());
						lshkDto.setYblx(yblyDto.getYblxdm());
						lshkDto.setYbbhrq(yblyDto.getYbbhrq());
						String ybbh = lshkservice.doMakeSerNum(SerialNumTypeEnum.SAMP_CODE, lshkDto);
						ybglModel.setYbbh(ybbh);
						ybbhs.add(ybbh);
						ybglModel.setYbbhs(ybbhs);
						ybglDao.insert(ybglModel);
						//同时更新标本空闲表
						SbkxglDto sbkxglDto = new SbkxglDto();
						sbkxglDto.setSbid(ybglModel.getHzid());
						sbkxglDto.setFsbid(ybglModel.getChtid());
						sbkxglDto.setYblx(ybglModel.getYblx());
						sbkxglDto.setCfqswz(ybglModel.getQswz());
						sbkxglDto.setCfjswz(ybglModel.getJswz());
						sbkxglDto.setXgry(yblyDto.getLrry());
						sbkxglService.updateLessSbkx(sbkxglDto);
					}
				}else {
					String ybid = StringUtil.generateUUID();
					ybglModel.setYbid(ybid);
					//生成内部用流水号
					lshkDto = new LshkDto();
					lshkDto.setYbly(yblyDto.getLyd());
					lshkDto.setYblx(yblyDto.getYblxdm());
					lshkDto.setYbbhrq(yblyDto.getYbbhrq());
					String ybbh = lshkservice.doMakeSerNum(SerialNumTypeEnum.SAMP_CODE, lshkDto);
					ybglModel.setYbbh(ybbh);
					ybbhs.add(ybbh);
					ybglModel.setYbbhs(ybbhs);
					ybglDao.insert(ybglModel);
					//同时更新标本空闲表
					SbkxglDto sbkxglDto = new SbkxglDto();
					sbkxglDto.setSbid(ybglModel.getHzid());
					sbkxglDto.setFsbid(ybglModel.getChtid());
					sbkxglDto.setYblx(ybglModel.getYblx());
					sbkxglDto.setCfqswz(ybglModel.getQswz());
					sbkxglDto.setCfjswz(ybglModel.getJswz());
					sbkxglDto.setXgry(yblyDto.getLrry());
					sbkxglService.updateLessSbkx(sbkxglDto);
				}
				
			}
		}catch(BusinessException e){
			logger.error(e.getMessage());
		}
		return result > 0;
	}
	
	/**
	 * 根据页面的入库类型，同时更新标本来源信息和标本管理信息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean update(YblyDto yblyDto,YbglModel ybglModel){
		int result = 0;
		try{
			result = dao.update(yblyDto);
			//入正式库的情况，要同时保存到标本管理系统中
			if(result > 0 && "00".equals(yblyDto.getRklx())){
				//保存原数量
				ybglModel.setYsl(ybglModel.getSl());
				
				ybglDao.update(ybglModel);
				
				//同时更新标本空闲表
				sbkxglService.recountSbkx(ybglModel.getHzid());
			}else if(result > 0 && "01".equals(yblyDto.getRklx())){
				ybglDao.delete(ybglModel);
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return result > 0;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public boolean delete(YblyModel yblyModel){
		int result = dao.delete(yblyModel);
		List<String> ids = yblyModel.getIds();
		if(ids != null && !ids.isEmpty()){
            for (String id : ids) {
                if (StringUtil.isNotBlank(id)) {
                    YbglDto ybglDto = new YbglDto();
                    ybglDto.setLyid(id);
                    ybglDto.setScry(yblyModel.getScry());
                    ybglDao.delete(ybglDto);
                }
            }
		}
		return result > 0;
	}

	/**
	 * 根据标本Ids修改状态
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateZtByIds(YblyDto yblyDto) {
		// TODO Auto-generated method stub
		int result = dao.updateZtByIds(yblyDto);
        return result != 0;
    }
}
