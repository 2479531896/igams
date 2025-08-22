package com.matridx.igams.sample.service.svcinterface;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.SbglDto;
import com.matridx.igams.sample.dao.entities.SbglModel;
import com.matridx.igams.sample.dao.entities.SbkxglDto;
import com.matridx.igams.sample.dao.entities.YbglDto;

import java.util.List;
import java.util.Map;

public interface ISbglService extends BaseBasicService<SbglDto, SbglModel>{
	/**
	 * 可以根据设备类型，父设备ID等获取设备清单
	 */
    List<SbglDto> getDeviceList(SbglDto sbglDto);
	
	/**
	 * 以树形结构获取所有设备清单
	 */
    List<SbglDto> getAllDeviceList(SbglDto sbglDto);
	
	/**
	 * 根据盒子信息获取位置内容
	 */
    Map<String,Object> getHzPosition(SbglDto sbglDto);
	
	/**
	 * 根据标本类型和数量获取一个推荐位置
	 */
    SbkxglDto getRecommendPos(SbkxglDto sbkxglDto);
	
	/**
	 * 根据标本类型和数量获取包括推荐位置等信息
	 */
    Map<String,Object> getRecommendInfo(SbkxglDto sbkxglDto);
	
	/**
	 * 根据总数转换成位置信息，并根据空闲信息进行标记
	 */
    List<JcsjDto> getPositionList(YbglDto ybglDto);

	/**
	 * 根据设备号查询设备信息
	 */
    List<SbglDto> selectSbglBySbh(String sbh);

	/**
	 * 根据盒子号、冰箱号、抽屉号，查询冰箱ID，盒子ID，抽屉ID
	 */
    List<SbglDto> selectSbidsBySbh(YbglDto ybglDto);

	/*
	* 获取所有的冰箱
	* */
    List<SbglDto> getAllBxh(String sblx);
	/*
	 * 通过父设备id获取子设备
	 * */
	List<SbglDto> getSbListByFsbid(SbglDto sbglDto);
	/*
	* 获取存放数最少的一组
	* */
	SbglDto getDefault();
	/*
	* 维护存放数
	* */
	boolean updateYcfs(SbglDto sbglDto);
	/*
	* 维护存放数
	* */
	boolean updateYcfsDtos(List<SbglDto> sbglDtos);
	/*
	 * 维护存放数出库
	 * */
    boolean updateYcfsForCK(SbglDto sbglDto);
	/*
	 * 维护存放数修改
	 * */
    boolean updateYcfsForMod(SbglDto sbglDto);
	/**
	 * 通过存储单位获取冰箱
	 */
	List<SbglDto> getSbListByJcdw(SbglDto sbglDto);
	/**
	 * 通过修改时间修改设备修改时间 锁
	 */
    boolean updateForXgsj(SbglDto sbglDto);

	/**
	 * 根据sbis查询设备信息
	 */
	List<SbglDto> getDeviceInfoBySbids(SbglDto sbglDto);
	/*
		修改样本设备信息
	 */
    boolean updateSb(SbglDto sbglDto);
	/*
		修改已存放数
	 */
	boolean updateYcfsForMods(SbglDto sbglDto);
	/*
		批量修改已存放数
	 */
	boolean batchUpdateYcfs(List<SbglDto> sbglDtos);
	/*
		修改设备s
	 */
    boolean updateSbs(List<SbglDto> list);

	/**
	 * @Description: 批量新增
	 * @param list
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/8/1 16:58
	 */
	boolean insetList(List<SbglDto> list);
}
