package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.DdspglModel;
import com.matridx.igams.common.dao.post.IDdspglDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class DdspglServiceImpl extends BaseBasicServiceImpl<DdspglDto, DdspglModel, IDdspglDao> implements IDdspglService{

	//@Autowired
	//IDdfbsglService ddfbsglService;
	
//	@Value("${matridx.wechat.applicationurl:}")
//    private String applicationurl;
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(DdspglDto ddspglDto) {
		if(StringUtil.isBlank(ddspglDto.getDdspglid())) {
			ddspglDto.setDdspglid(StringUtil.generateUUID());
		}
		int result=dao.insert(ddspglDto);
		return result > 0;
	}
	/**
	 * 更新钉钉审批信息
	 */
	@Override
	public DdspglDto insertInfo(JSONObject obj) {
		// TODO Auto-generated method stub
		String processInstanceId = obj.getString("processInstanceId");
		String processCode = obj.getString("processCode");
		String corpId = obj.getString("corpId");
		String EventType = obj.getString("EventType");
		String title = obj.getString("title");
		String type = obj.getString("type");
		String createTime = obj.getString("createTime");
		if(StringUtil.isNotBlank(createTime)){
			Date date = new Date(Long.parseLong(createTime));
			createTime = DateUtils.getCustomFomratCurrentDate(date, "yyyy-MM-dd HH:mm:ss");
		}
		String staffId = obj.getString("staffId");
		String remark = obj.getString("remark");
		String result = obj.getString("result");
		String finishTime = obj.getString("finishTime");
		if(StringUtil.isNotBlank(finishTime)){
			Date date = new Date(Long.parseLong(finishTime));
			finishTime = DateUtils.getCustomFomratCurrentDate(date, "yyyy-MM-dd HH:mm:ss");
		}
		String cljg = obj.getString("cljg");
		String wbcxidString = obj.getString("wbcxid");//获取外部程序id
		DdspglDto ddspglDto = new DdspglDto();
		ddspglDto.setProcessinstanceid(processInstanceId);
		ddspglDto.setProcesscode(processCode);
		ddspglDto.setCorpid(corpId);
		ddspglDto.setEventtype(EventType);
		ddspglDto.setTitle(title);
		ddspglDto.setType(type);
		ddspglDto.setCreatetime(createTime);
		ddspglDto.setStaffid(staffId);
		ddspglDto.setRemark(remark);
		ddspglDto.setResult(result);
		ddspglDto.setFinishtime(finishTime);
		if(StringUtil.isNotBlank(cljg)) {
			ddspglDto.setCljg(cljg);
		}
		ddspglDto.setWbcxid(wbcxidString);
		insertDto(ddspglDto);
		return ddspglDto;
	}

	/**
	 * 更新钉钉审批信息
	 */
	public boolean insertOrUpdateDdspgl(DdspglDto ddspglDto) {
		// TODO Auto-generated method stub
		int count = dao.insertOrUpdateDdspgl(ddspglDto);
		return count != 0;
	}
	
	/**
	 * 更新钉钉审批处理结果
	 */
	public boolean updatecljg(DdspglDto ddspglDto) {
		return dao.updatecljg(ddspglDto);
	}
	
	/**
	 * 根据类型，钉钉实例ID批量更新钉钉审批管理信息
	 */
	public boolean updateAll(DdspglDto ddspglDto) {
		return dao.updateAll(ddspglDto);
	}
	
	/**
	 * 获取完成状态的钉钉审批管理事件
	 */
	public DdspglDto getFinishInstenceSpgl(DdspglDto ddspglDto) {
		return dao.getFinishInstenceSpgl(ddspglDto);
	}
	
}
