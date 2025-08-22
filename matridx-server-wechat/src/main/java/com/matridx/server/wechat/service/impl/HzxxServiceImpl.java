package com.matridx.server.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.server.wechat.dao.entities.HzxxDto;
import com.matridx.server.wechat.dao.entities.HzxxModel;
import com.matridx.server.wechat.dao.entities.XxcqxxjlDto;
import com.matridx.server.wechat.dao.entities.ZdyfaDto;
import com.matridx.server.wechat.dao.post.IHzxxDao;
import com.matridx.server.wechat.service.svcinterface.IHzxxService;
import com.matridx.server.wechat.service.svcinterface.IXxcqxxjlService;
import com.matridx.server.wechat.service.svcinterface.IZdybtwzService;
import com.matridx.server.wechat.service.svcinterface.IZdyfaService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HzxxServiceImpl extends BaseBasicServiceImpl<HzxxDto, HzxxModel, IHzxxDao> implements IHzxxService{

	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IXxcqxxjlService xxcqxxjlService;
	@Autowired
	IZdyfaService zdyfaService;
	@Autowired
	IZdybtwzService zdybtwzService;
	
	/**
	 * 插入患者信息
	 * @param hzxxdto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(HzxxDto hzxxdto){
		hzxxdto.setHzid(StringUtil.generateUUID());
		int result = dao.insert(hzxxdto);
        return result != 0;
    }
	
	/**
	 * 患者信息统计
	 * @param hzxxDto
	 * @return
	 */
	@Override
	public List<HzxxDto> getPatientStatis(HzxxDto hzxxDto) {
		// TODO Auto-generated method stub
		return dao.getPatientStatis(hzxxDto);
	}

	/**
	 * 修改患者信息
	 * @param hzxxDto
	 * @return
	 */
	@Override
	public boolean updatePatient(HzxxDto hzxxDto) {
		// TODO Auto-generated method stub
		int result = dao.updatePatient(hzxxDto);
		if(result == 0) {
            return false;
        }
		if(hzxxDto.getFjids()!=null && hzxxDto.getFjids().size() > 0){
			for (int i = 0; i < hzxxDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(hzxxDto.getFjids().get(i),hzxxDto.getHzid());
				if(!saveFile) {
                    return false;
                }
			}
		}
		return true;
	}

	/**
	 * 新增患者信息
	 * @param hzxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertPatient(HzxxDto hzxxDto) {
		// TODO Auto-generated method stub
		hzxxDto.setLrry(hzxxDto.getWxid());
		boolean result = insertDto(hzxxDto);
		if(!result) {
            return false;
        }
		if(hzxxDto.getFjids()!=null && hzxxDto.getFjids().size() > 0){
			for (int i = 0; i < hzxxDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(hzxxDto.getFjids().get(i),hzxxDto.getHzid());
				if(!saveFile) {
                    return false;
                }
			}
		}
		if(StringUtil.isNotBlank(hzxxDto.getJlid())){
			Map<String,String> infoMap = new HashMap<>();
			infoMap.put("xm", hzxxDto.getXm());
			infoMap.put("xb", hzxxDto.getXbmc());
			infoMap.put("nl", hzxxDto.getNl());
			XxcqxxjlDto xxcqxxjlDto = new XxcqxxjlDto();
			xxcqxxjlDto.setJlid(hzxxDto.getJlid());
			XxcqxxjlDto t_xxcqxxjlDto = xxcqxxjlService.getDto(xxcqxxjlDto);
			if(infoMap.toString().equals(t_xxcqxxjlDto.getCqxx())){
				xxcqxxjlDto.setSfzq("1");
			}else{
				xxcqxxjlDto.setSfzq("0");
			}
			//保存信息到学习表
			xxcqxxjlDto.setSjxx(infoMap.toString());
			xxcqxxjlDto.setXgry(hzxxDto.getWxid());
			xxcqxxjlDto.setHzid(hzxxDto.getHzid());
			xxcqxxjlService.update(xxcqxxjlDto);
			//对比准确率度
			boolean isStudy = true;
			xxcqxxjlDto.setWxid(hzxxDto.getWxid());
			List<XxcqxxjlDto> xxcqxxjlDtos = xxcqxxjlService.getRecentCorrect(xxcqxxjlDto);
			if(xxcqxxjlDtos != null && xxcqxxjlDtos.size() > 0){
				for (int i = 0; i < xxcqxxjlDtos.size(); i++) {
					if(StringUtil.isNotBlank(xxcqxxjlDtos.get(i).getSfzq()) && "1".equals(xxcqxxjlDtos.get(i).getSfzq())){
						isStudy = false;
					}
				}
			}
			//判断是否需要学习
			if(isStudy){
				//判断识别是否准确
				ZdyfaDto zdyfaDto = new ZdyfaDto();
				zdyfaDto.setFaid(hzxxDto.getFaid());
				List<ZdyfaDto> zdyfaDtos = zdyfaService.getDtoList(zdyfaDto);
				@SuppressWarnings("unchecked")
				List<List<Map<String, String>>> resultList = JSON.parseObject(t_xxcqxxjlDto.getYsxx(), List.class);
				Map<String,Map<String, String>> keyMap = new HashMap<>();
				if(zdyfaDtos != null && zdyfaDtos.size() > 0){
					for (int i = 0; i < zdyfaDtos.size(); i++) {
						Map<String, String> map = new HashMap<>();
						map.put("words", zdyfaDtos.get(i).getZdxx());
						map.put("wordflg",zdyfaDtos.get(i).getBhbj());
						map.put("rightpoint", zdyfaDtos.get(i).getYjg());
						map.put("rightword", zdyfaDtos.get(i).getYbj());
						map.put("bottompoint", zdyfaDtos.get(i).getXjg());
						map.put("bottomword", zdyfaDtos.get(i).getXbj());
						map.put("leftpoint", zdyfaDtos.get(i).getZjg());
						map.put("leftword", zdyfaDtos.get(i).getZbj());
						map.put("toppoint", zdyfaDtos.get(i).getSjg());
						map.put("topword", zdyfaDtos.get(i).getSbj());
						map.put("sameflg", zdyfaDtos.get(i).getYwbj());
						keyMap.put(zdyfaDtos.get(i).getZdmc(), map);
					}
				}
				List<Map<String,String>> recoderList = new ArrayList<>();
				Map<String, Map<String, String>> studyResult = ToolUtil.study(resultList, keyMap, infoMap, recoderList);
				//处理学习结果
				zdybtwzService.dealStudy(studyResult, hzxxDto);
			}
		}
		
		return true;
	}



}
