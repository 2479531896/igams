package com.matridx.crf.web.service.impl;

import com.matridx.crf.web.dao.entities.*;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.crf.web.dao.post.IQmngsdmxqDao;
import com.matridx.crf.web.service.svcinterface.IQmngsdmxqService;

import java.util.List;

@Service
public class QmngsdmxqServiceImpl extends BaseBasicServiceImpl<QmngsdmxqDto, QmngsdmxqModel, IQmngsdmxqDao> implements IQmngsdmxqService{
    @Override
    public List<QmngsdmxqDto> getSjz(String ndzjlid){
        return dao.getSjz(ndzjlid);

    }
    /**
         * 保存动脉血气
     */
    @Override
    public void saveDmxq(List<QmngsdmxqDto> dmxqs, QmngsndzxxjlDto ndz) {
        //先删除已存在的动脉血气
        dao.deleteByNdz(ndz);
        //删除后所有的数据都是新增
        if(dmxqs!=null&&dmxqs.size()>0) {
            for (int i = dmxqs.size()-1; i >=0; i--) {
                if(StringUtil.isBlank(dmxqs.get(i).getSjz()))
                {
                    dmxqs.remove(i);
                    continue;
                }
                dmxqs.get(i).setQmngsndzjlid(ndz.getQmngsndzjlid());
            }
            if(dmxqs.size() > 0) {
                dao.insertDmxq(dmxqs);
            }
        }
    }
    
    /**
   	 * 获取动脉血气
   	 * @return
   	 */
	@Override
	public List<QmngsdmxqDto> queryDmxq(String hzid) {
		// TODO Auto-generated method stub
		return dao.queryDmxq(hzid);
	}
}
