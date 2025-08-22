package com.matridx.server.wechat.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.SjysxxDto;
import com.matridx.server.wechat.dao.entities.SjysxxModel;
import com.matridx.server.wechat.dao.post.ISjysxxDao;
import com.matridx.server.wechat.service.svcinterface.ISjysxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Service
public class SjysxxServiceImpl extends BaseBasicServiceImpl<SjysxxDto, SjysxxModel, ISjysxxDao> implements ISjysxxService{

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${matridx.wechat.companyurl:}")
	private String companyurl;
	
	private Logger log = LoggerFactory.getLogger(SjysxxServiceImpl.class);
	
	/**
	 * 根据送检医生查询医生信息
	 * @param sjys
	 * @return
	 */
	@Override
	public List<SjysxxDto> selectBySjys(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.selectBySjys(sjxxDto);
	}

	/**
	 * 根据送检信息新增医生信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertBySjxxDto(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		sjxxDto.setYsid(StringUtil.generateUUID());
		int result = dao.insertBySjxxDto(sjxxDto);
		if(result == 0)
			return false;
		return true;
	}

	/**
	 * 根据送检医生查询医生信息
	 * @param sjys
	 * @return
	 */
	@Override
	public List<SjysxxDto> selectSjysxxDtoBySjys(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.selectSjysxxDtoBySjys(sjxxDto);
	}

	/**
	 * 获取送检医生列表
	 * @param sjysxxDto
	 * @return
	 */
	@Override
	public List<SjysxxDto> getSjysxxDtos(SjysxxDto sjysxxDto) {
		// TODO Auto-generated method stub
		return dao.getSjysxxDtos(sjysxxDto);
	}

	/**
	 *树形结构查询送检医生
	 * @param sjysxxDto
	 * @return
	 */
	@Override
	public List<SjysxxDto> selectTreeSjysxx(SjysxxDto sjysxxDto)
	{
		// TODO Auto-generated method stub
		return dao.selectTreeSjysxx(sjysxxDto);
	}
	
	/**
	 * 查询条数
	 * @param sjysxxDto
	 * @return
	 */
	@Override
	public int getCountByWxid(SjysxxDto sjysxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getCountByWxid(sjysxxDto);
	}

	/**
	 * 获取录入人员list
	 * @param wxid
	 * @param ddid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getLrrylist(String wxid,String ddid){
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
		paramMap.add("wxid", wxid);
		paramMap.add("ddid", ddid);
		DBEncrypt crypt = new DBEncrypt();
		String url = crypt.dCode(companyurl);
		List<String> lrrylist = null;
		try {
			lrrylist = restTemplate.postForObject(url + "/ws/getLrryList", paramMap, List.class);
		} catch (Exception e) {
			log.error("获取录入人员列表getLrrylist --------- /ws/getLrryList 方法请求异常！ ");
		}
		if(lrrylist == null || lrrylist.size() == 0){
			lrrylist = new ArrayList<String>();
			if(StringUtil.isNotBlank(ddid)){
				lrrylist.add(ddid);
			}else if(StringUtil.isNotBlank(wxid)){
				lrrylist.add(wxid);
			}else{
				lrrylist.add("default");
			}
		}
		return lrrylist;
	}
}
