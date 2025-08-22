package com.matridx.igams.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 该组件是对redis stream命令的一些实现，可单独使用
 */
@Component
public class RedisStreamUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 创建消费组
     * @param key stream
     * @param group 消费组
     */
    public String creartGroup(String key, String group){
        return redisTemplate.opsForStream().createGroup(key, group);
    }



    /**
     * 消费组信息
     * @param key Stream
     * @param group 消费组
     */
    public StreamInfo.XInfoConsumers consumers(String key, String group){
        return redisTemplate.opsForStream().consumers(key, group);
    }

    /**
     * 消费组信息
     * @param key Stream
     */
    public StreamInfo.XInfoGroups groups(String key){
        return redisTemplate.opsForStream().groups(key);
    }



    /**
     * 确认已消费
     */
    public Long ack(String key, String group, String... recordIds){
        return redisTemplate.opsForStream().acknowledge(key, group, recordIds);
    }

    /**
     * 追加消息
     */
    public String add(String key, String field, Object value){
        Map<String, Object> content = new HashMap<>(1);
        content.put(field, value);
        return add(key, content);
    }

    public String add(String key, Map<String, Object> content){
        return redisTemplate.opsForStream().add(key, content).getValue();
    }

    /**
     * 返回待确认信息
     */
    public PendingMessagesSummary pendding(String key,String group){
       return redisTemplate.opsForStream().pending(key,group);
    }
    /**
     * 删除消息，这里的删除仅仅是设置了标志位，不影响消息总长度
     * 消息存储在stream的节点下，删除时仅对消息做删除标记，当一个节点下的所有条目都被标记为删除时，销毁节点
     */
    public Long del(String key, String... recordIds){
        return redisTemplate.opsForStream().delete(key, recordIds);
    }

    public Boolean delGroup(String key, String groupId){
        return redisTemplate.opsForStream().destroyGroup(key, groupId);
    }

    /**
     * 消息长度
     */
    public Long len(String key){
        return redisTemplate.opsForStream().size(key);
    }

    /**
     * 从开始读
     */
    public List<MapRecord<String, Object, Object>> read(String key){
        return redisTemplate.opsForStream().read(StreamOffset.fromStart(key));
    }

    /**
     * 从指定的ID开始读
     */
    public List<MapRecord<String, Object, Object>> read(String key, String recordId){
        return redisTemplate.opsForStream().read(StreamOffset.from(MapRecord.create(key, new HashMap<>(1)).withId(RecordId.of(recordId))));
    }

    public List<MapRecord<String, Object, Object>> range(String key){
        Range<String> range=Range.unbounded();
        return redisTemplate.opsForStream().range(key,range);
    }

    /**
     * 消费者读取
     */
    public List<MapRecord<String, Object, Object>>  read(Consumer consumer, StreamReadOptions readOptions, String key){
        StreamOffset<String> offset = StreamOffset.create(key,ReadOffset.lastConsumed());
        return redisTemplate.opsForStream().read(consumer,readOptions,offset);
    }

    /**
     * 根据流的key获取所有数据
     */
    public List<MapRecord<String, Object, Object>> readFromStreamKey(String key){
        StreamOffset<String> offset = StreamOffset.create(key,ReadOffset.lastConsumed());
        return redisTemplate.opsForStream().read(offset);
    }
}
