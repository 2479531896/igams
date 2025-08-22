package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.wechat.dao.entities.SjpdglDto;
import com.matridx.igams.wechat.dao.entities.SjtdxxDto;
import com.matridx.igams.wechat.dao.entities.SjtdxxModel;
import com.matridx.igams.wechat.dao.entities.SjwlxxDto;
import com.matridx.igams.wechat.dao.post.ISjtdxxDao;
import com.matridx.igams.wechat.dao.post.ISjwlxxDao;
import com.matridx.igams.wechat.service.svcinterface.ISjpdglService;
import com.matridx.igams.wechat.service.svcinterface.ISjtdxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class SjtdxxServiceImpl extends BaseBasicServiceImpl<SjtdxxDto, SjtdxxModel, ISjtdxxDao> implements ISjtdxxService {
    @Autowired
    ISjwlxxDao sjwlxxDao;
    @Autowired
    ISjpdglService sjpdglService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    ICommonService commonService;
    @Value("${matridx.wechat.menuurl:}")
    private String menuurl;
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveTdxx(SjwlxxDto sjwlxxDto) throws BusinessException {
//        List<JcsjDto> jsfsList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SENDING_METHOD.getCode());//寄送方式合并快递类型
        List<JcsjDto> jsfsList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode());
        for(JcsjDto dto:jsfsList){
            if("QYY".equals(dto.getCsdm())){
                sjwlxxDto.setJsfs(dto.getCsid());
                sjwlxxDto.setJsfsmc(dto.getCsmc());
            }
        }
        List<String> wlids = StringUtil.isNotBlank(sjwlxxDto.getWlids_str())? Arrays.asList(sjwlxxDto.getWlids_str().split(",")):new ArrayList<>();
        wlids = new ArrayList<>(wlids);
        List<SjwlxxDto> wlxxList = sjwlxxDao.getDtoListById(sjwlxxDto.getSjtdid());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(wlxxList.get(0).getSjwlid());
        List<String> ids = new ArrayList<>();
        for (SjwlxxDto dto : wlxxList) {
            ids.add(dto.getWlid());
        }
        List<String> lswlids = new ArrayList<>();
        lswlids.addAll(wlids);
        //剩下的是新增的
        Iterator<String> wlidsiter = wlids.iterator();
        while (wlidsiter.hasNext()){
            if (ids.contains(wlidsiter.next())){
                wlidsiter.remove();
            }
        }
        if (!CollectionUtils.isEmpty(wlids)){
            List<SjwlxxDto> sjwlxxDtos = sjwlxxDao.getDtoListByIds(wlids);
            for (SjwlxxDto dto : sjwlxxDtos) {
                //保存附件信息
                if (!CollectionUtils.isEmpty(fjcfbDtos)) {
                    String fjids  = "";
                    for (FjcfbDto fjcfbDto : fjcfbDtos) {
                        fjids = fjids + "," +fjcfbDto.getFjid();
                    }
                    fjids = fjids.substring(1);
                    MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                    paramMap.add("fjids",fjids);
                    paramMap.add("ywid", dto.getWlid());
                    RestTemplate restTemplate = new RestTemplate();
                    String param = restTemplate.postForObject(menuurl + "/wechat/getFileAddress", paramMap, String.class);
                    if (param != null) {
                        JSONArray parseArray = JSONObject.parseArray(param);
                        for (int i = 0; i < parseArray.size(); i++) {
                            FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
                            fjcfbModel.setYwid(dto.getWlid());
                            fjcfbModel.setFjid(StringUtil.generateUUID());
                            // 下载服务器文件到指定文件夹
                            commonService.downloadFile(fjcfbModel);
                            fjcfbService.insert(fjcfbModel);
                        }
                    }
                }
                //将原先的物流状态改为40
                SjwlxxDto sjwlxxDto_t=new SjwlxxDto();
                sjwlxxDto_t.setWlid(dto.getWlid());
                sjwlxxDto_t.setWlzt("40");
                sjwlxxDto_t.setJsfs(sjwlxxDto.getJsfs());
                sjwlxxDto_t.setXgry(sjwlxxDto.getXgry());
                sjwlxxDto_t.setSdsj(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                sjwlxxDto_t.setSdbz(wlxxList.get(0).getSdbz());
                int update = sjwlxxDao.update(sjwlxxDto_t);
                if(update<=0){
                    throw new BusinessException("msg","更新送检物流信息失败！");
                }
                SjpdglDto high=new SjpdglDto();
                high.setSjpdid(dto.getSjpdid());
                high.setLsjl(wlxxList.get(0).getLsjl());
                boolean uppd = sjpdglService.update(high);
                if (!uppd){
                    throw new BusinessException("msg","新增送检派单信息失败！");
                }
                //先新增一条新的物流信息
                sjwlxxDto.setSjwlid(dto.getWlid());
                sjwlxxDto.setWlid(StringUtil.generateUUID());
                sjwlxxDto.setWlzt("10");
                sjwlxxDto.setSdbz(null);
                sjwlxxDto.setSjpdid(dto.getSjpdid());
                sjwlxxDto.setGldh(wlxxList.get(0).getGldh());
                sjwlxxDto.setDddd(wlxxList.get(0).getDddd());
                sjwlxxDto.setBc(wlxxList.get(0).getBc());
                sjwlxxDto.setYjddsj(wlxxList.get(0).getYjddsj());
                sjwlxxDto.setLrry(sjwlxxDto.getXgry());
                int insert=sjwlxxDao.insert(sjwlxxDto);
                if(insert<=0){
                    throw new BusinessException("msg","新增送检物流信息失败！");
                }
            }
        }
        //剩下的是删除的
        Iterator<SjwlxxDto> wlxxiter = wlxxList.iterator();
        while (wlxxiter.hasNext()){
            if (lswlids.contains(wlxxiter.next().getWlid())){
                wlxxiter.remove();
            }
        }
        if (!CollectionUtils.isEmpty(wlxxList)){
            for (JcsjDto dto : jsfsList) {
                if ("GT".equals(dto.getCsdm())) {
                    sjwlxxDto.setJsfs(dto.getCsid());
                    sjwlxxDto.setJsfsmc(dto.getCsmc());
                }
            }
            for (SjwlxxDto dto : wlxxList) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
                stringBuffer.append("\n");
                stringBuffer.append("您的快递已送达至高铁站,班次为");
                stringBuffer.append(dto.getBc());
                stringBuffer.append("目的地为");
                stringBuffer.append(dto.getDddd());
                if (dto.getYjddsj() == null || dto.getYjddsj() == "") {
                    stringBuffer.append(",预计");
                    stringBuffer.append(dto.getYjddsj());
                    stringBuffer.append("到达");
                }
                if (StringUtil.isNotBlank(dto.getSdbz())) {
                    stringBuffer.append("\n");
                    stringBuffer.append("备注：");
                    stringBuffer.append(dto.getSdbz());
                }
                //删除后将原先的物流状态改为30
                SjwlxxDto sjwlxxDto_t=new SjwlxxDto();
                sjwlxxDto_t.setWlid(dto.getSjwlid());
                sjwlxxDto_t.setWlzt("30");
                sjwlxxDto_t.setXgry(sjwlxxDto.getXgry());
                sjwlxxDto_t.setJsfs(sjwlxxDto.getJsfs());
                sjwlxxDto_t.setSdsj(null);
                sjwlxxDto_t.setSdbz(null);
                int update = sjwlxxDao.updateWlxx(sjwlxxDto_t);
                if(update<=0){
                    throw new BusinessException("msg","更新送检物流信息失败！");
                }
                SjpdglDto high=new SjpdglDto();
                high.setSjpdid(dto.getSjpdid());
                high.setLsjl(stringBuffer.toString() + "\n");
                int uppd = sjpdglService.updateSjpdxx(high);
                if (uppd<1){
                    throw new BusinessException("msg","更新送检派单信息失败！");
                }
                //先删除 新增的一条新的物流信息
                SjwlxxDto sjwlxxDto_d = new SjwlxxDto();
                sjwlxxDto_d.setWlid(dto.getWlid());
                sjwlxxDto_d.setScry(sjwlxxDto.getXgry());
                int delete=sjwlxxDao.delete(sjwlxxDto_d);
                if(delete<=0){
                    throw new BusinessException("msg","更新送检物流信息失败！");
                }
                FjcfbModel fjcfbModel = new FjcfbModel();
                fjcfbModel.setYwid(dto.getSjwlid());
                fjcfbService.deleteByYwid(fjcfbModel);
            }
        }
        return true;
    }
}
