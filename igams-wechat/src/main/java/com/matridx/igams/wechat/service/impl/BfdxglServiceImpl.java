package com.matridx.igams.wechat.service.impl;


import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.wechat.dao.entities.*;
import com.matridx.igams.wechat.dao.post.IBfdxglDao;
import com.matridx.igams.wechat.service.svcinterface.IBfdxglService;
import com.matridx.igams.wechat.service.svcinterface.IBfdxlxrglService;
import com.matridx.igams.wechat.service.svcinterface.IBfglService;
import com.matridx.igams.wechat.service.svcinterface.IKhzyfpService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BfdxglServiceImpl extends BaseBasicServiceImpl<BfdxglDto, BfdxglModel, IBfdxglDao> implements IBfdxglService{

	@Autowired
	private IBfdxlxrglService bfdxlxrglService;
	@Autowired
	private IBfglService bfglService;
	@Autowired
	private IKhzyfpService khzyfpService;
	private Logger log = LoggerFactory.getLogger(BfdxglServiceImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addSaveVisitingObject(BfdxglDto bfdxglDto) {
		bfdxglDto.setDwid(StringUtil.generateUUID());
		List<BfdxlxrglDto> list=(List<BfdxlxrglDto>) JSON.parseArray(bfdxglDto.getLxr_json(), BfdxlxrglDto.class);
		if(!CollectionUtils.isEmpty(list)){
			for(BfdxlxrglDto dto:list){
				dto.setLxrid(StringUtil.generateUUID());
				dto.setDwid(bfdxglDto.getDwid());
				dto.setLrry(bfdxglDto.getLrry());
			}
			boolean inserted = bfdxlxrglService.insertList(list);
			if(!inserted){
				return false;
			}
		}
		KhzyfpDto khzyfpDto=new KhzyfpDto();
		khzyfpDto.setYhid(bfdxglDto.getLrry());
		khzyfpDto.setDwid(bfdxglDto.getDwid());
		khzyfpDto.setZyfpid(StringUtil.generateUUID());
		khzyfpDto.setLrry(bfdxglDto.getLrry());
		boolean insert = khzyfpService.insert(khzyfpDto);
		if(!insert){
			return false;
		}
		bfdxglDto.setFprs("1");
		int inserted = dao.insert(bfdxglDto);
		if(inserted==0){
			return false;
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean modSaveVisitingObject(BfdxglDto bfdxglDto) {
		int updated = dao.update(bfdxglDto);
		if(updated==0){
			return false;
		}
		BfdxlxrglDto bfdxlxrglDto=new BfdxlxrglDto();
		bfdxlxrglDto.setScry(bfdxglDto.getXgry());
		bfdxlxrglDto.setDwid(bfdxglDto.getDwid());
		bfdxlxrglService.delete(bfdxlxrglDto);
		List<BfdxlxrglDto> list=(List<BfdxlxrglDto>) JSON.parseArray(bfdxglDto.getLxr_json(), BfdxlxrglDto.class);
		if(list!=null&&!list.isEmpty()){
			List<BfdxlxrglDto> insertList=new ArrayList<>();
			List<BfdxlxrglDto> updateList=new ArrayList<>();
			for(BfdxlxrglDto dto:list){
				if(StringUtil.isNotBlank(dto.getLxrid())){
					dto.setXgry(bfdxglDto.getXgry());
					updateList.add(dto);
				}else{
					dto.setLxrid(StringUtil.generateUUID());
					dto.setDwid(bfdxglDto.getDwid());
					dto.setLrry(bfdxglDto.getXgry());
					insertList.add(dto);
				}
			}
			if(!insertList.isEmpty()){
				boolean inserted = bfdxlxrglService.insertList(insertList);
				if(!inserted){
					return false;
				}
			}
			if(!updateList.isEmpty()){
				boolean updated1 = bfdxlxrglService.updateList(updateList);
				if(!updated1){
					return false;
				}
			}
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delVisitingObject(BfdxglDto bfdxglDto) {
		int deleted = dao.delete(bfdxglDto);
		if(deleted==0){
			return false;
		}
		BfdxlxrglDto bfdxlxrglDto=new BfdxlxrglDto();
		bfdxlxrglDto.setIds(bfdxglDto.getIds());
		bfdxlxrglDto.setScry(bfdxglDto.getScry());
		boolean delete = bfdxlxrglService.delete(bfdxlxrglDto);
		if(!delete){
			return false;
		}
		KhzyfpDto khzyfpDto=new KhzyfpDto();
		khzyfpDto.setScry(bfdxglDto.getScry());
		khzyfpDto.setIds(bfdxglDto.getIds());
		khzyfpDto.setFpbj("KH");
		khzyfpService.delete(khzyfpDto);
		return true;
	}

	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String,Object> params, BfdxglDto bfdxglDto){
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
		for(DcszDto dcszDto:choseList){
			if(dcszDto == null || dcszDto.getDczd() == null)
				continue;

			sqlParam.append(",");
			if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		String sqlcs=sqlParam.toString();
		bfdxglDto.setSqlParam(sqlcs);
	}

	@Override
	public List<BfdxglDto> getListForSelectExp(Map<String, Object> params) {
		BfdxglDto bfdxglDto = (BfdxglDto) params.get("entryData");
		queryJoinFlagExport(params,bfdxglDto);
		return dao.getListForSelectExp(bfdxglDto);
	}

	@Override
	public int getCountForSearchExp(BfdxglDto bfdxglDto, Map<String, Object> params) {
		return dao.getCountForSearchExp(bfdxglDto);
	}

	@Override
	public List<BfdxglDto> getListForSearchExp(Map<String, Object> params) {
		BfdxglDto bfdxglDto = (BfdxglDto) params.get("entryData");
		queryJoinFlagExport(params, bfdxglDto);
		return dao.getListForSearchExp(bfdxglDto);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean mergeSaveVisitingObject(BfdxglDto bfdxglDto) {
		//igams_bfdxgl表,主体客户不做操作，被合并客户的scbj更新为1，同时更新被合并时间，合并主体ID(主体客户的主键ID)
		boolean merged = dao.merge(bfdxglDto);
		if(!merged){
			return false;
		}
		//igams_bfdxlxrgl表，直接根据前端传过来的json进行更新，主要需要更新scbj以及dwid
		List<BfdxlxrglDto> bfdxlxrglDtos=(List<BfdxlxrglDto>) JSON.parseArray(bfdxglDto.getLxr_json(), BfdxlxrglDto.class);
		if(bfdxlxrglDtos!=null&&!bfdxlxrglDtos.isEmpty()){
			for(BfdxlxrglDto bfdxlxrglDto : bfdxlxrglDtos){//将未删除的联系人全部转移至主体客户下
				if(!"1".equals(bfdxlxrglDto.getScbj()))
					bfdxlxrglDto.setDwid(bfdxglDto.getHbztid());
			}
			boolean merged_lxr = bfdxlxrglService.mergeList(bfdxlxrglDtos);
			if(!merged_lxr){
				return false;
			}
			List<BfdxlxrglDto>bfdxlxrglDtos_sc=bfdxlxrglDtos.stream().filter(item->item.getScbj().equals("1")).collect(Collectors.toList());;
			if(bfdxlxrglDtos_sc!=null&&!bfdxlxrglDtos_sc.isEmpty()){
				bfglService.mergeLxrid(bfdxlxrglDtos_sc);
			}
		}

		//igams_bfgl表,根据ids(被合并的客户ID)查找拜访记录信息，将查出来的记录的dwid都更新为主体客户的dwid
		BfglDto bfglDto=new BfglDto();
		bfglDto.setIds(bfdxglDto.getIds());
		bfglDto.setDwid(bfdxglDto.getHbztid());
		bfglService.merge(bfglDto);
		//igams_khzyfp表,直接根据前端传过来的json进行更新，主要更新scbj和khid
		List<KhzyfpDto> khzyfpDtos=(List<KhzyfpDto>) JSON.parseArray(bfdxglDto.getZyfp_json(), KhzyfpDto.class);
		if(khzyfpDtos!=null&&!khzyfpDtos.isEmpty()){
			boolean merged_zyfp = khzyfpService.mergeList(khzyfpDtos);
			if(!merged_zyfp){
				return false;
			}
		}
		return true;
	}

	@Override
	public String generateCode(BfdxglDto bfdxglDto) {
		/*----------获取流水号规则-------开始----------*/
		//当前规则为年月日+“-”+五位数流水号！范例：20220804-12345
		//客户编码替换规则{key:length:isConditionOfSerial}=>{替换关键词:长度:是否为计算流水号的条件}
		String dxbmgz = "{dwflcskz1:10:true}{-:1:true}{lsh:5:false}";
		log.error("客户编码规则："+ dxbmgz);
		/*----------客户编码规则处理-----开始----------*/
		//1、根据规则获取客户编码长度：dxbmength
		int dxbmLength = 0;//客户编码长度（累加）
		//2、流水号生成的查询条件（里面包含conditionList、startindex、seriallength、deafultserial）：serialMap
		Map<String, Object> serialMap = new HashMap<>();//流水号生成查询条件
		//3、客户编码规则中isConditionOfSerial为true的条件：conditionList
		List<Map<String,Object>> conditionList = new ArrayList<>();
		//4、客户编码字符串List
		List<Map<String,String>> dxbmStrList = new ArrayList<>();
		//5、流水号List
		List<Map<String,String>> serialStrList = new ArrayList<>();
		//生成后结果Map
		Map<String, String> resultMap = new HashMap<>();
		//6、循环替换dxbmgz中个关键词(除有关流水号的内容不替换)
		while (dxbmgz.contains("{") && dxbmgz.contains("}")) {
			Map<String,String> dxbmMap = new HashMap<>();
			int length = 0;//默认关键词长度为0
			boolean isConditionOfSerial = false;//默认 是否加入流水号生成的判断条件 为false
			int startIndex = dxbmgz.indexOf("{");//获取开始下标
			int endIndex = dxbmgz.indexOf("}");//获取结束下标
			if (startIndex != 0){
				// 情况1：替换符前方有字符串；若“{”在不在首位
				// xxxx-{jcdwdm:2:true}
				// 截取“xxxx-”存入dxbmStrList中
				String valueStr = dxbmgz.substring(0,startIndex);
				endIndex = startIndex-1;
				dxbmMap.put("key","string");//设置关键词为string
				dxbmMap.put("value",valueStr);//设置对应值
				dxbmMap.put("index", String.valueOf(dxbmLength));//设置位置（前面所有字符的长度）
				length = valueStr.length();
				dxbmMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
			}else {
				// 情况1：替换符前方无字符串；若“{”在首位
				// {year:4:true}
				//截取{}内的内容
				String key = dxbmgz.substring(startIndex + 1, endIndex);//year:4:true
				//若截取内容内有“:”则分割；
				//若截取内容内无“:”，则默认key为截取内容，长度为其map中长度，默认是否加入流水号生成的判断条件为false
				if (key.contains(":")) {
					String[] split = key.split(":");//例：[year:4:true]
					key = split[0];//第一位为关键词key，例：year
					if (StringUtil.isNotBlank(split[1])){
						try {
							length = Integer.parseInt(split[1]);//第二位为对应关键词的长度，例：2
						} catch (NumberFormatException e) {
							log.error("客户编码规则警告：key:"+key+"的length:"+split[1]+"不是数字");
						}
					}
					//若有第三位，则第三位为是否加入流水号生成的判断条件，例：true
					//若无第三位，默认为false
					if (split.length >= 3){
						isConditionOfSerial = "true".equalsIgnoreCase(split[2]);
					}
				}
				dxbmMap.put("key",key);//设置关键词
				//若为流水号，不做处理，
				if ("lsh".equals(key)){
					dxbmMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
					dxbmMap.put("value","");//设置值为空（后面处理）
					dxbmMap.put("index", String.valueOf(dxbmLength));//设置位置（前面所有字符的长度）
					serialStrList.add(dxbmMap);//存入流水号List中
				} else {
					//客户编码特殊处理
					dealDxbmKeyMap(key, bfdxglDto, resultMap);
					String valueStr = resultMap.get(key);//从infoMap中获取对应值，例：year对应为2023
					if (valueStr.length() > length && length != 0){
						valueStr = valueStr.substring(valueStr.length()-length);//将值截取至对应长度。例：截取01的后2位（01为对应值，2为对应值长度）（默认从末尾开始截取长度，若需要从前面开始截取，则需要增加特殊key,并在dealdxbmKeyMap方法中处理）
					}
					length = valueStr.length();//获取对应值最终长度（针对没设长度的时候）
					dxbmMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
					dxbmMap.put("value",valueStr);//设置值
					dxbmMap.put("index", String.valueOf(dxbmLength));//设置位置（前面所有字符的长度）
				}
				if (isConditionOfSerial){
					Map<String,Object> map = new HashMap<>();
					map.put("conditionIndex",dxbmLength+1);
					map.put("conditionLength",length);
					map.put("conditionKey",dxbmMap.get("value"));
					conditionList.add(map);
				}
			}
			dxbmStrList.add(dxbmMap);//将截取替换内容添加到list
			dxbmgz = dxbmgz.substring(endIndex+1);//截取剩余分规则
			dxbmLength += length;//客户编码长度累加
		}
		//7、若客户编码规则最后还有字符串，则进行处理
		Map<String,String> dxbmMap = new HashMap<>();
		dxbmMap.put("key","string");//设置关键词为string
		dxbmMap.put("value",dxbmgz);//设置对应值
		dxbmMap.put("index", String.valueOf(dxbmLength));//设置位置（前面所有字符的长度）
		int length = dxbmgz.length();
		dxbmMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
		dxbmStrList.add(dxbmMap);//将截取替换内容添加到list
		dxbmLength += length;//客户编码长度累加
		//8、遍历流水号List,循环生成对应的流水号
		for (Map<String, String> dxbmInfo : serialStrList) {
			serialMap.put("startindex", Integer.parseInt(dxbmInfo.get("index"))+1);//流水号开始位置
			serialMap.put("seriallength", Integer.parseInt(dxbmInfo.get("length")));//流水号长度
			serialMap.put("dxbmLength", dxbmLength);//客户编码长度
			serialMap.put("conditionList",conditionList);//条件List
			//生成获取流水号
			String customSerial = dao.getCodeSerial(serialMap);
			dxbmInfo.put("value",customSerial);
		}
		//拼接list获取完整客户编码
		StringBuilder dxbm = new StringBuilder();
		for (Map<String, String> dxbmStr : dxbmStrList) {
			dxbm.append(dxbmStr.get("value"));
		}
		return dxbm.toString();
	}

	public void dealDxbmKeyMap(String key, BfdxglDto bfdxglDto, Map<String,String> resultMap) {
		switch (key){
			case "-": resultMap.put("-", "-");break;
			case "dwflcskz1": resultMap.put("dwflcskz1",bfdxglDto.getDwflcskz1());break;
		}
	}

	/**
	 * 根据dwmc获取拜访对象
	 * @param bfdxglDto
	 * @return
	 */
	public List<BfdxglDto> getDtoByDwmc(BfdxglDto bfdxglDto){
		return dao.getDtoByDwmc(bfdxglDto);
	}
}
