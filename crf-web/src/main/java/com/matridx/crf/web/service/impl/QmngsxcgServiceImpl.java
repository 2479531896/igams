package com.matridx.crf.web.service.impl;

import com.matridx.crf.web.dao.entities.*;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.crf.web.dao.post.IQmngsxcgDao;
import com.matridx.crf.web.service.svcinterface.IQmngsxcgService;

import java.util.List;

@Service
public class QmngsxcgServiceImpl extends BaseBasicServiceImpl<QmngsxcgDto, QmngsxcgModel, IQmngsxcgDao> implements IQmngsxcgService{
    @Override
    public List<QmngsxcgDto> getSjz(String ndzjlid){
        return dao.getSjz(ndzjlid);

    }
    /*
     * 保存炎症指标
     */
    @Override
    public void saveXcg(List<QmngsxcgDto> xcgs, QmngsndzxxjlDto ndz) {
        //先删除已存在的动脉血气
        dao.deleteByNdz(ndz);
        //删除后所有的数据都是新增
        if(xcgs!=null&&xcgs.size()>0) {
            for (int i = xcgs.size()-1; i >=0; i--) {
                if(StringUtil.isBlank(xcgs.get(i).getSjz()))
                {
                    xcgs.remove(i);
                    continue;
                }
                xcgs.get(i).setQmngsndzjlid(ndz.getQmngsndzjlid());
            }
            if(xcgs.size() > 0) {
                dao.insertXcg(xcgs);
            }
        }
    }
    
    /**
   	 * 获取血常规
   	 * @return
   	 */
	@Override
	public List<QmngsxcgDto> queryXcg(String hzid) {
		// TODO Auto-generated method stub
		return dao.queryXcg(hzid);
	}
}
