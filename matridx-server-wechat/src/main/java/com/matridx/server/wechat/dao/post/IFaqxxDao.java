package com.matridx.server.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.FaqxxDto;
import com.matridx.server.wechat.dao.entities.FaqxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IFaqxxDao extends BaseBasicDao<FaqxxDto, FaqxxModel>{

	/**
	 * 根据资讯类型和资讯子类型获取微信资讯列表
	 * @param faqxxDto
	 * @return
	 */
	List<FaqxxDto> getWsWechatNewsList(FaqxxDto faqxxDto);

	/**
	 * 查询机构信息
	 * @return
	 */
    List<FaqxxDto> getFaqxxList();

	/**
	 * 根据标题模糊查询faq信息
	 * @param fqaxxDto
	 * @return
	 */
	List<FaqxxDto> getFqaxxByBt(FaqxxDto fqaxxDto);

	/**
	 * 根据位置查询faq信息
	 * @param fqaxxDto
	 * @return
	 */
	FaqxxDto getFqaxxByWz(FaqxxDto fqaxxDto);
	
	/**
	 * 查询前5条faq信息
	 * @param fqaxxDto
	 * @return
	 */
	List<FaqxxDto> getFaqxxgd(FaqxxDto fqaxxDto);

	/**
			* 获取信息列表
	 * @param FaqxxDto
	 * @return
			 */
	public List<FaqxxDto> getPagedFaqxxList(FaqxxDto faqxxDto);
	public FaqxxDto selectFaqxxByFaqid(FaqxxDto FaqxxDto);

	public int deleteFaqxx(FaqxxDto FaqxxDto);
	public FaqxxDto getFaqxxByBt(FaqxxDto faqxxDto);
	public int updateFaqxx(FaqxxDto FaqxxDto);

	/**
	 * 查询机构信息
	 * @return
	 */
	/*public List<JgxxDto> getJgxxList();

	*//**
	 * 获取机构信息列表
	 * @param jgxxDto
	 * @return
	 *//*
	public List<JgxxDto> getPagedJgxxList(JgxxDto jgxxDto);

	*//**
	 * 根据机构ID查询机构信息
	 * @param jgxxDto
	 * @return
	 *//*
	public FaqxxDto selectFaqxxByFaqid(FaqxxDto FaqxxDto);

	*//**
	 * 根据机构ID更新机构信息
	 * @param jgxxDto
	 * @return
	 *//*
	public int updateJgxx(JgxxDto jgxxDto);

	*//**
	 * 删除机构信息
	 * @param jgxxDto
	 * @return
	 *//*
	public int deleteJgxx(JgxxDto jgxxDto);

	*//**
	 * 根据机构名称查询机构信息
	 * @param jgxxDto
	 * @return
	 *//*
	public JgxxDto getJgxxByJgmc(JgxxDto jgxxDto);

	*//**
	 * 查询机构列表(除本身)
	 * @param jgxxDto
	 * @return
	 *//*
	public List<JgxxDto> getPagedOtherJgxxList(JgxxDto jgxxDto);

	*//**
	 * 通过机构名称模糊查询
	 * @param jgxxDto
	 * @return
	 *//*
	public List<JgxxDto> selsetJgxxByjgmc(JgxxDto jgxxDto);
	*//**
	 * 更具id 更改扩展参数有2
	 * @param jgxxDto
	 * @return
	 *//*
	public Integer updateKzcs2ByJgid(JgxxDto jgxxDto);

	*//**
	 * 查找所有机构的数据，包括删除标记为1的
	 * @return
	 *//*
	public List<JgxxDto> getAllJgxxList();
	*/
}
