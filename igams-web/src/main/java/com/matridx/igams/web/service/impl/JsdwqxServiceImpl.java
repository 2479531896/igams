package com.matridx.igams.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.JsdwqxDto;
import com.matridx.igams.web.dao.entities.JsdwqxModel;
import com.matridx.igams.web.dao.entities.XtjsDto;
import com.matridx.igams.web.dao.post.IJsdwqxDao;
import com.matridx.igams.web.service.svcinterface.IJsdwqxService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JsdwqxServiceImpl extends BaseBasicServiceImpl<JsdwqxDto, JsdwqxModel, IJsdwqxDao> implements IJsdwqxService{

	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	/**
	 * 查询机构列表(已选)
	 * @param jsdwqxDto
	 * @return
	 */
	public List<JsdwqxDto> getPagedYxJgxxList(JsdwqxDto jsdwqxDto){
		return dao.getPagedYxJgxxList(jsdwqxDto);
	}

	/**
	 * 查询机构列表(已选)(不分页)
	 * @param jsdwqxDto
	 * @return
	 */
	public List<JsdwqxDto> getYxJgxxList(JsdwqxDto jsdwqxDto){
		return dao.getYxJgxxList(jsdwqxDto);
	}

	/**
	 * 添加角色机构
	 * @param jsdwqxDto
	 * @return
	 */
	public boolean toSelectedJg(JsdwqxDto jsdwqxDto) {
		boolean result = dao.toSelectedJg(jsdwqxDto);
		if(result){
			jsdwqxDto.setPrefix(prefixFlg);
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.jsdwqx.insert",JSONObject.toJSONString(jsdwqxDto));
		}
		return result;
	}
	
	/**
	 * 去除角色机构
	 * @param jsdwqxDto
	 * @return
	 */
	public boolean toOptionalJg(JsdwqxDto jsdwqxDto) {
		boolean result = dao.toOptionalJg(jsdwqxDto);
		if(result){
			jsdwqxDto.setPrefix(prefixFlg);
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.jsdwqx.del",JSONObject.toJSONString(jsdwqxDto));
		}
		return result;
	}
	
	/**
	 * 获取角色机构权限
	 * @param xtjsDto
	 * @return
	 */
	@Override
	public Map<String, List<JsdwqxDto>> getPermission(XtjsDto xtjsDto) {
		// TODO Auto-generated method stub
		Map<String, List<JsdwqxDto>> map = new HashMap<>();
		List<JsdwqxDto> jsdwqxDtos = dao.getPermission(xtjsDto);
		if(!CollectionUtils.isEmpty(jsdwqxDtos)){
			List<JsdwqxDto> selectList = new ArrayList<>();
			List<JsdwqxDto> unselectList = new ArrayList<>();
            for (JsdwqxDto jsdwqxDto : jsdwqxDtos) {
                if (jsdwqxDto.getJgflg() == 1) {
                    selectList.add(jsdwqxDto);
                } else {
                    unselectList.add(jsdwqxDto);
                }
            }
			map.put("selectList", selectList);
			map.put("unselectList", unselectList);
		}
		return map;
	}

	/**
	 * 添加角色机构权限
	 * @param xtjsDto
	 * @return
	 */
	@Override
	public boolean updateByJsid(XtjsDto xtjsDto) {
		// TODO Auto-generated method stub
		//先删除再添加
		dao.deleteByJsid(xtjsDto);
		xtjsDto.setPrefix(prefixFlg);
		amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.jsdwqx.del",JSONObject.toJSONString(xtjsDto));
		boolean isSuccesss = insertByJsid(xtjsDto);
		if(!isSuccesss)
			return false;
		return false;
	}
	
	@Override
	public boolean insertByJsid(XtjsDto xtjsDto) {
		// TODO Auto-generated method stub
		List<String> jgids = xtjsDto.getJsjgids();
		if(!CollectionUtils.isEmpty(jgids)){
			List<JsdwqxDto> jsdwqxList = new ArrayList<>();
            for (String jgid : jgids) {
                JsdwqxDto jsdwqxDto = new JsdwqxDto();
                jsdwqxDto.setJsid(xtjsDto.getJsid());
                jsdwqxDto.setJgid(jgid);
                jsdwqxList.add(jsdwqxDto);
            }
			boolean isSuccesss = dao.insertByJsid(jsdwqxList);
			if(!isSuccesss)
				return false;
			xtjsDto.setIds(jgids);
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.jsdwqx.insert",JSONObject.toJSONString(xtjsDto));
		}
		return true;
	}
}
