package com.matridx.crf.web.dao.post;

import java.util.List;
import java.util.Map;

import com.matridx.crf.web.dao.entities.NdzxxDto;
import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.crf.web.dao.entities.HzxxDto;
import com.matridx.crf.web.dao.entities.HzxxModel;

@Mapper
public interface IHzxxDao extends BaseBasicDao<HzxxDto, HzxxModel>{
	public List<HzxxDto> getPagedDtoListMap(Map map);
	public Map<String,Object>  getHzxxDtoMap(HzxxDto hzxxDto);
	public List<Map> getHospitailList(HzxxDto hzxxDto);
	public Map<String,Object> getnryjbh(HzxxDto hzxxDto);
	/**
	 * 获取上传的文件信息
	 * @param hzxxDto
	 * @return
	 */
	public List<FjcfbDto> getFjcfb(HzxxDto hzxxDto);
	/**
	 * 获取单日上传的文件信息
	 * @param ndzxxjlDto
	 * @return
	 */
	public FjcfbDto getFjcfbByjlid(NdzxxjlDto ndzxxjlDto);

	public int updateBgsjById(HzxxDto hzxxDto);
}
