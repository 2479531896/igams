package com.matridx.server.wechat.service.svcinterface;
import java.util.List;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.FaqxxDto;
import com.matridx.server.wechat.dao.entities.FaqxxModel;


public interface IFaqxxService extends BaseBasicService<FaqxxDto, FaqxxModel>{

	/**
	 * 根据资讯类型和资讯子类型获取微信资讯列表
	 * @param faqxxDto
	 * @return
	 */
	List<FaqxxDto> getWsWechatNewsList(FaqxxDto faqxxDto);

	/**
	 * 查询信息
	 * @return
	 */
    List<FaqxxDto> getFaqxxList();

	/**
	 * 获取信息列表
	 * @param faqxxDto
	 * @return
	 */

    List<FaqxxDto> getPagedFaqxxList(FaqxxDto faqxxDto);

	/**
	 * 根据ID查询信息
	 * @param faqxxDto
	 * @return
	 */

    FaqxxDto selectFaqxxByFaqid(FaqxxDto faqxxDto);

	/**
	 * 根据ID更新信息
	 * @param faqxxDto
	 * @return
	 */


    boolean updateFaqxx(FaqxxDto faqxxDto);

	/**
	 * 删除信息
	 * @param faqxxDto
	 * @return
	 */

    boolean deleteFaqxx(FaqxxDto faqxxDto);

	/**
	 * 根据标题查询信息
	 * @param faqxxDto
	 * @return
	 */
    FaqxxDto getFaqxxByBt(FaqxxDto faqxxDto);


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
	 * @param faqxxDto
	 * @return
	 */
    FaqxxDto getDtoByid(FaqxxDto faqxxDto);
	/**
	 * 获取某一个的具体信息
	 * @param fqaxxDto
	 * @return
	 */

	List<FaqxxDto> getFaqxxgd(FaqxxDto fqaxxDto);




}
