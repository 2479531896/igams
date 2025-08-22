package com.matridx.igams.wechat.enums;
/**
 * 消息类型枚举类
 * @author xinyf
 * @date 2015-5-28
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CustomStaticEnum {
	//定义名称 （字段名，主表字段:可以不用关联，页面显示名称，检索字段，group字段：用于去除AS）
	//tjzd ,name,mainselect,maingroup,selectzd,groupzd
	NONE("none","无","none","none","none","none"),
	SJDW("sjdw","送检单位","sjdw","sjdw","sj.sjdw","sj.sjdw"),
	KS("ks","科室","ks","ks","sjdwxx.dwmc as ksmc","sjdwxx.dwmc"),
	YBLX("yblx","标本类型","yblx","yblx","jc_yb.csmc as yblxmc","jc_yb.csmc"),
	XSFL("fl","销售分类","fl","fl","jc_fl.csmc as flmc","jc_fl.csmc"),
	XSZFL("zfl","销售子分类","zfl","zfl","jc_zfl.csmc as zflmc","jc_zfl.csmc"),
	BGMB("bgmb","报告模板","bgmb","bgmb","jc_bg.csmc as bgmb","jc_bg.csmc"),
	DB("db","合作伙伴","db","db","sj.db","sj.db"),
	JCXM("jcxmid","检测项目","jcxmid","jcxmid","jc_xm.csmc as jcxmmc","jc_xm.csmc"),
	JSRQ("jsrq","接收日期","to_char(jsrq，'YYYY-MM-DD') jsrq","to_char(jsrq，'YYYY-MM-DD')","to_char(sj.jsrq，'YYYY-MM-DD') as jsrq","to_char(sj.jsrq，'YYYY-MM-DD')"),
	BGRQ("bgrq","报告日期","to_char(bgrq，'YYYY-MM-DD') bgrq","to_char(bgrq，'YYYY-MM-DD')","to_char(sj.bgrq，'YYYY-MM-DD') as bgrq","to_char(sj.bgrq，'YYYY-MM-DD')");
	
	private String code;
	private String value;
	private String mainSelect;
	private String mainGroup;
	private String selectzd;
	private String groupzd;
	
	CustomStaticEnum(String code, String value, String mainSelect, String mainGroup, String selectzd, String groupzd) {
		this.code = code;
		this.mainSelect = mainSelect;
		this.mainGroup = mainGroup;
		this.value = value;
		this.selectzd = selectzd;
		this.groupzd = groupzd;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getCode(){
		return code;
	}
	
	public String getMainSelect(){
		return mainSelect;
	}
	
	public String getMainGroup(){
		return mainGroup;
	}
	
	public String getSelectzd() {
		return selectzd;
	}
	
	public String getGroupzd() {
		return groupzd;
	}
	
	public static String getValueByCode(String code){
		for (CustomStaticEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
	
	public static List<Map<String, String>> getAllListMap(){
		List<Map<String, String>> listMap= new ArrayList<>();
		for (CustomStaticEnum enumi : values()) {
			Map<String, String> map= new HashMap<>();
			map.put("code",enumi.getCode());
			map.put("value",enumi.getValue());
			map.put("mainSelect",enumi.getMainSelect());
			map.put("mainGroup",enumi.getMainGroup());
			map.put("selectzd",enumi.getSelectzd());
			map.put("groupzd",enumi.getGroupzd());
			listMap.add(map);
		}
		return listMap;
	}
	
	public static List<Map<String, String>> getWhereListMap(){
		List<Map<String, String>> listMap= new ArrayList<>();
		for (CustomStaticEnum enumi : values()) {
			if(enumi.getCode().equals("jsrq")||enumi.getCode().equals("bgrq")) {
                continue;
            }
			Map<String, String> map= new HashMap<>();
			map.put("code",enumi.getCode());
			map.put("value",enumi.getValue());
			map.put("mainSelect",enumi.getMainSelect());
			map.put("mainGroup",enumi.getMainGroup());
			map.put("selectzd",enumi.getSelectzd());
			map.put("groupzd",enumi.getGroupzd());
			listMap.add(map);
		}
		return listMap;
	}
}
