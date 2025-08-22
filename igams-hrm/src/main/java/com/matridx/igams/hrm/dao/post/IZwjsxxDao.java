package com.matridx.igams.hrm.dao.post;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.ZwjsxxDto;
import com.matridx.igams.hrm.dao.entities.ZwjsxxModel;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface IZwjsxxDao extends BaseBasicDao<ZwjsxxDto, ZwjsxxModel> {
    /**
     * @description 批量插入职位晋升信息
     */
    boolean insertZwjsDtos(List<ZwjsxxDto> zwjsDtos);
}
