package com.matridx.igams.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.YhqtxxDto;
import com.matridx.igams.wechat.dao.entities.YhqtxxModel;

@Mapper
public interface IYhqtxxDao extends BaseBasicDao<YhqtxxDto, YhqtxxModel>{

	/**
	 * 查询所有的用户信息
	 * @return
	 */
	List<YhqtxxDto> getXtyh();
	
	/**
	 * 验证码验证
	 * @return
	 */
	YhqtxxDto getDtoByYzm(YhqtxxDto yhqtxxDto);

	/**
	 * 用户列表中伙伴设置查询出选中用户的所有上级的id
	 * @param yhid
	 * @return
	 */
	List<String> getSjidList(String yhid);
	/**
	 * @description 修改职级
	 * @param yhqtxxDto
	 * @return
	 */
	boolean updateZj(YhqtxxDto yhqtxxDto);

	/**
	 * 查询该用户的下级信息
	 * @param yhid
	 * @return
	 */
	List<String> getXjyhList(String yhid);
}
