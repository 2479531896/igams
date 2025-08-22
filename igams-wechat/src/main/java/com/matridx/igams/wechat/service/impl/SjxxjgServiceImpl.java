package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjxxjgDto;
import com.matridx.igams.wechat.dao.entities.SjxxjgModel;
import com.matridx.igams.wechat.dao.post.ISjxxjgDao;
import com.matridx.igams.wechat.service.svcinterface.ISjxxjgService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SjxxjgServiceImpl extends BaseBasicServiceImpl<SjxxjgDto, SjxxjgModel, ISjxxjgDao> implements ISjxxjgService{

	/** 
	 * 修改详细结果信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateDto(SjxxjgDto sjxxjgDto){
		int result = dao.update(sjxxjgDto);
        return result != 0;
    }
	
	/**
	 * 批量删除送检详细审核结果
	 * @param sjxxjgDto
	 * @return
	 */
	@Override
	public boolean deleteBySjxxjgDto(SjxxjgDto sjxxjgDto) {
		// TODO Auto-generated method stub
		int result = dao.deleteBySjxxjgDto(sjxxjgDto);
        return result != 0;
    }

	/**
	 * 批量新增送检详细审核结果
	 * @param sjxxjgDtos
	 * @return
	 */
	@Override
	public boolean insertBySjxxjgDtos(List<SjxxjgDto> sjxxjgDtos) {
		// TODO Auto-generated method stub
		return dao.insertBySjxxjgDtos(sjxxjgDtos);
	}

	/**
	 * 根据送检ID查询fjdid为null的详细信息
	 * @param sjxxjgDto
	 * @return
	 */
	public List<SjxxjgDto> getxxjgByFjdidIsNull(SjxxjgDto sjxxjgDto){
		return dao.getxxjgByFjdidIsNull(sjxxjgDto);
	}
	
	/**
	 * 根据送检ID查询Species下详细结果
	 * @param list
	 * @return
	 */
	public List<SjxxjgDto> getxxInSpecies(List<SjxxjgDto> list){
		return dao.getxxInSpecies(list);
	}
	
	/**
	 * 根据送检ID查询Genus下详细结果
	 * @param list
	 * @return
	 */
	public List<SjxxjgDto> getxxInGenus(List<SjxxjgDto> list){
		return dao.getxxInGenus(list);
	}
	
	/**
	 * 根据检测类型计算D,R,C下详细结果总和
	 * @param sjxxjgDto
	 * @return
	 */
	public List<SjxxjgDto> getJclxCount(SjxxjgDto sjxxjgDto){
		return dao.getJclxCount(sjxxjgDto);
	}
	
	/**
	 * 查询详细结果信息
	 * @param sjxxjgDto
	 * @return
	 */
	@Override
	public List<SjxxjgDto> getTreeAnalysis(SjxxjgDto sjxxjgDto) {
		// TODO Auto-generated method stub
		return dao.getTreeAnalysis(sjxxjgDto);
	}

	/**
	 * 根据送检ID查询详细结果信息
	 * @param sjid
	 * @return
	 */
	@Override
	public List<SjxxjgDto> selectBySjid(String sjid) {
		// TODO Auto-generated method stub
		return dao.selectBySjid(sjid);
	}

	/**
	 * 获取检测类型信息
	 * @param sjxxjgDto
	 * @return
	 */
	@Override
	public List<SjxxjgDto> getJclxInfo(SjxxjgDto sjxxjgDto) {
		// TODO Auto-generated method stub
		return dao.getJclxInfo(sjxxjgDto);
	}

	/**
	 * 获取导出数据
	 * @param sjxxjgDto
	 * @return
	 */
	public List<SjxxjgDto> getListByIds(SjxxjgDto sjxxjgDto){
		return dao.getListByIds(sjxxjgDto);
	}

	@Override
	public List<Map<String,Object>> getInfoList(SjxxjgDto sjxxjgDto){
		return dao.getInfoList(sjxxjgDto);
	}

}
