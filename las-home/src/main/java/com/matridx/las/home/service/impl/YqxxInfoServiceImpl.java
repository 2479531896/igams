package com.matridx.las.home.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.redis.RedisStreamUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.las.home.dao.entities.YqxxInfoDto;
import com.matridx.las.home.dao.entities.YqxxInfoModel;

import com.matridx.las.home.dao.post.IYqxxInfoDao;
import com.matridx.las.home.service.svcinterface.IYqxxInfoService;
import com.matridx.las.netty.dao.entities.CubsMaterialModel;
import com.matridx.las.netty.dao.entities.JkywzszDto;
import com.matridx.las.netty.dao.entities.YqxxinfosxDto;
import com.matridx.las.netty.dao.post.IJkywzszDao;
import com.matridx.las.netty.dao.post.IYqxxinfosxDao;
import com.matridx.las.netty.enums.GlobalParmEnum;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.enums.MaterialTypeEnum;
import com.matridx.las.netty.global.CubsParmGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.service.svcinterface.IJkywzszService;
import com.matridx.las.netty.service.svcinterface.IYqxxinfosxService;
import com.matridx.las.netty.util.SendMessgeToHtml;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YqxxInfoServiceImpl extends BaseBasicServiceImpl<YqxxInfoDto, YqxxInfoModel, IYqxxInfoDao> implements IYqxxInfoService {

    @Autowired
    private IJkywzszDao jkywzszDao;
    @Value("${matridx.sysid:}")
    private  String sysid;
    @Autowired
    private IJkywzszService jkywzszService;
    private static final Logger logger = LoggerFactory.getLogger(SendMessgeToHtml.class);
    @Autowired
    private  RedisStreamUtil streamUtil;
    @Autowired
    private IYqxxinfosxService yqxxinfosxService;
    @Autowired
    private IYqxxinfosxDao yqxxinfosxDao;
    @Override
    @Transactional
    public boolean insertYqxxInfo(List<YqxxInfoDto> list, String userid) {
        List<String> deviceidList = new ArrayList<>();
        List<JkywzszDto> upJkList = new ArrayList<>();
        List<JkywzszDto> inJkList = new ArrayList<>();
        List<YqxxinfosxDto> inSxist = new ArrayList<>();

        List<YqxxinfosxDto> upSxist = new ArrayList<>();

        Map<String,String> cubsMap =  CubsParmGlobal.getHavaUpCubis();
        if(cubsMap==null){
            cubsMap = new HashMap<>();
        }
        for (YqxxInfoDto dto : list) {
            dto.setCommanddeviceid(dto.getLx()+dto.getDeviceid());
            if (StringUtil.isNotBlank(dto.getFqyid())) {
                dto.setFqyid(dto.getFqyid().replace("-", ""));
            }
            if (StringUtil.isNotBlank(dto.getFqy())) {
                dto.setFqy(dto.getFqy().replace("-", ""));
            }
            dto.setYqxxid(StringUtil.generateUUID());
            if (MaterialTypeEnum.MATERIAL_CUBICS.getCode().equals(dto.getLx())) {
                deviceidList.add(dto.getDeviceid());
            }
            dto.setSysid(sysid);
            //添加建库仪位置设置
            List<JkywzszDto> jkywzszDtoList = dto.getJkywzszDtoList();
            if(jkywzszDtoList!=null&&jkywzszDtoList.size()>0){
                for(JkywzszDto jk:jkywzszDtoList){
                   if(jk!=null){
                       jk.setDeviceid(dto.getDeviceid());
                       if(StringUtil.isNotBlank(jk.getWzszid())){
                           jk.setScbj("0");
                           upJkList.add(jk);
                       }else{
                           jk.setWzszid(StringUtil.generateUUID());
                           inJkList.add(jk);
                       }
                       //检查是否上传
                        if(StringUtil.isNotBlank(jk.getJkytdh())){
                            String state = cubsMap.get(jk.getJkytdh());
                            List<MapRecord<String, Object, Object>> list1= streamUtil.range("CubisGroup");
                            List<String> list2 = new ArrayList<>();
                            if(list1!=null&&list1.size()>0){
                                for(MapRecord<String,Object,Object> m:list1){
                                    Map<Object,Object> map = m.getValue();
                                    for(Object o:map.keySet()){
                                        if(o!=null){
                                            list2.add(o.toString());
                                            break;
                                        }
                                    }
                                }
                            }

                            if((InstrumentStatusEnum.STATE_FREE.getCode().equals(state))
                                    &&!list2.contains(jk.getJkytdh())){
                                //如果没有初始化到redis里，就不能加入队列
                                CubsMaterialModel cubsMaterialModel = new CubsMaterialModel();
                                cubsMaterialModel.setPassId(jk.getJkytdh());
                                CubsMaterialModel cubsMaterialModel1 = InstrumentMaterialGlobal.getCubsMaterial(cubsMaterialModel);
                              if(cubsMaterialModel1!=null) {
                                  cubsMaterialModel1.setState(InstrumentStatusEnum.STATE_FREE.getCode());
                                  //加入空闲队列
                                  streamUtil.add("CubisGroup", jk.getJkytdh(), jk.getJkytdh());
                                  //修改状态
                                  InstrumentMaterialGlobal.setCubsMaterial(cubsMaterialModel1, false);

                              }

                                //CubsParmGlobal.putCubQueues(jk.getJkytdh());
                            }
                        }
                   }

                }
            }
            //属性保存
            List<YqxxinfosxDto> yqxxinfosxDtoList = dto.getYqxxinfosxDtos();
            if(yqxxinfosxDtoList!=null&&yqxxinfosxDtoList.size()>0){
                for(YqxxinfosxDto yqxxinfosxDto:yqxxinfosxDtoList){
                    yqxxinfosxDto.setYqxxid(dto.getYqxxid());
                    //没写类型默认不添加
                    if(StringUtil.isBlank(yqxxinfosxDto.getSxlx())){
                        continue;
                    }
                    if(StringUtil.isNotBlank(yqxxinfosxDto.getSxid())){
                        yqxxinfosxDto.setYqxxid(dto.getYqxxid());
                        upSxist.add(yqxxinfosxDto);
                    }else{
                        yqxxinfosxDto.setSxid(StringUtil.generateUUID());
                        inSxist.add(yqxxinfosxDto);
                    }
                }
            }

        }
        //保存属性
        if(upSxist.size()>0){
            yqxxinfosxDao.updateList(upSxist);
        }
        if(inSxist.size()>0){
            yqxxinfosxDao.insertList(inSxist);
        }
        //跟新建库仪设置
        if(inJkList.size()>0){
            jkywzszDao.insertList(inJkList);
        }
        jkywzszDao.updateList(upJkList);

        /*//查询建库仪位置信息，并实时更新
        JkywzszDto jkywzszDto = new JkywzszDto();
        List<JkywzszDto> listJkywz = jkywzszDao.getDtoList(jkywzszDto);
        //要删除的建库仪通道
        List<String> listJkywzRm = new ArrayList<>();
        //不需要新增的建库仪
        List<String> listJkywzNe = new ArrayList<>();
        //现有的通道的位置
        List<String> listWz = new ArrayList<>();
        if (listJkywz != null && listJkywz.size() > 0) {
            for (JkywzszDto dto : listJkywz) {
                listWz.add(dto.getJkywzbh());
                //不包括则，删除建库仪通道设置
                if (!deviceidList.contains(dto.getDeviceid()) && !listJkywzRm.contains(dto.getDeviceid())) {
                    int result = jkywzszDao.updateDto(dto);
                    //删除完成后，将
                    if (result > 0) listJkywzRm.add(dto.getDeviceid());
                } else if (deviceidList.contains(dto.getDeviceid())) {
                    //加入不需要新增的建库仪
                    listJkywzNe.add(dto.getDeviceid());

                }
                //删除已被删除的位置编号
                if (listJkywzRm.contains(dto.getDeviceid())) {
                    listWz.remove(dto.getJkywzbh());
                }
            }
        }
        //新建建库仪通道记录
        int bh = 1;
        if (deviceidList.size() > 0) {
            List<JkywzszDto> listNew = new ArrayList<>();
            for (String di : deviceidList) {
                if (listJkywzNe.contains(di)) {
                    continue;
                }
                //一个建库仪四个通道
                int num = Integer.parseInt(GlobalParmEnum.CUBIS_CHANNEL_NUM.getCode());
                for (int i = 1; i <= num; i++) {
                    while (listWz.contains(bh + "")) {
                        bh++;
                    }
                    JkywzszDto jkywzszDto1 = new JkywzszDto();
                    jkywzszDto1.setDeviceid(di);
                    jkywzszDto1.setWzszid(StringUtil.generateUUID());
                    jkywzszDto1.setJkywzbh(bh + "");
                    jkywzszDto1.setScbj("0");
                    jkywzszDto1.setLrry(userid);
                    listNew.add(jkywzszDto1);
                    bh++;

                }
            }
            if (listNew.size() > 0) {
                jkywzszDao.insertList(listNew);
            }


        }*/

        return dao.insertYqxxInfo(list) > 0;
    }

    /**
     *
     * @param dto
     * @return
     */
    @Override
    public List<YqxxInfoDto> getYqxxList(YqxxInfoDto dto) {
        List<YqxxInfoDto> list = dao.getYqxxList(dto);
        if(list!=null&&list.size()>0){
            for(YqxxInfoDto yq:list){
                //放入建库仪通道
                if(MaterialTypeEnum.MATERIAL_CUBICS.getCode().equals(yq.getLx())){
                    JkywzszDto jkywzszDto = new JkywzszDto();
                    if(StringUtil.isNotBlank(yq.getDeviceid())){
                        jkywzszDto.setDeviceid(yq.getDeviceid());
                    }else{
                        jkywzszDto.setDeviceid("-1");
                    }
                    List<JkywzszDto> jkywzszDtoList = jkywzszDao.getDtoList(jkywzszDto);
                    yq.setJkywzszDtoList(jkywzszDtoList);
                }
                //放入属性信息
                if(StringUtil.isNotBlank(yq.getYqxxid())) {
                    YqxxinfosxDto yqxxinfosxDto = new YqxxinfosxDto();
                    yqxxinfosxDto.setYqxxid(yq.getYqxxid());
                    List<YqxxinfosxDto> yqxxinfosxDtoList = yqxxinfosxService.getDtoList(yqxxinfosxDto);
                    yq.setYqxxinfosxDtos(yqxxinfosxDtoList);
                }
            }
        }
        return list;
    }
    /**
     * 获取组件信息
     * @param dto
     * @return
     */
    @Override
    public List<YqxxInfoDto> getYqxxZjList(YqxxInfoDto dto) {
        return null;
    }

    @Override
    public YqxxInfoDto getDtoById(YqxxInfoDto dto) {
        return dao.getDtoById(dto);
    }

    @Override
    @Transactional
    public boolean updateBySysId(YqxxInfoDto dto) {
        return dao.updateBySysId(dto) > 0;
    }

    @Override
    @Transactional
    public boolean updateById(YqxxInfoDto dto) {
        return dao.updateById(dto) > 0;
    }

    /**
     * 建库仪通道号
     *
     * @return
     */
    @Override
    public Map<String, Object> getJkywzxxList() {
        Map<String, Object> mapResult = new HashMap<>();
        JkywzszDto jkywzszDto = new JkywzszDto();
        List<JkywzszDto> list = jkywzszDao.getDtoList(jkywzszDto);
        JSONArray jsonArray = new JSONArray();
        //将查出来的数据封装成想要的格式
        if (list != null && list.size() > 0) {
            Map<String, List<JkywzszDto>> map = new HashMap<>();
            for (JkywzszDto jk : list) {
                List<JkywzszDto> list1 = map.get(jk.getDeviceid());
                if (list1 != null && list1.size() > 0) {
                    list1.add(jk);
                } else {
                    list1 = new ArrayList<>();
                    list1.add(jk);
                    map.put(jk.getDeviceid(), list1);
                }
            }
            for (String k : map.keySet()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceid", k);
                jsonObject.put("list", map.get(k));
                jsonArray.add(jsonObject);
            }
            //上传已上报的建库仪通道号
            Map<String,String> mapcus = CubsParmGlobal.getHavaUpCubis();
            JSONObject object = new JSONObject();
            JSONArray jsonArray1 = jkywzszService.getJkytdAndState();
            mapResult.put("jkytd",jsonArray1);
            mapResult.put("cubics", jsonArray);
            mapResult.put("message", "操作成功");
            mapResult.put("status", "success");
        } else {
            mapResult.put("message", "未找到建库仪信息");
            mapResult.put("status", "fail");
        }
        logger.info(mapResult.toString());
        return mapResult;
    }

}
