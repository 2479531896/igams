package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.WbsjxxDto;
import com.matridx.igams.wechat.dao.entities.WbsjxxModel;
import com.matridx.igams.wechat.dao.post.IWbsjxxDao;
import com.matridx.igams.wechat.service.svcinterface.IWbsjxxService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WbsjxxServiceImpl extends BaseBasicServiceImpl<WbsjxxDto, WbsjxxModel, IWbsjxxDao> implements IWbsjxxService {

    /**
     * 查询相似样本
     * @param wbsjxxDto
     * @return
     */
    public List<WbsjxxDto> getSimilarDtoList(WbsjxxDto wbsjxxDto){
        return dao.getSimilarDtoList(wbsjxxDto);
    }
    /**
     * 根据外部编码查询送检信息
     * @param wbsjxxDto
     * @return
     */
    public List<WbsjxxDto> getListByWbbm(WbsjxxDto wbsjxxDto){
        return dao.getListByWbbm(wbsjxxDto);
    }

    /**
     * 根据lastwbbm获取送检报告
     * @param wbsjxxDto
     * @return
     */
    @Override
    public List<WbsjxxDto> getListByLastWbbm(WbsjxxDto wbsjxxDto) {
        // TODO Auto-generated method stub
        return dao.getListByLastWbbm(wbsjxxDto);
    }
    /**
     * 根据sjid和jcxm查询外部送检信息
     * @param wbsjxxDto
     * @return
     */
    @Override
    public WbsjxxDto getDtoBySjidAndJcxm(WbsjxxDto wbsjxxDto) {
        // TODO Auto-generated method stub
        return dao.getDtoBySjidAndJcxm(wbsjxxDto);
    }


    /**
     * 根据sjid查询外部送检信息List
     * @param sjid
     * @return
     */
    @Override
    public List<WbsjxxDto> getListBySjid(String sjid) {
        // TODO Auto-generated method stub
        return dao.getListBySjid(sjid);
    }


    /**
     * 数据合并复制wbsjxx得sjid
     * @param sjid1
     * @param sjid2
     * @param user
     */
    @Override
    public void copyWbsjxx(String sjid1 ,String sjid2, User user){
        WbsjxxDto wbsjxxDto_1 = dao.getDtoBySjid(sjid1);
        WbsjxxDto wbsjxxDto_2 = dao.getDtoBySjid(sjid2);
        if (wbsjxxDto_1 != null){
            wbsjxxDto_1.setSjid(sjid2);
            dao.copyUpdate(wbsjxxDto_1);
        }
        if (wbsjxxDto_2 != null){
            wbsjxxDto_2.setSjid(sjid1);
            dao.copyUpdate(wbsjxxDto_2);
        }
    }

    @Override
    public int updateInfojsonBySjid(WbsjxxDto wbsjxxDto){
    	return dao.updateInfojsonBySjid(wbsjxxDto);
    }

    /**
     * @param wbsjxxs
     * @return
     */
    @Override
    public int updateList(List<WbsjxxDto> wbsjxxs) {
        return dao.updateList(wbsjxxs);
    }

	@Override
    public WbsjxxDto getDtoBySjid(String sjid) {
        // TODO Auto-generated method stub
        return dao.getDtoBySjid(sjid);
	}
}
