package com.matridx.igams.experiment.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.experiment.dao.entities.QclglDto;
import com.matridx.igams.experiment.dao.entities.QclglModel;
import com.matridx.igams.experiment.dao.entities.QclmxDto;
import com.matridx.igams.experiment.dao.post.IQclglDao;
import com.matridx.igams.experiment.service.svcinterface.IQclglService;
import com.matridx.igams.experiment.service.svcinterface.IQclmxService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class QclglServiceImpl extends BaseBasicServiceImpl<QclglDto, QclglModel, IQclglDao> implements IQclglService,IFileImport{

	@Autowired
	private IQclmxService qclmxService;
	
	/**
	 * 删除前处理管理和前处理明细
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteQclgl(QclglDto qclglDto)
	{
		boolean result=false;
		// TODO Auto-generated method stub
		try{
			result=delete(qclglDto);
			QclmxDto qclmxDto=new QclmxDto();
			qclmxDto.setIds(qclglDto.getIds());
			qclmxDto.setScry(qclglDto.getScry());
			qclmxService.delete(qclmxDto);
		} catch (Exception e){
			// TODO: handle exception
		}
		return result;
	}
	
	@Override
	public boolean existCheck(String fieldName, String value)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertNormalImportRec(Map<String, String> recMap, User user)
	{
		// TODO Auto-generated method stub
		if(!Objects.equals(recMap.get("0"), "") && !Objects.equals(recMap.get("3"), "")) {
			QclglDto qclglDto=new QclglDto();
			String file_name=recMap.get("file_name");
			int end=file_name.lastIndexOf(".");
			String subFilename=file_name.substring(0, end);
			qclglDto.setMc(subFilename);
			QclglDto qclglDto2= dao.getDto(qclglDto);
			if(qclglDto2!=null) {
				QclmxDto qclmxDto=new QclmxDto();
				qclmxDto.setNbbh(recMap.get("0"));
				qclmxDto.setXbjs(recMap.get("3"));
				qclmxDto.setQclid(qclglDto2.getQclid());
				int maxXh=qclmxService.getMaxXh(qclglDto2.getQclid());
				String sjid=qclmxService.getSjidByNbbh(qclmxDto.getNbbh());
				if(sjid !=null) {
					qclmxDto.setSjid(sjid);
				}
				qclmxDto.setXh((maxXh+1)+"");
				qclmxDto.setLrry(user.getYhid());
				qclmxService.insert(qclmxDto);
			}else {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				QclglDto qclglDto3=new QclglDto();
				qclglDto3.setLrsj(sdf.format(new Date()));
				qclglDto3.setJcdw(recMap.get("kzcs4"));
				int xh=dao.getXh(qclglDto3);
				qclglDto.setXh(xh+"");
				qclglDto3.setQclid(StringUtil.generateUUID());
				qclglDto3.setMc(subFilename);
				qclglDto3.setZt(StatusEnum.CHECK_PASS.getCode());
				qclglDto3.setLrry(user.getYhid());
				boolean result=insert(qclglDto3);
				if(result) {
					QclmxDto qclmxDto=new QclmxDto();
					qclmxDto.setQclid(qclglDto3.getQclid());
					qclmxDto.setNbbh(recMap.get("0"));
					qclmxDto.setXbjs(recMap.get("3"));
					qclmxDto.setXh(1+"");
					String sjid=qclmxService.getSjidByNbbh(qclmxDto.getNbbh());
					if(sjid !=null) {
						qclmxDto.setSjid(sjid);
					}
					qclmxDto.setLrry(user.getYhid());
					qclmxService.insert(qclmxDto);
				}
			}
			
		}
		return true;
	}
	
	/**
	 * 检查标题定义，主要防止模板信息过旧
	 */
	public boolean checkDefined(List<Map<String,String>> defined) {
		return true;
	}

}
