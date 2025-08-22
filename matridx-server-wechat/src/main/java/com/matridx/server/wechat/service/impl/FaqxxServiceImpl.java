package com.matridx.server.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.FaqxxDto;
import com.matridx.server.wechat.dao.entities.FaqxxModel;
import com.matridx.server.wechat.dao.post.IFaqxxDao;
import com.matridx.server.wechat.service.svcinterface.IFaqxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FaqxxServiceImpl extends BaseBasicServiceImpl<FaqxxDto, FaqxxModel, IFaqxxDao> implements IFaqxxService{

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(FaqxxDto faqxxDto)
	{
		if (StringUtil.isBlank(faqxxDto.getFaqid()))
		{
			faqxxDto.setFaqid(StringUtil.generateUUID());
		}
		int result = dao.insert(faqxxDto);
		if (result == 0)
			return false;

		return true;
	}

	/**
	 * 根据资讯类型和资讯子类型获取微信资讯列表
	 * @param faqxxDto
	 * @return
	 */
	public List<FaqxxDto> getWsWechatNewsList(FaqxxDto faqxxDto){
		return dao.getWsWechatNewsList(faqxxDto);
	}


	/**
	 * 根据标题模糊查询faq信息
	 * @param fqaxxDto
	 * @return
	 */
	public List<FaqxxDto> getFqaxxByBt(FaqxxDto fqaxxDto){
		return dao.getFqaxxByBt(fqaxxDto);
	}

	/**
	 * 根据位置查询faq信息
	 * @param fqaxxDto
	 * @return
	 */
	public FaqxxDto getFqaxxByWz(FaqxxDto fqaxxDto){
		return dao.getFqaxxByWz(fqaxxDto);
	}

	@Override
	public FaqxxDto getDtoByid(FaqxxDto faqxxDto) {
		return null;
	}


	/**
	 * 查询前5条faq信息
	 * @param fqaxxDto
	 * @return
	 */
	public List<FaqxxDto> getFaqxxgd(FaqxxDto fqaxxDto){
		return dao.getFaqxxgd(fqaxxDto);
	}


	public FaqxxDto selectFaqxxByFaqid(FaqxxDto faqxxDto) {
		return dao.selectFaqxxByFaqid(faqxxDto);
	}


	/**
	 * 删除信息
	 * @param faqxxDto
	 * @return
	 */
	public boolean deleteFaqxx(FaqxxDto faqxxDto) {

        return dao.deleteFaqxx(faqxxDto) > 0;
	}
	/**
	 * 根据ID更新信息
	 * @param faqxxDto
	 * @return
	 */
	public boolean updateFaqxx(FaqxxDto faqxxDto) {
        return dao.updateFaqxx(faqxxDto) > 0;
	}

	/**
	 * 获取机构信息列表
	 * @param faqxxDto
	 * @return
	 */
	@Override
	public List<FaqxxDto> getPagedFaqxxList (FaqxxDto faqxxDto){
//
		return dao.getPagedFaqxxList(faqxxDto);
	}
	public List<FaqxxDto> getFaqxxList() {
		return dao.getFaqxxList();
	}

	public FaqxxDto getFaqxxByBt(FaqxxDto faqxxDto) {
		return dao.getFaqxxByBt(faqxxDto);
	}

}
