package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.entities.QgmxModel;
import com.matridx.igams.production.dao.post.IQgmxDao;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.production.util.FileZipThread;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QgmxServiceImpl extends BaseBasicServiceImpl<QgmxDto, QgmxModel, IQgmxDao> implements IQgmxService{
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	IFjcfbService fjcfbService;
	
	@Autowired
	ICkhwxxService ckhwxxService;
	
	@Value("${matridx.fileupload.prefix}")
	private String prefix;
	
	@Value("${matridx.fileupload.tempPath}")
	private String tempFilePath;
	
	@Autowired
	IShgcService shgcService;

	private final Logger log = LoggerFactory.getLogger(QgmxServiceImpl.class);
	
	@Override 
	public List<QgmxDto> getPagedDtoList(QgmxDto qgmxDto){
		List<QgmxDto> t_List = dao.getPagedDtoList(qgmxDto);
		if(!CollectionUtils.isEmpty(t_List)){
			// 查询附件信息
			for (QgmxDto dto : t_List) {
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(qgmxDto.getQgid());
				fjcfbDto.setZywid(dto.getQgmxid());
				List<FjcfbDto> fjcfbDtos = fjcfbService.getListByZywid(fjcfbDto);
				if(!CollectionUtils.isEmpty(fjcfbDtos)) {
					dto.setFjbj("0");
				}
			}
		}
		return t_List;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(QgmxDto qgmxDto) {
		qgmxDto.setQgmxid(StringUtil.generateUUID());
		qgmxDto.setZt(StatusEnum.CHECK_PASS.getCode());
		int result =dao.insert(qgmxDto);
		return result > 0;
	}
	
	/**
	 * 新增采购明细数据
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertQgmx(List<QgmxDto> list) {
		if(!CollectionUtils.isEmpty(list)) {
			for (QgmxDto qgmxDto : list) {
				if (StringUtil.isBlank(qgmxDto.getQgmxid())) {
					qgmxDto.setQgmxid(StringUtil.generateUUID());
				}
				if (StringUtil.isBlank(qgmxDto.getZt())) {
					qgmxDto.setZt(StatusEnum.CHECK_PASS.getCode());
				}
				if (StringUtil.isBlank(qgmxDto.getHwjldw())) {
					qgmxDto.setHwjldw(null);
				}
				if (StringUtil.isBlank(qgmxDto.getHwbz())) {
					qgmxDto.setHwbz(null);
				}
			}
		}
		return dao.insertQgmx(list);
	}
	
	/**
	 * 取消采购操作
	 */
	@Override
	public boolean updateQxqg(String qgmxid) {
		return dao.updateQxqg(qgmxid);
	}

	/**
	 * 修改采购明细
	 */
	@Override
	public boolean updateForList(List<QgmxDto> list) {
		// TODO Auto-generated method stub
		return dao.updateForList(list);
	}
	
	/**
	 * 采购明细修改
	 */
	@Override
	public boolean updateQgmx(List<QgmxDto> list) {
		return dao.updateQgmx(list);
	}
	
	/**
	 * 根据物料子类别统称筛选条件查询相关请购明细数据
	 */
	public List<QgmxDto> getMxfjByWlzlbtc(QgmxDto qgmxDto){
		return dao.getMxfjByWlzlbtc(qgmxDto);
	}
	
	/**
	 * 下载请购明细附件
	 */
	public Map<String,Object> downloadQgmxFile(QgmxDto qgmxDto){
		Map<String, Object> map = new HashMap<>();
		try {
			List<QgmxDto> qgmxDtos=dao.getMxfjByWlzlbtc(qgmxDto);
			// TODO Auto-generated method stub
			//判断是否选中
			List<String> qgmxids = new ArrayList<>();
			if(!CollectionUtils.isEmpty(qgmxDtos)) {
				for (QgmxDto dto : qgmxDtos) {
					qgmxids.add(dto.getQgmxid());
				}
				qgmxDto.setIds(qgmxids);
				if(!CollectionUtils.isEmpty(qgmxids)){
					qgmxDtos=dao.getFileByYwidAndZywid(qgmxDto);
				}
				
				String key = String.valueOf(System.currentTimeMillis());
				redisUtil.hset("EXP_:_" + key, "Cnt", "0",3600);
				map.put("count", qgmxDtos.size());
				map.put("redisKey", key);
				String folderName = "UP" + System.currentTimeMillis();
				String storePath = prefix + tempFilePath + BusTypeEnum.IMP_PURCHASEZIP.getCode() + "/" + folderName;
				mkDirs(storePath);
				map.put("srcDir", storePath);
				// 开启线程拷贝临时文件
				FileZipExport fileZipExport = new FileZipExport();
				fileZipExport.init(key, storePath, folderName, qgmxDtos, redisUtil);
				FileZipThread reportZipThread = new FileZipThread(fileZipExport);
				reportZipThread.start();
				map.put("status", "success");
			}else {
				map.put("status", "success");
				map.put("message","未查询到相关附件!");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage());
		}

		return map;
	}
	
	/**
	 * 根据路径创建文件
	 */
	private void mkDirs(String storePath)
	{
		File file = new File(storePath);
		if (file.isDirectory())
		{
			return;
		}
		file.mkdirs();
	}
	
	/**
	 * 根据业务ID和子业务ID获取附件信息
	 */
	public List<QgmxDto> getFileByYwidAndZywid(QgmxDto qgmxDto){
		return dao.getFileByYwidAndZywid(qgmxDto);
	}
	
    
    /**
     * 根据请购明细ID清空关联ID
     */
    public boolean clearGlid(String qgmxid) {
        return dao.clearGlid(qgmxid);
    }

    /**
	 * 更新取消请购和拒绝审批信息
	*/
	public boolean updateQxqgxx(QgmxDto qgmxDto) {
		return dao.updateQxqgxx(qgmxDto);
	}
	
    /**
	 * 更新拒绝审批信息
	 */
    public boolean updateJjspxx(QgmxDto qgmxDto) {
    	return dao.updateJjspxx(qgmxDto);
    }
    
    /**
     * 请购明细列表(不分页)
     */
    public List<QgmxDto> getQgmxList(QgmxDto qgmxDto){
		//    	if(!"copy".equals(qgmxDto.getRequestName())) {
//    		if(!CollectionUtils.isEmpty(t_List)){
//    			// 查询附件信息
//    			for (int i = 0; i < t_List.size(); i++) {
//    				FjcfbDto fjcfbDto = new FjcfbDto();
//    				fjcfbDto.setYwid(qgmxDto.getQgid());
//    				fjcfbDto.setZywid(t_List.get(i).getQgmxid());
//    				List<FjcfbDto> fjcfbDtos = fjcfbService.getListByZywid(fjcfbDto);
//    				if(!CollectionUtils.isEmpty(fjcfbDtos)) {
//    					t_List.get(i).setFjbj("0");
//    				}
//    			}
//    		}
//    	}
    	return dao.getQgmxList(qgmxDto);
    }

    /**
	 * 修改请购明细信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updatePurchaseDetail(List<QgmxDto> qgmxDtos, QgglDto qgglDto) throws BusinessException {
		// TODO Auto-generated method stub
		// 判断新增、删除、修改明细信息
		if(!CollectionUtils.isEmpty(qgmxDtos)) {
			//获取新增的请购明细
			List<QgmxDto> addQgmxDtos = new ArrayList<>();
			for (int i = qgmxDtos.size()-1; i >= 0; i--) {
				qgmxDtos.get(i).setQgid(qgglDto.getQgid());
				qgmxDtos.get(i).setLrry(qgglDto.getLrry());
				qgmxDtos.get(i).setScry(qgglDto.getScry());
				qgmxDtos.get(i).setXgry(qgglDto.getXgry());
//				qgmxDtos.get(i).setYssl(qgmxDtos.get(i).getSl());
				if(StringUtil.isBlank(qgmxDtos.get(i).getQgmxid())) {
					qgmxDtos.get(i).setQgmxid(StringUtil.generateUUID());
					qgmxDtos.get(i).setSysl(qgmxDtos.get(i).getSl());
					qgmxDtos.get(i).setYssl(qgmxDtos.get(i).getSl());
					addQgmxDtos.add(qgmxDtos.get(i));
					qgmxDtos.remove(i);
				}
			}
			// 查询原有请购明细
			List<QgmxDto> selectQgmxDtos = dao.getListByQgid(qgglDto.getQgid());
			//获取删除的合同明细
			List<String> delQgmxids = new ArrayList<>();
			for (int i = selectQgmxDtos.size()-1; i >= 0; i--) {
				boolean isDel = true;
				for (QgmxDto qgmxDto : qgmxDtos) {
					if (selectQgmxDtos.get(i).getQgmxid().equals(qgmxDto.getQgmxid())) {
						if (StringUtil.isNotBlank(selectQgmxDtos.get(i).getSysl()) && StringUtil.isNotBlank(selectQgmxDtos.get(i).getSl())) {
							// 判断能否修改
							Double sysl = Double.parseDouble(selectQgmxDtos.get(i).getSysl()) - (Double.parseDouble(selectQgmxDtos.get(i).getSl()) - Double.parseDouble(qgmxDto.getSl()));
							if (StatusEnum.CHECK_PASS.getCode().equals(qgglDto.getZt())) {
								if (sysl < 0 || (StringUtil.isNotBlank(selectQgmxDtos.get(i).getYdsl()) && sysl < Double.parseDouble(selectQgmxDtos.get(i).getYdsl()))) {
									throw new BusinessException("msg", "修改数量小于合同已使用的数量!" + selectQgmxDtos.get(i).getWlbm());
								}
							}
							qgmxDto.setSysl(String.valueOf(sysl));
							isDel = false;
							break;
						} else {
							qgmxDto.setSysl(qgmxDto.getSl());
							isDel = false;
						}
					}
				}
				if(isDel) {
					delQgmxids.add(selectQgmxDtos.get(i).getQgmxid());
				}
			}
			if(!CollectionUtils.isEmpty(addQgmxDtos)) {
				boolean result = insertQgmx(addQgmxDtos);
				if(!result)
					return false;
			}
			if(!CollectionUtils.isEmpty(delQgmxids)) {
				QgmxDto qgmxDto = new QgmxDto();
				qgmxDto.setIds(delQgmxids);
				qgmxDto.setScbj("1");
				qgmxDto.setScry(qgglDto.getScry());
				boolean result = deleteQgmxDtos(qgmxDto);
				if(!result)
					return false;
			}
			if(!CollectionUtils.isEmpty(qgmxDtos)) {
				//获取修改的合同明细(剩余的htmxDtos)
				return dao.updateQgmx(qgmxDtos);
			}
		}
		return true;
	}

	/**
	 * 批量删除请购明细信息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public boolean deleteQgmxDtos(QgmxDto qgmxDto) {
		// TODO Auto-generated method stub
		if(!CollectionUtils.isEmpty(qgmxDto.getIds())) {
			int result = dao.deleteQgmxDtos(qgmxDto);
			return result != 0;
		}
		return true;
	}

	/**
	 * 根据请购明细ID获取信息
	 */
	@Override
	public List<HtmxDto> getListByQgmxids(QgmxDto qgmxDto,String dqjs) {
		if (StringUtil.isNotBlank(qgmxDto.getLllb())&&"1".equals(qgmxDto.getLllb())&&!"1".equals(dqjs)){
			qgmxDto.setJsid(dqjs);
		}
		// TODO Auto-generated method stub
		return dao.getListByQgmxids(qgmxDto);
	}

	/**
	 * 根据请购ID查询未填写统称物料
	 */
	@Override
	public List<QgmxDto> selectByQgid(String qgid) {
		// TODO Auto-generated method stub
		return dao.selectByQgid(qgid);
	}

	/**
	 * 获取请购物料列表
	 */
	@Override
	public List<QgmxDto> getPagedList(QgmxDto qgmxDto) {
		List<QgmxDto> list=dao.getPagedDtoList(qgmxDto);
		try{
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONS.getCode(), "zt", "qgid", new String[]{
				StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			log.error(e.getMessage());
		}
		return list;
	}

	/**
	 * 导出
	 */
	@Override
	public int getCountForSearchExp(QgmxDto qgmxDto, Map<String, Object> params) {
		return dao.getCountForSearchExp(qgmxDto);
	}
	/**
	 * 审核导出
	 */
	@Override
	public int getCountForAuditSearchExp(QgmxDto qgmxDto, Map<String, Object> params) {
		return dao.getCountForAuditSearchExp(qgmxDto);
	}
	/**
	 * 根据搜索条件获取审核导出信息
	 */
	public List<QgmxDto> getListForAuditSearchExp(Map<String,Object> params){
		QgmxDto qgmxDto = (QgmxDto)params.get("entryData");
		queryJoinFlagExport(params,qgmxDto);
		return dao.getListForAuditSearchExp(qgmxDto);
	}

	/**
	 * 根据选择信息获取审核导出信息
	 */
	public List<QgmxDto> getListForAuditSelectExp(Map<String,Object> params) {
		QgmxDto qgmxDto = (QgmxDto) params.get("entryData");
		queryJoinFlagExport(params, qgmxDto);
		return dao.getListForAuditSelectExp(qgmxDto);
	}
	/**
	 * 根据搜索条件获取导出信息
	 */
	public List<QgmxDto> getListForSearchExp(Map<String,Object> params){
		QgmxDto qgmxDto = (QgmxDto)params.get("entryData");
		queryJoinFlagExport(params,qgmxDto);
		return dao.getListForSearchExp(qgmxDto);
	}
	
	/**
	 * 根据选择信息获取导出信息
	 */
	public List<QgmxDto> getListForSelectExp(Map<String,Object> params){
		QgmxDto qgmxDto = (QgmxDto)params.get("entryData");
		queryJoinFlagExport(params,qgmxDto);
		return dao.getListForSelectExp(qgmxDto);
	}
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String,Object> params,QgmxDto qgmxDto){
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
		for(DcszDto dcszDto:choseList){
			if(dcszDto == null || dcszDto.getDczd() == null)
				continue;
		
			sqlParam.append(",");
			if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		String sqlcs=sqlParam.toString();
		qgmxDto.setSqlParam(sqlcs);
	}
	
	/**
	 * 获取请购物料明细信息
	 */
	@Override
	public List<QgmxDto> getDtoByQgidAndQgmxid(QgmxDto qgmxDto) {
		return dao.getDtoByQgidAndQgmxid(qgmxDto);
	}

	@Override
	public List<QgmxDto> getPagedQgmxDtos(QgmxDto qgmxDto) {
		// TODO Auto-generated method stub
		List<QgmxDto> list=dao.getPagedQgmxDtos(qgmxDto);
		try{
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONS.getCode(), "qgshzt", "qgid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONS_ADMINISTRATION.getCode(), "qgshzt", "qgid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE.getCode(), "qgshzt", "qgid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE.getCode(), "qgshzt", "qgid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONSTWO.getCode(), "qgshzt", "qgid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return list;
	}
	
	/**
	 * 价格统计
	 
	 */
	public List<Map<String,Object>> statisticPrice(QgmxDto qgmxDto){
		return dao.statisticPrice(qgmxDto);
	}
	
	/**
	 * 请购物料周期统计
	 */
	public List<Map<String,Object>> statisticQgmxCycle(QgmxDto qgmxDto){
		return dao.statisticQgmxCycle(qgmxDto);
	}
	
	/**
	 * 取消请购页面明细列表
	 */
	public List<QgmxDto> getQxqgmxList(QgmxDto qgmxDto){
		return dao.getQxqgmxList(qgmxDto);
	}

	/** 根据请购ID获取请购明细ID
	 */
	@Override
	public List<QgmxDto> getListByQgid(String qgid) {
		// TODO Auto-generated method stub
		return dao.getListByQgid(qgid);
	}
	

	@Override
	public void updateQgmxDhsl(QgmxDto qgmxDto) {
        dao.updateQgmxDhsl(qgmxDto);
    }

	@Override
	public boolean qgmxDtoModDhxx(List<QgmxDto> qgmxDtos) {
		// TODO Auto-generated method stub
		int result = dao.qgmxDtoModDhxx(qgmxDtos);
		return result > 0;
	}
	
	/**
	 * 根据请购ids获取请购信息（共通页面）
	 */
	public List<QgmxDto> getCommonDtoListByQgids(QgmxDto qgmxDto){
		return dao.getCommonDtoListByQgids(qgmxDto);
	}

	/**
	 * 删除行政入库明细 删除入库数量
	 */
	public boolean delRkslByQgmxid(QgmxDto qgmxDto){
		return dao.delRkslByQgmxid(qgmxDto);
	}

    /**
	 * 根据请购明细id获取请购id
	 */
	public List<QgmxDto>getQgidsByQgmxids(QgmxDto qgmxDto){
		return dao.getQgidsByQgmxids(qgmxDto);
	}

    /**
	 * 获取行政请购未确认列表
	 */
	public List<QgmxDto>getPageListUnconfirmPurchaseAdministration(QgmxDto qgmxDto){
		return dao.getPageListUnconfirmPurchaseAdministration(qgmxDto);
	}

    /**
	 * 根据请购明细id获取未确认请购明显信息
	 */
	public QgmxDto getWqrQgmxByQgmxid(QgmxDto qgmxDto){
		return dao.getWqrQgmxByQgmxid(qgmxDto);
	}

	/**
	 * 批量更新请购明细关联ID
	 */
	public boolean updateGlidDtos(List<QgmxDto> qgmxDtos){
		return dao.updateGlidDtos(qgmxDtos);
	}

	/**
	 * 更新确认标记
	 
	 */
	public boolean updateQrbj(QgmxDto qgmxDto){
		return dao.updateQrbj(qgmxDto);
	}


	/**
	 * 获取行政库存数据
	 */
	public List<QgmxDto>getXzkcList(QgmxDto qgmxDto){
		return dao.getXzkcList(qgmxDto);
	}
	
		@Override
	public List<QgmxDto> getQgmxDtos(QgmxDto qgmxDto) {
		return dao.getQgmxDtos(qgmxDto);
	}

	@Override
	public boolean updateRkslByIds(List<QgmxDto> qgmxDtos) {
		return dao.updateRkslByIds(qgmxDtos);
	}

	@Override
	public List<QgmxDto> getQgmxListByQgidAndQgmxid(QgmxDto qgmxDto) {
		return dao.getQgmxListByQgidAndQgmxid(qgmxDto);
	}

	@Override
	public List<QgmxDto> getAdminInStoreDetails(QgmxDto qgmxDto) {
		return dao.getAdminInStoreDetails(qgmxDto);
	}

	/**
	 * @Description: 查询改请购单的库存信息
	 * @param qgmxDto
	 * @return java.util.List<com.matridx.igams.production.dao.entities.QgmxDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/7/8 16:33
	 */
	@Override
	public List<QgmxDto> queryByKcxx(QgmxDto qgmxDto) {
		return dao.queryByKcxx(qgmxDto);
	}

	@Override
	public List<QgmxDto> getPagedDetails(QgmxDto qgmxDto) {
		return dao.getPagedDetails(qgmxDto);
	}

	@Override
	public int updateRksl(List<HwxxDto> hwxxDtos) {
		return dao.updateRksl(hwxxDtos);
	}

	@Override
	public boolean updateCpzch(List<QgmxDto> qgmxDtos) {
		return dao.updateCpzch(qgmxDtos);
	}
}
