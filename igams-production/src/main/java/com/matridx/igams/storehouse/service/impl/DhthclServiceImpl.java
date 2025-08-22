package com.matridx.igams.storehouse.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.storehouse.dao.entities.DhthclDto;
import com.matridx.igams.storehouse.dao.entities.DhthclModel;
import com.matridx.igams.storehouse.dao.post.IDhthclDao;
import com.matridx.igams.storehouse.service.svcinterface.IDhthclService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.util.CollectionUtils;

@Service
public class DhthclServiceImpl extends BaseBasicServiceImpl<DhthclDto, DhthclModel, IDhthclDao> implements IDhthclService,IAuditService{
	@Autowired
	IShgcService shgcService;
	private final Logger log = LoggerFactory.getLogger(DhthclServiceImpl.class);
	/**
	 * 根据类型和货物ID获取退回信息
	 * @param dhthclDto
	 * @return
	 */
	public DhthclDto getDtoByHwidAndLx(DhthclDto dhthclDto) {
		return dao.getDtoByHwidAndLx(dhthclDto);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(DhthclDto dhthclDto) {
		if(StringUtils.isBlank(dhthclDto.getDhthid())) {
			dhthclDto.setDhthid(StringUtil.generateUUID());
		}
		int result = dao.insert(dhthclDto);
		return result > 0;
	}

	@Override
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
		DhthclDto dhthclDto = (DhthclDto) baseModel;
		String dhthid = dhthclDto.getDhthid();
		DhthclDto dhthclDto2 = new DhthclDto();
		dhthclDto2.setXgry(operator.getYhid());
		dhthclDto2.setDhthid(dhthid);
		dhthclDto2.setClbh(dhthclDto.getClbh());
		dhthclDto2.setClcs(dhthclDto.getClcs());
		dao.update(dhthclDto2);
		return true;
	}

	@Override
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
		if (shgcList == null || shgcList.isEmpty()) {
			return true;
		}
		for (ShgcDto shgcDto : shgcList) {
			// 审核退回
			if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
				String dhthid = shgcDto.getYwid();
				DhthclDto dhthclDto = new DhthclDto();
				dhthclDto.setXgry(operator.getYhid());
				dhthclDto.setDhthid(dhthid);
				dhthclDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				dao.update(dhthclDto);
			//审核通过	
			}else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
				//更新到货审核表的状态
				String dhthid = shgcDto.getYwid();
				DhthclDto dhthclDto = new DhthclDto();
				dhthclDto.setXgry(operator.getYhid());
				dhthclDto.setDhthid(dhthid);
				dhthclDto.setZt(StatusEnum.CHECK_PASS.getCode());
				dao.update(dhthclDto);
				
			//审核中	
			}else {
				String dhthid = shgcDto.getYwid();
				DhthclDto dhthclDto = new DhthclDto();
				dhthclDto.setXgry(operator.getYhid());
				dhthclDto.setDhthid(dhthid);
				dhthclDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				dao.update(dhthclDto);
			}
		}
		return true;
	}

	@Override
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
		if (shgcList == null || shgcList.isEmpty()) {
			return true;
		}
		if (auditParam.isCancelOpe()) {
			// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String dhthid = shgcDto.getYwid();
				DhthclDto dhthclDto = new DhthclDto();
				dhthclDto.setXgry(operator.getYhid());
				dhthclDto.setDhthid(dhthid);
				dhthclDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				dao.update(dhthclDto);
			}
		} else {
			// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String dhthid = shgcDto.getYwid();
				DhthclDto dhthclDto = new DhthclDto();
				dhthclDto.setXgry(operator.getYhid());
				dhthclDto.setDhthid(dhthid);
				dhthclDto.setZt(StatusEnum.CHECK_NO.getCode());
				dao.update(dhthclDto);
			}
		}
		return true;
	}
	
	@Override
	public List<DhthclDto> getPagedDtoStockList(DhthclDto dhthclDto) {
		List<DhthclDto> dhthList=dao.getPagedDtoList(dhthclDto);
		try {
			shgcService.addShgcxxByYwid(dhthList, AuditTypeEnum.AUDIT_GOODS_BACK.getCode(), "zt", "dhthid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
		} catch (BusinessException e) {
			
			log.error(e.getMessage());
		}
		return dhthList;
	}
	  /**
     * 导出
     * 
     * @return
     */
    @Override
    public int getCountForSearchExp(DhthclDto dhthclDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(dhthclDto);
    }
	 /**
     * 根据搜索条件获取导出信息
     * 
     * @return
     */
    public List<DhthclDto> getListForSearchExp(Map<String, Object> params) {
    	DhthclDto dhthclDto = (DhthclDto) params.get("entryData");
        queryJoinFlagExport(params, dhthclDto);
        return dao.getListForSearchExp(dhthclDto);
    }
	
	/**
     * 根据选择信息获取导出信息
     * 
     * @return
     */
    public List<DhthclDto> getListForSelectExp(Map<String, Object> params) {
    	DhthclDto dhthclDto = (DhthclDto) params.get("entryData");
        queryJoinFlagExport(params, dhthclDto);
        return dao.getListForSelectExp(dhthclDto);
    }
    
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, DhthclDto dhthclDto) {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for (DcszDto dcszDto : choseList) {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs = sqlParam.toString();
        dhthclDto.setSqlParam(sqlcs);
    }

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
		return null;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
		Map<String, Object> map =new HashMap<>();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
		DhthclDto dhthclDto = new DhthclDto();
		dhthclDto.setIds(ids);
		List<DhthclDto> dtoList = dao.getDtoList(dhthclDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(DhthclDto dto:dtoList){
				list.add(dto.getDhthid());
			}
		}
		map.put("list",list);
		return map;
	}
}
