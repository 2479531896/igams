package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.CnvjgDto;
import com.matridx.igams.bioinformation.dao.entities.WkcxbbDto;
import com.matridx.igams.bioinformation.dao.entities.WkcxbbModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IWkcxbbDao extends BaseBasicDao<WkcxbbDto, WkcxbbModel> {

    /**
     * 通过版本号和文库编号获取文库版本信息
     */
    WkcxbbDto getDtoByBbhAndWkbh(WkcxbbDto wkcxbbDto);
    /**
     * 通过人的Taxid和wkckbb的jgid查询相关信息
     */
    WkcxbbDto getDtoByJgid(WkcxbbDto wkcxbbDto);
    /**
     * 查询送检信息相关信息
     */
    WkcxbbDto getSjxx(WkcxbbDto wkcxbbDto);
    /**
     * 查询物种信息相关信息
     */
    WkcxbbDto getWzqzfw(WkcxbbDto wkcxbbDto);

    WkcxbbDto getZxbbDto(WkcxbbDto wkcxbbDto);

    WkcxbbDto getZxbbDtoByXpAndWKbh(WkcxbbDto wkcxbbDto);

    /**
     * 添加物种Q值范围
     */
    boolean addWzqf(List<WkcxbbDto> list);
    boolean updateYblx();
    /**
     * 生信数据同步
     */
    List<WkcxbbDto> syncVersionData(WkcxbbDto wkcxbbDto);
    /**
     * 批量新增
     */
    boolean insertList(List<WkcxbbDto> list);






    boolean insertXpxx (List<Map<String,String>> list);

    boolean insertSample (List<Map<String,String>> list);

    boolean insertCnvjgxq (List<Map<String,String>> list);

    boolean insertCnvjg (List<CnvjgDto> list);


}
