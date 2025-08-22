package com.matridx.igams.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjwltzDto;
import com.matridx.igams.wechat.dao.entities.SjwltzModel;

import java.util.List;

@Mapper
public interface ISjwltzDao extends BaseBasicDao<SjwltzDto, SjwltzModel>{
    /**
     * 批量新增
     * @param list
     * @return
     */
    boolean insertList(List<SjwltzDto> list);

    /**
     * 通过送检派单ID获取送检物流通知信息
     * @param sjpdid
     * @return
     */
    List<SjwltzDto> getDtoListBySjpdid(String sjpdid);
    /**
     * 通过送检派单ID获取送检物流通知信息
     * @param sjpdid
     * @return
     */
    List<SjwltzDto> getDtoListBySjpd(String sjpdid);

    boolean deleteByYwid(String sjpdid);

    /**
     * 通过录入人员获取通知人员用户信息
     * @param userlist
     * @return
     */
    List<SjwltzDto> getYhinfo(List<SjwltzDto> userlist);

    /**
     * 通过ywid获取送检物流通知信息
     * @param wlid
     * @return
     */
    List<SjwltzDto> getDtoListByYwid(String wlid);
}
