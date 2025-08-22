package com.matridx.igams.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.PayinfoDto;
import com.matridx.igams.wechat.dao.entities.PayinfoModel;

@Mapper
public interface IPayinfoDao extends BaseBasicDao<PayinfoDto, PayinfoModel>{


	/**
	 * 根据业务信息查询支付信息
	 * @param payinfoDto
	 * @return
	 */
	List<PayinfoDto> selectByYwxx(PayinfoDto payinfoDto);

	/**
	 * 支付列表查询某条支付信息
	 * @return
	 */
	PayinfoDto getDtoPayment(PayinfoDto payinfoDto);

	/**
	 * 支付列表选中导出
	 * @param payinfoDto
	 * @return
	 */
    List<PayinfoDto> getListForSelectExp(PayinfoDto payinfoDto);

    /**
     * 搜索条件分页获取导出信息
     * @param payinfoDto
     * @return
     */
	List<PayinfoDto> getListForSearchExp(PayinfoDto payinfoDto);

	/**
	 * 根据搜索条件获取导出条数
	 * @param payinfoDto
	 * @return
	 */
	int getCountForSearchExp(PayinfoDto payinfoDto);

	/**
	 * 查询总金额
	 * @param
	 * @param
	 * @return
	 */
	PayinfoDto selectPayYuan(PayinfoDto payinfoDto);

}
