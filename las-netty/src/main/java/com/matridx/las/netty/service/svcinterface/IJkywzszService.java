package com.matridx.las.netty.service.svcinterface;

import com.alibaba.fastjson.JSONArray;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.netty.dao.entities.JkywzszDto;
import com.matridx.las.netty.dao.entities.JkywzszModel;
import com.matridx.las.netty.dao.entities.JqrcsszDto;

import java.util.List;
import java.util.Map;

public interface IJkywzszService extends BaseBasicService<JkywzszDto, JkywzszModel>{
	JkywzszDto queryById(String jkytdh);
	/***
	 * 添加建库仪位置信息
	 * @param dto
	 * @return
	 */
	public boolean saveJkywzsz (JqrcsszDto dto);

	/***
	 * 根据主键修改建库仪位置信息
	 * @param dto
	 * @return
	 */
	public boolean updateByWzszid (JqrcsszDto dto);

	/***
	 * 获取所有建库仪位置信息
	 * @return
	 */
	public List<JqrcsszDto> getList();

	/***
	 * 根据通道号获取建库仪位置信息
	 * @param dto
	 * @return
	 */
	public JqrcsszDto getByTdh(JqrcsszDto dto);
	/**
	 * 更新位置设置
	 */
	public Map<String ,Object> saveChannelSetup(JkywzszDto jkywzszDto);
	/**
	 * 获取已上报的通道号
	 */
	public JSONArray getJkytdAndState();

	/**
	 * 删除建库仪的通道设置
	 * @param deviceid
	 */
	public  void delJkywz(String deviceid);

	/**
	 * 删除所有通道号
	 */
	public  void delJkywzAll();
}
