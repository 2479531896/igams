package com.matridx.igams.production.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.entities.QgqxglDto;
import com.matridx.igams.production.dao.entities.QgqxmxDto;
import com.matridx.igams.production.dao.entities.QgqxmxModel;
import com.matridx.igams.production.dao.post.IQgqxmxDao;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.production.service.svcinterface.IQgqxmxService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class QgqxmxServiceImpl extends BaseBasicServiceImpl<QgqxmxDto, QgqxmxModel, IQgqxmxDao> implements IQgqxmxService{

	@Autowired
	private IQgmxService qgmxService;
	
	//@Autowired
	//private IQgqxglService qgqxglService;
	
	@Autowired
	private IShgcService shgcService;

	private final Logger log = LoggerFactory.getLogger(QgqxmxServiceImpl.class);
	/**
	 * 新增请购取消明细
	 */
	public boolean insertDtoList(List<QgqxmxDto> list){
		return dao.insertDtoList(list);
	}

 	/**
	 * 请购列表（查询审核状态）
	 */
	@Override
	public List<QgqxmxDto> getPagedDtoList(QgqxmxDto qgqxmxDto) {
		// TODO Auto-generated method stub
		List<QgqxmxDto> list=dao.getPagedDtoList(qgqxmxDto);
		try{
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONS_CANCEL.getCode(), "zt", "qgqxid", new String[]{
				StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return list;
	}
	
	
	/**
	 * 删除请购取消明细
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteQgqxmx(QgqxmxDto qgqxmxDto) throws BusinessException{
		List<QgqxmxDto> qgqxmxDto_list = dao.getDtoList(qgqxmxDto);
		List<String> ywids=new ArrayList<>();
		ywids.add(qgqxmxDto.getQgqxid());
		for (QgqxmxDto qgqxmxDto_t : qgqxmxDto_list) {
			QgmxDto qgmxDto = qgmxService.getDtoById(qgqxmxDto_t.getQgmxid());
			qgmxDto.setQxqg("0");
			BigDecimal sl = new BigDecimal(qgqxmxDto_t.getQxsl()).add(new BigDecimal(qgmxDto.getSl()));
			qgmxDto.setSl(sl.toString());
			BigDecimal sysl = new BigDecimal(qgqxmxDto_t.getQxsl()).add(new BigDecimal(qgmxDto.getSysl()));
			qgmxDto.setSysl(sysl.toString());
			qgmxDto.setXgry(qgqxmxDto.getScry());
			boolean result = qgmxService.updateQxqgxx(qgmxDto);
			if (!result)
				throw new BusinessException("msg", "请购明细信息修改失败(恢复请购)!");
		}
		int result_q = dao.delete(qgqxmxDto);
		if(result_q==0)
			throw new BusinessException("msg", "请购取消明细信息修改失败(恢复请购)!");
		shgcService.deleteByYwids(ywids);//删除审核过程,否则审批延期会有脏数据(虽然审核中不允许删除)
		return true;	
	}
	
	/**
	 * 请购取消明细列表
	 */
	public List<QgqxmxDto> getQgqxmxList(QgqxmxDto qgqxmxDto){

		return dao.getQgqxmxList(qgqxmxDto);
	}
	/**
	 * 导出
	 */
	@Override
	public int getCountForSearchExp(QgqxmxDto qgqxmxDto, Map<String, Object> params) {
		return dao.getCountForSearchExp(qgqxmxDto);
	}
	
	/**
	 * 根据搜索条件获取导出信息
	 */
	public List<QgqxmxDto> getListForSearchExp(Map<String,Object> params){
		QgqxmxDto qgqxmxDto = (QgqxmxDto)params.get("entryData");
		queryJoinFlagExport(params,qgqxmxDto);
		return dao.getListForSearchExp(qgqxmxDto);
	}
	
	/**
	 * 根据选择信息获取导出信息
	 */
	public List<QgqxmxDto> getListForSelectExp(Map<String,Object> params){
		QgqxmxDto qgqxmxDto = (QgqxmxDto)params.get("entryData");
		queryJoinFlagExport(params,qgqxmxDto);
		return dao.getListForSelectExp(qgqxmxDto);
	}
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String,Object> params,QgqxmxDto qgqxmxDto){
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
		qgqxmxDto.setSqlParam(sqlcs);
	}

	/**
	 * 取消请购审核通过更新请购明细sl，sysl字段
	 */
	public boolean updateQgmxByList(QgqxglDto qgqxglDto) {
		return dao.updateQgmxByList(qgqxglDto);
	}
	
	/**
	 * 更新请购取消明细数据
	 */
	public boolean updateDtoList(List<QgqxmxDto> list) {
		return dao.updateDtoList(list);
	}
	
	//获取其他有关该请购明细的取消请购信息
	/*public List<QgqxmxDto> getOtherQxqgList(QgqxmxDto qgqxmxDto){
		return dao.getOtherQxqgList(qgqxmxDto);
	}*/
	
	/**
	 * 获取请购明细以及可取消数
	 */
	public List<QgqxmxDto> getQgqxmxCancelList(QgqxmxDto qgqxmxDto){
		return dao.getQgqxmxCancelList(qgqxmxDto);
	}
}
