package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IHbsfbzDao extends BaseBasicDao<HbsfbzDto, HbsfbzModel>{
   
	/**
	 * 添加收费标准
	 * @param hbsfbzDto
	 * @return
	 */
    boolean insertsfbz(List<HbsfbzDto> hbsfbzDto);
	
	/**
	 * 查看收费标准
	 * @param hbid
	 * @return
	 */
    List<HbsfbzDto> selectByid(String hbid);
	
	/**
	 * 从基础数据中查询检测项目
	 * @return
	 */
    List<HbsfbzDto> jcsjjcxm();
	
	/**
	 * 查看
	 * @param hbid
	 * @return
	 */
    List<HbsfbzDto> viewByid(String hbid);

	/**
	 * 获取默认收费标准
	 * @param hbsfbzDto
	 * @return
	 */
    HbsfbzDto getDefaultDto(HbsfbzDto hbsfbzDto);
	
	/**
	 * 通过合作合作伙伴和项目查询收费
	 * @param hbsfbzDto
	 * @return
	 */
    HbsfbzDto getHbsfbzByHbmxAndXm(HbsfbzDto hbsfbzDto);

	/**
	 * 根据送检信息获取收费
	 * @param sjxxlist
	 * @return
	 */
    List<HbsfbzDto> getListBySjxxDtos(List<SjxxDto> sjxxlist);

	/**
	 * 批量修改
	 * @param
	 * @return
	 */
	Boolean updateSfbzByHt(SwhtmxDto swhtmxDto);

	/**
	 * 根据伙伴ID查找收费标准信息
	 * @param sjhbxxDto
	 * @return
	 */
	List<HbsfbzDto> getDtosByHbid(SjhbxxDto sjhbxxDto);
    List<SjhbxxDto> getPagedSfbzDtoList(SjhbxxDto sjhbxxDto);
    boolean batchModSfbz(List<HbsfbzDto> modList);

	/**
	 * 查找商务合同明细有效且状态为10or30的数据
	 * @param modList
	 * @return
	 */
    List<HbsfbzDto> getYxAndZt(List<HbsfbzDto> modList);

	/**
	 * 根据传入的list获取数据库中没有的收费标准
	 * @param modList
	 * @return
	 */
    List<HbsfbzDto> getAddFromTmp(List<HbsfbzDto> modList);

	/**
	 * 查找商务合同明细有效且状态为10or30的数据，子项目为null
	 * @param zxmisnotnullList
	 * @return
	 */
	List<HbsfbzDto> getYxAndZtZxmIsNull(List<HbsfbzDto> zxmisnotnullList);
}
