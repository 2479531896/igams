package com.matridx.igams.sample.service.svcinterface;

import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.YbglDto;
import com.matridx.igams.sample.dao.entities.YbglModel;

public interface IYbglService extends BaseBasicService<YbglDto, YbglModel>{
	/**
	 * 根据来源ID获取所有属于该来源的标本列表
	 */
    List<YbglDto> getDtoByLyid(String lyid);
	
	/**
	 * 插入标本信息，同时返回标本编号
	 */
    List<String> insertYbDto(YbglDto ybglDto);
	
	/**
	 * 获取标本的统计信息
	 */
    Map<String,Object> getStatisSample(YbglDto ybglDto);
	
	/**
	 * 获取标本使用的统计信息
	 */
    Map<String,Object> getStatisSampUse(YbglDto ybglDto);
	
	/**
	 * 标本统计页面，跟统计接口一样
	 */
    ModelAndView getStatisPage();
}
