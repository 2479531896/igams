package com.matridx.igams.detection.molecule.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxmModel;
import com.matridx.igams.detection.molecule.dao.post.IFzjcxmDao;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxmService;


@Service
public class FzjcxmServiceImpl extends BaseBasicServiceImpl<FzjcxmDto, FzjcxmModel, IFzjcxmDao> implements IFzjcxmService{
	/**
	 * 检测项目明细
	 */
	@Override
	public List<Map<String, String>> generateReportList(String fzjcid) {
		return dao.generateReportList(fzjcid);
	}

    public List<FzjcxmDto> getDtoListByFzjcid(FzjcxmDto fzjcxmDto){
        return dao.getDtoListByFzjcid(fzjcxmDto);
    }

	/**
	 * 根据分子检测id查询有关的检测项目信息
	 */
	public List<FzjcxmDto> getFzjcxmxxList(FzjcxmDto fzjcxmDto){
		return dao.getFzjcxmxxList(fzjcxmDto);
	}

	/**
	 * 增加分子检测项目
	 */
	public boolean insertFzjcxmDto(FzjcxmDto fzjcxmDto){
		return dao.insertFzjcxmDto(fzjcxmDto);
	}

	/**
	 * 根据分子检测信息的主键id删除关联的分子检测项目
	 */
	public boolean delFzjcxmByFzjcid(FzjcxmDto fzjcxmDto){
		return dao.delFzjcxmByFzjcid(fzjcxmDto);
	}
	/**
	 * 根据分子检测信息的主键id删除关联的分子检测项目
	 */
	public int deleteFzjcxmByFzjcid(String fzjcid){
		return dao.deleteFzjcxmByFzjcid(fzjcid);
	}
}
