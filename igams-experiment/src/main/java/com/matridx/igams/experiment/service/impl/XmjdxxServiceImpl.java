package com.matridx.igams.experiment.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.post.IGzglDao;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmjdrwDto;
import com.matridx.igams.experiment.dao.entities.XmjdxxDto;
import com.matridx.igams.experiment.dao.entities.XmjdxxModel;
import com.matridx.igams.experiment.dao.post.IXmjdrwDao;
import com.matridx.igams.experiment.dao.post.IXmjdxxDao;
import com.matridx.igams.experiment.service.svcinterface.IXmjdxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateTimeUtil;

@Service
public class XmjdxxServiceImpl extends BaseBasicServiceImpl<XmjdxxDto, XmjdxxModel, IXmjdxxDao> implements IXmjdxxService{
	
	@Autowired
	IXmjdrwDao xmjdrwDao;
	
	@Autowired
	IGzglDao gzglDao;

	/** 
	 * 插入项目阶段信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(XmjdxxDto xmjdxxDto){
		xmjdxxDto.setXmjdid(StringUtil.generateUUID());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		//获得本月第一天
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String firstDay = sdf.format(calendar.getTime());
		//获得本月最后一天
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		String lastDay = sdf.format(calendar.getTime());

		if(StringUtil.isBlank(xmjdxxDto.getSjksrq())){
			xmjdxxDto.setSjksrq(firstDay);
		}
		if(StringUtil.isBlank(xmjdxxDto.getSjjsrq())){
			xmjdxxDto.setSjjsrq(lastDay);
		}
		int result = dao.insert(xmjdxxDto);
		if(result == 0)
			return false;
		XmjdrwDto xmjdrwDto = new XmjdrwDto();
		xmjdrwDto.setXmid(xmjdxxDto.getXmid());
		xmjdrwDao.addNewJdxx(xmjdrwDto);
		return true;
	}
	
	/**
	 * 根据项目ID查询阶段列表
	 */
	@Override
	public List<XmjdxxDto> selectStageByXmid(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		List<XmjdxxDto> xmjdxxlist=dao.selectStageByXmid(xmglDto);
		if (xmjdxxlist.size()>0){
			for (XmjdxxDto xmjdxxDto : xmjdxxlist) {
				List<XmjdrwDto> xmjdrwlist;
				xmjdxxDto.setZt(xmglDto.getZt());
				xmjdxxDto.setEntire(xmglDto.getEntire());
				if (StringUtil.isNotBlank(xmglDto.getMyflg())) {
					xmjdxxDto.setYhid(xmglDto.getYhid());
					if (StringUtil.isNotBlank(xmglDto.getMySign())) {
						xmjdxxDto.setMySign(xmglDto.getMySign());
					}
					xmjdrwlist = xmjdrwDao.selectMyTaskByXmjdxx(xmjdxxDto);
				} else {
					xmjdrwlist = xmjdrwDao.selectTaskByXmjdxx(xmjdxxDto);
				}
				List<String> rwids = new ArrayList<>();
				if (xmjdrwlist != null && xmjdrwlist.size() > 0) {
					for (XmjdrwDto dto : xmjdrwlist) {
						if (dto.getZt().equals(StatusEnum.CHECK_PASS.getCode())) {
							dto.setBgcolor("#F5F5F5");
						} else {
							dto.setBgcolor("#D1EEEE");
						}
						rwids.add(dto.getRwid());
					}

					XmjdrwDto xmjdrwDto = new XmjdrwDto();
					xmjdrwDto.setRwids(rwids);
					List<Map<String, String>> countMap = xmjdrwDao.selectsum(xmjdrwDto);
					//查询所有的子任务
					for (XmjdrwDto dto : xmjdrwlist) {
						if (countMap != null) {
							for (Map<String, String> stringStringMap : countMap) {
								if (stringStringMap.get("frwid").equals(dto.getRwid())) {
									dto.setFinishnum(String.valueOf(stringStringMap.get("num")));
									dto.setUnfinishnum(String.valueOf(stringStringMap.get("compnum")));
								}
							}

						} else {
							dto.setFinishnum("0");
							dto.setUnfinishnum("0");
						}
					}
				}
				xmjdxxDto.setXmjdrwDtos(xmjdrwlist);
			}
		}
		return xmjdxxlist;
	}

	@Override
	public String selectMinTimeByXmid(XmglDto xmglDto) {
		return dao.selectMinTimeByXmid(xmglDto);
	}

	/**
	 * 查询最大的xh
	 */
	@Override
	public int getXh(XmjdxxDto xmjdxxDto){
		// TODO Auto-generated method stub
		return dao.getXh(xmjdxxDto);
	}
	
	/**
	 * 项目阶段排序
	 */
	@Override
	public boolean stageSort(XmjdxxDto xmjdxxDto) {
		// TODO Auto-generated method stub
		XmglDto xmglDto = new XmglDto();
		xmglDto.setXmid(xmjdxxDto.getXmid());
		List<XmjdxxDto> xmjdxxDtos = dao.selectStageByXmid(xmglDto);
		List<XmjdxxDto> t_xmjdxxDtos = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		int czlx = 0;
		int start = 0;
		for (int i = 0; i < xmjdxxDto.getIds().size(); i++) {
			if (!xmjdxxDto.getIds().get(i).equals(xmjdxxDtos.get(i).getXmjdid())){
				start = i;
				if (xmjdxxDtos.get(i).getXmjdid().equals(xmjdxxDto.getIds().get(i+1))){
					czlx = 1;
				}
				break;
			}
		}
		int end = start-1;
		for (int i = 0; i < xmjdxxDto.getIds().size(); i++) {
			if (!xmjdxxDto.getIds().get(i).equals(xmjdxxDtos.get(i).getXmjdid())){
				end ++;
			}
		}

		XmjdrwDto xmjddto = new XmjdrwDto();
		xmjddto.setXmid(xmjdxxDto.getXmid());
		List<XmjdrwDto> xmjdrwDtos = xmjdrwDao.selectXmrwxxs(xmjddto);
		for (XmjdrwDto xmjdrwDto : xmjdrwDtos) {
			if (StringUtil.isNotBlank(xmjdrwDto.getJdtime())){
				String[] strings = xmjdrwDto.getJdtime().split(",");
				StringBuilder str = new StringBuilder();
				for (int i = 0; i < xmjdxxDtos.size(); i++) {
					if (i<strings.length){
						if (StringUtil.isNotBlank(strings[i])){
							if (i == start && czlx == 1){
								if (end<strings.length){
									if (StringUtil.isNotBlank(strings[end])){
										str.append(strings[end]).append(",");
									}else {
										str.append("-,");
									}
								}else {
									str.append("-,");
								}
							}
							if (!((i == start && czlx == 0) || (i == end && czlx == 1))){
								str.append(strings[i]);
							}
							if (i == end && czlx == 0){
								if (start<strings.length){
									if (StringUtil.isNotBlank(strings[start])){
										str.append(",").append(strings[start]);
									}else {
										str.append(",-");
									}
								}else {
									str.append(",-");
								}
							}
						}else {
							str.append("-");
						}
					}else {
						str.append("-");
					}
					if (i!=xmjdxxDtos.size()-1 && !((i == start && czlx == 0) || (i == end && czlx == 1)))
						str.append(",");

				}
				xmjdrwDto.setJdtime(str.toString());
			}else{
				if (StringUtil.isBlank(xmjdrwDto.getLrsj())){
					xmjdrwDto.setLrsj("-");
				}
				StringBuilder str= new StringBuilder("" + xmjdrwDto.getLrsj() + ",");
				for (int i = 0; i < xmjdxxDtos.size()-1; i++) {
					if (i!= xmjdxxDtos.size()-2){
						str.append("-,");
					}else {
						str.append("-");
					}
				}
				xmjdrwDto.setJdtime(str.toString());
			}
		}
		int result;
		xmjdrwDao.updateXmjdxx(xmjdrwDtos);
		for (int j = 0; j < xmjdxxDto.getIds().size(); j++) {
			for (int i = 0; i < xmjdxxDtos.size(); i++) {
				if(xmjdxxDtos.get(i).getXmjdid().equals(xmjdxxDto.getIds().get(j))){
					XmjdxxDto t_xmjdxxDto = new XmjdxxDto();
					t_xmjdxxDto.setXh(String.valueOf(j+1));
					try {
						if(j == 0){
							if(StringUtil.isNotBlank(xmjdxxDtos.get(0).getSjksrq())){
								t_xmjdxxDto.setSjksrq(xmjdxxDtos.get(0).getSjksrq());
								cal.setTime(dft.parse(xmjdxxDtos.get(0).getSjksrq()));
								int count = 1;
								if(StringUtil.isNotBlank(xmjdxxDtos.get(i).getQx())){
									count = Integer.parseInt(xmjdxxDtos.get(i).getQx());
								}
								cal.add(Calendar.MONTH, count-1);
								t_xmjdxxDto.setSjjsrq(DateTimeUtil.getLastDayOfMonth(cal.getTime()));
							}
						}else{
							if(StringUtil.isNotBlank(t_xmjdxxDtos.get(j-1).getSjjsrq())){
								cal.setTime(dft.parse(t_xmjdxxDtos.get(j-1).getSjjsrq()));
								cal.add(Calendar.MONTH, 1);
								t_xmjdxxDto.setSjksrq(DateTimeUtil.getFirstDayOfMonth(cal.getTime()));
								int count = 1;
								if(StringUtil.isNotBlank(xmjdxxDtos.get(i).getQx())){
									count = Integer.parseInt(xmjdxxDtos.get(i).getQx());
								}
								cal.setTime(dft.parse(t_xmjdxxDtos.get(j-1).getSjjsrq()));
								cal.add(Calendar.MONTH, count);
								t_xmjdxxDto.setSjjsrq(DateTimeUtil.getLastDayOfMonth(cal.getTime()));
							}
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					t_xmjdxxDto.setXmjdid(xmjdxxDto.getIds().get(j));
					t_xmjdxxDtos.add(t_xmjdxxDto);
					xmjdxxDtos.remove(i);
					break;
				}
			}
		}
		result = dao.stageSort(t_xmjdxxDtos);
		return result != 0;
	}
	
	/**
	 * 根据frwid查询子任务所在阶段
	 */
	@Override
	public List<XmjdxxDto> getJdsByFrwid(XmjdrwDto xmjdrwDto){
		return dao.getJdsByFrwid(xmjdrwDto);
	}

	/**
	 * 批量新增阶段
	 */
	@Override
	public boolean insertByDtos(List<XmjdxxDto> xmjdDtos) {
		// TODO Auto-generated method stub
		int result = dao.insertByDtos(xmjdDtos);
		return result != 0;
	}

	/**
	 * 编辑项目阶段信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean modSaveProjectStage(XmjdxxDto xmjdxxDto) {
		// TODO Auto-generated method stub
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
			XmjdxxDto t_xmjdxxDto = dao.getDto(xmjdxxDto);
			Date date = dft.parse(t_xmjdxxDto.getSjjsrq());
			Date datejs = dft.parse(xmjdxxDto.getSjjsrq());
			Date dateks = dft.parse(xmjdxxDto.getSjksrq());
			xmjdxxDto.setSjksrq(DateTimeUtil.getFirstDayOfMonth(dateks));
			xmjdxxDto.setSjjsrq(DateTimeUtil.getLastDayOfMonth(datejs));
			if(!xmjdxxDto.getSjjsrq().equals(t_xmjdxxDto.getSjjsrq())){
				int count = getDifMonth(date, datejs);
				//查询后续阶段
				List<XmjdxxDto> xmjdxxDtos = dao.selectStageByXh(t_xmjdxxDto);
				if(xmjdxxDtos != null && xmjdxxDtos.size() > 0){
					for (XmjdxxDto dto : xmjdxxDtos) {
						cal.setTime(dft.parse(dto.getSjksrq()));
						cal.add(Calendar.MONTH, count);
						dto.setSjksrq(DateTimeUtil.getFirstDayOfMonth(cal.getTime()));
						cal.setTime(dft.parse(dto.getSjjsrq()));
						cal.add(Calendar.MONTH, count);
						dto.setSjjsrq(DateTimeUtil.getLastDayOfMonth(cal.getTime()));
					}
					int result = dao.updateByDtos(xmjdxxDtos);
					if(result == 0)
						return false;
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return updateDto(xmjdxxDto);
	}
	
	/**
	 * 计算日期差值
	 */
	public static int getDifMonth(Date startDate, Date endDate){
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);
        int result = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        int month = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
		return month + result;
    }

	/**
	 * 删除项目阶段
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean delStage(XmjdxxDto xmjdxxDto) {
		// TODO Auto-generated method stub

		XmjdxxDto dto = dao.getDto(xmjdxxDto);
		int result = dao.deleteById(xmjdxxDto.getXmjdid());
		if(result == 0)
			return false;
		
		// TODO Auto-generated method stub
		List<XmjdrwDto> xmjdrwDtos = xmjdrwDao.selectAllTaskByXmjdid(xmjdxxDto.getXmjdid());
		if(xmjdrwDtos != null && xmjdrwDtos.size() > 0){
			XmjdrwDto xmjdrwDto = new XmjdrwDto();
			List<String> rwlist=new ArrayList<>();
			for (XmjdrwDto value : xmjdrwDtos) {
				String rwid = value.getRwid();
				rwlist.add(rwid);
			}
			xmjdrwDto.setRwids(rwlist);
			result = xmjdrwDao.deleterwByRwid(xmjdrwDto);
			if(result > 0) {
				boolean isSuccess = gzglDao.deleteByYwid(rwlist);
				if(!isSuccess)
					return false;
			}
		}
		XmjdrwDto xmjddto = new XmjdrwDto();
		xmjddto.setXmid(dto.getXmid());
		List<XmjdrwDto> xmjdrwDtos1 = xmjdrwDao.selectXmrwxxs(xmjddto);
		XmglDto xmglDto = new XmglDto();
		xmglDto.setXmid(dto.getXmid());
		List<XmjdxxDto> xmjdxxDtos = dao.selectStageByXmid(xmglDto);
		for (XmjdrwDto xmjdrwDto : xmjdrwDtos1) {
			if (StringUtil.isNotBlank(xmjdrwDto.getJdtime())){
				String[] strings = xmjdrwDto.getJdtime().split(",");
				StringBuilder str = new StringBuilder();
				for (int i = 0; i < xmjdxxDtos.size(); i++) {
					if (i != Integer.parseInt(dto.getXh())-1){
						if (i<strings.length){
							if (StringUtil.isNotBlank(strings[i])){
								str.append(strings[i]);
							}else {
								str.append("-");
							}
						}else {
							str.append("-");
						}
						if (i!=xmjdxxDtos.size()-1)
							str.append(",");
					}
				}
				xmjdrwDto.setJdtime(str.toString());
			}else{
					if (StringUtil.isBlank(xmjdrwDto.getLrsj())){
						xmjdrwDto.setLrsj("-");
					}
					StringBuilder str= new StringBuilder("" + xmjdrwDto.getLrsj() + ",");
					for (int i = 0; i < xmjdxxDtos.size()-1; i++) {
						if (i!= xmjdxxDtos.size()-2){
							str.append("-,");
						}else {
							str.append("-");
						}
					}
				xmjdrwDto.setJdtime(str.toString());
			}
		}
		xmjdrwDao.updateXmjdxx(xmjdrwDtos1);
		return true;
	}
	
}
