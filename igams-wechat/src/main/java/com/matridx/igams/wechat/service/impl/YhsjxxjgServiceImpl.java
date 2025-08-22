package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjxxjgDto;
import com.matridx.igams.wechat.dao.entities.YhsjxxjgDto;
import com.matridx.igams.wechat.dao.entities.YhsjxxjgModel;
import com.matridx.igams.wechat.dao.post.IYhsjxxjgDao;
import com.matridx.igams.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.igams.wechat.service.svcinterface.IYhsjxxjgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class YhsjxxjgServiceImpl extends BaseBasicServiceImpl<YhsjxxjgDto, YhsjxxjgModel, IYhsjxxjgDao> implements IYhsjxxjgService{

	@Autowired
	ISjxxjgService sjxxjgService;
	
	/**
	 * 批量删除用户送检详细审核结果
	 * @param yhsjxxjgDto
	 * @return
	 */
	@Override
	public boolean deleteByYhsjxxjgDto(YhsjxxjgDto yhsjxxjgDto) {
		// TODO Auto-generated method stub
		int result = dao.deleteByYhsjxxjgDto(yhsjxxjgDto);
        return result != 0;
    }

	/**
	 * 批量新增用户送检详细审核结果
	 * @param yhsjxxjgDtos
	 * @return
	 */
	@Override
	public boolean insertByYhjxxjgDtos(List<YhsjxxjgDto> yhsjxxjgDtos) {
		// TODO Auto-generated method stub
		return dao.insertByYhsjxxjgDtos(yhsjxxjgDtos);
	}

	/**
	 * 医生修改送检详细结果信息
	 * @param yhsjxxjgDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean modAnalysis(YhsjxxjgDto yhsjxxjgDto) {
		// TODO Auto-generated method stub
		yhsjxxjgDto.setXgry(yhsjxxjgDto.getYhid());
		yhsjxxjgDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
		//判断是否已有用户结果信息
		List<YhsjxxjgDto> yhsjxxjgDtos = dao.selectByYhidAndSjid(yhsjxxjgDto);
		if(yhsjxxjgDtos != null && yhsjxxjgDtos.size() > 0){
			//直接修改
			dao.update(yhsjxxjgDto);
		}else{
			//查询送检详细结果信息
			List<SjxxjgDto> sjxxjgDtos = sjxxjgService.selectBySjid(yhsjxxjgDto.getSjid());
			if(sjxxjgDtos != null && sjxxjgDtos.size() > 0){
				yhsjxxjgDtos = new ArrayList<>();
				for (int i = 0; i < sjxxjgDtos.size(); i++) {
					String jsonString = JSONObject.toJSONString(sjxxjgDtos.get(i));
					JSONObject parseObject = JSONObject.parseObject(jsonString);
					YhsjxxjgDto t_yhsjxxjgDto = JSONObject.toJavaObject(parseObject, YhsjxxjgDto.class);
					t_yhsjxxjgDto.setGllx("wechat");
					t_yhsjxxjgDto.setZt(StatusEnum.CHECK_NO.getCode());
					t_yhsjxxjgDto.setYhid(yhsjxxjgDto.getYhid());
					yhsjxxjgDtos.add(t_yhsjxxjgDto);
				}
				//批量新增
				dao.insertByYhsjxxjgDtos(yhsjxxjgDtos);
				//修改当前数据
				dao.update(yhsjxxjgDto);
			}else{
				return false;
			}
		}
		return true;
	}

	/**
	 * 查询详细结果信息
	 * @param sjxxjgDto
	 * @return
	 */
	@Override
	public List<SjxxjgDto> getTreeAnalysis(SjxxjgDto sjxxjgDto) {
		// TODO Auto-generated method stub
		//先从用户送检详细结果表获取，为空从详细结果表获取
		List<SjxxjgDto> sjxxjgDtos = dao.getTreeAnalysis(sjxxjgDto);
		if(sjxxjgDtos == null || sjxxjgDtos.size() == 0){
			sjxxjgDtos = sjxxjgService.getTreeAnalysis(sjxxjgDto);
		}
		return sjxxjgDtos;
	}

	/**
	 * 获取详细结果信息
	 * @param sjxxjgDto
	 * @return
	 */
	@Override
	public List<SjxxjgDto> getAnalysis(SjxxjgDto sjxxjgDto) {
		// TODO Auto-generated method stub
		//先从用户送检详细结果表获取，为空从详细结果表获取
		List<SjxxjgDto> finalList = new ArrayList<>();
		List<SjxxjgDto> sjxxjgcllist = dao.getTreeAnalysis(sjxxjgDto);
		if(sjxxjgcllist == null || sjxxjgcllist.size() == 0){
			sjxxjgcllist = sjxxjgService.getTreeAnalysis(sjxxjgDto);
		}
		if(sjxxjgcllist != null && sjxxjgcllist.size() > 0) {
			int presize=0;
			for(int i=0;i<sjxxjgcllist.size();i++) {
				sjxxjgcllist.get(i).setIsshow(true);
				int size=sjxxjgcllist.get(i).getT_xssx().length();
				if(i == 0){
					finalList.add(sjxxjgcllist.get(i));
				}else{
					if(presize < size){
						finalList.set(finalList.size()-1, sjxxjgcllist.get(i));
					}else{
						finalList.add(sjxxjgcllist.get(i));
					}
				}
				presize=size;
			}
		}
		return finalList;
	}
}
