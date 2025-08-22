package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.common.dao.entities.XszbModel;

import java.util.List;

@Mapper
public interface IXszbDao extends BaseBasicDao<XszbDto, XszbModel>{

	/**
	 * 根据工号（即xtyh表的yhm）取到系统用户的信息
	 */
	XszbDto getYhxxByYhm(String gh);

    List<XszbDto> getXszbQyxx(XszbDto xszbDto);

    /**
	 * 判断插入的销售指标数据是否重复
     */
	XszbDto getXszbRepeat(XszbDto xszbDto);

	/**
	 * 根据伙伴名称取送检伙伴信息
	 */
	List<XszbDto> getSjhbByHbmc(String hbmc);

	/**
	 * 根据子分类查找销售指标数据
	 */
    List<String> getDtosByZfl(XszbDto xszbDto);
	/*
	 * 获取我的销售指标
	 */
	List<XszbDto> getMySalesIndicator(XszbDto xszbDto);
	/*
    	获取下属的指标
 	*/
	List<XszbDto> getSubordinateIndicator(XszbDto xszbDto);
	/*
		确认销售指标
	 */
	boolean confirmSalesIndicator(XszbDto xszbDto);
	/*
		废弃之前的销售指标
	 */
	void discardSalesIndicator(XszbDto xszbDto);
	/*
		插入销售指标数据
	 */
	void insertXszbDtos(List<XszbDto> xszbDtos);
	/*
	 * 获取销售指标清单
	 */
	List<XszbDto> getSalesIndicatorList(XszbDto xszbDto);
	/*
    	获取销售指标的销售额和目标
 	*/
	XszbDto getSalesIndicator(XszbDto xszbDto);

	List<XszbDto> getTempSaleList(List<XszbDto> xszbDtos);

	/**
	 * 批量更新指标
	 * @param xszbDtos
	 * @return
	 */
	boolean updateDtoList(List<XszbDto> xszbDtos);
}
