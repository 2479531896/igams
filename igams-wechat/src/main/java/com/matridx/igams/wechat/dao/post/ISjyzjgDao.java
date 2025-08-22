package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjyzjgDto;
import com.matridx.igams.wechat.dao.entities.SjyzjgModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISjyzjgDao extends BaseBasicDao<SjyzjgDto, SjyzjgModel> {

    int insert(SjyzjgDto sjyzjgDto);

    int update(SjyzjgDto sjyzjgDto);

    int delete(SjyzjgDto sjyzjgDto);

    int deleteByYzid(SjyzjgDto sjyzjgDto);

    /**
     * 通过样本编号获取相关的信息
     * @return
     */
    List<SjyzjgDto> getInfoByYbbh(SjyzjgDto sjyzjgDto);

    int updateListJl(List<SjyzjgDto> sjyzjgDtoList);

    int updateListCzbs(List<String> list);

    /**
     * 通过验证ID、类别、结果获取送检验证结果数据
     * @param sjyzjgDtoList
     * @return
     */
    List<SjyzjgDto> getByYzlbYzidYzjg(List<SjyzjgDto> sjyzjgDtoList);

    /**
     * 批量新增送检验证结果数据
     * @param updateSjyzjgList
     */
    void insertList(List<SjyzjgDto> updateSjyzjgList);
}
