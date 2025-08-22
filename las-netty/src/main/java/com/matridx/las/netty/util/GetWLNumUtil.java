package com.matridx.las.netty.util;


import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.redis.RedisStreamUtil;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.las.netty.dao.entities.AutoMaterialModel;
import com.matridx.las.netty.dao.entities.ChmMaterialModel;
import com.matridx.las.netty.dao.entities.EpVehicleModel;
import com.matridx.las.netty.dao.entities.OctalianpipeListModel;
import com.matridx.springboot.util.base.StringUtil;
import com.sf.csim.express.service.HttpClientUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetWLNumUtil {
    @Autowired
    private RedisSetAndGetUtil redisSetAndGetUtil;
    @Autowired
    private ChangeEpUtil changeEpUtil;
    @Autowired
    private RedisStreamUtil redisStreamUtil;
    private static  RedisSetAndGetUtil setAndGetUtil;
    private static RedisStreamUtil streamUtil;
    private static  ChangeEpUtil chEpUtil;
    @Autowired
    private  IDdxxglService DdxxglService;
    private static IDdxxglService ddxxglService;
    @PostConstruct
    public void init(){
        setAndGetUtil = redisSetAndGetUtil;
        chEpUtil=changeEpUtil;
        streamUtil=redisStreamUtil;
        ddxxglService=DdxxglService;
    }
    public static Map<String,Object> getWlNum(String wlType,String _key){
        Map<String,Object> result=new HashMap<>();
        switch (wlType){
            case "startAuto":
                //获取空闲配置仪得
                List<MapRecord<String, Object, Object>> listjq= streamUtil.range("AutoGroup");
                MapRecord<String, Object, Object> list_0=listjq.get(0);
                Map<Object,Object>valueMap=list_0.getValue();
                String key="";
                for (Object obj : valueMap.keySet()) {
                     key = String.valueOf(obj);
                     break;
                }
                //判断文库ep管载具上是否有样本
                EpVehicleModel wkEp=setAndGetUtil.getWzEpMap("ep3");
                if(!(wkEp!=null&&wkEp.getEpList()!=null&&wkEp.getEpList().size()>0)){
                    result.put("state",false);
                    result.put("msg","请检查文库样本是否存在");
                    return result;
                }
                //文库样本得数量
                int wkEpSize=wkEp.getEpList().size();
                Object ob = setAndGetUtil.getInstrument("auto", key);
                if (ob != null) {
                    //配置仪中的八连管数量
                    AutoMaterialModel autoMaterial = JSONObject.parseObject(ob.toString(), AutoMaterialModel.class);
                    if(autoMaterial.getOctalianpipeList()==null){
                        result.put("state",false);
                        result.put("msg","请在配置仪中添加八连管载具");
                        return result;
                    }
                    //第一次是 (n%8)+1  第二次是固定得3个
                    if(((wkEpSize%8)+4)<autoMaterial.getOctalianpipeList().getNum()){
                        result.put("state",false);
                        result.put("msg","请补充配置仪中八连管物料");
                        return result;
                    }
                    //后物料区八连管载具中八连管数量
                    Object hwlob = setAndGetUtil.getInstrument("auto_desk", "auto_octalianpipe1");
                    if (hwlob == null) {
                        result.put("state",false);
                        result.put("msg","请补充物料区八连管载具");
                        return result;
                    }
                    OctalianpipeListModel octalianpipeList = JSONObject.parseObject(ob.toString(), OctalianpipeListModel.class);
                    if(octalianpipeList==null){
                        result.put("state",false);
                        result.put("msg","请补充物料区八连管载具内八连管");
                        return result;
                    }
                    if (octalianpipeList.getNum()<6) {
                        result.put("state",false);
                        result.put("msg","请补满物料区八连管载具内八连管");
                        return result;
                    }
                }

                break;

            case "startCmh":
                //获取机器人身上八连管载具
                Object deskob = setAndGetUtil.getInstrument("agv", "agv_octalianpipe");
                if (deskob == null) {
                    result.put("state",false);
                    result.put("msg","请检查机器人身上八连管载具");
                    return result;
                }
                //ep管载中ep管数量判断
                OctalianpipeListModel deskOct=JSONObject.parseObject(deskob.toString(), OctalianpipeListModel.class);
                if(deskOct==null){
                    result.put("state",false);
                    result.put("msg","请检查机器人身上八连管载具");
                    return result;
                }

                //获取压盖机
                Object chmob = setAndGetUtil.getInstrument("chm", _key);
                if(chmob==null){
                    result.put("state",false);
                    result.put("msg","请检查机器人身上八连管载具");
                    return result;
                }
                ChmMaterialModel chmgMaterialModel = JSONObject.parseObject(chmob.toString(), ChmMaterialModel.class);
                if(chmgMaterialModel==null){
                    result.put("state",false);
                    result.put("msg","请检查压盖机是否正确");
                    return result;
                }
                //机器人身上得八连管》压盖机得八连管盖得时候
                if(deskOct.getOctNum()>chmgMaterialModel.getOctgNum()){
                    result.put("state",false);
                    result.put("msg","请检查压盖机八连管盖是否充足");
                    return result;
                }
                break;
        }
        result.put("state",true);
        return result;
    }

    /**
     *发送钉钉消息
     * @return
     */
    public static Map<String,Object> sendMessage(String msg){
        Map<String,Object> resmap = new HashMap<>();
        String xxlx= DingMessageType.SYSTS_TYPE.getCode()+"";
        //对外发送钉钉消息接口
        String url="http://dev.matridx.com/ws/pathogen/sendExternalMessageNew";
        //根据消息类型获取人员
        List<DdxxglDto> ddxxgldtolist = ddxxglService.selectByDdxxlx(xxlx);
        Map<String,Object> map=new HashMap<>();
        String ids="";
        if(CollectionUtils.isNotEmpty(ddxxgldtolist)) {
            //获取title
            String XX_HEAD = ddxxgldtolist.get(0).getDdxxmc();
            //这里是遍历用户
            for(DdxxglDto ddxxglDto :ddxxgldtolist) {
                //yhm为工号 拼接ids
                if(StringUtil.isNotBlank(ddxxglDto.getYhm())) {
                    if(StringUtil.isNotBlank(ids)){
                        ids+=ddxxglDto.getYhm();
                    }else{
                        ids+=","+ddxxglDto.getYhm();
                    }
                }
            }
            map.put("ids",ids);
            map.put("title",XX_HEAD);
            map.put("message",msg);
        }
		HttpClientUtil httpClientUtil=new HttpClientUtil();
		//返回消息 errorCode信息说明 status请求状态
		String result= httpClientUtil.post(url+"?ids="+map.get("ids")+"&title="+map.get("title")+"&message="+map.get("message"),"","");
		JSONObject jsonObject=JSONObject.parseObject(result);
		resmap.put("status", jsonObject.get("status").toString());
		resmap.put("errorCode", jsonObject.get("errorCode").toString());
        return resmap;
    }

}
