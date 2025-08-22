package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.KpsqDto;
import com.matridx.igams.wechat.dao.entities.KpsqModel;
import com.matridx.igams.wechat.dao.entities.SwyszkDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IKpsqDao extends BaseBasicDao<KpsqDto, KpsqModel>{

    /**
     *
     * @param kpsqDto
     * @return
     */
    SwyszkDto getAllInfo(KpsqDto kpsqDto);
    /**
     * 审核列表
     * @param
     * @return
     */
    List<KpsqDto> getPagedAuditList(KpsqDto kpsqDto);


    /**
     * 审核信息
     * @param
     * @return
     */
    List<KpsqDto> getAuditListRecheck(List<KpsqDto> list);
    /**
     * 获取应收账款更新数据
     */
    List<SwyszkDto> getUpdateData(List<String> ids);
}
