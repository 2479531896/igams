package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.DdspjlDto;
import com.matridx.igams.common.dao.entities.DdspjlModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface IDdspjlService  extends BaseBasicService<DdspjlDto, DdspjlModel> {
    /**
     * @Description: 筛选钉钉实例id
     * @param ddspjlDto
     * @return java.util.List<java.lang.String>
     * @Author: 郭祥杰
     * @Date: 2025/6/5 15:41
     */
    List<String> filtrateDdslid(DdspjlDto ddspjlDto);

    /**
     * @Description: 批量更新
     * @param ddspjlDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2025/6/5 16:17
     */
    boolean insertList(List<DdspjlDto> ddspjlDtoList);

    /**
     * @Description: 获取查看页面数据
     * @param processinstanceid
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2025/7/9 15:21
     */
    Map<String,Object> queryView(String processinstanceid);
}
