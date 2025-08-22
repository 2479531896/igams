package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.ZjglDto;
import com.matridx.igams.common.dao.entities.ZjglModel;
import com.matridx.igams.common.dao.post.IZjglDao;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IZjglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ZjglServiceImpl extends BaseBasicServiceImpl<ZjglDto, ZjglModel, IZjglDao> implements IZjglService{
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IFjcfbService fjcfbService;
    @Value("${matridx.fileupload.prefix}")
    private String prefix;

    @Value("${matridx.fileupload.tempPath}")
    private String tempFilePath;

    @Value("${matridx.fileupload.releasePath}")
    private String releaseFilePath;
    /**
     *  获取所有组件
     */
    @Override
    public List<ZjglDto> getAllComponent() {
       Object allComponentObj =redisUtil.get(RedisCommonKeyEnum.HOME_PAGE_ALL_COMPONENT.getKey());
        if (allComponentObj!=null)
            return JSON.parseArray(String.valueOf(allComponentObj),ZjglDto.class);
        List<ZjglDto> zjglDtos = dao.getAllComponent();
        redisUtil.set(RedisCommonKeyEnum.HOME_PAGE_ALL_COMPONENT.getKey(),JSON.toJSONString(zjglDtos));
        return zjglDtos;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String,Object> insertTool(ZjglDto zjglDto) {
        Map<String,Object> resultMap = new HashMap<>();
        boolean result = false;
        List<ZjglDto> zjglDtoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(zjglDto.getIds())) {
            result = insertToolFile(zjglDto,zjglDtoList);
        }else {
            String xh = dao.generateZjid(zjglDto);
            zjglDto.setZjid(StringUtil.generateUUID());
            zjglDto.setXh(xh);
            zjglDto.setZjlx("external");
            zjglDtoList.add(zjglDto);
            result = dao.insert(zjglDto)>0;
        }
        resultMap.put("status",result?"success":"fail");
        resultMap.put("message",result?"保存成功!":"保存失败!");
        resultMap.put("zjglDtoList",result?zjglDtoList: "");
        return resultMap;
    }

    /**
     * @Description: 上传工具附件
     * @param zjglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2025/4/29 11:22
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertToolFile(ZjglDto zjglDto,List<ZjglDto> zjglDtoList) {
        boolean result = false;
        if(!CollectionUtils.isEmpty(zjglDto.getIds())) {
            int xh = Integer.parseInt(dao.generateZjid(zjglDto));
            for (int i = 0; i < zjglDto.getIds().size(); i++) {
                Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+zjglDto.getIds().get(i));
                String wjm = (String)mFile.get("wjm");
                int pos = wjm.lastIndexOf(".");
                String zjmc = pos > 0 ? wjm.substring(0, pos) : wjm;
                String uuid = StringUtil.generateUUID();
                ZjglDto zjglD = new ZjglDto();
                zjglD.setZjid(uuid);
                zjglD.setZjmc(zjmc);
                String pathString = (String)mFile.get("fwjlj");
                String realPath = prefix + releaseFilePath + pathString.substring(prefix.length() + tempFilePath.length());
                String fwjm = (String)mFile.get("fwjm");
                zjglD.setZylj(realPath+"/"+fwjm);
                zjglD.setXh(String.valueOf(xh));
                xh = xh + 1;
                zjglD.setZjlx("tool");
                zjglDtoList.add(zjglD);
                fjcfbService.save2RealFile(zjglDto.getIds().get(i), uuid);
            }
            if(!CollectionUtils.isEmpty(zjglDtoList)){
                result = dao.insertList(zjglDtoList);
            }
        }
        return result;
    }

    /**
     * @Description: 删除工具
     * @param zjglDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2025/4/29 16:16
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> delTool(ZjglDto zjglDto) {
        Map<String,Object> resultMap = new HashMap<>();
        zjglDto.setScbj("1");
        boolean result = dao.delete(zjglDto)>0;
        resultMap.put("status",result?"success":"fail");
        resultMap.put("message",result?"删除成功!":"删除失败!");
        return resultMap;
    }
}
