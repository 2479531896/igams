package com.matridx.igams.common.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.LshgzDto;
import com.matridx.igams.common.dao.entities.LshgzModel;
import com.matridx.igams.common.dao.post.ILshgzDao;
import com.matridx.igams.common.service.svcinterface.ILshgzService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class LshgzServiceImpl extends BaseBasicServiceImpl<LshgzDto, LshgzModel, ILshgzDao> implements ILshgzService{

    /**
     * 插入流水号规则信息
     */
    @Override
    public boolean insert(LshgzModel lshgzModel){
    	String lshgzid = StringUtil.generateUUID();
    	lshgzModel.setLshgzid(lshgzid);
    	int result = dao.insert(lshgzModel);
    	return result > 0;
    }
}
