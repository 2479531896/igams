package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SwkhglDto;
import com.matridx.igams.wechat.dao.entities.SwkhglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ISwkhglDao extends BaseBasicDao<SwkhglDto, SwkhglModel>{

    /**
     * 选中导出
     * @param swkhglDto
     * @return
     */
    List<SwkhglDto> getListForSelectExp(SwkhglDto swkhglDto);

    /**
     * 搜索条件分页获取导出信息
     * @param swkhglDto
     * @return
     */
    List<SwkhglDto> getListForSearchExp(SwkhglDto swkhglDto);

    /**
     * 获取导出条数
     * @param swkhglDto
     * @return
     */
    int getCountForSearchExp(SwkhglDto swkhglDto);
    /**
     * 查询是否重复数据
     */
    List<SwkhglDto> getRepeatDtoList(SwkhglDto swkhglDto);

    /**
     * 更新对账字段
     * @param swkhglDto
     * @return
     */
    boolean updateDzzd(SwkhglDto swkhglDto);

    /**
     * 定时更新状态区分
     */
    boolean updateCustomStateDistinguish(String date);

}
