package com.matridx.server.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjycWechatDto;
import com.matridx.server.wechat.dao.entities.SjycWechatModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISjycWechatDao extends BaseBasicDao<SjycWechatDto, SjycWechatModel>{
    /**
     * 根据送检id获取异常信息
     * @param sjycDto
     * @return
     */
    List<SjycWechatDto> getDtoBySjid(SjycWechatDto sjycDto);


    /**
     * 根据送检ID获取异常数量
     * @param sjycDto
     * @return
     */
    int getCountBySjid(SjycWechatDto sjycDto);

    int evaluation(SjycWechatDto sjycDto);



    /**
     * 结束异常任务
     * @param sjycDto
     * @return
     */
    boolean finishYc(SjycWechatDto sjycDto);

    /**
     * 批量根据异常id获取异常信息(lrry,ycbt)
     * @param sjycDto
     * @return
     */
    List<SjycWechatDto> getFinishList(SjycWechatDto sjycDto);
}
