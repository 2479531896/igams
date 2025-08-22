package com.matridx.igams.experiment.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.RwrqDto;
import com.matridx.igams.experiment.dao.entities.RwrqModel;
import com.matridx.igams.experiment.dao.post.IRwrqDao;
import com.matridx.igams.experiment.service.svcinterface.IRwrqService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RwrqServiceImpl extends BaseBasicServiceImpl<RwrqDto, RwrqModel, IRwrqDao> implements IRwrqService{

	/**
	 * 根据任务ID以及项目阶段ID查询该任务的日期信息
	 */
	public RwrqDto getRqxxByRwidAndXmjdid(RwrqDto rwrqDto) {
		return dao.getRqxxByRwidAndXmjdid(rwrqDto);
	}
	/**
	 * 根据日期ID进行插入或删除
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertOrUpdateRwrq(List<RwrqDto> rwrqDtos) {
		int result = dao.insertOrUpdateRwrq(rwrqDtos);
		return result != 0;
	}
	/**
	 * 修改项目任务计划日期
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean modSaveTaskTime(RwrqDto rwrqDto) {
		// TODO Auto-generated method stub
		List<String> rqids = rwrqDto.getIds();
		List<String> jhjsrqs = rwrqDto.getJhjsrqs();
		List<String> jhksrqs = rwrqDto.getJhksrqs();
		List<String> xmjdids = rwrqDto.getXmjdids();
		List<RwrqDto> rwrqDtos = new ArrayList<>();
		for (int i = 0; i < xmjdids.size(); i++) {
			RwrqDto t_rwrqDto = new RwrqDto();
			t_rwrqDto.setRwid(rwrqDto.getRwid());
			t_rwrqDto.setXmjdid(xmjdids.get(i));
			if(StringUtil.isBlank(rqids.get(i))){
				t_rwrqDto.setRqid(StringUtil.generateUUID());
				t_rwrqDto.setLrry(rwrqDto.getXgry());
			}else{
				t_rwrqDto.setRqid(rqids.get(i));
				t_rwrqDto.setXgry(rwrqDto.getXgry());
			}
			if(StringUtil.isNotBlank(jhjsrqs.get(i))){
				t_rwrqDto.setJhjsrq(jhjsrqs.get(i));
			}
			if(StringUtil.isNotBlank(jhksrqs.get(i))){
				t_rwrqDto.setJhksrq(jhksrqs.get(i));
			}
			rwrqDtos.add(t_rwrqDto);
		}
		int result = dao.insertOrUpdateJhrq(rwrqDtos);
		return result != 0;
	}

	/**
	 * 根据任务ID以及项目阶段ID查询该任务的日期信息
	 */
	public int modRqxxByRwidAndXmjdid(RwrqDto rwrqDto) {
		return dao.modRqxxByRwidAndXmjdid(rwrqDto);
	}

	@Override
	public List<RwrqDto> getProjectProgress(RwrqDto rwrqDto) {
		List<RwrqDto> dtos = new ArrayList<>();
		List<RwrqDto> infoListJh = dao.getInfoListByJh(rwrqDto);
		List<RwrqDto> infoListSj = dao.getInfoListBySj(rwrqDto);
		Map<String, List<RwrqDto>> map = infoListSj.stream().filter(item-> StringUtil.isNotBlank(item.getClryxm())).collect(Collectors.groupingBy(RwrqDto::getClryxm));
		if (null != map && map.size()>0){
			Iterator<Map.Entry<String, List<RwrqDto>>> entries = map.entrySet().iterator();
			while (entries.hasNext()) {
				try {
					Map.Entry<String,  List<RwrqDto>> entry = entries.next();
					List<RwrqDto> resultModelList = entry.getValue();
					if (null != resultModelList && resultModelList.size() > 0){
						RwrqDto dto = new RwrqDto();
						dto.setClryxm(entry.getKey());
						dto.setDtosSj(listSort(infoListSj,entry.getKey()));
						dto.setDtosJh(listSort(infoListJh,entry.getKey()));
						dtos.add(dto);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		return dtos;
	}

	public List<List<RwrqDto>> listSort(List<RwrqDto> list,String key) throws ParseException {
		List<List<RwrqDto>> lists = new ArrayList<>();
		int count;
		do {
			count = 0;
			if (!CollectionUtils.isEmpty(list) && StringUtil.isNotBlank(key)){
				Long time = null;
				List<RwrqDto> dtos = new ArrayList<>();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				for (int i = 0; i < list.size(); i++) {
					if (key.equals(list.get(i).getClryxm())){
						if (dtos.size()==0){
							count++;
							dtos.add(list.get(i));
							time = 	dateFormat.parse(list.get(i).getEndTime()).getTime() ;
							list.remove(i);
							i--;
						}else{
							if (time <  dateFormat.parse(list.get(i).getStartTime()).getTime()){
								count++;
								dtos.add(list.get(i));
								time = dateFormat.parse(list.get(i).getEndTime()).getTime() ;
								list.remove(i);
								i--;
							}
						}
					}
				}
				if (!CollectionUtils.isEmpty(dtos)){
					lists.add(dtos);
				}

			}
		} while (count != 0);
		return lists;
	}

	@Override
	public boolean updateJdfss(List<RwrqDto> list) {
		return dao.updateJdfss(list);
	}
}
