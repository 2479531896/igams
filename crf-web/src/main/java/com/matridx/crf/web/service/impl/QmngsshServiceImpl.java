package com.matridx.crf.web.service.impl;

import com.matridx.crf.web.dao.entities.*;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.crf.web.dao.post.IQmngsshDao;
import com.matridx.crf.web.service.svcinterface.IQmngsshService;

import java.util.List;

@Service
public class QmngsshServiceImpl extends BaseBasicServiceImpl<QmngsshDto, QmngsshModel, IQmngsshDao> implements IQmngsshService{
    @Override
    public List<QmngsshDto> getSjz(String ndzjlid){
        return dao.getSjz(ndzjlid);

    }
    /*
     * 保存生化
     */
    @Override
    public void saveSh(List<QmngsshDto> shs, QmngsndzxxjlDto ndz) {
        //先删除已存在的动脉血气
        dao.deleteByNdz(ndz);
        //删除后所有的数据都是新增
        if(shs!=null&&shs.size()>0) {
            for (int i = shs.size()-1; i >=0; i--) {
                if(StringUtil.isBlank(shs.get(i).getSjz()))
                {
                    shs.remove(i);
                    continue;
                }
                shs.get(i).setQmngsndzjlid(ndz.getQmngsndzjlid());
            }
            if(shs.size() > 0) {
                dao.insertSh(shs);
            }
        }
    }
    
    /**
   	 * 获取生化配置
   	 * @return
   	 */
	@Override
	public List<QmngsshDto> queryShpz(String hzid) {
		// TODO Auto-generated method stub
		return dao.queryShpz(hzid);
	}
}
