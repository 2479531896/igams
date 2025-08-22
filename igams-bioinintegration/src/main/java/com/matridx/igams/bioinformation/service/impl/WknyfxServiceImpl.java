package com.matridx.igams.bioinformation.service.impl;


import com.alibaba.fastjson.JSON;
import com.matridx.igams.bioinformation.dao.entities.WknyfxDto;
import com.matridx.igams.bioinformation.dao.entities.WknyfxModel;
import com.matridx.igams.bioinformation.dao.post.IWknyfxDao;
import com.matridx.igams.bioinformation.service.svcinterface.IWknyfxService;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class WknyfxServiceImpl extends BaseBasicServiceImpl<WknyfxDto, WknyfxModel, IWknyfxDao> implements IWknyfxService {

	/**
	 * 批量新增
	 */
	public boolean insertList(List<WknyfxDto> list){
		return dao.insertList(list);
	}

	public List<WknyfxDto> getDtoList(WknyfxDto wknyfxDto){
		List<WknyfxDto> nyfxjgDtoList=dao.getDtoList(wknyfxDto);
		List<WknyfxDto> nyfxjgDtos=new ArrayList<>();
		for (WknyfxDto dto : nyfxjgDtoList) {
			if("1".equals(dto.getSfbg())){
				if(StringUtil.isNoneBlank(dto.getNr())){
					WknyfxDto wknyfxDto_t= JSON.parseObject(dto.getNr(),WknyfxDto.class);
					wknyfxDto_t.setNyjgid(dto.getNyjgid());
					wknyfxDto_t.setGenefamily(wknyfxDto_t.getNyjzu());
					wknyfxDto_t.setGene(wknyfxDto_t.getTopjy());
					wknyfxDto_t.setMechanism(wknyfxDto_t.getNyjzu());
					wknyfxDto_t.setTagrget(wknyfxDto_t.getNylb());
					wknyfxDto_t.setWkcxid(dto.getWkcxid());
					wknyfxDto_t.setSfbg(dto.getSfbg());
					wknyfxDto_t.setSpecies(wknyfxDto_t.getCkwz());
					wknyfxDto_t.setNyjy(dto.getNyjy());
					wknyfxDto_t.setDrug_class(wknyfxDto_t.getNylb());
					wknyfxDto_t.setOrigin_species(wknyfxDto_t.getCkwztaxid());
					wknyfxDto_t.setRelated_gene(wknyfxDto_t.getNyjzu());
					wknyfxDto_t.setMain_mechanism(wknyfxDto_t.getNyjz());
					wknyfxDto_t.setRef_species(wknyfxDto_t.getCkwz());
					wknyfxDto_t.setGenes(wknyfxDto_t.getTopjy());
					wknyfxDto_t.setReads(wknyfxDto_t.getTopjydjcxx());
					wknyfxDto_t.setNr(dto.getNr());
					nyfxjgDtos.add(wknyfxDto_t);
				}
			}
		}
		return nyfxjgDtos;
	}
}
