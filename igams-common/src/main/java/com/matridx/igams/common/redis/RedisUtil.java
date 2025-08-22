package com.matridx.igams.common.redis;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import org.apache.commons.codec.binary.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * @author ZENG.XIAO.YAN
 * @date2018年6月7日
 */
@Component
public final class RedisUtil {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	// =============================common============================
	/**
	* 指定缓存失效时间
	* @param key 键
	* @param time 时间(秒)
	* @return
	*/
	public boolean expire(String key, long time) {
		try {
			if (time > 0) {
				 redisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**修改key
	 *
	 * @param oldkey
	 * @param newkey
	 */
	public boolean rename(String oldkey,String newkey){
		if(redisTemplate.hasKey(oldkey)){
			if(redisTemplate.hasKey(newkey)){
				this.del(newkey);
			}
			return redisTemplate.renameIfAbsent(oldkey,newkey);
		}
		return false;
	}

	/**
	* 根据key 获取过期时间
	* @param key 键 不能为null
	* @return 时间(秒) 返回0代表为永久有效
	*/
	public long getExpire(String key) {
		//说明：返回值是键的剩余时间，-1表示该键没有设置过期时间，-2表示该键不存在
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	* 判断key是否存在
	* @param key 键
	* @return true 存在 false不存在
	*/
	public boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	* 删除缓存
	* @param key 可以传一个值 或多个
	*/
	@SuppressWarnings("unchecked")
	public void del(String... key) {
		if (key != null && key.length > 0) {
			if (key.length == 1) {
				 redisTemplate.delete(key[0]);
			} else {
				 redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
			}
		}
	}
	
	/**
	 * 模糊查询，并删除
	 * @param key
	 */
	public void delLike(String... key) {
		if (key != null) {
			for (String pattern : key) {
				//清除基础数据的所有键
				Set<String> keys = redisTemplate.keys("*"+pattern+"*");
				redisTemplate.delete(keys);
			}
		}
	}
	/**
	 * 模糊查询，并删除
	 * @param key
	 */
	public void delChildLikeItem(String key,String item) {
		if (StringUtil.isNotBlank(key)) {
			//清除基础数据的所有键
			Set<Object> keys = redisTemplate.opsForHash().keys(key);
			for (Object o : keys) {
				if (o!=null){
					String i_s = o.toString();
					if (i_s.contains(item)){
						hdel(key,i_s);
					}
				}
			}
		}
	}

	// ============================String=============================
	/**
	* 普通缓存获取
	* @param key 键
	* @return 值
	*/
	public Object get(String key) {
		return key == null ? null : redisTemplate.opsForValue().get(key);
	}

	/**
	* 普通缓存放入
	* @param key 键
	* @param value 值
	* @return true成功 false失败
	*/
	public boolean set(String key, Object value) {
		try {
			redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	* 普通缓存放入并设置时间
	* @param key 键
	* @param value 值
	* @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
	* @return true成功 false 失败
	*/
	public boolean set(String key, Object value, long time) {
		try {
			if (time > 0) {
				 redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			} else {
				 set(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	* 递增
	* @param key 键
	* @param delta 要增加几(大于0)
	* @return
	*/
	public long incr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递增因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, delta);
	}

	/**
	* 递减
	* @param key 键
	* @param delta 要减少几(小于0)
	* @return
	*/
	public long decr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递减因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, -delta);
	}

	// ================================Map=================================
	/**
	* HashGet
	* @param key 键 不能为null
	* @param item 项 不能为null
	* @return 值
	*/
	public Object hget(String key, String item) {
		return redisTemplate.opsForHash().get(key, item);
	}
	
    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 返回值为JcsjDto
     */
	public JcsjDto hgetDto(String key, String item) {
		Object obj = redisTemplate.opsForHash().get(key, item);
		if(obj ==null)
			return null;
		JcsjDto jcsjdto = JSON.parseObject(obj.toString(), JcsjDto.class);
		return jcsjdto;
	}
    /**
     * HashUserGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 返回值为UserDto
     */
	public User hugetDto(String key, String item) {
		Object obj = redisTemplate.opsForHash().get(key, item);
		User user;
		if (obj!=null){
			user = JSON.parseObject((String)obj, User.class);
		}else {
			user = null;
		}
		return user;
	}

	/**
	* 获取hashKey对应的所有键值
	* @param key 键
	* @return 对应的多个键值
	*/
	public Map<Object, Object> hmget(String key) {
		return redisTemplate.opsForHash().entries(key);
	}


	
	/**
	* 获取hashKey对应的所有键值
	* @param key 键
	* @return 对应的多个键值
	*/
	public List<JcsjDto> hmgetDto(String key) {
		Map<Object, Object> map = hmget(key);//获取key中所有项，遍历项转为bean存入list中
		List<JcsjDto> jclist = new ArrayList<>();
		for(Object obj : map.values()) {
			JcsjDto j = JSON.parseObject(obj.toString(),JcsjDto.class);
			jclist.add(j);
		}
		return jclist;
	}

	/**
	 * HashGet
	 * @param key 键 不能为null
	 * @param item 项 不能为null
	 * @return 值
	 */
	public Map<String,Object> hgetStatistics(String item) {
		//优先从default获取，若defalut无则从weekLeadStatis获取
		Map<String, Object> map = new HashMap<>();
		if (hHasKey("weekLeadStatisDefault",item)){
			map.put("obj",hget("weekLeadStatisDefault",item));
			map.put("key","weekLeadStatisDefault");
		}else if(hHasKey("weekLeadStatis",item)){
			map.put("obj",hget("weekLeadStatis",item));
			map.put("key","weekLeadStatis");
		}else{
			map.put("obj",null);
			map.put("key",null);
		}
		return map;
	}

	/**
	* HashSet
	* @param key 键
	* @param map 对应多个键值
	* @return true 成功 false 失败
	*/
	public boolean hmset(String key, Map<String, Object> map) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	* HashSet 并设置时间
	* @param key 键
	* @param map 对应多个键值
	* @param time 时间(秒)
	* @return true成功 false失败
	*/
	public boolean hmset(String key, Map<String, Object> map, long time) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			if (time > 0) {
				 expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * HashSet 并设置时间
	 * @param key 键
	 * @param map 对应多个键值
	 * @param time 时间(秒)
	 * @return true成功 false失败
	 */
	public boolean hmsetobj(String key, Map<Object, Object> map, long time) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}



	/**
	* 向一张hash表中放入数据,如果不存在将创建，不存在失效时间
	* @param key 键
	* @param item 项
	* @param value 值
	* @return true 成功 false失败
	*/
	public boolean hsetNoTime(String key, String item, Object value) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	* 向一张hash表中放入数据,如果不存在将创建,存在默认失效时间
	* @param key 键
	* @param item 项
	* @param value 值
	* @return true 成功 false失败
	*/
	public boolean hset(String key, String item, Object value) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			//若改key设置了过期时间则使用设置时间，无则设置一个默认失效时间 1h
			if(getExpire(key) <= 0){
				//设置默认失效时间：1小时
				expire(key, 3600);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 创建一次连接向Redis中批量放入hash数据
	 * @param map 值
	 * @param time 过期时间
	 * @return true 成功 false失败
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean hBatchSet(Map<String, Map<String, Object>> map,long time) {
		try {
			RedisSerializer keySerializer = redisTemplate.getKeySerializer();
			RedisSerializer hashKeySerializer = redisTemplate.getHashKeySerializer();
			RedisSerializer hashValueSerializer = redisTemplate.getHashValueSerializer();
			redisTemplate.executePipelined((RedisCallback<Map>) connection->{
                for (Map.Entry<String, Map<String, Object>> next : map.entrySet()) {
                    byte[] rawKey = keySerializer.serialize(next.getKey());
                    Map<byte[], byte[]> hashes = new LinkedHashMap(next.getValue().size());
                    for (Map.Entry<String, Object> stringObjectEntry : next.getValue().entrySet()) {
                        Map.Entry<String, Object> entry = (Map.Entry) stringObjectEntry;
                        hashes.put(hashKeySerializer.serialize(entry.getKey()),
                                hashValueSerializer.serialize(entry.getValue()));
                    }
                    connection.hMSet(rawKey, hashes);
					connection.expire(rawKey,time);
                }
				return null;
			});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 创建一次连接向Redis中批量放入hash数据
	 * @param map 值
	 * @return true 成功 false失败
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean hBatchSet(Map<String, Map<String, Object>> map) {
		try {
			RedisSerializer keySerializer = redisTemplate.getKeySerializer();
			RedisSerializer hashKeySerializer = redisTemplate.getHashKeySerializer();
			RedisSerializer hashValueSerializer = redisTemplate.getHashValueSerializer();
			redisTemplate.executePipelined((RedisCallback<Map>) connection->{
                for (Map.Entry<String, Map<String, Object>> next : map.entrySet()) {
                    byte[] rawKey = keySerializer.serialize(next.getKey());
                    Map<byte[], byte[]> hashes = new LinkedHashMap(next.getValue().size());
                    for (Map.Entry<String, Object> stringObjectEntry : next.getValue().entrySet()) {
                        Map.Entry<String, Object> entry = (Map.Entry) stringObjectEntry;
                        hashes.put(hashKeySerializer.serialize(entry.getKey()),
                                hashValueSerializer.serialize(entry.getValue()));
                    }
                    connection.hMSet(rawKey, hashes);
                }
				return null;
			});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	* 向一张hash表中放入数据,如果不存在将创建
	* @param key 键
	* @param item 项
	* @param value 值
	* @param time 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
	* @return true 成功 false失败
	*/
	public boolean hset(String key, String item, Object value, long time) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			if (time > 0) {
				 expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Map<Object, Object> values(String key){
		return redisTemplate.opsForHash().entries(key);
	}
	/**
	* 删除hash表中的值
	* @param key 键 不能为null
	* @param item 项 可以使多个 不能为null
	*/
	public void hdel(String key, Object... item) {
		redisTemplate.opsForHash().delete(key, item);
	}

	/**
	 * @Description: 根据key值删除所有hash数据
	 * @param key
	 * @return void
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 14:19
	 */
	public void hdelByKey(String key){
		redisTemplate.opsForHash().entries(key).clear();
	}

	/**
	* 判断hash表中是否有该项的值
	* @param key 键 不能为null
	* @param item 项 不能为null
	* @return true 存在 false不存在
	*/
	public boolean hHasKey(String key, String item) {
		return redisTemplate.opsForHash().hasKey(key, item);
	}

	/**
	* hash递增 如果不存在,就会创建一个 并把新增后的值返回
	* @param key 键
	* @param item 项
	* @param by 要增加几(大于0)
	* @return
	*/
	public double hincr(String key, String item, double by) {
		return redisTemplate.opsForHash().increment(key, item, by);
	}

	/**
	* hash递减
	* @param key 键
	* @param item 项
	* @param by 要减少记(小于0)
	* @return
	*/
	public double hdecr(String key, String item, double by) {
		return redisTemplate.opsForHash().increment(key, item, -by);
	}

	// ============================set=============================
	/**
	* 根据key获取Set中的所有值
	* @param key 键
	* @return
	*/
	public Set<Object> sGet(String key) {
		try {
			return redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	* 根据value从一个set中查询,是否存在
	* @param key 键
	* @param value 值
	* @return true 存在 false不存在
	*/
	public boolean sHasKey(String key, Object value) {
		try {
			return redisTemplate.opsForSet().isMember(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	* 将数据放入set缓存
	* @param key 键
	* @param values 值 可以是多个
	* @return 成功个数
	*/
	public long sSet(String key, Object... values) {
		try {
			return redisTemplate.opsForSet().add(key, values);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	* 将set数据放入缓存
	* @param key 键
	* @param time 时间(秒)
	* @param values 值 可以是多个
	* @return 成功个数
	*/
	public long sSetAndTime(String key, long time, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().add(key, values);
			if (time > 0)
				 expire(key, time);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	* 获取set缓存的长度
	* @param key 键
	* @return
	*/
	public long sGetSetSize(String key) {
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	/**
	* 移除值为value的
	* @param key 键
	* @param values 值 可以是多个
	* @return 移除的个数
	*/
	public long setRemove(String key, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().remove(key, values);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	// ===============================list=================================

	/**
	* 获取list缓存的内容
	* @param key 键

	* @return
	*/
	public List<Object> lGet(String key) {
		try {
			long end = redisTemplate.opsForList().size(key);
			return redisTemplate.opsForList().range(key, 0, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	* 获取list缓存的内容
	* @param key 键
	* @param start 开始
	* @param end 结束 0 到 -1代表所有值
	* @return
	*/
	public List<Object> lGet(String key, long start, long end) {
		try {
			return redisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取list缓存的内容(组装成List<JcsjDto>)
	 * @param key 键
	 * @return
	 */
	public  List<JcsjDto> lgetDto(String key){
		List<Object> list = lGet(key);
		List<JcsjDto> jclist = new ArrayList<>();
		for (Object obj : list) {
			JcsjDto j = JSON.parseObject(obj.toString(), JcsjDto.class);
			jclist.add(j);
		}
		return jclist;
	}
	/**
	* 获取list缓存的长度
	* @param key 键
	* @return
	*/
	public long lGetListSize(String key) {
		try {
			return redisTemplate.opsForList().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	* 通过索引 获取list中的值
	* @param key 键
	* @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	* @return
	*/
	public Object lGetIndex(String key, long index) {
		try {
			return redisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	// 使用LREM命令删除List中所有等于特定值的元素
	public void removeValueFromList(String key, Object value) {
		// 注意：0表示删除所有匹配的元素
		redisTemplate.opsForList().remove(key, 0, value);
	}
	/**
	* 将list放入缓存
	* @param key 键
	* @param value 值
	* @return
	*/
	public boolean lSet(String key, Object value) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	* 将list放入缓存
	* @param key 键
	* @param value 值
	* @param time 时间(秒)
	* @return
	*/
	public boolean lSet(String key, Object value, long time) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			if (time > 0)
				 expire(key, time);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	* 将list放入缓存
	* @param key 键
	* @param value 值
	* @return
	*/
	public boolean lSet(String key, List<Object> value) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	* 将list放入缓存
	* 
	* @param key 键
	* @param value 值
	* @param time 时间(秒)
	* @return
	*/
	public boolean lSet(String key, List<Object> value, long time) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			if (time > 0)
				 expire(key, time);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	* 根据索引修改list中的某条数据
	* @param key 键
	* @param index 索引
	* @param value 值
	* @return
	*/
	public boolean lUpdateIndex(String key, long index, Object value) {
		try {
			redisTemplate.opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	* 移除N个值为value
	* @param key 键
	* @param count 移除多少个
	* @param value 值
	* @return 移除的个数
	*/
	public long lRemove(String key, long count, Object value) {
		try {
			Long remove = redisTemplate.opsForList().remove(key, count, value);
			return remove;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 只保留从start到end之间的元素
	 *
	 * @param key   键
	 * @param start 开始
	 * @param end   结束
	 * @return 成功标识
	 */
	public boolean lRemove(String key, long start, long end) {
		try {
			redisTemplate.opsForList().trim(key, start, end);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将一个值插入到已存在的列表头部，列表不存在时操作无效。
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean lLeftPush(String key, Object value) {
		try {
			redisTemplate.opsForList().leftPush(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将一个值插入到已存在的列表尾部，列表不存在时操作无效。
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean lRightPush(String key, Object value) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将已存在的列表头部移除一个值，列表不存在时操作无效。
	 *
	 * @param key
	 * @return
	 */
	public Object lLeftPop(String key) {
		try {
			return redisTemplate.opsForList().leftPop(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将已存在的列表尾部移除一个值，列表不存在时操作无效。
	 *
	 * @param key
	 * @return
	 */
	public Object lRightPop(String key) {
		try {
			return redisTemplate.opsForList().rightPop(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//添加如果值一样，增加 Score 若没有直接增加
	public boolean zIncrementScore(String key,Object value,double delta,long time) {
		try {
			Double aDouble = redisTemplate.opsForZSet().incrementScore(key, value, delta);
			if (aDouble!=null&&aDouble>0){
				expire(key, time);
			}
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	//添加如果值一样，增加 Score 若没有直接增加
	public boolean zIncrementScore(String key,Object value,double delta) {
		try {
			redisTemplate.opsForZSet().incrementScore(key,value,delta);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	//通过值去删除
	public boolean zRemove(String key,Object... values) {
		try {
			redisTemplate.opsForZSet().remove(key,values);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	// (0,-1)就是获取全部  从小到大获取
	public Set<Object> zRange(String key,long start, long end) {
		try {
			return redisTemplate.opsForZSet().range(key,start,end);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	// (0,-1)就是获取全部 从大到小获取
	public Set<Object> reverseRange(String key,long start, long end) {
		try {
			return redisTemplate.opsForZSet().reverseRange(key,start,end);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	//获取指定值的索引(从小到大)
	public Long zRank(String key,Object o) {
		try {
			return redisTemplate.opsForZSet().rank(key,o);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	//获取指定值的索引(从大到小)
	public Long zReverseRank(String key,Object o) {
		try {
			return redisTemplate.opsForZSet().reverseRank(key,o);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	//通过索引删除
	public boolean zRemoveRange(String key,long start, long end) {
		try {
			redisTemplate.opsForZSet().removeRange(key, start, end);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	//如果有key返回false 没有返回true
	public Boolean setIfAbsent(String key, String value, long timeout, TimeUnit timeUnit) {
		return redisTemplate.opsForValue().setIfAbsent(key,value,timeout,timeUnit);
	}

	/**
	 * 返回命名空间中所有得key
	 * @param name
	 * @return
	 */
	public Set<String> getKeys(String name){
		return redisTemplate.keys(name);
	}
	public void hsetStoreImage(String key,String item, File imageFile,long time) {
		try (InputStream inputStream = new FileInputStream(imageFile)) {
			byte[] buffer = new byte[(int) imageFile.length()];
			int bytesRead = inputStream.read(buffer);
			if (bytesRead != -1) {
				String base64Image = Base64.encodeBase64String(buffer);
				redisTemplate.opsForHash().put(key, item, base64Image);
				if (time>0){
					expire(key, time);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//通过值获取list是否存在该值
	public boolean containsValueInList(String key, Object value) {
		// 获取List中指定范围的所有元素，这里获取全部元素
		List<Object> elements = redisTemplate.opsForList().range(key, 0, -1);
		if (CollectionUtils.isEmpty(elements)){
			return false;
		}
		// 在本地检查值是否存在
		return elements.contains(value);
	}
}