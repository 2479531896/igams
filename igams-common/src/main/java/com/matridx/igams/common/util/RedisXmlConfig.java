package com.matridx.igams.common.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.xml.BasicXmlReader;

@Configuration
public class RedisXmlConfig {
	
	@Autowired
	RedisUtil redisUtil;
	
	@Value("${matridx.basictype.file:}")
	private String basicTypeFile;
	
	@Value("${matridx.imptype.file:}")
	private String impTypeFile;
	
	@Value("${matridx.exptype.file:}")
	private String expTypeFile;
	
	private final String impkey = "IMP_:_CONFIG";
	private final String expkey = "EXP_:_CONFIG";
	private final String basickey = "BASIC_:_CONFIG";
	private final String taskkey = "TASK_:_CONFIG";
	
	/**
	 * 根据key获取所有的设置记录
	 * @param key
	 * @return
	 */
	public Map<Object, Object> getAllConfig(String key){
		return redisUtil.hmget(key);
	}
	
	/**
	 * 根据key获取所有的导入设置记录
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getImpConfig(String key){
		if(StringUtil.isBlank(key))
			return null;
		List<Map<String, String>> impConfig = (List<Map<String, String>>)redisUtil.get(impkey);
		//如果不存在，则直接读取xml文件，否则读取内存缓存
		if(impConfig == null){
			List<Map<String, String>> importList = BasicXmlReader.readXmlToList(impTypeFile);
			redisUtil.set(impkey, importList);
            for (Map<String, String> recodt : importList) {
                if (key.equals(recodt.get("id"))) {
                    return recodt;
                }
            }
		}else{
            for (Map<String, String> recodt : impConfig) {
                if (key.equals(recodt.get("id"))) {
                    return recodt;
                }
            }
		}
		return null;
	}
	
	/**
	 * 根据key获取所有的导入设置记录
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getTaskConfig(String key){
		if(StringUtil.isBlank(key))
			return null;
		List<Map<String, String>> taskConfig = (List<Map<String, String>>)redisUtil.get(taskkey);
		//如果不存在，则直接读取xml文件，否则读取内存缓存
		if(taskConfig == null){
			List<Map<String, String>> taskList = BasicXmlReader.readXmlToList("config/comm/config-task.xml");
			redisUtil.set(taskkey, taskList);
            for (Map<String, String> recodt : taskList) {
                if (key.equals(recodt.get("id"))) {
                    return recodt;
                }
            }
		}else{
            for (Map<String, String> recodt : taskConfig) {
                if (key.equals(recodt.get("id"))) {
                    return recodt;
                }
            }
		}
		return null;
	}
	
	/**
	 * 根据key获取所有的导出设置记录
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getExpConfig(String key){
		if(StringUtil.isBlank(key))
			return null;
		List<Map<String, String>> expConfig = (List<Map<String, String>>)redisUtil.get(expkey);
		//如果不存在，则直接读取xml文件，否则读取内存缓存
		if(expConfig == null){
			List<Map<String, String>> exportList = BasicXmlReader.readXmlToList(expTypeFile);
			redisUtil.set(expkey, exportList);
            for (Map<String, String> recodt : exportList) {
                if (key.equals(recodt.get("id"))) {
                    return recodt;
                }
            }
		}else{
            for (Map<String, String> recodt : expConfig) {
                if (key.equals(recodt.get("id"))) {
                    return recodt;
                }
            }
		}
		return null;
	}
	
	/**
	 * 根据key获取所有的基础类别设置记录
	 * @param key
	 * @return
	 */
	public Map<Object, Object> getJclbConfig(String key){
		if(StringUtil.isBlank(key))
			return null;
		List<Object> basicConfig = redisUtil.lGet(basickey);
		//如果不存在，则直接读取xml文件，否则读取内存缓存
		if(basicConfig == null){
			List<Map<Object, Object>> basicList = BasicXmlReader.readXmlToObjectList(basicTypeFile);
			redisUtil.lSet(basickey, basicList);
            for (Map<Object, Object> recodt : basicList) {
                if (key.equals(recodt.get("id"))) {
                    return recodt;
                }
            }
		}else{
            for (Object o : basicConfig) {
                @SuppressWarnings("unchecked")
                Map<Object, Object> recodt = (Map<Object, Object>) o;
                if (key.equals(recodt.get("id"))) {
                    return recodt;
                }
            }
		}
		return null;
	}
	
	public RedisUtil getRedisUtil(){
		return redisUtil;
	}
}
