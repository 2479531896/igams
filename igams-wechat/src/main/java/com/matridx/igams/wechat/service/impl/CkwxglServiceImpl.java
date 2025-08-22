package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.CkwxglDto;
import com.matridx.igams.wechat.dao.entities.CkwxglModel;
import com.matridx.igams.wechat.dao.entities.WechatReferencesModel;
import com.matridx.igams.wechat.dao.post.ICkwxglDao;
import com.matridx.igams.wechat.service.svcinterface.ICkwxglService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CkwxglServiceImpl extends BaseBasicServiceImpl<CkwxglDto, CkwxglModel, ICkwxglDao> implements ICkwxglService{

	/**
	 * 根据标本类型名称和物种ids获取文献信息
	 * @param ckwxglDto
	 * @return
	 */
	public List<CkwxglDto> getDtoByYblxAndWzid(CkwxglDto ckwxglDto){
		return dao.getDtoByYblxAndWzid(ckwxglDto);
	}
	
	/**
	 * 批量新增参考文献信息
	 * @param list
	 * @return
	 */
	public boolean insertByListDto(List<CkwxglDto> list) {
		return dao.insertByListDto(list);
	}

	/**
	 * 根据送检id查询参考文献
	 * @param papers
	 * @return
	 */
	@Override
	public String getRefsToString(List<WechatReferencesModel> papers) {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		if(papers!=null && papers.size()>0) {
			List<CkwxglDto> list=dao.getRefsForWord(papers);
			if(list!=null && list.size()>0) {
				for (int i = 1; i < list.size()+1; i++) {
					if(i>1)
						sb.append("\r\n");
					sb.append("["+i+"]"+list.get(i-1).getWzzz()+"{br}>{i}"+list.get(i-1).getWzbt()+"{br}"+list.get(i-1).getQkxx());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 获取参考文献并组装
	 * @param papers
	 * @return
	 */
	@Override
	public String getRefs(List<WechatReferencesModel> papers) {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		if(papers!=null && papers.size()>0) {
			List<CkwxglDto> list=dao.getRefsForWord(papers);
			if(list!=null && list.size()>0) {
				for (int i = 1; i < list.size()+1; i++) {
					if(i>1)
						sb.append("\r\n");
					sb.append("["+i+"]"+list.get(i-1).getWzzz()+list.get(i-1).getWzbt()+list.get(i-1).getQkxx());
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取需要新增的参考文献信息
	 * @param list
	 * @return
	 */
	public List<CkwxglDto> getAddCkwxList(List<CkwxglDto> list){
		return dao.getAddCkwxList(list);
	}
	
	/**
	 * 根据list中物种id和标本类型删除参考文献信息
	 * @param list
	 * @return
	 */
	public boolean delByWzidAndYblxInList(List<CkwxglDto> list) {
		return dao.delByWzidAndYblxInList(list);
	}

	public List<CkwxglDto> getListDtoByYblxAndWzid(List<CkwxglDto> ckwxglDtos){
		return dao.getListDtoByYblxAndWzid(ckwxglDtos);
	}
}
