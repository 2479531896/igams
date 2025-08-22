package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.HbsfbzDto;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjjcxmModel;
import com.matridx.igams.wechat.dao.entities.SjtssqDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.post.ISjjcxmDao;
import com.matridx.igams.wechat.dao.post.ISjxxDao;
import com.matridx.igams.wechat.service.svcinterface.IHbsfbzService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SjjcxmServiceImpl extends BaseBasicServiceImpl<SjjcxmDto, SjjcxmModel, ISjjcxmDao> implements ISjjcxmService{
	@Autowired
	IHbsfbzService hbsfbzService;
	@Autowired
	ISjxxDao sjxxDao;

	/**
	 * 根据送检信息新增检测项目
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean insertBySjxx(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		List<SjjcxmDto> sjjcxmDtos = sjxxDto.getSjjcxms();
		if(sjjcxmDtos != null && sjjcxmDtos.size() > 0){
			int result = dao.insertSjjcxmDtos(sjjcxmDtos);
            return result != 0;
		}
		return true;
	}

	@Override
	public boolean syncInfo(SjxxDto sjxxDto) throws BusinessException {
		List<SjjcxmDto> jcxmlist=(List<SjjcxmDto>) JSON.parseArray(sjxxDto.getJcxm(), SjjcxmDto.class);
		if (CollectionUtils.isEmpty(jcxmlist))
			throw new BusinessException("msg","检测项目数据为空！");
		dao.deleteSjjcxm(sjxxDto);
		List<SjjcxmDto> add_List = new ArrayList<>();
		List<SjjcxmDto> mod_List = new ArrayList<>();
		for (int i = jcxmlist.size()-1; i >= 0; i--) {
			if (StringUtil.isBlank(jcxmlist.get(i).getBs()))
				throw new BusinessException("msg","同步数据错误！");
			if ("1".equals(jcxmlist.get(i).getBs())){
				add_List.add(jcxmlist.get(i));
			}else{
				mod_List.add(jcxmlist.get(i));
			}
		}
		if (!CollectionUtils.isEmpty(add_List)){
			int result=dao.insertSjjcxmDtos(add_List);
			if (result==0)
				throw new BusinessException("msg","更新数据出错！");

		}
		if (!CollectionUtils.isEmpty(mod_List)){
			int result=dao.updateSyncInfo(mod_List);
			if (result==0)
				throw new BusinessException("msg","更新数据出错！");
		}
		return true;
	}

	@Override
	public Boolean insertSjjcxmDtos(List<SjjcxmDto> sjjcxmDtos) {
		return dao.insertSjjcxmDtos(sjjcxmDtos)!=0;
	}

	/**
	 * 根据送检信息新增检测项目
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean insertSjjcxm(SjxxDto sjxxDto,String yhid){
		// TODO Auto-generated method stub
		sjxxDto.setScry(yhid);
		String json = null;
		try {
		List<SjjcxmDto> jcxmlist=(List<SjjcxmDto>) JSON.parseArray(sjxxDto.getJcxm(), SjjcxmDto.class);
		if (CollectionUtils.isEmpty(jcxmlist))
			return false;
		SjjcxmDto dto = new SjjcxmDto();
		boolean flag = false;
		dto.setSjid(sjxxDto.getSjid());
		List<SjjcxmDto> dtoList = dao.getDtoList(dto);
		for (SjjcxmDto sjjcxmDto : dtoList) {
            if (StringUtil.isNotBlank(sjjcxmDto.getSfsf()) && "1".equals(sjjcxmDto.getSfsf()) && StringUtil.isNotBlank(sjjcxmDto.getSfje()) && !"0".equals(sjjcxmDto.getSfje())){
			//if (StringUtil.isNotBlank(sjjcxmDto.getSfsf()) && "1".equals(sjjcxmDto.getSfsf()) && StringUtil.isNotBlank(sjjcxmDto.getSfje())){
				flag = true;
				break;
			}
		}
		int delNum = dao.deleteSjjcxm(sjxxDto);
		int modNum = 0;
		HbsfbzDto hbsfbzDto=new HbsfbzDto();
		hbsfbzDto.setHbmc(sjxxDto.getDb());
		List<HbsfbzDto> hbsfbzDtos = hbsfbzService.getDtoList(hbsfbzDto);
		List<SjjcxmDto> add_List = new ArrayList<>();
		List<SjjcxmDto> mod_List = new ArrayList<>();
		SjxxDto dtoById = sjxxDao.getDtoById(sjxxDto.getSjid());
		for (int i = 0; i < jcxmlist.size(); i++) {
			SjjcxmDto sjjcxmDto = new SjjcxmDto();
			sjjcxmDto.setSjid(sjxxDto.getSjid());

			sjjcxmDto.setXh(String.valueOf(i + 1));
			//sjjcxmDto.setYfje(jcxmlist.get(i).getYfje());
			if(StringUtil.isBlank(jcxmlist.get(i).getYfje()))
				sjjcxmDto.setYfje("0");
			else
				sjjcxmDto.setYfje(jcxmlist.get(i).getYfje());
			sjjcxmDto.setSfje(jcxmlist.get(i).getSfje());
			sjjcxmDto.setFkrq(jcxmlist.get(i).getFkrq());
			sjjcxmDto.setSfsf(jcxmlist.get(i).getSfsf());
			sjjcxmDto.setJcxmid(jcxmlist.get(i).getJcxmid());
			sjjcxmDto.setJczxmid(jcxmlist.get(i).getJczxmid());
			if (StringUtil.isNotBlank(jcxmlist.get(i).getXmglid())){
				sjjcxmDto.setXmglid(jcxmlist.get(i).getXmglid());
				sjjcxmDto.setBs("0");
				sjjcxmDto.setXgry(yhid);
				//xg_flg==1代表普通修改，否则代表高级修改
				if(StringUtil.isNotBlank(sjxxDto.getXg_flg())&&"1".equals(sjxxDto.getXg_flg()) && "1".equals(jcxmlist.get(i).getSfsf())){
					//将当前伙伴与之前伙伴对比，如果修改了则重新给予应付金额
					if(dtoById!=null&&!dtoById.getDb().equals(sjxxDto.getDb())){
						sjjcxmDto.setYfje("0");
						if(hbsfbzDtos!=null&&hbsfbzDtos.size()>0){
							for(HbsfbzDto hbsfbzDto_t:hbsfbzDtos){
								if(sjjcxmDto.getJcxmid().equals(hbsfbzDto_t.getXm())){
									if(StringUtil.isNotBlank(sjjcxmDto.getJczxmid())){
										if(sjjcxmDto.getJczxmid().equals(hbsfbzDto_t.getZxm())){
											sjjcxmDto.setYfje(hbsfbzDto_t.getSfbz());
											break;
										}
									}else{
										sjjcxmDto.setYfje(hbsfbzDto_t.getSfbz());
										break;
									}
								}
							}
						}
					}
				}
				mod_List.add(sjjcxmDto);
			}else{
				sjjcxmDto.setXmglid(StringUtil.generateUUID());
				sjjcxmDto.setBs("1");
				sjjcxmDto.setLrry(yhid);
				if(StringUtil.isBlank(sjjcxmDto.getSfsf())){
					sjjcxmDto.setSfsf("1");
				}
				if(hbsfbzDtos!=null&&hbsfbzDtos.size()>0){
					for(HbsfbzDto hbsfbzDto_t:hbsfbzDtos){
						if(sjjcxmDto.getJcxmid().equals(hbsfbzDto_t.getXm())){
							if(StringUtil.isNotBlank(sjjcxmDto.getJczxmid())){
								if(sjjcxmDto.getJczxmid().equals(hbsfbzDto_t.getZxm())){
									sjjcxmDto.setYfje(hbsfbzDto_t.getSfbz());
									break;
								}
							}else{
								sjjcxmDto.setYfje(hbsfbzDto_t.getSfbz());
								break;
							}
						}
					}
				}
				add_List.add(sjjcxmDto);
			}
		}
		if (!CollectionUtils.isEmpty(add_List)){
			int result=dao.insertSjjcxmDtos(add_List);
			if (result==0)
				return false;

		}
		if (!CollectionUtils.isEmpty(mod_List)){
			modNum =dao.updateSyncInfo(mod_List);
			if (modNum == 0)
				return false;
		}
		if (flag && (delNum != modNum || !CollectionUtils.isEmpty(add_List) ))
			sjxxDto.setClbj("0");
		add_List.addAll(mod_List);
		json = JSONObject.toJSONString(add_List);
		} catch (Exception e) {
			if (!CollectionUtils.isEmpty(sjxxDto.getJcxmids())){
				dao.deleteSjjcxm(sjxxDto);
				List<SjjcxmDto> sjjcxmDtos = new ArrayList<>();
				for (int i = 0; i < sjxxDto.getJcxmids().size(); i++) {
					SjjcxmDto sjjcxmDto = new SjjcxmDto();
					sjjcxmDto.setSjid(sjxxDto.getSjid());
					sjjcxmDto.setXmglid(StringUtil.generateUUID());
					sjjcxmDto.setXh(String.valueOf(i + 1));
					sjjcxmDto.setJcxmid(sjxxDto.getJcxmids().get(i));
					sjjcxmDto.setBs("1");
					sjjcxmDto.setLrry(yhid);
					sjjcxmDto.setSfsf("1");
					String jczxmid = sjxxDto.getJczxm();
					if (StringUtil.isNotBlank(jczxmid)){
						sjjcxmDto.setJczxmid(jczxmid);
					}
					sjjcxmDtos.add(sjjcxmDto);
				}
				if (!CollectionUtils.isEmpty(sjjcxmDtos)){
					int result=dao.insertSjjcxmDtos(sjjcxmDtos);
					if (result==0)
						return false;
					json = JSONObject.toJSONString(sjjcxmDtos);
				}
			}
		}
		sjxxDto.setJcxm(json);
		return true;
	}

	@Override
	public Boolean updateListNew(List<SjjcxmDto> list) {
		return dao.updateListNew(list)!=0;
	}

	/**
	 * 获取检测项目所以数据
	 * @param sjjcxmDto
	 * @return
	 */
	@Override
	public List<SjjcxmDto> getAllDtoList(SjjcxmDto sjjcxmDto){
		return dao.getAllDtoList(sjjcxmDto);
	}

	@Override
	public int updateYszkInfoToNull(SjjcxmDto dto) {
		return dao.updateYszkInfoToNull(dto);
	}

	@Override
	public Boolean updateDcEmpty(SjjcxmDto dto) {
		return dao.updateDcEmpty(dto) != 0;
	}

	@Override
	public int updateListDzInfo(List<SjjcxmDto> sjjcxmDtos) {
		return dao.updateListDzInfo(sjjcxmDtos);
	}

	@Override
	public Boolean updateList(List<SjjcxmDto> sjjcxmDtos) {
		return dao.updateList(sjjcxmDtos)!=0;
	}

	@Override
	public List<String> getSjjcxm(String sjid){
		// TODO Auto-generated method stub
		
		return dao.getSjjcxm(sjid);
	}
	
	/**
	 * 根据送检信息修改检测项目
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean updateBySjxx(SjxxDto sjxxDto,SjxxDto oldDto) {
		// TODO Auto-generated method stub
		String json;
		List<SjjcxmDto> list = sjxxDto.getSjjcxms();
		HbsfbzDto hbsfbzDto=new HbsfbzDto();
		hbsfbzDto.setHbmc(sjxxDto.getDb());

		List<HbsfbzDto> hbsfbzDtos = hbsfbzService.getDtoList(hbsfbzDto);
		sjxxDto.setScry(sjxxDto.getXgry());
		List<SjjcxmDto> sjjcxmDtos_old = dao.getDtoList(sjxxDto.getSjid());
		dao.deleteSjjcxm(sjxxDto);
		List<SjjcxmDto> sjjcxmDtos = new ArrayList<>();
		List<SjjcxmDto> updateList = new ArrayList<>();
		if (list !=null && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				SjjcxmDto sjjcxmDto = new SjjcxmDto();
				sjjcxmDto.setSjid(sjxxDto.getSjid());
				sjjcxmDto.setXh(String.valueOf(i+1));
				sjjcxmDto.setJcxmid(list.get(i).getJcxmid());
				if(StringUtil.isNotBlank(list.get(i).getJczxmid())){
					sjjcxmDto.setJczxmid(list.get(i).getJczxmid());
				}
				//判断历史数据中是否有检测项目/子项目一致的数据，若一致，设置xmglid
				for (SjjcxmDto dto : sjjcxmDtos_old) {
					if (dto.getJcxmid().equals(sjjcxmDto.getJcxmid())){
						if (((!StringUtil.isAnyBlank(dto.getJczxmid(),sjjcxmDto.getJczxmid()) && dto.getJczxmid().equals(sjjcxmDto.getJczxmid()))
								|| (StringUtil.isAllBlank(dto.getJczxmid(),sjjcxmDto.getJczxmid())))
								&& StringUtil.isNotBlank(dto.getXmglid())){
							list.get(i).setXmglid(dto.getXmglid());
						}
					}
				}
				boolean isExist = false;
				if (StringUtil.isNotBlank(list.get(i).getXmglid())){
					if (sjjcxmDtos_old!=null && sjjcxmDtos_old.size()>0){
						for (SjjcxmDto dto : sjjcxmDtos_old) {
							if (list.get(i).getXmglid().equals(dto.getXmglid())){
								//将当前伙伴与之前伙伴对比，如果修改了则重新给予应付金额
								if(oldDto!=null && !oldDto.getDb().equals(sjxxDto.getDb()) && "1".equals(dto.getSfsf()) ){
									sjjcxmDto.setYfje("0");
									for(HbsfbzDto sfbzdto:hbsfbzDtos){
										if(sjjcxmDto.getJcxmid().equals(sfbzdto.getXm())){
											if(StringUtil.isNotBlank(sjjcxmDto.getJczxmid())){
												if(sjjcxmDto.getJczxmid().equals(sfbzdto.getZxm())){
													sjjcxmDto.setYfje(sfbzdto.getSfbz());
													break;
												}
											}else{
												sjjcxmDto.setYfje(sfbzdto.getSfbz());
												break;
											}
										}
									}
								}
								isExist = true;
								break;
							}
						}
					}
				}
				if (isExist){
					sjjcxmDto.setXmglid(list.get(i).getXmglid());
					sjjcxmDto.setXgry(sjxxDto.getScry());
					updateList.add(sjjcxmDto);
				}else{
					sjjcxmDto.setBs(StringUtil.isNotBlank(list.get(i).getXmglid())?"0":"1");
					sjjcxmDto.setXmglid(StringUtil.isNotBlank(list.get(i).getXmglid())?list.get(i).getXmglid():StringUtil.generateUUID());
					sjjcxmDto.setSfsf("1");
					sjjcxmDto.setLrry(sjxxDto.getLrry());
					for(HbsfbzDto dto:hbsfbzDtos){
						if(sjjcxmDto.getJcxmid().equals(dto.getXm())){
							if(StringUtil.isNotBlank(sjjcxmDto.getJczxmid())){
								if(sjjcxmDto.getJczxmid().equals(dto.getZxm())){
									sjjcxmDto.setYfje(dto.getSfbz());
									break;
								}
							}else{
								sjjcxmDto.setYfje(dto.getSfbz());
								break;
							}
						}
					}
					sjjcxmDtos.add(sjjcxmDto);
				}
			}
		}
		if(sjjcxmDtos!=null&&sjjcxmDtos.size()>0){
			int result = dao.insertSjjcxmDtos(sjjcxmDtos);
			if(result == 0)
				return false;
		}
		if(updateList!=null&&updateList.size()>0){
			boolean result = dao.revertData(updateList);
			if(!result)
				return false;
		}

		json = JSONObject.toJSONString(sjjcxmDtos);
		sjxxDto.setJcxm(json);
		return true;
	}
	
	/**
	 * 获取检测项目信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjjcxmDto> getSjjcxmDtos(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getSjjcxmDtos(sjxxDto);
	}
	
	/**
	 * 根据sjid查询检测项目信息
	 * @param sjids
	 * @return
	 */
	@Override
	public List<SjjcxmDto> getListBySjid(List<String> sjids) {
		// TODO Auto-generated method stub
		return dao.getListBySjid(sjids);
	}
	/**
	 * 根据sjid清空检测子项目
	 * @param sjid
	 * @return
	 */
	@Override
	public boolean emptySubDetect(String sjid) {
		// TODO Auto-generated method stub
		return dao.emptySubDetect(sjid);
	}

	/**
	 * 根据送检信息更新检测项目
	 * @param sjjcxmDtos
	 * @return
	 */
	public int  updateSjjcxmDtos(List<SjjcxmDto> sjjcxmDtos){
		return dao.updateSjjcxmDtos(sjjcxmDtos);
	}

	/**
	 * 重新调整应付金额
	 * @return
	 */
	public void readjustPayment(SjxxDto sjxxDto,List<SjjcxmDto> sjjcxmDtos,List<SjtssqDto> sjtssqDtos){
		HbsfbzDto hbsfbzDto=new HbsfbzDto();
		hbsfbzDto.setHbmc(sjxxDto.getDb());
		List<HbsfbzDto> hbsfbzDtos = hbsfbzService.getDtoList(hbsfbzDto);
		if(hbsfbzDtos!=null&&hbsfbzDtos.size()>0&&sjjcxmDtos!=null&&sjjcxmDtos.size()>0&&sjtssqDtos!=null&&sjtssqDtos.size()>0){
			BigDecimal sum=new BigDecimal("0");
			for(SjjcxmDto sjjcxmDto_t:sjjcxmDtos){
				boolean isFind=false;
				boolean MDFind=false;
				boolean MRFind=false;
				for(SjtssqDto sjtssqDto:sjtssqDtos){
					if(sjjcxmDto_t.getJcxmid().equals(sjtssqDto.getJcxmid())){
						isFind=true;
						if("MD".equals(sjtssqDto.getSqzxmdm())){
							MDFind=true;
							if(MRFind){
								sjjcxmDto_t.setYfje("0");
								sjjcxmDto_t.setSfsf("0");
							}else{
								for(HbsfbzDto hbsfbzDto_t:hbsfbzDtos){
									if("R".equals(hbsfbzDto_t.getXmcskz1())&&sjjcxmDto_t.getCskz3().equals(hbsfbzDto_t.getXmcskz3())){
										sjjcxmDto_t.setYfje(hbsfbzDto_t.getSfbz());
										break;
									}
								}
							}
						}else if("MR".equals(sjtssqDto.getSqzxmdm())){
							MRFind=true;
							if(MDFind){
								sjjcxmDto_t.setYfje("0");
								sjjcxmDto_t.setSfsf("0");
							}else{
								for(HbsfbzDto hbsfbzDto_t:hbsfbzDtos){
									if("D".equals(hbsfbzDto_t.getXmcskz1())&&sjjcxmDto_t.getCskz3().equals(hbsfbzDto_t.getXmcskz3())){
										sjjcxmDto_t.setYfje(hbsfbzDto_t.getSfbz());
										break;
									}
								}
							}
						}else if("MF".equals(sjtssqDto.getSqzxmdm())){
							sjjcxmDto_t.setYfje("0");
							sjjcxmDto_t.setSfsf("0");
							break;
						}
					}
				}
				if(!isFind){
					for(HbsfbzDto hbsfbzDto_t:hbsfbzDtos){
						if(sjjcxmDto_t.getJcxmid().equals(hbsfbzDto_t.getXm())){
							if(StringUtil.isNotBlank(sjjcxmDto_t.getJczxmid())){
								if(sjjcxmDto_t.getJczxmid().equals(hbsfbzDto_t.getZxm())){
									sjjcxmDto_t.setYfje(hbsfbzDto_t.getSfbz());
									break;
								}
							}else{
								sjjcxmDto_t.setYfje(hbsfbzDto_t.getSfbz());
								break;
							}
						}
					}
				}
				if(StringUtil.isNotBlank( sjjcxmDto_t.getYfje())){
					sum=sum.add(new BigDecimal(sjjcxmDto_t.getYfje()));
				}
			}
			sjxxDto.setFkje(sum.toString());
		}
	}

	/**
	 * 更新导出标记
	 * @param sjjcxmDto
	 * @return
	 */
	public boolean updateDcbj(SjjcxmDto sjjcxmDto){
		return dao.updateDcbj(sjjcxmDto);
	}
}
