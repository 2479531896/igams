package com.matridx.igams.storehouse.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.CkxxDto;
import com.matridx.igams.storehouse.dao.entities.CkxxModel;
import com.matridx.igams.storehouse.dao.post.ICkxxDao;
import com.matridx.igams.storehouse.service.svcinterface.ICkxxService;
import org.springframework.util.CollectionUtils;

@Service
public class CkxxServiceImpl extends BaseBasicServiceImpl<CkxxDto, CkxxModel, ICkxxDao> implements ICkxxService{
	@Override
	public List<CkxxDto> getItemInfoByZtQgsl(CkxxDto ckxxDto){
		return dao.getItemInfoByZtQgsl(ckxxDto);
	}
	@Override
	public List<CkxxDto> getItemInfoByZtHtcgzsl(CkxxDto ckxxDto){
		return dao.getItemInfoByZtHtcgzsl(ckxxDto);
	}
	@Override
	public List<CkxxDto> getItemInfoByZtHtwdhsl(CkxxDto ckxxDto){
		return dao.getItemInfoByZtHtwdhsl(ckxxDto);
	}


	@Override
	public List<CkxxDto> getScLcInfoList(CkxxDto ckxxDto) {
		List<CkxxDto> dtos = new ArrayList<>();
		List<CkxxDto> cpList = new ArrayList<>();
		List<CkxxDto> scList = new ArrayList<>();
		List<CkxxDto> dhList = new ArrayList<>();
		List<CkxxDto> zjList = new ArrayList<>();
		List<CkxxDto> ckxxDtos_scjd = dao.getScLcInfoList(ckxxDto);
		Map<String, List<CkxxDto>> listMap = ckxxDtos_scjd.stream().collect(Collectors.groupingBy(CkxxDto::getLx));
		if (!CollectionUtils.isEmpty(listMap)){
			for (Map.Entry<String, List<CkxxDto>> entry : listMap.entrySet()) {
				String lx = entry.getKey();
				List<CkxxDto> dtoList = entry.getValue();
				if (!CollectionUtils.isEmpty(dtoList)) {
					if ("SC".equals(lx)) {
						Map<String, List<CkxxDto>> shMap = dtoList.stream().collect(Collectors.groupingBy(CkxxDto::getShlb));
						if (!CollectionUtils.isEmpty(shMap)) {
							Iterator<Map.Entry<String, List<CkxxDto>>> iterator = shMap.entrySet().iterator();
							int pos = 0;
							String key = "";
							while (iterator.hasNext()) {
								Map.Entry<String, List<CkxxDto>> listEntry = iterator.next();
								if (listEntry.getValue().size() > pos) {
									pos = listEntry.getValue().size();
									key = listEntry.getKey();
								}
							}
							List<CkxxDto> ckxxDtos = shMap.get(key);

							for (int i = 1; i < ckxxDtos.size(); i++) {
								CkxxDto dto = new CkxxDto();
								dto.setLc(ckxxDtos.get(i).getGwmc());
								Long startTime = Long.parseLong(ckxxDtos.get(i - 1).getAvg_day());
								Long endTime = Long.parseLong(ckxxDtos.get(i).getAvg_day());
								long time = endTime - startTime;
								dto.setSl(String.format("%.2f", time / 86400d));
//								dto.setSl(String.valueOf(time));
								scList.add(dto);
							}
							for (Map.Entry<String, List<CkxxDto>> listEntry : shMap.entrySet()) {
								if (!listEntry.getKey().equals(key)) {
									List<CkxxDto> ckxxDtoList = listEntry.getValue();
									List<CkxxDto> scDtos = new ArrayList<>();
									for (int i = 1; i < ckxxDtoList.size(); i++) {
										CkxxDto ckxxDto1 = new CkxxDto();
										ckxxDto1.setLc(ckxxDtoList.get(i).getGwmc());
										Long startTime = Long.parseLong(ckxxDtoList.get(i - 1).getAvg_day());
										Long endTime = Long.parseLong(ckxxDtoList.get(i).getAvg_day());
										long time = endTime - startTime;
										ckxxDto1.setSl(String.format("%.2f", time / 86400d));
//										ckxxDto1.setSl(String.valueOf(time));
										scDtos.add(ckxxDto1);
									}
									if (!CollectionUtils.isEmpty(scDtos)&&!CollectionUtils.isEmpty(scList)) {
										for (CkxxDto dto : scList) {
											for (int i = 0; i < scDtos.size(); i++) {
												if (dto.getLc().equals(scDtos.get(i).getLc())) {
													dto.setSl(String.format("%.2f", (Double.parseDouble(dto.getSl()) + Double.parseDouble(scDtos.get(i).getSl())) / 2d));
													scDtos.remove(i);
													i--;
												}
											}
										}
									}
									if (!CollectionUtils.isEmpty(scDtos)) {
										scList.addAll(scDtos);
									}
								}
							}
						}
					} else {
						CkxxDto dto = new CkxxDto();
						dto.setLc(dtoList.get(0).getLx());
						Long startTime = Long.parseLong(dtoList.get(0).getAvg_day());
						Long endTime = Long.parseLong(dtoList.get(dtoList.size() - 1).getAvg_day());
						long time = endTime - startTime;
						dto.setSl(String.format("%.2f", time / 86400d));
//						dto.setSl(String.valueOf(time));
						if ("CP".equals(dtoList.get(0).getLx())) {
							dto.setLc("成品需求");
							cpList.add(dto);
						} else if ("DH".equals(dtoList.get(0).getLx())) {
							dto.setLc("到货");
							dhList.add(dto);
						} else if ("ZJ".equals(dtoList.get(0).getLx())) {
							dto.setLc("质检");
							zjList.add(dto);
						}
					}
				}
			}
		}
		dtos.addAll(cpList);
		dtos.addAll(scList);
		dtos.addAll(dhList);
		dtos.addAll(zjList);
		return dtos;
	}

	@Override
	public List<CkxxDto> getItemInfoByWlCkKcl(CkxxDto ckxxDto){
		return dao.getItemInfoByWlCkKcl(ckxxDto);
	}
	@Override
	public List<CkxxDto> getItemInfoByWlCkhwKcl(CkxxDto ckxxDto){
		return dao.getItemInfoByWlCkhwKcl(ckxxDto);
	}

	@Autowired
	IJcsjService jcsjService;
	/**
	 * 检查是不是父仓库
	 * @param fckid
	 * @return
	 */
	@Override
	public CkxxDto checkFck(String fckid){
		// TODO Auto-generated method stub
		return dao.checkFck(fckid);
	}

	@Override
	public CkxxDto getDtoByCkdm(String ckdm) {
		return dao.getDtoByCkdm(ckdm);
	}

	/**
	 * 删除仓库同时删除子仓库
	 * @param ckxxDto
	 * @return
	 */
	public  boolean deleteck(CkxxDto ckxxDto) {
		dao.deletezck(ckxxDto);
		dao.delete(ckxxDto);
		return true;
	}
	
	/**
	 * 查询库位信息
	 * @return
	 */
	public List<CkxxDto> getKwDtoList(CkxxDto ckxxDto){
		return dao.getKwDtoList(ckxxDto);
	}

	@Override
	public List<CkxxDto> getCRkslByBm(CkxxDto ckxxDto) {
		return dao.getCRkslByBm(ckxxDto);
	}

	@Override
	public List<CkxxDto> getdepotStatusLcInfo(CkxxDto ckxxDto) {
		return dao.getdepotStatusLcInfo(ckxxDto);
	}

	@Override
	public List<CkxxDto> getKwDtoListByFckid(CkxxDto ckxxDto) {
		return dao.getKwDtoListByFckid(ckxxDto);
	}


	/**
	 * 查询仓库
	 * @return
	 */
	@Override
	public List<CkxxDto> getPagedDtoList(CkxxDto ckxxDto){
		List<CkxxDto> ckxxDtoList = dao.getPagedDtoList(ckxxDto);
		for (CkxxDto ckxxDto1: ckxxDtoList) {
			if (StringUtil.isNotBlank(ckxxDto1.getCkqxlx())){
				JcsjDto jcsjDto = jcsjService.getDtoById(ckxxDto1.getCkqxlx());
				ckxxDto1.setCkqxlx(jcsjDto.getCsmc());
			}
		}
		return ckxxDtoList;
	}

	@Override
	public List<CkxxDto> getKhJysl(CkxxDto ckxxDto) {
		return dao.getKhJysl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlRksl(CkxxDto ckxxDto) {
		return dao.getDtoZlRksl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getdepotStatusZtInfo(CkxxDto ckxxDto) {
		return dao.getdepotStatusZtInfo(ckxxDto);
	}

	@Override
	public List<CkxxDto> getKhfhsl(CkxxDto ckxxDto) {
		return dao.getKhfhsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlZtQgzsl(CkxxDto ckxxDto) {
		return dao.getDtoZlZtQgzsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlZtHtcgzsl(CkxxDto ckxxDto) {
		return dao.getDtoZlZtHtcgzsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlZtHtwdhsl(CkxxDto ckxxDto) {
		return  dao.getDtoZlZtHtwdhsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlZtzjzsl(CkxxDto ckxxDto) {
		return dao.getDtoZlZtzjzsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlZtzjdclsl(CkxxDto ckxxDto) {
		return dao.getDtoZlZtzjdclsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoWlCkhwKcl(CkxxDto ckxxDto) {
		return dao.getDtoWlCkhwKcl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getItemInfoByLcCpsl(CkxxDto ckxxDto) {
		return dao.getItemInfoByLcCpsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getItemInfoByLcLlsl(CkxxDto ckxxDto) {
		return dao.getItemInfoByLcLlsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getItemInfoByFhsl(CkxxDto ckxxDto) {
		return dao.getItemInfoByFhsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getItemInfoByJysl(CkxxDto ckxxDto) {
		return dao.getItemInfoByJysl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoWlCkKcl(CkxxDto ckxxDto) {
		return dao.getDtoWlCkKcl(ckxxDto);
	}
	@Override
	public List<CkxxDto> getCkkcl(CkxxDto ckxxDto) {
		return dao.getCkkcl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlKhfh(CkxxDto ckxxDto) {
		return dao.getDtoZlKhfh(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlKhjy(CkxxDto ckxxDto) {
		return dao.getDtoZlKhjy(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlCksl(CkxxDto ckxxDto) {
		return dao.getDtoZlCksl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getItemInfo(CkxxDto ckxxDto) {
		return dao.getItemInfo(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlLcQgsl(CkxxDto ckxxDto) {
		return dao.getDtoZlLcQgsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlLcDhsl(CkxxDto ckxxDto) {
		return dao.getDtoZlLcDhsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlLcZjhgsl(CkxxDto ckxxDto) {
		return dao.getDtoZlLcZjhgsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlLcZjbhgsl(CkxxDto ckxxDto) {
		return dao.getDtoZlLcZjbhgsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlLcLlck(CkxxDto ckxxDto) {
		return dao.getDtoZlLcLlck(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlLcJysl(CkxxDto ckxxDto) {
		return dao.getDtoZlLcJysl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlLcGhsl(CkxxDto ckxxDto) {
		return dao.getDtoZlLcGhsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getDtoZlLcSfsl(CkxxDto ckxxDto) {
		return dao.getDtoZlLcSfsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getItemInfoByLcQgsl(CkxxDto ckxxDto) {
		return dao.getItemInfoByLcQgsl(ckxxDto);
	}

	@Override
	public List<CkxxDto> getItemInfoByLcCgrk(CkxxDto ckxxDto) {
		return dao.getItemInfoByLcCgrk(ckxxDto);
	}

	@Override
	public List<CkxxDto> getItemInfoByLcZjsl(CkxxDto ckxxDto) {
		return dao.getItemInfoByLcZjsl(ckxxDto);
	}
}
