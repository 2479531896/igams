package com.matridx.server.wechat.service.impl;

import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.server.wechat.dao.entities.FjsqDto;
import com.matridx.server.wechat.dao.entities.FjsqModel;
import com.matridx.server.wechat.dao.post.IFjsqDao;
import com.matridx.server.wechat.service.svcinterface.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FjsqServiceImpl extends BaseBasicServiceImpl<FjsqDto, FjsqModel, IFjsqDao> implements IFjsqService{
	
	@Autowired
	IShgcService shgcService;

	private Logger log = LoggerFactory.getLogger(FjsqServiceImpl.class);

    /**
	 * 送检列表查看复检申请信息
	 * @param fjsqDto
	 * @return
	 */
	@Override
	public List<FjsqDto> getListBySjid(FjsqDto fjsqDto){
		// TODO Auto-generated method stub
		List<FjsqDto> t_List=dao.getListBySjid(fjsqDto);
		try{
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_RECHECK.getCode(), "zt", "fjid", new String[]{
				StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_OUT_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_DING_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t_List;
	}
}
