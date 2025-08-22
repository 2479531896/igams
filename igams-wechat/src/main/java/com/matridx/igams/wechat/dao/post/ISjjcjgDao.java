package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.wechat.dao.entities.SjxxDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjjcjgDto;
import com.matridx.igams.wechat.dao.entities.SjjcjgModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISjjcjgDao extends BaseBasicDao<SjjcjgDto, SjjcjgModel>{
    /**
     * 批量插入
     */
    Boolean insertList(List<SjjcjgDto> list);
    /**
     * 送检修改
     */
    Boolean modSj(SjjcjgDto sjjcjgDto);
    /**
     * 复测修改
     */
    Boolean modFc(SjjcjgDto sjjcjgDto);
    /**
     * 批量修改
     */
    Boolean updateList(List<SjjcjgDto> sjjcjgDto);

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
     * 获取一周的检测数据
     * @param sjjcjgDto
     * @return
     */
    List<SjjcjgDto> getAllList(SjjcjgDto sjjcjgDto);

    /**
     * 查询范围ids
     */
    List<String> getYwidList(SjxxDto sjxxDto);

    /**
     * 批量更新结论
     * @param list
     * @return
     */
    boolean updatesjjcjgJlList(List<SjjcjgDto> list);
}
