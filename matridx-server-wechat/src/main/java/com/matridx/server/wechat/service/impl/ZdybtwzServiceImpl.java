package com.matridx.server.wechat.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.server.wechat.dao.entities.HzxxDto;
import com.matridx.server.wechat.dao.entities.ZdybtwzDto;
import com.matridx.server.wechat.dao.entities.ZdybtwzModel;
import com.matridx.server.wechat.dao.post.IZdybtwzDao;
import com.matridx.server.wechat.service.svcinterface.IZdybtwzService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Service
public class ZdybtwzServiceImpl extends BaseBasicServiceImpl<ZdybtwzDto, ZdybtwzModel, IZdybtwzDao> implements IZdybtwzService{

	@Autowired
	IFjcfbService fjcfbService;
	
	/**
	 * 点击保存按钮保存附件信息和标题位置信息
	 * @param map
	 * @param fjcfbDto
	 * @return
	 */
	public boolean SaveImageAndWz(List<Map<String,Map<String,String>>> list,FjcfbDto fjcfbDto,String faid) {
		List<ZdybtwzDto> btlist=new ArrayList<ZdybtwzDto>();
		for(int i=0;i<list.size();i++) {
			ZdybtwzDto zdybtwzDto=new ZdybtwzDto();
			for(Entry<String, Map<String, String>> vo : list.get(i).entrySet()){
			   Map<String, String> value = vo.getValue();
			   zdybtwzDto.setZdmc(vo.getKey());
			   zdybtwzDto.setFaid(faid);
			   zdybtwzDto.setZdyid(StringUtil.generateUUID());
			   zdybtwzDto.setZdxx(value.get("words"));
			   zdybtwzDto.setBhbj(value.get("wordflg"));
			   zdybtwzDto.setZbj(value.get("leftword"));
			   zdybtwzDto.setZjg(value.get("leftpoint"));
			   zdybtwzDto.setYbj(value.get("rightword"));
			   zdybtwzDto.setYjg(value.get("rightpoint"));
			   zdybtwzDto.setSbj(value.get("topword"));
			   zdybtwzDto.setSjg(value.get("toppoint"));
			   zdybtwzDto.setXbj(value.get("bottomword"));
			   zdybtwzDto.setXjg(value.get("bottompoint"));
			   zdybtwzDto.setLrry(fjcfbDto.getLrry());
			}
			btlist.add(zdybtwzDto);
		}
		boolean saveimg=fjcfbService.save2RealFile(fjcfbDto.getFjid(), faid);//保存图片至正式文件夹
		if(!saveimg)
			return false;
		FjcfbDto fjxx=fjcfbService.getDto(fjcfbDto);
		DBEncrypt crypt = new DBEncrypt();
		fjcfbDto.setWjlj(crypt.dCode(fjxx.getWjlj()));
		int result=dao.insertByZdybtwzDtos(btlist);
		if(result>0){
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 学习结果处理
	 * @param studyResult
	 * @param faid
	 * @return
	 */
	@Override
	public boolean dealStudy(Map<String, Map<String, String>> studyResult, HzxxDto hzxxDto) {
		// TODO Auto-generated method stub
		//根据字段数循环
		List<ZdybtwzDto> zdybtwzDtos = new ArrayList<ZdybtwzDto>();
		for(String key:studyResult.keySet()){
			Map<String, String> keyresult = studyResult.get(key);
			ZdybtwzDto zdybtwzDto = new ZdybtwzDto();
			zdybtwzDto.setFaid(hzxxDto.getFaid());
			zdybtwzDto.setZdmc(key);
			zdybtwzDto.setZdxx(keyresult.get("words"));
			zdybtwzDto.setBhbj(keyresult.get("wordflg"));
			zdybtwzDto.setSbj(keyresult.get("topword"));
			zdybtwzDto.setSjg(keyresult.get("toppoint"));
			zdybtwzDto.setZbj(keyresult.get("leftword"));
			zdybtwzDto.setZjg(keyresult.get("leftpoint"));
			zdybtwzDto.setXbj(keyresult.get("bottomword"));
			zdybtwzDto.setXjg(keyresult.get("bottompoint"));
			zdybtwzDto.setYbj(keyresult.get("rightword"));
			zdybtwzDto.setYjg(keyresult.get("rightpoint"));
			zdybtwzDto.setYwbj(keyresult.get("sameflg"));
			zdybtwzDto.setLrry(hzxxDto.getWxid());
			zdybtwzDto.setZdyid(StringUtil.generateUUID());
			zdybtwzDtos.add(zdybtwzDto);
		}
		//删除
		dao.deleteByFaid(hzxxDto.getFaid());
		//新增
		int result = dao.insertByZdybtwzDtos(zdybtwzDtos);
		if(result == 0)
			return false;
		return true;
	}

	/**
	 * 根据方案id查询当前已应用方案的自定义位置信息
	 * @param zdybtwzDto
	 * @return
	 */
	public List<ZdybtwzDto> getZdybtwzByFaid(ZdybtwzDto zdybtwzDto){
		return dao.getZdybtwzByFaid(zdybtwzDto);
	}
}
