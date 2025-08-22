package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjnyxDto;
import com.matridx.igams.wechat.dao.entities.SjnyxModel;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.post.ISjnyxDao;
import com.matridx.igams.wechat.service.svcinterface.ISjnyxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SjnyxServiceImpl extends BaseBasicServiceImpl<SjnyxDto, SjnyxModel, ISjnyxDao> implements ISjnyxService{

	/**
	 * 批量新增送检耐药性信息
	 * @param sjnyxDtos
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public boolean insertBysjnyxDtos(List<SjnyxDto> sjnyxDtos) {
		// TODO Auto-generated method stub
		return dao.insertBysjnyxDtos(sjnyxDtos);
	}

	/**
	 * 根据送检ID查询耐药信息
	 * @param sjxxDto
	 * @return
	 */
	public List<SjnyxDto> getNyxBySjid(SjxxDto sjxxDto) {
		return dao.getNyxBySjid(sjxxDto);
	}

	/**
	 * 根据送检ID查询耐药信息
	 * @param sjnyxDto
	 * @return
	 */
	@Override
	public List<SjnyxDto> getNyxByWkbh(SjnyxDto sjnyxDto){
		return dao.getNyxByWkbh(sjnyxDto);
	}
	/**
	 * 根据送检ID查询耐药信息
	 * @param sjxxDto
	 * @return
	 */
	public List<SjwzxxDto> getNyxMapBySjid(SjxxDto sjxxDto) {
		return dao.getNyxMapBySjid(sjxxDto);
	}

	/**
	 * 根据送检ID查询耐药信息，采用送检耐药性列表
	 * @param sjxxDto
	 * @return
	 */
	public List<SjnyxDto> getNyxListBySjid(SjxxDto sjxxDto)
	{
		return dao.getNyxListBySjid(sjxxDto);
	}


	/**
	 * 根据送检ID查询耐药信息，按照药品进行分类
	 * @param sjxxDto
	 * @return
	 */
	public List<SjnyxDto> getNyxYpListBySjid(SjxxDto sjxxDto){
		return dao.getNyxYpListBySjid(sjxxDto);
	}

	/**
	 * 根据Dto删除耐药信息
	 * @param sjnyxDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteBySjnyxDto(SjnyxDto sjnyxDto) {
		// TODO Auto-generated method stub
		int result = dao.deleteBySjnyxDto(sjnyxDto);
        return result != 0;
    }

	/**
	 * 送检耐药性组装为String类型
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public String getExplanationToString(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		List<SjnyxDto> list=dao.getExplanationForWord(sjxxDto);
		StringBuffer sb=new StringBuffer();
		if(list!=null && list.size()>0) {
			for (int i = 1; i <list.size()+1; i++) {
				if(i>1&& i!=list.size()+1) 
					sb.append("{br}{\\n}");
				else
					sb.append("\n");
				sb.append("{br}{b+i}"+i+"."+list.get(i-1).getJyjzmc()+":{br}"+list.get(i-1).getZsnr());
			}
		}
		return sb.toString();
	}

	/**
	 * 根据sjids和jclx批量删除耐药性
	 * @param sjnyxDto
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteBySjidsAndJclx(SjnyxDto sjnyxDto){
		return dao.deleteBySjidsAndJclx(sjnyxDto);
	}

	/**
	 * 根据送检ID查询耐药信息
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String,Object>> getNyxMapList(SjxxDto sjxxDto) {
		return dao.getNyxMapList(sjxxDto);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateBySjnyxDtos(List<SjnyxDto> sjnyxDtos){
		return dao.updateBySjnyxDtos(sjnyxDtos);
	}

	public List<SjnyxDto> getNyxBySjidAndJclx(SjxxDto sjxxDto){
		return dao.getNyxBySjidAndJclx(sjxxDto);
	}

	public List<SjnyxDto> getSamePositionNy(SjnyxDto sjnyxDto){
		return dao.getSamePositionNy(sjnyxDto);
	}
}
