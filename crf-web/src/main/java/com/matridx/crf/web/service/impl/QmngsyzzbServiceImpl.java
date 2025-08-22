package com.matridx.crf.web.service.impl;

import com.matridx.crf.web.dao.entities.*;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.crf.web.dao.post.IQmngsyzzbDao;
import com.matridx.crf.web.service.svcinterface.IQmngsyzzbService;

import java.util.List;

@Service
public class QmngsyzzbServiceImpl extends BaseBasicServiceImpl<QmngsyzzbDto, QmngsyzzbModel, IQmngsyzzbDao> implements IQmngsyzzbService{
    @Override
    public List<QmngsyzzbDto> getSjz(String ndzjlid){
        return dao.getSjz(ndzjlid);

    }
    /*
     * 保存炎症指标
     */
    @Override
    public void saveYzzb(List<QmngsyzzbDto> yzzbs, QmngsndzxxjlDto ndz) {
        //先删除已存在的动脉血气
        dao.deleteByNdz(ndz);
        //删除后所有的数据都是新增
        if(yzzbs!=null&&yzzbs.size()>0) {
            for (int i = yzzbs.size()-1; i >=0; i--) {
                if(StringUtil.isBlank(yzzbs.get(i).getSjz()))
                {
                    yzzbs.remove(i);
                    continue;
                }
                yzzbs.get(i).setQmngsndzjlid(ndz.getQmngsndzjlid());
            }
            if(yzzbs.size() > 0) {
                dao.insertYzzb(yzzbs);
            }
        }
    }
    
    /**
   	 * 获炎症指标
   	 * @return
   	 */
	@Override
	public List<QmngsyzzbDto> queryYzzb(String hzid) {
		// TODO Auto-generated method stub
		return dao.queryYzzb(hzid);
	}
}
