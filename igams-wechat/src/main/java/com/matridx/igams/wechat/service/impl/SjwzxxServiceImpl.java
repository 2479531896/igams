package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.WechatReferencesModel;
import com.matridx.igams.wechat.dao.post.ISjwzxxDao;
import com.matridx.igams.wechat.service.svcinterface.ISjnyxService;
import com.matridx.igams.wechat.service.svcinterface.ISjwzxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SjwzxxServiceImpl extends BaseBasicServiceImpl<SjwzxxDto, SjwzxxModel, ISjwzxxDao> implements ISjwzxxService{
	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath;
	@Autowired
	ISjnyxService sjnyxService;

	private Logger log = LoggerFactory.getLogger(SjwzxxServiceImpl.class);
	
	/**
	 * 批量新增送检物种信息
	 * @param sjwzxxDtos
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertBysjwzxxDtos(List<SjwzxxDto> sjwzxxDtos) {
		// TODO Auto-generated method stub
		return dao.insertBysjwzxxDtos(sjwzxxDtos);
	}

	/**
	 * 批量删除送检物种信息
	 * @param sjwzxxDto
	 * @return
	 */
	@Override
	public boolean deleteBysjwzxxDto(SjwzxxDto sjwzxxDto) {
		// TODO Auto-generated method stub
		int result = dao.deleteBysjwzxxDto(sjwzxxDto);
        return result != 0;
    }

	/**
	 * 报告模板高关低关数据 
	 * @param sjwzxxDto
	 * @return
	 */
	@Override
	public List<SjwzxxDto> selectAttention(SjwzxxDto sjwzxxDto) {
		// TODO Auto-generated method stub
		return dao.selectAttention(sjwzxxDto);
	}

	/**
	 * 2023-11-14 注释原因：未使用
	 * 通过sjid和检测类型获取送检物种信息
	 * @param sjwzxxDto
	 * @return
	 */
/*	@Override
	public List<SjwzxxDto> selectWzxxForWord(SjwzxxDto sjwzxxDto) {
		// TODO Auto-generated method stub
		List<SjwzxxDto> list=dao.selectWzxxForWord(sjwzxxDto);
		if(list!=null && list.size()>0) {
			for (SjwzxxDto sjwzxxDto_t:list) {
				if("Fungi".equals(sjwzxxDto_t.getWzlx())) {
					sjwzxxDto_t.setWzlx("真菌");
				}else if("Viruses".equals(sjwzxxDto_t.getWzlx())) {
					sjwzxxDto_t.setWzlx("病毒");
				}else if("Parasite".equals(sjwzxxDto_t.getWzlx())) {
					sjwzxxDto_t.setWzlx("寄生虫");
				}
				if(sjwzxxDto_t.getJzryzs()==null&&sjwzxxDto_t.getJzryzsbfb()==null) {
					sjwzxxDto_t.setIndex("--");
				}else {
					sjwzxxDto_t.setIndex((StringUtil.isBlank(sjwzxxDto_t.getJzryzs())?"--":sjwzxxDto_t.getJzryzs())+",高于"+(StringUtil.isBlank(sjwzxxDto_t.getJzryzsbfb())?"--":sjwzxxDto_t.getJzryzsbfb())+"%的同类标本");
				}
				Map<String,String> map=dao.getCsdmBySjid(sjwzxxDto_t.getSjid());
				if(map!=null) {
					String projectPath = StringUtil.isNotBlank(map.get("jcxmcs4"))?map.get("jcxmcs4") :"Q";
					String path=releaseFilePath+BusTypeEnum.IMP_TEMEPLATE_IMAGES.getCode()+"/"+projectPath + "/" + map.get("jcxmcs1").toString()+"/"+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzid():sjwzxxDto_t.getSid()) +"_"+map.get("yblxdm").toString()+".jpg";
					String cursor=releaseFilePath+BusTypeEnum.IMP_TEMEPLATE_IMAGES.getCode()+"/cursor/cursor.png";
					sjwzxxDto_t.setPercentile_path(path);
					sjwzxxDto_t.setCursor_path(cursor);
				}
			}
		}
		return list;
	}*/
	
	
	/**
	 * 2023-11-14 注释原因：未使用
	 * 通过sjid和检测类型获取送检物种信息
	 * @param sjwzxxDto
	 * @return
	 */
/*	@Override
	public List<SjwzxxDto> selectWzxx(SjwzxxDto sjwzxxDto) {
		// TODO Auto-generated method stub
		return dao.selectWzxxForWord(sjwzxxDto);
	}*/

	/**
	 * 根据送检ID查询耐药信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjwzxxDto> getNyxByForWzxx(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getNyxByForWzxx(sjxxDto);
	}

	/**
	 * 查询检测结果
	 * @param sjid
	 * @return
	 */
	@Override
	public Map<String,Object> getPathogensMap(String sjid,String jclx,List<WechatReferencesModel> papers) {
		// TODO Auto-generated method stub
		Map<String,Object> parMap= new HashMap<>();
		parMap.put("sjid",sjid);
		parMap.put("jclx", jclx);
		String pathogens="";
		String pathogens_hospital="";
		String pathogens_background="";
		String pathogens_commensal="";
		List<SjwzxxDto> list = dao.getJcjgForWordNew(parMap);
		if(list!=null && list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				if("1".equals(list.get(i).getSfgl())) {
					pathogens += "{br}{color:FF0000}"+list.get(i).getWzzwm()+"，";
					pathogens_hospital+="{br}{color:FF0000}"+list.get(i).getWzzwm();
					if(StringUtil.isNotBlank(list.get(i).getDqs())) {
						pathogens_hospital+="("+list.get(i).getDqs()+")";
					}
					pathogens_hospital+="，";
				}else {
					pathogens += "{br}"+list.get(i).getWzzwm()+"，";
					pathogens_hospital+="{br}"+list.get(i).getWzzwm();
					if(StringUtil.isNotBlank(list.get(i).getDqs())) {
						pathogens_hospital+="("+list.get(i).getDqs()+")";
					}
					pathogens_hospital+="，";
				}
				
			}
		}
		List<SjwzxxDto> list1 = dao.getBackgroundJcjgForWordNew(parMap);
		if(list1!=null && list1.size()>0) {
			for (int i = 0; i < list1.size(); i++) {
				pathogens_background += list1.get(i).getWzzwm()+"，";
			}
		}
		if(StringUtil.isNotBlank(pathogens_background)) {
			pathogens_background=pathogens_background.substring(0,pathogens_background.length()-1);
		}
		List<SjwzxxDto> list2 = dao.getCommensalJcjgForWordNew(parMap);
		if(list2!=null && list2.size()>0) {
			for (int i = 0; i < list2.size(); i++) {
				pathogens_commensal += list2.get(i).getWzzwm()+"，";
			}
		}
		if(StringUtil.isNotBlank(pathogens_commensal)) {
			pathogens_commensal=pathogens_commensal.substring(0,pathogens_commensal.length()-1);
		}
		if(StringUtil.isNotBlank(pathogens)) {
			pathogens=pathogens.substring(0,pathogens.length()-1);
		}
		if(StringUtil.isNotBlank(pathogens_hospital)) {
			pathogens_hospital=pathogens_hospital.substring(0,pathogens_hospital.length()-1);
		}
		
		//参考文献排序根据物种信息进行排序
		List<WechatReferencesModel> ckwxList= new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			boolean isFind = false;
			for (int j = 0; j < papers.size(); j++) {
				if((list.get(i).getWzid()!=null && list.get(i).getWzid().equals(papers.get(j).getSpecies_taxid()))||
						(list.get(i).getWzid()==null &&list.get(i).getSid()!=null && list.get(i).getSid().equals(papers.get(j).getSpecies_taxid()))) {
					ckwxList.add(papers.get(j));
					isFind = true;
					break;
				}
			}
			if(!isFind)
				ckwxList.add(null);
		}
		StringBuffer sb=new StringBuffer();
		for (int i = 1; i < ckwxList.size()+1; i++) {
			if(ckwxList.get(i-1) != null) {
				if(i>1) 
					sb.append("{br}{\\n}");
				else
					sb.append("\n");
				sb.append("["+i+"]"+ckwxList.get(i-1).getAuthor()+"{br}{i}"+ckwxList.get(i-1).getTitle()+"{br}"+ckwxList.get(i-1).getJournal());
			}
		}
		String references=sb.toString();
		Map<String,Object> returnMap= new HashMap<>();
		returnMap.put("pathogens", pathogens);
		returnMap.put("pathogens_hospital", pathogens_hospital);
		returnMap.put("references", references);
		returnMap.put("column", Math.max(list.size(), 0));
		returnMap.put("pathogens_background",pathogens_background);
		returnMap.put("pathogens_commensal",pathogens_commensal);
		return returnMap;
	}
	
	/**
	 * 查询检测结果
	 * @param sjid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getPathogensMapNew(String sjid,String jclx,List<WechatReferencesModel> papers, Map<String,Object> map) {
		List<SjwzxxDto> list = (List<SjwzxxDto>)map.get("pathogenDtolist");

		String pathogens="";
		String pathogens_hospital="";
		String onlypathogens = "";
		String onlypossible = "";

		//pathogens_hospital是指某些特殊模板需要增加读取数的显示
		if(list!=null && list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                String wzzwm = "--".equals(list.get(i).getWzzwm())||StringUtil.isBlank(list.get(i).getWzzwm())?list.get(i).getSzwm():list.get(i).getWzzwm();
				if(StringUtil.isNotBlank(list.get(i).getXgfx()))
					wzzwm=wzzwm+"("+list.get(i).getXgfx()+")";
				wzzwm = "--".equals(wzzwm)?"":wzzwm;
				if("possible".equals(list.get(i).getJglx()) || "pathogen".equals(list.get(i).getJglx())) {
					if("1".equals(list.get(i).getSfgl())) {
						pathogens += "{br}{color:FF0000}"+wzzwm+"，";
						if ("possible".equals(list.get(i).getJglx())){
							onlypossible += "{br}{color:FF0000}"+wzzwm+"，";
						}
						if ("pathogen".equals(list.get(i).getJglx())){
							onlypathogens += "{br}{color:FF0000}"+wzzwm+"，";
						}
						pathogens_hospital+="{br}{color:FF0000}"+wzzwm;
						if(StringUtil.isNotBlank(list.get(i).getDqs())) {
							pathogens_hospital+="("+list.get(i).getDqs()+")";
						}
						pathogens_hospital+="，";
					}else {
						pathogens += "{br}"+wzzwm+"，";
						if ("possible".equals(list.get(i).getJglx())){
							onlypossible += "{br}"+wzzwm+"，";
						}
						if ("pathogen".equals(list.get(i).getJglx())){
							onlypathogens += "{br}"+wzzwm+"，";
						}
						pathogens_hospital+="{br}"+wzzwm;
						if(StringUtil.isNotBlank(list.get(i).getDqs())) {
							pathogens_hospital+="("+list.get(i).getDqs()+")";
						}
						pathogens_hospital+="，";
					}
				}
			}
		}
		if(StringUtil.isNotBlank(pathogens)) {
			pathogens=pathogens.substring(0,pathogens.length()-1);
		}
		if(StringUtil.isNotBlank(onlypossible)) {
			onlypossible=onlypossible.substring(0,onlypossible.length()-1);
		}
		if(StringUtil.isNotBlank(onlypathogens)) {
			onlypathogens=onlypathogens.substring(0,onlypathogens.length()-1);
		}
		if(StringUtil.isNotBlank(pathogens_hospital)) {
			pathogens_hospital=pathogens_hospital.substring(0,pathogens_hospital.length()-1);
		}

		List<SjwzxxDto> commensalList = (List<SjwzxxDto>)map.get("commensalList");
		String onlycommensal = "";
		String commensal_hospital = "";
		if(commensalList!=null && commensalList.size()>0) {
			for (int i = 0; i < commensalList.size(); i++) {
				String wzzwm = "--".equals(commensalList.get(i).getWzzwm())||StringUtil.isBlank(commensalList.get(i).getWzzwm())?commensalList.get(i).getSzwm():commensalList.get(i).getWzzwm();
				if(StringUtil.isNotBlank(commensalList.get(i).getXgfx()))
					wzzwm=wzzwm+"("+commensalList.get(i).getXgfx()+")";
				wzzwm = "--".equals(wzzwm)?"":wzzwm;

				if("1".equals(commensalList.get(i).getSfgl())) {
					onlycommensal += "{br}{color:FF0000}"+wzzwm+"，";
					commensal_hospital+="{br}{color:FF0000}"+wzzwm;
					if(StringUtil.isNotBlank(commensalList.get(i).getDqs())) {
						commensal_hospital+="("+commensalList.get(i).getDqs()+")";
					}
					commensal_hospital+="，";
				}else {
					onlycommensal += "{br}"+wzzwm+"，";
					commensal_hospital+="{br}"+wzzwm;
					if(StringUtil.isNotBlank(commensalList.get(i).getDqs())) {
						commensal_hospital+="("+commensalList.get(i).getDqs()+")";
					}
					commensal_hospital+="，";
				}
			}
		}
		Map<String,Object> returnMap= new HashMap<>();
		returnMap.put("pathogens", pathogens);
		returnMap.put("onlypathogens", onlypathogens);
		returnMap.put("onlypossible", onlypossible);
		returnMap.put("onlycommensal", onlycommensal);
		returnMap.put("pathogens_hospital", pathogens_hospital);
		returnMap.put("commensal_hospital", commensal_hospital);
		returnMap.put("column",list!=null && list.size()>0?list.size():0);
		returnMap.put("pathogens_background",map.get("backgroundmc"));
		returnMap.put("pathogens_backgroundInfo",map.get("backgroundInfomc"));
		returnMap.put("pathogens_commensal",map.get("commensalmc"));
		returnMap.put("pathogens_commensal",map.get("commensalmc"));
		return returnMap;
	}

	/**
	 * 查询检测结果说明
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Map<String,Object> getPathogen_commentToString(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		Map<String, String> map= new HashMap<>();
		map.put("sjid", sjxxDto.getSjid());
		map.put("jglx", null);
		map.put("jclx", sjxxDto.getJclx());
		List<SjwzxxDto> list=dao.getComment(map);
		StringBuffer pathogen_comment_new=new StringBuffer();
		StringBuffer pathogen_comment_widthEnglish=new StringBuffer();
		StringBuffer pathogen_comment_hospital=new StringBuffer();
		if(list!=null && list.size()>0) {
			for (int i = 1; i < list.size()+1; i++) {
				if(StringUtil.isNotBlank(list.get(i-1).getWzid())){
					if(i>1&&i!=list.size()+1) {
						pathogen_comment_new.append("{br}{\\n}");
						pathogen_comment_widthEnglish.append("{br}{\\n}");
					}else{
						pathogen_comment_new.append("\n");
						pathogen_comment_widthEnglish.append("\n");
					}
					pathogen_comment_new.append("{br}{b}"+i+"."+list.get(i-1).getWzzwm()+":{br}"+list.get(i-1).getWzzs()+"["+i+"]。");
					pathogen_comment_widthEnglish.append("{br}{b}"+i+"."+list.get(i-1).getWzzwm()+list.get(i-1).getWzywm()+":{br}"+list.get(i-1).getWzzs()+"["+i+"]。");
				}
			}
			for (int i = 1; i < list.size()+1; i++) {
				if(StringUtil.isNotBlank(list.get(i).getWzid())) {
					if (i > 1 && i != list.size() + 1)
						pathogen_comment_hospital.append("{br}{\\n}");
					else
						pathogen_comment_hospital.append("\n");
					pathogen_comment_hospital.append("{br}{b}" + i + "." + list.get(i - 1).getWzzwm() + ":{br}" + list.get(i - 1).getWzzs() + "。");
				}
			}
		}
		
		Map<String,Object> resultMap= new HashMap<>();
		resultMap.put("pathogen_comment_new", pathogen_comment_new.toString());
		resultMap.put("pathogen_comment_widthEnglish", pathogen_comment_widthEnglish.toString());
		resultMap.put("pathogen_comment_hospital", pathogen_comment_hospital.toString());
		return resultMap;
	}

	/**
	 * 查询检测结果(旧模板)
	 * @param sjid
	 * @return
	 */
	@Override
	public Map<String,String> getJcjgForOldTemplate(String sjid,String jclx) {
		// TODO Auto-generated method stub
		Map<String,String> map= new HashMap<>();
		Map<String,String> parMap= new HashMap<>();
		parMap.put("sjid", sjid);
		parMap.put("jclx",jclx);
		List<SjwzxxDto> list=dao.getJcjgForWord(parMap);
		StringBuffer pathogens=new StringBuffer();
		StringBuffer possible=new StringBuffer();
		if(list!=null && list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				if("pathogen".equals(list.get(i).getJglx())) {
					pathogens.append(list.get(i).getWzzwm()+"，");
				}else if("possible".equals(list.get(i).getJglx())){
					possible.append(list.get(i).getWzzwm()+"，");
				}
			}
		}
		String pathogens_str=pathogens.toString();
		String possible_str=possible.toString();
		if(StringUtil.isNotBlank(pathogens_str)) {
			pathogens_str=pathogens_str.substring(0,pathogens_str.length()-1);
		}
		if(StringUtil.isNotBlank(possible_str)) {
			possible_str=possible_str.substring(0,possible_str.length()-1);
		}
		map.put("pathogens", pathogens_str);
		map.put("possible",  possible_str);
		return map;
	}

	/**
	 * 查询检测结果说明(旧模板)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Map<String, String> getPathogenForOldTemplate(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		Map<String,String> map= new HashMap<>();
		Map<String, String> param1= new HashMap<>();
		param1.put("sjid", sjxxDto.getSjid());
		param1.put("jglx", "pathogen");
		param1.put("jclx", sjxxDto.getJclx());
		int index =1;
		List<SjwzxxDto> list1=dao.getPathogen_comment(param1);
		StringBuffer pathogen_comment=new StringBuffer();
		if(list1!=null && list1.size()>0) {
			for (int i = 1; i < list1.size()+1; i++) {
				if("pathogen".equals(list1.get(i-1).getJglx())) {
					if(i >1 &&i!=list1.size()+1)
						 pathogen_comment.append("{br}{\\n}");
					else
						pathogen_comment.append("\n");
					pathogen_comment.append("{br}1."+i+"{br}{b}"+list1.get(i-1).getWzzwm()+":{br}"+list1.get(i-1).getWzzs()+"["+i+"]。");
					index++;
				}	
			}
		}
		
		Map<String, String> param2= new HashMap<>();
		param2.put("sjid", sjxxDto.getSjid());
		param2.put("jglx", "possible");
		param2.put("jclx", sjxxDto.getJclx());
		List<SjwzxxDto> list2=dao.getPathogen_comment(param2);
		StringBuffer possible_comment=new StringBuffer();
		if(list2!=null && list2.size()>0) {
			for (int i = 1; i < list2.size()+1; i++) {
				if(StringUtil.isNotBlank(list2.get(i-1).getWzid())){
					if("possible".equals(list2.get(i-1).getJglx())){
						if(i >1 &&i!=list2.size()+1)
							possible_comment.append("{br}{\\n}");
						else
							possible_comment.append("\n");
						possible_comment.append("{br}2."+i+"{br}{b}"+list2.get(i-1).getWzzwm()+":{br}"+list2.get(i-1).getWzzs()+"["+index+"]。");
						index++;
					}
				}
			}
		}
		if(StringUtil.isNotBlank(pathogen_comment)) {
			pathogen_comment.append("{br}{\\n}");
		}
		
		String pathogen_comment_old=pathogen_comment.toString()+possible_comment.toString();
		map.put("pathogen_comment_old",pathogen_comment_old);
		return map;
	}

	/**
	 * 查询检测结果说明
	 * @param sjwzxxDto
	 * @return
	 */
	@Override
	public Map<String, String> getComment(SjwzxxDto sjwzxxDto) {
		// TODO Auto-generated method stub
		Map<String,String> map= new HashMap<>();
		Map<String, String> param1= new HashMap<>();
		param1.put("sjid", sjwzxxDto.getSjid());
		param1.put("jclx", sjwzxxDto.getJclx());
		param1.put("jglx", "pathogen");
		List<SjwzxxDto> list1=dao.getPathogen_comment(param1);
		StringBuffer pathogen_comment=new StringBuffer();
		int size = 0;
		if(list1!=null && list1.size()>0) {
			size = list1.size();
			for (int i = 1; i < list1.size()+1; i++) {
				if("pathogen".equals(list1.get(i-1).getJglx())) {
					if(i >1 &&i!=list1.size()+1)
						pathogen_comment.append("\r\n");
					pathogen_comment.append("1."+i+list1.get(i-1).getWzzwm()+":"+list1.get(i-1).getWzzs()+"["+i+"]。");
				}	
			}
		}
		
		Map<String, String> param2= new HashMap<>();
		param2.put("sjid", sjwzxxDto.getSjid());
		param2.put("jclx", sjwzxxDto.getJclx());
		param2.put("jglx", "possible");
		List<SjwzxxDto> list2=dao.getPathogen_comment(param2);
		StringBuffer possible_comment=new StringBuffer();
		if(list2!=null && list2.size()>0) {
			for (int i = 1; i < list2.size()+1; i++) {
				if(StringUtil.isNotBlank(list2.get(i-1).getWzid())){
					if("possible".equals(list2.get(i-1).getJglx())){
						possible_comment.append("2."+i+list2.get(i-1).getWzzwm()+":"+list2.get(i-1).getWzzs()+"["+(i+size)+"]。");
					}
				}
			}
		}
		map.put("pathogen_comment",pathogen_comment.toString());
		map.put("possible_comment",possible_comment.toString());
		return map;
	}

	@Override
	public List<SjwzxxDto> getComment(Map<String, String> map) {
		return dao.getComment(map);
	}

	/**
	 * 查询参考文献（默认）区分高关低关
	 * @param sjid
	 * @param jclx
	 * @return
	 */
	@Override
	public String getReferences_default(String sjid,String jclx,List<WechatReferencesModel> papers) {
		// TODO Auto-generated method stub
		List<WechatReferencesModel> pathogenReference= new ArrayList<>();
		List<WechatReferencesModel> possibleReference= new ArrayList<>();
		StringBuffer pathogenSB=new StringBuffer();
		StringBuffer possibleSB=new StringBuffer();
		Map<String, String> param1= new HashMap<>();
		param1.put("sjid", sjid);
		param1.put("jclx", jclx);
		param1.put("jglx", "pathogen");
		List<SjwzxxDto> pathogenList=dao.getPathogen_comment(param1);
		if(pathogenList!=null&&pathogenList.size()>0) {
			for (int i = 0; i < pathogenList.size(); i++) {
				SjwzxxDto t_SjwzxxDto = pathogenList.get(i);
				boolean isFind = false;
				for (int j = 0; j < papers.size(); j++) {
					if((t_SjwzxxDto.getWzid()!=null && t_SjwzxxDto.getWzid().equals(papers.get(j).getSpecies_taxid()))||
							(t_SjwzxxDto.getWzid()==null && t_SjwzxxDto.getSid()!=null && t_SjwzxxDto.getSid().equals(papers.get(j).getSpecies_taxid()))){
						pathogenReference.add(papers.get(j));
						isFind = true;
						break;
					}
				}
				//如果相应的物种在文献那边没有找到，为了序号正确，则考虑增加一个空行
				if(!isFind) {
					pathogenReference.add(null);
				}
			}
		}
		
		Map<String, String> param2= new HashMap<>();
		param2.put("sjid", sjid);
		param2.put("jclx", jclx);
		param2.put("jglx", "possible");
		List<SjwzxxDto> possibleList=dao.getPathogen_comment(param2);
		if(possibleList!=null&&possibleList.size()>0) {
			for (int i = 0; i < possibleList.size(); i++) {
				SjwzxxDto t_SjwzxxDto = possibleList.get(i);
				boolean isFind = false;
				for (int j = 0; j < papers.size(); j++) {
					if((t_SjwzxxDto.getWzid()!=null && t_SjwzxxDto.getWzid().equals(papers.get(j).getSpecies_taxid()))||
							(t_SjwzxxDto.getWzid()==null && t_SjwzxxDto.getSid()!=null&& t_SjwzxxDto.getSid().equals(papers.get(j).getSpecies_taxid()))){
						possibleReference.add(papers.get(j));
						isFind = true;
						break;
					}
				}
				//如果相应的物种在文献那边没有找到，为了序号正确，则考虑增加一个空行
				if(!isFind) {
					possibleReference.add(null);
				}
			}
		}
		if(pathogenReference!=null && pathogenReference.size()>0) {
			for (int i = 1; i < pathogenReference.size()+1; i++) {
				if(pathogenReference.get(i-1)!=null) {
					if(i>1) 
						pathogenSB.append("{br}{\\n}");
					else
						pathogenSB.append("\n");
					pathogenSB.append("[1."+i+"]"+pathogenReference.get(i-1).getAuthor()+"{br}{i}"+pathogenReference.get(i-1).getTitle()+"{br}"+pathogenReference.get(i-1).getJournal());
				}
			}
		}
		String pathogenStr=pathogenSB.toString();

		if(possibleReference!=null && possibleReference.size()>0) {
			for (int i = 1; i < possibleReference.size()+1; i++) {
				if(i == 1) {
					if(pathogenStr!=null&&pathogenStr!="") {
						possibleSB.append(" ");
					}else {
						possibleSB.append("{br}{\\n}");	
					}
				}else {
					possibleSB.append("{br}{\\n}");
				}
				
				if(possibleReference.get(i-1)!=null)
					possibleSB.append("[2."+i+"]"+possibleReference.get(i-1).getAuthor()+"{br}{i}"+possibleReference.get(i-1).getTitle()+"{br}"+possibleReference.get(i-1).getJournal());
			}
		}
		String possibleStr=possibleSB.toString();
		return pathogenStr+possibleStr;
	}
	
	/**
	 * 查询参考文献（默认）区分高关低关
	 * @param sjid
	 * @param jclx
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getReferences_defaultNew(String sjid,String jclx,List<WechatReferencesModel> papers, Map<String,Object> resultMap) {
		// TODO Auto-generated method stub
				List<WechatReferencesModel> pathogenReference= new ArrayList<>();
				List<WechatReferencesModel> possibleReference= new ArrayList<>();
				StringBuffer pathogenSB=new StringBuffer();
				StringBuffer possibleSB=new StringBuffer();
				List<SjwzxxDto> pathogenList=(List<SjwzxxDto>)resultMap.get("pathogenList");
				if(pathogenList!=null&&pathogenList.size()>0) {
					for (int i = 0; i < pathogenList.size(); i++) {
						SjwzxxDto t_SjwzxxDto = pathogenList.get(i);
						boolean isFind = false;
						for (int j = 0; j < papers.size(); j++) {
							if((t_SjwzxxDto.getWzid()!=null && t_SjwzxxDto.getWzid().equals(papers.get(j).getSpecies_taxid()))||
									(t_SjwzxxDto.getWzid()==null && t_SjwzxxDto.getSid()!=null && t_SjwzxxDto.getSid().equals(papers.get(j).getSpecies_taxid()))){
								pathogenReference.add(papers.get(j));
								isFind = true;
								break;
							}
						}
						//如果相应的物种在文献那边没有找到，为了序号正确，则考虑增加一个空行
						if(!isFind) {
							pathogenReference.add(null);
						}
					}
				}
				
				List<SjwzxxDto> possibleList=(List<SjwzxxDto>)resultMap.get("possible");
				if(possibleList!=null&&possibleList.size()>0) {
					for (int i = 0; i < possibleList.size(); i++) {
						SjwzxxDto t_SjwzxxDto = possibleList.get(i);
						boolean isFind = false;
						for (int j = 0; j < papers.size(); j++) {
							if((t_SjwzxxDto.getWzid()!=null && t_SjwzxxDto.getWzid().equals(papers.get(j).getSpecies_taxid()))||
									(t_SjwzxxDto.getWzid()==null && t_SjwzxxDto.getSid()!=null&& t_SjwzxxDto.getSid().equals(papers.get(j).getSpecies_taxid()))){
								possibleReference.add(papers.get(j));
								isFind = true;
								break;
							}
						}
						//如果相应的物种在文献那边没有找到，为了序号正确，则考虑增加一个空行
						if(!isFind) {
							possibleReference.add(null);
						}
					}
				}
				if(pathogenReference!=null && pathogenReference.size()>0) {
					for (int i = 1; i < pathogenReference.size()+1; i++) {
						if(pathogenReference.get(i-1)!=null) {
							if(i>1) 
								pathogenSB.append("{br}{\\n}");
							else
								pathogenSB.append("\n");
							pathogenSB.append("[1."+i+"]"+pathogenReference.get(i-1).getAuthor()+"{br}{i}"+pathogenReference.get(i-1).getTitle()+"{br}"+pathogenReference.get(i-1).getJournal());
						}
					}
				}
				String pathogenStr=pathogenSB.toString();

				if(possibleReference!=null && possibleReference.size()>0) {
					for (int i = 1; i < possibleReference.size()+1; i++) {
						if(i == 1) {
							if(pathogenStr!=null&&pathogenStr!="") {
								possibleSB.append(" ");
							}else {
								possibleSB.append("{br}{\\n}");	
							}
						}else {
							possibleSB.append("{br}{\\n}");
						}
						
						if(possibleReference.get(i-1)!=null)
							possibleSB.append("[2."+i+"]"+possibleReference.get(i-1).getAuthor()+"{br}{i}"+possibleReference.get(i-1).getTitle()+"{br}"+possibleReference.get(i-1).getJournal());
					}
				}
				String possibleStr=possibleSB.toString();
				return pathogenStr+possibleStr;
	}

	/**
	 * 2023-11-14 注释原因：未使用
	 * 查询检测项目代码和标本类型代码
	 * @param sjid
	 * @return
	 */
/*	@Override
	public Map<String, String> getCsdmBySjid(String sjid) {
		return dao.getCsdmBySjid(sjid);
	}*/

	/**
	 * 根据外部编码获取检测结果信息
	 * @param ids
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getJcjgInfoByCodes(List<String> ids){
		List<Map<String,String>> infoList = dao.getJcjgInfoByCodes(ids);
		List<Map<String,Object>> resutList = new ArrayList<>();
		for (String id : ids) {
			//遍历样本信息
			Map<String, Object> resutMap = new HashMap<>();//样本map
			resutMap.put("code", id);//样本编号
			List<Map<String,String>> jcxmList = dao.getJcjgLxByCode(id);
			if (jcxmList != null && jcxmList.size()>0){
				List<Map<String,Object>> jcxmInfoList = new ArrayList<>();
				Map<String, Object> jcxmMap = new HashMap<>();//检测项目map
				for (Map<String,String>  jcxm: jcxmList) {
					if (jcxm != null && StringUtil.isNotBlank(jcxm.get("jcxm"))){
						jcxmMap.put("item", jcxm.get("jcxm"));
						jcxm.remove("jcxm");
						if (StringUtil.isNotBlank(jcxm.get("hostpercentile"))){
							DBEncrypt dbEncrypt = new DBEncrypt();
							String fileMsg = getFileMsg(dbEncrypt.dCode(jcxm.get("hostpercentile")));
							jcxm.put("hostpercentile", fileMsg);
						}
						jcxmMap.putAll(jcxm);
						List<Map<String, String>> positiveList = new ArrayList<>();
						List<Map<String, String>> negativeList = new ArrayList<>();
						for (int i = infoList.size() - 1; i >= 0; i--) {
							Map<String, String> info = infoList.get(i);
							if (jcxmMap.get("item").equals(info.get("jcxm")) && id.equals(info.get("ybbh"))){
								resutMap.put("wbbm", info.get("wbbm"));//外部编码
								info.remove("wbbm");
								info.remove("ybbh");
								info.remove("jcxm");
								String yblxdm = info.get("yblxdm");
								String jcxmcskz1 = info.get("jcxmcskz1");
								String projectPath = info.get("project_type");
								if(StringUtil.isBlank(projectPath)) {
									projectPath = StringUtil.isNotBlank( info.get("jcxmcskz4")) ? info.get("jcxmcskz4") :"Q";
								}
								info.remove("yblxdm");
								info.remove("jcxmcskz1");
								info.remove("jcxmcskz4");
								String path = releaseFilePath + BusTypeEnum.IMP_TEMEPLATE_IMAGES.getCode()+"/"+projectPath+"/"+jcxmcskz1+"/"+(StringUtil.isNotBlank(info.get("wzid"))?info.get("wzid"):info.get("sid")) +"_"+yblxdm+".jpg";
								String fileMsg = getFileMsg(path);
								info.put("percentile", fileMsg);
								if ("pathogen".equals(info.get("jglx")) || "possible".equals(info.get("jglx"))){
									positiveList.add(info);
								}else {
									negativeList.add(info);
								}
								infoList.remove(i);
							}
						}
						jcxmMap.put("positive", positiveList);
						jcxmMap.put("negative", negativeList);
						jcxmInfoList.add(jcxmMap);
					}
				}
				resutMap.put("result", jcxmInfoList);
			}
			resutList.add(resutMap);
		}
		return resutList;
	}

	/**
	 * 查询检测结果(旧模板)
	 * @param sjid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getJcjgForOldTemplateNew(String sjid, String jclx, Map<String, Object> resultmap) {
		// TODO Auto-generated method stub
				Map<String,String> map= new HashMap<>();
				List<SjwzxxDto> list = null;
				Object o_pathAndposs = resultmap.get("pathogensAndpossibles");
				if(o_pathAndposs!=null)
					list = (List<SjwzxxDto>)resultmap.get("pathogensAndpossibles");
//				List<SjwzxxDto> possibleList = (List<SjwzxxDto>)resultmap.get("possibleList");
//				List<SjwzxxDto> pathogenList = (List<SjwzxxDto>)resultmap.get("pathogenList");
//				if(possibleList!=null && possibleList.size()>0) {
//					list.addAll(possibleList);
//				}
//				if(pathogenList!=null && pathogenList.size()>0) {
//					list.addAll(pathogenList);
//				}
				
				StringBuffer pathogens=new StringBuffer();
				StringBuffer possible=new StringBuffer();
				if(list!=null && list.size()>0) {
					for (int i = 0; i < list.size(); i++) {
						if("pathogen".equals(list.get(i).getJglx())) {
							pathogens.append(list.get(i).getWzzwm()+"，");
						}else if("possible".equals(list.get(i).getJglx())){
							possible.append(list.get(i).getWzzwm()+"，");
						}
					}
				}
				String pathogens_str=pathogens.toString();
				String possible_str=possible.toString();
				if(StringUtil.isNotBlank(pathogens_str)) {
					pathogens_str=pathogens_str.substring(0,pathogens_str.length()-1);
				}
				if(StringUtil.isNotBlank(possible_str)) {
					possible_str=possible_str.substring(0,possible_str.length()-1);
				}
				map.put("pathogens", pathogens_str);
				map.put("possible",  possible_str);
				return map;
	}
	
	/**
	 * 查询检测结果说明
	 * @param sjxxDto
	 * @param reMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getPathogen_commentToStringNew(SjxxDto sjxxDto,Map<String,Object> reMap) {
		List<SjwzxxDto> list= new ArrayList<>();
		List<SjwzxxDto> possibleList= (List<SjwzxxDto>)reMap.get("possibleList");
		List<SjwzxxDto> pathogenList= (List<SjwzxxDto>)reMap.get("pathogenList");
		List<Map<String, String>> pathogen_comment_list = new ArrayList<>();
		if(pathogenList!=null && pathogenList.size()>0) {
			list.addAll(pathogenList);
		}
		if(possibleList!=null && possibleList.size()>0) {
			list.addAll(possibleList);
		}
		list=list.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList());


		StringBuffer pathogen_comment_new=new StringBuffer();
		StringBuffer pathogen_comment_widthEnglish=new StringBuffer();
		StringBuffer pathogen_comment_hospital=new StringBuffer();
		List<SjwzxxDto> pathogen_comment_new_List=new ArrayList<>();
		if(list!=null && list.size()>0) {
			//对list进行排序，先根据物种分类，再根据读取数
			List<SjwzxxDto> Bacterialist=new ArrayList<>();
			List<SjwzxxDto> Mycobacterialist=new ArrayList<>();
			List<SjwzxxDto> MCRlist=new ArrayList<>();
			List<SjwzxxDto> Fungilist=new ArrayList<>();
			List<SjwzxxDto> Viruses_DNAlist=new ArrayList<>();
			List<SjwzxxDto> Viruses_RNAlist=new ArrayList<>();
			List<SjwzxxDto> Parasitelist=new ArrayList<>();
			List<SjwzxxDto> t_list=new ArrayList<>();
			for(SjwzxxDto sjwzxxDto:list){
				if(StringUtil.isBlank(sjwzxxDto.getWzid()) && StringUtil.isBlank(sjwzxxDto.getSid())){
					//若物种既没有物种id也没有属id，则判断物种不存在
					continue;
				}
				if("Bacteria".equals(sjwzxxDto.getWzfl()))
					Bacterialist.add(sjwzxxDto);
				if("Mycobacteria".equals(sjwzxxDto.getWzfl()))
					Mycobacterialist.add(sjwzxxDto);
				if("MCR".equals(sjwzxxDto.getWzfl()))
					MCRlist.add(sjwzxxDto);
				if("Fungi".equals(sjwzxxDto.getWzfl()))
					Fungilist.add(sjwzxxDto);
				if("Viruses".equals(sjwzxxDto.getWzfl()) && ("DNA".equals(sjwzxxDto.getWzlx()) || "DNA病毒".equals(sjwzxxDto.getWzlx())))
					Viruses_DNAlist.add(sjwzxxDto);
				if("Viruses".equals(sjwzxxDto.getWzfl()) && ("RNA".equals(sjwzxxDto.getWzlx()) || "RNA病毒".equals(sjwzxxDto.getWzlx())))
					Viruses_RNAlist.add(sjwzxxDto);
				if("Parasite".equals(sjwzxxDto.getWzfl()))
					Parasitelist.add(sjwzxxDto);
			}
			t_list.addAll(Bacterialist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			t_list.addAll(Mycobacterialist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			t_list.addAll(MCRlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			t_list.addAll(Fungilist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			t_list.addAll(Viruses_DNAlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			t_list.addAll(Viruses_RNAlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			t_list.addAll(Parasitelist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			for (int i = 1; i < t_list.size()+1; i++) {
				if(StringUtil.isNotBlank(t_list.get(i-1).getWzid()) || StringUtil.isNotBlank(t_list.get(i-1).getSid())){
					if(i>1&&i!=t_list.size()+1) {
						pathogen_comment_new.append("{br}{\\n}");
						pathogen_comment_hospital.append("{br}{\\n}");
						pathogen_comment_widthEnglish.append("{br}{\\n}");
					}else {
						pathogen_comment_new.append("\n");
						pathogen_comment_hospital.append("\n");
						pathogen_comment_widthEnglish.append("\n");
					}
					pathogen_comment_new.append("{br}{b}"+i+"."+(t_list.get(i-1).getWzid()!=null?t_list.get(i-1).getWzzwm():t_list.get(i-1).getSzwm())+":{br}"+t_list.get(i-1).getWzzs()+"["+i+"]。");
					pathogen_comment_hospital.append("{br}{b}"+i+"."+(t_list.get(i-1).getWzid()!=null?t_list.get(i-1).getWzzwm():t_list.get(i-1).getSzwm())+":{br}"+t_list.get(i-1).getWzzs()+"。");
					pathogen_comment_widthEnglish.append("{br}{b}"+i+"."+(t_list.get(i-1).getWzid()!=null?t_list.get(i-1).getWzzwm():t_list.get(i-1).getSzwm())+(t_list.get(i-1).getWzid()!=null?t_list.get(i-1).getWzywm():t_list.get(i-1).getSm())+":{br}"+t_list.get(i-1).getWzzs()+"["+i+"]。");
					t_list.get(i-1).setYyxh(String.valueOf(i));
					pathogen_comment_new_List.add(t_list.get(i-1));
					Map<String, String> pathogen_comment_map = new HashMap<>();
					pathogen_comment_map.put("wzzwm",t_list.get(i-1).getWzzwm());
					pathogen_comment_map.put("wzywm",t_list.get(i-1).getWzywm());
					pathogen_comment_map.put("wzzs",t_list.get(i-1).getWzzs());
					pathogen_comment_list.add(pathogen_comment_map);
				}
			}
		}
		
		Map<String,Object> resultMap= new HashMap<>();
		resultMap.put("pathogen_comment_new", pathogen_comment_new.toString());
		resultMap.put("pathogen_comment_hospital", pathogen_comment_hospital.toString());
		resultMap.put("pathogen_comment_widthEnglish", pathogen_comment_widthEnglish.toString());
		resultMap.put("pathogen_comment_list", pathogen_comment_list);
		resultMap.put("pathogen_comment_new_List", pathogen_comment_new_List);
		return resultMap;
	}
	/**
	 * 查询检测结果说明(旧模板)
	 * @param sjxxDto
	 * @param resultMap
	 * @return
	 */
	@Override
	public Map<String, Object> getPathogenForOldTemplateNew(SjxxDto sjxxDto,Map<String,Object> resultMap) {
		// TODO Auto-generated method stub
		Map<String,Object> map= new HashMap<>();
		int index =1;
		
		List<SjwzxxDto> pathogen_comment_old_List = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<SjwzxxDto> list1=(List<SjwzxxDto>)resultMap.get("pathogenList");
		StringBuffer pathogen_comment=new StringBuffer();
		if(list1!=null && list1.size()>0) {
			for (int i = 1; i < list1.size()+1; i++) {
				if("pathogen".equals(list1.get(i-1).getJglx())) {
					if(i >1 &&i!=list1.size()+1)
						 pathogen_comment.append("{br}{\\n}");
					else
						pathogen_comment.append("\n");
					pathogen_comment.append("{br}1."+i+"{br}{b}"+list1.get(i-1).getWzzwm()+":{br}"+list1.get(i-1).getWzzs()+"["+i+"]。");
					list1.get(i-1).setOldyyxh(String.valueOf(i));
					pathogen_comment_old_List.add(list1.get(i-1));
					index++;
				}	
			}
		}
		
		@SuppressWarnings("unchecked")
		List<SjwzxxDto> list2=(List<SjwzxxDto>)resultMap.get("possibleList");
		StringBuffer possible_comment=new StringBuffer();
		if(list2!=null && list2.size()>0) {
			for (int i = 1; i < list2.size()+1; i++) {
				 if("possible".equals(list2.get(i-1).getJglx())){
					 if(i >1 &&i!=list2.size()+1)
						 possible_comment.append("{br}{\\n}");
					 else
						 possible_comment.append("\n");
					 possible_comment.append("{br}2."+i+"{br}{b}"+list2.get(i-1).getWzzwm()+":{br}"+list2.get(i-1).getWzzs()+"["+index+"]。");
					 list2.get(i-1).setOldyyxh(String.valueOf(i));
					 pathogen_comment_old_List.add(list2.get(i-1));
					 index++;
				}
			}
		}
		if(StringUtil.isNotBlank(pathogen_comment)) {
			pathogen_comment.append("{br}{\\n}");
		}
		
		String pathogen_comment_old=pathogen_comment.toString()+possible_comment.toString();
		map.put("pathogen_comment_old",pathogen_comment_old);
		map.put("pathogen_comment_old_List",pathogen_comment_old_List);
		return map;
	}
	public String getFileMsg(String path) {
		byte[] data = null;
		org.apache.commons.codec.binary.Base64 base64 = new Base64();//sun.misc.BASE64Decoder() jdk8可用，11不可用，改为当前的方法替换
		try {
			if (StringUtil.isNotBlank(path)){
				FileInputStream fileInputStream = new FileInputStream(path);
				data = new byte[fileInputStream.available()];
				fileInputStream.read(data);
				fileInputStream.close();
			}else {return "";}

		} catch (IOException e) {
			log.error("PDF文件转base64报错"+e.getMessage());
		}
		return base64.encodeAsString(data);//sun.misc.BASE64Decoder() jdk8可用，11不可用，改为当前的方法替换
	}

	/**
	 * 根据sjids和jclx删除
	 * @param sjwzxxDto
	 * @return
	 */
	public boolean deleteBySjidsAndJclx(SjwzxxDto sjwzxxDto){
		return dao.deleteBySjidsAndJclx(sjwzxxDto);
	}


	/**
	 * 根据sjid和ywlx获取检测结果
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Map<String, Object> getJcjgInfoByXinghe(SjxxDto sjxxDto){
		List<Map<String, String>> infoList = dao.getJcjgInfoBySjidAndYwlx(sjxxDto);
		Map<String, Object> resutMap = new HashMap<>();//样本map
		List<Map<String,Object>> result = new ArrayList<>();
		if (!CollectionUtils.isEmpty(infoList)){
			for (Map<String, String> info_temp : infoList) {
				Map<String, Object> temp = new HashMap<>();//物种
				if (info_temp.get("wzid")!=null) {
					temp.put("taxid",info_temp.get("wzid").toString());
				}
				if (info_temp.get("sid")!=null) {
					temp.put("genus_taxid",info_temp.get("sid").toString());
				}
				if (info_temp.get("sfd")!=null) {
					temp.put("genus_abundance",info_temp.get("sfd").toString());
				}
				if (info_temp.get("xdfd")!=null) {
					temp.put("abundance",info_temp.get("xdfd").toString());
				}
				if (info_temp.get("wzywm")!=null) {
					temp.put("name",info_temp.get("wzywm").toString());
				}
				if (info_temp.get("wzzwm")!=null) {
					temp.put("cn_name",info_temp.get("wzzwm").toString());
				}
				if (info_temp.get("sm")!=null) {
					temp.put("genus_name",info_temp.get("sm").toString());
				}
				if (info_temp.get("szwm")!=null) {
					temp.put("genus_cn_name",info_temp.get("szwm").toString());
				}
				if (info_temp.get("sdds")!=null) {
					temp.put("genusSeqNum",info_temp.get("sdds").toString());
				}
				if (info_temp.get("dqs")!=null) {
					temp.put("speciesSeqNum",info_temp.get("dqs").toString());
				}
				if (info_temp.get("rpm")!=null) {
					temp.put("rpm",info_temp.get("rpm").toString());
				}
				if (info_temp.get("fldj")!=null) {
					temp.put("rank_code",info_temp.get("fldj").toString());
				}
				if (info_temp.get("wzfl")!=null) {
					temp.put("sp_type",info_temp.get("wzfl").toString());
				}
				result.add(temp);
			}
		}
		resutMap.put("result", result);
		List<Map<String, Object>> nyList = sjnyxService.getNyxMapList(sjxxDto);
		List<Map<String,Object>> drug_resistance_stat = new ArrayList<>();
		if (!CollectionUtils.isEmpty(nyList)){
			for (Map<String, Object> ny_temp : nyList) {
				Map<String, Object> temp = new HashMap<>();//物种

				if (ny_temp.get("nyjyid")!=null) {
					temp.put("id",ny_temp.get("nyjyid").toString());
				}
				//igams_nyjyzsgl.nyjzu
				if (ny_temp.get("jyjzmc")!=null) {
					temp.put("gene_family",ny_temp.get("jyjzmc").toString());
				}
				if (ny_temp.get("xls")!=null) {
					temp.put("reads",ny_temp.get("xls").toString());
				}
				if (ny_temp.get("qyz")!=null) {
					temp.put("origin_species",ny_temp.get("qyz").toString());
				}
				//nyfxjg.gram_stain
//				if (ny_temp.get("")!=null) {
//					temp.put("gram_stain",ny_temp.get("").toString());
//				}
				//igams_nyjyzsgl.zsnr
				if (ny_temp.get("zsnr")!=null) {
					temp.put("comment",ny_temp.get("zsnr").toString());
				}
				if (ny_temp.get("jyfx")!=null) {
					temp.put("genes",ny_temp.get("jyfx").toString());
				}
				//nyfxjg.taxid
//				if (ny_temp.get("")!=null) {
//					temp.put("taxid",ny_temp.get("").toString());
//				}
				//nyfxjg.zdnyjy
//				if (ny_temp.get("")!=null) {
//					temp.put("top_gene",ny_temp.get("").toString());
//				}
				if (ny_temp.get("yp")!=null) {
					temp.put("drug_class",ny_temp.get("yp").toString());
				}
				if (ny_temp.get("jl")!=null) {
					temp.put("main_mechanism",ny_temp.get("jl").toString());
				}
				if (ny_temp.get("jy")!=null) {
					temp.put("related_gene",ny_temp.get("jy").toString());
				}
				drug_resistance_stat.add(temp);
			}
		}
		resutMap.put("drug_resistance_stat", drug_resistance_stat);
		return resutMap;
	}
	
	@Override
	public boolean updateBySjwzxxDtos(List<SjwzxxDto> sjwzxxDtos) {
		return dao.updateBySjwzxxDtos(sjwzxxDtos);
	}

	/**
	 * @param dto
	 * @return
	 */
	@Override
	public List<Map<String, Object>> searchResult(SjxxDto dto) {
		return dao.searchResult(dto);
	}


	@Override
	public List<SjwzxxDto> getJcjgByJglx(SjwzxxDto dto) {
		return dao.getJcjgByJglx(dto);
	}

}
