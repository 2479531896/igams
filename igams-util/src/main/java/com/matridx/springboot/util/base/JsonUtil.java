package com.matridx.springboot.util.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {
	
	private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
	/**
	 * 把json转化为list对象,json格式要求：外层以data为key,value为json数组，数组内部为可转化生成的对象
	 * 如：{data:[{tjdm:'wjqk',tjgx:'='},{tjdm:'pjcj',tjgx:'='}]},对象为：XmwhTjszForm
	 * 格式错误，转化失败，直接返回空
	 */
	public static List<Object> jsonToList(String json, Class<?> clazz) {
		Object[] array;
		JSONObject str;
		JSONArray jsonArray;
		try {
			str = JSONObject.fromObject(json);
			jsonArray = str.getJSONArray("data");
			array = (Object[]) JSONArray.toArray(jsonArray, clazz);

		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
		List<Object> list = new ArrayList<>();
		if (array != null) {
            for (Object o : array) {
                if (o != null) {
                    list.add(o);
                }
            }
		}
		return list;
	}

	/**
	 * 把json转化为list对象,json格式要求：外层以为json数组，数组内部为可转化生成的对象
	 * 如：[{tjdm:'wjqk',tjgx:'='},{tjdm:'pjcj',tjgx:'='}],对象为：XmwhTjszForm
	 * 格式错误，转化失败，直接返回空
	 */
	public static List<Object> jsonArrToList(String json, Class<?> clazz) {
		json = "{data:" + json + "}";
		return jsonToList(json, clazz);
	}

	/**
	 * 把json转化为list对象,json格式要求：外层以data为key,value为json数组，数组内部为可转化生成的对象
	 * 如：{data:[{tjdm:'wjqk',tjgx:'='},{tjdm:'pjcj',tjgx:'='}]},对象为：XmwhTjszForm
	 * 格式错误，转化失败，直接返回空
	 */
	public static List<Object> jsonToList(String json) {
		Object[] array;
		JSONObject str;
		JSONArray jsonArray;
		try {
			str = JSONObject.fromObject(json);
			jsonArray = str.getJSONArray("data");
			array = (Object[]) JSONArray.toArray(jsonArray);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
		List<Object> list = new ArrayList<>();
		if (array != null) {
            for (Object o : array) {
                if (o != null) {
                    list.add(o);
                }
            }
		}
		return list;
	}

	/**
	 * 把json转化为list对象,json格式要求：外层为json数组，数组内部为可转化生成的对象
	 * 如：[{tjdm:'wjqk',tjgx:'='},{tjdm:'pjcj',tjgx:'='}],对象为：XmwhTjszForm
	 * 格式错误，转化失败，直接返回空
	 */
	public static List<Object> jsonArrToList(String json) {
		json = "{data:" + json + "}";
		return jsonToList(json);
	}

	public static Object jsonToModel(String json, Class<?> clazz) {
		Object bean = null;
		JSONObject jsonObject;
		try {
			jsonObject = JSONObject.fromObject(json);
			bean = JSONObject.toBean(jsonObject, clazz);
		} catch (Exception e) {
			log.warn("json字符串转model出错："  + json + " ==> " + clazz.getName());
		}
		return bean;
	}
	
	@SuppressWarnings("unchecked")
	public static Object cleanAttribute(Object obj, String limitInfo) {
		try {
			if(obj == null || StringUtil.isBlank(limitInfo))
				return obj;
			if(obj.getClass().getSimpleName().equals("ArrayList")) {
				List<Object> o_list = (List<Object>)obj;
				if(o_list.isEmpty())
					return obj;
				Object subObject = o_list.get(0);
				String className = subObject.getClass().getSimpleName();
				
				Map<String,String> m_limitInfo = com.alibaba.fastjson.JSONObject.parseObject(limitInfo, HashMap.class);
				String keyName = null;
				for (String key : m_limitInfo.keySet()) {
					if(className.equalsIgnoreCase(key)){
						keyName = key;
						break;
					}
				}
				//如果找到需要限制的信息，则直接进行限制
				if(StringUtil.isNotBlank(keyName)) {
					String val = StringUtil.defaultString(m_limitInfo.get(keyName)).trim();
					String[] columns = val.split(",");
					//当开放的字段太多时，就无需限制
					SimplePropertyPreFilter filter = new SimplePropertyPreFilter(subObject.getClass(), columns);
					return com.alibaba.fastjson.JSONObject.parse(com.alibaba.fastjson.JSONObject.toJSONString(obj, filter));
				}else {
					return obj;
				}
			}else {
				String className = obj.getClass().getSimpleName();
				
				Map<String,String> m_limitInfo = com.alibaba.fastjson.JSONObject.parseObject(limitInfo, HashMap.class);
				String keyName = null;
				for (String key : m_limitInfo.keySet()) {
					if(className.equalsIgnoreCase(key)){
						keyName = key;
						break;
					}
				}
				//如果找到需要限制的信息，则直接进行限制
				if(StringUtil.isNotBlank(keyName)) {
					String val = StringUtil.defaultString(m_limitInfo.get(keyName)).trim();
					String[] columns = val.split(",");
					SimplePropertyPreFilter filter = new SimplePropertyPreFilter(obj.getClass(), columns);
					return com.alibaba.fastjson.JSONObject.parse(com.alibaba.fastjson.JSONObject.toJSONString(obj, filter));
				}else {
					return obj;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	public static void main(String[] args) {
		List<String> xm = new ArrayList<>();
		xm.add("333");
		Object res = JsonUtil.cleanAttribute(xm, "{\"aa\":\"a1,a2,a3\",\"bb\":\"b1,b2,b3\"}");
		System.out.println(res.toString());
		
		Map<String,String> map = new HashMap<>();
		map.put("aa", "a1,a2,a3");
		map.put("bb", "b1,b2,b3");
		System.out.println(com.alibaba.fastjson.JSONObject.toJSONString(map));
//		String json = "[{xn:'2012-2-13',xq:'01',lxdm:'01'},{xn:'2012-2-13',xq:'01',lxdm:'02'}]";
//		List list = jsonArrToList(json);
//		for (Object object : list) {
//			net.sf.ezmorph.bean.MorphDynaBean bean = (net.sf.ezmorph.bean.MorphDynaBean) object;
//
//			System.out.println(bean.get("xn"));
//		}
		
		
	}

}
