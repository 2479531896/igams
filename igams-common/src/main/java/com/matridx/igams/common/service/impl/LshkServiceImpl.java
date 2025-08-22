package com.matridx.igams.common.service.impl;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.entities.LshgzModel;
import com.matridx.igams.common.dao.entities.LshkDto;
import com.matridx.igams.common.dao.entities.LshkModel;
import com.matridx.igams.common.dao.post.ILshgzDao;
import com.matridx.igams.common.dao.post.ILshkDao;
import com.matridx.igams.common.enums.SerialNumTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.ILshkService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.reflect.ReflectionUtils;

@Service
public class LshkServiceImpl extends BaseBasicServiceImpl<LshkDto, LshkModel, ILshkDao> implements ILshkService{
	@Autowired
	ILshgzDao lshgzDao;
	
	/**
	 * 生成流水号编号
	 * @param type	编号类型
	 * @param lshkDto 编号相关参数
	 */
	public String doMakeSerNum(SerialNumTypeEnum type, LshkDto lshkDto) throws BusinessException {
		if(type==null) {
			throw new BusinessException("W11004");
		}
		if(lshkDto == null) lshkDto = new LshkDto();
		LshgzModel lshgzModel = new LshgzModel();
		lshgzModel.setLshlx(type.getCode());
		lshgzModel = lshgzDao.getModel(lshgzModel);
		String config = lshgzModel.getLshgz().trim();
		if(StringUtil.isBlank(config)) {
			throw new BusinessException("ICOMM_LSH00001");
		}
		Pattern pattern;
		Matcher matcher;
		//分离出前缀配置和其余的配置
		pattern = Pattern.compile("^(.*?)(\\[.*?\\])(.*?)(\\[.*?\\])$");
		matcher = pattern.matcher(config);
		String lshPart1;//其余配置1
		String lshPart2;//其余配置2
		String lshQzPart;//流水号前缀配置
		String lshQzPart2;//流水号前缀配置2
		if(matcher.find()){
			lshPart1 = matcher.group(1);
			lshQzPart = matcher.group(2);
			lshPart2 = matcher.group(3);
			lshQzPart2 = matcher.group(4);
		}else{
			throw new BusinessException("ICOMM_LSH00001");
		}
		//清除前缀、最大流水号
		lshkDto.setLshqz(null);
		lshkDto.setZdlsh(null);
		//设置类型
		lshkDto.setLshlx(type.getCode());
		//获取时间
		Date date = new Date();
		if(StringUtil.isNotBlank(lshkDto.getYbbhrq())){
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        try {
	        	date = sdf.parse(lshkDto.getYbbhrq());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		//解析前缀，并去掉前后方括号
		String lshQz = parseConfig(lshQzPart, lshkDto, date).replaceAll("^\\[|\\]$", "");
		String lshQz2 = parseConfig(lshQzPart2, lshkDto, date).replaceAll("^\\[|\\]$", "");
		//设置前缀：通过前缀获取流水号
		lshkDto.setLshqz(lshQz+lshQz2);
		//解析其余部分并拼接
		return parseConfig(lshPart1, lshkDto, date)+lshQz+parseConfig(lshPart2, lshkDto, date)+lshQz2;
	}
	
	/**
	 * 解析流水号的设置规则
	 */
	private String parseConfig(String config, LshkDto lshkDto, Date date) throws BusinessException {
		Pattern pattern;
		Matcher matcher;
		List<String> params = new ArrayList<>();
		pattern = Pattern.compile("\\{[^}]+\\}");
		matcher = pattern.matcher(config);
		//解析每个{}配置项
		while(matcher.find()) {
			String group = matcher.group();
			//去掉配置下的首位大括号
			String _config = group.replaceAll("^\\{|\\}$", "").trim();
			String[] _configArr = _config.split(":");
			String attr = _configArr[0];
			String val = "";
			LshkModel lshkModel;
			
			//根据第一个配置属性获取属性值
			//特殊属性处理
			if("year".equalsIgnoreCase(attr)){
				val = String.valueOf(DateUtils.getYear(date));
			}else if("yearmonth".equalsIgnoreCase(attr)){
				val = DateUtils.getYear(date) +String.valueOf(DateUtils.getMonth(date));
			}else if("month".equalsIgnoreCase(attr)){
				val = String.valueOf(DateUtils.getMonth(date));
			}else if("date".equalsIgnoreCase(attr)){
				val = String.valueOf(DateUtils.getDate(date));
			}else if("yearmonthdate".equalsIgnoreCase(attr)){
				val = DateUtils.getCustomFomratCurrentDate(date, "YYYYMMdd");
			}else if("dateformat".equalsIgnoreCase(attr)){
				val = DateUtils.getCustomFomratCurrentDate(date, "YYYYMMddHHmmss");
			}else if("lsh".equalsIgnoreCase(attr)){
				if(StringUtil.isBlank(lshkDto.getZdlsh())) {
					//流水号前缀尚未获取到，则生成规则有问题
					if(StringUtil.isBlank(lshkDto.getLshqz())){
						throw new BusinessException("ICOMM_LSH00001");
					}
					//查询数据库最大流水号
					lshkModel = dao.getModel(lshkDto);
					if(lshkModel == null || StringUtil.isBlank(lshkModel.getZdlsh())){
						val = String.format("%0"+_configArr[1].trim()+"d", Integer.parseInt(GlobalString.EXTEND_ONE));
					}else{
						val = StringUtil.serialPlus(lshkModel.getZdlsh(),Integer.parseInt(_configArr[1].trim()));
					}
				}else{
					val =String.format("%0"+_configArr[1].trim()+"d", Integer.parseInt(lshkDto.getZdlsh()));
				}
			}else{
				//默认获取lshkDto中属性
				Object valObj = ReflectionUtils.getFieldValueIgnoreCase(lshkDto, attr);
				if(valObj!=null){
					val = valObj.toString();
				}
			}
			
			//获取第二个配置属性进行格式化操作
			if(_configArr.length>=2){
				if("dateformat".equalsIgnoreCase(attr)){
					val = DateUtils.getCustomFomratCurrentDate(date, _configArr[1].trim());
				}
			}
			
			//生成流水号后进行保存操作
			if("lsh".equalsIgnoreCase(attr)&&StringUtil.isBlank(lshkDto.getZdlsh())){
				lshkDto.setZdlsh(val);
				lshkModel = dao.getModel(lshkDto);
				if(lshkModel == null || StringUtil.isBlank(lshkModel.getZdlsh())){
					String lshid = StringUtil.generateUUID();
					lshkDto.setLshid(lshid);
					//插入流水号库记录
					int result = dao.insert(lshkDto);
					if(result != 1){
						throw new BusinessException("I99002");
					}
				}else{
					lshkDto.setLshid(lshkModel.getLshid());
					//更新流水号库记录
					int result = dao.update(lshkDto);
					if(result != 1){
						throw new BusinessException("I99004");
					}
				}
			}
			params.add(val);
			//替换为可以让MessageFormat处理的config
			config = Pattern.compile(group,Pattern.LITERAL).matcher(config).replaceFirst("{"+(params.size()-1)+"}");
		}
		config = MessageFormat.format(config, params.toArray()); 
		return config;
	}
	
	/**
	 * 根据现在的编码，去除前缀查找流水号库里的编码，如果现在的编码更新，则更新成现在的编码
	 */
	public boolean updateSerNum(String lshlx,String lshqz,String nowNum){
		
		String num = nowNum.replace(lshqz, "");
		
		LshkDto lshkDto = new LshkDto();
		lshkDto.setLshlx(lshlx);
		lshkDto.setLshqz(lshqz);
		
		LshkModel t_LshkDto= dao.getModel(lshkDto);
		
		if(t_LshkDto == null){
			LshkModel t_LshkModel = new LshkModel();
			String lshid = StringUtil.generateUUID();
			t_LshkModel.setLshid(lshid);
			t_LshkModel.setZdlsh(num);
			t_LshkModel.setLshlx(lshlx);
			t_LshkModel.setLshqz(lshqz);
			dao.insert(t_LshkModel);
		}else{
			String zdlsh = t_LshkDto.getZdlsh();
			
			//判断生成得物料编码的流水号是否小于流水表里的流水号，如果小于，不更新流水表流水号，如果大于，更新。
			if("MATER_CODE".equals(t_LshkDto.getLshlx())) {
				if(StringUtil.isNotBlank(nowNum) && Integer.parseInt(num)< Integer.parseInt(zdlsh)) {
					return true;
				}
			}
			
			if(isUpdateNum(zdlsh,num)){
				t_LshkDto.setZdlsh(num);
				
				dao.update(t_LshkDto);
			}
		}
		
		return true;
	}
	
	private boolean isUpdateNum(String preNum,String afterNum){
		int diffSize = preNum.compareTo(afterNum);
		return diffSize < 0;
	}
}
