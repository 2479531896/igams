package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.KhglDto;
import com.matridx.igams.storehouse.dao.entities.KhglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IKhglxxDao extends BaseBasicDao<KhglDto, KhglModel> {
    /**
     * 查询客户编码最大值
     * @param khglDto
     * @return
     */
    Integer countMax(KhglDto khglDto);

    Integer deleteByIds(KhglDto khglDto);

    KhglDto getKhglDtoByKhdm(String khdm);


    /**
     * 根据搜索条件获取导出条数
     * @param khglDto
     * @return
     */
    int getCountForSearchExp(KhglDto khglDto);
    /**
     * 从数据库分页获取导出数据
     *
     * @param khglDto
     * @return
     */
    List<KhglDto> getListForSearchExp(KhglDto khglDto);

    /**
     * 从数据库分页获取导出数据
     *
     * @param khglDto
     * @return
     */
    List<KhglDto> getListForSelectExp(KhglDto khglDto);

    KhglDto getKhglDtoByKhjc(String khjc);
    boolean updateList(KhglDto khglDto);
}
