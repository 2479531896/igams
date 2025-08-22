package com.matridx.server.wechat.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.SjqxDto;
import com.matridx.server.wechat.dao.entities.SjqxModel;
import com.matridx.server.wechat.dao.post.ISjqxDao;
import com.matridx.server.wechat.service.svcinterface.ISjqxService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class SjqxServiceImpl extends BaseBasicServiceImpl<SjqxDto, SjqxModel, ISjqxDao> implements ISjqxService{

    /**
     * 插入送检权限信息
     * @param sjqxModel
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insert(SjqxModel sjqxModel){
    	String sjqxid = StringUtil.generateUUID();
    	sjqxModel.setSjqxid(sjqxid);
    	int result = dao.insert(sjqxModel);
    	return result > 0;
    }

    /**
	 * 新增送检权限
	 * @param sjqxDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSaveAudit(SjqxDto sjqxDto) {
		// TODO Auto-generated method stub
		dao.deleteByWxid(sjqxDto.getWxid());
		List<String> kss = sjqxDto.getKss();
		List<String> sjdws = sjqxDto.getSjdws();
		if(kss != null && sjdws != null && sjdws.size() == kss.size()) {
			List<SjqxDto> sjqxDtos = new ArrayList<>();
			for (int i = 0; i < kss.size(); i++) {
				SjqxDto t_sjqxDto = new SjqxDto();
				t_sjqxDto.setSjdw(sjdws.get(i));
				t_sjqxDto.setKs(kss.get(i));
				t_sjqxDto.setWxid(sjqxDto.getWxid());
				t_sjqxDto.setSjqxid(StringUtil.generateUUID());
				sjqxDtos.add(t_sjqxDto);
			}
			int result = dao.insertBySjqxDtos(sjqxDtos);
            return result > 0;
		}
		return false;
	}
}
