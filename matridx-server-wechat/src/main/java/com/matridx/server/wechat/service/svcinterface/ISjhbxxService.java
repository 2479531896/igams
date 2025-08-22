package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjhbxxDto;
import com.matridx.server.wechat.dao.entities.SjhbxxModel;

public interface ISjhbxxService extends BaseBasicService<SjhbxxDto, SjhbxxModel>{

	/**
	 * 保存本地删除信息至微信服务器
	 * @param sjhbxxDto
	 * @throws BusinessException 
	 */
    void receiveDelPartner(SjhbxxDto sjhbxxDto) throws BusinessException;

	/**
	 * 保存本地新增信息至微信服务器
	 * @param sjhbxxDto
	 * @throws BusinessException 
	 */
    void receiveAddPartner(SjhbxxDto sjhbxxDto) throws BusinessException;

	/**
	 * 保存本地更新信息至微信服务器
	 * @param sjhbxxDto
	 * @throws BusinessException 
	 */
    void receiveModPartner(SjhbxxDto sjhbxxDto) throws BusinessException;

	/**
	 * 通过hbid查询 合作伙伴
	 * @param list
	 * @return
	 */
    List<String> getHbmcByHbid(List<String> list);

	/**
	 * 查询伙伴为'无'的用户信息
	 * @return
	 */
    List<SjhbxxDto> getXtyhByHbmc();
	/**
	 * 启用
	 * @param sjhbxxDto
	 * @return
	 */
	boolean enablepartner(SjhbxxDto sjhbxxDto);

	/**
	 * 停用
	 * @param sjhbxxDto
	 * @return
	 */
	boolean disablepartner(SjhbxxDto sjhbxxDto);

}
