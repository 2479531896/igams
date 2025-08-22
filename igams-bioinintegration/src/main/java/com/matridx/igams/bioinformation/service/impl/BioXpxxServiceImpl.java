package com.matridx.igams.bioinformation.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.bioinformation.dao.entities.BioXpxxDto;
import com.matridx.igams.bioinformation.dao.entities.BioXpxxModel;
import com.matridx.igams.bioinformation.dao.entities.TimeDto;
import com.matridx.igams.bioinformation.dao.post.IBioXpxxDao;
import com.matridx.igams.bioinformation.service.svcinterface.IBioXpxxService;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BioXpxxServiceImpl extends BaseBasicServiceImpl<BioXpxxDto, BioXpxxModel, IBioXpxxDao> implements IBioXpxxService {

    @Autowired
    IJcsjService jcsjService;
    private final Logger log = LoggerFactory.getLogger(BioXpxxServiceImpl.class);


    /**
     * 查找某测序仪归属省份范围下测序仪的所有芯片数据
     */
    @Override
    public List<BioXpxxDto> getXpxxByCxylist(List<JcsjDto> cxyChild) {
        return dao.getXpxxByCxylist(cxyChild);
    }
    /**
     * 获取生信审核系统芯片列表
     */
    @Override
    public List<BioXpxxDto> getPagedBioChipList(BioXpxxDto bioXpxxDto){
        if("1".equals(bioXpxxDto.getSjqxsx())&&(bioXpxxDto.getSjids()==null)){//无权查看任何时候，直接返回空
            return new ArrayList<>();
        }
        return dao.getPagedBioChipList(bioXpxxDto);
    }
    /**
     * 获取今日芯片列表芯片列表
     */
    @Override
    public List<BioXpxxDto> getDtoTodayChipList(BioXpxxDto bioXpxxDto){
        Calendar yesterdaytime = Calendar.getInstance();
        yesterdaytime.add(Calendar.DATE, -1);
        yesterdaytime.set(Calendar.HOUR_OF_DAY, 16);
        yesterdaytime.set(Calendar.MINUTE, 0);
        yesterdaytime.set(Calendar.SECOND, 0);
        yesterdaytime.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat time= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bioXpxxDto.setKssj(time.format(yesterdaytime.getTime()));
        Calendar todaytime = Calendar.getInstance();
        todaytime.set(Calendar.HOUR_OF_DAY, 16);
        todaytime.set(Calendar.MINUTE, 0);
        todaytime.set(Calendar.SECOND, 0);
        todaytime.set(Calendar.MILLISECOND, 0);
        bioXpxxDto.setJssj(time.format(todaytime.getTime()));
        if("1".equals(bioXpxxDto.getSjqxsx())&&(bioXpxxDto.getSjids()==null)){//无权查看任何时候，直接返回空
            return new ArrayList<>();
        }
        List<BioXpxxDto> bioXpxxDtos1 =dao.getDtoTodayChipList(bioXpxxDto);
        List <BioXpxxDto> bioXpxxDtos =new ArrayList<>(bioXpxxDtos1);
        for (BioXpxxDto dto : bioXpxxDtos) {
            List<TimeDto> sjlist=new ArrayList<>();
            if(StringUtil.isNotBlank(dto.getCxkssj()))
            {
                TimeDto timeDto=new TimeDto();
                timeDto.setName("上机测序");
                timeDto.setDescription(dto.getCxkssj());
                sjlist.add(timeDto);
            }

            if(StringUtil.isNotBlank(dto.getCxjssj()))
            {
                TimeDto timeDto=new TimeDto();
                timeDto.setName("测序完成");
                timeDto.setDescription(dto.getCxjssj());
                sjlist.add(timeDto);
            }
            if(StringUtil.isNotBlank(dto.getFxkssj()))
            {
                TimeDto timeDto=new TimeDto();
                timeDto.setName("开始分析");
                timeDto.setDescription(dto.getFxkssj());
                sjlist.add(timeDto);
            }
            if(StringUtil.isNotBlank(dto.getFxjssj()))
            {
                TimeDto timeDto=new TimeDto();
                timeDto.setName("分析完成");
                timeDto.setDescription(dto.getFxjssj());
                sjlist.add(timeDto);
            }
            dto.setSjlist(sjlist);
        }
        return bioXpxxDtos;
    }

    /**
     * 定时任务更新芯片信息
     */
    public void updateChipInfo() {
        try {
            JcsjDto jcsjDto_t = new JcsjDto();
            jcsjDto_t.setJclb(BasicDataTypeEnum.SEQUENCER_CODE.getCode());
            List<JcsjDto> list = jcsjService.getJcsjDtoList(jcsjDto_t);
            jcsjDto_t.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
            List<JcsjDto> syslist = jcsjService.getJcsjDtoList(jcsjDto_t);
            String token = "eba3f34c24943e1c";//token固定永久有效
            RestTemplate resttemplate = new RestTemplate();
            List<JcsjDto> cxyAddList = new ArrayList<>();//用于存储新增的测序仪基础数据
            List<JcsjDto> cxyModList = new ArrayList<>();//用于存储修改的测序仪基础数据
            //获取生信部测序仪列表
            String getCxyUrl = "http://bcl.matridx.top/BCL/api/machine/";
            List<Map<String, Object>> cxylist = resttemplate.getForObject(getCxyUrl + "?token=" + token, List.class);
            if (cxylist != null && !cxylist.isEmpty()) {
                for (int i = 0; i < cxylist.size(); i++) {
                    if("true".equals(String.valueOf(cxylist.get(i).get("active")))){
                        boolean flag = true;
                        for (JcsjDto dto : list) {
                            if (dto.getCsdm().equals(String.valueOf(cxylist.get(i).get("uid")))&&dto.getCsmc().equals(String.valueOf(cxylist.get(i).get("name")))) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            JcsjDto jcsjDto = new JcsjDto();
                            jcsjDto.setCsid(StringUtil.generateUUID());
                            jcsjDto.setJclb(BasicDataTypeEnum.SEQUENCER_CODE.getCode());
                            jcsjDto.setCsdm(String.valueOf(cxylist.get(i).get("uid")));
                            jcsjDto.setCsmc(String.valueOf(cxylist.get(i).get("name")));
                            jcsjDto.setCspx(String.valueOf(i+1));
                            jcsjDto.setSfmr("0");
                            jcsjDto.setSfgb("0");
                            String organization = String.valueOf(cxylist.get(i).get("organization"));
                            if(StringUtil.isNotBlank(organization)){
                                for(JcsjDto sys:syslist){
                                    if(organization.substring(0,1).equals(sys.getCskz2())){
                                        jcsjDto.setFcsid(sys.getCsid());
                                    }
                                }
                            }
                            cxyAddList.add(jcsjDto);
                            JcsjDto t_jcsjDto=new JcsjDto();
                            t_jcsjDto.setJclb(BasicDataTypeEnum.SEQUENCER_CODE.getCode());
                            t_jcsjDto.setCsdm(String.valueOf(cxylist.get(i).get("uid")));
                            t_jcsjDto.setScbj("2");
                            cxyModList.add(t_jcsjDto);
                        }
                    }
                }
                if (!cxyModList.isEmpty()) {
                    jcsjService.updateListScbj(cxyModList);
                }
                if (!cxyAddList.isEmpty()) {
                    jcsjService.insertList(cxyAddList);
                }
                jcsjDto_t.setJclb(BasicDataTypeEnum.SEQUENCER_CODE.getCode());
                list = jcsjService.getJcsjDtoList(jcsjDto_t);
                //获取生信部测序仪列表
                String getXpUrl = "http://bcl.matridx.top/BCL/core/chip/lifecycle/";
                List<Map<String, Object>> xplist = resttemplate.getForObject(getXpUrl + "?token=" + token, List.class);
                List<Map<String, Object>> wkgllist =new ArrayList<>();
                if (xplist != null && !xplist.isEmpty()) {
                    List<BioXpxxDto> addList = new ArrayList<>();
                    List<BioXpxxDto> modList = new ArrayList<>();
                    List<String> ids = new ArrayList<>();
                    List<BioXpxxDto> bioXpxxDtos = new ArrayList<>();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (Map<String, Object> stringObjectMap : xplist) {
                        Map<String, Object> wkglMap = new HashMap<>();
                        String s = JSON.toJSONString(stringObjectMap);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        JSONObject meta = jsonObject.getJSONObject("meta");
                        JSONObject sequence = jsonObject.getJSONObject("sequence");
                        JSONObject analysis = jsonObject.getJSONObject("analysis");
                        JSONObject bcl2fqc = jsonObject.getJSONObject("bcl2fqc");
                        ids.add(meta.getString("chip"));
                        BioXpxxDto bioXpxxDto = new BioXpxxDto();
                        bioXpxxDto.setXpid(StringUtil.generateUUID());
                        bioXpxxDto.setXpm(meta.getString("chip"));
                        Date date = new Date();
                        if (StringUtil.isNotBlank(sequence.getString("start"))) {
                            date.setTime(new BigDecimal(sequence.getString("start")).longValue() * 1000);
                            bioXpxxDto.setCxkssj(df.format(date));
                        }
                        if (StringUtil.isNotBlank(sequence.getString("finish"))) {
                            date.setTime(new BigDecimal(sequence.getString("finish")).longValue() * 1000);
                            bioXpxxDto.setCxjssj(df.format(date));
                        }
                        bioXpxxDto.setSfsdcx("");
                        bioXpxxDto.setFm(meta.getString("lib_count"));
                        bioXpxxDto.setWksm(meta.getString("lib_count"));
                        bioXpxxDto.setCzry("");
                        bioXpxxDto.setCxmd("");
                        bioXpxxDto.setSftx("");
                        for (JcsjDto dto : list) {
                            if (dto.getCsmc().equals(meta.getString("machine"))) {
                                bioXpxxDto.setCxyid(dto.getCsid());
                                break;
                            }
                        }
                        bioXpxxDto.setTotalcycle(sequence.getString("total_cycle"));
                        bioXpxxDto.setThiscycle(sequence.getString("current_cycle"));
                        bioXpxxDto.setFxjd("");
                        String status = sequence.getString("status");
                        if ("测序中".equals(status)) {
                            bioXpxxDto.setZt("50");
                        } else if ("测序完成".equals(status)) {
                            bioXpxxDto.setZt("51");
                            date.setTime(new BigDecimal(sequence.getString("finish")).longValue() * 1000);
                            wkglMap.put("xjsj", df.format(date));
                            wkglMap.put("xpbh", meta.get("chip"));
                            wkglMap.put("chiptype", sequence.getString("chip_type"));
                            wkgllist.add(wkglMap);
                        } else if ("数据分析中".equals(status)) {
                            bioXpxxDto.setZt("52");
                        } else if ("分析完成".equals(status)) {
                            bioXpxxDto.setZt("53");
                        } else if ("审核完成".equals(status)) {
                            bioXpxxDto.setZt("54");

                        }
                        status = bcl2fqc.getString("status");
                        if ("测序中".equals(status)) {
                            bioXpxxDto.setZt("50");
                        } else if ("测序完成".equals(status)) {
                            bioXpxxDto.setZt("51");
                        } else if ("数据分析中".equals(status)) {
                            bioXpxxDto.setZt("52");
                        } else if ("分析完成".equals(status)) {
                            bioXpxxDto.setZt("53");
                            date.setTime(new BigDecimal(analysis.getString("start")).longValue() * 1000);
                            bioXpxxDto.setFxkssj(df.format(date));
                            if (StringUtil.isNotBlank(analysis.getString("finish"))) {
                                date.setTime(new BigDecimal(analysis.getString("finish")).longValue() * 1000);
                                bioXpxxDto.setFxjssj(df.format(date));
                            }
                        } else if ("审核完成".equals(status)) {
                            bioXpxxDto.setZt("54");
                        }
                        bioXpxxDtos.add(bioXpxxDto);

                    }
                    List<BioXpxxDto> xpxxByXpms = dao.getXpxxByXpms(ids);
                    for (BioXpxxDto bioXpxxDto : bioXpxxDtos) {
                        boolean flag = true;
                        for (BioXpxxDto bioXpxxDto_t : xpxxByXpms) {
                            if (bioXpxxDto.getXpm().equals(bioXpxxDto_t.getXpm())) {
                                flag = false;
                                bioXpxxDto.setXpid(bioXpxxDto_t.getXpid());
                                break;
                            }
                        }
                        if (flag) {
                            addList.add(bioXpxxDto);
                        } else {
                            modList.add(bioXpxxDto);
                        }
                    }
                    if (!addList.isEmpty()) {
                        dao.insertList(addList);
                    }
                    if (!modList.isEmpty()) {
                        dao.updateList(modList);
                    }

                }
                if(!wkgllist.isEmpty()){
                    dao.updateWkgl(wkgllist);
                    dao.updateSjsygl(wkgllist);
                }
            }
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }

    /**
     * 根据芯片名查询芯片ID
     */
    public String getXpidByXpm(String xpm){
        return dao.getXpidByXpm(xpm);
    }

    /**
     * 批量新增
     */
    public boolean insertList(List<BioXpxxDto> list){
        return dao.insertList(list);
    }

    public boolean insertDto(BioXpxxDto bioXpxxDto){
        return dao.insertDto(bioXpxxDto)>0;
    }
}
