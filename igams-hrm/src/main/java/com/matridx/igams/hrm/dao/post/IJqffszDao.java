package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.JqffszDto;
import com.matridx.igams.hrm.dao.entities.JqffszModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IJqffszDao extends BaseBasicDao<JqffszDto, JqffszModel> {
    List<JqffszDto> selectDtoByList(List<JqffszDto> list);
    boolean insertList(List<JqffszDto> list);
    List<JqffszDto> selectDtosByYhIdAndJqlx(List<JqffszDto> list);
    /**
     * @description 获取可发放假期设置
     * @param jqffszDto
     * @return
     */
    List<JqffszDto> getListWithDistribute(JqffszDto jqffszDto);

    //如果是当年最后一天 清空ljed(累计额度),nxzyed(年限转移额度)
    boolean updateToNull(JqffszDto jqffszDto);
    /*
    修改假期发放设置累计额度
 */
    boolean updateLjeds(List<JqffszDto> jqffszDtos);
}
