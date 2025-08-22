package com.matridx.server.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjycWechatDto;
import com.matridx.server.wechat.dao.entities.SjycWechatModel;

import java.util.List;

/**
 * @author hmz
 * @date2023/06/27 version V1.0
 **/
public interface ISjycWechatService extends BaseBasicService<SjycWechatDto, SjycWechatModel> {
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

    boolean evaluation(SjycWechatDto sjycDto);

    /**
     * 保存异常信息
     * @param sjycDto
     * @return
     */
    boolean addSaveException(SjycWechatDto sjycDto);

    /**
     * 修改送检异常信息(异常结束)
     * @param sjycDto
     * @return
     */
    boolean modSaveException(SjycWechatDto sjycDto);



    /**
     * 结束异常任务
     * @param sjycDto
     * @return
     */
    boolean finishYc(SjycWechatDto sjycDto);
}
