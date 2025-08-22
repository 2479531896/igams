package com.matridx.igams.wechat.service.impl;


import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.DdyhDto;
import com.matridx.igams.wechat.dao.entities.DdyhModel;
import com.matridx.igams.wechat.dao.post.IDdyhDao;
import com.matridx.igams.wechat.service.svcinterface.IDdyhService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class DdyhServiceImpl extends BaseBasicServiceImpl<DdyhDto, DdyhModel, IDdyhDao> implements IDdyhService{

	/**
	 * 根据钉钉ID获取用户列表
	 * @param ddid
	 * @return
	 */
	public DdyhDto getUserByDdid(String ddid) {
		return dao.getUserByDdid(ddid);
	}

	/**
	 * 根据用户ID获取用户角色信息
	 * @param yhid
	 * @return
	 */
	public List<DdyhDto> getUserByYhid(String yhid){
		return dao.getUserByYhid(yhid);
	}
	
	/**
	 * 通过用户id查询单位限定
	 * @param yhid
	 * @return
	 */
	@Override
	public boolean getDwxd(String yhid){
		// TODO Auto-generated method stub
		List<DdyhDto> list = dao.getUserByYhid(yhid);
		if(!CollectionUtils.isEmpty(list)) {
			int flg=0;
            for (DdyhDto ddyhDto : list) {
                if ("0".equals(ddyhDto.getDwxdbj())) {
                    flg++;
                }
            }
            return flg == 0;
		}
		return false;
	}

	/**
	 * 根据用户名查询用户ID
	 * @param yhm
	 * @return
	 */
	@Override
	public List<DdyhDto> getUserByYhm(String yhm) {
		// TODO Auto-generated method stub
		return dao.getUserByYhm(yhm);
	}
}
