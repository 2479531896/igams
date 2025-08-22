package com.matridx.igams.storehouse.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.JycglDto;
import com.matridx.igams.storehouse.dao.entities.JycglModel;
import com.matridx.igams.storehouse.dao.post.IJycglDao;
import com.matridx.igams.storehouse.service.svcinterface.IJycglService;
@Service()
public class JycglServiceImpl extends BaseBasicServiceImpl<JycglDto, JycglModel, IJycglDao> implements IJycglService {
	/**
	 * 获取用户检验车货物id及数量
	 */
	@Override
	public JycglDto getJychwidList(String userId,String kzcs1) {
		StringBuilder buffer = new StringBuilder();
		String jycids = "";
		String size = "0";
		List<String> ids = dao.getJychwidList(userId,kzcs1);
		if(!CollectionUtils.isEmpty(ids)) {
			for(String id:ids) {
				buffer.append(",").append(id);
			}
			jycids = buffer.substring(1);
			size = String.valueOf(ids.size());
		}
		JycglDto jyc = new JycglDto();
		jyc.setHwids(jycids);
		jyc.setHwCount(size);
		return jyc;
	}
	/**
	 * 获取检验车里货物信息
	 */
	@Override
	public List<JycglDto> getGoods(JycglDto jycgl) {
		// TODO Auto-generated method stub
		return dao.getGoods(jycgl);
	}
	/**
     * 根据id删除购物车里的数据
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteHw(String ryid, List<String> hwids) {
		JycglDto jyc = new JycglDto();
		jyc.setRyid(ryid);
		jyc.setIds(hwids);
        dao.deleteHw(jyc);
    }

}
