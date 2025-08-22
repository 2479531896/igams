package com.matridx.las.netty.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.enums.YesNotEnum;
import com.matridx.igams.common.redis.RedisStreamUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;


import com.matridx.las.netty.dao.entities.JkywzszDto;
import com.matridx.las.netty.dao.entities.JkywzszModel;
import com.matridx.las.netty.dao.entities.JqrcsszDto;
import com.matridx.las.netty.dao.post.IJkywzszDao;
import com.matridx.las.netty.dao.post.IJqrcsszDao;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.global.CubsParmGlobal;
import com.matridx.las.netty.service.svcinterface.IJkywzszService;
import com.matridx.las.netty.util.SendMessgeToHtml;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JkywzszServiceImpl extends BaseBasicServiceImpl<JkywzszDto, JkywzszModel, IJkywzszDao> implements IJkywzszService {

    @Autowired
    private IJqrcsszDao jqrcsszDao;
    @Autowired
    private RedisStreamUtil streamUtil;
    @Override
    public JkywzszDto queryById(String jkytdh) {
        return dao.queryById(jkytdh);
    }

    @Override
    public boolean saveJkywzsz(JqrcsszDto dto) {
        return jqrcsszDao.saveJkywzsz(dto)>0;
    }

    @Override
    public boolean updateByWzszid(JqrcsszDto dto) {
        return jqrcsszDao.updateByWzszid(dto)>0;
    }

    @Override
    public List<JqrcsszDto> getList() {
        return jqrcsszDao.getList();
    }

    @Override
    public JqrcsszDto getByTdh(JqrcsszDto dto) {
        return jqrcsszDao.getByTdh(dto);
    }
    private static final Logger logger = LoggerFactory.getLogger(SendMessgeToHtml.class);

    /**
     * 保存前端建库仪设置功能
     * @param jkywzszDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> saveChannelSetup(JkywzszDto jkywzszDto) {
        Map<String,Object> map = new HashMap<>();
        try {
            //查询下是否已配置过，如果没配置过，且状态是在线状态，则需要加入空闲队列
            if(StringUtil.isNotBlank(jkywzszDto.getJkytdh())){
                JkywzszDto jkywzszDto1 =  dao.queryById(jkywzszDto.getJkytdh());
                if(jkywzszDto1!=null){
                    //查看，是否是正常状态
                    List<MapRecord<String, Object, Object>> list1= streamUtil.range("CubisGroup");
                    List<String> list2 = new ArrayList<>();
                    if(list1!=null&&list1.size()>0){
                        for(MapRecord<String,Object,Object> m:list1){
                            Map<Object,Object> map1 = m.getValue();
                            for(Object o:map1.keySet()){
                                if(o!=null){
                                    list2.add(o.toString());
                                    break;
                                }
                            }
                        }
                    }
                    Map<String ,String> mapC = CubsParmGlobal.getHavaUpCubis();
                    if(mapC!=null&&mapC.keySet().size()>0){
                        String zt = mapC.get(jkywzszDto.getJkytdh());
                        if(InstrumentStatusEnum.STATE_FREE.getCode().equals(zt)
                                &&!list2.contains(jkywzszDto.getJkytdh())){
                            //放入队列
                            CubsParmGlobal.putCubQueues(jkywzszDto.getJkytdh());
                        }
                    }
                }

            }
           int num =  dao.updateTdhById(jkywzszDto);

           if(num==0){
               map.put("message", "保存失败");
               map.put("status", "fail");
               return  map;
           }
            map.put("jkytd",getJkytdAndState());
            map.put("message", "操作成功");
            map.put("status", "success");
        }catch (Exception e){
            map.put("message", "保存失败");
            map.put("status", "fail");
        }
        logger.info(map.toString());
        return map;
    }
    @Override
    public  JSONArray getJkytdAndState(){
        //给前端推送得通道号加上状态，是否已经本设置
        List<String> list = dao.getTdhList(new JkywzszDto());
        if(list==null){
            list = new ArrayList<>();
        }

        Map<String ,String> mapC = CubsParmGlobal.getHavaUpCubis();
        JSONArray jsonArray = new JSONArray();
        if(mapC!=null&&mapC.keySet().size()>0){
            for(String k:mapC.keySet()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("jkytdh",k);
                jsonObject.put("zt",mapC.get(k));
                if(list.contains(k)){
                    jsonObject.put("sfsz", YesNotEnum.YES.getCode());
                }else{
                    jsonObject.put("sfsz", YesNotEnum.YES.getCode());
                }
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }
  public  void delJkywz(String deviceid){
       dao.delJkywzsz(deviceid);
  }
    public  void delJkywzAll(){
        dao.delJkywzszAll();
    }

}
