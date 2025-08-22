package com.matridx.igams.wechat.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjlcznDto;
import com.matridx.igams.wechat.dao.entities.SjlcznModel;
import com.matridx.igams.wechat.dao.post.ISjlcznDao;
import com.matridx.igams.wechat.service.svcinterface.ISjlcznService;

@Service
public class SjlcznServiceImpl extends BaseBasicServiceImpl<SjlcznDto, SjlcznModel, ISjlcznDao> implements ISjlcznService{
	
	/**
	 * 将临床诊疗指南转为String类型
	 * @param sjid
	 * @return
	 */
	@Override
	public String getGuideToString(String sjid) {
		// TODO Auto-generated method stub
		List<SjlcznDto> list=dao.getGuideForWord(sjid);
		StringBuffer sb=new StringBuffer();
		if(list!=null && list.size()>0) {
			for (int i = 1; i < list.size()+1; i++) {
				if(i>1)
					sb.append("{br}{\\n}");
				else
					sb.append("\n");
				sb.append("["+i+"]"+list.get(i-1).getYygs());
			}
		}
		return sb.toString();
	}

}
