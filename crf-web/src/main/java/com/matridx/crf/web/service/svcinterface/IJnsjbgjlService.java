package com.matridx.crf.web.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.crf.web.dao.entities.JnsjbgjlDto;
import com.matridx.crf.web.dao.entities.JnsjbgjlModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.Map;

public interface IJnsjbgjlService extends BaseBasicService<JnsjbgjlDto, JnsjbgjlModel> {

    /**
     * 新增艰难梭菌报告记录
     * @return
     */
    boolean insertJnsj(JSONObject json) ;

    boolean updateJnsj(JSONObject json) ;

    Map<String,Object> getDateByBgId(JnsjbgjlDto dto);

    /**
     * 查看艰难梭菌报告记录，很多字段0/1/2等在sql里直接转为是/否等
     * @param jnsjbgjlDto
     * @return
     */
    JnsjbgjlDto getViewDto(JnsjbgjlDto jnsjbgjlDto);

    /**
     * 删除艰难梭菌信息
     * @param jnsjbgjlDto
     * @return
     */
    boolean delJnsjInfo(JnsjbgjlDto jnsjbgjlDto);

    /**
     * 扩展更新艰难梭菌报告记录
     * @param jnsjbgjlDto
     * @return
     */
    boolean extendUpdateJnsj(JnsjbgjlDto jnsjbgjlDto);

    /**
     * 生成病理入组编号
     * @param cskz1
     * @return
     */
    String generateBlrzbh(String cskz1);

}
