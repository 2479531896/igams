package com.matridx.igams.common.service.impl;


import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.JgxxModel;
import com.matridx.igams.common.dao.post.IJgxxDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JgxxServiceImpl extends BaseBasicServiceImpl<JgxxDto, JgxxModel, IJgxxDao> implements IJgxxService {

	//@Autowired(required=false)
    //private AmqpTemplate amqpTempl;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	
	@Override
	public List<JgxxDto> getJgxxList() {
		return dao.getJgxxList();
	}

	/**
	 * 获取机构信息列表
	 */
	@Override
	public List<JgxxDto> getPagedJgxxList (JgxxDto jgxxDto){
//		List<JgxxDto> jgxxDtos = dao.getPagedJgxxList(jgxxDto);
//		for (JgxxDto jgxx: jgxxDtos) {
//			if (StringUtil.isNotBlank(jgxx.getKzcs2())){
//				String[] temps = jgxx.getKzcs2().split(",");
//				String kzcs2 ="";
//				for (int i = 0; i < temps.length; i++) {
//					CkxxDto ckxxDto = ckxxService.getDtoById(temps[i]);
//					kzcs2 += ckxxDto.getCkmc();
//					if (i != temps.length-1){
//						kzcs2+=",";
//					}
//				}
//				jgxx.setKzcs2(kzcs2);
//			}
//		}
		return dao.getPagedJgxxList(jgxxDto);
	}
	
	@Override
	public boolean insertDto(JgxxDto jgxxDto) {
		if(StringUtil.isBlank(jgxxDto.getJgid())){
			jgxxDto.setJgid(StringUtil.generateUUID());
		}
		int result = dao.insert(jgxxDto);
		if(result == 0)
			return false;
		jgxxDto.setPrefix(prefixFlg);
//		amqpTempl.convertAndSend("sys.igams", "sys.igams.jgxx.update",JSONObject.toJSONString(jgxxDto));
		return true;
	}
	
	/**
	 * 根据机构ID查询机构信息
	 */
	public JgxxDto selectJgxxByJgid(JgxxDto jgxxDto) {
		return dao.selectJgxxByJgid(jgxxDto);
	}
	
	/**
	 * 根据机构ID更新机构信息
	 */
	public boolean updateJgxx(JgxxDto jgxxDto) {
		int result=dao.updateJgxx(jgxxDto);
		if(result>0) {
			jgxxDto.setPrefix(prefixFlg);
//			amqpTempl.convertAndSend("sys.igams", "sys.igams.jgxx.update",JSONObject.toJSONString(jgxxDto));
			return true;
		}
		return false;
	}
	
	/**
	 * 删除机构信息
	 */
	public boolean deleteJgxx(JgxxDto jgxxDto) {
		int result=dao.deleteJgxx(jgxxDto);
		if(result>0) {
			jgxxDto.setPrefix(prefixFlg);
//			amqpTempl.convertAndSend("sys.igams", "sys.igams.jgxx.del",JSONObject.toJSONString(jgxxDto));
			return true;
		}
		return false;
	}
	
	/**
	 * 根据机构名称查询机构信息
	 */
	public JgxxDto getJgxxByJgmc(JgxxDto jgxxDto) {
		return dao.getJgxxByJgmc(jgxxDto);
	}
	
	/**
	 * 查询机构列表(除本身)
	 */
	public List<JgxxDto> getPagedOtherJgxxList(JgxxDto jgxxDto){
		return dao.getPagedOtherJgxxList(jgxxDto);
	}

	@Override
	public List<JgxxDto> selsetJgxxByjgmc(JgxxDto jgxxDto){
		// TODO Auto-generated method stub
		return dao.selsetJgxxByjgmc(jgxxDto);
	}

	/**
	 * 查找所有机构的数据，包括删除标记为1的
	 */
	@Override
	public List<JgxxDto> getAllJgxxList() {
		// TODO Auto-generated method stub
		return dao.getAllJgxxList();
	}

	/**
	 * 批量更新机构信息
	 */
	@Override
	public boolean updateJgxxList(List<JgxxDto> jgxxModList) {
		return dao.updateJgxxList(jgxxModList);
	}

	@Override
	public boolean deleteByWbcxid(JgxxDto jgxxDto) {
		return dao.deleteByWbcxid(jgxxDto);
	}

	@Override
	public List<JgxxDto> getJgxxByWbcx(JgxxDto jgxxDto) {
		return dao.getJgxxByWbcx(jgxxDto);
	}

	@Override
	public void getJgxxByFjgid(String jgid, List<JgxxDto> jgxxDtos, List<JgxxDto> zjgxxs) {
		for (JgxxDto jgxxDto : jgxxDtos) {
			if (jgid.equals(jgxxDto.getFjgid())){
				zjgxxs.add(jgxxDto);
				getJgxxByFjgid(jgxxDto.getJgid(),jgxxDtos,zjgxxs);
			}
		}
	}

	@Override
	public List<JgxxDto> getJgxx2List(JgxxDto jgxxDto) {
		return dao.getJgxx2List(jgxxDto);
	}
	@Override
	public List<JgxxDto> getJgxx2ListAll() {
		return dao.getJgxx2ListAll();
	}

	@Override
	public JgxxDto getFirstLevelJgidByJgxx(JgxxDto jgxxDto, List<JgxxDto> jgxxList) {
		if (StringUtil.isBlank(jgxxDto.getFjgid())|| "1".equals(jgxxDto.getFjgid())) {
			return jgxxDto;
		}
		for (JgxxDto dto : jgxxList) {
			if (dto.getJgid().equals(jgxxDto.getFjgid())) {
				if ("1".equals(dto.getFjgid()) || StringUtil.isBlank(dto.getFjgid())){
					return dto;
				}else {
					return getFirstLevelJgidByJgxx(dto, jgxxList);
				}
			}
		}
		return null;
	}

	@Override
	public List<JgxxDto> queryByWbcx(JgxxDto jgxxDto) {
		return dao.queryByWbcx(jgxxDto);
	}

	@Override
	public JgxxDto queryByJgxxDto(JgxxDto jgxxDto) {
		return dao.queryByJgxxDto(jgxxDto);
	}
}
