package com.matridx.igams.wechat.service.impl;


import java.util.List;

import org.springframework.stereotype.Service;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.YhqtxxDto;
import com.matridx.igams.wechat.dao.entities.YhqtxxModel;
import com.matridx.igams.wechat.dao.post.IYhqtxxDao;
import com.matridx.igams.wechat.service.svcinterface.IYhqtxxService;

@Service
public class YhqtxxServiceImpl extends BaseBasicServiceImpl<YhqtxxDto, YhqtxxModel, IYhqtxxDao> implements IYhqtxxService{

	/**
	 * 查询所有的用户信息
	 * @return
	 */
	@Override
	public List<YhqtxxDto> getXtyh() {
		// TODO Auto-generated method stub
		return dao.getXtyh();
	}

	/**
	 * 验证码验证
	 * @return
	 */
	@Override
	public YhqtxxDto getDtoByYzm(YhqtxxDto yhqtxxDto) {
		return dao.getDtoByYzm(yhqtxxDto);
	}

	/**
	 * 用户列表中伙伴设置查询出选中用户的所有上级的id
	 * @param yhid
	 * @return
	 */
	@Override
	public List<String> getSjidList(String yhid) {
		// TODO Auto-generated method stub
		return dao.getSjidList(yhid);
	}

	@Override
	public boolean updateZj(YhqtxxDto yhqtxxDto) {
		return dao.updateZj(yhqtxxDto);
	}

	/**
	 * 查询该用户的下级信息
	 * @param yhid
	 * @return
	 */
	public List<String> getXjyhList(String yhid){
		return dao.getXjyhList(yhid);
	}
}
