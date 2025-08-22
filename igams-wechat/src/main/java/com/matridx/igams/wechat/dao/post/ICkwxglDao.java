package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.CkwxglDto;
import com.matridx.igams.wechat.dao.entities.CkwxglModel;
import com.matridx.igams.wechat.dao.entities.WechatReferencesModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICkwxglDao extends BaseBasicDao<CkwxglDto, CkwxglModel>{

	/**
	 * 根据标本类型名称和物种ids获取文献信息
	 * @param ckwxglDto
	 * @return
	 */
    List<CkwxglDto> getDtoByYblxAndWzid(CkwxglDto ckwxglDto);
	
	/**
	 * 批量新增参考文献信息
	 * @param list
	 * @return
	 */
    boolean insertByListDto(List<CkwxglDto> list);
	
	/**
	 * 根据送检id查询参考文献
	 * @param papers
	 * @return
	 */
    List<CkwxglDto> getRefsForWord(List<WechatReferencesModel> papers);
	
	/**
	 * 获取需要新增的参考文献信息
	 * @param list
	 * @return
	 */
    List<CkwxglDto> getAddCkwxList(List<CkwxglDto> list);
	
	/**
	 * 根据list中物种id和标本类型删除参考文献信息
	 * @param list
	 * @return
	 */
	boolean delByWzidAndYblxInList(List<CkwxglDto> list);

	List<CkwxglDto> getListDtoByYblxAndWzid(List<CkwxglDto> ckwxglDtos);
 }
