package com.matridx.igams.wechat.service.svcinterface;


import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.CkwxglDto;
import com.matridx.igams.wechat.dao.entities.CkwxglModel;
import com.matridx.igams.wechat.dao.entities.WechatReferencesModel;

import java.util.List;

public interface ICkwxglService extends BaseBasicService<CkwxglDto, CkwxglModel>{

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
	 * 参考文献组装为String
	 * @param papers
	 * @return
	 */
    String getRefsToString(List<WechatReferencesModel> papers);

	/**
	 * 获取参考文献并组装
	 * @param papers
	 * @return
	 */
    String getRefs(List<WechatReferencesModel> papers);
	
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
