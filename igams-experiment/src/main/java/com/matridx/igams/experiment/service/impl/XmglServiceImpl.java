package com.matridx.igams.experiment.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.post.IGzglDao;
import com.matridx.igams.common.enums.OpennessTypeEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.JdmbDto;
import com.matridx.igams.experiment.dao.entities.XmcyDto;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmglModel;
import com.matridx.igams.experiment.dao.entities.XmjdrwDto;
import com.matridx.igams.experiment.dao.entities.XmjdxxDto;
import com.matridx.igams.experiment.dao.post.IXmglDao;
import com.matridx.igams.experiment.dao.post.IXmjdrwDao;
import com.matridx.igams.experiment.service.svcinterface.IJdmbService;
import com.matridx.igams.experiment.service.svcinterface.IXmcyService;
import com.matridx.igams.experiment.service.svcinterface.IXmglService;
import com.matridx.igams.experiment.service.svcinterface.IXmjdxxService;
import com.matridx.igams.experiment.service.svcinterface.IXmscxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateTimeUtil;

@Service
public class XmglServiceImpl extends BaseBasicServiceImpl<XmglDto, XmglModel, IXmglDao> implements IXmglService{
	@Autowired
	private IXmjdxxService xmjdxxService;
	@Autowired
	private IXmjdrwDao xmjdrwDao;
	@Autowired
	IXmcyService xmcyService;
	@Autowired
	IXmscxxService xmscxxService;
	@Autowired
	IJdmbService jdmbService;
	@Autowired
	IGzglDao gzglDao;
	
	/** 
	 * 插入项目信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(XmglDto xmglDto){
		xmglDto.setXmid(StringUtil.generateUUID());
		int result = dao.insert(xmglDto);
		return result != 0;
	}
	
	/**
	 * 获取个人项目列表
	 */
	@Override
	public List<XmglDto> getPersionDtoList(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		return dao.getPersionDtoList(xmglDto);
	}
	
	/**
	 * 删除项目或文件夹
	 */
	@Override
	public boolean deleteByXmid(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		boolean isSuccess = xmscxxService.insertByXmglDto(xmglDto);
		if(!isSuccess)
			return false;
		int result = dao.deleteByXmid(xmglDto);
		return result != 0;
	}
	
	/**
	 * 项目合并到文件夹
	 */
	@Override
	public boolean projectPacking(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		boolean result = update(xmglDto);
		if(!result)
			return false;
		//判断项目公开性
		XmglDto f_xmglDto = new XmglDto();
		f_xmglDto.setXmid(xmglDto.getFxmid());
		f_xmglDto = dao.getDto(f_xmglDto);
		XmglDto z_xmglDto = dao.getDto(xmglDto);
		if((!OpennessTypeEnum.PRIVATE_PROJECT.getCode().equals(z_xmglDto.getXmgkx()) && OpennessTypeEnum.PRIVATE_PROJECT.getCode().equals(f_xmglDto.getXmgkx())) 
				|| (OpennessTypeEnum.SHARE_PROJECT.getCode().equals(z_xmglDto.getXmgkx()) && OpennessTypeEnum.PUBLIC_PROJECT.getCode().equals(f_xmglDto.getXmgkx()))){
			f_xmglDto.setXmgkx(z_xmglDto.getXmgkx());
			result = update(f_xmglDto);
			if(!result)
				return false;
		}
		//判断项目成员
		result = xmcyService.addMemberToFxm(z_xmglDto);
		return result;
	}
	
	/**
	 * 项目移动到上级目录
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean projectReturnTop(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		XmglDto f_xmglDto = new XmglDto();
		f_xmglDto.setXmid(xmglDto.getFxmid());
		f_xmglDto = dao.getDto(f_xmglDto);
		xmglDto.setFxmid(f_xmglDto.getFxmid());
		int result = dao.updateFxmid(xmglDto);
		if(result == 0)
			return false;
		//查询子节点
		List<XmglDto> xmglDtos = dao.selectByFxmid(f_xmglDto);
		//判断项目公开性
		if(OpennessTypeEnum.PUBLIC_PROJECT.getCode().equals(f_xmglDto.getXmgkx()) || OpennessTypeEnum.SHARE_PROJECT.getCode().equals(f_xmglDto.getXmgkx())){
			boolean share = true;
			boolean pub = true;
			if(xmglDtos != null && xmglDtos.size() > 0){
				for (XmglDto dto : xmglDtos) {
					if (OpennessTypeEnum.SHARE_PROJECT.getCode().equals(dto.getXmgkx())) {
						share = false;
						break;
					}
					if (OpennessTypeEnum.PUBLIC_PROJECT.getCode().equals(dto.getXmgkx())) {
						pub = false;
					}
				}
			}
			if(share && pub){
				f_xmglDto.setXmgkx(OpennessTypeEnum.PRIVATE_PROJECT.getCode());
				result = dao.updateToPrivate(f_xmglDto);
				if(result == 0)
					return false;
			}else if(!share && OpennessTypeEnum.PUBLIC_PROJECT.getCode().equals(f_xmglDto.getXmgkx())) {
				// 需要修改父项目
				f_xmglDto.setXmgkx(OpennessTypeEnum.SHARE_PROJECT.getCode());
				result = dao.updateFxmOpenness(f_xmglDto);
				if(result == 0)
					return false;
			}else if(share && OpennessTypeEnum.SHARE_PROJECT.getCode().equals(f_xmglDto.getXmgkx())) {
				f_xmglDto.setXmgkx(OpennessTypeEnum.PUBLIC_PROJECT.getCode());
				result = dao.updateToPrivate(f_xmglDto);
				if(result == 0)
					return false;
			}
		}
		//查询剩余人员
		List<String> xmids = new ArrayList<>();
		if(xmglDtos != null && xmglDtos.size() > 0){
			for (XmglDto dto : xmglDtos) {
				xmids.add(dto.getXmid());
			}
		}
		List<String> yhids = xmcyService.selectYhidGroup(xmids);
		f_xmglDto.setIds(yhids);
		return xmcyService.updateMemberByYhids(f_xmglDto);
	}
	
	/**
	 * 新增项目和模板
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSaveXm(XmglDto xmglDto){
		//判断模板
		if(StringUtil.isBlank(xmglDto.getMbid())){
			xmglDto.setMbid("default");
		}
		boolean isSuccess = insertDto(xmglDto); 
		if(!isSuccess)
			return false;
		//修改项目公开性信息
		isSuccess = updateXmgkx(xmglDto);
		if(!isSuccess)
			return false;
		//新增模板信息
		isSuccess = SaveXmModel(xmglDto);
		if(!isSuccess)
			return false;
		//项目添加登录人员
		XmcyDto xmcyDto = new XmcyDto();
		xmcyDto.setYhid(xmglDto.getLrry());
		xmcyDto.setXh("1");
		xmcyDto.setXmid(xmglDto.getXmid());
		isSuccess = xmcyService.insert(xmcyDto);
		return isSuccess;
	}
	
	
	/**
	 * 新增项目
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean  SaveXmModel(XmglDto xmglDto) {
		List<JdmbDto> jdmbDtos = jdmbService.selectJdmbByMbid(xmglDto.getMbid());
			if(jdmbDtos!=null && jdmbDtos.size()>0){
				List<XmjdxxDto> xmjdDtos = new ArrayList<>();
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		        String preMonth = dft.format(cal.getTime());
				for (JdmbDto jdmbDto : jdmbDtos) {
					XmjdxxDto xmjdDto = new XmjdxxDto();
					xmjdDto.setXmjdid(StringUtil.generateUUID());
					xmjdDto.setXmid(xmglDto.getXmid());
					xmjdDto.setXh(jdmbDto.getXh());
					xmjdDto.setJdid(jdmbDto.getJdid());
					xmjdDto.setJdmc(jdmbDto.getJdmc());
					xmjdDto.setLrry(xmglDto.getLrry());
					if (jdmbDto.getQx() != null && !Objects.equals(jdmbDto.getQx(), "")) {
						int month = Integer.parseInt(jdmbDto.getQx());
						cal.add(Calendar.MONTH, month - 1);
					}
					xmjdDto.setJhksrq(preMonth);
					xmjdDto.setSjksrq(preMonth);
					xmjdDto.setJhjsrq(DateTimeUtil.getLastDayOfMonth(cal.getTime()));
					xmjdDto.setSjjsrq(DateTimeUtil.getLastDayOfMonth(cal.getTime()));
					xmjdDtos.add(xmjdDto);
					cal.add(Calendar.MONTH, 1);
					preMonth = dft.format(cal.getTime());
				}
				return xmjdxxService.insertByDtos(xmjdDtos);
			}
		return true;
	}
	

	/**
	 * 获取公开项目列表
	 */
	@Override
	public List<XmglDto> getPublicDtoList(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		return dao.getPublicDtoList(xmglDto);
	}

	/**
	 * 获取项目公开类型
	 */
	@Override
	public List<OpennessTypeEnum> getOpennessType() {
		// TODO Auto-generated method stub
		return new ArrayList<>(Arrays.asList(OpennessTypeEnum.values()));
	}
	
	/**
	 * 根据输入内容查询系统用户信息
	 */
	@Override
	public List<XmglDto> selectXtyh(XmglDto xmglDto){
		return dao.selectXtyh(xmglDto);
	}

	/**
	 * 彻底删除项目
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean completeDelProject(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		//查询项目下所有任务
		List<XmjdrwDto> xmjdrwDtos = xmjdrwDao.getAllXmjdrwByXmid(xmglDto.getXmid());
		//清理删除信息表数据
		boolean isSuccess = xmscxxService.deleteByXmglDto(xmglDto);
		if(!isSuccess)
			return false;
		int result = dao.completeDelProject(xmglDto);
		if(result == 0)
			return false;
		if(StringUtil.isNotBlank(xmglDto.getFxmid())){
			//查询未彻底删除子项目
			XmglDto f_xmglDto = new XmglDto();
			f_xmglDto.setXmid(xmglDto.getFxmid());
			f_xmglDto = dao.getDto(f_xmglDto);
			List<XmglDto> zxmglDtos = dao.selectAllZxm(f_xmglDto);
			if(zxmglDtos != null && zxmglDtos.size() > 0){
				//查询剩余人员
				List<String> xmids = new ArrayList<>();
				for (XmglDto zxmglDto : zxmglDtos) {
					xmids.add(zxmglDto.getXmid());
				}
				List<String> yhids = xmcyService.selectYhidGroup(xmids);
				f_xmglDto.setIds(yhids);
				isSuccess = xmcyService.updateMemberByYhids(f_xmglDto);
				if(!isSuccess)
					return false;
			}
			//判断项目公开性
			if(OpennessTypeEnum.PUBLIC_PROJECT.getCode().equals(f_xmglDto.getXmgkx()) || OpennessTypeEnum.SHARE_PROJECT.getCode().equals(f_xmglDto.getXmgkx())){
				boolean share = true;
				boolean pub = true;
				if(zxmglDtos != null && zxmglDtos.size() > 0){
					for (XmglDto zxmglDto : zxmglDtos) {
						if (OpennessTypeEnum.SHARE_PROJECT.getCode().equals(zxmglDto.getXmgkx())) {
							share = false;
							break;
						}
						if (OpennessTypeEnum.PUBLIC_PROJECT.getCode().equals(zxmglDto.getXmgkx())) {
							pub = false;
						}
					}
				}
				if(share && pub){
					f_xmglDto.setXmgkx(OpennessTypeEnum.PRIVATE_PROJECT.getCode());
					result = dao.updateToPrivate(f_xmglDto);
					if(result == 0)
						return false;
				}else if(!share && OpennessTypeEnum.PUBLIC_PROJECT.getCode().equals(f_xmglDto.getXmgkx())) {
					// 需要修改父项目
					f_xmglDto.setXmgkx(OpennessTypeEnum.SHARE_PROJECT.getCode());
					result = dao.updateFxmOpenness(f_xmglDto);
					if(result == 0)
						return false;
				}else if(share && OpennessTypeEnum.SHARE_PROJECT.getCode().equals(f_xmglDto.getXmgkx())) {
					f_xmglDto.setXmgkx(OpennessTypeEnum.PUBLIC_PROJECT.getCode());
					result = dao.updateToPrivate(f_xmglDto);
					if(result == 0)
						return false;
				}
			}
		}
		if(xmjdrwDtos != null && xmjdrwDtos.size() > 0){
			XmjdrwDto xmjdrwDto = new XmjdrwDto();
			List<String> rwlist= new ArrayList<>();
			for (XmjdrwDto dto : xmjdrwDtos) {
				String rwid = dto.getRwid();
				rwlist.add(rwid);
			}
			xmjdrwDto.setRwids(rwlist);
			result = xmjdrwDao.deleterwByRwid(xmjdrwDto);
			if(result > 0) {
				isSuccess = gzglDao.deleteByYwid(rwlist);
				return isSuccess;
			}
		}
		return true;
	}

	/**
	 * 还原项目
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean recoverProject(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		//清理删除信息表数据
		boolean isSuccess = xmscxxService.deleteByXmglDto(xmglDto);
		if(!isSuccess)
			return false;
		int result = dao.recoverProject(xmglDto);
		return result != 0;
	}

	/**
	 * 判断项目父文件夹是否存在
	 */
	@Override
	public boolean confirmFxmDto(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(xmglDto.getFxmid())){
			XmglDto t_xmglDto = new XmglDto();
			t_xmglDto.setXmid(xmglDto.getFxmid());
			XmglDto s_xmglDto = dao.getDtoById(t_xmglDto.getXmid());
			return s_xmglDto != null;
		}
		return true;
	}

	/**
	 * 还原项目到根目录
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean recoverProjecPath(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		//清理删除信息表数据
		boolean isSuccess = xmscxxService.deleteByXmglDto(xmglDto);
		if(!isSuccess)
			return false;
		int result = dao.recoverProjecPath(xmglDto);
		if(result == 0)
			return false;
		result = dao.recoverProject(xmglDto);
		if(result == 0)
			return false;
		//查询未彻底删除子项目
		XmglDto f_xmglDto = new XmglDto();
		f_xmglDto.setXmid(xmglDto.getFxmid());
		f_xmglDto = dao.getDto(f_xmglDto);
		List<XmglDto> zxmglDtos = dao.selectAllZxm(f_xmglDto);
		if(zxmglDtos != null && zxmglDtos.size() > 0){
			//查询剩余人员
			List<String> xmids = new ArrayList<>();
			for (XmglDto zxmglDto : zxmglDtos) {
				xmids.add(zxmglDto.getXmid());
			}
			List<String> yhids = xmcyService.selectYhidGroup(xmids);
			f_xmglDto.setIds(yhids);
			isSuccess = xmcyService.updateMemberByYhids(f_xmglDto);
			if(!isSuccess)
				return false;
		}
		//判断项目公开性
		if(OpennessTypeEnum.PUBLIC_PROJECT.getCode().equals(f_xmglDto.getXmgkx()) || OpennessTypeEnum.SHARE_PROJECT.getCode().equals(f_xmglDto.getXmgkx())){
			boolean share = true;
			boolean pub = true;
			if(zxmglDtos != null && zxmglDtos.size() > 0){
				for (XmglDto zxmglDto : zxmglDtos) {
					if (OpennessTypeEnum.SHARE_PROJECT.getCode().equals(zxmglDto.getXmgkx())) {
						share = false;
						break;
					}
					if (OpennessTypeEnum.PUBLIC_PROJECT.getCode().equals(zxmglDto.getXmgkx())) {
						pub = false;
					}
				}
			}
			if(share && pub){
				f_xmglDto.setXmgkx(OpennessTypeEnum.PRIVATE_PROJECT.getCode());
				result = dao.updateToPrivate(f_xmglDto);
				return result != 0;
			}else if(!share && OpennessTypeEnum.PUBLIC_PROJECT.getCode().equals(f_xmglDto.getXmgkx())) {
				// 需要修改父项目
				f_xmglDto.setXmgkx(OpennessTypeEnum.SHARE_PROJECT.getCode());
				result = dao.updateFxmOpenness(f_xmglDto);
				return result != 0;
			}else if(share && OpennessTypeEnum.SHARE_PROJECT.getCode().equals(f_xmglDto.getXmgkx())) {
				f_xmglDto.setXmgkx(OpennessTypeEnum.PUBLIC_PROJECT.getCode());
				result = dao.updateToPrivate(f_xmglDto);
				return result != 0;
			}
		}
		
		return true;
	}
	
	/**
	 * 公开项目状态
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateFxmOpenness(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		int result = dao.updateFxmOpenness(xmglDto);
		return result != 0;
	}

	/**
	 * 新增文件夹
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSaveFolder(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		boolean isSuccess = insertDto(xmglDto);
		if(!isSuccess)
			return false;
		//项目添加登录人员
		XmcyDto xmcyDto = new XmcyDto();
		xmcyDto.setYhid(xmglDto.getLrry());
		xmcyDto.setXh("1");
		xmcyDto.setXmid(xmglDto.getXmid());
		isSuccess = xmcyService.insert(xmcyDto);
		return isSuccess;
	}

	/**
	 * 编辑项目保存
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean modSaveProject(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		XmglDto t_xmglDto = dao.getDto(xmglDto);
		int result = dao.update(xmglDto);
		if(result == 0)
			return false;
		if(StringUtil.isBlank(t_xmglDto.getXmgkx())||(!t_xmglDto.getXmgkx().equals(xmglDto.getXmgkx()))){
			return updateXmgkx(xmglDto);
		}
		return true;
	}
	
	/**
	 * 修改项目公开性信息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateXmgkx(XmglDto xmglDto){
		//查询所有父节点
		List<XmglDto> f_xmglDtos = dao.selectProjectStatus(xmglDto);
		if(OpennessTypeEnum.PRIVATE_PROJECT.getCode().equals(xmglDto.getXmgkx())){
			if(f_xmglDtos != null && f_xmglDtos.size() > 0){
				for (int i = 0; i < f_xmglDtos.size(); i++) {
					if(!f_xmglDtos.get(i).getXmid().equals(xmglDto.getXmid())){
						//查询子节点信息
						List<XmglDto> z_xmglDtos = dao.selectByFxmid(f_xmglDtos.get(i));
						if(z_xmglDtos != null && z_xmglDtos.size() > 0){
							for (XmglDto z_xmglDto : z_xmglDtos) {
								//判断是否存在公开项目
								if (!z_xmglDto.getXmid().equals(xmglDto.getXmid()) && (OpennessTypeEnum.PUBLIC_PROJECT.getCode().equals(z_xmglDto.getXmgkx()) || OpennessTypeEnum.SHARE_PROJECT.getCode().equals(z_xmglDto.getXmgkx())) && !z_xmglDto.getXmid().equals(f_xmglDtos.get(i - 1).getFxmid())) {
									return true;
								}
							}
						}
					}
					//文件夹改为私有
					f_xmglDtos.get(i).setXmgkx(OpennessTypeEnum.PRIVATE_PROJECT.getCode());
					int result = dao.updateToPrivate(f_xmglDtos.get(i));
					if(result == 0)
						return false;
				}
			}
		}else{
			return updateFxmOpenness(xmglDto);
		}
		return true;
	}

	@Override
	public List<XmglDto> selectcatalogue(XmglDto xmglDto){
		// TODO Auto-generated method stub
		return dao.selectcatalogue(xmglDto);
	}

	/**
	 * 判断当前用户是否有删除权限
	 */
	@Override
	public boolean delPermission(XmglDto xmglDto) {
		// TODO Auto-generated method stub
		List<XmglDto> xmglDtos = dao.delPermission(xmglDto);
		return xmglDtos == null || xmglDtos.size() <= 0;
	}
	
	/**
	 * 校验项目名称是否已存在
	 */
	public boolean selectXmByXmmc(XmglDto xmglDto) {
		return dao.selectXmByXmmc(xmglDto) == null || dao.selectXmByXmmc(xmglDto).size() <= 0;
	}
	
}
