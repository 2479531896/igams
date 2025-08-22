package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.KsmxDto;
import com.matridx.igams.hrm.dao.entities.KsmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IKsmxDao extends BaseBasicDao<KsmxDto, KsmxModel> {
    /**
     * 选项列表
     */
    List<KsmxDto> getDtoList();


    /**
     * 根据选项代码获取对应的选项内容
     * @param
     * @return
     */
    KsmxDto getXxnrByXxdm(KsmxDto ksmxDto);


    /**
     * 根据tkid删除对应选项
     */
    void deleteByTkid(KsmxDto ksmxDto);

    /**
     * 批量新增
     */
    boolean insertList(List<KsmxDto> ksmxDtos);

}
