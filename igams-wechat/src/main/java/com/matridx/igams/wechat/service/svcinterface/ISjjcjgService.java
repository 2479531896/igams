package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjjcjgDto;
import com.matridx.igams.wechat.dao.entities.SjjcjgModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;

import java.util.List;
import java.util.Map;

public interface ISjjcjgService extends BaseBasicService<SjjcjgDto, SjjcjgModel>{

    /**
     * 批量插入
     */
    Boolean insertList(List<SjjcjgDto> list);

    /**
     * 获取送检检测结果
     * @param sjjcjgDto
     * @return
     */
    List<Map<String,Object>> getSjjcjgList(SjjcjgDto sjjcjgDto);

    /**
     * 获取相关文献List
     */
    List<SjjcjgDto> getXgwxList(SjjcjgDto sjjcjgDto);

    /**
     * 发送报告
     */
    Boolean sendReport(String ywid,String shlb,String yhm) throws BusinessException;

    /**
     * 获取相关文献List
     */
    List<SjjcjgDto> streamList(List<SjjcjgDto> list);

    /**
     * 获取Rfs检测结果
     */
    List<SjjcjgDto> getWeekList(SjxxDto sjxxDto);
    /**
     * 批量更新结论
     * @param list
     * @return
     */
    boolean updatesjjcjgJlList(List<SjjcjgDto> list);
}
