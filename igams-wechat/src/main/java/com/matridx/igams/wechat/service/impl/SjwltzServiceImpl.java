package com.matridx.igams.wechat.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjwltzDto;
import com.matridx.igams.wechat.dao.entities.SjwltzModel;
import com.matridx.igams.wechat.dao.post.ISjwltzDao;
import com.matridx.igams.wechat.service.svcinterface.ISjwltzService;

import java.util.List;

@Service
public class SjwltzServiceImpl extends BaseBasicServiceImpl<SjwltzDto, SjwltzModel, ISjwltzDao> implements ISjwltzService{
    /**
     * 批量新增
     * @param list
     * @return
     */
    public boolean insertList(List<SjwltzDto> list){
        return dao.insertList(list);
    }

    /**
     * 通过送检派单ID获取送检物流通知信息
     * @param sjpdid
     * @return
     */
    @Override
    public List<SjwltzDto> getDtoListBySjpdid(String sjpdid) {
        return dao.getDtoListBySjpdid(sjpdid);
    }
    /**
     * 通过送检派单ID获取送检物流通知信息
     * @param sjpdid
     * @return
     */
    @Override
    public List<SjwltzDto> getDtoListBySjpd(String sjpdid) {
        return dao.getDtoListBySjpd(sjpdid);
    }

    @Override
    public boolean deleteByYwid(String sjpdid) {
        return dao.deleteByYwid(sjpdid);
    }

    /**
     * 通过录入人员获取通知人员用户信息
     * @param userlist
     * @return
     */
    @Override
    public List<SjwltzDto> getYhinfo(List<SjwltzDto> userlist) {
        return dao.getYhinfo(userlist);
    }

    /**
     * 通过ywid获取送检物流通知信息
     * @return
     */
    @Override
    public List<SjwltzDto> getDtoListByYwid(String wlid) {
        return dao.getDtoListByYwid(wlid);
    }

}
