package com.matridx.igams.sample.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.sample.dao.entities.SbglDto;
import com.matridx.igams.sample.dao.entities.SbglModel;
import com.matridx.igams.sample.dao.entities.YbglDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISbglDao extends BaseBasicDao<SbglDto, SbglModel>{
	/**
	 * 获取设备清单
	 */
    List<SbglDto> getDeviceList(SbglDto sbglDto);
	
	/**
	 * 获取所有设备清单
	 */
    List<SbglDto> getAllDeviceList(SbglDto sbglDto);
	
	/**
	 * 根据设备ID获取设备信息以及标本信息
	 */
    SbglDto getSbYbDto(SbglDto sbglDto);

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
    List<SbglDto> getAllBxh(SbglDto sbglDto);

	/*
	 * 通过父设备id获取子设备
	 * */
	List<SbglDto> getSbListByFsbid(SbglDto sbglDto);
	/*
	 * 获取存放数最少的一组
	 * */
	SbglDto getDefault();
	// 维护存放数
	boolean updateYcfs(SbglDto sbglDto);

	// 维护已存放数
	boolean updateYcfsDtos(List<SbglDto> sbglDtos);

    boolean updateYcfsForCK(SbglDto sbglDto);

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
	 * @Date: 2024/8/1 16:59
	 */
	boolean insetList(List<SbglDto> list);
}
